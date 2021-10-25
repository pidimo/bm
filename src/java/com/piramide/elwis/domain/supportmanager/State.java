package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:46:04 PM
 * To change this template use File | Settings | File Templates.
 */

public interface State extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getStageType();

    void setStageType(Integer stageType);

    Integer getSequence();

    void setSequence(Integer sequence);

    Integer getStateId();

    void setStateId(Integer stateId);

    String getStateName();

    void setStateName(String name);

    Integer getStateType();

    void setStateType(Integer type);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);
}
