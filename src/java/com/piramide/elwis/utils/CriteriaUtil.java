package com.piramide.elwis.utils;

/**
 * Class that contains the values to build on the criteria tree.
 * This utility is used for the arrangement in the tree.
 *
 * @author yumi
 */

public class CriteriaUtil {

    private Integer type;
    private Integer categoryId;
    private Integer criteriaId;
    private Integer criteriaType;
    private String descriptionKey;

    public CriteriaUtil(Integer type, Integer categoryId, Integer criteriaId, Integer criteriaType, String descriptionKey) {
        this.type = type;
        this.categoryId = categoryId;
        this.criteriaId = criteriaId;
        this.criteriaType = criteriaType;
        this.descriptionKey = descriptionKey;
    }

    public CriteriaUtil(Integer criteriaType, String descriptionKey) {
        this.descriptionKey = descriptionKey;
        this.criteriaType = criteriaType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Integer criteriaId) {
        this.criteriaId = criteriaId;
    }

    public Integer getCriteriaType() {
        return criteriaType;
    }

    public void setCriteriaType(Integer criteriaType) {
        this.criteriaType = criteriaType;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public void setDescriptionKey(String descriptionKey) {
        this.descriptionKey = descriptionKey;
    }

    public String showValues() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" ... type           ...." + type + "\n");
        buffer.append(" ... descriptionKey ...." + descriptionKey + "\n");
        buffer.append(" ... criteriaType   ...." + criteriaType + "\n");
        buffer.append(" ... categoryId     ...." + categoryId + "\n");
        buffer.append(" ... criteriaId     ...." + criteriaId + "\n");
        return buffer.toString();
    }
}
