package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 13, 2005
 * Time: 4:46:18 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing create article  from question command ");
        log.debug("Operation = " + getOp());
        ArticleDTO articleDTO = new ArticleDTO(paramDTO);
        ArticleHome articleHome = (ArticleHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLE);
        ArticleQuestionHome questionHome = (ArticleQuestionHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLEQUESTION);
        Article article = null;
        ArticleQuestion question = null;
        SupportFreeText freeText = null;

        //para verificar si la pregunta ya fue respondida por otra persona, solo puede crearse un articulo por pregunta.
        if (paramDTO.get("rootQuestionId") != null && !"".equals(paramDTO.get("rootQuestionId"))) {
            try {
                article = articleHome.findByQuestionKey(new Integer(paramDTO.get("rootQuestionId").toString()));
            } catch (FinderException e) {
            }
        }

        if (article != null) {
            resultDTO.isFailure();
            resultDTO.addResultMessage("Article.question.hasResponded", paramDTO.get("summary"));
            resultDTO.setForward("Error");
            return;
        }
        //para verificar si question ya fue eliminado por otro usuario antes de crear un articulo en base a este.
        if (paramDTO.get("rootQuestionId") != null && !"".equals(paramDTO.get("rootQuestionId"))) {
            try {
                question = questionHome.findByPrimaryKey(new Integer(paramDTO.get("rootQuestionId").toString()));
            } catch (FinderException e) {
            }
        }

        if (question == null && paramDTO.get("rootQuestionId") != null && !"".equals(paramDTO.get("rootQuestionId"))) {
            resultDTO.isFailure();
            resultDTO.addResultMessage("customMsg.NotFound", paramDTO.get("summary"));
            resultDTO.setForward("Error");
            return;
        }


        try {
            SupportFreeTextHome frHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
            freeText = frHome.create(paramDTO.get("content").toString().getBytes(),
                    new Integer(paramDTO.get("companyId").toString()),
                    new Integer(FreeTextTypes.FREETEXT_SUPPORT));
            articleDTO.put("contentId", freeText.getFreeTextId());
        } catch (CreateException e) {
            e.printStackTrace();
        }
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User u = null;
        DateTimeZone zone = null;
        String timeZone = null;
        try {
            u = userHome.findByPrimaryKey(new Integer(paramDTO.get("createUserId").toString()));
        } catch (FinderException e) {
        }
        if (u != null) {
            timeZone = u.getTimeZone();
            zone = DateTimeZone.forID(timeZone);
        }
        if (timeZone == null) {
            zone = DateTimeZone.getDefault();
        }
        DateTime createDateTime = new DateTime(zone);
        articleDTO.put("createDateTime", new Long(createDateTime.getMillis()));
        articleDTO.put("updateDateTime", new Long(createDateTime.getMillis()));
        articleDTO.put("visitDateTime", new Long(createDateTime.getMillis()));
        articleDTO.put("createUserId", paramDTO.get("createUserId"));
        articleDTO.put("updateUserId", paramDTO.get("createUserId"));
        articleDTO.put("viewTimes", new Integer(0));
        articleDTO.put("vote1", new Integer(0));
        articleDTO.put("vote2", new Integer(0));
        articleDTO.put("vote3", new Integer(0));
        articleDTO.put("vote4", new Integer(0));
        articleDTO.put("vote5", new Integer(0));

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        String newNumber = InvoiceUtil.i.getArticleNumber(companyId);
        if (null != newNumber) {
            articleDTO.put("number", newNumber);
        }

        article = (Article) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, articleDTO, resultDTO, false, false, false, false);
        if (article != null && !resultDTO.isFailure()) {
            resultDTO.put("articleId", article.getArticleId());
            if (null == article.getNumber() || "".equals(article.getNumber().trim())) {
                article.setNumber(article.getArticleId().toString());
            }

//for articleHistory
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO dto = new ArticleHistoryDTO();
            dto.put("userId", paramDTO.get("createUserId"));
            dto.put("companyId", article.getCompanyId());
            dto.put("articleId", article.getArticleId());
            dto.put("logDateTime", article.getCreateDateTime());
            dto.put("action", SupportConstants.CREATE_ARTICLE);
            cmd.createHistory(dto);

            //article user access
            userArticleAccessRight(article, ctx);
        }
    }

    private void userArticleAccessRight(Article article, SessionContext ctx) {
        String userGroupIds = (String) paramDTO.get("articleAccessUserGroupIds");

        List<Integer> userGroupIdList = ArticleAccessRightCmdUtil.getUserGroupIdList(userGroupIds);

        UserArticleAccessCmd userArticleAccessCmd = new UserArticleAccessCmd();
        userArticleAccessCmd.setOp("assignArticleUserGroups");
        userArticleAccessCmd.putParam("articleId", article.getArticleId());
        userArticleAccessCmd.putParam("companyId", article.getCompanyId());
        userArticleAccessCmd.putParam("articleAccessUserGroupIdList", userGroupIdList);
        userArticleAccessCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = userArticleAccessCmd.getResultDTO();

        List<Integer> resultUserGroupIdList = (List<Integer>) myResultDTO.get("articleAccessUserGroupIdList");

        //update article access right status
        boolean isAccessCreatorUser = ArticleAccessRightCmdUtil.existCreatorUserItem(userGroupIds);
        if (isAccessCreatorUser) {
            article.setPublishedTo(SupportConstants.ArticlePublishedToStatus.CREATOR_USER.getConstant());
            resultUserGroupIdList.add(SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY);
        } else if (resultUserGroupIdList.isEmpty()) {
            article.setPublishedTo(SupportConstants.ArticlePublishedToStatus.ALL_USERS.getConstant());
        } else {
            article.setPublishedTo(null);
        }

        resultDTO.put("articleAccessUserGroupIds", ArticleAccessRightCmdUtil.composeIdListAsStringValue(resultUserGroupIdList));
    }


    public boolean isStateful() {
        return false;
    }
}
