/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.admin;

import javax.ejb.*;

public abstract class ApplicationMailSignatureBean implements EntityBean {
    EntityContext entityContext;

    public String ejbCreate(String languageIso, byte[] htmlSignature, byte[] textSignature) throws CreateException {
        setLanguageIso(languageIso);
        setHtmlSignature(htmlSignature);
        setTextSignature(textSignature);
        return null;
    }

    public void ejbPostCreate(String languageIso, byte[] htmlSignature, byte[] textSignature) throws CreateException {

    }

    public ApplicationMailSignatureBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract String getLanguageIso();

    public abstract void setLanguageIso(String languageIso);

    public abstract Boolean getEnabled();

    public abstract void setEnabled(Boolean enabled);

    public abstract byte[] getHtmlSignature();

    public abstract void setHtmlSignature(byte[] htmlSignature);

    public abstract byte[] getTextSignature();

    public abstract void setTextSignature(byte[] textSignature);
}
