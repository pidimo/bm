package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Automatic assign responsible to activity contacts
 *
 * @author Miky
 * @version $Id: ActivityAutomaticAssingResponsibleCmd.java 10484 2014-08-28 22:51:28Z miguel $
 */
public class ActivityAutomaticAssingResponsibleCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    private final String ASSIGNSIZE = "size";
    private final String RESTSIZE = "rest";

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityAutomaticAssingResponsibleCmd................" + paramDTO);

        Integer activityId = new Integer(paramDTO.get("activityId").toString());

        boolean isHomogeneouslyAssign = paramDTO.containsKey("homogeneously");
        boolean isCustomerPriorityAssign = paramDTO.containsKey("customerPriority");
        Object conflictAssignCriteria = paramDTO.get("conflictCriteria");

        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);
        if (activity != null) {

            if (activity.getActivityUsers().size() > 0) {

                if (isHomogeneouslyAssign) {
                    if (isCustomerPriorityAssign) {

                        if (conflictAssignCriteria == null) {
                            if (AssignHomogeneouslyWithCustomerPriorityIsValid(activity)) {
                                assignHomogeneouslyWithCustomerPriorityAll(activity);
                            } else {
                                resultDTO.setResultAsFailure();
                                resultDTO.setForward("Confirmation");
                                return;
                            }
                        } else {
                            if (CampaignConstants.CONFLICTASSIGN_ALL_CUSTOMERS.equals(conflictAssignCriteria.toString())) {
                                assignHomogeneouslyWithCustomerPriorityAll(activity);
                            } else if (CampaignConstants.CONFLICTASSIGN_JUST_WHAT_GETS.equals(conflictAssignCriteria.toString())) {
                                assignHomogeneouslyWithCustomerJustWhatGets(activity);
                            }
                        }
                    } else {
                        homogeneouslyAssign(activity);
                    }
                } else {
                    if (isCustomerPriorityAssign) {
                        if (conflictAssignCriteria == null) {
                            if (AssignPercentWithCustomerPriorityIsValid(activity)) {
                                assignPercentWithCustomerPriorityAll(activity);
                            } else {
                                resultDTO.setResultAsFailure();
                                resultDTO.setForward("Confirmation");
                                return;
                            }
                        } else {
                            if (CampaignConstants.CONFLICTASSIGN_ALL_CUSTOMERS.equals(conflictAssignCriteria.toString())) {
                                assignPercentWithCustomerPriorityAll(activity);
                            } else if (CampaignConstants.CONFLICTASSIGN_JUST_WHAT_GETS.equals(conflictAssignCriteria.toString())) {
                                assignPercentWithCustomerJustWhatGets(activity);
                            }
                        }
                    } else {
                        assignPercentCriteria(activity);
                    }
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

    /**
     * assign responsibles to contacts Homogeneously, first assign all contacts related with user how customer
     * and then assign all homogeneously
     *
     * @param activity
     */
    private void assignHomogeneouslyWithCustomerPriorityAll(CampaignActivity activity) {
        log.debug("Assign homogeneous with customer priority all.....");
        Collection activityUsers = activity.getActivityUsers();
        List allCampContact = getCampContactWithoutResponsible(activity);

        Map userAssignedMap = new HashMap();

        //customer assign
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            int sizeAssigned = customerAssign(activityUser, allCampContact);
            if (sizeAssigned > 0) {
                userAssignedMap.put(activityUser, new Integer(sizeAssigned));
            }
        }

        //balanced assign
        balancedAssign(activityUsers, allCampContact, userAssignedMap, 0, 1);
    }

    /**
     * assign responsibles to contacts Homogeneously, first assign just what gets to each user the contacts related how customer
     * and then assign all homogeneously
     *
     * @param activity
     */
    private void assignHomogeneouslyWithCustomerJustWhatGets(CampaignActivity activity) {
        log.debug("Assign homogeneuos with customer just what get....");
        Collection activityUsers = activity.getActivityUsers();
        List allCampContact = getCampContactWithoutResponsible(activity);

        Map<String, Integer> asssignSizeMap = getHomogeneouslyAssignSize(activityUsers.size(), allCampContact.size());
        int assignSize = asssignSizeMap.get(ASSIGNSIZE);
        int restSize = asssignSizeMap.get(RESTSIZE);

        Map userAssignedMap = new HashMap();

        //customer assign
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            int sizeAssigned = customerAssign(activityUser, allCampContact, assignSize, (restSize > 0));
            if (sizeAssigned > 0) {
                userAssignedMap.put(activityUser, new Integer(sizeAssigned));
                if (sizeAssigned > assignSize) {
                    restSize = restSize - (sizeAssigned - assignSize);
                }
            }
        }

        //balanced assign
        balancedAssign(activityUsers, allCampContact, userAssignedMap, 0, 1);
    }

    /**
     * assign responsibles to all contacts Homogeneously
     *
     * @param activity
     */
    private void homogeneouslyAssign(CampaignActivity activity) {
        log.debug("homogeneuos assign....");
        Collection activityUsers = activity.getActivityUsers();
        List allCampContact = getCampContactWithoutResponsible(activity);
        balancedAssign(activityUsers, allCampContact, new HashMap(), 0, 1);
    }

    /**
     * percent assign with customer priority criteria, all contact related how customer
     * be assigned to an user
     *
     * @param activity
     */
    private void assignPercentWithCustomerPriorityAll(CampaignActivity activity) {
        log.debug("assign percent with customer priority all.....");
        assignPercentWithCustomerPriority(activity, true);
    }

    /**
     * percent assign with customer priority criteria, only just what get of contact related how customer
     * be assigned to an user
     *
     * @param activity
     */
    private void assignPercentWithCustomerJustWhatGets(CampaignActivity activity) {
        log.debug("Assign percent with customer just what get....");
        assignPercentWithCustomerPriority(activity, false);
    }

    private void assignPercentWithCustomerPriority(CampaignActivity activity, boolean assignAllCustomers) {
        Collection activityUsers = activity.getActivityUsers();
        activityUsers = orderByPercent(activityUsers.toArray());

        List allCampContact = getCampContactWithoutResponsible(activity);
        double size = allCampContact.size();

        Map<Integer, Double> userPercentMap = getUserPercentAsMap(activityUsers);
        Map<Integer, Double> userAssignedMap = new HashMap<Integer, Double>();

        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            if (userPercentMap.containsKey(activityUser.getUserId())) {
                double assignSize = Math.round((size * userPercentMap.get(activityUser.getUserId())) / 100);
                if (assignSize == 0) {
                    assignSize = 1;
                }

                double sizeAssigned;
                if (assignAllCustomers) {
                    sizeAssigned = customerAssign(activityUser, allCampContact);
                } else {
                    sizeAssigned = customerAssign(activityUser, allCampContact, assignSize, false);
                }

                if (sizeAssigned > assignSize) {
                    userPercentMap = updateUserPercents(size, userPercentMap, activityUser.getUserId(), sizeAssigned);
                    userPercentMap.remove(activityUser.getUserId());

                } else if (sizeAssigned == assignSize) {
                    userPercentMap.remove(activityUser.getUserId());

                } else if (sizeAssigned > 0) {
                    userAssignedMap.put(activityUser.getUserId(), sizeAssigned);
                    //recalculate percent
                    double percentAssigned = (sizeAssigned * 100) / size;
                    double newPercent = userPercentMap.get(activityUser.getUserId()) - percentAssigned;

                    userPercentMap.put(activityUser.getUserId(), newPercent);
                }
            }
        }

        //complete assign
        if (!allCampContact.isEmpty()) {
            completePercentAssignWithCustomerPriority(activityUsers, allCampContact, userPercentMap, userAssignedMap, size);
        }
    }

    /**
     * complete percent assign with customer priority criteria if yet exist contacts without assign
     *
     * @param activityUsers    users
     * @param campContactList
     * @param userPercentMap   user percents map
     * @param userAssignMap    previous user assigned information
     * @param totalContactSize initial contact size before of assign
     */
    private void completePercentAssignWithCustomerPriority(Collection activityUsers, List campContactList, Map<Integer, Double> userPercentMap, Map<Integer, Double> userAssignMap, double totalContactSize) {
        log.debug("complete asign percent with customer priority... contacts:" + campContactList.size());

        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
            Integer userId = activityUser.getUserId();
            if (userPercentMap.containsKey(userId) && userPercentMap.get(userId) > 0) {
                double assignSize = Math.round((totalContactSize * userPercentMap.get(userId)) / 100);
                if (assignSize == 0 && !userAssignMap.containsKey(userId)) {
                    assignSize = 1;
                }

                double sizeAssigned = 0;
                for (Iterator iterator2 = campContactList.iterator(); iterator2.hasNext(); ) {
                    CampaignContact campContact = (CampaignContact) iterator2.next();
                    if (sizeAssigned < assignSize) {
                        assignResponsible(userId, campContact);
                        sizeAssigned++;

                        iterator2.remove();
                    } else {
                        break;
                    }
                }
            }
        }

        if (!campContactList.isEmpty()) {
            log.debug("Assign has not completed... contacts:" + campContactList.size());
            completePercentCriteriaAssign(activityUsers, campContactList, 0);
        }
    }

    /**
     * Percent assign
     *
     * @param activity
     */
    private void assignPercentCriteria(CampaignActivity activity) {
        log.debug("Percent assign....");
        Collection activityUsers = activity.getActivityUsers();
        activityUsers = orderByPercent(activityUsers.toArray());

        List allCampContact = getCampContactWithoutResponsible(activity);
        double size = allCampContact.size();

        int index = 0;
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();
            ////log.debug("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP"+activityUser.getPercent());
            if (activityUser.getPercent() != null) {
                double assignSize = Math.round((size * activityUser.getPercent().intValue()) / 100);
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

    /**
     * get all contacts without responsible
     *
     * @param activity
     * @return list
     */
    private List getCampContactWithoutResponsible(CampaignActivity activity) {
        List result = new ArrayList();
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        try {
            Collection collection = campaignContactHome.findByActivityIdWithoutResponsible(activity.getActivityId());
            result = new ArrayList(collection);
        } catch (FinderException ignore) {
        }
        return result;
    }

    /**
     * Assign all contacts related how customers to this user
     *
     * @param activityUser
     * @param campContactList
     * @return int, size assigned to this user
     */
    private int customerAssign(CampaignActivityUser activityUser, List campContactList) {
        return customerAssign(activityUser, campContactList, -1, false);
    }

    /**
     * Assign contacts related how customers to this user
     *
     * @param activityUser    user
     * @param campContactList contact list
     * @param assingSize      size to will be assign, '-1' to assign all
     * @param hasRest         boolean to inidicate if homogeneous assign size has rest
     * @return int, size assigned to this user
     */
    private int customerAssign(CampaignActivityUser activityUser, List campContactList, double assingSize, boolean hasRest) {
        List indexRemoveList = new ArrayList();

        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        User user = (User) EJBFactory.i.findEJB(new UserDTO(activityUser.getUserId()));

        Collection<Integer> customerIdCollection;
        try {
            customerIdCollection = customerHome.findCustomerIdByEmployeeIdCompanyId(user.getAddressId(), activityUser.getCompanyId());
        } catch (FinderException e) {
            log.debug("Finder exception...", e);
            customerIdCollection = new ArrayList<Integer>();
        }

        if (!customerIdCollection.isEmpty()) {
            for (int i = 0; i < campContactList.size(); i++) {
                CampaignContact campContact = (CampaignContact) campContactList.get(i);

                if (customerIdCollection.contains(campContact.getAddressId())) {

                    if (assingSize < 0) {
                        assignResponsible(activityUser.getUserId(), campContact);
                        indexRemoveList.add(new Integer(i));
                    } else {
                        if (indexRemoveList.size() < assingSize || (hasRest && indexRemoveList.size() < (assingSize + 1))) {
                            assignResponsible(activityUser.getUserId(), campContact);
                            indexRemoveList.add(new Integer(i));
                        } else {
                            //assign completed
                            i = campContactList.size();
                            break;
                        }
                    }
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

    /**
     * Assign all contacts homogeneously
     *
     * @param activityUsers   users
     * @param campContactList contacts list
     * @param userAssignedMap Map with previous assign information
     * @param iniIndex        initial index in contact list
     * @param assignSize      initial assign size to all users
     */
    private void balancedAssign(Collection activityUsers, List campContactList, Map userAssignedMap, int iniIndex, int assignSize) {

        if (activityUsers.size() > 0) {
            int index = iniIndex;
            for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
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

    /**
     * complete percent assign if yet exist contacts without assign
     *
     * @param activityUsers   users
     * @param campContactList
     * @param iniIndex        initial index in contact list
     */
    private void completePercentCriteriaAssign(Collection activityUsers, List campContactList, int iniIndex) {
        int index = iniIndex;
        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
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

    /**
     * validate if homogeneous assign with customer priority criteria has not produce assign conflict
     *
     * @param activity
     * @return true or false
     */
    private boolean AssignHomogeneouslyWithCustomerPriorityIsValid(CampaignActivity activity) {
        Collection activityUsers = activity.getActivityUsers();
        List allCampContact = getCampContactWithoutResponsible(activity);

        Map<String, Integer> asssignSizeMap = getHomogeneouslyAssignSize(activityUsers.size(), allCampContact.size());

        int assignSize = asssignSizeMap.get(ASSIGNSIZE);
        int restSize = asssignSizeMap.get(RESTSIZE);

        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            int sizeAssigned = customerAssignSize(activityUser, allCampContact);
            if (sizeAssigned > assignSize) {
                if (restSize > 0) {
                    restSize--;
                    if (sizeAssigned > (assignSize + 1)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * validate if percent assign with customer priority criteria has not produce assign conflict
     *
     * @param activity
     * @return true or false
     */
    private boolean AssignPercentWithCustomerPriorityIsValid(CampaignActivity activity) {
        Collection activityUsers = activity.getActivityUsers();
        activityUsers = orderByPercent(activityUsers.toArray());

        List allCampContact = getCampContactWithoutResponsible(activity);
        double size = allCampContact.size();

        for (Iterator iterator = activityUsers.iterator(); iterator.hasNext(); ) {
            CampaignActivityUser activityUser = (CampaignActivityUser) iterator.next();

            if (activityUser.getPercent() != null) {
                double assignSize = Math.round((size * activityUser.getPercent().intValue()) / 100);
                if (assignSize == 0) {
                    assignSize = 1;
                }

                int sizeAssigned = customerAssignSize(activityUser, allCampContact);
                if (sizeAssigned > assignSize) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * get homogeneous assign size
     *
     * @param totalUser
     * @param totalContact
     * @return Map contain 'assign size' and 'rest size'
     */
    private Map<String, Integer> getHomogeneouslyAssignSize(int totalUser, int totalContact) {
        int rest = 0;
        int sizeAssign = Math.round(totalContact / totalUser);

        int totalAssign = sizeAssign * totalUser;
        if (totalAssign < totalContact) {
            rest = totalContact - totalAssign;
        }

        Map<String, Integer> asssignSizeMap = new HashMap<String, Integer>();
        asssignSizeMap.put(ASSIGNSIZE, sizeAssign);
        asssignSizeMap.put(RESTSIZE, rest);

        log.debug("Assign homogeneous calculated size..." + asssignSizeMap);

        return asssignSizeMap;
    }

    /**
     * get the customer related assign size to this user, and remove all customer related contacts
     * of contact list
     *
     * @param activityUser    user
     * @param campContactList contact list
     * @return size of related customers to this user
     */
    private int customerAssignSize(CampaignActivityUser activityUser, List campContactList) {

        List indexRemoveList = new ArrayList();

        CustomerHome customerHome = (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        User user = (User) EJBFactory.i.findEJB(new UserDTO(activityUser.getUserId()));

        Collection<Integer> customerIdCollection;
        try {
            customerIdCollection = customerHome.findCustomerIdByEmployeeIdCompanyId(user.getAddressId(), activityUser.getCompanyId());
        } catch (FinderException e) {
            log.debug("Finder exception...", e);
            customerIdCollection = new ArrayList<Integer>();
        }

        if (!customerIdCollection.isEmpty()) {
            for (int i = 0; i < campContactList.size(); i++) {
                CampaignContact campContact = (CampaignContact) campContactList.get(i);
                if (customerIdCollection.contains(campContact.getAddressId())) {
                    indexRemoveList.add(new Integer(i));
                }
            }
        }

        //remove campContact related with customer
        for (int i = 0; i < indexRemoveList.size(); i++) {
            Integer index = (Integer) indexRemoveList.get(i);
            campContactList.remove(index.intValue() - i);
        }

        return indexRemoveList.size();
    }

    /**
     * get user percents as Map{key=userId, value=percent}
     *
     * @param activityUsers
     * @return Map
     */
    private Map<Integer, Double> getUserPercentAsMap(Collection<CampaignActivityUser> activityUsers) {
        Map<Integer, Double> userPercentMap = new HashMap<Integer, Double>();
        for (CampaignActivityUser activityUser : activityUsers) {
            if (activityUser.getPercent() != null) {
                userPercentMap.put(activityUser.getUserId(), new Double(activityUser.getPercent()));
            }
        }
        return userPercentMap;
    }

    /**
     * update percents of all others users, because this user has exceded of percent assign
     *
     * @param totalContacts  initial total contacts before assign
     * @param userPercentMap actual user percents {key=userId, value=percent}
     * @param userId         user id of exceded percent assign
     * @param sizeAssigned   total size assigned to this user
     * @return userPercentMap updated
     */
    private Map<Integer, Double> updateUserPercents(double totalContacts, Map<Integer, Double> userPercentMap, Integer userId, double sizeAssigned) {
        if (userPercentMap.containsKey(userId)) {
            double totalPercentAssigned = (sizeAssigned * 100) / totalContacts;
            double excededPercent = totalPercentAssigned - userPercentMap.get(userId);

            double deductPercent = excededPercent / (userPercentMap.size() - 1);
            for (Integer userIdAsKey : userPercentMap.keySet()) {
                if (!userIdAsKey.equals(userId)) {
                    double newPercent = userPercentMap.get(userIdAsKey) - deductPercent;
                    userPercentMap.put(userIdAsKey, newPercent);
                }
            }
        }
        return userPercentMap;
    }

}