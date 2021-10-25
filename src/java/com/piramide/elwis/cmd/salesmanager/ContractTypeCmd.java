package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.salesmanager.ContractTypeDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * <p/>
 * This class handles CRUD operations (create, read, update and delete) for ContractType catalog.
 *
 * @author Ivan
 */
public class ContractTypeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ContractTypeCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        ContractTypeDTO dto = getContractTypeDTO();

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

    private void create(ContractTypeDTO contractTypeDTO) {
        ExtendedCRUDDirector.i.create(contractTypeDTO, resultDTO, false);
    }

    private void read(ContractTypeDTO contractTypeDTO, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(contractTypeDTO, resultDTO, checkReferences);
    }

    private void update(ContractTypeDTO contractTypeDTO) {
        ExtendedCRUDDirector.i.update(contractTypeDTO, resultDTO, false, true, false, "Fail");
    }

    private void delete(ContractTypeDTO contractTypeDTO) {
        ExtendedCRUDDirector.i.delete(contractTypeDTO, resultDTO, true, "Fail");
    }

    private ContractTypeDTO getContractTypeDTO() {
        ContractTypeDTO dto = new ContractTypeDTO();

        dto.put("name", paramDTO.get("name"));
        dto.put("tobeInvoiced", "true".equals(paramDTO.get("tobeInvoiced")));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("contractTypeId", getValueAsInteger("contractTypeId"));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        log.debug("-> Working on ContractTypeDTO = " + dto + " OK");
        return dto;
    }

    private Integer getValueAsInteger(String key) {
        Integer value = null;
        if (null != paramDTO.get(key) &&
                !"".equals(paramDTO.get(key).toString().trim())) {
            try {
                value = Integer.valueOf(paramDTO.get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + paramDTO.get(key) + " FAIL");
            }
        }

        return value;
    }

    public boolean isStateful() {
        return false;
    }
}
