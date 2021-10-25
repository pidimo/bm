package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.domain.productmanager.CompetitorProduct;
import com.piramide.elwis.dto.productmanager.CompetitorProductDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CompetitorProductDeleteCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CompetitorProductDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete competitor command");

        CompetitorProductDTO competitorDTO = new CompetitorProductDTO(paramDTO);
        IntegrityReferentialChecker.i.check(competitorDTO, resultDTO);
        if (resultDTO.isFailure()) { //is referenced
            return;
        }

        CompetitorProduct competitor = null;
        try {
            competitor = (CompetitorProduct) EJBFactory.i.findEJB(competitorDTO);

            try {
                competitor.remove();  //remove below the additional not CMR relationships
            } catch (RemoveException e) {
                ctx.setRollbackOnly();
                log.error("Error removing competitor", e);
                competitorDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            }
        } catch (EJBFactoryException e) {
            log.debug("Competitor to delete cannot be found...");
            // if not found has been deleted by other user
            ctx.setRollbackOnly();//invalid the transaction
            competitorDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
