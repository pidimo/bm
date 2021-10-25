package com.piramide.elwis.web.dashboard.component.web.struts.action;

import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.core.ApplyStrategy;
import com.piramide.elwis.web.dashboard.component.web.strategy.ListStrategy;
import com.piramide.elwis.web.dashboard.component.web.strategy.ReadUrlStrategy;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class DrawComponentAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        int componentId = -1;

        try {
            componentId = new Integer(request.getParameter("componentId"));
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        if (-1 != componentId) {
            Locale locale = getLocale(request);
            String uiComponent = "";
            Component xmlComponent = Builder.i.findComponentById(componentId);

            //process ListComponents
            if (null != xmlComponent.getComponentConfiguration().getIuImplementation() &&
                    "LIST".equals(xmlComponent.getComponentConfiguration().getIuImplementation())) {
                ListStrategy l = new ListStrategy(this, componentId, request, response, locale);
                ApplyStrategy app = new ApplyStrategy(l);

                uiComponent = app.applyStrategy();
            }

            //process urlComponents
            if (null != xmlComponent.getComponentConfiguration().getComponentUrl()) {
                ReadUrlStrategy r = new ReadUrlStrategy(this, componentId, request, response, locale);

                ApplyStrategy app = new ApplyStrategy(r);
                uiComponent = app.applyStrategy();
            }

            //write in page component contain
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "max-age=0");
            try {
                PrintWriter writer = response.getWriter();
                writer.write(Functions.filterAjaxResponse(uiComponent));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return null;
        }
        return mapping.findForward("Fail");
    }

    public abstract Map dataBaseReadParameters(HttpServletRequest request);

    public abstract ActionServlet getActionServlet();

    public abstract String getApplicationResourcesPath();

    public abstract Map getSearchParameters(HttpServletRequest request);

    public abstract Map getConverterParameters(HttpServletRequest request);

    public abstract Locale getLocale(HttpServletRequest request);
}
