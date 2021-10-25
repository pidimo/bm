package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.financemanager.AccountDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * <p/>
 * This class handles CRUD operations (create, read, update and delete) for Account catalog.
 *
 * @author Ivan
 */
public class AccountCmd extends EJBCommand {
    private Log log = LogFactory.getLog(AccountCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        AccountDTO dto = getAccountDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(dto);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(dto);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(dto);
        }
        if (isRead) {
            boolean checkReferences = (null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences")));
            read(dto, checkReferences);
        }
    }

    private void create(AccountDTO dto) {
        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    private void read(AccountDTO dto, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
    }

    private void update(AccountDTO dto) {
        ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, "Fail");
    }

    private void delete(AccountDTO dto) {
        ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
    }


    private AccountDTO getAccountDTO() {
        AccountDTO accountDTO = new AccountDTO();
        Integer accountId = null;
        if (null != paramDTO.get("accountId") &&
                !"".equals(paramDTO.get("accountId").toString().trim())) {
            try {
                accountId = Integer.valueOf(paramDTO.get("accountId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse accountId=" + paramDTO.get("accountId") + " FAIL");
            }
        }

        accountDTO.put("accountId", accountId);
        accountDTO.put("name", paramDTO.get("name"));
        accountDTO.put("companyId", paramDTO.get("companyId"));
        accountDTO.put("number", paramDTO.get("number"));
        accountDTO.put("version", paramDTO.get("version"));
        accountDTO.put("withReferences", paramDTO.get("withReferences"));

        log.debug("-> Working on AccountDTO=" + accountDTO + " OK");
        return accountDTO;
    }

    public boolean isStateful() {
        return false;
    }
}
