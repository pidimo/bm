package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.ReadMailCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.EmailValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: MailForm.java 10320 2013-02-26 20:02:58Z miguel ${CLASS_NAME}.java,v 1.2 16-02-2005 05:09:15 PM ivan Exp $
 * @deprecated mail state has been updated as bit code
 */

public class MailForm extends WebmailDefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        User sessionUser = (User) request.getSession().getAttribute("user");
        Integer userMailId = (Integer) sessionUser.getValue("userId");

        String mailAccountId = (String) getDto("mailAccountId");
        if ("true".equals(getDto("hasChangedAccount"))) {
            super.validate(mapping, request);
            if (!GenericValidator.isBlankOrNull(mailAccountId)) {
                MailFormHelper mailHelper = new MailFormHelper();
                mailHelper.setDefaultSignature(Integer.valueOf(mailAccountId), userMailId, this, request);
            }
            setPageParameters(request);
            request.setAttribute("skipErrors", true);
            ActionErrors returnToPageErrors = new ActionErrors();
            returnToPageErrors.add("returnToPage", new ActionError("Common.error"));
            return returnToPageErrors;
        }

        ActionErrors errors = super.validate(mapping, request);

        //make trim in the list of emails (to, cc, bcc)
        setDto("to", trimListOfEmails(getDto("to").toString()));
        setDto("cc", trimListOfEmails(getDto("cc").toString()));
        setDto("bcc", trimListOfEmails(getDto("bcc").toString()));
        if (!errors.isEmpty()) {
            validateListEmails(errors);
            setPageParameters(request);
            return errors;
        }

        //validate list of emails
        validateListEmails(errors);

        DateTimeZone dateTimeZone = (DateTimeZone) sessionUser.getValue("dateTimeZone");
        this.setDto("userTimeZone", dateTimeZone);

        int maxSize = Integer.parseInt(sessionUser.getValue("maxAttachSize").toString()); // Mb;
        double attachSize = 0; //Mb;

        List fileWithData = new ArrayList();
        int contFilesWithData = 0;
        Integer number = Integer.valueOf((String) this.getDto("numberFiles"));

        List arrayByteWrapperList = new ArrayList();
        List fileNamesList = new ArrayList();
        List visibilityList = new ArrayList();
        double tempSize = 0;

        ArrayList fromRedirect[] = null;

        //Attachs from redirect
        Object redirect = getDto("redirect");


/*
        if (redirect != null && (redirect.toString().equals(WebMailConstants.MAIL_STATE_ANSWERED) ||
                redirect.toString().equals(WebMailConstants.MAIL_STATE_FORWARD))) {
            fromRedirect = getAttachsOfRedirect(request);

            if (fromRedirect != null && fromRedirect[0] != null) {

                String visibilitys[] = request.getParameterValues("dto(attachVisibilitys)");
                for (int i = 0; i < fromRedirect[0].size(); i++) {
                    ArrayByteWrapper wrapper_i = (ArrayByteWrapper) fromRedirect[0].get(i);
                    arrayByteWrapperList.add(wrapper_i);
                    fileNamesList.add((String) fromRedirect[1].get(i));
                    tempSize = wrapper_i.getFileData().length / 1024; //Kb;
                    tempSize = tempSize / 1024; //Mb;
                    attachSize = attachSize + tempSize;
                    visibilityList.add(visibilitys[i]);
                }
            }
        }
*/

        //End attachs from redirect
        for (int i = 0; i < number.intValue(); i++) {

            FormFile file = (FormFile) this.getDto("file" + (i));
            this.getDtoMap().remove("file" + (i));
            if (file != null && !"".equals(file.getFileName())) {
                ArrayByteWrapper wrapper = new ArrayByteWrapper();
                try {
                    wrapper.setFileData(file.getFileData());
                    String fileName = file.getFileName();
                    fileNamesList.add(fileName);
                    arrayByteWrapperList.add(wrapper);

                    //verif size
                    if (file.getFileSize() > 0) {
                        tempSize = file.getFileSize() / 1024; //Kb;
                        tempSize = tempSize / 1024; //Mb;
                        attachSize = attachSize + tempSize;
                    } else {
                        errors.add("file_invalid", new ActionError("Webmail.Attach.FileInvalid", file.getFileName()));
                    }

                    //view file attach
                    Map fileMap = new HashMap();
                    fileMap.put("fileData", "file" + contFilesWithData);
                    fileWithData.add(fileMap);
                    contFilesWithData++;

                } catch (IOException e) {
                    log.error("Cannot upload file...");
                }
            }
        }

        //verif size
        if (attachSize > maxSize) {
            errors.add("attach", new ActionError("Webmail.Attach.exceedAttach", new Integer(maxSize)));
        }
        request.setAttribute("ListfileWithData", fileWithData);
        request.setAttribute("contFiles", new Integer(contFilesWithData));

        this.setDto("arrayByteWrapperList", arrayByteWrapperList);
        this.setDto("fileNamesList", fileNamesList);
        this.setDto("visibilityList", visibilityList);

        String attachIds[] = request.getParameterValues("dto(attachIds)");
        if (attachIds != null) {
            this.setDto("attachIdsArray", (List) Arrays.asList(attachIds));
        }

        if (!errors.isEmpty() && contFilesWithData > 0) {
            errors.add("attach", new ActionError("Webmail.Attach.attachAnew"));
        }

        boolean haveAttachements = (null != attachIds ? true : false);
