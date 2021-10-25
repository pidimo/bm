package com.piramide.elwis.web.supportmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 19, 2005
 * Time: 2:48:15 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(ArticleForm.class);

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);// validating with super class
        log.debug("errors... " + !errors.isEmpty());

        if (!errors.isEmpty() && "update".equals(getDto("op")) && "true".equals(getDto("voted"))) {
            request.setAttribute("jsLoad", "onLoad=\"isRating()\"");
        }

        return errors;
    }
}

