package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.supportmanager.SupportUser;
import com.piramide.elwis.domain.supportmanager.SupportUserHome;
import com.piramide.elwis.domain.supportmanager.SupportUserPK;
import com.piramide.elwis.dto.supportmanager.SupportUserDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 29, 2005
 * Time: 10:07:04 AM
 * To change this template use File | Settings | File Templates.
 */

public class SupportUserCmd extends GeneralCmd {//EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug(" ------ supportUserCMD... execute -------");
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        Address address = null;
        Product product = null;
        User user = null;
        StringBuffer addressName = new StringBuffer();
        SupportUserDTO supportUserDTO = new SupportUserDTO(paramDTO);
        SupportUser supportUser = null;
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        SupportUserHome supportUserHome = (SupportUserHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_USER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        SupportUserPK pk = new SupportUserPK();

        if (!"create".equals(getOp())) {
            pk.productId = new Integer(paramDTO.get("productId").toString());
            pk.userId = new Integer(paramDTO.get("userId").toString());
            supportUserDTO.setPrimKey(pk);
        }

        super.setOp(this.getOp());

        if (!"update".equals(getOp()) && !"create".equals(getOp())) {
            supportUser = (SupportUser) super.execute(ctx, supportUserDTO);
        } else {
            if (paramDTO.get("save") != null) {
                if (!"create".equals(getOp())) {
                    String userid = null;
                    if (paramDTO.get("userId_selected") != null && !"".equals(paramDTO.get("userId_selected"))) {
                        userid = paramDTO.get("userId_selected").toString();
                    } else {
                        userid = paramDTO.get("lastUser").toString();
                    }
                    try {
                        SupportUserPK pk1 = new SupportUserPK();
                        pk1.productId = new Integer(paramDTO.get("productId").toString());
                        pk1.userId = new Integer(userid);
                        supportUser = supportUserHome.findByPrimaryKey(pk1);
                    } catch (FinderException e) {
                    }
                    if (supportUser != null) {
                        resultDTO.addResultMessage("SupportUser.duplicate", paramDTO.get("productName").toString());
                        resultDTO.put("duplicate", "true");
                        resultDTO.setResultAsFailure();
                        return;
                    } else {
                        pk.productId = new Integer(paramDTO.get("productId").toString());
                        pk.userId = new Integer(paramDTO.get("lastUser").toString());
                        supportUserDTO.setPrimKey(pk);
                        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, supportUserDTO, resultDTO);
                        if (!resultDTO.isFailure()) {
                            pk.productId = new Integer(paramDTO.get("productId").toString());
                            pk.userId = new Integer(paramDTO.get("userId_selected").toString());
                            supportUserDTO.setPrimKey(pk);
                            CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, supportUserDTO, resultDTO);
                        }
                    }
                } else {
                    try {
                        SupportUserPK pk1 = new SupportUserPK();
                        pk1.productId = new Integer(paramDTO.get("productId").toString());
                        pk1.userId = new Integer(paramDTO.get("userId").toString());
                        supportUser = supportUserHome.findByPrimaryKey(pk1);
                    } catch (FinderException e) {
                    }
                    if (supportUser != null) {
                        resultDTO.addResultMessage("SupportUser.duplicate", paramDTO.get("productName").toString());
                        resultDTO.put("duplicate", "true");
                        resultDTO.setResultAsFailure();
                        return;
                    } else {
                        CRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, supportUserDTO, resultDTO);
                    }
                }
            }
        }
        if ("".equals(getOp()) && supportUser != null) {
            try {
                product = productHome.findByPrimaryKey(supportUser.getProductId());
                user = userHome.findByPrimaryKey(supportUser.getUserId());
                address = addressHome.findByPrimaryKey(user.getAddressId());
                addressName.append(address.getName1());

                if (address.getName2() != null && address.getName2() != "") {
                    addressName.append(", ").append(address.getName2());
                }
                resultDTO.put("userName", addressName.toString());
                resultDTO.put("productName", product.getProductName());
                resultDTO.put("lastUser", user.getUserId());
                resultDTO.put("emailNotification", user.getNotificationSupportCaseEmail());
            } catch (FinderException e) {
                log.debug(" .... some object has not exist ... !!!");
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
