package com.piramide.elwis.web.webmail.util;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.common.validator.EmailValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Util to manage token field recipients in form, use to validate, rewrite and build cmd recipient structure
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class TokenFieldEmailRecipientHelper {
    private Log log = LogFactory.getLog(this.getClass());

    private List<Map> toList;
    private List<Map> ccList;
    private List<Map> bccList;

    public TokenFieldEmailRecipientHelper(DefaultForm defaultForm) {
        toList = new ArrayList<Map>();
        ccList = new ArrayList<Map>();
        bccList = new ArrayList<Map>();

        List<String> identifierList = findTokenFieldIdentifierList(defaultForm);

        readTokenFieldFormData(defaultForm, identifierList);

        log.debug("token field TO:" + toList);
        log.debug("token field CC:" + ccList);
        log.debug("token field BCC:" + bccList);
    }

    private List<String> findTokenFieldIdentifierList(DefaultForm defaultForm) {
        List<String> identifierList = new ArrayList<String>();
        Map formDtoMap = defaultForm.getDtoMap();

        for (Iterator iterator1 = formDtoMap.keySet().iterator(); iterator1.hasNext(); ) {
            String key = (String) iterator1.next();
            if (key.startsWith("tokenFieldIdentifier_")) {
                String tokenIdentifier = (String) formDtoMap.get(key);
                if (!GenericValidator.isBlankOrNull(tokenIdentifier)) {
                    identifierList.add(tokenIdentifier);
                }
            }
        }
        return identifierList;
    }

    private void readTokenFieldFormData(DefaultForm defaultForm, List<String> identifierList) {
        if (identifierList != null) {
            for (String tokenIdentifier : identifierList) {
                Map tokenMap = readTokenFieldRecipient(defaultForm, tokenIdentifier);

                if (tokenMap != null) {
                    if (tokenIdentifier.startsWith(WebMailConstants.TokenFieldType.TO.getConstant())) {
                        toList.add(tokenMap);
                    } else if (tokenIdentifier.startsWith(WebMailConstants.TokenFieldType.CC.getConstant())) {
                        ccList.add(tokenMap);
                    } else if (tokenIdentifier.startsWith(WebMailConstants.TokenFieldType.BCC.getConstant())) {
                        bccList.add(tokenMap);
                    }
                }
            }
        }
    }

    private Map readTokenFieldRecipient(DefaultForm defaultForm, String tokenIdentifier) {
        Map map = null;
        if (tokenIdentifier != null) {
            String email = (String) defaultForm.getDto("tokenEmail_" + tokenIdentifier);
            String contactName = (String) defaultForm.getDto("tokenContactName_" + tokenIdentifier);
            String addressId = (String) defaultForm.getDto("tokenAddressId_" + tokenIdentifier);
            String contactPersonOfId = (String) defaultForm.getDto("tokenContactPersonOfId_" + tokenIdentifier);

            map = composeTokenFieldMap(email, contactName, addressId, contactPersonOfId);
        }
        return map;
    }

    public static Map composeTokenFieldMap(String email, String contactName, String addressId, String contactPersonOfId) {
        Map map = null;
        if (!GenericValidator.isBlankOrNull(email)) {
            map = new HashMap();
            map.put("email", email);
            map.put("contactName", (!GenericValidator.isBlankOrNull(contactName)) ? contactName : email);
            map.put("addressId", addressId);
            map.put("contactPersonOfId", contactPersonOfId);
        }
        return map;
    }

    public List<ActionError> validateEmails() {
        List<ActionError> errors = validateEmails(getToList());
        errors.addAll(validateEmails(getCcList()));
        errors.addAll(validateEmails(getBccList()));
        return errors;
    }

    private List<ActionError> validateEmails(List<Map> tokenRecipientMapList) {
        List<ActionError> errors = new ArrayList<ActionError>();

        if (tokenRecipientMapList != null) {
            for (Map tokenMap : tokenRecipientMapList) {
                String email = (String) tokenMap.get("email");

                if (!EmailValidator.i.isValid(email)) {
                    errors.add(new ActionError("errors.email", email));
                }
            }
        }
        return errors;
    }

    public List<ActionError> validateDuplicatedEmails() {
        List<ActionError> errors = new ArrayList<ActionError>();

        Map<String, List> emailsMap = countEmailOcurrences();

        for (String email : emailsMap.keySet()) {
            List ocurrences = emailsMap.get(email);
            if (ocurrences.size() > 1) {
                errors.add(new ActionError("EmailRecipient.tokenField.email.manyOcurrences.error", email, composeEmailOccurrences(ocurrences)));
            }
        }
        return errors;
    }

    private Map<String, List> countEmailOcurrences() {
        List<Map> allRecipients = new ArrayList<Map>();
        allRecipients.addAll(getToList());
        allRecipients.addAll(getCcList());
        allRecipients.addAll(getBccList());

        Map<String, List> emailsMap = new HashMap<String, List>();

        for (Map tokenMap : allRecipients) {
            String email = (String) tokenMap.get("email");
            String contactName = (String) tokenMap.get("contactName");

            List ocurrences = emailsMap.get(email);
            if (null == ocurrences) {
                ocurrences = new ArrayList();
            }

            ocurrences.add(contactName);
            emailsMap.put(email, ocurrences);
        }
        return emailsMap;
    }

    private String composeEmailOccurrences(List<String> occurrences) {
        String emailOccurrences = "";
        for (String conctacName : occurrences) {
            if (!emailOccurrences.isEmpty()) {
                emailOccurrences += " ; ";
            }
            emailOccurrences += conctacName;
        }
        return emailOccurrences;
    }

    public void buildRecipientStructure(DefaultForm defaultForm) {

        Map recipients = new HashMap();

        List<Map> toRecipient = buildRecipientStructureTO();
        List<Map> ccRecipient = buildRecipientStructureCC();
        List<Map> bccRecipient = buildRecipientStructureBCC();

        if (!toRecipient.isEmpty()) {
            recipients.put("to", toRecipient);
        }
        if (!ccRecipient.isEmpty()) {
            recipients.put("cc", ccRecipient);
        }
        if (!bccRecipient.isEmpty()) {
            recipients.put("bcc", bccRecipient);
        }

        defaultForm.setDto("recipients", recipients);
    }

    public List<Map> buildRecipientStructureTO() {
        return buildRecipientStructure(getToList());
    }

    public List<Map> buildRecipientStructureCC() {
        return buildRecipientStructure(getCcList());
    }

    public List<Map> buildRecipientStructureBCC() {
        return buildRecipientStructure(getBccList());
    }

    private List<Map> buildRecipientStructure(List<Map> tokenRecipientMapList) {
        List<Map> recipientList = new ArrayList<Map>();

        if (tokenRecipientMapList != null) {
            for (Map tokenMap : tokenRecipientMapList) {
                String email = (String) tokenMap.get("email");
                String contactName = (String) tokenMap.get("contactName");
                Integer addressId = getIntegerValue((String) tokenMap.get("addressId"));
                Integer contactPersonOfId = getIntegerValue((String) tokenMap.get("contactPersonOfId"));

                List<Integer> addressIdList = new ArrayList<Integer>();
                List<Integer> contactPersonOfList = new ArrayList<Integer>();

                if (addressId != null) {
                    addressIdList.add(addressId);
                    if (contactPersonOfId != null) {
                        contactPersonOfList.add(contactPersonOfId);
                    }
                }

                //build recipient structure to send the cmd
                EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
                Map recipientMap = emailRecipientHelper.buildMapRecipient(email, contactName, addressIdList, contactPersonOfList);

                recipientList.add(recipientMap);
            }
        }
        return recipientList;
    }

    private Integer getIntegerValue(String num) {
        Integer value = null;
        if (num != null) {
            try {
                value = Integer.valueOf(num);
            } catch (NumberFormatException e) {
                value = null;
            }
        }
        return value;
    }

    public void rewriteTokenFieldRecipients(HttpServletRequest request) {
        request.setAttribute("toTokenFieldMapList", getToList());
        request.setAttribute("ccTokenFieldMapList", getCcList());
        request.setAttribute("bccTokenFieldMapList", getBccList());
    }

    public boolean existTokenFieldRecipients() {
        return !getToList().isEmpty() || !getCcList().isEmpty() || !getBccList().isEmpty();
    }

    public List<Map> getToList() {
        return toList;
    }

    public List<Map> getCcList() {
        return ccList;
    }

    public List<Map> getBccList() {
        return bccList;
    }
}
