package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Fernando Montaño
 * @version $Id: SalesProcessEvaluationCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */


public class SalesProcessEvaluationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SalesProcessEvaluationCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("processId to read = " + paramDTO.get("processId"));
        List result = new LinkedList();

        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);

        try {
            Collection contacts = contactHome.findByProcessId(new Integer(paramDTO.get("processId").toString()));
            ContactDTO contactDTO = null;
            for (Iterator iterator = contacts.iterator(); iterator.hasNext();) {
                Contact contactBean = (Contact) iterator.next();
                contactDTO = new ContactDTO();
                contactDTO.put("date", DateUtils.integerToDate(contactBean.getDateStart()));
                contactDTO.put("probability", contactBean.getProbability());
                contactDTO.put("processId", contactBean.getProcessId());
                result.add(contactDTO);
            }
            resultDTO.put("resultList", result);


        } catch (FinderException e) {
            //ignore
        }

    }

    public boolean isStateful() {
        return false;
    }
}