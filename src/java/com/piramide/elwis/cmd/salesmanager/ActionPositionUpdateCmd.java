package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.domain.salesmanager.SalesFreeText;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;

import javax.ejb.SessionContext;

/**
 * @author : yumi
 *         <p/>
 *         Jatun s.r.l
 */
public class ActionPositionUpdateCmd extends EJBCommand {


    public void executeInStateless(SessionContext ctx) {
        resultDTO.put("productName", paramDTO.get("productName"));

        ActionPositionDTO dto = new ActionPositionDTO(paramDTO);

        //the total pricess for action positions always must be recalculating
        dto.remove("totalPrice");
        dto.remove("totalPriceGross");

        ActionPosition actionPosition =
                (ActionPosition) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");

        //ActionPosition was delete by other user
        if (null == actionPosition) {
            return;
        }

        //Version error
        if (resultDTO.isFailure()) {
            ActionPositionReadCmd actionPositionReadCmd = new ActionPositionReadCmd();
            actionPositionReadCmd.putParam("positionId", actionPosition.getPositionId());
            actionPositionReadCmd.putParam("productName", paramDTO.get("productName"));
            actionPositionReadCmd.executeInStateless(ctx);
            resultDTO.putAll(actionPositionReadCmd.getResultDTO());
            return;
        }

        //update total prices
        SaleUtil.i.calculateActionPositionTotalPrices(actionPosition);

        //update freetext
        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, actionPosition, "DescriptionText", SalesFreeText.class,
                SalesConstants.JNDI_FREETEXT, FreeTextTypes.FREETEXT_SALES, "description");
        resultDTO.put("contactId", actionPosition.getContactId());
        resultDTO.put("processId", actionPosition.getProcessId());
    }

    public boolean isStateful() {
        return false;
    }
}

