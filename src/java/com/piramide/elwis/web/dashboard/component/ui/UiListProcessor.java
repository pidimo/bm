package com.piramide.elwis.web.dashboard.component.ui;

import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.*;
import com.piramide.elwis.web.dashboard.component.core.UIWrapper;
import com.piramide.elwis.web.dashboard.component.execute.ConditionEvaluator;
import com.piramide.elwis.web.dashboard.component.execute.Executor;
import com.piramide.elwis.web.dashboard.component.ui.velocity.*;
import org.apache.struts.util.ResponseUtils;

import java.util.*;

/**
 * @author : ivan
 */
public class UiListProcessor extends UiProcessor {
    public UiListProcessor(UIWrapper wrapper, Map params, Map dataBaseParameters, int componentId) {
        super(dataBaseParameters, params, wrapper, componentId);
    }


    public StringBuilder buildUIComponent() {

        Map options = new HashMap();
        Component xmlComponent = Builder.i.findComponentById(super.getComponentId());
        processVisibleColumns(xmlComponent);

        String componentName = getComponentName(xmlComponent);

        List<Map> info = super.getWrapper().getProcessColumns();
        String urlAccess = buildUrlAccess(xmlComponent);

        if (!"".equals(urlAccess)) {
            UiUtils utils = new UiUtils(xmlComponent.getAccessColumns(), urlAccess);
            info = buildURLColumns(utils, xmlComponent);
        }

        List<Map> windowActions = buildUrlWindowActions(xmlComponent.getActions());


        String resume = buildNumberResume(super.getParams().get("NUMBER_OF_ELEMENTS").toString(),
                super.getParams().get("NUMBER_OF_SHOWED_ELEMENTS").toString());
        options.put("resumeMsg", resume);
        options.put("messages", super.getWrapper().getTemplateResourceBundle());
        options.put("componentName", componentName);
        options.put("selectedColumns", super.getWrapper().getSelectedColumns());
        options.put("accessColumns", super.getWrapper().getAccessColumns());
        options.put("sizeOfSelectedColumns", super.getWrapper().getSelectedColumns().size());
        options.put("actions", xmlComponent.getActions());
        options.put("xmlComponentId", super.getComponentId());
        options.put("windowActions", windowActions);
        options.put("isDashboardBootstrapUI", Functions.isBootstrapUIMode(getWrapper().getHttpServletRequest()));
        options.putAll(super.getParams());


        String className = Executor.getUIProcessorClassName(super.getComponentId());
        StringBuilder componentUI;
        if (null != className) {
            TemplateFactory factory = new CustomTemplateFactory(className);
            Template customTemplate = factory.getTemplate();
            componentUI = customTemplate.merge(super.getWrapper().getProcessColumns(), options);
        } else {
            String uiIUProcesorType = Executor.getUIProcessorType(super.getComponentId());
            TemplateFactory factory = new ListTemplateFactory();
            Template listTemplate = factory.getTemplate();
            componentUI = listTemplate.merge(info, options);
        }
        return componentUI;
    }


    private void processVisibleColumns(Component xmlComponent) {
        Map<String, Converter> convertersMap = new HashMap<String, Converter>();

        for (Column c : super.getWrapper().getSelectedColumns()) {
            if (null != c.getConverter()) {
                String conv = c.getConverter();

                com.piramide.elwis.web.dashboard.component.configuration.structure.Converter confConverter;
                confConverter = xmlComponent.getDashBoardConfiguration().getConverter(conv);
                String clazz = confConverter.getClazz();
                Converter converter = Executor.getConverter(clazz);

                convertersMap.put(c.getConverter(), converter);
            }
        }


        int i = 0;
        for (Map m : super.getWrapper().getProcessColumns()) {
            for (Column c : super.getWrapper().getSelectedColumns()) {
                String v = m.get(c.getName()).toString();
                String fv = v;
                Object obj = v;
                if (null != v && !"".equals(v)) {
                    if (null != c.getConverter()) {
                        Converter converter = convertersMap.get(c.getConverter());
                        Map aCm = super.getWrapper().getAccessColumns().get(i);
                        Map my = new HashMap();
                        my.putAll(m);
                        my.putAll(aCm);

                        obj = converter.convert(v, super.getWrapper().getConverterParams(),
                                super.getWrapper().getTemplateResourceBundle(), my, xmlComponent);
                        fv = obj.toString();
                    } else {
                        if (null != c.getPatternKey()) {
                            fv = super.getWrapper().getTemplateResourceBundle().formatColumn(fv,
                                    super.getWrapper().getTemplateResourceBundle().getMessage(c.getPatternKey()));
                        } else {
                            if (!c.getConstants().isEmpty()) {
                                Constant cons = c.getConstant(v);
                                fv =
                                        (null != cons ?
                                                super.getWrapper().getTemplateResourceBundle().getMessage(cons.getResourceKey()) :
                                                v + " not defined in xml");
                            }
                        }
                    }
                }
                m.put(c.getName(), fv);
            }
            i++;
        }
    }

