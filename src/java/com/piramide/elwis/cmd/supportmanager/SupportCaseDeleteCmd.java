package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.supportmanager.SupportCaseDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Delete logic for sales process
 *
 * @author Fernando Montaño?
 * @version $Id: SupportCaseDeleteCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */

public class SupportCaseDeleteCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(SupportCaseDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SupportCaseDeleteCmd...");
        SupportCaseDTO supportCaseDTO = new SupportCaseDTO(paramDTO);
        try {
            SupportCase supportCase = (SupportCase) EJBFactory.i.findEJB(supportCaseDTO);
            SupportContactHome supportContactHome = (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
            //remove the action related with communications
            log.debug("Removing supportContacts and communications");
            Collection supportContacts = supportContactHome.findAllSupportContactByCase(supportCase.getCaseId());
            for (Iterator iterator = supportContacts.iterator(); iterator.hasNext();) {
                SupportContact supportContact = (SupportContact) iterator.next();
                ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
                contactDeleteCmd.putParam("contactId", supportContact.getContactId());
                contactDeleteCmd.putParam("op", "delete");
                supportContact.remove();
                contactDeleteCmd.executeInStateless(ctx);
            }
            log.debug("Remove attach!!");
            Collection attachs = supportCase.getSupportAttachs();
            for (Iterator iterator = attachs.iterator(); iterator.hasNext();) {
                SupportAttach supportAttach = (SupportAttach) iterator.next();
                supportAttach.remove();
                iterator = attachs.iterator();
            }

            //Delete activitys!!!!
            log.debug("Set null parente");
            Collection activities = supportCase.getActivities();
            for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
                SupportCaseActivity activity = (SupportCaseActivity) iterator.next();
                activity.setParentActivity(null);
            }
            log.debug("Delete activities");
            for (Iterator iterator = activities.iterator(); iterator.hasNext();) {
                SupportCaseActivity activity = (SupportCaseActivity) iterator.next();
                SupportFreeText freeText = activity.getDescriptionText();
                activity.remove();
                log.debug("Remove freetext");
                freeText.remove();
                iterator = activities.iterator();
            }
            SupportFreeText freeText = supportCase.getDescriptionText();
            //supportCase.setDescriptionText(null);
            log.debug("finally removing sales process");
            supportCase.remove();
            freeText.remove();


        } catch (Exception e) {
            ctx.setRollbackOnly();
            log.debug("Error removing the support contact, it seems was deleted by other user");
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            supportCaseDTO.addNotFoundMsgTo(resultDTO);
        }
    }

    private void deleteSupportContact(Integer caseId, Integer contactId) {
        SupportContactHome supportContactHome = (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
        SupportContactPK pk = new SupportContactPK();
        pk.caseId = caseId;
        pk.contactId = contactId;
        try {
            SupportContact deleteSupportContact = supportContactHome.findByPrimaryKey(pk);
            deleteSupportContact.remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
