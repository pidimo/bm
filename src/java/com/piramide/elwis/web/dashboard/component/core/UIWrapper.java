package com.piramide.elwis.web.dashboard.component.core;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class UIWrapper extends AbstractWrapper {
    public UIWrapper(List<Column> columns,
                     List<Map> accessColumns,
                     List<Map> processColumns,
                     List<Column> accessColumnsObjects,
                     ResourceBundleManager resources, Map elParams, Map converterParams,
                     HttpServletRequest request,
                     HttpServletResponse response) {

        super.put("selectedColumns", columns);
        super.put("accessColumns", accessColumns);
        super.put("processColumns", processColumns);
        super.put("ResourceBundleManager", resources);
        super.put("elParams", elParams);
        super.put("httpServletRequest", request);
        super.put("httpServletResponse", response);
        super.put("converterParams", converterParams);
        super.put("accessColumnsObjects", accessColumnsObjects);

    }

    public UIWrapper() {
    }

    public void setSelectedColumns(List<Column> columns) {
        super.put("selectedColumns", columns);
    }

    public List<Column> getSelectedColumns() {
        return (List<Column>) super.get("selectedColumns");
    }

    public void setAccessColumns(List<Map> accessColumns) {
        super.put("accessColumns", accessColumns);
    }

    public List<Map> getAccessColumns() {
        return (List<Map>) super.get("accessColumns");
    }

    public void setProcessColumns(List<Map> processColumns) {
        super.put("processColumns", processColumns);
    }

    public List<Map> getProcessColumns() {
        return (List<Map>) super.get("processColumns");
    }

    public void setTemplateResourcesBundle(ResourceBundleManager resources) {
        super.put("ResourceBundleManager", resources);
    }

    public ResourceBundleManager getTemplateResourceBundle() {
        return (ResourceBundleManager) super.get("ResourceBundleManager");
    }

    public void setElParams(Map elParams) {
        super.put("elParams", elParams);
    }

    public Map getElParams() {
        return (Map) super.get("elParams");
    }

    public void setHttpServletRequest(HttpServletRequest request) {
        super.put("httpServletRequest", request);
    }

    public HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) super.get("httpServletRequest");
    }

    public void setHttpServletResponse(HttpServletResponse response) {
        super.put("httpServletResponse", response);
    }

    public HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) super.get("httpServletResponse");
    }

    public Map getConverterParams() {
        return (Map) super.get("converterParams");
    }

    public void setConverterParams(Map converterParams) {
        super.put("converterParams", converterParams);
    }

    public void setAccessColumnsObjects(List<Column> accessColumns) {
        super.put("accessColumnsObjects", accessColumns);
    }

    public List<Column> getAccessColumnsObjects() {
        return (List<Column>) super.get("accessColumnsObjects");
    }
}
