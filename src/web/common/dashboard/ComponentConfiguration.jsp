<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.reader.Builder" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.structure.Component" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.util.Constant" %>
<%@ page import="com.piramide.elwis.web.dashboard.component.web.util.WebUtils" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>


<c:set var="type_boolean"><%=Constant.TYPE_BOOLEAN%>
</c:set>
<c:set var="type_date"><%=Constant.TYPE_DATE%>
</c:set>
<c:set var="type_integer"><%=Constant.TYPE_INTEGER%>
</c:set>
<c:set var="type_string"><%=Constant.TYPE_STRING%>
</c:set>


<c:set var="view_check"><%=Constant.VIEW_CHECK%>
</c:set>
<c:set var="view_range"><%=Constant.VIEW_RANGE%>
</c:set>
<c:set var="view_select"><%=Constant.VIEW_SELECT%>
</c:set>
<c:set var="view_text"><%=Constant.VIEW_TEXT%>
</c:set>

<c:set var="order_asc"><%=Constant.ORDER_ASC%>
</c:set>
<c:set var="order_desc"><%=Constant.ORDER_DESC%>
</c:set>

<fmt:message key="Common.order.asc" var="asc_key"/>
<fmt:message key="Common.order.desc" var="desc_key"/>

<%
    int componentId = new Integer(request.getParameter("componentId")).intValue();
    Component xmlComponent = Builder.i.findComponentById(componentId);

    request.setAttribute("componentColumns", xmlComponent.getVisibleColumns());
    request.setAttribute("componentName", xmlComponent.getResourceKey());

    Map selectParams = new HashMap();
    selectParams.put("request", (HttpServletRequest) pageContext.getRequest());
    selectParams.put("servletContext", pageContext.getServletConfig().getServletContext());
%>

<script language="JavaScript">
function selectColumn(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);

    var selected_index = sourceBox.selectedIndex;
    if (selected_index != -1) {
        var opt = new Option();
        opt.value = sourceBox.options[selected_index].value;
        opt.text = sourceBox.options[selected_index].text;
        targetBox.options[targetBox.options.length] = opt;
        sourceBox.remove(selected_index);
    }
}

function moveUp(boxId) {
    var box = document.getElementById(boxId);
    var selected_index = box.selectedIndex;
    if (selected_index != -1) {
        if (selected_index != 0) {
            var x = box.options[selected_index];
            var y = box.options[selected_index - 1];
            var opt = new Option();
            opt.value = y.value;
            opt.text = y.text;
            y.value = x.value;
            y.text = x.text;
            x.value = opt.value;
            x.text = opt.text;
            box.options[selected_index - 1].selected = true;
            box.options[selected_index].selected = false;
        }
    }
}

function moveDown(boxId) {
    var box = document.getElementById(boxId);
    var selected_index = box.selectedIndex;
    if (selected_index != -1) {
        if (selected_index < box.options.length - 1) {
            var x = box.options[selected_index];
            var y = box.options[selected_index + 1];
            var opt = new Option();
            opt.value = y.value;
            opt.text = y.text;
            y.value = x.value;
            y.text = x.text;
            x.value = opt.value;
            x.text = opt.text;
            box.options[selected_index + 1].selected = true;
            box.options[selected_index].selected = false;
        }
    }
}

function selectAll() {
    var targetBox = document.getElementById("selectedColumnsId");
    var sourceBox = document.getElementById("availableColumnsId");
    for (var i = 0; i < sourceBox.options.length; i++) {
        sourceBox.options[i].selected = true;
    }

    for (var i = 0; i < targetBox.options.length; i++) {
        targetBox.options[i].selected = true;
    }
}

