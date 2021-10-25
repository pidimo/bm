package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.catalogmanager.Country;
import com.piramide.elwis.domain.catalogmanager.CountryHome;
import com.piramide.elwis.domain.schedulermanager.Holiday;
import com.piramide.elwis.dto.schedulermanager.HolidayDTO;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: LoadHolidaysCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class LoadHolidaysCmd extends EJBCommand {
    private Log log = LogFactory.getLog(LoadHolidaysCmd.class);
    private DateTime startRangeDate;
    private DateTime endRangeDate;
    private Map holidays;
    int monthStart;
    int monthEnd;
    int sday;
    int eday;
//    public static final String EMPTY_COUNTRY = "EMPTY_COUNTRY";

    public LoadHolidaysCmd() {
        holidays = new HashMap();
    }

    public void executeInStateless(SessionContext ctx) {
        holidays = new HashMap();
        startRangeDate = (DateTime) paramDTO.get("startRangeDate");
        endRangeDate = (DateTime) paramDTO.get("endRangeDate");
        //DateTimeZone zone = (DateTimeZone) paramDTO.get("timeZone");
        monthStart = startRangeDate.getMonthOfYear();
        monthEnd = endRangeDate.getMonthOfYear();
        sday = startRangeDate.getDayOfMonth();
        eday = endRangeDate.getDayOfMonth();
        Integer companyId = (Integer) paramDTO.get("companyId");

        Integer countryId = null;
        String countryCode = null;
        if (paramDTO.containsKey("countryId")) {
            countryId = (Integer) paramDTO.get("countryId");
            countryCode = (String) paramDTO.get("countryCode");
        }
        Collection holidaysDateType;
        Collection holidaysOccurrenceType;
        boolean showOnlyForConfiguredCountry = countryId != null;
        try {
            if (((Integer) paramDTO.get("type")).intValue() == 4) {//YEARLY_VIEW_TYPE
                monthStart = 1;
                monthEnd = 12;
            }

            //      log.debug("MonthStart:" + monthStart + "-MonthEnd:" + monthEnd);
            String finderByDate;
            String finderByOccurrence;
            Object[] args;

            boolean specialFinder = false;
            if (monthStart > monthEnd) {
                specialFinder = true;
            }

            if (showOnlyForConfiguredCountry) {
                if (!specialFinder) {
                    finderByDate = "findByDateRangeInDateType";
                    finderByOccurrence = "findByMonthRangeInOccurrenceType";
                    args = new Object[]{countryId, new Integer(monthStart), new Integer(monthEnd), companyId};
                } else {
                    finderByDate = "findOnlyForJanuaryInDateType";
                    finderByOccurrence = "findOnlyForJanuaryInOccurrenceType";
                    args = new Object[]{countryId, companyId};
                }
            } else {
                //              log.debug("Find all holidays");
                if (!specialFinder) {
                    finderByDate = "findAllByDateRangeInDateType";
                    finderByOccurrence = "findAllByMonthRangeInOccurrenceType";
                    args = new Object[]{new Integer(monthStart), new Integer(monthEnd), companyId};
                } else {
                    finderByDate = "findAllOnlyForJanuaryInDateType";
                    finderByOccurrence = "findAllOnlyForJanuaryInOccurrenceType";
                    args = new Object[]{companyId};
                }
            }
//            log.debug("Executing:" + finderByDate + " - " + finderByOccurrence);
            holidaysDateType = (Collection) EJBFactory.i.callFinder(new HolidayDTO(), finderByDate, args);
            holidaysOccurrenceType = (Collection) EJBFactory.i.callFinder(new HolidayDTO(), finderByOccurrence, args);
        } catch (EJBFactoryException e) {
            log.error("Unexpected error", e);
            return;
        }
        //log.debug("holidaysDateType:" + holidaysDateType);
        //log.debug("holidaysOccurrenceType:" + holidaysOccurrenceType);
        CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
        // Proccess holidayDateType
        addHolidays(holidaysDateType, showOnlyForConfiguredCountry, countryCode, countryId, countryHome, true);
        //Proccess holidayOccurrenceType
        addHolidays(holidaysOccurrenceType, showOnlyForConfiguredCountry, countryCode, countryId, countryHome, false);
    }

    private void addHolidays(Collection holidayList, boolean showOnlyForConfiguredCountry, String countryCode,
                             Integer countryId, CountryHome countryHome, boolean isDateType) {
        for (Iterator iterator = holidayList.iterator(); iterator.hasNext();) {
            Holiday holiday = (Holiday) iterator.next();

            String finalCountryCode = "";//countryCode;
            //boolean addHoliday = true;
            if (showOnlyForConfiguredCountry) {
                if (holiday.getCountryId() != null) {
                    finalCountryCode = countryCode;
                }
            } else {
                if (holiday.getCountryId() != null) {
                    try {
                        Country country = countryHome.findByPrimaryKey(holiday.getCountryId());
                        finalCountryCode = country.getCountryAreaCode();
                    } catch (FinderException e) {
                        log.error("Unexpected error", e);
                    }
                } /*else
                    addHoliday = false;*/
            }

            //if (addHoliday) {
            if (isDateType) {
                int y = 0;
                if (holiday.getOccurrence() != null) {
                    y = holiday.getOccurrence().intValue();
                }
                addHoliday(holiday.getDay().intValue(), holiday.getMonth().intValue(), y, holiday.getTitle(), finalCountryCode, holiday.getMoveToMonday().booleanValue());
            } else {
                int year = ((Integer) paramDTO.get("year")).intValue();
                DateTime date = getValidDateForOccurrenceType(new DateTime(year, holiday.getMonth().intValue(), 1, 0, 1, 0, 0),
                        holiday.getDay().intValue(), holiday.getOccurrence().intValue());
                if (date != null) {
                    addHoliday(date.getDayOfMonth(), date.getMonthOfYear(), date.getYear(), holiday.getTitle(), finalCountryCode, holiday.getMoveToMonday().booleanValue());
                }
            }
            //}
        }
    }

    private void addHoliday(int day, int month, int year, String title, String country, boolean moveNextMonday) {
        DateTime nextMonday = null;
        boolean showMoveNext = false;
        if (moveNextMonday) {
            nextMonday = moveToNextMonday(day, month, year);
            if (nextMonday != null) {
                showMoveNext = true;
            }
        }
        if (isValidDate(day, month, year)) {
            add(day + "-" + month, title, country, showMoveNext);
        }

        if (isValidDate(nextMonday)) {
            add(nextMonday.getDayOfMonth() + "-" + nextMonday.getMonthOfYear(), title, country, showMoveNext);
        }
    }

    private void add(String holidayKey, String title, String country, boolean showMoveNext) {

        if (!holidays.containsKey(holidayKey)) {
            holidays.put(holidayKey, new HashMap());
        }
        Map holidayByCountry = (Map) holidays.get(holidayKey);
        if (!holidayByCountry.containsKey(country)) {
            holidayByCountry.put(country, new ArrayList());
        }
        List holidayTitles = (List) holidayByCountry.get(country);
        holidayTitles.add(Collections.singletonMap(title, Boolean.valueOf(showMoveNext)));

    }

    private boolean isValidDate(DateTime date) {
        return date != null && isValidDate(date.getDayOfMonth(), date.getMonthOfYear(), date.getYear());
    }

    private boolean isValidDate(int day, int month, int year) {
        //       log.debug("d:" + day + "-sd:" + sday + "-ed:" + eday + " - m:" + month + "-sm:" + monthStart + "-em:" + monthEnd);
        if (year > 0 && !(startRangeDate.getYear() <= year && year <= endRangeDate.getYear())) {
            return false;
        }

        if (monthStart <= monthEnd && (month < monthStart || monthEnd < month)) {
            return false;
        }

        if (month == monthStart && day < sday) {
            return false;
        }
        if (month == monthEnd && day > eday) {
            return false;
        }
        return true;
/*
        int holiday = (endRangeDate.getYear() - startRangeDate.getYear()) * 1000 +  month * 100 + day;
        log.debug("holiday:"+holiday+" - sd:"+startDate+" - ed:"+endDate);
        return startDate <= holiday && holiday <= endDate;*/
    }

    private DateTime getValidDateForOccurrenceType(DateTime date, int dayOfweek, int weekOfMonth) {
        DateTime result;
        RecurrenceManager manager = new RecurrenceManager();
        if (weekOfMonth == SchedulerConstants.MONTHLY_OCCUR_LAST) {
            result = date.dayOfMonth().setCopy(date.dayOfMonth().getMaximumValue());
            int lastMonth = result.monthOfYear().get();
            result = result.dayOfWeek().setCopy(dayOfweek);
            int newMonth = result.monthOfYear().get();
            if (newMonth != lastMonth) //if the date is in the next month, then decrease one week
            {
                result = result.minus(Period.weeks(1));
            }
        } else {
            result = manager.getOccurenceDayOfMonth(date, dayOfweek, weekOfMonth);
        }

        if (result == null) {
            return null;
        }
        return result;
    }

    private DateTime moveToNextMonday(int day, int month, int year) {
        DateTime result = new DateTime(year, month, day, 0, 0, 0, 0);
        DateTime date = null;
        if (result.getDayOfWeek() > 5) {
            date = result.plus(Period.days(8 - result.getDayOfWeek()));
        }
        return date;
    }

    public boolean isStateful() {
        return false;
    }

    public Map getHolidays() {
        log.debug("HOLIDAYS:" + holidays);
        return holidays;
    }
}

