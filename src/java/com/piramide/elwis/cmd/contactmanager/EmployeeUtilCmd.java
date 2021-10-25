package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.domain.contactmanager.EmployeeHome;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class EmployeeUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(EmployeeUtilCmd.class);

    public void executeInStateless(SessionContext ctx) {
        String op = getOp();
        if ("isEmployee".equals(op)) {
            Integer userAddressId = (Integer) paramDTO.get("userAddressId");
            isEmployee(userAddressId);
        }

        if ("companyEmployee".equals(op)) {
            getCompanyEmployees();
        }
    }

    private boolean isEmployee(Integer userAddressId) {
        EmployeeHome employeeHome =
                (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        try {
            employeeHome.findByPrimaryKey(userAddressId);
            resultDTO.put("isEmployee", true);
            return true;
        } catch (FinderException e) {
            resultDTO.put("isEmployee", false);
            return false;
        }
    }

    private void getCompanyEmployees() {
        List<EmployeeDTO> result = new ArrayList<EmployeeDTO>();

        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        Collection<Employee> employees = null;
        try {
            employees = employeeHome.findByCompanyId(companyId);
        } catch (FinderException e) {
            employees = new ArrayList<Employee>();
        }

        for (Employee employee : employees) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            DTOFactory.i.copyToDTO(employee, employeeDTO);

            Address address = employee.getAddress();
            employeeDTO.put("name", address.getName());

            result.add(employeeDTO);
        }

        resultDTO.put("companyEmployees", result);
    }


    public boolean isStateful() {
        return false;
    }
}
