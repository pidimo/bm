<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<app2:jScriptUrl url="/finance/Download/Invoice/Document.do?dto(invoiceId)=${invoiceForm.dtoMap['invoiceId']}"
                 var="jsDownloadUrl">
    <app2:jScriptUrlParam param="dto(freeTextId)" value="id_freeText"/>
</app2:jScriptUrl>

<%--url to send invoice via email--%>
<app2:jScriptUrl
        url="${urlSendViaEmail}?invoiceId=${invoiceForm.dtoMap['invoiceId']}"
        var="jsSendViaEmailUrl">
    <app2:jScriptUrlParam param="telecomId" value="telecomId"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    <!--
    function downloadInvoice(id_freeText) {
        location.href = ${jsDownloadUrl};
    }

    function copyOfButtonPressed() {
        document.getElementById('copyOfButtonPressed').value = 'true';
    }

    function onSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.getElementById('changeInvoiceType').value = 'true';
            document.getElementById('isUpdateCustomerInfo').value = 'false';
            document.forms[0].submit();
        }
    }

    function sendViaEmail(emailBoxId) {
        var telecomId = document.getElementById(emailBoxId).value;
        location.href = ${jsSendViaEmailUrl};
    }

    function reSubmit() {
        document.getElementById('isUpdateCustomerInfo').value = 'false';
        document.forms[0].submit();
    }

    //-->
</script>

<fmt:message var="datePattern" key="datePattern" scope="request"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces" scope="request"/>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>" scope="request"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>" scope="request"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>" scope="request"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}" scope="request"/>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>

<html:form action="${action}" focus="${'create' == op ? 'dto(type)' : 'dto(addressName)'}">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(hasRelation)" styleId="hasRelationId"/>

<c:if test="${'update'== op || 'delete'== op}">
    <html:hidden property="dto(invoiceId)"/>
</c:if>

<c:if test="${'update' == op}">
    <c:set var="documentId" value="${invoiceForm.dtoMap['documentId']}" scope="request"/>
    <c:if test="${not empty msgConfirmation}">
        <html:hidden property="dto(reGenerate)" value="true"/>
    </c:if>

    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(documentId)"/>
    <html:hidden property="dto(ruleFormat)"/>
    <html:hidden property="dto(ruleNumber)"/>
    <html:hidden property="dto(customerNumber)"/>
    <html:hidden property="dto(entityInvoiceDate)"/>
    <html:hidden property="dto(confirmationMsg)" value="${confirmationMsg}" styleId="confirmationMsgId"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="95%" align="center" class="container">
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICE"
                             styleClass="button"
                             property="save"
                             tabindex="25">
            ${button}
        </app2:securitySubmit>

        <c:if test="${'update' == op}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICE"
                                 styleClass="button"
                                 property="generate"
                                 tabindex="26">
                <fmt:message key="Invoice.generate"/>
            </app2:securitySubmit>
            <c:if test="${not empty documentId}">
                <app:url var="urlDownload"
                         value="/Download/Invoice/Document.do?dto(freeTextId)=${documentId}&dto(invoiceId)=${invoiceForm.dtoMap['invoiceId']}"/>
                <html:button property="" styleClass="button" onclick="location.href='${urlDownload}'" tabindex="27">
                    <fmt:message key="Invoice.open"/>
                </html:button>
            </c:if>
            
            <c:set var="isSendViaEmail"
                   value="${not empty urlSendViaEmail and not empty documentId and app2:hasDefaultMailAccount(pageContext.request)}"/>
            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:button property="" styleClass="button" onclick="sendViaEmail('upToEmail_id')" tabindex="28">
                        <fmt:message key="Invoice.sendViaEmail"/>
                    </html:button>

                    <app:telecomSelect property="toEmail"
                                       styleId="upToEmail_id"
                                       tabindex="29"
                                       telecomIdColumn="telecomid"
                                       numberColumn="telecomnumber"
                                       telecomType="${TELECOMTYPE_EMAIL}"
                                       addressId="${invoiceForm.dtoMap['addressId']}"
                                       contactPersonId="${invoiceForm.dtoMap['contactPersonId']}"
                                       showOwner="true"
                                       styleClass="select"
                                       optionStyleClass="list" showDescription="false"
                                       selectPredetermined="true"/>
                </app2:checkAccessRight>
            </c:if>
        </c:if>

        <html:cancel styleClass="button" tabindex="30">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<tr>
    <td colspan="4" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<c:if test="${'create' == op}">
