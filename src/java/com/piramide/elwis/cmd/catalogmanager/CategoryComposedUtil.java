package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.utils.CategoryUtil;
import net.java.dev.strutsejb.dto.ParamDTO;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CategoryComposedUtil.java 15-abr-2009 11:05:16 $
 */
public class CategoryComposedUtil {

    public static boolean paramHasSecondTable(ParamDTO paramDTO) {
        return (getSecondTable(paramDTO) != null);
    }

    public static String getSecondTable(ParamDTO paramDTO) {
        return (paramDTO.get("secondTable") != null ? (String) paramDTO.get("secondTable") : null);
    }

    public static boolean useSecondGroupId(ParamDTO paramDTO, Category category) {
        boolean useSecondGroup = false;
        if (paramDTO.get("moduleTable") != null) {
            String initTable = (String) paramDTO.get("moduleTable");
            useSecondGroup = CategoryUtil.useCategorySecondGroupId(initTable, category.getTable());
        }
        return useSecondGroup;
    }
}
