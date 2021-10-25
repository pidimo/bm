package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.supportmanager.ArticleQuestion;
import com.piramide.elwis.dto.supportmanager.ArticleQuestionDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 22, 2005
 * Time: 3:57:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class QuestionArticleCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ArticleQuestion command");
        log.debug("Operation = " + getOp());
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        Address ownerName = null;
        Product product = null;
        User user = null;
        StringBuffer addressName = new StringBuffer();

        ArticleQuestion question = (ArticleQuestion) ExtendedCRUDDirector.i.read(new ArticleQuestionDTO(paramDTO), resultDTO, false);

        if (question != null && !resultDTO.isFailure()) {

            if (question.getSupportFreeText() != null) {
                if (new String(question.getSupportFreeText().getFreeTextValue()).length() > 39) {
                    resultDTO.put("detail", new String(question.getSupportFreeText().getFreeTextValue()));
                } else {
                    resultDTO.put("detail", new String(question.getSupportFreeText().getFreeTextValue()));
                }
            }
            try {
                user = userHome.findByPrimaryKey(question.getCreateUserId());
                ownerName = addressHome.findByPrimaryKey(user.getAddressId());
            } catch (FinderException e) {
            }
            if (user != null) { //el usuario creador
                addressName = new StringBuffer();
                addressName.append(ownerName.getName1());
                if (ownerName.getName2() != null && ownerName.getName2() != "") {
                    addressName.append(", ").append(ownerName.getName2());
                }
                resultDTO.put("ownerQuestionName", addressName.toString());
            }
            if (question.getProductId() != null) {
                try {
                    product = productHome.findByPrimaryKey(question.getProductId());
                    resultDTO.put("productName", product.getProductName());
                } catch (FinderException e) {
                }
            }
            resultDTO.put("summary", question.getSummary());
            resultDTO.put("createDateTimeQuestion", question.getCreateDateTime());
            resultDTO.put("rootQuestionId", question.getQuestionId());
            resultDTO.put("articleTitle", question.getSummary());
            resultDTO.put("categoryId", question.getCategoryId());
            resultDTO.put("productId", question.getProductId());
            resultDTO.put("question", new Boolean(true));
        }
    }

    public boolean isStateful() {
        return false;
    }
}