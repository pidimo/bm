package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.cmd.admin.CompanyReadLightCmd;
import com.piramide.elwis.cmd.admin.LightlyRoleCmd;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.*;

/**
 * Form ta make validate usar information
 *
 * @author
 * @version UserInfoForm.java, v 2.0 Jul 1, 2004 6:35:31 PM
 */

public class UserInfoForm extends DefaultForm {

    private Log log = LogFactory.getLog(UserInfoForm.class);

    public Object[] getDefineRoles() {
        List list = (List) getDto("defineRoles");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setDefineRoles(Object[] array21) {
        if (array21 != null) {
            setDto("defineRoles", Arrays.asList(array21));
        }
    }

    public Object[] getUndefineRoles() {
        List list = (List) getDto("undefineRoles");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setUndefineRoles(Object[] list1) {
        this.setDto("undefineRoles", Arrays.asList(list1));
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (!isSaveButtonPressed(request)) {
            ActionErrors errors = new ActionErrors();
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");

            reWriteRoles(request);

            //to manage mobile hide conditions
            if (getDto("mobileActive") != null) {
                setDto("mobileActive", new Boolean(true));
            }
            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        validateAccessIP(locale, errors);// para validar el IP de donde accede

        validateEnableMobileAccess(request, errors);

        if (!errors.isEmpty()) {
            reWriteRoles(request);
        }
        return errors;
    }

    private void reWriteRoles(HttpServletRequest request) {
        List assignedRoles = (List) this.getDto("defineRoles");
        List availableRoles = (List) this.getDto("undefineRoles");

        Collection rolesByCompany = (Collection) request.getAttribute("rolesByCompany");

        if (rolesByCompany == null) {
            rolesByCompany = readRolesForIU(request);
        }

        Map rolesByType = separateAssignedRolesToAvailableRoles(rolesByCompany, assignedRoles, availableRoles);
        if ("create".equals(this.getDto("op"))) {
            request.setAttribute("jsLoad", "onLoad=\"userTypeChange()\"");
        }

        request.setAttribute("availableRoles", rolesByType.get("residual"));
        request.setAttribute("defineRoles", rolesByType.get("assigned"));
    }


    public void validateAccessIP(Locale locale, ActionErrors errors) {

        String ipIU = (String) this.getDto("accessIp");
        StringTokenizer ipAvailible = new StringTokenizer(ipIU.trim(), ",");
        StringBuffer ipOne = new StringBuffer();
        StringTokenizer ip = null;
        String oneIP;
        String value;
        int strc;

        while (ipAvailible.hasMoreTokens()) {

            oneIP = ipAvailible.nextToken();
            int charValue = 0;
            int index = 0;
            strc = 0;
            ip = new StringTokenizer(oneIP.trim(), ".");
            boolean errorNoEmpty = true;
            if (ip.countTokens() != 4 && ip.countTokens() > 0) {
                errorNoEmpty = false;
            }

            while (ip.hasMoreTokens() && errorNoEmpty && ip.countTokens() != 0) {

                value = null;
                value = ip.nextToken().trim();
                boolean val = true;
                ipOne.append(value);    //url
                val = FormatUtils.isValidDecimalNumber(value, locale, 3, 0);
                strc++;

                if (!val) {
                    if ((!"*".equals(value)) || strc == 1 && "*".equals(value)) {
                        errorNoEmpty = false;
                    }
                } else {
                    if ((new Integer(value)).intValue() > 255 || (new Integer(value)).intValue() < 0) {
                        errorNoEmpty = false;
                    }
                }
                if (ip.hasMoreTokens()) {
                    ipOne.append(".");
                }
            }
            while (index < oneIP.length() && errorNoEmpty) {
                if (".".equals(String.valueOf(oneIP.charAt(index)))) {
                    charValue++;
                }
                index++;
            }

            if (charValue != 3 && errorNoEmpty) {
                errorNoEmpty = false;
            }
            if (!errorNoEmpty) {
                errors.add("invalidIpAccess", new ActionError("error.invalid_ipaccess", oneIP));
            }
            if (ipAvailible.hasMoreTokens()) {
                ipOne.append(",").append(" ");
            }
        }
        if (errors.isEmpty()) {
            this.setDto("accessIp", ipOne.toString());
        }
    }


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        Collection rolesByCompany = readRolesForIU(request);
        request.setAttribute("availableRoles", (List) rolesByCompany);
        request.setAttribute("rolesByCompany", (List) rolesByCompany);
    }

    private Map separateAssignedRolesToAvailableRoles(Collection allRoles,
                                                      Collection assignedRolesIds,
                                                      Collection residualRolesIds) {
        Map roles = new HashMap();
        List assigned = new ArrayList();
        List residual = new ArrayList();

        for (Iterator iterator = allRoles.iterator(); iterator.hasNext();) {
            RoleDTO roleDTO = (RoleDTO) iterator.next();
            if (assignedRolesIds != null && assignedRolesIds.contains(roleDTO.get("roleId").toString())) {
                assigned.add(roleDTO);
            }
            if (residualRolesIds != null && residualRolesIds.contains(roleDTO.get("roleId").toString())) {
                residual.add(roleDTO);
            }
        }
        roles.put("assigned", assigned);
        roles.put("residual", residual);

        return roles;
    }

    private Collection readRolesForIU(HttpServletRequest request) {

        ResultDTO resultDTO = new ResultDTO();
        LightlyRoleCmd rolesCmd = new LightlyRoleCmd();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        rolesCmd.putParam("companyId", user.getValue("companyId"));
        try {
            resultDTO = BusinessDelegate.i.execute(rolesCmd, request);
        } catch (AppLevelException e) {
            log.debug("Cannot retrieve roles for this company...");
        }

        Collection rolesByCompany = (Collection) resultDTO.get("rolesByCompany");

        return rolesByCompany;
    }

    private void validateEnableMobileAccess(HttpServletRequest request, ActionErrors errors) {
        boolean isMobileEnabled;
        boolean isWVAppMobileEnabled;

        //define boolean values
        if (super.getDto("mobileActive") != null) {
            super.setDto("mobileActive", new Boolean(true));
            isMobileEnabled = true;
        } else {
            super.setDto("mobileActive", new Boolean(false));
            isMobileEnabled = false;
        }

        if (super.getDto("visibleMobileApp") != null) {
            super.setDto("visibleMobileApp", new Boolean(true));
        } else {
            super.setDto("visibleMobileApp", new Boolean(false));
        }

        if (super.getDto("enableMobileWVApp") != null) {
            super.setDto("enableMobileWVApp", new Boolean(true));
            isWVAppMobileEnabled = true;
        } else {
            super.setDto("enableMobileWVApp", new Boolean(false));
            isWVAppMobileEnabled = false;

            setDto("visibleMobileApp", new Boolean(false));
            setDto("mobileOrganizationId", null);
        }

        if (isMobileEnabled || isWVAppMobileEnabled) {
            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

            CompanyReadLightCmd companyReadLightCmd = new CompanyReadLightCmd();
            companyReadLightCmd.putParam("companyId", companyId);

            ResultDTO resultDTO = null;
            try {
                resultDTO = BusinessDelegate.i.execute(companyReadLightCmd, request);
            } catch (AppLevelException e) {
                log.error("Cannot execute " + CompanyReadLightCmd.class.getName(), e);
            }

            if (resultDTO != null) {
                Boolean companyMobileEnabled = (resultDTO.get("mobileActive") != null) ? (Boolean) resultDTO.get("mobileActive") : Boolean.FALSE;

                if (companyMobileEnabled) {
                    if (resultDTO.get("mobileUserAllowed") != null) {
                        Integer mobileUserAllowed = (Integer) resultDTO.get("mobileUserAllowed");

                        //check user allowed for bmapp
                        if (isMobileEnabled) {
                            Integer usersWithAccessEnabled = getCompanyUsersWithAccessToMobileEnabled(companyId);

                            if ((usersWithAccessEnabled + 1) > mobileUserAllowed) {
                                errors.add("userAcces", new ActionError("Company.mobile.ErrorUserAllowed", mobileUserAllowed));
                            }
                        }

                        //check user allowed for wvapp
                        if (isWVAppMobileEnabled) {
                            Integer wvappUsersWithAccessEnabled = getCompanyUsersWithAccessToWVAppEnabled(companyId);

                            if ((wvappUsersWithAccessEnabled + 1) > mobileUserAllowed) {
                                errors.add("userWVAppAcces", new ActionError("Company.mobile.wvapp.ErrorUserAllowed", mobileUserAllowed));
                            }
                        }
                    }
                } else {
                    errors.add("accessCompany", new ActionError("Company.mobile.ErrorInactiveCompany"));
                }
            }
        }

    }

    private Integer getCompanyUsersWithAccessToMobileEnabled(Integer companyId) {
        Integer countUsers = 0;

        Integer userId = null;
        if (getDto("userId")!= null && !GenericValidator.isBlankOrNull(getDto("userId").toString())) {
            userId = new Integer(getDto("userId").toString());
        }

        CompanyReadCmd readCmd = new CompanyReadCmd();
        readCmd.setOp("countUsersWithMobileAccessEnabled");
        readCmd.putParam("companyId", companyId);
        readCmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, null);
            if (resultDTO.get("usersWithMobileAccess") != null) {
                countUsers = (Integer) resultDTO.get("usersWithMobileAccess");
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute " + CompanyReadCmd.class.getName(), e);
        }

        return countUsers;
    }

    private Integer getCompanyUsersWithAccessToWVAppEnabled(Integer companyId) {
        Integer countUsers = 0;

        Integer userId = null;
        if (getDto("userId")!= null && !GenericValidator.isBlankOrNull(getDto("userId").toString())) {
            userId = new Integer(getDto("userId").toString());
        }

        CompanyReadCmd readCmd = new CompanyReadCmd();
        readCmd.setOp("countUsersWithWVAppAccessEnabled");
        readCmd.putParam("companyId", companyId);
        readCmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, null);
            if (resultDTO.get("usersWithWVAppAccess") != null) {
                countUsers = (Integer) resultDTO.get("usersWithWVAppAccess");
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute " + CompanyReadCmd.class.getName(), e);
        }

        return countUsers;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("dto(save)");
    }


}
