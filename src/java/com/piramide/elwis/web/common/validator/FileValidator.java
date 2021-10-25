package com.piramide.elwis.web.common.validator;

import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.utils.ExtensionMimeDetectorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.struts.action.ActionError;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class FileValidator {
    private Log log = LogFactory.getLog(FileValidator.class);

    private final String octetStreamContentType = "application/octet-stream";

    public static FileValidator i = new FileValidator();

    private FileValidator() {
    }

    public ActionError validateWordTemplate(FormFile file,
                                            HttpServletRequest request,
                                            String resourceKey) {
        log.debug("-> Validate Word template:");
        log.debug("-> File name    : " + file.getFileName());
        log.debug("-> File size    : " + file.getFileSize());
        log.debug("-> Content Type : " + file.getContentType());

        if (GenericValidator.isBlankOrNull(file.getFileName())) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, resourceKey));
        }

        ActionError error;

        if (null != (error = validateContent(file))) {
            return error;
        }

        if (null != (error = validateFileSize(file, request))) {
            return error;
        }

        if (null != (error = validateType(file, ".doc", "application/msword", "application/vnd.ms-word"))) {
            return error;
        }

        if (null != (error = poiReader(file))) {
            return error;
        }

        return null;
    }


    public ActionError validateContent(FormFile file) {
        try {
            if (null == file.getFileData()) {
                log.warn("-> Content file is null");
                return new ActionError("Common.error.fileInvalid", file.getFileName());
            }

            if (0 == file.getFileData().length) {
                log.warn("-> File not contain data");
                return new ActionError("Common.error.fileInvalid", file.getFileName());
            }
        } catch (IOException e) {
            log.warn("-> File was deleted ");
            return new ActionError("Common.error.fileNotFound");
        }

        return null;
    }

    public ActionError validateImageFile(FormFile file, HttpServletRequest request, String resourceKey) {
        log.debug("-> Validate Image file:");
        log.debug("-> File name    : " + file.getFileName());
        log.debug("-> File size    : " + file.getFileSize());
        log.debug("-> Content Type : " + file.getContentType());

        if (GenericValidator.isBlankOrNull(file.getFileName())) {
            return new ActionError("errors.required", JSPHelper.getMessage(request, resourceKey));
        }

        ActionError error;
        if (null != (error = validateContent(file))) {
            return error;
        }

        String[] imgContentTypes = {"image/gif", "image/ief", "image/jpeg", "image/tiff", "image/pjpeg", "image/png"};
        if (!isValidContentType(imgContentTypes, file.getContentType())) {
            return new ActionError("File.error.imageType", file.getFileName());
        }

        if (null != (error = validateFileSize(file, request))) {
            return error;
        }

        return null;
    }

    public ActionError validateFileSize(FormFile file, Integer uploadLimit) {
        double fileSize = file.getFileSize() / Math.pow(1024, 2);
        if (fileSize > uploadLimit) {
            log.warn("-> File size=" + fileSize + " exceed upload limit=" + uploadLimit);
            return new ActionError("Common.error.fileExceedSize", uploadLimit);
        }
        return null;
    }


    public ActionError validateExcelFile(FormFile file, String resourceKey) {
        final String excelExtention = "xls";

        //validate extention
        String fileName = file.getFileName();
        if (!excelExtention.equals(getFileExtension(fileName))) {
            return new ActionError(resourceKey);
        }

        //check if file is used by another application
        if (octetStreamContentType.equals(file.getContentType())) {
            log.warn("->File is used by another application");
            return new ActionError("File.error.opened", file.getFileName());
        }

        //validate mimeType
        List<String> xlsMimeTypes = ExtensionMimeDetectorFactory.i.getMimeTypes(fileName);
        if (!xlsMimeTypes.contains(file.getContentType())) {
            return new ActionError(resourceKey);
        }

        return null;
    }


    public final String getFileExtension(String fileName) {
        int lastPoint = fileName.lastIndexOf('.');
        if (lastPoint == -1) {
            return null;
        }

        return fileName.substring(lastPoint + 1, fileName.length());
    }

    /**
     * Validates if the file's content type is in the list of content types passed as parameter.
     *
     * @param file         the file
     * @param allowedTypes the extension of the allowed files
     * @param contentTypes the list of possible valid content types for the extension
     * @return the action error
     */
    public ActionError validateType(FormFile file, String allowedTypes, String... contentTypes) {
        if (octetStreamContentType.equals(file.getContentType())) {
            log.warn("->File is used by another application");
            return new ActionError("File.error.opened", file.getFileName());
        }
        List<String> contentTypeList = Arrays.asList(contentTypes);

        if (!contentTypeList.contains(file.getContentType())) {
            return new ActionError("File.error.allowedTypes", file.getFileName(), allowedTypes);
        }

        return null;
    }

    private ActionError validateFileSize(FormFile file, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        int uploadLimit = Integer.parseInt(user.getValue("maxAttachSize").toString());
        double fileSize = file.getFileSize() / Math.pow(1024, 2);
        if (fileSize > uploadLimit) {
            log.warn("-> File size=" + fileSize + " exceed upload limit=" + uploadLimit);
            return new ActionError("Common.error.fileExceedSize", uploadLimit);
        }
        return null;
    }

    private ActionError poiReader(FormFile file) {
        try {
            InputStream stream = new ByteArrayInputStream(file.getFileData());
            POIFSReader r = new POIFSReader();
            r.read(stream);
        } catch (IOException e) {
            log.warn("-> POI cannot process File " + file.getFileName(), e);
            return new ActionError("Common.error.fileInvalid", file.getFileName());
        }

        return null;
    }


    private boolean isValidContentType(String[] validContentTypes, String contentType) {
        for (String validContentType : validContentTypes) {
            if (validContentType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

}
