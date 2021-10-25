/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-23 05:13:25 PM $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignTextImg extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImageStoreId();

    void setImageStoreId(Integer imageStoreId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);
}
