package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

/**
 * This Class represents the PayCondition Entity Bean
 *
 * @author Ivan
 * @version $Id: PayConditionBean.java 8457 2008-09-02 20:25:23Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class PayConditionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setPayConditionId(PKGenerator.i.nextKey(CatalogConstants.TABLE_PAYCONDITION));
        setVersion(new Integer(1));

        DTOFactory.i.copyFromDTO(dto, this, false);
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


    public abstract Integer getPayConditionId();

    public abstract void setPayConditionId(Integer payConditionId);

    public abstract Integer getPayDays();

    public abstract void setPayDays(Integer payDays);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract Integer getFirstConditionId();

    public abstract void setFirstConditionId(Integer firstCondition);

    public abstract Integer getSecondConditionId();

    public abstract void setSecondConditionId(Integer secondCondition);

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract Integer getPayDaysDiscount();

    public abstract void setPayDaysDiscount(Integer payDaysDiscount);


    /*public String getFirstCondition() throws FinderException {
        ArrayList translations = new ArrayList();

        LangTextHome firstConditionHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        Collection collection = firstConditionHome.findByLangTextId(getFirstConditionId());
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            LangText o = (LangText) iterator.next();
            translations.add(o);
        }
        return ((LangText) translations.get(0)).getText();
    }*/

    /*public void setFirstCondition(String name) throws FinderException {
        ArrayList translations = new ArrayList();
        if (getFirstConditionId() != null) {
            LangTextHome firstConditionHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            Collection collection = firstConditionHome.findByLangTextId(getFirstConditionId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                LangText o = (LangText) iterator.next();
                translations.add(o);
            }
            ((LangText) translations.get(0)).setText(name);
        }
    }*/

    /*public String getSecondCondition() throws FinderException {
        ArrayList translations = new ArrayList();
        if (getSecondConditionId() != null) {
            LangTextHome secondConditionHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            Collection collection = secondConditionHome.findByLangTextId(getSecondConditionId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                LangText o = (LangText) iterator.next();
                translations.add(o);
            }
            return ((LangText) translations.get(0)).getText();
        } else
            return "";
    }*/

    /*public void setSecondCondition(String name) throws FinderException {
        ArrayList translations = new ArrayList();
        if (getSecondConditionId() != null) {
            LangTextHome secondConditionHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            Collection collection = secondConditionHome.findByLangTextId(getSecondConditionId());
            for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                LangText o = (LangText) iterator.next();
                translations.add(o);
            }
            ((LangText) translations.get(0)).setText(name);
        }
    }*/

    /*public LangText getLanguageTranslation() throws FinderException {

        ArrayList translations = new ArrayList();
        LangTextHome home = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        Collection firstConditions = home.findByLangTextId(getFirstConditionId());
        for (Iterator iterator = firstConditions.iterator(); iterator.hasNext();) {
            LangText o = (LangText) iterator.next();
            translations.add(o);
        }
        return ((LangText) translations.get(0));
    }*/

    public void setLanguageTranslation() {

    }

    public abstract String getPayConditionName();

    public abstract void setPayConditionName(String name);

    public abstract java.util.Collection getPayConditionTexts();

    public abstract void setPayConditionTexts(java.util.Collection payConditionTexts);
}
