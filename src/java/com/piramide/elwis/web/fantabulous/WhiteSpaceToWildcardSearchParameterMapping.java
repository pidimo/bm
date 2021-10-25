package com.piramide.elwis.web.fantabulous;

import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 4.5
 */
public class WhiteSpaceToWildcardSearchParameterMapping extends ActionMapping {
    private List<String> parameterNames;
    private static Log log = LogFactory.getLog(WhiteSpaceToWildcardSearchParameterMapping.class);

    public WhiteSpaceToWildcardSearchParameterMapping() {
        super();
        parameterNames = new ArrayList<String>();
    }


    public void setParameterName(String parameterName) {
        this.parameterNames.add(parameterName);
    }

    public void preProcessParameters(ActionForm form) {
        SearchForm searchForm = (SearchForm) form;
        log.debug("Setting white spaces to wildcard (%): " + parameterNames);
        for (String param : parameterNames) {
            if (searchForm.getParameter(param) != null) {
                searchForm.setParameter(param, ((String) searchForm.getParameter(param)).trim().replaceAll(" ", "%"));
            }
        }
    }

    public void posProcessParameters(ActionForm form) {
        SearchForm searchForm = (SearchForm) form;
        log.debug("Setting wildcard (%) to white spaces: " + parameterNames);
        for (String param : parameterNames) {
            if (searchForm.getParameter(param) != null) {
                searchForm.setParameter(param, ((String) searchForm.getParameter(param)).trim().replaceAll("%", " ").trim());
            }
        }
    }

}
