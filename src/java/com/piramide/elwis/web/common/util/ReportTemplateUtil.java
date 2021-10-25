package com.piramide.elwis.web.common.util;

import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.piramide.elwis.utils.Constants;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Jatun S.R.L.
 * Util to resize report template width
 *
 * @author Miky
 * @version $Id: ReportTemplateUtil.java 09-oct-2008 15:33:02 $
 */
public class ReportTemplateUtil {
    private Log log = LogFactory.getLog(this.getClass());

    public static String TITLE_BAND = "1";
    public static String PAGEHEADER_BAND = "2";
    public static String COLUMNHEADER_BAND = "3";
    public static String GROUPHEADER_BAND = "4";
    public static String DETAIL_BAND = "5";
    public static String GROUPFOOTER_BAND = "6";
    public static String COLUMNFOOTER_BAND = "7";
    public static String PAGEFOOTER_BAND = "8";
    public static String SUMMARY_BAND = "9";

    private JasperDesign jasperDesign;
    private Map<String, List> moveMap;
    private Map<String, List> resizeMap;
    private List<TemplateTableColumn> tableResizeList;

    private int pageWidth;
    private int resizeWidth;

    public ReportTemplateUtil() {
        moveMap = new HashMap<String, List>();
        resizeMap = new HashMap<String, List>();
        tableResizeList = new ArrayList<TemplateTableColumn>();
    }

    /**
     * Resize template
     *
     * @param templateStream  own template as stream
     * @param pageSize        own page size
     * @param pageOrientation own page orientation
     * @return InputStream
     * @throws JRException
     * @throws IOException
     */
    public InputStream resizeTemplate(InputStream templateStream, String pageSize, String pageOrientation) throws JRException, IOException {
        log.debug("Excecuting resizeTemplate......");
        InputStream inputStream = templateStream;
        if (existPageSize(pageSize, pageOrientation)) {
            jasperDesign = JRXmlLoader.load(templateStream);
            if (jasperDesign.getPageWidth() != pageWidth) {
                log.debug("Modify template.......");
                calculateResizeWidth();
                moveElement();
                resizeElement();
                tableResize();
            }

            //set in stream
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            JRXmlWriter.writeReport(jasperDesign, arrayOutputStream, Constants.CHARSET_ENCODING);

            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
            inputStream = arrayInputStream;
            arrayOutputStream.close();
            arrayInputStream.close();
        }
        return inputStream;
    }

    /**
     * verify if exist page configuration and set page properties
     *
     * @param pageSize
     * @param pageOrientation
     * @return boolean
     */
    private boolean existPageSize(String pageSize, String pageOrientation) {
        boolean exist = false;
        if (ReportGeneratorConstants.PAGE_A4.equals(pageSize)) {
            exist = true;
            if (ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT.equals(pageOrientation)) {
                pageWidth = ReportGeneratorConstants.PAGE_A4_WIDTH;
            } else {
                pageWidth = ReportGeneratorConstants.PAGE_A4_HEIGHT;
            }
        } else if (ReportGeneratorConstants.PAGE_LETTER.equals(pageSize)) {
            exist = true;
            if (ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT.equals(pageOrientation)) {
                pageWidth = ReportGeneratorConstants.PAGE_LETTER_WIDTH;
            } else {
                pageWidth = ReportGeneratorConstants.PAGE_LETTER_HEIGHT;
            }
        } else if (ReportGeneratorConstants.PAGE_LEGAL.equals(pageSize)) {
            exist = true;
            if (ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT.equals(pageOrientation)) {
                pageWidth = ReportGeneratorConstants.PAGE_LEGAL_WIDTH;
            } else {
                pageWidth = ReportGeneratorConstants.PAGE_LEGAL_HEIGHT;
            }
        }
        return exist;
    }

    private JRBand getJRBand(String bandKey) {
        JRBand jrBand = null;
        if (TITLE_BAND.equals(bandKey)) {
            jrBand = jasperDesign.getTitle();
        } else if (PAGEFOOTER_BAND.equals(bandKey)) {
            jrBand = jasperDesign.getPageFooter();
        } else if (SUMMARY_BAND.equals(bandKey)) {
            jrBand = jasperDesign.getSummary();
        } else if (DETAIL_BAND.equals(bandKey)) {
            jrBand = jasperDesign.getDetailSection().getBands()[0];
        } else if (COLUMNHEADER_BAND.equals(bandKey)) {
            jrBand = jasperDesign.getColumnHeader();
        }
        return jrBand;
    }

    private JRElement getJRELementByKey(JRBand jrBand, String keyElement) {
        JRElement jrElement = jrBand.getElementByKey(keyElement);
        if (jrElement == null) {
            throw new NoSuchElementException("No such element:" + keyElement);
        }
        return jrElement;
    }

    private void calculateResizeWidth() {
        int ownSumMargin = jasperDesign.getRightMargin() + jasperDesign.getLeftMargin();
        int ownColumnWidth = jasperDesign.getColumnWidth();
        int columnWidth = pageWidth - ownSumMargin;
        resizeWidth = columnWidth - ownColumnWidth;
    }

    private void moveElement() {
        for (String bandKey : moveMap.keySet()) {
            JRBand jrBand = getJRBand(bandKey);
            moveElement(moveMap.get(bandKey), jrBand);
        }
    }

