package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.catalogmanager.Currency;
import com.piramide.elwis.domain.catalogmanager.CurrencyHome;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the Currency
 *
 * @author Ivan
 * @version $Id: CurrencyCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */
public class CurrencyCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CurrencyCmd...");
        if ("getBasicCurrency".equals(getOp())) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            getBasicCurrency(companyId);
            return;
        }
        if ("create".equals(this.getOp())) {
            changeDefaultCurrencyForCreate();
        }
        if ("update".equals(this.getOp())) {
            changeDefaultCurrency();
        }
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, CurrencyDTO.class);
    }

    private void getBasicCurrency(Integer companyId) {
        CurrencyHome currencyHome =
                (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);

        CurrencyDTO currencyDTO = null;
        try {
            Currency basicCurreny = currencyHome.getIsBasicCurrency(companyId);
            if (null != basicCurreny) {
                currencyDTO = new CurrencyDTO();
                DTOFactory.i.copyToDTO(basicCurreny, currencyDTO);
            }
        } catch (FinderException e) {
            log.debug("-> Execute 'getIsBasicCurrency' companyId=" + companyId + " FAIL");
        }

        resultDTO.put("getBasicCurrency", currencyDTO);
    }

    public boolean isStateful() {
        return false;
    }

    private void changeDefaultCurrency() {
        String isBasicCurrency = paramDTO.getAsString("isBasicCurrency");
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        paramDTO.remove("isBasicCurrency");
        paramDTO.put("isBasicCurrency", Boolean.valueOf(("on".equals(isBasicCurrency) ? true : false)));
        if (paramDTO.get("isBasicCurrency").equals(Boolean.valueOf(true))) {
            try {
                Currency oldBasicCurrency = currencyHome.getIsBasicCurrency(Integer.valueOf((String) paramDTO.get("companyId")));
                Currency newBasicCurrency = currencyHome.findByPrimaryKey(Integer.valueOf((String) paramDTO.get("currencyId")));
                oldBasicCurrency.setIsBasicCurrency(Boolean.valueOf(false));
                newBasicCurrency.setIsBasicCurrency(Boolean.valueOf(true));
            } catch (FinderException e) {
                log.error("Cannot find Currency on changeDefaultCurrency Method...");
            }
        }
    }

    private void changeDefaultCurrencyForCreate() {
        String isBasicCurrency = paramDTO.getAsString("isBasicCurrency");
        CurrencyHome currencyHome = (CurrencyHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CURRENCY);
        paramDTO.remove("isBasicCurrency");
        paramDTO.put("isBasicCurrency", Boolean.valueOf(("on".equals(isBasicCurrency) ? true : false)));
        if (paramDTO.get("isBasicCurrency").equals(Boolean.valueOf(true))) {
            try {
                Currency oldBasicCurrency = currencyHome.getIsBasicCurrency(Integer.valueOf((String) paramDTO.get("companyId")));
                oldBasicCurrency.setIsBasicCurrency(Boolean.valueOf(false));
            } catch (FinderException e) {
                log.error("Cannot find Currency on changeDefaultCurrencyForCreate Method...");
            }
        }
    }
}

