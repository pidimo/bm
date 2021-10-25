package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.Recent;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.contactmanager.RecentDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Bussines logic for Recents of one user.
 *
 * @author Fernando Montaño
 * @version $Id: RecentCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class RecentCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command with operation = " + getOp());

        log.debug("userId= " + paramDTO.get("userId"));

        try {

            log.debug("addressId = " + paramDTO.get("addressId"));
            log.debug("companyId = " + paramDTO.get("companyId"));
            RecentDTO recentDTO = new RecentDTO(paramDTO);


            User user = (User) EJBFactory.i.findEJB(new UserDTO(paramDTO));

            int maxRecentList = 10; // max recent list by default, if no have a recentid limit
            Integer maxList = user.getMaxRecentList();
            if (maxList != null) {
                log.debug("Max user recent list = " + maxList);
                maxRecentList = maxList.intValue();
            }

            Collection recents = (Collection) EJBFactory.i.callFinder(new RecentDTO(), "findByCompanyUser",
                    new Object[]{new Integer(recentDTO.get("userId").toString()),
                            new Integer(recentDTO.get("companyId").toString())});


            //iterating current recents for user
            HashMap recentsHash = new HashMap(); // key = addressId, value= bean reference
            for (Iterator iterator = recents.iterator(); iterator.hasNext();) {
                Recent recent = (Recent) iterator.next();
                recentsHash.put(recent.getAddressId(), recent); //put address
            }

            if (CRUDDirector.OP_UPDATE.equals(getOp())) {

                Integer addressId = new Integer(paramDTO.get("addressId").toString());
                //Check if recent list contains the recent addressId, if true, update positions
                int currentRecentPosition;
                if (recentsHash.containsKey(addressId)) {
                    log.debug("addressId is present in recent list");
                    currentRecentPosition = ((Recent) recentsHash.get(addressId)).getRecentId().intValue();
                    //update positions
                    for (Iterator it = recentsHash.values().iterator(); it.hasNext();) {
                        Recent recent = (Recent) it.next();
                        if (recent.getRecentId().intValue() <= currentRecentPosition) {
                            recent.setRecentId(new Integer(recent.getRecentId().intValue() + 1));
                        }
                    }
                    //update recent position to first place (1)
                    Recent recent = (Recent) recentsHash.get(addressId);
                    recent.setRecentId(new Integer(1));

                } else { //if no contains, then create it
                    log.debug("addressId is not present in recent list");
                    //increment current positions to recent lits
                    for (Iterator it = recentsHash.values().iterator(); it.hasNext();) {
                        Recent recent = (Recent) it.next();
                        recent.setRecentId(new Integer(recent.getRecentId().intValue() + 1));
                    }

                    recentDTO.put("recentId", new Integer(1));
                    Recent recent = (Recent) EJBFactory.i.createEJB(recentDTO);
                    recentsHash.put(recent.getAddressId(), recent); //put recently created recent Bean
                }
            }


            //do in update and delete command operation types.
            //delete last recent addresses if maxRecentList is exceeded
            if (recentsHash.size() > maxRecentList) {
                for (Iterator iterator = recentsHash.values().iterator(); iterator.hasNext();) {
                    Recent recent = (Recent) iterator.next();
                    if (recent.getRecentId().intValue() > maxRecentList) {
                        recent.remove();
                    }

                }
            }


        } catch (EJBFactoryException e) {
            log.debug("Do not found recents for this user");
        } catch (RemoveException re) {
            log.debug("Error removing recent");
        }

    }

    public boolean isStateful() {
        return false;
    }
}
