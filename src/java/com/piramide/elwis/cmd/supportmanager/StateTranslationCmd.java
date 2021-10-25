package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.catalogmanager.GeneralTranslationCmd;
import com.piramide.elwis.dto.supportmanager.StateDTO;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: ivan
 * Date: Apr 17, 2006
 * Time: 2:22:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateTranslationCmd extends GeneralTranslationCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        super.setOp(this.getOp());
        super.executeInStateless(ctx, "langTextId", "stateId", "stateName", paramDTO, StateDTO.class);

        ResultDTO myResultDTO = super.resultDTO;

        if (null != myResultDTO.get(NEW_LANGTEXT)) {
            paramDTO.put("langTextId", myResultDTO.get(NEW_LANGTEXT));
        }

        if ("update".equals(this.getOp())
                && !"Fail".equals(resultDTO.getForward())
                && !"".equals(paramDTO.get("langTextId"))) {
            super.sincronizeFirstTranslation(ctx,
                    paramDTO,
                    "stateName",
                    "stateId",
                    "langTextId",
                    StateDTO.class);
        }

    }

    public boolean isStateful() {
        return super.isStateful();
    }
}
