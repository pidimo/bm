package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportQueryParamUpdateForwardAction extends ReportJrxmlManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing ReportQueryParamUpdateForwardAction................");

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

        return super.execute(mapping, form, request, response);
    }
}
