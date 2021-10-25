package com.piramide.elwis.cmd.supportmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.supportmanager.ArticleCategory;
import com.piramide.elwis.domain.supportmanager.ArticleCategoryHome;
import com.piramide.elwis.dto.supportmanager.ArticleCategoryDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
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
public class CopyCategoryCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        ArticleCategoryHome articleCategoryHome =
                (ArticleCategoryHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLECATEGORY);
        Collection sourceCaterories = null;
        try {
            sourceCaterories = articleCategoryHome.findByCompanyId(source.getCompanyId());
            Map<Integer, Integer> identifiers = createRootElements(sourceCaterories, target.getCompanyId());
            updateParentElements(sourceCaterories, identifiers);
        } catch (FinderException e) {
            log.debug("Cannot read source article categories for source company " + source.getCompanyId());
        }
    }

    private Map<Integer, Integer> createRootElements(Collection sourceCategories, Integer targetCompanyId) {

        ArticleCategoryHome articleCategoryHome =
                (ArticleCategoryHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLECATEGORY);

        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        if (null != sourceCategories) {
            for (Object obj : sourceCategories) {
                ArticleCategory articleCategory = (ArticleCategory) obj;

                ArticleCategoryDTO targetDTO = new ArticleCategoryDTO();
                DTOFactory.i.copyToDTO(articleCategory, targetDTO);
                targetDTO.put("companyId", targetCompanyId);
                targetDTO.remove("categoryId");
                targetDTO.remove("parentCategoryId");

                try {
                    ArticleCategory tarArticleCategory = articleCategoryHome.create(targetDTO);
                    result.put(articleCategory.getCategoryId(), tarArticleCategory.getCategoryId());
                } catch (CreateException e) {
                    log.debug("Cannot create target category for company " + targetCompanyId);
                }
            }
        }

        return result;
    }

    private void updateParentElements(Collection sourceCategories, Map<Integer, Integer> identifierMap) {
        ArticleCategoryHome articleCategoryHome =
                (ArticleCategoryHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_ARTICLECATEGORY);

        if (null != sourceCategories) {
            for (Object obj : sourceCategories) {
                ArticleCategory sourceElement = (ArticleCategory) obj;
                if (null != sourceElement.getParentCategoryId()) {
                    Integer targetIdentifier = identifierMap.get(sourceElement.getCategoryId());
                    Integer targetParentIdentifier = identifierMap.get(sourceElement.getParentCategoryId());
                    try {
                        ArticleCategory targetElement = articleCategoryHome.findByPrimaryKey(targetIdentifier);
                        targetElement.setParentCategoryId(targetParentIdentifier);
                    } catch (FinderException e) {
                        log.debug("Cannot find target element with id " + targetIdentifier);
                    }
                }
            }
        }
    }
}
