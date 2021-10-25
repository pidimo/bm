package com.piramide.elwis.domain.contactmanager;

import java.io.Serializable;

/**
 * Contact Person Entity bean primary key class.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ContactPersonPK implements Serializable {
    public Integer addressId;
    public Integer contactPersonId;

    public ContactPersonPK() {
    }

    public ContactPersonPK(Integer addressId, Integer contactPersonId) {
        this.addressId = addressId;
        this.contactPersonId = contactPersonId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactPersonPK)) {
            return false;
        }

        final ContactPersonPK contactPersonPK = (ContactPersonPK) o;

        if (addressId != null ? !addressId.equals(contactPersonPK.addressId) : contactPersonPK.addressId != null) {
            return false;
        }
        if (contactPersonId != null ? !contactPersonId.equals(contactPersonPK.contactPersonId) : contactPersonPK.contactPersonId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (addressId != null ? addressId.hashCode() : 0);
        result = 29 * result + (contactPersonId != null ? contactPersonId.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "[addressId= " + addressId + ", contactPersonId= " + contactPersonId + "]";
    }
}
