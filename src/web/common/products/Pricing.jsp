<%@ include file="/Includes.jsp" %>

<table width="75%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<br>
<html:form action="${action}" focus="dto(quantity)" >
    <table id="Pricing.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <html:hidden property="dto(unitName)" />
        <html:hidden property="dto(productId)" value="${param.productId}"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${'create' == op}">
            <html:hidden property="dto(productId)" value="${param.productId}"/>
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
        <TD colspan="2" class="title"><c:out value="${title}"/></TD>
    </TR>
    <TR>
        <TD class="label" width="25%"><fmt:message    key="Product.quantity"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(quantity)" styleClass="numberText" style="text-align:right" maxlength="11" view="${'delete' == op || 'update'== op}"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%"><fmt:message   key="Product.unit"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(unitName)" styleClass="numberText" maxlength="40" view="${'delete' == op || 'update'== op || 'create'== op}"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%"><fmt:message   key="Product.price"/></TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(price)" styleClass="numberText" maxlength="20" view="${'delete' == op}" numberType="decimal" maxFloat="2" maxInt="10" />
        </TD>
    </tr>
    <tr>
        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
            
<app2:securitySubmit operation="${op}" functionality="PRICING" styleClass="button" property="dto(save)">
    ${button}
</app2:securitySubmit>

            <c:if test="${op == 'create'}" >

<app2:securitySubmit operation="${op}" functionality="PRICING" styleClass="button" property="SaveAndNew">
    <fmt:message   key="Common.saveAndNew"/>
</app2:securitySubmit>

            </c:if>
            <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
        </table>
</html:form>
    </td>
</tr>
</table>