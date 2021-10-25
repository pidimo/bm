package com.piramide.elwis.web.admin.form;

import com.octo.captcha.service.CaptchaServiceException;
import com.piramide.elwis.web.admin.captcha.CaptchaServiceSingleton;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: ivan
 */
public class TrialCompanyForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        String locale_lang = (String) getDto("locale");
        if (null != locale_lang) {
            java.util.Locale locale = new java.util.Locale(locale_lang);
            request.getSession().setAttribute("org.apache.struts.action.LOCALE", locale);
        }
        ActionErrors errors = super.validate(mapping, request);
        if (errors.isEmpty()) {
            String captchaId = request.getSession().getId();
            String captchaStr = (String) getDto("registrationCode");
            try {
                boolean isResponseCorrect = CaptchaServiceSingleton.getInstance().
                        validateResponseForID(captchaId, captchaStr);
                if (!isResponseCorrect) {
                    errors.add("registration_code", new ActionError("Company.registrationCode.error",
                            JSPHelper.getMessage(request, "Company.registrationCode")));
                }
            } catch (CaptchaServiceException e) {
                log.error("Cannot execute captcha ", e);
            }
        }
        return errors;
    }
}
