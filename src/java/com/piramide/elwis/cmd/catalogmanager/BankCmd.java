package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.AddressBank;
import com.piramide.elwis.domain.catalogmanager.Bank;
import com.piramide.elwis.dto.catalogmanager.AddressBankDTO;
import com.piramide.elwis.dto.catalogmanager.BankDTO;
import com.piramide.elwis.utils.DuplicatedEntryChecker;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the Bank
 *
 * @author Ivan
 * @version $Id: BankCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class BankCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        log.debug("Executing BankCmd...**");
        if ("create".equals(getOp())) {
            isRead = false;
            create();
        }
        if ("update".equals(getOp())) {
            isRead = false;
            try {
                update();
            } catch (Exception e) {
                log.debug("Exception on BankCmd...");
                ctx.setRollbackOnly();
            }
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }
        if (isRead == true) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void create() {

        String isAddress = (String) paramDTO.get("isAddress");
        Bank bank = null;
        BankDTO bankDTO = new BankDTO(paramDTO);
        DuplicatedEntryChecker.i.check(bankDTO, resultDTO, this);

        if (resultDTO.isFailure()) {
            return;
        }

        bank = (Bank) CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, bankDTO, resultDTO);

        AddressBankDTO mydto = new AddressBankDTO();
        mydto.put("name", paramDTO.get("bankName"));
        mydto.put("companyId", paramDTO.get("companyId"));
        if (isAddress != null) {
            AddressBank address = (AddressBank) EJBFactory.i.createEJB(mydto);
            bank.setAddress(address);
            bank.setBankName(null);
        } else {
            bank.setBankName((String) paramDTO.get("bankName"));
        }

        resultDTO.put("bankId", bank.getBankId());
        resultDTO.put("contactId", paramDTO.get("contactId"));
    }

    private void update() throws RemoveException {
        BankDTO bankDTO;
        Bank bank = null;
        String isAddress = (String) paramDTO.get("isAddress");
        VersionControlChecker.i.check(new BankDTO(paramDTO), resultDTO, paramDTO);
        bankDTO = new BankDTO(paramDTO);
        if (resultDTO.get("EntityBeanNotFound") != null) {
            new BankDTO(paramDTO).addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }
        if (resultDTO.isFailure()) {
            CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, bankDTO, resultDTO);
            return;
        } else {
            DuplicatedEntryChecker.i.check(bankDTO, resultDTO, this);
            if (resultDTO.isFailure()) {
                return;
            }
            bank = (Bank) CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, bankDTO, resultDTO);
            if (isAddress != null) {
                if (bank.getAddress() == null) {
                    AddressBankDTO mydto = new AddressBankDTO();
                    mydto.put("name", bank.getBankName());
                    mydto.put("companyId", bank.getCompanyId());
                    AddressBank address = (AddressBank) EJBFactory.i.createEJB(mydto);
                    bank.setAddress(address);
                    bank.setBankName(null);
                }
            } else {
                if (bank.getAddress() != null) {
                    bank.setBankName(bank.getAddress().getName());
                    bank.getAddress().remove();
                    bank.setAddress(null);
                }
            }
        }
    }

    private void delete() {
        BankDTO bankDTO = new BankDTO(paramDTO);
        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, bankDTO, resultDTO);
        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }
    }

    private void read() {
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new BankDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, new BankDTO(paramDTO), resultDTO);
        if (resultDTO.isFailure()) {
            return;
        }
    }
}