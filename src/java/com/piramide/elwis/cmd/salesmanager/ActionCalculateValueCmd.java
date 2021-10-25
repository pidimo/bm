package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.domain.salesmanager.Action;
import com.piramide.elwis.domain.salesmanager.ActionPK;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

/**
 * Calculate the value of a sales process action through actions positions and discount.
 *
 * @author Fernando Montaño
 * @version $Id: ActionCalculateValueCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ActionCalculateValueCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionCalculateValueCmd.class);

    public void executeInStateless(SessionContext ctx) {
        ActionDTO actionDTO = new ActionDTO(paramDTO);
        ActionPK pK = new ActionPK();
        pK.contactId = new Integer(actionDTO.getAsInt("contactId"));
        pK.processId = new Integer(actionDTO.getAsInt("processId"));
        actionDTO.setPrimKey(pK);

        Action action = null;
        try {
            action = (Action) EJBFactory.i.findEJB(actionDTO);
            if (action != null) {

                double result = 0;
                Collection positions = (Collection) EJBFactory.i.callFinder(new ActionPositionDTO(),
                        "findByProcessAndContactId", new Object[]{action.getProcessId(), action.getContactId()});

                //acumulating the total price of action positions.
                for (Iterator iterator = positions.iterator(); iterator.hasNext();) {
                    BigDecimal priceToAdd = null;

                    ActionPosition position = (ActionPosition) iterator.next();
                    if (FinanceConstants.NetGrossFLag.NET.equal(action.getNetGross())) {
                        priceToAdd = position.getTotalPrice();
                    }

                    if (FinanceConstants.NetGrossFLag.GROSS.equal(action.getNetGross())) {
                        priceToAdd = position.getTotalPriceGross();
                    }

                    if (null == priceToAdd) {
                        priceToAdd = new BigDecimal(0);
                    }

                    result += priceToAdd.doubleValue();
                }

                //return the total action value.
                resultDTO.put("actionValue", new BigDecimal(result));
            }
        } catch (EJBFactoryException e) {
            //action was deleted by other user
        }
    }

    public boolean isStateful() {
        return false;
    }
}
