package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.supportmanager.ArticleRelated;
import com.piramide.elwis.domain.supportmanager.ArticleRelatedHome;
import com.piramide.elwis.domain.supportmanager.ArticleRelatedPK;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.dto.supportmanager.ArticleRelatedDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 18, 2005
 * Time: 4:10:57 PM
 * To change this template use File | Settings | File Templates.
 */

public class RelationCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ArticleRelatedCmd  ..." + this.getOp());

        ArticleRelatedHome relatedHome = (ArticleRelatedHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLERELATED);
        ArticleRelated related = null;
        ArticleRelatedDTO relatedDTO = new ArticleRelatedDTO(paramDTO);
        if ("create".equals(getOp())) {
            //para verificar que no relacione articulos que ya estan relacionados.
            ArticleRelated articleRelated = null;
            ArticleRelatedPK pk = new ArticleRelatedPK();
            pk.articleId = new Integer(paramDTO.get("articleId").toString());
            pk.relatedArticleId = new Integer(paramDTO.get("relatedArticleId").toString());
            try {
                articleRelated = relatedHome.findByPrimaryKey(pk);
            } catch (FinderException e) {
                log.debug("articleRelated notFound ... " + e);
            }
            if (articleRelated != null) {
                resultDTO.isFailure();
                resultDTO.addResultMessage("msg.Duplicated", paramDTO.get("articleName"));
                resultDTO.setForward("Fail");
                return;
            }
            relatedDTO.put("actionHistory", "9");
        } else {
            ArticleRelatedPK pk = new ArticleRelatedPK();
            pk.articleId = new Integer(paramDTO.get("articleId").toString());
            pk.relatedArticleId = new Integer(paramDTO.get("relatedArticleId").toString());
            relatedDTO.setPrimKey(pk);
            relatedDTO.put("actionHistory", "11");
        }

        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        super.checkReferences = false;
        related = (ArticleRelated) super.execute(ctx, relatedDTO);

        //for History
        if (!"".equals(getOp()) && !resultDTO.isFailure()) {
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO articleHistoryDTO = new ArticleHistoryDTO();
            articleHistoryDTO.put("companyId", paramDTO.get("companyId"));
            articleHistoryDTO.put("articleId", relatedDTO.get("articleId"));
            articleHistoryDTO.put("action", relatedDTO.get("actionHistory"));
            articleHistoryDTO.put("userId", paramDTO.get("userId"));
            cmd.createHistory(articleHistoryDTO);
        } else { // para  leer los datos del articulo relacionado
            ArticleReadCmd articleReadCmd = new ArticleReadCmd();
            articleReadCmd.putParam("articleId", paramDTO.get("relatedArticleId"));
            articleReadCmd.putParam("userId", paramDTO.get("userId"));
            articleReadCmd.executeInStateless(ctx);
            resultDTO.putAll(articleReadCmd.getResultDTO());
        }
    }

    public boolean isStateful() {
        return false;
    }
}

