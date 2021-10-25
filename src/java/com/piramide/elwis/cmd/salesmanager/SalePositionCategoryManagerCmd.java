package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * Sale position category cmd
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.3
 */
public class SalePositionCategoryManagerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SalePositionCategoryManagerCmd... " + paramDTO);

        String op = this.getOp();
        if ("readFieldValues".equals(op)) {
            readCategoryValues(ctx);
        }
        if ("create".equals(op)) {
            create(ctx);
        }
        if ("update".equals(op)) {
            update(ctx);
        }
        if ("delete".equals(op)) {
            delete(ctx);
        }
    }

    private void readCategoryValues(SessionContext ctx) {
        Integer salePositionId = new Integer(paramDTO.get("salePositionId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        String finderName = "findBySalePositionId";
        Object[] params = new Object[]{salePositionId, companyId};
        List paramsAsList = Arrays.asList(params);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("readCAtegoryFieldValues");
        cmd.executeInStateless(ctx);
        ResultDTO myResultDTO = cmd.getResultDTO();
        resultDTO.putAll(myResultDTO);
    }

    private void create(SessionContext ctx) {
        Integer salePositionId = new Integer(paramDTO.get("salePositionId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        List<Map> sourceValues = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("identifier", "salePositionId");
        map.put("value", salePositionId);
        sourceValues.add(map);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam(paramDTO);
        cmd.putParam("companyId", companyId);
        cmd.setOp("createValues");
        cmd.executeInStateless(ctx);
    }

    private void update(SessionContext ctx) {
        Integer salePositionId = new Integer(paramDTO.get("salePositionId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        String finderName = "findBySalePositionId";
        Object[] params = new Object[]{salePositionId, companyId};
        List paramsAsList = Arrays.asList(params);

        List<Map> sourceValues = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("identifier", "salePositionId");
        map.put("value", salePositionId);
        sourceValues.add(map);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("sourceValues", sourceValues);
        cmd.putParam("companyId", companyId);
        cmd.putParam(paramDTO);
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("updateValues");
        cmd.executeInStateless(ctx);

        readCategoryValues(ctx);
    }

    private void delete(SessionContext ctx) {
        Integer salePositionId = new Integer(paramDTO.get("salePositionId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        String finderName = "findBySalePositionId";
        Object[] params = new Object[]{salePositionId, companyId};
        List paramsAsList = Arrays.asList(params);

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", finderName);
        cmd.putParam("params", paramsAsList);
        cmd.setOp("deleteValues");
        cmd.executeInStateless(ctx);
    }

    public boolean isStateful() {
        return false;
    }
}
