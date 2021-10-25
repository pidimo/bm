package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.domain.supportmanager.SupportContactHome;
import com.piramide.elwis.domain.supportmanager.SupportContactPK;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportContactCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(SupportContactCmd.class);
    SessionContext ctx;

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        this.ctx = ctx;
        log.debug("Executing  ... SupportCaseActivityUpdateCmd     .....");
    }

    public void create(Integer caseId, Integer companyId, Integer contactId) throws AppLevelException {
        create(null, caseId, companyId, contactId);
    }

    public void create(Integer activityId, Integer caseId, Integer companyId, Integer contactId) throws AppLevelException {
        SupportContactHome contactHome = (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
        try {
            contactHome.create(activityId, caseId, companyId, contactId);
            log.debug("Created description....");
        } catch (CreateException e) {
            throw new AppLevelException(e.getMessage());
        }
    }


    public SupportContact readByContact(Integer caseId, Integer contacId) throws AppLevelException {
        SupportContactHome contactHome = (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
        SupportContactPK pk = new SupportContactPK();
        SupportContact supportContact = null;
        pk.caseId = caseId;
        pk.contactId = contacId;
        try {
            supportContact = contactHome.findByPrimaryKey(pk);
            return supportContact;
        } catch (FinderException e) {
            //throw new AppLevelException(e.getMessage());
            log.debug(" communication not found ..." + e.getMessage());
            return null;
        }
    }

    public List readByActivity(Integer caseId, Integer activityId) throws AppLevelException {
        SupportContactHome contactHome =
                (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);

        List supportContacts;
        try {
            supportContacts = (List) contactHome.findBySupportCaseActivity(activityId, caseId);
            if (supportContacts.isEmpty()) {
                return null;
            }

            return supportContacts;
        } catch (FinderException e) {
            log.debug("-> Read SupportContacts for caseId=" + caseId + ", activityId=" + activityId + " FAIL");
        }
        return null;
    }

    /* public Integer deleteByActivity(Integer caseId, Integer activityId) throws AppLevelException {
        try {
            SupportContact supportContact = readByActivity(caseId, activityId);
            Integer contactId = supportContact.getContactId();
            supportContact.remove();
            return contactId;
        } catch (RemoveException e) {
            throw new AppLevelException(e.getMessage());
        }
    }*/

    public void deleteByCase(Integer caseId, Integer contacId) throws AppLevelException {
        SupportContact supportContact = readByContact(caseId, contacId);
        try {
            supportContact.remove();
        } catch (RemoveException e) {
            throw new AppLevelException(e.getMessage());
        }
    }

    public void setCtx(SessionContext ctx) {
        this.ctx = ctx;
    }

    public boolean isStateful() {
        return false;
    }
}