/*
        boolean isForwardOrAswer =
                (redirect != null &&
                        (redirect.toString().equals(WebMailConstants.MAIL_STATE_ANSWERED) ||
                                redirect.toString().equals(WebMailConstants.MAIL_STATE_FORWARD)) ? true : false);
*/
        boolean isForwardOrAswer = false;

        boolean cannotReadAttachments = (null == fromRedirect ? true : false);


        if (haveAttachements && isForwardOrAswer && cannotReadAttachments) {
            errors.add("attachmentsErr", new ActionError("Webmail.attach.notFound"));
        }
        //If error then re-set the attachIds
        if (!errors.isEmpty()) {
            setPageParameters(request);
        }

        String createOutCommunication = (String) getDto("createOutCommunication");
        String saveSendItem = (String) getDto("saveSendItem");

        if (null != getDto("save") && null != createOutCommunication && null == saveSendItem) {
            errors.add("saveSendItem", new ActionError("Webmail.mailCommunications.outCommunications",
                    JSPHelper.getMessage(request, "Webmail.common.saveInSentItems")));
        }


        if (!errors.isEmpty()) {
            setPageParameters(request);
        }

        return errors;
    }


    private void setPageParameters(HttpServletRequest request) {
        readAttachsGroup(request);
        request.setAttribute("dto(html)", getDto("html"));

        //Re-SET values for the page
        request.setAttribute("fromAError", "true");
        request.setAttribute("mailFilter", getDto("mailFilter"));
        request.setAttribute("mailSearch", getDto("mailSearch"));
        request.setAttribute("searchText", getDto("searchText"));
        request.setAttribute("searchFilter", getDto("searchFilter"));
        request.setAttribute("searchFolder", getDto("searchFolder"));
    }

    public String toString() {
        return this.getDtoMap().toString();
    }

    /**
     * Get the attachs of a mail(in case of a forward, reply, reply all)
     *
     * @param request
     * @return two Lists, the first with the arrayByteWrapperList, and the second with the fileNamesList
     */
    public ArrayList[] getAttachsOfRedirect(HttpServletRequest request) {
        log.debug("Executing getAttachsOfRedirect method...");
        ArrayList res[] = null;

        String attachIds[] = request.getParameterValues("dto(attachIds)");
        if (attachIds != null) {
            ReadMailCmd readMailCmd = new ReadMailCmd();
            DTO dto = new DTO();
            dto.put("attachIds", attachIds);
            dto.put("op", "readRedirectAttachs");
            readMailCmd.putParam(dto);
            ResultDTO resultDTO = new ResultDTO();
            try {
                resultDTO = BusinessDelegate.i.execute(readMailCmd, request);
            } catch (AppLevelException ale) {
            }

            if (!resultDTO.isFailure()) {
                res = new ArrayList[2];
                res[0] = (ArrayList) resultDTO.get("arrayByteWrapperList");
                res[1] = (ArrayList) resultDTO.get("fileNamesList");
            }
        }

        return (res);
    }

    /**
     * Get the selected attachs of a mail(in case of an error)
     *
     * @param request
     * @deprecated replaced by MailFormHelper.getEmailAttachments(HttpServletRequest)
     */
    public void readAttachsGroup(HttpServletRequest request) {
        log.debug("Executing readAttachsGroup method...");

        String attachIds[] = request.getParameterValues("dto(attachIds)");
        if (attachIds != null) {
            ReadMailCmd readMailCmd = new ReadMailCmd();
            DTO dto = new DTO();
            dto.put("attachIds", attachIds);
            dto.put("op", "readAttachsGroup");
            readMailCmd.putParam(dto);
            ResultDTO resultDTO = new ResultDTO();
            try {
                resultDTO = BusinessDelegate.i.execute(readMailCmd, request);
            } catch (AppLevelException ale) {
            }
            if (!resultDTO.isFailure()) {
                request.setAttribute("form_attachs", resultDTO.get("attachs"));
                request.setAttribute("form_hasAttachs", resultDTO.get("hasAttachs"));
            } else {
                request.setAttribute("form_attachs", null);
                request.setAttribute("form_hasAttachs", null);
            }

        }
    }

    /**
     * make trim in the list of mails (to,cc,bcc)
     *
     * @param listEmails is an cad with emails
     * @return new cad
     */
    public String trimListOfEmails(String listEmails) {
        //log.debug("Executing trimListOfEmails method...");
        String newListEmails = "";
        StringTokenizer st = new StringTokenizer(listEmails.trim(), ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (!GenericValidator.isBlankOrNull(token)) {

                StringTokenizer stEmail = new StringTokenizer(token);
                String arrEmail[] = new String[stEmail.countTokens()];
                int cont = 0;
                while (stEmail.hasMoreTokens()) {
                    arrEmail[cont] = stEmail.nextToken();
                    cont++;
                }

                String tempEmail = "";
                for (int i = 0; i < arrEmail.length; i++) {

                    String cad = arrEmail[i];
                    if (cad.equals("<") || (i == arrEmail.length - 1) || arrEmail[i + 1].equals(">")) {
                        tempEmail = tempEmail + cad;
                    } else {
                        tempEmail = tempEmail + cad + " ";
                    }

                }

                newListEmails = newListEmails + tempEmail.trim() + ", ";
            }
        }
        if (newListEmails.length() > 0) {
            newListEmails = newListEmails.substring(0, newListEmails.length() - 2);  //delete ", " of the end
        }

        return newListEmails;
    }

    private void validateListEmails(ActionErrors errors) {
        log.debug("Executing method validateListEmails.......................... ");
        LabelValueBean labelValueBean;
        Map mapOfListEmails = new HashMap();

        ArrayList listEmails = new ArrayList();
        if (!GenericValidator.isBlankOrNull(getDto("to").toString())) {
            labelValueBean = new LabelValueBean("to", getDto("to").toString());
            listEmails.add(labelValueBean);
        }
        if (!GenericValidator.isBlankOrNull(getDto("cc").toString())) {
            labelValueBean = new LabelValueBean("cc", getDto("cc").toString());
            listEmails.add(labelValueBean);
        }
        if (!GenericValidator.isBlankOrNull(getDto("bcc").toString())) {
            labelValueBean = new LabelValueBean("bcc", getDto("bcc").toString());
            listEmails.add(labelValueBean);
        }

        for (Iterator iterator = listEmails.iterator(); iterator.hasNext();) {

            labelValueBean = (LabelValueBean) iterator.next();
            String value = labelValueBean.getValue();
            String label = labelValueBean.getLabel();
            List listValidEmails = new ArrayList();

            log.debug("List of emails:" + value);

            StringTokenizer st = new StringTokenizer(value.trim(), ",");
            boolean flagQuote = false;
            String dirEmail = "";

            while (st.hasMoreTokens()) {
                String cad = st.nextToken();
                cad = cad.trim();
                log.debug("cad in split.................:" + cad);

                if (!GenericValidator.isBlankOrNull(cad)) {

                    //only email
                    if (countCharacter('\"', cad) == 0 && !flagQuote) {

                        dirEmail = cad;
                        if (dirEmail.startsWith("<") && dirEmail.endsWith(">")) {
                            dirEmail = dirEmail.substring(1, dirEmail.length() - 1).trim();
                        }

                        if (EmailValidator.i.isValid(dirEmail)) {
                            listValidEmails = addDataToList(listValidEmails, dirEmail, null);
                        } else {
                            errors.add("email", new ActionError("errors.email", dirEmail));
                        }
                        dirEmail = "";

                        // exist '\"' and not start with this , (is invalid and show error)
                    } else if (countCharacter('\"', cad) > 0 && !flagQuote && !cad.startsWith("\"")) {

                        dirEmail = cad;
                        addErrorPersonalNameOrEmail(errors, dirEmail);  //add error
                        dirEmail = "";

                        // 2 quotes or more
                    } else if (countCharacter('\"', cad) >= 2) {


                        if (dirEmail.length() > 0) {
                            dirEmail = dirEmail.substring(0, dirEmail.length() - 2); //remove ", " (2 characters)
                            addErrorPersonalNameOrEmail(errors, dirEmail.trim());  //add error
                        }

                        dirEmail = cad;
                        if (countCharacter('\"', cad) == 2 && dirEmail.startsWith("\"") && dirEmail.lastIndexOf("<") != -1 && dirEmail.endsWith(">") && (dirEmail.substring(0, dirEmail.lastIndexOf("<")).trim().endsWith("\""))) {
                            int beginStr = dirEmail.lastIndexOf("<");
                            int endStr = dirEmail.lastIndexOf(">");
                            String compoundDirEmail = dirEmail.substring(beginStr + 1, endStr).trim();
                            if (EmailValidator.i.isValid(compoundDirEmail)) {
                                listValidEmails = addDataToList(listValidEmails, compoundDirEmail, dirEmail);
                            } else {
                                errors.add("email", new ActionError("errors.email", compoundDirEmail));
                            }
                        } else {
                            addErrorPersonalNameOrEmail(errors, dirEmail); //add errors
                        }

                        dirEmail = "";
                        flagQuote = false;

                        // 1 quote and flagQuote in false
                    } else if (countCharacter('\"', cad) == 1 && !flagQuote) {
                        dirEmail = dirEmail + cad + ((st.countTokens() > 0) ? ", " : "");
                        flagQuote = true;

                        // not quote and flagQuote in true
                    } else if (countCharacter('\"', cad) == 0 && flagQuote) {
                        dirEmail = dirEmail + cad + ((st.countTokens() > 0) ? ", " : "");

                        // 1 quote and flagQuote in true
                    } else if (countCharacter('\"', cad) == 1 && flagQuote) {

                        dirEmail = dirEmail + cad;
                        if (dirEmail.lastIndexOf("<") != -1 && dirEmail.endsWith(">") && (dirEmail.substring(0, dirEmail.lastIndexOf("<")).trim().endsWith("\""))) {
                            int beginStr = dirEmail.lastIndexOf("<");
                            int endStr = dirEmail.lastIndexOf(">");
                            String compoundDirEmail = dirEmail.substring(beginStr + 1, endStr).trim();

                            if (EmailValidator.i.isValid(compoundDirEmail)) {
                                listValidEmails = addDataToList(listValidEmails, compoundDirEmail, dirEmail);
                            } else {
                                errors.add("email", new ActionError("errors.email", compoundDirEmail));
                            }
                        } else {
                            addErrorPersonalNameOrEmail(errors, dirEmail); //add errors
                        }
                        dirEmail = "";
                        flagQuote = false;
                    }

                    // if end the tokens and not ocured error
                    if (st.countTokens() == 0 && dirEmail.length() > 0) {
                        addErrorPersonalNameOrEmail(errors, dirEmail.trim()); //add errors
                    }
                }
            }
            //put in map of emails valid
            mapOfListEmails.put(label, listValidEmails);
            log.debug("mapOfListEmails......................." + mapOfListEmails);
        }
        //set in dto
        if (errors.isEmpty()) {
            setDto("recipients", mapOfListEmails);
        }

    }

    /**
     * this method always add a error, can be personal name error or email error
     *
     * @param errors the before errors
     * @param cad    the string what be evaluate to put the error
     */
    private void addErrorPersonalNameOrEmail(ActionErrors errors, String cad) {

        if (cad.lastIndexOf("<") != -1 && cad.endsWith(">")) {
            int beginStr = cad.lastIndexOf("<");
            int endStr = cad.lastIndexOf(">");
            String compoundDirEmail = cad.substring(beginStr + 1, endStr).trim();
            if (!EmailValidator.i.isValid(compoundDirEmail)) {
                errors.add("email", new ActionError("errors.email", compoundDirEmail));
            } else {
                errors.add("personalName", new ActionError("Webmail.compose.personalNameError", cad));
            }
        } else {
            errors.add("email", new ActionError("errors.email", cad));
        }
    }

    /**
     * count the number of repeat of an character in a string
     *
     * @param charSearch the character to search
     * @param cad        the string
     * @return int , number of repeat of the character
     */
    private int countCharacter(char charSearch, String cad) {
        int cont = 0;
        for (int i = 0; i < cad.length(); i++) {
            if (cad.charAt(i) == charSearch) {
                cont++;
            }
        }
        return cont;
    }


    /**
     * add data to list
     *
     * @param list  the list to add
     * @param email the address email
     * @param text  String with personal name
     * @return list
     */
    private List addDataToList(List list, String email, String text) {
        String name = null;
        Map map = new HashMap();

        if (text != null && countCharacter('\"', text) >= 2) {
            int beginStr = text.indexOf("\"");
            int endStr = text.lastIndexOf("\"");
            name = text.substring(beginStr + 1, endStr).trim();
        }

        map.put("personalName", name);
        map.put("email", email);
        map.put("contactPersonOf", getContactPersonIds(email, name));
        map.put("addressId", getAddressId(email, name));

        list.add(map);

        return list;
    }

    /**
     * get addressId of your contact person
     *
     * @param email        address email
     * @param personalName personal name
     * @return list with Ids
     */
    private List getContactPersonIds(String email, String personalName) {
        List resultList = new ArrayList();
        String ids = getDto("sentContactPersonIDs").toString();

        String[] arrayAll = ids.split("<s>");  //separator <s>
        for (int i = 0; i < arrayAll.length; i++) {
            String value = arrayAll[i];
            String[] arrayValue = value.split("<,>");  //array[0=id, 1=dirEmail, 2=personalName]
            if (arrayValue.length == 3 && arrayValue[1].equals(email) && trimListOfEmails(arrayValue[2]).equals(personalName)) {
                Integer addressId = Integer.valueOf(arrayValue[0]);
                if (!existThisId(resultList, addressId)) {
                    resultList.add(addressId);
                }
            }
        }
        return resultList;
    }

    /**
     * verify if exist this id in the list
     *
     * @param list
     * @param addressId
     * @return true or false
     */
    private boolean existThisId(List list, Integer addressId) {

        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Integer listId = (Integer) iterator.next();
            if (listId.equals(addressId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * get addressId of the contact with this email
     *
     * @param email        address email
     * @param personalName personal name
     * @return List of addressIds of this email
     */
    private List getAddressId(String email, String personalName) {
        List resultList = new ArrayList();
        String ids = getDto("sentAddressIDs").toString();

        String[] arrayAll = ids.split("<s>");  //separator <s>
        for (int i = 0; i < arrayAll.length; i++) {
            String value = arrayAll[i];
            String[] arrayValue = value.split("<,>");  //array[0=id, 1=dirEmail, 2=personalName]
            if (arrayValue.length == 3 && arrayValue[1].equals(email) && trimListOfEmails(arrayValue[2]).equals(personalName)) {
                Integer addressId = Integer.valueOf(arrayValue[0]);
                if (!existThisId(resultList, addressId)) {
                    resultList.add(addressId);
                }
            }
        }
        return resultList;
    }

}
