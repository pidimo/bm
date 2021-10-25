package com.piramide.elwis.web.common.dynamicsearch;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.dynamicsearch.el.DynamicSearchUtil;
import com.piramide.elwis.web.common.dynamicsearch.fantabulous.FantabulousConditionCompleter;
import com.piramide.elwis.web.common.dynamicsearch.form.DynamicSearchForm;
import com.piramide.elwis.web.common.dynamicsearch.persistence.DynamicSearchPersistenceManager;
import com.piramide.elwis.web.common.dynamicsearch.structure.FieldOperator;
import com.piramide.elwis.web.common.dynamicsearch.util.OperatorUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.persistence.PersistenceManager;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchExecute {
    protected final Log log = LogFactory.getLog(this.getClass());

    private ListStructure listStructure;
    private DynamicSearchForm dynamicSearchForm;
    private String module;

    public DynamicSearchExecute(ListStructure listStructure, DynamicSearchForm dynamicSearchForm, String module) {
        this.listStructure = listStructure;
        this.dynamicSearchForm = dynamicSearchForm;
        this.module = module;
    }

    public ListStructure execute(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        boolean isLoadFromPersistence = isLoadFromPersistence();
        String dynamicSearchName;
        List searchFieldAliasList = new ArrayList();
        Map parameterMap;

        if (isLoadFromPersistence) {
            parameterMap = PersistenceManager.persistence().loadStatus(userId.toString(), listStructure.getListName(), module);
            dynamicSearchName = findDynamicSearchNameInParameters(parameterMap);
            if (dynamicSearchName != null) {
                searchFieldAliasList = DynamicSearchPersistenceManager.load(dynamicSearchName, module, request);
            }
        } else {
            dynamicSearchName = dynamicSearchForm.getDynamicSearchName();
            searchFieldAliasList = dynamicSearchForm.getSearchPropertiesAsList();
            parameterMap = dynamicSearchForm.getParams();
        }

        log.debug("Execute with: dynamicSearchName=" + dynamicSearchName + " fieldAlias=" + searchFieldAliasList);

        if (dynamicSearchName != null) {
            List<DynamicSearchProperty> dynamicSearchProperties = getDynamicSearchProperties(searchFieldAliasList, parameterMap, dynamicSearchName, request);

            FantabulousConditionCompleter conditionCompleter = new FantabulousConditionCompleter(dynamicSearchName, request);
            listStructure = conditionCompleter.completeConditions(listStructure, dynamicSearchProperties);

            if (isLoadFromPersistence) {
                request.setAttribute("searchFieldsUIList", DynamicSearchUtil.getDynamicSearchFieldsAsUI(searchFieldAliasList, dynamicSearchName, parameterMap, request));
            } else {
                DynamicSearchPersistenceManager.save(searchFieldAliasList, dynamicSearchName, module, request);
            }
        }

        return listStructure;
    }

    private boolean isLoadFromPersistence() {
        return (dynamicSearchForm.getDynamicSearchName() == null
                || (dynamicSearchForm.getSearchPropertiesAsList().isEmpty() && !dynamicSearchForm.getPageParams().isEmpty()));
    }

    private String findDynamicSearchNameInParameters(Map parameterMap) {
        String name = null;
        if (parameterMap.containsKey(DynamicSearchForm.dynamicSearchNameParameter)) {
            name = (String) parameterMap.get(DynamicSearchForm.dynamicSearchNameParameter);
        }
        return name;
    }

    private List<DynamicSearchProperty> getDynamicSearchProperties(List searchFieldAliasList, Map parameterMap, String dynamicSearchName, HttpServletRequest request) {
        List<DynamicSearchProperty> dynamicSearchProperties = new ArrayList<DynamicSearchProperty>();

        for (int i = 0; i < searchFieldAliasList.size(); i++) {
            String fieldAlias = (String) searchFieldAliasList.get(i);

            if (isValidValue(fieldAlias)) {
                DynamicSearchConstants.Operator operator = findOperator(fieldAlias, parameterMap);
                if (operator != null) {

                    FieldOperator fieldOperator = DynamicSearchUtil.findFieldOperator(dynamicSearchName, fieldAlias, operator, request);
                    if (fieldOperator != null) {

                        if (fieldOperator.isParameter()) {
                            processAsParameter(fieldOperator, fieldAlias, operator, parameterMap);
                        } else {

                            DynamicSearchProperty dynamicSearchProperty = null;
                            if (OperatorUtil.isOperatorWithoutValue(operator.getConstant())) {
                                dynamicSearchProperty = new DynamicSearchProperty(fieldAlias, operator);
                            } else {
                                List values = getValues(fieldAlias, operator, parameterMap);
                                if (!values.isEmpty()) {
                                    dynamicSearchProperty = new DynamicSearchProperty(fieldAlias, operator, values);
                                }
                            }

                            if (dynamicSearchProperty != null) {
                                dynamicSearchProperties.add(dynamicSearchProperty);
                            }
                        }
                    }
                }
            }
        }

        return dynamicSearchProperties;
    }

    private void processAsParameter(FieldOperator fieldOperator, String fieldAlias, DynamicSearchConstants.Operator operator, Map parameterMap) {
        if (fieldOperator.getParameterName() != null) {
            //add parameter with new name in form
            List values = getValues(fieldAlias, operator, parameterMap);
            if (!values.isEmpty()) {
                dynamicSearchForm.addPropertyFilter(fieldOperator.getParameterName(), values.get(0).toString());
            }
        }
    }

    private DynamicSearchConstants.Operator findOperator(String fieldAlias, Map parameterMap) {
        String operatorParameterName = DynamicSearchForm.composeOperatorParameterName(fieldAlias);
        String operatorConstant = (String) parameterMap.get(operatorParameterName);
        return DynamicSearchConstants.Operator.findOperator(operatorConstant);
    }

    private List getValues(String fieldAlias, DynamicSearchConstants.Operator operator, Map parameterMap) {
        List values = new ArrayList();

        if (DynamicSearchUtil.isDynamicSearchTwoBoxView(operator.getConstant())) {
            String value1 = (String) parameterMap.get(DynamicSearchForm.composeValue1ParameterName(fieldAlias));
            String value2 = (String) parameterMap.get(DynamicSearchForm.composeValue2ParameterName(fieldAlias));

            if (isValidValue(value1)) {
                values.add(value1);
            }

            if (isValidValue(value2)) {
                values.add(value2);
            }
        } else {
            String value = (String) parameterMap.get(fieldAlias);
            if (isValidValue(value)) {
                values.add(value);
            }
        }

        return values;
    }

    private boolean isValidValue(String value) {
        return !GenericValidator.isBlankOrNull(value);
    }

}
