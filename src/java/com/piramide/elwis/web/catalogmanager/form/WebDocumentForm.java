package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.dto.catalogmanager.WebParameterDTO;
import com.piramide.elwis.dto.catalogmanager.WebParameterWrapperDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.url.UrlValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentForm extends DefaultForm {

    public Object[] getWebParameterId() {
        List list = (List) getDto("webParameterIds");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setWebParameterId(Object[] array) {
        if (array != null) {
            setDto("webParameterIds", Arrays.asList(array));
        }
    }

    public Object[] getNewWebParameter() {
        List list = (List) getDto("newWebParameterKeys");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setNewWebParameter(Object[] array) {
        if (array != null) {
            setDto("newWebParameterKeys", Arrays.asList(array));
        }
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        log.debug("Executing validate WebDocumentForm......." + getDtoMap());

        ActionErrors errors = super.validate(actionMapping, request);
        validateUrl(errors, request);

        processWebParameters(errors, request);

        return errors;
    }

    private void validateUrl(ActionErrors errors, HttpServletRequest request) {
        String url = (String) getDto("url");

        if (!GenericValidator.isBlankOrNull(url)) {
            UrlValidator urlValidator = new UrlValidator();
            if (!urlValidator.isValid(url)) {
                errors.add("urlError", new ActionError("WebDocument.error.notValidUrl", JSPHelper.getMessage(request, "WebDocument.url")));
            }
        }
    }

    private void processWebParameters(ActionErrors errors, HttpServletRequest request) {
        List webParameterIdList = (List) getDto("webParameterIds");
        List newWebParameterKeyList = (List) getDto("newWebParameterKeys");

        WebParameterWrapperDTO wrapperDTO = new WebParameterWrapperDTO();
        wrapperDTO.processParamDtoValues(getDtoMap(), webParameterIdList, newWebParameterKeyList);

        ActionErrors parameterErrors = validateWebParameters(wrapperDTO, request);
        if (!parameterErrors.isEmpty()) {
            errors.add(parameterErrors);
        }

        this.setDto("webParameterWrapper", wrapperDTO);
    }

    private ActionErrors validateWebParameters(WebParameterWrapperDTO wrapperDTO, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        for (int i = 0; i < VariableConstants.VariableType.values().length; i++) {
            VariableConstants.VariableType variableType = VariableConstants.VariableType.values()[i];

            ActionErrors oldParameterErrors = validateWebParameters(wrapperDTO.getWebParameterListByType(variableType));
            if (!oldParameterErrors.isEmpty()) {
                errors.add(oldParameterErrors);
                break;
            }

            ActionErrors newsParameterErrors = validateWebParameters(wrapperDTO.getNewsWebParameterListByType(variableType));
            if (!newsParameterErrors.isEmpty()) {
                errors.add(newsParameterErrors);
                break;
            }
        }

        return errors;
    }

    private ActionErrors validateWebParameters(List<WebParameterDTO> webParameterDTOList) {
        ActionErrors errors = new ActionErrors();

        for (WebParameterDTO webParameterDTO : webParameterDTOList) {
            errors = validateWebParameter(webParameterDTO);
            if (!errors.isEmpty()) {
                break;
            }
        }
        return errors;
    }

    private ActionErrors validateWebParameter(WebParameterDTO webParameterDTO) {
        ActionErrors errors = new ActionErrors();

        String parameterName = (String) webParameterDTO.get("parameterName");
        String variableName = (String) webParameterDTO.get("variableName");

        if ((GenericValidator.isBlankOrNull(parameterName) && !GenericValidator.isBlankOrNull(variableName))
                || (GenericValidator.isBlankOrNull(variableName) && !GenericValidator.isBlankOrNull(parameterName))) {
            errors.add("paramError", new ActionError("WebDocument.error.parameter"));
        }
        return errors;
    }

}
