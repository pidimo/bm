package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.admin.AdminFreeTextHome;
import com.piramide.elwis.domain.admin.Role;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Fernando Montaño
 * @version $Id: RoleCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class RoleCmd extends GeneralCmd {

    private Log log = LogFactory.getLog(RoleCmd.class);

    public void executeInStateless(SessionContext ctx) {
        super.checkDuplicate = false;
        RoleDTO roleDTO = new RoleDTO(paramDTO);
        Role roleBean = (Role) super.execute(ctx, roleDTO);

        if (CRUDDirector.OP_CREATE.equals(getOp())) {
            roleBean.setIsDefault(new Boolean(false));
        }

        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, roleBean, "DescriptionText", AdminFreeTextHome.class,
                AdminConstants.JNDI_ADMIN_FREETEXT, FreeTextTypes.FREETEXT_ROLE, "roleDescription");

    }

    public boolean isStateful() {
        return false;
    }
}
