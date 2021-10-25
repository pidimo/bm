<%@ include file="/Includes.jsp" %>
<%
    String vatRateVatId = request.getParameter("dto(vatId)");
    String vatLabel = request.getParameter("dto(vatLabel)");
    request.setAttribute("vatId", vatRateVatId);
    request.setAttribute("vatLabel", vatLabel);
%>


<calendar:initialize/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(validFrom)" >

    <c:if test="${null != vatId}">
        <html:hidden property="dto(vatId)" value="${vatId}"/>
    </c:if>
    <c:if test="${null == vatId}">
        <c:set var="vatId" value="vatRateForm.dtoMap['vatId']"/>
    </c:if>

    <table id="CategoryValue.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(locale)" value="${sessionScope.user.valueMap['locale']}"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(vatLabel)" value="${vatRateForm.dtoMap['vatLabel']}" />
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>



    <c:if test="${'update' == op}">
        <html:hidden property="dto(vatrateId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(vatrateId)"/>
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <TR>
        <TD colspan="2" class="title"><c:out value="${title}"/></TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="VatRate.vat"/></TD>
        <TD class="contain" width="75%">
            ${vatLabel}
            <html:hidden property="dto(vatId)" value="${vatId}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="VatRate.validFrom"/></TD>
        <TD class="contain" width="75%">
            <fmt:message   var="datePattern" key="datePattern"/>
            <html:hidden property="dto(datePattern)" value="${datePattern}"/>
            <app:dateText property="dto(validFrom)" styleId="validFromId"
 calendarPicker="${op != 'delete'}" datePatternKey="datePattern"
 styleClass="text" view="${op == 'delete'}" maxlength="10" convert="true"
 currentDate="false"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="VatRate.vatRate"/></TD>
        <TD class="contain" width="75%">
            <app:numberText  property="dto(vatRate)" styleClass="text"
 maxlength="8" view="${'delete' == op}" numberType="decimal" maxInt="3" maxFloat="2"/>
        </TD>
    </TR>
    </table>
    <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="VATRATE" styleClass="button">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}" >
                <app2:securitySubmit operation="${op}" functionality="VATRATE" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>
</html:form>

    </td>
</tr>
</table>