package com.piramide.elwis.web.schedulermanager.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Oct 31, 2005
 * Time: 12:07:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParticipantForm extends SearchForm {

    private Map dto;
    String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Map getDto() {
        return dto;
    }

    public void setDto(Map dto) {
        this.dto = dto;
    }

    public ParticipantForm() {
        this.dto = new HashMap();
    }

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }


    private Log log = LogFactory.getLog(ParticipantForm.class);

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
