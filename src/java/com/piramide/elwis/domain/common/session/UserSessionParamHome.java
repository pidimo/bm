package com.piramide.elwis.domain.common.session;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * UserSessionParam Local Home Interface
 *
 * @author Fernando Montaño
 * @version $Id: UserSessionParamHome.java 9821 2009-10-08 22:07:10Z ivan $
 */


public interface UserSessionParamHome extends EJBLocalHome {
    UserSessionParam findByPrimaryKey(UserSessionParamPK key) throws FinderException;

    Collection findParamByType(Integer user, String status, String module, Integer type) throws FinderException;

    Collection findParamByNotEqualType(Integer user, String status, String module, Integer type) throws FinderException;

    UserSessionParam create(ComponentDTO dto) throws CreateException;

    Collection findParamsByNotEqualParamName(Integer userId,
                                             String statusName,
                                             String module,
                                             String paramName) throws FinderException;

    Collection findParamsByEqualParamName(Integer userId,
                                          String statusName,
                                          String module,
                                          String paramName) throws FinderException;
}
