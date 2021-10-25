package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.CategoryUtil;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import java.util.Collection;

/**
 * This Class represents the Category Entity Bean
 *
 * @author Ivan
 * @version $Id: CategoryBean.java 12573 2016-08-10 17:02:39Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class CategoryBean implements EntityBean {
    private static Log log = LogFactory.getLog(CategoryBean.class);

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCategoryId(PKGenerator.i.nextKey(CatalogConstants.TABLE_CATEGORY));
        setVersion(1);
        return null;
    }


    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract String getCategoryName();

    public abstract void setCategoryName(String nameId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract String getTable();

    public abstract void setTable(String table);

    public abstract Integer getLangTextId();

    public abstract void setLangTextId(Integer langTextId);

    public abstract String getType();

    public abstract Integer getCategoryType();

    public abstract void setCategoryType(Integer categoryType);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract FreeText getFreeText();

    public abstract void setFreeText(FreeText freeText);

    public void setFreeText(EJBLocalObject freeText) {
        setFreeText((FreeText) freeText);
    }

    public abstract Collection getCategoryValue();

    public abstract void setCategoryValue(Collection categoryValue);

    public abstract CategoryGroup getCategoryGroup();

    public abstract void setCategoryGroup(CategoryGroup categoryGroup);

    public abstract Integer getCategoryGroupId();

    public abstract void setCategoryGroupId(Integer categoryGroupId);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract Integer getParentCategory();

    public abstract void setParentCategory(Integer parentcategory);

    public abstract Boolean getHasSubCategories();

    public abstract void setHasSubCategories(Boolean hasSubCategories);

    public abstract Collection getCategory();

    public abstract void setCategory(Collection category);

    public abstract Collection getChildrenCategories();

    public abstract void setChildrenCategories(Collection childrenCategories);

    public abstract Collection getCategoryFieldValues();

    public abstract void setCategoryFieldValues(Collection categoryFieldValues);

    public abstract Collection getCategoryRelations();

    public abstract void setCategoryRelations(Collection categoryRelations);

    public abstract Category getParentCategoryObj();

    public abstract void setParentCategoryObj(Category parentCategoryObj);

    public abstract Integer getSecondGroupId();

    public abstract void setSecondGroupId(Integer secondGroupId);

    public abstract CategoryGroup getSecondCategoryGroup();

    public abstract void setSecondCategoryGroup(CategoryGroup secondCategoryGroup);

    public Collection ejbHomeSelectCategoriesWithOutGroup(String tableId, String secondTableId, Integer companyId) throws FinderException {
        StringBuffer query = new StringBuffer();
        query.append("SELECT Object(a) FROM Category as a ")
                .append(" WHERE ((a.table=").append("'" + tableId + "'").append(" AND a.categoryGroupId IS NULL)")
                .append(" OR")
                .append(" (a.table=").append("'" + secondTableId + "'").append(" AND " + (CategoryUtil.useCategorySecondGroupId(tableId, secondTableId) ? "a.secondGroupId" : "a.categoryGroupId") + " IS NULL))")
                .append(" AND a.companyId=")
                .append(companyId)
                .append(" ORDER BY a.sequence ASC");

        log.debug("The ejbHome sql = " + new String(query));
        Object args[] = {};

        return ejbSelectCategoriesWithOutGroup(query.toString(), args);
    }

    public abstract Collection ejbSelectCategoriesWithOutGroup(String sql, Object[] params) throws FinderException;

    public abstract String getFieldIdentifier();

    public abstract void setFieldIdentifier(String fieldIdentifier);
}
   