package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.webmailmanager.MailGroupAddrCmd;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddressGroupListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AddressGroupListAction extends WebmailListAction {
    private boolean isAdd = false;

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing addressGroupListAction");

        //Reads the name of the group
        Object mailGroupAddrId = request.getParameter("dto(mailGroupAddrId)");

        DTO dto = new DTO();
        dto.put("mailGroupAddrId", mailGroupAddrId);
        dto.put("op", "read");
        MailGroupAddrCmd mailGroupAddrCmd = new MailGroupAddrCmd();
        mailGroupAddrCmd.putParam(dto);
        ResultDTO resDTO = new ResultDTO();
        resDTO = BusinessDelegate.i.execute(mailGroupAddrCmd, request);
        request.setAttribute("groupName", resDTO.get("name"));
        request.setAttribute("mailGroupAddrId", resDTO.get("mailGroupAddrId"));

        if (!isAdd) {
            //Set the parameter for the list
            SearchForm searchForm = (SearchForm) form;
            searchForm.setParameter("groupAddressId", mailGroupAddrId);
        }
        return (super.execute(mapping, form, request, response));
    }
}
