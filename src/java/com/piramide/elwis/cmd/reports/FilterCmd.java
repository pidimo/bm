package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.reportmanager.Filter;
import com.piramide.elwis.domain.reportmanager.FilterHome;
import com.piramide.elwis.domain.reportmanager.FilterValue;
import com.piramide.elwis.domain.reportmanager.FilterValueHome;
import com.piramide.elwis.dto.reports.FilterDTO;
import com.piramide.elwis.dto.reports.FilterValueDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: FilterCmd.java 10342 2013-03-29 00:12:40Z miguel $
 */

public class FilterCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FilterCmd................" + paramDTO);

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        FilterDTO filterDTO = new FilterDTO(paramDTO);

        if (paramDTO.get("path") != null) {
            filterDTO.remove("path");
            filterDTO.put("tempPath", paramDTO.get("path")); //used to set from bean
        }

        if ("create".equals(this.getOp())) {

            Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
            FilterHome filterHome = (FilterHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTER);
            Integer sequence = null;
            try {
                Integer maxSeqquence = filterHome.selectMaxSequence(reportId, companyId);
                if (maxSeqquence != null) {
                    sequence = new Integer(maxSeqquence.intValue() + 1);
                } else {
                    sequence = new Integer(1);
                }
            } catch (FinderException e) {
                log.debug("Not sequence defined.............");
                sequence = new Integer(1);
            }
            String aliasCondition = ReportConstants.ALIAS_FILTER + sequence;
            filterDTO.put("sequence", sequence);
            filterDTO.put("aliasCondition", aliasCondition);

        } else if ("delete".equals(this.getOp())) {
            //delete all filter values
            Filter filter = (Filter) ExtendedCRUDDirector.i.read(filterDTO, resultDTO, false);
            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
                return;
            } else {
                Collection values = filter.getFilterValues();
                //remove values
                if (!values.isEmpty()) {
                    Object[] objArray = values.toArray();
                    for (int i = 0; i < objArray.length; i++) {
                        FilterValue filterValue = (FilterValue) objArray[i];
                        //EJBFactory.i.removeEJB(filterValue);
                        ExtendedCRUDDirector.i.delete(new FilterValueDTO(filterValue.getFilterValueId()), resultDTO, false, null);
                    }
                }
            }
        }

        //verifiy if is parameter filter
        if (paramDTO.get("isParameter") != null) {
            filterDTO.put("isParameter", Boolean.TRUE);
        } else {
            filterDTO.put("isParameter", Boolean.FALSE);
        }

        //execute cmd
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        Filter filter = (Filter) super.execute(ctx, filterDTO);

        if (resultDTO.get("path") != null) {
            resultDTO.put("path", new String((byte[]) resultDTO.get("path")));
        }

        if (super.resultDTO.isFailure()) {
            readFilterValues(filter);
        } else {
            //update filter value
            if (filter != null) {
                if ("create".equals(this.getOp())) {
                    if (paramDTO.get("values") != null) {
                        List valuesList = (List) paramDTO.get("values");
                        createFilterValues(filter, valuesList, null);
                    }
                } else if ("update".equals(this.getOp())) {
                    //update the path if is necessary
                    if (paramDTO.get("path") != null) {
                        filter.setPath(paramDTO.get("path").toString().getBytes());
                    }

                    List valuesList = new ArrayList();
                    if (paramDTO.get("values") != null) {
                        valuesList = (List) paramDTO.get("values");
                    }
                    updateFilterValues(filter, valuesList);
                } else if ("read".equals(this.getOp())) {
                    readFilterValues(filter);
                }

                resultDTO.put("filterId", filter.getFilterId());
            }
        }
    }


    private void createFilterValues(Filter filter, List valuesList, Integer startIndex) {

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        //create filter values
        if (filter != null) {
            int index = 0;
            if (startIndex != null) {
                index = startIndex.intValue();
            }
            while (index < valuesList.size()) {
                Object value = valuesList.get(index);

                FilterValueDTO filterValueDTO = new FilterValueDTO();
                filterValueDTO.put("companyId", companyId);
                filterValueDTO.put("filterId", filter.getFilterId());
                filterValueDTO.put("sequence", new Integer(index));

                if (value instanceof Map) {
                    //is compound pk
                    Map valueMap = (Map) value;
                    filterValueDTO.put("filterValue", valueMap.get("value"));
                    if (valueMap.containsKey("pkSeq")) {
                        filterValueDTO.put("pkSequence", valueMap.get("pkSeq"));
                    }
                } else {
                    filterValueDTO.put("filterValue", value);
                }
                ExtendedCRUDDirector.i.create(filterValueDTO, resultDTO, false);
                index++;
            }
        }
    }

    private void updateFilterValues(Filter filter, List formValuesList) {

        Collection values = filter.getFilterValues();
        if (!values.isEmpty()) {
            List removeValueIdList = new ArrayList();
            List filterValues = new ArrayList(values);
            int index = 0;
            while (index < filterValues.size()) {
                FilterValue filterValue = (FilterValue) filterValues.get(index);

                if (index < formValuesList.size()) {
                    Object value = formValuesList.get(index);
                    filterValue.setSequence(new Integer(index));

                    if (value instanceof Map) {
                        //is compound pk
                        Map valueMap = (Map) value;
                        filterValue.getFilterValue();
                        filterValue.setFilterValue((String) valueMap.get("value"));
                        if (valueMap.containsKey("pkSeq")) {
                            filterValue.setPkSequence(Integer.valueOf(String.valueOf(valueMap.get("pkSeq"))));
                        } else {
                            filterValue.getPkSequence();
                            filterValue.setPkSequence(null);
                        }
                    } else {
                        filterValue.getFilterValue();
                        filterValue.setFilterValue(String.valueOf(value));
                        filterValue.getPkSequence();
                        filterValue.setPkSequence(null);
                    }
                } else {
                    removeValueIdList.add(filterValue.getFilterValueId());
                }
                index++;
            }

            if (index < formValuesList.size()) {
                //create rest filter values
                createFilterValues(filter, formValuesList, new Integer(index));
            } else if (!removeValueIdList.isEmpty()) {
                //delete filter values
                for (int i = 0; i < removeValueIdList.size(); i++) {
                    ExtendedCRUDDirector.i.delete(new FilterValueDTO((Integer) removeValueIdList.get(i)), resultDTO, false, null);
                }
            }
        } else {
            //create all values
            createFilterValues(filter, formValuesList, null);
        }
    }

    protected String readFilterValues(Filter filter) {
        String filterValue = null;

        if (filter != null) {
            FilterValueHome filterValueHome = (FilterValueHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_FILTERVALUE);
            Collection filterValues = null;
            try {
                filterValues = filterValueHome.findByFilterId(filter.getFilterId()); //find with orders
            } catch (FinderException e) {
                log.debug("not values to filter:" + filter.getAliasCondition());
                e.printStackTrace();
            }
            if (filterValues != null) {
                StringBuffer valueBuffer = new StringBuffer();
                LinkedList valuesList = new LinkedList(filterValues);
                for (ListIterator iterator = valuesList.listIterator(); iterator.hasNext();) {
                    FilterValue value = (FilterValue) iterator.next();

                    if (value.getFilterValue() != null) {

                        if (filter.getFilterType().equals(ReportConstants.FILTER_WITH_DB_VALUE)) {
                            if (value.getPkSequence() != null) {
                                valueBuffer.append(value.getFilterValue());
                                if (iterator.hasNext()) {
                                    FilterValue nextValue = (FilterValue) valuesList.get(iterator.nextIndex());
                                    if (value.getPkSequence().equals(nextValue.getPkSequence())) {
                                        valueBuffer.append(ReportConstants.PRIMARYKEY_SEPARATOR);
                                    } else {
                                        valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                                    }
                                }
                            } else {
                                valueBuffer.append(value.getFilterValue());
                                if (iterator.hasNext()) {
                                    valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                                }
                            }
                        } else {
                            valueBuffer.append(value.getFilterValue());
                            if (iterator.hasNext()) {
                                valueBuffer.append(ReportConstants.FILTERVALUE_SEPARATOR);
                            }
                        }
                    }
                }
                //log.debug("FilterValueCompose:::::" + valueBuffer.toString());
                filterValue = valueBuffer.toString();
                resultDTO.put("filterValue", valueBuffer.toString());
            }
        }
        return filterValue;
    }
}
