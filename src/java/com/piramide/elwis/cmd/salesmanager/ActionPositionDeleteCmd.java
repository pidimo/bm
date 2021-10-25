package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jan 26, 2005
 * Time: 9:21:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ActionPositionDeleteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ActionPositionDeleteCmd ...");
        ActionPositionDTO dto = new ActionPositionDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
    }

    public boolean isStateful() {
        return false;
    }
}
