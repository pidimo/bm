package com.piramide.elwis.web.common.accessrightdatalevel.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class AccessRightDataLevel {

    private List<DataLevelConfig> dataLevelConfigList;

    public AccessRightDataLevel() {
        dataLevelConfigList = new ArrayList<DataLevelConfig>();
    }

    public List<DataLevelConfig> getDataLevelConfigList() {
        return dataLevelConfigList;
    }

    public void setDataLevelConfigList(List<DataLevelConfig> dataLevelConfigList) {
        this.dataLevelConfigList = dataLevelConfigList;
    }

    public void addDataLevelConfig(DataLevelConfig dataLevelConfig) {
        dataLevelConfigList.add(dataLevelConfig);

    }

    public DataLevelConfig findDataLevelConfig(String configName) {
        for (DataLevelConfig dataLevelConfig : dataLevelConfigList) {
            if (dataLevelConfig.getName().equals(configName)) {
                return dataLevelConfig;
            }
        }
        return null;
    }

}

