<%@ page import="com.jatun.common.web.JavaScriptEncoder" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.reader.Builder" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.structure.Component" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.web.util.WebUtils" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>

<style type="text/css">
    TABLE.dashboard_table {
        table-layout: fixed;
    }

    TD.dashboard_td {
        overflow: hidden;
        white-space: nowrap;
    }

    TD.message {
        font-family: Verdana, Helvetica, sans-serif;
        font-size: 11px;
        vertical-align: middle;
        padding: 3px 4px 3px 4px;
        height: 25px;
        text-align: left;
        border: #4646A3 1px solid;
        background-color: #FFFFCC;
        color: #000000;
    }
</style>
<%
    //constant messages
    request.setAttribute("EXPIRED", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.sessionExpired")));
    request.setAttribute("ERROR", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "error.tooltip.unexpected")));
    request.setAttribute("LOADING", JavaScriptEncoder.encode(JSPHelper.getMessage(request, "Common.message.loading")));
    String errorMSG = "<table><tr><td class=\"message\"> " + JSPHelper.getMessage(request, "error.tooltip.unexpected") + "</td></tr></table>";
    String expiredMSG = "<table><tr><td class=\"message\"> " + JSPHelper.getMessage(request, "Common.sessionExpired") + "</td></tr></table>";
    String loadingMSG = "<table><tr><td class=\"message\"> " + JSPHelper.getMessage(request, "Common.message.loading") + "</td></tr></table>";
    request.setAttribute("errorMSG", errorMSG);
    request.setAttribute("expiredMSG", expiredMSG);
    request.setAttribute("loadMSG", loadingMSG);
%>


<c:set var="contextPath" value="<%=request.getContextPath()%>"/>

<c:set var="counter" value="${0}"/>
<c:forEach var="component" items="${containderStructure}" varStatus="i">
    <c:set var="xmlCId" value="${component.xmlComponentId}" scope="request"/>
    <%
        Integer id = (Integer) request.getAttribute("xmlCId");
        Component c = Builder.i.findComponentById(id.intValue());

        String p = c.getPermission();
        String f = c.getFunctionality();

        request.setAttribute("functionality", f);
        request.setAttribute("permission", p);


        Boolean isUrlComponent = Boolean.valueOf(false);
        String url = "";
        if (null != c.getComponentConfiguration().getComponentUrl()) {
            isUrlComponent = Boolean.valueOf(true);

            Map elParameters = WebUtils.evaluateELParametersOfUrls(pageContext, c.getComponentConfiguration().getComponentUrl().getParams());
            url = WebUtils.buildUrlAccess(c.getComponentConfiguration().getComponentUrl(), elParameters, (HttpServletRequest) pageContext.getRequest(), false);
        }

        Date d = new Date();
        url = url + "&time=" + d.getTime();

        request.setAttribute("isUrlComponent", isUrlComponent);
        request.setAttribute("url", url);
        request.setAttribute("time", String.valueOf(d.getTime()));
    %>

    <app2:checkAccessRight functionality="${functionality}" permission="${permission}" var="have_permissions"/>
    <html:hidden property="xmlComponentId" value="${component.xmlComponentId}" styleId="xmlComponent_${i.count}"/>
    <html:hidden property="dbComponent" value="${component.componentId}" styleId="dbComponent_${i.count}"/>
    <html:hidden property="permissions" value="${have_permissions}" styleId="permissions_${i.count}"/>
    <html:hidden property="isComponentUrl" value="${isUrlComponent}"
                 styleId="isComponentUrl_${component.xmlComponentId}_${component.componentId}"/>
    <app:url value="${url}" var="encodedURL" contextRelative="false"/>
    <app:url
            value="/Dashboard/Container/DrawComponent.do?componentId=${component.xmlComponentId}&dbComponentId=${component.componentId}&dashboardContainerId=${dashboardContainerId}&time=${time}"
            var="encodedDUrl" contextRelative="false"/>
    <html:hidden property="url" value="${encodedURL}"
                 styleId="url_${component.xmlComponentId}_${component.componentId}"/>
    <html:hidden property="dUrl" value="${encodedDUrl}"
                 styleId="dUrl_${component.xmlComponentId}_${component.componentId}"/>
    <c:set var="counter" value="${i.count}"/>
</c:forEach>

