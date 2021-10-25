package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.ReaderCampaignTemplateCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to load templates for type with ajax management
 *
 * @author Miky
 * @version $Id: LoadTemplateByTypeAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LoadTemplateByTypeAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing LoadTemplateByTypeAction................" + request.getParameterMap());

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        String campaignId = request.getParameter("campaignId");
        String documentType = request.getParameter("documentType");

        List resultList = new ArrayList();
        if (campaignId != null && documentType != null) {

            ReaderCampaignTemplateCmd cmd = new ReaderCampaignTemplateCmd();
            cmd.putParam("campaignId", campaignId);
            cmd.putParam("documentType", documentType);

            try {
                BusinessDelegate.i.execute(cmd, request);
                resultList = (List) cmd.getResultDTO().get("listTemplate");
            } catch (AppLevelException e) {
            }
        }

        //compose select tag
        StringBuffer out = new StringBuffer();
        out.append("<SELECT NAME=\"templateSelect\"")
                .append(" id=\"templSelect\"")
                .append(" class=\"mediumSelect\"")
                .append(" onchange=\"javascript:buttonEnable(this,'executeButton')\"")
                .append(" onkeyup=\"javascript:buttonEnable(this,'executeButton')\"")
                        //.append("tabindex=\"\"")
                .append(">");

        out.append("\t<OPTION value=\"\">")
                .append("&nbsp;")
                .append("</OPTION>\n");

        for (Iterator iterator = resultList.iterator(); iterator.hasNext();) {
            Map map = (Map) iterator.next();
            out.append(renderOption(map));
        }
        out.append("</SELECT>");

        //compose xml doc
        response.setContentType("text/html");
        response.setCharacterEncoding(Constants.CHARSET_ENCODING);
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append(out);

        try {
            PrintWriter write = response.getWriter();
            log.debug("xml:\n" + xmlResponse);
            write.write(xmlResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forward;
    }

    /**
     * Render OPTION html tag
     *
     * @param item map of items
     * @return the string of option rendered
     */
    private String renderOption(Map item) {
        StringBuffer output = new StringBuffer();
        output.append("\t<OPTION value=\"")
                .append(item.get("templateId"))
                .append("\"");
        output.append(">")
                .append(item.get("description"))
                .append("</OPTION>\n");
        return new String(output);
    }

}
