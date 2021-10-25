package com.piramide.elwis.web.webmail.action;

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
 * Action to find revipient to autocomplete in compose mail, JSON data is send as response
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class MailFindRecipientsAjaxAction extends DefaultAction {
    private Integer RESULT_LIMIT = 50;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing MailFindRecipientsAjaxAction................" + request.getParameterMap());
        log.debug("Query to find................" + request.getParameter("tokenFieldQuery"));

        String tokenFieldQuery = request.getParameter("tokenFieldQuery");
        executeFantabulousList(tokenFieldQuery, request, response);

        return null;
    }

    private void executeFantabulousList(String searchStr, HttpServletRequest request, HttpServletResponse response) {
        List<Map> resultSetList = new ArrayList<Map>();

        if (!GenericValidator.isBlankOrNull(searchStr)) {

            //fix the search parameter to find names
            searchStr = searchStr.trim();
            String nameSearch = searchStr.replaceAll(" ", "%");

            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
            String listName = "findRecipientEmailTelecomList";

            FantabulousUtil fantabulousUtil = new FantabulousUtil();
            fantabulousUtil.setModule("/webmail");
            fantabulousUtil.addSearchParameter("companyId", companyId.toString());
            fantabulousUtil.addSearchParameter("telecomNumber", searchStr);
            fantabulousUtil.addSearchParameter("addressSearchNameFixed", nameSearch);
            fantabulousUtil.addSearchParameter("contactPersonSearchNameFixed", nameSearch);

            resultSetList = fantabulousUtil.getData(request, listName);
        }

        JSONArray resultJsonArray = processFantabulousListResult(resultSetList);
        processExecuteResponse(response, resultJsonArray);
    }

    private JSONArray processFantabulousListResult(List<Map> resultSetList) {
        JSONArray jsonArray = new JSONArray();

        if (resultSetList != null) {

            int limitIndex = resultSetList.size();
            if (limitIndex > RESULT_LIMIT) {
                limitIndex = RESULT_LIMIT;
            }

            for (int i = 0; i < limitIndex; i++) {
                Map resultSetMap = resultSetList.get(i);

                String telecomNumber = (String) resultSetMap.get("telecomNumber");
                String telecomAddressId = (String) resultSetMap.get("telecomAddressId");
                String telecomContactPersonId = (String) resultSetMap.get("telecomContactPersonId");
                String addressName = (String) resultSetMap.get("addressName");
                String contactPersonName = (String) resultSetMap.get("contactPersonName");
                boolean isPredetermined = "1".equals(resultSetMap.get("telecomPredetermined"));

                //define recipient email structure
                String addressId = telecomAddressId;
                String contactName = addressName;
                String organizationId = "";
                String organizationName = "";

                if (!GenericValidator.isBlankOrNull(telecomContactPersonId)) {
                    addressId = telecomContactPersonId;
                    organizationId = telecomAddressId;

                    contactName = contactPersonName;
                    organizationName = "[" + addressName + "]";
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("label", contactName);
                jsonObject.put("value", telecomNumber);
                jsonObject.put("organizationName", organizationName);
                jsonObject.put("addressId", addressId);
                jsonObject.put("contactPersonOfId", organizationId);
                jsonObject.put("containerCssClass", isPredetermined ? "suggestionDefaultEmail" : "");

                //add in json array
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }


    private void testJsonResult(HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("label", "juan perez");
        jsonObject.put("value", "111@111.com");
        jsonObject.put("addressId", "4");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("label", "miky rojas");
        jsonObject2.put("value", "2222@111.com");
        jsonObject2.put("addressId", "5");


        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        jsonArray.add(jsonObject2);

        setDataInResponse(response, jsonArray.toJSONString());
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