package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import org.joda.time.*;

import java.util.*;

import static com.piramide.elwis.utils.DateUtils.dateToInteger;

/**
 * Class to calculate the recurrences dates
 *
 * @author Fernando Montaño
 * @version $Id: RecurrenceManager.java 10062 2011-06-28 14:40:10Z fernando $
 */

public class RecurrenceManager {

    List exceptions; //list of intervals exceptions
    DateTime appointmentStartDateTime;
    int recurEvery;
    String rangeType;
    Integer rangeValue;
    String recurType;
    String recurValue;
    String recurValueType;

    DateTime initialDateTime;
    DateTime rootInitialDateTime;
    int dayOnWeekPos;
    DateTimeZone ownerTimeZone;

    /**
     * Calculate the next Weekly series DateTime.
     *
     * @param appointmentStartDateTime the appointment start time.
     * @param exceptions               the exceptions for the recurrence.
     * @param recurEvery               the interval of recurrence
     * @param recurType                the type of recurrence
     * @param recurValue               the value of the recurrence. To weeks they are  1-7 or Monday to Sunday (a list).
     * @param rangeType                the type of range
     * @param rangeValue               the value of the range, if defined.
     * @param recurValueType           the recurValueType if avalaible
     */
    public RecurrenceManager(List exceptions, DateTime appointmentStartDateTime, int recurEvery, String rangeType,
                             Integer rangeValue, String recurType, String recurValue, String recurValueType,
                             DateTimeZone ownerTimeZone) {
        this.appointmentStartDateTime = appointmentStartDateTime;
        this.recurEvery = recurEvery;
        this.rangeType = rangeType;
        this.rangeValue = rangeValue;
        this.recurValue = recurValue;
        this.recurValueType = recurValueType;
        this.recurType = recurType;

        ///for the getNextDateTime() method only
        initialDateTime = appointmentStartDateTime;
        rootInitialDateTime = appointmentStartDateTime;
        dayOnWeekPos = 1;
        this.ownerTimeZone = ownerTimeZone;
        this.exceptions = exceptions;
    }

    public RecurrenceManager cloneMe() {
        return new RecurrenceManager(new ArrayList(), appointmentStartDateTime, recurEvery, rangeType, rangeValue, recurType, recurValue, recurValueType, ownerTimeZone);
    }

    public RecurrenceManager() {
    }


    /**
     * Calculate the next DateTime for daily recurrence
     *
     * @param initialDateTime the initial date time to start the calculations
     * @return the next RecurDateTime of the serie if there is one, null if there is no more dates for the serie.
     */
    public RecurDateTime getNextDailyDateTime(DateTime initialDateTime) {
        DateTime nextDateTime = initialDateTime.plus(Period.days(recurEvery));
        if (isNextDateTimeInValidRange(nextDateTime)) {
            if (exceptions.contains(dateToInteger(nextDateTime))) {
                return getNextDailyDateTime(nextDateTime);
            } else {
                return new RecurDateTime(nextDateTime);
            }
        }
        return null; //there is no avalaible next date in the recurrence
    }

    /**
     * Return the next date in the day series given an initial time which it does not belongs necessary to the series.
     *
     * @param anyDateTime the initial date to get next date in the serie
     * @return return the next date in the series
     */
    public RecurDateTime getNextDailyDateTimeFor(DateTime anyDateTime) {
        Period period = new Period(appointmentStartDateTime, anyDateTime, PeriodType.days());
        int diff = period.getDays();
        int intervalsToJump = diff / recurEvery;
        DateTime seriesDateTime = appointmentStartDateTime;
        seriesDateTime = seriesDateTime.plus(Period.days(recurEvery * intervalsToJump));

        return getNextDailyDateTime(seriesDateTime);
    }


