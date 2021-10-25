package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 */
public class Column implements Prototype {
    private Log log = LogFactory.getLog(this.getClass());

    private int id;
    private int position;
    private String name;
    private String resourceKey;
    private String order;
    private Boolean defaultColumn = false;
    private Boolean accessColumn = false;
    private String propertyName = null;
    private String patternKey;
    private String converter = null;
    private String size;
    private boolean inverseOrder;

    List<Constant> constants = new ArrayList<Constant>();
    List<Condition> conditions = new ArrayList<Condition>();


    public Column() {
    }

    public Column(int id, String name, String order) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.accessColumn = false;
        this.propertyName = "";

    }

    public Column(Column col) {
        this.id = col.getId();
        this.name = col.getName();
        this.resourceKey = col.getResourceKey();
        this.order = col.getOrder();
        this.defaultColumn = col.isDefaultColumn();
        this.setAccessColumn(col.getAccessColumn());
        this.setPropertyName(col.getPropertyName());
        this.setConverter(col.getConverter());
        this.setConstants(col.getConstants());
        this.setSize(col.getSize());
        this.setInverseOrder(col.isInverseOrder());
        this.setPatternKey(col.getPatternKey());
    }

    public int getId() {
        return id;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public boolean isOrderable() {
        return null != order && !"".equals(order);
    }

    public boolean isDefaultColumn() {
        return defaultColumn;
    }

    public void setDefaultColumn(Boolean defaultColumn) {
        this.defaultColumn = defaultColumn;
    }

    public Boolean getAccessColumn() {
        return accessColumn;
    }

    public void setAccessColumn(Boolean accessColumn) {
        this.accessColumn = accessColumn;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resouceKey) {
        this.resourceKey = resouceKey;
    }

    public String getPatternKey() {
        return patternKey;
    }

    public void setPatternKey(String patternKey) {
        this.patternKey = patternKey;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public List<Constant> getConstants() {
        return constants;
    }

    public void setConstants(List<Constant> constants) {
        this.constants = constants;
    }

    public void addConstant(Constant c) {
        constants.add(c);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Constant getConstant(String value) {
        for (Constant c : constants) {
            if (c.getValue().equals(value)) {
                return c;
            }
        }
        return null;
    }


    public boolean isInverseOrder() {
        return inverseOrder;
    }

    public void setInverseOrder(boolean inverseOrder) {
        this.inverseOrder = inverseOrder;
    }

    public String toString() {
        return "Column(id=" + id + " name=" + name + " size=" + size + " order=" + order + ")";
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }


    public Object clone() {
        Column clone = null;
        try {
            clone = (Column) super.clone();
            List<Condition> cloneConditions = new ArrayList<Condition>();
            for (Condition condition : conditions) {
                Condition cloneCondition = (Condition) condition.clone();
                cloneConditions.add(cloneCondition);
            }
            clone.setConditions(cloneConditions);

            List<Constant> cloneConstants = new ArrayList<Constant>();
            for (Constant constant : constants) {
                Constant cloneConstant = (Constant) constant.clone();
                cloneConstants.add(cloneConstant);
            }
            clone.setConstants(cloneConstants);

        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Column' object ", e);
        }
        return clone;
    }
}
