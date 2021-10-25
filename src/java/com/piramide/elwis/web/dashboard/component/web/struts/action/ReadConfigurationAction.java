package com.piramide.elwis.web.dashboard.component.web.struts.action;

import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.core.ComponentManager;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import com.piramide.elwis.web.dashboard.component.web.util.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class ReadConfigurationAction extends Action {
    private Log log = LogFactory.getLog(ReadConfigurationAction.class);

    protected Component dbComponent;
    protected Component xmlComponent;


    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse httpservletresponse) throws Exception {
        int componentid = -1;
        try {
            componentid = new Integer(request.getParameter("componentId"));
        } catch (NumberFormatException nfe) {
            log.error("Cannot read componentId in parameter", nfe);
        } catch (NullPointerException npe) {
            log.error("Cannot read componentId in parameter", npe);
        }
        if (-1 != componentid) {

            Map parameters = dataBaseReadParameters(request);
            xmlComponent = Builder.i.findComponentById(componentid);

            log.debug("en ReadConfigurationAction xmlComponent " + xmlComponent);

            dbComponent = (Component) xmlComponent.clone();
            ComponentManager.i.readComponentFromDataBase(parameters,
                    xmlComponent,
                    dbComponent, request);

            log.debug("en ReadConfigurationAction xmlComponent " + dbComponent);
            if (null == dbComponent) {
                return mapping.findForward("Fail");
            }

            List availableColumns = WebUtils.getAvailableColumns(xmlComponent, dbComponent);
            request.setAttribute(Constant.AVAILABLE_COLUMNS, availableColumns);

            List<Column> orderableColumns = WebUtils.getOrderableColumns(xmlComponent, dbComponent);
            request.setAttribute(Constant.ORDERABLE_COLUMNS_REQ, orderableColumns);

            List<Column> selectedColumns = dbComponent.getVisibleColumns();
            if (null == dbComponent.getVisibleColumns() || dbComponent.getVisibleColumns().isEmpty()) {
                selectedColumns = xmlComponent.getDefaultColumns();
            }
            request.setAttribute(Constant.SELECTED_COLUMNS, selectedColumns);

            return mapping.findForward("Success");
        } else {
            return mapping.findForward("Fail");
        }
    }

    public abstract Map dataBaseReadParameters(HttpServletRequest request);
}
