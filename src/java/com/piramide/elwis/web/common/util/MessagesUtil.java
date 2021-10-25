package com.piramide.elwis.web.common.util;

import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.util.MessageResources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 8, 2004
 * Time: 7:29:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessagesUtil {

    public static final MessagesUtil i = new MessagesUtil();

    public ActionErrors convertToActionErrors(
            ActionMapping map,
            HttpServletRequest req,
            ResultDTO resultDTO) {

        final ActionErrors actionErrors = new ActionErrors();
        for (Iterator it = resultDTO.getResultMessages(); it.hasNext();) {
            final ResultMessage rm = (ResultMessage) it.next();
            final Object[] localizedParams =
                    convertToLocalizedParams(
                            rm.getParams(),
                            getMessageResources(map),
                            req.getLocale());
            final ActionError error =
                    new ActionError(rm.getKey(), localizedParams);
            actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
        }
        return actionErrors;
    }

    public List<ActionError> processResultMessages(ActionMapping mapping,
                                                   HttpServletRequest request,
                                                   ResultDTO resultDTO) {
        List<ActionError> result = new ArrayList<ActionError>();

        for (Iterator it = resultDTO.getResultMessages(); it.hasNext();) {
            ResultMessage resultMessage = (ResultMessage) it.next();
            Object[] localizedParameters = convertToLocalizedParams(
                    resultMessage.getParams(),
                    getMessageResources(mapping),
                    request.getLocale());
            result.add(new ActionError(resultMessage.getKey(), localizedParameters));
        }

        return result;
    }

    private MessageResources getMessageResources(ActionMapping map) {

        final MessageResourcesConfig mrc =
                map.getModuleConfig().findMessageResourcesConfig(Globals.MESSAGES_KEY);
        final MessageResources msgRes =
                MessageResources.getMessageResources(mrc.getParameter());
        msgRes.setReturnNull(true);
        return msgRes;
    }

    private Object[] convertToLocalizedParams(
            Object[] params,
            MessageResources msgRes,
            Locale locale) {

        final Object[] localizedParams = new Object[params.length];
        for (int i = 0; i < localizedParams.length; i++) {
            localizedParams[i] = msgRes.getMessage(locale, (String) params[i]);
            final boolean isNotFound = (localizedParams[i] == null);
            if (isNotFound) {
                localizedParams[i] = params[i];
            }
        }
        return localizedParams;
    }

    /**
     * save in session messages in key Constants.POPUP_MESSAGE_KEY
     *
     * @param session
     * @param errors
     */
    public void savePopupMessages(HttpSession session, ActionErrors errors) {
        if (errors == null || errors.isEmpty()) {
            session.removeAttribute(Constants.POPUP_MESSAGE_KEY);
        } else {
            session.setAttribute(Constants.POPUP_MESSAGE_KEY, errors);
        }
    }

    /**
     * save popup messages as struts ERRORS in request
     *
     * @param request
     */
    public void setAsErrorsPopupMessages(HttpServletRequest request) {
        Object errorsObject = request.getSession().getAttribute(Constants.POPUP_MESSAGE_KEY);
        if (errorsObject != null) {
            if (errorsObject instanceof ActionErrors) {
                request.setAttribute(Globals.ERROR_KEY, errorsObject);
            }
            request.getSession().removeAttribute(Constants.POPUP_MESSAGE_KEY);
        }
    }

    /**
     * save popup messages as struts ERRORS in session
     *
     * @param session
     */
    public void setAsErrorsPopupMessages(HttpSession session) {
        Object errorsObject = session.getAttribute(Constants.POPUP_MESSAGE_KEY);
        if (errorsObject != null) {
            if (errorsObject instanceof ActionErrors) {
                session.setAttribute(Globals.ERROR_KEY, errorsObject);
            }
            session.removeAttribute(Constants.POPUP_MESSAGE_KEY);
        }
    }

    /**
     * Get the decimal number format example e.g. 9,999.99
     *
     * @param locale locale
     * @param maxFloatDigits digits after decimal separator
     * @return String
     */
    public String getDecimalNumberFormat(Locale locale, int maxFloatDigits) {
        StringBuffer decimalFormat = new StringBuffer();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);

        decimalFormat.append("9")
                .append(decimalFormatSymbols.getGroupingSeparator())
                .append("999")
                .append(decimalFormatSymbols.getDecimalSeparator());
        for (int i = 0; i < maxFloatDigits; i++) {
            decimalFormat.append("9");
        }
        return decimalFormat.toString();
    }

    public String getDecimalNumberFormat(HttpServletRequest request, int maxFloatDigits) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        return getDecimalNumberFormat(locale, maxFloatDigits);
    }
}
