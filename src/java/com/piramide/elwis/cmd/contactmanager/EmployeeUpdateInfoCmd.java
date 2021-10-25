package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Date;

/**
 * Employee Comand, bussines Logic encarge to manage employess from persons
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version EmployeeUpdateInfoCmd.java, v 2.0 May 24, 2004 11:47:00 AM
 */
public class EmployeeUpdateInfoCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update command..." + paramDTO);
        log.debug("Operation = " + getOp());

        if (!paramDTO.hasOp()) {
            read();
            return;
        }

        if (paramDTO.get("delete") != null) {// checking if is referenced

            log.debug("checking if employee to delete is referenced");
            Address address = null;
            EmployeeDTO dto = new EmployeeDTO(paramDTO);

            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) {
                //is referenced
                read();
                return;
            }

            log.debug("Deleting the employee");

            try {
                EJBFactory.i.removeEJB(dto);
                paramDTO.put("addressId", paramDTO.get("employeeId"));

                address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO));
                Byte newCode = new Byte(CodeUtil.removeCode(address.getCode().byteValue(), CodeUtil.employee));
                address.setCode(newCode);
                address.setLastModificationUserId(new Integer(paramDTO.get("recordUserId").toString()));
                address.setLastModificationDate(DateUtils.dateToInteger(new Date(System.currentTimeMillis())));
                log.debug("code: " + newCode);

                resultDTO.put("addressId", paramDTO.get("employeeId"));
                resultDTO.setForward("SuccessDelete");

            } catch (EJBFactoryException e) {

                log.debug("Employee to delete cannot be found...");
                // if not found has been deleted by other user
                ctx.setRollbackOnly();//invalid the transaction
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.put("addressId", paramDTO.get("employeeId"));
                resultDTO.setForward("Fail");
                return;
            }
            return;
        }

        if (paramDTO.get("save") != null) {

            log.debug("update data...");
            paramDTO.put("company", new Integer((String) paramDTO.get("companyId")));
            EmployeeDTO employeeDTO = new EmployeeDTO(paramDTO);

            log.debug("values before to op " + getOp() + " :" + paramDTO);
            Employee employee = (Employee) ExtendedCRUDDirector.i.update(employeeDTO, resultDTO, false, true, false, "Fail");

            if (employee != null) {
                if (resultDTO.isFailure()) {
                    log.debug("version control checker has report an concurrent modification");
                    // restoring addressId of address that contact person belong.
                    resultDTO.put("addressId", employee.getEmployeeId());

                    if (employee.getHealthFund() != null) { //put health Fundation Name if exists
                        String healthFundName = employee.getHealthFund().getName();
                        resultDTO.put("healthFundName", healthFundName);
                        resultDTO.put("importhealthFundName", healthFundName);
                    } else {
                        resultDTO.put("healthFundName", "");
                        resultDTO.put("importhealthFundName", "");
                    }
                    return;
                }
            } else {
                log.debug("not found");
                resultDTO.put("addressId", paramDTO.get("employeeId"));
                return;
            }
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void read() {
        if (paramDTO.get("employeeId") == null) {
            paramDTO.put("employeeId", new Integer(paramDTO.get("addressId").toString()));
        }

        Employee employee = (Employee) ExtendedCRUDDirector.i.read(new EmployeeDTO(paramDTO), resultDTO, false);
        if (employee != null) {
            if (employee.getHealthFund() != null) {
                String healthFundName = employee.getHealthFund().getName();

                resultDTO.put("healthFundName", healthFundName);
                resultDTO.put("importhealthFundName", healthFundName);
            } else {
                resultDTO.put("healthFundName", "");
                resultDTO.put("importhealthFundName", "");
            }
        } else {
            resultDTO.put("addressId", paramDTO.get("employeeId"));
            resultDTO.setForward("Fail");
            return;
        }
    }
}
