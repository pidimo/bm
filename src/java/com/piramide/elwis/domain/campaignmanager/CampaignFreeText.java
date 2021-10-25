package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignFreeText.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 28-jul-2004 15:09:36 Yumi Exp $
 */
public interface CampaignFreeText extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getType();

    void setType(Integer freeTextType);

    byte[] getValue();

    void setValue(byte[] freeTextValue);

    Integer getVersion();

    void setVersion(Integer version);
}
