package com.piramide.elwis.web.dashboard.component.web.struts.action;

import com.piramide.elwis.web.dashboard.component.core.ComponentManager;
import com.piramide.elwis.web.dashboard.component.web.struts.form.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * This class conducts the following operations with the configuration form.
 * 1. Filters the values that are by defect
 * 2. Filter the empty values
 * 3. Group the visible columns in a single object (Ready of Maps)
 * 4. Group the filters in single object (Ready of Maps)
 * 5. Keep the configuration from the configuration in the data base
 *
 * @author : ivan
 */
public abstract class SaveConfigurationAction extends Action {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse httpservletresponse) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;

        int componentId = -1;
        try {
            componentId = new Integer(defaultForm.getDto("componentId").toString());
        } catch (NumberFormatException nfe) {
            log.debug("Cannot read componentId in com.piramide.elwis.web.dashboard.component.web.struts.form.DefaultForm ", nfe);
        } catch (NullPointerException npe) {
            log.debug("Cannot read componentId in com.piramide.elwis.web.dashboard.component.web.struts.form.DefaultForm ", npe);
        }
        if (componentId != -1) {
            Map parameters = dataBaseReadParameters(request);
            if (null == parameters) {
                parameters = new HashMap();
            }

            DefaultForm myForm = (DefaultForm) form;

            myForm.addVisibleColumnsToDto();
            myForm.addOrderableColumnsToDto();
            myForm.removeDefaultValues();
            myForm.addFiltersToDto();

            //parameters.putAll(myForm.getDtoMap());

            Map formDTO = new HashMap();
            formDTO.putAll(myForm.getDtoMap());
            ComponentManager.i.saveComponentToDB(formDTO, parameters, componentId, request);
            return mapping.findForward("Success");
        } else {
            return mapping.findForward("Fail");
        }
    }

    public abstract Map dataBaseReadParameters(HttpServletRequest request);

}
