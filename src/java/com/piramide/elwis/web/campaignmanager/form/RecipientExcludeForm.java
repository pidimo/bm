package com.piramide.elwis.web.campaignmanager.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: RecipientExcludeForm.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class RecipientExcludeForm extends SearchForm {

    private Map dto;

    public Map getDto() {
        return dto;
    }

    public void setDto(Map dto) {
        this.dto = dto;
    }

    public RecipientExcludeForm() {
        this.dto = new HashMap();
    }

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }


    private Log log = LogFactory.getLog(com.piramide.elwis.web.campaignmanager.form.RecipientExcludeForm.class);

    public Object[] getExcludes() {

        List list = (List) getDto("excludes");


        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setExcludes(Object[] array21) {
        if (array21 != null) {
            setDto("excludes", Arrays.asList(array21));
        }
    }
}

