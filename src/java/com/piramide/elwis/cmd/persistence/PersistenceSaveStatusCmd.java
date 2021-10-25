package com.piramide.elwis.cmd.persistence;

import com.piramide.elwis.domain.common.session.*;
import com.piramide.elwis.dto.common.session.SessionDTO;
import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.persistence.PersistenceConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;


/**
 * @author Alejandro Ruiz
 * @version 4.3
 */

public class PersistenceSaveStatusCmd extends EJBCommand {
    private static Log log = LogFactory.getLog(PersistenceSaveStatusCmd.class);
    public static String RESULT = "RESULT";
    private static final String DEFAULT_MODULE = "DEFAULT_MODULE";

    public void initialize(String user, String statusName, String module) {
        log.debug("Initialize: User =" + user + " - StatusName =" + statusName + " - Module =" + module);
        paramDTO.put(PersistenceConstants.USER, new Integer(user));
        paramDTO.put(PersistenceConstants.STATUS_NAME, statusName);
        paramDTO.put(PersistenceConstants.MODULE, !"".equals(module) ? module : DEFAULT_MODULE);
    }

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        UserSessionHome userSessionHome = (UserSessionHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_USERSESSION);

        UserSession userSession = findUserSession(userSessionHome);
        if (userSession == null) {
            try {
                log.debug("Create a session state... Values:" + paramDTO);
                userSession = userSessionHome.create(new SessionDTO(paramDTO));
            } catch (CreateException e) {
                return;
            }
        }

