package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.List;

/**
 * Execute business logic for delete a contact person.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonDeleteCmd.java 9667 2009-09-02 18:53:26Z miguel $
 */

public class ContactPersonDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ContactPersonDeleteCmd command");
        log.debug("contactPersonId = " + paramDTO.get("contactPersonId"));
        log.debug("addressId that contanins the contactperson = " + paramDTO.get("addressId"));

        ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                new Integer(paramDTO.get("contactPersonId").toString()));
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO(paramDTO);
        contactPersonDTO.setPrimKey(pK);

        if (CRUDDirector.OP_DELETE.equals(paramDTO.getOp())) { // delete the contact person from list delete option.
            log.debug("Deleting the contact person");
            IntegrityReferentialChecker.i.check(contactPersonDTO, resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }

            ContactPerson cp = (ContactPerson) CRUDDirector.i.read(contactPersonDTO, resultDTO);
            if (null != cp) {
                //delete categoryfieldvalues for contact or organization
                String finderName = "findByAddressIdAndContactPersonId";
                Object[] params = new Object[]{cp.getAddressId(),
                        cp.getContactPersonId(),
                        cp.getCompanyId()};
                List paramsAsList = Arrays.asList(params);

                CategoryUtilCmd myCmd = new CategoryUtilCmd();
                myCmd.putParam("finderName", finderName);
                myCmd.putParam("params", paramsAsList);
                myCmd.setOp("deleteValues");
                myCmd.executeInStateless(ctx);

                CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, contactPersonDTO, resultDTO);
            }

            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
                return;
            }
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
