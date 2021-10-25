package com.piramide.elwis.web.bmapp.action.uimanager;

import com.piramide.elwis.cmd.uimanager.StyleSheetUtilCmd;
import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Util to manage WVApp style sheet configuration
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class StyleSheetWVAppUtil {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Read style configuration for company or user related to wvapp app
     * @param companyId the companyId
     * @param userId null to read only company configuration
     * @return list
     */
    private List<Map> readStylesAsListMap(Integer companyId, Integer userId) {
        List<Map> result = new ArrayList<Map>();

        if (companyId != null) {

            StyleSheetUtilCmd cmd = new StyleSheetUtilCmd();
            cmd.setOp("readWVAppStyles");
            cmd.putParam("companyId", companyId);
            cmd.putParam("listStyleNames", getStyleClassNamesList());
            cmd.putParam("styleSheetType", UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant());

            if (userId != null) {
                cmd.putParam("userId", userId);
            }

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
                if (resultDTO.get("wvappStyleMapList") != null) {
                    result = (List<Map>) resultDTO.get("wvappStyleMapList");
                }

            } catch (AppLevelException e) {
                log.debug("Error in execute cmd StyleSheetUtilCmd...", e);
            }
        }

        return result;
    }

    public List<Map> readWVAppCompanyStyles(Integer companyId) {
        return readStylesAsListMap(companyId, null);
    }

    public List<Map> readWVAppUserStyles(Integer companyId, Integer userId) {
        return readStylesAsListMap(companyId, userId);
    }


    /**
     * List o style class names related with wvapp app
     * @return list
     */
    private static List<String> getStyleClassNamesList() {
        List<String> list = new ArrayList<String>();
        list.add(".wvappGeneral");

        return list;
    }
}
