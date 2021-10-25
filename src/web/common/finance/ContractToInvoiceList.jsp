<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<calendar:initialize/>

<script language="JavaScript" type="text/javascript">
    <!--
    function check()
    {
        field = document.getElementById('formList').selected;
        guia = document.getElementById('formList').guia;
        var i;

        if (guia.checked){
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = true;
                    }
                } else {
                    if (!field.disabled) field.checked = true;
                }
            }
        } else {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = false;
                    }
                } else {
                    if (!field.disabled) field.checked = false;
                }
            }
        }
    }

    function resetFields() {
        var form = document.contractToInvoiceListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "hidden") {
                form.elements[i].value = "";
            }
        }
    }


    //form management
    function createToAll(){
        document.getElementById('allButton').value = true;
        goSubmit();
    }

    function createToSelected(){
        document.getElementById('allButton').value = false;
        goSubmit();
    }

    function goSubmit() {
        document.forms[1].submit();
    }

    function setSubmit(issubmit) {
        //this function is called from fantabulous html box list
    }

    //-->
</script>

<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<c:set var="PERIODIC_PAYMETHOD" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>

<html:form action="/Contract/ToInvoice/List.do" focus="parameter(address)">
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
<tr>
    <td class="title" colspan="4">
        <fmt:message key="Contract.toInvoice"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%">
        <fmt:message key="Contract.contact"/>
    </td>
    <td class="contain" width="35%">
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(address)" styleId="fieldAddressName_id" styleClass="middleText"
                  maxlength="40" tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                          hide="false" tabindex="1"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear" tabindex="1"/>
    </td>
    <td class="label" width="15%">
        <fmt:message key="Contract.product"/>
    </td>
    <td class="contain" width="35%">
        <html:hidden property="parameter(productId)" styleId="field_key"/>
        <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
        <html:hidden property="parameter(2)" styleId="field_unitId"/>
        <html:hidden property="parameter(3)" styleId="field_price"/>

        <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                  readonly="true" tabindex="3"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" tabindex="3"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" tabindex="3"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.payMethod"/>
    </td>
    <td class="contain">
        <c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>
        <html:select property="parameter(payMethod)" styleClass="middleSelect" tabindex="2">
            <html:option value=""/>
            <html:options collection="payMethodList" property="value" labelProperty="label"/>
        </html:select>

    </td>
    <td class="label">
        <fmt:message key="Contract.toInvoice.date"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <app:dateText property="parameter(currentDate)" maxlength="10" tabindex="4" styleId="dateId" currentDate="true"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" tabindex="4"><fmt:message key="Common.go"/></html:submit>
        <html:button property="reset1" tabindex="5" styleClass="button" onclick="resetFields();">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>
</table>
</html:form>


