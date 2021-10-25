package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.supportmanager.Article;
import com.piramide.elwis.domain.supportmanager.ArticleHome;
import com.piramide.elwis.domain.supportmanager.ArticleLink;
import com.piramide.elwis.dto.supportmanager.ArticleHistoryDTO;
import com.piramide.elwis.dto.supportmanager.ArticleLinkDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 17, 2005
 * Time: 3:31:08 PM
 * To change this template use File | Settings | File Templates.
 */

public class LinkCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ArticleLinkCmd..." + this.getOp());

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ArticleHome articleHome = (ArticleHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLE);
        User u = null;
        Article article = null;
        User user = null;
        Address ownerName = null;
        StringBuffer addressName = new StringBuffer();
        DateTimeZone zone = null;
        String timeZone = null;
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
        DateTime createDateTime = new DateTime(zone);
        if (CRUDDirector.OP_CREATE.equals(getOp())) {
            paramDTO.put("createDateTime", new Long(createDateTime.getMillis()));
        }

        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;

        ArticleLink articleLink = (ArticleLink) super.execute(ctx, new ArticleLinkDTO(paramDTO));

        if ("".equals(getOp()) && articleLink != null) {
            try {
                user = userHome.findByPrimaryKey(articleLink.getCreateUserId());
                article = articleHome.findByPrimaryKey(new Integer(paramDTO.get("articleId").toString()));
                ownerName = addressHome.findByPrimaryKey(user.getAddressId());
            } catch (FinderException e) {
                log.debug("userCreate link or article or ownerName not found ... " + e);
            }

            if (user != null) { //el usuario creador
                addressName = new StringBuffer();
                addressName.append(ownerName.getName1());
                if (ownerName.getName2() != null && ownerName.getName2() != "") {
                    addressName.append(", ").append(ownerName.getName2());
                }
                resultDTO.put("ownerName", addressName.toString());
            }
        }
        //for History
        if (!"".equals(getOp()) && !resultDTO.isFailure()) {
            HistoryCmd cmd = new HistoryCmd();
            ArticleHistoryDTO articleHistoryDTO = new ArticleHistoryDTO();
            articleHistoryDTO.put("companyId", paramDTO.get("companyId"));
            articleHistoryDTO.put("articleId", paramDTO.get("articleId"));
            articleHistoryDTO.put("action", paramDTO.get("actionHistory"));
            articleHistoryDTO.put("userId", paramDTO.get("userId"));
            cmd.createHistory(articleHistoryDTO);
        }
        if (article != null) {
            resultDTO.put("articleOwnerId", article.getCreateUserId());
        }
    }

    public boolean isStateful() {
        return false;
    }
}

