package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * @author Miky
 * @version $Id: TemplateTextImgPK.java 2009-06-19 06:23:53 PM $
 */
public class TemplateTextImgPK implements Serializable {
    public Integer imageStoreId;
    public Integer templateId;
    public Integer languageId;

    public TemplateTextImgPK() {
    }

    public TemplateTextImgPK(Integer imageStoreId, Integer templateId, Integer languageId) {
        this.imageStoreId = imageStoreId;
        this.templateId = templateId;
        this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TemplateTextImgPK that = (TemplateTextImgPK) o;

        if (imageStoreId != null ? !imageStoreId.equals(that.imageStoreId) : that.imageStoreId != null) {
            return false;
        }
        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageStoreId != null ? imageStoreId.hashCode() : 0;
        result = 31 * result + (templateId != null ? templateId.hashCode() : 0);
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
        return result;
    }
}
