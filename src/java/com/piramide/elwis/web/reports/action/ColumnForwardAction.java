package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ColumnForwardAction.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class ColumnForwardAction extends ReportManagerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing ColumnForwardAction................");

        ActionForward forward = super.execute(mapping, form, request, response);

        if ("Success".equals(forward.getName())) {
            TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
            if (treeView != null) {
                LightlyReportCmd cmd = new LightlyReportCmd();
                cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
                cmd.setOp("read");

                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                String module = (String) resultDTO.get("module");
                String functionality = (String) resultDTO.get("initialTableReference");

                String treeViewFunctionality = treeView.getTreeModel().getName();
                log.debug("FUNCT::" + functionality);
                log.debug("MOD::" + module);
                log.debug("TREEEEEEE FUNCT:" + treeViewFunctionality);
                log.debug("TREEEEEEE MOD:" + treeView.getModule());
                if (!module.equals(treeView.getModule()) || !functionality.equals(treeViewFunctionality)) {
                    TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
                }
            }
        }

        return forward;
    }
}
