package com.piramide.elwis.web.dashboard.component.execute;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Condition;

import java.util.List;
import java.util.Map;

/**
 * @author ivan
 */
public abstract class ConditionEvaluator {
    public abstract String evaluate(Map data, List<Condition> conditions, String componentName);
}
