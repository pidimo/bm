package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.ContactUpdateCmd;
import com.piramide.elwis.domain.salesmanager.ActionPK;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.SessionContext;
import java.util.Date;
import java.util.Iterator;

/**
 * Update sales process action logic
 *
 * @author Fernando Montaño
 * @version $Id: ActionUpdateCmd.java 10043 2011-02-03 19:26:47Z ivan $
 */

public class ActionUpdateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        ActionDTO actionDTO = new ActionDTO(paramDTO);
        ActionPK pK = new ActionPK();
        pK.contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
        pK.processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");

        //update active field, if this field has unchecked, arrives null value to command
        Boolean active = EJBCommandUtil.i.getValueAsBoolean(this, "active");
        if (null == active) {
            active = false;
        }
        actionDTO.put("active", active);

        actionDTO.setPrimKey(pK);

        actionDTO.remove("contactId"); //removing member of primary key
        actionDTO.remove("processId");//removing member of primary key

        //The creator userId cannot be updated at update time
        actionDTO.remove("userId");

        actionDTO.put("updateDateTime", DateUtils.createDate((new Date()).getTime(), DateTimeZone.getDefault().getID()).getMillis());

        ExtendedCRUDDirector.i.update(actionDTO, resultDTO, false, false, false, "Fail");

        if (!resultDTO.isFailure()) {
            actionDTO.put("contactId", pK.contactId); //restoring the contactid

            EJBCommand cmd = null;

            if ("true".equals(paramDTO.get("generate")) || "true".equals(paramDTO.get("build"))) {
                log.debug("Is GENERATE!!!");
                cmd = new GenerateDocumentCmd();
            } else {

                cmd = new ContactUpdateCmd();
            }

            cmd.putParam(actionDTO);

            try {
                cmd.executeInStateless(ctx);
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
            log.debug("After execute Dyna CMD");

            ResultDTO contactResultDTO = cmd.getResultDTO();
            resultDTO.putAll(contactResultDTO);
            if (contactResultDTO.hasResultMessage()) { //reading the error messages
                if (contactResultDTO.isFailure()) {
                    for (Iterator iterator = contactResultDTO.getResultMessages(); iterator.hasNext();) {
                        ResultMessage message = (ResultMessage) iterator.next();
                        resultDTO.addResultMessage(message);
                    }
                    resultDTO.setResultAsFailure();
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
