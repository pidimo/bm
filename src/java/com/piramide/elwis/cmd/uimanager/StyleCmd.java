package com.piramide.elwis.cmd.uimanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.uimanager.StyleDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleCmd.java 9714 2009-09-17 21:55:43Z fernando $
 */
public class StyleCmd extends EJBCommand {
    Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing StyleCmd......................" + getParamDTO());

        if ("create".equals(getOp())) {
            create();
        }
        if ("update".equals(getOp())) {
            update();
        }
        if ("delete".equals(getOp())) {
            delete();
        }
    }

    public void create() {
        StyleDTO styleDTO = new StyleDTO();
        styleDTO.put("name", paramDTO.get("name"));
        styleDTO.put("styleSheetId", Integer.valueOf(paramDTO.get("styleSheetId").toString()));
        styleDTO.put("companyId", Integer.valueOf(paramDTO.get("companyId").toString()));
        ExtendedCRUDDirector.i.create(styleDTO, resultDTO, true);
    }

    public void update() {
        StyleDTO styleDTO = new StyleDTO();
        styleDTO.putAll(paramDTO);
        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE, styleDTO, resultDTO);
    }

    public void delete() {
        StyleDTO styleDTO = new StyleDTO();
        styleDTO.put("styleId", paramDTO.get("styleId"));
        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_DELETE, styleDTO, resultDTO);
    }
}
