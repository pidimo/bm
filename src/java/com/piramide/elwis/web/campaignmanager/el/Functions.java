package com.piramide.elwis.web.campaignmanager.el;

import com.piramide.elwis.cmd.campaignmanager.*;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTime;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Jatun S.R.L.
 * campaign manager jstl functions
 *
 * @author Miky
 * @version $Id: Functions.java 10517 2015-02-26 01:45:04Z miguel $
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);
    //begin miky//

    /**
     * supervice the status of the responsible of an campaing contact
     *
     * @param userId
     * @param taskId
     * @param taskStatus
     * @param request
     * @return Map with keys 'title' and 'color'
     */
    public static Map getResponsibleStatus(String userId, String taskId, String taskStatus, HttpServletRequest request) {
        String title = "";
        String color = "";
        String red = "#FF0000";
        String yellow = "#FFFF00";
        String green = "#00FF00";
        String blue = "#0000FF";

        boolean hasUserId = !GenericValidator.isBlankOrNull(userId);
        boolean hasTaskId = !GenericValidator.isBlankOrNull(taskId);
        boolean hasTaskStatus = !GenericValidator.isBlankOrNull(taskStatus);

        if (hasUserId && hasTaskId && hasTaskStatus && taskStatus.equals(SchedulerConstants.CONCLUDED)) {
            color = blue;
            title = JSPHelper.getMessage(request, "Activity.campContact.responsibleStatus.blue");
        } else if (hasUserId && hasTaskId && hasTaskStatus && !taskStatus.equals(SchedulerConstants.CONCLUDED)) {
            color = green;
            title = JSPHelper.getMessage(request, "Activity.campContact.responsibleStatus.green");
        } else if (hasUserId && !hasTaskId) {
            color = yellow;
            title = JSPHelper.getMessage(request, "Activity.campContact.responsibleStatus.yellow");
        } else if (!hasUserId) {
            color = red;
            title = JSPHelper.getMessage(request, "Activity.campContact.responsibleStatus.red");
        }

        Map statusMap = new HashMap();
        statusMap.put("title", title);
        statusMap.put("color", color);
        return statusMap;
    }

    /**
     * verify if an campaing contact already has assigned an sales process
     *
     * @param contactId
     * @param activityId
     * @param servletRequest
     * @return boolean
     */
    public static boolean campaignContactHasSalesProcess(String contactId, String activityId, ServletRequest servletRequest) {
        boolean res;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CampaignContactSalesProcessReadCmd processReadCmd = new CampaignContactSalesProcessReadCmd();
        processReadCmd.putParam("addressId", contactId);
        processReadCmd.putParam("activityId", activityId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(processReadCmd, request);
            res = (Boolean) resultDTO.get("hasProcess");
        } catch (AppLevelException e) {
            //this will not happen never
            res = true;
        }
        return res;
    }

    /**
     * compose list of acttach as LabelValueBean
     *
     * @param data text with keys ($-$,$V$) i.e. '033102fight_1_prv.gif$V$81$-$convBurroTotalizer.rtf$V$82'
     * @return List
     */
    public static List composeAttachLabelValueBean(String data) {

        List result = new ArrayList();
        String[] arrayTupla = data.split(CampaignConstants.KEY_SEPARATOR);
        for (int i = 0; i < arrayTupla.length; i++) {
            String s = arrayTupla[i];
            if (!"".equals(s.trim())) {
                String[] tupla = s.split(CampaignConstants.KEY_SEPARATOR_VALUE);
                result.add(new LabelValueBean(tupla[0], tupla[1]));
            }
        }
        return result;
    }

    /**
     * get responsible type to generate an send mails
     *
     * @param request
     * @return list
     */
    public static List getResponsibleSendType(String internalCurrentUser, HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.activity.emailGenerate.activityResponsible"), CampaignConstants.ACTIVITY_RESPONSIBLE));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.activity.emailGenerate.contactResponsible"), CampaignConstants.CONTACT_RESPONSIBLE));

        if (internalCurrentUser != null && "true".equals(internalCurrentUser)) {
            list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.activity.emailGenerate.currentuser"), CampaignConstants.CURRENT_USER_SEND));
        }

        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * get sender employees for this activity
     *
     * @param activityId
     * @param request
     * @return list
     */
    public static List getSenderEmployeeList(String activityId, HttpServletRequest request) {
        ArrayList list = new ArrayList();
        User user = RequestUtils.getUser(request);

        ActivitySenderEmployeeReadCmd readCmd = new ActivitySenderEmployeeReadCmd();
        readCmd.putParam("activityId", activityId);
        readCmd.putParam("userId", user.getValue("userId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
            if (!resultDTO.isFailure()) {
                Integer activityRespEmployeeId = new Integer(resultDTO.get("activityEmployeeId").toString());
                Integer campaignRespEmployeeId = new Integer(resultDTO.get("campaignEmployeeId").toString());
                Integer currentUserAsEmployeeId = null;

                if (resultDTO.get("userEmployeeId") != null) {
                    currentUserAsEmployeeId = new Integer(resultDTO.get("userEmployeeId").toString());
                }

                list.add(new LabelValueBean(resultDTO.get("activityEmployeeName").toString(), activityRespEmployeeId.toString()));

                if (!campaignRespEmployeeId.equals(activityRespEmployeeId)) {
                    list.add(new LabelValueBean(resultDTO.get("campaignEmployeeName").toString(), campaignRespEmployeeId.toString()));
                }
                if (currentUserAsEmployeeId != null &&
                        !currentUserAsEmployeeId.equals(activityRespEmployeeId)
                        && !currentUserAsEmployeeId.equals(campaignRespEmployeeId)) {
                    list.add(new LabelValueBean(resultDTO.get("userEmployeeName").toString(), currentUserAsEmployeeId.toString()));
                }


            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }

        list = SortUtils.orderByProperty(list, "label");

        //add contact responsible sender
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.activity.emailGenerate.contactResponsible"), CampaignConstants.CONTACT_RESPONSIBLE));

        return list;
    }

    /**
     * get sender employee emails
     *
     * @param activityId
     * @param senderEmployeeId
     * @param telecomTypeId
     * @param request
     * @return list
     */
    public static List getSenderEmployeeEmails(String activityId, String senderEmployeeId, String telecomTypeId, HttpServletRequest request) {
        ArrayList resultList = new ArrayList();
        User user = RequestUtils.getUser(request);

        if (!GenericValidator.isBlankOrNull(senderEmployeeId) && !GenericValidator.isBlankOrNull(telecomTypeId)) {

            boolean senderIsContactResponsible = (CampaignConstants.CONTACT_RESPONSIBLE.equals(senderEmployeeId));

            ActivitySenderEmployeeEmailReadCmd emailReadCmd = new ActivitySenderEmployeeEmailReadCmd();
            emailReadCmd.putParam("senderEmployeeId", senderEmployeeId);
            emailReadCmd.putParam("telecomTypeId", telecomTypeId);
            emailReadCmd.putParam("userId", user.getValue("userId"));
            emailReadCmd.putParam("companyId", user.getValue("companyId"));
            if (senderIsContactResponsible) {
                emailReadCmd.putParam("activityId", activityId);
            }


            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(emailReadCmd, request);
                if (!resultDTO.isFailure()) {

                    List emailTelecomList = (List) resultDTO.get("emailTelecomList");
                    for (Iterator iterator = emailTelecomList.iterator(); iterator.hasNext();) {
                        Map telecomMap = (Map) iterator.next();

                        Map resultMap = new HashMap();
                        resultMap.put("label", telecomMap.get("telecomNumber"));
                        resultMap.put("value", telecomMap.get("telecomNumber"));
                        if (senderIsContactResponsible) {
                            resultMap.put("predetermined", "false");
                        } else {
                            resultMap.put("predetermined", telecomMap.get("predetermined"));
                        }

                        resultList.add(resultMap);
                    }

                    resultList = SortUtils.orderByPropertyMap(resultList, "label");

                    //add contact responsible email sender
                    if (senderIsContactResponsible) {
                        Map resultMap = new HashMap();
                        resultMap.put("label", JSPHelper.getMessage(request, "Campaign.activity.emailGenerate.AddressOfContactResponsible"));
                        resultMap.put("value", CampaignConstants.DEFAULT_ADDRESS_OF_CONTACTRESPONSIBLE);
                        resultMap.put("predetermined", "true");

                        resultList.add(resultMap);
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd..", e);
            }
        }

        return resultList;
    }

    /**
     * verify if the campaign has only one template with this document type
     *
     * @param campaignId
     * @param documentType
     * @return true or false
     */
    public static boolean campaignHasOnlyOneTemplate(String campaignId, String documentType) {
        boolean hasOnlyOneTemplate = false;
        CampaignTemplateReadByTypeCmd readByTypeCmd = new CampaignTemplateReadByTypeCmd();
        readByTypeCmd.putParam("campaignId", campaignId);
        readByTypeCmd.putParam("documentType", documentType);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readByTypeCmd, null);
            if (!resultDTO.isFailure()) {
                hasOnlyOneTemplate = resultDTO.getAsBool("isOnlyOneTemplate");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
        return hasOnlyOneTemplate;
    }

    /**
     * get users (current user, activity responsible, campaign responsible) to create task for activity responsibles
     *
     * @param request
     * @param activityId
     * @return list LabelValueBean
     */
    public static List getActivityCreateTaskUsers(String activityId, HttpServletRequest request) {
        ArrayList list = new ArrayList();
        User user = RequestUtils.getUser(request);

        ActivityTaskCreateUsersReadCmd readCmd = new ActivityTaskCreateUsersReadCmd();
        readCmd.putParam("activityId", activityId);
        readCmd.putParam("userId", user.getValue("userId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
            if (!resultDTO.isFailure()) {
                Integer activityRespUserId = new Integer(resultDTO.get("activityUserId").toString());
                Integer currentUserId = new Integer(resultDTO.get("currentUserId").toString());

                //add in result list
                list.add(new LabelValueBean(resultDTO.get("activityResponsibleName").toString(), activityRespUserId.toString()));
                if (!currentUserId.equals(activityRespUserId)) {
                    list.add(new LabelValueBean(resultDTO.get("currentUserName").toString(), currentUserId.toString()));
                }

                if (resultDTO.containsKey("campaignUserId") && resultDTO.get("campaignUserId") != null) {
                    Integer campaignRespUserId = new Integer(resultDTO.get("campaignUserId").toString());
                    if (!campaignRespUserId.equals(activityRespUserId) && !campaignRespUserId.equals(currentUserId)) {
                        list.add(new LabelValueBean(resultDTO.get("campaignResponsibleName").toString(), campaignRespUserId.toString()));
                    }
                } else {
                    list.add(new LabelValueBean(resultDTO.get("campaignResponsibleName").toString(), CampaignConstants.CAMPAIGNRESPONSIBLE_IS_NOT_VALIDUSER));
                }
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }
        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * get telecom types and replace all '"' characters existent in name of telecom type to '\"',
     * this is necessary to show as label in html elwis plugin
     *
     * @param servletRequest
     * @return List telecom types
     */
    public static List getTelecomTypesToHTMLElwisPlugin(ServletRequest servletRequest) {
        List resultList = new ArrayList();
        List telecomTypesList = com.piramide.elwis.web.contactmanager.el.Functions.getTelecomTypes(servletRequest);
        for (Iterator iterator = telecomTypesList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            labelValueBean.setLabel(labelValueBean.getLabel().replaceAll("\"", "\\\\\""));

            resultList.add(labelValueBean);
        }
        return resultList;
    }

    /**
     * get all telecom type translations and replace all '"' characters existent in name of telecom type to '\"',
     * this is necessary to show as label in html elwis plugin
     *
     * @param telecomTypeId
     * @param servletRequest
     * @return list
     */
    public static List getTelecomTypeAllTranslation(String telecomTypeId, ServletRequest servletRequest) {
        List resultList = new ArrayList();

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        Map systemLanguages = SystemLanguage.systemLanguages;
        for (Iterator iterator = systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String isoLanguage = (String) iterator.next();

            TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
            cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
            cmd.putParam("telecomTypeId", telecomTypeId);
            cmd.putParam("companyId", user.getValue("companyId"));
            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, isoLanguage);
            try {
                ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
                if (!resultDto.isFailure() && resultDto.get(TelecomTypeSelectCmd.RESULT) != null) {
                    TelecomTypeDTO dto = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
                    String telecomTypeName = dto.get("telecomTypeName").toString();
                    resultList.add(telecomTypeName.replaceAll("\"", "\\\\\""));
                }
            } catch (AppLevelException e) {
                log.error("Error executing", e);
            }
        }

        return resultList;
    }

    /**
     * get all resource translations as javascript array in string notation
     * this is to HTMLElwisPlugin, write "varTranslations" attribute
     *
     * @param resource
     * @return string
     */
    public static String writeResourceTranslationAsJSArray(String resource) {
        StringBuffer jsArray = new StringBuffer();
        jsArray.append(" varTranslations:[");

        Map systemLanguages = SystemLanguage.systemLanguages;
        for (Iterator iterator = systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String isoLanguage = (String) iterator.next();
            Locale locale = new Locale(isoLanguage);

            jsArray.append("\"")
                    .append(JSPHelper.getMessage(locale, resource))
                    .append("\"");

            if (iterator.hasNext()) {
                jsArray.append(", ");
            }
        }

        jsArray.append("]");
        return jsArray.toString();
    }

    /**
     * Read all activities of this campaign except el activity with this activity id
     *
     * @param campaignId campaign
     * @param activityId activioty that will be exclude
     * @param request
     * @return list LabelValueBean
     */
    public static List getActivitiesToCopyContacts(String campaignId, String activityId, HttpServletRequest request) {
        ArrayList list = new ArrayList();
        User user = RequestUtils.getUser(request);
        String userLocale = user.getValue("locale").toString();

        CopyContactCampaignActivitiesReadCmd readCmd = new CopyContactCampaignActivitiesReadCmd();
        readCmd.putParam("campaignId", campaignId);
        readCmd.putParam("activityId", activityId);
        readCmd.putParam("companyId", user.getValue("companyId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, request);
            if (!resultDTO.isFailure()) {
                List activityList = (List) resultDTO.get("activityList");
                for (Iterator iterator = activityList.iterator(); iterator.hasNext();) {
                    Map activityMap = (Map) iterator.next();
                    list.add(new LabelValueBean(JSPHelper.getMessage(new Locale(userLocale), "CampaignActivity.copyContacts.item", activityMap.get("title")), activityMap.get("activityId").toString()));
                }
            }
        } catch (AppLevelException e) {
            //this will not happen never
            log.debug("Error in execute cmd..", e);
        }
        return SortUtils.orderByProperty(list, "label");
    }

    /**
     * verify if to user responsible for this activity contact already created an task
     *
     * @param campContactId campaign activity contact id
     * @param campaignId
     * @return true or false
     */
    public static boolean activityUserHasTaskCreated(String campContactId, String campaignId) {
        boolean hasTaskCreated = false;
        if (!GenericValidator.isBlankOrNull(campContactId) && !GenericValidator.isBlankOrNull(campaignId)) {
            ActivityCampaignContactCmd activityContactCmd = new ActivityCampaignContactCmd();
            activityContactCmd.putParam("campaignContactId", campContactId);
            activityContactCmd.putParam("campaignId", campaignId);
            activityContactCmd.putParam("op", "read");

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(activityContactCmd, null);
                if (!resultDTO.isFailure()) {
                    if (resultDTO.containsKey("hasTask")) {
                        hasTaskCreated = Boolean.parseBoolean(resultDTO.get("hasTask").toString());
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }
        return hasTaskCreated;
    }

    /**
     * Verify if in this activity with this template already create communicatios
     *
     * @param activityId
     * @param templateId
     * @param request
     * @return errors
     */
    public static ActionErrors generationComunicationsAlreadyCreated(Integer activityId, Integer templateId, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        boolean commAlreadyCreated = true;

        User user = RequestUtils.getUser(request);
        Integer userId = new Integer(user.getValue("userId").toString());

        //calculate validate time
        int hours = 24;
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        dateTime = dateTime.minusHours(hours);
        log.debug("CHANGEG tieme:" + dateTime);

        ReadGeneratedCampaignInTimeCmd generatedInTimeCmd = new ReadGeneratedCampaignInTimeCmd();
        generatedInTimeCmd.putParam("activityId", activityId);
        generatedInTimeCmd.putParam("templateId", templateId);
        generatedInTimeCmd.putParam("generationTime", new Long(dateTime.getMillis()));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(generatedInTimeCmd, null);
            if (!resultDTO.isFailure()) {
                if (resultDTO.containsKey("alreadyGenerated")) {
                    commAlreadyCreated = resultDTO.getAsBool("alreadyGenerated");
                }
            }
            if (commAlreadyCreated) {
                errors.add("created", new ActionError("Campaign.activity.generation.alreadyCreateCommunication", resultDTO.get("templateDescription"), hours));
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }

        return errors;
    }

    /**
     * Verify if from Campaign Light with this template already create communicatios
     *
     * @param templateId
     * @param request
     * @return errors
     */
    public static ActionErrors generationComunicationsAlreadyCreated(Integer templateId, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        boolean commAlreadyCreated = true;

        //calculate validate time
        int hours = 24;
        DateTime dateTime = new DateTime(System.currentTimeMillis());
        dateTime = dateTime.minusHours(hours);
        log.debug("CHANGEG time:" + dateTime);

        ReadGeneratedCampaignInTimeCmd generatedInTimeCmd = new ReadGeneratedCampaignInTimeCmd();
        generatedInTimeCmd.putParam("op", "campaignLightGenerationTime");
        generatedInTimeCmd.putParam("templateId", templateId);
        generatedInTimeCmd.putParam("generationTime", new Long(dateTime.getMillis()));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(generatedInTimeCmd, null);
            if (!resultDTO.isFailure()) {
                if (resultDTO.containsKey("alreadyGenerated")) {
                    commAlreadyCreated = resultDTO.getAsBool("alreadyGenerated");
                }
            }
            if (commAlreadyCreated) {
                errors.add("created", new ActionError("Campaign.light.generation.alreadyCreateCommunication", resultDTO.get("templateDescription"), hours));
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }

        return errors;
    }

    /**
     * verify if this communication is created from campaign generation
     *
     * @param contactId
     * @return true or false
     */
    public static boolean isCampaignGenerationCommunication(String contactId) {
        log.debug("Is camapign gen communication....:" + contactId);
        boolean isCampGenCommunication = false;

        if (!GenericValidator.isBlankOrNull(contactId)) {
            CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
            campGenCommunicationCmd.putParam("contactId", contactId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(campGenCommunicationCmd, null);
                if (!resultDTO.isFailure()) {
                    if (resultDTO.containsKey("isFromGeneration")) {
                        isCampGenCommunication = resultDTO.getAsBool("isFromGeneration");
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }
        return isCampGenCommunication;
    }

    /**
     * verify if this communication is created from campaign generation
     * and get campaignId and campaign name
     *
     * @param contactId
     * @return Map
     */
    public static Map getGenerationCampaignInfo(String contactId) {
        Map campaignInfoMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(contactId)) {
            CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
            campGenCommunicationCmd.putParam("contactId", contactId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(campGenCommunicationCmd, null);
                if (!resultDTO.isFailure()) {
                    if (campGenCommunicationCmd.isCampaignGenerationCommunication()) {
                        campaignInfoMap.put("campaignId", resultDTO.get("campaignId"));
                        campaignInfoMap.put("campaignName", resultDTO.get("campaignName"));
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }
        return campaignInfoMap;
    }

    /**
     * Get document generation order by columns
     *
     * @param request
     * @return list
     */
    public static List getDocumentGenerationOrderBy(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.contactName"), CampaignConstants.DocOrderedColumn.CONTACT_NAME.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.contactPersonName"), CampaignConstants.DocOrderedColumn.CONTACTPERSON_NAME.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.country"), CampaignConstants.DocOrderedColumn.COUNTRY.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.city"), CampaignConstants.DocOrderedColumn.CITY.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.zip"), CampaignConstants.DocOrderedColumn.ZIP.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.orderBy.streetHouseNumber"), CampaignConstants.DocOrderedColumn.STREET_HOUSENUMBER.getConstantAsString()));

        return list;
    }

    /**
     * get order type as list (ascending, descending)
     *
     * @param request
     * @return list
     */
    public static List getDocumentColumnOrderType(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.ascendingOrder"), CampaignConstants.ASCENDING_ORDER));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, "Campaign.docGenerate.descendingOrder"), CampaignConstants.DESCENDING_ORDER));
        return list;
    }

    /**
     * Get has email campaign additional criteria list
     * @param request
     * @return
     */
    public static List getHasHasNotEmailCriteria(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean("", CampaignConstants.HasHasNotEmailCriteria.EMPTY.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.HasHasNotEmailCriteria.HASEMAIL.getResource()), CampaignConstants.HasHasNotEmailCriteria.HASEMAIL.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.HasHasNotEmailCriteria.HASNOTEMAIL.getResource()), CampaignConstants.HasHasNotEmailCriteria.HASNOTEMAIL.getConstantAsString()));
        return list;
    }

    /**
     * List of Sender prefix types
     * @param request
     * @return List
     */
    public static List getSenderPrefixType(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.SenderPrefixType.NOPREFIX.getResource()), CampaignConstants.SenderPrefixType.NOPREFIX.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.SenderPrefixType.SENDERNAME.getResource()), CampaignConstants.SenderPrefixType.SENDERNAME.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.SenderPrefixType.MAILACCOUNTPREFIX.getResource()), CampaignConstants.SenderPrefixType.MAILACCOUNTPREFIX.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, CampaignConstants.SenderPrefixType.DEFINEDBYUSER.getResource()), CampaignConstants.SenderPrefixType.DEFINEDBYUSER.getConstantAsString()));
        return list;
    }

    /**
     * Get the relation field of this CampaignCriterionValue
     * @param campCriterionValueId
     * @return String
     */
    public static String getCampaignCriterionValueRelationField(String campCriterionValueId) {
        String relationField = null;

        if (!GenericValidator.isBlankOrNull(campCriterionValueId)) {
            CampaignCriterionValueReadCmd readCmd = new CampaignCriterionValueReadCmd();
            readCmd.putParam("campCriterionValueId", Integer.valueOf(campCriterionValueId));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, null);
                if (!resultDTO.isFailure()) {
                    if (resultDTO.get("relationField") != null) {
                        relationField = resultDTO.get("relationField").toString();
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }
        return relationField;
    }

    /**
     * COmpose static value for 'relation exist' criteria operator
     * @param campCriterionValueId
     * @param request
     * @return String
     */
    public static String composeRelationExistsOperatorValue(String campCriterionValueId, HttpServletRequest request) {
        String value = "";
        if (!GenericValidator.isBlankOrNull(campCriterionValueId)) {
            CampaignCriterionValueReadCmd readCmd = new CampaignCriterionValueReadCmd();
            readCmd.putParam("campCriterionValueId", Integer.valueOf(campCriterionValueId));

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(readCmd, null);
                if (!resultDTO.isFailure()) {
                    if (resultDTO.get("descriptionKey") != null) {
                        value = JSPHelper.getMessage(request, "CampaignCriteria.relationExists.to") + " " + JSPHelper.getMessage(request, resultDTO.get("descriptionKey").toString());
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd...", e);
            }
        }
        return value;
    }

    public static boolean existCampaignSentLogFailedRecipients(String campaignSentLogId) {
        Boolean existFailedRecipients = false;

        SentLogContactCmd sentLogContactCmd = new SentLogContactCmd();
        sentLogContactCmd.putParam("op", "checkForRetryOp");
        sentLogContactCmd.putParam("campaignSentLogId", campaignSentLogId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(sentLogContactCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("isRetryCampaign")) {
                existFailedRecipients = (Boolean) resultDTO.get("isRetryCampaign");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
        return existFailedRecipients;
    }

    public static String getCampaignSentLogSummary(String campaignSentLogId) {
        String summary = "0/0";

        SentLogContactCmd sentLogContactCmd = new SentLogContactCmd();
        sentLogContactCmd.putParam("op", "campaignSentLogSummary");
        sentLogContactCmd.putParam("campaignSentLogId", campaignSentLogId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(sentLogContactCmd, null);
            if (!resultDTO.isFailure()) {
                summary = resultDTO.get("totalSuccess") + "/" + resultDTO.get("totalContacts");
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
        return summary;
    }

    public static Integer getCampaignGenerationRecipients(String campaignId, String activityId) {
        Integer totalRecipients = 0;

        String op = "recipients";
        if (!GenericValidator.isBlankOrNull(activityId)) {
            op = "actRecipients";
        }

        CampaignGenerateUtilCmd utilCmd = new CampaignGenerateUtilCmd();
        utilCmd.putParam("op", op);
        utilCmd.putParam("campaignId", campaignId);
        utilCmd.putParam("activityId", activityId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(utilCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("countRecipients")) {
                totalRecipients = Integer.valueOf(resultDTO.get("countRecipients").toString());
            }
        } catch (AppLevelException e) {
            log.debug("Error in execute cmd...", e);
        }
        return totalRecipients;
    }

    //end miky//


}
