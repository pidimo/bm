package com.piramide.elwis.web.campaignmanager.util;

import com.piramide.elwis.cmd.campaignmanager.ReadAllCampaignTemplateFreeTextCmd;
import com.piramide.elwis.cmd.utils.WordDocumentBuilderUtil;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.exception.ColumnNotFoundException;
import org.alfacentauro.fantabulous.format.structure.Column;
import org.alfacentauro.fantabulous.format.structure.Configuration;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.ejb.CreateException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Util to helper in actions to generate campaign word documents
 *
 * @author Miky
 * @version $Id: GenerateDocumentHelper.java 06-may-2009 11:24:16 $
 */
public class GenerateDocumentHelper {
    private static Log log = LogFactory.getLog(GenerateDocumentHelper.class);

    /**
     * validate word document templates
     *
     * @param templateId
     * @param mapping
     * @param request
     * @return errors
     */
    public static ActionErrors validateDocumentTemplates(Integer templateId, ActionMapping mapping, HttpServletRequest request) {
        log.warn("validate word document template....");

        ActionErrors errors = new ActionErrors();
        //validate template text
        ReadAllCampaignTemplateFreeTextCmd templateFreeTextCmd = new ReadAllCampaignTemplateFreeTextCmd();
        templateFreeTextCmd.putParam("templateId", templateId);

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(templateFreeTextCmd, request);
        } catch (AppLevelException e) {
            log.warn("Error in read templates...", e);
            errors.add("serverError", new ActionError("msg.ServerError"));
            return errors;
        }

        log.warn("after read word templates.." + resultDTO);

        if (resultDTO.isFailure()) {
            log.warn("Fail read template texts");
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            return errors;
        } else {
            List templateTextList = (List) resultDTO.get("templateTextList");
            String[] templateFieldNames = (String[]) resultDTO.get("fieldNames");

            String[] valuesTest = new String[templateFieldNames.length];
            for (int i = 0; i < templateFieldNames.length; i++) {
                valuesTest[i] = "";
            }

            List valuesTestList = new ArrayList();
            valuesTestList.add(valuesTest);

            WordDocumentBuilderUtil documentBuilderUtil = null;
            try {
                documentBuilderUtil = new WordDocumentBuilderUtil();
            } catch (CreateException e) {
                log.debug("can not create document byuilder service..", e);
                errors.add("serverError", new ActionError("Campaign.document.error.notInitiated"));
                return errors;
            }

            for (Iterator iterator = templateTextList.iterator(); iterator.hasNext();) {
                Map templateTextMap = (Map) iterator.next();
                ArrayByteWrapper byteWrapper = (ArrayByteWrapper) templateTextMap.get("byteWrapper");

                try {
                    documentBuilderUtil.renderCampaignDocument(valuesTestList, templateFieldNames, byteWrapper.getFileData());
                } catch (CreateDocumentException e) {
                    log.debug("Error in document builder...", e);
                    String templateName = templateTextMap.get("templateName").toString();
                    String languageName = templateTextMap.get("languageName").toString();

                    ActionError err = errorMessage(e, templateName, languageName);
                    if (err != null) {
                        errors.add("docError", err);
                    }
                }
            }
        }
        log.warn("end validate word document template....");
        return errors;
    }

    /**
     * compose campaign document error
     *
     * @param e            CreateDocumentException
     * @param templateName
     * @param languageName
     * @return ActionError
     */
    public static ActionError errorMessage(CreateDocumentException e, String templateName, String languageName) {
        ActionError error = null;
        if ("Document.error.wordVersion".equals(e.getMessage())) {
            error = new ActionError("Campaign.document.error.wordVersion", templateName, languageName);

        } else if ("Document.error.campaign".equals(e.getMessage())) {
            error = new ActionError("Campaign.document.error.campaign", templateName, languageName);

        } else if ("Document.error.invalidVariable".equals(e.getMessage())) {
            error = new ActionError("Campaign.document.error.invalidVariable", templateName, languageName);
        }
        return error;
    }

    /**
     * Set column orders in list structure
     *
     * @param listStructure
     * @param defaultForm
     * @return listStructure
     */
    public static ListStructure setColumnOrders(ListStructure listStructure, DefaultForm defaultForm) {
        String firstColumnOrder = (String) defaultForm.getDto("firstOrder");
        String secondColumnOrder = (String) defaultForm.getDto("secondOrder");

        List fantaColumnOrderList = new ArrayList();
        if (!GenericValidator.isBlankOrNull(firstColumnOrder)) {
            boolean isAscendant = CampaignConstants.ASCENDING_ORDER.equals(defaultForm.getDto("firstOrderType"));
            fantaColumnOrderList = getStructureColumnOrders(listStructure, firstColumnOrder, isAscendant);
        }
        if (!GenericValidator.isBlankOrNull(secondColumnOrder)) {
            boolean isAscendant = CampaignConstants.ASCENDING_ORDER.equals(defaultForm.getDto("secondOrderType"));
            fantaColumnOrderList.addAll(getStructureColumnOrders(listStructure, secondColumnOrder, isAscendant));
        }

        if (fantaColumnOrderList.isEmpty()) {
            //set default order
            fantaColumnOrderList = getStructureColumnOrders(listStructure, CampaignConstants.DocOrderedColumn.CONTACT_NAME.getConstantAsString(), true);
            fantaColumnOrderList.addAll(getStructureColumnOrders(listStructure, CampaignConstants.DocOrderedColumn.CONTACTPERSON_NAME.getConstantAsString(), true));
        }
        listStructure.setOrder(fantaColumnOrderList);

        return listStructure;
    }

    /**
     * return fantabulous column order List
     *
     * @param listStructure
     * @param columnKey
     * @param isAscendant
     * @return list
     */
    private static List getStructureColumnOrders(ListStructure listStructure, String columnKey, boolean isAscendant) {
        List fantaColumnOrderList = new ArrayList();
        Configuration conf = listStructure.getConfiguration();

        for (int i = 0; i < CampaignConstants.DocOrderedColumn.values().length; i++) {
            CampaignConstants.DocOrderedColumn docOrderedColumn = CampaignConstants.DocOrderedColumn.values()[i];
            if (docOrderedColumn.getConstantAsString().equals(columnKey)) {
                String[] columnNames = docOrderedColumn.getColumnNames();
                for (int j = 0; j < columnNames.length; j++) {
                    String columnName = columnNames[j];
                    try {
                        Column column = conf.getColumn(columnName);
                        column.setAscendent(isAscendant);

                        fantaColumnOrderList.add(column);
                    } catch (ColumnNotFoundException e) {
                        log.debug("Fantabulos column not found..." + e);
                    }
                }
            }
        }

        return fantaColumnOrderList;
    }
}
