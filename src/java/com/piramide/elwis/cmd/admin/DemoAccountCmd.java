package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.DemoAccount;
import com.piramide.elwis.dto.admin.DemoAccountDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class DemoAccountCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing DemoAccountCmd................" + paramDTO);
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

    private DemoAccountDTO composeDemoAccountDTOForCreate() {
        DemoAccountDTO demoAccountDTO = new DemoAccountDTO();
        demoAccountDTO.put("lastName", paramDTO.get("lastName"));
        demoAccountDTO.put("firstName", paramDTO.get("firstName"));
        demoAccountDTO.put("companyName", paramDTO.get("companyName"));
        demoAccountDTO.put("email", paramDTO.get("email"));
        demoAccountDTO.put("phoneNumber", paramDTO.get("phoneNumber"));
        demoAccountDTO.put("isAlreadyCreated", Boolean.FALSE);
        demoAccountDTO.put("companyLogin", composeCompanyLogin());

        String userLogin = composeUserLogin();
        demoAccountDTO.put("userLogin", userLogin);
        demoAccountDTO.put("password", userLogin);

        return demoAccountDTO;
    }

    private void read() {
        boolean checkReferences = ("true".equals(paramDTO.get("withReferences")));
        Integer demoAccountId = new Integer(paramDTO.get("demoAccountId").toString());

        DemoAccount demoAccount = (DemoAccount) ExtendedCRUDDirector.i.read(new DemoAccountDTO(demoAccountId), resultDTO, checkReferences);
    }

    private void create() {
        DemoAccountDTO demoAccountDTO = composeDemoAccountDTOForCreate();
        DemoAccount demoAccount = (DemoAccount) ExtendedCRUDDirector.i.create(demoAccountDTO, resultDTO, false);
    }

    private void update() {
        DemoAccountDTO demoAccountDTO = new DemoAccountDTO(paramDTO);
        DemoAccount demoAccount = (DemoAccount) ExtendedCRUDDirector.i.update(demoAccountDTO, resultDTO, false, true, true, "Fail");
    }

    private void delete() {
        DemoAccountDTO demoAccountDTO = new DemoAccountDTO(paramDTO);
        ExtendedCRUDDirector.i.delete(demoAccountDTO, resultDTO, true, "Fail");
    }

    private String composeCompanyLogin() {
        Integer index = PKGenerator.i.nextKey(AdminConstants.DEMO_COMPANYLOGIN_INDEX);
        String companyLoginPrefix = ConfigurationFactory.getConfigurationManager().getValue("elwis.demoAccount.companyLogin.prefix");

        return companyLoginPrefix + index;
    }

    private String composeUserLogin() {
        String userLogin = null;

        String lastName = (String) paramDTO.get("lastName");
        if (lastName != null) {
            lastName = lastName.trim();

            int emptyCharIndex = lastName.indexOf(" ");
            if (emptyCharIndex >= 3) {
                userLogin = lastName.substring(0, emptyCharIndex);
            } else {
                userLogin = lastName;
            }

            if (userLogin.length() > 20) {
                userLogin = userLogin.substring(0, 19);
            }
        }

        return userLogin;
    }
}
