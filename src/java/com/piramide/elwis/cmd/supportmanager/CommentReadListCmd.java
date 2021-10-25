package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.supportmanager.Article;
import com.piramide.elwis.domain.supportmanager.ArticleComment;
import com.piramide.elwis.domain.supportmanager.ArticleCommentHome;
import com.piramide.elwis.dto.supportmanager.ArticleCommentDTO;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 23, 2005
 * Time: 10:52:57 AM
 * To change this template use File | Settings | File Templates.
 */

public class CommentReadListCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read CommentReadListCMD .... command");

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ArticleCommentHome commentHome = (ArticleCommentHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLECOMMENT);
        Address ownerName;
        User user;
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map> commentList = new ArrayList<Map>();

        Article article = (Article) ExtendedCRUDDirector.i.read(new ArticleDTO(paramDTO), resultDTO, false);

        if (!resultDTO.isFailure()) {
            Collection<ArticleComment> list;
            try {
                //noinspection unchecked
                list = commentHome.findByArticleId(article.getArticleId());

                for (ArticleComment comment : list) {
                    map = new HashMap<String, Object>();
                    if (comment.getSupportFreeText() != null) {
                        map.put("content", new String(comment.getSupportFreeText().getFreeTextValue()));
                    }
                    user = userHome.findByPrimaryKey(comment.getCreateUserId());
                    ownerName = addressHome.findByPrimaryKey(user.getAddressId());
                    map.put("createDateTime", comment.getCreateDateTime());
                    map.put("ownerName", ownerName.getName());
                    map.put("commentId", comment.getCommentId());
                    map.put("userCreateId", comment.getCreateUserId());
                    map.put("articleUserId", article.getCreateUserId());
                    commentList.add(map);
                }
            } catch (FinderException e) {
                new ArrayList<ArticleComment>();
            }
        }

        resultDTO.put("commentList", commentList);

//for delete Comment............
        if ("delete".equals(getOp())) {
            ArticleCommentDTO dto = new ArticleCommentDTO();
            dto.put("commentId", paramDTO.get("commentId"));
            ExtendedCRUDDirector.i.delete(dto, resultDTO, false, "Fail");
            if (resultDTO.hasResultMessage()) {
                resultDTO.setForward("Fail");
            } else {   // for history
                HistoryCmd cmd = new HistoryCmd();
                ArticleHistoryDTO articleHistoryDTO = new ArticleHistoryDTO();
                articleHistoryDTO.put("companyId", paramDTO.get("companyId"));
                articleHistoryDTO.put("articleId", paramDTO.get("articleId"));
                articleHistoryDTO.put("action", SupportConstants.DELETE_COMMENT);
                articleHistoryDTO.put("userId", paramDTO.get("userId"));
                cmd.createHistory(articleHistoryDTO);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
