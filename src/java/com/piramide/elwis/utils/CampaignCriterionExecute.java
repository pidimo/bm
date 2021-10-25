package com.piramide.elwis.utils;

import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.controller.Controller;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.SearchParameter;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGenerator;
import org.alfacentauro.fantabulous.sqlgenerator.SqlGeneratorManager;
import org.alfacentauro.fantabulous.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class responsible of to build criteria dynamic in campaign.
 *
 * @author yumi
 */

public class CampaignCriterionExecute {
    final Log log = LogFactory.getLog(CampaignCriterionExecute.class);

    public List executeCriterias(ListStructure structure,
                                 ArrayList listCriterion, // criterion list
                                 ArrayList parameter, boolean isDistinct,
                                 ListStructure productStructure,
                                 ListStructure customerStructure,
                                 ListStructure addressStructure,
                                 ListStructure contactPersonStructure,
                                 ListStructure salePositionStructure) throws FinderException {
        return executeCriterias(structure, listCriterion, parameter, isDistinct, true,
                productStructure,
                customerStructure,
                addressStructure,
                contactPersonStructure,
                salePositionStructure);
    }

    public List executeCriterias(ListStructure structure,
                                 ArrayList listCriterion, // criterion list
                                 ArrayList parameter, boolean isDistinct, boolean number,
                                 ListStructure productStructure,
                                 ListStructure customerStructure,
                                 ListStructure addressStructure,
                                 ListStructure contactPersonStructure,
                                 ListStructure salePositionStructure) throws FinderException {

        List recipients = new ArrayList();
        Parameters parameters = new Parameters();
        parameters.setSearchParameters(parameter);

        /* create group conditions */
        GroupCondition groupCond = new GroupCondition();
        Condition condition = iterateConditions(structure, listCriterion, 0,
                productStructure,
                customerStructure,
                addressStructure,
                contactPersonStructure,
                salePositionStructure,
                parameter);
        groupCond.setGroupCondition(condition);
        ListStructure listStructure = new ListStructure();
        groupCond.addAndCondition(structure.getCondition());
        listStructure.setCondition(groupCond);
        listStructure.setTables(structure.getTables());
        listStructure.setRelations(structure.getRelations());
        listStructure.setDistinct(true);
        listStructure.setConfiguration(structure.getConfiguration());
        listStructure.setOrder(structure.getOrder());
        listStructure.setMainTable(structure.getMainTable());

        SqlGenerator generator = SqlGeneratorManager.newInstance();
        String sqlQuery = generator.generate(listStructure, parameters);
        recipients = (List) Controller.getList(listStructure, parameters);
        log.debug(" ... CampaignCriterionExecute        SQL -->  " + sqlQuery);
        return recipients;
    }

    private Condition createCondition(Integer fieldType, ListStructure structure,
                                      String condName,
                                      String operator,
                                      String value,
                                      StringTokenizer values,
                                      ArrayList parameters) {
        Condition con;
        DBField dbField = structure.getField(condName);

        if ("GREATER_EQUAL".equals(operator)) {
            operator = "GREATER";
        }
        if ("LESS_EQUAL".equals(operator)) {
            operator = "LESS";
        }
        if ("BETWEEN1".equals(operator) || "BETWEEN0".equals(operator)) {
            operator = "BETWEEN";
        }

        if ("BETWEEN".equals(operator)) {
            GroupCondition condition = new GroupCondition();
            SimpleCondition simple = new SimpleCondition();
            SimpleCondition cond1 = new SimpleCondition();
            simple.setField1(dbField);
            simple.setOperator("GREATER_EQUAL");
            cond1.setField1(dbField);
            cond1.setOperator("LESS_EQUAL");
            String value1 = values.nextToken();
            simple.setValue(value);
            cond1.setValue(value1);
            cond1.addAndCondition(simple);
            condition.setGroupCondition(cond1);
            con = condition;

        } else {
            SimpleCondition condition = new SimpleCondition();
            condition.setField1(dbField);

            if (CampaignConstants.COMPARATOR_IN.equals(operator)) {
                if (new Integer(4).equals(fieldType)) {
                    condition.setOperator("LIKE");
                } else {
                    condition.setOperator("EQUAL");
                }
            } else if (CampaignConstants.COMPARATOR_NOTIN.equals(operator)) {
                condition.setOperator("DISTINCT");
            } else if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(operator)) {
                condition.setOperator("EQUAL");
            } else {
                condition.setOperator(operator);
            }

            if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(operator)) {
                condition.setField2(structure.getField(value));
            } else {
                condition.setValue(value);
            }

