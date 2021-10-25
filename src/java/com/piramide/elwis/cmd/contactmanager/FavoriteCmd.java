package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.FavoriteDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Command that manages Favorite business logic execution.
 *
 * @author Ivan
 * @version $Id: FavoriteCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FavoriteCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        ArrayList listFavorites = new ArrayList();
        Collection favorites = null;
        Integer userId = new Integer(paramDTO.getAsInt("userId"));
        Integer companyId = new Integer(paramDTO.getAsInt("companyId"));
        if ("save".equals(getOp())) { //if save operation was the executed
            ArrayList selectet = new ArrayList();
            for (int i = 0; i < paramDTO.getAsInt("sizeOfFavorites"); i++) {
                if (paramDTO.get("item" + (i + 1)) != null)     //filter the favorites to deleted
                {
                    selectet.add(new Integer(paramDTO.getAsInt("item" + (i + 1))));
                }
            }

            if (!selectet.isEmpty())  //if any selected then delete favorite
            {
                for (Iterator iterator = selectet.iterator(); iterator.hasNext();) {
                    Integer s = (Integer) iterator.next();
                    FavoriteDTO favoriteDTO = new FavoriteDTO();
                    favoriteDTO.put("companyId", companyId);
                    favoriteDTO.put("userId", userId);
                    favoriteDTO.put("addressId", s);
                    FavoritePK pk = new FavoritePK(userId, s);
                    FavoriteHome favoriteHome = (FavoriteHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_FAVORITE);
                    try {
                        Favorite favorite = favoriteHome.findByPrimaryKey(pk);
                        favorite.remove();
                    } catch (RemoveException e) {
                    } catch (FinderException e) {
                    }
                }
            }
        } else {
            if ("update".equals(getOp())) { // if the update favortes
                userId = new Integer(paramDTO.getAsInt("userId"));
                companyId = new Integer(paramDTO.getAsInt("companyId"));
                addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                listFavorites = new ArrayList();

                favorites = null;
                try {
                    favorites = addressHome.findByFavorites(companyId, userId);
                    for (Iterator iterator = favorites.iterator(); iterator.hasNext();) {
                        AddressDTO addressDTO = new AddressDTO();
                        Address o = (Address) iterator.next();
                        addressDTO.put("addressId", o.getAddressId());
                        addressDTO.put("name1", o.getName1());
                        addressDTO.put("name2", o.getName2());
                        addressDTO.put("name3", o.getName3());
                        addressDTO.put("addressType", o.getAddressType());
                        listFavorites.add(addressDTO);
                    }
                    resultDTO.put("favoriteList", listFavorites);    //put the list of favorites on the resultDTO
                    resultDTO.put("sizeOfFavorites", new Integer(listFavorites.size())); //put the size of list the favorites
                } catch (FinderException e) {
                }
            } else { //if a new favorite was added to list
                FavoriteDTO favoritesDTO = new FavoriteDTO();
                favoritesDTO.put("addressId", paramDTO.get("addressId"));
                favoritesDTO.put("userId", paramDTO.get("userId"));
                favoritesDTO.put("companyId", paramDTO.get("companyId"));
                favoritesDTO.put("addressType", paramDTO.get("addressType"));
                favoritesDTO.put("name1", paramDTO.get("name1"));
                favoritesDTO.put("name2", paramDTO.get("name2"));
                favoritesDTO.put("name3", paramDTO.get("name3"));
                log.debug("the formAdvancedSearch value = " + paramDTO.get("fromAdvancedSearch"));
                try {
                    AddressHome addrHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                    addrHome.findByPrimaryKey(new Integer(paramDTO.getAsInt("addressId")));
                    EJBFactory.i.createEJB(favoritesDTO);
                    if (paramDTO.get("fromAdvancedSearch") != null) {
                        resultDTO.setForward("SuccessToAdvancedSearch");
                    } else {
                        resultDTO.setForward("Success");
                    }
                    return;
                } catch (EJBFactoryException e) {
                    if (e.getCause() instanceof javax.ejb.DuplicateKeyException) {
                        favoritesDTO.addDuplicatedMsgTo(resultDTO);
                        if (paramDTO.get("fromAdvancedSearch") != null) {
                            resultDTO.setForward("SuccessToAdvancedSearch");
                        } else {
                            resultDTO.setForward("Success");
                        }
                        return;
                    }
                    favoritesDTO.addNotFoundMsgTo(resultDTO);
                    if (paramDTO.get("fromAdvancedSearch") != null) {
                        resultDTO.setForward("SuccessToAdvancedSearch");
                    } else {
                        resultDTO.setForward("Success");
                    }
                    return;
                } catch (FinderException e) {   //Si el Address se elimino capturamos la excepcion
                    AddressDTO addrDTO = new AddressDTO(paramDTO);
                    addrDTO.addNotFoundMsgTo(resultDTO);
                    if (paramDTO.get("fromAdvancedSearch") != null) {
                        resultDTO.setForward("SuccessToAdvancedSearch");
                    } else {
                        resultDTO.setForward("Success");
                    }
                    return;
                }

            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
