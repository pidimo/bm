package com.piramide.elwis.domain.financemanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderTextPK implements Serializable {
    public Integer languageId;
    public Integer reminderLevelId;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ReminderTextPK that = (ReminderTextPK) o;

        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (reminderLevelId != null ? !reminderLevelId.equals(that.reminderLevelId) : that.reminderLevelId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (languageId != null ? languageId.hashCode() : 0);
        result = 31 * result + (reminderLevelId != null ? reminderLevelId.hashCode() : 0);
        return result;
    }
}
