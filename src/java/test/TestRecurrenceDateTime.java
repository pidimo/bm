package test;

import com.piramide.elwis.dto.schedulermanager.RecurDateTime;
import com.piramide.elwis.dto.schedulermanager.RecurrenceManager;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: TestRecurrenceDateTime.java 9119 2009-04-17 00:21:59Z fernando $
 */

public class TestRecurrenceDateTime {

    public List exceptions = new ArrayList();
    DateTime apptTime;
    DateTime initialTime;
    public String rangeType;
    public Integer rangeValue;
    //public  Integer rangeValue = new Integer(20050730);
    RecurrenceManager recurManager;
    String recurType;
    String recurValue = "";
    String recurValueType = "";


    public int interval;

    public TestRecurrenceDateTime() {

        apptTime = new DateTime("2005-10-19T11:00:00", DateTimeZone.forID("Europe/Berlin"));
        //apptTime = new DateTime();                                            
        interval = 1;
        rangeType = SchedulerConstants.RECUR_RANGE_NO_ENDING;
        rangeValue = new Integer(20051026);
        recurType = SchedulerConstants.RECURRENCE_YEARLY;
        recurValue = "2";

        recurValueType = SchedulerConstants.RECUR_YEARLY_THIS_DAY;
        //exceptions.add(new Integer(20051023));
        //exceptions.add(new Integer(20050603));

        recurManager = new RecurrenceManager(exceptions, apptTime, interval, rangeType, rangeValue, recurType,
                recurValue, recurValueType, DateTimeZone.getDefault());

    }

