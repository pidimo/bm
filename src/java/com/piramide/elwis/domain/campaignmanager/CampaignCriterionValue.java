/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterionValue.java 10314 2013-02-06 19:04:04Z miguel ${NAME}.java, v 2.0 28-jul-2004 16:55:41 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface CampaignCriterionValue extends EJBLocalObject {
    Integer getCampCriterionValueId();

    void setCampCriterionValueId(Integer campCriterionValueId);

    Integer getTableId();

    void setTableId(Integer tableId);

    String getDescriptionKey();

    void setDescriptionKey(String descriptionKey);

    String getField();

    void setField(String field);

    Integer getFieldType();

    void setFieldType(Integer fieldType);

    String getFieldName();

    void setFieldName(String fieldName);

    String getTableName();

    void setTableName(String tableName);

    Collection getCampaignCriterion();

    void setCampaignCriterion(Collection campaignCriterion);

    String getRelationField();

    void setRelationField(String relationField);

    String getContactPersonField();

    void setContactPersonField(String contactPersonField);
}
