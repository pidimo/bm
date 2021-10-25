package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.TelecomType;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.EmailValidator;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * Class which gives operations for address and contact persons, such validations, utils, and so on.
 *
 * @author Fernando Montaño
 * @version $Id: AddressContactPersonHelper.java 12515 2016-02-26 19:03:50Z miguel $
 */

public class AddressContactPersonHelper {

    private static Log log = LogFactory.getLog(AddressContactPersonHelper.class);

    /**
     * validate existence of telecomtypes related to telecoms and validate for type EMAIL a valid value.
     * if user has added an telecomtype and wrote a text on telecom data textfield, it must be validated
     * and if it's valid then add it to telecomMap.
     *
     * @param errors     Action errors
     * @param request    the request
     * @param dtoMap     the Form dtoMap
     * @param telecomMap the telecom Map
     * @param user       user session
     * @return action errors
     */
    public synchronized static ActionErrors validateTelecoms(ActionErrors errors, HttpServletRequest request, Map dtoMap,
                                                             Map telecomMap, User user) {
        log.debug("Validating the telecoms in the Form");  //validating new entered telecom telecomtypeid/telecomvalue/descr.
        if ("".equals(dtoMap.get("telecomTypeId")) && !"".equals(dtoMap.get("telecomValue").toString().trim())) {
            errors.add("telecomType", new ActionError("errors.required", JSPHelper.getMessage(request, "Telecom.telecomType")));
        } else if (!"".equals(dtoMap.get("telecomTypeId")) && "".equals(dtoMap.get("telecomValue").toString().trim())) {
            errors.add("telecomValue", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Telecom.value")));
        } else if (!"".equals(dtoMap.get("telecomTypeId")) && !"".equals(dtoMap.get("telecomValue").toString().trim())) { //then validate existence of telecomtype
            TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
            cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
            cmd.putParam("companyId", user.getValue("companyId"));
            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
            cmd.putParam("telecomTypeId", dtoMap.get("telecomTypeId"));
            try {
                ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
                TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
                if (telecomTypeDTO != null) { //it was found

                    TelecomDTO telecomDTO = new TelecomDTO(((String) dtoMap.get("telecomValue")).trim(),
                            (String) dtoMap.get("telecomDescription"),
                            !telecomMap.containsKey(dtoMap.get("telecomTypeId")));

                    TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, telecomDTO, telecomTypeDTO); //add the new telecom

                    dtoMap.remove("telecomValue"); //leaving empty
                    dtoMap.remove("telecomTypeId");
                    dtoMap.remove("telecomDescription");

                } else { //it was no found
                    errors.add("telecomType", new ActionError("error.SelectedNotFound",
                            JSPHelper.getMessage(request, "Telecom.telecomType")));
                }
            } catch (AppLevelException e) {
                log.error("Error executing", e);
            }
        }

        /***Process when is creation operation only, remove the telecomtypes which has empty the value telecom **/
        if ("create".equals(dtoMap.get("op"))) {


            for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
                TelecomWrapperDTO wrapperDTO = (TelecomWrapperDTO) iterator.next();
                for (Iterator iterator1 = wrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                    TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                    if (GenericValidator.isBlankOrNull(telecomDTO.getData().trim())) {
                        wrapperDTO.removeTelecomDTO(telecomDTO);
                        iterator1 = wrapperDTO.getTelecoms().iterator();
                    }
                }
                if (wrapperDTO.getSize() == 0) {
                    telecomMap.remove(wrapperDTO.getTelecomTypeId());
                    iterator = telecomMap.values().iterator();
                }
            }
        }


        Map duplicatedTelecomsByType = new HashMap();
        //validating for the grid of telecoms
        for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
            TelecomWrapperDTO wrapperDTO = (TelecomWrapperDTO) iterator.next();

            if (wrapperDTO.getPredeterminedIndex() != null) {
                wrapperDTO.getTelecom(Integer.valueOf(wrapperDTO.getPredeterminedIndex()).intValue()).setPredetermined(true);
            } else {
                if (wrapperDTO.getTelecoms().size() > 1) {
                    errors.add("telecomTypePredetermined", new ActionError("error.TelecomType.nopredetermined",
                            wrapperDTO.getTelecomTypeName()));
                } else if (wrapperDTO.getTelecoms().size() == 1) { //is unique, then set as predetermined
                    wrapperDTO.getTelecom(0).setPredetermined(true);
                }
            }

