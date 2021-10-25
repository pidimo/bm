package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.supportmanager.SupportCase;
import com.piramide.elwis.domain.supportmanager.SupportCaseHome;
import com.piramide.elwis.domain.supportmanager.SupportUser;
import com.piramide.elwis.domain.supportmanager.SupportUserHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class AutoAssignateUserCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    SessionContext ctx;
    private int userType;
    public static final int ROOT = 0;
    public static final int ANOTHER_SUPPORT_USER = 1;
    public static final int SUPPORT_USER = 2;
    String isProduct;

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing  ... SupportCaseCmd     ...");
        this.ctx = ctx;
    }

    public Integer getPossibleUserForAssignate(Map dto) {

        Integer toUserId = (Integer) dto.get("toUserId");

        if ("true".equals(dto.get("auto_assigned"))) {
            if (dto.get("isProduct") != null && !"".equals(dto.get("isProduct"))) {
                isProduct = dto.get("isProduct").toString();
            }
            toUserId = calculateSupportUser((String) dto.get("productId"), (Integer) dto.get("companyId"), isProduct);
            if (toUserId == null) {
                toUserId = (Integer) dto.get("toUserId");
            }
        }
        log.debug("PossibleUserForAssignate:" + toUserId);
        //if("true".equals("supportCreate")) toUserId = (Integer) dto.get("toUserId");
        return toUserId;
    }

    private Integer getValidUser(Collection users, Collection cases) {
        Integer toUserId = null;
        Map countAssigned = new HashMap(users.size());
        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            SupportUser supportUser = (SupportUser) iterator.next();
            countAssigned.put(supportUser.getUserId(), new Integer(0));
        }
        //log.debug("countAssigned:\n" + countAssigned);
        for (Iterator iterator = cases.iterator(); iterator.hasNext();) {
            SupportCase supportCase = (SupportCase) iterator.next();
            if (countAssigned.containsKey(supportCase.getToUserId())) {
                Integer count = new Integer(((Integer) countAssigned.get(supportCase.getToUserId())).intValue() + 1);
                countAssigned.put(supportCase.getToUserId(), count);
            }
        }

        Integer lessValue = new Integer(Integer.MAX_VALUE);
        for (Iterator iterator = countAssigned.entrySet().iterator(); iterator.hasNext();) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (lessValue.intValue() > ((Integer) entry.getValue()).intValue()) {
                lessValue = (Integer) entry.getValue();
                toUserId = (Integer) entry.getKey();
            }
        }
        return toUserId;
    }

    /**
     * How work:<br>
     * <li>If not assigned users to support a product,
     * assign to another user that assigned to support another product</li>
     * <li>If not assigned users to any product,
     * assign to root company user
     *
     * @param productid
     * @param companyId
     * @return Valid userId for assignate the support case
     */

    private Integer calculateSupportUser(String productid, Integer companyId, String isProduct) {
        log.debug("calculateSupportUser:" + productid + " - " + companyId);
        try {

            Integer productId = new Integer(productid);
            Integer toUserId = null;
            //Integer caseTypeId = new Integer(casetypeid);
            SupportUserHome supportUserHome = (SupportUserHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_USER);
            SupportCaseHome supportCaseHome = (SupportCaseHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE);

            Collection usersAssignatedProductSupport = supportUserHome.findAllByProduct(productId, companyId);
            if (usersAssignatedProductSupport.isEmpty()) {
                Collection usersAssignatedAnotherProductSupport = supportUserHome.findAllByCompany(companyId);

                if (!usersAssignatedAnotherProductSupport.isEmpty()) {
                    userType = ANOTHER_SUPPORT_USER;
                    if (usersAssignatedAnotherProductSupport.size() == 1) {
                        toUserId = ((SupportUser) usersAssignatedAnotherProductSupport.iterator().next()).getUserId();
                    } else {
                        Collection casesFromOtherSupportUsers = supportCaseHome.findAllByNotCloseStage(companyId);
                        toUserId = getValidUser(usersAssignatedAnotherProductSupport, casesFromOtherSupportUsers);
                    }
                } else {
                    userType = ROOT;
                    UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
                    User root = userHome.findRootUserByCompany(companyId);
                    toUserId = root.getUserId();
                }

                if (SupportConstants.TRUE_VALUE.equals(isProduct)) {
                    toUserId = null;
                }
            } else {
                userType = SUPPORT_USER;
                if (usersAssignatedProductSupport.size() == 1) {
                    toUserId = ((SupportUser) usersAssignatedProductSupport.iterator().next()).getUserId();
                } else {
                    Collection cases = supportCaseHome.findAllByProductWhithNotCloseStage(companyId, productId);
                    if (cases.isEmpty()) {
                        toUserId = ((SupportUser) usersAssignatedProductSupport.iterator().next()).getUserId();
                    } else {
                        toUserId = getValidUser(usersAssignatedProductSupport, cases);
                    }
                }
            }
            return toUserId;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
    }

    public int getUserType() {
        return userType;
    }

    public boolean isStateful() {
        return false;
    }
}
