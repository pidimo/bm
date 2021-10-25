package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * This Class represents the Local interface of the Template Entity Bean
 *
 * @author Ivan
 * @version $Id: Template.java 9553 2009-08-14 21:46:04Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */
public interface Template extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getDescription();

    void setDescription(String description);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getVersion();

    void setVersion(Integer version);

    Collection getTemplateText();

    void setTemplateText(Collection templateText);

    Integer getMediaType();

    void setMediaType(Integer mediaType);
}
