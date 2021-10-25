package com.piramide.elwis.cmd.campaignmanager.util;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignRecipientWrapper {
    private Integer campaignId;
    private Integer campaignContactId;
    private Integer sentLogContactId;

    private Integer addressId;
    private Integer contactPersonId;
    private Integer activityId;
    private Integer userId;
    private RecipientType recipientType;

    public static enum RecipientType {
        CAMPAIGN_CONTACT(1),
        SENT_LOG_CONTACT(2);

        private final int constant;

        RecipientType(int val) {
            constant = val;
        }

        public int getConstant() {
            return constant;
        }

        public boolean equal(Integer constant) {
            return this.constant == constant;
        }
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public Integer getCampaignContactId() {
        return campaignContactId;
    }

    public void setCampaignContactId(Integer campaignContactId) {
        this.campaignContactId = campaignContactId;
    }

    public Integer getSentLogContactId() {
        return sentLogContactId;
    }

    public void setSentLogContactId(Integer sentLogContactId) {
        this.sentLogContactId = sentLogContactId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getContactPersonId() {
        return contactPersonId;
    }

    public void setContactPersonId(Integer contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(RecipientType recipientType) {
        this.recipientType = recipientType;
    }
}
