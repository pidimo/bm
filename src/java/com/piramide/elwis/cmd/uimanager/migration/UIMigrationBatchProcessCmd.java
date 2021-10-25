package com.piramide.elwis.cmd.uimanager.migration;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
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
import java.util.*;

/**
 * Cmd to migrate old Style sheet to bootstrap style sheet
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.6
 */
public class UIMigrationBatchProcessCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UIMigrationBatchProcessCmd......." + paramDTO);

        if ("allMigration".equals(getOp())) {
            allLayoutMigration(ctx);
        }
        if ("userMigration".equals(getOp())) {
            userLayoutMigration(ctx);
        }
    }

    private void userLayoutMigration(SessionContext ctx) {
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);

        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        StyleSheet styleSheet = null;
        try {
            styleSheet = styleSheetHome.findByUserIdAndCompanyId(userId, companyId, UIManagerConstants.StyleSheetType.NORMAL.getConstant());
        } catch (FinderException e) {
            e.printStackTrace();
        }

        if (styleSheet != null) {
            try {
                migrateStyleSheet(styleSheet);
            } catch (CreateException e) {
                log.info("Error in user layout migration process...", e);
                ctx.setRollbackOnly();
            }
        }
    }

    private void allLayoutMigration(SessionContext ctx) {
        try {
            companiesLayoutMigration();
            usersLayoutMigration();
        } catch (CreateException e) {
            log.info("Error in layout migration process...", e);
            ctx.setRollbackOnly();
        }
    }

    private void companiesLayoutMigration() throws CreateException {
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);

        //company layout migration
        Collection oldCompanies;
        try {
            oldCompanies = styleSheetHome.findByStyleSheetTypeAllCompanies(UIManagerConstants.StyleSheetType.NORMAL.getConstant());
        } catch (FinderException ignore) {
            oldCompanies = new ArrayList();
        }

        for (Iterator iterator = oldCompanies.iterator(); iterator.hasNext(); ) {
            StyleSheet styleSheet = (StyleSheet) iterator.next();
            migrateStyleSheet(styleSheet);
        }
    }

    private void usersLayoutMigration() throws CreateException {
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);

        //user layout migration
        Collection oldUsers;
        try {
            oldUsers = styleSheetHome.findByStyleSheetTypeAllUser(UIManagerConstants.StyleSheetType.NORMAL.getConstant());
        } catch (FinderException ignore) {
            oldUsers = new ArrayList();
        }

        for (Iterator iterator = oldUsers.iterator(); iterator.hasNext(); ) {
            StyleSheet styleSheet = (StyleSheet) iterator.next();
            migrateStyleSheet(styleSheet);
        }
    }

    private void migrateStyleSheet(StyleSheet oldStyleSheet) throws CreateException {

        log.info("Starting old layout migration for companyId=" + oldStyleSheet.getCompanyId() + " ,userId=" + oldStyleSheet.getUserId());

        if (!alreadyExistBootstrapStyle(oldStyleSheet)) {
            StyleSheet newStyleSheet = createStyleSheet(oldStyleSheet);

            for (Iterator iterator = oldStyleSheet.getStyles().iterator(); iterator.hasNext();) {
                Style oldStyle = (Style) iterator.next();

                for (Iterator iterator2 = oldStyle.getStyleAttributes().iterator(); iterator2.hasNext();) {
                    StyleAttribute oldAttribute = (StyleAttribute) iterator2.next();
                    migrationProcess(newStyleSheet, oldStyle, oldAttribute);
                }
            }

            log.info("Migration is success. companyId=" + oldStyleSheet.getCompanyId() + " ,userId=" + oldStyleSheet.getUserId());
        } else {
            log.info("Can not migrate because there is already a configuration for the new bootstrap layout. companyId=" + oldStyleSheet.getCompanyId() + " ,userId=" + oldStyleSheet.getUserId());
        }
    }

    private void migrationProcess(StyleSheet newStyleSheet, Style oldStyle, StyleAttribute oldAttribute) throws CreateException {
        String oldClassName = oldStyle.getName();
        String oldAttributeName = oldAttribute.getName();
        String oldAttributeValue = oldAttribute.getValue();

        List<Map> listStyles = getNewBootstrapStyles(oldClassName, oldAttributeName, oldAttributeValue);
        if (!listStyles.isEmpty()) {
            for (Map map : listStyles) {

                String className = map.get("className").toString();
                String attributeName = map.get("attribute").toString();
                String attributeValue = map.get("value").toString();

                boolean alreadyExist = false;
                if ("true".equals(map.get("isConstantStyle"))) {
                    alreadyExist = alreadyExistAttribute(newStyleSheet.getStyleSheetId(), className, attributeName);
                }

                if (!alreadyExist) {
                    createBootstrapCSS(newStyleSheet, className, attributeName, attributeValue);
                }
            }
        }
    }

    private List<Map> getNewBootstrapStyles(String oldClassName, String oldAttributeName, String oldAttributeValue) {
        List<Map> result = new ArrayList<Map>();

        UIMigrationModule uiMigrationModule = new UIMigrationModuleGeneral();
        result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);

        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleMenu();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleDetailTabs();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleForms();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleLists();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleSchedulerCalendars();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleSchedulerTask();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleDashboard();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }
        if (result.isEmpty()) {
            uiMigrationModule = new UIMigrationModuleProjects();
            result = uiMigrationModule.migration(oldClassName, oldAttributeName, oldAttributeValue);
        }

        return result;
    }

    private boolean createBootstrapCSS(StyleSheet newStyleSheet, String className, String attributeName, String value) throws CreateException {
        StyleAttribute newStyleAttribute = null;

        Style newStyle = findStyle(newStyleSheet.getStyleSheetId(), className);
        if (newStyle == null) {
            newStyle = createStyle(className, newStyleSheet);
        }

        if (newStyle != null) {
            newStyleAttribute = createStyleAttribute(attributeName, value, newStyle);
        }

        return newStyleAttribute != null;
    }

    private StyleSheet createStyleSheet(StyleSheet oldStyleSheet) throws CreateException{
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);

        StyleSheetDTO styleSheetDTO = new StyleSheetDTO();
        styleSheetDTO.put("companyId", oldStyleSheet.getCompanyId());
        styleSheetDTO.put("userId", oldStyleSheet.getUserId());
        styleSheetDTO.put("styleSheetType", UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant());

        return styleSheetHome.create(styleSheetDTO);
    }

    private Style createStyle(String name, StyleSheet newStyleSheet) throws CreateException{
        StyleHome styleHome = (StyleHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLE);

        StyleDTO styleDTO = new StyleDTO();
        styleDTO.put("name", name);
        styleDTO.put("companyId", newStyleSheet.getCompanyId());
        styleDTO.put("styleSheetId", newStyleSheet.getStyleSheetId());

        return styleHome.create(styleDTO);
    }

    private StyleAttribute createStyleAttribute(String name, String value, Style newStyle) throws CreateException{
        StyleAttributeHome styleAttributeHome = (StyleAttributeHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLEATTRIBUTE);

        StyleAttributeDTO styleAttributeDTO = new StyleAttributeDTO();
        styleAttributeDTO.put("name", name);
        styleAttributeDTO.put("value", value);
        styleAttributeDTO.put("companyId", newStyle.getCompanyId());
        styleAttributeDTO.put("styleId", newStyle.getStyleId());

        return styleAttributeHome.create(styleAttributeDTO);
    }

    private Style findStyle(Integer styleSheetId, String name) {
        Style style = null;

        if (styleSheetId != null && name != null) {
            StyleHome styleHome = (StyleHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLE);
            try {
                Collection collection = styleHome.findByStyleSheetIdAndName(styleSheetId, name);
                if (!collection.isEmpty()) {
                    style = (Style) collection.iterator().next();
                }
            } catch (FinderException e) {
                log.debug("Error in find style...", e);
            }
        }
        return style;
    }



    private boolean alreadyExistBootstrapStyle(StyleSheet oldStyleSheet) {
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        StyleSheet bootstrapStyleSheet = null;

        try {
            if (oldStyleSheet.getUserId() != null) {
                bootstrapStyleSheet = styleSheetHome.findByUserIdAndCompanyId(oldStyleSheet.getUserId(), oldStyleSheet.getCompanyId(), UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant());
            } else {
                bootstrapStyleSheet = styleSheetHome.findByCompanyId(oldStyleSheet.getCompanyId(), UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant());
            }
        } catch (FinderException e) {
            log.debug("Error in find style sheet.. " + e);
        }

        return bootstrapStyleSheet != null;
    }

    private boolean alreadyExistAttribute(Integer styleSheetId, String className, String attributeName) {
        StyleAttributeHome styleAttributeHome = (StyleAttributeHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLEATTRIBUTE);
        Collection collection = new ArrayList();

        if (styleSheetId != null && className != null && attributeName != null) {
            try {
                collection = styleAttributeHome.findByStyleSheetIdAndClassNameAndAttributeName(styleSheetId, className, attributeName);
            } catch (FinderException e) {
                log.debug("Error in find style attribute...", e);
            }
        }

        return !collection.isEmpty();
    }

}
