package com.piramide.elwis.web.schedulermanager.taglib;

import com.piramide.elwis.dto.schedulermanager.AppointmentView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Apr 14, 2005
 * Time: 5:32:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppointmentDay {
    protected static Log log = LogFactory.getLog("AppointmentDay");

    private List appointments;
    private List outCalendarViewAppointments;
    private List allDayAppointments;

    private int[][] dayTable;
    private int[] appointmentRows;
    private int[] appointmentColumns;

    private int intervalsAmount;
    private int virtualMinuteIntervalTime;
    private final static int minuteIntervalTime = 15;
    private int workStartHour;
    private int workEndHour;
    private int processedColumns;

    private int I;
    private int J;
    private Integer today;
    private int minuteIntervals;
    private int segmentByInterval;

    private List changeStartTime;
    private List changeEndTime;

    public AppointmentDay(int virtualMinuteIntervalTime, Integer today) {
        this.virtualMinuteIntervalTime = virtualMinuteIntervalTime;
        minuteIntervals = virtualMinuteIntervalTime / minuteIntervalTime;
        log.debug("Interval Amount:" + intervalsAmount);
        log.debug("Virtual intetrval:" + minuteIntervals);

        this.today = today;
        appointments = new ArrayList();
        allDayAppointments = new ArrayList();
        outCalendarViewAppointments = new ArrayList();
        segmentByInterval = virtualMinuteIntervalTime / minuteIntervalTime;
        changeStartTime = new ArrayList();
        changeEndTime = new ArrayList();
    }

    public void setIntervalView(int workStartHour, int workEndHour) {
        this.workStartHour = workStartHour;
        this.workEndHour = workEndHour;
    }

    public void calculateAppointmentPositionProccess() {
        I = -1;
        J = -1;
        updateAppointmentTimes();
        processedColumns = 1;
        intervalsAmount = (workEndHour - workStartHour) * 60 / minuteIntervalTime;
        dayTable = new int[intervalsAmount][processedColumns];
        calculateBasicTable();
        calculateFinalTable();
    }

    public void updateAppointmentTimes() {
        for (Iterator iterator = changeStartTime.iterator(); iterator.hasNext();) {
            AppointmentView appointment = ((AppointmentView) iterator.next());
            workStartHour = 0;
            appointment.getStartTime().changeTime(workStartHour, 0);
        }

        for (Iterator iterator = changeEndTime.iterator(); iterator.hasNext();) {
            AppointmentView appointment = ((AppointmentView) iterator.next());
            //before change the end time, save this
            appointment.setRealEndTime(appointment.getEndTime().getHour(), appointment.getEndTime().getMinute());
            workEndHour = 24;
            appointment.getEndTime().changeTime(workEndHour, 0);
        }
    }

    public void addAppointment(AppointmentView appointment) {
        // Only add if the appoint stay in work interval time
        //log.debug("APP:" + appointment.getTitle() + "TODAY:" + today + " - StartDate:" + appointment.getStartDate().toIntegerDate() + " - AllDay:" + appointment.isAllDay() + " - SameDay:" + appointment.isSameDay() + " - InRange:" + appointment.inRangeDay(today) + " - IsLimit:" + appointment.isLimitRangeDay(today));

        if ((!appointment.isAllDay() && appointment.isSameDay())) {//El startDate del appointmet es el mismo al dia en proceso
            appointments.add(appointment);                               //|| (!appointment.isSameDay() && appointment.isLimitRangeDay(today))
            //log.debug("First if");
        } else if ((appointment.isAllDay() || appointment.inRangeDay(today)) ||
                (!appointment.isLimitRangeDay(today) && !appointment.isSameDay())) {
            appointment.setAllDay(true);
            allDayAppointments.add(appointment);
            //log.debug("Second if");
        } else { /*(!appointment.isSameDay() && !appointment.inRangeDay(today))*/
            //log.debug("Third if");
            if (today.equals(appointment.getStartDate().toIntegerDate())) {
                changeEndTime.add(appointment);
                //log.debug("3-a");
            } else if (today.equals(appointment.getEndDate().toIntegerDate())) {
                changeStartTime.add(appointment);
                //log.debug("3-b");
            }

            appointments.add(appointment);
        }
        /*log.debug("appointments:" + appointments);
        log.debug("ChangeStat:" + changeStartTime);
        log.debug("ChangeEnd:" + changeEndTime);*/
    }

    public boolean hasNextRows() {
        return I + 1 < intervalsAmount;
    }

    public boolean hasNextColumn() {
        return J + 1 < processedColumns;
    }

    public int[] nextRow() {
        I++;
        J = -1;
        return getTimeLabel();
    }
/*
    public boolean hasCollapsedRows() {
        return true;
    }


    public class CollapseCells {
        private int i;
        private int j;

        public CollapseCells(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public boolean isCollapsedRow() {
            int collapsedRowsAmount = 0;
            // The current row isn't time with label
            if ((i * minuteIntervalTime) % virtualMinuteIntervalTime != 0) return false;

            int segmentAmount = 0;
            for (; i < intervalsAmount; i++) {
                boolean emptyRow = true;
                for (; j < processedColumns; j++) {
                    if (dayTable[i][j] != 0) return false;
                }
                if ((i * minuteIntervalTime) % virtualMinuteIntervalTime == 0)
                    segmentAmount = segmentByInterval;
                else
                    segmentAmount--;
                if (segmentAmount == 0) {
                    collapsedRowsAmount++;
                }
            }
            return true;
        }

        public void moveToNewPosition() {
            I = i;
            J = j;
        }
    }*/


    private int[] getTimeLabel() {
        int[] time = new int[2];
        int x = I * minuteIntervalTime;
        if (x % virtualMinuteIntervalTime != 0) {
            return null;
        }
        time[0] = x / 60 + workStartHour;
        time[1] = x % 60;
        return time;
    }

    public AppointmentDayCell nextColumn() {
        J++;
        return new AppointmentDayCell();
    }

    public int getAppointmentAmount() {
        return allDayAppointments.size() + appointments.size();
    }

    public List getAllDayAppointments() {
        return allDayAppointments;
    }

    public List getOutCalendarViewAppointments() {
        return outCalendarViewAppointments;
    }

    public class AppointmentDayCell {
        private boolean fail;
        private boolean isEmpty;
        private int position;
        private int cols;

        public AppointmentDayCell() {
            isEmpty = false;
            fail = false;
            while (true) {
                if (J >= processedColumns) {
                    fail = true;
                    break;
                } else if (dayTable[I][J] < 0) {// Todas las celdas del appointment son marcados con el valor en negativo, para saber que fueron procesadas
                    position = (dayTable[I][J] * -1) - 1;
                    for (int x = 0; x < appointmentRows[position] && x + I < intervalsAmount; x++) {
                        for (int y = 0; y < appointmentColumns[position] && J + y < processedColumns; y++) {
                            dayTable[I + x][J + y] = dayTable[I + x][J + y] * -1;
                        }
                    }
                    J += appointmentColumns[position] - 1;
                    break;
                } else if (dayTable[I][J] == 0) {
                    isEmpty = true;
                    for (cols = 0; J + cols < processedColumns && dayTable[I][J + cols] == 0; cols++) {
                        ;
                    }
                    J += cols;
                    break;
                } else {
                    J++;
                }
            }
        }

        public boolean isFail() {
            return fail;
        }

        public boolean isEmpty() {
            return isEmpty;
        }

        public AppointmentView get() {
            return (AppointmentView) appointments.get(position);
        }

        public int getAppointmentRows() {
            return appointmentRows[position];
        }

        public int getAppointmentCols() {
            return !isEmpty ? appointmentColumns[position] : cols;
        }
    }

    private void calculateBasicTable() {
        int positionInAppointmentList = 0;

        appointmentRows = new int[appointments.size()];
        appointmentColumns = new int[appointments.size()];
        //appointmentGenerate = new boolean[appointments.size()];

        for (Iterator iterator = appointments.iterator(); iterator.hasNext();) {
            positionInAppointmentList++;

            AppointmentView appointment = (AppointmentView) iterator.next();
            int currentColumn = 0;
            int[] posAndRows = getPositionAndInterval(appointment);
            int position = posAndRows[0];
            int rows = posAndRows[1];
            if (rows == 0) {
                continue; // Por si acaso, en teoria no deberia parasar.
            }
            appointmentRows[positionInAppointmentList - 1] = rows;
            appointmentColumns[positionInAppointmentList - 1] = 1;
            if (position >= 0 && currentColumn >= 0) {
                while (currentColumn < dayTable[0].length && position < dayTable.length && dayTable[position][currentColumn] > 0) { //No se registro ningun appointment
                    ensureCapacity(++currentColumn);
                }

                for (int i = 0; i < rows && position + i < dayTable.length && currentColumn < dayTable[0].length; i++) {
                    dayTable[position + i][currentColumn] = positionInAppointmentList;
                }
            }
        }
    }

    private void ensureCapacity(int column) {
        if (column + 1 > processedColumns) {
            processedColumns++;
            for (int i = 0; i < dayTable.length; i++) {
                int[] oldData = dayTable[i];
                dayTable[i] = new int[processedColumns];
                System.arraycopy(oldData, 0, dayTable[i], 0, oldData.length);
            }
        }
    }

    private void calculateFinalTable() {
        int[] emptyNextforAppointment = new int[appointmentRows.length];
        for (int i = 0; i < dayTable.length; i++) {
            for (int j = 0; j < processedColumns; j++) {
                if (dayTable[i][j] > 0) {
                    int appointmentPosition = dayTable[i][j] - 1;
                    int cols = isEmptyColumn(i, j, appointmentPosition);
                    if (cols > 0) {
                        /*emptyNextforAppointment[appointmentPosition] += 2;
                        i = appointmentExpand(i, j, emptyNextforAppointment, appointmentPosition);*/
                        appointmentExpand(i, j, appointmentPosition, cols);
                    } else {
                        emptyNextforAppointment[appointmentPosition] += 1;
                    }
                }
            }
        }
    }

    private void appointmentExpand(int i, int j, int position, int cols) {
        int appointmenRows = appointmentRows[position];
        if (cols > processedColumns) {
            cols = processedColumns;
        }
        appointmentColumns[position] = cols;
        int val = (position + 1) * -1;
        for (int l = 0; l < cols && j + l < processedColumns; l++) {
            for (int k = 0; k < appointmenRows && k + i < intervalsAmount; k++) {
                dayTable[k + i][j + l] = val;
            }
        }
    }

    private int isEmptyColumn(int i, int j, int position) {
        int possibleCols = appointmentColumns[position];
        for (int y = 1; y + j < processedColumns; y++) {
            boolean empty = true;
            for (int x = 0; x < appointmentRows[position] && x + i < intervalsAmount; x++) {
                if (dayTable[i + x][j + y] != 0) {
                    empty = false;
                    break;
                }
            }
            if (empty) {
                possibleCols++;
            } else {
                break;
            }
        }
        return possibleCols;
    }


    public int[][] getDayTable() {
        return dayTable;
    }


    private int[] getPositionAndInterval(AppointmentView appointment) {
        int[] result = new int[2];
        AppointmentView.AppointmentTime startTime;
        AppointmentView.AppointmentTime endTime;
        if (today.equals(appointment.getStartDate().toIntegerDate())) {//El startDate del appointmet es el mismo al dia en proceso
            startTime = appointment.getStartTime();
            if (appointment.getStartTime().getHour() < workStartHour) {
                startTime = appointment.newAppointmentTime(workStartHour, 0);
            }
            endTime = appointment.getEndTime();
        } else {
            int endhour = 0;
            int endminute = 0;

            int endComparation = appointment.getEndDate().compareTo(today);
            if (endComparation > 0) { // appStartDate is less to TODAY and appEndDate is great to TODAY
                appointment.setAllDay(true);
                endhour = workEndHour;
                endminute = 0;
            } else { // appStartDate is less to TODAY and appEndDate is equal to TODAY
                endhour = appointment.getEndTime().getHour(); //- workStartHour;
                endminute = appointment.getEndTime().getMinute();
            }
            startTime = appointment.newAppointmentTime(workStartHour, 0);
            endTime = appointment.newAppointmentTime(endhour, endminute);
        }
        result[0] = getPosition(startTime);
        result[1] = getRows(startTime, endTime);
        return result;
    }

    private int getRows(AppointmentView.AppointmentTime startTime, AppointmentView.AppointmentTime endTime) {
        return getIntervalDuration(endTime.getHour() - startTime.getHour(), endTime.getMinute() - startTime.getMinute());
    }


    private int getPosition(AppointmentView.AppointmentTime time) {
        return getIntervalDuration(time.getHour() - workStartHour, time.getMinute());
    }


    private int getIntervalDuration(int offsetHour, int offsetMinute) {
        return getIntervalDuration(offsetHour * 60 + offsetMinute);
    }

    private int getIntervalDuration(int minutes) {
        return minutes / minuteIntervalTime;
    }

    public List getAppointments() {
        return appointments;
    }

    /**
     * Call after execute calculateAppointmentPositionProccess()
     *
     * @return u
     */
    public int getProcessedColumns() {
        return processedColumns;
    }

    public void setWorkStartHour(int workStartHour) {
        this.workStartHour = workStartHour;
    }

    public void setWorkEndHour(int workEndHour) {
        this.workEndHour = workEndHour;
    }

    public int getMinuteIntervals() {
        return minuteIntervals;
    }

    public int getWorkEndHour() {
        return workEndHour;
    }

}
