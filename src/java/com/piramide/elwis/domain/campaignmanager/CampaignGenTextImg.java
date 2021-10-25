/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-26 05:54:58 PM $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignGenTextImg extends EJBLocalObject {
    Integer getCampaignGenTextId();

    void setCampaignGenTextId(Integer campaignGenTextId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getImageStoreId();

    void setImageStoreId(Integer imageStoreId);
}
