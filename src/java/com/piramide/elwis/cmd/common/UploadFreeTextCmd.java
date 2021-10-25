package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.CustomEncrypt;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.common.FileFreeText;
import com.piramide.elwis.domain.common.FileFreeTextHome;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;

/**
 * @author Tayes
 * @version $Id: UploadFreeTextCmd.java 10133 2011-08-12 20:00:43Z fernando $
 */
public class UploadFreeTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());
    private static final String ERR_ID_NULL = "0";
    private static final String ERR_ID_FIND = "1";
    private static final String ERR_CLOSE = "2";
    private static final String SUCCEED = "3";
    private static final String ERR_CONCURRENCE = "4";
    private static final String ERR_CANTUPLOAD = "5";
    private static final String ERR_INVALIDLOGIN = "6";
    private static final String ERR_INVALIDCOMPANY = "7";
    private static final String ERR_INACTIVECOMPANY = "8";
    private static final String ERR_INVALIDDATA = "9";
    private static final String ERR_UPDATE_TELECOMS = "10";
    private static final String MSG_FIELDS_UPTODATE = "11";

    private String telecom_status;
    private Integer companyId;

    private boolean isValidCompany(String companyLogin) {
        try {
            CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
            Company company = companyHome.findByName(companyLogin);
            telecom_status = company.getTelecomTypeStatus();

            companyId = company.getCompanyId();
            if (!company.getActive().booleanValue()) {
                response(ERR_INACTIVECOMPANY);
                return false;
            }
        } catch (FinderException e) {
            response(ERR_INVALIDCOMPANY);
            return false;
        }
        return true;
    }

    private String xmlResponse() {
        StringBuffer xml = new StringBuffer();
        xml.append("[").append(ERR_UPDATE_TELECOMS).append("]\n");
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        try {
            Collection telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(companyId);
            xml.append(telecomXML(telecomTypes));
        } catch (FinderException e) {
            e.printStackTrace();
        }
        return xml.toString();
    }

    /**
     * @param defautlName
     * @param companyPrefix
     * @param employeePrefix
     * @param contactPrefix
     * @param telecomTypeId
     * @param langTextId
     * @param iso
     * @param xml
     * @param langTextHome   TODO: Hay que cambiar esto para que soporte mas telecoms, por ahora por cada nuevo telecom hay que
     *                       anadir un nuevo parametro
     */
    private void addTelecomResource(String defautlName, String companyPrefix, String employeePrefix, String contactPrefix,
                                    Integer telecomTypeId, Integer langTextId, String iso, StringBuffer xml,
                                    LangTextHome langTextHome) {
        String resource;
        try {
            LangText langText = langTextHome.findByLangTextIdAndLanguageRelatedToUI(langTextId, iso);
            resource = langText.getText();
        } catch (FinderException e) {
            resource = defautlName;
        }

        //encode label name
        resource = encodeText(resource);

        /**
         * TODO: The following line quotes \ and $ in order to avoid error in the math, but the problem is  that after that the \ character keeps as \\ and $ as \$
         */
        resource = Matcher.quoteReplacement(resource);


        xml.append("<resource key=\"btn").append(telecomTypeId).append("\">").append(resource).append("</resource>\n");
        //resource = prefix.replaceAll("{0}", resource).replace(' ', '_');
        xml.append("<resource key=\"Company_tel_").append(telecomTypeId).append("\">").append(companyPrefix.replaceAll("<T>", resource).replace(' ', '_')).append("</resource>\n");
        xml.append("<resource key=\"Employee_tel_").append(telecomTypeId).append("\">").append(employeePrefix.replaceAll("<T>", resource).replace(' ', '_')).append("</resource>\n");
        // Telecoms for Addresss
        xml.append("<resource key=\"Address_tel_").append(telecomTypeId).append("\">").append(contactPrefix.replaceAll("<T>", resource).replace(' ', '_')).append("</resource>\n");
    }


    private String encodeText(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\"", "&quot;");
        text = text.replaceAll("\'", "&apos;");
        return text;
    }

    private void addTelecom(String fieldName, Integer telecomTypeId, StringBuffer xml) {
        xml.append("<subcontrol type=\"button\" >\n");
        xml.append("<gui  key=\"btn").append(telecomTypeId).append("\" tooltip=\"\" faceid=\"464\" style=\"icon-caption\" />\n");
        xml.append("<operation name=\"addField\">\n");
        xml.append("<parameters>\n");
        xml.append("<parameter name=\"field\" value=\"").append(fieldName).append(telecomTypeId).append("\"/>");
        xml.append("</parameters>\n");
        xml.append("</operation>\n");
        xml.append("</subcontrol>\n");
    }

    private StringBuffer telecomXML(Collection telecomTypes) {
        String[] resourcesEN = (String[]) paramDTO.get("resourcesEN");
        String[] resourcesES = (String[]) paramDTO.get("resourcesES");
        String[] resourcesDE = (String[]) paramDTO.get("resourcesDE");
        String[] resourcesFR = (String[]) paramDTO.get("resourcesFR");

        StringBuffer xmlResources_en = new StringBuffer("<elwis-resources>\n");
        StringBuffer xmlResources_de = new StringBuffer("<elwis-resources>\n");
        StringBuffer xmlResources_es = new StringBuffer("<elwis-resources>\n");
        StringBuffer xmlResources_fr = new StringBuffer("<elwis-resources>\n");

        StringBuffer xmlCompany = new StringBuffer();
        StringBuffer xmlEmployee = new StringBuffer();
        StringBuffer xmlAddress = new StringBuffer();

        xmlCompany.append("<*><elwis-config>\n");

        xmlCompany.append("<telecom name=\"company\">\n")
                .append("\t<subcontrol type=\"popup\">\n")
                .append("\t\t<gui  key=\"btncompany_telecoms\" tooltip=\"\" faceid=\"464\" style=\"icon-caption\" />\n");
        xmlEmployee.append("<telecom name=\"employee\">\n")
                .append("\t<subcontrol type=\"popup\">\n")
                .append("\t\t<gui  key=\"btnemployee_telecoms\" tooltip=\"\" faceid=\"464\" style=\"icon-caption\" />\n");
        xmlAddress.append("<telecom name=\"address\">\n")
                .append("\t<subcontrol type=\"popup\">\n")
                .append("\t\t<gui  key=\"btnaddress_telecoms\" tooltip=\"\" faceid=\"464\" style=\"icon-caption\" />\n");

        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext(); ) {
            com.piramide.elwis.domain.contactmanager.TelecomType telecomType = (com.piramide.elwis.domain.contactmanager.TelecomType) iterator.next();
            addTelecom("Company_tel_", telecomType.getTelecomTypeId(), xmlCompany);
            addTelecom("Employee_tel_", telecomType.getTelecomTypeId(), xmlEmployee);
            addTelecom("Address_tel_", telecomType.getTelecomTypeId(), xmlAddress);
            String telecomLabel = telecomType.getTelecomTypeName();
            addTelecomResource(telecomLabel, resourcesEN[0], resourcesEN[1], resourcesEN[2],
                    telecomType.getTelecomTypeId(), telecomType.getLangTextId(),
                    "en", xmlResources_en, langTextHome);
            addTelecomResource(telecomLabel, resourcesES[0], resourcesES[1], resourcesES[2],
                    telecomType.getTelecomTypeId(), telecomType.getLangTextId(),
                    "es", xmlResources_es, langTextHome);
            addTelecomResource(telecomLabel, resourcesDE[0], resourcesDE[1], resourcesDE[2],
                    telecomType.getTelecomTypeId(), telecomType.getLangTextId(),
                    "de", xmlResources_de, langTextHome);
            addTelecomResource(telecomLabel, resourcesFR[0], resourcesFR[1], resourcesFR[2],
                    telecomType.getTelecomTypeId(), telecomType.getLangTextId(),
                    "fr", xmlResources_fr, langTextHome);

        }
        xmlResources_en.append("</elwis-resources>\n");
        xmlResources_es.append("</elwis-resources>\n");
        xmlResources_de.append("</elwis-resources>\n");
        xmlResources_fr.append("</elwis-resources>\n");
        xmlCompany.append("</subcontrol>\n</telecom>\n");
        xmlEmployee.append("</subcontrol>\n</telecom>\n");
        xmlAddress.append("</subcontrol>\n</telecom>\n");

        // Link the xml
        xmlCompany.append(xmlEmployee).append(xmlAddress)
                .append("</elwis-config>\n")
                .append("\n<===>")
                .append(xmlResources_en)
                .append("\n<===>")
                .append(xmlResources_es)
                .append("\n<===>")
                .append(xmlResources_de)
                .append("\n<===>")
                .append(telecom_status)
                .append("\n<===>")
                .append(xmlResources_fr)
                .append("\n<===>");
        return xmlCompany;
    }


    private void verifyUpload() throws Exception {
        String company = paramDTO.getAsString("company");
        //boolean isUpdateFields = paramDTO.getAsBool("updateFields");
        if (isValidCompany(company)) {
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            try {
                User user = userHome.findUserWithinCompanyLogon(paramDTO.getAsString("login"),
                        EncryptUtil.i.encryt(paramDTO.getAsString("password")), company);

                if ("operationUpdate".equals(paramDTO.getAsString("uploadOperation"))) {
                    if (!paramDTO.getAsString("telecomsKey").equals(telecom_status)) {
                        telecomsResponse();
                    } else {
                        response(MSG_FIELDS_UPTODATE);
                        return;
                    }
                } else {

                    String decode = CustomEncrypt.i.decode(paramDTO.getAsString("code"));

                    Integer type = new Integer(decode.substring(0, decode.indexOf(':')));

                    String moduleCode = "";
                    switch (type.intValue()) {
                        case FreeTextTypes.FREETEXT_CONTACT:
                            moduleCode = "COMMUNICATION";
                            break;
                        case FreeTextTypes.FREETEXT_TEMPLATE_TEXT:
                            moduleCode = "TEMPLATE";
                            break;
                        case FreeTextTypes.FREETEXT_CAMPAIGN_TEXT:
                            moduleCode = "CAMPAIGNTEMPLATE";
                            break;
                        default: {
                            response(ERR_INVALIDDATA);
                            return;
                        }
                    }

                    SystemFunctionHome systemFunctionHome = (SystemFunctionHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_SYSTEMFUNCTION);
                    SystemFunction systemFunction = systemFunctionHome.findByCode(moduleCode);

                    boolean canUpload = false;
                    for (Iterator iterator = user.getUserRole().iterator(); iterator.hasNext(); ) {
                        UserRole userRole = (UserRole) iterator.next();

                        Role role = userRole.getRole();
                        AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
                        try {
                            AccessRights accessRights = accessRightsHome.findByFunctionAndRole(systemFunction.getFunctionId(), role.getRoleId());
                            log.debug("AccessRight:" + accessRights.getActive() + "Permision:" + accessRights.getPermission());
                            if (accessRights.getActive().booleanValue() &&
                                    PermissionUtil.hasAccessRight(accessRights.getPermission(), PermissionUtil.UPDATE)) {
                                log.debug("Cant Upload!!!");
                                canUpload = true;
                                // Verify for update the telecoms fields
                                if (!moduleCode.equals("COMMUNICATION") && !paramDTO.getAsString("telecomsKey").equals(telecom_status)) {
                                    telecomsResponse(); // Send ERROR CODE and data xml's
                                } else {
                                    response(SUCCEED);
                                }
                                break;
                            }
                        } catch (FinderException e) {
                        }
                    }
                    if (!canUpload) {
                        response(ERR_CANTUPLOAD);
                    }
                }
            } catch (ServiceUnavailableException e) {

            } catch (FinderException e) {
                response(ERR_INVALIDLOGIN);
            }
        }
    }

    private void uploadFile() {
        InputStream inputStream = (InputStream) paramDTO.get("file");
        try {
            String decode = CustomEncrypt.i.decode(paramDTO.getAsString("code"));
            log.debug("DECODE:" + decode);
            Integer type, id, version;
            int pos = decode.indexOf(':');
            type = new Integer(decode.substring(0, pos));
            int pos1 = decode.indexOf('-', pos);
            id = new Integer(decode.substring(pos + 1, pos1));
            version = new Integer(decode.substring(pos1 + 1));

            log.debug("ID" + id + " - Type:" + type + " - Version:" + version);
            FileFreeTextHome fileHome = (FileFreeTextHome) EJBFactory.i.getEJBLocalHome(Constants.JNDI_FILEFREETEXT);
            FileFreeText fileFreeText = fileHome.findByPrimaryKeyAndType(id, type);

            byte[] file = new byte[inputStream.available()];
            inputStream.read(file);
            log.debug("VERSION SEND DOC:" + version);
            log.debug("VERSION Contact:" + fileFreeText.getVersion());
            if (!version.equals(fileFreeText.getVersion())) {
                response(ERR_CONCURRENCE);
                return;
            }
            fileFreeText.setVersion(new Integer(version.intValue() + 1));

            log.debug("Can upload file");
            fileFreeText.setValue(file);
            response(SUCCEED);
        } catch (NumberFormatException e) {
            response(ERR_ID_NULL);
        } catch (FinderException e) {
            response(ERR_ID_FIND);
            log.debug("Dont'n exist the file");
        } catch (IOException e) {
            response(ERR_ID_FIND);
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            response(ERR_ID_NULL);
        }
    }
    // TODO: La seguridad no es muy buena, lo mejor seria que solo haga una peticion, donde tambien se sube el archivo

    public void executeInStateless(SessionContext ctx) {
        if ("login".equals(paramDTO.getAsString("op"))) {
            try {
                verifyUpload();
            } catch (Exception e) {
                e.printStackTrace();
                response(ERR_ID_NULL);
            }
        } else {
            uploadFile();
        }
    }

    private void telecomsResponse() {
        resultDTO.put("response", xmlResponse());
    }

    private void response(String msg) {
        resultDTO.put("response", "[" + msg + "]");
    }

    public boolean isStateful() {
        return false;
    }
}