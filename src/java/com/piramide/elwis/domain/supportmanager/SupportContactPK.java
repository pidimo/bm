package com.piramide.elwis.domain.supportmanager;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:27:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupportContactPK implements Serializable {
    public Integer contactId;
    public Integer caseId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final SupportContactPK that = (SupportContactPK) o;

        if (caseId != null ? !caseId.equals(that.caseId) : that.caseId != null) {
            return false;
        }
        if (contactId != null ? !contactId.equals(that.contactId) : that.contactId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (contactId != null ? contactId.hashCode() : 0);
        result = 29 * result + (caseId != null ? caseId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "ContactId:" + contactId + "-CaseId:" + caseId;
    }
}
