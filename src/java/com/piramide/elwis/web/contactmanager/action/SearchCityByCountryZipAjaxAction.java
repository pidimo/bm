package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action to search city by zip, result is send as JSON to the ajax request
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.4.2
 */
public class SearchCityByCountryZipAjaxAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing SearchCityByCountryZipAjaxAction................" + request.getParameterMap());

        String countryId = request.getParameter("countryId");
        String cityZip = request.getParameter("cityZip");

        executeFantabulousList(countryId, cityZip, request, response);

        return null;
    }

    private void executeFantabulousList(String countryId, String cityZip, HttpServletRequest request, HttpServletResponse response) {
        List<Map> resultSetList = new ArrayList<Map>();

        if (!GenericValidator.isBlankOrNull(countryId) && !GenericValidator.isBlankOrNull(cityZip)) {

            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
            String listName = "searchCityList";

            FantabulousUtil fantabulousUtil = new FantabulousUtil();
            fantabulousUtil.setModule("/contacts");
            fantabulousUtil.addSearchParameter("companyId", companyId.toString());
            fantabulousUtil.addSearchParameter("countryId", countryId.trim());
            fantabulousUtil.addSearchParameter("zipEqual", cityZip.trim());

            resultSetList = fantabulousUtil.getData(request, listName);
        }

        processFantabulousListResult(resultSetList, response);
    }

    private void processFantabulousListResult(List<Map> resultSetList, HttpServletResponse response) {
        String cityId = "";
        String cityName = "";

        if (resultSetList != null && !resultSetList.isEmpty()) {

            //read the first row
            Map resultSetMap = resultSetList.get(0);
            cityId = (String) resultSetMap.get("cityId");
            cityName = (String) resultSetMap.get("name");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", "SuccessCitySearch");
        jsonObject.put("cityId", cityId);
        jsonObject.put("cityName", cityName);

        setDataInResponse(response, jsonObject.toJSONString());
    }

    private void setDataInResponse(HttpServletResponse response, String jsonData) {
        log.debug("Response Value:\n" + jsonData);

        response.setContentType("application/json");
        try {
            PrintWriter write = response.getWriter();
            write.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}