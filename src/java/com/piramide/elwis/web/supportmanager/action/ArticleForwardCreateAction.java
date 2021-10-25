package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 17, 2005
 * Time: 11:05:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleForwardCreateAction extends CheckEntriesForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("--------  ArticleForwardCreateAction executeFunction  --------");
        DefaultForm articleForm = (DefaultForm) form;
        return super.execute(mapping, articleForm, request, response);
    }
}
