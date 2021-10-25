/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:18:23 $
 */
package com.piramide.elwis.domain.project;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProjectActivityBean implements EntityBean {
    public ProjectActivityBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getProjectId();

    public abstract void setProjectId(Integer projectId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Project getProject();

    public abstract void setProject(Project project);

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setActivityId(PKGenerator.i.nextKey(ProjectConstants.TABLE_PROJECT_ACTIVITY));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }
}
