package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Salutation;
import com.piramide.elwis.domain.catalogmanager.SalutationHome;
import com.piramide.elwis.dto.catalogmanager.SalutationDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopySalutationCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        SalutationHome salutationHome = (SalutationHome)
                EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_SALUTATION);

        Collection sourceSalutations = null;
        try {
            sourceSalutations = salutationHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read salutations of source company...");
        }
        if (null != sourceSalutations && !sourceSalutations.isEmpty()) {
            for (Object obj : sourceSalutations) {
                Salutation sourceSalutation = (Salutation) obj;
                Integer targetAddressTextId = CopyCatalogUtil.i.buildLangText(sourceSalutation.getAddressTextId(), target.getCompanyId());
                Integer targetLetterTextId = CopyCatalogUtil.i.buildLangText(sourceSalutation.getLetterTextId(), target.getCompanyId());

                SalutationDTO dto = new SalutationDTO();
                DTOFactory.i.copyToDTO(sourceSalutation, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("addressTextId", targetAddressTextId);
                dto.put("letterTextId", targetLetterTextId);
                dto.put("salutationId", null);

                try {
                    salutationHome.create(dto);
                } catch (CreateException e) {
                    log.error("Cannot create salutation", e);
                }
            }
        }
    }
}
