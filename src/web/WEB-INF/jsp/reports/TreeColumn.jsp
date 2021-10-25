<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.jatun.titus.listgenerator.Titus" %>
<%@ page import="com.jatun.titus.listgenerator.config.ConfigurationFactory" %>
<%@ page import="com.jatun.titus.listgenerator.structure.Table" %>
<%@ page import="com.jatun.titus.listgenerator.structure.filter.ResourceBundleWrapper" %>
<%@ page import="com.jatun.titus.listgenerator.util.DynamicColumnParameterUtil" %>
<%@ page import="com.jatun.titus.listgenerator.view.TableTreeView" %>
<%@ page import="com.jatun.titus.listgenerator.view.TableView" %>
<%@ page import="com.jatun.titus.listgenerator.view.XmlTreeView" %>
<%@ page import="com.jatun.titus.listgenerator.view.visitor.AbstractTableViewWalker" %>
<%@ page import="com.jatun.titus.listgenerator.view.visitor.TableTreeViewGenerator" %>
<%@ page import="com.piramide.elwis.web.reports.el.Functions" %>
<%@ page import="javax.servlet.jsp.jstl.core.Config" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>

<link rel="StyleSheet" href='<c:url value="/js/dtree/dtree.css"/>' type="text/css"/>
<tags:jscript language="JavaScript" src="/js/dtree/dtree.jsp"/>
<tags:jscript language="JavaScript" src="/js/st-xmlhttp.js"/>

<!--<textarea id="mt" rows="10" cols="80"></textarea>-->
<textarea id="mt" rows="10" cols="80" style="display:none" class="${app2:getFormInputClasses()}"></textarea>

<%
    String tableName = (String) request.getAttribute("initReportTable");
    String reportModule = (String) request.getAttribute("reportModule");
    //System.out.println("----------------->TableName:"+tableName);

    //dynamic columns parameters
    Map dynamicColumnParameters = Functions.getCategoryDynamicColumnParams(request);
    request.setAttribute("paramDynamicColumn", DynamicColumnParameterUtil.renderParamtersAsString(dynamicColumnParameters));

    TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
//    if (treeView == null) {
    Table table = Titus.getStructureManager(request.getSession().getServletContext()).getTable(tableName);
    ResourceBundleWrapper bundle = ConfigurationFactory.getConfigurationManager().getResourceBundleWrapper();
    bundle.initialize((Locale) Config.get(request.getSession(), Config.FMT_LOCALE));
    AbstractTableViewWalker generator = new TableTreeViewGenerator();
    generator.setResourceBundle(bundle);
    //set dynamic columns parameters
    generator.setParameters(dynamicColumnParameters);
    generator.startProcess(table);

    treeView = new XmlTreeView((TableView) generator.getResult());
    treeView.setInverseRelationLabel(bundle.getMessage(ConfigurationFactory.getConfigurationManager().getInverseResource()));
    treeView.setLoadingLabel(bundle.getMessage(ConfigurationFactory.getConfigurationManager().getLoadingResource()));
    treeView.setModule(reportModule);
    TableTreeView.setInstance(request.getParameter("reportId"), treeView, request.getSession());
//    }

    //constant messages
    request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
    request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
    request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
%>

<app2:jScriptUrl url="/titus.html?op=render&name=${param.reportId}" var="jsLoadTreeUrl" addModuleParams="false"/>

<app2:jScriptUrl
        url="/titus.html?locale=${sessionScope.user.valueMap['locale']}&op=reload&name=${param.reportId}&dynamicParam=${paramDynamicColumn}"
        var="jsReloadTreeUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="jsPath" value="path"/>
    <app2:jScriptUrlParam param="topOpenPaths" value="topOpenPaths"/>
    <app2:jScriptUrlParam param="paths" value="paths"/>
</app2:jScriptUrl>

<app2:jScriptUrl url="${ajaxUrlExecute}?reportId=${param.reportId}&command=filter" var="jsAjaxUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="jsPath" value="nodePath"/>
    <app2:jScriptUrlParam param="date" value="dateTime"/>
</app2:jScriptUrl>

<script type="text/javascript">
    var requestCount = 0;

    function showToolTip(message) {
        var treeDiv = document.getElementById("dtree");
        var msgDiv = document.getElementById("msgId");

        msgDiv.innerHTML = unescape(message);
        msgDiv.style.display = 'inline';
        msgDiv.style.left = treeDiv.offsetLeft + treeDiv.offsetWidth - msgDiv.offsetWidth - 20;
    }

    function hideToolTip() {
        var msgDiv = document.getElementById("msgId");
        requestCount--;
        if (requestCount <= 0) {
            msgDiv.style.display = 'none';
            requestCount = 0;
        } else {
            showToolTip('${LOADING}');
        }
    }

    /*server response error process*/
    function ajaxErrorProcess(requestStatusCode) {
        requestCount--;
        if (requestStatusCode == 404) { //session expired http request status code
            showToolTip('${EXPIRED}');
        } else {
            showToolTip('${ERROR}');
        }
    }

</script>

<div>
    <div class="row">
        <div class="col-xs-8">
            <div style="position:relative;">
                <div id="msgId" class="messageToolTip" style="display:none; position:absolute; top:0px; width:100px;">
                    <fmt:message key="Common.message.loading"/>
                </div>
                <div class="dtree" id="dtree"  style="overflow-x: scroll;overflow-y: scroll; width: 300px; min-width:300px; height:350px;">
                    <script type="text/javascript">

                        var d;
                        var node;

                        function myload() {
                            <%--var url = 'titus.html?op=render&name=${param.reportId}';--%>
                            var url = ${jsLoadTreeUrl};
                            makeHttpRequest(url, 'doit', true, 'ajaxErrorProcess');
                        }

                        function reload(path, id, paths, topOpenPaths) {
                            var url = ${jsReloadTreeUrl};
                            makeHttpRequest(url, 'doit', true, 'ajaxErrorProcess');
                            node = id;
                        }

                        function doit(xmlTree) {
                            var tree = xmlTree.getElementsByTagName('tree');
                            var nodes = tree[0].getElementsByTagName('node');
                            d = new dTree('d');
                            var logDiv = document.getElementById("log");
                            for (j = 0; j < nodes.length; j++) {
                                var id = nodes[j].getAttribute("id");
                                var parentId = nodes[j].getAttribute("parentId");
                                var pos = nodes[j].getAttribute("pos");
                                var label = nodes[j].getAttribute("label");
                                var method = nodes[j].getAttribute("method");
                                if (method == null)
                                    d.add(parseInt(id), parseInt(parentId), pos, label);
                                else
                                    d.add(parseInt(id), parseInt(parentId), pos, label, method);
                            }
                            document.getElementById("dtree").innerHTML = d.toString();
                            if (node > -1) {
                                d.o(node);
                                node = -1;
                            }
                            document.getElementById("dtree").style.overflowX="scroll";
                            document.getElementById("dtree").style.overflowY="scroll";
                        }


                        function doSomething(nodeId) {
                            var nodePath = escape(d.getPath(nodeId));
                            var dateTime = new Date().getTime();

                            //message in tooltip
                            showToolTip('${LOADING}');

                            var url = ${jsAjaxUrl};
                            requestCount++;
                            makeHttpRequest(url, '${ajaxCallJavaScript}', true, 'ajaxErrorProcess');
                        }

                    </script>

                </div>
            </div>
        </div>
        <div class="" style="min-width: 200px">
            <div id="divView"></div>
        </div>
    </div>
</div>
<style>
    .dtree .clip {
        overflow: visible !important;
    }
</style>

