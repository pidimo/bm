package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.LanguageCmd;
import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import com.piramide.elwis.web.contactmanager.form.ContactPersonForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * Action Class for interact with ContactPersonForm.
 * This class manage the UI search and Select, or create a new ContactPerson
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonAddAction.java 9811 2009-10-06 22:45:11Z miguel $
 */

public class ContactPersonAddAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("ContactPersonAddAction executing...");
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        SearchForm searchForm = (SearchForm) form;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        if ((searchForm.getParameter("newButton")) != null) {
            log.debug("Create new Contact Person without choose one from list...");
            ContactPersonForm personForm = new ContactPersonForm();
            LanguageCmd cmd = new LanguageCmd();
            if (mapping instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping) {
                log.debug(" ... check entries limit ... ");
                CheckEntriesMapping entriesMapping = (CheckEntriesMapping) mapping;
                ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
                limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
                limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
                limitUtilCmd.putParam("functionality", "CONTACT");
                try {
                    BusinessDelegate.i.execute(limitUtilCmd, request);
                } catch (AppLevelException e) {
                    log.debug(" ... error execute ModuleEntriesUtilCmd ...");
                }
                if (!limitUtilCmd.getResultDTO().isFailure()) {
                    if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate")).booleanValue()) {
                        log.debug(" ... can't create ...");
                        ActionErrors errors = new ActionErrors();
                        Map map = new HashMap();
                        personForm.getDtoMap().putAll(map);
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                        saveErrors(request.getSession(), errors);
                        return mapping.findForward("redirect");
                    }
                }
            }

            personForm.getDtoMap().put("languageId", (Integer) cmd.getLanguageContact(new Integer(user.getValue("companyId").toString()), new Integer(request.getParameter("contactId").toString())));
            personForm.getDtoMap().put("newAddress", new Boolean(true));
            personForm.getDtoMap().put("name1", searchForm.getParameter("name1"));
            personForm.getDtoMap().put("name2", searchForm.getParameter("name2"));
            personForm.getDtoMap().put("searchName", searchForm.getParameter("searchName"));
            personForm.setDto("contactPersonTelecomMap", AddressContactPersonHelper.getDefaultTelecomTypes(request));
            request.setAttribute("contactPersonForm", personForm);

            //check entry limits for address table.
            return mapping.findForward("New");

        } else if (searchForm.getParameter("searchButton") != null) {
            return mapping.findForward("Search");
        } else {
            return super.execute(mapping, searchForm, request, response);
        }
    }
}
