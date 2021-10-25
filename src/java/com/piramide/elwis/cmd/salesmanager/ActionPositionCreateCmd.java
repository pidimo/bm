package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author : yumi
 *         <p/>
 *         Jatun s.r.l
 */
public class ActionPositionCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        ActionPositionDTO positionDTO = new ActionPositionDTO(paramDTO);

        //the total pricess for action positions always must be recalculating
        positionDTO.remove("totalPrice");
        positionDTO.remove("totalPriceGross");

        Action action = getAction(new Integer(paramDTO.get("contactId").toString()),
                new Integer(paramDTO.get("processId").toString()));

        if (action == null) {
            resultDTO.setForward("Fail");
            return;
        }

        ActionPosition actionPosition;
        actionPosition = (ActionPosition) ExtendedCRUDDirector.i.create(positionDTO, resultDTO, false);

        //update total prices
        SaleUtil.i.calculateActionPositionTotalPrices(actionPosition);

        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, actionPosition, "DescriptionText", SalesFreeText.class,
                SalesConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_ACTIONPOSITION, "description");
    }

    private Action getAction(Integer contactId, Integer processId) {
        ActionPK pK = new ActionPK();
        pK.contactId = contactId;
        pK.processId = processId;

        ActionHome actionHome = (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);
        try {
            return actionHome.findByPrimaryKey(pK);
        } catch (FinderException e) {
            log.debug("-> Read Parent Action FAIL,  it was deleted by another user ");
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
