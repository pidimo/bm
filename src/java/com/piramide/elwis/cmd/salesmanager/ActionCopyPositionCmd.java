package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionCopyPositionCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ActionCopyPositionCmd extends EJBCommand {

    private Log log = LogFactory.getLog(ActionCopyPositionCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("processId = " + paramDTO.get("processId"));
        log.debug("contactId = " + paramDTO.get("contactId"));
        log.debug("copyFromContactId = " + paramDTO.get("copyFromContactId"));


        ActionHome actionHome = (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);
        SalesFreeTextHome freeTextHome = (SalesFreeTextHome)
                EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_FREETEXT);

        ActionPK pKFrom = new ActionPK();
        pKFrom.contactId = new Integer(paramDTO.get("copyFromContactId").toString());
        pKFrom.processId = new Integer(paramDTO.get("processId").toString());

        ActionPK pKTo = new ActionPK();
        pKTo.contactId = new Integer(paramDTO.get("contactId").toString());
        pKTo.processId = new Integer(paramDTO.get("processId").toString());


        try {
            Action actionFrom = actionHome.findByPrimaryKey(pKFrom);
            Action actionTo = actionHome.findByPrimaryKey(pKTo);

            Boolean containValidActionPositions = containValidActionPositions(actionFrom, actionTo.getNetGross(), ctx);
            if (null != containValidActionPositions && !containValidActionPositions) {

                resultDTO.setResultAsFailure();
                return;
            }

            ActionPositionDTO positionDTO;
            ActionPosition position = null;
            byte[] freeTextValue;

            Integer lastAction = getLastActionNumber(actionTo.getProcessId(),
                    actionTo.getContactId(),
                    actionTo.getCompanyId(), ctx);

            Integer counter = 1;
            List actionPositionsToCopy = getActionPositionsToCopy(actionFrom.getProcessId(), actionFrom.getContactId());
            for (int i = 0; i < actionPositionsToCopy.size(); i++) {
                freeTextValue = null;
                ActionPosition positionFrom = (ActionPosition) actionPositionsToCopy.get(i);
                positionDTO = new ActionPositionDTO();
                DTOFactory.i.copyToDTO(positionFrom, positionDTO);
                positionDTO.put("contactId", actionTo.getContactId());
                positionDTO.put("number", lastAction + counter);
                if (positionFrom.getDescriptionText() != null) {
                    freeTextValue = positionFrom.getDescriptionText().getValue();
                }
                positionDTO.put("descriptionId", null);

                position = (ActionPosition) ExtendedCRUDDirector.i.create(positionDTO, resultDTO, false);
                if (position != null && freeTextValue != null) {
                    try {
                        SalesFreeText descriptionText = freeTextHome.create(freeTextValue,
                                position.getCompanyId(), new Integer(FreeTextTypes.FREETEXT_SALES));
                        position.setDescriptionId(descriptionText.getFreeTextId());
                    } catch (CreateException e) {
                        log.debug("Error creating the descriptiotn text");
                        //leave empty the description
                    }
                }

                counter++;
            }
        } catch (FinderException e) {
            log.debug("The action was deleted by other user... It cannot be possible to copy positions ");
        } catch (Exception e) {
            log.error("Cannot be possible to copy sales process action positions, unexpected error", e);
        }

    }

    private boolean containValidActionPositions(Action action, Integer netGrossField, SessionContext ctx) {
        ActionCmd actionCmd = new ActionCmd();
        actionCmd.setOp("containValidActionPositions");
        actionCmd.putParam("contactId", action.getContactId());
        actionCmd.putParam("processId", action.getProcessId());
        actionCmd.putParam("pageNetGross", netGrossField);
        actionCmd.executeInStateless(ctx);

        return (Boolean) actionCmd.getResultDTO().get("containValidActionPositions");
    }

    private List getActionPositionsToCopy(Integer processId, Integer contactId) {
        ActionPositionHome actionPositionHome =
                (ActionPositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONPOSITION);

        try {
            return (List) actionPositionHome.findByProcessAndContactId(processId, contactId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private Integer getLastActionNumber(Integer processId, Integer contacId, Integer companyId, SessionContext ctx) {
        ActionPositionCmd actionPositionCmd = new ActionPositionCmd();
        actionPositionCmd.setOp("getLastPositionNumber");
        actionPositionCmd.putParam("processId", processId);
        actionPositionCmd.putParam("contactId", contacId);
        actionPositionCmd.putParam("companyId", companyId);


        actionPositionCmd.executeInStateless(ctx);
        Integer maxNumber = (Integer) actionPositionCmd.getResultDTO().get("getLastPositionNumber");
        if (null == maxNumber) {
            return 0;
        }

        return maxNumber;
    }


    public boolean isStateful() {
        return false;
    }
}
