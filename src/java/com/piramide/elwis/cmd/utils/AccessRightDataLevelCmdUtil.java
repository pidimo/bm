package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.utils.ContactConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevelCmdUtil {
    private static Log log = LogFactory.getLog(AccessRightDataLevelCmdUtil.class);

    public static List<Integer> splitValueAsIntegerList(String value) {
        List<Integer> list = new ArrayList<Integer>();
        if (value != null) {
            String[] idsArray = value.split(",");
            for (String id : idsArray) {
                if (id != null && !"".equals(id)) {
                    list.add(Integer.valueOf(id.trim()));
                }
            }
        }
        return list;
    }

    public static String composeIdListAsStringValue(List<Integer> idList) {
        String result = "";
        if (idList != null) {
            for (Integer id : idList) {
                if (result.length() > 0) {
                    result += ",";
                }
                result += id.toString();
            }
        }
        return result;
    }

    public static List<Integer> getUserGroupIdList(String userGroupIds) {
        List<Integer> resultList = new ArrayList<Integer>();
        List<Integer> idList = splitValueAsIntegerList(userGroupIds);
        for (Integer id : idList) {
            if (!id.equals(ContactConstants.CREATORUSER_ACCESSRIGHT)) {
                resultList.add(id);
            }
        }
        return resultList;
    }

    public static boolean existCreatorUserItem(String userGroupIds) {
        List<Integer> idList = splitValueAsIntegerList(userGroupIds);
        for (Integer id : idList) {
            if (id.equals(ContactConstants.CREATORUSER_ACCESSRIGHT)) {
                return true;
            }
        }
        return false;
    }
}
