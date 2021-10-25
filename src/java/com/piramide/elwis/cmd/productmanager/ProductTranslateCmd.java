package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.TranslateCmd;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import net.java.dev.strutsejb.EJBCommand;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductTranslateCmd extends EJBCommand {

    public void executeInStateless(SessionContext sessionContext) {
        TranslateCmd translateCmd = new TranslateCmd();
        translateCmd.setOp(this.getOp());
        translateCmd.putParam("dtoClassName", ProductDTO.class.getName());
        translateCmd.putParam("synchronizedFieldName", "productName");
        translateCmd.putParam(paramDTO);
        translateCmd.executeInStateless(sessionContext);
        resultDTO.putAll(translateCmd.getResultDTO());
    }

    public boolean isStateful() {
        return false;
    }
}
