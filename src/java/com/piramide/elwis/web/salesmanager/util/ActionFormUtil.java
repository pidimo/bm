package com.piramide.elwis.web.salesmanager.util;

import com.piramide.elwis.cmd.salesmanager.ActionTypeUtilCmd;
import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.salesmanager.ActionTypeDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class ActionFormUtil {

    public static final ActionFormUtil i = new ActionFormUtil();

    private ActionFormUtil() {
    }

    @SuppressWarnings(value = "unchecked")
    public Map getDefaultValuesForCreate(HttpServletRequest request) {
        Map result = new HashMap();

        CurrencyDTO basicCurrencyDTO =
                com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);

        if (null != basicCurrencyDTO) {
            result.put("currencyId", basicCurrencyDTO.get("currencyId"));
        }

        CompanyDTO companyDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCompanyConfiguration(request);
        result.put("netGross", companyDTO.get("netGross"));
        result.put("type", companyDTO.get("mediaType"));
        result.put("active", "true");

        return result;
    }

    @SuppressWarnings(value = "unchecked")
    public Map getDefaultValuesFromSessionUser(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Map result = new HashMap();
        result.put("createDateTime", DateUtils.createDate((new Date()).getTime(), dateTimeZone.getID()).getMillis());
        result.put("userName", Functions.getAddressName(user.getValue(Constants.USER_ADDRESSID)));
        return result;
    }


    public ActionTypeDTO getActionType(String actionTypeId, HttpServletRequest request) {
        ActionTypeUtilCmd cmd = new ActionTypeUtilCmd();
        cmd.setOp("getActionType");
        cmd.putParam("actionTypeId", actionTypeId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (ActionTypeDTO) resultDTO.get("getActionType");
        } catch (AppLevelException e) {
            return null;
        }
    }
}