            if (ForeignkeyValidator.i.exists(CatalogConstants.TABLE_TELECOMTYPE, "telecomtypeid",
                    wrapperDTO.getTelecomTypeId())) {
                for (Iterator iterator1 = wrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                    TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                    telecomDTO.setData(telecomDTO.getData().trim());//remove white spaces

                    if (GenericValidator.isBlankOrNull(telecomDTO.getData())) {
                        //add the error only one time by telecomtype
                        if (!errors.get("telecomValueEmpty" + wrapperDTO.getTelecomTypeId()).hasNext()) {
                            errors.add("telecomValueEmpty" + wrapperDTO.getTelecomTypeId(),
                                    new ActionError("error.Telecom.values.required", wrapperDTO.getTelecomTypeName()));
                        }

                    } else {
                        //validate valid emails
                        if (TelecomType.EMAIL_TYPE.equals(wrapperDTO.getTelecomTypeType())) {

                            if (!EmailValidator.i.isValid(telecomDTO.getData())) {
                                //add the error only one time  by telecomtype
                                if (!errors.get("telecomValueEmail" + wrapperDTO.getTelecomTypeId()).hasNext()) {
                                    errors.add("telecomValueEmail" + wrapperDTO.getTelecomTypeId(),
                                            new ActionError("error.TelecomEmailInvalid", wrapperDTO.getTelecomTypeName()));
                                }
                            }
                        }

                        if (duplicatedTelecomsByType.containsKey(wrapperDTO.getTelecomTypeId() + "-" + telecomDTO.getData())) {
                            //add the error only one time  by telecomtype
                            if (!errors.get("telecomDuplicated" + wrapperDTO.getTelecomTypeId() + "-" +
                                    telecomDTO.getData()).hasNext()) {
                                errors.add("telecomDuplicated" + wrapperDTO.getTelecomTypeId() + "-" + telecomDTO.getData(),
                                        new ActionError("error.Telecom.duplicated", telecomDTO.getData(),
                                                wrapperDTO.getTelecomTypeName()));
                            }

                        } else {
                            duplicatedTelecomsByType.put(wrapperDTO.getTelecomTypeId() + "-" + telecomDTO.getData(), null);
                        }
                    }
                }

            } else {
                //telecom type was deleted by other user
                errors.add("telecomType", new ActionError("error.TelecomTypeNotFound", wrapperDTO.getTelecomTypeName()));
                telecomMap.remove(wrapperDTO.getTelecomTypeId());//remove from UI
                iterator = telecomMap.values().iterator();
            }

        }

        return errors;

    }


    public static void restorePredeterminedTelecoms(Map telecomMap) {
        for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
            TelecomWrapperDTO wrapperDTO = (TelecomWrapperDTO) iterator.next();

            if (wrapperDTO.getPredeterminedIndex() != null) {
                wrapperDTO.getTelecom(Integer.valueOf(wrapperDTO.getPredeterminedIndex()).intValue()).setPredetermined(true);
            }
        }
    }

    /**
     * return a map of telecomtypewrapper dto, where each telecomtype wrapper has one empty telecomDTO.
     *
     * @param request the request
     * @return Map of telecom wrapper dto by default.
     */
    public static Map getDefaultTelecomTypes(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
        cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_LIST);
        cmd.putParam(Constants.COMPANYID, user.getValue("companyId"));
        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
            List result = (LinkedList) resultDTO.get(TelecomTypeSelectCmd.RESULT);
            TelecomDTO emptyTelecomDTO = new TelecomDTO("", "", true);
            Map resultMap = new LinkedHashMap();
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) iterator.next();
                TelecomWrapperDTO.addToMapTelecomDTO(resultMap, emptyTelecomDTO, telecomTypeDTO);//add the telecom
            }
            return resultMap;

        } catch (AppLevelException e) {
            log.error("Error trying to recover telecomtypes as predefined values for create form", e);
        }
        return new LinkedHashMap(); //empty values to skip page rendering error.
    }

    /**
     * Return the translated telecomtype name for a single telecomtype, given a telecomtype id
     *
     * @param request       the request
     * @param telecomTypeId the telecomtype id
     * @return the string name if success, otherwise returns empty string.
     */
    public static String getTelecomTypeName(HttpServletRequest request, Object telecomTypeId) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
        cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
        cmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
        cmd.putParam("telecomTypeId", telecomTypeId);
        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
            TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
            if (telecomTypeDTO != null) {
                return telecomTypeDTO.getAsString("telecomTypeName");
            }
        } catch (AppLevelException e) {
            log.error("Error trying to recover telecomtypes as predefined values for create form", e);
        }
        return "";
    }

    /**
     * Validate The Image file uploaded
     */

    public static ActionErrors validateImageFile(ActionErrors errors, Map dtoMap, FormFile imageFile) {
        boolean hasErrors = false;
        if (imageFile != null && !GenericValidator.isBlankOrNull(imageFile.getFileName())) {
            if (!(imageFile.getContentType().equals("image/jpeg") || imageFile.getContentType().equals("image/gif")
                    || imageFile.getContentType().equals("image/pjpeg") || imageFile.getContentType().equals("image/png"))) {
                errors.add("file_typeNotAllowed", new ActionError("Address.error.photo_invalidType", "gif, jpeg, png"));
                hasErrors = true;
            } else if (imageFile.getFileSize() > 204800) { //200KB
                errors.add("maxPhotoLengthExceeded", new ActionError("Address.error.photo_maxLengthExceeded", "200 KB"));
                hasErrors = true;
            }

            if (!hasErrors) {
                ArrayByteWrapper wrapper = new ArrayByteWrapper();
                try {
                    wrapper.setFileData(imageFile.getFileData());
                    dtoMap.put("image", wrapper);
                } catch (IOException e) {
                    log.error("unexpected error, cannot apply the arraybytewrapper to file", e);
                }
            }

        }
        return errors;
    }
}
