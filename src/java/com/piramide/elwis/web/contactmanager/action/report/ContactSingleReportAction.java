package com.piramide.elwis.web.contactmanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.contactmanager.ReadAddressCategoryValuesCmd;
import com.piramide.elwis.cmd.contactmanager.ReadAddressTelecomNumberCmd;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.InputStream;
import java.util.*;

/**
 * Jatun Team
 *
 * @author Alvaro
 * @version $Id: ContactSingleReportAction, 11-jun-2008 11:49:40
 */
public class ContactSingleReportAction extends ReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContactSingleReportAction..............");
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionForward actionForward = null;

        if (searchForm.getParams().get("generate") != null) {

            ActionErrors errors = new ActionErrors();
            ReportActionUtil reportActionUtil = new ReportActionUtil();

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Locale locale = new Locale(user.getValue("locale").toString());
            DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
            ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());
            Integer companyId = new Integer(user.getValue(Constants.COMPANYID).toString());


            //Fantabulous list parameters
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(searchForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));
            //add addressId
            Integer addressId = new Integer((String) searchForm.getParameter("addressId_Selected"));
            Integer contactPersonId = null;
            boolean isContactPerson = false;
            if (searchForm.getParameter("contactPersonId_selected") != null && searchForm.getParameter("contactPersonId_selected").toString().length() > 0) {
                contactPersonId = new Integer((String) searchForm.getParameter("contactPersonId_selected"));
                parameters.addSearchParameter("addressId", addressId.toString());
                parameters.addSearchParameter("contactPersonId", contactPersonId.toString());
                isContactPerson = true;
                parameters.addSearchParameter("contactPersonTelecom", contactPersonId.toString()); //for telecoms
                parameters.addSearchParameter("addressIdTelecom", addressId.toString()); //for telecoms
                parameters.addSearchParameter("communicationContactPersonId", contactPersonId.toString()); //for communications
                parameters.addSearchParameter("communicationAddressId", addressId.toString()); //for communications
            } else {
                parameters.addSearchParameter("addressIdTelecom", addressId.toString());  //for telecoms
                parameters.addSearchParameter("addressId", addressId.toString()); //for the report
                parameters.addSearchParameter("communicationAddressId", addressId.toString()); //for communications
            }

            //read way description
            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", addressId);
            ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
            String wayDescription = (String) resultDTO.get("wayDescription");

            String reportFormat = searchForm.getReportFormat();
            String reportPageSize = searchForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT;
            String tempDirectory = reportActionUtil.getTempDirectory();
            HashMap<String, ReportData> subreports = new HashMap<String, ReportData>();

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);

            //Telecoms subreport
            String telecomTypeListName;
            InputStream templateInputStreamTelecoms;
            if (isContactPerson) {
                telecomTypeListName = "reportContactPersonTelecomTypeList";
                templateInputStreamTelecoms = request.getSession().getServletContext().getResourceAsStream(
                        "/common/contacts/jaspertemplates/ContactPersonTelecomsSubReport.jrxml");
            } else {
                telecomTypeListName = "reportContactTelecomTypeList";
                templateInputStreamTelecoms = request.getSession().getServletContext().getResourceAsStream(
                        "/common/contacts/jaspertemplates/AddressTelecomsSubReport.jrxml");
            }

            HashMap subReportTelecomsConfigParams = reportActionUtil.getReportConfig(templateInputStreamTelecoms, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACT_TELECOMS", true,
                    resourceBundle);

            //read telecoms numbers
            ReadAddressTelecomNumberCmd telecomNumberCmd = new ReadAddressTelecomNumberCmd();
            telecomNumberCmd.putParam("op", isContactPerson ? "isContactPerson" : "");
            telecomNumberCmd.putParam("companyId", companyId);
            telecomNumberCmd.putParam("addressId", addressId);
            telecomNumberCmd.putParam("contactPersonId", contactPersonId);
            telecomNumberCmd.putParam("isoLanguage", user.getValue("locale"));

            resultDTO = BusinessDelegate.i.execute(telecomNumberCmd, request);
            Map telecomNumbersMap = (Map<String, String>) resultDTO.get("addressNumberMap");
            reportParams.put(isContactPerson ? "NUMBERMAP" : "NUMBERMAP_ADDRESS", telecomNumbersMap);
            Map telecomTypeNamesMap = (Map<String, String>) resultDTO.get("telecomTypeNameMap");
            reportParams.put(isContactPerson ? "TELECOMTYPE_NAMEMAP" : "TELECOMTYPE_NAMEMAP_ADDRESS", telecomTypeNamesMap);

            ReportData subReportTelecomsData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportTelecomsConfigParams, reportParams,
                    telecomTypeListName, parameters);

            subreports.put("SUBREPORT_CONTACT_TELECOMS", subReportTelecomsData);

            //telecom numbers for contactPerson owne
            String telecomTypeListName_address = "reportContactTelecomTypeList";
            InputStream templateInputStreamTelecoms_address = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/AddressTelecomsSubReport.jrxml");

            HashMap subReportTelecomsConfigParams_address = reportActionUtil.getReportConfig(templateInputStreamTelecoms_address, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACT_TELECOMS", true,
                    resourceBundle);

            ReadAddressTelecomNumberCmd telecomNumberCmd_address = new ReadAddressTelecomNumberCmd();
            telecomNumberCmd_address.putParam("op", "");
            telecomNumberCmd_address.putParam("companyId", companyId);
            telecomNumberCmd_address.putParam("addressId", addressId);
            telecomNumberCmd_address.putParam("isoLanguage", user.getValue("locale"));

            resultDTO = BusinessDelegate.i.execute(telecomNumberCmd_address, request);
            Map telecomNumbersMap_address = (Map<String, String>) resultDTO.get("addressNumberMap");
            reportParams.put("NUMBERMAP_ADDRESS", telecomNumbersMap_address);
            Map telecomTypeNamesMap_address = (Map<String, String>) resultDTO.get("telecomTypeNameMap");
            reportParams.put("TELECOMTYPE_NAMEMAP_ADDRESS", telecomTypeNamesMap_address);

            ReportData subReportTelecomsData_address = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportTelecomsConfigParams_address, reportParams,
                    telecomTypeListName_address, parameters);
            subreports.put("SUBREPORT_CP_OWNER_TELECOMS", subReportTelecomsData_address);

            //contact person info subreport
            HashMap<String, ReportData> contactPersonSubreports = new HashMap<String, ReportData>();
            contactPersonSubreports.put("SUBREPORT_CONTACT_TELECOMS", subReportTelecomsData);

            InputStream templateInputStreamContactPersonInfo = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/ContactPersonInfoSubReportTemplate.jrxml");

            HashMap subReportContactPersonInfoConfigParams = reportActionUtil.getReportConfig(templateInputStreamContactPersonInfo, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACTPERSON_INFO", true,
                    resourceBundle);

            ReportData subReportContactPersonInfoData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContactPersonInfoConfigParams, reportParams,
                    "contactPersonInfoList", parameters,
                    contactPersonSubreports);
            subreports.put("SUBREPORT_CONTACTPERSON_INFO", subReportContactPersonInfoData);

            //category values sub report
            ReadAddressCategoryValuesCmd categoryValuesCmd = new ReadAddressCategoryValuesCmd();
            categoryValuesCmd.putParam("op", isContactPerson ? "isContactPerson" : "");
            categoryValuesCmd.putParam("companyId", companyId);
            categoryValuesCmd.putParam("addressId", addressId);
            categoryValuesCmd.putParam("contactPersonId", contactPersonId);

            resultDTO = BusinessDelegate.i.execute(categoryValuesCmd, request);
            List<Map> categoryValuesList = (List<Map>) resultDTO.get("listCategoryValues");
            reportParams.putAll(composeCategoryValuesParams(categoryValuesList, request));

            //add param to get category type
            if (isContactPerson) {
                parameters.addSearchParameter("tableId", ContactConstants.CONTACTPERSON_CATEGORY);
            } else {
                parameters.addSearchParameter("tableId", ContactConstants.ADDRESS_CATEGORY);
            }

            InputStream templateInputStreamCategoryValues = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/AddressCategoryValuesSubReport.jrxml");

            HashMap subReportCategoryValuesConfigParams = reportActionUtil.getReportConfig(templateInputStreamCategoryValues, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CATEGORYVALUES", true,
                    resourceBundle);
            ReportData subReportCategoryValuesData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportCategoryValuesConfigParams, reportParams,
                    "reportAddressCategoryList", parameters);
            subreports.put("SUBREPORT_CATEGORYVALUES", subReportCategoryValuesData);

            //Communications subreport
            InputStream templateInputStreamCommunications = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/ContactCommunicationsSubReportTemplate.jrxml");
            Parameters communicationsSubReportParams = new Parameters();
            communicationsSubReportParams.addSearchParameters(parameters.getSearchParameters());
            if (searchForm.getParameter("communicationsLimit") != null && searchForm.getParameter("communicationsLimit").toString().length() > 0) {
                communicationsSubReportParams.getPageParam().setPageNumber(1);
                Integer pageSize = new Integer(searchForm.getParameter("communicationsLimit").toString());
                communicationsSubReportParams.getPageParam().setPageSize(pageSize);
            }
            HashMap subReportCommunicationsConfigParams = reportActionUtil.getReportConfig(templateInputStreamCommunications, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACT_COMMUNICATIONS", true,
                    resourceBundle);
            ReportData subReportCommunicationsData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportCommunicationsConfigParams, reportParams,
                    "contactCommunicationList", communicationsSubReportParams);
            subreports.put("SUBREPORT_CONTACT_COMMUNICATIONS", subReportCommunicationsData);

            //contact person Communication subreport
            InputStream templateInputStreamContactPersonComm = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/ContactPersonCommunicationSubReport.jrxml");

            HashMap subReportContactPersonCommConfigParams = reportActionUtil.getReportConfig(templateInputStreamContactPersonComm, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACTPERSON_COMMUNICATION", true,
                    resourceBundle);
            ReportData subReportContactPersonCommData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContactPersonCommConfigParams, reportParams,
                    "contactCommunicationList", communicationsSubReportParams);
            subreports.put("SUBREPORT_CONTACTPERSON_COMMUNICATION", subReportContactPersonCommData);

            //ContactPersons subreport
            //add param to get active contact persons
            parameters.addSearchParameter("active", String.valueOf(1));

            InputStream templateInputStreamContactPersons = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/ContactContactPersonsReportTemplate.jrxml");

            HashMap subReportContactPersonsConfigParams = reportActionUtil.getReportConfig(templateInputStreamContactPersons, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "SUBREPORT_CONTACT_CONTACT_PERSONS", true,
                    resourceBundle);
            ReportData subReportContactPersonsData = reportActionUtil.generateSubReport(
                    getServlet(), request,
                    subReportContactPersonsConfigParams, reportParams,
                    "contactPersonList", parameters);
            subreports.put("SUBREPORT_CONTACT_CONTACT_PERSONS", subReportContactPersonsData);

            boolean showCommunications = "true".equals(searchForm.getParameter("showCommunications"));
            reportParams.put("SHOW_COMMUNICATIONS", Boolean.valueOf(showCommunications));

            //MasterReport
            InputStream templateInputStreamReceipt = request.getSession().getServletContext().getResourceAsStream(
                    "/common/contacts/jaspertemplates/ContactSingleReportTemplate.jrxml");

            HashMap reportConfigParams = reportActionUtil.getReportConfig(templateInputStreamReceipt, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "Contact_report", false,
                    resourceBundle);

            //Loading the company logo image..
            /**HashMap companyLogoInfo=reportActionUtil.getCompanLogoInfo(request,true);

             reportParams.put("IS_TITLE_IMAGE_STREAM", companyLogoInfo.get("IS_TITLE_IMAGE_STREAM"));
             reportParams.put("TITLE_IMAGE_STREAM", companyLogoInfo.get("TITLE_IMAGE_STREAM"));
             if(new Boolean(companyLogoInfo.get("IS_TITLE_IMAGE_STREAM").toString())){
             reportConfigParams.put("TITLE_IMAGE_WIDTH", companyLogoInfo.get("TITLE_IMAGE_WIDTH"));
             reportConfigParams.put("TITLE_IMAGE_HEIGHT", companyLogoInfo.get("TITLE_IMAGE_HEIGHT"));
             }**/
            //If is contact person
            if (isContactPerson) {
                reportParams.put("ISCONTACTPERSON", Boolean.TRUE);
            } else {
                reportParams.put("ISCONTACTPERSON", Boolean.FALSE);
            }

            //add way description as param
            reportParams.put("WAYDESCRIPTION", (wayDescription != null) ? wayDescription : "");
            reportConfigParams.put("reportTitleString", searchForm.getReportTitle());
            reportActionUtil.generateReport(getServlet(),
                    request, response,
                    reportConfigParams, reportParams,
                    "contactSingleReportList", parameters,
                    subreports);
            //End master report


        } else {
            actionForward = mapping.findForward("Success");
        }
        return (actionForward);
    }

    /**
     * Compose category values to send as param in report
     *
     * @param categoryValuesList list with info of catetory
     * @param request
     * @return Map
     */
    private Map composeCategoryValuesParams(List<Map> categoryValuesList, HttpServletRequest request) {
        Map reportParams = new HashMap();
        Map categoryNames = new HashMap();
        Map categoryValues = new HashMap();
        for (Map categoryMap : categoryValuesList) {
            String categoryId = categoryMap.get("categoryId").toString();
            Object value = categoryMap.get("value");
            if (CatalogConstants.CategoryType.DATE.getConstant().equals(categoryMap.get("type").toString())) {
                String datePattern = JSPHelper.getMessage(request, "datePattern");
                Date date = DateUtils.integerToDate(new Integer(value.toString()));
                value = DateUtils.parseDate(date, datePattern);
            }
            if (CatalogConstants.CategoryType.DECIMAL.getConstant().equals(categoryMap.get("type").toString())) {
                Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
                value = FormatUtils.formatingDecimalNumber(value, locale, 11, 2);
            }

            categoryNames.put(categoryId, categoryMap.get("name"));
            categoryValues.put(categoryId, value);
        }

        reportParams.put("CATEGORYNAMEMAP", categoryNames);
        reportParams.put("CATEGORYVALUEMAP", categoryValues);

        return reportParams;
    }
}
