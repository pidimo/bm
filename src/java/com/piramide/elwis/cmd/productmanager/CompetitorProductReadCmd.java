package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.CompetitorProduct;
import com.piramide.elwis.dto.productmanager.CompetitorProductDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CompetitorProductReadCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CompetitorProductReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read  competitorproduct command");
        log.debug("Operation = " + getOp());

        //Read the product
        CompetitorProduct competitorProduct = (CompetitorProduct) ExtendedCRUDDirector.i.read(new CompetitorProductDTO(paramDTO), resultDTO, true);

        if (competitorProduct != null) {
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            Address address = null;
            try {
                address = addressHome.findByPrimaryKey(competitorProduct.getCompetitorId());
                StringBuffer sb = new StringBuffer(address.getName1());
                if (address.getName2() != null) {
                    sb.append(", ").append(address.getName2());
                }
                resultDTO.put("competitorName", new String(sb));
            } catch (FinderException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
