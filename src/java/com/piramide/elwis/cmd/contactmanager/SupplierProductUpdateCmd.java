package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.productmanager.ProductSupplier;
import com.piramide.elwis.domain.productmanager.ProductSupplierPK;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.productmanager.ProductSupplierDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: SupplierProductUpdateCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class SupplierProductUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update supplier product command");
        log.debug("Operation = " + getOp());
        ProductSupplierPK pk = new ProductSupplierPK(new Integer(paramDTO.getAsInt("supplierId")),
                new Integer(paramDTO.getAsInt("productId")));
        paramDTO.remove("supplierId");
        paramDTO.remove("productId");
        ProductSupplierDTO dto = new ProductSupplierDTO(paramDTO);
        dto.setPrimKey(pk);
        dto.put("active", new Boolean("on".equals(paramDTO.getAsString("active")) ? true : false));
        resultDTO.put("productName", paramDTO.getAsString("productName"));
        ProductSupplier productSupplier = (ProductSupplier) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
        resultDTO.put("unitName", paramDTO.get("unitName"));

        if (productSupplier != null) {
            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(productSupplier.getSupplierId()));
            resultDTO.put("supplierName", address.getName());
        }

    }

    public boolean isStateful() {
        return false;
    }
}

