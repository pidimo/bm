package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.cmd.catalogmanager.CategoryCmd;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CategoryValueForwardAction extends ForwardAction {

    private Log log = LogFactory.getLog(CategoryValueForwardAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();

        String categoryId = request.getParameter("dto(categoryId)");
        if (null == categoryId) {
            errors.add("categoryId", new ActionError("customMsg.NotFound",
                    request.getParameter("dto(categoryName)")));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        } else {
            errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_CATEGORY, "categoryid",
                    request.getParameter("dto(categoryId)"), errors, new ActionError("customMsg.NotFound",
                            request.getParameter("dto(categoryName)")));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainSearch");
            }
        }

        DefaultForm defaultForm = (DefaultForm) form;
        CategoryDTO dto = readCategory(categoryId, request);

        defaultForm.setDto("categoryId", dto.get("categoryId"));
        defaultForm.setDto("tableId", dto.get("table"));
        defaultForm.setDto("categoryName", request.getParameter("dto(categoryName)"));
        return super.execute(mapping, defaultForm, request, response);
    }

    private CategoryDTO readCategory(String categoryId, HttpServletRequest request) {
        CategoryCmd categoryCmd = new CategoryCmd();
        categoryCmd.setOp("readCategory");
        categoryCmd.putParam("categoryId", Integer.valueOf(categoryId));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoryCmd, request);
            return (CategoryDTO) resultDTO.get("categoryDTO");
        } catch (AppLevelException e) {
            log.debug("-> Execute " + CategoryCmd.class.getName() + " FAIL");
        }
        return null;
    }
}
