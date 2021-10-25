package com.piramide.elwis.domain.reportmanager;

import java.io.Serializable;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRolePK implements Serializable {
    public Integer reportId;
    public Integer roleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReportRolePK that = (ReportRolePK) o;

        if (reportId != null ? !reportId.equals(that.reportId) : that.reportId != null) {
            return false;
        }
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (reportId != null ? reportId.hashCode() : 0);
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}
