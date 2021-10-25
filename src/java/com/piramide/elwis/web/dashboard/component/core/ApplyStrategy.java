package com.piramide.elwis.web.dashboard.component.core;

/**
 * @author : ivan
 */
public class ApplyStrategy {
    private AbstractStrategy strategy;

    public ApplyStrategy(AbstractStrategy strategy) {
        this.strategy = strategy;
    }

    public String applyStrategy() {
        return strategy.buildComponent();
    }
}
