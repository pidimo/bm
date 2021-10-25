package com.piramide.elwis.utils.webmail;

import com.piramide.elwis.utils.WebMailConstants;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ResourceManager {

    ResourceBundle resourceBundle;

    public ResourceManager(String userLanguage) {
        if (null != userLanguage) {
            resourceBundle = ResourceBundle.getBundle(WebMailConstants.WEBMAIL_SERVICE_RESOURCES,
                    new Locale(userLanguage));
        } else {
            resourceBundle = ResourceBundle.getBundle(WebMailConstants.WEBMAIL_SERVICE_RESOURCES);
        }
    }

    public String buildForwardMessageSubject(String subject) {
        return formatString("ForwardMessage.subject", new Object[]{subject});
    }

    public String buildForwardMessageBody(String emailAccount) {
        return formatString("ForwardMessage.body", new Object[]{emailAccount});
    }

    public String buildReplyMessageSubject(String replySubject, String sourceSubject) {
        return formatString("ReplyMessage.subject", new Object[]{replySubject, sourceSubject});
    }

    public Map<String, String> getReplySubjectResourceConstants() {
        Map<String, String> map = new HashMap<String, String>();

        String subjectResourceVal = resourceBundle.getString("ReplyMessage.subject");
        String firstParam = "{0}";
        String secondParam = "{1}";

        int firstIndex = subjectResourceVal.indexOf(firstParam);
        int secondIndex = subjectResourceVal.indexOf(secondParam);

        if (firstIndex != -1 && secondIndex != -1) {

            String containConstant = subjectResourceVal.substring(firstIndex + firstParam.length(), secondIndex);
            String endConstant = subjectResourceVal.substring(secondIndex + secondParam.length());

            map.put("containConstant", containConstant.trim());
            map.put("endConstant", endConstant.trim());
        }

        return map;
    }


    public String buildReplyMessageBody() {
        return formatString("ReplyMessage.body", new Object[]{});
    }

    public String buildReplyFailureMessageSubject() {
        return formatString("ReplyFailureMessage.subject", new Object[]{});
    }

    public String buildReplyFailureMessageBody(String formatedDate, String subject, String maxSizeFormated) {
        return formatString("ReplyFailureMessage.body", new Object[]{formatedDate, subject, maxSizeFormated});
    }

    public String buildReplyFailureMessageOmmited() {
        return formatString("ReplyFailureMessage.omitted", new Object[]{});
    }

    public String buildOriginalMessageTitle() {
        return formatString("Resume.originalMessage.Title", new Object[]{});
    }

    public String buildFromTitle() {
        return formatString("Resume.from.Title", new Object[]{});
    }

    public String buildToTitle() {
        return formatString("Resume.to.Title", new Object[]{});
    }

    public String buildCcTitle() {
        return formatString("Resume.cc.Title", new Object[]{});
    }

    public String buildSubjectTitle() {
        return formatString("Resume.subject.Title", new Object[]{});
    }

    public String buildDateTitle() {
        return formatString("Resume.date.Title", new Object[]{});
    }

    private String formatString(String key, Object[] params) {
        MessageFormat formatString = new MessageFormat(resourceBundle.getString(key));
        return formatString.format(params);
    }
}
