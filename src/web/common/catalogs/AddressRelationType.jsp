<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(title)" >
       <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

        <html:hidden property="dto(op)" value="${op}"/>

<%--if update action or delete action--%>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(relationTypeId)"/>
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

        <TR>
            <TD class="label" width="25%" nowrap>
                <fmt:message    key="AddressRelationType.title"/>
            </TD>
            <TD class="contain" width="75%">
            <app:text property="dto(title)" styleClass="largetext" maxlength="60" view="${'delete' == op}" tabindex="1"/>
            </TD>
        </TR>
        <TR>
            <TD class="label">
                <fmt:message    key="AddressRelationType.relationType"/>
            </TD>
            <TD class="contain">
                <c:set var="relationTypes" value="${app2:getAddressRelationTypeTypeList(pageContext.request)}"/>
                <html:select property="dto(relationType)" styleClass="middleSelect" tabindex="2"
                             readonly="${'delete' == op}">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="relationTypes" property="value" labelProperty="label"/>
                </html:select>
            </TD>
        </TR>
        </table>

        <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
            <TR>
                <TD class="button">
                    <app2:securitySubmit operation="${op}" functionality="ADDRESSRELATIONTYPE" tabindex="5"
                                         styleClass="button">${button}</app2:securitySubmit>
                    <c:if test="${op == 'create'}">
                        <app2:securitySubmit operation="${op}" functionality="ADDRESSRELATIONTYPE" styleClass="button" tabindex="6"
                                             property="SaveAndNew"><fmt:message
                                key="Common.saveAndNew"/></app2:securitySubmit>
                    </c:if>
                    <html:cancel styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/></html:cancel>
                </TD>
            </TR>
        </table>
</html:form>
    </td>
</tr>
</table>
