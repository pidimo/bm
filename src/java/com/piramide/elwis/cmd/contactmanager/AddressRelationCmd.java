package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.contactmanager.AddressRelation;
import com.piramide.elwis.domain.contactmanager.ContactFreeTextHome;
import com.piramide.elwis.dto.contactmanager.AddressRelationDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AddressRelationCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AddressRelationCmd................" + paramDTO);
        boolean isRead = true;

        if ("create".equals(getOp())) {
            isRead = false;
            create();
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }

        if (isRead) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void read() {
        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));

        AddressRelationDTO addressRelationDTO = new AddressRelationDTO(paramDTO);
        AddressRelation addressRelation = (AddressRelation) ExtendedCRUDDirector.i.read(addressRelationDTO, resultDTO, checkReferences);

        if (addressRelation != null) {
            if (addressRelation.getContactFreeText() != null) {
                resultDTO.put("comment", new String(addressRelation.getContactFreeText().getValue()));
            }
        }
    }

    private void create() {
        AddressRelationDTO addressRelationDTO = new AddressRelationDTO(paramDTO);

        AddressRelation addressRelation = (AddressRelation) ExtendedCRUDDirector.i.create(addressRelationDTO, resultDTO, true);

        if (addressRelation != null) {
            if (hasComment()) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, addressRelation, "ContactFreeText", ContactFreeTextHome.class,
                        ContactConstants.JNDI_CONTACTFREETEXT, FreeTextTypes.FREETEXT_CONTACT, "comment");
            }
        }
    }

    private void update() {
        AddressRelationDTO addressRelationDTO = new AddressRelationDTO(paramDTO);

        AddressRelation addressRelation = (AddressRelation)  ExtendedCRUDDirector.i.update(addressRelationDTO, resultDTO, true, true, true, "Fail");

        if (addressRelation != null && !resultDTO.isFailure()) {

            if (hasComment() || addressRelation.getCommentId() != null) {
                FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, addressRelation, "ContactFreeText", ContactFreeTextHome.class,
                        ContactConstants.JNDI_CONTACTFREETEXT, FreeTextTypes.FREETEXT_CONTACT, "comment");
            }
        }
    }

    private void delete() {
        AddressRelationDTO addressRelationDTO = new AddressRelationDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(addressRelationDTO, resultDTO, true, "Fail");
    }

    private boolean hasComment() {
        return (paramDTO.get("comment") != null && !"".equals(paramDTO.get("comment").toString()));
    }
}
