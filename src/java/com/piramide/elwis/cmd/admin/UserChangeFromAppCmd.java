package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactFreeTextHome;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Cmd to update user fields from mobile app
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class UserChangeFromAppCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserChangeFromAppCmd................" + paramDTO);

        Integer userId = new Integer(paramDTO.get("userId").toString());

        User user = findUser(userId);

        if (user != null) {
            updatePassword(user);
            updateVisibleMobileApp(user);
            updateUserAddressImage(user);
        }

        if (resultDTO.isFailure()) {
            ctx.setRollbackOnly();
            resultDTO.setForward("Fail");
        }
    }

    private void updatePassword(User user) {
        String userPassword = (String) paramDTO.get("userPassword");
        String password2 = (String) paramDTO.get("password2");

        if (userPassword != null && password2 != null) {
            String currentPassword = user.getUserPassword();

            try {
                if (currentPassword.equals(EncryptUtil.i.encryt(userPassword))) {
                    user.setUserPassword(EncryptUtil.i.encryt(password2));
                } else {
                    resultDTO.addResultMessage("User.passInvalid");
                    resultDTO.setResultAsFailure();
                }
            } catch (ServiceUnavailableException e) {
                log.error("Encrypt service unavaliable...", e);
            }
        }
    }

    private void updateVisibleMobileApp(User user) {
        Object visibleMobileApp = paramDTO.get("visibleMobileApp");
        if (visibleMobileApp != null) {
            Boolean visible = (visibleMobileApp instanceof Boolean) ? (Boolean) visibleMobileApp : ("true".equals(visibleMobileApp.toString()));
            user.setVisibleMobileApp(visible);
        }
    }

    private void updateUserAddressImage(User user) {
        ArrayByteWrapper image = (ArrayByteWrapper) paramDTO.get("image");

        if (image != null) {
            Address address = findAddress(user.getAddressId());
            log.debug("Updating user address img...");

            if (address != null) {
                if (address.getImageFreeText() != null) {
                    address.getImageFreeText().setValue(image.getFileData());
                } else {
                    ContactFreeTextHome contactFreeTextHome = (ContactFreeTextHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);
                    ContactFreeText imageFreeText;
                    try {
                        imageFreeText = contactFreeTextHome.create(image.getFileData(), address.getCompanyId(), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                        address.setImageFreeText(imageFreeText);
                    } catch (CreateException e) {
                        log.error("unexpected error creating the image", e);
                    }
                }
            }
        }
    }

    private User findUser(Integer userId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        if (userId != null) {
            try {
                user = userHome.findByPrimaryKey(userId);
            } catch (FinderException e) {
                log.debug("Not found user.. " + userId);
            }
        }
        return user;
    }

    private Address findAddress(Integer addressId) {
        Address address = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        if (addressId != null) {
            try {
                address = addressHome.findByPrimaryKey(addressId);
            } catch (FinderException e) {
                log.debug("Not found address with id:" + addressId, e);
            }
        }
        return address;
    }

    public boolean isStateful() {
        return false;
    }
}