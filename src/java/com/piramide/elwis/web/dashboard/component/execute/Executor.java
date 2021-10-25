package com.piramide.elwis.web.dashboard.component.execute;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.util.ResourceReader;

/**
 * @author : ivan
 */
public class Executor {
    public static DataProcessor getDataProcessor(int componentId) {
        DataProcessor source;
        Component component = Builder.i.findComponentById(componentId);
        String className = component.getDashBoardConfiguration().getDataProcessor();
        source = (DataProcessor) ResourceReader.getClassInstance(className);
        return source;
    }

    public static FilterPreProcessor getFilterPreProcessor(int componentId) {
        FilterPreProcessor processor = null;
        Component component = Builder.i.findComponentById(componentId);
        String className = component.getComponentConfiguration().getFilterProcessor();
        if (null != className && !"".equals(className.trim())) {
            processor = (FilterPreProcessor) ResourceReader.getClassInstance(className);
        }

        return processor;
    }

    public static ConditionEvaluator getConditionEvaluator(Component component) {
        ConditionEvaluator evaluator = null;

        String className = component.getDashBoardConfiguration().getConditionEvaluator();
        if (null != className && !"".equals(className)) {
            evaluator = (ConditionEvaluator) ResourceReader.getClassInstance(className);
        }

        return evaluator;
    }

    public static String getUIProcessorClassName(int componentId) {
        String className = null;
        Component component = Builder.i.findComponentById(componentId);
        className = component.getComponentConfiguration().getIuImplementationClass();
        return className;
    }

    public static String getUIProcessorType(int componentId) {
        String uiProcessorType = null;
        Component component = Builder.i.findComponentById(componentId);
        uiProcessorType = component.getComponentConfiguration().getIuImplementation();
        return uiProcessorType;
    }


    public static Converter getConverter(String clazz) {
        Converter c;
        c = (Converter) ResourceReader.getClassInstance(clazz);
        return c;
    }
}
