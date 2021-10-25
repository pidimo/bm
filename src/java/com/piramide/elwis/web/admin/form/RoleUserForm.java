package com.piramide.elwis.web.admin.form;


import org.alfacentauro.fantabulous.web.form.SearchForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : ivan
 * @version : $Id RoleUserForm ${time}
 */
public class RoleUserForm extends SearchForm {

    boolean isSubmit = false;
    List aditionals = new ArrayList();


    public Object[] getAditionals() {

        return aditionals.toArray();
    }

    public void setAditionals(Object[] array21) {
        if (array21 != null) {
            aditionals = Arrays.asList(array21);
        }
    }

    public void setIsSubmit(String submit) {
        isSubmit = Boolean.valueOf(submit).booleanValue();
    }

    public String getIsSubmit() {
        return Boolean.valueOf(isSubmit).toString();
    }
}