<app:url value="/Dashboard/Container/DrawComponent.do" var="drawURL"/>
<script type="text/javascript">
    function ajaxErrorProcess(requestStatusCode, xmlComponentId, dbComponentId) {
        var divElement = document.getElementById('component_' + xmlComponentId + '_' + dbComponentId);

        if (requestStatusCode == 404) { //session expired http request status code
            divElement.innerHTML = '${expiredMSG}';
        } else {
            divElement.innerHTML = '${errorMSG}';
        }
    }
    function makeHttpRequest(url, callback_function, return_xml, errorprocess_function, xmlComponentId, dbComponentId, otherText) {

        var http_request = false;

        if (window.XMLHttpRequest) { // Mozilla, Safari,...
            http_request = new XMLHttpRequest();
            if (http_request.overrideMimeType) {
                http_request.overrideMimeType('text/xml');
            }
        } else if (window.ActiveXObject) { // IE
            try {
                http_request = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    http_request = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e) {
                }
            }
        }

        if (!http_request) {
            alert('Unfortunatelly you browser doesn\'t support this feature.');
            return false;
        }
        http_request.onreadystatechange = function () {
            if (http_request.readyState == 4) {
                if (http_request.status == 200) {
                    if (return_xml) {
                        eval(callback_function + '(http_request.responseXML)');
                    } else {
                        eval(callback_function + '(http_request.responseText, xmlComponentId, dbComponentId, otherText)');
                    }
                } else {
                    if (errorprocess_function != null && errorprocess_function != undefined) {
                        eval(errorprocess_function + '(http_request.status, xmlComponentId, dbComponentId)');
                    } else {
                        alert('There was a problem with the request.(Code: ' + http_request.status + ')');
                    }
                }
            }
        }
        http_request.open('GET', url, true);
        http_request.setRequestHeader("isAjaxRequest", "true");
        http_request.send(null);
    }


    function errors(requestStatusCode) {
        alert('ha invocado a errors ');
    }

    function concatText(text, xmlComponentId, dbComponentId, otherText) {
        var divElement = document.getElementById('component_' + xmlComponentId + '_' + dbComponentId);
        divElement.innerHTML = concatInPanelBootstrap(otherText,text)
    }
    function concatInPanelBootstrap(otherText,text){
        return '<div>' +
                '<div class="well well-sm well-sm-custom">'+
                    otherText +
                '</div>' +
                    text+
                '</div>';
    }

    function addHTMLtoDiv(text, xmlComponentId, dbComponentId) {
        var divElement = document.getElementById('component_' + xmlComponentId + '_' + dbComponentId);
        var isComponentUrl = document.getElementById('isComponentUrl_' + xmlComponentId + '_' + dbComponentId);

        var d = new Date();
        var time = d.getTime();
        if ('true' == isComponentUrl.value) {
            var url = document.getElementById('url_' + xmlComponentId + '_' + dbComponentId);

            var urlTxt = url.value;

            makeHttpRequest(urlTxt, 'concatText', false, null, xmlComponentId, dbComponentId, text);
        } else {
            divElement.innerHTML = text;
        }
    }
    function read() {
        var d = new Date();
        var time = d.getTime();
        for (var i = 1; i <= ${counter}; i++) {
            var xmlComponentId = document.getElementById("xmlComponent_" + i).value;
            var dbComponentId = document.getElementById("dbComponent_" + i).value;
            var permissions = document.getElementById("permissions_" + i).value;

            var url = document.getElementById('dUrl_' + xmlComponentId + '_' + dbComponentId);
            var urlTxt = url.value;
            if (permissions == "true") {
                makeHttpRequest(urlTxt, 'addHTMLtoDiv', false, 'ajaxErrorProcess', xmlComponentId, dbComponentId, null);
            }
        }
    }


</script>
<%--<div class="dtree" style="width:950px;" align="center">--%>
<div class="row">
    <div class="col-xs-12 col-sm-6">
        <div class="dtree">
            <c:forEach var="component" items="${left}">
                <c:set var="xmlCIdf" value="${component.xmlComponentId}" scope="request"/>

                <%
                    Integer id = (Integer) request.getAttribute("xmlCIdf");
                    Component c = Builder.i.findComponentById(id.intValue());

                    String p = c.getPermission();
                    String f = c.getFunctionality();

                    request.setAttribute("functionality", f);
                    request.setAttribute("permission", p);
                %>
                <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                    <div id="component_${component.xmlComponentId}_${component.componentId}"
                         style="width:100%; overflow-x:auto;">
                            ${loadMSG}
                    </div>
                    <br/>
                </app2:checkAccessRight>
            </c:forEach>
        </div>
    </div>
    <div class="col-xs-12 col-sm-6">
        <div class="dtree">
            <c:forEach var="component" items="${right}">
                <c:set var="xmlCIdr" value="${component.xmlComponentId}" scope="request"/>

                <%
                    Integer id = (Integer) request.getAttribute("xmlCIdr");

                    Component c = Builder.i.findComponentById(id.intValue());
                    String p = c.getPermission();
                    String f = c.getFunctionality();
                    request.setAttribute("functionality", f);
                    request.setAttribute("permission", p);
                %>
                <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                    <div id="component_${component.xmlComponentId}_${component.componentId}"
                         style="width:100%; overflow-x:auto;">
                            ${loadMSG}
                    </div>
                    <br/>
                </app2:checkAccessRight>
            </c:forEach>
        </div>
    </div>
</div>
<div style="padding-top:5px">
    @@VERSION@@
</div>