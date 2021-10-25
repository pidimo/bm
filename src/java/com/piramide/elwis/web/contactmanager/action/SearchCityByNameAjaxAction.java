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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action to search city by city name and zip, result is send as JSON to the ajax request
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.4.2
 */
public class SearchCityByNameAjaxAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing SearchCityByNameAjaxAction................" + request.getParameterMap());
        log.debug("Query to find................" + request.getParameter("cityNameToken"));

        String cityNameToken = request.getParameter("cityNameToken");
        String countryId = request.getParameter("countryId");
        String cityZip = request.getParameter("cityZip");

        executeFantabulousList(cityNameToken, countryId, cityZip, request, response);

        return null;
    }

    private void executeFantabulousList(String searchStr, String countryId, String cityZip, HttpServletRequest request, HttpServletResponse response) {
        List<Map> resultSetList = new ArrayList<Map>();

        if (!GenericValidator.isBlankOrNull(searchStr) && !GenericValidator.isBlankOrNull(countryId)) {

            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
            String listName = "searchCityList";

            FantabulousUtil fantabulousUtil = new FantabulousUtil();
            fantabulousUtil.setModule("/contacts");
            fantabulousUtil.addSearchParameter("companyId", companyId.toString());
            fantabulousUtil.addSearchParameter("cityName", searchStr.trim());
            fantabulousUtil.addSearchParameter("countryId", countryId.trim());

            if (!GenericValidator.isBlankOrNull(cityZip)) {
                fantabulousUtil.addSearchParameter("zipEqual", cityZip.trim());
            }

            resultSetList = fantabulousUtil.getData(request, listName);
        }

        JSONArray resultJsonArray = processFantabulousListResult(resultSetList);
        processExecuteResponse(response, resultJsonArray);
    }

    private JSONArray processFantabulousListResult(List<Map> resultSetList) {
        JSONArray jsonArray = new JSONArray();

        if (resultSetList != null) {

            for (int i = 0; i < resultSetList.size(); i++) {
                Map resultSetMap = resultSetList.get(i);

                String cityId = (String) resultSetMap.get("cityId");
                String cityName = (String) resultSetMap.get("name");
                String cityZip = (String) resultSetMap.get("zip");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cityId", cityId);
                jsonObject.put("cityName", cityName);
                jsonObject.put("cityZip", cityZip);

                //add in json array
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    private void processExecuteResponse(HttpServletResponse response, JSONArray jsonArray) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ajaxForward", "Success");
        jsonObject.put("resultDataArray", jsonArray);

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