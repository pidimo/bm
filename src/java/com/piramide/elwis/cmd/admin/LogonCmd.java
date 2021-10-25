package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.cmd.webmailmanager.EmailUserCmd;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.EncryptUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Check if one user is valid inside a company when trying to logon in application
 *
 * @author Mauren Carrasco
 * @version 4.2.1
 */

public class LogonCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(LogonCmd.class);

    @Override
    public boolean isReadOnly() {
        return true;
    }

    public void executeInStateless(SessionContext ctx) {
        String companyLogin = paramDTO.getAsString("companyLogin");
        String login = paramDTO.getAsString("login");
        User userLogin = null;
        User user = null;

        //validate the user and the company

        if (!paramDTO.hasOp() && isValidCompany(companyLogin)) {

            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            try {
                userLogin = userHome.findUserLogonWithCompany(login, companyLogin);
                user = userHome.findUserWithinCompanyLogon(login, EncryptUtil.i.encryt(paramDTO.getAsString("password")), companyLogin);
                CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
                Company company = companyHome.findByName(companyLogin);
                Collection users;
                users = userHome.findUserByCompanyAndActiveUsers(company.getCompanyId(), Boolean.TRUE);

                if (company.getUsersAllowed() != null && !"".equals(company.getUsersAllowed())) {
                    if (company.getUsersAllowed().intValue() < users.size()) {
                        //pregunta si es Root
                        if (user.getIsDefaultUser().booleanValue()) {
                            Integer total = new Integer(users.size() - company.getUsersAllowed().intValue());
                            resultDTO.addResultMessage("Admin.ErrorUserAllowed", company.getUsersAllowed().toString(), total.toString());
                        } else {//sino es root no le deja entrar al sistema
                            resultDTO.setResultAsFailure();
                            resultDTO.addResultMessage("Admin.ErrorUserExceeds");
                            //  return;
                        }
                    }
                }

                if (checkForActiveUser()) {
                    if (!user.getActive()) {
                        resultDTO.addResultMessage("Admin.ErrorInactiveUser");
                        resultDTO.setResultAsFailure();
                        return;
                    }
                }

                //read needed attributes
                readUserAndCompanyAttributes(user, resultDTO);

                //read webmail configurations
                readWebmailAttributes(user, resultDTO, ctx);

                processSecurityAccessRights(user, resultDTO);

                //If user has restricted the IP access, it must be notified.
                if (user.getAccessIp() != null && !"".equals(user.getAccessIp())) {
                    log.debug(".. currentIP ...     " + paramDTO.get("ip"));
                    if (!validateAccessIp(paramDTO.get("ip").toString(), user)) {
                        //resultDTO.put("bannedUser", Constants.TRUE_VALUE);
                        resultDTO.addResultMessage("User.deniedAccessIp", paramDTO.get("ip"));
                        resultDTO.setResultAsFailure();
                        return;
                    }
                }

            } catch (ServiceUnavailableException e) {
                log.error("Encript Error!!!");
                resultDTO.setResultAsFailure();
            } catch (FinderException e) {
                log.debug("Invalid password or userName !");
                resultDTO.setResultAsFailure();
            }
            if (user == null && userLogin != null) {
                log.info("Invalid login attempt was detected [" + login + "/" + companyLogin + "]");
                resultDTO.addResultMessage("Common.passInvalid");
            } else if (userLogin == null) {
                log.info("Invalid login attempt was detected [" + login + "/" + companyLogin + "]");
                resultDTO.addResultMessage("Common.notLogin");
            }
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private boolean checkForActiveUser() {
        boolean checkActiveUser = true;

        if ("true".equals(paramDTO.get("isLoginFromWVApp"))) {
            checkActiveUser = false;
        }
        return checkActiveUser;
    }

    private boolean isValidCompany(String companyLogin) {
        try {
            CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
            Company company = companyHome.findByName(companyLogin);
            if (!company.getActive().booleanValue()) { // The company is inactive

                resultDTO.addResultMessage("Company.error.inactive");
                return false;
            }
            // ------------------ Control de tiempos para licencias
            Integer startLicense = company.getStartLicenseDate();
            Integer finishLicense = company.getFinishLicenseDate();
            Integer today = DateUtils.dateToInteger(new Date());

            if (startLicense != null && finishLicense != null &&
                    (today.intValue() < startLicense.intValue() || today.intValue() > finishLicense.intValue())) {
                resultDTO.put("msgError", "Admin.Error.Logon.InvervalLicense");
                resultDTO.put("msgErrorParam", startLicense);
                resultDTO.put("msgErrorParam2", finishLicense);
                resultDTO.setResultAsFailure();
                return false;

            } else if (startLicense != null && finishLicense == null && today.intValue() < startLicense.intValue()) {
                resultDTO.put("msgError", "Admin.Error.Logon.StartLicense");
                resultDTO.put("msgErrorParam", startLicense);
                resultDTO.setResultAsFailure();
                return false;
            } else if (startLicense == null && finishLicense != null && today.intValue() > finishLicense.intValue()) {
                resultDTO.put("msgError", "Admin.Error.Logon.FinishLicense");
                resultDTO.put("msgErrorParam", finishLicense);
                resultDTO.setResultAsFailure();
                return false;
            }
            //---------------------

            /*else
            if (company.getIsTrial().booleanValue() && // If is trial company and finish time period, the compnay pass to inactive
                    (company.getFinishLicenseDate() != null && DateUtils.dateToInteger(new Date()).compareTo(company.getFinishLicenseDate()) > 0))
            {
                //log.debug("IS FAIL PERIOD");
                resultDTO.addResultMessage("Company.error.ExpiredPeriod");
                return false;
            }*/

        } catch (FinderException e) {
            resultDTO.addResultMessage("Company.error.invalid");
            return false;
        }
        return true;
    }

    public boolean isStateful() {
        return false;
    }

    /**
     * Read the WebMail user preferences, the method execute <code>EmailUserCmd</code> command with
     * operation <code>'readUserConfiguration'</code>. After put the results in <code>ResultDTO</code> object.
     *
     * @param user      <code>User</code> object.
     * @param resultDTO <code>ResultDTO</code> object than store the results.
     * @param ctx       <code>SessionContext</code> object, context where the <code>EmailUserCmd</code> is executed.
     */
    private void readWebmailAttributes(User user, ResultDTO resultDTO, SessionContext ctx) {
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("readUserConfiguration");
        emailUserCmd.putParam("emailUserId", user.getUserId());
        emailUserCmd.executeInStateless(ctx);

        Boolean backgroundDownload = (Boolean) emailUserCmd.getResultDTO().get("backgroundDownload");
        if (null == backgroundDownload) {
            backgroundDownload = false;
        }

        resultDTO.put("backgroundDownload", backgroundDownload);
    }

    protected void readUserAndCompanyAttributes(User user, ResultDTO resultDTO) {
        /** Setting user values **/
        resultDTO.put("userId", user.getUserId());
        resultDTO.put(Constants.USER_ADDRESSID, user.getAddressId());
        resultDTO.put(Constants.USER_TYPE, user.getType().toString());
        resultDTO.put("login", user.getUserLogin());
        resultDTO.put("userLanguage", user.getFavoriteLanguage());
        resultDTO.put("userMaxRecentList", user.getMaxRecentList());
        resultDTO.put("userRowsPerPage", user.getRowsPerPage());
        resultDTO.put("userTimeout", user.getTimeout());
        resultDTO.put("hasMailAccount", user.getHasMailAccount());
        resultDTO.put("dateTimeZone", user.getTimeZone() != null ? DateTimeZone.forID(user.getTimeZone()) :
                DateTimeZone.getDefault());
        /** Getting company information **/
        resultDTO.put("companyId", user.getCompany().getCompanyId());
        resultDTO.put("maxAttachSize", user.getCompany().getMaxAttachSize());
        resultDTO.put("companyLogin", user.getCompany().getLogin());
        resultDTO.put("companyRowsPerPage", user.getCompany().getRowsPerPage());
        resultDTO.put("companyTheme", user.getCompany().getStyle());
        resultDTO.put("companyTimeout", user.getCompany().getTimeout());
        resultDTO.put("companyMailDomain", user.getCompany().getMailDomain());
        resultDTO.put("isDefaultCompany", user.getCompany().getIsDefault());
        resultDTO.put("companyType", user.getCompany().getCompanyType());

        //mobile access data
        resultDTO.put("companyMobileActive", user.getCompany().getMobileActive());
        resultDTO.put("mobileStartLicense", user.getCompany().getMobileStartLicense());
        resultDTO.put("mobileEndLicense", user.getCompany().getMobileEndLicense());
        resultDTO.put("userMobileActive", user.getMobileActive());

        //wvapp access data
        resultDTO.put("enableMobileWVApp", user.getEnableMobileWVApp());
    }

    /**
     * Process security access rights for the user
     */
    protected void processSecurityAccessRights(User user, ResultDTO resultDTO) {

        AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);

        Map accessRightsMap = new HashMap();
        Collection userRoles = user.getUserRole();

        for (Iterator iteratorRoles = userRoles.iterator(); iteratorRoles.hasNext(); ) {
            UserRole userRole = (UserRole) iteratorRoles.next();
            Role roleBean = userRole.getRole();
            Collection roleAccessRights;
            try {
                roleAccessRights = accessRightsHome.findAccessRightsByRole(roleBean.getRoleId(), roleBean.getCompanyId());
            } catch (FinderException e) {
                roleAccessRights = new ArrayList(0);//no access rights assigned to the role
            }

            for (Iterator iteratorAccessRights = roleAccessRights.iterator(); iteratorAccessRights.hasNext(); ) {
                AccessRights accessRight = (AccessRights) iteratorAccessRights.next();
                if (accessRight.getActive().booleanValue()) {
                    /** checking if the function code already exists in the accessrightsmap, if
                     * true it's necessary to update the permission Byte for access right
                     * through operations with the byte value contained in the map and the new value
                     * of the accessright permission.
                     */
                    if (accessRightsMap.containsKey(accessRight.getSystemFunction().getFunctionCode())) {
                        accessRightsMap.put(accessRight.getSystemFunction().getFunctionCode(),
                                new Byte(new Integer(((accessRight.getPermission().byteValue()) | ((Byte)
                                        accessRightsMap.get(accessRight.getSystemFunction().getFunctionCode())).byteValue())).byteValue()));

                    } else { //add the access right with the permission
                        accessRightsMap.put(accessRight.getSystemFunction().getFunctionCode(), accessRight.getPermission());
                    }
                }
            }
        }
        log.debug("Access Rights: " + accessRightsMap);
        //setting the access right map to resultDTO
        resultDTO.put("accessRights", accessRightsMap);
    }

    private boolean validateAccessIp(String currentIp, User user) {

        boolean validate = true;
        StringTokenizer userAccessIp = new StringTokenizer(user.getAccessIp(), ",");
        StringTokenizer currentIP_ = null;
        StringTokenizer ip = null;
        String oneIP;
        boolean error = false;
        boolean enter = false;
        int incr = 0;
        String a;
        String b;
        log.debug("       ... validateAccessIp ... function execute ....." + currentIp);

        while (userAccessIp.hasMoreTokens() && !enter) {
            ip = null;
            oneIP = userAccessIp.nextToken();
            ip = new StringTokenizer(oneIP.trim(), ".");
            currentIP_ = new StringTokenizer(currentIp, ".");
            a = ip.nextToken().trim();
            b = currentIP_.nextToken().trim();
            error = false;

            if (a.equals(b)) {// compara con el primer valor del IP, porq no puede ser comodin
                incr = 1;
                while (ip.hasMoreTokens() && currentIP_.hasMoreTokens() && !error) {
                    incr++;
                    String ipValue = currentIP_.nextToken();
                    String c = ip.nextToken();
                    if (!c.equals(ipValue.trim()) && !"*".equals(c.trim())) {
                        error = true;
                    } else {
                        error = false;
                    }
                }
            } else {
                error = true;
            }

            if (!error) {
                enter = true;
            }
        }
        if (!enter) {
            validate = false;
        }
        return validate;
    }


}