    /**
     * Calculate the next Weekly series DateTime.
     *
     * @param initialDateTime     the initial date time to calculate the next
     * @param rootInitialDateTime the initial root date time to calculate the next
     * @param dayOnWeekPosition   the last number of day in the week serie (1 - 7) of the last recurrence.
     * @return a RecurDateTime object if there is a next avalaible, null otherwise.
     */
    public RecurDateTime getNextWeeklyDateTime(DateTime initialDateTime, DateTime rootInitialDateTime,
                                               int dayOnWeekPosition) {
        List recurDays = processRecurrenceDaysOfWeek(recurValue);

        Interval weekInterval = new Interval(initialDateTime,
                initialDateTime.plus(Period.days(7 - dayOnWeekPosition)));

        RecurDateTime nextDateTime = getNextAvailableDateTimeOnWeekSeries(recurDays, weekInterval, rootInitialDateTime,
                dayOnWeekPosition);

        if (isNextDateTimeInValidRange(nextDateTime.getDateTime())) {
            if (exceptions.contains(dateToInteger(nextDateTime.getDateTime()))) {
                int currentPos = Integer.parseInt(nextDateTime.getData());
                if (currentPos < 7) {
                    return getNextWeeklyDateTime(nextDateTime.getDateTime().plus(Period.days(1)), rootInitialDateTime,
                            currentPos + 1);
                } else { //if it is the 7 day (last of the week)
                    return getNextWeeklyDateTime(nextDateTime.getDateTime().minus(Period.days(6)).
                            plus(Period.weeks(recurEvery)), rootInitialDateTime,
                            Integer.parseInt(nextDateTime.getData()));
                }

            } else {
                return nextDateTime;
            }
        }
        return null; //there is no avalaible next date in the recurrence
    }

    /**
     * Return the next date in the week series given an initial time which it does not belongs necessary to the series.
     *
     * @param anyDateTime the initial date to get next date in the serie
     * @return return the next date in the serie
     */
    public RecurDateTime getNextWeeklyDateTimeFor(DateTime anyDateTime) {
        Period period = new Period(appointmentStartDateTime, anyDateTime, PeriodType.weeks());
        int diff = period.getWeeks();
        int intervalsToJump = diff / recurEvery;
        DateTime seriesDateTime = appointmentStartDateTime;
        seriesDateTime = seriesDateTime.plus(Period.weeks(recurEvery * intervalsToJump));
        RecurDateTime firstOnWeekDate = getNextWeeklyDateTime(seriesDateTime, seriesDateTime, 1);
        RecurDateTime nextDateTime = null;
        if (firstOnWeekDate != null) { //the first on week is valid
            if (firstOnWeekDate.getMillis() <= anyDateTime.getMillis()) {
                DateTime initDate = firstOnWeekDate.getDateTime();
                DateTime rootDate = firstOnWeekDate.getDateTime();
                int initWeekPos = Integer.parseInt(firstOnWeekDate.getData());
                while ((nextDateTime = getNextWeeklyDateTime(initDate, rootDate, initWeekPos)) != null) {
                    if (nextDateTime.getMillis() > anyDateTime.getMillis()) {
                        break;
                    } else {
                        initDate = nextDateTime.getDateTime();
                        rootDate = nextDateTime.getDateTime();
                        initWeekPos = Integer.parseInt(nextDateTime.getData());
                    }
                }
            } else {
                nextDateTime = firstOnWeekDate;
            }
        }
        return nextDateTime;
    }

