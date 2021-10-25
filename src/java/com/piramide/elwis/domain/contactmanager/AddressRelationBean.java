package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public abstract class AddressRelationBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setRelationId(PKGenerator.i.nextKey(ContactConstants.TABLE_ADDRESSRELATION));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }


    public AddressRelationBean() {
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getRelatedAddressId();

    public abstract void setRelatedAddressId(Integer relatedAddressId);

    public abstract Integer getCommentId();

    public abstract void setCommentId(Integer commentId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getRelationId();

    public abstract void setRelationId(Integer relationId);

    public abstract Integer getRelationTypeId();

    public abstract void setRelationTypeId(Integer relationTypeId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract ContactFreeText getContactFreeText();

    public abstract void setContactFreeText(ContactFreeText contactFreeText);

    public void setContactFreeText(EJBLocalObject contactFreeText) {
        setContactFreeText((ContactFreeText) contactFreeText);
    }

}
