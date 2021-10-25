package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.UserOfGroup;
import com.piramide.elwis.domain.admin.UserOfGroupPK;
import com.piramide.elwis.dto.admin.UserOfGroupDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * User: yumi
 * Date: Apr 20, 2005
 * Time: 4:21:34 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserOfGroupDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug(" ... UserOfGroupDeleteCMD ...");
        log.debug("paramDTO : " + paramDTO);

        String op = paramDTO.getOp();
        Integer userId = Integer.valueOf((String) paramDTO.get("userId"));
        Integer userGroupId = Integer.valueOf((String) paramDTO.get("userGroupId"));
        String name = paramDTO.get("name").toString();

        UserOfGroupPK pK = new UserOfGroupPK();
        pK.userGroupId = userGroupId;
        pK.userId = userId;
        UserOfGroupDTO dto = new UserOfGroupDTO();
        dto.setPrimKey(pK);
        dto.put("userId", userId);
        dto.put("userGroupId", userGroupId);
        dto.put("name", name);

        if ("".equals(op)) {

            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
            UserOfGroup usr = (UserOfGroup) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
            if (null == usr) {
                resultDTO.setForward("Fail");
            }
        }

        if (ExtendedCRUDDirector.OP_DELETE.equals(op)) {
            ExtendedCRUDDirector.i.delete(dto, resultDTO, true, "Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
