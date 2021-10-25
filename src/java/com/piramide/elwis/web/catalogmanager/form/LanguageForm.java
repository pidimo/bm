package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.ReadLangTextCmd;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: LanguageForm.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 18-03-2005 10:49:55 AM ivan Exp $
 */
public class LanguageForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);

        String op = (String) getDto("op");
        if ("update".equals(op)) {

            ReadLangTextCmd cmd = new ReadLangTextCmd();
            cmd.putParam("type", "systemTranslation");
            cmd.putParam("languageId", getDto("languageId"));


            ResultDTO dto = new ResultDTO();
            try {
                dto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {

            }

            Collection translations = (Collection) dto.get("translations");
            String actualIso = (String) dto.get("actualIso");

            String iso = (String) getDto("languageIso");


            if (actualIso != null && !actualIso.equals(iso)) {
                if (!translations.isEmpty()) {
                    //add error cannot change iso value because this language contains IU translations
                    String resourceKey = JSPHelper.getSystemLanguage(actualIso, request);
                    errors.add("languageAsociated", new ActionError("error.Language.asociated", resourceKey));
                }
            }
        }

        return errors;
    }
}