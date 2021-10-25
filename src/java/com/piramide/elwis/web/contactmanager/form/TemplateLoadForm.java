package com.piramide.elwis.web.contactmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

/**
 * @author Yumi
 * @version $Id: TemplateLoadForm.java 1066 2004-06-05 14:20:27Z mauren $
 */
public final class TemplateLoadForm extends DefaultForm {

    FormFile file;

    public TemplateLoadForm() {
        super();
    }

    public FormFile getFile() {
        return file;
    }

    public void setFile(FormFile file) {
        this.file = file;
    }

    /* String file;

     public String getFile() {
         return file;
     }

     public void setFile(String file) {
         this.file = file;
     }
 */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        return super.validate(mapping, request);
    }
}
