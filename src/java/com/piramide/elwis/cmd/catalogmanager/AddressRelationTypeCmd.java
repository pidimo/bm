package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.catalogmanager.AddressRelationTypeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public class AddressRelationTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AddressRelationTypeCmd...");
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, AddressRelationTypeDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}