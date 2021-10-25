package com.piramide.elwis.web.dashboard.action;

import com.piramide.elwis.cmd.dashboard.DashboardConfigurationCmd;
import com.piramide.elwis.cmd.dashboard.FindDashboardCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.form.ContainerConfigurationForm;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 10:51:15 AM
 */
public class DashboardContainerAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }


        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        int userId = new Integer(user.getValue("userId").toString());
        int companyId = new Integer(user.getValue("companyId").toString());

        FindDashboardCmd fCmd = new FindDashboardCmd();
        fCmd.putParam("userId", userId);
        fCmd.putParam("companyId", companyId);
        ResultDTO myRDTO = BusinessDelegate.i.execute(fCmd, request);

        Map m = (Map) myRDTO.get("dashboardContainer");
        int dashboardContainerId = (Integer) m.get("dashboardContainerId");


        boolean isRead = (mapping.getPath().indexOf("Read") > 0);
        boolean isDrawComponent = (mapping.getPath().indexOf("DrawContainer") > 0);
        boolean isSaveConfiguration = ((mapping.getPath().indexOf("Save")) > 0);
        boolean isComponentClose = (mapping.getPath().indexOf("ComponentClose") > 0);


        if (isRead) {

            DashboardConfigurationCmd cmd = new DashboardConfigurationCmd();
            cmd.putParam("dashboardContainerId", dashboardContainerId);
            cmd.setOp("read");
            ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
            processComponentToConfigurationView(request, myResultDTO);
        }

        if (isDrawComponent) {

            DashboardConfigurationCmd cmd = new DashboardConfigurationCmd();
            cmd.putParam("dashboardContainerId", dashboardContainerId);
            cmd.setOp("read");
            ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
            processComponentsToDraw(request, myResultDTO);
        }

        if (isSaveConfiguration) {

            ContainerConfigurationForm myForm = (ContainerConfigurationForm) form;

            List<String> left = new ArrayList<String>();
            if (null != myForm.getLeft()) {
                left = Arrays.asList(myForm.getLeft());
            }

            List<String> right = new ArrayList<String>();
            if (null != myForm.getRight()) {
                right = Arrays.asList(myForm.getRight());
            }

            DashboardConfigurationCmd cmd = new DashboardConfigurationCmd();
            cmd.putParam("dashboardContainerId", dashboardContainerId);
            cmd.putParam("left", left);
            cmd.putParam("right", right);
            cmd.setOp("save");
            BusinessDelegate.i.execute(cmd, request);
        }

        if (isComponentClose) {
            int dbComponentId = new Integer(request.getParameter("dbComponentId"));
            int xmlComponent = new Integer(request.getParameter("componentId"));

            DashboardConfigurationCmd cmd = new DashboardConfigurationCmd();
            cmd.putParam("dbComponentId", dbComponentId);
            cmd.putParam("xmlComponenetId", xmlComponent);
            cmd.putParam("dashboardContainerId", dashboardContainerId);
            cmd.setOp("deleteComponent");
            BusinessDelegate.i.execute(cmd, request);

            DashboardConfigurationCmd cmd1 = new DashboardConfigurationCmd();
            cmd1.putParam("dashboardContainerId", dashboardContainerId);
            cmd1.setOp("read");
            ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd1, request);
            processComponentsToDraw(request, myResultDTO);
        }

        request.setAttribute("dashboardContainerId", dashboardContainerId);
        return mapping.findForward("Success");

    }

    private void processComponentsToDraw(HttpServletRequest request, ResultDTO resultDTO) {

        List<Map> structure = (List<Map>) resultDTO.get("structure");

        List<Map> left = new ArrayList<Map>();
        List<Map> right = new ArrayList<Map>();
        if (null != structure) {
            for (Map m : structure) {
                Integer columnX = (Integer) m.get("columnY");
                if (columnX == 1) {
                    left.add(m);
                }
                if (columnX == 2) {
                    right.add(m);
                }
            }
        }


        request.setAttribute("right", right);
        request.setAttribute("left", left);
        request.setAttribute("containderStructure", structure);
    }

    private void processComponentToConfigurationView(HttpServletRequest request, ResultDTO resultDTO) {
        List<Component> xmlComponents = Builder.i.getComponentList();
        List<Map> selectedComponents = (List<Map>) resultDTO.get("structure");

        List<Map> availableComponents = new ArrayList<Map>();
        List<Map> left = new ArrayList<Map>();
        List<Map> right = new ArrayList<Map>();

        for (Component xmlComponent : xmlComponents) {
            if (!containComponent(xmlComponent.getId(), selectedComponents)) {
                availableComponents.add(convertComponentObjToMap(xmlComponent));
            }
        }

        for (Map m : selectedComponents) {
            int id = (Integer) m.get("xmlComponentId");
            Component xmlComponent = Builder.i.findComponentById(id);
            completeMapComponent(m, xmlComponent);
        }

        for (Map m : selectedComponents) {
            Integer columnX = (Integer) m.get("columnY");
            if (columnX == 1) {
                left.add(m);
            }
            if (columnX == 2) {
                right.add(m);
            }
        }

        request.setAttribute("availableComponents", availableComponents);
        request.setAttribute("left", left);
        request.setAttribute("right", right);
    }

    private void completeMapComponent(Map m, Component xmlComponent) {
        m.put("nameResource", xmlComponent.getResourceKey());
        m.put("name", xmlComponent.getName());
    }

    private boolean containComponent(int id, List<Map> mapComponents) {

        for (Map m : mapComponents) {
            if (id == ((Integer) m.get("xmlComponentId"))) {
                return true;
            }
        }
        return false;
    }

    private Map convertComponentObjToMap(Component c) {
        Map m = new HashMap();
        m.put("xmlComponentId", c.getId());
        m.put("nameResource", c.getResourceKey());
        m.put("name", c.getName());
        return m;
    }


}

