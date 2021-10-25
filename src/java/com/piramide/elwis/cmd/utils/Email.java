package com.piramide.elwis.cmd.utils;

import javax.mail.internet.MailDateFormat;
import java.io.Serializable;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jan 27, 2005
 * Time: 7:09:35 PM
 * To change this template use File | Settings | File Templates.
 */

public class Email implements Serializable {
    public final static String HTML_MAIL_TYPE = "text/html";
    public final static String TEXT_MAIL_TYPE = "text/plain";
    private Map images;
    private String to;
    private String from;
    private String fromPersonal;
    private String subject;
    private String message;
    private String bodyType;
    private TimeZone timeZone;
    private boolean manyRecipients = false;
    MailDateFormat mailDateFormat = new MailDateFormat();
    private Integer userId = null;

    List attachPaths;

    public Email() {
        images = new HashMap(0);
        mailDateFormat.setTimeZone(TimeZone.getDefault());
    }

    public Email(String to, String from, String subject, String message, String bodyType) {
        this.to = to;
        manyRecipients = (to.indexOf(",") != -1);
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.bodyType = bodyType;
        this.images = new HashMap();
        mailDateFormat.setTimeZone(TimeZone.getDefault());
    }

    public Email(String to, String from, String subject, String message, String bodyType, TimeZone timeZone) {
        this.to = to;
        manyRecipients = (to.indexOf(",") != -1);
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.bodyType = bodyType;
        this.images = new HashMap();
        this.timeZone = timeZone;
        mailDateFormat.setTimeZone(this.timeZone);
    }

    public String getHeaderSentDate() {
        return mailDateFormat.format(new Date());
    }

    public Email(Map images) {
        this.images = images;
        mailDateFormat.setTimeZone(TimeZone.getDefault());
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getImages() {
        return images;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        mailDateFormat.setTimeZone(timeZone);
    }

    public String toString() {
        return "Email{" +
                "to='" + to + "'" +
                ", from='" + from + "'" +
                ", subject='" + subject + "'" +
                ", message='" + message + "'" +
                ", body type='" + bodyType + "'" +
                "}";
    }

    public String getFromPersonal() {
        return fromPersonal;
    }

    public void setFromPersonal(String fromPersonal) {
        this.fromPersonal = fromPersonal;
    }

    public boolean isManyRecipients() {
        return manyRecipients;
    }

    public List getAttachPaths() {
        return attachPaths;
    }

    public void setAttachPaths(List attachPaths) {
        this.attachPaths = attachPaths;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
