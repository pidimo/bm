package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.CommunicationManagerCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Alfacentauro Team
 * <p/>
 * this class help to send the address to registry who new contacts
 *
 * @author miky
 * @version $Id: MailAddressSentForm.java 9438 2009-07-09 16:49:34Z ivan $
 */
public class MailAddressSentForm extends WebmailDefaultForm {

    public void setSelectedMails(Object[] selectedMails) {
        if (selectedMails != null) {
            setDto("selectedMails", Arrays.asList(selectedMails));
        } else {
            setDto("selectedMails", new ArrayList());
        }
    }

    public Object[] getSelectedMails() {
        List addreses = (List) getDto("selectedMails");
        return (addreses != null ? addreses.toArray() : new ArrayList().toArray());
    }


    public void setAddresses(Object[] address) {
        log.debug("Set addresses ....... " + address);
        if (address != null) {
            setDto("addresses", Arrays.asList(address));
        } else {
            setDto("addresses", new ArrayList());
        }
    }

    public Object[] getAddresses() {
        List addreses = (List) getDto("addresses");
        return (addreses != null ? addreses.toArray() : new ArrayList().toArray());
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate MailAddressSentForm.........");
        log.debug("*********************************");
        log.debug("---- > " + this.getDtoMap());
        log.debug("*********************************");
        ActionErrors errors = super.validate(mapping, request);

        List selectedMails = (List) getDto("selectedMails");
        if (null == selectedMails || selectedMails.isEmpty()) {
            errors.add("select", new ActionError("Webmail.mailContact.selectAddress"));
        } else {
            if (null == getDto("mailId") || "".equals(getDto("mailId").toString())) {
                request.setAttribute("mailListDispatched", request.getSession().getAttribute("mailListDispatched"));
            } else {
                Integer mailId = Integer.valueOf(getDto("mailId").toString());
                request.setAttribute("mailId", mailId);

                Boolean saveSendItem = Boolean.valueOf(getDto("saveSendItem").toString());
                request.setAttribute("saveSendItem", saveSendItem);

                ResultDTO myResultDTO = new ResultDTO();
                User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
                CommunicationManagerCmd cmd = new CommunicationManagerCmd();
                cmd.setOp("checkRecipientSent");
                cmd.putParam("mailId", mailId);
                cmd.putParam("companyId", Integer.valueOf(user.getValue("companyId").toString()));
                cmd.putParam("saveSendItems", saveSendItem);
                try {
                    myResultDTO = BusinessDelegate.i.execute(cmd, request);
                } catch (AppLevelException e) {
                    log.debug("Cannot execute CommunicationCmd ... ");
                }

                List mailListDispatched = (List) myResultDTO.get("mailListDispatched");
                if (mailListDispatched.isEmpty()) {
                    List addresses = (List) getDto("addresses");

                    mailListDispatched = new ArrayList();
                    for (Iterator iterator2 = addresses.iterator(); iterator2.hasNext();) {
                        String[] values = iterator2.next().toString().split("<,>"); // {diremail,name,iscontact}
                        Map mailMap = new HashMap(3);
                        mailMap.put("dirEmail", values[0]);
                        mailMap.put("addressName", values[1]);
                        mailMap.put("isContact", values[2]);
                        mailListDispatched.add(mailMap);
                    }
                }
            }
        }
        return errors;
    }

}
