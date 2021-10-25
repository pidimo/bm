/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-06-19 06:20:40 PM $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface TemplateTextImgHome extends EJBLocalHome {

    TemplateTextImg create(ComponentDTO dto) throws CreateException;

    TemplateTextImg findByPrimaryKey(TemplateTextImgPK key) throws FinderException;

    Collection findByTemplateIdLanguageId(Integer templateId, Integer languageId) throws FinderException;


}
