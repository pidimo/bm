package com.piramide.elwis.domain.project;

import java.io.Serializable;

/**
 * @author Fernando Montao
 * @version $Id: ProjectUserPK.java 2009-02-20 11:49:56 $
 */
public class ProjectUserPK implements Serializable {

    public Integer projectId;
    public Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectUserPK that = (ProjectUserPK) o;

        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = projectId != null ? projectId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
