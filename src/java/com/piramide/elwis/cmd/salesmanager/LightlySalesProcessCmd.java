package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * User: Fernando Montaño
 * Date: Jan 7, 2005
 * Time: 3:51:48 PM
 */
public class LightlySalesProcessCmd extends EJBCommand {
    private Log log = LogFactory.getLog(com.piramide.elwis.cmd.salesmanager.LightlySalesProcessCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read sales process command ... ");
        log.debug("salesprocessId to read = " + paramDTO.get("processId"));
        //Read the product
        SalesProcessDTO salesProcessDTO = new SalesProcessDTO(paramDTO);
        SalesProcess salesProcess;
        try {
            salesProcess = (SalesProcess) EJBFactory.i.findEJB(salesProcessDTO);
            resultDTO.put("processName", salesProcess.getProcessName());
            resultDTO.put("probability", salesProcess.getProbability());
            resultDTO.put("addressId", salesProcess.getAddressId());

            //reading the address information
            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", salesProcess.getAddressId());
            addressCmd.executeInStateless(ctx);
            resultDTO.putAll(addressCmd.getResultDTO());

        } catch (EJBFactoryException ex) {
            salesProcessDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
