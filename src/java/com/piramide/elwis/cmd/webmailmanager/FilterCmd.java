package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.webmailmanager.Condition;
import com.piramide.elwis.domain.webmailmanager.Filter;
import com.piramide.elwis.domain.webmailmanager.Folder;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.dto.webmailmanager.ConditionDTO;
import com.piramide.elwis.dto.webmailmanager.FilterDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, entry duplicated); all relatinated with the Filters and conditions
 * <p/>
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: FilterCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FilterCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing FilterCmd........" + paramDTO);

        if ("create".equals(getOp())) {
            create();
        }
        if ("update".equals(getOp())) {
            update();
        }
        if ("delete".equals(getOp())) {
            delete();
        }
        if ("read".equals(getOp())) {
            read();
        }

        getFoldersUserMail();

    }

    public void create() {

        if (isUnique(paramDTO.get("filterName").toString(), "create")) {

            FilterDTO dto = new FilterDTO();
            dto.put("filterName", paramDTO.get("filterName"));
            dto.put("companyId", new Integer(paramDTO.get("companyId").toString()));
            dto.put("folderId", new Integer(paramDTO.get("folderId").toString()));

            //FilterHome  filterHome =  (FilterHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FILTER);
            Filter filter = (Filter) EJBFactory.i.createEJB(dto);

            Integer keyFilter = filter.getFilterId();

            // ----------------------- conditions -------------------------------------------
            List ListIdentifyCond = (List) paramDTO.get("identifyCondition");
            for (Iterator iterator = ListIdentifyCond.iterator(); iterator.hasNext();) {
                String identify = iterator.next().toString();

                if (textNotIsEmpty(paramDTO.get("text" + identify).toString())) {

                    String textCondition = trimAll(paramDTO.getAsString("text" + identify));
                    ConditionDTO dtoCondition = new ConditionDTO();
                    dtoCondition.put("conditionNameKey", new Integer(paramDTO.get("namekey" + identify).toString()));
                    dtoCondition.put("conditionKey", new Integer(paramDTO.get("conditionkey" + identify).toString()));
                    dtoCondition.put("conditionText", textCondition);
                    dtoCondition.put("companyId", new Integer(paramDTO.get("companyId").toString()));
                    dtoCondition.put("filterId", keyFilter);
                    //ConditionHome  conditionHome =  (ConditionHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_CONDITION);
                    //Condition condition = (Condition) CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE,dtoCondition,resultDTO);

                    Condition condition = (Condition) EJBFactory.i.createEJB(dtoCondition);
                }
            }

        } else {
            resultDTO.addResultMessage("Webmail.filter.errors.uniqueName", paramDTO.get("filterName"));
            resultDTO.setResultAsFailure();
        }
    }

    public void update() {

        if (isUnique(paramDTO.get("filterName").toString(), "update")) {

            Integer filterKey = new Integer((String) paramDTO.get("filterId"));

            FilterDTO dtoFilter = new FilterDTO();
            dtoFilter.put("filterId", filterKey);
            dtoFilter.put("filterName", paramDTO.get("filterName"));
            dtoFilter.put("companyId", new Integer(paramDTO.get("companyId").toString()));
            dtoFilter.put("folderId", new Integer(paramDTO.get("folderId").toString()));
            EJBFactory.i.updateEJB(dtoFilter);

            // ---------- conditions list ----------
            List ListIdentifyCond = (List) paramDTO.get("identifyCondition");
            for (Iterator iterator = ListIdentifyCond.iterator(); iterator.hasNext();) {
                String identify = iterator.next().toString();

                String textCondition = trimAll(paramDTO.getAsString("text" + identify));
                ConditionDTO dtoCondition = new ConditionDTO();
                if (textNotIsEmpty(textCondition)) {
                    dtoCondition.put("conditionNameKey", new Integer(paramDTO.get("namekey" + identify).toString()));
                    dtoCondition.put("conditionKey", new Integer(paramDTO.get("conditionkey" + identify).toString()));
                    dtoCondition.put("conditionText", textCondition);
                    dtoCondition.put("companyId", new Integer(paramDTO.get("companyId").toString()));
                    dtoCondition.put("filterId", filterKey);

                    if (!paramDTO.get("conditionId" + identify).equals("null")) {
                        dtoCondition.put("conditionId", new Integer(paramDTO.get("conditionId" + identify).toString()));
                        EJBFactory.i.updateEJB(dtoCondition);
                    } else {
                        Condition condition = (Condition) EJBFactory.i.createEJB(dtoCondition);
                    }
                } else if (!paramDTO.get("conditionId" + identify).equals("null")) {
                    dtoCondition.put("conditionId", new Integer(paramDTO.get("conditionId" + identify).toString()));
                    EJBFactory.i.removeEJB(dtoCondition);
                }
            }
        } else {
            resultDTO.addResultMessage("Webmail.filter.errors.uniqueName", paramDTO.get("filterName"));
            resultDTO.setResultAsFailure();
        }
    }

    public void delete() {

        Integer filterKey = new Integer(paramDTO.get("filterId").toString());

        // ---------- conditions list ----------
        FilterDTO dto = new FilterDTO();
        dto.put("filterId", filterKey);

        try {
            Filter filter = (Filter) EJBFactory.i.findEJB(dto);
            //Filter filter=(Filter)ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_READ,dto,resultDTO);
            if (!filter.getConditions().isEmpty()) {
                Collection conds = (Collection) filter.getConditions();
                Object[] ob = conds.toArray();
                for (int i = 0; i < ob.length; i++) {
                    Condition condition = (Condition) ob[i];
                    EJBFactory.i.removeEJB(condition);
                }
            }
            EJBFactory.i.removeEJB(dto);
        } catch (EJBFactoryException e) {
            log.error("ERROR IN EJB" + e);
        }
    }

    public void read() {

        Integer filterKey = new Integer((String) paramDTO.get("filterId"));

        FilterDTO dtoFilter = new FilterDTO();
        dtoFilter.put("filterId", filterKey);
        Filter filter = (Filter) EJBFactory.i.findEJB(dtoFilter);

        FilterDTO newFilter = new FilterDTO();
        DTOFactory.i.copyToDTO(filter, newFilter);
        resultDTO.putAll(newFilter);
        log.debug("filterssss" + newFilter);

        Collection conditions = (Collection) filter.getConditions();
        Iterator i = conditions.iterator();
        Collection dtosConds = new ArrayList();
        while (i.hasNext()) {
            ConditionDTO conditionDTO = new ConditionDTO();
            DTOFactory.i.copyToDTO(i.next(), conditionDTO);
            dtosConds.add(conditionDTO);
        }
        resultDTO.put("filterConditions", dtosConds);
        Integer numCond = new Integer(dtosConds.size() - 1);
        resultDTO.put("numCond", numCond);
        resultDTO.put("folderId", filter.getFolderId());

        log.debug("conditions " + dtosConds);


    }

    public boolean textNotIsEmpty(String text) {
        for (int j = 0; j < text.length(); j++) {
            if (text.charAt(j) != ' ') {
                return true;
            }
        }
        return false;
    }

    public String trimAll(String cad) {
        StringTokenizer stSpace = new StringTokenizer(cad);  /// tokenizer for space
        String newCad = "";
        while (stSpace.hasMoreTokens()) {
            newCad = newCad + " " + stSpace.nextToken();
        }

        newCad = newCad.trim();
        return newCad;
    }

    public boolean isUnique(String textUniqueNameFilter, String operation) {

        Integer userMailId = new Integer(paramDTO.get("userMailId").toString());

        UserMailDTO userMailDto = new UserMailDTO();
        userMailDto.put("userMailId", userMailId);

        UserMail userMail = (UserMail) EJBFactory.i.findEJB(userMailDto);

        Collection folders = userMail.getFolders();
        Iterator iterator = folders.iterator();
        while (iterator.hasNext()) {
            Folder fold = (Folder) iterator.next();
            Collection filters = fold.getFilters();
            Iterator iteratorFilt = filters.iterator();

            while (iteratorFilt.hasNext()) {
                Filter filt = (Filter) iteratorFilt.next();

                if (operation.equals("create")) {
                    if (filt.getFilterName().equals(textUniqueNameFilter)) {
                        return false;
                    }
                }

                if (operation.equals("update")) {
                    Integer filterKey = new Integer((String) paramDTO.get("filterId"));
                    if (filt.getFilterName().equals(textUniqueNameFilter) && !filt.getFilterId().equals(filterKey)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void getFoldersUserMail() {

        Integer defaultFolder = new Integer(WebMailConstants.FOLDER_DEFAULT);
        Integer trashFolder = new Integer(WebMailConstants.FOLDER_TRASH);

        try {
            Integer userMailId = new Integer(paramDTO.get("userMailId").toString());
            UserMailDTO userMailDto = new UserMailDTO();
            userMailDto.put("userMailId", userMailId);

            UserMail userMail = (UserMail) EJBFactory.i.findEJB(userMailDto);
            Collection folders = userMail.getFolders();
            folders = orderByFolderName(folders.toArray());
            List folderList = new ArrayList();
            for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
                Folder folder = (Folder) iterator.next();
                if (folder.getFolderType().equals(defaultFolder)) {
                    Map mapFolder = new HashMap(2);
                    mapFolder.put("userFolder", folder.getFolderName());
                    mapFolder.put("userFolderId", folder.getFolderId());
                    folderList.add(mapFolder);
                }
                if (folder.getFolderType().equals(trashFolder)) {
                    Map mapFolder = new HashMap(2);
                    mapFolder.put("userFolder", "Webmail.folder.trash");
                    mapFolder.put("userFolderId", folder.getFolderId());
                    folderList.add(mapFolder);
                }
            }
            resultDTO.put("userFolderList", folderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection orderByFolderName(Object[] arrayFolders) {
        Collection res;
        for (int i = 0; i < arrayFolders.length - 1; i++) {
            for (int j = i + 1; j < arrayFolders.length; j++) {
                Folder folder1 = (Folder) arrayFolders[i];
                Folder folder2 = (Folder) arrayFolders[j];
                String cad1 = folder1.getFolderName();
                String cad2 = folder2.getFolderName();
                if (cad1.compareToIgnoreCase(cad2) > 0) {
                    Object aux = arrayFolders[i];
                    arrayFolders[i] = arrayFolders[j];
                    arrayFolders[j] = aux;
                }
            }

        }
        res = Arrays.asList(arrayFolders);

        return res;
    }

}

