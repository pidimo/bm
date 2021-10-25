package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Supplier;
import com.piramide.elwis.domain.productmanager.ProductSupplier;
import com.piramide.elwis.domain.productmanager.ProductSupplierHome;
import com.piramide.elwis.domain.productmanager.ProductSupplierPK;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.dto.contactmanager.SupplierProductDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: SupplierProductCreateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SupplierProductCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create supplierProduct command....................." + paramDTO);
        log.debug("Operation = " + getOp() + " productName" + paramDTO.get("productName"));
        ProductSupplier productSupplier = null;
        Integer addressId = null;
        boolean isFromProducts = paramDTO.getAsBool("isProduct");

        if (isFromProducts) {
            addressId = new Integer(paramDTO.get("supplierId").toString());
        } else {
            addressId = new Integer(paramDTO.get("addressId").toString());
        }

        if (addressId != null && paramDTO.get("productId") != null) {
            ProductSupplierPK pk = new ProductSupplierPK(addressId,
                    new Integer(paramDTO.getAsInt("productId")));
            ProductSupplierHome home = (ProductSupplierHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTSUPPLIER);
            try {
                productSupplier = home.findByPrimaryKey(pk);
            } catch (FinderException e) {
            }
        }

        if (productSupplier == null) {
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

            SupplierProductDTO supplierProductDTO = new SupplierProductDTO(paramDTO);
            supplierProductDTO.put("supplierId", supplier.getSupplierId());
            ProductSupplier newProductSupplier = (ProductSupplier) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, supplierProductDTO, resultDTO, false, false, false, false);
            newProductSupplier.setActive(new Boolean("on".equals(paramDTO.getAsString("active"))));


        } else {
            if (isFromProducts) {
                resultDTO.addResultMessage("Products.supplierContactDuplicate");
            } else {
                resultDTO.addResultMessage("Products.supplierDuplicate");
            }

            resultDTO.setForward("Fail");
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
