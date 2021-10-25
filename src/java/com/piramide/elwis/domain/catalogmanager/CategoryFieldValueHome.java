package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CategoryFieldValueHome extends EJBLocalHome {
    com.piramide.elwis.domain.catalogmanager.CategoryFieldValue findByPrimaryKey(Integer key) throws FinderException;

    public CategoryFieldValue create(ComponentDTO dto) throws CreateException;

    public Collection findCategoryUsages(Integer categoryId, Integer companyId) throws FinderException;

    public Collection findCategoryValueUsages(Integer categoryId, Integer companyId) throws FinderException;

    public CategoryFieldValue findCategoryTypeUsages(Integer categoryId, Integer companyId) throws FinderException;

    public Collection findByProductId(Integer productId, Integer companyId) throws FinderException;

    public Collection findBySalesProcessId(Integer processId, Integer companyId) throws FinderException;

    public Collection findByCustomerId(Integer customerId, Integer companyId) throws FinderException;

    public Collection findByAddressId(Integer addressId, Integer companyId) throws FinderException;

    public Collection findByAddressIdAndContactPersonId(Integer addressId, Integer contactPersonId, Integer companyId) throws FinderException;

    public CategoryFieldValue findByAttachId(Integer attachId, Integer companyId) throws FinderException;

    public Collection findBySalePositionId(Integer salePositionId, Integer companyId) throws FinderException;


    public Collection findValueByAddressId(Integer addressId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findValueByContactPersonId(Integer addressId, Integer contactPersonId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findValueByCustomerId(Integer customerId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findValueByProductId(Integer productId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findValueBySalesProcessId(Integer processId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findValueBySalePositionId(Integer salePositionId, Integer companyId, Integer valueId) throws FinderException;

    public Collection findByAddressIdCategoryId(Integer addressId, Integer categoryId, Integer companyId) throws FinderException;

    public Collection findByAddressIdContactPersonIdCategoryId(Integer addressId, Integer contactPersonId, Integer categoryId, Integer companyId) throws FinderException;

    public Collection findByCustomerIdCategoryId(Integer customerId, Integer categoryId, Integer companyId) throws FinderException;
}
