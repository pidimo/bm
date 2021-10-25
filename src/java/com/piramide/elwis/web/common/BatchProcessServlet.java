package com.piramide.elwis.web.common;

import com.piramide.elwis.web.contactmanager.util.TelecomBatchProcessUtil;
import com.piramide.elwis.web.reports.util.TitusStructureBatchProcessUtil;
import com.piramide.elwis.web.uimanager.util.UIManagerBatchProcessUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * <p/>
 * This class implements a Servlets that runs at last position in the Initialization chain.
 * In this class should be implemented the BatchProcesses methods.
 *
 * @author alvaro
 * @version $Id: BatchProcessServlet.java 03-sep-2009 18:50:15
 */
public class BatchProcessServlet extends HttpServlet {
    protected static Log log = LogFactory.getLog("BatchProcessServlet");

    public void init() throws ServletException {
        super.init();
        log.debug("Executing BatchProcessServlet..................................... ");

        removedColumnsTitusBatchProcess();

        //todo: remove when ELWIS v5.2.4 is released
        fixTelecomPredeterminedBatchProcess();

        //todo: remove when ELWIS v6.0 is released
        migrateOldLayoutBatchProcess();
    }

    protected void finalize() throws Throwable {
        super.finalize();
        log.debug("Finalize BatchProcessServlet.....................................");
    }
    //Batch processes methods....


    /**
     * miky: titus batch process to ELWIS v4.2.2
     */
    private void removedColumnsTitusBatchProcess() {
        log.debug("Executing removedColumnsTitusBatchProcess.........");

        List<Map> tableFieldList = new ArrayList<Map>();

        //removed column in contracts
        Map map = new HashMap();
        map.put("table", "productcontract");
        map.put("column", "productid");
        tableFieldList.add(map);

        //before deleted columns
        map = new HashMap();
        map.put("table", "saleposition");
        map.put("column", "invoicetoid");
        tableFieldList.add(map);

        map = new HashMap();
        map.put("table", "template");
        map.put("column", "editorid");
        tableFieldList.add(map);

        map = new HashMap();
        map.put("table", "contact");
        map.put("column", "contactmediaid");
        tableFieldList.add(map);

        map = new HashMap();
        map.put("table", "invoice");
        map.put("column", "vatid");
        tableFieldList.add(map);

        TitusStructureBatchProcessUtil titusBatchProcessUtil = new TitusStructureBatchProcessUtil(this.getServletContext());
        titusBatchProcessUtil.fixRemovedColumns(tableFieldList);
    }

    /**
     * miky: batch process to ELWIS v5.2.4
     * todo: remove when this is released
     */
    private void fixTelecomPredeterminedBatchProcess() {
        TelecomBatchProcessUtil telecomBatchProcessUtil = new TelecomBatchProcessUtil(this.getServletContext());
        telecomBatchProcessUtil.fixPredeterminedTelecomError();
    }

    private void migrateOldLayoutBatchProcess() {
        UIManagerBatchProcessUtil uiManagerBatchProcessUtil = new UIManagerBatchProcessUtil();
        uiManagerBatchProcessUtil.migrateOldLayoutInBootstrapLayout();
    }

}
