package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.contactmanager.ImportRecord;
import com.piramide.elwis.domain.contactmanager.ImportRecordHome;
import com.piramide.elwis.domain.contactmanager.RecordDuplicateHome;
import com.piramide.elwis.dto.contactmanager.RecordDuplicateDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class RecordDuplicateCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing RecordDuplicateCmd..... " + paramDTO);

        String op = this.getOp();

        if ("batchCreate".equals(op)) {
            create(sessionContext);
        }
    }

    private void create(SessionContext ctx) {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Map importRecordIdMap = (Map) paramDTO.get("importRecordDuplicateMap");

        if (importRecordIdMap != null) {

            for (Iterator iterator = importRecordIdMap.keySet().iterator(); iterator.hasNext();) {
                Integer importRecordId = (Integer) iterator.next();
                List<Integer> addressIdList = (List<Integer>) importRecordIdMap.get(importRecordId);

                if (!addressIdList.isEmpty()) {
                    create(importRecordId, addressIdList, companyId, ctx);
                    updateImportRecordAsDuplicae(importRecordId, ctx);
                }

            }
        }
    }

    private void create(Integer importRecordId, List<Integer> addressIdList, Integer companyId, SessionContext ctx) {
        RecordDuplicateHome recordDuplicateHome = (RecordDuplicateHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_RECORDDUPLICATE);
        for (Integer addressId : addressIdList) {
            RecordDuplicateDTO recordDuplicateDTO = new RecordDuplicateDTO();
            recordDuplicateDTO.put("importRecordId", importRecordId);
            recordDuplicateDTO.put("addressId", addressId);
            recordDuplicateDTO.put("companyId", companyId);

            try {
                recordDuplicateHome.create(recordDuplicateDTO);
            } catch (CreateException e) {
                resultDTO.setResultAsFailure();
                log.error("Error in create record duplicate: importRecordId=" + importRecordId + " addressId=" + addressId, e);
            }
        }
    }

    private void updateImportRecordAsDuplicae(Integer importRecordId, SessionContext ctx) {
        ImportRecordHome importRecordHome = (ImportRecordHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_IMPORTRECORD);
        try {
            ImportRecord importRecord = importRecordHome.findByPrimaryKey(importRecordId);
            importRecord.setIsDuplicate(true);
        } catch (FinderException e) {
            log.debug("Not found import record:" + importRecordId);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
