package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.Pricing;
import com.piramide.elwis.domain.productmanager.PricingPK;
import com.piramide.elwis.dto.productmanager.PricingDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * Execute product delete bussines logic.
 *
 * @author Fernando Montaño
 * @version $Id: PricingDeleteCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class PricingDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete command");
        PricingDTO pricingDTO = new PricingDTO(paramDTO);
        if ("delete".equals(getOp())) {
            IntegrityReferentialChecker.i.check(pricingDTO, resultDTO);
            if (resultDTO.isFailure()) { //is referenced
                return;
            }

            Pricing pricing = null;
            try {
                PricingPK pK = new PricingPK(new Integer(paramDTO.getAsInt("productId")),
                        new Integer(paramDTO.get("quantity").toString()));
                log.debug("Executing delete command" + pK);
                pricingDTO.setPrimKey(pK);
                pricing = (Pricing) EJBFactory.i.findEJB(pricingDTO);

                try {
                    pricing.remove();  //remove below the additional not CMR relationships
                } catch (RemoveException e) {
                    ctx.setRollbackOnly();
                    log.error("Error removing pricing", e);
                    pricingDTO.addNotFoundMsgTo(resultDTO);
                    resultDTO.setForward("Fail");
                }
            } catch (EJBFactoryException e) {
                log.debug("Product to delete cannot be found...");
                // if not found has been deleted by other user
                ctx.setRollbackOnly();//invalid the transaction
                pricingDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");

            }
        } else {
            PricingPK pK = new PricingPK(new Integer(paramDTO.get("productId").toString()),
                    new Integer(paramDTO.get("quantity").toString()));
            pricingDTO.setPrimKey(pK);
            ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_READ,
                    pricingDTO, resultDTO, false, false, false, false);
        }


    }

    public boolean isStateful() {
        return false;
    }
}
