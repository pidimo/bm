package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 13, 2005
 * Time: 4:47:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing delete article command ... " + paramDTO);

        ArticleRelatedHome relatedHome = (ArticleRelatedHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLERELATED);
        Collection collection = new ArrayList(0);
        try {
            collection = relatedHome.findByArticleId(new Integer(paramDTO.get("articleId").toString()), new Integer(paramDTO.get("companyId").toString()));
        } catch (FinderException e) {
            log.debug("articleRelated for this article notFound ... " + e);
        }

        if (collection.size() > 0) {
            resultDTO.isFailure();
            resultDTO.addResultMessage("Article.relatedMessage", paramDTO.get("articleTitle"));
            resultDTO.setForward("Fail");
            return;
        }

        Collection link, history, relation, comment, rating, attachs;
        ArticleDTO articleDTO = new ArticleDTO(paramDTO);
        Article article = null;
        try {
            article = (Article) EJBFactory.i.findEJB(articleDTO);
            if (article != null && !resultDTO.isFailure()) {
                link = article.getArticleLink();
                history = article.getArticleHistory();
                relation = article.getArticleRelated();
                comment = article.getArticleComment();
                rating = article.getArticleRating();
                attachs = article.getSupportAttach();

                log.debug("Delete LINK ");
                for (Iterator iterator = link.iterator(); iterator.hasNext();) {
                    ((ArticleLink) iterator.next()).remove();
                    iterator = link.iterator();
                }
                log.debug("Delete ARTICLERATING...   ");
                for (Iterator iterator = rating.iterator(); iterator.hasNext();) {
                    ((ArticleRating) iterator.next()).remove();
                    iterator = rating.iterator();
                }


                log.debug("Delete relation...   ");
                for (Iterator iterator = relation.iterator(); iterator.hasNext();) {
                    ((ArticleRelated) iterator.next()).remove();
                    iterator = relation.iterator();
                    ArticleRelated a;
                }

                log.debug("Delete comment...   ");
                for (Iterator iterator = comment.iterator(); iterator.hasNext();) {
                    ((ArticleComment) iterator.next()).remove();
                    iterator = comment.iterator();
                }

                log.debug("Delete history...   ");
                for (Iterator iterator = history.iterator(); iterator.hasNext();) {
                    ((ArticleHistory) iterator.next()).remove();
                    iterator = history.iterator();
                }

                log.debug("Delete attachs...   ");
                for (Iterator iterator = attachs.iterator(); iterator.hasNext();) {
                    SupportAttach attach = (SupportAttach) iterator.next();
                    try {
                        SupportFreeTextHome freeTextHome =
                                (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);

                        SupportFreeText freeText = freeTextHome.findByPrimaryKey(attach.getFreetextId());
                        attach.remove();
                        freeText.remove();
                        iterator = attachs.iterator();
                    } catch (RemoveException e) {
                        log.warn("Cannot remove Freetext...");
                        ctx.setRollbackOnly();
                        return;
                    } catch (FinderException e1) {
                        log.warn("Cannot find Freetext...");
                        ctx.setRollbackOnly();
                        return;
                    }
                }

                Integer contentId = article.getContentId();

                article.remove();  //remove below the additional not CMR relationships

                removeArticleContent(contentId, ctx);
            }
        } catch (RemoveException e) {
            log.debug("Article to delete cannot be found...");
            ctx.setRollbackOnly();//invalid the transaction
            articleDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
        }
    }

    private void removeArticleContent(Integer contentId, SessionContext ctx) throws RemoveException {
        log.debug("remove content...");

        if (contentId != null) {
            SupportFreeTextHome freeTextHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
            try {
                SupportFreeText contentFreeText = freeTextHome.findByPrimaryKey(contentId);
                contentFreeText.remove();
            } catch (FinderException e) {
                log.debug("Not found article content:" + contentId, e);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}

