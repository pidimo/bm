package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.webmail.el.Functions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * Jatun S.R.L.
 * This class contains util methods for several classes
 *
 * @author Alvaro Sejas
 * @version 4.2.2
 */
public class WebMailNavigationUtil {
    private static Log log = LogFactory.getLog(WebMailNavigationUtil.class);

    /**
     * Verify if the request contains parameters for a mailSearch.
     *
     * @param request Request
     * @return True if the request is comming of a single mail search
     */
    public static boolean isSearch(HttpServletRequest request) {
        return null != request.getParameter("mailSearch") && "true".equals(request.getParameter("mailSearch").trim());
    }

    /**
     * Verify if the request contains parameters for a mailAdvancedSearch.
     *
     * @param request Request
     * @return True if the request is comming of an advanced mail search
     */
    public static boolean isAvancedSearch(HttpServletRequest request) {
        return null != request.getParameter("mailAdvancedSearch") && "true".equals(request.getParameter("mailAdvancedSearch").trim());
    }


    public static void setComposedValuesInResultList(HttpServletRequest request,
                                                     org.alfacentauro.fantabulous.controller.ResultList rs,
                                                     String columnToShow) throws Exception {
        log.debug("searching setComposedValuesInResultList....");
        ArrayList listado = new ArrayList(rs.getResult());
        for (int i = 0; i < listado.size(); i++) {
            org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) listado.get(i);

            boolean showToColumn = (WebMailConstants.ColumnToShow.TO.equal(columnToShow)
                    || (WebMailConstants.ColumnToShow.FROM_TO.equal(columnToShow) && String.valueOf(WebMailConstants.OUT_VALUE).equals(fieldHash.get("INOUT"))));

            if (showToColumn) {
                String mailRecipients_i = Functions.getEmailRecipientsAsString(fieldHash.get("MAILID").toString(), WebMailConstants.TO_TYPE_DEFAULT, request);
                fieldHash.put("MAILTOFROM", mailRecipients_i);
            } else {
                //compose 'from' column
                Object mailFrom = fieldHash.get("MAILFROM");
                Object mailPersonalFrom = fieldHash.get("MAILPERSONALFROM");
                if (!GenericValidator.isBlankOrNull((String) mailPersonalFrom) && !mailFrom.equals(mailPersonalFrom)) {
                    fieldHash.put("MAILTOFROM", mailPersonalFrom + " <" + mailFrom + ">");
                } else {
                    fieldHash.put("MAILTOFROM", mailFrom);
                }
            }
        }
    }

    public static void setMailIndex(org.alfacentauro.fantabulous.controller.ResultList rs) throws Exception {
        log.debug("searching mail index");
        int baseIndex = rs.getParameters().getPageParam().getStartIndex();
        ArrayList listado = new ArrayList(rs.getResult());
        for (int i = 0; i < listado.size(); i++) {
            org.alfacentauro.fantabulous.result.FieldHash fieldHash = (org.alfacentauro.fantabulous.result.FieldHash) listado.get(i);
            fieldHash.put("MAILINDEX", baseIndex + i);
        }
    }
}
