package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.productmanager.CompetitorProduct;
import com.piramide.elwis.domain.productmanager.CompetitorProductHome;
import com.piramide.elwis.dto.productmanager.CompetitorProductDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CompetitorProductCreateCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class CompetitorProductCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create competitorProduct command");
        log.debug("Operation = " + getOp());

        paramDTO.put("entryDate", DateUtils.dateToInteger(new Date()));
        paramDTO.put("changeDate", DateUtils.dateToInteger(new Date()));

        AddressHome competitorHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        CompetitorProductHome competitorProductHome = (CompetitorProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_COMPETITORPRODUCT);
        CompetitorProduct competitorProduct = null;

        Address competitor = null;
        try {
            competitor = competitorHome.findByPrimaryKey(new Integer(paramDTO.get("competitorId").toString()));
            competitorProduct = competitorProductHome.findByCompetitorProductNameCompetitorId(new Integer(paramDTO.get("companyId").toString()), paramDTO.get("productName").toString(), competitor.getAddressId());
        } catch (FinderException e) {
        }

        if (competitor != null) {
            if (competitorProduct == null) {
                CompetitorProductDTO competitorProductDTO = new CompetitorProductDTO(paramDTO);
                ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE, competitorProductDTO, resultDTO, false, false, false, false);
            } else {
                resultDTO.put("competitorName", paramDTO.get("competitorName"));
                resultDTO.addResultMessage("Competitor.duplicate");
                resultDTO.setForward("fail");
                return;
            }

        } else {
            resultDTO.put("competitorName", paramDTO.get("competitorName"));
            resultDTO.addResultMessage("Competitor.notFound");
            resultDTO.setForward("fail");
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
