package com.piramide.elwis.cmd.uimanager;

import com.piramide.elwis.domain.uimanager.Style;
import com.piramide.elwis.domain.uimanager.StyleAttribute;
import com.piramide.elwis.domain.uimanager.StyleHome;
import com.piramide.elwis.dto.uimanager.StyleAttributeDTO;
import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Util for style sheet configuration
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class StyleSheetUtilCmd  extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing StyleSheetUtilCmd......." + paramDTO);

        if ("readWVAppStyles".equals(getOp())) {
            readStyles();
        }
    }

    public void readStyles() {
        List<Map> stylesMapList = new ArrayList<Map>();

        Integer userId = null;
        List<String> styleNameList = (List<String>) paramDTO.get("listStyleNames");
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer styleSheetType = Integer.valueOf(paramDTO.get("styleSheetType").toString());

        if (paramDTO.get("userId") != null) {
            userId = Integer.valueOf(paramDTO.get("userId").toString());
        }

        for (String name : styleNameList) {
            Style style = findStyle(name, userId, companyId, styleSheetType);

            if (style != null) {
                Collection styleAttributes = style.getStyleAttributes();
                List styleAttributeDtos = new ArrayList();
                for (Iterator iterator2 = styleAttributes.iterator(); iterator2.hasNext();) {
                    StyleAttribute styleAttribute = (StyleAttribute) iterator2.next();
                    StyleAttributeDTO attributeDTO = new StyleAttributeDTO();

                    DTOFactory.i.copyToDTO(styleAttribute, attributeDTO);
                    styleAttributeDtos.add(attributeDTO);
                }

                //add style configuration
                Map mapStyle = new HashMap();
                mapStyle.put("styleClassName", style.getName());
                mapStyle.put("styleAttributesList", styleAttributeDtos);
                stylesMapList.add(mapStyle);
            }
        }

        resultDTO.put("wvappStyleMapList", stylesMapList);
    }

    private Style findStyle(String name, Integer userId, Integer companyId, Integer styleSheetType) {
        Style style = null;

        if (companyId != null && name != null && styleSheetType != null) {
            StyleHome styleHome = (StyleHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLE);
            try {
                Collection collection;

                if (userId != null) {
                    collection = styleHome.findByNameOfUser(name, userId, companyId, styleSheetType);
                } else {
                    collection = styleHome.findByNameOfCompany(name, companyId, styleSheetType);
                }

                if (!collection.isEmpty()) {
                    style = (Style) collection.iterator().next();
                }
            } catch (FinderException e) {
                log.debug("Error in find style...", e);
            }
        }
        return style;
    }

}
