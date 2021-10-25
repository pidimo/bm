package com.piramide.elwis.cmd.persistence;

import com.piramide.elwis.domain.common.session.UserSession;
import com.piramide.elwis.domain.common.session.UserSessionHome;
import com.piramide.elwis.domain.common.session.UserSessionPK;
import com.piramide.elwis.domain.common.session.UserSessionParam;
import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.persistence.PersistenceConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Oct 9, 2004
 * Time: 11:19:39 AM
 */
public class PersistenceLoadStatusCmd extends EJBCommand {
    private static Log log = LogFactory.getLog(PersistenceLoadStatusCmd.class);
    public static String RESULT = "RESULT";
    public static final String DEFAULT_MODULE = "DEFAULT_MODULE";

    public void initialize(String user, String statusName, String module) {
        //log.debug("Initialize: User=" + user + " - StatusName=" + statusName + " - Module=" + module);
        paramDTO.put(PersistenceConstants.USER, new Integer(user));
        paramDTO.put(PersistenceConstants.STATUS_NAME, statusName);
        paramDTO.put(PersistenceConstants.MODULE, !"".equals(module) ? module : DEFAULT_MODULE);
    }

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        UserSessionHome userSessionHome = (UserSessionHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_USERSESSION);


        UserSession userSession = null;
        try {
            //log.debug("Load status for user");
            userSession = userSessionHome.findByPrimaryKey(new UserSessionPK((Integer) paramDTO.get(PersistenceConstants.USER),
                    paramDTO.getAsString(PersistenceConstants.STATUS_NAME),
                    paramDTO.getAsString(PersistenceConstants.MODULE)));

            //log.debug("UserSession:" + userSession.getUserId() + "-" + userSession.getStatusName() + "-" + userSession.getModule());
        } catch (FinderException e) {
            log.debug("No exist session for this user");
        }

        if (userSession != null) {
            if (PersistenceConstants.LOAD_FILTER.equals(getOp())) {
                //log.debug("load filter...");
                HashMap map = new HashMap();
                for (Iterator iterator = userSession.getParams().iterator(); iterator.hasNext();) {
                    UserSessionParam param = (UserSessionParam) iterator.next();
                    if (param.getType().intValue() == PersistenceConstants.TYPE_FILTER) {
                        map.put(param.getParamName(), param.getValue());
                    }
                }
                resultDTO.put(RESULT, map);
            } else if (PersistenceConstants.LOAD_STATUS.equals(getOp())) {
                //log.debug("Load Status... with Params:" + paramDTO);
                Map parameters = new HashMap(userSession.getParams().size());

                for (Iterator iterator = userSession.getParams().iterator(); iterator.hasNext();) {
                    UserSessionParam param = (UserSessionParam) iterator.next();
                    if (param.getType().intValue() != PersistenceConstants.TYPE_FILTER) {
                        //log.debug("Status:" + param.getParamName() + " - " + param.getValue());
                        parameters.put(param.getParamName(), param.getValue());
                    }
                }
                resultDTO.put(RESULT, parameters);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }


}
