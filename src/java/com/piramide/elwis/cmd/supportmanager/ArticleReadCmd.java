package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.supportmanager.Article;
import com.piramide.elwis.domain.supportmanager.ArticleRating;
import com.piramide.elwis.domain.supportmanager.ArticleRatingHome;
import com.piramide.elwis.dto.supportmanager.ArticleDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ProductConstants;
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
 * Date: Aug 15, 2005
 * Time: 10:43:55 AM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read Article command");
        log.debug("Operation = " + getOp());
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ArticleRatingHome ratingHome = (ArticleRatingHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLERATING);
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        Address ownerName = null;
        Address changeName = null;
        Product product = null;
        ArticleRating articleRating = null;
        User user = null;
        User u = null;
        User changeUser = null;
        String timeZone = null;
        DateTimeZone zone = null;
        DateTime visitDateTime = null;
        StringBuffer addressName = new StringBuffer();


        Article article = (Article) ExtendedCRUDDirector.i.read(new ArticleDTO(paramDTO), resultDTO, false);
        //para incrementar las visitas a este  articulo.
        if (article != null && !resultDTO.isFailure()) {
            if (!paramDTO.get("userId").equals(article.getCreateUserId())) {
                article.setViewTimes(new Integer(article.getViewTimes().intValue() + 1));
            }
            if (!"<br />".equals(new String(article.getSupportFreeText().getFreeTextValue()).trim())) {
                resultDTO.put("content", new String(article.getSupportFreeText().getFreeTextValue()));
            }
            try {
                user = userHome.findByPrimaryKey(article.getCreateUserId());
                changeUser = userHome.findByPrimaryKey(article.getUpdateUserId());
                u = userHome.findByPrimaryKey(new Integer(paramDTO.get("userId").toString()));
                ownerName = addressHome.findByPrimaryKey(user.getAddressId());
                changeName = addressHome.findByPrimaryKey(changeUser.getAddressId());
                articleRating = ratingHome.findByArticleIdAndUserId(new Integer(paramDTO.get("userId").toString()), article.getArticleId());
                timeZone = u.getTimeZone();
                zone = DateTimeZone.forID(timeZone);
                if (timeZone == null) {
                    zone = DateTimeZone.getDefault();
                }
                visitDateTime = new DateTime(zone);
            } catch (FinderException e) {
                log.debug("user or addressName  or articleRating notFound ");
            } // para actualizar la ultima visita para el siguiente usuario que venga a ver
            visitDateTime = new DateTime(zone);
            article.setVisitDateTime(new Long(visitDateTime.getMillis()));

            if (articleRating != null) {
                resultDTO.put("voted", "true");
            }

            if (user != null) { //el usuario creador
                addressName = new StringBuffer();
                addressName.append(ownerName.getName1());
                if (ownerName.getName2() != null && ownerName.getName2() != "") {
                    addressName.append(", ").append(ownerName.getName2());
                }
                resultDTO.put("ownerName", addressName.toString());
            }

            if (changeUser != null) {//el usuario que fue el ultimo en modificar el articulo.
                addressName = new StringBuffer();
                addressName.append(changeName.getName1());
                if (changeName.getName2() != null && changeName.getName2() != "") {
                    addressName.append(", ").append(changeName.getName2());
                }
                resultDTO.put("changeName", addressName.toString());
            }

            //for productName
            if (article.getProductId() != null) {
                try {
                    product = productHome.findByPrimaryKey(article.getProductId());
                    resultDTO.put("productName", product.getProductName());
                } catch (FinderException e) {
                    log.debug("product notFound ..." + product);
                }
            }
            resultDTO.put("readyBy", article.getViewTimes());
            resultDTO.put("number", article.getNumber());

            //**********      para las estrellitas      ***********
            int value, p = 0;
            long position = 0;
            int vote1, vote2, vote3, vote4, vote5 = 0;
            vote1 = article.getVote1().intValue();
            vote2 = article.getVote2().intValue();
            vote3 = article.getVote3().intValue();
            vote4 = article.getVote4().intValue();
            vote5 = article.getVote5().intValue();
            value = vote1 + vote2 + vote3 + vote4 + vote5;
            p = vote1 * 1 + vote2 * 2 + vote3 * 3 + vote4 * 4 + vote5 * 5;
            if (value > 0) {
                position = p / value;
            }
            Integer pos = new Integer(Math.round(position) - 1);
            Integer noPos = new Integer(4 - Math.round(position));
            resultDTO.put("position", pos);
            resultDTO.put("noPosition", noPos);

            //read article user access
            readUserArticleAccessRight(article, ctx);
        }
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


    public boolean isStateful() {
        return false;
    }
}
