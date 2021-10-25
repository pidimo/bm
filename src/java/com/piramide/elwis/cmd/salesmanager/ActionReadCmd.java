package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.ContactReadCmd;
import com.piramide.elwis.cmd.contactmanager.utils.AddressUtil;
import com.piramide.elwis.domain.salesmanager.Action;
import com.piramide.elwis.domain.salesmanager.ActionPK;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Read action logic
 *
 * @author Fernando Montaño
 * @version $Id: ActionReadCmd.java 10009 2010-11-15 10:10:53Z ivan $
 */
public class ActionReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionReadCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing Action Read Command...");
        log.debug("contactId = " + paramDTO.get("contactId"));
        log.debug("processId = " + paramDTO.get("processId"));

        ActionDTO actionDTO = new ActionDTO(paramDTO);
        ActionPK pK = new ActionPK();

        pK.contactId = new Integer(paramDTO.get("contactId").toString());
        pK.processId = new Integer(paramDTO.get("processId").toString());


        boolean checkReferences = false;
        if (null != paramDTO.get("withReferences") && "true".equals(paramDTO.get("withReferences").toString())) {
            checkReferences = true;
        }

        actionDTO.setPrimKey(pK);

        Action action = (Action) ExtendedCRUDDirector.i.read(actionDTO, resultDTO, checkReferences);
        if (null == action && resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }


        //if copy action positions is enabled
        if (action != null && paramDTO.get("copyFromContactId") != null && !"".equals(paramDTO.get("copyFromContactId"))) {
            ActionCopyPositionCmd copyPositionCmd = new ActionCopyPositionCmd();
            copyPositionCmd.putParam("processId", action.getProcessId());
            copyPositionCmd.putParam("contactId", action.getContactId());
            copyPositionCmd.putParam("copyFromContactId", paramDTO.get("copyFromContactId"));
            copyPositionCmd.executeInStateless(ctx);
            if (copyPositionCmd.getResultDTO().isFailure()) {
                resultDTO.addResultMessage("Action.copyInvalidActionPositions.error");
            }
        }

        resultDTO.put("haveActionPositions", false);
        if (null != action) {
            if (null != action.getUserId()) {
                resultDTO.put("userName", AddressUtil.i.getAddressName(action.getUserId(), ctx));
            }

            resultDTO.put("haveActionPositions", null != action.getActionPositions() && !action.getActionPositions().isEmpty());
        }


        //check references is not required in read contact when action is forward to delete
        if (actionDTO.containsKey("withReferences")) {
            actionDTO.remove("withReferences");
        }

        ContactReadCmd contactCmd = new ContactReadCmd();
        contactCmd.putParam(actionDTO);
        contactCmd.executeInStateless(ctx);
        ResultDTO contactResultDTO = contactCmd.getResultDTO();
        resultDTO.putAll(contactResultDTO);


    }

    public boolean isStateful() {
        return false;
    }
}
