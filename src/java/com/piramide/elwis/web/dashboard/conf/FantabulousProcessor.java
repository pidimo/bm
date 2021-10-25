package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.dashboard.component.configuration.structure.*;
import com.piramide.elwis.web.dashboard.component.execute.DataProcessor;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import com.piramide.elwis.web.dashboard.component.web.struts.action.DrawComponentAction;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.queryexecutor.Query;
import org.alfacentauro.fantabulous.queryexecutor.SQLExecutorException;
import org.alfacentauro.fantabulous.result.ExecutorException;
import org.alfacentauro.fantabulous.result.FieldResultList;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.struts.action.ActionServlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:16:28 PM
 */
public class FantabulousProcessor extends DataProcessor {
    private FieldResultList elementResultList;
    private int numberOfAllElements = 0;
    private Integer numberOfElements = 0;

    public void readComponentContain(Component cloneComponent,
                                     Map searchParameters,
                                     DrawComponentAction action,
                                     HttpServletRequest request) {

        ActionServlet actionServlet = action.getActionServlet();

        List fantaParameters = buildFantabulousParams(cloneComponent);
        fantaParameters.addAll(buildFantabulousParams(searchParameters));

        List orderParams = buildFantabulousOrderParams(cloneComponent.getVisibleColumns());
        orderParams.addAll(buildFantabulousOrderParams(cloneComponent.getDefaultSortableColumns()));


        numberOfElements = new Integer(cloneComponent.getRowCounterFilter().getInitialValue());

        String module = cloneComponent.getParameter("module").getValue();
        String listName = cloneComponent.getParameter("FantaListName").getValue();

        executeFantabulousList(
                listName,
                module,
                fantaParameters,
                orderParams,
                actionServlet.getServletContext(),
                request);
    }


    public void readFilterContain(int componentId, ConfigurableFilter filter, Map elParameters, Map parameters) {
        List<Parameter> filterParameters = new ArrayList<Parameter>(filter.getParameters());

        Parameter listName = getParameter(filterParameters, "FantaListName");
        Parameter module = getParameter(filterParameters, "module");

        filterParameters.remove(listName);
        filterParameters.remove(module);

        HttpServletRequest request = (HttpServletRequest) parameters.get("request");
        ServletContext servletContext = (ServletContext) parameters.get("servletContext");

        List searchParams = filterParametersToFantabulousParams(filterParameters, elParameters);
        List orderParams = new ArrayList();

        executeFantabulousList(listName.getValue(),
                module.getValue(),
                searchParams,
                orderParams,
                servletContext,
                request);
    }


    private List filterParametersToFantabulousParams(List<Parameter> parameters, Map elParams) {
        List searchParameterList = new ArrayList();

        for (Parameter p : parameters) {

            String field = p.getName();
            String value;
            Map m = new HashMap();

            if (p.getValue().indexOf("$") == 0) {
                value = elParams.get(p.getName()).toString();
            } else {
                value = p.getValue();
            }
            searchParameterList.add(new SearchParameter(field, value));

        }
        return searchParameterList;
    }

    private void executeFantabulousList(String listName,
                                        String module,
                                        List searchParams,
                                        List orderParams,
                                        ServletContext servletContext,
                                        HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = new Integer(user.getValue(Constants.USERID).toString());

        FantabulousManager fantaManager;

        if (null != module && !"".equals(module)) {
            fantaManager = FantabulousManager.loadFantabulousManager(servletContext, module);
        } else {
            fantaManager = FantabulousManager.loadFantabulousManager(servletContext, request);
        }

        try {
            ListStructure list = fantaManager.getList(listName);
            list = addAccessRightSecurity(list, userId);

            Parameters parameters = new Parameters();

            parameters.setSearchParameters(searchParams);
            parameters.setOrderParameters(orderParams);

            int resultsCount = Query.i.executeResultCountQuery(list, parameters);

            if (resultsCount > 0) {
                parameters.getPageParam().setPageNumber(1);
                if (0 == numberOfElements) {
                    parameters.getPageParam().setPageSize(resultsCount);
                } else {
                    parameters.getPageParam().setPageSize(numberOfElements);
                }
                elementResultList = Query.i.executeListQuery(list, resultsCount, parameters);
            } else {
                elementResultList = new FieldResultList();
            }
            numberOfAllElements = resultsCount;
        } catch (ListStructureNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutorException e) {
            e.printStackTrace();
        } catch (SQLExecutorException e) {
            e.printStackTrace();
        }
    }

    private Parameter getParameter(List<Parameter> params, String paramName) {
        for (Parameter prm : params) {
            if (paramName.equals(prm.getName())) {
                return prm;
            }
        }
        return null;
    }

    private List buildFantabulousParams(Component cloneComponent) {
        List searchParameters = new ArrayList();

        List<ConfigurableFilter> filters = cloneComponent.getConfigurableFilters();
        List<StaticFilter> staticFilters = cloneComponent.getStaticFilters();

        if (null != filters) {
            for (ConfigurableFilter configurableFilter : filters) {
                String field = configurableFilter.getName();
                if (configurableFilter.isRangeView()) {
                    String initialValue = configurableFilter.getInitialValue();
                    String finalValue = configurableFilter.getFinalValue();
                    SearchParameter initialParam = new SearchParameter(field, initialValue);
                    SearchParameter finalParam = new SearchParameter(field, finalValue);
                    searchParameters.add(initialParam);
                    searchParameters.add(finalParam);
                } else {
                    //add all filters with initial value is not null
                    String value = (
                            "".equals(configurableFilter.getInitialValue()) ?
                                    null :
                                    configurableFilter.getInitialValue()
                    );
                    SearchParameter param = new SearchParameter(field, value);
                    searchParameters.add(param);
                }
            }
        }

        if (staticFilters != null) {
            for (StaticFilter staticFilter : staticFilters) {
                String field = staticFilter.getName();
                String value = staticFilter.getValue();

                if (value != null && !"".equals(value)) {
                    SearchParameter param = new SearchParameter(field, value);
                    searchParameters.add(param);
                }
            }
        }

        return searchParameters;
    }

    private List buildFantabulousOrderParams(List<Column> columns) {
        List orderParams = new ArrayList();

        if (null != columns) {
            for (Column column : columns) {
                if (column.isOrderable() && !Constant.ORDER_NONE.equals(column.getOrder())) {
                    boolean orderValue = Constant.ORDER_ASC.equals(column.getOrder());
                    if (column.isInverseOrder()) {
                        orderValue = !orderValue;
                    }

                    orderParams.add(new OrderParam(column.getName(), orderValue));
                }
            }
        }
        return orderParams;
    }


    private List buildFantabulousParams(Map searchParameters) {
        List searchParams = new ArrayList();
        if (null != searchParameters) {
            for (Iterator it = searchParameters.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                SearchParameter searchParam = new SearchParameter(key, value.toString());
                searchParams.add(searchParam);
            }
        }

        return searchParams;
    }

    public List<Map> getData() {
        Collection fieldResult = elementResultList.getFieldResults();

        List<Map> result = new ArrayList<Map>();
        for (Iterator it = fieldResult.iterator(); it.hasNext();) {
            Map element = (Map) it.next();
            result.add(element);
        }
        return result;
    }

    public int getNumberOfAllElements() {
        return numberOfAllElements;
    }

    private ListStructure addAccessRightSecurity(ListStructure sourceListStructure, Integer userId) {
        return AccessRightDataLevelSecurity.i.processAllAccessRightByList(sourceListStructure, userId);
    }

}