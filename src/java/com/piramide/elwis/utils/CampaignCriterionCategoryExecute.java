package com.piramide.elwis.utils;

import com.piramide.elwis.domain.campaignmanager.CampaignCriterion;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeText;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeTextHome;
import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.structure.Condition;
import org.alfacentauro.fantabulous.structure.GroupCondition;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.structure.SimpleCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.StringTokenizer;

/**
 * Class responsible of to build category criteria dynamic in campaign.
 *
 * @author : Mauren
 */

public class CampaignCriterionCategoryExecute {
    Log log = LogFactory.getLog(this.getClass());

    public ListStructure categoryExecuteCriterias(String isContactPerson,
                                                  ListStructure greatStructure,
                                                  ListStructure structure,
                                                  CampaignCriterion criterion) throws FinderException {

        log.debug(" ... CampaignCriterionCategoryExecute ...");
        Condition condition = null;
        Condition categoryCondition = null;
        Integer fieldType = null;
        CampaignFreeText freeText = null;
        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        try {
            freeText = freeTextHome.findByPrimaryKey(criterion.getValueId());
            CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CATEGORY);
            Category category = categoryHome.findByPrimaryKey(criterion.getCategoryId());
            String field = "categoryId";
            fieldType = category.getCategoryType();
            StringTokenizer tokenizer = new StringTokenizer(new String(freeText.getValue()), ",");
// create group condition
            categoryCondition = createCategoryCondition(isContactPerson, greatStructure, structure, field, CampaignConstants.EQUAL, category);//list, key, op, value

            if (fieldType.equals(1)) {
                field = "integerValue";
            } else if (fieldType.equals(2)) {
                field = "decimalValue";
            } else if (fieldType.equals(3)) {
                field = "dateValue";
            } else if (fieldType.equals(4)) {
                field = "stringValue";
            } else {
                field = "categoryValueId";
            }
//create simple condition
            condition = createCondition(structure, field, criterion.getOperator(), tokenizer.nextToken(), tokenizer);//list, key, op, value

            GroupCondition groupCond = new GroupCondition();
            ListStructure listStructure = new ListStructure();
            groupCond.addAndCondition(categoryCondition);
            groupCond.setGroupCondition(condition);
            listStructure.setCondition(groupCond);
            listStructure.setTables(structure.getTables());
            listStructure.setRelations(structure.getRelations());
            listStructure.setDistinct(true);
            listStructure.setConfiguration(structure.getConfiguration());
            listStructure.setOrder(structure.getOrder());
            listStructure.setMainTable(structure.getMainTable());

            return listStructure;

        } catch (FinderException e) {
            log.debug("campaignCriterionValues not  found .... ");
            return null;
        }
    }

    private Condition createCategoryCondition(String field,
                                              ListStructure greatStructure,
                                              ListStructure structure,
                                              String condName,
                                              String operator,
                                              Category category) {
        SimpleCondition condition = new SimpleCondition();
        condition.setOperator(operator);
        condition.setField1(structure.getField(condName));
        condition.setValue(category.getCategoryId().toString());

        if ("2".equals(field)) {
            SimpleCondition cPersonCond = new SimpleCondition();
            cPersonCond.setField1(structure.getField("addressId"));
            cPersonCond.setOperator(operator);
            cPersonCond.setField2(greatStructure.getField("contactAddressId"));
            condition.addAndCondition(cPersonCond);
        }

        if (ContactConstants.ADDRESS_CATEGORY.equals(field) && ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(category.getTable())) {
            SimpleCondition cPersonCond = new SimpleCondition();
            cPersonCond.setField1(structure.getField("contactPersonId"));
            cPersonCond.setOperator("IS");
            cPersonCond.setValue("NULL");
            condition.addAndCondition(cPersonCond);
        }
        return condition;
    }

    private Condition createCondition(ListStructure structure,
                                      String condName,
                                      String operator,
                                      String value,
                                      StringTokenizer values) {
        Condition con;
        log.debug("... category operator ... " + operator);
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
            simple.setField1(structure.getField(condName));
            simple.setOperator("GREATER_EQUAL");
            simple.setValue(value);
            SimpleCondition cond1 = new SimpleCondition();
            String value1 = values.nextToken();
            cond1.setField1(simple.getField1());
            cond1.setOperator("LESS_EQUAL");
            cond1.setValue(value1);

            simple.addAndCondition(cond1);
            condition.setGroupCondition(simple);
            con = condition;
        } else {
            SimpleCondition condition = new SimpleCondition();
            condition.setField1(structure.getField(condName));

            if (CampaignConstants.COMPARATOR_IN.equals(operator) || CampaignConstants.COMPARATOR_NOTIN.equals(operator)
                    || CampaignConstants.COMPARATOR_DISTINCT.equals(operator)) {
                condition.setOperator(CampaignConstants.COMPARATOR_EQUAL);
            } else {
                condition.setOperator(operator);
            }
            condition.setValue(value);

            String nextVal;
            if (values.hasMoreTokens()) {
                nextVal = values.nextToken();
                if (CampaignConstants.COMPARATOR_IN.equals(operator) ||
                        CampaignConstants.COMPARATOR_NOTIN.equals(operator)
                        || CampaignConstants.COMPARATOR_DISTINCT.equals(operator))//add
                {
                    condition.addOrCondition(createCondition(structure, condName, operator, nextVal, values));
                } else {
                    condition.addAndCondition(createCondition(structure, condName, operator, nextVal, values));
                }
            }
            con = condition;
        }
        return con;
    }
}
