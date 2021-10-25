package com.piramide.elwis.web.reports.dynamiccolumns;

import com.jatun.titus.listgenerator.structure.dynamiccolumn.ColumnLoader;
import com.jatun.titus.listgenerator.structure.dynamiccolumn.DynamicColumnField;
import com.piramide.elwis.cmd.catalogmanager.CategoriesReadCmd;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Jatun S.R.L.
 * Category dynamic column loader implementation
 *
 * @author Miky
 * @version $Id: CategoryColumnLoader.java 04-sep-2008 16:27:48 $
 */
public abstract class CategoryColumnLoader implements ColumnLoader {
    private Log log = LogFactory.getLog(this.getClass());

    public abstract String getTableId();

    public String getSecondTableId() {
        return null;
    }

    /**
     * compose and get category dynamic columns
     *
     * @param parameters
     * @return list
     */
    public Collection<DynamicColumnField> getColumns(Map parameters) {

        Collection<DynamicColumnField> columns = new ArrayList<DynamicColumnField>();

        CategoriesReadCmd categoriesReadCmd = new CategoriesReadCmd();
        categoriesReadCmd.putParam("companyId", parameters.get("companyId"));
        categoriesReadCmd.putParam("tableId", getTableId());
        if (getSecondTableId() != null) {
            categoriesReadCmd.putParam("secondTableId", getSecondTableId());
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(categoriesReadCmd, null);
            if (!resultDTO.isFailure() && resultDTO.containsKey("categoryList")) {
                List categories = (List) resultDTO.get("categoryList");
                for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
                    Map categoryMap = (Map) iterator.next();

                    CategoryDynamicColumnField columnField = new CategoryDynamicColumnField();
                    columnField.setKey(new Integer(categoryMap.get("categoryId").toString()));
                    columnField.setLabel(categoryMap.get("categoryName").toString());
                    columnField.setFieldTitusPath(composeRelativeTitusPath(categoryMap.get("categoryType").toString()));

                    columns.add(columnField);
                }
            }
        } catch (AppLevelException e) {
            log.error("Error in execute cmd...", e);
            return columns;
        }

        return columns;
    }

    /**
     * Compose relative titus path from related "categfieldvalue" table
     *
     * @param categoryType
     * @return String
     */
    private String composeRelativeTitusPath(String categoryType) {
        String relativeTitusPath = null;

        if (CatalogConstants.CategoryType.ATTACH.getConstant().equals(categoryType)) {
            relativeTitusPath = "filename";
        } else if (CatalogConstants.CategoryType.COMPOUND_SELECT.getConstant().equals(categoryType)) {
            relativeTitusPath = ">>categoryvalue:categoryvalueid.categoryvaluename";
        } else if (CatalogConstants.CategoryType.DATE.getConstant().equals(categoryType)) {
            relativeTitusPath = "datevalue";
        } else if (CatalogConstants.CategoryType.DECIMAL.getConstant().equals(categoryType)) {
            relativeTitusPath = "decimalvalue";
        } else if (CatalogConstants.CategoryType.FREE_TEXT.getConstant().equals(categoryType)) {
            relativeTitusPath = ">>freetext:freetextid.freetextvalue";
        } else if (CatalogConstants.CategoryType.INTEGER.getConstant().equals(categoryType)) {
            relativeTitusPath = "integervalue";
        } else if (CatalogConstants.CategoryType.LINK_VALUE.getConstant().equals(categoryType)) {
            relativeTitusPath = "linkvalue";
        } else if (CatalogConstants.CategoryType.SINGLE_SELECT.getConstant().equals(categoryType)) {
            relativeTitusPath = ">>categoryvalue:categoryvalueid.categoryvaluename";
        } else if (CatalogConstants.CategoryType.TEXT.getConstant().equals(categoryType)) {
            relativeTitusPath = "stringvalue";
        }

        return relativeTitusPath;
    }

}
