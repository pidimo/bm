package com.piramide.elwis.web.dashboard.component.configuration.reader;


import com.piramide.elwis.web.dashboard.component.configuration.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 */
public class BuildStructure {
    private Log log = LogFactory.getLog(this.getClass());

    private ReadComponentDefinition reader;

    public BuildStructure(String filePath) throws IOException, JDOMException {
        log.debug("BuildStructure('" + filePath + "')");

        reader = new ReadComponentDefinition(filePath);
    }

    public BuildStructure(InputStream inputStream) throws IOException, JDOMException {
        log.debug("BuildStructure(java.io.InputStream)");

        reader = new ReadComponentDefinition(inputStream);
    }

    public List<Component> createStructure() {

        List<Component> componentObjects = new ArrayList<Component>();
        Element dataProcessorTAG = reader.getElementX(XmlConstants.DataProcessor.elementName, reader.getRootElement());
        Element convertersTAG = reader.getElementX(XmlConstants.Converters.elementName, reader.getRootElement());


        String value = dataProcessorTAG.getTextTrim();

        Configuration cfg = new Configuration();
        cfg.setDataProcessor(value);

        Element persistenceProcessorTAG = reader.getElementX(XmlConstants.PersistenceProcessor.elementName, reader.getRootElement());
        String clssValue = persistenceProcessorTAG.getTextTrim();

        Element conditionEvaluatorTAG = reader.getElementX(XmlConstants.ConditionEvaluator.elementName, reader.getRootElement());
        String classValue = conditionEvaluatorTAG.getTextTrim();

        cfg.setPersistenceProcessor(clssValue);
        cfg.setConverters(buildConverters(convertersTAG));
        cfg.setConditionEvaluator(classValue);

        List components = reader.getChlidrenOfRoot(XmlConstants.Component.elementName);
        for (int i = 0; i < components.size(); i++) {
            Element componentTAG = (Element) components.get(i);

            Element paramsTAG = componentTAG.getChild(XmlConstants.Params.elementName);

            Element columnsTAG = componentTAG.getChild(XmlConstants.Columns.elementName);

            Element windowOptionsTAG = componentTAG.getChild(XmlConstants.WindowOptions.elementName);

            Element configurationsTAG = componentTAG.getChild(XmlConstants.Configurations.elementName);

            Element filterTAG = componentTAG.getChild(XmlConstants.Filters.elementName);

            List<Parameter> params = buildParameters(paramsTAG);

            List<Column> columns = buildColumns(columnsTAG);

            List<Action> actions = buildActions(windowOptionsTAG);

            List<Filter> filters = buildFilters(filterTAG);

            Configuration componentConfiguration = buildComponentConfigurations(configurationsTAG);


            String ids = componentTAG.getAttributeValue(XmlConstants.Component.attrId);
            String name = componentTAG.getAttributeValue(XmlConstants.Component.attrName);
            String resource = componentTAG.getAttributeValue(XmlConstants.Component.attrNameResource);
            String functionality = componentTAG.getAttributeValue(XmlConstants.Component.attrFunctionality);
            String permission = componentTAG.getAttributeValue(XmlConstants.Component.attrPermission);

            int id;
            try {
                id = new Integer(ids);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(nfe);
            }

            Component component = new Component(id, name, columns, actions, filters, cfg);
            component.setParameters(params);
            component.setResourceKey(resource);
            component.setComponentConfiguration(componentConfiguration);
            component.setFunctionality(functionality);
            component.setPermission(permission);
            componentObjects.add(component);
        }


        return componentObjects;
    }

    private List<Converter> buildConverters(Element convertersTAG) {
        List<Converter> l = new ArrayList<Converter>();
        List converterTAGs = reader.getChildrenOfElementX(XmlConstants.Converter.elementName, convertersTAG);

        for (int i = 0; i < converterTAGs.size(); i++) {
            Element e = (Element) converterTAGs.get(i);
            String name = e.getAttributeValue(XmlConstants.Converter.attrName);
            String clazz = e.getAttributeValue(XmlConstants.Converter.attrClass);
            Converter c = new Converter();
            c.setClazz(clazz);
            c.setName(name);
            l.add(c);
        }
        return l;
    }

