package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the PayCondition Entity Bean
 *
 * @author Ivan
 * @version $Id: PayCondition.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface PayCondition extends EJBLocalObject {
    Integer getPayConditionId();

    void setPayConditionId(Integer payConditionId);

    Integer getPayDays();

    void setPayDays(Integer payDays);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer versionId);

    Integer getFirstConditionId();

    void setFirstConditionId(Integer firstCondition);

    Integer getSecondConditionId();

    void setSecondConditionId(Integer secondCondition);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    Integer getPayDaysDiscount();

    void setPayDaysDiscount(Integer payDaysDiscount);

    /* public String getFirstCondition() throws FinderException;

  public void setFirstCondition(String name) throws FinderException;

  public String getSecondCondition() throws FinderException;

  public void setSecondCondition(String name) throws FinderException;

  public LangText getLanguageTranslation() throws FinderException;

  public void setLanguageTranslation();*/

    String getPayConditionName();

    void setPayConditionName(String name);

    java.util.Collection getPayConditionTexts();

    void setPayConditionTexts(java.util.Collection payConditionTexts);
}
