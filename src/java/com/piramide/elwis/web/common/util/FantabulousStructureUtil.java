package com.piramide.elwis.web.common.util;

import org.alfacentauro.fantabulous.format.structure.Column;
import org.alfacentauro.fantabulous.format.structure.FormatField;
import org.alfacentauro.fantabulous.format.structure.SimpleFieldFormat;
import org.alfacentauro.fantabulous.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util to manage some components of fantabulous structure
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class FantabulousStructureUtil {
    private static Log log = LogFactory.getLog(FantabulousStructureUtil.class);

    public static Field findField(ListStructure listStructure, String fieldAlias) {
        Field field = null;
        field = listStructure.getField(fieldAlias);
        return field;
    }

    public static Relation composeJoinRelation(String pkAlias, String fkAlias, ListStructure listStructure) {
        return composeRelation(pkAlias, fkAlias, listStructure, Join.JOIN);
    }

    public static Relation composeLeftJoinRelation(String pkAlias, String fkAlias, ListStructure listStructure) {
        return composeRelation(pkAlias, fkAlias, listStructure, Join.LEFT_JOIN);
    }

    private static Relation composeRelation(String pkAlias, String fkAlias, ListStructure listStructure, String joinType) {
        Relation relation = new Relation();
        relation.setJoinType(joinType);
        relation.setPk(listStructure.getField(pkAlias));
        relation.setFk(listStructure.getField(fkAlias));

        return relation;
    }

    public static SimpleCondition composeSimpleCondition(Field field, String fantaOperator, String value) {
        SimpleCondition condition = new SimpleCondition();
        condition.setField1(field);
        condition.setOperator(fantaOperator);
        condition.setValue(value);
        return condition;
    }

    public static SimpleCondition composeSimpleCondition(Field field, String fantaOperator, Field field2) {
        SimpleCondition condition = new SimpleCondition();
        condition.setField1(field);
        condition.setOperator(fantaOperator);
        condition.setField2(field2);
        return condition;
    }

    public static Condition addAndCondition(Condition condition, Condition newCondition) {

        if (condition != null) {
            getFinalFantaCondition(condition).addAndCondition(newCondition);
        } else {
            condition = newCondition;
        }
        return condition;
    }

    public static Condition addOrCondition(Condition condition, Condition newCondition) {

        if (condition != null) {
            getFinalFantaCondition(condition).addOrCondition(newCondition);
        } else {
            condition = newCondition;
        }
        return condition;
    }

    public static ListStructure addAndConditionToListStructure(ListStructure listStructure, Condition fantaCondition) {

        if (listStructure != null && fantaCondition != null) {
            //add fantabulous condition to list structure of fantabulous
            Condition listCondition = listStructure.getCondition();
            if (listCondition != null) {
                getFinalFantaCondition(listCondition).addAndCondition(fantaCondition);
            } else {
                listStructure.setCondition(fantaCondition);
            }
        }

        return listStructure;
    }

    public static Condition getFinalFantaCondition(Condition fantaCondition) {
        while (fantaCondition.hasNext()) {
            fantaCondition = fantaCondition.next();
        }
        return fantaCondition;
    }

    public static DBField composeDBField(String name, String alias, DBTypes dbTypes, boolean isPrimaryKey) {
        DBField dbField = new DBField();
        dbField.setFieldName(name);
        dbField.setField(alias);
        dbField.setDbType(dbTypes.value());
        dbField.setKey(isPrimaryKey);

        return dbField;
    }

    public static Column composeColumn(String fieldAlias, ListStructure listStructure) {
        return composeColumn(fieldAlias, fieldAlias, listStructure);
    }

    public static Column composeColumn(String columnName, String fieldAlias, ListStructure listStructure) {
        Column column = new Column(columnName);
        Field field = listStructure.getField(fieldAlias);

        SimpleFieldFormat simple = new SimpleFieldFormat();
        simple.setField(new FormatField(field));
        column.addField(simple);
        column.addFieldsOrder(field);

        return column;
    }
}
