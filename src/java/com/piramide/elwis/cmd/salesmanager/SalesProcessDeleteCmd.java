package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Delete logic for sales process
 *
 * @author Fernando Montaño?
 * @version $Id: SalesProcessDeleteCmd.java 10013 2010-12-14 18:08:15Z ivan $
 */
public class SalesProcessDeleteCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(SalesProcessDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        SalesProcessDTO salesProcessDTO = new SalesProcessDTO(paramDTO);
        try {
            SalesProcess salesProcess = (SalesProcess) EJBFactory.i.findEJB(salesProcessDTO);

            if (salesProcess != null) { //it can be deleted        
                //remove the action related with communications
                ContactDTO contactDTO = new ContactDTO();
                log.debug("Removing action positions...");
                for (Iterator iterator = salesProcess.getActions().iterator(); iterator.hasNext();) {
                    Action action = (Action) iterator.next();
                    Collection positions = (Collection) EJBFactory.i.callFinder(new ActionPositionDTO(),
                            "findByProcessAndContactId", new Object[]{action.getProcessId(), action.getContactId()});
                    Iterator itPositions = positions.iterator();
                    while (itPositions.hasNext()) {
                        ActionPosition position = (ActionPosition) itPositions.next();
                        log.debug("Removing position = " + position.getPositionId());
                        position.remove();
                    }
                }

                Iterator it = salesProcess.getActions().iterator();
                log.debug("Removing actions and communications");
                while (it.hasNext()) {
                    Action action = (Action) it.next();
                    log.debug("Removing action and communication: (" + action.getContactId() + ", "
                            + action.getProcessId() + ")");
                    contactDTO.put("contactId", action.getContactId());
                    action.remove();//remove the action

                    EJBFactory.i.removeEJB(contactDTO); //remove the contact

                    it = salesProcess.getActions().iterator(); //update de iterator
                }

                deleteCategoryValues(salesProcess, ctx);

                //removing the description
                if (salesProcess.getDescriptionText() != null) {
                    SalesFreeTextHome freeTextHome = (SalesFreeTextHome)
                            EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_FREETEXT);
                    SalesFreeText freeText = freeTextHome.findByPrimaryKey(salesProcess.getDescriptionId());
                    salesProcess.setDescriptionText(null);
                    freeText.remove();
                }

                log.debug("finally removing sales process");
                salesProcess.remove();
            }

        } catch (Exception e) {
            ctx.setRollbackOnly();
            log.debug("Error removing the sales process, it seems was deleted by other user");
            resultDTO.isClearingForm = true;
            resultDTO.setResultAsFailure();
            resultDTO.setForward("Fail");
            salesProcessDTO.addNotFoundMsgTo(resultDTO);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void deleteCategoryValues(SalesProcess salesProcess, SessionContext ctx) {
        List paramsAsList = Arrays.asList(salesProcess.getProcessId(), salesProcess.getCompanyId());

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", "findBySalesProcessId");
        cmd.putParam("params", paramsAsList);
        cmd.setOp("deleteValues");
        cmd.executeInStateless(ctx);
    }
}
