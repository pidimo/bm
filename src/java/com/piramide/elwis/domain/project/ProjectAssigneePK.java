package com.piramide.elwis.domain.project;

import java.io.Serializable;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ProjectAssigneePK implements Serializable {
    public Integer projectId;
    public Integer addressId;

    public ProjectAssigneePK() {
    }

    public ProjectAssigneePK(Integer projectId, Integer addressId) {
        this.projectId = projectId;
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProjectAssigneePK that = (ProjectAssigneePK) o;

        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) {
            return false;
        }
        if (projectId != null ? !projectId.equals(that.projectId) : that.projectId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = projectId != null ? projectId.hashCode() : 0;
        result = 31 * result + (addressId != null ? addressId.hashCode() : 0);
        return result;
    }
}
