package com.piramide.elwis.web.bmapp.el;

import com.piramide.elwis.cmd.catalogmanager.CountryUtilCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.salesmanager.SalePositionParticipantCmd;
import com.piramide.elwis.cmd.webmailmanager.MailAccountCmd;
import com.piramide.elwis.cmd.webmailmanager.ReadMailCmd;
import com.piramide.elwis.cmd.webmailmanager.ReadTelecomCmd;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.RESTConstants;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.PageParam;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.alfacentauro.fantabulous.controller.ResultList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static String escapeJSON(String text) {
        if (text == null || text.length() == 0) {
            return text;
        }

        char         c = 0;
        int          i;
        int          len = text.length();
        StringBuilder sb = new StringBuilder();
        String       t;

        for (i = 0; i < len; i += 1) {
            c = text.charAt(i);
            switch (c) {
                case '\\':
                case '"':
                    sb.append('\\');
                    sb.append(c);
                    break;
                case '/':
                    //                if (b == '<') {
                    sb.append('\\');
                    //                }
                    sb.append(c);
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (c < ' ') {
                        t = "000" + Integer.toHexString(c);
                        sb.append("\\u" + t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }

        return sb.toString();
    }

    public static Map getFantabulousListResultInfo(String listName, HttpServletRequest request) {
        Map map = new HashMap();
        ResultList resultList = (ResultList) request.getAttribute(listName);

        if (resultList != null) {
            Parameters parameters =  resultList.getParameters();
            log.debug("Result list info to:" + listName + " parameters:" + parameters);

            if (parameters != null && parameters.getPageParam() != null) {
                PageParam pageParam = parameters.getPageParam();
                map.put("pageNumber", pageParam.getPageNumber());
                map.put("pageSize", pageParam.getPageSize());
                map.put("totalPages", pageParam.getTotalPages());
                map.put("listSize", pageParam.getListSize());
            }
        }
        return map;
    }

    public static Integer getFantabulousRowsPerPage() {
        return 20;
    }

    public static Long getDateTimeAsMillis(Object dateAsInteger, Object hour, Object minute, HttpServletRequest request) {

        if (dateAsInteger != null && !GenericValidator.isBlankOrNull(dateAsInteger.toString())
                && hour != null && !GenericValidator.isBlankOrNull(hour.toString())
                && minute != null && !GenericValidator.isBlankOrNull(minute.toString())) {

            User user = RequestUtils.getUser(request);
            DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");
            if (dateTimeZone == null) {
                dateTimeZone = DateTimeZone.getDefault();
            }

            DateTime dateTime = DateUtils.integerToDateTime(Integer.valueOf(dateAsInteger.toString()), Integer.valueOf(hour.toString()), Integer.valueOf(minute.toString()), dateTimeZone);
            return dateTime.getMillis();
        }
        return null;
    }

    public static Map getMailAddressInfo(String mailId, String folderType, String fromEmail, HttpServletRequest request) {
        Map infoMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(mailId)) {
            if (WebMailConstants.FOLDER_SENDITEMS.equals(folderType)) {

                ReadMailCmd readMailCmd = new ReadMailCmd();
                readMailCmd.setOp("readRecipientsEmails");
                readMailCmd.putParam("mailId", new Integer(mailId));

                ResultDTO resultDTO = new ResultDTO();
                try {
                    resultDTO = BusinessDelegate.i.execute(readMailCmd, request);
                } catch (AppLevelException e) {
                }

                List recipientMapList = new ArrayList();
                if (!resultDTO.isFailure() && resultDTO.get("mailRecipientsData") != null) {
                    recipientMapList = (List) resultDTO.get("mailRecipientsData");
                }

                if (recipientMapList.size() == 1) {
                    Map map = (Map) recipientMapList.get(0);
                    Map addressMap = getAddressInfoByEmail((String) map.get("email"), request);
                    infoMap = setMailIconType(addressMap);
                } else {

                    infoMap.put("mailIconType", RESTConstants.MailListIconType.RECIPIENTS_UNKNOWN.getConstantAsString());
                    boolean isAllRecipientsKnown = true;
                    for (Object aRecipientMapList : recipientMapList) {
                        Map map = (Map) aRecipientMapList;
                        Map addressMap = getAddressInfoByEmail((String) map.get("email"), request);

                        if (addressMap.get("addressId") != null) {
                            infoMap.put("mailIconType", RESTConstants.MailListIconType.RECIPIENTS_AT_LEAST_ONE_KNOWN.getConstantAsString());
                        } else {
                            isAllRecipientsKnown = false;
                        }
                    }

                    if (isAllRecipientsKnown && !recipientMapList.isEmpty()) {
                        infoMap.put("mailIconType", RESTConstants.MailListIconType.RECIPIENTS_KNOWN.getConstantAsString());
                    }
                }

            } else {
                Map addressMap = getAddressInfoByEmail(fromEmail, request);
                infoMap = setMailIconType(addressMap);
            }
        }

        return infoMap;
    }

    private static Map setMailIconType(Map addressMap) {
        addressMap.put("mailIconType", RESTConstants.MailListIconType.UNKNOWN.getConstantAsString());

        if (addressMap.get("addressId") != null) {
            addressMap.put("mailIconType", RESTConstants.MailListIconType.KNOWN_WITHOUT_PICTURE.getConstantAsString());
            if (addressMap.get("imageId") != null) {
                addressMap.put("mailIconType", RESTConstants.MailListIconType.KNOWN_WITH_PICTURE.getConstantAsString());
            }
        }
        return addressMap;
    }

    private static Map getAddressInfoByEmail(String email, HttpServletRequest request) {
        Map addressMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(email)) {

            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue("companyId");

            //all address are requested for who the email
            ReadTelecomCmd readTelecomCmd = new ReadTelecomCmd();
            readTelecomCmd.setOp("readTelecomJustOne");
            readTelecomCmd.putParam("email", email);
            readTelecomCmd.putParam("companyId", companyId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readTelecomCmd, null);
                if (!resultDTO.isFailure() && resultDTO.containsKey("telecomMap")) {
                    Map telecomMap = (Map) resultDTO.get("telecomMap");
                    Integer addressId = (Integer) telecomMap.get("addressId");
                    Integer contactPersonId = (Integer) telecomMap.get("contactPersonId");

                    if (contactPersonId != null) {
                        addressId = contactPersonId;
                    }
                    //read address info
                    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
                    addressCmd.putParam("addressId", addressId);
                    ResultDTO addressResultDTO = BusinessDelegate.i.execute(addressCmd, null);
                    if (!addressResultDTO.isFailure()) {

                        addressMap.put("addressId", addressId);
                        addressMap.put("imageId", addressResultDTO.get("imageId"));
                    }
                }
            } catch (AppLevelException e) {
                log.error("-> Execute cmd FAIL", e);
            }
        }
        return addressMap;
    }

    public static List<CityDTO> findCountryCityDTOList(Object countryId, Object companyId) {
        List<CityDTO> result = new ArrayList<CityDTO>();

        if (countryId != null && !GenericValidator.isBlankOrNull(countryId.toString())) {

            CountryUtilCmd countryUtilCmd = new CountryUtilCmd();
            countryUtilCmd.setOp("findCountryCities");
            countryUtilCmd.putParam("countryId", countryId);
            countryUtilCmd.putParam("companyId", companyId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(countryUtilCmd, null);
                if (!resultDTO.isFailure() && resultDTO.containsKey("countryCityDTOList")) {
                    result = (List<CityDTO>) resultDTO.get("countryCityDTOList");
                }
            } catch (AppLevelException e) {
                log.error("-> Execute cmd FAIL", e);
            }
        }
        return result;
    }

    public static Map getDefaultUserMailAccountInfo(HttpServletRequest request) {
        Map mailAccountDTO = null;

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userMailId = (Integer) user.getValue("userId");

        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("searchDefaultAccount");
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            if (resultDTO.containsKey("defaultAccount") && resultDTO.get("defaultAccount") != null) {
                mailAccountDTO = (Map) resultDTO.get("defaultAccount");
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute MailAccountCmd ", e);
        }

        return mailAccountDTO;
    }

    public static void initializeSchedulerUserId(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        user.setValue("schedulerUserId", user.getValue(Constants.USERID));
    }

    public static boolean checkAlreadyIsParticipantInEvent(Integer productId, Integer userAddressId, HttpServletRequest request) {
        boolean isParticipant = false;

        if (productId != null && userAddressId != null) {
            SalePositionParticipantCmd cmd = new SalePositionParticipantCmd();
            cmd.setOp("checkParticipant");
            cmd.putParam("productId", productId);
            cmd.putParam("contactPersonId", userAddressId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                if (resultDTO.get("alreadyIsParticipant") != null) {
                    isParticipant = (Boolean) resultDTO.get("alreadyIsParticipant");
                }
            } catch (AppLevelException e) {
                log.error("Cannot execute SalePositionParticipantCmd ", e);
            }
        }

        return isParticipant;
    }


}
