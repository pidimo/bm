package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.supportmanager.ArticleCategoryDTO;
import com.piramide.elwis.dto.supportmanager.ArticleQuestionDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 22, 2005
 * Time: 11:31:07 AM
 * To change this template use File | Settings | File Templates.
 */

public class QuestionCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing QuestionCmd..." + this.getOp());

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        SupportFreeTextHome freeTextHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        ArticleHome articleHome = (ArticleHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLE);
        Article article = null;
        Product product = null;
        User u = null;
        User user = null;
        Address ownerName = null;
        DateTimeZone zone = null;
        String timeZone = null;
        SupportFreeText freeText = null;
        ArticleQuestion question = null;
        String productName = "";
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
            try {
                freeText = freeTextHome.create(paramDTO.get("detail").toString().getBytes(),
                        new Integer(paramDTO.get("companyId").toString()),
                        new Integer(FreeTextTypes.FREETEXT_SUPPORT));
                paramDTO.put("detailId", freeText.getFreeTextId());
            } catch (CreateException e) {
                e.printStackTrace();
            }
            paramDTO.put("createDateTime", new Long(createDateTime.getMillis()));
            paramDTO.put("createUserId", paramDTO.get("userId"));
        }
        super.setOp(this.getOp());
        super.checkDuplicate = false;
        super.isClearingForm = false;
        super.checkReferences = true;

        question = (ArticleQuestion) super.execute(ctx, new ArticleQuestionDTO(paramDTO));

        if (!resultDTO.isFailure() && "update".equals(getOp()) && question != null) {
            question.getSupportFreeText().setFreeTextValue(paramDTO.get("detail").toString().getBytes());
        }
        if (question != null) {
            try {
                article = articleHome.findByQuestionKey(question.getQuestionId());
            } catch (FinderException e) {
            }
        }

        if ("".equals(getOp()) && question != null) {
            if (question.getSupportFreeText() != null) {
                resultDTO.put("detail", new String(question.getSupportFreeText().getFreeTextValue()));
                try {
                    user = userHome.findByPrimaryKey(question.getCreateUserId());
                    ownerName = addressHome.findByPrimaryKey(user.getAddressId());
                } catch (FinderException e) {
                    log.debug("user create notFound ... " + e);
                }
                if (ownerName != null)  //el usuario creador
                {
                    resultDTO.put("ownerName", ownerName.getName());
                }
            }

            if (question.getProductId() != null) {
                try {
                    product = productHome.findByPrimaryKey(question.getProductId());
                    resultDTO.put("productName", product.getProductName());
                    productName = product.getProductName();
                } catch (FinderException e) {
                    log.debug("product notFound ... " + e);
                }
            }
            // verifica si ya esta respondida
            if (article == null) {
                resultDTO.put("article", new Boolean(true));
            } else {
                resultDTO.put("article", new Boolean(false));
            }
            // for sen notification
        } else if (("create".equals(getOp()) || "update".equals(getOp())) && question != null) {

            if (question.getProductId() != null && question.getPublished().intValue() == 0) {

                Collection users = null;
                Address address = null;
                User userCreate = null;
                boolean emptyMail = false;
                StringBuffer sendMails = new StringBuffer();
                SupportUserHome supportHome = (SupportUserHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_USER);
                UserHome uHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
                AddressHome userNameHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                try {
                    userCreate = uHome.findByPrimaryKey(question.getCreateUserId());
                    users = supportHome.findAllByProduct(question.getProductId(), question.getCompanyId());
                    address = userNameHome.findByPrimaryKey(userCreate.getAddressId());

                    for (Iterator iterator = users.iterator(); iterator.hasNext();) {

                        SupportUser supportUser = null;
                        supportUser = (SupportUser) iterator.next();
                        user = null;
                        user = userHome.findByPrimaryKey(supportUser.getUserId());

                        if (user != null) {
                            if (article != null) {
                                if (user.getUserId().equals(article.getCreateUserId())) {
                                    sendMails.append(user.getNotificationQuestionEmail());
                                    emptyMail = true;
                                }
                            } else {
                                if (user.getNotificationQuestionEmail() != null) {
                                    if (sendMails.length() > 0) {
                                        sendMails.append(", ");
                                    }
                                    sendMails.append(user.getNotificationQuestionEmail());
                                    emptyMail = true;
                                }
                            }
                        }
                    }
                    // for send MAILS - notifications users ...........
                    if (question.getProductId() != null) {
                        try {
                            product = productHome.findByPrimaryKey(question.getProductId());
                            productName = product.getProductName();
                        } catch (FinderException e) {
                            log.debug("product notFound ... " + e);
                        }
                    }
                    if (emptyMail) {
                        sendNotification(productName, question, address.getName(), userCreate.getNotificationQuestionEmail(), sendMails.toString());
                        resultDTO.put("sendNotification", SupportConstants.TRUE_VALUE);
                    }
                } catch (FinderException e) {
                }
            }

            if (resultDTO.isFailure()) {
                resultDTO.put("ownerName", paramDTO.get("ownerName"));
                if (question != null) {
                    resultDTO.put("detail", new String(question.getSupportFreeText().getFreeTextValue()));
                }
                resultDTO.setResultAsFailure();
            }
        }
    }


    public void sendNotification(String productName, ArticleQuestion question, String addressName, String mailFrom, String userSend) throws FinderException {
        Map templateParameters = new HashMap();
        Map mailParameters = new HashMap();
        String from = mailFrom;
        mailParameters.put("mails", userSend);
        mailParameters.put("message", new String(question.getSupportFreeText().getFreeTextValue()).replaceAll("\\n", "<br>"));
        mailParameters.put("subject", question.getSummary());
        mailParameters.put("emailTo", userSend);
        mailParameters.put("fromPersonal", addressName);
        mailParameters.put("productName", productName);

        if (mailFrom != null) {
            StringTokenizer st = new StringTokenizer(mailFrom, ",");
            if (st.hasMoreTokens()) {
                from = st.nextToken();
            }
        }
        mailParameters.put("emailFrom", from);
        templateParameters.put("askedBy", addressName);
        templateParameters.put("emailTo", userSend);
        templateParameters.put("summary", question.getSummary());
        templateParameters.put("category", (question.getCategoryId() != null) ? getCategoryName(question.getCategoryId()) : "");
        templateParameters.put("productName", productName);
        templateParameters.put("askedOn", question.getCreateDateTime());
        templateParameters.put("description", new String(question.getSupportFreeText().getFreeTextValue()).replaceAll("\\n", "<br>"));
        templateParameters.put("subject", question.getSummary());

        resultDTO.put("TEMPLATE_PARAMETERS", templateParameters);
        resultDTO.put("MAIL_PARAMETERS", mailParameters);
    }

    private String getCategoryName(Integer categoryId) {
        ArticleCategory articleCategory = (ArticleCategory) EJBFactory.i.findEJB(new ArticleCategoryDTO(categoryId));
        return articleCategory.getCategoryName();
    }

    public boolean isStateful() {
        return false;
    }
}



