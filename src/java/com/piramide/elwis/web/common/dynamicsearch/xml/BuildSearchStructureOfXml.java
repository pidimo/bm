package com.piramide.elwis.web.common.dynamicsearch.xml;

import com.piramide.elwis.web.common.dynamicsearch.DynamicSearchConstants;
import com.piramide.elwis.web.common.dynamicsearch.structure.DynamicSearch;
import com.piramide.elwis.web.common.dynamicsearch.structure.DynamicSearchStructure;
import com.piramide.elwis.web.common.dynamicsearch.structure.Field;
import com.piramide.elwis.web.common.dynamicsearch.structure.FieldOperator;
import com.piramide.elwis.web.common.dynamicsearch.structure.dynamicfield.DynamicField;
import com.piramide.elwis.web.common.dynamicsearch.util.OperatorUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class BuildSearchStructureOfXml {
    private final Log log = LogFactory.getLog(this.getClass());
    private DynamicSearchStructure structure;
    private ReadDynamicSearchStructure reader;

    public BuildSearchStructureOfXml() {
        initialize();
    }

    private void initialize() {
        structure = new DynamicSearchStructure();
    }

    public void buildStructure(InputStream inputStream) throws IOException, JDOMException {
        reader = new ReadDynamicSearchStructure(inputStream);
        composeStructure();
    }

    public DynamicSearchStructure getStructure() {
        return structure;
    }

    private void composeStructure() {
        List<Element> dynamicSearchList = reader.getDynamicSearchs();
        for (Element dynamicSearchElement : dynamicSearchList) {
            String dynamicSearchName = dynamicSearchElement.getAttributeValue(XmlConstants.ATTRIB_DYNAMICSEARCH_NAME);

            DynamicSearch dynamicSearch = new DynamicSearch(dynamicSearchName);

            List<Element> fieldsList = reader.getFields(dynamicSearchElement);
            for (Element fieldElement : fieldsList) {

                Field field = composeField(fieldElement);

                Element innerFieldElement = reader.getInnerFieldElement(fieldElement);
                if (innerFieldElement != null) {
                    Field innerField = composeField(innerFieldElement);
                    field.setInnerField(innerField);
                }
                dynamicSearch.addField(field);
            }

            //dynamic fields
            List<Element> dynamicFieldsList = reader.getDynamicFields(dynamicSearchElement);
            for (Element dynamicFieldElement : dynamicFieldsList) {
                DynamicField dynamicField = composeDynamicField(dynamicFieldElement);
                dynamicSearch.addDynamicField(dynamicField);
            }

            structure.addDynamicSearch(dynamicSearch);
        }
    }

    private Field composeField(Element fieldElement) {
        String alias = fieldElement.getAttributeValue(XmlConstants.ATTRIB_FIELD_ALIAS);
        String type = fieldElement.getAttributeValue(XmlConstants.ATTRIB_FIELD_TYPE);
        String resource = fieldElement.getAttributeValue(XmlConstants.ATTRIB_FIELD_RESOURCE);

        Field field = new Field();
        field.setAlias(alias);
        field.setResource(resource);

        DynamicSearchConstants.FieldType fieldType = DynamicSearchConstants.FieldType.findFieldType(type);
        if (fieldType != null) {
            field.setType(fieldType);
        } else {
            throw new RuntimeException("Not found valid field type:" + type);
        }

        addFieldOperators(field, fieldElement);

        return field;
    }

    private void addFieldOperators(Field field, Element fieldElement) {

        List<Element> operatorsList = reader.getFieldOperators(fieldElement);
        if (operatorsList.isEmpty()) {
            addFieldOperatorsByType(field);

        } else {
            for (Element operatorElement : operatorsList) {
                String operatorName = operatorElement.getAttributeValue(XmlConstants.ATTRIB_OPERATOR_NAME);
                String parameterName = operatorElement.getAttributeValue(XmlConstants.ATTRIB_OPERATOR_PARAMETERNAME);
                String isParameter = operatorElement.getAttributeValue(XmlConstants.ATTRIB_OPERATOR_ISPARAMETER);
                String isDefaultAttr = operatorElement.getAttributeValue(XmlConstants.ATTRIB_OPERATOR_ISDEFAULT);

                DynamicSearchConstants.Operator operator = DynamicSearchConstants.Operator.findOperator(operatorName);
                if (operator == null) {
                    throw new RuntimeException("Not found valid field operator:" + operatorName);
                }

                boolean isDefault = false;
                if (isDefaultAttr != null) {
                    isDefault = Boolean.valueOf(isDefaultAttr);
                } else if (operatorsList.size() == 1) {
                    isDefault = true;
                }

                FieldOperator fieldOperator = new FieldOperator();
                fieldOperator.setOperator(operator);
                fieldOperator.setParameterName(parameterName);
                fieldOperator.setIsParameter(isParameter != null ? Boolean.valueOf(isParameter) : false);
                fieldOperator.setIsDefault(isDefault);

                field.addOperator(fieldOperator);
            }
        }
    }

    public static void addFieldOperatorsByType(Field field) {
        List<DynamicSearchConstants.Operator> operators = new ArrayList<DynamicSearchConstants.Operator>();
        DynamicSearchConstants.Operator defaultOperator = OperatorUtil.getDefaultOperatorByFieldType(field);

        if (DynamicSearchConstants.FieldType.STRING.equals(field.getType())) {
            operators = OperatorUtil.getTextOperators();
        } else if (DynamicSearchConstants.FieldType.INTEGER.equals(field.getType())
                || DynamicSearchConstants.FieldType.DECIMAL.equals(field.getType())) {
            operators = OperatorUtil.getNumberOperators();
        } else if (DynamicSearchConstants.FieldType.DATE.equals(field.getType())) {
            operators = OperatorUtil.getDateOperators();
        } else if (DynamicSearchConstants.FieldType.DATABASE.equals(field.getType())) {
            operators = OperatorUtil.getDataBaseOperators();
        } else if (DynamicSearchConstants.FieldType.CONSTANT.equals(field.getType())) {
            operators = OperatorUtil.getConstantOperators();
        } else if (DynamicSearchConstants.FieldType.BITWISE.equals(field.getType())) {
            operators = OperatorUtil.getBitwiseOperators();
        }

        for (DynamicSearchConstants.Operator operator : operators) {
            boolean isDefault = operator.equals(defaultOperator);

            FieldOperator fieldOperator = new FieldOperator();
            fieldOperator.setOperator(operator);
            fieldOperator.setIsDefault(isDefault);

            field.addOperator(fieldOperator);
        }
    }

    private DynamicField composeDynamicField(Element dynamicFieldElement) {
        String loadFieldClass = dynamicFieldElement.getAttributeValue(XmlConstants.ATTRIB_DYNAMICFIELD_LOADFIELDCLASS);
        String joinFieldAlias = dynamicFieldElement.getAttributeValue(XmlConstants.ATTRIB_DYNAMICFIELD_JOINFIELDALIAS);
        String joinFieldAlias2 = dynamicFieldElement.getAttributeValue(XmlConstants.ATTRIB_DYNAMICFIELD_JOINFIELDALIAS2);

        DynamicField dynamicField = new DynamicField(loadFieldClass);
        dynamicField.setJoinFieldAlias(joinFieldAlias);
        dynamicField.setJoinFieldAlias2(joinFieldAlias2);
        return dynamicField;
    }

}
