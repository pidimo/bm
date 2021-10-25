package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public interface WebParameterHome extends EJBLocalHome {
    com.piramide.elwis.domain.catalogmanager.WebParameter findByPrimaryKey(Integer key) throws FinderException;

    WebParameter create(ComponentDTO dto) throws CreateException;

    Collection selectWebParameterIdsByWebDocument(Integer webDocumentId) throws FinderException;
}
