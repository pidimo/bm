package com.piramide.elwis.web.dashboard.component.configuration.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 */
public class Configuration {
    private List<Option> options = new ArrayList<Option>();
    private List<Converter> converters = new ArrayList<Converter>();
    private final String DATA_PROCESSOR = "DATA_PROCESSOR";
    private final String PERSISTENCE_PROCESSOR = "PERSISTENCE_PROCESSOR";
    private final String FILTER_PREPROCESSOR = "FILTER_PREPROCESSOR";
    private final String IU_IMPLEMENTATION = "IU_IMPLEMENTATION";
    private final String IU_IMPLEMENTATION_CLASS = "IU_IMPLEMENTATION_CLASS";
    private final String ACCESS_URL = "ACCESS_URL";
    private final String COMPONENT_URL = "COMPONENT_URL";
    private final String CONDITION_EVALUATOR = "CONDITON_EVALUATOR";

    public String getConditionEvaluator() {
        return getOptionValue(CONDITION_EVALUATOR);
    }

    public String getDataProcessor() {
        return getOptionValue(DATA_PROCESSOR);
    }

    public String getPersistenceProcessor() {
        return getOptionValue(PERSISTENCE_PROCESSOR);
    }

    public String getFilterProcessor() {
        return getOptionValue(FILTER_PREPROCESSOR);
    }

    public String getIuImplementation() {
        return getOptionValue(IU_IMPLEMENTATION);
    }

    public String getIuImplementationClass() {
        return getOptionValue(IU_IMPLEMENTATION_CLASS);
    }

    public Option getAccessUrl() {
        return getOption(ACCESS_URL);
    }

    public void setConditionEvaluator(String value) {
        Option opt = new Option(CONDITION_EVALUATOR, value);
        addOption(opt);
    }

    public void setDataProcessor(String value) {
        Option opt = new Option(DATA_PROCESSOR, value);
        addOption(opt);
    }

    public void setPersistenceProcessor(String value) {
        Option opt = new Option(PERSISTENCE_PROCESSOR, value);
        addOption(opt);
    }

    public void setFilterPreprocessor(String value) {
        Option opt = new Option(FILTER_PREPROCESSOR, value);
        addOption(opt);
    }

    public void setIuImplementation(String value) {
        Option opt = new Option(IU_IMPLEMENTATION, value);
        addOption(opt);
    }

    public void setIuImplementationClass(String value) {
        Option opt = new Option(IU_IMPLEMENTATION_CLASS, value);
        addOption(opt);
    }

    public void setAccessUrl(String url, List<Parameter> parameters) {
        Option opt = new Option(ACCESS_URL, url);
        opt.setParams(parameters);
        addOption(opt);
    }

    private Option getOption(String optionName) {
        for (Option opt : options) {
            String optName = opt.getName();
            if (optionName.equals(optName)) {
                return opt;
            }
        }
        return null;
    }

    private String getOptionValue(String optionName) {
        Option opt = getOption(optionName);
        if (null != opt) {
            return opt.getValue();
        } else {
            return null;
        }
    }

    private void addOption(Option opt) {
        options.add(opt);
    }

    public List<Converter> getConverters() {
        return converters;
    }

    public void setConverters(List<Converter> converters) {
        this.converters = converters;
    }

    public Converter getConverter(String name) {
        for (Converter c : converters) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    public void setComponentUrl(String url, List<Parameter> paremeters) {
        Option opt = new Option(COMPONENT_URL, url);
        opt.setParams(paremeters);
        addOption(opt);
    }

    public Option getComponentUrl() {
        return getOption(COMPONENT_URL);
    }

    public String toString() {
        return options.toString();
    }
}