    public static void main(String args[]) throws Exception {
//        System.out.println("TESTING RECURRENCES");
        //TestRecurrenceDateTime test = new TestRecurrenceDateTime();

        /*** Test the getNextDateTime method **/
        /*while ((rdt = test.getRecurManager().getNextDateTime()) != null) {
            System.out.println("Next DateTime = " + rdt.getDateTime());
        }
        */
        //RecurrenceManager rm = test.getRecurManager();

        /** daily */
        //System.out.println("Next Daily yearly = " + rm.getNextDailyDateTimeFor(new DateTime("2005-01-26")));

        /** weekly */
        //System.out.println("Next weekly DateTime = " + rm.getNextWeeklyDateTimeFor(new DateTime("2005-12-01")));

        /** Monthly **/
        //System.out.println("Next weekly DateTime = " + rm.getNextMonthlyDateTimeFor(new DateTime("2005-04-04")));

        /** Yearly **/
        //System.out.println("Next weekly DateTime = " + rm.getNextYearlyDateTimeFor(new DateTime("2007-10-30")));
        //System.out.println("Next weekly DateTime = " + rm.getNextYearlyDateTime(new DateTime("2007-10-31")));

        //testing range
        /* for (Iterator iterator =
                rm.getRecurrencesBetween(new DateTime("2005-01-01", DateTimeZone.forID("Europe/Berlin")),
                        new DateTime("2006-12-31", DateTimeZone.forID("Europe/Berlin"))).iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            System.out.println(o);

        }*/
        /*System.out.println("Recurrence in the range = " +
                rm.getRecurrencesBetween(new DateTime("2005-01-01", DateTimeZone.forID("Europe/Berlin")),
                        new DateTime("2005-01-31", DateTimeZone.forID("Europe/Berlin"))));*/

        //printing datetimes
        /*DateTime dt = new DateTime(1129240800000L, DateTimeZone.forID("Europe/Berlin"));*/

        /*Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
    System.out.println("time zone = " + c.getTimeZone().getID());
    System.out.println("millis = " + c.getTimeInMillis());
    Date d =c.getTime();
    System.out.println("date millis =" + d.getTime());
    MailDateFormat mdf = new MailDateFormat();
    System.out.println("mdf timezone = " + mdf.getTimeZone());
    System.out.println("Date format  " + mdf.format(c.getTime()));
    System.out.println("Date parse  " + mdf.parse("Tue, 28 Mar 2006 18:20:00 +0200 (CEST)"));


    System.out.println("The date is = " + d);


    DateTime dt = new DateTime(1143552360000L, DateTimeZone.forID("+02:00"));
    System.out.println("DateTime = " + dt);*/

        /*
        String end = "ENDPARAMS";
        String str = "dto(countryId)%ENDPARAMS";
        String pvSep = "%";
        String vSep = "SEP";

        String [] paramsAndValues = str.split(pvSep);

        String[] params = paramsAndValues[0].split("&");
        String[] values = paramsAndValues[1].split(vSep);
        for (int i = 0; i < params.length; i++) {
            System.out.println("param = " + i + " = " + params[i]);
        }
        for (int i = 0; i < values.length; i++) {
            System.out.println("value " + i + " = " + values[i]);
        }

        for (int i = 0; i < params.length; i++) {
            if (!values[i].equals(end))
                System.out.println("param = " + params[i] + " value= " + ((values[i].endsWith(end)) ? values[i].substring(0, values[i].indexOf(end)) : values[i]));
            else
                System.out.println("param = " + params[i] + " value= " + "");
        }

        */
/*

        String str = "Casena ñ a encriptar una cade/na a encriptar";
        byte[] iv = {0x0a, 0x01, 0x02, 0x03, 0x04, 0x0b, 0x0c, 0x0d};
        KeyGenerator kg = KeyGenerator.getInstance("DES");
        kg.init(56);
        SecretKeySpec key = new SecretKeySpec("ABCDEFGH".getBytes(), "DES");

        //SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        //SecretKey key = keygen.generateKey();
        IvParameterSpec ips = new IvParameterSpec(iv);


        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        String encrypted = new String(cipher.doFinal(str.getBytes()));

        System.out.println("Encrypted = " + encrypted);

        System.out.println("cipher = " + EncryptUtil.i.cipher(str, String.valueOf(System.currentTimeMillis())));

*/

        /* KeyGenerator keygen = KeyGenerator.getInstance("DESede");
  SecretKey desKey = keygen.generateKey();
  Cipher desCipher = Cipher.getInstance("DESede");
  desCipher.init(Cipher.ENCRYPT_MODE, desKey);

// Our cleartext
byte[] cleartext = "This is just an exesta es otr mensaje ñ y algo mas laeg/()?=sjsjhjsd.dtr Ñ ample".getBytes();

// Encrypt the cleartext
byte[] ciphertext = desCipher.doFinal(cleartext);
  String encry = new BASE64Encoder().encode(ciphertext);
  System.out.println("Encrypted = " + encry);

// Initialize the same cipher for decryption
  desCipher.init(Cipher.DECRYPT_MODE, desKey);
  byte[] decoded = new BASE64Decoder().decodeBuffer(encry);
// Decrypt the ciphertext
  byte[] cleartext1 = desCipher.doFinal(decoded);
  System.out.println("decrypted = " + new String(cleartext1) );*/

        /*String ori = "Esta es una cadena con caracteres raros";
        System.out.println("Original text = " + ori);
        String encrypted = UrlEncryptCipher.i.encrypt(ori);
        System.out.println("Encrypted = " + encrypted);   */
        /*UrlBase64Encoder enc = new UrlBase64Encoder();*/
        //System.out.println("decoded = " + new String(enc.decode("kxPJsHawLthRYUSwxsdoHFY_DOtDl2as2nu54-S64qlXhG97jMqy4eXPh2_f8mD5nq50nyREYYG_9Iy71kHnrA**")));

        /*System.out.println("decrypted = " + UrlEncryptCipher.i.decrypt("4O7EWkbn3zcTZ1DIfQNXZNXqkksXvZTWdii7SYoBTIC-nHnUawgqMH4KhsXhmlhEuodOn0HiCnAkQZ5gOOytE9KVdduBBgbos9ngqdpYuGsRbVhOeOI6-ojRq6YFmBQED_2WqOc0F3lTehSfl79YX2kJ4PlJESfR03R7hOYpiIO9KD6kqaUaKN38e4zE1Vo1svpAnGGqGN6nTCyS5L0B1A**"));
        System.out.println("decrypted = " + UrlEncryptCipher.i.decrypt("kxPJsHawLtgibRRYUAOTycQy-C8waoSoB9oqyUWt1ldS7AGOH4ZJlpTUrVYYBbDp_XpRxlyX1hsIxuprVkd-Hxu7rjwEI5-6ijLQlvTmGaXKCENjdmxbP20-KWKCXG0VCqFDYjRuBqIFyUjgsCDW_Q7WrqjI7lyCnIOCyYjS92zEtJQLucnapw**"));
        System.out.println("decrypted = " + UrlEncryptCipher.i.decrypt("Llr64RjvNknxd2uV-pvSj1tnJo8G_QwQGDQX-mh_bSc55F-rwP7jWFFNuVpDT9BAd5gkHw1NYvwbZ6MBd7zsLNHe4t-L7TomhxG6jGdLwK6a-9WRkl9AgW9_Wgn8MwH6QEe2Q0BVbUJ7VtDoJKTK2g7WrqjI7lyCnIOCyYjS92zEtJQLucnapw**"));*/

        /*DateTime startRange = new DateTime("2006-12-10T10:00:00.000-04:00");
DateTime endRange = new DateTime("2006-12-10T23:59:00.000-04:00");

DateTime apptStart = new DateTime("2006-12-19T22:30:00.000-04:00");
DateTime apptEnd = new DateTime("2006-12-20T23:30:00.000-04:00");

DateTime recur = new DateTime("2006-12-15T10:00:00.000-04:00");

Interval range = new Interval(startRange, endRange);
Period p = new Period(startRange, endRange);
Duration d = new Duration(startRange, endRange);
DateTime newDt= recur.plus(p);
DateTime newDt1= recur.plus(d);
System.out.println("The new end date is: " + newDt);
System.out.println("The new end date is with duration: " + newDt1);*/
        //System.out.println("The time is = " + new DateTime(1167072300000L));

        /*   DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
                String a = fmt.print(new DateTime("2006-12-15T23:59:59.999-04:00"));
                System.out.println("The date is = " + a);
        */

        /*
        Interval appt = new Interval(apptStart, apptEnd);
        System.out.println("Range = " + range);
        System.out.println("Appt = " + appt);
        if(appt.isBefore(range) || appt.isAfter(range)) {
            System.out.println("Range does NOT contains appt.");

        }else {
            System.out.println("range contains appt");

        }
        */

        /*String n = "+49-56()  9-8655/6(656)6";
        String res = n.replaceAll("[\\(\\)\\.\\-\\ \\/\\\\]", "");
        System.out.println("Replaced is: " + res);


        System.out.println("Boolean.valueOf(null)? : " + Boolean.valueOf(null));*/


        System.out.println("decrypted = " + UrlEncryptCipher.i.decrypt("e32Fz5Hlvfj8JgrOSr_AWzv5mRGHTUL-NJ_JUFZJ06rg41P3GzXj0Q**"));
        System.out.println("decrypted = " + UrlEncryptCipher.i.encrypt("Dashboard/Container/DrawContainer.do"));
    }


    public RecurrenceManager getRecurManager() {
        return recurManager;
    }

    public void setRecurManager(RecurrenceManager recurManager) {
        this.recurManager = recurManager;
    }

    public void testDailyRecurrence(DateTime initialDateTime) {

        System.out.println("Next Daily Date = " + recurManager.getNextDailyDateTime(initialDateTime));
    }

    public void testWeeklyRecurrence(DateTime initialDateTime, int dayOfWeekPos) {
        RecurDateTime dt = recurManager.getNextWeeklyDateTime(initialDateTime, initialDateTime, dayOfWeekPos);
        if (dt != null)
            System.out.println("Next Weekly Date = " + dt.getDateTime() + "  new day position = " + dt.getData());
        else
            System.out.println("There is no next date for the weekly recurrence");
    }

    public void testMonthlyRecurrence(DateTime initialTime) {
        System.out.println("Next Monthly date = " + recurManager.getNextMonthlyDateTime(initialTime, initialTime));
    }

    public void testYearlyRecurrence(DateTime initialTime) {
        System.out.println("Next Monthly date = " + recurManager.getNextYearlyDateTime(initialTime));
    }


}
