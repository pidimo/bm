package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: SignatureBean.java 8011 2008-02-20 14:40:49Z ivan ${NAME}.java ,v 1.1 02-02-2005 04:15:25 PM miky Exp $
 */

public abstract class SignatureBean implements EntityBean {
    EntityContext entityContext;

    public SignatureBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {

        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setSignatureId(PKGenerator.i.nextKey(WebMailConstants.TABLE_SIGNATURE));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

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

    public abstract Integer getSignatureId();

    public abstract void setSignatureId(Integer signatureId);

    public abstract String getSignatureName();

    public abstract void setSignatureName(String signatureName);

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract Integer getTextSignatureId();

    public abstract void setTextSignatureId(Integer textsignatureid);

    public abstract Integer getHtmlSignatureId();

    public abstract void setHtmlSignatureId(Integer htmlSignatureId);

    public abstract Integer getMailAccountId();

    public abstract void setMailAccountId(Integer mailAccountId);

    public abstract java.util.Collection getMails();

    public abstract void setMails(java.util.Collection mails);
}
