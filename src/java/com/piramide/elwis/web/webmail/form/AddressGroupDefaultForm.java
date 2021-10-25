package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.AddressGroupCmd;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddressGroupDefaultForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AddressGroupDefaultForm extends WebmailDefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        if (getDto("save") != null) {
            AddressGroupCmd addressGroupCmd = new AddressGroupCmd();
            DTO dto = new DTO();
            dto.put("op", "verifyTelecom");
            dto.put("addressGroupEmailId", getDto("addressGroupEmailId"));
            addressGroupCmd.putParam(dto);
            ResultDTO resultDTO = new ResultDTO();
            try {
                resultDTO = BusinessDelegate.i.execute(addressGroupCmd, request);
                if (resultDTO.get("ERRORTELECOM").toString().equals("TRUE")) {
                    errors.add("AddressGroup", new ActionError("Webmail.addressGroup.telecomNotFound", getDto("addressGroupName").toString()));
                }
            }
            catch (Exception ex) {
            }
        }
        return (errors);
    }
}
