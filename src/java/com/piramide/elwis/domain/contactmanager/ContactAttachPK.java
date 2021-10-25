/*
 * {Proyect_Name}
 *
 * {Copyright}
 *
 * $Id: ContactAttachPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * @author Alejandro C. Ruiz
 * @version 1.0 $Date: 2009-09-12 11:46:08 -0400 (Sat, 12 Sep 2009) $
 */
public class ContactAttachPK implements Serializable {
    public Integer contactId;
    public Integer freeTextId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final ContactAttachPK that = (ContactAttachPK) o;

        if (contactId != null ? !contactId.equals(that.contactId) : that.contactId != null) {
            return false;
        }
        if (freeTextId != null ? !freeTextId.equals(that.freeTextId) : that.freeTextId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (contactId != null ? contactId.hashCode() : 0);
        result = 29 * result + (freeTextId != null ? freeTextId.hashCode() : 0);
        return result;
    }
}
