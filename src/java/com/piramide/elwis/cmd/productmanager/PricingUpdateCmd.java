package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.Pricing;
import com.piramide.elwis.domain.productmanager.PricingHome;
import com.piramide.elwis.domain.productmanager.PricingPK;
import com.piramide.elwis.dto.productmanager.PricingDTO;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;

/**
 * Execute product creation business logic.
 *
 * @author Fernando Montaño
 * @version $Id: PricingUpdateCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class PricingUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update command");
        log.debug("Operation = " + getOp());
        log.debug("PRODUCT:= " + paramDTO.get("productId"));
        log.debug("QUANTITY:= " + paramDTO.get("quantity"));

        PricingDTO pricingDTO = new PricingDTO(paramDTO);

        if ("update".equals(getOp())) {
            PricingPK pK = new PricingPK(new Integer(paramDTO.getAsInt("productId")),
                    new Integer(paramDTO.get("quantity").toString()));
            pricingDTO.setPrimKey(pK);
            VersionControlChecker.i.check(pricingDTO, resultDTO, paramDTO);
            log.debug("PRICING ..........VERSION:" + resultDTO.get("version"));
            if (resultDTO.get("EntityBeanNotFound") != null) {
                resultDTO.put("quantity", paramDTO.get("quantity"));
                pricingDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (resultDTO.isFailure()) {
                CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, pricingDTO, resultDTO);
                return;
            }


            PricingHome pricingHome = (PricingHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRICING);
            try {
                Pricing pricing = pricingHome.findByPrimaryKey(pK);
                pricing.setPrice((BigDecimal) paramDTO.get("price"));
                pricing.setVersion((Integer) paramDTO.get("version"));
            } catch (FinderException e) {
                resultDTO.setResultAsFailure();
                pricingDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            }

        } else {
            log.debug("Reading pricing...");
            PricingPK pK = new PricingPK(new Integer(paramDTO.getAsInt("productId")),
                    new Integer(paramDTO.get("quantity").toString()));

            pricingDTO.setPrimKey(pK);

            ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_READ,
                    pricingDTO, resultDTO, false, false, false, false);

            resultDTO.put("unitName", paramDTO.get("unitName"));
            if (resultDTO.isFailure()) {
                resultDTO.put("quantity", paramDTO.get("quantity"));
                resultDTO.setForward("Fail");
            }
        }

    }

    public boolean isStateful() {
        return false;
    }
}
