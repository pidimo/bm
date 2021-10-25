package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.projects.ProjectActivityDTO;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProjectActivityCmd extends ProjectManagerCmd {

    @Override
    public void executeInStateless(SessionContext context) {
        ProjectActivityDTO projectActivityDTO = getProjectActovityDTO();

        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create(projectActivityDTO);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            update(projectActivityDTO);
        }

        if ("delete".equals(getOp())) {
            isRead = false;
            delete(projectActivityDTO);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(projectActivityDTO, withReferences);
        }
    }

    protected void create(ProjectActivityDTO projectActivityDTO) {
        ExtendedCRUDDirector.i.create(projectActivityDTO, resultDTO, false);
    }

    protected void read(ProjectActivityDTO projectActivityDTO, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(projectActivityDTO, resultDTO, checkReferences);
    }

    protected void update(ProjectActivityDTO projectActivityDTO) {
        ExtendedCRUDDirector.i.update(projectActivityDTO, resultDTO, false, true, true, "Fail");
    }

    protected void delete(ProjectActivityDTO projectActivityDTO) {
        ExtendedCRUDDirector.i.delete(projectActivityDTO, resultDTO, true, "Fail");
    }

    protected ProjectActivityDTO getProjectActovityDTO() {
        ProjectActivityDTO projectActivityDTO = new ProjectActivityDTO();
        projectActivityDTO.put("name", paramDTO.get("name"));
        EJBCommandUtil.i.setValueAsInteger(this, projectActivityDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, projectActivityDTO, "activityId");
        EJBCommandUtil.i.setValueAsInteger(this, projectActivityDTO, "projectId");
        EJBCommandUtil.i.setValueAsInteger(this, projectActivityDTO, "version");
        projectActivityDTO.put("withReferences", paramDTO.get("withReferences"));

        return projectActivityDTO;
    }

    public boolean isStateful() {
        return false;
    }
}
