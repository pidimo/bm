package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Execute delete address business logic.
 *
 * @author Fernando Montaño
 * @version $Id: AddressDeleteCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AddressDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command with Operation = " + getOp());
        log.debug("addressId to read = " + paramDTO.get("addressId"));

        AddressDTO addressDTO = new AddressDTO(paramDTO);
        IntegrityReferentialChecker.i.check(addressDTO, resultDTO);

        if (resultDTO.isFailure()) {
            resultDTO.putAll(addressDTO);
            resultDTO.setForward("Referenced");
            return;
        }

        if (CRUDDirector.OP_DELETE.equals(paramDTO.getOp())) { // delete the address when access to option throught list delete option.
            log.debug("Deleting the address");

            Address address = null;
            try {
                address = (Address) EJBFactory.i.findEJB(addressDTO);
            } catch (EJBFactoryException e) {
                log.debug("Address to delete cannot be found...");
                // if not found has been deleted by other user
                ctx.setRollbackOnly();//invalid the transaction
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }

            if (address != null) {
                try {
                    address.setBankAccountId(null);

                    //removing telecoms of address and all contact persons
                    log.debug("removing telecoms");
                    TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
                    try {
                        Collection telecoms = telecomHome.findAddressTelecoms(address.getAddressId());
                        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                            Telecom telecom = (Telecom) iterator.next();
                            telecom.remove();
                        }

                    } catch (FinderException e) {
                        log.error("unexpected telecoms deleting error", e);
                    }

                    // removing communications
                    log.debug("Removing communications");
                    Iterator communications = address.getCommunications().iterator();
                    ContactFreeText communicationFreetext = null;
                    while (communications.hasNext()) {
                        Contact communication = (Contact) communications.next();
                        communicationFreetext = communication.getContactFreeText();
                        communication.remove();
                        if (communicationFreetext != null) {
                            communicationFreetext.remove();
                        }
                        communications = address.getCommunications().iterator();
                    }

                    log.debug("removing image freetext");
                    // removing freetexts
                    if (address.getImageFreeText() != null) {
                        ContactFreeText image = address.getImageFreeText();
                        address.setImageFreeText(null);
                        image.remove();
                    }
                    log.debug("removing description freetext");
                    if (address.getFreeText() != null) {
                        address.getFreeText().remove();
                    }
                    log.debug("removing waydescription freetext");
                    if (address.getWayDescription() != null) {
                        address.getWayDescription().remove();
                    }

                    //delete categoryfieldvalues for contact or organization
                    String finderName = "findByAddressId";
                    Object[] params = new Object[]{address.getAddressId(), address.getCompanyId()};
                    List paramsAsList = Arrays.asList(params);

                    CategoryUtilCmd myCmd = new CategoryUtilCmd();
                    myCmd.putParam("finderName", finderName);
                    myCmd.putParam("params", paramsAsList);
                    myCmd.setOp("deleteValues");
                    myCmd.executeInStateless(ctx);


                    //delete contact persons
                    Collection contactPersons = address.getContactPersons();
                    Object[] obj = contactPersons.toArray();
                    for (int i = 0; i < obj.length; i++) {
                        ContactPerson contactPerson = (ContactPerson) obj[i];

                        ContactPersonDeleteCmd contactPersonDeleteCmd = new ContactPersonDeleteCmd();
                        contactPersonDeleteCmd.setOp("delete");
                        contactPersonDeleteCmd.putParam("addressId", contactPerson.getAddressId());
                        contactPersonDeleteCmd.putParam("contactPersonId", contactPerson.getContactPersonId());
                        contactPersonDeleteCmd.executeInStateless(ctx);

                        ResultDTO contactPersonResultDTO = contactPersonDeleteCmd.getResultDTO();
                        if (contactPersonResultDTO.isFailure() || "Fail".equals(contactPersonResultDTO.getForward())) {
                            //fail delete contact person, set rollback
                            log.error("Error removing contact person of address..");

                            resultDTO.addResultMessage("Address.error.removeContactPerson", address.getName());
                            resultDTO.setForward("Fail");
                            ctx.setRollbackOnly();
                            return;
                        }
                    }

                    //finally remove address
                    address.remove();

                } catch (RemoveException e) {
                    ctx.setRollbackOnly();
                    log.error("Error removing address", e);
                    addressDTO.addNotFoundMsgTo(resultDTO);
                    resultDTO.setForward("Fail");
                }


            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
