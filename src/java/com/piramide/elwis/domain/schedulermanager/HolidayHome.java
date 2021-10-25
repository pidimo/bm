package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 5, 2005
 * Time: 11:17:16 AM
 * To change this template use File | Settings | File Templates.
 */

public interface HolidayHome extends EJBLocalHome {
    Holiday create(ComponentDTO dto) throws CreateException;

    Holiday findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCompanyId(Integer companyId) throws FinderException;

    Collection findOnlyForJanuaryInDateType(Integer countryId, Integer companyId) throws FinderException;

    Collection findAllOnlyForJanuaryInDateType(Integer countryId) throws FinderException;

    Collection findByDateRangeInDateType(Integer countryId,
                                         Integer minDate, Integer maxDate,
                                         Integer companyId) throws FinderException;

    Collection findAllByDateRangeInDateType(Integer minDate, Integer maxDate,
                                            Integer companyId) throws FinderException;

    Collection findOnlyForJanuaryInOccurrenceType(Integer countryId, Integer companyId) throws FinderException;

    Collection findAllOnlyForJanuaryInOccurrenceType(Integer companyId) throws FinderException;

    Collection findByMonthRangeInOccurrenceType(Integer countryId, Integer minMonth,
                                                Integer maxMonth, Integer companyId) throws FinderException;

    Collection findAllByMonthRangeInOccurrenceType(Integer minMonth, Integer maxMonth, Integer companyId) throws FinderException;
}
