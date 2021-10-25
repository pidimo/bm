package com.piramide.elwis.web.common.accessrightdatalevel.xml;

import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightDataLevel;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightList;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.DataLevelConfig;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.Table;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.JDOMException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class BuildAccessRightDataLevelStructure {
    private final Log log = LogFactory.getLog(this.getClass());
    private AccessRightDataLevel accessRightDataLevel;
    private ReadAccessRightDataLevelStructure reader;

    public BuildAccessRightDataLevelStructure() {
        initialize();
    }

    private void initialize() {
        accessRightDataLevel = new AccessRightDataLevel();
    }

    public void buildStructure(InputStream inputStream) throws IOException, JDOMException {
        reader = new ReadAccessRightDataLevelStructure(inputStream);
        composeStructure();
    }

    public AccessRightDataLevel getAccessRightDataLevel() {
        return accessRightDataLevel;
    }

    private void composeStructure() {
        composeDataLevelConfig();
    }

    private void composeDataLevelConfig() {
        List<Element> elements = reader.getDataLevelConfigs();
        for (Element dataLevelConfigElement : elements) {

            String name = dataLevelConfigElement.getAttributeValue(XmlConstants.ATTRIB_DATALEVELCONFIG_NAME);

            DataLevelConfig dataLevelConfig = new DataLevelConfig(name);
            composeLists(dataLevelConfigElement, dataLevelConfig);
            composeTables(dataLevelConfigElement, dataLevelConfig);

            accessRightDataLevel.addDataLevelConfig(dataLevelConfig);
        }


    }

    private void composeLists(Element dataLevelConfigElement, DataLevelConfig dataLevelConfig) {
        List<Element> lists = reader.getLists(dataLevelConfigElement);
        for (Element listElement : lists) {

            String listName = listElement.getAttributeValue(XmlConstants.ATTRIB_LIST_LISTNAME);
            String aliasField = listElement.getAttributeValue(XmlConstants.ATTRIB_LIST_ALIASFIELD);

            AccessRightList accessRightList = new AccessRightList(listName, aliasField);

            dataLevelConfig.addAccessRightList(accessRightList);
        }
    }

    private void composeTables(Element dataLevelConfigElement, DataLevelConfig dataLevelConfig) {
        List<Element> tables = reader.getTables(dataLevelConfigElement);
        for (Element tableElement : tables) {

            String tableName = tableElement.getAttributeValue(XmlConstants.ATTRIB_TABLE_TABLENAME);
            String relationField = tableElement.getAttributeValue(XmlConstants.ATTRIB_TABLE_RELATIONFIELD);

            Table table = new Table(tableName, relationField);

            dataLevelConfig.addTable(table);
        }
    }
}
