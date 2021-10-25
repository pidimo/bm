package com.piramide.elwis.cmd.test;


import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import org.alfacentauro.fantabulous.structure.Condition;
import org.alfacentauro.fantabulous.structure.GroupCondition;
import org.alfacentauro.fantabulous.structure.SimpleCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Period;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Execute product read operation.
 *
 * @author Ernesto
 * @version $Id: TestCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class TestCmd extends EJBCommand {

    private final static Log log = LogFactory.getLog(TestCmd.class);
    boolean test;

    public void executeInStateless(SessionContext ctx) {
    }

    public boolean isTest() {
        return test;
    }

    public TestCmd() {

    }

    List fields;

    private static void iterateConditions(Condition condition, List fields) {
        //List fields = new ArrayList();

        if (condition == null) {
            return;// new ArrayList();
        }

        if (condition.isGroup()) {
            GroupCondition groupCondition = (GroupCondition) condition;
            //fields.addAll(iterateConditions(groupCondition.getGroupCondition()));
            iterateConditions(groupCondition.getGroupCondition(), fields);
        } else {
            SimpleCondition simpleCondition = (SimpleCondition) condition;
            fields.add(simpleCondition.getField1());
            if (simpleCondition.getField2() != null) {
                fields.add(simpleCondition.getField2());
            }
        }
        //fields.add(iterateConditions(condition.next()));
        iterateConditions(condition.next(), fields);
        //return fields;
    }

    public static void main(String argv[]) throws Exception {
        //byte [] keyValue = "1776364000000A8A".getBytes();
        byte[] keyValue = "fm2uf6dm0dy9mq7w".getBytes();

        byte[] text = "alpha".getBytes();
        SecretKeySpec keySpec = new SecretKeySpec(keyValue, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);


    }

    public static void main1(String argv[]) throws Exception {
        /*Object [] ids = processOffsetIt("11934,11935,11930,;0-6;3");
        Object [] s1 = (Object [])ids[0];
        for (int i = 0; i < s1.length; i++) {
            System.out.print(s1[i]);
            System.out.print(" - ");
        }
        System.out.println(ids[1] + " - "+ids[2]+" - "+ ids[3]);

        System.out.println(getPagination("11934,11935,11930,;0-6;3", "11930"));
          */
        //"fm2uf6dm0dy9mq7ws9"
        byte[] keyValue = "1776364000000A8A".getBytes();
        byte[] text = "alpha".getBytes();
        byte[] msgBuf;
        int rest, size;
        size = text.length;
        rest = size & 7;
        if (rest != 0) {
            msgBuf = new byte[(size & (~7)) + 8];
            System.arraycopy(text, 0, msgBuf, 0, size);
            for (int i = size; i < msgBuf.length; i++) {
                msgBuf[i] = ' ';
            }
        } else {
            msgBuf = new byte[size];
            System.arraycopy(text, 0, msgBuf, 0, size);
        }


    }

    private static Map getPagination(String offsetIds, String mailId) {
        Map map = new HashMap(2);
        Object[] values = processOffsetIt(offsetIds);
        if (values == null) {
            return map;
        }
        int left = ((Integer) values[1]).intValue();
        //int right = ((Integer) values[2]).intValue();
        int size = ((Integer) values[3]).intValue();

        String[] mailIds = (String[]) values[0];

        int position;
        for (position = 0; position < mailIds.length; position++) {
            if (mailId.equals(mailIds[position])) {
                break;
            }
        }

        if (position - 1 >= 0) {
            map.put("previus", mailIds[position - 1]);
        }

        if (position + 1 < mailIds.length) {
            map.put("next", mailIds[position + 1]);
        }

        map.put("size", new Integer(size));
        map.put("position", new Integer(left + position + 1));
        return map;
    }

    private static Object[] processOffsetIt(String offsetIds) {
        int pos = offsetIds.indexOf(";") + 1;//mailID: 123,7845,374,485;0-4;8
        int rangePos = offsetIds.indexOf("-", pos);
        int sizePos = offsetIds.indexOf(";", pos) + 1;

        try {
            int left = Integer.parseInt(offsetIds.substring(pos, rangePos));
            int right = Integer.parseInt(offsetIds.substring(rangePos + 1, sizePos - 1));
            int size = Integer.parseInt(offsetIds.substring(sizePos));

            String[] mails = offsetIds.substring(0, pos - 1).split(",");
            return new Object[]{mails, new Integer(left), new Integer(right), new Integer(size)};
        } catch (Exception e) {
            log.error("Unexpected error...", e);
        }
        return null;
    }


    public static DateTime isValidHoliday(int dayOfweek, int weekOfMonth, int month, boolean nextMonday, DateTime s, DateTime e) {
        DateTime now = new DateTime();
        DateTime date = new DateTime(now.getYear(), month, 1, 0, 0, 0, 0);
        DateTime result = null;
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

        if (nextMonday && result.getDayOfWeek() > 5) {
            result = result.plus(Period.days(8 - result.getDayOfWeek()));
            if (result.getMillis() < s.getMillis() || result.getMillis() > e.getMillis()) {
                return null;
            }
        }


        return result;
    }


    public static DateTime EasterSun(int Yr) {
        double AA = Math.floor(Yr / 100);
        double BB = AA - Math.floor(AA / 4);
        double CC = Yr % 19;
        double DD = (15 + 19 * CC + BB - Math.floor((AA + 1 - Math.floor((AA + 8) / 25)) / 3)) % 30;
        double EE = DD - Math.floor((CC + 11 * DD) / 319);
        double S = 22 + EE + (140004 - (Yr + Math.floor(Yr / 4)) % 7 + BB - EE) % 7;
        double EMo = 3 + Math.floor(S / 32);
        double EDy = 1 + (S - 1) % 31;
        DateTime d = new DateTime(Yr, (int) EMo, (int) EDy, 0, 0, 0, 0);
        return d;
    }

    public boolean isStateful() {
        return false;
    }
}