    private Configuration buildComponentConfigurations(Element configurationsTAG) {

        Element filterPreProcessorTAG = reader.getElementX(XmlConstants.FilterPreProcessor.elementName, configurationsTAG);
        Element uiProcessor = reader.getElementX(XmlConstants.UiProcessor.elementName, configurationsTAG);
        Element accessUrlTAG = reader.getElementX(XmlConstants.AccessUrl.elementName, configurationsTAG);
        Element componentUrlTAG = reader.getElementX(XmlConstants.ComponentUrl.elementName, configurationsTAG);


        Configuration cfg = new Configuration();

        String type = "";
        String className = "";

        if (null != uiProcessor) {
            type = uiProcessor.getAttributeValue(XmlConstants.UiProcessor.attrType);
            className = uiProcessor.getAttributeValue(XmlConstants.UiProcessor.attrClass);
            cfg.setIuImplementation(type);

            if (XmlConstants.UtilConstants.attrCustom.equals(type)) {
                cfg.setIuImplementationClass(className);
            }
        }


        if (null != filterPreProcessorTAG) {
            String clssValue = filterPreProcessorTAG.getTextTrim();
            cfg.setFilterPreprocessor(clssValue);
        }

        if (null != accessUrlTAG) {
            List<Parameter> parameters;
            String url = accessUrlTAG.getAttributeValue(XmlConstants.AccessUrl.attrUrl);
            Element paramsE = accessUrlTAG.getChild(XmlConstants.Params.elementName);
            parameters = buildParameters(paramsE);
            cfg.setAccessUrl(url, parameters);
        }

        if (null != componentUrlTAG) {
            List<Parameter> parameters;
            String url = componentUrlTAG.getAttributeValue(XmlConstants.ComponentUrl.attrUrl);
            Element paramsE = componentUrlTAG.getChild(XmlConstants.Params.elementName);
            parameters = buildParameters(paramsE);
            cfg.setComponentUrl(url, parameters);
        }

        return cfg;
    }

    private List<Parameter> buildParameters(Element paramsTAG) {

        List<Parameter> l = new ArrayList<Parameter>();

        if (null == paramsTAG) {
            return l;
        }

        List list = reader.getChildrenOfElementX(XmlConstants.Param.elementName, paramsTAG);
        for (int i = 0; i < list.size(); i++) {
            Element e = (Element) list.get(i);

            String name = e.getAttributeValue(XmlConstants.Param.attrName);
            String value = e.getAttributeValue(XmlConstants.Param.attrValue);
            if (null == value) {
                value = "";
            }
            String columnId = e.getAttributeValue(XmlConstants.Param.attrColumnId);
            Parameter p = new Parameter(name, value);
            p.setColumnId(columnId);

            l.add(p);
        }
        return l;
    }

