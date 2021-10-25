package com.piramide.elwis.cmd.uimanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.uimanager.StyleAttributeDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleAttributeCmd.java 9714 2009-09-17 21:55:43Z fernando $
 */
public class StyleAttributeCmd extends EJBCommand {
    Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing StyleAttributeCmd......................" + getParamDTO());

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
        StyleAttributeDTO attributeDTO = new StyleAttributeDTO();
        attributeDTO.put("name", paramDTO.get("name"));
        attributeDTO.put("value", paramDTO.get("value"));
        attributeDTO.put("styleId", Integer.valueOf(paramDTO.get("styleId").toString()));
        attributeDTO.put("companyId", Integer.valueOf(paramDTO.get("companyId").toString()));
        ExtendedCRUDDirector.i.create(attributeDTO, resultDTO, true);
    }

    public void update() {
        StyleAttributeDTO attributeDTO = new StyleAttributeDTO();
        attributeDTO.putAll(paramDTO);
        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE, attributeDTO, resultDTO);
    }

    public void delete() {
        StyleAttributeDTO attributeDTO = new StyleAttributeDTO();
        attributeDTO.put("attributeId", paramDTO.get("attributeId"));
        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_DELETE, attributeDTO, resultDTO);
    }
}
