/**
 * @author Fernando Montaño   16:28:11
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface DepartmentHome extends EJBLocalHome {
    //<< Titus
    Department create(ComponentDTO dto) throws CreateException;

    Collection findByCompanyId(Integer companyId, Integer organizationId) throws FinderException;

    //Titus>>
    Department findByPrimaryKey(Integer key) throws FinderException;

    Collection findByAddress(Integer organizationId) throws FinderException;

    Collection findByContactPerson(Integer organizationId, Integer managerId) throws FinderException;

}
