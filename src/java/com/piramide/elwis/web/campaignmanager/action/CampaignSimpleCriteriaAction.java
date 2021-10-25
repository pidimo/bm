package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionSimpleForm;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jan 8, 2005
 * Time: 8:12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignSimpleCriteriaAction extends Action {

    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("campaignSimpleCriteriaAction ... execute ... ");
        String field = "";
        String tablename = "";
        String typefield = "";
        String fieldname = "";
        ArrayList list = new ArrayList();

        String value = request.getAttribute("values").toString();

        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        field = tokenizer.nextToken();
        tablename = tokenizer.nextToken();
        typefield = tokenizer.nextToken();
        fieldname = tokenizer.nextToken();
        String title = tokenizer.nextToken();
        String campCriterionValueId = tokenizer.nextToken();

        DefaultForm criterionForm = (CampaignCriterionSimpleForm) form;
        criterionForm.setDto("campCriterionValueId", campCriterionValueId);
        criterionForm.setDto("titlePage", title);
        criterionForm.setDto("typefield", typefield);
        criterionForm.setDto("tablename", tablename);
        criterionForm.setDto("field", field);

        return mapping.findForward("Success");
    }
}

