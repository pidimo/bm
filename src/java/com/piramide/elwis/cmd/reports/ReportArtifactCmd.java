package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.reportmanager.ReportArtifact;
import com.piramide.elwis.domain.reportmanager.ReportArtifactHome;
import com.piramide.elwis.dto.reports.ReportArtifactDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public class ReportArtifactCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("ReportArtifactCmd executing......................... " + paramDTO);

        boolean isRead = true;
        ReportArtifactDTO dto = getReportArtifactDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(dto);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(dto);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(dto);
        }
        if ("findByReport".equals(getOp())) {
            isRead = false;
            getArtifactInfoByReportId();
        }

        if (isRead) {
            boolean checkReferences = (null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences")));
            read(dto, checkReferences);
        }
    }

    private ReportArtifactDTO getReportArtifactDTO() {
        ReportArtifactDTO reportArtifactDTO = new ReportArtifactDTO();
        Integer artifactId = null;
        if (null != paramDTO.get("artifactId") &&
                !"".equals(paramDTO.get("artifactId").toString().trim())) {
            try {
                artifactId = Integer.valueOf(paramDTO.get("artifactId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse artifactId=" + paramDTO.get("artifactId") + " FAIL");
            }

            reportArtifactDTO.put("artifactId", artifactId);
        }

        reportArtifactDTO.put("op", paramDTO.get("op"));
        reportArtifactDTO.put("fileName", paramDTO.get("fileName"));
        reportArtifactDTO.put("companyId", paramDTO.get("companyId"));
        reportArtifactDTO.put("reportId", paramDTO.get("reportId"));
        reportArtifactDTO.put("version", paramDTO.get("version"));
        reportArtifactDTO.put("withReferences", paramDTO.get("withReferences"));

        log.debug("-> Working on ReportArtifactDTO=" + reportArtifactDTO + " OK");
        return reportArtifactDTO;
    }

    private void create(ReportArtifactDTO dto) {
        ReportArtifact reportArtifact = (ReportArtifact) ExtendedCRUDDirector.i.create(dto, resultDTO, true);
        if (reportArtifact != null && !resultDTO.isFailure()) {

            //create the file
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
            ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");
            try {
                FreeText freeText = freeTextHome.create(wrapper.getFileData(), reportArtifact.getCompanyId(), FreeTextTypes.FREETEXT_REPORT);
                reportArtifact.setFileId(freeText.getFreeTextId());
            } catch (CreateException e) {
                log.error("Can't create freetext....", e);
            }
        }
    }

    private ReportArtifact read(ReportArtifactDTO dto, boolean checkReferences) {
        ReportArtifact reportArtifact = (ReportArtifact) ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
        return reportArtifact;
    }

    private void update(ReportArtifactDTO dto) {
        ReportArtifact reportArtifact = (ReportArtifact) ExtendedCRUDDirector.i.update(dto, resultDTO, true, true, false, "Fail");
        if (reportArtifact != null && !resultDTO.isFailure()) {

            //update the file
            ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");

            if (wrapper != null) {
                reportArtifact.getArtifactFile().setValue(wrapper.getFileData());
                reportArtifact.setFileName(wrapper.getFileName());
            }
        }
    }

    private void delete(ReportArtifactDTO dto) {
        Integer artifactId = new Integer(paramDTO.get("artifactId").toString());
        ReportArtifactHome reportArtifactHome = (ReportArtifactHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTARTIFACT);

        try {
            ReportArtifact reportArtifact = reportArtifactHome.findByPrimaryKey(artifactId);
            if (reportArtifact != null && reportArtifact.getArtifactFile() != null) {
                //first remove file
                try {
                    reportArtifact.getArtifactFile().remove();
                } catch (RemoveException e) {
                }
            }
        } catch (FinderException e) {
        }

        ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
    }

    private void getArtifactInfoByReportId() {
        List<Map> artifactFileInfoList = new ArrayList<Map>();

        Integer reportId = new Integer(paramDTO.get("reportId").toString());
        ReportArtifactHome reportArtifactHome = (ReportArtifactHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_REPORTARTIFACT);

        Collection artifacts = null;
        try {
            artifacts = reportArtifactHome.findByReportId(reportId);
        } catch (FinderException e) {
            artifacts = new ArrayList();
        }

        for (Iterator iterator = artifacts.iterator(); iterator.hasNext();) {
            ReportArtifact reportArtifact = (ReportArtifact) iterator.next();
            if (reportArtifact.getFileId() != null) {
                Map map = new HashMap();
                map.put("artifactId", reportArtifact.getArtifactId());
                map.put("fileName", reportArtifact.getFileName());
                map.put("fileId", reportArtifact.getFileId());
                artifactFileInfoList.add(map);
            }
        }

        resultDTO.put("artifactInfoList", artifactFileInfoList);
    }


    public boolean isStateful() {
        return false;
    }
}
