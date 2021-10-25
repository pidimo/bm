/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:12:44 $
 */
package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProjectHome extends EJBLocalHome {
    com.piramide.elwis.domain.project.Project findByPrimaryKey(Integer key) throws FinderException;

    Project create(ComponentDTO dto) throws CreateException;

    Collection findByResposibleId(Integer responsibleId, Integer companyId) throws FinderException;

    Collection findByCustomer(Integer customerId) throws FinderException;

    Collection findByContactPerson(Integer customerId, Integer contactPersonId) throws FinderException;
}