    private List<Column> buildColumns(Element columnsTAG) {

        List<Column> result = new ArrayList<Column>();
        if (null == columnsTAG) {
            return result;
        }

        List columns = reader.getChildrenOfElementX(XmlConstants.ColumnTag.elementName.getConstant(), columnsTAG);
        for (int i = 0; i < columns.size(); i++) {
            Element e = (Element) columns.get(i);

            int id;
            try {
                id = new Integer(e.getAttributeValue(XmlConstants.ColumnTag.attrId.getConstant()));
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(nfe);
            }

            String name = e.getAttributeValue(XmlConstants.ColumnTag.attrName.getConstant());
            String resourceKey = e.getAttributeValue(XmlConstants.ColumnTag.attrResourceKey.getConstant());
            String defaultColumn = e.getAttributeValue(XmlConstants.ColumnTag.attrDefault.getConstant());
            String accessColumn = e.getAttributeValue(XmlConstants.ColumnTag.attrAccessColumn.getConstant());
            String order = e.getAttributeValue(XmlConstants.ColumnTag.attrOrder.getConstant());
            String propertyName = e.getAttributeValue(XmlConstants.ColumnTag.attrPropertyName.getConstant());
            String patternKey = e.getAttributeValue(XmlConstants.ColumnTag.attrPatternKey.getConstant());
            String converter = e.getAttributeValue(XmlConstants.ColumnTag.attrConverter.getConstant());
            String size = e.getAttributeValue(XmlConstants.ColumnTag.attrSize.getConstant());
            String inverseOrder = e.getAttributeValue(XmlConstants.ColumnTag.attrInverseOrder.getConstant());


            boolean isAccessColumn = XmlConstants.UtilConstants.attrTrue.equals(accessColumn);
            boolean isDefaultColumn =
                    XmlConstants.UtilConstants.attrTrue.equals(defaultColumn) ? Boolean.valueOf(true) : Boolean.valueOf(false);
            boolean isInverseOrder = XmlConstants.UtilConstants.attrTrue.equals(inverseOrder);

            Column c = new Column(id, name, order);

            c.setDefaultColumn(isDefaultColumn);
            c.setAccessColumn(isAccessColumn);
            c.setPropertyName(propertyName);
            c.setResourceKey(resourceKey);
            c.setPatternKey(patternKey);
            c.setConverter(converter);
            c.setSize(size);
            c.setInverseOrder(isInverseOrder);

            List<Constant> lc = buildConstants(e);
            c.setConstants(lc);

            List<Condition> conditions = buildConditions(e);
            c.setConditions(conditions);
            result.add(c);
        }
        return result;
    }

    private List<Constant> buildConstants(Element columnTAG) {
        List<Constant> r = new ArrayList<Constant>();

        List constants = reader.getChildrenOfElementX(XmlConstants.Constant.elementName, columnTAG);
        if (null != constants) {
            for (int i = 0; i < constants.size(); i++) {
                Element e = (Element) constants.get(i);

                String value = e.getAttributeValue(XmlConstants.Constant.attrValue);
                String resourceKey = e.getAttributeValue(XmlConstants.Constant.attrResourceKey);

                Constant c = new Constant();
                c.setValue(value);
                c.setResourceKey(resourceKey);

                r.add(c);
            }
        }

        return r;
    }

    private List<Condition> buildConditions(Element columnTAG) {
        List<Condition> c = new ArrayList<Condition>();

        List conditions = reader.getChildrenOfElementX(XmlConstants.ConditionTag.elementName.getConstant(), columnTAG);
        if (null != conditions) {
            for (int i = 0; i < conditions.size(); i++) {
                Element e = (Element) conditions.get(i);

                String type = e.getAttributeValue(XmlConstants.ConditionTag.attrType.getConstant());
                String operator = e.getAttributeValue(XmlConstants.ConditionTag.attrOperator.getConstant());
                String value = e.getAttributeValue(XmlConstants.ConditionTag.attrValue.getConstant());
                String style = e.getAttributeValue(XmlConstants.ConditionTag.attrStyle.getConstant());

                Condition cn = new Condition(type, operator, value, style);
                c.add(cn);
            }
        }

        return c;
    }

    private List<Action> buildActions(Element windowOptionsTAG) {
        List<Action> result = new ArrayList<Action>();

        List windowOptions = reader.getChildrenOfElementX(XmlConstants.WindowOption.elementName, windowOptionsTAG);
        for (int i = 0; i < windowOptions.size(); i++) {
            Element e = (Element) windowOptions.get(i);
            String actionV = e.getAttributeValue(XmlConstants.WindowOption.attrAction);
            String iconUrl = e.getAttributeValue(XmlConstants.WindowOption.attrIconUrl);
            String resourceKey = e.getAttributeValue(XmlConstants.WindowOption.attrResourceKey);

            Action action = new Action();
            action.setAction(actionV);
            action.setIconUrl(iconUrl);
            action.setResourcekey(resourceKey);
            result.add(action);
        }
        return result;
    }

