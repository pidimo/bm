<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<app2:jScriptUrl url="/contacts/ContactPerson/Ajax/Select.do?op=${op}" var="jsContactPersonUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="addressId" value="address_id"/>
</app2:jScriptUrl>


<script type="text/javascript">

    function searchContactPerson(objSelect) {
        var address_id = objSelect.value;
        makePOSTAjaxContactPersonRequest(${jsContactPersonUrl}, "");
    }


    function makePOSTAjaxContactPersonRequest(urlAddress, parameters) {
        $.ajax({
            async:true,
            type: "POST",
            dataType: "html",
            data: parameters,
            url:urlAddress,
            beforeSend:setAjaxLoadMessage,
            success: function(data) {
                setContactPersonHtmlSelect(data);
                hideAjaxMessage();
            },
            error: function(ajaxRequest) {
                ajaxErrorProcess(ajaxRequest.status);
            }
        });
    }

    function setContactPersonHtmlSelect(data) {
        document.getElementById('contactPersonSelectDiv').innerHTML = data;
    }

    function hideAjaxMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.style.display = 'none';
    }

    function setAjaxLoadMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
        messagesDiv.style.display = 'inline';
    }

    function ajaxErrorProcess(status) {
        var messagesDiv = document.getElementById('ajaxMessageId');
        if (status == 404 || status == 302) {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
        } else {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
        }
        messagesDiv.style.display = 'inline';
    }

</script>


<TABLE cellspacing="0" cellpadding="4" width="85%" border="0" align="center">
<tr>
<td>
<html:form action="${action}" focus="dto(number)" enctype="multipart/form-data">
<html:hidden property="dto(locale)" value="${sessionScope.user.valueMap['locale']}"/>
<html:hidden property="dto(customerId)" value="${param.contactId}"/>
<html:hidden property="dto(addressId)" value="${param.contactId}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(addressType)" value="${addressType}"/>
<html:hidden property="dto(name1)" value="${name1}"/>
<html:hidden property="dto(name2)" value="${name2}"/>
<html:hidden property="dto(name3)" value="${name3}"/>
<html:hidden property="dto(version)"/>
<html:hidden property="dto(op)" value="update"/>

<table cellSpacing=0 cellPadding="0" width="100%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="update" functionality="CUSTOMER" property="dto(save)"
                                 styleClass="button"
                                 tabindex="2"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${save}"/>
            </app2:securitySubmit>

            <%--top links--%>
            &nbsp;
            <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                <app2:categoryTabLink id="customerCategoryLinkId"
                                      action="/contacts/Customer/CategoryTab/Forward/Update.do?index=${param.index}&contactId=${param.contactId}&dto(addressId)=${param.contactId}"
                                      categoryConstant="1"
                                      finderName="findValueByCustomerId"
                                      styleClass="folderTabLink">
                    <app2:categoryFinderValue forId="customerCategoryLinkId" value="${param.contactId}"/>
                </app2:categoryTabLink>
            </app2:checkAccessRight>

        </TD>
    </TR>
