package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.DateUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.TimeZone;

/**
 * Task message wrapper , which is used to send a mail of notification
 * qhen the status of some task has been changed by any assigned user or
 * creator user.
 *
 * @author Fernando Montaño
 * @version $Id: TaskNotificationMessage.java 9122 2009-04-17 00:31:07Z fernando $
 */

public class TaskNotificationMessage implements Serializable {
    private String messageSubject;
    private String messageHeaderTitle;
    private String taskTitle;
    private String startDateTitle;
    private String expireDateTitle;
    private String statusTitle;
    private String priorityTitle;
    private String modifiedByTitle;
    private String notesTitle;
    private String descriptionTitle;

    private String noExpirationValue;
    private String statusValue;

    private String dateTimePattern;
    private String datePattern;
    private Map statusValues;
    private TimeZone timeZone;
    private static final String COLON = ": ";
    private static final String RETURN = "\n";

    public TaskNotificationMessage(String messageSubject, String messageHeaderTitle, String taskTitle,
                                   String startDateTitle, String expireDateTitle, String statusTitle,
                                   String priorityTitle, String modifiedByTitle, String notesTitle,
                                   String descriptionTitle, String noExpirationValue, Map statusValues,
                                   String dateTimePattern,
                                   String datePattern, TimeZone dateTimeZone) {
        this.messageSubject = messageSubject;
        this.messageHeaderTitle = messageHeaderTitle;
        this.taskTitle = taskTitle;
        this.startDateTitle = startDateTitle;
        this.expireDateTitle = expireDateTitle;
        this.noExpirationValue = noExpirationValue;
        this.statusTitle = statusTitle;
        this.priorityTitle = priorityTitle;
        this.modifiedByTitle = modifiedByTitle;
        this.notesTitle = notesTitle;
        this.descriptionTitle = descriptionTitle;
        this.statusValues = statusValues;
        this.dateTimePattern = dateTimePattern;
        this.datePattern = datePattern;
        this.timeZone = dateTimeZone;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public String getMessageHeaderTitle() {
        return messageHeaderTitle;
    }


    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    /**
     * Process the complete message body
     *
     * @return
     */
    public String getMessageBody(String title, Integer startDate, Integer dueDate, String dueHour, Integer status,
                                 String priority, String modifiedBy, String notes, String description) {

        messageSubject = messageSubject + " " + title;
        StringBuffer message = new StringBuffer(messageHeaderTitle);
        message.append(RETURN)
                .append(RETURN)
                .append(taskTitle)
                .append(COLON)
                .append(title)
                .append(RETURN)
                .append(startDateTitle)
                .append(COLON)
                .append(DateUtils.parseDate(DateUtils.integerToDate(startDate), datePattern))
                .append(RETURN)
                .append(expireDateTitle)
                .append(COLON);
        if (dueDate != null) {
            message.append(DateUtils.parseDate(DateUtils.integerToDate(dueDate), datePattern))
                    .append(" ")
                    .append(dueHour)
                    .append(RETURN);
        } else {
            message.append(noExpirationValue)
                    .append(RETURN);
        }
        message.append(priorityTitle)
                .append(COLON)
                .append(priority)
                .append(RETURN)
                .append(statusTitle)
                .append(COLON)
                .append(statusValues.get(status.toString()))
                .append(RETURN)
                .append(modifiedByTitle)
                .append(COLON)
                .append(modifiedBy)
                .append(RETURN)
                .append(notesTitle)
                .append(COLON)
                .append(RETURN)
                .append(notes)
                .append(RETURN)
                .append(descriptionTitle)
                .append(COLON)
                .append(RETURN)
                .append(description)
                .append(RETURN);

        return new String(message);
    }

}
