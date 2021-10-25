package com.piramide.elwis.web.reports.filterers;

import com.jatun.titus.listgenerator.engine.FantaOperator;
import com.jatun.titus.listgenerator.engine.TitusOperator;
import com.jatun.titus.listgenerator.engine.fantabulous.CustomCondition;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.structure.Condition;
import org.alfacentauro.fantabulous.structure.Field;
import org.alfacentauro.fantabulous.structure.GroupCondition;
import org.alfacentauro.fantabulous.structure.SimpleCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;

/**
 * @author: ivan
 * Date: Mar 13, 2006
 * Time: 4:40:37 PM
 */
public class DateTimeFilterer implements CustomCondition {
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

    public Condition processTitusFilter(Field field, List list, Map map, TitusOperator titusOperator) {
        log.debug("processTitusFilter(org.alfacentauro.fantabulous.structure.Field, " +
                "java.util.List, '" + map + "', com.jatun.titus.listgenerator.engine.TitusOperator )");


        Condition condition = null;
        List operators = titusOperator.getFantaOperators();

        DateTimeZone timeZone = DateTimeZone.getDefault();

        if (null != map.get("timeZone")) {
            timeZone = (DateTimeZone) map.get("timeZone");
        }

        if (titusOperator.isSimgleOperator()) {
            Long longDate = null;

            FantaOperator fantaOperator = (FantaOperator) operators.get(0);

            if (!list.isEmpty()) {
                try {
                    longDate = new Long(list.get(0).toString());
                } catch (NumberFormatException nfe) {

                }
            } else {
                if (TitusOperator.UNIT_DAY.equals(titusOperator.getUnit())) {
                    int increment = titusOperator.getIncrement().intValue();
                    longDate = getDayX(increment, timeZone);
                }
            }

            if (null != longDate) {
                int fOpId = changeFantaOperatorNameToConstant(fantaOperator);
                switch (fOpId) {
                    case 1: {
                        condition = fantaOperatorIsEQUAL(field, longDate, timeZone);
                        break;
                    }
                    case 2: {
                        condition = fantaOperatorIsLESS(field, longDate, timeZone);
                        break;
                    }
                    case 3: {
                        condition = fantaOperatorIsLESS_EQUAL(field, longDate, timeZone);
                        break;
                    }
                    case 4: {
                        condition = fantaOperatorIsGREATER(field, longDate, timeZone);
                        break;
                    }
                    case 5: {
                        condition = fantaOperatorIsGREATER_EQUAL(field, longDate, timeZone);
                        break;
                    }
                    case 6: {
                        condition = fantaOperatorIsDISTINCT(field, longDate, timeZone);
                        break;
                    }
                }
            }
        }
        if (titusOperator.isIntervalOperator()) {
            Long firstValue = null;
            Long secondValue = null;

            if (!list.isEmpty()) {
                try {
                    firstValue = new Long(list.get(0).toString());
                    secondValue = new Long(list.get(1).toString());
                } catch (NumberFormatException nfe) {

                }
            } else {
                int increment = titusOperator.getIncrement().intValue();
                if (titusOperator.isDayUnit()) {
                    Map values = orderValues(getDayX(0, timeZone), getDayX(increment, timeZone));
                    firstValue = (Long) values.get("first");
                    secondValue = (Long) values.get("second");
                }


                if (titusOperator.isMonthUnit()) {
                    Calendar month = getMonthX(increment, timeZone);

                    Calendar firstDay = getElementXOfDate(month.getTimeInMillis(),
                            Calendar.DATE,
                            month.getActualMinimum(Calendar.DATE),
                            timeZone);
                    Calendar secondDay = getElementXOfDate(month.getTimeInMillis(),
                            Calendar.DATE,
                            month.getActualMaximum(Calendar.DATE),
                            timeZone);

                    firstValue = firstDay.getTimeInMillis();
                    secondValue = secondDay.getTimeInMillis();
                }

                if (titusOperator.isYearUnit()) {
                    Calendar year = getYearX(increment, timeZone);

                    Calendar enero = getElementXOfDate(year.getTimeInMillis(),
                            Calendar.MONTH,
                            Calendar.JANUARY,
                            timeZone);
                    Calendar diciembre = getElementXOfDate(year.getTimeInMillis(),
                            Calendar.MONTH,
                            Calendar.DECEMBER,
                            timeZone);


                    Calendar firstDay = getElementXOfDate(enero.getTimeInMillis(),
                            Calendar.DATE,
                            enero.getActualMinimum(Calendar.DATE),
                            timeZone);
                    Calendar secondDay = getElementXOfDate(diciembre.getTimeInMillis(),
                            Calendar.DATE,
                            diciembre.getActualMaximum(Calendar.DATE),
                            timeZone);

                    firstValue = firstDay.getTimeInMillis();
                    secondValue = secondDay.getTimeInMillis();
                }

            }
            if (null != firstValue && null != secondValue) {
                condition = fantaOperatorIsInterval(field, firstValue, secondValue, timeZone);
            }
        }

        return condition;
    }