            String nextVal;
            if (values.hasMoreTokens()) {
                nextVal = values.nextToken();
                if (CampaignConstants.COMPARATOR_IN.equals(operator)) {
                    condition.addOrCondition(createCondition(fieldType, structure, condName, operator, nextVal, values, parameters));
                } else {
                    condition.addAndCondition(createCondition(fieldType, structure, condName, operator, nextVal, values, parameters));
                }
            }
            con = condition;
        }
        return con;
    }

    private GroupCondition createGroupCondition(Integer fieldType, ListStructure structure, String key, String op, String value, ArrayList parameter) {
        GroupCondition groupCondition = new GroupCondition();
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        groupCondition.setGroupCondition(createCondition(fieldType, structure, key, op, tokenizer.nextToken(), tokenizer, parameter));
        return groupCondition;
    }

    /* Iterate conditions
    */
    public Condition iterateConditions(ListStructure structure, List criterions, int pos,
                                       ListStructure productStructure,
                                       ListStructure customerStructure,
                                       ListStructure addressStructure,
                                       ListStructure contactPersonStructure,
                                       ListStructure salePositionStructure,
                                       ArrayList parameters) throws FinderException {
        CampaignCriterion criterion = (CampaignCriterion) criterions.get(pos);
        Condition condition = null;
        Integer fieldType = null;
        CampaignFreeText freeText = null;
        String fieldName;
        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        try {
            freeText = freeTextHome.findByPrimaryKey(criterion.getValueId());
        } catch (FinderException e) {
            log.debug(" ... criterionValues not found ...");
        } /* *** FOR CATEGORIES *** */

        if (criterion.getCategoryId() != null && criterion.getCampCriterionValueId() == null) {

            CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CATEGORY);
            Category category = categoryHome.findByPrimaryKey(criterion.getCategoryId());

            String field = null;
            ListStructure categoryStructureList = null;

            if ("1".equals(category.getTable())) {
                field = "customerid";//field="category_customerid";
                categoryStructureList = customerStructure;
            } else if ("3".equals(category.getTable())) {
                field = "productid";//field="category_productid";
                categoryStructureList = productStructure;
            } else if ("4".equals(category.getTable())) {
                field = "addressid";//field="category_addressid";
                categoryStructureList = addressStructure;
            } else if ("2".equals(category.getTable())) {
                field = "contactPersonId";//field="category_contactPersonid";
                categoryStructureList = contactPersonStructure;
            } else if (ContactConstants.SALE_POSITION_CATEGORY.equals(category.getTable())) {
                field = "salepositionid";
                categoryStructureList = salePositionStructure;
            }

            if ("1".equals(category.getTable()) || "3".equals(category.getTable())) {
                parameters.add(new SearchParameter("isCustomer", CampaignConstants.TRUEVALUE));
            } else if ("2".equals(category.getTable())) {
                parameters.add(new SearchParameter("isContactPerson", CampaignConstants.TRUEVALUE));
            }


            if (ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(category.getTable())) {

                Condition composedCondition = composeCategoryCondition(ContactConstants.ADDRESS_CATEGORY, structure, addressStructure, criterion, "addressid");
                composedCondition.addOrCondition(composeCategoryCondition(ContactConstants.CONTACTPERSON_CATEGORY, structure, contactPersonStructure, criterion, "contactPersonId"));

                GroupCondition groupCond = new GroupCondition();
                groupCond.setGroupCondition(composedCondition);
                condition = groupCond;
            } else {
                condition = composeCategoryCondition(category.getTable(), structure, categoryStructureList, criterion, field);
            }
        } else {
            //for dinamic tables
            CampaignCriterionValueHome criterionValueHome = (CampaignCriterionValueHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCRITERIONVALUE);
            try {
                CampaignCriterionValue criterionValue = criterionValueHome.findByPrimaryKey(criterion.getCampCriterionValueId());
                String conditionValue = new String(freeText.getValue());

                if (ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(criterionValue.getTableId().toString())) {
                    condition = composeAddressContactPersonCondition(criterionValue, criterion, conditionValue, structure, parameters);
                } else {
                    condition = composeCondition(criterionValue, criterion, conditionValue, structure, parameters);
                }
            } catch (FinderException e) {
                log.debug(" ... criterion value not found ...");
            }
        }

        if (pos + 1 < criterions.size()) {
            Condition condIt = iterateConditions(structure, criterions, pos + 1,
                    productStructure,
                    customerStructure,
                    addressStructure,
                    contactPersonStructure,
                    salePositionStructure,
                    parameters);
            if (condIt != null) {
                condition.addAndCondition(condIt);
            }
        }
        return condition;
    }

    private Condition composeCondition(CampaignCriterionValue criterionValue, CampaignCriterion criterion, String value, ListStructure structure, ArrayList parameters) {
        Condition condition;
        Integer fieldType = criterionValue.getFieldType();
        String fieldName = criterionValue.getField();

        //for IN comparations
        if (!"0".equals(criterionValue.getFieldName()) && !"code".equals(criterionValue.getField())) {
            condition = createGroupCondition(fieldType, structure, fieldName, criterion.getOperator(), value, parameters);//list, key, op, value
        } else {
            StringTokenizer tokenizer = null;
            tokenizer = new StringTokenizer(value, ",");
            condition = createCondition(fieldType, structure, fieldName, criterion.getOperator(), tokenizer.nextToken(), tokenizer, parameters);
        }

        return condition;
    }

    private Condition composeAddressContactPersonCondition(CampaignCriterionValue criterionValue, CampaignCriterion criterion, String value, ListStructure structure, ArrayList parameters) {
        Condition condition;

        if (criterionValue.getContactPersonField() != null) {

            Condition cpCondition = createGroupCondition(criterionValue.getFieldType(), structure, criterionValue.getField(), criterion.getOperator(), value, parameters);
            cpCondition.addOrCondition(createGroupCondition(criterionValue.getFieldType(), structure, criterionValue.getContactPersonField(), criterion.getOperator(), value, parameters));

            GroupCondition groupCondition = new GroupCondition();
            groupCondition.setGroupCondition(cpCondition);
            condition = groupCondition;
        } else {
            condition = composeCondition(criterionValue, criterion, value, structure, parameters);
        }
        return condition;
    }

    /*
     * build category conditions, this are special case because must to generate nested queries.
    */
    public ListStructure fillCategoryCondition(String field,
                                               ListStructure greatStructure,
                                               ListStructure structure,
                                               CampaignCriterion criterion) throws FinderException {
        log.debug("... fillCategoryConditions ...");
        ListStructure lStructure = null;
        CampaignCriterionCategoryExecute categoryExecute = new CampaignCriterionCategoryExecute();
        lStructure = categoryExecute.categoryExecuteCriterias(field, greatStructure, structure, criterion);
        return lStructure;
    }

    private Condition composeCategoryCondition(String tableId, ListStructure structure, ListStructure categoryStructureList, CampaignCriterion criterion, String fieldAlias) throws FinderException {

        Condition condition = null;
        /* build category conditions (nested query) */
        ListStructure listStructure = fillCategoryCondition(tableId, structure, categoryStructureList, criterion);

        SimpleCondition simpleCondition = new SimpleCondition();
        simpleCondition.setField1(structure.getField(fieldAlias));
        if (CampaignConstants.COMPARATOR_DISTINCT.equals(criterion.getOperator()) ||
                CampaignConstants.COMPARATOR_NOTIN.equals(criterion.getOperator())) {
            simpleCondition.setOperator(CampaignConstants.COMPARATOR_NOT_IN);
        } else {
            simpleCondition.setOperator(CampaignConstants.COMPARATOR_IN);
        }


        ListCondition listCondition = new ListCondition(simpleCondition);
        listCondition.setList(listStructure);
        condition = listCondition;

        return condition;
    }
}