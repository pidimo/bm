package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: TemplateTextPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TemplateTextPK implements Serializable {
    public Integer languageId;
    public Integer templateId;

    public TemplateTextPK() {
    }

    public TemplateTextPK(Integer languageId, Integer templateId) {
        this.languageId = languageId;
        this.templateId = templateId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TemplateTextPK)) {
            return false;
        }

        final TemplateTextPK templateTextPK = (TemplateTextPK) o;

        if (languageId != null ? !languageId.equals(templateTextPK.languageId) : templateTextPK.languageId != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(templateTextPK.templateId) : templateTextPK.templateId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (languageId != null ? languageId.hashCode() : 0);
        result = 29 * result + (templateId != null ? templateId.hashCode() : 0);
        return result;
    }

}
