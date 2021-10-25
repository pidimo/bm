package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.catalogmanager.VatDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the catalog
 *
 * @author Ivan
 * @version id: Vat.java,v 1.24 2004/08/16 21:53:53 ivan Exp Vat.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class VatCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing VatCmd with \n" +
                "paramDTO = " + paramDTO);
        if (null != paramDTO.get("withReferences")) {
            Boolean withReferences = Boolean.valueOf(paramDTO.get("withReferences").toString());
            if (!withReferences.booleanValue()) {
                super.checkReferences = false;
            }
        }
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, VatDTO.class);
    }
}