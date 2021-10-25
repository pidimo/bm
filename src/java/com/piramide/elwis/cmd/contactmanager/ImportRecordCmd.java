package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.ImportRecord;
import com.piramide.elwis.domain.contactmanager.RecordColumnHome;
import com.piramide.elwis.dto.contactmanager.ImportRecordDTO;
import com.piramide.elwis.dto.contactmanager.RecordColumnDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ImportRecordCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ImportRecordCmd................" + paramDTO);
        boolean isRead = true;

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

        if (isRead) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void read() {
        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));

        ImportRecordDTO importRecordDTO = new ImportRecordDTO(paramDTO);
        ImportRecord importRecord = (ImportRecord) ExtendedCRUDDirector.i.read(importRecordDTO, resultDTO, checkReferences);
    }

    private void create() {
        ImportRecordDTO importRecordDTO = new ImportRecordDTO(paramDTO);

        ImportRecord importRecord = (ImportRecord) ExtendedCRUDDirector.i.create(importRecordDTO, resultDTO, false);

        if (importRecord != null) {
            createRecordColumns(importRecord);
            resultDTO.put("importRecordId", importRecord.getImportRecordId());
        }
    }

    private void update() {
        ImportRecordDTO importRecordDTO = new ImportRecordDTO(paramDTO);

        ImportRecord importRecord = (ImportRecord)  ExtendedCRUDDirector.i.update(importRecordDTO, resultDTO, false, true, true, "Fail");

        if (importRecord != null && !resultDTO.isFailure()) {
        }
    }

    private void delete() {
        ImportRecordDTO importRecordDTO = new ImportRecordDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(importRecordDTO, resultDTO, true, "Fail");
    }

    private void createRecordColumns(ImportRecord importRecord) {
        RecordColumnHome recordColumnHome = (RecordColumnHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDCOLUMN);

        if (paramDTO.get("recordColumnMapList") != null) {
            List<Map> recordColumnList = (List<Map>) paramDTO.get("recordColumnMapList");

            for (Map columnMap : recordColumnList) {
                RecordColumnDTO recordColumnDTO = new RecordColumnDTO();
                recordColumnDTO.put("importRecordId", importRecord.getImportRecordId());
                recordColumnDTO.put("importColumnId", columnMap.get("importColumnId"));
                recordColumnDTO.put("companyId", importRecord.getCompanyId());
                recordColumnDTO.put("columnValue", columnMap.get("columnValue"));
                recordColumnDTO.put("columnIndex", columnMap.get("columnIndex"));

                try {
                    recordColumnHome.create(recordColumnDTO);
                } catch (CreateException e) {
                    log.error("Error in create record column to import record:" + importRecord.getImportRecordId(), e);
                }
            }
        }
    }

}
