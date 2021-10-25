package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.*;
import com.jatun.titus.listgenerator.structure.externalbuilder.Column;
import com.jatun.titus.listgenerator.structure.externalbuilder.CompoundColumn;
import com.jatun.titus.listgenerator.structure.filter.DBFilter;
import com.jatun.titus.listgenerator.structure.filter.Filter;
import com.jatun.titus.listgenerator.structure.filter.TableDBFilter;
import com.jatun.titus.listgenerator.structure.type.DBType;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractExternalListAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.reports.el.Functions;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Jatun S.R.L.
 * execute and set the result in the request of an external list structure
 *
 * @author Miky
 * @version $Id: FilterExternalListAction.java 10334 2013-03-17 16:35:02Z miguel $
 */

public class FilterExternalListAction extends AbstractExternalListAction {
    private ListStructure fantaListStructure;

    @Override
    protected boolean hasAssignedCompanyIdFilter(HttpServletRequest request) {
        Integer reportCompanyId = getReportCompanyId(request);
        if (reportCompanyId != null) {
            addStaticFilter(Constants.COMPANYID, reportCompanyId.toString());
            return true;
        }
        return false;
    }

    private Integer getReportCompanyId(HttpServletRequest request) {
        Integer reportCompanyId = null;
        log.debug("External filter reportId:" + request.getParameter("reportId"));

        String reportId = request.getParameter("reportId");
        if (reportId != null) {
            reportCompanyId = Functions.getReportCompanyId(reportId);
        }
        return reportCompanyId;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing FilterExternalListAction............." + request.getParameterMap());

        ActionForward actionForward;
        boolean isViewInSelect = (request.getAttribute("viewInSelect") != null &&
                ((Boolean) request.getAttribute("viewInSelect")).booleanValue());

        String filterFieldPath = request.getParameter("filterPath");
        if (filterFieldPath != null) {
            request.getSession().setAttribute("filterPath", filterFieldPath);
        } else if (isViewInSelect) {
            log.debug("IS DBFILTER TO SELECT----------");
            filterFieldPath = (String) request.getAttribute("sFilterPath");
        } else {
            filterFieldPath = (String) request.getSession().getAttribute("filterPath");
        }

        log.debug("FILTER PATH:" + filterFieldPath);

        //get list structure
        StructureManager manager = Titus.getStructureManager(request.getSession().getServletContext());
        Field filterFieldRef = manager.getFieldByTitusPath(filterFieldPath);

        //relation filter table
        String relationFilterTableName = TitusPathUtil.getTableNameOwner(filterFieldPath);

        //get table name of superior foreing relation
        String superiorRelationTableName = TitusPathUtil.getTableNameOfSuperiorForeingRelation(filterFieldPath);

        //db filter
        Filter filter = filterFieldRef.getFilter();
        if (Functions.isDbFilter(filter)) {
            DBFilter dbFilter = (DBFilter) filter;

            PrimaryKey primaryKey = null;
            if (dbFilter instanceof TableDBFilter) {

                TableDBFilter tableDBFilter = (TableDBFilter) dbFilter;
                if (tableDBFilter.hasFilterInAnotherTable(relationFilterTableName)) {
                    Table relationFilterTable = manager.getTable(relationFilterTableName);
                    if (relationFilterTable.getRelationDBFilter() != null) {
                        dbFilter = relationFilterTable.getRelationDBFilter();
                        primaryKey = relationFilterTable.getPrimaryKey();
                    } else {
                        log.debug("this table not have relation filter defined....:" + relationFilterTable.getName());
                    }
                } else {
                    primaryKey = filterFieldRef.getParentTable().getPrimaryKey();
                }
            } else {
                primaryKey = filterFieldRef.getParentTable().getPrimaryKey();
            }

            fantaListStructure = (ListStructure) dbFilter.getStructure();
            //verif has list structure in foreign key
            if (superiorRelationTableName != null && !(dbFilter instanceof TableDBFilter)) {
                Table superiorRelationTable = manager.getTable(superiorRelationTableName);
                Table ownerDbFilterTable = filterFieldRef.getParentTable();
                for (Iterator iterator = superiorRelationTable.getForeignKeyIterator(); iterator.hasNext();) {
                    ForeignKey foreignKey = (ForeignKey) iterator.next();
                    if (ownerDbFilterTable.getName().equals(foreignKey.getRelatedTable().getName()) &&
                            foreignKey.getStructure() != null) {
                        fantaListStructure = (ListStructure) foreignKey.getStructure();
                    }
                }
            }

            //set execute in first time
            fantaListStructure.setExecuteFirstTime(isViewInSelect);

            //get alias mapping
            Map aliasMapping = dbFilter.getAliasRegistered();
            log.debug("ALIAS-MAPPING::" + aliasMapping);


            //get column names
            boolean hasSearchParam = false;
            List resultList = new ArrayList();
            List columnList = dbFilter.getColumnList();
            List primaryKeyColumnList = new ArrayList();
            for (Iterator iterator = columnList.iterator(); iterator.hasNext();) {
                Column titusColumn = (Column) iterator.next();

                if (titusColumn.isCompoundColumn()) {
                    CompoundColumn compoundColumn = (CompoundColumn) titusColumn;

                    CompoundField compounField = compoundColumn.getCompoundField();
                    String columnResource = !compoundColumn.hasResource() ? compounField.getResource() : compoundColumn.getResource();
                    boolean isNumericType = compounField.getType().getBaseType() == DBType.DBBaseType.BASETYPE_NUMERIC;

                    Map compoundMap = new HashMap();
                    compoundMap.put("compoundColumn", compoundColumn);
                    compoundMap.put("aliasMap", aliasMapping);
                    compoundMap.put("resource", columnResource);
                    compoundMap.put("columnOrder", compoundColumn.getAliasName()); //set column order name, only compound column
                    compoundMap.put("isNumericType", Boolean.valueOf(isNumericType));
                    resultList.add(compoundMap);

                    //set in request the compound column of filter field ref
                    if (filterFieldRef.getName().equals(compounField.getName()) && !hasSearchParam) {
                        hasSearchParam = true;
                        request.setAttribute("isCompoundColumn", compoundColumn);
                        request.setAttribute("columnAliasMap", aliasMapping);

                        //search params
                        String searchParam = "";
                        Map pathsOfCompoundField = TitusPathUtil.getTitusPathFieldsFromCompoundField(compounField, compoundColumn.getFieldPath());
                        for (Iterator iterator2 = pathsOfCompoundField.keySet().iterator(); iterator2.hasNext();) {
                            String path = (String) iterator2.next();
                            Field field = (Field) pathsOfCompoundField.get(path);
                            String aliasColumn = aliasMapping.get(path).toString();

                            if (compounField.isFilterable(field.getName())) {
                                if (searchParam.length() > 0) {
                                    searchParam = searchParam + "@_" + aliasColumn;
                                } else {
                                    searchParam = aliasColumn;
                                }
                            }

                            if (compounField.isAlphabetFilter(field.getName())) {
                                request.setAttribute("alphabetParam", aliasColumn);
                            }
                        }
                        request.setAttribute("searchParam", searchParam);
                    }

                } else {
                    //is simple column
                    String titusPath = titusColumn.getFieldPath();
                    String columnName = aliasMapping.get(titusPath).toString();
                    Field columnField = manager.getFieldByTitusPath(titusPath);
                    String columnResource = !titusColumn.hasResource() ? columnField.getResource() : titusColumn.getResource();

                    if (titusColumn.isVisible()) {
                        boolean isNumericType = columnField.getType().getBaseType() == DBType.DBBaseType.BASETYPE_NUMERIC;
                        Map map = new HashMap();
                        map.put("column", columnName);
                        map.put("resource", columnResource);
                        map.put("fieldPath", titusPath);
                        map.put("isNumericType", Boolean.valueOf(isNumericType));
                        resultList.add(map);
                    }

                    //get primary key columns
                    for (Iterator iterator2 = primaryKey.getKeyFields(); iterator2.hasNext();) {
                        Field primaryKeyField = (Field) iterator2.next();
                        if (primaryKeyField.getParentTable().getName().equals(columnField.getParentTable().getName()) &&
                                primaryKeyField.getName().equals(columnField.getName())) {
                            primaryKeyColumnList.add(columnName);
                            break;
                        }
                    }

                    //set in request the column alias of filter field ref
                    if (filterFieldRef.getName().equals(columnField.getName()) && !hasSearchParam) {
                        hasSearchParam = true;
                        request.setAttribute("searchParam", columnName);
                        request.setAttribute("searchParamPath", titusPath);
                        request.setAttribute("alphabetParam", columnName);
                    }
                }
            }

            request.setAttribute("columnList", resultList);
            request.setAttribute("primaryKeyList", primaryKeyColumnList);
            log.debug("PKLIST::::" + primaryKeyColumnList);
            log.debug("S:" + request.getAttribute("searchParam"));
            log.debug("A:" + request.getAttribute("alphabetParam"));
        }

        if (isViewInSelect) {
            Map listSearchParametersMap = new HashMap();
            if (TitusPathUtil.isDynamicColumn(filterFieldPath)) {
                listSearchParametersMap = getDynamicColumnSearchParameter(filterFieldPath, manager);
            }
            //process if the result should show in an select
            executeListStructureToViewInSelect(request, listSearchParametersMap);
            actionForward = null;
        } else {
            actionForward = super.execute(mapping, form, request, response);
        }

        return actionForward;
    }

