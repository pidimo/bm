package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.CampaignActivity;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityUser;
import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactPK;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * assign user to all activity campaign contact
 *
 * @author Miky
 * @version $Id: ActivityContactAllResponsibleCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ActivityContactAllResponsibleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityContactAllResponsibleCmd................" + paramDTO);

        String criteria = paramDTO.get("criteria").toString();
        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);
        if (activity != null) {

            if (activity.getActivityUsers().size() > 0) {

                if (CampaignConstants.ASSIGN_FROM_CUSTOMER.equals(criteria)) {
                    assignCustomerCriteria(activity);
                }
                if (CampaignConstants.ASSIGN_FROM_PERCENT.equals(criteria)) {
                    assignPercentCriteria(activity);
                }
            } else {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("Campaign.activity.user.automaticAssign.emptyActivityUser");
            }

        }
    }

    public boolean isStateful() {
        return false;
    }

    private void assignCustomerCriteria(CampaignActivity activity) {
        Collection activityUsers = activity.getActivityUsers();
        List allCampContact = getCampContactWithoutResponsible(activity.getCampaignContacts());

        Map userAssignedMap = new HashMap();

        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);

        //customer assign
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext();) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
            User user = (User) EJBFactory.i.findEJB(new UserDTO(activityUser.getUserId()));
            Collection customerUserList = new ArrayList();
            try {
                customerUserList = customerHome.findByEmployeeIdAndCompanyId(user.getAddressId(), activity.getCompanyId());
            } catch (FinderException e) {
                log.debug("Error in finder excecute.....");
            }

            int sizeAssigned = customerAssign(activityUser.getUserId(), customerUserList, allCampContact);
            if (sizeAssigned > 0) {
                userAssignedMap.put(activityUser, new Integer(sizeAssigned));
            }
        }

        //balanced assign
        balancedAssign(activityUsers, allCampContact, userAssignedMap, 0, 1);
    }

    private void assignPercentCriteria(CampaignActivity activity) {
        Collection activityUsers = activity.getActivityUsers();
        activityUsers = orderByPercent(activityUsers.toArray());

        List allCampContact = getCampContactWithoutResponsible(activity.getCampaignContacts());
        int size = allCampContact.size();

        int index = 0;
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext();) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
            ////log.debug("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP"+activityUser.getPercent());
            if (activityUser.getPercent() != null) {
                int assignSize = Math.round((size * activityUser.getPercent().intValue()) / 100);
                if (assignSize == 0) {
                    assignSize = 1;
                }

                for (int i = 0; i < assignSize; i++) {
                    if (index < allCampContact.size()) {
                        CampaignContact campContact = (CampaignContact) allCampContact.get(index);
                        assignResponsible(activityUser.getUserId(), campContact);
                        index++;
                    }
                }
            }
        }
        //complete assign
        if (index < allCampContact.size()) {
            //log.debug("COMPLETETTTTTTTTTTTTTTT");
            completePercentCriteriaAssign(activityUsers, allCampContact, index);
        }
    }

    private List getCampContactWithoutResponsible(Collection campContacts) {
        List result = new ArrayList();
        for (Iterator iterator = campContacts.iterator(); iterator.hasNext();) {
            CampaignContact campContact = (CampaignContact) iterator.next();
            if (campContact.getUserId() == null) {
                result.add(campContact);
            }
        }
        return result;
    }

    private int customerAssign(Integer userId, Collection userCustomers, List campContactList) {
        List indexRemoveList = new ArrayList();
        for (int i = 0; i < campContactList.size(); i++) {
            CampaignContact campContact = (CampaignContact) campContactList.get(i);
            for (Iterator iterator2 = userCustomers.iterator(); iterator2.hasNext();) {
                Customer customer = (Customer) iterator2.next();

                if (campContact.getAddressId().equals(customer.getCustomerId())) {
                    assignResponsible(userId, campContact);
                    indexRemoveList.add(new Integer(i));
                }
            }
        }

        //remove of campContact assigned
        for (int i = 0; i < indexRemoveList.size(); i++) {
            Integer index = (Integer) indexRemoveList.get(i);
            campContactList.remove(index.intValue() - i);
        }

        return indexRemoveList.size();
    }

    private void balancedAssign(Collection activityUsers, List campContactList, Map userAssignedMap, int iniIndex, int assignSize) {

        if (activityUsers.size() > 0) {
            int index = iniIndex;
            for (Iterator iterator = activityUsers.iterator(); iterator.hasNext();) {
                CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
                boolean flag = true;
                if (userAssignedMap.containsKey(activityUser)) {
                    int userSize = ((Integer) userAssignedMap.get(activityUser)).intValue();
                    if (assignSize <= userSize) {
                        flag = false;
                    }
                }

                if (flag && index < campContactList.size()) {
                    CampaignContact campContact = (CampaignContact) campContactList.get(index);
                    assignResponsible(activityUser.getUserId(), campContact);
                    index++;
                }
            }

            if (index < campContactList.size()) {
                assignSize++;
                balancedAssign(activityUsers, campContactList, userAssignedMap, index, assignSize);
            }
        }
    }

    private void assignResponsible(Integer userId, CampaignContact campContact) {

        CampaignContactPK pk = new CampaignContactPK(campContact.getCampaignContactId(), campContact.getCampaignId());
        CampaignContactDTO dto = new CampaignContactDTO();
        dto.setPrimKey(pk);
        dto.put("userId", userId);
        dto.put("version", campContact.getVersion());

        ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE, dto, new ResultDTO(), true, false, false, false);
    }

    private Collection orderByPercent(Object[] arrayObj) {
        Collection res;
        for (int i = 0; i < arrayObj.length - 1; i++) {
            for (int j = i + 1; j < arrayObj.length; j++) {
                CampaignActivityUser user1 = (CampaignActivityUser) arrayObj[i];
                CampaignActivityUser user2 = (CampaignActivityUser) arrayObj[j];
                if (user2.getPercent().intValue() > user1.getPercent().intValue()) {
                    Object aux = arrayObj[i];
                    arrayObj[i] = arrayObj[j];
                    arrayObj[j] = aux;
                }
            }
        }
        res = Arrays.asList(arrayObj);

        return res;
    }

    private void completePercentCriteriaAssign(Collection activityUsers, List campContactList, int iniIndex) {
        int index = iniIndex;
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext();) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            if (index < campContactList.size()) {
                CampaignContact campContact = (CampaignContact) campContactList.get(index);
                assignResponsible(activityUser.getUserId(), campContact);
                index++;
            }
        }
        if (index < campContactList.size()) {
            completePercentCriteriaAssign(activityUsers, campContactList, index);
        }
    }
}