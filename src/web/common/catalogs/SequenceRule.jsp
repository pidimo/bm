<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

<script language="JavaScript">
    function onSubmit() {
        document.getElementById('sequenceRuleFormId').submit();
    }

    function hideSequenceRuleFormPanel() {
        document.getElementById('formPanel').style.visibility = "hidden";
        document.getElementById('formPanel').style.position = "absolute";
    }

</script>
<tags:initSelectPopup/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="sequenceRuleTypeList" value="${app2:getSequenceRuleTypes(pageContext.request)}"/>
<c:set var="sequenceRuleResetTypes" value="${app2:getSequeceRuleResetTypes(pageContext.request)}"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>"/>
<c:set var="customerType" value="<%=FinanceConstants.SequenceRuleType.CUSTOMER.getConstantAsString()%>"/>
<c:set var="articleType" value="<%=FinanceConstants.SequenceRuleType.ARTICLE.getConstantAsString()%>"/>
<c:set var="supportCaseType" value="<%=FinanceConstants.SequenceRuleType.SUPPORT_CASE.getConstantAsString()%>"/>
<c:set var="contractNumber" value="<%=FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.getConstantAsString()%>"/>

<c:set var="helpUrl" value="/catalogs/SequenceRuleFormatField.jsp"/>
<c:if test="${customerType == sequenceRuleForm.dtoMap['type'] || articleType == sequenceRuleForm.dtoMap['type'] || supportCaseType == sequenceRuleForm.dtoMap['type']}">
    <c:set var="helpUrl" value="/catalogs/CustomerSequenceRuleHelp.jsp"/>
</c:if>
<c:if test="${contractNumber == sequenceRuleForm.dtoMap['type']}">
    <c:set var="helpUrl" value="/catalogs/ProductContractNumberRuleHelp.jsp"/>
</c:if>

<html:form action="${action}" focus="dto(label)" styleId="sequenceRuleFormId">

    <div id="formPanel">
    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(numberId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(confirmatioMsg)" value="${sequenceRuleForm.dtoMap['confirmatioMsg']}"/>
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
                <fmt:message key="SequenceRule.label"/>
            </TD>
            <TD class="contain" width="75%">
                <app:text property="dto(label)" styleClass="largeText" maxlength="149"
                          view="${'delete' == op}" tabindex="1"/>
            </TD>
        </tr>
        <tr>
            <TD class="label">
                <fmt:message key="SequenceRule.type"/>
            </TD>
            <TD class="contain">
                <html:select property="dto(type)"
                             styleClass="select"
                             readonly="${'create' != op}"
                             onchange="onSubmit();"
                             tabindex="2">
                    <html:option value=""/>
                    <html:options collection="sequenceRuleTypeList"
                                  property="value"
                                  labelProperty="label"/>
                </html:select>
            </TD>
        </tr>
        <TR>
            <TD class="label" width="25%" nowrap>
                <fmt:message key="SequenceRule.format"/>
            </TD>
            <TD class="contain" width="75%">
                <app:text property="dto(format)" styleClass="largeText" maxlength="149"
                          view="${'delete' == op}" tabindex="2"/>
                <c:if test="${'delete' != op}">
                    <tags:selectPopup url="${helpUrl}"
                                      name="SequenceRuleFormatFieldHelp"
                                      titleKey="Common.help"
                                      width="400" heigth="300"
                                      scrollbars="0"
                                      imgPath="/img/help.gif"
                                      imgWidth="10"
                                      imgHeight="13"
                                      tabindex="2"/>
                </c:if>
            </TD>
        </TR>
        <c:if test="${voucherType == sequenceRuleForm.dtoMap['type'] || contractNumber == sequenceRuleForm.dtoMap['type']}">
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.resetType"/>
                </TD>
                <TD class="contain" width="75%">
                    <html:select property="dto(resetType)"
                                 styleClass="select"
                                 readonly="${'delete' == op}"
                                 tabindex="3">
                        <html:option value=""/>
                        <html:options collection="sequenceRuleResetTypes"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </TD>
            </TR>
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.startNumber"/>
                </TD>
                <TD class="contain" width="75%">
                    <app:numberText property="dto(startNumber)"
                                    styleClass="numberText"
                                    maxlength="8"
                                    numberType="integer"
                                    view="${'delete' == op}"
                                    tabindex="4"/>
                </TD>
            </TR>
        </c:if>
        <c:if test="${'create' != op}">
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.lastNumber"/>
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
        <c:if test="${'update' == op && null != sequenceRuleForm.dtoMap['lastDate']}">
            <tr>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.lastDate"/>
                </TD>
                <TD class="contain" width="75%">
                    <html:hidden property="dto(lastDate)"/>
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(sequenceRuleForm.dtoMap['lastDate'])}"
                                    pattern="${datePattern}"/>
                        ${dateValue}
                </TD>
            </tr>
        </c:if>

        <%--debitor info only to voucher type--%>
        <c:if test="${voucherType == sequenceRuleForm.dtoMap['type'] and app2:hasAssignedLexwareModule(pageContext.request)}">
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.debitor"/>
                </TD>
                <TD class="contain" width="75%">
                    <html:hidden property="dto(debitorId)" styleId="fieldAddressId_id"/>
                    <app:text property="dto(debitorName)"
                              styleClass="middleText" maxlength="40" readonly="true"
                              styleId="fieldAddressName_id"
                              view="${'delete' == op}"
                              tabindex="6"/>

                    <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                                      titleKey="Common.search"
                                      tabindex="7"
                                      hide="${'delete' == op}"/>
                    <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                           titleKey="Common.clear"
                                           tabindex="8"
                                           hide="${'delete' == op}"/>
                </TD>
            </TR>
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="SequenceRule.debitorNumber"/>
                </TD>
                <TD class="contain" width="75%">
                    <app:text property="dto(debitorNumber)"
                              styleClass="middleText"
                              maxlength="20"
                              view="${'delete' == op}"
                              tabindex="9"/>
                </TD>
            </TR>
        </c:if>
    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <app2:securitySubmit property="save"
                                     operation="${op}"
                                     functionality="SEQUENCERULE"
                                     styleClass="button"
                                     tabindex="12">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="SEQUENCERULE" styleClass="button"
                                         property="SaveAndNew"
                                         tabindex="13">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button"
                             tabindex="14"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
    </div>

    <div id="numberAvailableToFormatPanel">
        <c:if test="${numberAvailableToFormatMsg eq 'true'}">
            <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                <TR>
                    <TD class="button">
                        <app2:securitySubmit property="dto(deleteAllNumbers)" operation="${op}" functionality="SEQUENCERULE"
                                             styleClass="button" tabindex="15">
                            <fmt:message key="SequenceRule.button.deleteAllfreeNumber"/>
                        </app2:securitySubmit>

                        <app2:securitySubmit property="dto(updateNumberFormat)" operation="${op}" functionality="SEQUENCERULE"
                                             styleClass="button" tabindex="16">
                            <fmt:message key="SequenceRule.button.updatefreeNumber"/>
                        </app2:securitySubmit>

                        <html:cancel styleClass="button" tabindex="17">
                            <fmt:message key="SequenceRule.button.cancelChangeFormat"/>
                        </html:cancel>

                        <html:hidden property="dto(confirmChangeFormat)" value="true" styleId="confirmChangeFormatId"/>
                    </TD>
                </TR>
            </table>
        </c:if>
    </div>
</html:form>

