package com.piramide.elwis.web.common.el;

import com.piramide.elwis.cmd.catalogmanager.CategoryFieldValueUtilCmd;
import com.piramide.elwis.utils.CategoryConstants;
import com.piramide.elwis.utils.Constants;
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
 * EL functions for category
 * @author Miguel A. Rojas Cardenas
 * @version 6.1.1
 */
public class CategoryFunctions {
    private static Log log = LogFactory.getLog(CategoryFunctions.class);

    public static String readCustomerBusinessAreaCategoryFieldValue(Object customerId, HttpServletRequest request) {
        String value = "";

        if (customerId != null && !GenericValidator.isBlankOrNull(customerId.toString())) {

            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

            CategoryFieldValueUtilCmd utilCmd = new CategoryFieldValueUtilCmd();
            utilCmd.setOp("readByCategoryFieldIdentifier");
            utilCmd.putParam("customerId", customerId);
            utilCmd.putParam("companyId", companyId);
            utilCmd.putParam("fieldIdentifier", CategoryConstants.CategoryFieldIdentifier.CUSTOMER_BUSINESS_AREA.getConstant());

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(utilCmd, request);
                if (resultDTO.get("valueCategoryField") != null) {
                    value = resultDTO.get("valueCategoryField").toString();
                }
            } catch (AppLevelException e) {
                log.error("-> Execute " + CategoryFieldValueUtilCmd.class.getName() + " FAIL", e);
            }
        }

        return value;
    }
}
