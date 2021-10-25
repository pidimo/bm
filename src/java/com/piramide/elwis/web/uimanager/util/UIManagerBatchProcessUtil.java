package com.piramide.elwis.web.uimanager.util;

import com.piramide.elwis.cmd.uimanager.migration.UIMigrationBatchProcessCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Util to execute the batch process of UI layout migration
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.6
 */
public class UIManagerBatchProcessUtil {

    private Log log = LogFactory.getLog(this.getClass());


    public void migrateOldLayoutInBootstrapLayout() {
        log.info("Start ui layout migration batch process..");

        UIMigrationBatchProcessCmd cmd = new UIMigrationBatchProcessCmd();
        cmd.setOp("allMigration");

        try {
            BusinessDelegate.i.execute(cmd, null);
        } catch (AppLevelException e) {
            log.error("Fail execute bussines delegate", e);
        }

        log.info("End ui layout migration batch process..");
    }

}
