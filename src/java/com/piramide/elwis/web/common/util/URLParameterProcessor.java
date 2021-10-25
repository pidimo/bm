package com.piramide.elwis.web.common.util;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.plugin.ModuleParamPlugIn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ModuleConfig;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Process URL parameters to add defined parameters to specific url.
 *
 * @author Fernando
 * @version $Id: URLParameterProcessor.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class URLParameterProcessor {

    private static Log log = LogFactory.getLog(URLParameterProcessor.class);


    public static void addModuleParameters(StringBuffer finalUrl, HttpServletRequest request, ServletContext servletContext,
                                           boolean addModuleName, boolean addModuleParams) {

        ModuleConfig config = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        boolean question = finalUrl.indexOf("?") >= 0;
        if (addModuleName && config != null) {
            //add module name
            if (config.getPrefix() != null && !"".equals(config.getPrefix())) {
                if (!question) {
                    finalUrl.append('?');
                    question = true;
                } else {
                    finalUrl.append("&");
                }

                finalUrl.append("module")
                        .append('=')
                        .append(config.getPrefix().substring(1));
            }
        }
        List paramList = null;

        if (config != null) {
            paramList = (LinkedList) servletContext.getAttribute(ModuleParamPlugIn.MODULE_PARAMS_KEY + config.getPrefix());
        }

        if (addModuleParams && paramList != null) {
            if (paramList != null && paramList.size() > 0) {
                for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                    String paramKey = (String) iterator.next();

                    //if value is in the request
                    if (request.getAttribute(paramKey) != null) {
                        if (!question) {
                            finalUrl.append('?');
                            question = true;
                        } else {
                            finalUrl.append("&");
                        }
                        //if is comming from the request it seems readed from db, so it should encoded.
                        try {
                            finalUrl.append(paramKey)
                                    .append('=')
                                    .append(URLEncoder.encode(request.getAttribute(paramKey).toString(),
                                            Constants.CHARSET_ENCODING));
                        } catch (UnsupportedEncodingException e) {
                            log.error("Error encoding param", e);
                        }
                        //if value is in parameters
                    } else if (request.getParameter(paramKey) != null) {
                        if (!question) {
                            finalUrl.append('?');
                            question = true;
                        } else {
                            finalUrl.append("&");
                        }
                        try {
                            finalUrl.append(paramKey)
                                    .append('=')
                                    .append(URLEncoder.encode(request.getParameter(paramKey), Constants.CHARSET_ENCODING));
                        } catch (UnsupportedEncodingException e) {
                            log.error("Error encoding param", e);
                        }
                    }
                }
            }
        }
    }

    public static void addParameterToUrl(StringBuffer finalUrl, List paramList, HttpServletRequest request, boolean isTextList) {
        boolean question = finalUrl.indexOf("?") >= 0;
        if (paramList != null && paramList.size() > 0) {
            for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
                String paramKey = (String) iterator.next();

                //if value is in the request
                if (request.getAttribute(paramKey) != null) {
                    if (!question) {
                        finalUrl.append('?');
                        question = true;
                    } else {
                        finalUrl.append("&");
                    }
                    try {
                        finalUrl.append(paramKey)
                                .append('=')
                                .append(!isTextList ? request.getAttribute(paramKey) :
                                        URLEncoder.encode((String) request.getAttribute(paramKey), Constants.CHARSET_ENCODING));
                    } catch (UnsupportedEncodingException e) {
                        log.error("Error encoding url param", e);
                    }
                    //if value is in parameters
                } else if (request.getParameter(paramKey) != null) {
                    if (!question) {
                        finalUrl.append('?');
                        question = true;
                    } else {
                        finalUrl.append("&");
                    }
                    try {
                        finalUrl.append(paramKey)
                                .append('=')
                                .append(!isTextList ? request.getParameter(paramKey) :
                                        URLEncoder.encode(request.getParameter(paramKey), Constants.CHARSET_ENCODING));
                    } catch (UnsupportedEncodingException e) {
                        log.error("Error encoding url param", e);
                    }
                }

            }
        }
    }
}
