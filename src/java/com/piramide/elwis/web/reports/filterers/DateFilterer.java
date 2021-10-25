package com.piramide.elwis.web.reports.filterers;

import com.jatun.titus.listgenerator.engine.TitusOperator;
import com.jatun.titus.listgenerator.engine.fantabulous.CustomCondition;
import com.piramide.elwis.utils.DateUtils;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.structure.Condition;
import org.alfacentauro.fantabulous.structure.Field;
import org.alfacentauro.fantabulous.structure.GroupCondition;
import org.alfacentauro.fantabulous.structure.SimpleCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Jatun S.R.L.
 * this class create fantabulous conditon to date represent how integer
 *
 * @author Miky
 * @version $Id: DateFilterer.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class DateFilterer implements CustomCondition {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * condition for get an valid date
     *
     * @param field fantabulous field
     * @param map   parameters to condition
     * @return Condition
     */
    public Condition getConditionForChart(Field field, Map map) {

        SimpleCondition simpleCondition = new SimpleCondition();
        simpleCondition.setField1(field);
        simpleCondition.setOperator(Constants.OPERATOR_GREATER);
        simpleCondition.setValue("19000000"); //min date expressed how integer

        return simpleCondition;
    }

    /**
     * create condition process
     *
     * @param field         fantabulous field
     * @param list          condition values
     * @param map           parameters to help create condition
     * @param titusOperator titus operator object
     * @return condition of fantabulous
     */
    public Condition processTitusFilter(Field field, List list, Map map, TitusOperator titusOperator) {

        log.debug("EXCECUTING DateFilterer::::::::::::::::::::::::::::::::::::::::::::::::::::::::");

        Condition condition = null;

        if (titusOperator.isSimgleOperator()) {

            SimpleCondition simpleCondition = new SimpleCondition();
            simpleCondition.setField1(field);
            simpleCondition.setOperator(titusOperator.getFantaOperator(0).getName());

            String value = null;
            if (!list.isEmpty()) {
                value = (String) list.get(0);
            }

            // titusoperator is an number eg. 1, 2, or 100
            if (titusOperator.isNumberUnit() && value != null) {
                simpleCondition.setValue(value);
            }

            // titusoperator is day eg. today, yesterday, x day
            if (titusOperator.isDayUnit()) {
                if (null != value) {
                    simpleCondition.setValue(value);
                } else {

                    Date valueDate =
                            new Date(incrementDate(new Date(), titusOperator.getIncrement().intValue()).longValue());

                    //date to integer
                    Integer integerDate = DateUtils.dateToInteger(valueDate);

                    simpleCondition.setValue(integerDate.toString());
                }
            }

            if (simpleCondition.getValue() != null) {
                condition = simpleCondition;
            }
        }

        // if titusoperator is interval eg. [a - b] ~> a <= x <= b
        if (titusOperator.isIntervalOperator()) {

            String firstValue = null;
            String secondValue = null;

            if (!list.isEmpty()) {
                firstValue = list.get(0).toString();
                secondValue = list.get(1).toString();
            } else {
                Integer increment = titusOperator.getIncrement();
                if (titusOperator.isDayUnit()) {
                    log.debug("is day......");
                    List orderedValues = orderDate(new Date(),
                            new Date(incrementDate(new Date(), increment.intValue()).longValue()));

                    Date minDate = (Date) orderedValues.get(0);
                    Date maxDate = (Date) orderedValues.get(1);

                    //date to integer
                    firstValue = DateUtils.dateToInteger(minDate).toString();
                    secondValue = DateUtils.dateToInteger(maxDate).toString();

                } else if (titusOperator.isMonthUnit()) {
                    log.debug("is month......");
                    List orderedValues = getMonthX(new Date(), increment.intValue());

                    Date minDate = new Date(((Long) orderedValues.get(0)).longValue());
                    Date maxDate = new Date(((Long) orderedValues.get(1)).longValue());

                    //date to integer
                    firstValue = DateUtils.dateToInteger(minDate).toString();
                    secondValue = DateUtils.dateToInteger(maxDate).toString();
                } else if (titusOperator.isYearUnit()) {
                    log.debug("is year......");
                    List orderedValues = getYearX(new Date(), increment.intValue());

                    Date minDate = new Date(((Long) orderedValues.get(0)).longValue());
                    Date maxDate = new Date(((Long) orderedValues.get(1)).longValue());

                    //date to integer
                    firstValue = DateUtils.dateToInteger(minDate).toString();
                    secondValue = DateUtils.dateToInteger(maxDate).toString();
                }

            }
            if (null != firstValue && null != secondValue) {
                condition = fantaOperatorIsInterval(field, firstValue, secondValue, titusOperator);
            }
        }

        return condition;
    }

    /**
     * create interval condition
     *
     * @param field         fantabulous field
     * @param v1            first value
     * @param v2            second value
     * @param titusOperator
     * @return condition of fantabulous
     */
    private Condition fantaOperatorIsInterval(Field field, String v1, String v2, TitusOperator titusOperator) {
        GroupCondition groupCondition = new GroupCondition();

        SimpleCondition c1 = new SimpleCondition();
        c1.setField1(field);
        //c1.setOperator("GREATER_EQUAL");
        log.debug("INTEVAL::::::::::::" + titusOperator.getFantaOperator(0).getName());
        c1.setOperator(titusOperator.getFantaOperator(0).getName());
        c1.setValue(v1);

        SimpleCondition c2 = new SimpleCondition();
        c2.setField1(field);
        //c2.setOperator("LESS_EQUAL");
        log.debug("INTEVAL::::::::::::" + titusOperator.getFantaOperator(1).getName());
        c2.setOperator(titusOperator.getFantaOperator(1).getName());
        c2.setValue(v2);

        c1.addAndCondition(c2);

        groupCondition.setGroupCondition(c1);

        return groupCondition;
    }

    /**
     * increment days in this date
     *
     * @param date
     * @param inc  increment
     * @return Long object of increment days to this date
     */
    private Long incrementDate(Date date, int inc) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.DATE, inc);

        return new Long(calendar.getTimeInMillis());
    }

    /**
     * calculates the first date and the last date of the X month
     *
     * @param date
     * @param inc  month increment
     * @return list with long date values
     */
    private List getMonthX(Date date, int inc) {
        List r = new ArrayList();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.MONTH, inc);

        int actualYear = calendar.get(Calendar.YEAR);
        int actualMonth = calendar.get(Calendar.MONTH);
        int minDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.YEAR, actualYear);
        minCalendar.set(Calendar.MONTH, actualMonth);
        minCalendar.set(Calendar.DAY_OF_MONTH, minDay);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);
        //minCalendar.set(actualYear, actualMonth, minDay,0,0,0);

        Date min = minCalendar.getTime();

        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.YEAR, actualYear);
        maxCalendar.set(Calendar.MONTH, actualMonth);
        maxCalendar.set(Calendar.DAY_OF_MONTH, maxDay);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        maxCalendar.set(Calendar.MILLISECOND, 999);
        //maxCalendar.set(actualYear, actualMonth, maxDay,23,59,59);

        Date max = maxCalendar.getTime();

        r.add(new Long(min.getTime()));
        r.add(new Long(max.getTime()));

        return r;
    }

    /**
     * calculates the first date and the last date of the X year
     *
     * @param date
     * @param inc  year increment
     * @return list with long date values
     */
    private List getYearX(Date date, int inc) {
        List r = new ArrayList();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.YEAR, inc);

        int actualYear = calendar.get(Calendar.YEAR);

        Calendar minCalendar = Calendar.getInstance();

        minCalendar.set(Calendar.YEAR, actualYear);
        minCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        minCalendar.set(Calendar.DAY_OF_MONTH, 1);
        minCalendar.set(Calendar.HOUR_OF_DAY, 0);
        minCalendar.set(Calendar.MINUTE, 0);
        minCalendar.set(Calendar.SECOND, 0);
        minCalendar.set(Calendar.MILLISECOND, 0);
        //minCalendar.set(actualYear, minMonth, minDay,0,0,0);


        Date min = minCalendar.getTime();

        Calendar maxCalendar = Calendar.getInstance();

        maxCalendar.set(Calendar.YEAR, actualYear);
        maxCalendar.set(Calendar.MONTH, Calendar.DECEMBER);
        maxCalendar.set(Calendar.DAY_OF_MONTH, 31);
        maxCalendar.set(Calendar.HOUR_OF_DAY, 23);
        maxCalendar.set(Calendar.MINUTE, 59);
        maxCalendar.set(Calendar.SECOND, 59);
        maxCalendar.set(Calendar.MILLISECOND, 999);

        //maxCalendar.set(actualYear, maxMonth, maxDay,23,59,59);
        Date max = maxCalendar.getTime();

        r.add(new Long(min.getTime()));
        r.add(new Long(max.getTime()));

        return r;
    }

    /**
     * order two values into a lis [MIN_value, MAX_value]
     *
     * @param a
     * @param b
     * @return list
     */
    private List orderDate(Date a, Date b) {

        List r = new ArrayList();
        if (a.getTime() < b.getTime()) {
            r.add(a);
            r.add(b);
        } else {
            r.add(b);
            r.add(a);
        }
        return r;
    }


}

