package com.piramide.elwis.cmd.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Util to manage user password change
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class PasswordChangeCmdUtil {
    private Log log = LogFactory.getLog(this.getClass());
    private SessionContext ctx;

    public PasswordChangeCmdUtil(SessionContext ctx) {
        this.ctx = ctx;
    }

    public void userRoleUpdated(Integer userId, List<Integer> oldRoleIdsList, List<Integer> currentRoleIdsList) {
        Map<String, List<Integer>> parseMap = parseAsNewExistAndRemovedValues(oldRoleIdsList, currentRoleIdsList);
        List<Integer> removedRoleIds = parseMap.get("removeIds");

        updateUserPasswordChange(userId, removedRoleIds, currentRoleIdsList);
    }

    public void userRoleRemoved(Integer userId, Integer removedRoleId) {
        List<Integer> removedRoleIds = new ArrayList<Integer>();
        removedRoleIds.add(removedRoleId);
        updateUserPasswordChange(userId, removedRoleIds, new ArrayList<Integer>());
    }

    public void userRoleAssigned(Integer userId, Integer assignedRoleId) {
        List<Integer> assignedRoleIds = new ArrayList<Integer>();
        assignedRoleIds.add(assignedRoleId);
        updateUserPasswordChange(userId, new ArrayList<Integer>(), assignedRoleIds);
    }

    private void updateUserPasswordChange(Integer userId, List<Integer> removedRoleIds, List<Integer> assignedRoleIdsList) {
        PasswordChangeCmd passwordChangeCmd = new PasswordChangeCmd();
        passwordChangeCmd.setOp("roleUpdatedByUser");
        passwordChangeCmd.putParam("userId", userId);
        passwordChangeCmd.putParam("removedRoleIds", removedRoleIds);
        passwordChangeCmd.putParam("assignedRoleIds", assignedRoleIdsList);
        passwordChangeCmd.executeInStateless(ctx);
    }

    public void deleteUserPasswordChange(Integer userId) {
        PasswordChangeCmd passwordChangeCmd = new PasswordChangeCmd();
        passwordChangeCmd.setOp("deleteFromUser");
        passwordChangeCmd.putParam("userId", userId);
        passwordChangeCmd.executeInStateless(ctx);
    }

    public void deleteUserPasswordChange(Integer userId, Integer passwordChangeId) {
        PasswordChangeCmd passwordChangeCmd = new PasswordChangeCmd();
        passwordChangeCmd.setOp("deleteUserPasswordChange");
        passwordChangeCmd.putParam("userId", userId);
        passwordChangeCmd.putParam("passwordChangeId", passwordChangeId);
        passwordChangeCmd.executeInStateless(ctx);
    }

    private Map<String, List<Integer>> parseAsNewExistAndRemovedValues(List<Integer> oldIdsList, List<Integer> currentIdsList) {
        Map<String, List<Integer>> parseMap = new HashMap<String, List<Integer>>();

        List<Integer> newIds = new ArrayList<Integer>();
        List<Integer> existIds = new ArrayList<Integer>();
        List<Integer> removeIds = new ArrayList<Integer>();

        //verify old elements
        for (int i = 0; i < oldIdsList.size(); i++) {
            Integer value = oldIdsList.get(i);
            if (currentIdsList.contains(value)) {
                existIds.add(value);
            } else {
                removeIds.add(value);
            }
        }

        for (int i = 0; i < currentIdsList.size(); i++) {
            Integer value = currentIdsList.get(i);
            if (!existIds.contains(value)) {
                newIds.add(value);
            }
        }

        parseMap.put("newIds", newIds);
        parseMap.put("existIds", existIds);
        parseMap.put("removeIds", removeIds);

        return parseMap;
    }

}
