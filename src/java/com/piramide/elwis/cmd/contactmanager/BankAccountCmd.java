package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.BankAccount;
import com.piramide.elwis.domain.contactmanager.BankAccountHome;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.BankAccountDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * BankAcount Comand, bussines Logic encarge to manage Bank accounts
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version BankAccountCmd.java, v 2.0 May 10, 2004 11:24:32 AM
 */
public class BankAccountCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing BankAccountCmd");
        BankAccount bankAccount = null;
        Address address = null;
        /* Chek References from list departments layaout*/
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new BankAccountDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        /* Delete operation from update layout*/
        if (paramDTO.get("delete") != null || "delete".equals(getOp())) {
            address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO));
            Integer defaultBankAccountId = address.getBankAccountId();

            Integer actualBankAccountId = new Integer(paramDTO.get("bankAccountId").toString());
            if (actualBankAccountId.equals(defaultBankAccountId)) {
                address.setBankAccountId(null);
            }

            BankAccountDTO dto = new BankAccountDTO(paramDTO);
            //IntegrityReferentialChecker.i.check(dto, resultDTO);

            if (resultDTO.isFailure()) {
                return;
            }
            ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, dto, resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
                return;
            }
            resultDTO.setForward("Success");
            return;
        }
        if ("update".equals(getOp())) {
            log.debug("update data");
            BankAccountDTO dto = new BankAccountDTO(paramDTO);
            VersionControlChecker.i.check(dto, resultDTO, paramDTO);
            if (resultDTO.get("EntityBeanNotFound") != null) { // bean not found in version control
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (resultDTO.isFailure()) {// if version control number not are equals, therefore only read again and return
                log.debug("version control checker has report an concurrent modification, reading office again");
                resultDTO.setForward("VersionUpdate"); // ready to read bean again
                BankAccount BankAccount = (BankAccount) CRUDDirector.i.read(dto, resultDTO); // read
                if (resultDTO.isFailure()) {
                    resultDTO.setForward("Fail"); // if department was deleted by other user
                    return;
                }
                resultDTO.put("departmentId", BankAccount.getBankAccountId());
                CRUDDirector.i.read(new BankAccountDTO(resultDTO), resultDTO);
                paramDTO.setOp(CRUDDirector.OP_READ);
                return; // return
            }
            bankAccount = (BankAccount) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, new BankAccountDTO(paramDTO), resultDTO);
        }
        if ("create".equals(getOp())) {
            bankAccount = (BankAccount) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, new BankAccountDTO(paramDTO), resultDTO);
        }


        if ("create".equals(getOp()) || "update".equals(getOp())) {
            paramDTO.put("addressId", new Integer((String) paramDTO.get("addressId")));
            try {
                address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO));
            } catch (EJBFactoryException ex) {
                // bean not found in Operation whit Address
                new AddressDTO().addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            if (paramDTO.get("defaultBankAccount") != null) {
                log.debug("bankAccountId:" + resultDTO.get("bankAccountId"));
                address.setBankAccountId((Integer) resultDTO.get("bankAccountId"));
            } /*else if ("update".equals(getOp())) {
                address.setBankAccountId(null);
            }*/
        }

        if ("isOnlyOneAccount".equals(getOp())) {
            isOnlyOneBankAccount();
        }

        if ("".equals(getOp())) {
            log.debug("read data...");
            bankAccount = (BankAccount) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, new BankAccountDTO(paramDTO), resultDTO);
            try {
                address = (Address) EJBFactory.i.findEJB(new AddressDTO(resultDTO));
            } catch (EJBFactoryException ex) {
                // bean not found in Operation whit Address
                new AddressDTO().addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;

            }
            if (resultDTO.get("bankAccountId").equals(address.getBankAccountId())) {
                resultDTO.put("defaultBankAccount", "on");
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * Verify if is there registered only one bank account in company for this address
     * and set the bank account id in resultDTO
     */
    private void isOnlyOneBankAccount() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer bankAccountId = null;

        BankAccountHome bankAccountHome = (BankAccountHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_BANKACCOUNT);
        try {
            Collection bankAccounts = bankAccountHome.findByCompanyId(companyId, addressId);

            //get id only if has registered one bank account
            if (bankAccounts.size() == 1) {
                BankAccount bankAccount = (BankAccount) bankAccounts.iterator().next();
                bankAccountId = bankAccount.getBankAccountId();
            }
        } catch (FinderException e) {
            log.debug("Error in execute BankAccount finder..");
        }

        resultDTO.put("bankAccountId", bankAccountId);
    }
}
