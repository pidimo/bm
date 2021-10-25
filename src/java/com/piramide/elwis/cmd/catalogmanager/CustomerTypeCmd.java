package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.catalogmanager.CustomerTypeDTO;
import com.piramide.elwis.utils.DuplicatedEntryChecker;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the customer type
 *
 * @author Ivan
 * @version $Id: CustomerTypeCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class CustomerTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CustomerTypeCmd...");
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, CustomerTypeDTO.class);
        /*boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create();
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }
        if (isRead == true) read();*/
    }

    public boolean isStateful() {
        return false;
    }

    private void create() {
        CustomerTypeDTO customerTypeDTO = new CustomerTypeDTO(paramDTO);
        DuplicatedEntryChecker.i.check(customerTypeDTO, resultDTO, this);
        if (resultDTO.isFailure()) {
            return;
        }
        CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, customerTypeDTO, resultDTO);
    }

    private void update() {
        CustomerTypeDTO customerTypeDTO;
        VersionControlChecker.i.check(new CustomerTypeDTO(paramDTO), resultDTO, paramDTO);
        customerTypeDTO = new CustomerTypeDTO(paramDTO);
        if (resultDTO.get("EntityBeanNotFound") != null) {
            new CustomerTypeDTO(paramDTO).addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Cancel");
            return;
        }
        if (resultDTO.isFailure()) {
            CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, customerTypeDTO, resultDTO);
            return;
        } else {
            DuplicatedEntryChecker.i.check(customerTypeDTO, resultDTO, this);
            if (resultDTO.isFailure()) {
                return;
            }
            CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, customerTypeDTO, resultDTO);
        }
    }

    private void delete() {
        CustomerTypeDTO customerTypeDTO = new CustomerTypeDTO(paramDTO);
        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, customerTypeDTO, resultDTO);
        if (resultDTO.isFailure()) {
            resultDTO.setForward("Cancel");
            return;
        }
    }

    private void read() {
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new CustomerTypeDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, new CustomerTypeDTO(paramDTO), resultDTO);
        if (resultDTO.isFailure()) {
            return;
        }
    }
}

