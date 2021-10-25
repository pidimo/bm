package com.piramide.elwis.web.common.accessrightdatalevel.util;

import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightDataLevel;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightList;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.DataLevelConfig;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.Table;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.structure.UnionListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevelUtil {
    private static Log log = LogFactory.getLog(AccessRightDataLevelUtil.class);

    public static List<String> splitValueAsList(String value) {
        List<String> list = new ArrayList<String>();
        if (value != null) {
            String[] strArray = value.split(",");
            for (String str : strArray) {
                if (str != null && !"".equals(str.trim())) {
                    list.add(str.trim());
                }
            }
        }
        return list;
    }

    public static AccessRightList findList(String listName, String dataLevelConfigName) {
        AccessRightList accessRightList = null;
        AccessRightDataLevel accessRightDataLevel = AccessRightDataLevelFactory.i.getAccessRightDataLevel();
        if (accessRightDataLevel != null) {
            DataLevelConfig dataLevelConfig = accessRightDataLevel.findDataLevelConfig(dataLevelConfigName);
            if (dataLevelConfig != null) {
                accessRightList = dataLevelConfig.findAccessRightList(listName);
            }
        }
        return accessRightList;
    }

    public static boolean existList(String listName, String dataLevelConfigName) {
        AccessRightList accessRightList = findList(listName, dataLevelConfigName);
        return accessRightList != null;
    }

    public static boolean existList(ListStructure listStructure, String dataLevelConfigName) {
        if (listStructure.getType() == UnionListStructure.UNIONLIST_TYPE) {
            UnionListStructure unionListStructure = (UnionListStructure) listStructure;

            Iterator unionLists = unionListStructure.getList().iterator();
            while (unionLists.hasNext()) {
                ListStructure unionList = (ListStructure) unionLists.next();
                if (existList(unionList.getListName(), dataLevelConfigName)) {
                    return true;
                }
            }
        } else {
            return existList(listStructure.getListName(), dataLevelConfigName);
        }
        return false;
    }

    public static Table findTable(String tableName, String dataLevelConfigName) {
        Table table = null;
        AccessRightDataLevel accessRightDataLevel = AccessRightDataLevelFactory.i.getAccessRightDataLevel();
        if (accessRightDataLevel != null) {
            DataLevelConfig dataLevelConfig = accessRightDataLevel.findDataLevelConfig(dataLevelConfigName);
            if (dataLevelConfig != null) {
                table = dataLevelConfig.findTable(tableName);
            }
        }
        return table;
    }

}