</table>
<table id="Customer.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <TR>
        <TD colspan="4" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label" width="20%">
            <fmt:message key="Customer.number"/>
        </TD>
        <TD class="contain" width="80%">
            <html:text property="dto(number)" styleClass="mediumText" maxlength="20" tabindex="4"
                       readonly="${op == 'delete'}" style="text-align:right"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.type"/>
        </TD>
        <TD class="contain">
            <app:catalogSelect property="dto(customerTypeId)" catalogTable="customertype" idColumn="customertypeid"
                               labelColumn="customertypename" styleClass="mediumSelect" tabindex="5"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.priority"/>
        </TD>
        <TD class="contain">
            <fanta:select property="dto(priorityId)" listName="priorityList"
                          labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" module="/catalogs" firstEmpty="true" tabIndex="6">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="type" value="CUSTOMER"/>
            </fanta:select>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.branch"/>
        </TD>
        <TD class="contain">
            <app:catalogSelect property="dto(branchId)" catalogTable="branch" idColumn="branchid"
                               labelColumn="branchname"
                               styleClass="largeSelect" tabindex="7"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.source"/>
        </TD>
        <TD class="contain">
            <app:catalogSelect property="dto(sourceId)" catalogTable="addresssource" idColumn="sourceid"
                               labelColumn="sourcename" styleClass="largeSelect" tabindex="8"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.payCondition"/>
        </TD>
        <TD class="contain">
            <app:catalogSelect property="dto(payConditionId)" catalogTable="paycondition" idColumn="payconditionid"
                               labelColumn="conditionname" styleClass="mediumSelect" tabindex="9"/>
        </TD>
    </TR>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.payMorality"/>
        </TD>
        <TD class="contain">
            <app:catalogSelect property="dto(payMoralityId)" catalogTable="paymorality" idColumn="paymoralityid"
                               labelColumn="paymoralityname" styleClass="mediumSelect" tabindex="10"/>
        </TD>
    </TR>
    <TD class="label">
        <fmt:message key="Customer.employee"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(employeeId)" listName="employeeBaseList"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                      readOnly="${op == 'delete'}"
                      module="/contacts" tabIndex="11" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.expectedTurnOver"/>
        </TD>
        <TD class="contain">
            <app:numberText property="dto(expectedTurnOver)" styleClass="mediumText" maxlength="14"
                            view="${'delete' == op}" numberType="decimal" tabindex="12" maxInt="11" maxFloat="2"
                            style="text-align:right"/>
        </TD>
    </TR>

    <c:if test="${addressType == organizationType}">
        <TR>
            <TD class="label">
                <fmt:message key="Customer.numberOfEmpoyees"/>
            </TD>
            <TD class="contain">
                <html:text property="dto(numberOfEmployees)" styleClass="mediumText" maxlength="6" tabindex="15"
                           readonly="${op == 'delete'}" style="text-align:right"/>
            </TD>
        </TR>
        <TR>
            <TD class="label">
                <fmt:message key="Contact.taxNumber"/>
            </TD>
            <TD class="contain">
                <html:text property="dto(taxNumber)" styleClass="mediumText" maxlength="20" tabindex="14"
                           readonly="${op == 'delete'}"/>
            </TD>
        </TR>
    </c:if>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.partner"/>
        </TD>
        <TD class="contain">
            <!-- search Partner-->
            <html:hidden property="dto(partnerId)" styleId="fieldAddressId_id"/>
            <app:text property="dto(partnerName)" styleClass="mediumText" maxlength="40" readonly="true"
                      tabindex="14"
                      view="${op =='delete'}" styleId="fieldAddressName_id"/>


            <tags:selectPopup url="/contacts/SearchAddress.do" name="searchPartner" titleKey="Common.search"
                              hide="${op == 'delete'}"/>
            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                   titleKey="Common.clear" hide="${op == 'delete'}"/>
        </TD>
    </TR>
    <tr>
        <TD class="label">
            <fmt:message key="Customer.invoiceAddress"/>
        </TD>
        <TD class="contain">
            <fanta:select property="dto(invoiceAddressId)" listName="invoiceAddressRelationList"
                          labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="mediumSelect"
                          readOnly="${op == 'delete'}"
                          onChange="searchContactPerson(this)"
                          module="/contacts" tabIndex="15" firstEmpty="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${param.contactId}"/>
            </fanta:select>
        </TD>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Customer.invoiceContactPerson"/>
        </td>
        <td class="contain">
            <div id="ajaxMessageId" class="messageToolTip" style="display:none; position:absolute; ">
                <fmt:message key="Common.message.loading"/>
            </div>
            <div id="contactPersonSelectDiv">
                <fanta:select property="dto(invoiceContactPersonId)" listName="searchContactPersonList"
                              module="/contacts" firstEmpty="true"
                              labelProperty="contactPersonName"
                              valueProperty="contactPersonId"
                              styleClass="mediumSelect"
                              tabIndex="15"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                 value="${not empty customerForm.dtoMap['invoiceAddressId']?customerForm.dtoMap['invoiceAddressId']:0}"/>
                </fanta:select>
            </div>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="Customer.invoiceAdditionalAddress"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="15">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty param.contactId ? param.contactId : 0}"/>
            </fanta:select>
        </td>
    </tr>
    <TR>
        <TD class="label">
            <fmt:message key="Customer.defaultDiscount"/>
        </TD>
        <TD class="contain">
            <app:numberText property="dto(defaultDiscount)"
                            styleId="discount_name"
                            styleClass="numberText"
                            numberType="decimal"
                            maxInt="10"
                            maxFloat="2"
                            view="${op == 'delete'}"
                            tabindex="15"/>
            <c:out value="%"/>
        </TD>
    </TR>


    <tr>
        <td colspan="2">
            <c:set var="elementCounter" value="${15}" scope="request"/>
            <c:set var="ajaxAction" value="/contacts/Customer/MainPageReadSubCategories.do" scope="request"/>
            <c:set var="downloadAction"
                   value="/contacts/Customer/MainPage/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}"
                   scope="request"/>
            <c:set var="formName" value="customerForm" scope="request"/>
            <c:set var="table" value="<%=ContactConstants.CUSTOMER_CATEGORY%>" scope="request"/>
            <c:set var="operation" value="update" scope="request"/>
            <c:set var="labelWidth" value="20" scope="request"/>
            <c:set var="containWidth" value="80" scope="request"/>
            <c:set var="generalWidth" value="${200}" scope="request"/>
            <c:import url="/common/catalogs/CategoryUtil.jsp"/>
            <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
        </td>
    </tr>
</table>
<table cellSpacing=0 cellPadding=0 width="100%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="update" functionality="CUSTOMER" property="dto(save)"
                                 styleClass="button"
                                 tabindex="${tabindex+1}"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${save}"/>
            </app2:securitySubmit>
        </TD>
    </TR>
</table>
<br>
</html:form>
</td>
</tr>
</TABLE>

