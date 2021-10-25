package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.dto.contactmanager.AddressDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Comand to import any Address to Employee
 *
 * @author Titus
 * @version ImportEmployeeCmd.java, v 2.0 Jun 17, 2004 4:14:28 PM
 */
public class ImportEmployeeCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Importing person address command with Operation = " + getOp());
        log.debug("Address Id to import = " + paramDTO.get("employeeAddresId"));
        paramDTO.put("addressId", paramDTO.get("employeeAddresId"));
        CRUDDirector.i.doCRUD(getOp(), new AddressDTO(paramDTO), resultDTO);
        paramDTO.remove("addressId");
    }

    public boolean isStateful() {
        return false;
    }
}
