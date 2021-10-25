package com.piramide.elwis.web.reports.form;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.dynamiccolumn.DynamicColumnFieldNotFoundException;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.reports.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: ColumnForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ColumnForm extends DefaultForm {
    public void setListColumns(Object[] obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            String o = (String) obj[i];
            list.add(o);
        }
        this.setDto("listColumns", list);
    }

    public Object[] getListColumns() {
        List list = (List) getDto("listColumns");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public void setPreviousColumns(Object[] obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            String o = (String) obj[i];
            list.add(o);
        }
        this.setDto("previousColumns", list);
    }

    public Object[] getPreviousColumns() {
        List list = (List) getDto("previousColumns");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ColumnForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        //validate functionality init table
        LightlyReportCmd cmd = new LightlyReportCmd();
        cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
        cmd.setOp("read");

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            log.debug("Error in execute LightlyReportCmd....");
        }
        if (resultDTO != null && resultDTO.containsKey("initialTableReference")) {

            TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
            if (treeView != null) {

                String module = (String) resultDTO.get("module");
                String functionality = (String) resultDTO.get("initialTableReference");

                String treeViewFunctionality = treeView.getTreeModel().getName();
                log.debug("FUNCT::" + functionality);
                log.debug("MOD::" + module);
                log.debug("TREEEEEEE FUNCT:" + treeViewFunctionality);
                log.debug("TREEEEEEE MOD:" + treeView.getModule());
                if (!module.equals(treeView.getModule()) || !functionality.equals(treeViewFunctionality)) {
                    errors.add("init", new ActionError("Common.error.concurrency"));
                    //remove instance to update the tree
                    TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
                }
            }
        }

        if (getDto("listColumns") == null) {
            setDto("listColumns", new ArrayList());
        }
        if (getDto("previousColumns") == null) {
            setDto("previousColumns", new ArrayList());
        }

        if (errors.isEmpty()) {
            errors = processDynamicColumns(request);
            if (!errors.isEmpty()) {
                //remove instance to update the tree
                TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
            }
        }

        return errors;
    }

    /**
     * process selected dynamic columns
     *
     * @param request
     * @return ActionErrors
     */
    private ActionErrors processDynamicColumns(HttpServletRequest request) {
        List columnSelectedList = (List) getDto("listColumns");
        ActionErrors errors = new ActionErrors();

        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        Map parameters = Functions.getCategoryDynamicColumnParams(request);
        boolean hasDynamicColumns = false;
        for (int i = 0; i < columnSelectedList.size(); i++) {
            String text = (String) columnSelectedList.get(i);
            if (text.trim().length() > 0) {
                String path = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_PATH);

                if (path != null && TitusPathUtil.isDynamicColumn(path)) {
                    hasDynamicColumns = true;
                    if (columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_CATEGORYID) == null) {
                        text = concatData(text, TitusPathUtil.getDynamicColumnId(path), ReportConstants.KEY_COLUMNSEPARATOR_CATEGORYID);
                    }

                    try {
                        Map tableResourceFieldLabel = TitusPathUtil.getResourceTableAndFieldLabelDynamicColumn(path, structureManager, parameters);

                        String label = columnTextParser(text, ReportConstants.KEY_COLUMNSEPARATOR_LABEL);
                        if (label == null || label.trim().length() == 0) {
                            text = concatData(text, (String) tableResourceFieldLabel.get(TitusPathUtil.FIELDLABEL), ReportConstants.KEY_COLUMNSEPARATOR_LABEL);
                        }
                    } catch (DynamicColumnFieldNotFoundException e) {
                        log.debug("Not found selected dynamic field.." + e);
                        errors.add("field", new ActionError("Report.columns.dynamicColumn.error.deleted"));
                        return errors;
                    }
                    columnSelectedList.set(i, text);
                }
            }
        }
        if (hasDynamicColumns) {
            setDto("listColumns", columnSelectedList);
        }
        return errors;
    }

    /**
     * parser data of column, get substring that be between the parserId
     *
     * @param text     text to parser
     * @param parserId key to parser
     * @return String column data
     */
    private String columnTextParser(String text, String parserId) {
        String res = null;
        int firstIndex = text.indexOf(parserId);
        int lastIindex = text.lastIndexOf(parserId);
        if (firstIndex != lastIindex) {
            res = text.substring((firstIndex + parserId.length()), lastIindex).trim();
        }

        return res;
    }

    private String concatData(String text, String value, String sepatarator) {
        return (text + sepatarator + value + sepatarator);
    }

}
