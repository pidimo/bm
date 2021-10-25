package com.piramide.elwis.cmd.uimanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.uimanager.*;
import com.piramide.elwis.dto.uimanager.StyleAttributeDTO;
import com.piramide.elwis.dto.uimanager.StyleDTO;
import com.piramide.elwis.dto.uimanager.StyleSheetDTO;
import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyStyleSheet implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        copyStyleSheet(source.getCompanyId(), target.getCompanyId());
    }

    private void copyStyleSheet(Integer sourceCompanyId, Integer targetCompanyId) {
        StyleSheetHome styleSheetHome =
                (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        try {
            StyleSheet sourceElement = styleSheetHome.findByCompanyId(sourceCompanyId, UIManagerConstants.StyleSheetType.NORMAL.getConstant());

            StyleSheet targetElement = createStyleSheet(sourceElement, targetCompanyId);

            createStyles(sourceElement.getStyles(), targetElement.getStyleSheetId(), targetCompanyId);

        } catch (FinderException e) {
            log.debug("Cannot read stylesheet for source company = " + sourceCompanyId);
        } catch (CreateException e) {
            log.debug("Cannot create target styleSheet for company = " + targetCompanyId);
        }

        //copy bootstrap styles
        try {
            StyleSheet sourceElement = styleSheetHome.findByCompanyId(sourceCompanyId, UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant());

            StyleSheet targetElement = createStyleSheet(sourceElement, targetCompanyId);

            createStyles(sourceElement.getStyles(), targetElement.getStyleSheetId(), targetCompanyId);

        } catch (FinderException e) {
            log.debug("Cannot read bootstrap stylesheet for source company = " + sourceCompanyId);
        } catch (CreateException e) {
            log.debug("Cannot create target bootstrap styleSheet for company = " + targetCompanyId);
        }
    }

    private void createStyles(Collection sourceStyles, Integer styleSheetId, Integer companyId) {
        StyleHome styleHome =
                (StyleHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLE);
        if (null != sourceStyles) {
            for (Object object : sourceStyles) {
                Style sourceStyle = (Style) object;
                StyleDTO targetStyleDTO = new StyleDTO();
                targetStyleDTO.put("companyId", companyId);
                targetStyleDTO.put("styleSheetId", styleSheetId);
                targetStyleDTO.put("name", sourceStyle.getName());

                try {
                    Style targetStyle = styleHome.create(targetStyleDTO);
                    sourceStyle.getStyleAttributes();

                    createStyleAttributes(sourceStyle.getStyleAttributes(), targetStyle.getStyleId(), companyId);

                } catch (CreateException e) {
                    log.debug("Cannot create style for companyId = " + companyId);
                }
            }
        }
    }

    private void createStyleAttributes(Collection sourceStyleAttributes,
                                       Integer styleId,
                                       Integer companyId) throws CreateException {

        StyleAttributeHome attributeHome =
                (StyleAttributeHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLEATTRIBUTE);

        if (null != sourceStyleAttributes) {
            for (Object object : sourceStyleAttributes) {
                StyleAttribute sourceAttribute = (StyleAttribute) object;
                StyleAttributeDTO targetDTO = new StyleAttributeDTO();
                targetDTO.put("companyId", companyId);
                targetDTO.put("name", sourceAttribute.getName());
                targetDTO.put("styleId", styleId);
                targetDTO.put("value", sourceAttribute.getValue());

                attributeHome.create(targetDTO);
            }
        }
    }

    private StyleSheet createStyleSheet(StyleSheet sourceStyleSheet, Integer companyId) throws CreateException {
        StyleSheetHome styleSheetHome =
                (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        StyleSheetDTO targetStyleSheetDTO = new StyleSheetDTO();
        targetStyleSheetDTO.put("companyId", companyId);
        targetStyleSheetDTO.put("userId", sourceStyleSheet.getUserId());
        targetStyleSheetDTO.put("styleSheetType", sourceStyleSheet.getStyleSheetType());
        targetStyleSheetDTO.remove("styleSheetId");
        return styleSheetHome.create(targetStyleSheetDTO);
    }
}
