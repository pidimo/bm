package com.piramide.elwis.web.webmail.el;

import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.webmailmanager.ComposeTemporalMailCmd;
import com.piramide.elwis.cmd.webmailmanager.FolderCmd;
import com.piramide.elwis.cmd.webmailmanager.MailAccountCmd;
import com.piramide.elwis.cmd.webmailmanager.PreferencesCmd;
import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderPojo;
import com.piramide.elwis.cmd.webmailmanager.util.foldertree.FolderTreeModel;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.utils.TelecomType;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.htmlfilter.ImageStoreByImgTagFilter;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.webmail.form.MailFormHelper;
import com.piramide.elwis.web.webmail.util.EmailRecipientHelper;
import com.piramide.elwis.web.webmail.util.FolderTreeUtil;
import com.piramide.elwis.web.webmail.util.TokenFieldEmailRecipientHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.core.Config;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;

/**
 * Webmail manager jstl functions
 *
 * @author Miguel Rojas
 * @version 4.2.1
 */
public class Functions {

    private static Log log = LogFactory.getLog(com.piramide.elwis.web.webmail.el.Functions.class);

    public static List getTelecomTypesOfEmails(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
        cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_LIST);
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
        cmd.putParam(TelecomTypeSelectCmd.TYPE, TelecomType.EMAIL_TYPE);    //add for type email

        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
            List result = (LinkedList) resultDto.get(TelecomTypeSelectCmd.RESULT);
            ArrayList selectList = new ArrayList();
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                TelecomTypeDTO dto = (TelecomTypeDTO) iterator.next();
                selectList.add(new LabelValueBean(String.valueOf(dto.get("telecomTypeName")),
                        String.valueOf(dto.get("telecomTypeId"))));
            }
            return SortUtils.orderByProperty(selectList, "label");

        } catch (AppLevelException e) {
            log.error("Error executing", e);
        }
        return new ArrayList();
    }


    /**
     * Gets a ArrayList with data of the telecoms in the request(as fantabulous lists)
     * author: Alvaro
     *
     * @param request
     * @param addressId
     * @param contactPersonAddressId
     * @return an array list
     */
    public static ArrayList getTelecomsDataCollection(javax.servlet.http.HttpServletRequest request, Object addressId,
                                                      Object contactPersonAddressId) {
        ArrayList res = new ArrayList();
        boolean withoutEmails = false;

        contactPersonAddressId = (contactPersonAddressId != null && contactPersonAddressId.toString().length() > 0 && !contactPersonAddressId.toString().equals(" ")) ? contactPersonAddressId : "null";
        ArrayList rl = new ArrayList();
        if (request.getAttribute("telecomsForAnContactPerson") != null) {
            rl = new ArrayList(((org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("telecomsForAnContactPerson")).getResult());
        } else {
            rl = new ArrayList(((org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("telecomsForAnAddress")).getResult());
        }

        HashMap hm = new HashMap();
        if (rl.size() == 0) {
            hm.put("TELECOMNUMBER", JSPHelper.getMessage(request, "Webmail.Address.withoutEmails"));
            hm.put("TELECOMID", "");
            res.add(hm);
            withoutEmails = true;
        } else {
            if (rl.size() > 1) {
                hm = new HashMap();
                hm.put("TELECOMID", "ALL&" + addressId + "&" + contactPersonAddressId);
                hm.put("TELECOMNUMBER", JSPHelper.getMessage(request, "Webmail.addressGroup.allMails"));
                res.add(hm);
            }
            for (int i = 0; i < rl.size(); i++) {
                hm = new HashMap();
                hm = (HashMap) rl.get(i);
                hm.put("TELECOMID", hm.get("TELECOMID") + "&" + addressId + "&" + contactPersonAddressId);
                res.add(hm);
            }
        }
        request.setAttribute("withoutEmails", withoutEmails + "");
        return (res);
    }

    /**
     * get condition keys to an filter
     *
     * @param servletRequest
     * @return a condition list
     */
    public static List getConditionsKeys(javax.servlet.ServletRequest servletRequest) {
        List result = new ArrayList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.condition1"), WebMailConstants.CONDITION_CONTAIN));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.condition2"), WebMailConstants.CONDITION_NOT_CONTAIN));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.condition3"), WebMailConstants.CONDITION_BEGIN_WITH));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.condition4"), WebMailConstants.CONDITION_TERMINATED_IN));

        return result;
    }

    /**
     * get condition name keys to an filter
     *
     * @param servletRequest
     * @return list
     */
    public static List getConditionNameKeys(javax.servlet.ServletRequest servletRequest) {
        List result = new ArrayList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.conditionNameKey1"), WebMailConstants.FROM_PART));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.conditionNameKey2"), WebMailConstants.TO_o_CC_PART));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.condition.conditionNameKey3"), WebMailConstants.SUBJECT_PART));

        return result;
    }

    /**
     * Format a dateTime with a pattern an with a locale
     *
     * @param date
     * @param timeZone
     * @param datePattern
     * @param locale
     * @return the formated string for the datetime
     */
    public static String getFormattedDateTimeWithTimeZoneAndLocale(String date,
                                                                   DateTimeZone timeZone,
                                                                   String datePattern,
                                                                   String locale) {
        log.debug("getFormattedDateTimeWithTimeZoneAndLocale('" + date + "', '" + timeZone + "', '" + datePattern + "', '" + locale + "')");
        String result = "";
        try {
            Long finalDate = new Long(date);
            DateTime d = new DateTime(finalDate.longValue(), timeZone);
            DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);
            fmt = fmt.withLocale(new Locale(locale));

            result = fmt.print(d);


            log.debug("result :   " + result);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * convert bytes value to mbytes
     *
     * @param bytes
     * @return mbytes
     */
    public static String getMBytesValueFromBytes(String bytes) {
        String res = "0";
        if (bytes != null && GenericValidator.isDouble(bytes)) {
            double bytesValue = Double.parseDouble(bytes);
            double mbValue = ((bytesValue / 1024) / 1024); //convert bytes to megabytes

            if (mbValue < 0.1) {
                mbValue = 0.1;
            }
            res = String.valueOf(mbValue);
        } else if (bytes != null && GenericValidator.isLong(bytes)) {
            long bytesValue = Long.parseLong(bytes);
            double mbValue = ((bytesValue / 1024) / 1024); //convert bytes to megabytes

            if (mbValue < 0.1) {
                mbValue = 0.1;
            }
            res = String.valueOf(mbValue);
        }
        return res;
    }


    public static Boolean useHtmlEditor(Integer userId, javax.servlet.ServletRequest servletRequest) {
        MailFormHelper emailHelper = new MailFormHelper();
        return emailHelper.useHtmlEditor(userId, (HttpServletRequest) servletRequest);
    }

    public static Boolean hasEmailAccount(Integer userId, javax.servlet.ServletRequest servletRequest) {
        MailFormHelper emailHelper = new MailFormHelper();
        Integer emailUserChecker = emailHelper.isValidEmailUser(userId, (HttpServletRequest) servletRequest);
        return -1 != emailUserChecker;
    }

    public static Boolean isAutomaticDownloadConfigurated(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        return (Boolean) user.getValue("backgroundDownload");
    }

    public static List getHtmlEditorFonts() {
        List result = new ArrayList();
        for (String fontLabel : WebMailConstants.EDITORFONT_MAP.keySet()) {
            result.add(new LabelValueBean(fontLabel, WebMailConstants.EDITORFONT_MAP.get(fontLabel)));
        }
        return result;
    }

    public static List getHtmlEditorFontSizes() {
        List result = new ArrayList();
        for (String fontLabel : WebMailConstants.EDITORFONTSIZE_MAP.keySet()) {
            result.add(new LabelValueBean(fontLabel, WebMailConstants.EDITORFONTSIZE_MAP.get(fontLabel)));
        }
        return result;
    }

    public static Map readUserMailConfiguration(javax.servlet.ServletRequest servletRequest) {
        Map userConfMap = new HashMap();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);

        PreferencesCmd cmd = new PreferencesCmd();
        cmd.putParam("userId", user.getValue("userId"));

        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);

            userConfMap.put("font", resultDto.get("editorFont"));
            userConfMap.put("fontSize", resultDto.get("editorFontSize"));
        } catch (AppLevelException e) {
            log.error("Error executing", e);
        }

        return userConfMap;
    }

    public static String getInboxFolderId(String userMailId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId);
        fantabulousUtil.addSearchParameter("folderType", WebMailConstants.FOLDER_INBOX);
        fantabulousUtil.setModule("/webmail");

        List<Map> folders = fantabulousUtil.getData(request, "uiFolderList");
        if (folders.isEmpty()) {
            log.error("-> Cannot read Inbox folder [userMailId=" + userMailId + "]");
            return null;
        }

        Map inboxFolder = folders.get(0);
        return (String) inboxFolder.get("folderId");
    }

    public static List getCustomFolders(String userMailId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId);
        fantabulousUtil.addSearchParameter("folderType", WebMailConstants.FOLDER_DEFAULT);
        fantabulousUtil.setModule("/webmail");

        List<Map> customFolders = fantabulousUtil.getData(request, "folderAndMailsList");
        List result = new ArrayList();
        for (Map folderMap : customFolders) {
            String folderId = (String) folderMap.get("folderId");
            String folderName = (String) folderMap.get("folderName");
            String unreadEmails = (String) folderMap.get("unReadMails");
            String parentFolderId = (String) folderMap.get("parentFolderId");

            Map mapFolder = new HashMap();
            mapFolder.put("folderName", folderName);
            mapFolder.put("folderId", folderId);
            mapFolder.put("unReadMails", unreadEmails);
            mapFolder.put("parentFolderId", parentFolderId);
            result.add(mapFolder);
        }

        return result;
    }

    public static String getCustomFoldersAsJSONArray(String userMailId, javax.servlet.ServletRequest servletRequest) {
        JSONArray jsonArray = new JSONArray();
        List customFolderList = getCustomFolders(userMailId, servletRequest);

        processCustomFoldersAsJSON(customFolderList, jsonArray);
        return jsonArray.toJSONString();
    }

    /**
     * Porcess folder list as json array
     * @param customFolderList folder list
     * @param jsonArray json array
     */
    private static void processCustomFoldersAsJSON(List customFolderList, JSONArray jsonArray) {

        for (int i = 0; i < customFolderList.size(); i++) {
            Map folderMap = (Map) customFolderList.get(i);
            String parentFolderId = (String) folderMap.get("parentFolderId");

            if (parentFolderId != null && !"".equals(parentFolderId.trim())) {
                //process as child folder
                addChildFolderInJsonArray(jsonArray, customFolderList, folderMap);
            } else {
                //this is parent folder
                JSONObject folderJson = new JSONObject(folderMap);
                folderJson.put("childFolders", new JSONArray());

                //add in json array
                jsonArray.add(folderJson);
                //remove folder from list
                customFolderList.remove(i);
            }
        }

        if (customFolderList.size() > 0) {
            //process child folders recursively
            processCustomFoldersAsJSON(customFolderList, jsonArray);
        }
    }

    /**
     * Add child folder in json array
     * @param jsonArray the json array
     * @param customFolderList folder list
     * @param childFolderMap Map Object, this can not be changed because is used to remove fron list as object
     */
    private static void addChildFolderInJsonArray(JSONArray jsonArray, List customFolderList, Map childFolderMap) {
        String childParentFolderId = (String) childFolderMap.get("parentFolderId");

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject folderJson = (JSONObject) jsonArray.get(i);
            String folderId = (String) folderJson.get("folderId");
            JSONArray childs = (JSONArray) folderJson.get("childFolders");

            if (folderId.equals(childParentFolderId)) {
                JSONObject childFolderJson = new JSONObject(childFolderMap);
                childFolderJson.put("childFolders", new JSONArray());

                childs.add(childFolderJson);

                //remove folder from list
                customFolderList.remove(childFolderMap);
                break;
            } else if (childs.size() > 0) {
                addChildFolderInJsonArray(childs, customFolderList, childFolderMap);
            }
        }
    }

    public static List<Map> getNewEmailsByFolder(String userMailId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId);
        fantabulousUtil.setModule("/webmail");


        List<Map> folders = fantabulousUtil.getData(request, "uiFolderList");

        List<Map> result = new ArrayList<Map>();
        for (Map dbFolder : folders) {
            String folderId = (String) dbFolder.get("folderId");
            String folderName = (String) dbFolder.get("folderName");
            List<String> emailIds = getNewEmails(folderId, companyId.toString(), request);

            if (emailIds.isEmpty()) {
                continue;
            }

            Map data = new HashMap();
            data.put("folderName", folderName);
            data.put("folderId", folderId);
            data.put("newEmailIds", emailIds);
            result.add(data);
        }

        return result;
    }

    public static List<String> getNewEmails(String folderId, String companyId, HttpServletRequest request) {
        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId);
        fantabulousUtil.addSearchParameter("folderId", folderId);
        fantabulousUtil.setModule("/webmail");

        List<Map> newEmails = fantabulousUtil.getData(request, "newEmailList");
        List<String> result = new ArrayList<String>();
        for (Map emailId : newEmails) {
            result.add(emailId.get("emailId").toString());
        }

        return result;
    }

    public static Map getWebmailFolders(String userMailId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId);
        fantabulousUtil.setModule("/webmail");

        List<Map> folders = fantabulousUtil.getData(request, "folderAndMailsList");
        Map uiFolders = new HashMap();
        List systemFolders = new ArrayList();
        List userFolders = new ArrayList();

        for (Map dbFolder : folders) {
            String type = (String) dbFolder.get("folderType");
            String id = (String) dbFolder.get("folderId");
            String name = (String) dbFolder.get("folderName");

            String emailCounter = (String) dbFolder.get("countMails");
            String unReadEmailCounter = (String) dbFolder.get("unReadMails");
            Map uiFolder = new HashMap();
            uiFolder.put("id", id);
            uiFolder.put("type", type);
            uiFolder.put("name", name);
            uiFolder.put("emailCounter", emailCounter);
            uiFolder.put("unReadEmailCounter", unReadEmailCounter);

            //now read systemfolders only
            if (WebMailConstants.FOLDER_DEFAULT.equals(type)) {
                userFolders.add(uiFolder);
            } else {
                systemFolders.add(uiFolder);
            }
        }

        uiFolders.put("systemFolders", systemFolders);
        uiFolders.put("userFolders", userFolders);

        return uiFolders;
    }

    /**
     * @param userMailId
     * @param servletRequest
     * @return
     * @deprecated replaced by getWebmailFolders
     */
    public static Map getSystemFolders(String userMailId, javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId);
        fantabulousUtil.addSearchParameter("isSystemFolder", "true");
        fantabulousUtil.setModule("/webmail");

        Map systemFolder = new HashMap();
        List<Map> folders = fantabulousUtil.getData(request, "folderAndMailsList");
        for (Map folderMap : folders) {
            String folderId = (String) folderMap.get("folderId");
            String folderType = (String) folderMap.get("folderType");
            String folderSize = (String) folderMap.get("countMails");
            String unreadSize = (String) folderMap.get("unReadMails");

            if (WebMailConstants.FOLDER_INBOX.equals(folderType)) {
                systemFolder.put("inboxId", folderId);
                systemFolder.put("inboxSize", folderSize);
                systemFolder.put("unreadSize", unreadSize);
            }
            if (WebMailConstants.FOLDER_SENDITEMS.equals(folderType)) {
                systemFolder.put("sentId", folderId);
                systemFolder.put("sentSize", folderSize);
            }
            if (WebMailConstants.FOLDER_TRASH.equals(folderType)) {
                systemFolder.put("trashId", folderId);
                systemFolder.put("trashSize", folderSize);
                systemFolder.put("trashUnreadSize", unreadSize);
            }
            if (WebMailConstants.FOLDER_DRAFTITEMS.equals(folderType)) {
                systemFolder.put("draftId", folderId);
                systemFolder.put("draftSize", folderSize);
            }
            if (WebMailConstants.FOLDER_OUTBOX.equals(folderType)) {
                systemFolder.put("outboxId", folderId);
                systemFolder.put("outboxSize", folderSize);
            }
        }

        return systemFolder;
    }

    /**
     * Get column show types to configure mail tray list view
     *
     * @param servletRequest servletRequest
     * @return List
     */
    public static List getColumnToShowTypes(ServletRequest servletRequest) {
        List result = new ArrayList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.folder.show.from"), WebMailConstants.ColumnToShow.FROM.getConstantAsString()));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.folder.show.to"), WebMailConstants.ColumnToShow.TO.getConstantAsString()));
        result.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.folder.show.fromTo"), WebMailConstants.ColumnToShow.FROM_TO.getConstantAsString()));

        return result;
    }

    /**
     * Get label of column to show type
     *
     * @param constant       type
     * @param servletRequest servletRequest
     * @return String
     */
    public static String getColumnToShowLabel(String constant, ServletRequest servletRequest) {
        String label = "";

        for (Iterator iterator = getColumnToShowTypes(servletRequest).iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            if (labelValueBean.getValue().equals(constant)) {
                label = labelValueBean.getLabel();
                break;
            }
        }
        return label;
    }

    /**
     * Get column to show for this folder
     *
     * @param folderId
     * @return String
     */
    public static String getFolderColumnToShow(String folderId) {
        String showColumn = WebMailConstants.ColumnToShow.FROM.getConstantAsString();

        FolderCmd folderCmd = new FolderCmd();
        folderCmd.putParam("op", "read");
        folderCmd.putParam("folderId", folderId);
        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(folderCmd, null);
            if (!resultDto.isFailure()) {
                Object columnToShow = resultDto.get("columnToShow");
                if (columnToShow != null) {
                    showColumn = columnToShow.toString();
                }
            }
        } catch (AppLevelException e) {
            log.error("Error executing... ", e);
        }
        return showColumn;
    }

    public static String getEmailRecipientsAsString(String mailId, String type,
                                                    javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("mailId", mailId);
        if (null != type) {
            fantabulousUtil.addSearchParameter("type", type);
        }
        fantabulousUtil.setModule("/webmail");

        List<Map> recipients = fantabulousUtil.getData(request, "emailRecipientList");

        String result = "";
        for (int i = 0; i < recipients.size(); i++) {
            Map recipient = recipients.get(i);

            String email = (String) recipient.get("email");
            String personal = (String) recipient.get("personal");
            if (email.equals(personal)) {
                result += email;
            } else {
                result += personal + " <" + email + ">";
            }

            if (i < recipients.size() - 1) {
                result += "; ";
            }

        }
        return result;
    }

    public static String countEmails(String folderId, String stateConstant, HttpServletRequest request) {
        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("folderId", folderId);

        if (null != stateConstant) {
            fantabulousUtil.addSearchParameter("state", stateConstant);
        }

        List<Map> counteMap = fantabulousUtil.getData(request, "emailCounterList");
        if (counteMap.size() == 1) {
            Map result = counteMap.get(0);
            return (String) result.get("counter");
        }
        return "0";
    }

    public static List getEmailPriorities(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> priorities = new ArrayList<LabelValueBean>();
        priorities.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.defaultPriority"),
                WebMailConstants.MAIL_PRIORITY_DEFAULT));
        priorities.add(new LabelValueBean(JSPHelper.getMessage(request, "Webmail.highPriority"),
                WebMailConstants.MAIL_PRIORITY_HIGHT));
        return priorities;
    }

    public static List getFoldersForParent(javax.servlet.http.HttpServletRequest request, Integer userMailId, Integer folderId) {
        List res = new ArrayList();
        FolderCmd folderCmd = new FolderCmd();
        folderCmd.setOp("getCustomFolders");
        folderCmd.putParam("userMailId", userMailId);
        try {
            BusinessDelegate.i.execute(folderCmd, request);
        } catch (AppLevelException e) {
            log.error("Error in loading folders.... " + e.getMessage());
        }
        if (folderCmd.getResultDTO() != null && folderCmd.getResultDTO().containsKey("customFoldersList")) {
            List<HashMap> folders = (List<HashMap>) folderCmd.getResultDTO().get("customFoldersList");
            FolderTreeModel folderTreeModel = new FolderTreeModel(new DefaultMutableTreeNode());
            folderTreeModel.fillTree(folders);
            res = folderTreeModel.getDescendantFoldersList((DefaultMutableTreeNode) folderTreeModel.getRoot());
            if (folderId != null) {//Remove folder subtree
                FolderPojo actualFolder = new FolderPojo();
                actualFolder.setFolderId(folderId);
                DefaultMutableTreeNode actualNode = folderTreeModel.findNodeInTree(new DefaultMutableTreeNode(actualFolder));
                if (actualNode != null) {
                    actualNode.removeFromParent();
                    res = folderTreeModel.getDescendantFoldersList((DefaultMutableTreeNode) folderTreeModel.getRoot());
                }
            }
            res = folderTreeModel.sortList(res);
        }

        return (res);
    }

    public static String getFoldersAsXmlString(Integer userMailId, Integer companyId, PageContext pageContext, Integer folderId) {
        String res = "";
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());
        fantabulousUtil.addSearchParameter("userMailId", userMailId.toString());
        fantabulousUtil.setModule("/webmail");

        List<Map> foldersMapList = fantabulousUtil.getData(request, "folderAndMailsList");
        List<HashMap> sortListResults = new ArrayList<HashMap>();
        for (Map folderResultMap : foldersMapList) {
            HashMap folderMap = new HashMap(folderResultMap);

            //overwrite the unread count mails column with all counter mails only if thi is outbox folder
            if (WebMailConstants.FOLDER_OUTBOX.equals(folderMap.get("folderType"))) {
                String emailCounter = (String) folderMap.get("countMails");
                folderMap.put("unReadMails", emailCounter);
            }

            //sorting the folders
            if (WebMailConstants.FOLDER_DEFAULT.equals(folderMap.get("folderType"))) {
                sortListResults.add(folderMap);
            } else {
                sortListResults.add(0, folderMap);
            }
        }

        FolderTreeModel folderTreeModel = new FolderTreeModel(new DefaultMutableTreeNode());
        folderTreeModel.fillTree(sortListResults);
        FolderTreeUtil folderTreeUtil = new FolderTreeUtil(folderTreeModel);
        res = folderTreeUtil.getTreeAsUList(pageContext, folderId);
        //log.debug("generated tree.... " + res);

        return (res);
    }

    /**
     * foreign key validation to image store into email body
     *
     * @param emailBody body
     * @return ActionError
     */
    public static ActionError emailBodyImageStoreForeignKeyValidation(String emailBody) {
        ActionError actionError = null;

        List<Integer> imageStoreIdList = new ArrayList<Integer>();

        //filter html body by store image id
        ImageStoreByImgTagFilter imgTagFilter = new ImageStoreByImgTagFilter();
        HtmlEmailParser parser = new HtmlEmailDOMParser();
        parser.addFilter(imgTagFilter);
        try {
            parser.parseHtml(emailBody);
            imageStoreIdList = imgTagFilter.getImageStoreIdList();
        } catch (Exception e) {
            log.debug("filter store image IMG tags FAIL");
        }

        for (Integer imageStoreId : imageStoreIdList) {
            boolean existImageStore = ForeignkeyValidator.i.exists(WebMailConstants.TABLE_IMAGESTORE, "imagestoreid", imageStoreId);
            if (!existImageStore) {
                actionError = new ActionError("Webmail.body.imageStore.error");
                break;
            }
        }
        return actionError;
    }

    public static List<LabelValueBean> getPriorityValues(HttpServletRequest request) {
        List<LabelValueBean> res = new ArrayList<LabelValueBean>();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        res.add(new LabelValueBean("", ""));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.defaultPriority"), WebMailConstants.MAIL_PRIORITY_DEFAULT));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.highPriority"), WebMailConstants.MAIL_PRIORITY_HIGHT));
        return (res);
    }

    /**
     * Get a list of possible mail states (WebMailConstants.MAIL_ADVANCEDFILTER_)
     *
     * @param request Request
     * @return a LabelValue list with mail states
     */
    public static List<LabelValueBean> getMailStates(HttpServletRequest request) {
        List<LabelValueBean> res = new ArrayList<LabelValueBean>();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        res.add(new LabelValueBean("", ""));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.read"), WebMailConstants.MAIL_ADVANCEDFILTER_READ));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.unRead"), WebMailConstants.MAIL_ADVANCEDFILTER_UNREAD));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.answered"), WebMailConstants.MAIL_ADVANCEDFILTER_ANSWERED));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.notAnswered"), WebMailConstants.MAIL_ADVANCEDFILTER_UNANSWERED));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.forwarded"), WebMailConstants.MAIL_ADVANCEDFILTER_FORWARD));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.notForwarded"), WebMailConstants.MAIL_ADVANCEDFILTER_UNFORWARD));
        return (res);

    }

    /**
     * Get a list of values for mail attach states (Empty, yes, no)
     *
     * @param request Request
     * @return a LabelValue list with mail attach states
     */
    public static List<LabelValueBean> getAttachStates(HttpServletRequest request) {
        List<LabelValueBean> res = new ArrayList<LabelValueBean>();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        res.add(new LabelValueBean("", ""));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.withAttach"), WebMailConstants.MAIL_ADVANCEDFILTER_WITHATTACH));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.tray.withoutAttach"), WebMailConstants.MAIL_ADVANCEDFILTER_WITHOUTATTACH));
        return (res);
    }

    /**
     * Get a list of values for mail is related to communications (Empty, yes, no)
     *
     * @param request Request
     * @return a LabelValue list with mail is related to communications options
     */
    public static List<LabelValueBean> getCommunicationStates(HttpServletRequest request) {
        List<LabelValueBean> res = new ArrayList<LabelValueBean>();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        res.add(new LabelValueBean("", ""));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.yes"), WebMailConstants.MAIL_ADVANCEDFILTER_WITHCOMMUNICATION));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Common.no"), WebMailConstants.MAIL_ADVANCEDFILTER_WITHOUTCOMMUNICATION));
        return (res);
    }

    /**
     * Gets the incoming/outgoing labelValues for a select
     *
     * @param request Request
     * @return A list with the LabelValueBeans
     */
    public static List<LabelValueBean> getIncOutValues(HttpServletRequest request) {
        List<LabelValueBean> res = new ArrayList<LabelValueBean>();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        res.add(new LabelValueBean("", ""));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.advancedSearch.in"), String.valueOf(WebMailConstants.IN_VALUE)));
        res.add(new LabelValueBean(JSPHelper.getMessage(locale, "Webmail.advancedSearch.out"), String.valueOf(WebMailConstants.OUT_VALUE)));
        return (res);
    }

    /**
     * Invoque to <code>MailAccountCmd</code> command with <code>opeation='isDuplicatedPOPAccont'</code> to
     * validate duplicated pop accounts for the same user.
     *
     * @param popUser       <code>String</code> object that is the pop user.
     * @param popServer     <code>String</code> object that is the pop server name.
     * @param formOperation <code>String</code> object that is the form operation ( create or update ).
     * @param mailAccountId <code>Integer</code> object that is the account identifier, is used in update operations.
     * @param userMailId    <code>Integer</code> object that is email user identifier.
     * @param companyId     <code>Integer</code> object that is the company identifier.
     * @param request       <code>HttpServletRequest</code> object used to execute the command object.
     * @return true if exists another account for the user.
     */
    public static boolean isDuplicatedPOPAccont(String popUser,
                                                String popServer,
                                                String formOperation,
                                                Integer mailAccountId,
                                                Integer userMailId,
                                                Integer companyId,
                                                HttpServletRequest request) {

        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("isDuplicatedPOPAccont");
        mailAccountCmd.putParam("popUser", popUser);
        mailAccountCmd.putParam("popServer", popServer);
        mailAccountCmd.putParam("formOperation", formOperation);
        mailAccountCmd.putParam("mailAccountId", mailAccountId);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("companyId", companyId);

        boolean isDuplicatedPOPAccont = false;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            isDuplicatedPOPAccont = (Boolean) resultDTO.get("isDuplicatedPOPAccont");
        } catch (AppLevelException e) {
            log.error("Cannot execute MailAccountCmd ", e);
        }

        return isDuplicatedPOPAccont;
    }

    public static ResultMessage isAvailablePOPAccount(String popUser,
                                                      String popServer,
                                                      Integer userMailId,
                                                      Integer companyId,
                                                      HttpServletRequest request) {
        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("checkUserMailAcountAndServer");
        mailAccountCmd.putParam("login", popUser);
        mailAccountCmd.putParam("serverName", popServer);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            if (resultDTO.isFailure()) {
                Iterator iterator = resultDTO.getResultMessages();

                List<ResultMessage> resultMessages = new ArrayList<ResultMessage>();
                while (iterator.hasNext()) {
                    ResultMessage message = (ResultMessage) iterator.next();
                    resultMessages.add(message);
                }
                if (!resultMessages.isEmpty()) {
                    return resultMessages.get(0);
                }
            }
        } catch (AppLevelException e) {
            return null;
        }

        return null;
    }

    public static ResultMessage isAvailableSMTPAccount(String smtpUser,
                                                       String smtpServer,
                                                       Integer userMailId,
                                                       Integer companyId,
                                                       HttpServletRequest request) {
        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("isAvailableSMTPAccount");
        mailAccountCmd.putParam("smtpUser", smtpUser);
        mailAccountCmd.putParam("smtpServer", smtpServer);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            if (resultDTO.isFailure()) {
                Iterator iterator = resultDTO.getResultMessages();

                List<ResultMessage> resultMessages = new ArrayList<ResultMessage>();
                while (iterator.hasNext()) {
                    ResultMessage message = (ResultMessage) iterator.next();
                    resultMessages.add(message);
                }
                if (!resultMessages.isEmpty()) {
                    return resultMessages.get(0);
                }
            }
        } catch (AppLevelException e) {
            return null;
        }

        return null;
    }

    public static ResultMessage popServerConnection(String popUser,
                                                    String popServer,
                                                    String password,
                                                    String popPort,
                                                    Integer useSSLConnection,
                                                    Integer userMailId,
                                                    Integer mailAccountId,
                                                    String formOperation,
                                                    HttpServletRequest request) {
        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("checkEmailAccount");
        mailAccountCmd.putParam("login", popUser);
        mailAccountCmd.putParam("password", password);
        mailAccountCmd.putParam("serverName", popServer);
        mailAccountCmd.putParam("serverPort", popPort);
        mailAccountCmd.putParam("userSSLConnection", useSSLConnection);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("mailAccountId", mailAccountId);
        mailAccountCmd.putParam("validationOperation", formOperation);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            if (resultDTO.isFailure()) {
                Iterator iterator = resultDTO.getResultMessages();
                List<ResultMessage> resultMessages = new ArrayList<ResultMessage>();
                while (iterator.hasNext()) {
                    ResultMessage message = (ResultMessage) iterator.next();
                    resultMessages.add(message);
                }
                if (!resultMessages.isEmpty()) {
                    return resultMessages.get(0);
                }
            }
        } catch (AppLevelException e) {
            return null;
        }

        return null;
    }

    public static WebMailConstants.EmailAccountErrorType smtpServerConnection(String email,
                                                                              String smtpUser,
                                                                              String smtpPassword,
                                                                              String smtpServer,
                                                                              String smtpPort,
                                                                              String formOperation,
                                                                              Boolean smtpAuthentication,
                                                                              Integer userMailId,
                                                                              Integer mailAccountId,
                                                                              Integer smtpSSL,
                                                                              HttpServletRequest request) {
        String validationOperation = null;
        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        if ("create".equals(formOperation)) {
            validationOperation = "validateCreateSMTPAccount";
        }
        if ("update".equals(formOperation)) {
            validationOperation = "validateUpdateSMTPAccount";
        }
        mailAccountCmd.setOp(validationOperation);
        mailAccountCmd.putParam("email", email);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("mailAccountId", mailAccountId);
        mailAccountCmd.putParam("login", smtpUser);
        mailAccountCmd.putParam("password", smtpPassword);
        mailAccountCmd.putParam("smtpServer", smtpServer);
        mailAccountCmd.putParam("smtpPort", smtpPort);
        mailAccountCmd.putParam("smtpAuthentication", smtpAuthentication);
        mailAccountCmd.putParam("smtpSSL", smtpSSL);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            return (WebMailConstants.EmailAccountErrorType) resultDTO.get(validationOperation);
        } catch (AppLevelException e) {
            return null;
        }

    }

    /**
     * Invoque to <code>MailAccountCmd</code> command with <code>opeation='isDuplicatedSMTPAccount'</code> to
     * validate duplicated pop accounts for the same user.
     *
     * @param smtpUser      <code>String</code> object that is the smtp user.
     * @param smtpServer    <code>String</code> object that is the smtp server name.
     * @param formOperation <code>String</code> object that is the form operation ( create or update ).
     * @param mailAccountId <code>Integer</code> object that is the account identifier, is used in update operations.
     * @param userMailId    <code>Integer</code> object that is email user identifier.
     * @param companyId     <code>Integer</code> object that is the company identifier.
     * @param request       <code>HttpServletRequest</code> object used to execute the command object.
     * @return true if exists another account for the user.
     */
    public static boolean isDuplicatedSMTPAccount(String smtpUser,
                                                  String smtpServer,
                                                  String formOperation,
                                                  Integer mailAccountId,
                                                  Integer userMailId,
                                                  Integer companyId,
                                                  HttpServletRequest request) {

        MailAccountCmd mailAccountCmd = new MailAccountCmd();
        mailAccountCmd.setOp("isDuplicatedSMTPAccount");
        mailAccountCmd.putParam("smtpUser", smtpUser);
        mailAccountCmd.putParam("smtpServer", smtpServer);
        mailAccountCmd.putParam("formOperation", formOperation);
        mailAccountCmd.putParam("mailAccountId", mailAccountId);
        mailAccountCmd.putParam("userMailId", userMailId);
        mailAccountCmd.putParam("companyId", companyId);

        boolean isDuplicatedSMTPAccont = false;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(mailAccountCmd, request);
            isDuplicatedSMTPAccont = (Boolean) resultDTO.get("isDuplicatedSMTPAccount");
        } catch (AppLevelException e) {
            log.error("Cannot execute MailAccountCmd ", e);
        }

        return isDuplicatedSMTPAccont;
    }

    public static boolean hasDefaultMailAccount(HttpServletRequest request) {
        return getDefaultMailAccountId(request) != null;
    }

    /**
     * Get the configured default mail account id if exists
     * @param request
     * @return Integer
     */
    public static Integer getDefaultMailAccountId(HttpServletRequest request) {
        Integer mailAccountId = null;

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
                Map mailAccountDTO = (Map) resultDTO.get("defaultAccount");
                mailAccountId = (Integer) mailAccountDTO.get("mailAccountId");
            }
        } catch (AppLevelException e) {
            log.error("Cannot execute MailAccountCmd ", e);
        }

        return mailAccountId;
    }

    public static boolean hasMailContact(String mailContactCount, String mailInOut) {
        boolean result = false;
        if (!GenericValidator.isBlankOrNull(mailInOut)) {
            if (!GenericValidator.isBlankOrNull(mailContactCount) && Integer.valueOf(mailContactCount) > 0) {
                result = true;
            }
        }
        return result;
    }

    public static boolean allRecipientsWithMailContact(String mailContactCount, String recipientsCount, String mailInOut) {
        boolean result = false;
        Integer mailContacts = valueAsInteger(mailContactCount);
        Integer recipients = valueAsInteger(recipientsCount);

        if (String.valueOf(WebMailConstants.IN_VALUE).equals(mailInOut)) {
            if (mailContacts != null && mailContacts > 0) {
                result = true;
            }
        } else if (String.valueOf(WebMailConstants.OUT_VALUE).equals(mailInOut)) {
            if (mailContacts != null && recipients != null) {
                result = (mailContacts.intValue() == recipients.intValue());
            }
        }
        return result;
    }

    public static boolean isActionMailContact(String actionMailContactCount, String mailInOut) {
        boolean result = false;
        if (!GenericValidator.isBlankOrNull(mailInOut) && String.valueOf(WebMailConstants.OUT_VALUE).equals(mailInOut)) {
            Integer actionMailContacts = valueAsInteger(actionMailContactCount);
            if (actionMailContacts != null && actionMailContacts > 0) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isSupportActivityMailContact(String supportMailContactCount, String mailInOut) {
        boolean result = false;
        if (!GenericValidator.isBlankOrNull(mailInOut) && String.valueOf(WebMailConstants.OUT_VALUE).equals(mailInOut)) {
            Integer supportActivityContacts = valueAsInteger(supportMailContactCount);
            if (supportActivityContacts != null && supportActivityContacts > 0) {
                result = true;
            }
        }
        return result;
    }

    private static Integer valueAsInteger(String valueStr) {
        Integer valueInt = null;
        if (!GenericValidator.isBlankOrNull(valueStr)) {
            valueInt = Integer.valueOf(valueStr);
        }
        return valueInt;
    }

    public static boolean hasMailStateRead(String state) {
        return !GenericValidator.isBlankOrNull(state) && MailStateUtil.hasReadState(new Byte(state));
    }

    public static boolean hasMailStateAnswered(String state) {
        return !GenericValidator.isBlankOrNull(state) && MailStateUtil.hasAnsweredState(new Byte(state));
    }

    public static boolean hasMailStateForward(String state) {
        return !GenericValidator.isBlankOrNull(state) && MailStateUtil.hasForwardState(new Byte(state));
    }

    public static boolean hasMailStateSend(String state) {
        return !GenericValidator.isBlankOrNull(state) && MailStateUtil.hasSendState(new Byte(state));
    }

    public static List<AttachDTO> readMailTemporalAttachs(String tempMailId, HttpServletRequest request) {
        List<AttachDTO> resultList = new ArrayList<AttachDTO>();

        if (tempMailId != null) {

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

            ComposeTemporalMailCmd composeTemporalMailCmd = new ComposeTemporalMailCmd();
            composeTemporalMailCmd.setOp("readTempAttachs");
            composeTemporalMailCmd.putParam("tempMailId", tempMailId);
            composeTemporalMailCmd.putParam("companyId", companyId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(composeTemporalMailCmd, request);
                if (resultDTO.get("mailTemporalAttachList") != null) {
                    resultList = (List<AttachDTO>) resultDTO.get("mailTemporalAttachList");
                }
            } catch (AppLevelException e) {
                log.error("Cannot execute ComposeTemporalMailCmd..", e);
            }
        }
        return resultList;
    }

    public static List<Map> getTokenFieldFormRecipients(String tokenFieldType, DefaultForm defaultForm, HttpServletRequest request) {
        List<Map> resultMapList = new ArrayList<Map>();

        if (WebMailConstants.TokenFieldType.TO.getConstant().equals(tokenFieldType)) {
            if (request.getAttribute("toTokenFieldMapList") != null) {
                resultMapList = (List<Map>) request.getAttribute("toTokenFieldMapList");
            } else {
                resultMapList = parseDtoFormRecipientInTokenField(WebMailConstants.TokenFieldType.TO, defaultForm);
            }

        } else if (WebMailConstants.TokenFieldType.CC.getConstant().equals(tokenFieldType)) {
            if (request.getAttribute("ccTokenFieldMapList") != null) {
                resultMapList = (List<Map>) request.getAttribute("ccTokenFieldMapList");
            } else {
                resultMapList = parseDtoFormRecipientInTokenField(WebMailConstants.TokenFieldType.CC, defaultForm);
            }

        } else if (WebMailConstants.TokenFieldType.BCC.getConstant().equals(tokenFieldType)) {
            if (request.getAttribute("bccTokenFieldMapList") != null) {
                resultMapList = (List<Map>) request.getAttribute("bccTokenFieldMapList");
            } else {
                resultMapList = parseDtoFormRecipientInTokenField(WebMailConstants.TokenFieldType.BCC, defaultForm);
            }
        }

        return resultMapList;
    }

    private static List<Map> parseDtoFormRecipientInTokenField(WebMailConstants.TokenFieldType tokenFieldType, DefaultForm defaultForm) {
        List<Map> resultMapList = new ArrayList<Map>();

        if (defaultForm != null) {
            log.debug("Email to::::" + defaultForm.getDto("to"));
            log.debug("Email cc::::" + defaultForm.getDto("cc"));
            log.debug("Email bcc::::" + defaultForm.getDto("bcc"));

            //thi is set in read email cmd, here is info related with communications
            List<Map> recipientsWithContactList = (List<Map>) defaultForm.getDto("recipientsWithContact");

            if (WebMailConstants.TokenFieldType.TO.equals(tokenFieldType)) {
                String to = (String) defaultForm.getDto("to");
                resultMapList = parseRecipientsInTokenField(to, recipientsWithContactList);

            } else if (WebMailConstants.TokenFieldType.CC.equals(tokenFieldType)) {
                String cc = (String) defaultForm.getDto("cc");
                resultMapList = parseRecipientsInTokenField(cc, recipientsWithContactList);
            } else if (WebMailConstants.TokenFieldType.BCC.equals(tokenFieldType)) {
                String bcc = (String) defaultForm.getDto("bcc");
                resultMapList = parseRecipientsInTokenField(bcc, recipientsWithContactList);
            }
        }

        return resultMapList;
    }

    private static List<Map> parseRecipientsInTokenField(String recipientsToParse, List<Map> recipientsWithContactList) {
        List<Map> resultMapList = new ArrayList<Map>();

        if (recipientsToParse != null) {
            EmailRecipientHelper emailRecipientHelper = new EmailRecipientHelper();
            List<Map> parseRecipientList = emailRecipientHelper.getRecipients(recipientsToParse);

            for (int i = 0; i < parseRecipientList.size(); i++) {
                Map parseRecipientMap = parseRecipientList.get(i);
                String email = (String) parseRecipientMap.get(EmailRecipientHelper.RecipientKey.EMAIL.getKey());
                String contactName = (String) parseRecipientMap.get(EmailRecipientHelper.RecipientKey.PERSONAL_NAME.getKey());

                String addressId = "";
                String contactPersonOfId = "";

                if (recipientsWithContactList != null) {
                    for (Map recipientsWithContactMap : recipientsWithContactList) {

                        String emailContact = (String) recipientsWithContactMap.get(EmailRecipientHelper.RecipientKey.EMAIL.getKey());
                        List<Integer> addressIdList = (List<Integer>) recipientsWithContactMap.get(EmailRecipientHelper.RecipientKey.ADDRESS_ID.getKey());
                        List<Integer> contacpersonOfList = (List<Integer>) recipientsWithContactMap.get(EmailRecipientHelper.RecipientKey.CONTACT_PERSON_OF.getKey());

                        if (email.equals(emailContact)) {

                            if (addressIdList != null && !addressIdList.isEmpty()) {
                                addressId = addressIdList.get(0).toString();
                            }

                            if (contacpersonOfList != null && !contacpersonOfList.isEmpty()) {
                                contactPersonOfId = contacpersonOfList.get(0).toString();
                                contacpersonOfList.remove(0);
                            }
                            break;
                        }
                    }
                }


                Map tokenFieldMap = TokenFieldEmailRecipientHelper.composeTokenFieldMap(email, contactName, addressId, contactPersonOfId);
                resultMapList.add(tokenFieldMap);
            }
        }

        return resultMapList;
    }

}