function addOrder(obj) {
    var idx = obj.selectedIndex;

    var asc = '${asc_key}';
    var desc = '${desc_key}';
    var empty = '[ ]';

    if (-1 != idx) {
        var value = obj.options[idx].value;
        var text = obj.options[idx].text;
        var order = document.getElementById('columnOrder_' + value);
        var name = document.getElementById('columnName_' + value);
        if (order.value != '') {
            document.getElementById("selectOrder").style.display = "";
            document.getElementById("selectedColumn").value = name.value;

            if (text.indexOf(asc) > -1) {
                document.getElementById("selectOrdId").value = 'ASC';
            }
            if (text.indexOf(desc) > -1) {
                document.getElementById("selectOrdId").value = 'DESC';
            }
            if (text.indexOf(empty) > -1) {
                document.getElementById("selectOrdId").value = '';
            }
            if (text.indexOf('[]') > -1) {
                document.getElementById("selectOrdId").value = '';
            }
        } else
            document.getElementById("selectOrder").style.display = "none";
    }
}
function selectOrder(obj) {
    var idx = obj.selectedIndex;
    var opt = obj.options[idx];
    var columnName = document.getElementById("selectedColumn").value;

    document.getElementById(columnName).value = opt.value;

    var selectColumnsBox = document.getElementById("selectedColumnsId");
    var idxS = selectColumnsBox.selectedIndex;
    var name = selectColumnsBox.options[idxS].text;

    var asc = '${asc_key}';
    var desc = '${desc_key}';
    var empty = '[ ]';

    if (name.indexOf(asc) > -1) {
        name = name.replace(asc, opt.text);
    }
    if (name.indexOf(desc) > -1) {
        name = name.replace(desc, opt.text);
    }
    if (name.indexOf(empty) > -1) {
        var r = opt.text;
        var replacement = '[' + r + ']';
        name = name.replace(empty, replacement);
    }
    if (name.indexOf('[]') > -1) {
        name = name.replace('[]', '[ ]');
    }
    selectColumnsBox.options[idxS].text = name;
}

function submitPreProcess() {
    selectAll();

    <c:if test="${not empty preProcessSubmitJs}">
        ${preProcessSubmitJs}
    </c:if>
}
</script>

<c:if test="${empty componentAction}">
    <c:set var="componentAction" value="/Dashboard/Component/Configuration/save" scope="request"/>
</c:if>

<html:form
        action="${componentAction}.do?componentId=${param.componentId}&dbComponentId=${param.dbComponentId}">
<html:hidden property="dto(componentId)" value="${param.componentId}"/>
<html:hidden property="dto(dbComponentId)" value="${param.dbComponentId}"/>

<c:forEach var="col" items="${componentColumns}" varStatus="i">
    <html:hidden property="dto(columnORD_${i.count})" value="${col.order}" styleId="columnOrder_${col.id}"/>
    <html:hidden property="dto(columnNAME_${i.count})" value="${col.name}" styleId="columnName_${col.id}"/>
</c:forEach>
<html:hidden property="dto(selectedColumn)" styleId="selectedColumn"/>

<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" class="contaner">
<tr>
<td>
<table width="50%" border="0" cellpadding="0" cellspacing="0" align="center">
<tr>
    <td colspan="4" class="title">
        <fmt:message key="${componentName}"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="label">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td colspan="2" class="label">
                    <fmt:message key="dashboard.columns.available"/>
                </td>
                <td colspan="2" class="label">
                    <fmt:message key="dashboard.columns.selected"/>
                </td>
            </tr>
            <tr>
                <td align="left">
                    <html:select property="availableColumns" multiple="true" size="5" styleId="availableColumnsId"
                                 styleClass="multipleSelect">
                        <c:forEach var="ac" items="${availableColumns}">
                            <html:option value="${ac.id}">
                                <fmt:message key="${ac.resourceKey}"/>
                                <c:if test="${ac.orderable}">
                                    [
                                    <c:if test="${ac.order == order_asc}">
                                        ${asc_key}
                                    </c:if>
                                    <c:if test="${ac.order == order_desc}">
                                        ${desc_key}
                                    </c:if>
                                    ]
                                </c:if>
                            </html:option>
                        </c:forEach>
                    </html:select>
                </td>
                <td align="center">
                    <html:button property="select"
                                 onclick="javascript:selectColumn('availableColumnsId','selectedColumnsId');"
                                 styleClass="adminRighttArrow" titleKey="Common.add">
                        &nbsp;
                    </html:button>
                    <br/><br/>
                    <html:button property="unselect"
                                 onclick="javascript:selectColumn('selectedColumnsId','availableColumnsId');"
                                 styleClass="adminLeftArrow" titleKey="Common.delete">
                        &nbsp;
                    </html:button>
                </td>
                <td align="right">
                    <html:select property="selectedColumns" multiple="true" size="5" styleId="selectedColumnsId"
                                 onchange="javascript:addOrder(this);" styleClass="multipleSelect">
                        <c:forEach var="sel" items="${selectedColumns}">
                            <html:option value="${sel.id}">
                                <fmt:message key="${sel.resourceKey}"/>
                                <c:if test="${sel.orderable}">
                                    [
                                    <c:if test="${sel.order == order_asc}">
                                        ${asc_key}
                                    </c:if>
                                    <c:if test="${sel.order == order_desc}">
                                        ${desc_key}
                                    </c:if>
                                    ]
                                </c:if>
                            </html:option>
                        </c:forEach>
                    </html:select>
                </td>
                <td>
                    <html:button property="up"
                                 onclick="javascript:moveUp('selectedColumnsId');"
                                 styleClass="adminUpArrow"
                                 titleKey="Common.up">
                        &nbsp;
                    </html:button>
                    <br/><br/>
                    <html:button property="down"
                                 onclick="javascript:moveDown('selectedColumnsId');"
                                 styleClass="adminDownArrow"
                                 titleKey="Common.down">
                        &nbsp;
                    </html:button>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td id="selectOrder" style="display:none" colspan="4">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td width="50%" class="label">&nbsp;</td>
                <td width="50%" class="label">
                    &nbsp;
                    <fmt:message key="dashboard.column.order"/>
                    &nbsp;
                    <html:select property="dto(select)" onchange="javascript:selectOrder(this);" styleId="selectOrdId"
                                 styleClass="shortSelect">
                        <html:option value=""></html:option>
                        <html:option value="ASC">${asc_key}</html:option>
                        <html:option value="DESC">${desc_key}</html:option>
                    </html:select>
                    <br/>
                </td>
            </tr>
        </table>
    </td>