    private Condition fantaOperatorIsEQUAL(Field field, Long longDate, DateTimeZone timeZone) {

        GroupCondition groupCondition = new GroupCondition();
        DateTime value = new DateTime(longDate.longValue(), timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 0, 0, 0, 0, timeZone);
        DateTime valueB = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 23, 59, 59, 999, timeZone);
        SimpleCondition c1 = new SimpleCondition();
        c1.setField1(field);
        c1.setOperator("GREATER_EQUAL");
        c1.setValue((new Long(valueA.getMillis())).toString());

        SimpleCondition c2 = new SimpleCondition();
        c2.setField1(field);
        c2.setOperator("LESS_EQUAL");
        c2.setValue((new Long(valueB.getMillis())).toString());

        c2.addAndCondition(c1);

        groupCondition.setGroupCondition(c2);

        return groupCondition;
    }

    private Condition fantaOperatorIsLESS(Field field, Long longDate, DateTimeZone timeZone) {
        SimpleCondition condition = new SimpleCondition();
        DateTime value = new DateTime(longDate, timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 0, 0, 0, 0, timeZone);

        condition.setField1(field);
        condition.setOperator("LESS");
        condition.setValue((new Long(valueA.getMillis())).toString());

        return condition;
    }

    private Condition fantaOperatorIsLESS_EQUAL(Field field, Long longDate, DateTimeZone timeZone) {
        SimpleCondition condition = new SimpleCondition();
        DateTime value = new DateTime(longDate, timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 23, 59, 59, 999, timeZone);

        condition.setField1(field);
        condition.setOperator("LESS_EQUAL");
        condition.setValue((new Long(valueA.getMillis())).toString());

        return condition;
    }

    private Condition fantaOperatorIsGREATER(Field field, Long longDate, DateTimeZone timeZone) {
        SimpleCondition condition = new SimpleCondition();
        DateTime value = new DateTime(longDate, timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 23, 59, 59, 999, timeZone);

        condition.setField1(field);
        condition.setOperator("GREATER");
        condition.setValue((new Long(valueA.getMillis())).toString());

        return condition;
    }

    private Condition fantaOperatorIsGREATER_EQUAL(Field field, Long longDate, DateTimeZone timeZone) {
        SimpleCondition condition = new SimpleCondition();
        DateTime value = new DateTime(longDate, timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 0, 0, 0, 0, timeZone);

        condition.setField1(field);
        condition.setOperator("GREATER_EQUAL");
        condition.setValue((new Long(valueA.getMillis())).toString());

        return condition;
    }

    private Condition fantaOperatorIsDISTINCT(Field field, Long longDate, DateTimeZone timeZone) {

        GroupCondition groupCondition = new GroupCondition();
        DateTime value = new DateTime(longDate.longValue(), timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 0, 0, 0, 0, timeZone);
        DateTime valueB = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 23, 59, 59, 999, timeZone);
        SimpleCondition c1 = new SimpleCondition();
        c1.setField1(field);
        c1.setOperator("LESS");
        c1.setValue((new Long(valueA.getMillis())).toString());

        SimpleCondition c2 = new SimpleCondition();
        c2.setField1(field);
        c2.setOperator("GREATER");
        c2.setValue((new Long(valueB.getMillis())).toString());

        c2.addOrCondition(c1);

        groupCondition.setGroupCondition(c2);

        return groupCondition;
    }

    private Condition fantaOperatorIsInterval(Field field, Long v1, Long v2, DateTimeZone timeZone) {
        log.debug("fantaOperatorIsInterval(org.alfacentauro.fantabulous.structure.Field, '" +
                v1 + "', '" + v2 + "', org.joda.time.DateTimeZone)");
        GroupCondition groupCondition = new GroupCondition();
        DateTime value = new DateTime(v1, timeZone);
        DateTime valueA = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 0, 0, 0, 0, timeZone);

        value = new DateTime(v2, timeZone);
        DateTime valueB = new DateTime(value.getYear(), value.getMonthOfYear(), value.getDayOfMonth(), 23, 59, 59, 999, timeZone);

        SimpleCondition c1 = new SimpleCondition();
        c1.setField1(field);
        c1.setOperator("GREATER_EQUAL");
        c1.setValue((new Long(valueA.getMillis())).toString());

        SimpleCondition c2 = new SimpleCondition();
        c2.setField1(field);
        c2.setOperator("LESS_EQUAL");
        c2.setValue((new Long(valueB.getMillis())).toString());

        c2.addAndCondition(c1);

        groupCondition.setGroupCondition(c2);

        return groupCondition;
    }

    private int changeFantaOperatorNameToConstant(FantaOperator fantaOperator) {
        int id = -1;
        if ("EQUAL".equals(fantaOperator.getName())) {
            id = 1;
        }
        if ("LESS".equals(fantaOperator.getName())) {
            id = 2;
        }
        if ("LESS_EQUAL".equals(fantaOperator.getName())) {
            id = 3;
        }
        if ("GREATER".equals(fantaOperator.getName())) {
            id = 4;
        }
        if ("GREATER_EQUAL".equals(fantaOperator.getName())) {
            id = 5;
        }
        if ("DISTINCT".equals(fantaOperator.getName())) {
            id = 6;
        }
        return id;
    }

    private Long getDayX(int increment, DateTimeZone timeZone) {
        timeZone.toTimeZone();
        Date date = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.setTimeZone(timeZone.toTimeZone());
        calendar.add(Calendar.DATE, increment);

        return new Long(calendar.getTimeInMillis());
    }

    private Map orderValues(Long v1, Long v2) {
        Long aux = null;
        if (v2.longValue() < v1.longValue()) {
            aux = v1;
            v1 = v2;
            v2 = aux;
        }
        Map result = new HashMap();
        result.put("first", v1);
        result.put("second", v2);
        return result;
    }

    private Calendar getElementXOfDate(Long dateAsLong,
                                       int calendarField,
                                       int calendarValue,
                                       DateTimeZone timeZone) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dateAsLong);
        c.setTimeZone(timeZone.toTimeZone());
        c.set(calendarField, calendarValue);

        return c;
    }

    private Calendar getMonthX(int increment, DateTimeZone timeZone) {

        Date actualMonth = new Date();

        Calendar newMonth = Calendar.getInstance();
        newMonth.setTimeInMillis(actualMonth.getTime());
        newMonth.setTimeZone(timeZone.toTimeZone());
        newMonth.add(Calendar.MONTH, increment);

        return newMonth;
    }

    private Calendar getYearX(int increment, DateTimeZone timeZone) {
        Date actualYear = new Date();

        Calendar newYear = Calendar.getInstance();
        newYear.setTimeInMillis(actualYear.getTime());
        newYear.setTimeZone(timeZone.toTimeZone());
        newYear.add(Calendar.YEAR, increment);

        return newYear;
    }
}

