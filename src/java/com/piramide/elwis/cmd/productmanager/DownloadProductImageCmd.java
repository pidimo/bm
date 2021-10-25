package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.domain.productmanager.ProductFreeText;
import com.piramide.elwis.domain.productmanager.ProductFreeTextHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the catalog
 *
 * @author Ivan
 * @version id: DownloadProductImageCmd.java,v 1.24 2004/08/16 21:53:53 ivan Exp DownloadProductImageCmd.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class DownloadProductImageCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing DownloadProductImageCmd...");
        ArrayByteWrapper value = null;
        ProductFreeTextHome productFreeTextHome = (ProductFreeTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
        try {
            ProductFreeText freeText = productFreeTextHome.findByPrimaryKey(new Integer(paramDTO.get("freeTextId").toString()));
            value = new ArrayByteWrapper(freeText.getValue());
        } catch (FinderException e) {
            log.debug("free text image not found....");
            resultDTO.setResultAsFailure();
        }
        resultDTO.put("image", value);
        resultDTO.put("fileName", (paramDTO.get("productPictureName") != null ? paramDTO.get("productPictureName").toString() : ""));
        resultDTO.put("thumbnail", (paramDTO.get("thumbnail") != null ? "true" : "false"));
    }

    public boolean isStateful() {
        return false;
    }


}

