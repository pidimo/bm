package com.piramide.elwis.web.utils;

import com.piramide.elwis.web.common.util.URLParameterProcessor;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.ResultList;
import org.alfacentauro.fantabulous.web.mapping.URLParameterReWriteMapping;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: LinkEncoderUtil.java 18-may-2009 16:48:34
 */
public class LinkEncoderUtil {
    public static String encodeLink(Boolean addModuleParams,
                                    String action,
                                    PageContext pageContext) {
        String res = null;
        StringBuffer buffer = new StringBuffer(RequestUtils.getActionMappingURL(action, pageContext));

        URLParameterProcessor.addModuleParameters(buffer, (HttpServletRequest) pageContext.getRequest(),
                pageContext.getServletContext(), true, addModuleParams);
        ActionMapping mapping = (ActionMapping) pageContext.getRequest().getAttribute(Globals.MAPPING_KEY);

        /**
         * Adding rewriting paramenters to the action params.
         */
        if (mapping instanceof URLParameterReWriteMapping) {
            URLParameterProcessor.addParameterToUrl(buffer, ((URLParameterReWriteMapping) mapping).getParamReWriteList(), (HttpServletRequest) pageContext.getRequest(), false);
            URLParameterProcessor.addParameterToUrl(buffer, ((URLParameterReWriteMapping) mapping).getParamReWriteTextList(), (HttpServletRequest) pageContext.getRequest(), true);
        }
        if (mapping != null) {
            String listName;
            String parameter = mapping.getParameter();
            if (parameter != null) {
                if (parameter.indexOf("@") > 0) {
                    listName = parameter.substring(parameter.indexOf("@") + 1);
                } else {
                    listName = parameter;
                }

                ResultList resultList = (ResultList) pageContext.getRequest().getAttribute(listName);
                if (resultList != null) {
                    List order = resultList.getParameters().getOrderParameters();
                    if (!order.isEmpty()) {
                        OrderParam orderParam = (OrderParam) resultList.getParameters().getOrderParameters().get(0);
                        if (buffer.indexOf("?") == -1) {
                            buffer.append("?");
                        } else {
                            buffer.append("&");
                        }
                        buffer.append("order=").append(orderParam.getColumn()).append("-").append(orderParam.isAscending());
                    }
                }
            }
        }
        res = ((HttpServletResponse) pageContext.getResponse()).encodeURL(new String(buffer));
        return (res);
    }
}
