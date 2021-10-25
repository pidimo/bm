package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: CategoryTranslationCmd.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 18-03-2005 03:58:23 PM ivan Exp $
 */
public class CategoryTranslationCmd extends GeneralTranslationCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        super.setOp(this.getOp());
        super.executeInStateless(ctx, "langTextId", "categoryId", "categoryName", paramDTO, CategoryDTO.class);
        ResultDTO myResultDTO = super.resultDTO;

        if (null != myResultDTO.get(NEW_LANGTEXT)) {
            paramDTO.put("langTextId", myResultDTO.get(NEW_LANGTEXT));
        }

        if ("update".equals(this.getOp())
                && !"Fail".equals(resultDTO.getForward())
                && !"".equals(paramDTO.get("langTextId"))) {
            super.sincronizeFirstTranslation(ctx,
                    paramDTO,
                    "categoryName",
                    "categoryId",
                    "langTextId",
                    CategoryDTO.class);
        }

    }

    public boolean isStateful() {
        return super.isStateful();
    }
}
