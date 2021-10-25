package com.piramide.elwis.web.reports.el;

import com.piramide.elwis.cmd.admin.UserCmd;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;

/**
 * Util to manage report export settings
 * @author Miguel A. Rojas Cardenas
 * @version 2.9
 */
public class ReportSettingUtil {
    private static Log log = LogFactory.getLog(ReportSettingUtil.class);

    public static void saveUserReportSettingConfig(String csvDelimiter, String reportCharset, HttpServletRequest request) {

        if (!GenericValidator.isBlankOrNull(csvDelimiter) || !GenericValidator.isBlankOrNull(reportCharset)) {
            User user = RequestUtils.getUser(request);

            UserCmd userCmd = new UserCmd();
            userCmd.setOp("update");
            userCmd.putParam("userId", user.getValue("userId"));
            userCmd.putParam("csvDelimiter", csvDelimiter);
            userCmd.putParam("reportCharset", reportCharset);

            try {
                BusinessDelegate.i.execute(userCmd, request);
            } catch (AppLevelException e) {
                log.debug("Error in execute userCmd..", e);
            }
        }
    }

    public static String readUserCsvDelimiterConfig(HttpServletRequest request) {
        String csvDelimiter = null;
        User user = RequestUtils.getUser(request);

        UserCmd userCmd = new UserCmd();
        userCmd.setOp("read");
        userCmd.putParam("userId", user.getValue("userId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userCmd, request);
            if (!resultDTO.isFailure()) {
                csvDelimiter = (String) resultDTO.get("csvDelimiter");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute userCmd..", e);
        }

        if (GenericValidator.isBlankOrNull(csvDelimiter)) {
            csvDelimiter = ReportConstants.CSVFieldDelimiter.COMMA.getDelimiter();
        }

        return csvDelimiter;
    }

    public static String readUserReportCharsetConfig(HttpServletRequest request) {
        String charset = null;
        User user = RequestUtils.getUser(request);

        UserCmd userCmd = new UserCmd();
        userCmd.setOp("read");
        userCmd.putParam("userId", user.getValue("userId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userCmd, request);
            if (!resultDTO.isFailure()) {
                charset = (String) resultDTO.get("reportCharset");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute userCmd..", e);
        }

        if (GenericValidator.isBlankOrNull(charset)) {
            charset = ReportConstants.ReportCharset.UTF_8.getConstant();
        }

        return charset;
    }
}
