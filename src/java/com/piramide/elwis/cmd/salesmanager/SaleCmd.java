package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.Sale;
import com.piramide.elwis.domain.salesmanager.SaleHome;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public abstract class SaleCmd extends SaleManagerCmd {
    private static Log log = LogFactory.getLog(SaleCmd.class);

    protected SaleDTO getSaleDTO() {
        SaleDTO saleDTO = new SaleDTO();

        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "saleId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "contactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "customerId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "processId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "saleDate");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "sellerId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "netGross");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "contactId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "currencyId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "sentAddressId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "sentContactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, saleDTO, "additionalAddressId");

        saleDTO.put("title", paramDTO.get("title"));
        saleDTO.put("version", paramDTO.get("version"));

        //use in CRUD utility
        saleDTO.put("withReferences", paramDTO.get("withReferences"));
        return saleDTO;
    }

    protected boolean hasAssociatedSale(Integer processId, Integer companyId) {
        SaleHome saleHome =
                (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);

        try {
            Collection sales = saleHome.findByProsessId(processId, companyId);
            boolean result = !sales.isEmpty();
            resultDTO.put("hasAssociatedSale", result);
            if (!sales.isEmpty()) {
                Sale firstSale = (Sale) sales.toArray()[0];
                resultDTO.put("title", firstSale.getTitle());
                resultDTO.put("saleId", firstSale.getSaleId());
            }

            return result;
        } catch (FinderException e) {
            resultDTO.put("hasAssociatedSale", false);
            return false;
        }
    }
}
