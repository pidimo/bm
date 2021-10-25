package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Office;
import com.piramide.elwis.dto.contactmanager.OfficeDTO;
import com.piramide.elwis.utils.HashMapCleaner;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Office Comand, bussines Logic encarge to manage Officess
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version OfficeCmd.java, v 2.0 May 10, 2004 10:54:50 AM
 */
public class OfficeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing OfficeCmd");
        HashMapCleaner.clean(paramDTO);
        /* Chek References from list office layout*/
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new OfficeDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        /* Delete operation from update layout*/
        if (paramDTO.get("delete") != null || "delete".equals(getOp())) {
            OfficeDTO dto = new OfficeDTO(paramDTO);

            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }

            CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, dto, resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
                return;
            }
            resultDTO.setForward("Success");
            return;
        }
        /* Update operation form layout */
        if ("update".equals(getOp())) {
            log.debug("update data");
            OfficeDTO dto = new OfficeDTO(paramDTO);
            VersionControlChecker.i.check(dto, resultDTO, paramDTO);
            if (resultDTO.get("EntityBeanNotFound") != null) { // bean not found in version control
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (resultDTO.isFailure()) {// if version control number not are equals, therefore only read again and return
                log.debug("version control checker has report an concurrent modification, reading office again");
                resultDTO.setForward("VersionUpdate"); // ready to read bean again
                Office office = (Office) CRUDDirector.i.read(dto, resultDTO); // read
                if (resultDTO.isFailure()) {
                    resultDTO.setForward("Fail"); // if department was deleted by other user
                    return;
                }
                resultDTO.put("officeId", office.getOfficeId());
                CRUDDirector.i.read(new OfficeDTO(resultDTO), resultDTO);
                paramDTO.setOp(CRUDDirector.OP_READ);
                return; // return
            }

            dto.put("version", paramDTO.get("version"));
            Office office = (Office) CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, dto, resultDTO);
            if (resultDTO.isFailure()) { // if bean was removed
                resultDTO.setForward("Fail"); // if address has been deleted by other user
                return;
            }
            if (office != null) {

                if (office.getSupervisorId() != null && paramDTO.get("supervisorId") == null) {
                    office.setSupervisorId(null);
                }

                CRUDDirector.i.read(dto, resultDTO); // read updated values
            }
            return;

        }
        if ("create".equals(getOp())) {
            log.debug(paramDTO);
            CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, new OfficeDTO(paramDTO), resultDTO);
            return;
        }
        CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, new OfficeDTO(paramDTO), resultDTO);
    }

    public boolean isStateful() {
        return false;
    }
}