<tr>
    <td class="label">
        <fmt:message key="Invoice.type"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(changeInvoiceType)" styleId="changeInvoiceType" value="false"/>
        <html:select property="dto(type)"
                     styleClass="middleSelect"
                     readonly="false"
                     tabindex="1"
                     onchange="javascript:onSubmit(this);">
            <html:option value=""/>
            <html:options collection="invoiceTypeList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
    <td width="15%" class="label">
        <fmt:message key="Invoice.currency"/>
    </td>
    <td width="35%" class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="middleSelect"
                      module="/catalogs"
                      tabIndex="11" readOnly="false">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <c:choose>
        <c:when test="${creditNoteType == invoiceForm.dtoMap['type']}">
            <td width="15%" class="label">
                <fmt:message key="Invoice.creditNoteOf"/>
            </td>
            <td width="35%" class="contain">
                <html:hidden property="dto(creditNoteOfId)" styleId="fieldInvoiceId_id"/>
                <html:hidden property="dto(copyOfButtonPressed)" value="false" styleId="copyOfButtonPressed"/>
                <app:text property="dto(relatedInvoiceNumber)"
                          styleClass="middleText"
                          maxlength="40"
                          readonly="true"
                          styleId="fieldInvoiceNumber_id"
                          tabindex="2"/>

                <tags:selectPopup
                        url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                        name="invoiceSearchList"
                        titleKey="Common.search"
                        width="820"
                        submitOnSelect="true"
                        tabindex="3"/>
                <tags:clearSelectPopup keyFieldId="fieldInvoiceId_id"
                                       nameFieldId="fieldInvoiceNumber_id"
                                       titleKey="Common.clear"
                                       tabindex="4"/>
            </td>
        </c:when>
        <c:otherwise>
            <td width="15%" class="label">
                <fmt:message key="Invoice.copyOf"/>
            </td>
            <td width="35%" class="contain">
                <html:hidden property="dto(copyOfInvoiceId)" styleId="fieldInvoiceId_id"/>
                <html:hidden property="dto(copyOfButtonPressed)" value="false" styleId="copyOfButtonPressed"/>
                <app:text property="dto(number)"
                          styleClass="middleText" maxlength="40" readonly="true"
                          styleId="fieldInvoiceNumber_id"
                          tabindex="2"/>

                <tags:selectPopup
                        url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                        name="invoiceSearchList"
                        titleKey="Common.search"
                        width="820"
                        submitOnSelect="true"
                        tabindex="3"/>
                <tags:clearSelectPopup keyFieldId="fieldInvoiceId_id"
                                       nameFieldId="fieldInvoiceNumber_id"
                                       titleKey="Common.clear"
                                       tabindex="4"/>
            </td>
        </c:otherwise>
    </c:choose>

    <td class="label">
        <fmt:message key="Invoice.invoiceDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(invoiceDate)"
                      styleId="startDate"
                      calendarPicker="true"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      tabindex="12"
                      currentDate="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.sequenceRule"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sequenceRuleId)"
                      listName="sequenceRuleList"
                      labelProperty="label"
                      valueProperty="numberId"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="${voucherType}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.payCondition"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(payConditionId)"
                      listName="payConditionList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      tabIndex="13">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.contact"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(addressName)"
                  styleClass="middleText" maxlength="40" readonly="true"
                  styleId="fieldAddressName_id"
                  view="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                  tabindex="6"/>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          tabindex="7"
                          hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               tabindex="8"
                               hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"/>

        <html:hidden property="dto(isUpdateCustomerInfo)" value="true" styleId="isUpdateCustomerInfo"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.template"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(templateId)"
                      listName="invoiceTemplateList"
                      labelProperty="title"
                      valueProperty="templateId"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      tabIndex="14"
                      readOnly="false">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect" tabIndex="9"
                      readOnly="false">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.netGross"/>
    </td>
    <td class="contain">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="middleSelect"
                     readonly="${'true' == invoiceForm.dtoMap['hasRelation']}"
                     tabindex="15">
            <html:option value=""/>
            <html:options collection="netGrossOptions"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.sentAddress"/>
    </td>
    <td class="contain" colspan="3">
        <fanta:select property="dto(sentAddressId)" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      onChange="reSubmit()"
                      module="/contacts" tabIndex="9" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.sentContactPerson"/>
    </td>
    <td class="contain" colspan="3">
        <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      tabIndex="9"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                         value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.additionalAddress"/>
    </td>
    <td class="contain" colspan="3">
        <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                      firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}" tabIndex="9">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>
