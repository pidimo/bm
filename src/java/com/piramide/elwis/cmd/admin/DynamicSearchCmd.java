package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.DynamicSearch;
import com.piramide.elwis.domain.admin.DynamicSearchField;
import com.piramide.elwis.domain.admin.DynamicSearchFieldHome;
import com.piramide.elwis.domain.admin.DynamicSearchHome;
import com.piramide.elwis.dto.admin.DynamicSearchDTO;
import com.piramide.elwis.dto.admin.DynamicSearchFieldDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing DynamicSearchCmd................" + paramDTO);

        if ("saveSearch".equals(getOp())) {
            save();
        }
        if ("readFields".equals(getOp())) {
            readDynamicSearshFields();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void save() {
        String name = paramDTO.getAsString("name");
        String module = paramDTO.getAsString("module");
        Integer userId = new Integer(paramDTO.get("userId").toString());
        List fieldList = (List) paramDTO.get("searchFields");

        DynamicSearch dynamicSearch = findDynamicSearch(name, module, userId);
        if (dynamicSearch == null) {
            dynamicSearch = create();
        }

        if (dynamicSearch != null) {
            List<Map> fieldMapList = refactorFieldList(fieldList);
            saveField(fieldMapList, dynamicSearch);
        }
    }

    private void saveField(List<Map> fieldMapList, DynamicSearch dynamicSearch) {
        Collection fields = dynamicSearch.getDynamicSearchFields();
        List fieldsList = new ArrayList(fields);

        for (int i = 0; i < fieldsList.size(); i++) {
            DynamicSearchField dynamicSearchField = (DynamicSearchField) fieldsList.get(i);

            Map fieldMap = findAndRemoveFieldInList(fieldMapList, dynamicSearchField.getAlias());
            if (fieldMap != null) {
                //update
                dynamicSearchField.setPosition(Integer.valueOf(fieldMap.get("position").toString()));
            } else {
                //delete
                try {
                    dynamicSearchField.remove();
                } catch (RemoveException e) {
                    log.debug("Canot remove DynamicSearchField..." + dynamicSearchField.getDynamicSearchFieldId(), e);
                }
            }
        }

        //create new fields
        for (Map fieldMap : fieldMapList) {
            createField(fieldMap, dynamicSearch);
        }
    }

    private DynamicSearch findDynamicSearch(String name, String module, Integer userId) {
        DynamicSearch dynamicSearch = null;
        DynamicSearchHome dynamicSearchHome = (DynamicSearchHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_DYNAMICSEARCH);
        try {
            dynamicSearch = dynamicSearchHome.findByNameModuleUser(name, module, userId);
        } catch (FinderException e) {
            log.debug("Not found dynamic search..", e);
        }
        return dynamicSearch;
    }

    private DynamicSearch create() {
        DynamicSearchDTO dynamicSearchDTO = new DynamicSearchDTO(paramDTO);
        DynamicSearch dynamicSearch = (DynamicSearch) ExtendedCRUDDirector.i.create(dynamicSearchDTO, resultDTO, false);
        return dynamicSearch;
    }

    private void createField(Map fieldMap, DynamicSearch dynamicSearch) {
        DynamicSearchFieldDTO  dynamicSearchFieldDTO = new DynamicSearchFieldDTO();
        dynamicSearchFieldDTO.put("alias", fieldMap.get("alias"));
        dynamicSearchFieldDTO.put("position", fieldMap.get("position"));
        dynamicSearchFieldDTO.put("dynamicSearchId", dynamicSearch.getDynamicSearchId());
        dynamicSearchFieldDTO.put("companyId", dynamicSearch.getCompanyId());

        DynamicSearchField dynamicSearchField = (DynamicSearchField) ExtendedCRUDDirector.i.create(dynamicSearchFieldDTO, resultDTO, false);
    }

    private List<Map> refactorFieldList(List fieldList) {
        List<Map> fieldMapList = new ArrayList<Map>();
        for (int i = 0; i < fieldList.size(); i++) {
            String fieldAlias = (String) fieldList.get(i);

            Map map = new HashMap();
            map.put("alias", fieldAlias);
            map.put("position", i);
            fieldMapList.add(map);
        }
        return fieldMapList;
    }

    private Map findAndRemoveFieldInList(List<Map> fieldMapList, String fieldAlias) {
        Map fieldMap = null;
        for (int i = 0; i < fieldMapList.size(); i++) {
            Map map = fieldMapList.get(i);
            if (fieldAlias.equals(map.get("alias"))) {
                fieldMap = fieldMapList.remove(i);
                break;
            }
        }
        return fieldMap;
    }

    private void readDynamicSearshFields() {
        String name = paramDTO.getAsString("name");
        String module = paramDTO.getAsString("module");
        Integer userId = new Integer(paramDTO.get("userId").toString());

        List<String> fieldList = new ArrayList<String>();

        DynamicSearch dynamicSearch = findDynamicSearch(name, module, userId);
        if (dynamicSearch != null) {
            DynamicSearchFieldHome dynamicSearchFieldHome = (DynamicSearchFieldHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_DYNAMICSEARCHFIELD);
            Collection dynamicSearchFields;
            try {
                dynamicSearchFields = dynamicSearchFieldHome.findByDynamicSearch(dynamicSearch.getDynamicSearchId());
            } catch (FinderException e) {
                dynamicSearchFields = new ArrayList();
            }

            for (Iterator iterator = dynamicSearchFields.iterator(); iterator.hasNext();) {
                DynamicSearchField dynamicSearchField = (DynamicSearchField) iterator.next();
                fieldList.add(dynamicSearchField.getAlias());
            }
        }
        resultDTO.put("searchFields", fieldList);
    }
}
