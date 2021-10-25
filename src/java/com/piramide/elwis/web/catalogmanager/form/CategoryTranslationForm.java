package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.catalogmanager.CategoryTranslationCmd;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author: ivan
 * Date: Feb 13, 2006
 * Time: 10:06:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryTranslationForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();


        String isDefault = null;
        String languageId = null;
        isDefault = (String) getDto("isDefault");
        if (null != isDefault) {
            int separatorIndex = isDefault.indexOf('_');

            languageId = isDefault.substring(0, separatorIndex);
            setDto("isDefault", languageId);

            String counterOfSelectedAsDefault = isDefault.substring(separatorIndex + 1);

            String defaultText = (String) getDto("text" + counterOfSelectedAsDefault);

            if (null == defaultText || "".equals(defaultText.trim())) {
                errors.add("isDefautlNotFound", new ActionError("Translation.error.defaultTranslation"));
            }


        } else {
            errors.add("isDefautlNotFound", new ActionError("errors.required", JSPHelper.getMessage(request, "Category.defaultTranslation")));
        }


        if (!errors.isEmpty()) {
            Integer categoryId = new Integer((String) getDto("categoryId"));
            String categoryName = (String) getDto("categoryName");

            CategoryTranslationCmd cmd = new CategoryTranslationCmd();
            cmd.setOp("read");
            cmd.putParam("categoryId", categoryId);
            cmd.putParam("categoryName", categoryName);

            try {
                ResultDTO myResutlDTO = new ResultDTO();


                myResutlDTO = BusinessDelegate.i.execute(cmd, request);
                this.getDtoMap().putAll(myResutlDTO);

                List translatedSystemLanguages = (List) myResutlDTO.get("translatedSystemLanguages");
                if (null != translatedSystemLanguages) {
                    for (int i = 0; i < translatedSystemLanguages.size(); i++) {
                        this.setDto("text" + (i + 1), getDto("text" + (i + 1)));

                        LangTextDTO dto = (LangTextDTO) translatedSystemLanguages.get(i);
                        if (null != languageId
                                && null != dto.get("languageId")
                                && languageId.equals(dto.get("languageId").toString()))

                        {
                            dto.put("isDefault", Boolean.valueOf(true));
                        }

                    }
                }
            } catch (AppLevelException e) {
                log.error("Cannot execute CategoryTranslationCmd... ", e);
            }
        }

        return errors;
    }
}
