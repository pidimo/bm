package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.salesmanager.SaleReadCmd;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionBySaleCreateForwardAction extends SaleManagerForwardActon {
    protected Log log = LogFactory.getLog(this.getClass());

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String saleId = request.getParameter("saleId");
        Functions.setSalePositionDefaultValues(defaultForm, saleId, request);

        //set sale default contact person, only for contact module
        SaleReadCmd saleReadCmd = new SaleReadCmd();
        saleReadCmd.putParam("saleId", saleId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(saleReadCmd, request);
            defaultForm.setDto("contactPersonId", resultDTO.get("contactPersonId"));
        } catch (AppLevelException e) {
            log.debug("Error in execute sale read cmd.." + saleId);
        }
    }
}
