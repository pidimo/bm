package com.piramide.elwis.web.webmail.util;

import com.piramide.elwis.cmd.utils.WebmailHTMLParser;
import com.piramide.elwis.dto.webmailmanager.AttachDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.FileValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class AttachFormHelper {
    private final String COUNTER_IDENTIFIER = "attachmentCounter";
    private final String ATTACHMENT_IDENTIFIER = "file";
    public static final String ATTACHID_KEY = "attachId_";
    public static final String VISIBLE_KEY = "visible_";
    public static final String ATTACHFILE_KEY = "attachFile_";
    private final String SIZE_KEY = "size_";

    private List<FormFile> attachments = new ArrayList<FormFile>();

    public List<AttachDTO> rebuildAttachmentList(DefaultForm defaultForm) {

        List<AttachDTO> attachments = new ArrayList<AttachDTO>();
        List<String> attachIds = getAttachIds(defaultForm);

        for (String id : attachIds) {
            String visible = (String) defaultForm.getDto(VISIBLE_KEY + id);
            String attachFile = (String) defaultForm.getDto(ATTACHFILE_KEY + id);
            String size = (String) defaultForm.getDto(SIZE_KEY + id);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.put("attachId", id);
            attachDTO.put("attachFile", attachFile);
            attachDTO.put("visible", visible);
            attachDTO.put("size", size);

            attachments.add(attachDTO);


            if (null != defaultForm.getDto(id) && "true".equals(defaultForm.getDto(id).toString().trim())) {
                defaultForm.setDto(id, true);
            } else {
                defaultForm.setDto(id, false);
            }
        }

        return attachments;
    }

    private List<String> getAttachIds(DefaultForm form) {
        List<String> result = new ArrayList<String>();
        for (Object object : form.getDtoMap().entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String key = entry.getKey().toString();
            if (key.contains(ATTACHID_KEY)) {
                result.add(getAttachId(key));
            }
        }

        return result;
    }

    private String getAttachId(String code) {
        return code.replace(ATTACHID_KEY, "").trim();
    }

    public ActionError validateAttachments(DefaultForm defaultForm, HttpServletRequest request) {
        String attachmentCounter = (String) defaultForm.getDto(COUNTER_IDENTIFIER);

        if (GenericValidator.isBlankOrNull(attachmentCounter)) {
            return null;
        }

        User user = RequestUtils.getUser(request);
        Integer uploadLimit = (Integer) user.getValue("maxAttachSize");

        /*Integer oldAttachmentSize = sumOldAttachmentSizes(defaultForm);
        double oldAttachmentsFilesSize = oldAttachmentSize / Math.pow(1024, 2);

        if (oldAttachmentsFilesSize > uploadLimit)
            return new ActionError("Common.error.fileExceedSize", uploadLimit);


        uploadLimit = uploadLimit - Integer.valueOf(String.valueOf(Math.round(oldAttachmentsFilesSize)));*/

        Integer counter = Integer.valueOf(attachmentCounter);

        for (int i = 1; i <= counter; i++) {
            if (null == defaultForm.getDto(ATTACHMENT_IDENTIFIER + i)) {
                continue;
            }

            FormFile formFile = (FormFile) defaultForm.getDto(ATTACHMENT_IDENTIFIER + i);
            if (null == formFile.getFileName() || "".equals(formFile.getFileName().trim())) {
                continue;
            }

            ActionError contentValidation = FileValidator.i.validateContent(formFile);
            if (null != contentValidation) {
                return contentValidation;
            }

            ActionError uploadLimitValidation = FileValidator.i.validateFileSize(formFile, uploadLimit);
            if (null != uploadLimitValidation) {
                return new ActionError("Webmail.Attach.exceedAttach", user.getValue("maxAttachSize"));
            }

            double fileSize = formFile.getFileSize() / Math.pow(1024, 2);
            uploadLimit = uploadLimit - Integer.valueOf(String.valueOf(Math.round(fileSize)));

            attachments.add(formFile);
        }

        return null;
    }

    /**
     * Validate file input for save temporal attach
     * @param defaultForm
     * @param request
     * @return ActionError
     */
    public ActionError validateFileInputAttach(DefaultForm defaultForm, HttpServletRequest request) {

        User user = RequestUtils.getUser(request);
        Integer uploadLimit = (Integer) user.getValue("maxAttachSize");

        if (null == defaultForm.getDto("fileInputAttach")) {
            return null;
        }

        FormFile formFile = (FormFile) defaultForm.getDto("fileInputAttach");
        if (null == formFile.getFileName() || "".equals(formFile.getFileName().trim())) {
            return null;
        }

        ActionError contentValidation = FileValidator.i.validateContent(formFile);
        if (null != contentValidation) {
            return contentValidation;
        }

        ActionError uploadLimitValidation = FileValidator.i.validateFileSize(formFile, uploadLimit);
        if (null != uploadLimitValidation) {
            return uploadLimitValidation;
        }

        attachments.add(formFile);

        return null;
    }


    public List<AttachDTO> buildOldAttachmentStructure(DefaultForm defaultForm) {
        List<AttachDTO> structure = new ArrayList<AttachDTO>();

        List<String> attachIds = getAttachIds(defaultForm);
        for (String id : attachIds) {
            String attachId = (String) defaultForm.getDto(ATTACHID_KEY + id);
            String visible = (String) defaultForm.getDto(VISIBLE_KEY + id);
            String attachFile = (String) defaultForm.getDto(ATTACHFILE_KEY + id);

            if ("true".equals(visible)) {
                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", Integer.valueOf(attachId));
                attachDTO.put("attachName", attachFile);
                attachDTO.put("visible", true);
                structure.add(attachDTO);
                continue;
            }

            boolean checked = false;
            if (null != defaultForm.getDto(id) && "true".equals(defaultForm.getDto(id).toString().trim())) {
                checked = true;
            }

            if (checked) {
                AttachDTO attachDTO = new AttachDTO();
                attachDTO.put("attachId", Integer.valueOf(attachId));
                attachDTO.put("attachName", attachFile);
                attachDTO.put("visible", false);
                structure.add(attachDTO);
            }
        }

        return structure;
    }

    public List<ArrayByteWrapper> buildNewAttachmentStructure() {
        if (null == attachments) {
            return new ArrayList<ArrayByteWrapper>();
        }


        List<ArrayByteWrapper> structure = new ArrayList<ArrayByteWrapper>();
        for (FormFile formFile : attachments) {
            ArrayByteWrapper wrapper = new ArrayByteWrapper();
            try {
                wrapper.setFileName(formFile.getFileName());
                wrapper.setFileData(formFile.getFileData());
            } catch (IOException e) {
                continue;
            }

            structure.add(wrapper);
        }

        attachments.clear();

        return structure;
    }

    public List<FormFile> getAttachments() {
        return attachments;
    }

    public String updateBody(String body,
                             List<AttachDTO> attachments,
                             HttpServletRequest request, HttpServletResponse response) {
        String newBody = body;
        for (Object obj : attachments) {
            AttachDTO attachDTO = (AttachDTO) obj;
            String attachId = attachDTO.get("attachId").toString();
            String attachName = (String) attachDTO.get("attachFile");
            Boolean isVisible = (Boolean) attachDTO.get("visible");
            if (!isVisible) {
                continue;
            }

            String url = request.getContextPath() +
                    WebMailConstants.downloadImageURL + "?dto(attachId)=" + attachId;

            String encodedUrl = response.encodeURL(url);
            newBody = WebmailHTMLParser.settingUpApplicationContextInImages(newBody,
                    encodedUrl, attachId, attachName);
        }

        return newBody;
    }
}
