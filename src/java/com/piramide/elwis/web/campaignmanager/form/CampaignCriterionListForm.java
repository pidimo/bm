package com.piramide.elwis.web.campaignmanager.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Dec 7, 2004
 * Time: 10:48:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignCriterionListForm extends SearchForm {

    private Log log = LogFactory.getLog(com.piramide.elwis.web.campaignmanager.form.CampaignCriterionListForm.class);


    private Integer addressType;
    private Integer contactType;
    private Boolean isDouble;
    private Boolean includePartner;
    private Boolean deletePrevius;
    private Integer totalHits;
    private Integer hasEmail;
    private Integer hasEmailTelecomType;


    public Boolean getDeletePrevius() {
        return deletePrevius;
    }

    public void setDeletePrevius(Boolean deletePrevius) {
        this.deletePrevius = deletePrevius;
    }

    private Map dto = new HashMap();

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }

    public Integer getAddressType() {
        return addressType;
    }

    public void setAddressType(Integer addressType) {
        this.addressType = addressType;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Boolean getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(Boolean aDouble) {
        isDouble = aDouble;
    }

    public Boolean getIncludePartner() {
        return includePartner;
    }

    public void setIncludePartner(Boolean includePartner) {
        this.includePartner = includePartner;
    }

    public Integer getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(Integer totalHits) {
        this.totalHits = totalHits;
    }

    public Integer getHasEmail() {
        return hasEmail;
    }

    public void setHasEmail(Integer hasEmail) {
        this.hasEmail = hasEmail;
    }

    public Integer getHasEmailTelecomType() {
        return hasEmailTelecomType;
    }

    public void setHasEmailTelecomType(Integer hasEmailTelecomType) {
        this.hasEmailTelecomType = hasEmailTelecomType;
    }
}
