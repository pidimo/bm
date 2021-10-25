/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: TemplateTextHome.java 9761 2009-09-29 19:43:01Z miguel ${NAME}.java, v 2.0 09-jun-2004 11:19:11 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface TemplateTextHome extends EJBLocalHome {
    public TemplateText create(ComponentDTO dto) throws CreateException;

    TemplateText findByPrimaryKey(TemplateTextPK key) throws FinderException;

    TemplateText findDefaultTemplate(Integer templateId) throws FinderException;

    TemplateText findByDocumentFreeTextId(Integer freeTextId) throws FinderException;
}