    /**
     * Calculate the next avalaible date in a montlhy recurrence type
     *
     * @param initialDateTime     initial date root of the serie
     * @param rootInitialDateTime the root of the first date of the occurence in the serie.
     * @return a RecurDateTime if there is a next available, null if there is no more recurrence avalaible.
     */
    public RecurDateTime getNextMonthlyDateTime(DateTime initialDateTime, DateTime rootInitialDateTime) {
        DateTime nextDateTime = null;
        if (SchedulerConstants.RECUR_MONTHLY_DAY_OF_MONTH.equals(recurValueType)) { //recur on day of month
            int dayOfMonth = Integer.valueOf(recurValue).intValue();
            try {
                nextDateTime = initialDateTime.dayOfMonth().setCopy(dayOfMonth);
                if (!isDateTimeAfterThan(nextDateTime, rootInitialDateTime)) {
                    nextDateTime = initialDateTime.plus(Period.months(recurEvery)).dayOfMonth().setCopy(dayOfMonth);
                }

            } catch (IllegalArgumentException e) { //if set 31 to february month or out of range day for a month
                return getNextMonthlyDateTime(initialDateTime.plus(Period.months(recurEvery)), rootInitialDateTime);
            }

        } else { //recur on a ocurrence of day
            String[] occurAndDay = recurValue.split(SchedulerConstants.RECURRENCE_VALUES_SEPARATOR);
            int ocurrence = new Integer(occurAndDay[0]).intValue();
            int onDay = new Integer(occurAndDay[1]).intValue();
            int lastMonth;
            int newMonth;
            //if it is the last to check
            if (SchedulerConstants.MONTHLY_OCCUR_LAST == ocurrence) {
                nextDateTime = initialDateTime.dayOfMonth().setCopy(initialDateTime.dayOfMonth().getMaximumValue());
                lastMonth = nextDateTime.monthOfYear().get();
                nextDateTime = nextDateTime.dayOfWeek().setCopy(onDay);
                newMonth = nextDateTime.monthOfYear().get();
                if (newMonth != lastMonth) //if the date is in the next month, then decrease one week
                {
                    nextDateTime = nextDateTime.minus(Period.weeks(1));
                }
            } else { //for the another types of
                nextDateTime = initialDateTime.dayOfMonth().setCopy(1); //get the first day of the month
                nextDateTime = getOccurenceDayOfMonth(nextDateTime, onDay, ocurrence);

                if (nextDateTime == null) { //if there is no a valid occurence in this month
                    if (getNextMonthlyDateTime(initialDateTime.dayOfMonth().setCopy(1).plus(Period.months(recurEvery)),
                            rootInitialDateTime) != null) {
                        nextDateTime = getNextMonthlyDateTime(initialDateTime.dayOfMonth().setCopy(1).plus(Period.months(recurEvery)),
                                rootInitialDateTime).getDateTime();
                    } else {
                        return null; //the nexttime has finished by the range, so there is no a new valid one. So return null.
                    }

                }
            }


            if (!isDateTimeAfterThan(nextDateTime, rootInitialDateTime)) {
                return getNextMonthlyDateTime(initialDateTime.dayOfMonth().setCopy(1).plus(Period.months(recurEvery)),
                        rootInitialDateTime);
            }

        }

        if (isNextDateTimeInValidRange(nextDateTime)) {
            if (exceptions.contains(dateToInteger(nextDateTime))) {
                return getNextMonthlyDateTime(nextDateTime.plus(Period.months(recurEvery)), rootInitialDateTime);
            } else {
                return new RecurDateTime(nextDateTime);
            }
        }
        return null; //there is no avalaible next date in the recurrence
    }


    /**
     * Return the next date in the month series given an initial time which it does not belongs necessary to the series.
     *
     * @param anyDateTime the initial date to get next date in the serie
     * @return return the next date in the serie
     */
    public RecurDateTime getNextMonthlyDateTimeFor(DateTime anyDateTime) {
        Period period = new Period(appointmentStartDateTime, anyDateTime, PeriodType.months());
        int diff = period.getMonths();
        int intervalsToJump = diff / recurEvery;
        DateTime seriesDateTime = appointmentStartDateTime;
        seriesDateTime = seriesDateTime.plus(Period.months(recurEvery * intervalsToJump));

        RecurDateTime firstOccurrence = getNextMonthlyDateTime(seriesDateTime, seriesDateTime);

        RecurDateTime nextDateTime = null;
        if (firstOccurrence != null) { //the first on week is valid
            if (firstOccurrence.getMillis() <= anyDateTime.getMillis()) {
                DateTime initDate = firstOccurrence.getDateTime();
                DateTime rootDate = firstOccurrence.getDateTime();
                while ((nextDateTime = getNextMonthlyDateTime(initDate, rootDate)) != null) {
                    if (nextDateTime.getMillis() > anyDateTime.getMillis()) {
                        break;
                    } else {
                        initDate = nextDateTime.getDateTime();
                        rootDate = nextDateTime.getDateTime();
                    }
                }
            } else {
                nextDateTime = firstOccurrence;
            }
        }
        return nextDateTime;
    }