    private List<Filter> buildFilters(Element filtersTAG) {
        List<Filter> result = new ArrayList<Filter>();

        if (null == filtersTAG) {
            return result;
        }

        List filters = filtersTAG.getChildren(XmlConstants.Filter.elementName);
        for (int i = 0; i < filters.size(); i++) {
            Element e = (Element) filters.get(i);
            String name = e.getAttributeValue(XmlConstants.Filter.attrName);
            String nameResource = e.getAttributeValue(XmlConstants.Filter.attrResourceKey);
            String secondResourceKey = e.getAttributeValue(XmlConstants.Filter.attrSecondResourceKey);
            String type = e.getAttributeValue(XmlConstants.Filter.attrType);
            String view = e.getAttributeValue(XmlConstants.Filter.attrView);
            String selectValue = e.getAttributeValue(XmlConstants.Filter.attrSelectValue);
            String selectId = e.getAttributeValue(XmlConstants.Filter.attrSelectId);


            String defaultValue = null;
            String className = null;
            String methodName = null;
            List<Parameter> parameters = null;
            Object defaultValueAsObject = null;

            viewTypeValidator(view, type, name);

            Element valueElement = e.getChild(XmlConstants.Values.elementName);

            Element defaultElement = valueElement.getChild(XmlConstants.Default.elementName);

            Element clssElement = valueElement.getChild(XmlConstants.ConstantClass.elementName);

            Element qryElement = valueElement.getChild(XmlConstants.Query.elementName);

            if (null != defaultElement) {
                defaultValue = defaultElement.getAttributeValue(XmlConstants.Default.attrValue);
                defaultValueAsObject = typeValidator(defaultValue, type, name);
                elementDefaulViewValidator(view, name);
            }

            if (null != clssElement) {
                elementConstantClassViewValidator(view, name);
                className = clssElement.getAttributeValue(XmlConstants.ConstantClass.attrClassName);
                methodName = clssElement.getAttributeValue(XmlConstants.ConstantClass.attrMethodName);
            }

            if (null != qryElement) {
                elementQueryViewValidator(view, name);
                Element paramsE = qryElement.getChild(XmlConstants.Params.elementName);
                parameters = buildParameters(paramsE);
            }

            ConfigurableFilter confFilter = new ConfigurableFilter(name, nameResource, type);
            confFilter.setView(view);
            confFilter.setClassAndMethodName(className, methodName);
            String value = null;
            if (null != defaultValueAsObject) {
                value = defaultValueAsObject.toString();
            }
            confFilter.setInitialValue(value);
            if (confFilter.isRangeView()) {
                confFilter.setFinalValue(confFilter.getInitialValue());
                confFilter.setSecondResourceKey(secondResourceKey);
            }
            confFilter.setParameters(parameters);
            confFilter.setSelectId(selectId);
            confFilter.setSelectValue(selectValue);

            result.add(confFilter);
        }

        List staticFilters = filtersTAG.getChildren(XmlConstants.StaticFilter.elementName);
        for (int i = 0; i < staticFilters.size(); i++) {
            Element e = (Element) staticFilters.get(i);

            String name = e.getAttributeValue(XmlConstants.StaticFilter.attrName);
            String multipleValue = e.getAttributeValue(XmlConstants.StaticFilter.attrMultipleValue);

            boolean isMultipleValue = XmlConstants.UtilConstants.attrTrue.equals(multipleValue) ? Boolean.valueOf(true) : Boolean.valueOf(false);

            StaticFilter staticFilter = new StaticFilter(name);
            staticFilter.setMultipleValue(isMultipleValue);

            result.add(staticFilter);
        }

        Element rowCounterElement = filtersTAG.getChild(XmlConstants.RowCounterFilterTag.elementName.getConstant());
        if (null != rowCounterElement) {
            String name = rowCounterElement.getAttributeValue(XmlConstants.RowCounterFilterTag.attrName.getConstant());
            String resourceKey =
                    rowCounterElement.getAttributeValue(XmlConstants.RowCounterFilterTag.attrResourceKey.getConstant());
            String value = rowCounterElement.getAttributeValue(XmlConstants.RowCounterFilterTag.attrValue.getConstant());

            RowCounterFilter rowCounterFilter = new RowCounterFilter(name, resourceKey);
            Object newValue = typeValidator(value, rowCounterFilter.getDataType(), name);
            rowCounterFilter.setInitialValue(newValue.toString());

            result.add(rowCounterFilter);
        }
        return result;
    }

