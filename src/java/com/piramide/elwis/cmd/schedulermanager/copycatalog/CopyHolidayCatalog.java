package com.piramide.elwis.cmd.schedulermanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Country;
import com.piramide.elwis.domain.catalogmanager.CountryHome;
import com.piramide.elwis.domain.schedulermanager.Holiday;
import com.piramide.elwis.domain.schedulermanager.HolidayHome;
import com.piramide.elwis.dto.schedulermanager.HolidayDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyHolidayCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        copyHolidays(source.getCompanyId(), target.getCompanyId());
    }

    private void copyHolidays(Integer sourceCompanyId, Integer targetCompanyId) {
        HolidayHome holidayHome =
                (HolidayHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_HOLIDAY);

        Collection sourceHolidays = null;
        try {
            sourceHolidays = holidayHome.findByCompanyId(sourceCompanyId);
        } catch (FinderException e) {
            log.debug("Cannot read source holidays..." + sourceCompanyId);
        }

        if (null != sourceHolidays) {
            for (Object obj : sourceHolidays) {
                Holiday sourceHoliday = (Holiday) obj;
                HolidayDTO targetHolidayDTO = new HolidayDTO();
                DTOFactory.i.copyToDTO(sourceHoliday, targetHolidayDTO);
                targetHolidayDTO.put("companyId", targetCompanyId);
                targetHolidayDTO.remove("holidayId");

                Integer targetCountyId = null;
                if (null != sourceHoliday.getCountryId()) {
                    String sourceCountryName = null;
                    try {
                        sourceCountryName = getCountryName(sourceHoliday.getCountryId());
                    } catch (FinderException e) {
                        log.debug("Source Holiday " + sourceHoliday.getHolidayId() + " cannot country assignated");
                    }

                    try {
                        targetCountyId = getCountryId(sourceCountryName, targetCompanyId);
                    } catch (FinderException e) {
                        log.debug(sourceCountryName + " cannot registred in targetCompany " + targetCompanyId);
                    }
                }
                targetHolidayDTO.put("countryId", targetCountyId);

                try {
                    holidayHome.create(targetHolidayDTO);
                } catch (CreateException e) {
                    log.debug("Cannot create holiday for target company " + targetCompanyId);
                }
            }
        }
    }

    private String getCountryName(Integer countryId) throws FinderException {
        CountryHome countryHome =
                (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
        Country country = countryHome.findByPrimaryKey(countryId);
        return country.getCountryName();
    }

    private Integer getCountryId(String name, Integer companyId) throws FinderException {
        CountryHome countryHome =
                (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
        Country country = countryHome.findByCountryName(name, companyId);
        return country.getCountryId();
    }
}
