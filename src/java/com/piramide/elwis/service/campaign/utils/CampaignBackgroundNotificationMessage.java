package com.piramide.elwis.service.campaign.utils;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import org.joda.time.DateTimeZone;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Util to compose notification email body
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignBackgroundNotificationMessage {
    private static final String SUBJECT_KEY = "CampaignNotification.Msg.subject";
    private static final String BODY_DESCRIPTION_KEY = "CampaignNotification.Msg.bodyDescription";
    private static final String SUMMARY_KEY = "CampaignNotification.Msg.summary";

    //detail
    private static final String TOTALRECIPIENTS_KEY = "CampaignNotification.Msg.totalRecipients";
    private static final String SUCCESS_KEY = "CampaignNotification.Msg.success";
    private static final String FAIL_KEY = "CampaignNotification.Msg.fail";

    private static final String DATE_PATTERN_KEY = "datePattern";
    private static final String DATETIME_PATTERN_KEY = "dateTimePattern";

    private String subject;
    private Long generationTime;
    private Integer totalRecipients;
    private Integer totalSuccess;
    private Integer totalFail;

    private ResourceBundle resourceBundle;
    private Locale locale;
    private DateTimeZone dateTimeZone;


    public CampaignBackgroundNotificationMessage(String isoLanguage) {

        if (isoLanguage != null) {
            resourceBundle = ResourceBundle.getBundle(CampaignConstants.CAMPAIGN_SERVICE_RESOURCES, new Locale(isoLanguage));
            locale = new Locale(isoLanguage);
        } else {
            resourceBundle = ResourceBundle.getBundle(CampaignConstants.CAMPAIGN_SERVICE_RESOURCES);
            locale = Locale.getDefault();
        }

        this.subject = resourceBundle.getString(SUBJECT_KEY);
        this.dateTimeZone = DateTimeZone.getDefault();
    }

    /**
     * Returns the Message to be sent by email.
     *
     * @return the message formatted
     */
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(formatString(BODY_DESCRIPTION_KEY, new Object[]{formatDateTimeValue(generationTime)})).append("\n");
        sb.append("\n").append(resourceBundle.getString(SUMMARY_KEY)).append("\n");

        addMessage(TOTALRECIPIENTS_KEY, totalRecipients, sb);
        addMessage(SUCCESS_KEY, totalSuccess, sb);
        addMessage(FAIL_KEY, totalFail, sb);

        return new String(sb);
    }

    private void addMessage(String key, String value, StringBuilder sb) {
        addKeyValueMessage(key, value, sb);
    }

    private void addMessage(String key, Integer integerValue, StringBuilder sb) {
        addKeyValueMessage(key, integerValue, sb);
    }

    private void addDateMessage(String key, Integer dateAsInteger, StringBuilder sb) {
        if (dateAsInteger != null) {
            addKeyValueMessage(key, formatDateValue(dateAsInteger), sb);
        }
    }

    private void addKeyValueMessage(String key, Object value, StringBuilder sb) {
        if (key != null && value != null) {
            sb.append(formatString(key, new Object[]{value})).append("\n");
        }
    }

    private String formatDateValue(Integer dateAsInteger) {
        String dateValue = null;
        if (dateAsInteger != null) {
            dateValue = DateUtils.parseDate(dateAsInteger, resourceBundle.getString(DATE_PATTERN_KEY));
        }
        return dateValue;
    }

    private String formatDateTimeValue(Long dateAsLong) {
        String dateValue = null;
        if (dateAsLong != null) {
            dateValue = DateUtils.parseDate(dateAsLong, resourceBundle.getString(DATETIME_PATTERN_KEY), dateTimeZone.getID());
        }
        return dateValue;
    }

    /**
     * Format an string with params
     *
     * @param key    the resource key
     * @param params the object params
     * @return formatted string
     */
    private String formatString(String key, Object[] params) {
        MessageFormat formatString = new MessageFormat(resourceBundle.getString(key));
        return formatString.format(params);
    }

    /*
    * Getter and Setter
    */
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(Long generationTime) {
        this.generationTime = generationTime;
    }

    public Integer getTotalRecipients() {
        return totalRecipients;
    }

    public void setTotalRecipients(Integer totalRecipients) {
        this.totalRecipients = totalRecipients;
    }

    public Integer getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(Integer totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public Integer getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(Integer totalFail) {
        this.totalFail = totalFail;
    }

    public DateTimeZone getDateTimeZone() {
        return dateTimeZone;
    }

    public void setDateTimeZone(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }
}
