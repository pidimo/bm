package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.catalogmanager.ProductType;
import com.piramide.elwis.dto.catalogmanager.ProductTypeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the catalog
 *
 * @author Ivan
 * @version id: ProductType.java,v 1.24 2004/08/16 21:53:53 ivan Exp ProductType.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class ProductTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ProductTypeCmd...");
        super.setOp(this.getOp());
        ProductType productType = (ProductType) super.executeInStateless(ctx, paramDTO, ProductTypeDTO.class);

        if (productType != null) {
            resultDTO.put("typeId", productType.getTypeId());
        }
    }
}