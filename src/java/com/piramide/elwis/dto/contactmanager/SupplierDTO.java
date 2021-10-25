package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Supplier DTO value class.
 *
 * @author Fernando Montaño
 * @version $Id: SupplierDTO.java 9890 2009-11-20 22:29:50Z ivan $
 */
public class SupplierDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_SUPPLIERID = "supplierId";

    /**
     * Creates an instance.
     */
    public SupplierDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupplierDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_SUPPLIERID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_SUPPLIER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", "Supplier");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {

                resultDTO.addResultMessage("customMsg.Supplier.NotFound", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("customMsg.Supplier.NotFound", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            }

        } else { // when a supplier was deleted by other user
            resultDTO.addResultMessage("Supplier.NotFound");
        }


    }

    public ComponentDTO createDTO() {
        return new SupplierDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ProductConstants.TABLE_PRODUCTSUPPLIER, "supplierid");
        tables.put(FinanceConstants.TABLE_INCOMINGINVOICE, "supplierid");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "addressId");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {

                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            }

        } else { // when a supplier was deleted by other user
            resultDTO.addResultMessage("customMsg.Referenced");
        }
    }
}
