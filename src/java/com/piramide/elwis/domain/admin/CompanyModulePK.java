package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: CompanyModulePK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CompanyModulePK implements Serializable {
    public Integer companyId;
    public Integer moduleId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompanyModulePK)) {
            return false;
        }

        final CompanyModulePK companyModulePK = (CompanyModulePK) o;

        if (companyId != null ? !companyId.equals(companyModulePK.companyId) : companyModulePK.companyId != null) {
            return false;
        }
        if (moduleId != null ? !moduleId.equals(companyModulePK.moduleId) : companyModulePK.moduleId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (companyId != null ? companyId.hashCode() : 0);
        result = 29 * result + (moduleId != null ? moduleId.hashCode() : 0);
        return result;
    }
}