</tr>
</c:if>

<c:if test="${'create' != op}">
    <c:set var="existsReminders"
           value="${app2:existsInvoiceReminders(invoiceForm.dtoMap['invoiceId'], pageContext.request)}"
           scope="request"/>

    <c:if test="${creditNoteType == invoiceForm.dtoMap['type']}">
        <c:import url="/common/finance/CreditNoteFragment.jsp"/>
    </c:if>

    <c:if test="${invoiceType == invoiceForm.dtoMap['type']}">
        <c:import url="/common/finance/InvoiceFragment.jsp"/>
    </c:if>

</c:if>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="Invoice.notes"/>
        <html:textarea property="dto(notes)"
                       styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       tabindex="15"
                       readonly="${'delete' == op}"/>
    </td>
</tr>
<c:if test="${'create' != op}">
    <tr>
        <td colspan="4">
            <c:set var="invoiceId" value="${param.invoiceId}" scope="request"/>
            <c:import url="/common/finance/InvoiceVatTable.jsp"/>
        </td>
    </tr>
</c:if>
<tr>
    <td colspan="4" class="button">
        <app2:securitySubmit operation="${op}"
                             functionality="INVOICE"
                             styleClass="button"
                             property="save"
                             tabindex="17">
            ${button}
        </app2:securitySubmit>

        <c:if test="${'update' == op}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INVOICE"
                                 styleClass="button"
                                 property="generate"
                                 tabindex="18">
                <fmt:message key="Invoice.generate"/>
            </app2:securitySubmit>
            <c:if test="${not empty documentId}">
                <html:button property="" styleClass="button" onclick="location.href='${urlDownload}'" tabindex="19">
                    <fmt:message key="Invoice.open"/>
                </html:button>
            </c:if>

            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:button property="" styleClass="button" onclick="sendViaEmail('belowToEmail_id')" tabindex="20">
                        <fmt:message key="Invoice.sendViaEmail"/>
                    </html:button>
                    <app:telecomSelect property="toEmail"
                                       styleId="belowToEmail_id"
                                       tabindex="21"
                                       telecomIdColumn="telecomid"
                                       numberColumn="telecomnumber"
                                       telecomType="${TELECOMTYPE_EMAIL}"
                                       addressId="${invoiceForm.dtoMap['addressId']}"
                                       contactPersonId="${invoiceForm.dtoMap['contactPersonId']}"
                                       showOwner="true"
                                       styleClass="select"
                                       optionStyleClass="list" showDescription="false"
                                       selectPredetermined="true"/>

                </app2:checkAccessRight>
            </c:if>
        </c:if>

        <html:cancel styleClass="button" tabindex="22">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>