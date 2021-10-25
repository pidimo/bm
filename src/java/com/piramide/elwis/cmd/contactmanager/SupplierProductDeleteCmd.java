package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.ProductSupplierPK;
import com.piramide.elwis.dto.contactmanager.SupplierProductDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: SupplierProductDeleteCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class SupplierProductDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete supplierProduct command");

        resultDTO.put("productName", paramDTO.get("productName"));
        resultDTO.put("supplierName", paramDTO.get("supplierName"));
        ProductSupplierPK pk = new ProductSupplierPK(new Integer(paramDTO.getAsInt("supplierId")),
                new Integer(paramDTO.getAsInt("productId")));
        SupplierProductDTO dto = new SupplierProductDTO(paramDTO);
        dto.setPrimKey(pk);
        ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
        if (resultDTO.hasResultMessage()) {
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