</tr>


<tr>
    <td colspan="4" class="title">
        <fmt:message key="dashboard.Filters"/>
    </td>
</tr>

<c:forEach var="column" items="${orderableColumns}" varStatus="i">
    <c:set var="ord" value="${column.order}"/>
    <c:if test="${column.order == 'NONE'}">
        <c:set var="ord" value=""/>
    </c:if>
    <html:hidden property="dto(${column.name})" value="${column.order}" styleId="${column.name}"/>
</c:forEach>

<c:forEach var="filter" items="${filters}" varStatus="i">
    <tr>
        <td class="label">
            <fmt:message key="${filter.resourceKey}"/>
        </td>
        <td class="contain" colspan="3">
            <c:choose>
                <c:when test="${filter.view == view_check}">
                    <html:checkbox property="dto(${filter.name})"/>
                </c:when>
                <c:when test="${filter.view == view_range}">
                    <html:text property="dto(${filter.name}_0)" styleClass="dateText"/>
                    -
                    <html:text property="dto(${filter.name}_1)" styleClass="dateText"/>
                </c:when>
                <c:when test="${filter.view == view_select}">
                    <c:set var="filterName" value="${filter.name}" scope="request"/>
                    <%
                        String filterName = (String) request.getAttribute("filterName");

                        ConfigurableFilter cf = xmlComponent.getConfigurableFilter(filterName);
                        if (cf.readConstantValuesFromConstantClass()) {
                            List options = WebUtils.getValuesFromConstantClass(cf, (HttpServletRequest) pageContext.getRequest());
                            request.setAttribute("options", options);
                        }
                        if (cf.readConstantValuesFromDataBase()) {
                            List options = WebUtils.getValuesFromDB(xmlComponent.getId(), cf, selectParams, pageContext);
                            request.setAttribute("options", options);
                        }

                    %>
                    <html:select property="dto(${filter.name})" styleClass="mediumSelect">
                        <c:forEach var="option" items="${options}">
                            <html:option value="${option.value}">${option.label}</html:option>
                        </c:forEach>
                    </html:select>
                </c:when>
                <c:when test="${filter.view == view_text}">
                    <html:text property="dto(${filter.name})" styleClass="dateText"/>
                </c:when>
            </c:choose>
        </td>
    </tr>
</c:forEach>

    <c:if test="${not empty staticFilterUrl}">
        <c:import url="${staticFilterUrl}"/>
    </c:if>

<tr>
    <td colspan="4" class="button" width="100%">
        <app2:checkAccessRight functionality="DASHBOARD" permission="UPDATE">
            <html:submit styleClass="button" property="save" onclick="javascript:submitPreProcess();">
                <fmt:message key="Common.save"/>
            </html:submit>
        </app2:checkAccessRight>
        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>

</td>
</tr>
</table>
</html:form>