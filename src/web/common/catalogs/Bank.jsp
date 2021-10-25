<%@ include file="/Includes.jsp" %>
<br>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>

            <html:form action="${action}" focus="dto(bankName)">
                <table border="0" cellpadding="0" cellspacing="0" width="65%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="dto(contactId)" value="${param.contactId}"/>
                    <html:hidden property="dto(bankAccountId)" value="${param.bankAccountId}"/>

                        <%--if update action or delete action--%>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(bankId)"/>
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
                        <TD class="label" width="25%"><fmt:message key="Bank.name"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(bankName)" styleClass="largetext" maxlength="40"
                                      view="${'delete' == op}"/>
                        </TD>
                    </tr>

                    <tr>
                        <TD class="label" width="25%"><fmt:message key="Bank.code"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(bankCode)" styleClass="largetext" maxlength="8"
                                      view="${'delete' == op}"/>
                        </TD>
                    </tr>

                    <tr>
                        <TD class="label" width="25%"><fmt:message key="Bank.label"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(bankLabel)" styleClass="largetext" maxlength="20"
                                      view="${'delete' == op}"/>
                        </TD>
                    </tr>

                    <tr>
                        <TD class="label" width="25%" nowrap><fmt:message key="Bank.internationalCode"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(bankInternationalCode)" styleClass="largetext" maxlength="20"
                                      view="${'delete' == op}"/>
                        </TD>
                    </tr>
                    <tr>
                        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false"/>><img
                                src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
                    </tr>
                </table>

                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="65%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="BANK"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:choose>

                                <c:when test="${op == 'create' && !isFromContacts}">
                                    <app2:securitySubmit operation="${op}" functionality="BANK" styleClass="button"
                                                         property="SaveAndNew"><fmt:message key="Common.saveAndNew"/>
                                    </app2:securitySubmit>
                                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${op != 'create'}">
                                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                                    </c:if>
                                    <c:if test="${op == 'create'}">
                                    <html:cancel styleClass="button" onclick="window.close()"><fmt:message key="Common.cancel"/></html:cancel>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </TD>
                    </TR>
                </table>

            </html:form>

        </td>
    </tr>
</table>