<%@ page import="com.jatun.titus.listgenerator.view.TableTreeView"%>
<%@ include file="/Includes.jsp" %>
<script type="text/javascript" language="JavaScript">

function jump(obj)
{
    tableIni = lib_getObj('text1').value;
    cad = obj + '&iniTableParam=' + tableIni;    //add in parameter of the form
    //alert(cad);
    window.location=cad;
}
function key(id, event){
    if(event.keyCode == 13) {
        var link = document.getElementById(id);
        tableIni = lib_getObj('text1').value;
        cad = link + '&iniTableParam=' + tableIni;    //add in parameter of the form
        window.location=cad;
    }
}
</script>
<%
    if (request.getParameter("reportId") == null) {
        request.getParameterMap().put("reportId","1234567");
    }

    TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
    if (treeView != null) {
        String treeViewFunctionality = treeView.getTreeModel().getName();
        if (!treeViewFunctionality.equals(request.getParameter("iniTableParam"))) {
            TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
        }
    }
%>

Functionality:<input type="text" name="text1" id="text1" onkeypress="javascript:key('linkId', event);"/>
<app:link action="Report/FilterTest.do" onclick="javascript:jump(this);return false;" styleId="linkId">show tree</app:link>

<c:choose>
    <c:when test="${not empty param['iniTableParam']}">
        <c:set var="initReportTable" value="${param['iniTableParam']}" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="initReportTable" value="address" scope="request"/>
    </c:otherwise>
</c:choose>

<c:import url="/common/reports/ReportFilter.jsp"/>
