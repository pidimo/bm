package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Date;

/**
 * Employee Comand, bussines Logic realize to manage employess from organizations
 * Create, Update, Delete an Read operations
 *
 * @author Titus
 * @version EmployeeCmd.java, v 2.0 May 14, 2004 12:39:11 PM
 */

public class EmployeeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing EmployeeCmd ... " + paramDTO);
        Address address = null;

        if ("update".equals(getOp())) {
            log.info("update data...");

            paramDTO.put("company", new Integer((String) paramDTO.get("organizationId")));
            EmployeeDTO employeeDTO = new EmployeeDTO(paramDTO);
            Employee employee = (Employee) ExtendedCRUDDirector.i.update(employeeDTO, resultDTO, false, true, false, "Fail");

            if (employee != null) {
                if (resultDTO.isFailure()) {// if version control number not are equals, therefore only read again and return

                    resultDTO.put("name1", paramDTO.get("name1"));
                    resultDTO.put("name2", paramDTO.get("name2"));

                    // restoring addressId of address that contact person belong.
                    if (employee.getHealthFund() != null) { //put health Fundation Name if exists
                        String healthFundName = employee.getHealthFund().getName();
                        resultDTO.put("healthFundName", healthFundName);
                        resultDTO.put("importhealthFundName", healthFundName);
                    } else {
                        resultDTO.put("healthFundName", "");
                        resultDTO.put("importhealthFundName", "");
                    }
                    resultDTO.put("addressId", employee.getEmployeeId());
                    return; // return
                }
            }
            return;
        }

        if (!paramDTO.hasOp()) { // read data
            log.debug("read data...");

            if (paramDTO.get("employeeId") != null && !"".equals(paramDTO.get("employeeId"))) {
                log.debug("enter here");
                paramDTO.put("addressId", paramDTO.get("employeeId"));
                try {
                    address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO));
                } catch (EJBFactoryException ex) {
                    // bean not found in Operation whit Address
                    new AddressDTO().addNotFoundMsgTo(resultDTO);
                    resultDTO.setForward("Fail");
                    return;
                }
                resultDTO.put("name1", address.getName1());
                resultDTO.put("name2", address.getName2());
                resultDTO.put("searchName", address.getSearchName());

            } else if (paramDTO.get("addressId") != null && !"".equals(paramDTO.get("addressId"))) {
                paramDTO.put("employeeId", new Integer(paramDTO.get("addressId").toString()));
            }

            log.debug("values before to op " + getOp() + " :" + paramDTO);
            Employee employee = (Employee) ExtendedCRUDDirector.i.read(new EmployeeDTO(paramDTO), resultDTO, true);

            if (employee != null) {
                if (employee.getHealthFund() != null) { //put health Fundation Name if exists
                    String healthFundName = employee.getHealthFund().getName();
                    resultDTO.put("healthFundName", healthFundName);
                    resultDTO.put("importhealthFundName", healthFundName);
                } else {
                    resultDTO.put("healthFundName", "");
                    resultDTO.put("importhealthFundName", "");
                }
            }
        }

        if ("create".equals(getOp())) {
            paramDTO.put("addressType", ContactConstants.ADDRESSTYPE_PERSON);
            paramDTO.put("company", new Integer((String) paramDTO.get("organizationId")));
            if (paramDTO.get("recordUserId") != null) {
                paramDTO.put("recordUserId", new Integer((String) paramDTO.get("recordUserId")));
            }
            if (paramDTO.get("companyId") != null) {
                paramDTO.put("companyId", new Integer((String) paramDTO.get("companyId")));
            }


            log.debug("values before to op " + getOp() + " :" + paramDTO);
            paramDTO.put("employeeId", new Integer((String) paramDTO.get("employeeAddresId")));

            log.debug("employeeID: " + paramDTO.get("employeeAddresId"));
            EmployeeDTO employeeDTO = new EmployeeDTO(paramDTO);

            ExtendedCRUDDirector.i.create(employeeDTO, resultDTO, false);
            if (resultDTO.isFailure()) {
                resultDTO.put("name1", paramDTO.get("name1"));
                resultDTO.put("name2", paramDTO.get("name2"));
                resultDTO.setForward("Fail");
                return;
            }

            log.debug("enter from import addres");
            paramDTO.put("addressId", new Integer((String) paramDTO.get("employeeAddresId")));

            try {
                address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO)); //find  person an update data

                Byte newCode = new Byte(CodeUtil.addCode(address.getCode(), CodeUtil.employee));

                address.setCode(newCode);   // new code = last code + code employee
                address.setLastModificationUserId((Integer) paramDTO.get("recordUserId"));     // modify register imformation
                address.setLastModificationDate(DateUtils.dateToInteger(new Date(System.currentTimeMillis())));
                address.setVersion(new Integer(address.getVersion().intValue() + 1)); //ensure concurrency checking
                log.debug("code: " + newCode);
            } catch (EJBFactoryException ex) {
                resultDTO.put("name1", paramDTO.get("name1"));
                resultDTO.put("name2", paramDTO.get("name2"));
                new EmployeeDTO().addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
            paramDTO.remove("addresId");
            return;
        }

        //Delete Operation
        if (paramDTO.get("delete") != null || "delete".equals(getOp())) {
            log.info("delete data   ...");
            EmployeeDTO dto = new EmployeeDTO(paramDTO);

            try {

                EJBFactory.i.removeEJB(dto);
                paramDTO.put("addressId", new Integer((String) paramDTO.get("employeeId")));
                address = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO));
                Byte newCode = new Byte(CodeUtil.removeCode(address.getCode().byteValue(), CodeUtil.employee));
                address.setCode(newCode);
                address.setLastModificationUserId(new Integer(paramDTO.get("recordUserId").toString()));
                address.setLastModificationDate(DateUtils.dateToInteger(new Date(System.currentTimeMillis())));

                log.debug("code: " + newCode);
                resultDTO.setForward("Success");

            } catch (EJBFactoryException e) {
                log.debug("Employee to delete cannot be found...");
                // if not found has been deleted by other user
                ctx.setRollbackOnly();//invalid the transaction
                dto.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}