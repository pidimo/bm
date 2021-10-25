package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Supplier;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.utils.CodeUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: AddressToSupplierCmd.java 24-feb-2009 18:03:55
 */
public class AddressToSupplierCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing AddressToSupplierCmd....................." + paramDTO);
        Integer addressId = new Integer(paramDTO.get("addressId").toString());

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.put(SupplierDTO.KEY_SUPPLIERID, addressId);
        supplierDTO.put("companyId", paramDTO.get("companyId"));
        Supplier supplier = null;
        try {
            supplier = (Supplier) EJBFactory.i.findEJB(supplierDTO);
        } catch (EJBFactoryException e) {
            /* the supplier was not found registered as supplier, in this case create it and convert
            the related address in a supplier */
            AddressDTO addressDTO = new AddressDTO(addressId);
            try {
                Address address = (Address) EJBFactory.i.findEJB(addressDTO);//recovering the address of the supplier
                supplier = (Supplier) EJBFactory.i.createEJB(supplierDTO);
                Byte newCode = new Byte(CodeUtil.addCode(address.getCode(), CodeUtil.supplier));
                address.setCode(newCode);   // new code = last code + supplier code
                address.setVersion(new Integer(address.getVersion().intValue() + 1)); //ensure concurrency checking

            } catch (EJBFactoryException e1) {//address not found
                resultDTO.addResultMessage("ProductSupplier.Supplier.NotFound");
                resultDTO.setForward("Fail");
                return;
            }

        }
    }

    public boolean isStateful() {
        return false;
    }
}