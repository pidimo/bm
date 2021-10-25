package com.piramide.elwis.web.catalogmanager.util;

import com.piramide.elwis.cmd.contactmanager.ContactWebDocumentExecuteCmd;
import com.piramide.elwis.dto.catalogmanager.WebParameterDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Util to generate web document parameters
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentExecuteUtil {
    private Log log = LogFactory.getLog(this.getClass());

    private String url;

    public WebDocumentExecuteUtil() {
        this.url = null;
    }

    public String getUrl() {
        return url;
    }

    public ActionErrors generateDocumentUrl(Integer contactId, ActionMapping mapping, HttpServletRequest request) {
        log.debug("Generate url to communication..." + contactId);

        ActionErrors errors = new ActionErrors();

        User user = RequestUtils.getUser(request);

        ContactWebDocumentExecuteCmd executeCmd = new ContactWebDocumentExecuteCmd();
        executeCmd.putParam("contactId", contactId);
        executeCmd.putParam("userAddressId", user.getValue("userAddressId"));
        executeCmd.putParam("requestLocale", user.getValue("locale"));

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(executeCmd, request);
            log.debug("RESULT:" + resultDTO);
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd..", e);
        }

        if (resultDTO != null) {
            if (resultDTO.isFailure()) {
                errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            }

            if (errors.isEmpty()) {
                this.url = composeWedDocumentUrl(resultDTO);
            }
        }

        return errors;
    }

    private String composeWedDocumentUrl(ResultDTO resultDTO) {
        String webDocumentUrl = "";

        String generationId = (String) resultDTO.get("webGenerateUUID");

        Map variableValuesMap = (Map) resultDTO.get("mapVariableValues");
        List<WebParameterDTO> webParameterDTOList = (List<WebParameterDTO>) resultDTO.get("listWebParameterDTO");
        Map webDocumentMap = (Map) resultDTO.get("webDocumentMap");

        if (variableValuesMap != null && webParameterDTOList != null && webDocumentMap != null) {
            String domain = webDocumentMap.get("url").toString();
            String generationIdParam = composeGenerationIdParam(generationId);
            String params = composeParameters(webParameterDTOList, variableValuesMap);

            webDocumentUrl = domain + (domain.indexOf("?") > 0 ? "&" : "?") + generationIdParam;

            if (params != null && params.length() > 0) {
                webDocumentUrl = webDocumentUrl + "&" + params;
            }
        }

        log.debug("Final web document URL:" + webDocumentUrl);

        return webDocumentUrl;
    }

    private String composeGenerationIdParam(String generationId) {
        return "generationId=" + generationId;
    }

    private String composeParameters(List<WebParameterDTO> webParameterDTOList, Map variableValuesMap) {
        String allParams = "";

        for (WebParameterDTO webParameterDTO : webParameterDTOList) {
            String param = composeParameter(webParameterDTO, variableValuesMap);

            if (param != null) {
                if (allParams.length() > 0) {
                    allParams = allParams + "&";
                }
                allParams = allParams + param;
            }
        }

        return allParams;
    }

    private String composeParameter(WebParameterDTO webParameterDTO, Map variableValuesMap) {
        String param = null;

        String parameterName = (String) webParameterDTO.get("parameterName");
        String variableName = (String) webParameterDTO.get("variableName");

        if (parameterName != null && variableName != null) {
            param = parameterName + "=";
            if (variableValuesMap.get(variableName) != null) {
                String value = filterParamValue(variableValuesMap.get(variableName).toString());
                param = param + value;
            }
        }

        return param;
    }

    private String filterParamValue(String value) {
        if (value != null) {
            value = Functions.encode(value);
        }
        return value;
    }

}
