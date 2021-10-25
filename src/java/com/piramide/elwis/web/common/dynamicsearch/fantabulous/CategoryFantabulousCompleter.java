package com.piramide.elwis.web.common.dynamicsearch.fantabulous;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield.CategoryField;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.exception.FieldNotFoundException;
import org.alfacentauro.fantabulous.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class CategoryFantabulousCompleter {
    protected Log log = LogFactory.getLog(this.getClass());

    public static final String TABLE_CATEGORYFIELDVALUE = "categfieldvalue";
    public static final String FIELD_CFV_ADDRESSID = "addressid";
    public static final String FIELD_CFV_ATTACHID = "attachid";
    public static final String FIELD_CFV_CATEGORYID = "categoryid";
    public static final String FIELD_CFV_CATEGORYVALUEID = "categoryvalueid";
    public static final String FIELD_CFV_CONTACTPERSONID = "contactpersonid";
    public static final String FIELD_CFV_CUSTOMERID = "customerid";
    public static final String FIELD_CFV_DATEVALUE = "datevalue";
    public static final String FIELD_CFV_DECIMALVALUE = "decimalvalue";
    public static final String FIELD_CFV_FIELDVALUEID = "fieldvalueid";
    public static final String FIELD_CFV_FILENAME = "filename";
    public static final String FIELD_CFV_FREETEXTID = "freetextid";
    public static final String FIELD_CFV_INTEGERVALUE = "integervalue";
    public static final String FIELD_CFV_LINKVALUE = "linkvalue";
    public static final String FIELD_CFV_PROCESSID = "processid";
    public static final String FIELD_CFV_PRODUCTID = "productid";
    public static final String FIELD_CFV_STRINGVALUE = "stringvalue";

    public static final String TABLE_CATEGORYVALUE = "categoryvalue";
    public static final String FIELD_CV_CATEGORYID = "categoryid";
    public static final String FIELD_CV_CATEGORYVALUEID = "categoryvalueid";
    public static final String FIELD_CV_CATEGORYVALUENAME = "categoryvaluename";
    public static final String FIELD_CV_TABLEID = "tableid";

    public static final String TABLE_FREETEXT = "freetext";
    public static final String FIELD_FT_FREETEXTID = "freetextid";
    public static final String FIELD_FT_FREETEXTVALUE = "freetextvalue";

    public static final String BLOBREAD_FUNCTION = "blobReadFunction";

    public static final String PREFIX_CFV = "01";
    public static final String PREFIX_CV = "02";
    public static final String PREFIX_FT = "03";

    private int postFixIndex;
    private CategoryField categoryField;

    public CategoryFantabulousCompleter(CategoryField categoryField, int postFixIndex) {
        this.categoryField = categoryField;
        this.postFixIndex = postFixIndex;
    }

    public boolean completeListStructure(ListStructure listStructure) {
        log.debug("Complete category fantabulous...");
        boolean isSuccess = verifyJoinFieldsInListStructure(listStructure);

        if (isSuccess) {
            listStructure.addTable(getCategoryFieldValueTable());

            DynamicSearchConstants.CategoryFieldType categoryFieldType = categoryField.getCategoryFieldType();
            if (DynamicSearchConstants.CategoryFieldType.ADDRESS.equals(categoryFieldType)) {
                addAddressCategoryRelations(listStructure);

            } else if (DynamicSearchConstants.CategoryFieldType.CONTACTPERSON.equals(categoryFieldType)) {
                addContactPersonCategoryRelations(listStructure);

            } else if (DynamicSearchConstants.CategoryFieldType.CUSTOMER.equals(categoryFieldType)) {
                Relation relation = composeRelationAddCategoryCondition(composeAlias(PREFIX_CFV, FIELD_CFV_CUSTOMERID), categoryField.getJoinFieldAlias(), listStructure);
                listStructure.addRelation(relation);

            } else if (DynamicSearchConstants.CategoryFieldType.PRODUCT.equals(categoryFieldType)) {
                Relation relation = composeRelationAddCategoryCondition(composeAlias(PREFIX_CFV, FIELD_CFV_PRODUCTID), categoryField.getJoinFieldAlias(), listStructure);
                listStructure.addRelation(relation);

            } else if (DynamicSearchConstants.CategoryFieldType.SALESPROCESS.equals(categoryFieldType)) {
                Relation relation = composeRelationAddCategoryCondition(composeAlias(PREFIX_CFV, FIELD_CFV_PROCESSID), categoryField.getJoinFieldAlias(), listStructure);
                listStructure.addRelation(relation);
            }

            //add value relation if is required
            addValueRelations(listStructure);
        }
        return isSuccess;
    }

    public String getRealCategoryFieldAlias(DynamicSearchConstants.Operator operator) {
        String alias = null;
        CatalogConstants.CategoryType categoryType = categoryField.getCategoryType();

        if (CatalogConstants.CategoryType.TEXT.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_STRINGVALUE);

        } else if (CatalogConstants.CategoryType.INTEGER.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_INTEGERVALUE);

        } else if (CatalogConstants.CategoryType.DECIMAL.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_DECIMALVALUE);

        } else if (CatalogConstants.CategoryType.DATE.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_DATEVALUE);

        } else if (CatalogConstants.CategoryType.LINK_VALUE.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_LINKVALUE);

        } else if (CatalogConstants.CategoryType.ATTACH.equals(categoryType)) {
            alias = composeAlias(PREFIX_CFV, FIELD_CFV_FILENAME);

        } else if (CatalogConstants.CategoryType.FREE_TEXT.equals(categoryType)) {
            alias = composeAlias(PREFIX_FT, BLOBREAD_FUNCTION);

        } else if (CatalogConstants.CategoryType.SINGLE_SELECT.equals(categoryType)
                || CatalogConstants.CategoryType.COMPOUND_SELECT.equals(categoryType)) {
            alias = composeAlias(PREFIX_CV, FIELD_CV_CATEGORYVALUENAME);

            if (DynamicSearchConstants.Operator.IS.equals(operator)
                    || DynamicSearchConstants.Operator.IS_NOT.equals(operator)
                    || DynamicSearchConstants.Operator.ONE_OF.equals(operator)
                    || DynamicSearchConstants.Operator.NOT_ONE_OF.equals(operator)) {
                alias = composeAlias(PREFIX_CV, FIELD_CV_CATEGORYVALUEID);
            }
        }
        return alias;
    }

    public boolean isFunctionAlias() {
        return CatalogConstants.CategoryType.FREE_TEXT.equals(categoryField.getCategoryType());
    }

    private boolean verifyJoinFieldsInListStructure(ListStructure listStructure) {
        Field joinField = findFantabulousField(listStructure, categoryField.getJoinFieldAlias());
        boolean existFields = joinField != null;
        if (categoryField.getJoinFieldAlias2() != null) {
            Field joinField2 = findFantabulousField(listStructure, categoryField.getJoinFieldAlias2());
            existFields = joinField2 != null;
        }
        return existFields;
    }

    private Field findFantabulousField(ListStructure listStructure, String fieldAlias) {
        Field field = null;
        try {
            field = listStructure.getField(fieldAlias);
        } catch (FieldNotFoundException e) {
            log.debug("Not found field to JOIN with category field in list structure:" + fieldAlias);
        }
        return field;
    }

    private Relation composeRelation(String pkAlias, String fkAlias, ListStructure listStructure) {
        Relation relation = new Relation();
        relation.setJoinType(Join.LEFT_JOIN);
        relation.setPk(listStructure.getField(pkAlias));
        relation.setFk(listStructure.getField(fkAlias));

        return relation;
    }

    private Relation composeRelationAddCategoryCondition(String pkAlias, String fkAlias, ListStructure listStructure) {
        Relation relation = composeRelation(pkAlias, fkAlias, listStructure);

        SimpleCondition condition = composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_CFV, FIELD_CFV_CATEGORYID)), Constants.OPERATOR_EQUAL, categoryField.getCategoryId().toString());
        relation.setCondition(condition);
        return relation;
    }

    private void addAddressCategoryRelations(ListStructure listStructure) {
        Relation relation = composeRelationAddCategoryCondition(composeAlias(PREFIX_CFV, FIELD_CFV_ADDRESSID), categoryField.getJoinFieldAlias(), listStructure);

        SimpleCondition conditionCP = composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_CFV, FIELD_CFV_CONTACTPERSONID)), Constants.OPERATOR_IS, "NULL");
        Condition condition = addAndCondition(relation.getCondition(), conditionCP);
        relation.setCondition(condition);

        listStructure.addRelation(relation);
    }

    private void addContactPersonCategoryRelations(ListStructure listStructure) {
        Relation relation = composeRelationAddCategoryCondition(composeAlias(PREFIX_CFV, FIELD_CFV_ADDRESSID), categoryField.getJoinFieldAlias(), listStructure);

        SimpleCondition conditionCP = new SimpleCondition();
        conditionCP.setField1(listStructure.getField(composeAlias(PREFIX_CFV, FIELD_CFV_CONTACTPERSONID)));
        conditionCP.setOperator(Constants.OPERATOR_EQUAL);
        conditionCP.setField2(listStructure.getField(categoryField.getJoinFieldAlias2()));

        Condition condition = addAndCondition(relation.getCondition(), conditionCP);
        relation.setCondition(condition);

        listStructure.addRelation(relation);
    }

    private void addValueRelations(ListStructure listStructure) {
        CatalogConstants.CategoryType categoryType = categoryField.getCategoryType();

        if (CatalogConstants.CategoryType.SINGLE_SELECT.equals(categoryType)
                || CatalogConstants.CategoryType.COMPOUND_SELECT.equals(categoryType)) {

            listStructure.addTable(getCategoryValueTable());
            Relation relation = composeRelation(composeAlias(PREFIX_CV, FIELD_CV_CATEGORYVALUEID), composeAlias(PREFIX_CFV, FIELD_CFV_CATEGORYVALUEID), listStructure);
            listStructure.addRelation(relation);

        } else if (CatalogConstants.CategoryType.FREE_TEXT.equals(categoryType)) {

            listStructure.addTable(getFreeTextTable());
            Relation relation = composeRelation(composeAlias(PREFIX_FT, FIELD_FT_FREETEXTID), composeAlias(PREFIX_CFV, FIELD_CFV_FREETEXTID), listStructure);
            listStructure.addRelation(relation);

            //function to read freetext blob data
            Function function = new Function();
            function.setField(composeAlias(PREFIX_FT, BLOBREAD_FUNCTION));
            function.setClazz("com.piramide.elwis.utils.dbfunction.BlobReadFunction");

            FunctionParameter functionParameter = new FunctionParameter();
            functionParameter.setType("field");
            functionParameter.setField(listStructure.getField(composeAlias(PREFIX_FT, FIELD_FT_FREETEXTVALUE)));
            List params = new ArrayList();
            params.add(functionParameter);

            function.setParameters(params);

            listStructure.addFunction(function);
        }
    }

    private List getFunctions(Iterator iteratorFunctions, ListStructure list) {
        List functions = new ArrayList();
        while (iteratorFunctions.hasNext()) {
            Function function = new Function();
            Element functionElement = (Element) iteratorFunctions.next();
            function.setFieldName(functionElement.getAttributeValue("name"));
            function.setField(functionElement.getAttributeValue("alias"));

            if (functionElement.getAttributeValue("class") != null) {
                function.setClazz(functionElement.getAttributeValue("class"));
            }

            List params = new ArrayList();
            Iterator iteratorParameters = functionElement.getChildren().iterator();

            while (iteratorParameters.hasNext()) {
                Element parameter = (Element) iteratorParameters.next();
                FunctionParameter functionParameter = new FunctionParameter();
                if (parameter.getAttributeValue("type") != null)
                    functionParameter.setType(parameter.getAttributeValue("type"));
                if (parameter.getAttributeValue("field") != null)
                    functionParameter.setField(list.getField(parameter.getAttributeValue("field")));
                if (parameter.getAttributeValue("value") != null)
                    functionParameter.setValue(parameter.getAttributeValue("value"));
                params.add(functionParameter);
            }
            function.setParameters(params);
            functions.add(function);
        }
        return functions;
    }


    private SimpleCondition composeSimpleCondition(Field field, String fantaOperator, String value) {
        SimpleCondition condition = new SimpleCondition();
        condition.setField1(field);
        condition.setOperator(fantaOperator);
        condition.setValue(value);
        return condition;
    }

    private Condition addAndCondition(Condition condition, Condition newCondition) {

        if (condition != null) {
            getFinalFantaCondition(condition).addAndCondition(newCondition);
        } else {
            condition = newCondition;
        }
        return condition;
    }

    private Condition getFinalFantaCondition(Condition fantaCondition) {
        while (fantaCondition.hasNext()) {
            fantaCondition = fantaCondition.next();
        }
        return fantaCondition;
    }

    private Table getCategoryFieldValueTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_CFV, TABLE_CATEGORYFIELDVALUE));
        table.setTableName(TABLE_CATEGORYFIELDVALUE);

        table.addField(composeDBField(FIELD_CFV_FIELDVALUEID, composeAlias(PREFIX_CFV, FIELD_CFV_FIELDVALUEID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_CFV_ADDRESSID, composeAlias(PREFIX_CFV, FIELD_CFV_ADDRESSID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_ATTACHID, composeAlias(PREFIX_CFV, FIELD_CFV_ATTACHID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_CATEGORYID, composeAlias(PREFIX_CFV, FIELD_CFV_CATEGORYID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_CATEGORYVALUEID, composeAlias(PREFIX_CFV, FIELD_CFV_CATEGORYVALUEID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_CONTACTPERSONID, composeAlias(PREFIX_CFV, FIELD_CFV_CONTACTPERSONID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_CUSTOMERID, composeAlias(PREFIX_CFV, FIELD_CFV_CUSTOMERID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_DATEVALUE, composeAlias(PREFIX_CFV, FIELD_CFV_DATEVALUE), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_DECIMALVALUE, composeAlias(PREFIX_CFV, FIELD_CFV_DECIMALVALUE), DBTypes.DECIMAL, false));
        table.addField(composeDBField(FIELD_CFV_FILENAME, composeAlias(PREFIX_CFV, FIELD_CFV_FILENAME), DBTypes.STRING, false));
        table.addField(composeDBField(FIELD_CFV_FREETEXTID, composeAlias(PREFIX_CFV, FIELD_CFV_FREETEXTID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_INTEGERVALUE, composeAlias(PREFIX_CFV, FIELD_CFV_INTEGERVALUE), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_LINKVALUE, composeAlias(PREFIX_CFV, FIELD_CFV_LINKVALUE), DBTypes.STRING, false));
        table.addField(composeDBField(FIELD_CFV_PROCESSID, composeAlias(PREFIX_CFV, FIELD_CFV_PROCESSID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_PRODUCTID, composeAlias(PREFIX_CFV, FIELD_CFV_PRODUCTID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CFV_STRINGVALUE, composeAlias(PREFIX_CFV, FIELD_CFV_STRINGVALUE), DBTypes.STRING, false));

        return table;
    }

    private Table getCategoryValueTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_CV, TABLE_CATEGORYVALUE));
        table.setTableName(TABLE_CATEGORYVALUE);

        table.addField(composeDBField(FIELD_CV_CATEGORYVALUEID, composeAlias(PREFIX_CV, FIELD_CV_CATEGORYVALUEID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_CV_CATEGORYID, composeAlias(PREFIX_CV, FIELD_CV_CATEGORYID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_CV_CATEGORYVALUENAME, composeAlias(PREFIX_CV, FIELD_CV_CATEGORYVALUENAME), DBTypes.STRING, false));
        table.addField(composeDBField(FIELD_CV_TABLEID, composeAlias(PREFIX_CV, FIELD_CV_TABLEID), DBTypes.STRING, false));

        return table;
    }

    private Table getFreeTextTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_FT, TABLE_FREETEXT));
        table.setTableName(TABLE_FREETEXT);

        table.addField(composeDBField(FIELD_FT_FREETEXTID, composeAlias(PREFIX_FT, FIELD_FT_FREETEXTID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_FT_FREETEXTVALUE, composeAlias(PREFIX_FT, FIELD_FT_FREETEXTVALUE), DBTypes.STRING, false));

        return table;
    }

    private DBField composeDBField(String name, String alias, DBTypes dbTypes, boolean isPrimaryKey) {
        DBField dbField = new DBField();
        dbField.setFieldName(name);
        dbField.setField(alias);
        dbField.setDbType(dbTypes.value());
        dbField.setKey(isPrimaryKey);

        return dbField;
    }

    private String composeAlias(String prefix, String name) {
        return name + prefix + String.valueOf(postFixIndex);
    }



}



