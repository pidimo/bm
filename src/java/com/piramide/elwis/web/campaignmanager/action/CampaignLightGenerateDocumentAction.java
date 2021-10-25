package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignGenerationCmd;
import com.piramide.elwis.cmd.campaignmanager.GenerationCommunicationCreateCmd;
import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.cmd.utils.WordDocumentBuilderUtil;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.util.GenerateDocumentHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.exception.ListStructureNotFoundException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.alfacentauro.fantabulous.web.FantabulousManager;
import org.apache.struts.action.*;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignLightGenerateDocumentAction.java 06-may-2009 15:31:56 $
 */
public class CampaignLightGenerateDocumentAction extends CampaignManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.warn("Executing CampaignLightGenerateDocumentAction...." + request.getParameterMap());
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionErrors errors = new ActionErrors();
        //validate if current user is internal
        if (!isInternalUser(request)) {
            errors.add("userError", new ActionError("Campaign.generate.error.internalUser"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("requestLocale", request.getLocale().toString());
        Integer templateIdAsInt = new Integer(defaultForm.getDto("templateId").toString());

        boolean isCreateCommunications = (defaultForm.getDto("createComm") != null);
        String communicationTitle = (String) defaultForm.getDto("docCommTitle");


        errors = GenerateDocumentHelper.validateDocumentTemplates(templateIdAsInt, mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        //set fantabulous list strucuture in paramDTO
        FantabulousManager fantabulousManager = FantabulousManager.loadFantabulousManager(request.getSession().getServletContext(), request);
        try {
            ListStructure listStructure = fantabulousManager.getList("DocumentGenerationCampLigthContactList");
            listStructure = GenerateDocumentHelper.setColumnOrders(listStructure, defaultForm);
            defaultForm.setDto("FantaStructure", listStructure);
        } catch (ListStructureNotFoundException e) {
            log.warn("Not found list........... ", e);
        }

        ActionForward forward = super.execute(mapping, defaultForm, request, response);

        log.warn("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if ("Success".equals(forward.getName())) {

            String companyId = dtoValues.get("companyId").toString();
            String campaignId = dtoValues.get("campaignId").toString();
            String templateId = dtoValues.get("templateId").toString();
            String templateName = dtoValues.get("templateName").toString();

            User user = RequestUtils.getUser(request);
            String userId = user.getValue("userId").toString();

            List templateValueList = (List) dtoValues.get("templateValuesList");
            String[] templateFieldNames = (String[]) dtoValues.get("fieldNames");

            //clear previous generated document folder
            ElwisCacheManager.deleteCampaignDocumentFolder(companyId, campaignId, userId);

            WordDocumentBuilderUtil documentBuilderUtil = null;
            try {
                documentBuilderUtil = new WordDocumentBuilderUtil();
            } catch (CreateException e) {
                log.warn("can not create document builder service..", e);
                errors.add("serverError", new ActionError("Campaign.document.error.notInitiated"));
                saveErrors(request, errors);
                return mapping.findForward("Fail");
            }

            List summaryList = new ArrayList();
            for (Iterator iterator = templateValueList.iterator(); iterator.hasNext();) {
                Map templateValueMap = (Map) iterator.next();
                ArrayByteWrapper byteWrapper = (ArrayByteWrapper) templateValueMap.get("freeText");
                List fieldValuesList = (List) templateValueMap.get("valuesList");
                String languageId = templateValueMap.get("languageId").toString();
                String languageName = templateValueMap.get("languageName").toString();
                try {
                    byte[] wordDoc = documentBuilderUtil.renderCampaignDocument(fieldValuesList, templateFieldNames, byteWrapper.getFileData());
                    ElwisCacheManager.saveGeneratedCampaignDocument(companyId, campaignId, userId, wordDoc, templateId, languageId);
                } catch (CreateDocumentException e) {
                    log.warn("Error in document builder...", e);
                    ActionError err = GenerateDocumentHelper.errorMessage(e, templateName, languageName);
                    if (err != null) {
                        errors.add("docError", err);
                    }
                    if (!errors.isEmpty()) {
                        saveErrors(request, errors);
                        return mapping.findForward("Fail");
                    }
                } catch (IOException e) {
                    log.debug("Error when document generated in saved in elwis cache...", e);
                    errors.add("saveError", new ActionError("Campaign.document.error.cannotSavedInCache"));
                    saveErrors(request, errors);
                    return mapping.findForward("Fail");
                }

                //document summary information
                Map generationResumeMap = new HashMap();
                generationResumeMap.put("languageId", languageId);
                generationResumeMap.put("language", languageName);
                generationResumeMap.put("count", templateValueMap.get("recipientCount"));
                summaryList.add(generationResumeMap);
            }

            request.setAttribute("summaryList", summaryList);

            request.setAttribute("totalContact", dtoValues.get("totalContact"));
            request.setAttribute("totalProcessed", dtoValues.get("totalProcessed"));
            request.setAttribute("totalFail", dtoValues.get("totalFail"));

            request.setAttribute("templateId", templateId);
            request.setAttribute("templateName", templateName);

            //create communications
            if (isCreateCommunications) {
                createDocumentGenerationCommunications(new Integer(campaignId), new Integer(companyId),
                        new Integer(templateId), communicationTitle, (List) dtoValues.get("contactInfoList"), request);
            }
        }

        log.warn("end action CampaignGenerateDocumentAction...");

        return forward;
    }

    private void createDocumentGenerationCommunications(Integer campaignId, Integer companyId, Integer templateId,
                                                        String note, List<Map> contactInfoList, HttpServletRequest request) {
        log.debug("create comm in campaign light document generation......");

        User user = RequestUtils.getUser(request);
        Integer userId = new Integer(user.getValue("userId").toString());

        Integer generationId = null;
        //create campaign generation
        CampaignGenerationCmd generationCmd = new CampaignGenerationCmd();
        generationCmd.putParam("op", "create");
        generationCmd.putParam("campaignId", campaignId);
        generationCmd.putParam("templateId", templateId);
        generationCmd.putParam("companyId", companyId);
        generationCmd.putParam("userId", userId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(generationCmd, request);
            if (!resultDTO.isFailure()) {
                generationId = (Integer) resultDTO.get("generationId");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute generation create cmd...", e);
        }

        GenerationCommunicationCreateCmd commCreateCmd = new GenerationCommunicationCreateCmd();
        commCreateCmd.putParam("op", "campaignLightGeneration");
        commCreateCmd.putParam("generationId", generationId);
        commCreateCmd.putParam("campaignId", campaignId);
        commCreateCmd.putParam("companyId", companyId);
        commCreateCmd.putParam("templateId", templateId);
        commCreateCmd.putParam("userId", userId);
        commCreateCmd.putParam("note", note);
        commCreateCmd.putParam("addressInfoList", contactInfoList);

        try {
            BusinessDelegate.i.execute(commCreateCmd, request);
        } catch (AppLevelException e) {
            log.debug("Error in execute communications create cmd...", e);
        }
    }

    private boolean isInternalUser(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        return com.piramide.elwis.web.admin.el.Functions.isInternalUser(user.getValue("userId"));
    }


}
