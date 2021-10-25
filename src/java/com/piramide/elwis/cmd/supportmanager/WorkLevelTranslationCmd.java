package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.catalogmanager.GeneralTranslationCmd;
import com.piramide.elwis.dto.supportmanager.SupportCaseWorkLevelDTO;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: ivan
 * Date: Apr 18, 2006
 * Time: 2:01:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLevelTranslationCmd extends GeneralTranslationCmd {

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        super.setOp(this.getOp());
        super.executeInStateless(ctx, "langTextId", "workLevelId", "workLevelName", paramDTO, SupportCaseWorkLevelDTO.class);

        ResultDTO myResultDTO = super.resultDTO;

        if (null != myResultDTO.get(NEW_LANGTEXT)) {
            paramDTO.put("langTextId", myResultDTO.get(NEW_LANGTEXT));
        }

        if ("update".equals(this.getOp())
                && !"Fail".equals(resultDTO.getForward())
                && !"".equals(paramDTO.get("langTextId"))) {
            super.sincronizeFirstTranslation(ctx,
                    paramDTO,
                    "workLevelName",
                    "workLevelId",
                    "langTextId",
                    SupportCaseWorkLevelDTO.class);
        }

    }

    public boolean isStateful() {
        return super.isStateful();
    }
}
