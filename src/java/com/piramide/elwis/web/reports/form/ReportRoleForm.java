package com.piramide.elwis.web.reports.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;

import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class ReportRoleForm extends SearchForm {

    List selectedRoles = new ArrayList();

    public List getList() {
        return selectedRoles;
    }

    public Object[] getSelectedRoles() {
        return selectedRoles.toArray();
    }

    public void setSelectedRoles(Object[] selectedRoles) {
        if (null != selectedRoles) {
            this.selectedRoles = Arrays.asList(selectedRoles);
        }
    }

    Map dto;

    public ReportRoleForm() {
        this.dto = new HashMap();
    }

    public void setDto(String key, Object value) {
        dto.put(key, value);
    }

    public Object getDto(String key) {
        return dto.get(key);
    }

    public Map getDtoMap() {
        return dto;
    }
}
