<%@ page import="com.jatun.titus.listgenerator.structure.type.DBType,
                 com.piramide.elwis.web.reports.form.ColumnGroupForm,
                 java.util.ArrayList,
                 java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>
<%
    List resourceList = new ArrayList();
    resourceList.add("ColumnGroup.firstAxisX");

    resourceList.add("ColumnGroup.firstAxisY");

    request.setAttribute("resourceList", resourceList);
    request.setAttribute("resourceListSize", new Integer(resourceList.size()));

    List lastSelected = (List) request.getAttribute("lastSelected");
    if (null != lastSelected) {

        ColumnGroupForm myForm = (ColumnGroupForm) request.getAttribute("columnGroupForm");

        for (int i = 0; i < lastSelected.size(); i++) {
            Map selected = (Map) lastSelected.get(i);
            String key = (String) selected.get("key");
            Integer value = (Integer) selected.get("value");
            String order = (String) selected.get("order");
            myForm.setDto("columnId_" + key, value);
            myForm.setDto("columnOrder_" + key, order);
        }
    }
%>


<c:set var="column_type_date"><%=DBType.DBTypeNameAsInt.DATE%></c:set>
<c:set var="column_type_dateTime"><%=DBType.DBTypeNameAsInt.DATETIME%></c:set>

<script>
    function enable(selectObject, id) {
        var y = selectObject.options[selectObject.selectedIndex];
        if (y.id == '${column_type_date}' || y.id == '${column_type_dateTime}') {
            document.getElementById(id).style.display = "";
        } else {
            document.getElementById(id).style.display = "none";
        }
    }
</script>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>


<html:form action="/Report/GroupingMatrix/Create.do">


<html:hidden property="dto(columnGroupTotalVersion)" value="${columnGroupForm.dtoMap['columnGroupTotalVersion']}"/>

<html:hidden property="dto(reportId)" value="${columnGroupForm.dtoMap['reportId']}"/>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<html:hidden property="dto(op)" value="create"/>

<html:hidden property="dto(resourceListSize)" value="${resourceListSize}"/>


<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <tr>
        <TD class="button">
            <app2:securitySubmit operation="UPDATE" functionality="GROUP"
                                 styleClass="button"><fmt:message key="Common.save"/></app2:securitySubmit>
                <%--<html:submit styleClass="button"><fmt:message key="Common.save"/></html:submit>--%>
        </TD>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
    <tr>
        <td colspan="4" class="title"><fmt:message key="Report.Title.Grouping"/></td>
    </tr>

    <tr>

        <td>
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>

                    <td class="label" align="right">&nbsp;</td>
                    <td class="label" align="right"><fmt:message key="ColumnGroup.columns"/></td>
                    <td class="label"><fmt:message key="Report.shortOrder"/></td>
                    <td class="label"><fmt:message key="Report.groupDateBy"/></td>
                </tr>

                <c:forEach var="resource" items="${resourceList}" varStatus="selectIndex">
                    <html:hidden property="dto(version_${selectIndex.count})" styleId="versionId${selectIndex.count}"/>
                    <html:hidden property="dto(axis_${selectIndex.count})" styleId="axisId${selectIndex.count}"/>

                    <fmt:message key="${resource}" var="myResource"/>
                    <html:hidden property="dto(resource)" value="${myResource}" styleId="resourceId${selectIndex.count}"/>
                    <c:if test="${fn:contains(resource,'AxisX')}">
                        <html:hidden property="dto(columnIdisAxis_${selectIndex.count})"
                                     value="columnId_${selectIndex.count}_X" styleId="columnXId${selectIndex.count}"/>
                    </c:if>

                    <c:if test="${fn:contains(resource,'AxisY')}">
                        <html:hidden property="dto(columnIdisAxis_${selectIndex.count})"
                                     value="columnId_${selectIndex.count}_Y" styleId="columnYId${selectIndex.count}"/>
                    </c:if>

                    <html:hidden property="dto(columnGroupId_${selectIndex.count})" styleId="columnGroupId${selectIndex.count}"/>
                    <tr>
                        <td class="label" width="40%">${myResource}</td>

                        <td class="contain" width="30%">

                            <html:select property="dto(columnId_${selectIndex.count})" styleClass="largeSelect"
                                         onchange="enable(this, 'select_${selectIndex.count}')">
                                <html:option value="" styleId="${column.columnType}"></html:option>
                                <c:forEach var="table" items="${structureSelect}">

                                    <c:forEach var="column" items="${table.columns}">
                                        <html:option value="${column.columnId}"
                                                     styleId="${column.columnType}">${app2:composeColumnLabelByTitusPath(column.titusPath, column.label, pageContext.request)}</html:option>
                                    </c:forEach>

                                </c:forEach>
                            </html:select>

                        </td>

                        <td class="contain" width="15%">

                            <html:select property="dto(columnOrder_${selectIndex.count})" styleClass="shortSelect">
                                <html:option value=""></html:option>
                                <c:forEach var="item" items="${order}">
                                    <html:option value="${item.value}">${item.label}</html:option>
                                </c:forEach>
                            </html:select>

                        </td>

                        <td class="contain" width="15%">

                            <c:set var="index" value="${selectIndex.count}" scope="request"/>
                            <%
                                Integer index = Integer.valueOf(request.getAttribute("index").toString());

                                Boolean isSelected = (Boolean) request.getAttribute("select_" + index);
                                ColumnGroupForm form = (ColumnGroupForm) request.getAttribute("columnGroupForm");
                                Boolean isSelectedInForm = (Boolean) form.getDto("select_" + index);
                                Boolean isConcurrence = (Boolean) form.getDto("concurrenceError");

                                Boolean value = (isSelected != null ? isSelected : isSelectedInForm);

                                if(null != isConcurrence)
                                    value = isSelectedInForm;


                                if (null != value)
                                    request.setAttribute("select", value);
                                else
                                    request.setAttribute("select", Boolean.valueOf(false));
                            %>

                            <label id="select_${selectIndex.count}"   ${!select ? "style=\"display: none;\"":""} >
                                <html:select property="dto(groupByDate_${selectIndex.count})" styleClass="shortSelect">
                                    <c:forEach var="item" items="${groupDate}">
                                        <html:option value="${item.value}">${item.label}</html:option>
                                    </c:forEach>
                                </html:select>
                            </label>
                        </td>

                    </tr>
                </c:forEach>

            </table>
        </td>

    </tr>
</table>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="UPDATE" functionality="GROUP"
                                 styleClass="button"><fmt:message key="Common.save"/></app2:securitySubmit>
                <%--<html:submit styleClass="button"><fmt:message   key="Common.save"/></html:submit>--%>
        </TD>
    </TR>
</table>
</html:form>
</td>
</tr>
</table>