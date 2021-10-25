package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.el.Functions;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * This class verifies if an user ahver permission for compose a mail, and if have an mail account configurated
 *
 * @author Alvaro Sejas
 * @version 4.2.1
 */
public class CommunicationForwardAction extends org.apache.struts.actions.ForwardAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm communicationForm = (DefaultForm) form;

        communicationForm.setDto("dateStart", "");
        String communicationType = (String) communicationForm.getDto("type");
        if (communicationForm.getDto("type") != null && CommunicationTypes.EMAIL.equals(communicationType)) {
            User user = RequestUtils.getUser(request);
            Integer userId = (Integer) user.getValue(Constants.USERID);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);


            MailFormHelper emailHelper = new MailFormHelper();
            Integer emailUserChecker = emailHelper.isValidEmailUser(userId, request);
            if (-1 == emailUserChecker) {
                return mapping.findForward("NoUserMail");
            }

            emailHelper.setDefaultEmailAccount(userId, companyId, request, communicationForm);

            if (null != communicationForm.getDto("mailAccountId")) {
                String actualEmailAccount = communicationForm.getDto("mailAccountId").toString();
                emailHelper.setDefaultSignature(Integer.valueOf(actualEmailAccount), userId, communicationForm, request);
            }

            communicationForm.setDto("dateStart", DateUtils.dateToInteger(new Date()));
            buildEmailAddress(communicationForm, request);
        }

        return super.execute(mapping, communicationForm, request, response);
    }

    private void buildEmailAddress(DefaultForm defaultForm, HttpServletRequest request) {
        String contactId = request.getParameter("contactId");
        String addressId = (String) defaultForm.getDto("addressId");
        String contactPersonId = (String) defaultForm.getDto("contactPersonId");
        String email = (String) defaultForm.getDto("selectedEmail");
        String addressName = Functions.getAddressName(addressId);

        if (!GenericValidator.isBlankOrNull(contactPersonId)) {
            addressName = Functions.getAddressName(contactPersonId);
        }

        List<Integer> addressListIds = new ArrayList<Integer>();

        Map toRecipient = new HashMap();
        toRecipient.put(EmailRecipientHelper.RecipientKey.PERSONAL_NAME.getKey(), addressName);
        toRecipient.put(EmailRecipientHelper.RecipientKey.EMAIL.getKey(), email);
        toRecipient.put(EmailRecipientHelper.RecipientKey.ADDRESS_ID.getKey(), Arrays.asList(Integer.valueOf(contactId)));
        toRecipient.put(EmailRecipientHelper.RecipientKey.CONTACT_PERSON_OF.getKey(), new ArrayList<Integer>());
        if (null != contactPersonId && !"".equals(contactPersonId.trim())) {
            toRecipient.put(EmailRecipientHelper.RecipientKey.ADDRESS_ID.getKey(), Arrays.asList(Integer.valueOf(contactPersonId))); //contactPersonId
            toRecipient.put(EmailRecipientHelper.RecipientKey.CONTACT_PERSON_OF.getKey(), Arrays.asList(Integer.valueOf(contactId)));//addressId
        } else {
            addressListIds.add(Integer.valueOf(contactId));
        }

        List<Map> recipients = new ArrayList<Map>();
        recipients.add(toRecipient);

        EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
        defaultForm.setDto("dynamicHiddens", emailRecipientHelper.getDynamicHiddens(recipients));
        defaultForm.setDto("addressList", emailRecipientHelper.buildIdList(addressListIds));

        String element = "\"" + addressName + "\" <" + email + ">";
        defaultForm.setDto("to", element);
    }
}
