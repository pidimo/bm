package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.CostCenter;
import com.piramide.elwis.domain.catalogmanager.CostCenterHome;
import com.piramide.elwis.dto.catalogmanager.CostCenterDTO;
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
public class CopyCostCenterCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        CostCenterHome costCenterHome =
                (CostCenterHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COSTCENTER);
        Collection sourceCostCenters;
        try {
            sourceCostCenters = costCenterHome.findByCompanyId(source.getCompanyId());

            Map<Integer, Integer> identifierMap = createRootElements(sourceCostCenters, target);
            updateParentCostCentes(sourceCostCenters, identifierMap);
        } catch (FinderException e) {
            log.debug("Cannot read costCenters objects for source company with identifier " + source.getCompanyId());
        }
    }

    /**
     * This method create Root CostCenter objects  for target company and return <code>Map</code> object
     * that contain costcenter identifier mapping, this <code>Map</code> object is used to stablish
     * parent relation in target costcenters.
     *
     * @param sourceCostCenters Collection of source costcenters.
     * @param targetCompany     target company.
     * @return <code>Map</code> that contain costcenter identifier mapping.
     */
    private Map<Integer, Integer> createRootElements(Collection sourceCostCenters, Company targetCompany) {

        Map<Integer, Integer> identifierMap = new HashMap<Integer, Integer>();
        for (Object object : sourceCostCenters) {
            CostCenter sourceCostCenter = (CostCenter) object;
            CostCenterDTO targetCostCenterDTO = new CostCenterDTO();
            DTOFactory.i.copyToDTO(sourceCostCenter, targetCostCenterDTO);
            targetCostCenterDTO.put("companyId", targetCompany.getCompanyId());
            targetCostCenterDTO.remove("parentCostCenterId");
            CostCenter targetCostCenter =
                    (CostCenter) ExtendedCRUDDirector.i.create(targetCostCenterDTO, new ResultDTO(), false);

            identifierMap.put(sourceCostCenter.getCostCenterId(), targetCostCenter.getCostCenterId());
        }

        return identifierMap;
    }

    /**
     * This method updates parent relation in target costCenters using <code>Map</code> object
     * that contain costCenters identifier mapping.
     *
     * @param sourceCostCenters Collection of source costcenters
     * @param identifierMap     <code>Map</code> that contain costcenter identifier mapping.
     */
    private void updateParentCostCentes(Collection sourceCostCenters, Map<Integer, Integer> identifierMap) {
        CostCenterHome costCenterHome =
                (CostCenterHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COSTCENTER);
        for (Object object : sourceCostCenters) {
            CostCenter sourceCostCenter = (CostCenter) object;
            if (null != sourceCostCenter.getParentCostCenterId()) {
                Integer targetIdentifier = identifierMap.get(sourceCostCenter.getCostCenterId());
                Integer targetParentIdentifier = identifierMap.get(sourceCostCenter.getParentCostCenterId());

                try {
                    CostCenter targetCostCenter = costCenterHome.findByPrimaryKey(targetIdentifier);
                    targetCostCenter.setParentCostCenterId(targetParentIdentifier);
                } catch (FinderException e) {
                    log.debug("Cannot find target costCenter with identifier " + targetIdentifier);
                }
            }
        }
    }
}
