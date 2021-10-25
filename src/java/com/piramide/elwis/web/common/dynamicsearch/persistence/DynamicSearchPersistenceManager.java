package com.piramide.elwis.web.common.dynamicsearch.persistence;

import com.piramide.elwis.cmd.admin.DynamicSearchCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class DynamicSearchPersistenceManager {
    private static Log log = LogFactory.getLog(DynamicSearchPersistenceManager.class);

    public static void save(List fieldAliasList, String dynamicSearchName, String module, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        DynamicSearchCmd dynamicSearchCmd = new DynamicSearchCmd();
        dynamicSearchCmd.setOp("saveSearch");
        dynamicSearchCmd.putParam("name", dynamicSearchName);
        dynamicSearchCmd.putParam("module", module);
        dynamicSearchCmd.putParam("userId", userId);
        dynamicSearchCmd.putParam("companyId", companyId);
        dynamicSearchCmd.putParam("searchFields", fieldAliasList);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(dynamicSearchCmd, request);
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
    }

    public static List load(String dynamicSearchName, String module, HttpServletRequest request) {
        List fieldAliasList = new ArrayList();

        User user = RequestUtils.getUser(request);
        Integer companyId = Integer.valueOf(user.getValue(Constants.COMPANYID).toString());
        Integer userId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        DynamicSearchCmd dynamicSearchCmd = new DynamicSearchCmd();
        dynamicSearchCmd.setOp("readFields");
        dynamicSearchCmd.putParam("name", dynamicSearchName);
        dynamicSearchCmd.putParam("module", module);
        dynamicSearchCmd.putParam("userId", userId);
        dynamicSearchCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(dynamicSearchCmd, request);
            if (!resultDTO.isFailure()) {
                fieldAliasList = (List) resultDTO.get("searchFields");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
        return fieldAliasList;
    }

}
