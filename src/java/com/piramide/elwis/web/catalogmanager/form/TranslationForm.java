package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.web.utils.TranslationFormUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class TranslationForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        List<String> uiLanguages = TranslationFormUtil.getUILanguages(request);
        setDto("uiLanguages", uiLanguages);

        ActionErrors errors = super.validate(mapping, request);

        TranslationFormUtil translationFormUtil = new TranslationFormUtil(this.getDtoMap());
        translationFormUtil.validate(request, errors);

        return errors;
    }
}
