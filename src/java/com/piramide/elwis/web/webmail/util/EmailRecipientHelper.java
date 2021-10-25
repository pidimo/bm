package com.piramide.elwis.web.webmail.util;

import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JavaScriptEncoder;
import com.piramide.elwis.web.common.validator.EmailValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class EmailRecipientHelper {
    private Log log = LogFactory.getLog(EmailRecipientHelper.class);

    private final String RECIPIENT_REGEX = "(\\s*\"?\\s*[\\w\\d\\W&&[^>\"@<]]*\\s*\"?\\s*<?\\s*[\\w\\d\\W&&[^>\"<\\s,]]*\\s*>?\\s*[,|;]?)";

    private final String SEPARATOR_COMMA = ",";
    private final String SEPARATOR_SEMICOLON = ";";

    public enum RecipientKey {
        EMAIL("email"),
        PERSONAL_NAME("personalName"),
        CONTACT_PERSON_OF("contactPersonOf"),
        ADDRESS_ID("addressId");
        private String key;

        RecipientKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    private final String HIDDEN_IDENTIFIER = "addressElement_";
    private final String HIDDEN_ID = "addressElementId";

    public List<Map> buildRecipienStructure(DefaultForm defaultForm, String recipientToParse) {

        List<Map> userIntercfaceRecipients = getRecipients(recipientToParse);

        List<Map> hiddenElements = getDynamicHiddens(defaultForm);
        for (Map uiRecipient : userIntercfaceRecipients) {
            String email = (String) uiRecipient.get(RecipientKey.EMAIL.getKey());
            String personal = (String) uiRecipient.get(RecipientKey.PERSONAL_NAME.getKey());
            if (null == personal) {
                continue;
            }

            for (Map hidden : hiddenElements) {
                String value = (String) hidden.get("value");
                List<String> parsedValue = parseHiddenValue(value);

                String hiddenName = JavaScriptEncoder.unescape(parsedValue.get(0));
                String hiddenEmail = JavaScriptEncoder.unescape(parsedValue.get(1));

                if (email.equals(hiddenEmail) && personal.equals(hiddenName)) {
                    String hiddenAddressId = parsedValue.get(2);
                    String hiddenContactPersonIds = parsedValue.get(3);

                    ((List) uiRecipient.get(RecipientKey.ADDRESS_ID.getKey())).add(Integer.valueOf(hiddenAddressId));

                    if (!"".equals(hiddenContactPersonIds)) {
                        List<String> contactPersonIds = new ArrayList<String>(Arrays.asList(hiddenContactPersonIds.split("(,)")));
                        ((List) uiRecipient.get(RecipientKey.CONTACT_PERSON_OF.getKey())).addAll(changeList(contactPersonIds));
                    }
                }
            }
        }

        return userIntercfaceRecipients;
    }

    private List<String> parseHiddenValue(String value) {
        List<String> parsedValue = new ArrayList<String>(Arrays.asList(value.split("(\\|)")));
        if (parsedValue.size() < 4) {
            parsedValue.add("");
        }
        return parsedValue;
    }

    public List<Map> getRecipients(String recipient) {
        List<String> elements = parseRecipient(recipient);

        List<Map> result = new ArrayList<Map>();
        for (String element : elements) {
            if (!element.contains("<") && !element.contains(">")) {
                result.add(buildMapRecipient(element.replace(",", "").trim(), null));
                continue;
            }

            String email = element.substring(element.indexOf("<") + 1, element.indexOf(">"));
            String personal = element.substring(element.indexOf("\"") + 1, element.lastIndexOf("\""));

            result.add(buildMapRecipient(email, personal));
        }
        return result;
    }

    private Map buildMapRecipient(String email, String personal) {
        return buildMapRecipient(email, personal, null, null);
    }

    public Map buildMapRecipient(String email, String personal, List<Integer> addressIdList, List<Integer> contactPersonOfList) {
        Map recipient = new HashMap();
        recipient.put(RecipientKey.EMAIL.getKey(), email);

        recipient.put(RecipientKey.PERSONAL_NAME.getKey(), null);
        if (null != personal && !"".equals(personal.trim())) {
            recipient.put(RecipientKey.PERSONAL_NAME.getKey(), personal);
        }

        if (addressIdList == null) {
            addressIdList = new ArrayList<Integer>();
        }
        if (contactPersonOfList == null) {
            contactPersonOfList = new ArrayList<Integer>();
        }

        recipient.put(RecipientKey.CONTACT_PERSON_OF.getKey(), contactPersonOfList);
        recipient.put(RecipientKey.ADDRESS_ID.getKey(), addressIdList);

        return recipient;
    }

    public List<Map> getDynamicHiddens(DefaultForm defaultForm) {
        List<Map> hiddenElements = new ArrayList<Map>();

        for (Object object : defaultForm.getDtoMap().entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String key = entry.getKey().toString();
            if (key.contains(HIDDEN_IDENTIFIER)) {
                String value = entry.getValue().toString();
                String code = getDynamicHiddenCode(key);
                Map<String, String> element = new HashMap<String, String>();
                element.put("dtoName", code);
                element.put("hiddenId", HIDDEN_ID);
                element.put("value", value);
                hiddenElements.add(element);
            }
        }

        return hiddenElements;
    }

    public List<Map> getDynamicHiddens(List<Map> recipients) {
        if (null == recipients) {
            return new ArrayList<Map>();
        }

        List<Map> result = new ArrayList<Map>();

        for (Map recipient : recipients) {
            String personalName = "";
            if (null != recipient.get(RecipientKey.PERSONAL_NAME.getKey())) {
                personalName = (String) recipient.get(RecipientKey.PERSONAL_NAME.getKey());
            }

            String email = (String) recipient.get(RecipientKey.EMAIL.getKey());
            List<Integer> addressIdList = (List<Integer>) recipient.get(RecipientKey.ADDRESS_ID.getKey());
            List<Integer> contacpersonOfList = (List<Integer>) recipient.get(RecipientKey.CONTACT_PERSON_OF.getKey());

            String value = Functions.encode(personalName) +
                    "|" + Functions.encode(email) +
                    "|" + addressIdList.get(0) +
                    "|" + buildIdList(contacpersonOfList);
            String dtoName = addressIdList.get(0).toString();
            Map<String, String> element = new HashMap<String, String>();
            element.put("dtoName", dtoName);
            element.put("hiddenId", HIDDEN_ID);
            element.put("value", value);

            result.add(element);
        }
        return result;
    }

    private String getDynamicHiddenCode(String code) {
        return code.replace(HIDDEN_IDENTIFIER, "").trim();
    }

    public List<ActionError> validateDuplicatedEmails(String to, String cc, String bcc) {
        Map<String, List> recipientMap = countEmailOcurrences(to, cc, bcc);

        List<ActionError> errors = new ArrayList<ActionError>();
        for (Object object : recipientMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String key = (String) entry.getKey();
            List ocurrences = (List) entry.getValue();
            if (ocurrences.size() == 1) {
                continue;
            }
            errors.add(new ActionError("EmailRecipient.email.manyOcurrences.error", key));

        }
        return errors;
    }

    private Map<String, List> countEmailOcurrences(String to, String cc, String bcc) {
        List<Map> allRecipients = new ArrayList<Map>();
        allRecipients.addAll(getRecipients(to));
        allRecipients.addAll(getRecipients(cc));
        allRecipients.addAll(getRecipients(bcc));

        Map<String, List> recipientMap = new HashMap<String, List>();

        for (Map recipient : allRecipients) {
            String email = (String) recipient.get(RecipientKey.EMAIL.getKey());
            String personalName = (String) recipient.get(RecipientKey.PERSONAL_NAME.getKey());
            if (null == personalName) {
                personalName = email;
            }

            List ocurrences = recipientMap.get(email);
            if (null == ocurrences) {
                ocurrences = new ArrayList();
            }

            ocurrences.add(personalName);
            recipientMap.put(email, ocurrences);
        }

        /*List<Map> dynamicHiddens = getDynamicHiddens(defaultForm);
        for (Map hidden : dynamicHiddens) {
            String value = (String) hidden.get("value");
            List<String> parsedValue = parseHiddenValue(value);
            String hiddenEmail = Functions.decode(parsedValue.get(1));
            List ocurrences = recipientMap.get(hiddenEmail);
            if(ocurrences.size() == 1)
                continue;

            String code = (String) hidden.get("dtoName");

            defaultForm.getDtoMap().remove(HIDDEN_IDENTIFIER + code);
        }*/

        return recipientMap;
    }

    public List<ActionError> validateEmails(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        List<Map> recipientMaps = getRecipients(recipient);

        List<ActionError> errors = new ArrayList<ActionError>();
        for (Map uiRecipient : recipientMaps) {
            String email = (String) uiRecipient.get(RecipientKey.EMAIL.getKey());

            if (!EmailValidator.i.isValid(email)) {
                errors.add(new ActionError("errors.email", email));
            }
        }
        return errors;
    }

    public List<ActionError> validateEmailFormat(String recipient) {
        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<ActionError>();
        }

        List<String> plainEmailList = splitInputBulkEmails(recipient);
        log.debug("Splited Emails:" + plainEmailList);

        List<ActionError> errors = new ArrayList<ActionError>();
        for (String emailFormat : plainEmailList) {
            if (!isValidFormat(emailFormat)) {
                errors.add(new ActionError("Webmail.compose.personalNameError", emailFormat));
            }
        }

        return errors;
    }

    private List<String> splitInputBulkEmails(String bulkEmails) {
        List<String> plainEmailList = new ArrayList<String>();
        if (bulkEmails != null) {
            plainEmailList = applyCommaRegExpression(bulkEmails);
        }
        return plainEmailList;
    }

    private List<String> applyCommaRegExpression(String bulkEmails) {
        List<String> plainEmailList = new ArrayList<String>();
        if (bulkEmails != null) {

            Matcher matcher = Pattern.compile(RECIPIENT_REGEX, Pattern.CASE_INSENSITIVE).matcher(bulkEmails);
            while (safeFind(matcher)) {
                String plainEmail = matcher.group(1);

                if ("".equals(plainEmail.trim())) {
                    continue;
                }

                plainEmail = plainEmail.trim();
                //remove last ',' or ';' character
                if (plainEmail.endsWith(SEPARATOR_COMMA) || plainEmail.endsWith(SEPARATOR_SEMICOLON)) {
                    plainEmail = plainEmail.substring(0, plainEmail.length() - 1);
                    plainEmail = plainEmail.trim();
                }

                plainEmailList.add(plainEmail);
            }
        }

        return plainEmailList;
    }

    public String buildIdList(List<Integer> listOfIdentifiers) {
        String result = "";
        if (null == listOfIdentifiers || listOfIdentifiers.isEmpty()) {
            return result;
        }

        for (int i = 0; i < listOfIdentifiers.size(); i++) {
            result += listOfIdentifiers.get(i);
            if (i < listOfIdentifiers.size() - 1) {
                result += ",";
            }
        }

        return result;
    }

    private List<Integer> changeList(List<String> list) {
        List<Integer> result = new ArrayList<Integer>();

        for (String element : list) {
            Integer id;
            try {
                id = Integer.valueOf(element);
            } catch (NullPointerException e) {
                continue;
            } catch (NumberFormatException e) {
                continue;
            }
            result.add(id);
        }
        return result;
    }

    private List<String> parseRecipient(String recipient) {
        recipient = addWhiteSpace(recipient);

        if (GenericValidator.isBlankOrNull(recipient)) {
            return new ArrayList<String>();
        }

        List<String> plainEmailList = splitInputBulkEmails(recipient);

        List<String> result = new ArrayList<String>();
        for (String emailFormat : plainEmailList) {
            if (isValidFormat(emailFormat)) {
                result.add(emailFormat.trim());
            }
        }

        return result;
    }

    private String addWhiteSpace(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        final String regex = "((,)(\\S))";
        Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(stringBuilder);
        while (safeFind(matcher)) {
            stringBuilder.replace(matcher.start(2), matcher.end(2), ", ");
        }

        return stringBuilder.toString();
    }

    private boolean isValidFormat(String format) {
        final String quoteSymbol = "\"";
        final String greaterThanSymbol = ">";
        final String lessThanSymbol = "<";
        final String empty = "";
        final String arrobaSymbol = "@";
        final String commaSymbol = ",";
        final String space = " ";

        String cad = format.trim();

        //remove last ',' character
        if (cad.lastIndexOf(",") == cad.length() - 1) {
            cad = cad.substring(0, cad.length() - 1);
            cad = cad.trim();
        }

        if (cad.indexOf(quoteSymbol) == -1 &&
                cad.indexOf(greaterThanSymbol) == -1 &&
                cad.indexOf(lessThanSymbol) == -1 &&
                cad.indexOf(empty) == 0 &&
                cad.contains(arrobaSymbol) &&
                !cad.contains(commaSymbol) &&
                !cad.contains(space)) {

            //this an simple email
            return true;
        }

        if (!cad.startsWith(quoteSymbol)) {
            return false;
        }

        if (cad.lastIndexOf(quoteSymbol) == 0) {
            return false;
        }

        if (cad.indexOf(greaterThanSymbol) != cad.length() - 1) {
            return false;
        }

        if (cad.indexOf(lessThanSymbol) == -1) {
            return false;
        }

        return true;
    }

    private static boolean safeFind(Matcher matcher) {
        try {
            return matcher.find();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
