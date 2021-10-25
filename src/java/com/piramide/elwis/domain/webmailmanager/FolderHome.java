/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: FolderHome.java 9438 2009-07-09 16:49:34Z ivan ${NAME}.java, v 2.0 02-feb-2005 15:17:35 Ivan Exp $
 */
package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface FolderHome extends EJBLocalHome {
    public Folder findByPrimaryKey(Integer key) throws FinderException;

    public Folder create(ComponentDTO dto) throws CreateException;

    public Folder findByFolderType(Integer userMailId, Integer type, Integer companyId) throws FinderException;

    public Collection findDefaultFolders(Integer userMailId, Integer companyId) throws FinderException;

    public Collection findByUserMailKey(Integer userMailId, Integer companyId) throws FinderException;

    public Collection findByUserMailKeySort(Integer userMailId, Integer companyId) throws FinderException;

    public Collection selectGetFolderIdentifiers(Integer userMailId, Integer companyId) throws FinderException;
}
