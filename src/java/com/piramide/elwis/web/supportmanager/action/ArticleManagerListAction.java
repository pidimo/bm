package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelSecurity;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.struts.action.*;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 17, 2005
 * Time: 3:13:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ArticleManagerListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if ("true".equals(request.getParameter("cancel"))) {
            log.debug("cancel . articleManagerListAction");
            return mapping.findForward("Cancel");
        }
        log.debug("Article list action execution...");
        //cheking if working address was not deleted by other user.

        log.debug("articleId for user session  = " + request.getParameter("articleId"));
        ActionErrors errors = new ActionErrors();
        User user = RequestUtils.getUser(request);
        String articleId;
        DateTimeZone timeZone;

        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }
        request.setAttribute("timeZone", timeZone);

        if ("".equals(request.getParameter("articleId"))) {
            articleId = request.getAttribute("articleId").toString();
        } else {
            articleId = request.getParameter("articleId");
        }
        if (request.getParameter("articleId") != null) {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setPrimKey(articleId);
            try {
                EJBFactory.i.findEJB(articleDTO); //article already exists
            } catch (EJBFactoryException e) {
                if (e.getCause() instanceof FinderException) {
                    log.debug("The article was deleted by other user... show errors and " +
                            "return to articleList search page");
                    errors.add("articleSessionNotFound", new ActionError("error.ArticleSession.NotFound"));
                    saveErrors(request, errors);
                    return mapping.findForward("MainSearch");
                }
            }
        } else { //if campaignId not found
            errors.add("articleSessionNotFound", new ActionError("error.ArticleSession.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        log.debug("Final AddSeach:");
        addFilter("articleId", articleId);
        setModuleId("article", articleId);

        return super.execute(mapping, form, request, response);
    }

    @Override
    public ListStructure getListStructure() throws Exception {
        return AccessRightDataLevelSecurity.i.processAccessRightByList(super.getListStructure(), userId, AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS);
    }

}

