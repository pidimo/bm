/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.domain.reportmanager;

import javax.ejb.EJBLocalObject;

public interface ReportRole extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRoleId();

    void setRoleId(Integer roleId);

    Integer getReportId();

    void setReportId(Integer reportId);
}
