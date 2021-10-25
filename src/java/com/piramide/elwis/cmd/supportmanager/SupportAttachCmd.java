package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.dto.supportmanager.SupportAttachDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseActivityDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * @author : ivan
 * @version : $Id SupportAttachCmd ${time}
 */
public class SupportAttachCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SupportAttachCmd... \n" +
                paramDTO + "\n-------------------------------------------");
        String op = this.getOp();

        boolean isRead = true;
        SupportFreeTextHome freeTextHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);

        SupportFreeText freeText = null;

        if ("create".equals(op)) {
            isRead = false;
            byte[] uploadFile = ((ArrayByteWrapper) paramDTO.get("uploadFile")).getFileData();
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer freeTextType = Integer.valueOf(String.valueOf(FreeTextTypes.FREETEXT_SUPPORT));

            try {
                freeText = freeTextHome.create(uploadFile, companyId, freeTextType);

                SupportAttachDTO dto = new SupportAttachDTO();
                if (paramDTO.containsKey("caseId")) {
                    Integer caseId = new Integer((String) paramDTO.get("caseId"));
                    try {
                        SupportCaseActivity activity = (SupportCaseActivity) EJBFactory.i.callFinder(new SupportCaseActivityDTO(),
                                "findByTypeOpen",
                                new Object[]{caseId, new Integer(SupportConstants.OPENTYPE_OPEN)});
                        if (activity == null) {
                            activity = (SupportCaseActivity) EJBFactory.i.callFinder(new SupportCaseActivityDTO(),
                                    "findByTypeOpen",
                                    new Object[]{caseId, new Integer(SupportConstants.OPENTYPE_CASECLOSE)});
                        }
                        dto.put("activityId", activity.getActivityId());
                        dto.put("caseId", caseId);
                    } catch (Exception e) {
                        log.debug(" ... error ...case notFound ... ");
                        resultDTO.setResultAsFailure();
                        return;
                    }

                } else if (paramDTO.containsKey("articleId")) {//Is attach for articles!!!
                    dto.put("articleId", paramDTO.get("articleId"));
                } else {
                    resultDTO.setResultAsFailure();
                    return;
                }
                dto.put("comment", paramDTO.get("comment"));
                dto.put("companyId", paramDTO.get("companyId"));
                dto.put("freetextId", freeText.getFreeTextId());
                dto.put("size", paramDTO.get("size"));
                dto.put("supportAttachName", paramDTO.get("supportAttachName"));
                dto.put("uploadDateTime", paramDTO.get("uploadDateTime"));
                dto.put("userId", paramDTO.get("userId"));
                log.debug("Create attach with:" + dto);
                ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            } catch (CreateException e) {
                log.debug("Cannot create FreeTextSupport...");
            }
        }
        if ("update".equals(op)) {
            isRead = false;
            SupportAttachDTO dto = new SupportAttachDTO(paramDTO);
            SupportAttach attach = (SupportAttach) ExtendedCRUDDirector.i.update(dto,
                    resultDTO,
                    false,
                    true,
                    false,
                    "Fail");

            if (!"Fail".endsWith(resultDTO.getForward()) && !"".equals(resultDTO.getForward())) {
                if (null != paramDTO.get("uploadFile")) {
                    byte[] uploadFile = ((ArrayByteWrapper) paramDTO.get("uploadFile")).getFileData();
                    try {
                        SupportFreeText newFreeText = freeTextHome.findByPrimaryKey(attach.getFreetextId());
                        newFreeText.setFreeTextValue(uploadFile);
                    } catch (FinderException e) {
                        log.warn("Cannot Find SupportFreeText...");
                    }
                }
            }
        }

        if ("delete".equals(op)) {
            isRead = false;
            SupportAttachDTO dto = new SupportAttachDTO(paramDTO);
            SupportAttach attach = (SupportAttach) ExtendedCRUDDirector.i.read(dto, resultDTO, false);

            if (null != attach) {
                try {
                    SupportFreeText newFreeText = freeTextHome.findByPrimaryKey(attach.getFreetextId());
                    attach.remove();
                    newFreeText.remove();
                } catch (FinderException e) {
                    log.warn("Cannot Find SupportFreeText...");
                } catch (RemoveException e) {
                    log.warn("Cannot remove Freetext...");
                }
            } else {
                resultDTO.setForward("Fail");
            }
        }

        if (isRead) {
            SupportAttachDTO dto = new SupportAttachDTO(paramDTO);
            SupportAttach attach = (SupportAttach) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
            if (attach != null) {
                if (attach.getArticleId() != null) {
                    ArticleHome articleHome = (ArticleHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLE);
                    try {
                        Article article = articleHome.findByPrimaryKey(attach.getArticleId());
                        if (article != null) {
                            resultDTO.put("createUserId", article.getCreateUserId());
                        }
                    } catch (FinderException e) {
                        log.debug("article notFound ... " + e);
                    }
                }
            }
        }

        // for ArticleHistory   YUMI
        if (paramDTO.get("articleId") != null &&
                !"".equals(paramDTO.get("articleId").toString().trim()) &&
                !"".equals(getOp())) {
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO dto = new ArticleHistoryDTO();
            dto.put("userId", paramDTO.get("userId"));
            dto.put("companyId", paramDTO.get("companyId"));
            dto.put("articleId", paramDTO.get("articleId"));
            dto.put("action", paramDTO.get("actionValue"));
            cmd.createHistory(dto);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
