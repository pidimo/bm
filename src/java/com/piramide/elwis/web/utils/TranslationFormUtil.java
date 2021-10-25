package com.piramide.elwis.web.utils;

import com.piramide.elwis.cmd.catalogmanager.LanguageUtilCmd;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class TranslationFormUtil {
    private Log log = LogFactory.getLog(TranslationFormUtil.class);

    private Map dtoMap;

    private static enum UIElement {
        language("language"),
        text("text"),
        isDefault("isDefault"),
        separator("_"),
        hasDefinedDefaultValue("hasDefinedDefaultValue"),
        uiLanguages("dto(uiLanguages)");

        private String constant;

        UIElement(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public TranslationFormUtil(Map dtoMap) {
        this.dtoMap = dtoMap;
    }

    public void validate(HttpServletRequest request, ActionErrors errors) {
        ActionError defaultError = validateDefault(request);
        if (null != defaultError) {
            errors.add("defaultError", defaultError);
        }

        ActionError languageError = validateLanguages(request);
        if (null != languageError) {
            errors.add("languageError", languageError);
        }
    }

    public void validateLanguages(HttpServletRequest request, ActionErrors errors) {
        ActionError languageError = validateLanguages(request);
        if (null != languageError) {
            errors.add("languageError", languageError);
        }
    }

    public static List<String> getUILanguages(HttpServletRequest request) {
        String[] uiLanguagesAsArray = new String[]{};
        if (null != request.getParameterValues(UIElement.uiLanguages.getConstant())) {
            uiLanguagesAsArray = request.getParameterValues(UIElement.uiLanguages.getConstant());
        }

        return Arrays.asList(uiLanguagesAsArray);
    }

    protected ActionError validateDefault(HttpServletRequest request) {
        if (GenericValidator.isBlankOrNull((String) dtoMap.get(UIElement.isDefault.getConstant()))) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, "common.defaultTranslation"));
        }

        String defaultUiLanguageId = (String) dtoMap.get(UIElement.isDefault.getConstant());
        if (GenericValidator.isBlankOrNull((String) dtoMap.get(buildTextKey(defaultUiLanguageId)))) {
            return new ActionError("errors.required", dtoMap.get(buildLanguageKey(defaultUiLanguageId)));
        }

        return null;
    }

    protected ActionError validateLanguages(HttpServletRequest request) {
        List<String> uiLanguages = getUILanguages(request);

        for (String uiLanguage : uiLanguages) {
            String text = (String) dtoMap.get(buildTextKey(uiLanguage));
            if (null != text &&
                    !"".equals(text.trim())) {
                LanguageDTO languageDTO = getLanguage(Integer.valueOf(uiLanguage), request);
                if (null == languageDTO) {
                    return new ActionError("customMsg.NotFound", dtoMap.get(buildLanguageKey(uiLanguage)));
                }
            }
        }

        return null;
    }

    private LanguageDTO getLanguage(Integer languageId, HttpServletRequest request) {
        LanguageUtilCmd languageUtilCmd = new LanguageUtilCmd();
        languageUtilCmd.setOp("getLanguage");
        languageUtilCmd.putParam("languageId", Integer.valueOf(languageId));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(languageUtilCmd, request);
            return (LanguageDTO) resultDTO.get("getLanguage");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + LanguageUtilCmd.class.getName() + " FAIL", e);
        }
        return null;
    }

    private String buildTextKey(String languageId) {
        return UIElement.text.getConstant() +
                UIElement.separator.getConstant() +
                languageId;
    }

    private String buildLanguageKey(String languageId) {
        return UIElement.language.getConstant() +
                UIElement.separator.getConstant() +
                languageId;
    }
}
