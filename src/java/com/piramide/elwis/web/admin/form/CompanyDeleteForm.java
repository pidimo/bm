package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.cmd.admin.SystemModuleCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CompanyDeleteForm extends DefaultForm {
    private Log log = LogFactory.getLog(CompanyDeleteForm.class);

    private List modules = new ArrayList();

    public Object[] getModules() {
        modules = (List) getDto("modules");
        return modules.toArray();
    }

    public void setModules(Object[] elements) {
        if (elements != null) {
            setDto("modules", Arrays.asList(elements));
        } else {
            setDto("modules", new ArrayList());
        }
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest httpServletRequest) {
        return super.validate(actionMapping, httpServletRequest);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        SystemModuleCmd cmd = new SystemModuleCmd();
        cmd.setOp("readSystemModules");
        try {
            BusinessDelegate.i.execute(cmd, request);
            ResultDTO resultDTO = cmd.getResultDTO();
            List<Map> systemModules = (List<Map>) resultDTO.get("systemModules");
            request.setAttribute("moduleList", systemModules);
        } catch (AppLevelException e) {
            log.debug("-> Execute SystemModuleCmd FAIL");
        }
    }
}
