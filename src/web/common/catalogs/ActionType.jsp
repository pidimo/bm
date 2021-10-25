<%@ include file="/Includes.jsp" %>

<tags:initSelectPopup/>
<c:set var="helpUrl" value="/catalogs/ActionTypeNumberRuleHelp.jsp"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">

    <tr>
        <td>
            <html:form action="${action}" focus="dto(actionTypeName)">
                <html:hidden property="dto(op)" value="${op}"/>

                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(actionTypeId)"/>
                    <html:hidden property="dto(numberId)"/>
                </c:if>

                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>

                <table id="ActionType.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ActionType.name"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(actionTypeName)"
                                      styleClass="largetext"
                                      maxlength="80"
                                      view="${'delete' == op}"
                                      tabindex="1"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label">
                            <fmt:message key="ActionType.probability"/>
                        </TD>
                        <TD class="contain">
                            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                            <html:select property="dto(probability)"
                                         styleClass="shortSelect"
                                         readonly="${op == 'delete'}"
                                         tabindex="2">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="probabilities" property="value" labelProperty="label"/>
                            </html:select>
                            <fmt:message key="Common.probabilitySymbol"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label">
                            <fmt:message key="ActionType.sequence"/>
                        </TD>
                        <TD class="contain">
                            <app:text property="dto(sequence)"
                                      styleClass="shortText"
                                      style="text-align:right"
                                      maxlength="4"
                                      view="${'delete' == op}"
                                      tabindex="3"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ActionType.format"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(format)"
                                      styleClass="largeText"
                                      maxlength="149"
                                      view="${'delete' == op}"
                                      tabindex="4"/>
                            <c:if test="${'delete' != op}">
                                <tags:selectPopup url="${helpUrl}"
                                                  name="SequenceRuleFormatFieldHelp"
                                                  titleKey="Common.help"
                                                  width="400" heigth="300"
                                                  scrollbars="0"
                                                  imgPath="/img/help.gif"
                                                  imgWidth="10"
                                                  imgHeight="13"
                                                  tabindex="4"/>
                            </c:if>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ActionType.resetType"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <html:select property="dto(resetType)"
                                         styleClass="select"
                                         readonly="${'delete' == op}"
                                         tabindex="5">
                                <html:option value=""/>
                                <html:options collection="sequenceRuleResetTypes"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="ActionType.startNumber"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:numberText property="dto(startNumber)"
                                            styleClass="numberText"
                                            maxlength="8"
                                            numberType="integer"
                                            view="${'delete' == op}"
                                            tabindex="6"/>
                        </TD>
                    </TR>
                    <c:if test="${'create' != op}">
                        <TR>
                            <TD class="label" width="25%" nowrap>
                                <fmt:message key="ActionType.lastNumber"/>
                            </TD>
                            <TD class="contain" width="75%">
                                <app:numberText property="dto(lastNumber)"
                                                styleClass="numberText"
                                                maxlength="8"
                                                numberType="integer"
                                                view="true"
                                                tabindex="5"/>
                            </TD>
                        </TR>
                    </c:if>
                    <c:if test="${'update' == op && null != actionTypeForm.dtoMap['lastDate']}">
                        <tr>
                            <TD class="label" width="25%" nowrap>
                                <fmt:message key="ActionType.lastDate"/>
                            </TD>
                            <TD class="contain" width="75%">
                                <html:hidden property="dto(lastDate)"/>
                                <fmt:formatDate var="dateValue"
                                                value="${app2:intToDate(actionTypeForm.dtoMap['lastDate'])}"
                                                pattern="${datePattern}"/>
                                    ${dateValue}
                            </TD>
                        </tr>
                    </c:if>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="ACTIONTYPE"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="ACTIONTYPE" styleClass="button"
                                                     property="SaveAndNew"><fmt:message
                                        key="Common.saveAndNew"/></app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>
        </td>
    </tr>
</table>



