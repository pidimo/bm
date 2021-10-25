package com.piramide.elwis.domain.dashboard;

import java.io.Serializable;

/**
 * @author : ivan
 *         Date: Aug 30, 2006
 *         Time: 5:42:12 PM
 */
public class AdminComponentPK implements Serializable {
    public Integer containerId;
    public Integer componentId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final AdminComponentPK that = (AdminComponentPK) o;

        if (componentId != null ? !componentId.equals(that.componentId) : that.componentId != null) {
            return false;
        }
        if (containerId != null ? !containerId.equals(that.containerId) : that.containerId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (containerId != null ? containerId.hashCode() : 0);
        result = 29 * result + (componentId != null ? componentId.hashCode() : 0);
        return result;
    }
}
