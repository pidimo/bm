package com.piramide.elwis.domain.common.config;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SystemConstantPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SystemConstantPK implements Serializable {
    public String name;
    public String type;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SystemConstantPK)) {
            return false;
        }

        final SystemConstantPK systemConstantPK = (SystemConstantPK) o;

        if (name != null ? !name.equals(systemConstantPK.name) : systemConstantPK.name != null) {
            return false;
        }
        if (type != null ? !type.equals(systemConstantPK.type) : systemConstantPK.type != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 29 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
