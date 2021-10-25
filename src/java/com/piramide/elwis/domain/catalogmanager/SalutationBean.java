package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

/**
 * This Class represents the Salutation Entity Bean
 *
 * @author yumi
 * @version $Id: SalutationBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class SalutationBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setSalutationId(PKGenerator.i.nextKey(CatalogConstants.TABLE_SALUTATION));
        setVersion(new Integer(1));

        DTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {


/*        LangTextDTO letterPartDTO = new LangTextDTO();
        letterPartDTO.put("companyId", dto.get("companyId"));
        letterPartDTO.put("languageId", dto.get("languageId"));
        letterPartDTO.put("text", dto.get("letterText"));


        LangTextDTO addressPartDTO = new LangTextDTO();
        addressPartDTO.put("companyId", dto.get("companyId"));
        addressPartDTO.put("languageId", dto.get("languageId"));
        addressPartDTO.put("text", dto.get("addressText"));

        LangText letterPart = (LangText) EJBFactory.i.createEJB(letterPartDTO);
        LangText addressPart = (LangText) EJBFactory.i.createEJB(addressPartDTO);

        setLetterTextId(letterPart.getLangTextId());
        setAddressTextId(addressPart.getLangTextId());*/
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getSalutationId();

    public abstract void setSalutationId(Integer salutationId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract Integer getAddressTextId();

    public abstract void setAddressTextId(Integer addressTextId);

    public abstract Integer getLetterTextId();

    public abstract void setLetterTextId(Integer letterTextId);

    public abstract String getSalutationLabel();

    public abstract void setSalutationLabel(String salutationLabel);
}
