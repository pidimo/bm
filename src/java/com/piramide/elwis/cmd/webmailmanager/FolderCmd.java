package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderPojo;
import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderTreeModel;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, entry duplicated); all relatinated with the Folders
 * <p/>
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: FolderCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FolderCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        if (paramDTO.get("cancel") != null) {
            if (paramDTO.get("ofCompose") != null) {
                resultDTO.setForward("Compose");
                return;
            } else {
                resultDTO.setForward("Cancel");
                return;
            }
        } else if (paramDTO.get("ofCompose") != null) {
            resultDTO.setForward("Compose");
        }

        if ("create".equals(getOp())) {
            create();
        }
        if ("update".equals(getOp())) {
            update();
        }
        if ("delete".equals(getOp())) {
            delete(ctx);
        }
        if ("read".equals(getOp())) {
            read();
        }
        if ("getCustomFolders".equals(getOp())) {
            getUserCustomFolders();
        }
        if ("getAllFolders".equals(getOp())) {
            getUserFolders();
        }
        if ("readDelete".equals(getOp())) {
            log.debug("read folder information to delete...............");
            read();
            if (!resultDTO.isFailure()) {
                MailTrayCmd mailTrayCmd = new MailTrayCmd();
                mailTrayCmd.setOp("readFolderToEmpty");
                mailTrayCmd.putParam("toEmptyFolder", resultDTO.get("folderId"));
                mailTrayCmd.putParam("companyId", resultDTO.get("companyId"));
                mailTrayCmd.putParam("userMailId", resultDTO.get("userMailId"));
                mailTrayCmd.executeInStateless(ctx);
                resultDTO.putAll(mailTrayCmd.getResultDTO());
                //read subfolders...
                FolderDTO folderDTO = new FolderDTO();
                folderDTO.put("folderId", resultDTO.get("folderId"));
                List<FolderPojo> childrens = getSubFolders(folderDTO, true);
                boolean found = false;
                for (int i = 0; i < childrens.size() && !found; i++) {
                    FolderPojo fp = childrens.get(i);
                    if (fp.getFolderId().equals((Integer) resultDTO.get("folderId"))) {
                        childrens.remove(fp);
                        found = true;
                    }
                }
                resultDTO.put("subFoldersList", childrens);
            }
        }
        if ("changeOpenStatus".equals(getOp())) {
            changeFolderOpenStatus(new Integer(paramDTO.get("folderId").toString()));
        }
    }

    public void create() {
        Integer typeFolder = new Integer(WebMailConstants.FOLDER_DEFAULT);
        Date actualDate = new Date();

        FolderDTO folderDto = new FolderDTO();
        folderDto.put("folderName", paramDTO.get("folderName"));
        folderDto.put("folderDate", DateUtils.dateToInteger(actualDate));
        folderDto.put("folderType", typeFolder);
        folderDto.put("userMailId", new Integer(paramDTO.get("userMailId").toString()));   //be obtained from the sesion
        folderDto.put("companyId", new Integer(paramDTO.get("companyId").toString()));
        folderDto.put("columnToShow", paramDTO.get("columnToShow"));
        folderDto.put("isOpen", paramDTO.get("isOpen"));
        folderDto.put("parentFolderId", paramDTO.get("parentFolderId"));

        ExtendedCRUDDirector.i.create(folderDto, resultDTO, false);

    }

    public void update() {
        Integer folderKey = new Integer((String) paramDTO.get("folderId"));
        FolderDTO folderDto = new FolderDTO();

        folderDto.put("folderId", folderKey);
        folderDto.put("folderName", paramDTO.get("folderName"));
        folderDto.put("columnToShow", paramDTO.get("columnToShow"));
        folderDto.put("parentFolderId", paramDTO.get("parentFolderId"));

        ExtendedCRUDDirector.i.update(folderDto, resultDTO, false, false, false, "Fail");
    }

    public void delete(SessionContext ctx) {
        log.debug("deleting folders tree.......................");
        Integer folderKey = new Integer((String) paramDTO.get("folderId"));

        FolderDTO folderDto = new FolderDTO();
        folderDto.put("folderId", folderKey);
        try {
            //Create folderTreeModel
            List<FolderPojo> childrens = getSubFolders(folderDto, false);
            for (FolderPojo children : childrens) {
                deleteFolder(ctx, children.getFolderId());
            }
        } catch (EJBFactoryException ef) {
            log.error("ERROR IN EJB ");
            ef.printStackTrace();
        }
    }

    private void deleteFolder(SessionContext ctx, Integer folderId) {
        Integer folderKey = new Integer(folderId);

        FolderDTO folderDto = new FolderDTO();
        folderDto.put("folderId", folderKey);

        if (IsFolderDefault(folderDto)) {
            try {
                Folder folder = (Folder) EJBFactory.i.findEJB(folderDto);
                //Clean folder
                MailTrayCmd mailTrayCmd = new MailTrayCmd();
                mailTrayCmd.setOp("emptyFolderComplete");
                mailTrayCmd.putParam("toEmptyFolderId", folderKey);
                mailTrayCmd.putParam("companyId", folder.getCompanyId());
                mailTrayCmd.putParam("userMailId", folder.getUserMailId());
                mailTrayCmd.executeInStateless(ctx);

                //remove associated filters
                Collection filters = folder.getFilters();
                Object[] obj = filters.toArray();
                for (int i = 0; i < obj.length; i++) {
                    Filter filt = (Filter) obj[i];

                    FilterCmd filterCmd = new FilterCmd();
                    filterCmd.putParam("op", "delete");
                    filterCmd.putParam("filterId", filt.getFilterId());
                    filterCmd.executeInStateless(ctx);
                }

                EJBFactory.i.removeEJB(folderDto);
            } catch (EJBFactoryException ef) {
                log.error("ERROR IN EJB ");
                ef.printStackTrace();
            }

        } else {
            resultDTO.addResultMessage("Webmail.folder.errors.deleteFolder");
            resultDTO.setResultAsFailure();
        }

    }

    public void read() {
        Integer folderKey = new Integer((String) paramDTO.get("folderId"));
        FolderDTO folderDto = new FolderDTO();
        folderDto.put("folderId", folderKey);
        folderDto.put("folderName", paramDTO.get("folderName"));

        ExtendedCRUDDirector.i.read(folderDto, resultDTO, false);
    }

    public boolean IsFolderDefault(FolderDTO FDto) {
        Integer defaultFolder = new Integer(WebMailConstants.FOLDER_DEFAULT);
        try {
            Folder folder = (Folder) EJBFactory.i.findEJB(FDto);
            if (folder.getFolderType().equals(defaultFolder)) {
                return true;
            }
        } catch (EJBFactoryException ef) {
            log.error("ERROR IN EJB" + ef);
            ef.printStackTrace();
        }
        return false;
    }

    public boolean mailNotInFolder(FolderDTO FDto) {

        try {
            Folder folder = (Folder) EJBFactory.i.findEJB(FDto);
            if (folder.getMails().isEmpty()) {
                return true;
            }
        } catch (EJBFactoryException ef) {
            log.error("ERROR IN EJB" + ef);
            ef.printStackTrace();
        }
        return false;
    }

    public void getUserCustomFolders() {
        Integer userMailId = new Integer(paramDTO.get("userMailId").toString());
        UserMailDTO userMailDto = new UserMailDTO();
        userMailDto.put("userMailId", userMailId);

        UserMail userMail = (UserMail) EJBFactory.i.findEJB(userMailDto);

        resultDTO.put("customFoldersList", getCustomFolders(userMail.getUserMailId(), userMail.getCompanyId()));

    }

    public void getUserFolders() {
        Integer userMailId = new Integer(paramDTO.get("userMailId").toString());
        UserMailDTO userMailDto = new UserMailDTO();
        userMailDto.put("userMailId", userMailId);

        UserMail userMail = (UserMail) EJBFactory.i.findEJB(userMailDto);

        resultDTO.put("foldersList", getFolders(userMail.getUserMailId(), userMail.getCompanyId()));

    }

    private List getCustomFolders(Integer emailUserId, Integer companyId) {
        FolderHome folderHome =
                (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        List folderList = new ArrayList();
        try {
            Collection defaultFolders = folderHome.findDefaultFolders(emailUserId, companyId);
            for (Object defaultFolderObject : defaultFolders) {
                Folder defaultFolder = (Folder) defaultFolderObject;
                Map mapFolder = new HashMap();
                mapFolder.put("folderName", defaultFolder.getFolderName());
                mapFolder.put("folderId", defaultFolder.getFolderId());
                mapFolder.put("unReadMails", getUnreadMessagesCounter(defaultFolder.getFolderId(),
                        companyId));
                mapFolder.put("parentFolderId", defaultFolder.getParentFolderId());
                mapFolder.put("folderType", defaultFolder.getFolderType());
                folderList.add(mapFolder);
            }
        } catch (FinderException e) {
            log.debug("-> Read custom folders emailUserId=" + emailUserId + ", companyId=" + companyId + " FAIL");
        }
        return folderList;
    }

    private List getFolders(Integer emailUserId, Integer companyId) {
        FolderHome folderHome =
                (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        List folderList = new ArrayList();
        try {
            Collection defaultFolders = folderHome.findByUserMailKeySort(emailUserId, companyId);
            for (Object defaultFolderObject : defaultFolders) {
                Folder defaultFolder = (Folder) defaultFolderObject;
                Map mapFolder = new HashMap();
                mapFolder.put("folderName", defaultFolder.getFolderName());
                mapFolder.put("folderId", defaultFolder.getFolderId());
                mapFolder.put("unReadMails", getUnreadMessagesCounter(defaultFolder.getFolderId(),
                        companyId));
                mapFolder.put("parentFolderId", defaultFolder.getParentFolderId());
                mapFolder.put("folderType", defaultFolder.getFolderType());
                if (defaultFolder.getFolderType().toString().equals(WebMailConstants.FOLDER_DEFAULT)) {
                    folderList.add(mapFolder);
                } else {
                    folderList.add(0, mapFolder);
                }
            }
        } catch (FinderException e) {
            log.debug("-> Read custom folders emailUserId=" + emailUserId + ", companyId=" + companyId + " FAIL");
        }
        return folderList;
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

    private int getUnreadMessagesCounter(Integer folderId, Integer companyId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        try {
            return mailHome.selectCountUnReadMessages(folderId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot count unread messages...");
        }
        return 0;
    }

    private List<FolderPojo> getSubFolders(FolderDTO folderDto, boolean sort) {
        Folder folder = (Folder) EJBFactory.i.findEJB(folderDto);
        List<HashMap> customFolders = getCustomFolders(folder.getUserMailId(), folder.getCompanyId());
        FolderTreeModel treeModel = new FolderTreeModel(null);
        treeModel.fillTree(customFolders);
        FolderPojo folderPojo = new FolderPojo();
        folderPojo.setFolderId(folder.getFolderId());
        DefaultMutableTreeNode treeNode = treeModel.findNodeInTree(new DefaultMutableTreeNode(folderPojo));
        List<FolderPojo> childrens = treeModel.getDescendantFoldersList(treeNode);
        if (sort) {
            childrens = treeModel.sortList(childrens);
        }
        return (childrens);
    }

    private void changeFolderOpenStatus(Integer folderId) {
        FolderDTO folderDto = new FolderDTO();
        folderDto.put("folderId", folderId);
        Folder folder = (Folder) EJBFactory.i.findEJB(folderDto);
        folder.setIsOpen(!folder.getIsOpen());
    }
}

