package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.List;

/**
 * Cmd util to process category field values
 * @author Miguel A. Rojas Cardenas
 * @version 6.1.1
 */
public class CategoryFieldValueUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CategoryFieldValueUtilCmd.... " + paramDTO);
        String op = this.getOp();

        if ("readByCategoryFieldIdentifier".equals(op)) {
            readFieldValueByCategoryIdentifier(ctx);
        }
    }

    private void readFieldValueByCategoryIdentifier(SessionContext ctx) {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String fieldIdentifier = (String) paramDTO.get("fieldIdentifier");

        Category category = findCategory(fieldIdentifier, companyId);
        String fieldValue = null;

        if (category != null) {
            if (ContactConstants.CUSTOMER_CATEGORY.equals(category.getTable())) {
                fieldValue = readCustomerFieldValue(category, ctx);
            }
        }

        resultDTO.put("valueCategoryField", fieldValue);
    }

    private String readCustomerFieldValue(Category category, SessionContext ctx) {
        String fieldValue = null;
        Integer customerId = new Integer(paramDTO.get("customerId").toString());

        if (customerId != null) {
            //read categoryFieldValues object
            String finderName = "findByCustomerId";
            Object[] params = new Object[]{customerId, category.getCompanyId()};
            List paramsAsList = Arrays.asList(params);

            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("finderName", finderName);
            cmd.putParam("params", paramsAsList);
            cmd.setOp("readCAtegoryFieldValues");
            cmd.executeInStateless(ctx);
            ResultDTO myResultDTO = cmd.getResultDTO();

            //get the value
            if (myResultDTO.get(category.getCategoryId().toString()) != null) {
                fieldValue = myResultDTO.get(category.getCategoryId().toString()).toString();
            }
        }

        return fieldValue;
    }

    private Category findCategory(String fieldIdentifier, Integer companyId) {
        CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CATEGORY);

        if (fieldIdentifier != null && companyId != null) {
            try {
                return categoryHome.findByFieldIdentifier(fieldIdentifier, companyId);
            } catch (FinderException e) {
                log.error("Error in find category by fieldIdentifier="+ fieldIdentifier+" , and companyId=" + companyId + ". This should be unique for a category by company", e);
            }
        }
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
