package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.supportmanager.Article;
import com.piramide.elwis.domain.supportmanager.ArticleHome;
import com.piramide.elwis.domain.supportmanager.ArticleRatingPK;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.dto.supportmanager.ArticleRatingDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 13, 2005
 * Time: 4:47:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update article command");
        log.debug("Operation = " + getOp());
        ArticleHome articleHome = (ArticleHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLE);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User u = null;
        DateTimeZone zone = null;
        String timeZone = null;
        Article article = null;
        ArticleDTO articleDTO = new ArticleDTO(paramDTO);

        try {
            article = articleHome.findByPrimaryKey(new Integer(paramDTO.get("articleId").toString()));
        } catch (FinderException e) {
            log.debug("Article notFound ... ");
        }

        if (article != null) {
            if (!SchedulerConstants.TRUE_VALUE.equals(paramDTO.get("rating"))) {
                article = (Article) ExtendedCRUDDirector.i.update(articleDTO, resultDTO, false, true, false, "Fail");
                if (!resultDTO.isFailure()) {
                    article.getSupportFreeText().setFreeTextValue(paramDTO.get("content").toString().getBytes());

                    //user article access
                    userArticleAccessRight(article, ctx);
                }

            } else { //para rating ...
                if ("1".equals(paramDTO.get("rate"))) {
                    article.setVote1(new Integer(article.getVote1().intValue() + 1));
                } else if ("2".equals(paramDTO.get("rate"))) {
                    article.setVote2(new Integer(article.getVote2().intValue() + 1));
                } else if ("3".equals(paramDTO.get("rate"))) {
                    article.setVote3(new Integer(article.getVote3().intValue() + 1));
                } else if ("4".equals(paramDTO.get("rate"))) {
                    article.setVote4(new Integer(article.getVote4().intValue() + 1));
                } else if ("5".equals(paramDTO.get("rate"))) {
                    article.setVote5(new Integer(article.getVote5().intValue() + 1));
                } else {
                    log.debug(" ... no press button to evaluate ... ");
                    resultDTO.setResultAsFailure();
                    resultDTO.addResultMessage("Article.ratingMessage");
                    resultDTO.setForward("Redirect");
                    return;
                }
                //rating
                ArticleRatingPK pk = new ArticleRatingPK();
                ArticleRatingDTO ratingDTO = new ArticleRatingDTO(paramDTO);
                pk.articleId = article.getArticleId();
                pk.userId = new Integer(paramDTO.get("userId").toString());
                ratingDTO.setPrimKey(pk);
                ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, ratingDTO, resultDTO, false, false, false, false);
            }
            try {
                u = userHome.findByPrimaryKey(new Integer(paramDTO.get("userId").toString()));
            } catch (FinderException e) {
            }
            if (u != null) {
                timeZone = u.getTimeZone();
                zone = DateTimeZone.forID(timeZone);
            }
            if (timeZone == null) {
                zone = DateTimeZone.getDefault();
            }
            DateTime updateDateTime = new DateTime(zone);
//para poner la ultima fecha de actualizacion del articulo ...
            article.setUpdateUserId(new Integer(paramDTO.get("userId").toString()));
            article.setUpdateDateTime(new Long(updateDateTime.getMillis()));

//for articleHistory
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO dto = new ArticleHistoryDTO();
            dto.put("companyId", article.getCompanyId());
            dto.put("articleId", article.getArticleId());
            dto.put("logDateTime", article.getUpdateDateTime());
            dto.put("action", SupportConstants.UPDATE_ARTICLE);
            dto.put("userId", article.getUpdateUserId());
            cmd.createHistory(dto);
        } else {
            resultDTO.put("ownerName", paramDTO.get("ownerName"));
            resultDTO.put("changeName", paramDTO.get("changeName"));
            resultDTO.put("readyBy", paramDTO.get("readyBy"));
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("customMsg.NotFound", paramDTO.get("articleTitle"));
            resultDTO.setForward("Fail");
            return;
        }
        if (resultDTO.isFailure()) {
            if (article != null) {
                resultDTO.put("content", new String(article.getSupportFreeText().getFreeTextValue()));
                //read user access
                readUserArticleAccessRight(article, ctx);
            }
            resultDTO.put("position", paramDTO.get("position"));
            resultDTO.put("voted", paramDTO.get("voted"));
            resultDTO.put("ownerName", paramDTO.get("ownerName"));
            resultDTO.put("changeName", paramDTO.get("changeName"));
            resultDTO.put("productName", paramDTO.get("productName"));
            resultDTO.put("readyBy", paramDTO.get("readyBy"));
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
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

    private void readUserArticleAccessRight(Article article, SessionContext ctx) {
        UserArticleAccessCmd userArticleAccessCmd = new UserArticleAccessCmd();
        userArticleAccessCmd.setOp("readArticleUserGroups");
        userArticleAccessCmd.putParam("articleId", article.getArticleId());

        userArticleAccessCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = userArticleAccessCmd.getResultDTO();

        List<Integer> resultUserGroupIdList = (List<Integer>) myResultDTO.get("articleAccessUserGroupIdList");

        if (article.getPublishedTo() != null && SupportConstants.ArticlePublishedToStatus.CREATOR_USER.equal(article.getPublishedTo())) {
            resultUserGroupIdList.add(SupportConstants.ARTICLE_ACCESS_CREATOR_USER_KEY);
        }

        resultDTO.put("articleAccessUserGroupIds", ArticleAccessRightCmdUtil.composeIdListAsStringValue(resultUserGroupIdList));
    }

}
