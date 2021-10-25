package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.CompetitorProduct;
import com.piramide.elwis.domain.productmanager.CompetitorProductHome;
import com.piramide.elwis.dto.productmanager.CompetitorProductDTO;
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
 * @version $Id: CompetitorProductUpdateCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class CompetitorProductUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update competitor product command");
        log.debug("Operation = " + getOp());

        CompetitorProductDTO cproductDTO = new CompetitorProductDTO(paramDTO);
        CompetitorProductHome competitorProductHome = (CompetitorProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_COMPETITORPRODUCT);
        CompetitorProduct competitorProduct = null;
        CompetitorProduct competitorP = null;
        paramDTO.put("entryDate", paramDTO.get("entryDate"));
        paramDTO.put("changeDate", DateUtils.dateToInteger(new Date()));
        try {
            competitorP = competitorProductHome.findByPrimaryKey(new Integer(paramDTO.get("competitorProductId").toString()));
            competitorProduct = competitorProductHome.findByCompetitorProductNameCompetitorId(new Integer(paramDTO.get("companyId").toString()),
                    paramDTO.get("productName").toString(), new Integer(paramDTO.getAsString("competitorId")));
        } catch (FinderException e) {
        }

        if (competitorProduct != null) {
            if (competitorP.getCompetitorProductId() != competitorProduct.getCompetitorProductId()) {
                resultDTO.addResultMessage("Competitor.duplicate");
                resultDTO.setForward("fail");
                return;
            } else {
                ExtendedCRUDDirector.i.update(cproductDTO, resultDTO, false, true, false, "Fail");
            }
        } else {
            ExtendedCRUDDirector.i.update(cproductDTO, resultDTO, false, true, false, "Fail");
        }
        resultDTO.put("competitorName", paramDTO.get("competitorName"));
        resultDTO.put("entryDateU", paramDTO.get("entryDateU"));
    }

    public boolean isStateful() {
        return false;
    }
}
