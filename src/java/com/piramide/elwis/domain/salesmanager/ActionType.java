package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionType.java 12682 2017-05-22 22:58:15Z miguel ${NAME}.java, v 2.0 14-01-2005 10:41:23 AM Fernando Montaño Exp $
 */


public interface ActionType extends EJBLocalObject {
    Integer getActionTypeId();

    void setActionTypeId(Integer actionTypeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getProbability();

    void setProbability(Integer probability);

    Integer getSequence();

    void setSequence(Integer sequence);

    String getActionTypeName();

    void setActionTypeName(String actionTypeName);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);
}
