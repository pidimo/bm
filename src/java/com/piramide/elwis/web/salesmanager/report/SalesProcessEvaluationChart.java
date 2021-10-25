package com.piramide.elwis.web.salesmanager.report;

import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.UnitType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.awt.*;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Class with process the report graph for sales process evaluation
 *
 * @author Fernando Montaño
 * @version $Id: SalesProcessEvaluationChart.java 9124 2009-04-17 00:35:24Z fernando $
 */

public class SalesProcessEvaluationChart {

    public static final String PERIOD_DAY = "1";
    public static final String PERIOD_WEEK = "2";
    public static final String PERIOD_MONTH = "3";
    public static final String PERIOD_YEAR = "4";

    private static Log log = LogFactory.getLog(SalesProcessEvaluationChart.class);

    /**
     * Generate a XY graph
     *
     * @param list    a list of actionDTO
     * @param request the request
     * @param pw      the printwriter
     * @param period  the period of time
     * @return the image file name
     */
    public static String generateXYChart(List list, HttpServletRequest request, PrintWriter pw, String period) {
        HttpSession session = request.getSession();
        Locale locale = (Locale) Config.get(session, Config.FMT_LOCALE);
        String filename = null;

        try {

            //create for only one sales process date time serie.
            TimeSeries timeSerie = new TimeSeries(JSPHelper.getMessage(request, "SalesProcessAction.plural"), Day.class);
            Iterator iter = list.listIterator();
            while (iter.hasNext()) {
                ContactDTO contactDTO = (ContactDTO) iter.next();
                timeSerie.addOrUpdate(new Day((Date) contactDTO.get("date")), (Integer) contactDTO.get("probability"));
            }

            TimeSeriesCollection timeSeries = new TimeSeriesCollection();
            timeSeries.addSeries(timeSerie);

            //  Create tooltip and URL generators
            SimpleDateFormat sdf = new SimpleDateFormat(JSPHelper.getMessage(request, "datePattern"), locale);
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(0);

            StandardXYToolTipGenerator ttg = new StandardXYToolTipGenerator("{0}: ({1}, {2}%)",
                    sdf, numberFormat);


            StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES,
                    ttg, null);

            renderer.setBaseShapesVisible(true);
            renderer.setItemLabelsVisible(false);
            renderer.setShapesFilled(true);
            renderer.setSeriesPaint(0, Color.blue);


            DateTickUnit tickUnit;
            if (PERIOD_DAY.equals(period)) {
                tickUnit = new DateTickUnit(DateTickUnit.DAY, 2, new SimpleDateFormat("dd"));
            } else if (PERIOD_WEEK.equals(period)) {
                tickUnit = new DateTickUnit(DateTickUnit.DAY, 7, new SimpleDateFormat("ww"));
            } else if (PERIOD_MONTH.equals(period)) {
                tickUnit = new DateTickUnit(DateTickUnit.MONTH, 1, new SimpleDateFormat("MM"));
            } else if (PERIOD_YEAR.equals(period)) {
                tickUnit = new DateTickUnit(DateTickUnit.YEAR, 1, new SimpleDateFormat("yy"));
            } else { //by default week
                tickUnit = new DateTickUnit(DateTickUnit.DAY, 7, new SimpleDateFormat("ww"));
            }

            DateAxis dateAxis = new DateAxis(JSPHelper.getMessage(request, "Common.date"));
            dateAxis.setTickUnit(tickUnit);
            dateAxis.setUpperMargin(0.25D);


            NumberAxis valueAxis = new NumberAxis(JSPHelper.getMessage(request, "SalesProcess.probability") + " (" +
                    JSPHelper.getMessage(request, "Common.probabilitySymbol") + ")");
            valueAxis.setAutoRangeIncludesZero(false);
            valueAxis.setUpperBound(100D);
            valueAxis.setLowerBound(0D);
            valueAxis.setTickUnit(new NumberTickUnit(10, numberFormat));
            //valueAxis.setUpperMargin(0.1D);

            XYPlot plot = new XYPlot(timeSeries, dateAxis, valueAxis, renderer);
            plot.setBackgroundPaint(Color.white);
            plot.setDomainGridlinePaint(Color.lightGray);
            plot.setRangeGridlinePaint(Color.lightGray);
            plot.setDomainCrosshairVisible(true);
            plot.setRangeCrosshairVisible(true);

            //red
            IntervalMarker intervalmarker = new IntervalMarker(0, 33);
            intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
            intervalmarker.setPaint(new Color(123, 248, 205));
            plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
            //yellow
            intervalmarker = new IntervalMarker(33, 66);
            intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
            intervalmarker.setPaint(new Color(255, 246, 195));
            plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);
            //green
            intervalmarker = new IntervalMarker(66, 120);
            intervalmarker.setLabelAnchor(RectangleAnchor.LEFT);
            intervalmarker.setPaint(new Color(255, 177, 175));
            plot.addRangeMarker(intervalmarker, Layer.BACKGROUND);

            Date today = new Date();
            ValueMarker marker = new ValueMarker(today.getTime());
            marker.setLabel(" " + JSPHelper.getMessage(request, "Common.today"));
            marker.setLabelPaint(Color.darkGray);
            marker.setPaint(Color.darkGray);
            marker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
            marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
            marker.setLabelOffset(new RectangleInsets(UnitType.ABSOLUTE, 1, 1, 1, 1));
            marker.setStroke(new BasicStroke(1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{2f}, 0f));
            plot.addDomainMarker(marker);


            JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
            //Image background, by default white
            chart.setBackgroundPaint(Color.WHITE);

            //  Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filename = ServletUtilities.saveChartAsPNG(chart, 550, 350, info, session);

            //  Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info, false);
            pw.flush();

        } catch (Exception e) {
            log.error("Error generating the image", e);
        }
        return filename;
    }
}
