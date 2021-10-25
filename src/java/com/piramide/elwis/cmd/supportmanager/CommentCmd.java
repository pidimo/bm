package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.supportmanager.ArticleComment;
import com.piramide.elwis.domain.supportmanager.SupportFreeText;
import com.piramide.elwis.domain.supportmanager.SupportFreeTextHome;
import com.piramide.elwis.dto.supportmanager.ArticleCommentDTO;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 23, 2005
 * Time: 9:12:42 AM
 * To change this template use File | Settings | File Templates.
 */

public class CommentCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CommentCmd ..." + this.getOp());
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address ownerName = null;
        User u = null;
        User user = null;
        DateTimeZone zone = null;
        String timeZone = null;
        SupportFreeText freeText = null;
        try {
            u = userHome.findByPrimaryKey(new Integer(paramDTO.get("userId").toString()));
        } catch (FinderException e) {
            log.debug("userSession notFound ... " + e);
        }
        if (u != null) {
            timeZone = u.getTimeZone();
            zone = DateTimeZone.forID(timeZone);
        }
        if (timeZone == null) {
            zone = DateTimeZone.getDefault();
        }
        DateTime createDateTime = new DateTime(zone);

        if (CRUDDirector.OP_CREATE.equals(getOp())) {
            paramDTO.put("createUserId", paramDTO.get("userId"));
            try {
                SupportFreeTextHome frHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);

                freeText = frHome.create(paramDTO.get("description").toString().getBytes(),
                        new Integer(paramDTO.get("companyId").toString()),
                        new Integer(FreeTextTypes.FREETEXT_SUPPORT));
                paramDTO.put("descriptionId", freeText.getFreeTextId());
            } catch (CreateException e) {
                e.printStackTrace();
            }
            paramDTO.put("createDateTime", new Long(createDateTime.getMillis()));
        }
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        super.checkVersion = false;

        ArticleComment comment = (ArticleComment) super.execute(ctx, new ArticleCommentDTO(paramDTO));

        if ("".equals(getOp()) && comment != null) {
            if (comment.getSupportFreeText() != null) {
                resultDTO.put("detail", new String(comment.getSupportFreeText().getFreeTextValue()));
                try {
                    user = userHome.findByPrimaryKey(comment.getCreateUserId());
                    ownerName = addressHome.findByPrimaryKey(user.getAddressId());
                } catch (FinderException e) {
                    log.debug("userCreate comment notFound ... ");
                }
                if (ownerName != null) {
                    resultDTO.put("ownerName", ownerName.getName());
                }
            }
        }
        //for History
        if (!"".equals(getOp()) && !resultDTO.isFailure()) {
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO articleHistoryDTO = new ArticleHistoryDTO();
            articleHistoryDTO.put("companyId", paramDTO.get("companyId"));
            articleHistoryDTO.put("articleId", paramDTO.get("articleId"));
            articleHistoryDTO.put("action", SupportConstants.CREATE_COMMENT);
            articleHistoryDTO.put("userId", paramDTO.get("userId"));
            cmd.createHistory(articleHistoryDTO);
        }
        if (resultDTO.isFailure()) {
            // para setear el listado de comentarios en caso de que falle....
            CommentReadListCmd cmd = new CommentReadListCmd();
            cmd.putParam("articleId", paramDTO.get("articleId"));
            cmd.executeInStateless(ctx);
            resultDTO.put("commentList", cmd.getResultDTO().get("commentList"));
            if (cmd.getResultDTO().isFailure()) {
                Iterator iterator = cmd.getResultDTO().getResultMessages();
                while (iterator.hasNext()) {
                    resultDTO.addResultMessage((ResultMessage) iterator.next());
                }
                resultDTO.setResultAsFailure();
            }
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}