    private String buildNumberResume
            (String
                    all_values, String
                    showed_values) {
        List<String> s = new ArrayList<String>();
        s.add(all_values);
        s.add(showed_values);
        String m = super.getWrapper().getTemplateResourceBundle().getMessage("dashboard.resume", s.toArray());
        return " " + m;
    }

    private String getComponentName
            (Component
                    xmlComponent) {
        String s;
        s = xmlComponent.getName();
        if (null != xmlComponent.getResourceKey() && !"".equals(xmlComponent.getResourceKey())) {
            s = xmlComponent.getResourceKey();
        }
        return s;
    }


    private String buildUrlAccess
            (Component
                    xmlComponent) {

        Option accessUrl = xmlComponent.getComponentConfiguration().getAccessUrl();
        if (null == accessUrl) {
            return "";
        }

        String context = super.getWrapper().getHttpServletRequest().getContextPath();

        String str = accessUrl.getValue();
        String prms = "";
        List<Parameter> urlParams = accessUrl.getParams();

        if (null != urlParams) {
            for (Parameter p : urlParams) {
                if (super.getWrapper().getElParams().containsKey(p.getName())) {
                    Object v = super.getWrapper().getElParams().get(p.getName());
                    prms += p.getName() + "=" + v.toString();
                } else if (null != p.getColumnId() && !"".equals(p.getColumnId())) {
                    prms += p.getName() + "=#" + p.getColumnId();
                } else {
                    prms += p.getName() + "=" + p.getValue();
                }

                if (urlParams.indexOf(p) < (urlParams.size() - 1)) {
                    prms += "&";
                }
            }
        }
        if (!"".equals(prms)) {
            str += "?" + prms;
        }
        return context + str;
    }

    private List<Map> buildURLColumns
            (UiUtils
                    utils, Component component) {

        List<Map> r = new ArrayList<Map>();
        int colIdx = 0;


        for (Map vc : super.getWrapper().getProcessColumns()) {
            Map acc = super.getWrapper().getAccessColumns().get(colIdx);

            String nbps = "&nbsp;";
            String personalStyle = formatColumn(acc, component);

            int urlIdx = 0;
            LinkedHashMap urlMap = new LinkedHashMap();
            for (Column sc : super.getWrapper().getSelectedColumns()) {
                String str = utils.buildURL(acc);
                String val = (String) vc.get(sc.getName());

                String align = " align=\"left\" ";
                if (isNumber(val)) {
                    align = " align=\"right\" ";
                }

                if (urlIdx == 0) {
                    String cad = "<a href=\"" + super.getWrapper().getHttpServletResponse().encodeURL(str) + "\" title=\"" + ResponseUtils.filter(val) + "\">" +
                            "<div id=\"componentElement\" " + align + " > \n" +
                            ResponseUtils.filter(val) +
                            "\n</div>\n" +
                            "</a>\n";
                    urlMap.put(sc.getName(), cad);
                } else {
                    String s = "<div id=\"componentElement\" " + align + " title=\"" + ResponseUtils.filter(val) + "\" >\n" +
                            ResponseUtils.filter(val) +
                            "\n</div>\n";
                    if (null == val || "".equals(val)) {
                        val = nbps;
                        s = val;
                    }
                    urlMap.put(sc.getName(), s);
                }
                urlMap.put("dbstyle#3175", personalStyle);

                urlIdx++;
            }

            r.add(urlMap);
            colIdx++;
        }
        return r;
    }

    private String formatColumn(Map accessColumnsMap, Component component) {
        List<Column> accessColumnsObj = super.getWrapper().getAccessColumnsObjects();
        String style = "";
        for (Column accessColumnObj : accessColumnsObj) {
            if (!accessColumnObj.getConditions().isEmpty()) {
                ConditionEvaluator evaluator = Executor.getConditionEvaluator(component);
                style = evaluator.evaluate(accessColumnsMap, accessColumnObj.getConditions(), component.getName());
            }
        }
        return style;
    }

    private boolean isNumber(String str) {
        char[] aux;
        boolean result = true;
        if (null != str) {
            aux = str.toCharArray();

            for (int i = 0; i < aux.length; i++) {
                char c = aux[i];

                if (!Character.isDigit(c)) {
                    if ('.' != c && ',' != c && '%' != c &&
                            ' ' != c) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
