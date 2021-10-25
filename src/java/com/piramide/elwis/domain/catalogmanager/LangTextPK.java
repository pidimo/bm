package com.piramide.elwis.domain.catalogmanager;

import java.io.Serializable;

/**
 * LangText entity primary key class
 *
 * @author Fernando Montaño
 * @version $Id: LangTextPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class LangTextPK implements Serializable {
    public Integer langTextId;
    public Integer languageId;

    public LangTextPK() {
    }

    public LangTextPK(Integer langTextId, Integer languageId) {
        this.langTextId = langTextId;
        this.languageId = languageId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LangTextPK)) {
            return false;
        }

        final LangTextPK langTextPK = (LangTextPK) o;

        if (langTextId != null ? !langTextId.equals(langTextPK.langTextId) : langTextPK.langTextId != null) {
            return false;
        }
        if (languageId != null ? !languageId.equals(langTextPK.languageId) : langTextPK.languageId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (langTextId != null ? langTextId.hashCode() : 0);
        result = 29 * result + (languageId != null ? languageId.hashCode() : 0);
        return result;
    }
}
