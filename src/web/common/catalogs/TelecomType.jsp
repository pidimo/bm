<%@ page import="java.util.List"%>
<%@ include file="/Includes.jsp" %>

<%
    List telecomTypeList = JSPHelper.getTelecomTypeTypes(request);
    request.setAttribute("systemTelecomType",telecomTypeList);
%>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

<html:form action="${action}" focus="dto(telecomTypeName)" >
    <html:hidden property="dto(actualType)" value="${telecomTypeForm.dtoMap['type']}"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(telecomTypeId)"/>

        <c:set var="haveFirstTranslation" value="${telecomTypeForm.dtoMap['haveFirstTranslation']}"/>
        <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}" />
        <html:hidden property="dto(langTextId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
    <TR>
        <TD colspan="2" class="title"><c:out value="${title}"/></TD>
    </TR>

    <TR>
        <TD class="label" width="40%" nowrap><fmt:message    key="TelecomType.name"/></TD>
        <TD class="contain" width="60%">
            <app:text property="dto(telecomTypeName)" styleClass="largetext" maxlength="20" view="${'delete' == op}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="TelecomType.position"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(position)" styleClass="largetext" style="text-align:right" maxlength="4" view="${'delete' == op}"/>
        </TD>
    </TR>

    <tr>
        <td class="label" nowrap ><fmt:message key="TelecomType.type"/></td>
        <td class="contain">

    <c:if test="${op == 'create' || op == 'update'}">
        <html:select property="dto(type)" styleClass="select">
            <html:option value=""/>
            <html:options collection="systemTelecomType"  property="value" labelProperty="label"/>
        </html:select>
    </c:if>
            
    <c:if test="${op == 'delete'}">
        <c:set var="telecomTypeConstant" value="${telecomTypeForm.dtoMap['type']}" scope="request"/>
<%
    String constant = (String) request.getAttribute("telecomTypeConstant");
    String key = JSPHelper.getTelecomType(constant,request);
    request.setAttribute("constantKey",key);
%>
    ${constantKey}
    </c:if>

        </td>
    </tr>

    <tr>
        <td class="label" ><fmt:message key="TelecomType.language"/></td>
        <td class="contain" >
            <fanta:select property="dto(languageId)" listName="systemLanguageList" labelProperty="name"  valueProperty="id"
 firstEmpty="true" styleClass="select" readOnly="${op == 'delete'|| true == haveFirstTranslation}" >
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>
        </td>
    </tr>
    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="TELECOMTYPE" styleClass="button">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}" >
                <app2:securitySubmit operation="${op}" functionality="TELECOMTYPE" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>

</html:form>

    </td>
</tr>
</table>



