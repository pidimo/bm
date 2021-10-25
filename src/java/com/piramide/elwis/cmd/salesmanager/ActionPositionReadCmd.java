package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.dto.salesmanager.ActionPositionDTO;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author : yumi
 *         <p/>
 *         Jatun s.r.l
 */

public class ActionPositionReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read actionPosition command");
        log.debug("Operation = " + getOp());

        ActionPosition position = (ActionPosition) ExtendedCRUDDirector.i.read(new ActionPositionDTO(paramDTO), resultDTO, false);
        if (position != null) {
            if (position.getDescriptionText() != null) {
                resultDTO.put("description", new String(position.getDescriptionText().getValue()));
                ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
                try {
                    Product product = productHome.findByPrimaryKey(position.getProductId());
                    resultDTO.put("productName", product.getProductName());
                } catch (FinderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        } else {
            resultDTO.setForward("Fail");
            return;
        }
        resultDTO.put("contactId", position.getContactId());
        resultDTO.put("processId", position.getProcessId());
    }

    public boolean isStateful() {
        return false;
    }
}

