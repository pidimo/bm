package com.piramide.elwis.domain.dashboard;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: AttributePK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AttributePK implements Serializable {
    public Integer componentViewId;
    public String name;

    public AttributePK() {
    }

    public AttributePK(Integer componentViewId, String name) {
        this.componentViewId = componentViewId;
        this.name = name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttributePK)) {
            return false;
        }

        final AttributePK attributePK = (AttributePK) o;

        if (componentViewId != null ? !componentViewId.equals(attributePK.componentViewId) : attributePK.componentViewId != null) {
            return false;
        }
        if (name != null ? !name.equals(attributePK.name) : attributePK.name != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (componentViewId != null ? componentViewId.hashCode() : 0);
        result = 29 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
