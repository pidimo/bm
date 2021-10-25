<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(number)">
                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(accountId)"/>
                    </c:if>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <tr>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="Account.number"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(number)" styleClass="text" maxlength="149"
                                      view="${'delete' == op}" tabindex="1"/>
                        </TD>
                    </tr>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="Account.name"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(name)" styleClass="text" maxlength="149"
                                      view="${'delete' == op}" tabindex="2"/>
                        </TD>
                    </TR>
                </table>

                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}"
                                                 functionality="ACCOUNT"
                                                 styleClass="button"
                                                 tabindex="3">
                                ${button}
                            </app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}"
                                                     functionality="ACCOUNT"
                                                     styleClass="button"
                                                     property="SaveAndNew"
                                                     tabindex="4">
                                    <fmt:message key="Common.saveAndNew"/>
                                </app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button" tabindex="5">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>