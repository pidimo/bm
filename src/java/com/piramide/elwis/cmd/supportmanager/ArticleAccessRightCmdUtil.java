package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.utils.AccessRightDataLevelCmdUtil;
import com.piramide.elwis.utils.SupportConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ArticleAccessRightCmdUtil {
    private static Log log = LogFactory.getLog(ArticleAccessRightCmdUtil.class);

    public static String composeIdListAsStringValue(List<Integer> idList) {
        return AccessRightDataLevelCmdUtil.composeIdListAsStringValue(idList);
    }

    public static List<Integer> getUserGroupIdList(String userGroupIds) {
        List<Integer> resultList = new ArrayList<Integer>();
        List<Integer> idList = AccessRightDataLevelCmdUtil.splitValueAsIntegerList(userGroupIds);
        for (Integer id : idList) {
            if (!id.equals(SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY)) {
                resultList.add(id);
            }
        }
        return resultList;
    }

    public static boolean existCreatorUserItem(String userGroupIds) {
        List<Integer> idList = AccessRightDataLevelCmdUtil.splitValueAsIntegerList(userGroupIds);
        for (Integer id : idList) {
            if (id.equals(SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY)) {
                return true;
            }
        }
        return false;
    }

}
