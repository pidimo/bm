package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.AddressGroup;
import com.piramide.elwis.domain.webmailmanager.MailGroupAddr;
import com.piramide.elwis.dto.webmailmanager.MailGroupAddrDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;

/**
 * AlfaCentauro Team
 * <p/>
 * This class implements the functionalities of create, read, delete and update over a MailGroupAddr bean
 *
 * @author Alvaro
 * @version $Id: MailGroupAddrCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class MailGroupAddrCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass()); //For the debug messages

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        MailGroupAddrDTO dto = new MailGroupAddrDTO();
        dto.put("name", paramDTO.get("name"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put(UserMailDTO.KEY_USERMAILID, paramDTO.get("userMailId"));
        String op = "";
        try {
            op = paramDTO.get("op").toString();
            resultDTO.put("op", op);
        }
        catch (NullPointerException n) {
            op = "read";
        }
        Object mailGroupAddrId = paramDTO.get("mailGroupAddrId");

        if (op.equals("create")) {
            createMailGroupAddr(dto);
        } else if (op.equals("update")) {
            updateMailGroupAddr(dto, mailGroupAddrId);
        } else if (op.equals("delete")) {
            deleteMailGroupAddr(mailGroupAddrId);
        } else if (op.equals("read")) {
            readMailGroupAddr(mailGroupAddrId);
        }
    }

    /**
     * Creates a MailGroupAddr Bean
     *
     * @param dto Is the MailGroupAddrDTO with the data of the new Bean
     */
    public void createMailGroupAddr(ComponentDTO dto) {
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, dto, resultDTO);
    }

    /**
     * Updates a MailGroupAddr Bean
     *
     * @param dto              Is the MailGroupAddrDTO with the data of the Bean to update
     * @param mailGroupAddr_id Is the id of the Bean to modify
     */
    public void updateMailGroupAddr(ComponentDTO dto, Object mailGroupAddr_id) {
        Integer mailGroupAddrId = new Integer(mailGroupAddr_id.toString());
        dto.put(dto.getPrimKeyName(), mailGroupAddrId);
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, dto, resultDTO);
    }

    /**
     * Deletes a MailGroupAddr Bean
     *
     * @param mailGroupAddr_id Is the id of the Bean to delete
     */
    public void deleteMailGroupAddr(Object mailGroupAddr_id) {
        Integer mailGroupAddrId = new Integer(mailGroupAddr_id.toString());
        MailGroupAddrDTO dto = new MailGroupAddrDTO();
        dto.put(dto.getPrimKeyName(), mailGroupAddrId);
        //Erase the addressgroups(the cascade delete dont works)
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, dto, new ResultDTO());
        ArrayList addressGroups = new ArrayList(mailGroupAddr.getAddressGroups());
        for (int i = 0; i < addressGroups.size(); i++) {
            EJBFactory.i.removeEJB((AddressGroup) addressGroups.get(i));
        }
        //Erase the MailGroupAddr
        EJBFactory.i.removeEJB(mailGroupAddr);
    }

    /**
     * Reads the data of a MailGroupAddr Bean and put this in the resultDTO
     *
     * @param mailGroupAddr_id Is the Id of the MailGroupAddr Bean to read
     */
    public void readMailGroupAddr(Object mailGroupAddr_id) {
        MailGroupAddrDTO dto = new MailGroupAddrDTO();
        Integer mailGroupAddrId = new Integer(mailGroupAddr_id.toString());
        dto.put(dto.getPrimKeyName(), mailGroupAddrId);
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, dto, resultDTO);
    }
}
