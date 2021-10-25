package com.piramide.elwis.web.webmail.form;

import org.alfacentauro.fantabulous.web.form.SearchForm;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Mar 17, 2005
 * Time: 4:26:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchMailAddressForm extends SearchForm {

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    private String searchType;

    private Map dto = new HashMap();

    public void setDto(String s, Object o) {
        dto.put(s, o);
    }

    public Object getDto(String s) {
        return dto.get(s);
    }

}