    /**
     * Calculate a next recurrent DateTime in one recurrence serie
     *
     * @param initialDateTime the initial date time to start.
     * @return a RecurDateTime is there is an avalaible in the serie, null if there is no more avalaible.
     */
    public RecurDateTime getNextYearlyDateTime(DateTime initialDateTime) {
        DateTime nextDateTime = null;

        int initialDayOfMonth = appointmentStartDateTime.getDayOfMonth();

        if (SchedulerConstants.RECUR_YEARLY_ON_MONTH.equals(recurValueType)) { //recur on the same initial day but in a selected month
            int month = Integer.parseInt(recurValue);
            nextDateTime = initialDateTime.monthOfYear().setCopy(month);
            if (appointmentStartDateTime.year().isLeap() && month == DateTimeConstants.FEBRUARY
                    && initialDayOfMonth == 29) {
                try {
                    nextDateTime = nextDateTime.dayOfMonth().setCopy(initialDayOfMonth);
                } catch (IllegalArgumentException e) {
                    nextDateTime = getNextAvailableYearDateTime(nextDateTime, initialDayOfMonth, recurEvery);
                }
            } else {
                try {
                    nextDateTime = nextDateTime.dayOfMonth().setCopy(initialDayOfMonth);
                } catch (IllegalArgumentException e) {
                    /**
                     * it means the recurrence is invalid, example start on 2005-01-31
                     * and try to recur on February 31, this is invalid.
                     */
                    return null;
                }
            }

        } else { //recur on the same day/month every year
            DateTime onMonthDateTime = initialDateTime.monthOfYear().setCopy(appointmentStartDateTime.getMonthOfYear());
            nextDateTime = getNextAvailableYearDateTime(onMonthDateTime, initialDayOfMonth, recurEvery);
        }
        if (!isDateTimeAfterThan(nextDateTime, initialDateTime)) {
            nextDateTime = getNextAvailableYearDateTime(nextDateTime, initialDayOfMonth, recurEvery);
        }
        if (isNextDateTimeInValidRange(nextDateTime)) {
            if (exceptions.contains(dateToInteger(nextDateTime))) {
                return getNextYearlyDateTime(nextDateTime);
            } else {
                return new RecurDateTime(nextDateTime);
            }
        }
        return null; //there is no available next date in the recurrence
    }

    /**
     * Calculate the next available year for the recurrence
     *
     * @param initialDateTime   the initial date
     * @param initialDayOfMonth the month which you want to recur
     * @param recurEvery        the interval
     * @return the next dateTime
     */
    private DateTime getNextAvailableYearDateTime(DateTime initialDateTime, int initialDayOfMonth, int recurEvery) {
        try {
            return initialDateTime.plus(Period.years(recurEvery)).dayOfMonth().setCopy(initialDayOfMonth);
        } catch (IllegalArgumentException e) { //if set to an invalid day in the respective month
            return getNextAvailableYearDateTime(initialDateTime.plus(Period.years(recurEvery)), initialDayOfMonth,
                    recurEvery);
        }
    }

    /**
     * Return the next date in the year series given an initial time which it does not belongs necessary to the series.
     *
     * @param anyDateTime the initial date to get next date in the serie
     * @return return the next date in the serie
     */
    public RecurDateTime getNextYearlyDateTimeFor(DateTime anyDateTime) {
        Period period = new Period(appointmentStartDateTime, anyDateTime, PeriodType.years());
        int diff = period.getYears();
        int intervalsToJump = diff / recurEvery;
        DateTime seriesDateTime = appointmentStartDateTime;
        seriesDateTime = seriesDateTime.plus(Period.years(recurEvery * intervalsToJump)).minus(Period.years(1));//decrease to ensure next greather
        RecurDateTime firstOccurrence = getNextYearlyDateTime(seriesDateTime);

        RecurDateTime nextDateTime = null;
        if (firstOccurrence != null) { //the first on week is valid
            if (firstOccurrence.getMillis() <= anyDateTime.getMillis()) {
                DateTime initDate = firstOccurrence.getDateTime();
                while ((nextDateTime = getNextYearlyDateTime(initDate)) != null) {
                    if (nextDateTime.getMillis() > anyDateTime.getMillis()) {
                        break;
                    } else {
                        initDate = nextDateTime.getDateTime();
                    }
                }
            } else {
                nextDateTime = firstOccurrence;
            }
        }
        return nextDateTime;

    }

    /**
     * Return the next date in the series given an initial time which it does not belongs necessary to the series.
     *
     * @param anyTime any initial date to get next date in the series greater than such date
     * @return return the next date in the series, null if there's no next valid recurrence date
     */
    public RecurDateTime getNextDateTimeFor(DateTime anyTime) {
        if (SchedulerConstants.RECURRENCE_DAILY.equals(recurType)) {
            return getNextDailyDateTimeFor(anyTime);
        } else if (SchedulerConstants.RECURRENCE_WEEKLY.equals(recurType)) {
            return getNextWeeklyDateTimeFor(anyTime);
        } else if (SchedulerConstants.RECURRENCE_MONTHLY.equals(recurType)) {
            return getNextMonthlyDateTimeFor(anyTime);
        } else { //year
            return getNextYearlyDateTimeFor(anyTime);
        }
    }


