/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

public interface ApplicationMailSignature extends EJBLocalObject {
    String getLanguageIso();

    void setLanguageIso(String languageIso);

    Boolean getEnabled();

    void setEnabled(Boolean enabled);

    byte[] getHtmlSignature();

    void setHtmlSignature(byte[] htmlSignature);

    byte[] getTextSignature();

    void setTextSignature(byte[] textSignature);
}
