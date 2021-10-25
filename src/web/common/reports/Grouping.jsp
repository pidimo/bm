<%@ page import="com.jatun.titus.listgenerator.structure.type.DBType,
                 com.piramide.elwis.web.reports.form.ColumnGroupForm,
                 java.util.ArrayList,
                 java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>
<%
    List resourceList = new ArrayList();
    resourceList.add("Report.first");
    resourceList.add("Report.second");
    resourceList.add("Report.third");
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
<html:form action="/Report/Grouping/Create.do">

<html:hidden property="dto(columnGroupTotalVersion)" value="${columnGroupForm.dtoMap['columnGroupTotalVersion']}" styleId="totalVersionId"/>

<html:hidden property="dto(reportId)" value="${columnGroupForm.dtoMap['reportId']}"/>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<html:hidden property="dto(op)" value="create" styleId="opId"/>

<html:hidden property="dto(resourceListSize)" value="${resourceListSize}" styleId="resourceListId"/>


<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="UPDATE" functionality="GROUP"
                                 styleClass="button"><fmt:message key="Common.save"/></app2:securitySubmit>
                <%--<html:submit styleClass="button"><fmt:message key="Common.save"/></html:submit>--%>
        </TD>
    </TR>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
<tr>
    <td colspan="4" class="title"><fmt:message key="Report.Title.Grouping"/></td>
</tr>
<tr>
    <td class="label" align="right">&nbsp;</td>
    <td class="label" align="right"><fmt:message key="ColumnGroup.columns"/></td>
    <td class="label"><fmt:message key="Report.shortOrder"/></td>
    <td class="label"><fmt:message key="Report.groupDateBy"/></td>
</tr>

<c:set var="counter" value="${1}"/>


<c:forEach var="resource" items="${resourceList}" varStatus="selectIndex">
    <html:hidden property="dto(version_${selectIndex.count})" styleId="versionId${counter}"/>
    <html:hidden property="dto(columnGroupId_${counter})" styleId="colGroupId${counter}"/>

    <c:set var="ind" value="${counter}"/>

    <html:hidden property="dto(columnReferenceSelected_${ind})" styleId="colRefSelectId${ind}"/>
    <html:hidden property="dto(tableReferenceSelected_${ind})" styleId="tableRefSelectId${ind}"/>
    <html:hidden property="dto(labelSelected_${ind})" styleId="labelSelectId${ind}"/>

    <c:set var="attrColumnReferenceSelected" value="columnReferenceSelected_${ind}"/>
    <c:set var="attrTableReferenceSelected" value="tableReferenceSelected_${ind}"/>
    <c:set var="attrLabelSelected" value="labelSelected_${ind}"/>
    <c:set var="attrTitusPath" value="titusPath_${ind}"/>

    <c:set var="myTableReference" value="${columnGroupForm.dtoMap[attrTableReferenceSelected]}"/>
    <c:set var="myColumnReference" value="${columnGroupForm.dtoMap[attrColumnReferenceSelected]}"/>
    <c:set var="myLabel" value="${columnGroupForm.dtoMap[attrLabelSelected]}"/>
    <c:set var="myTitusPath" value="${columnGroupForm.dtoMap[attrTitusPath]}"/>


    <c:if test="${' ' != myTitusPath && null != myTitusPath}">
        <html:hidden property="dto(selectedName_${counter})"
                     value="${app2:composeColumnLabelByTitusPath(myTitusPath, myLabel, pageContext.request)}" styleId="selectedNameId${counter}"/>
    </c:if>
    <tr>
        <td class="label" width="40%"><fmt:message key="${resource}"/></td>

        <td class="contain" width="30%">


            <html:select property="dto(columnId_${counter})" styleClass="largeSelect"
                         onchange="enable(this, 'select_${counter}')">
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
            <html:select property="dto(columnOrder_${counter})" styleClass="shortSelect">
                <option></option>
                <c:forEach var="item" items="${order}">
                    <html:option value="${item.value}">${item.label}</html:option>
                </c:forEach>
            </html:select>
        </td>

        <td class="contain" width="15%">

            <c:if test="${dto.select1 != null}">
                <c:set var="select1" value="${dto.select1}" scope="request"/>
            </c:if>
            <c:if test="${dto.select2 != null}">
                <c:set var="select2" value="${dto.select1}" scope="request"/>
            </c:if>
            <c:if test="${dto.select3 != null}">
                <c:set var="select3" value="${dto.select1}" scope="request"/>
            </c:if>


            <c:set var="myIndex" value="${counter}" scope="request"/>
            <%
                Integer index = Integer.valueOf(request.getAttribute("myIndex").toString());

                Boolean isSelected = (Boolean) request.getAttribute("select_" + index);
                ColumnGroupForm form = (ColumnGroupForm) request.getAttribute("columnGroupForm");
                Boolean isSelectedInForm = (Boolean) form.getDto("select_" + index);

                Boolean isConcurrence = (Boolean) form.getDto("concurrenceError");
                Boolean cmdError = (Boolean) form.getDto("cmdError");

                Boolean value = (isSelected != null ? isSelected : isSelectedInForm);

                if(null != isConcurrence || null != cmdError)
                    value = isSelectedInForm;


                if (null != value)
                    request.setAttribute("select", value);
                else
                    request.setAttribute("select", Boolean.valueOf(false));
            %>

            <label id="select_${counter}"   ${!select ? "style=\"display: none;\"":""} >
                <html:select property="dto(groupByDate_${counter})" styleClass="shortSelect">
                    <c:forEach var="item" items="${groupDate}">
                        <html:option value="${item.value}">${item.label}</html:option>
                    </c:forEach>
                </html:select>
            </label>
        </td>

    </tr>

    <c:set var="counter" value="${counter + 1}"/>
</c:forEach>


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
