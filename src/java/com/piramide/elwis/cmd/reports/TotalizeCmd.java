package com.piramide.elwis.cmd.reports;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.reportmanager.*;
import com.piramide.elwis.dto.reportmanager.ReportDTO;
import com.piramide.elwis.dto.reports.ColumnDTO;
import com.piramide.elwis.dto.reports.TotalizeDTO;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id TotalizeCmd ${time}
 */
public class TotalizeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        Integer reportId = Integer.valueOf(paramDTO.get("reportId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        if ("read".equals(this.getOp()) || "".equals(this.getOp())) {
            read(reportId, companyId);
        }
        if ("create".equals(this.getOp())) {
            create(reportId, companyId, ctx);
        }
    }


    private void create(Integer reportId, Integer companyId, SessionContext ctx) {

        Integer dbSum = countTotalizeVersion(reportId, companyId);

        Integer sumVersion = Integer.valueOf(paramDTO.get("sumVersion").toString());

        if (!dbSum.equals(sumVersion)) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.setForward("Fail");
            return;
        }

        ChartHome chartHome =
                (ChartHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_CHART);

        List updateErrorsList = new ArrayList();

        if (null != paramDTO.get("sumRecords")) {
            Totalize sumTotalize = readEspecialTotalize(null, reportId, companyId);

            if (null == sumTotalize) {
                TotalizeDTO dto = new TotalizeDTO();
                dto.put("companyId", companyId);
                dto.put("reportId", reportId);
                dto.put("totalizeType", ReportConstants.TOTALIZER_TYPE_SUMRECORDS);

                ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            } else {
                Integer actualVersion = sumTotalize.getVersion();
                sumTotalize.setVersion(new Integer(actualVersion.intValue() + 1));
            }
        } else {
            Totalize sumTotalize = readEspecialTotalize(null, reportId, companyId);
            if (null != sumTotalize) {

                Chart chartX = null;
                Chart chartY = null;
                Chart chartZ = null;
                try {
                    chartX = chartHome.findByTotalizeId(sumTotalize.getTotalizeId(), companyId);
                    chartY = chartHome.findByTotalizeId(sumTotalize.getTotalizeId(), companyId);
                    chartZ = chartHome.findByTotalizeId(sumTotalize.getTotalizeId(), companyId);
                } catch (FinderException e) {
                }

                if (null != chartX || null != chartY || null != chartZ) {
                    Map element = new HashMap();
                    element.put("totalizeType", ReportConstants.TOTALIZER_TYPE_SUMRECORDS);
                    element.put("columnName", "");
                    updateErrorsList.add(element);
                } else {
                    try {
                        sumTotalize.remove();
                    } catch (RemoveException e) {
                    }
                }
            }
        }

        //all checkBox that have selected in IU
        List iuCheckBoxList = getOptions("checkBox");

        //all checkBox that have read from data base
        List iuHiddenList = getOptions("checked");

        //exclude the selected checkBox from the all checkBox
        //and iuHiddenList contains only checkbox can be removed
        iuHiddenList.removeAll(iuCheckBoxList);


        ColumnTotalizeHome columnTotalizeHome =
                (ColumnTotalizeHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMNTOTALIZE);


        ColumnHome columnHome =
                (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        //delete all totalizers that have not selected
        for (int i = 0; i < iuHiddenList.size(); i++) {
            Map hiddenElement = (Map) iuHiddenList.get(i);

            Integer columnId = Integer.valueOf(hiddenElement.get("columnId").toString());
            Integer totalizeId = Integer.valueOf(hiddenElement.get("totalizeId").toString());
            Integer version = Integer.valueOf(hiddenElement.get("version").toString());

            TotalizeDTO totalizeDTO = new TotalizeDTO();
            totalizeDTO.put(totalizeDTO.getPrimKeyName(), totalizeId);

            Totalize totalize =
                    (Totalize) ExtendedCRUDDirector.i.read(totalizeDTO, resultDTO, false);

            //concurrency control
            if ((resultDTO.isFailure()) ||
                    (!totalize.getVersion().equals(version))) {
                resultDTO.addResultMessage("Common.error.concurrency");
                resultDTO.setForward("Fail");
                return;
            }


            Chart chartX = null;
            Chart chartY = null;
            Chart chartZ = null;
            try {
                chartX = chartHome.findByTotalizeId(totalize.getTotalizeId(), companyId);
                chartY = chartHome.findByTotalizeId(totalize.getTotalizeId(), companyId);
                chartZ = chartHome.findByTotalizeId(totalize.getTotalizeId(), companyId);
            } catch (FinderException fe) {
            }

            //reference integrity control
            if (null != chartX || null != chartY || null != chartZ) {

                Map element = new HashMap();
                try {
                    Column column = columnHome.findByPrimaryKey(columnId);
                    element.put("columnName", paramDTO.get("columnName_" + column.getColumnId()));
                } catch (FinderException e) {
                }

                element.put("totalizeType", totalize.getTotalizeType());

                updateErrorsList.add(element);
            } else {

                ColumnTotalizePK pk = new ColumnTotalizePK();
                pk.columnId = columnId;
                pk.totalizeId = totalizeId;
                try {
                    ColumnTotalize columnTotalize = columnTotalizeHome.findByPrimaryKey(pk);
                    columnTotalize.remove();
                } catch (FinderException e) {
                } catch (RemoveException e) {
                }

                try {
                    totalize.remove();
                } catch (RemoveException e) {
                }
            }
        }

        //create all totalizer that have selected in IU
        for (int i = 0; i < iuCheckBoxList.size(); i++) {
            Map checkBox = (Map) iuCheckBoxList.get(i);


            Integer totalizeId = null;
            Integer version = null;
            try {
                //if exists this totalize then update version
                totalizeId = Integer.valueOf(checkBox.get("totalizeId").toString());
                version = Integer.valueOf(checkBox.get("version").toString());

                TotalizeDTO dto = new TotalizeDTO();
                dto.put(dto.getPrimKeyName(), totalizeId);
                Totalize totalize = (Totalize) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
                if (resultDTO.isFailure()) {
                    resultDTO.addResultMessage("Common.error.concurrency");
                    resultDTO.setForward("Fail");
                    return;
                }
                totalize.setVersion(new Integer(version.intValue() + 1));
            } catch (NumberFormatException nfe) {
            }

            if (null == totalizeId) {
                //if no have totalizeId then create new totalize
                Integer columnId = Integer.valueOf(checkBox.get("columnId").toString());


                Integer totalizeType = Integer.valueOf(checkBox.get("totalizeType").toString());


                TotalizeDTO dto = new TotalizeDTO();
                dto.put("companyId", companyId);
                dto.put("reportId", reportId);
                dto.put("totalizeType", totalizeType);

                Totalize totalize = (Totalize) ExtendedCRUDDirector.i.create(dto, resultDTO, false);


                ColumnTotalizePK pk = new ColumnTotalizePK();
                pk.columnId = columnId;
                pk.totalizeId = totalize.getTotalizeId();
                try {
                    columnTotalizeHome.create(pk, companyId);
                } catch (CreateException ce) {
                    log.debug("Cannot create columnTotalize entity ... ");
                    resultDTO.addResultMessage("Report.common.deleteColumn");
                    resultDTO.setForward("Fail");
                    ctx.setRollbackOnly();

                    return;
                }
            }
        }

        for (int i = 0; i < updateErrorsList.size(); i++) {
            Map element = (Map) updateErrorsList.get(i);

            Integer type = (Integer) element.get("totalizeType");
            String iuValue = "";
            iuValue = (type.equals(ReportConstants.TOTALIZER_TYPE_SUM) ?
                    paramDTO.get("msgSum").toString() : iuValue);

            iuValue = (type.equals(ReportConstants.TOTALIZER_TYPE_AVERAGE) ?
                    paramDTO.get("msgAverage").toString() : iuValue);

            iuValue = (type.equals(ReportConstants.TOTALIZER_TYPE_LARGUESTVALUE) ?
                    paramDTO.get("msgLargestValue").toString() : iuValue);

            iuValue = (type.equals(ReportConstants.TOTALIZER_TYPE_SMALLESTVALUE) ?
                    paramDTO.get("msgSmallestValue").toString() : iuValue);

            iuValue = (type.equals(ReportConstants.TOTALIZER_TYPE_SUMRECORDS) ?
                    paramDTO.get("sumRecordsResource").toString() : iuValue);

            resultDTO.addResultMessage("Chart.common.CannotDelete", iuValue + " " + element.get("columnName"));
            resultDTO.setForward("Fail");
        }

        /*resultDTO.put("errorUpdateList", updateErrorsList);*/
    }

    /**
     * if into paramDTO have keys
     * <p/>
     * {checkBox_1_NONE_0_NONE = on,  checked_1_34_0_1=true, ... }
     * <p/>
     * then this method recober this keys and parsing
     * by example
     * getOptions("checkBox")
     *
     * @param option identifier that encounter in the paramDTO key
     * @return List that contain the parsed values of a key
     *         example
     *         [{columnId=1, totalizeId=NONE, totalizeType=0, version=NONE},
     *         {columnId=1, totalizeId=34, totalizeType=0, version=1} ...  ]
     */
    private List getOptions(String option) {
        List iuKeys = new ArrayList();

        for (Iterator it = paramDTO.entrySet().iterator(); it.hasNext();) {
            Map.Entry element = (Map.Entry) it.next();
            String key = (String) element.getKey();

            if (key.indexOf(option) > -1) {
                iuKeys.add(parseKey(key, "_"));
            }
        }
        return iuKeys;
    }

    private Map parseKey(String key, String delimiter) {
        StringTokenizer tokenizer = new StringTokenizer(key, delimiter);

        List keys = new ArrayList();
        while (tokenizer.hasMoreTokens()) {
            String actual = (String) tokenizer.nextElement();
            keys.add(actual);
        }
        Map result = new HashMap();
        result.put("columnId", keys.get(1));
        result.put("totalizeId", keys.get(2));
        result.put("totalizeType", keys.get(3));
        result.put("version", keys.get(4));

        return result;
    }


    //read the columns by report, and read all totalizers associated to this columns
    private void read(Integer reportId, Integer companyId) {


        resultDTO.put("sumVersion", countTotalizeVersion(reportId, companyId));

        ReportDTO reportDto = new ReportDTO();
        reportDto.put(reportDto.getPrimKeyName(), reportId);

        //read the actual report
        Report report = (Report) ExtendedCRUDDirector.i.read(reportDto, resultDTO, false);

        Totalize sumTotalize = readEspecialTotalize(report, reportId, companyId);
        if (null != sumTotalize) {
            resultDTO.put("sumRecords", Boolean.valueOf(true));
        } else {
            resultDTO.put("sumRecords", null);
        }

        //structure that contains all necessary information about the columns and totalize object
        //to construct the interface
        //example of this structure
        //[
        //  { label = test1, columnId = 1, totalizers = [{totalizerId = 10, totalizeType = 0},...,
        //                                               {totalizerId = 17, totalizeType = 3}]},
        //  { label = test2, columnId = 3, totalizers = [{totalizerId = 10, totalizeType = 0},...,
        //                                               {totalizerId = 17, totalizeType = 3}]}
        // ]

        ColumnHome columnHome = (ColumnHome) EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_COLUMN);

        List structure = new ArrayList();
        // gets all columns for this report
        Collection columns = null;
        try {
            columns = columnHome.findByReportId(reportId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find Columns for reportId = " + reportId, e);
        }
        if (null != columns) {
            for (Iterator it = columns.iterator(); it.hasNext();) {

                Column column = (Column) it.next();
                if (column.getIsTotalizer().booleanValue()) {
                    //get all associations with columnTotalizer
                    Collection columnTotalizers = column.getColumnTotalizers();


                    ColumnDTO colDTO = new ColumnDTO();
                    colDTO.put("columnId", column.getColumnId());
                    colDTO.put("label", column.getLabel());
                    colDTO.put("tableReference", column.getTableReference());
                    colDTO.put("columnReference", column.getColumnReference());
                    colDTO.put("path", new String(column.getPath()));

                    List totalizers = getTotalizersDTOList(columnTotalizers);
                    colDTO.put("totalizers", totalizers);

                    structure.add(colDTO);
                }
            }
        }

        resultDTO.put("structure", structure);
    }


    private Totalize readEspecialTotalize(Report report, Integer reportId, Integer CompanyId) {
        Totalize myTotalize = null;

        if (null == report) {
            ReportDTO reportDto = new ReportDTO();
            reportDto.put(reportDto.getPrimKeyName(), reportId);
            //read the actual report
            report = (Report) ExtendedCRUDDirector.i.read(reportDto, resultDTO, false);
        }


        Collection totalizers = report.getTotalizers();
        for (Iterator it = totalizers.iterator(); it.hasNext();) {
            Totalize totalize = (Totalize) it.next();
            if (ReportConstants.TOTALIZER_TYPE_SUMRECORDS.equals(totalize.getTotalizeType())) {
                myTotalize = totalize;
                break;
            }

        }

        return myTotalize;
    }

    private Integer countTotalizeVersion(Integer reportId, Integer companyId) {
        TotalizeHome totalizeHome = (TotalizeHome)
                EJBFactory.i.getEJBLocalHome(ReportConstants.JNDI_TOTALIZE);
        Integer sum = Integer.valueOf("0");
        try {
            Collection totalizers =
                    totalizeHome.findByReportIdNonCustomTotalize(reportId, companyId);
            int i = 0;
            for (Iterator iterator = totalizers.iterator(); iterator.hasNext();) {
                Totalize totalize = (Totalize) iterator.next();
                i += totalize.getVersion().intValue();
            }
            sum = new Integer(i);
        } catch (FinderException e) {
        }

        return sum;
    }

    //constructs totalizer part structure
    //example
    //totalizers = [{totalizerId = 10, totalizeType = 0},...,
    //              {totalizeType = 3}]
    private List getTotalizersDTOList(Collection columnTotalizers) {

        List totalizers = new ArrayList();
        for (Iterator ctIt = columnTotalizers.iterator(); ctIt.hasNext();) {
            ColumnTotalize columnTotalize = (ColumnTotalize) ctIt.next();

            TotalizeDTO totalizeDTO = new TotalizeDTO();
            totalizeDTO.put(totalizeDTO.getPrimKeyName(), columnTotalize.getTotalizeId());

            //get the totalize object associated with the column
            Totalize totalize = (Totalize) ExtendedCRUDDirector.i.read(totalizeDTO, resultDTO, false);

            //filter only non custom totalizers
            if (ReportConstants.TOTALIZER_NONCUSTOM_TYPES.contains(totalize.getTotalizeType())) {
                totalizeDTO.put("totalizeType", totalize.getTotalizeType());
                totalizeDTO.put("version", totalize.getVersion());
                totalizers.add(totalizeDTO);
            }
        }

        //complete totalizers that are non custom type
        totalizers = completeNonCustomTotalize(totalizers);

        return totalizers;
    }

    //complete the List of totalizers with totalizerDTO objects
    private List completeNonCustomTotalize(List dtoTotalizeList) {

        List newList = new ArrayList();

        List auxList = new ArrayList();

        //fill the new list whith the totalizeDTO objects
        for (int i = 0; i < ReportConstants.TOTALIZER_NONCUSTOM_TYPES.size(); i++) {
            TotalizeDTO totalizeDTO = new TotalizeDTO();

            totalizeDTO.put("totalizeType", ReportConstants.TOTALIZER_NONCUSTOM_TYPES.get(i));
            newList.add(totalizeDTO);
        }

        //fills auxList with newList
        auxList = new ArrayList(newList);

        //change the newList values by dto values accorging to totalizeType value
        for (int i = 0; i < dtoTotalizeList.size(); i++) {
            TotalizeDTO dto = (TotalizeDTO) dtoTotalizeList.get(i);

            Integer type = (Integer) dto.get("totalizeType");

            for (int j = 0; j < auxList.size(); j++) {

                TotalizeDTO actualDto = (TotalizeDTO) newList.get(j);

                if (type.equals((Integer) actualDto.get("totalizeType"))) {
                    newList.remove(j);
                    newList.add(j, dto);
                }
            }
        }


        return newList;
    }

    public boolean isStateful() {
        return false;
    }
}