    /**
     * execute list structure
     *
     * @param request
     */
    private void executeListStructureToViewInSelect(HttpServletRequest request, Map searchParamMap) {

        Integer companyId = getReportCompanyId(request);
        if (companyId == null) {
            User user = RequestUtils.getUser(request);
            companyId = (Integer) user.getValue("companyId");
        }

        Parameters params = new Parameters();
        params.addSearchParameter("companyId", companyId.toString());

        //add custom search params in list
        if (searchParamMap != null) {
            for (Iterator iterator = searchParamMap.keySet().iterator(); iterator.hasNext();) {
                String parameterKey = (String) iterator.next();
                params.addSearchParameter(parameterKey, searchParamMap.get(parameterKey).toString());
            }
        }

        SqlGenerator generator = SqlGeneratorManager.newInstance();
        String sqlQuery = generator.generate(fantaListStructure, params);
        log.debug("SQL:::::" + sqlQuery);

        //execute list
        Collection resultList = Controller.getList(fantaListStructure, params);
        log.debug("RESULT--------------" + resultList);

        List list = new ArrayList();
        for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
            Map map = (Map) iterator.next();

            //compose label
            String label;
            if (request.getAttribute("isCompoundColumn") != null) {
                label = Functions.compoundColumnConverter(map, request.getAttribute("isCompoundColumn"), (Map) request.getAttribute("columnAliasMap"), request);
            } else {
                String fieldRefValue = Functions.getMapValueByKey(map, request.getAttribute("searchParam"));
                String fieldRefPath = (String) request.getAttribute("searchParamPath");
                label = Functions.applyConverterDbToViewInSimpleField(fieldRefPath, fieldRefValue, request);
            }
            //compose value
            String value = Functions.getPrimaryKeyValue(map, (List) request.getAttribute("primaryKeyList"));
            list.add(new LabelValueBean(label, value));
        }
        log.debug("SELECT LIST::::::" + list);
        request.setAttribute("selectData", list);
    }

    public ListStructure getExternalListStructure() {
        return fantaListStructure;
    }

    private Map getDynamicColumnSearchParameter(String titusPath, StructureManager manager) {
        Map parametersMap = new HashMap();
        if (TitusPathUtil.isDynamicColumn(titusPath)) {
            DynamicColumn dynamicColumn = manager.getDynamicColumnByTitusPath(titusPath);
            if (dynamicColumn != null) {
                DynamicForeignKey dynamicForeignKey = dynamicColumn.getDynamicForeignKey();
                if (dynamicForeignKey.getColumnKeyParam() != null) {
                    parametersMap.put(dynamicForeignKey.getColumnKeyParam(), TitusPathUtil.getDynamicColumnId(titusPath));
                }
            }
        }
        return parametersMap;
    }
}
