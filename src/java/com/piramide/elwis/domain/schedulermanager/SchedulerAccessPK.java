package com.piramide.elwis.domain.schedulermanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: SchedulerAccessPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SchedulerAccessPK implements Serializable {
    public Integer ownerUserId;
    public Integer userId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchedulerAccessPK)) {
            return false;
        }

        final SchedulerAccessPK schedulerAccessPK = (SchedulerAccessPK) o;

        if (ownerUserId != null ? !ownerUserId.equals(schedulerAccessPK.ownerUserId) : schedulerAccessPK.ownerUserId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(schedulerAccessPK.userId) : schedulerAccessPK.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (ownerUserId != null ? ownerUserId.hashCode() : 0);
        result = 29 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
