<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function check()
    {
        field = document.getElementById('formList').selected;
        guia = document.getElementById('formList').guia;
        var i;

        if (guia.checked) {
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

    function printAll() {
        document.getElementById('printButton').value = true;
        goSubmit();
    }

    function printSelected() {
        document.getElementById('printButton').value = false;
        goSubmit();
    }

    //form management
    var isListSubmit = "false";
    function goSubmit() {
        if (isListSubmit == "false") {
            setSubmit(true);
            document.forms[1].submit();
        }
        isListSubmit = "false";
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = ('true' == ss);
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        isListSubmit = "true";
    }

    //-->
</script>

<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<calendar:initialize/>

<html:form action="/Invoice/RangePrintList.do"
           focus="parameter(invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)">
    <table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
        <tr>
            <td class="title" colspan="4">
                <fmt:message key="Invoice.print.range"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%">
                <fmt:message key="Invoice.contact"/>
            </td>
            <td class="contain" width="35%">
                <html:text
                        property="parameter(invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
                        styleClass="mediumText"
                        maxlength="80" tabindex="1"/>
            </td>

            <td class="label" width="15%">
                <fmt:message key="Invoice.invoiceDate"/>
            </td>
            <td class="contain" width="35%">
                <fmt:message key="Common.from"/>&nbsp;
                <app:dateText property="parameter(invoiceDateFrom)" maxlength="10" styleId="invoiceDateFrom"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="4"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(invoiceDateTo)" maxlength="10" styleId="invoiceDateTo"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true" tabindex="5"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Invoice.number"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <html:text property="parameter(numberFrom)" styleClass="shortText" tabindex="2"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <html:text property="parameter(numberTo)" styleClass="shortText" tabindex="3"/>
            </td>
            <td class="label">
                <fmt:message key="Invoice.openAmount"/>
            </td>
            <td class="contain">
                <fmt:message key="Common.from"/>&nbsp;
                <app:numberText property="parameter(openAmountFrom)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="6" maxlength="13"/>
                &nbsp;
                <fmt:message key="Common.to"/>&nbsp;
                <app:numberText property="parameter(openAmountTo)" numberType="decimal" maxInt="10" maxFloat="2"
                                styleClass="numberText" tabindex="7" maxlength="13"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" align="center" class="alpha">
                <fanta:alphabet action="/Invoice/RangePrintList.do" parameterName="alphabetName1"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="7"><fmt:message key="Common.go"/></html:submit>
            </td>
        </tr>
    </table>
</html:form>

<%
    if (request.getAttribute("invoicePrintList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("invoicePrintList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<html:form action="/Invoice/RangePrint.do" styleId="formList" onsubmit="return testSubmit();">
    <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <html:hidden property="dto(printAllIds)" value="${allPrintIds}"/>
                <html:hidden property="dto(isPrintAll)" value="${false}" styleId="printButton"/>
                <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                        <tr>
                            <td class="button">
                                <c:if test="${size > 0}">
                                    <html:button property="selectedPrint" styleClass="button" onclick="printSelected()"
                                                 tabindex="8">
                                        <fmt:message key="Invoice.print.printSelected"/>
                                    </html:button>
                                    <html:button property="allPrint" styleClass="button" onclick="printAll()"
                                                 tabindex="9">
                                        <fmt:message key="Invoice.print.printAll"/>
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
                <fanta:table list="invoicePrintList"
                             width="100%"
                             id="invoice"
                             action="Invoice/RangePrintList.do"
                             imgPath="${baselayout}"
                             align="center" withCheckBox="true">

                    <c:if test="${size > 0}">
                        <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                              property="documentId" headerStyle="listHeader"
                                              styleClass="radio listItemCenter" width="5%"/>
                    </c:if>

                    <fanta:dataColumn name="number" styleClass="listItem" title="Invoice.number"
                                      headerStyle="listHeader"
                                      width="16%"
                                      orderable="true"
                                      maxLength="40"/>
                    <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                                      headerStyle="listHeader"
                                      width="15%"
                                      orderable="true"/>
                    <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                                      headerStyle="listHeader"
                                      width="10%"
                                      orderable="true"/>
                    <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                                      headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                        <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                        pattern="${datePattern}"/>
                        ${dateValue}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="paymentDate" styleClass="listItem" title="Invoice.paymentDate"
                                      headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                        <fmt:formatDate var="paymentDateValue" value="${app2:intToDate(invoice.paymentDate)}"
                                        pattern="${datePattern}"/>
                        ${paymentDateValue}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                                      headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                        <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                          pattern="${numberFormat}"/>
                        ${totalAmountNet}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight"
                                      title="Invoice.totalAmountGross"
                                      headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                        <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                          pattern="${numberFormat}"/>
                        ${totalAmountGross}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                                      headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                        <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                          pattern="${numberFormat}"/>
                        ${openAmount}
                    </fanta:dataColumn>
                </fanta:table>

            </td>
        </tr>
        <tr>
            <td>
                <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                    <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                        <tr>
                            <td class="button">
                                <c:if test="${size > 0}">
                                    <html:button property="selectedPrint" styleClass="button" onclick="printSelected()"
                                                 tabindex="10">
                                        <fmt:message key="Invoice.print.printSelected"/>
                                    </html:button>
                                    <html:button property="allPrint" styleClass="button" onclick="printAll()"
                                                 tabindex="11">
                                        <fmt:message key="Invoice.print.printAll"/>
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