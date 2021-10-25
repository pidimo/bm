/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:58:10 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface ProjectFreeTextHome extends EJBLocalHome {
    com.piramide.elwis.domain.project.ProjectFreeText findByPrimaryKey(Integer key) throws FinderException;

    ProjectFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;
}
