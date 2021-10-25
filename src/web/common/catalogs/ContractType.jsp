<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(name)">
                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(contractTypeId)"/>
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
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ContractType.name"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(name)" styleClass="text" maxlength="99"
                                      view="${'delete' == op}" tabindex="1"/>
                        </TD>
                    </TR>
                    <tr>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ContractType.tobeInvoiced"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <html:checkbox property="dto(tobeInvoiced)"
                                           disabled="${'delete' == op}"
                                           value="true"
                                           styleClass="radio" tabindex="2"/>
                        </TD>
                    </tr>
                </table>

                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}"
                                                 functionality="CONTRACTTYPE"
                                                 styleClass="button"
                                                 tabindex="3">
                                ${button}
                            </app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}"
                                                     functionality="CONTRACTTYPE"
                                                     styleClass="button"
                                                     property="SaveAndNew"
                                                     tabindex="4">
                                    <fmt:message key="Common.saveAndNew"/>
                                </app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button"
                                         tabindex="5">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>



