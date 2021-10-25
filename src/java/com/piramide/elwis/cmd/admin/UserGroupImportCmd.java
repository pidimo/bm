package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserOfGroup;
import com.piramide.elwis.domain.admin.UserOfGroupHome;
import com.piramide.elwis.domain.admin.UserOfGroupPK;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.admin.UserOfGroupDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 19, 2005
 * Time: 6:47:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class UserGroupImportCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO : " + paramDTO);

        List values = null;

        if (paramDTO.get("aditionals") != null) {
            values = (List) paramDTO.get("aditionals");
            String imp = null;
            UserOfGroupDTO dto = new UserOfGroupDTO(paramDTO);
            UserOfGroupHome groupHome = (UserOfGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USEROFGROUP);

            boolean hasDeletedItem = false;
            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                imp = (String) iterator.next();
                UserOfGroup relationEmployeeGroup = null;
                UserOfGroupPK pK_ = new UserOfGroupPK();
                pK_.userId = new Integer(imp);
                pK_.userGroupId = new Integer(paramDTO.get("userGroupId").toString());

                try {
                    relationEmployeeGroup = groupHome.findByPrimaryKey(pK_);
                } catch (FinderException e) {
                    log.debug("Cannot find UserOfGroup");
                }
                if (relationEmployeeGroup != null) {
                    resultDTO.setResultAsFailure();
                } else {
                    ResultDTO myResultDTO = new ResultDTO();
                    Integer id = new Integer(imp);
                    UserDTO usrDto = new UserDTO();
                    usrDto.setPrimKey(id);
                    User usr = (User) ExtendedCRUDDirector.i.read(usrDto, myResultDTO, false);
                    if (null == usr) {
                        hasDeletedItem = true;
                    } else {
                        dto.put("userId", new Integer(imp));
                        dto.put("companyId", usr.getCompanyId());
                        UserOfGroupPK pK = new UserOfGroupPK(new Integer(imp), new Integer(paramDTO.get("userGroupId").toString()));
                        dto.setPrimKey(pK);
                        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, dto, resultDTO);
                    }
                }
            }
            resultDTO.put("hasDeletedItems", Boolean.valueOf(hasDeletedItem));
        }
        resultDTO.put("userGroupId", paramDTO.get("userGroupId"));
    }

    public boolean isStateful() {
        return false;
    }
}
