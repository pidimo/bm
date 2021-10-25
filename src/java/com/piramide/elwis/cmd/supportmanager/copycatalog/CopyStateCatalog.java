package com.piramide.elwis.cmd.supportmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.supportmanager.State;
import com.piramide.elwis.domain.supportmanager.StateHome;
import com.piramide.elwis.dto.supportmanager.StateDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopyStateCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        StateHome stateHome = (StateHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_STATE);

        Collection sourceStates = null;

        try {
            sourceStates = stateHome.findSupportCatalogByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read state objects from source company");
        }

        if (null != sourceStates && !sourceStates.isEmpty()) {
            for (Object obj : sourceStates) {
                State sourceState = (State) obj;
                Integer targetLangTextId = CopyCatalogUtil.i.buildLangText(sourceState.getLangTextId(), target.getCompanyId());
                StateDTO dto = new StateDTO();
                DTOFactory.i.copyToDTO(sourceState, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("langTextId", targetLangTextId);
                ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
            }
        }
    }
}
