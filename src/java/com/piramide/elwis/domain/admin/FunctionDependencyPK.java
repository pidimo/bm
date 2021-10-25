package com.piramide.elwis.domain.admin;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: FunctionDependencyPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class FunctionDependencyPK implements Serializable {
    public Integer functionId;
    public Integer functionDependencyId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionDependencyPK)) {
            return false;
        }

        final FunctionDependencyPK functionDependencyPK = (FunctionDependencyPK) o;

        if (functionDependencyId != null ? !functionDependencyId.equals(functionDependencyPK.functionDependencyId) : functionDependencyPK.functionDependencyId != null) {
            return false;
        }
        if (functionId != null ? !functionId.equals(functionDependencyPK.functionId) : functionDependencyPK.functionId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (functionId != null ? functionId.hashCode() : 0);
        result = 29 * result + (functionDependencyId != null ? functionDependencyId.hashCode() : 0);
        return result;
    }
}
