package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * Get categories filtered by tableId and companyId
 *
 * @author Miky
 * @version $Id: CategoriesReadCmd.java 04-sep-2008 17:03:38 $
 */
public class CategoriesReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing CategoriesReadCmd..... " + paramDTO);

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String tableId = paramDTO.get("tableId").toString();
        String tableIdSecond = tableId;
        if (paramDTO.get("secondTableId") != null) {
            tableIdSecond = paramDTO.get("secondTableId").toString();
        }

        String isoLanguage = null;

        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);
        Collection categories;
        try {
            categories = categoryHome.findByTableIdOrSecondTableId(tableId, tableIdSecond, companyId);
        } catch (FinderException e) {
            log.debug("Error in find categories..." + e);
            categories = new ArrayList();
        }

        List categoryList = new ArrayList();

        for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
            Category category = (Category) iterator.next();
            Map categoryMap = new HashMap();
            categoryMap.put("categoryId", category.getCategoryId());
            categoryMap.put("categoryName", getCategoryName(category, isoLanguage));
            categoryMap.put("categoryType", category.getCategoryType());
            categoryMap.put("table", category.getTable());

            categoryList.add(categoryMap);
        }

        resultDTO.put("categoryList", categoryList);
    }

    private String getCategoryName(Category category, String isoLanguage) {
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        // read the default translation for category name
        String translation = "";
        try {
            translation = langTextHome.findByLangTextIdAndLanguageRelatedToUI(category.getLangTextId(), isoLanguage).getText();
        } catch (FinderException e) {
            try {
                translation = langTextHome.findByIsDefault(category.getLangTextId()).getText();
            } catch (FinderException e1) {
                translation = category.getCategoryName();
            }
        }
        return translation;
    }

    public boolean isStateful() {
        return false;
    }
}