        if (PersistenceConstants.SAVE_STATUS.equals(getOp())) {
            log.debug("Delete old userSessionParams.");
            deleteOldParameters(userSession);

            log.debug("Save Status...");
            Map parameters = (Map) paramDTO.remove(PersistenceConstants.PARAMETER_SEARCH);
            List<OrderParam> order = (List<OrderParam>) paramDTO.remove(PersistenceConstants.PARAMETER_ORDER);

            Map config = (Map) paramDTO.remove(PersistenceConstants.PARAMETER);

            UserSessionParam orderSessionParam = null;
            List orderParameters = getOrderParameters(userSession);
            if (!orderParameters.isEmpty()) {
                orderSessionParam = (UserSessionParam) orderParameters.get(0);
            }

            // Update fields only change values that exist in DB or create if not exist
            for (Iterator iterator = parameters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry searchParameter = (Map.Entry) iterator.next();
                try {
                    log.debug("Create param:" + searchParameter);
                    userSession.getParams().add(createParam((String) searchParameter.getKey(), (String) searchParameter.getValue(), PersistenceConstants.TYPE_PARAMS));
                } catch (CreateException e) {
                    log.debug("Cant create SearchParam:" + searchParameter.getKey());
                    e.printStackTrace();
                }
            }
            if (order != null && !order.isEmpty()) {
                StringBuffer orderParamsString = new StringBuffer();
                for (Iterator<OrderParam> orderParamIterator = order.iterator(); orderParamIterator.hasNext();) {
                    OrderParam param = orderParamIterator.next();
                    orderParamsString.append(getOrderValue(param));
                    if (orderParamIterator.hasNext()) {
                        orderParamsString.append("|");
                    }
                }
                if (orderParamsString.length() > 0) {
                    if (orderSessionParam == null) {
                        try {
                            userSession.getParams().add(createParam(PersistenceConstants.PARAM_ORDER, orderParamsString.toString(), PersistenceConstants.TYPE_ORDER));
                        } catch (CreateException e) {
                            log.debug("Can't create OrderParams...." + e.getMessage());
                            e.printStackTrace();
                        }
                    } else {
                        orderSessionParam.setValue(orderParamsString.toString());
                    }
                }
            }
            // Update page number...
            String page = (String) config.get("pageNumber");

            try {
                userSession.getParams().add(createParam(PersistenceConstants.PARAM_PAGE, page, PersistenceConstants.TYPE_PAGE));
            } catch (CreateException e) {
                log.debug("Cant create PageParam:" + page);
            }


        } else if (PersistenceConstants.SAVE_FILTER.equals(getOp())) {
            log.debug("Save Filter...");
            Map filters = (Map) paramDTO.remove(PersistenceConstants.PARAMETER_SEARCH);
            Collection filtersLoad = userSession.getParams();

            Map params = new HashMap();
            for (Iterator iterator = filtersLoad.iterator(); iterator.hasNext();) {
                UserSessionParam userSessionParam = (UserSessionParam) iterator.next();
                if (PersistenceConstants.TYPE_FILTER == userSessionParam.getType().intValue()) {
                    params.put(userSessionParam.getParamName(), userSessionParam);
                }
            }

            // Update fields only change values that exist in DB or create if not exist
            for (Iterator iterator = filters.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                UserSessionParam userSessionParam = (UserSessionParam) params.get(entry.getKey());
                if (userSessionParam != null) {
                    log.debug("Update param ...");
                    if (!userSessionParam.getValue().equals(entry.getValue())) // Update Fields..  :)
                    {
                        userSessionParam.setValue((String) entry.getValue());
                    }
                } else // Create param...
                {
                    try {
                        log.debug("Create param....");
                        userSession.getParams().add(createParam((String) entry.getKey(), (String) entry.getValue(), PersistenceConstants.TYPE_FILTER));
                    } catch (CreateException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (PersistenceConstants.DELETE_STATUS.equals(getOp())) {
            log.debug("Delete old userSessionParams.");
            deletePersistence(userSession);
        }
    }


    private UserSessionParam createParam(String name, String value, int type) throws CreateException {
        SessionDTO dto = new SessionDTO(paramDTO);
        dto.put(PersistenceConstants.PARAM_NAME, name);
        dto.put(PersistenceConstants.PARAM_VALUE, value);
        dto.put(PersistenceConstants.TYPE, new Integer(type));
        return getUserSessionParamHome().create(dto);
    }

    public void deletePersistence(UserSession userSession) {
        deleteOldParameters(userSession);

        List orderParameters = getOrderParameters(userSession);
        for (int i = 0; i < orderParameters.size(); i++) {
            UserSessionParam orderParam = (UserSessionParam) orderParameters.get(i);
            resultDTO.put(PersistenceConstants.PARAMETER_ORDER, orderParam.getValue());
        }
    }

    /**
     * Recover unused <code>UserSessionParam</code> objects, extract the <code>UserSessionParamPK</code> from every
     * <code>UserSessionParam</code> and finally deletes the <code>UserSessionParam</code> object.
     * <p/>
     * The method filter the unused primary keys because sometimes the object to remove could not be found at remove
     * time, this is a concurrency problem.
     *
     * @param userSession <code>UserSession</code> object that contain unused <code>UserSessionParam</code>
     *                    objects to delete.
     */
    private void deleteOldParameters(UserSession userSession) {
        List<UserSessionParamPK> pksToDelete = new ArrayList<UserSessionParamPK>();

        List parametersToDelete = getParametersToDelete(userSession);

        //filter the primary keys
        for (int i = 0; i < parametersToDelete.size(); i++) {
            UserSessionParam param = (UserSessionParam) parametersToDelete.get(i);
            pksToDelete.add((UserSessionParamPK) param.getPrimaryKey());
        }

        for (int i = 0; i < pksToDelete.size(); i++) {
            UserSessionParamHome userSessionParamHome = getUserSessionParamHome();

            UserSessionParamPK pk = pksToDelete.get(i);

            try {
                UserSessionParam param = userSessionParamHome.findByPrimaryKey(pk);
                param.remove();
            } catch (FinderException e) {
                log.debug("Cannot find UsesSessionParam pk=" + pk);
            } catch (RemoveException e) {
                log.error("Cannot remove UserSessionParams.", e);
            }
        }
    }

    private List getParametersToDelete(UserSession userSession) {
        UserSessionParamHome userSessionParamHome = getUserSessionParamHome();
        try {
            return (List) userSessionParamHome.findParamsByNotEqualParamName(userSession.getUserId(),
                    userSession.getStatusName(),
                    userSession.getModule(),
                    "PARAM_ORDER");
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private List getOrderParameters(UserSession userSession) {
        UserSessionParamHome userSessionParamHome = getUserSessionParamHome();
        try {
            return (List) userSessionParamHome.findParamsByEqualParamName(userSession.getUserId(),
                    userSession.getStatusName(),
                    userSession.getModule(), PersistenceConstants.PARAM_ORDER);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private UserSession findUserSession(UserSessionHome userSessionHome) {
        UserSession userSession = null;
        try {
            log.debug("Load status for user");
            userSession = userSessionHome.findByPrimaryKey(new UserSessionPK((Integer) paramDTO.get(PersistenceConstants.USER),
                    paramDTO.getAsString(PersistenceConstants.STATUS_NAME),
                    paramDTO.getAsString(PersistenceConstants.MODULE)));
            log.debug("UserSession:" + userSession.getUserId() + "-" + userSession.getStatusName() + "-" + userSession.getModule());
        } catch (FinderException e) {
            log.debug("No exist session for this user");
        }
        return userSession;
    }

    private UserSessionParamHome getUserSessionParamHome() {
        return (UserSessionParamHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_USERSESSION_PARAM);
    }

    private String getOrderValue(OrderParam order) {
        return order.getColumn() + "-" + order.isAscending() + "-" + order.getPosition();
    }

    public boolean isStateful() {
        return false;
    }
}