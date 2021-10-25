/**
 * Created by IntelliJ IDEA.
 * @author: ivan
 * Date: 23-10-2006
 * Time: 03:25:58 PM
 * To change this template use File | Settings | File Templates.
 */
package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

public interface CampaignType extends EJBLocalObject {
    Integer getCampaignTypeId();

    void setCampaignTypeId(Integer campaignTypeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getTitle();

    void setTitle(String title);

    Integer getVersion();

    void setVersion(Integer version);
}
