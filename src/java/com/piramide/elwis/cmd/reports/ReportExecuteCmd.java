package com.piramide.elwis.cmd.reports;

import com.jatun.titus.reportgenerator.ReportGenerator;
import com.jatun.titus.reportgenerator.util.ReportData;
import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ServiceLocator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportExecuteCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ReportExecuteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug(" Executing ReportExecuteCmd....");
        Object op = paramDTO.get("op");
        if (op == null) {
            op = "execute";
        }

        if (op.toString().equals("execute")) {
            executeReport();
        }
    }

    private void executeReport() {
        //Get the connection for the fill process
        Connection conn = null;
        try {
            final DataSource ds = (DataSource) ServiceLocator.i.lookup(Constants.JNDI_ELWISDS);
            conn = ds.getConnection();
        } catch (Exception ex) {
            log.debug("ERROR IN Getting the connection from Jboss IN REPORTEXECUTECMD.....");
            ex.printStackTrace();
        }
        ReportData reportData = (ReportData) paramDTO.get("reportData");
        reportData.putReportConfigParam("DBConnection", conn);

        //Execute the report
        reportData = new ReportGenerator().generateReport(reportData);
        resultDTO.put("reportData", reportData);

        //Close the connection (if is open)
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException sqlex) {/*The connection is closed*/}
    }
}
