package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Cmd to manage sale position as participant
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class SalePositionParticipantCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SalePositionParticipantCmd................" + paramDTO);

        if ("checkParticipant".equals(getOp())) {
            checkProductParticipant();
        }
    }

    private void checkProductParticipant() {
        Integer productId = new Integer(paramDTO.get("productId").toString());
        Integer contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());

        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        Boolean alreadyRegistered = false;
        try {
            Collection collection = salePositionHome.findByProductAndContactPersonId(productId, contactPersonId);
            alreadyRegistered = !collection.isEmpty();
        } catch (FinderException ignore) {
        }

        resultDTO.put("alreadyIsParticipant", alreadyRegistered);
    }

    public boolean isStateful() {
        return false;
    }
}