package com.piramide.elwis.web.persistence;

import com.piramide.elwis.cmd.admin.ReadUserCmd;
import com.piramide.elwis.cmd.persistence.PersistenceLoadStatusCmd;
import com.piramide.elwis.cmd.persistence.PersistenceSaveStatusCmd;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.persistence.Persistence;
import org.alfacentauro.fantabulous.persistence.PersistenceConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Oct 8, 2004
 * Time: 6:12:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersistenceStatus implements Persistence {
    protected static Log log = LogFactory.getLog(PersistenceStatus.class);

    public Map loadStatus(String user, String statusName, String module) {
        PersistenceLoadStatusCmd loadStatusCmd = new PersistenceLoadStatusCmd();
        try {
            loadStatusCmd.setOp(PersistenceConstants.LOAD_STATUS);
            loadStatusCmd.initialize(user, statusName, module);
            ResultDTO resultDTO = BusinessDelegate.i.execute(loadStatusCmd, null);
            if (resultDTO.containsKey(PersistenceLoadStatusCmd.RESULT)) {
                return (Map) resultDTO.get(PersistenceLoadStatusCmd.RESULT);
            }
        } catch (AppLevelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new HashMap();
    }

    public Map loadFilter(String user, String statusName, String module) {
        PersistenceLoadStatusCmd loadStatusCmd = new PersistenceLoadStatusCmd();
        try {
            loadStatusCmd.setOp(PersistenceConstants.LOAD_FILTER);
            loadStatusCmd.initialize(user, statusName, module);
            ResultDTO resultDTO = BusinessDelegate.i.execute(loadStatusCmd, null);
            if (resultDTO.containsKey(PersistenceLoadStatusCmd.RESULT)) {
                return (Map) resultDTO.get(PersistenceLoadStatusCmd.RESULT);
            }
        } catch (AppLevelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new HashMap();
    }

    public void saveStatus(String user, String statusName, String module, Parameters parameters) {

        Integer companyId = readCompanyId(user);

        if (null != companyId) {
            PersistenceSaveStatusCmd saveStatusCmd = new PersistenceSaveStatusCmd();
            saveStatusCmd.setOp(PersistenceConstants.SAVE_STATUS);
            saveStatusCmd.putParam(PersistenceConstants.PARAMETER_SEARCH, parameters);
            saveStatusCmd.putParam(Constants.COMPANYID, companyId);
            saveStatusCmd.initialize(user, statusName, module);
            try {
                BusinessDelegate.i.execute(saveStatusCmd, null);
            } catch (AppLevelException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void saveStatus(String user, String statusName, String module, Map parameters, List<OrderParam> orders, Map config) {
        Integer companyId = readCompanyId(user);
        if (null != companyId) {
            log.debug(" ... saveStatus function ...");
            log.debug("user : " + user + " stausName : " + statusName + "  module : " + module + " parameters : " + parameters + " orders : " + orders + " config : " + config);
            PersistenceSaveStatusCmd saveStatusCmd = new PersistenceSaveStatusCmd();
            saveStatusCmd.setOp(PersistenceConstants.SAVE_STATUS);
            saveStatusCmd.putParam(PersistenceConstants.PARAMETER_SEARCH, parameters);
            saveStatusCmd.putParam(PersistenceConstants.PARAMETER_ORDER, orders);
            saveStatusCmd.putParam(PersistenceConstants.PARAMETER, config);
            saveStatusCmd.putParam(Constants.COMPANYID, companyId);
            saveStatusCmd.initialize(user, statusName, module);
            try {
                BusinessDelegate.i.execute(saveStatusCmd, null);
            } catch (AppLevelException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void saveFilter(String user, String statusName, String module, Map filter) {
        Integer companyId = readCompanyId(user);
        if (null != companyId) {
            PersistenceSaveStatusCmd saveStatusCmd = new PersistenceSaveStatusCmd();
            saveStatusCmd.setOp(PersistenceConstants.SAVE_FILTER);
            saveStatusCmd.putParam(PersistenceConstants.PARAMETER_SEARCH, filter);
            saveStatusCmd.putParam(Constants.COMPANYID, companyId);
            saveStatusCmd.initialize(user, statusName, module);
            try {
                BusinessDelegate.i.execute(saveStatusCmd, null);
            } catch (AppLevelException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public String deleteStatus(String user, String listName, String module) {
        Integer companyId = readCompanyId(user);

        PersistenceSaveStatusCmd saveStatusCmd = new PersistenceSaveStatusCmd();
        saveStatusCmd.setOp(PersistenceConstants.DELETE_STATUS);
        saveStatusCmd.putParam(Constants.COMPANYID, companyId);
        saveStatusCmd.initialize(user, listName, module);
        try {
            BusinessDelegate.i.execute(saveStatusCmd, null);
            return saveStatusCmd.getResultDTO().getAsString(PersistenceConstants.PARAMETER_ORDER);
        } catch (AppLevelException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }


    private Integer readCompanyId(String userId) {
        ReadUserCmd readUserCmd = new ReadUserCmd();
        readUserCmd.putParam("userId", Integer.valueOf(userId));

        try {
            BusinessDelegate.i.execute(readUserCmd, null);
            UserDTO elwisUser = (UserDTO) readUserCmd.getResultDTO().get("elwisUser");
            if (null != elwisUser) {
                return (Integer) elwisUser.get("companyId");
            }

        } catch (AppLevelException e) {
            log.error("-> Cannot Execute ReadUserCmd in " + PersistenceStatus.class.getName());
        }
        return null;
    }
}