    /**
     * Return a list of RecurDateTime objects, the list means all recurrences ocurrences between both given datetimes.
     * If the range contains the appointment start time, such date is removed from the result, because this list
     * returns only the recurrences, and the recurrences always should be greather than creation date.
     * Note: All dates must have defined the TimeZone, also the start and end range date time. They must be with the
     * appointment start date timezone.
     *
     * @param startRangeTime the start time, as starting. The start is took as midnight
     * @param endRangeTime   the end time, as limit. The limit is taked as midnight of one day in the future.
     * @return a list of RecurDateTime object which recurrence coincides between given two datetimes.
     */
    public List getRecurrencesBetween(DateTime startRangeTime, DateTime endRangeTime) {
        List result = new LinkedList();
        DateMidnight first = startRangeTime.toDateMidnight().minus(Period.days(1));
        Interval interval = new Interval(first, endRangeTime.plus(Period.days(1)).toDateMidnight());
        RecurDateTime seriesTime = null;

        DateTime firstTime = first.toDateTime(startRangeTime.getZone());
        if (SchedulerConstants.RECURRENCE_DAILY.equals(recurType)) {
            while ((seriesTime = getNextDailyDateTimeFor(firstTime)) != null) {
                if (interval.contains(seriesTime.getDateTime())) {
                    if (seriesTime.getMillis() >= startRangeTime.toDateMidnight().getMillis() &&
                            seriesTime.getMillis() > appointmentStartDateTime.getMillis()) {
                        result.add(seriesTime.getDateTime());
                    }
                    firstTime = seriesTime.getDateTime();
                } else {
                    break;
                }
            }
        } else if (SchedulerConstants.RECURRENCE_WEEKLY.equals(recurType)) {
            while ((seriesTime = getNextWeeklyDateTimeFor(firstTime)) != null) {
                if (interval.contains(seriesTime.getDateTime())) {
                    if (seriesTime.getMillis() >= startRangeTime.toDateMidnight().getMillis() &&
                            seriesTime.getMillis() > appointmentStartDateTime.getMillis()) {
                        result.add(seriesTime.getDateTime());
                    }
                    firstTime = seriesTime.getDateTime();
                } else {
                    break;
                }
            }

        } else if (SchedulerConstants.RECURRENCE_MONTHLY.equals(recurType)) {
            while ((seriesTime = getNextMonthlyDateTimeFor(firstTime)) != null) {
                if (interval.contains(seriesTime.getDateTime())) {
                    if (seriesTime.getMillis() >= startRangeTime.toDateMidnight().getMillis() &&
                            seriesTime.getMillis() > appointmentStartDateTime.getMillis()) {
                        result.add(seriesTime.getDateTime());
                    }
                    firstTime = seriesTime.getDateTime();
                } else {
                    break;
                }
            }
        } else { //year
            while ((seriesTime = getNextYearlyDateTimeFor(firstTime)) != null) {
                if (interval.contains(seriesTime.getDateTime())) {
                    if (seriesTime.getMillis() >= startRangeTime.toDateMidnight().getMillis() &&
                            seriesTime.getMillis() > appointmentStartDateTime.getMillis()) {
                        result.add(seriesTime.getDateTime());
                    }
                    firstTime = seriesTime.getDateTime();
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Check if a date is a valid date in the range of the appointment.
     *
     * @param nextDateTime the date time to validate
     * @return true if the given date is less than the range maximun date.
     */
    private boolean isNextDateTimeInValidRange(DateTime nextDateTime) {
        if (SchedulerConstants.RECUR_RANGE_NO_ENDING.equals(rangeType)) //no ending date
        {
            return true; //is valid because it don't have ending date.
        } else if (SchedulerConstants.RECUR_RANGE_AFTER_OCCURRENCE.equals(rangeType)) { //end after n ocurrences
            if (SchedulerConstants.RECURRENCE_DAILY.equals(recurType)) { //daily
                return (dateToInteger(nextDateTime) <=
                        dateToInteger(appointmentStartDateTime.plus(Period.days(recurEvery * rangeValue - 1))));
            } else if (SchedulerConstants.RECURRENCE_WEEKLY.equals(recurType)) { //weekly

                return (dateToInteger(nextDateTime) <= dateToInteger(lastValidWeekOccurrence(appointmentStartDateTime,
                        recurEvery, rangeValue, processRecurrenceDaysOfWeek(recurValue))));

            } else if (SchedulerConstants.RECURRENCE_MONTHLY.equals(recurType)) { //monthly
                return (dateToInteger(nextDateTime) <=
                        dateToInteger(appointmentStartDateTime.plus(Period.months(recurEvery *
                                rangeValue - 1)).dayOfMonth().setCopy(appointmentStartDateTime.plus(Period.months(recurEvery *
                                rangeValue - 1)).dayOfMonth().getMaximumValue())));
            } else { //yearly
                return (dateToInteger(nextDateTime) <=
                        dateToInteger(appointmentStartDateTime.plus(Period.years(recurEvery *
                                rangeValue - 1))));
            }
        } else if (SchedulerConstants.RECUR_RANGE_DATE.equals(rangeType)) { //end the range in a specific date
            return dateToInteger(nextDateTime) <= rangeValue;
        }
        return false;
    }

    /**
     * Returns the last valid DateTime in the recurrence for the weekly recurrence based on its occurrences number.
     *
     * @param startTime        the appointment start time
     * @param recurEvery       the interval of recurrence (every 1 week, 2 weeks...)
     * @param totalOccurrences the number of valid occurrences for the serie
     * @param recurDays        the valid days of the week for the recurrence
     * @return the date time of the last valid reccurrence in the weekly series.
     */
    private DateTime lastValidWeekOccurrence(DateTime startTime, int recurEvery, int totalOccurrences, List<Integer> recurDays) {
        int lastWeek = lastWeekOfOccurrence(totalOccurrences, recurDays.size());
        int currentOcurrences = (lastWeek * recurDays.size()) - recurDays.size();
        DateTime lastWeekFirstDate = startTime.plus(Period.weeks((lastWeek - 1) * recurEvery));
        Interval weekInterval = new Interval(lastWeekFirstDate, lastWeekFirstDate.plus(Period.days(6)));
        DateTime lastOcurrenceDatetime = null;
        for (int i = 0; i <= weekInterval.toPeriod().getDays(); i++) {
            lastOcurrenceDatetime = weekInterval.getStart().plus(Period.days(i));
            if (recurDays.contains(lastOcurrenceDatetime.getDayOfWeek())) {
                currentOcurrences++;
                if (currentOcurrences == totalOccurrences) {
                    break;
                }
            }
        }
        return lastOcurrenceDatetime;
    }

    /**
     * Calculates the last occurrence week.
     *
     * @param totalOccurrences the max number or occurrences allowed
     * @param daysPerWeek      the number of days per week
     * @return the number of the week in which the recurrence should finish.
     */
    private int lastWeekOfOccurrence(int totalOccurrences, int daysPerWeek) {
        if (totalOccurrences % daysPerWeek == 0) {
            return totalOccurrences / daysPerWeek;
        } else {
            return totalOccurrences / daysPerWeek + 1;
        }
    }

    /**
     * Process days selected for one day separated by SchedulerConstants.RECURRENCE_VALUES_SEPARATOR string.
     *
     * @param recurValue the value
     * @return an List of days as integers with the days selected in the week.
     */
    private List<Integer> processRecurrenceDaysOfWeek(String recurValue) {
        List<Integer> result = new ArrayList<Integer>();
        StringTokenizer tokens = new StringTokenizer(recurValue, SchedulerConstants.RECURRENCE_VALUES_SEPARATOR);
        while (tokens.hasMoreTokens()) {
            result.add(new Integer(tokens.nextToken()));
        }
        return result;
    }


    /**
     * Process a next avalaible Date in one week which is represented as one interval of 1 - 7 days.
     *
     * @param recurDays           a list of the days configured to recur on.
     * @param weekInterval        the week interval
     * @param rootInitialDateTime The root datetime of the calculation
     * @param beforeDayOnWeekPos  the last day position within the last recurrence reminder.
     * @return a RecurDateTime corresponding the next day on the week.
     */
    private RecurDateTime getNextAvailableDateTimeOnWeekSeries(List recurDays, Interval weekInterval,
                                                               DateTime rootInitialDateTime, int beforeDayOnWeekPos) {

        Map avalaibleDaysOnRestOfWeek = new LinkedHashMap();
        for (int i = 0; i <= weekInterval.toPeriod().getDays(); i++) {
            avalaibleDaysOnRestOfWeek.put(new Integer(weekInterval.getStart().plus(Period.days(i)).getDayOfWeek()),
                    weekInterval.getStart().plus(Period.days(i)));
        }
        for (Iterator iterator = avalaibleDaysOnRestOfWeek.keySet().iterator(); iterator.hasNext(); ) {
            Integer day = (Integer) iterator.next();
            DateTime date = (DateTime) avalaibleDaysOnRestOfWeek.get(day);
            if (date.getMillis() > rootInitialDateTime.getMillis() && recurDays.contains(day)) {
                Interval positionInterval = new Interval(weekInterval.getStart(), date);
                return new RecurDateTime(date, String.valueOf(positionInterval.toPeriod().getDays() + beforeDayOnWeekPos));
            }
        }

        // if above for iteration does not return a valid date means that the week had end, so it is needed to start
        // the week serie using the recurevery attribute.
        DateTime firstDayOfTheSeriesWeek = weekInterval.getStart().minus(Period.days(beforeDayOnWeekPos - 1));
        firstDayOfTheSeriesWeek = firstDayOfTheSeriesWeek.plus(Period.weeks(recurEvery));
        weekInterval = new Interval(firstDayOfTheSeriesWeek,
                firstDayOfTheSeriesWeek.plus(Period.days(6)));
        return getNextAvailableDateTimeOnWeekSeries(recurDays, weekInterval, rootInitialDateTime, 1);
    }

    /**
     * Return a n day of occurence in the month.
     *
     * @param firstDayOfMonth the first day of the month
     * @param day             day of occurrence
     * @param occurrence      number of occurrence
     * @return the respective n day in the month if exists, null otherwise.
     */
    public DateTime getOccurenceDayOfMonth(DateTime firstDayOfMonth, int day, int occurrence) {
        DateTime result = null;
        Map occurMap = new LinkedHashMap();
        Interval interval = new Interval(firstDayOfMonth.toDateMidnight(),
                firstDayOfMonth.dayOfMonth().setCopy(firstDayOfMonth.dayOfMonth().getMaximumValue()).
                        plus(Period.days(1)).toDateMidnight());

        MutableDateTime mdt = firstDayOfMonth.toMutableDateTime(firstDayOfMonth.getZone());

        int di = 1;
        while (interval.contains(mdt)) {
            mdt.setDayOfWeek(day);
            if (interval.contains(mdt)) {
                occurMap.put(new Integer(di), mdt.toDateTime(mdt.getZone()));
                mdt.addWeeks(1);
                di++;
            } else {
                mdt.addWeeks(1);
            }
        }

        Integer occur = new Integer(occurrence);
        if (occurMap.containsKey(occur)) {
            result = (DateTime) occurMap.get(occur);
        }

        return result;
    }

    /**
     * Check if a Date is greather than another date only taking care on year/month/day without time.
     *
     * @param toCompareDate   to compare date
     * @param compareWithDate date with before date shall be compared
     * @return true if greater, false otherwise
     */
    public boolean isDateTimeAfterThan(DateTime toCompareDate, DateTime compareWithDate) {
        return toCompareDate.toDateMidnight().getMillis() > compareWithDate.toDateMidnight().getMillis();

    }

    public String getRecurValueType() {
        return recurValueType;
    }

    public void setInitialDateTime(DateTime appointmentStartDateTime) {
        this.initialDateTime = appointmentStartDateTime;
        this.rootInitialDateTime = appointmentStartDateTime;
    }

    public DateTime getAppointmentStartDateTime() {
        return appointmentStartDateTime;
    }

    public DateTime getInitialDateTime() {
        return initialDateTime;
    }

    public void setRangeValue(Integer rangeValue) {
        this.rangeValue = rangeValue;
    }

    public void setDayOnWeekPos(int dayOnWeekPos) {
        this.dayOnWeekPos = dayOnWeekPos;
    }


}
