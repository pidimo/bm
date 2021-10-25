package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Branch;
import com.piramide.elwis.domain.catalogmanager.BranchHome;
import com.piramide.elwis.dto.catalogmanager.BranchDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyBranchCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        BranchHome branchHome = (BranchHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_BRANCH);
        try {
            Collection sourceBranches = branchHome.findByCompanyId(source.getCompanyId());
            Map<Integer, Integer> identifierMap = createRootElements(sourceBranches, target);
            updateParentBraches(sourceBranches, identifierMap);
        } catch (FinderException e) {
            log.debug("Cannot read Branch objects for source company with identifier " + source.getCompanyId());
        }
    }

    /**
     * This method create Root Branch objects  for target company and return <code>Map</code> object
     * that contain branck identifier mapping, this <code>Map</code> object is used to stablish
     * parent relation in target branch group.
     *
     * @param sourceBranches Collection of source branch
     * @param targetCompany  target company.
     * @return <code>Map</code> that contain costcenter identifier mapping.
     */
    private Map<Integer, Integer> createRootElements(Collection sourceBranches, Company targetCompany) {
        Map<Integer, Integer> identifierMap = new HashMap<Integer, Integer>();

        for (Object object : sourceBranches) {
            Branch sourceBranch = (Branch) object;
            BranchDTO targetBanchDTO = new BranchDTO();
            DTOFactory.i.copyToDTO(sourceBranch, targetBanchDTO);
            targetBanchDTO.put("companyId", targetCompany.getCompanyId());
            targetBanchDTO.remove("group");
            Branch targetBranch = (Branch) ExtendedCRUDDirector.i.create(targetBanchDTO, new ResultDTO(), false);
            identifierMap.put(sourceBranch.getBranchId(), targetBranch.getBranchId());
        }

        return identifierMap;
    }

    /**
     * This method updates parent relation in target Branches using <code>Map</code> object
     * that contain branch identifier mapping.
     *
     * @param sourceBranches Collection of source costcenters
     * @param identifierMap  <code>Map</code> that contain costcenter identifier mapping.
     */
    private void updateParentBraches(Collection sourceBranches, Map<Integer, Integer> identifierMap) {
        BranchHome branchHome = (BranchHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_BRANCH);
        if (null != sourceBranches) {
            for (Object object : sourceBranches) {
                Branch sourceBranch = (Branch) object;

                if (null != sourceBranch.getGroup()) {
                    Integer targetIdentifier = identifierMap.get(sourceBranch.getBranchId());
                    Integer targetGroup = identifierMap.get(sourceBranch.getGroup());

                    try {
                        Branch targetBranch = branchHome.findByPrimaryKey(targetIdentifier);
                        targetBranch.setGroup(targetGroup);
                    } catch (FinderException e) {
                        log.debug("Cannot find target Branch with identifier " + targetIdentifier);
                    }
                }
            }
        }
    }
}