<%
    if (request.getAttribute("contractToInvoiceList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("contractToInvoiceList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<html:form action="/Contract/ToInvoice.do" styleId="formList" onsubmit="return false;">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:hidden property="dto(allComposeIds)" value="${allListComposeIds}"/>
            <html:hidden property="dto(isCreateAll)" value="${false}" styleId="allButton"/>
            <html:hidden property="dto(listDateFilter)" value="${listDateFilter}"/>

            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">
                            <c:if test="${size > 0}">
                                <html:button property="" styleClass="button" onclick="createToSelected()" tabindex="6">
                                    <fmt:message key="Contract.toInvoice.createForSelected"/>
                                </html:button>
                                <html:button property="" styleClass="button" onclick="createToAll()" tabindex="7">
                                    <fmt:message key="Contract.toInvoice.createForAll"/>
                                </html:button>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="contractToInvoiceList"
                         width="100%"
                         id="contract"
                         action="Contract/ToInvoice/List.do"
                         imgPath="${baselayout}"
                         align="center" withCheckBox="true">

                <c:set var="editLink"
                       value="ProductContractByContractToInvoice/Forward/Update.do?contractId=${contract.contractId}&dto(contractId)=${contract.contractId}&dto(addressName)=${app2:encode(contract.addressName)}&dto(productName)=${app2:encode(contract.productName)}&index=0"/>
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW" var="hasViewPermission"/>

                <c:set var="productEditLink"
                       value="/products/Product/Forward/Update.do?productId=${contract.productId}&dto(productId)=${contract.productId}&dto(productName)=${app2:encode(contract.productName)}&index=0"/>
                <app2:checkAccessRight functionality="PRODUCT" permission="VIEW" var="hasProductViewPermission"/>

                <c:if test="${size > 0}">
                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                                  property="contractInvoiceId" headerStyle="listHeader"
                                                  styleClass="radio listItemCenter" width="5%"
                                                  />
                </c:if>

                <c:choose>
                    <c:when test="${hasViewPermission}">
                        <fanta:dataColumn name="contractNumber" action="${editLink}" styleClass="listItem" title="ProductContract.contractNumber"
                                          headerStyle="listHeader" width="10%" orderable="true"/>
                        <fanta:dataColumn name="addressName" action="${editLink}" styleClass="listItem" title="Contract.contact"
                                          headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:dataColumn name="contractNumber" styleClass="listItem" title="ProductContract.contractNumber"
                                          headerStyle="listHeader" width="10%" orderable="true"/>
                        <fanta:dataColumn name="addressName" styleClass="listItem" title="Contract.contact"
                                          headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
                    </c:otherwise>
                </c:choose>

                <fanta:dataColumn name="customerName" styleClass="listItem" title="Sale.customer"
                                          headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>

                <fanta:dataColumn name="productName" styleClass="listItem" title="Contract.product"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <c:choose>
                        <c:when test="${hasProductViewPermission}">
                            <fanta:textShorter title="${contract.productName}">
                                <app:link action="${productEditLink}" contextRelative="true">
                                    <c:out value="${contract.productName}"/>
                                </app:link>
                            </fanta:textShorter>
                        </c:when>
                        <c:otherwise>
                            <fanta:textShorter title="${contract.productName}">
                                <c:out value="${contract.productName}"/>
                            </fanta:textShorter>
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>
                <fanta:dataColumn name="price" styleClass="listItemRight" title="Contract.price"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    <fmt:formatNumber var="price" value="${contract.price}" type="number"
                                      pattern="${numberFormat}"/>
                    ${price}
                </fanta:dataColumn>
                <fanta:dataColumn name="discount" styleClass="listItemRight" title="Contract.discount"
                                  headerStyle="listHeader" width="5%" orderable="true" renderData="false">
                    <fmt:formatNumber var="discount" value="${contract.discount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${discount}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItemRight" title="Contract.openAmount"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${contract.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    <c:if test="${contract.payMethod != PERIODIC_PAYMETHOD}">
                        ${openAmount}
                    </c:if>
                </fanta:dataColumn>
                <fanta:dataColumn name="orderDate" styleClass="listItem" title="Contract.orderDate"
                                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(contract.orderDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="payMethod" styleClass="listItem" title="Contract.payMethod"
                                  headerStyle="listHeader" width="8%" orderable="false" renderData="false">
                    <c:set var="payMethodVar" value="${app2:getPayMethodName(contract.payMethod,pageContext.request)}"/>
                    <fanta:textShorter title="${payMethodVar}">
                        <c:out value="${payMethodVar}"/>
                    </fanta:textShorter>
                </fanta:dataColumn>
                <fanta:dataColumn name="contractTypeName" styleClass="listItem2" title="Contract.contractType"
                                  headerStyle="listHeader"
                                  width="8%"
                                  orderable="true"/>
            </fanta:table>

        </td>
    </tr>
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">
                            <c:if test="${size > 0}">
                                <html:button property="" styleClass="button" onclick="createToSelected()" tabindex="8">
                                    <fmt:message key="Contract.toInvoice.createForSelected"/>
                                </html:button>
                                <html:button property="" styleClass="button" onclick="createToAll()" tabindex="9">
                                    <fmt:message key="Contract.toInvoice.createForAll"/>
                                </html:button>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
</table>
</html:form>