package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Update sales process probability logic
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessUpdateProbabilityCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */

public class SalesProcessUpdateProbabilityCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SalesProcessUpdateProbabilityCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("processId = " + paramDTO.get("processId"));
        log.debug("addressId = " + paramDTO.get("addressId"));
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        SalesProcessHome processHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);

        try {
            Integer maxDateStart = contactHome.selectMaxStartDate((Integer) paramDTO.get("processId"),
                    (Integer) paramDTO.get("addressId"));
            Integer maxContactIdStart = contactHome.selectMaxContactIdByStartDate((Integer) paramDTO.get("processId"),
                    (Integer) paramDTO.get("addressId"), maxDateStart);

            Contact contact = contactHome.findByPrimaryKey(maxContactIdStart);

            SalesProcess process = processHome.findByPrimaryKey(contact.getProcessId());

            process.setProbability(contact.getProbability());


        } catch (FinderException e) {
            log.debug("There is no communications registered to sales process");
        } catch (Exception e) {
            log.error("Cannot be possible to update sales process probability, unexpected error", e);
        }


    }

    public boolean isStateful() {
        return false;
    }
}
