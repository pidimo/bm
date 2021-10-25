package com.piramide.elwis.web.admin.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yumi
 * Date: Apr 20, 2005
 * Time: 10:14:41 AM
 * To change this template use File | Settings | File Templates.
 */

public class UserGroupImportForm extends SearchForm {

    private Map dto;
    private Log log = LogFactory.getLog(UserGroupImportForm.class);


    public Map getDto() {
        return dto;
    }

    public void setDto(Map dto) {
        this.dto = dto;
    }

    public UserGroupImportForm() {
        this.dto = new HashMap();
    }

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }

    public Object[] getAditionals() {

        List list = (List) getDto("aditionals");


        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setAditionals(Object[] array21) {
        if (array21 != null) {
            setDto("aditionals", Arrays.asList(array21));
        }
    }
}

