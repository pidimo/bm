package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.cmd.salesmanager.ActionUpdateCmd;
import net.java.dev.strutsejb.EJBCommand;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ActionManagerUpdateAction extends ActionManagerAction {
    protected EJBCommand getGenerateDocumentCmd() {
        return new ActionUpdateCmd();
    }
}
