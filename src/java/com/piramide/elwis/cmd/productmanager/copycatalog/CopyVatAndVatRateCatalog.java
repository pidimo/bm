package com.piramide.elwis.cmd.productmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Vat;
import com.piramide.elwis.domain.catalogmanager.VatHome;
import com.piramide.elwis.domain.catalogmanager.VatRate;
import com.piramide.elwis.domain.catalogmanager.VatRateHome;
import com.piramide.elwis.dto.catalogmanager.VatDTO;
import com.piramide.elwis.dto.catalogmanager.VatRateDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopyVatAndVatRateCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Copy Vat and Vat Rate catalogs");
        VatHome vatHome = (VatHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VAT);

        Collection sourceVats = null;
        try {
            sourceVats = vatHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read Vats of source company");
        }

        if (null != sourceVats && !sourceVats.isEmpty()) {
            for (Object obj : sourceVats) {
                Vat sourceVat = (Vat) obj;
                VatDTO dto = new VatDTO();
                DTOFactory.i.copyToDTO(sourceVat, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("vatId", null);

                Vat targetVat = (Vat) ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);

                createVatRates(target, sourceVat.getVatId(), targetVat.getVatId());
            }
        }
    }

    private void createVatRates(Company target, Integer sourceVatId, Integer targetVatId) {
        VatRateHome vatRateHome = (VatRateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VATRATE);

        Collection vatRates = null;
        try {
            vatRates = vatRateHome.findByVatId(sourceVatId);
        } catch (FinderException e) {
            log.warn("Cannot read vat rates of source company");
        }

        if (null != vatRates && !vatRates.isEmpty()) {
            for (Object obj : vatRates) {
                VatRate sourceVatRate = (VatRate) obj;
                VatRateDTO dto = new VatRateDTO();
                DTOFactory.i.copyToDTO(sourceVatRate, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("vatId", targetVatId);
                ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
            }
        }
    }
}
