package com.piramide.elwis.domain.salesmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: ActionPK.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ActionPK implements Serializable {

    public Integer contactId;
    public Integer processId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionPK)) {
            return false;
        }

        final ActionPK actionPK = (ActionPK) o;

        if (contactId != null ? !contactId.equals(actionPK.contactId) : actionPK.contactId != null) {
            return false;
        }
        if (processId != null ? !processId.equals(actionPK.processId) : actionPK.processId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (contactId != null ? contactId.hashCode() : 0);
        result = 29 * result + (processId != null ? processId.hashCode() : 0);
        return result;
    }
}
