package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.Table;
import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.reports.el.Functions;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: ColumnTreeAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ColumnTreeAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ColumnTreeAction................" + request.getParameter("reportId"));
        log.debug("Action Parameters................" + request.getParameterMap());

        ActionForward forward;
        forward = null;

        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
        String jsPath = request.getParameter("jsPath");
        String nodeName = treeView.getTitusNodePath(jsPath);
        String completePath = treeView.getTitusPath(jsPath);

        log.debug("-------------------------------------");
        log.debug("jsPath:" + jsPath);
        log.debug("nodeName:" + nodeName);
        log.debug("completePath:" + completePath);

        String label = Functions.composeColumnLabelByTitusPath(completePath, null, request);
        completePath = Functions.encodeFieldPath(completePath);

        Field field = structureManager.getFieldByTitusPath(nodeName);
        Table table = field.getParentTable();

        StringBuffer value = new StringBuffer();
        value.append(ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF + field.getName() + ReportConstants.KEY_COLUMNSEPARATOR_COLUMNREF);
        value.append(ReportConstants.KEY_COLUMNSEPARATOR_PATH + completePath + ReportConstants.KEY_COLUMNSEPARATOR_PATH);
        value.append(ReportConstants.KEY_COLUMNSEPARATOR_TABLEREF + table.getName() + ReportConstants.KEY_COLUMNSEPARATOR_TABLEREF);
        value.append(ReportConstants.KEY_COLUMNSEPARATOR_TYPE + field.getType().getType() + ReportConstants.KEY_COLUMNSEPARATOR_TYPE);
        if (field.isTotalize()) {
            value.append(ReportConstants.KEY_COLUMNSEPARATOR_ISTOTALIZER + field.isTotalize() + ReportConstants.KEY_COLUMNSEPARATOR_ISTOTALIZER);
        }


        response.setContentType("text/xml");
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<column>\n")
                .append("<label>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(label))
                .append("</label>\n")
                .append("<value>")
                .append(value)
                .append("</value>\n");
        xmlResponse.append("</column>");

        try {
            PrintWriter write = response.getWriter();
            log.debug("xml:\n" + xmlResponse);
            write.write(xmlResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forward;
    }
}
