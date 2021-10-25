package com.piramide.elwis.cmd.projects;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.projects.SubProjectDTO;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SubProjectCmd extends ProjectManagerCmd {

    @Override
    public void executeInStateless(SessionContext context) {
        boolean isRead = true;
        SubProjectDTO subProjectDTO = getSubProjectDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(subProjectDTO);
        }

        if ("update".equals(getOp())) {
            isRead = false;
            update(subProjectDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(subProjectDTO);
        }
        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            read(subProjectDTO, withReferences);
        }
    }

    protected void delete(SubProjectDTO subProjectDTO) {
        ExtendedCRUDDirector.i.delete(subProjectDTO, resultDTO, true, "Fail");
    }

    protected void update(SubProjectDTO subProjectDTO) {
        ExtendedCRUDDirector.i.update(subProjectDTO, resultDTO, false, true, true, "Fail");
    }

    protected void read(SubProjectDTO subProjectDTO, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(subProjectDTO, resultDTO, checkReferences);
    }

    protected void create(SubProjectDTO subProjectDTO) {
        ExtendedCRUDDirector.i.create(subProjectDTO, resultDTO, false);
    }

    protected SubProjectDTO getSubProjectDTO() {
        SubProjectDTO subProjectDTO = new SubProjectDTO();
        subProjectDTO.put("name", paramDTO.get("name"));
        EJBCommandUtil.i.setValueAsInteger(this, subProjectDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, subProjectDTO, "projectId");
        EJBCommandUtil.i.setValueAsInteger(this, subProjectDTO, "subProjectId");
        EJBCommandUtil.i.setValueAsInteger(this, subProjectDTO, "version");
        subProjectDTO.put("withReferences", paramDTO.get("withReferences"));

        return subProjectDTO;
    }

    public boolean isStateful() {
        return false;
    }
}
