<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(currencyName)" >
    <table id="Currency.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<%--if update action or delete action--%>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(currencyId)"/>
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

<%--Currency form fields--%>
<%--Currency name--%>
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="Currency.name"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(currencyName)" styleClass="largetext" maxlength="30" view="${'delete' == op}"/>
        </TD>
    </TR>

<%--Currency label--%>
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="Currency.label"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(currencyLabel)" styleClass="largetext" maxlength="4" view="${'delete' == op}"/>
        </TD>
    </TR>

<%--Currency Symbol--%>
    <TR>
        <TD class="label"><fmt:message   key="Currency.symbol"/></TD>
        <TD class="contain">
        <app:text property="dto(currencySymbol)"  styleClass="largetext" maxlength="4" view="${'delete' == op}"/>
        </TD>
    </TR>

<%--currency unit--%>
    <TR>
        <TD class="label"><fmt:message   key="Currency.unit"/></TD>
        <TD class="contain">
        <app:numberText  property="dto(unit)" styleClass="text" maxlength="34" view="${'delete' == op}" numberType="decimal" maxFloat="2" maxInt="11"  />
        </TD>
    </TR>

<%--Currency default--%>
    <TR>
        <TD class="label"><fmt:message   key="Currency.isBasicCurrency"/></TD>
        <TD class="contain">
        <html:checkbox property="dto(isBasicCurrency)" disabled="${op == 'delete'}" styleClass="radio"/>
        </TD>
    </TR>
    <tr>
        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>

<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
        <app2:securitySubmit operation="${op}" functionality="CURRENCY" styleClass="button">${button}</app2:securitySubmit>
        <c:if test="${op == 'create'}" >
            <app2:securitySubmit operation="${op}" functionality="CURRENCY" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>

</html:form>

    </td>
</tr>
</table>
<c:remove var="hasBasicCurrency" />