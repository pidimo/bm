package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This class allows import and person Address to create a new contact person, when an import create method
 * is used to create a contact person.
 *
 * @author Fernando Montaño
 * @version $Id: ImportPersonAddressCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ImportPersonAddressCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Importing person address command with Operation = " + getOp());
        log.debug("Address Id to import = " + paramDTO.get("addressIdToImport"));
        paramDTO.put("addressId", paramDTO.get("addressIdToImport"));
        CRUDDirector.i.doCRUD(getOp(), new AddressDTO(paramDTO), resultDTO);
        //If date do not have year
        if (resultDTO.get("birthday") != null) {
            if (resultDTO.get("birthday").toString().length() <= 5) {
                resultDTO.put("dateWithoutYear", "true");
            }
        }


    }

    public boolean isStateful() {
        return false;
    }
}
