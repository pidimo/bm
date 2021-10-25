package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.CityCmd;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;


/**
 * This class executes de special logic for catalog city
 *
 * @author Ivan
 * @version $Id: SearchCityAction.java 12635 2017-01-31 21:28:46Z miguel $
 */
public class SearchCityAction extends com.piramide.elwis.web.common.action.ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        log.debug("SearchCity Action....");
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();

        if (request.getParameter("createButton") != null) {
            log.debug("validate and then send to confirm creation");

            //country cannot be empty
            if (GenericValidator.isBlankOrNull(searchForm.getParameter("countryId").toString())) {
                errors.add("countryId", new ActionError("errors.required", JSPHelper.getMessage(request,
                        "Contact.country")));
            }
            if (GenericValidator.isBlankOrNull(searchForm.getParameter("cityName").toString().trim())) {
                errors.add("city", new ActionError("errors.required", JSPHelper.getMessage(request,
                        "Contact.city")));
            }

            if (!GenericValidator.isBlankOrNull(searchForm.getParameter("countryId").toString())) {//validate if country exists
                errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_COUNTRY, "countryid",
                        searchForm.getParameter("countryId"), errors, new ActionError("error.SelectedNotFound",
                                JSPHelper.getMessage(request, "Contact.country")));
            }

            if (!errors.isEmpty()) {
                saveErrors(request, errors);

            } else { //create the city
                CityCmd cityCmd = new CityCmd();
                cityCmd.setOp("create");
                cityCmd.putParam("countryId", searchForm.getParameter("countryId"));
                cityCmd.putParam("cityName", searchForm.getParameter("cityName"));
                cityCmd.putParam("cityZip", searchForm.getParameter("zip").toString().trim());
                cityCmd.putParam("companyId", searchForm.getParameter("companyId"));
                ResultDTO resultDTO = BusinessDelegate.i.execute(cityCmd, request);
                if (resultDTO.hasResultMessage()) { //if have errors
                    for (Iterator iterator = resultDTO.getResultMessages(); iterator.hasNext();) {
                        ResultMessage message = (ResultMessage) iterator.next();
                        errors.add("cityCreation", new ActionError(message.getKey(), message.getParams()));

                    }
                    saveErrors(request, errors);
                } else { //select the recently created city

                    StringBuffer openUrl = new StringBuffer()
                            .append("onload=\"")
                            .append("opener.selectCity('")
                            .append(searchForm.getParameter("countryId"))
                            .append("', '")
                            .append(resultDTO.get("cityId"))
                            .append("', '")
                            .append(searchForm.getParameter("zip"))
                            .append("', '")
                            .append(searchForm.getParameter("cityName"))
                            .append("')\"");

                    request.setAttribute("jsLoad", new String(openUrl));
                }
            }
        }

        if (request.getParameter("actionMessage") != null) {
            if ("notFound".equals(request.getParameter("actionMessage"))) {
                errors.add("cityNotFound", new ActionError("Address.error.enteredCityNotFound"));
            }

            if ("tooMuch".equals(request.getParameter("actionMessage"))) {
                errors.add("tooMuchCities", new ActionError("Address.error.tooMuchCitiesByZip"));
            }

            if ("notFoundWithZipCity".equals(request.getParameter("actionMessage"))) {

                if (Functions.hasAccessRight(request, "CITY", "CREATE")) {
                    errors.add("cityNotFound", new ActionError("Address.error.enteredCityNotFound.selectOrCreate"));
                } else {
                    errors.add("cityNotFound", new ActionError("Address.error.enteredCityNotFound"));
                }
            }
            saveErrors(request, errors);
        }

        return super.execute(mapping, form, request, response);


    }
}
