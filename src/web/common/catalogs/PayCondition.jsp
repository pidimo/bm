<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>

    </tr>
    <tr>
        <td>
            <html:form action="${action}" focus="dto(payConditionName)">
                <table id="PayCondition.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(payConditionId)"/>
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
                        <TD class="label" width="25%"><fmt:message key="PayCondition.name"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(payConditionName)" styleClass="largetext" maxlength="40"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%"><fmt:message key="PayCondition.PayDays"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(payDays)"
                                      styleClass="text"
                                      style="text-align:right"
                                      maxlength="4"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%"><fmt:message key="PayCondition.payDaysDiscount"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(payDaysDiscount)" styleClass="text" style="text-align:right"
                                      maxlength="4" view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%">
                            <fmt:message key="PayCondition.Discount"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:numberText property="dto(discount)"
                                            styleClass="text"
                                            maxlength="8"
                                            view="${'delete' == op}"
                                            numberType="decimal"
                                            maxInt="3"
                                            maxFloat="2"/>
                        </TD>
                    </TR>
                    <c:if test="${'create' == op}">
                        <tr>
                            <td class="label" nowrap>
                                <fmt:message key="PayCondition.languageForTexts"/>
                            </td>
                            <td class="contain">
                                <fanta:select property="dto(languageId)" listName="languageBaseList"
                                              labelProperty="name" valueProperty="id" firstEmpty="true"
                                              styleClass="select" readOnly="${'delete' == op}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </td>
                        </tr>
                        <tr>
                            <TD class="topLabel" colspan="2">
                                <fmt:message key="PayConditionText.text"/>
                                <html:textarea property="dto(payConditionText)"
                                               styleClass="middleDetail"
                                               style="height:120px;width:99%;"
                                               readonly="${op == 'delete'}"/>
                            </td>
                        </tr>
                    </c:if>
                </table>

                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="PAYCONDITION"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="PAYCONDITION" styleClass="button"
                                                     property="SaveAndNew"><fmt:message
                                        key="Common.saveAndNew"/></app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>
        </td>
    </tr>
</table>