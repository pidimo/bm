package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: 08-jun-2007
 * Time: 11:07:21
 * To change this template use File | Settings | File Templates.
 */

public class TaskCommunicationReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command with Operation = " + getOp());
        log.debug("contactPersonId to read = " + paramDTO.get("contactPersonId"));
        log.debug("addressId = " + paramDTO.get("addressId"));
        if (paramDTO.get("withReferences") != null) {// checking if is referenced
            log.debug("with references = true");
            ContactPersonDTO dto = new ContactPersonDTO(paramDTO);
            IntegrityReferentialChecker.i.check(dto, resultDTO);
        }
        if (resultDTO.isFailure()) {
            return;
        } else {

            if (!"".equals(paramDTO.get("contactPersonId")) && paramDTO.get("contactPersonId") != null) {

                AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                        new Integer(paramDTO.get("contactPersonId").toString()));
                ContactPersonDTO contactPersonDTO = new ContactPersonDTO(paramDTO);
                contactPersonDTO.setPrimKey(pK);
                ContactPerson contactPerson = (ContactPerson) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, contactPersonDTO, resultDTO);
                try {
                    Address address = addressHome.findByPrimaryKey(contactPerson.getContactPersonId());
                    resultDTO.put("contactId", paramDTO.get("addressId"));
                    resultDTO.put("contactPersonName", address.getName());
                } catch (FinderException e) {
                    log.debug("... contactPerson not found ...");
                    resultDTO.setResultAsFailure();
                    resultDTO.setForward("Fail");
                }
            } else {
                resultDTO.put("contactId", paramDTO.get("addressId"));
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}

