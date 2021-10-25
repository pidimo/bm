package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public interface WebDocumentHome extends EJBLocalHome {
    com.piramide.elwis.domain.catalogmanager.WebDocument findByPrimaryKey(Integer key) throws FinderException;

    WebDocument create(ComponentDTO dto) throws CreateException;
}
