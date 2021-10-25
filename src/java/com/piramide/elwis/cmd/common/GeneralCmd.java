package com.piramide.elwis.cmd.common;

import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: GeneralCmd.java 10013 2010-12-14 18:08:15Z ivan $
 */
public class GeneralCmd extends EJBCommand {
    private Log log = LogFactory.getLog(GeneralCmd.class);
    /**
     * Define if check duplicates. By default is true.
     */
    protected boolean checkDuplicate = true;
    /**
     * Define if check references. By default is true.
     */
    protected boolean checkReferences = true;
    /**
     * Define if is clearing Form. By default is true.
     */
    protected boolean isClearingForm = true;

    /**
     * Define if it's required to do version control
     */
    protected boolean checkVersion = true;

    public EJBLocalObject executeInStateless(SessionContext ctx, ParamDTO paramDTO, Class dtoName) {
        try {
            ComponentDTO dto = (ComponentDTO) Class.forName(dtoName.getName()).newInstance();
            dto.putAll(paramDTO);
            return execute(ctx, dto);
        } catch (ClassNotFoundException e) {
            log.error("GeneralCmd ClassNotFoundException" + e);
        } catch (IllegalAccessException e) {
            log.error("GeneralCmd IllegalAccessException" + e);
        } catch (InstantiationException e) {
            log.error("GeneralCmd InstantiationException" + e);
        }
        return null;
    }

    public EJBLocalObject execute(SessionContext ctx, ComponentDTO dto) {
        EJBLocalObject bean = null;
        if (CRUDDirector.OP_CREATE.equals(getOp())) {
            bean = create(dto, ctx);
        } else if (CRUDDirector.OP_UPDATE.equals(getOp())) {
            bean = update(dto, ctx);
        } else if (CRUDDirector.OP_DELETE.equals(getOp())) {
            delete(dto);
        } else {
            bean = read(dto, ctx);
        }
        return bean;
    }


    private EJBLocalObject create(ComponentDTO dto, SessionContext ctx) {

        return ExtendedCRUDDirector.i.create(dto, resultDTO, checkDuplicate);

    }

    protected EJBLocalObject update(ComponentDTO dto, SessionContext ctx) {
        return ExtendedCRUDDirector.i.update(dto, resultDTO, checkDuplicate, checkVersion, isClearingForm, "Fail");
    }

    private void delete(ComponentDTO dto) {
        ExtendedCRUDDirector.i.delete(dto, resultDTO, checkReferences, "Fail");
    }

    protected EJBLocalObject read(ComponentDTO dto, SessionContext ctx) {
        return ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
    }

    public boolean isStateful() {
        return false;
    }


}