    private Object typeValidator(String value, String type, String nameF) {
        Object obj = value;
        boolean integerValidator = XmlConstants.UtilConstants.attrInteger.equals(type);
        boolean booleanValidator = XmlConstants.UtilConstants.attrBoolean.equals(type);

        if (integerValidator) {
            try {
                obj = new Integer(value);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("error on filter: " + nameF + ", invalid value has defined in <default value=\"" + value + "\"/>, they must be an number or sequence of numbers");
            }
        }


        if (booleanValidator) {
            boolean test = ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value));
            if (!test) {
                throw new RuntimeException("error on filter: " + nameF + ", invalid value has defined in <default value=\"" + value + "\"/>, they must be an TRUE or FALSE");
            }

            obj = Boolean.valueOf(Boolean.valueOf(value.toLowerCase()));
        }

        return obj;
    }

    private void viewTypeValidator(String view, String type, String nameF) {
        boolean isSelectView = XmlConstants.UtilConstants.attrSelect.equals(view);
        boolean isTextView = XmlConstants.UtilConstants.attrText.equals(view);
        boolean isCheckView = XmlConstants.UtilConstants.attrCheck.equals(view);
        boolean isRangeView = XmlConstants.UtilConstants.attrRange.equals(view);


        if (isTextView && XmlConstants.UtilConstants.attrBoolean.equals(type)) {
            throw new RuntimeException("error on filter: " + nameF + " not supported options view=" + view + " and type=" + type);
        }

        if (isSelectView && (null != type && !"".equals(type))) {
            throw new RuntimeException("error on filter: " + nameF + " not supported options view=" + view + " and type=" + type);
        }

        if (isCheckView && !XmlConstants.UtilConstants.attrBoolean.equals(type)) {
            throw new RuntimeException("error on filter: " + nameF + " not supported options view=" + view + " and type=" + type);
        }

        if (isRangeView && (XmlConstants.UtilConstants.attrString.equals(type) ||
                XmlConstants.UtilConstants.attrBoolean.equals(type))) {
            throw new RuntimeException("error on filter: " + nameF + " not supported options view=" + view + " and type=" + type);
        }
    }

    private void elementQueryViewValidator(String view, String nameF) {
        if (!XmlConstants.UtilConstants.attrSelect.equals(view)) {
            throw new RuntimeException("error on filter: " + nameF + ", <query> element, they can be used selecting to the option ?SELECT?, in the view field of the filter");
        }
    }

    private void elementConstantClassViewValidator(String view, String nameF) {
        if (!XmlConstants.UtilConstants.attrSelect.equals(view)) {
            throw new RuntimeException("error on filter: " + nameF + ", <constantClass> element, they can be used selecting to the option ?SELECT?, in the view field of the filter");
        }
    }

    private void elementDefaulViewValidator(String view, String nameF) {
        if (XmlConstants.UtilConstants.attrSelect.equals(view)) {
            throw new RuntimeException("error on filter: " + nameF + ", <default> element, they can be used selecting to the option ?TEXT, CHECK, RANGE?, in the view field of the filter");
        }
    }
}
