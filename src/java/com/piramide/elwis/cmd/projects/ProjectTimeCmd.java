package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.project.Project;
import com.piramide.elwis.domain.project.ProjectFreeText;
import com.piramide.elwis.domain.project.ProjectTime;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.projects.ProjectTimeDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProjectConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectTimeCmd extends ProjectManagerCmd {
    private Log log = LogFactory.getLog(ProjectTimeCmd.class);

    @Override
    public void executeInStateless(SessionContext context) {
        boolean isRead = true;
        ProjectTimeDTO projectTimeDTO = getProjectTimeDTO();
        if ("create".equals(getOp())) {
            isRead = false;
            String description = (String) paramDTO.get("description");
            create(projectTimeDTO, description);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            String description = (String) paramDTO.get("description");
            update(projectTimeDTO, description, context);
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            delete(projectTimeDTO);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(projectTimeDTO, withReferences, context);
        }
    }

    private void delete(ProjectTimeDTO projectTimeDTO) {
        ProjectTime projectTime =
                (ProjectTime) ExtendedCRUDDirector.i.read(projectTimeDTO, resultDTO, true);

        //projectTime was deleted by another user
        if (null == projectTime) {
            resultDTO.setForward("Fail");
            return;
        }

        //validate status change
        if (null != projectTime) {
            String oldStatus = (String) paramDTO.get("oldStatus");
            if (!projectTime.getStatus().toString().equals(oldStatus)) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("ProjectTime.statusChange.error");
                return;
            }
        }

        Project project = projectTime.getProject();

        try {
            projectTime.remove();
            updateTotalTimes(project);
        } catch (RemoveException e) {
            log.debug("-> Cannot Remove ProjectTime ", e);
        }
    }

    private void create(ProjectTimeDTO projectTimeDTO, String description) {
        ProjectTime projectTime =
                (ProjectTime) ExtendedCRUDDirector.i.create(projectTimeDTO, resultDTO, false);

        ProjectFreeText projectFreeText = createFreeText(description,
                projectTime.getCompanyId(),
                FreeTextTypes.FREETEXT_PROJECT_TIME);

        projectTime.setDescriptionText(projectFreeText);

        if (ProjectConstants.ProjectTimeStatus.RELEASED.getValue() == projectTime.getStatus()) {
            updateTotalTimes(projectTime.getProject());
            defineReleasedUserData(projectTime);
        }
    }

    private void update(ProjectTimeDTO projectTimeDTO, String description, SessionContext context) {

        ProjectTime oldTime = getProjectTime((Integer) projectTimeDTO.get("timeId"));
        //validate status change
        if (null != oldTime) {
            String oldStatus = (String) paramDTO.get("oldStatus");
            if (!oldTime.getStatus().toString().equals(oldStatus)) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("ProjectTime.statusChange.error");
                return;
            }
        }

        ProjectTime projectTime =
                (ProjectTime) ExtendedCRUDDirector.i.update(projectTimeDTO, resultDTO, false, true, true, "Fail");

        if (null == projectTime) {
            resultDTO.setForward("Fail");
            return;
        }

        //version error 
        if (resultDTO.isFailure()) {
            read(projectTimeDTO, false, context);
            return;
        }

        if (null == projectTime.getDescriptionText()) {
            ProjectFreeText projectFreeText =
                    createFreeText(description, projectTime.getCompanyId(), FreeTextTypes.FREETEXT_PROJECT_TIME);
            projectTime.setDescriptionText(projectFreeText);
        } else {
            projectTime.getDescriptionText().setValue(description.getBytes());
        }

        updateTotalTimes(projectTime.getProject());

        defineReleasedUserData(projectTime);
    }

    private void updateTotalTimes(Project project) {
        BigDecimal totalInvoice = sumProjectTimesByProject(project.getProjectId(), project.getCompanyId(), true);
        BigDecimal totalNoInvoice = sumProjectTimesByProject(project.getProjectId(), project.getCompanyId(), false);

        project.setTotalInvoice(totalInvoice);
        project.setTotalNoInvoice(totalNoInvoice);
    }

    private void read(ProjectTimeDTO projectTimeDTO, boolean checkReferences, SessionContext context) {
        ProjectTime projectTime =
                (ProjectTime) ExtendedCRUDDirector.i.read(projectTimeDTO, resultDTO, checkReferences);

        //element was deleted by another user
        if (null == projectTime) {
            return;
        }

        //read User Name
        UserDTO userDTO = getUserDTO(projectTime.getUserId());
        resultDTO.put("userName", readAddressName((Integer) userDTO.get("addressId"), context));

        //read Assignee Name
        if (null != projectTime.getAssigneeId()) {
            resultDTO.put("assigneeName", readAddressName(projectTime.getAssigneeId(), context));
        }

        //read projectTime description
        String description = readProjectFreeText(projectTime.getDescriptionText());
        resultDTO.put("description", description);

        //enable or diable tobeInvoiced option
        if (ProjectConstants.ToBeInvoicedType.ALL_TIMES.getValue() == projectTime.getProject().getToBeInvoiced()) {
            resultDTO.put("disableInvoiceable", true);
        }

        if (ProjectConstants.ToBeInvoicedType.NO_TIMES.getValue() == projectTime.getProject().getToBeInvoiced()) {
            resultDTO.put("disableInvoiceable", true);
        }

        if (ProjectConstants.ToBeInvoicedType.DEPENDS.getValue() == projectTime.getProject().getToBeInvoiced()) {
            resultDTO.put("disableInvoiceable", false);
        }

        if (null != projectTime.getConfirmedById()) {
            UserDTO confirmedUserDTO = getUserDTO(projectTime.getConfirmedById());
            resultDTO.put("confirmedByName", readAddressName((Integer) confirmedUserDTO.get("addressId"), context));
        }

        if (null != projectTime.getReleasedUserId()) {
            UserDTO releasedUserDTO = getUserDTO(projectTime.getReleasedUserId());
            resultDTO.put("releasedUserName", readAddressName((Integer) releasedUserDTO.get("addressId"), context));
        }

        if (projectTime.getFromDateTime() != null) {
            DateTime dateTime = new DateTime(projectTime.getFromDateTime());
            resultDTO.put("fromHour", dateTime.getHourOfDay());
            resultDTO.put("fromMin", dateTime.getMinuteOfHour());
        }

        if (projectTime.getToDateTime() != null) {
            DateTime dateTime = new DateTime(projectTime.getToDateTime());
            resultDTO.put("toHour", dateTime.getHourOfDay());
            resultDTO.put("toMin", dateTime.getMinuteOfHour());
        }

        //use to validate status change in update operations
        resultDTO.put("oldStatus", projectTime.getStatus());

        resultDTO.put("projectStatus", projectTime.getProject().getStatus());

    }

    private ProjectTimeDTO getProjectTimeDTO() {
        ProjectTimeDTO projectTimeDTO = new ProjectTimeDTO();
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "timeId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "activityId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "confirmedById");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "date");

        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "projectId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "status");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "subProjectId");
        EJBCommandUtil.i.setValueAsBigDecimal(this, projectTimeDTO, "time");

        projectTimeDTO.put("toBeInvoiced", false);
        if (null != paramDTO.get("toBeInvoiced") && "true".equals(paramDTO.get("toBeInvoiced").toString())) {
            projectTimeDTO.put("toBeInvoiced", true);
        }

        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "userId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "assigneeId");
        EJBCommandUtil.i.setValueAsInteger(this, projectTimeDTO, "version");

        projectTimeDTO.put("withReferences", paramDTO.get("withReferences"));

        //used to show user name in error messages
        projectTimeDTO.put("userName", paramDTO.get("userName"));

        projectTimeDTO.put("fromDateTime", paramDTO.get("fromDateTime"));
        projectTimeDTO.put("toDateTime", paramDTO.get("toDateTime"));


        return projectTimeDTO;
    }

    private void defineReleasedUserData(ProjectTime projectTime) {

        if (projectTime.getReleasedUserId() == null && ProjectConstants.ProjectTimeStatus.RELEASED.getValue() == projectTime.getStatus()) {
            Integer releasedUserId = new Integer(paramDTO.get("sessionUserId").toString());

            projectTime.setReleasedUserId(releasedUserId);
            projectTime.setReleasedDate(DateUtils.dateToInteger(new Date()));
        }
    }

    public boolean isStateful() {
        return false;
    }
}
