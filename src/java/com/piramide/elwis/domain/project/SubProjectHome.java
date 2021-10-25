/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:19:07 $
 */
package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface SubProjectHome extends EJBLocalHome {
    com.piramide.elwis.domain.project.SubProject findByPrimaryKey(Integer key) throws FinderException;

    SubProject create(ComponentDTO dto) throws CreateException;

    Collection findByProjectId(Integer projectId, Integer companyId) throws FinderException;
}
