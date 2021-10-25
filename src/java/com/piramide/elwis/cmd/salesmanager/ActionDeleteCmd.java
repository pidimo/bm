package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.domain.salesmanager.Action;
import com.piramide.elwis.domain.salesmanager.ActionPK;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Delete sales process action logic
 *
 * @author Fernando Montaño
 * @version $Id: ActionDeleteCmd.java 9504 2009-08-03 11:05:31Z ivan $
 */
public class ActionDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing Action Delete Command...");

        ActionDTO actionDTO = new ActionDTO(paramDTO);
        ActionPK pK = new ActionPK();
        pK.contactId = new Integer(actionDTO.get("contactId").toString());
        pK.processId = new Integer(actionDTO.get("processId").toString());
        actionDTO.setPrimKey(pK);
        actionDTO.put("withReferences", true);


        Action action = (Action) ExtendedCRUDDirector.i.read(actionDTO, resultDTO, true);
        if (null == action && resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }

        try {
            //Action action = (Action) EJBFactory.i.findEJB(actionDTO);

            if (action != null) { //it exists

                //removing action positions
                Collection positions = (Collection) EJBFactory.i.callFinder(new ActionPositionDTO(),
                        "findByProcessAndContactId", new Object[]{action.getProcessId(), action.getContactId()});
                Iterator itPositions = positions.iterator();
                while (itPositions.hasNext()) {
                    ActionPosition position = (ActionPosition) itPositions.next();
                    position.remove();
                }
                //removing action
                action.remove();

                //removing contact related
                ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
                contactDeleteCmd.putParam(paramDTO);
                contactDeleteCmd.executeInStateless(ctx);
                //ContactDTO contactDTO = new ContactDTO(actionDTO);
                //EJBFactory.i.removeEJB(contactDTO); //remove the contact
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            log.debug("It seems it was deleted by other user.");
            resultDTO.isClearingForm = true;
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            actionDTO.addNotFoundMsgTo(resultDTO);
        }


    }

    public boolean isStateful() {
        return false;
    }
}
