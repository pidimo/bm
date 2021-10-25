<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(vatLabel)" >



<c:set var="vatId" value="${vatForm.dtoMap['vatId']}"/>
<c:set var="vatLabel" value="${vatForm.dtoMap['vatLabel']}"/>

    <table id="vat.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<%--if update action or delete action--%>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(vatId)"/>
    </c:if>

<%--for the version control if update action--%>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

<%--for the control referencial integrity if delete action--%>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
<%--Vat form field--%>
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="Vat.name"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(vatLabel)" styleClass="largetext" maxlength="40" view="${'delete' == op}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="Vat.description"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(vatDescription)" styleClass="largetext" maxlength="80" view="${'delete' == op}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap>
            <fmt:message key="Vat.taxKey"/>
        </TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(taxKey)"
                            styleClass="numberText"
                            maxlength="8"
                            numberType="integer"
                            view="${'delete' == op}"/>
        </TD>
    </TR>

    </table>

<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
        <app2:securitySubmit operation="${op}" functionality="VAT" styleClass="button">${button}</app2:securitySubmit>
        <c:if test="${op == 'create'}" >
                <app2:securitySubmit operation="${op}" functionality="VAT" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>
</html:form>

    </td>
</tr>
</table>

<app2:checkAccessRight functionality="VATRATE" permission="VIEW">
<c:if test="${op=='update'}">
    <iframe name="frame1" src="<app:url value="VatRate/List.do?parameter(vatRateVatId)=${vatId}&dto(vatId)=${vatId}&dto(vatLabel)=${app2:encode(vatLabel)}" />"  class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
    </iframe>
</c:if>
</app2:checkAccessRight>