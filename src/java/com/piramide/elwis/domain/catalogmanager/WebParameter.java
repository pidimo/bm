package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public interface WebParameter extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getParameterName();

    void setParameterName(String parameterName);

    String getVariableName();

    void setVariableName(String variableName);

    Integer getVariableType();

    void setVariableType(Integer variableType);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getWebDocumentId();

    void setWebDocumentId(Integer webDocumentId);

    Integer getWebParameterId();

    void setWebParameterId(Integer webParameterId);

    WebDocument getWebDocument();

    void setWebDocument(WebDocument webDocument);
}