    private void moveElement(List<String> KeyList, JRBand jrBand) {
        for (String key : KeyList) {
            JRElement jrElement = getJRELementByKey(jrBand, key);
            jrElement.setX(jrElement.getX() + resizeWidth);
        }
    }

    private void resizeElement() {
        for (String bandKey : resizeMap.keySet()) {
            JRBand jrBand = getJRBand(bandKey);
            resizeElement(resizeMap.get(bandKey), jrBand);
        }
    }

    private void resizeElement(List<String> KeyList, JRBand jrBand) {
        for (String key : KeyList) {
            JRElement jrElement = getJRELementByKey(jrBand, key);
            jrElement.setWidth(jrElement.getWidth() + resizeWidth);
        }
    }

    /**
     * resize main table columns
     */
    private void tableResize() {

        int columns = tableResizeList.size();
        int resize = resizeWidth / columns;
        int rest = Math.abs(resizeWidth - (columns * resize));
        int restCount = 0;
        int moveX = 0;

        //order by X
        orderTableColumnByX();

        for (TemplateTableColumn templateTableColumn : tableResizeList) {
            int resizeColumn = resize;
            if (restCount < rest) {
                resizeColumn += (resize > 0 ? 1 : -1);
                restCount++;
            }

            for (String bandKey : templateTableColumn.getTableColumnMap().keySet()) {
                JRBand jrBand = getJRBand(bandKey);
                tableColumnResize(templateTableColumn.getTableColumnMap().get(bandKey), jrBand, resizeColumn, moveX);
            }

            //update moveX to next column
            moveX += resizeColumn;
        }
    }

    private void tableColumnResize(List<String> KeyList, JRBand jrBand, int resize, int moveX) {
        for (String key : KeyList) {
            JRElement jrElement = getJRELementByKey(jrBand, key);
            jrElement.setWidth(jrElement.getWidth() + resize);
            jrElement.setX(jrElement.getX() + moveX);
        }
    }

    /**
     * Order table column by X position
     */
    private void orderTableColumnByX() {
        Object[] arrayTableColumns = tableResizeList.toArray();

        for (int i = 0; i < arrayTableColumns.length - 1; i++) {
            for (int j = i + 1; j < arrayTableColumns.length; j++) {
                TemplateTableColumn tableColumn1 = (TemplateTableColumn) arrayTableColumns[i];
                TemplateTableColumn tableColumn2 = (TemplateTableColumn) arrayTableColumns[j];

                JRElement jrElement1 = getFirtsColumnElement(tableColumn1.getTableColumnMap());
                JRElement jrElement2 = getFirtsColumnElement(tableColumn2.getTableColumnMap());

                if (jrElement1.getX() > jrElement2.getX()) {
                    TemplateTableColumn aux = (TemplateTableColumn) arrayTableColumns[i];
                    arrayTableColumns[i] = arrayTableColumns[j];
                    arrayTableColumns[j] = aux;
                }
            }
        }
        tableResizeList = (List) Arrays.asList(arrayTableColumns);
    }

    private JRElement getFirtsColumnElement(Map<String, List> columnMap) {
        JRElement jrElement = null;
        for (Iterator<String> iterator = columnMap.keySet().iterator(); iterator.hasNext();) {
            String bandKey = iterator.next();
            List columnKeyList = columnMap.get(bandKey);
            if (!columnKeyList.isEmpty()) {
                JRBand jrBand = getJRBand(bandKey);
                jrElement = getJRELementByKey(jrBand, columnKeyList.get(0).toString());
                break;
            }
        }

        return jrElement;
    }

    /**
     * Add move elements, this only to move
     *
     * @param bandKey    key of band
     * @param elementKey key of element
     */
    public void addMoveElementKey(String bandKey, String elementKey) {
        if (moveMap.containsKey(bandKey)) {
            moveMap.get(bandKey).add(elementKey);
        } else {
            List<String> elementKeyList = new ArrayList<String>();
            elementKeyList.add(elementKey);
            moveMap.put(bandKey, elementKeyList);
        }
    }

    /**
     * Add resize elements, only to resize
     *
     * @param bandKey    key of band
     * @param elementKey key of element
     */
    public void addResizeElementKey(String bandKey, String elementKey) {
        if (resizeMap.containsKey(bandKey)) {
            resizeMap.get(bandKey).add(elementKey);
        } else {
            List<String> elementKeyList = new ArrayList<String>();
            elementKeyList.add(elementKey);
            resizeMap.put(bandKey, elementKeyList);
        }
    }

    /**
     * Add main table column to resize
     *
     * @param templateTableColumn Object that manage main table column elements
     */
    public void addResizeTableColumn(TemplateTableColumn templateTableColumn) {
        tableResizeList.add(templateTableColumn);
    }

    /**
     * Class to manage main table elements
     */
    public class TemplateTableColumn {
        private Map<String, List> tableColumnMap;

        public TemplateTableColumn() {
            tableColumnMap = new HashMap<String, List>();
        }

        public void addTableColumnElementKey(String bandKey, String elementKey) {
            if (tableColumnMap.containsKey(bandKey)) {
                tableColumnMap.get(bandKey).add(elementKey);
            } else {
                List<String> elementKeyList = new ArrayList<String>();
                elementKeyList.add(elementKey);
                tableColumnMap.put(bandKey, elementKeyList);
            }
        }

        public Map<String, List> getTableColumnMap() {
            return tableColumnMap;
        }
    }

}
