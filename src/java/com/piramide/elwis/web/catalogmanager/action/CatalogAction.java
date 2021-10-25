package com.piramide.elwis.web.catalogmanager.action;

import com.piramide.elwis.cmd.catalogmanager.CatalogCmd;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class executes de special logic for catalog city
 *
 * @author Ivan
 * @version $Id: CatalogAction.java 9703 2009-09-12 15:46:08Z fernando $
 * @noinspection ALL
 */

public class CatalogAction extends Action {
    private Log log = LogFactory.getLog(CatalogAction.class);
    private boolean hasErrors;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {
        if (isCancelled(request)) {
            String toCancel = "Cancel";
            if (request.getParameter("cancel") != null) {
                toCancel = "Success";
            }
            return (mapping.findForward(toCancel));
        }
        ActionForward forward = null;
        hasErrors = false;
        try {
            forward = executeCatalogCommand(mapping, form, request);
        } catch (AppLevelException e) {
            log.debug("execute: ", e);
            throw e;
        } catch (Exception e) {
            log.error("execute: ", e);
            throw e;
        }

        if (request.getParameter("SaveAndNew") != null) {
            log.debug("Is SaveAndNew");
            DefaultForm newForm = (DefaultForm) form;
            //If errors occur in the filling the form
            if (hasErrors) {
                return mapping.getInputForward();
            } else {
                newForm.getDtoMap().clear();
                forward = mapping.findForward("SaveAndNew");
            }
        }
        return forward;
    }

    private ActionForward executeCatalogCommand(
            ActionMapping mapping,
            ActionForm actionForm,
            HttpServletRequest req)
            throws AppLevelException {
        CatalogCmd catalogCmd = new CatalogCmd();
        if (mapping.getPath().indexOf("Delete") != -1) {
            catalogCmd.setCheckReferences(true);
        }

        //get formDTO (DefaultForm's DTO)
        final DefaultForm defaultForm = (DefaultForm) actionForm;
        final Map formDTO = defaultForm.getDtoMap();

        String[] configs = mapping.getParameter().split(",");
        String className = configs[0];

        catalogCmd.setDtoClassName(className);
        catalogCmd.putParam(formDTO);
        catalogCmd.putParam("companyId", RequestUtils.getUser(req).getValue("companyId"));
        Map copyFields = new HashMap();
        if (configs.length > 1) {
            for (int i = 1; i < configs.length; i++) {
                String[] config = configs[i].split("=");
                String configType = config[0].trim();
                if ("boolean".equals(configType)) {
                    String[] booleans = config[1].split("-");
                    for (int j = 0; j < booleans.length; j++) {
                        String booleanField = booleans[j].trim();
                        boolean fieldValue = "true".equals(catalogCmd.getParamDTO().getAsString(booleanField));
                        catalogCmd.putParam(booleanField, new Boolean(fieldValue));
                    }
                } else if ("forwardNotFound".equals(configType)) {
                    catalogCmd.setNotFoundForward(configs[1]);
                } else if ("validate".equals(configType)) {
                    catalogCmd.setValidate(true);
                } else if ("copyField".equals(configType)) {
                    String[] fields = config[1].split("-");
                    copyFields.put(fields[0], fields[1]);
                }
            }
        }

        //execute EJBCommand
        final ResultDTO resultDTO = BusinessDelegate.i.execute(catalogCmd, req);
        if (copyFields.size() > 0) {
            for (Iterator iterator = copyFields.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                resultDTO.put(entry.getKey(), resultDTO.get(entry.getValue()));
            }
        }
        //clear form if needed
        if (resultDTO.isClearingForm) {
            formDTO.clear();
        }

        formDTO.putAll(resultDTO);
        //convert ResultMessages to ActionErrors
        if (resultDTO.hasResultMessage()) {
            hasErrors = true;
            saveErrors(req, MessagesUtil.i.convertToActionErrors(mapping, req, resultDTO));
        }

        //determine forward. if the result is failure, get back to previous page
        if (resultDTO.isFailure()) {
            return new ActionForward(mapping.getInput());
        } else {
            return mapping.findForward(resultDTO.getForward());
        }

    }
}
