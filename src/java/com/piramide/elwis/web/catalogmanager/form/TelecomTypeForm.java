package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: TelecomTypeForm.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 30-03-2005 09:49:22 AM ivan Exp $
 */
public class TelecomTypeForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Validate TelecomType form...");

        ActionErrors errors = new ActionErrors();

        errors = super.validate(mapping, request);

        String op = (String) getDto("op");
        if ("update".equals(op)) {

            String actualType = (String) this.getDto("actualType");


            if (!actualType.equals(this.getDto("type"))) {

                TelecomTypeDTO telecomTypeDTO = new TelecomTypeDTO();
                telecomTypeDTO.put("telecomTypeId", this.getDto("telecomTypeId"));

                ResultDTO globalDTO = new ResultDTO();
                IntegrityReferentialChecker.i.check(telecomTypeDTO, globalDTO);

                if (globalDTO.isFailure()) {
                    this.setDto("type", actualType);
                    String resourceKey = JSPHelper.getTelecomType(actualType, request);
                    errors.add("languageAsociated", new ActionError("error.TelecomType.asociated", resourceKey));
                }
            }

        }
        return errors;
    }
}
