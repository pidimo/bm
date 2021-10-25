<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

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


<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
<tr>
    <td class="title" colspan="4">
        <fmt:message key="Invoice.toRemind"/>
    </td>
</tr>
<tr>
    <html:form action="/Invoice/ToRemind/List.do" focus="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)">
    <td class="label">
        <fmt:message key="Common.search"/>
    </td>
    <td class="contain">
        <html:text property="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
                styleClass="largeText"
                maxlength="80" tabindex="1"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.toRemind.level"/>
    </td>
    <td class="contain">
        <fmt:message key="Invoice.notReminderedYet" var="notReminderedLabel"/>
        <c:set var="notRemindered" value="<%=SalesConstants.INVOICE_NOT_REMINDERED_YET%>"/>
        <fanta:select property="parameter(toRemindLevel)"
                      listName="reminderLevelList"
                      labelProperty="name"
                      valueProperty="level"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true" tabIndex="2">
            <fanta:addItem key="${notRemindered}" value="${notReminderedLabel}"/>
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
        &nbsp;
        <html:submit styleClass="button" tabindex="3"><fmt:message key="Common.go"/></html:submit>
    </td>
    </html:form>
</tr>
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="/Invoice/ToRemind/List.do" parameterName="alphabetName1"/>
    </td>
</tr>
</table>


<%
    if (request.getAttribute("invoiceToRemindList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("invoiceToRemindList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<html:form action="/Invoice/ToRemind.do" styleId="formList" onsubmit="return false;">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:hidden property="dto(allInvoiceIds)" value="${allListInvoiceIds}"/>
            <html:hidden property="dto(isCreateAll)" value="${false}" styleId="allButton"/>

            <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">
                            <c:if test="${size > 0}">
                                <html:button property="" styleClass="button" onclick="createToSelected()" tabindex="3">
                                    <fmt:message key="Invoice.toRemind.createForSelected"/>
                                </html:button>
                                <html:button property="" styleClass="button" onclick="createToAll()" tabindex="4">
                                    <fmt:message key="Invoice.toRemind.createForAll"/>
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
            <c:set var="maxLevel" value="${app2:getMaxReminderLevel(pageContext.request)}"/>

            <fanta:table list="invoiceToRemindList"
                         width="100%"
                         id="invoice"
                         action="Invoice/ToRemind/List.do"
                         imgPath="${baselayout}"
                         align="center" withCheckBox="true">

                <c:set var="editLink" value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>

                <c:if test="${size > 0}">
                    <c:set var="isLastLevel" value="${invoice.reminderLevel >= maxLevel}"/>
                    <c:remove var="lightClass"/>
                    <c:if test="${isLastLevel}">
                        <c:set var="lightClass" value=" listItemHighlight"/>
                    </c:if>

                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                                  property="invoiceId" headerStyle="listHeader"
                                                  styleClass="radio listItemCenter${lightClass}" width="5%"
                                                  disabled="${isLastLevel}"/>
                </c:if>

                <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem${lightClass}" title="Invoice.number"
                                  headerStyle="listHeader"
                                  width="16%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="invoiceDate" styleClass="listItem${lightClass}" title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="addressName" styleClass="listItem${lightClass}" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true"/>
                <fanta:dataColumn name="currencyName" styleClass="listItem${lightClass}" title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight${lightClass}" title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItemRight${lightClass}" title="Invoice.openAmount"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
                <fanta:dataColumn name="levelName" styleClass="listItem${lightClass}" title="Invoice.reminderLevel"
                                  headerStyle="listHeader" width="11%" orderable="true"/>
                <fanta:dataColumn name="reminderDate" styleClass="listItem2${lightClass}" title="Invoice.reminderDate"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatDate var="reminderDateValue" value="${app2:intToDate(invoice.reminderDate)}"
                                    pattern="${datePattern}"/>
                    ${reminderDateValue}
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">
                            <c:if test="${size > 0}">
                                <html:button property="" styleClass="button" onclick="createToSelected()" tabindex="5">
                                    <fmt:message key="Invoice.toRemind.createForSelected"/>
                                </html:button>
                                <html:button property="" styleClass="button" onclick="createToAll()" tabindex="6">
                                    <fmt:message key="Invoice.toRemind.createForAll"/>
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