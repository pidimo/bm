package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.EJBLocalObject;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class CategoryTabManagerCmd extends EJBCommand {

    @Override
    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        if ("update".equals(getOp())) {
            isRead = false;
            update(ctx);
        }

        if (isRead) {
            read(ctx);
        }
    }

    @SuppressWarnings(value = "unchecked")
    protected void read(SessionContext ctx) {
        EJBLocalObject entity = findEJBLocalObject(ctx);

        if (null != entity && isValidTab(entity, ctx)) {

            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("finderName", getEntityFinderMethodName());
            cmd.putParam("params", Arrays.asList(getEntityId(entity, ctx), getCompanyId(entity, ctx)));
            cmd.setOp("readCAtegoryFieldValues");
            cmd.executeInStateless(ctx);

            ResultDTO myResultDTO = cmd.getResultDTO();
            resultDTO.putAll(myResultDTO);
        }
    }

    @SuppressWarnings(value = "unchecked")
    protected void update(SessionContext ctx) {
        EJBLocalObject entity = findEJBLocalObject(ctx);

        if (null != entity && isValidTab(entity, ctx)) {
            List<Map> sourceValues = new ArrayList<Map>();
            sourceValues.add(getBasicConfigurationMap(entity, ctx));

            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("sourceValues", sourceValues);
            cmd.putParam("companyId", getCompanyId(entity, ctx));
            cmd.putParam(paramDTO);
            cmd.putParam("finderName", getEntityFinderMethodName());
            cmd.putParam("params", Arrays.asList(getEntityId(entity, ctx), getCompanyId(entity, ctx)));
            cmd.setOp("updateValues");

            cmd.executeInStateless(ctx);

            resultDTO.put("op", "read");

            if ("Fail".equals(cmd.getResultDTO().getForward())) {
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("Common.error.concurrency");
                return;
            }

            resultDTO.addResultMessage("Common.changesOK");
        }
    }

    protected boolean isValidTab(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        CategoryTabCmd categoryTabCmd = new CategoryTabCmd();
        categoryTabCmd.setOp("checkCategoryTab");
        categoryTabCmd.putParam("finderName", getValuesFinderMethodName());
        categoryTabCmd.putParam("params", new Object[]{getEntityId(ejbLocalObject, ctx)});
        categoryTabCmd.putParam("categoryTabId", EJBCommandUtil.i.getValueAsInteger(this, "categoryTabId"));
        categoryTabCmd.executeInStateless(ctx);

        if (null != categoryTabCmd.getResultDTO().get("errorMessage")) {
            resultDTO.addResultMessage(categoryTabCmd.getResultDTO().get("errorMessage").toString());
            setTabValidationFailureForward(ejbLocalObject);
            return false;
        }

        return true;
    }

    protected EJBLocalObject findEJBLocalObject(SessionContext ctx) {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    protected String getValuesFinderMethodName() {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    protected String getEntityFinderMethodName() {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    protected Integer getEntityId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    protected Integer getCompanyId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    protected String getEntityIdFieldName() {
        throw new UnsupportedOperationException("This method should be overwritten in the child class.");
    }

    @SuppressWarnings(value = "unchecked")
    protected Map getBasicConfigurationMap(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        Map configurationMap = new HashMap();
        configurationMap.put("identifier", getEntityIdFieldName());
        configurationMap.put("value", getEntityId(ejbLocalObject, ctx));

        return configurationMap;
    }

    protected void setTabValidationFailureForward(EJBLocalObject ejbLocalObject) {
        resultDTO.setForward("Detail");
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
