<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function check() {
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

    //form management
    function createToAll() {
        document.getElementById('allButton').value = true;
        goSubmit();
    }

    function createToSelected() {
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

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/Invoice/ToRemind/List.do"
               focus="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
               styleClass="form-horizontal">

        <legend class="title">
            <fmt:message key="Invoice.toRemind"/>
        </legend>

        <div class="${app2:getSearchWrapperClasses()}">
            <div class="form-group col-sm-5">
                <label class="${app2:getFormLabelClasses()} label-left" for="search_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:text
                            property="parameter(number@_invoiceAddressName1@_invoiceAddressName2@_invoiceAddressName3@_invoiceAddressSearchName)"
                            styleClass="${app2:getFormInputClasses()} largeText"
                            maxlength="80" tabindex="1"/>
                </div>
            </div>

            <div class="form-group col-sm-5">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="Invoice.toRemind.level"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <fmt:message key="Invoice.notReminderedYet" var="notReminderedLabel"/>
                    <c:set var="notRemindered" value="<%=SalesConstants.INVOICE_NOT_REMINDERED_YET%>"/>
                    <fanta:select property="parameter(toRemindLevel)"
                                  listName="reminderLevelList"
                                  labelProperty="name"
                                  valueProperty="level"
                                  styleClass="${app2:getFormSelectClasses()} middleSelect"
                                  module="/catalogs"
                                  firstEmpty="true" tabIndex="2">
                        <fanta:addItem key="${notRemindered}" value="${notReminderedLabel}"/>
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>

            <div class="${app2:getFormGroupTwoSearchButton()}">
                <div class="col-sm-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="3">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/Invoice/ToRemind/List.do"
                            mode="bootstrap"
                            parameterName="alphabetName1"/>
        </div>
    </html:form>

    <%
        if (request.getAttribute("invoiceToRemindList") != null) {
            ResultList resultList = (ResultList) request.getAttribute("invoiceToRemindList");
            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
        }
    %>

    <html:form action="/Invoice/ToRemind.do" styleId="formList" onsubmit="return false;">

        <html:hidden property="dto(allInvoiceIds)" value="${allListInvoiceIds}" styleId="allInvoiceIds_id"/>
        <html:hidden property="dto(isCreateAll)" value="${false}" styleId="allButton"/>

        <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
            <c:if test="${size > 0}">
                <div class="row">
                    <div class="col-xs-12">
                        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="createToSelected()"
                                     tabindex="3">
                            <fmt:message key="Invoice.toRemind.createForSelected"/>
                        </html:button>
                        <html:button property=""
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="createToAll()"
                                     tabindex="4">
                            <fmt:message key="Invoice.toRemind.createForAll"/>
                        </html:button>
                    </div>
                </div>
            </c:if>
        </app2:checkAccessRight>

        <c:set var="maxLevel" value="${app2:getMaxReminderLevel(pageContext.request)}"/>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" list="invoiceToRemindList"
                         width="100%"
                         id="invoice"
                         styleClass="${app2:getFantabulousTableLargeClases()}"
                         action="Invoice/ToRemind/List.do"
                         imgPath="${baselayout}"
                         align="center" withCheckBox="true">

                <c:set var="editLink"
                       value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>

                <%--address link--%>
                <tags:addressEditContextRelativeUrl varName="addressEditLink" addressId="${invoice.addressId}" addressType="${invoice.addressType}" name1="${invoice.addressName1}" name2="${invoice.addressName2}" name3="${invoice.addressName3}"/>

                <c:if test="${size > 0}">
                    <c:set var="isLastLevel" value="${invoice.reminderLevel >= maxLevel}"/>
                    <c:remove var="lightClass"/>
                    <c:if test="${isLastLevel}">
                        <c:set var="lightClass" value=" listItemHighlight"/>
                    </c:if>

                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                          property="invoiceId" headerStyle="listHeader"
                                          styleClass="listItemCenter${lightClass}" width="5%"
                                          disabled="${isLastLevel}"/>
                </c:if>

                <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem${lightClass}"
                                  title="Invoice.number"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="invoiceTitle" styleClass="listItem${lightClass}" title="Invoice.title"
                                  headerStyle="listHeader"
                                  width="12%"
                                  orderable="true"/>
                <fanta:dataColumn name="invoiceDate" styleClass="listItem${lightClass}"
                                  title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="addressName" styleClass="listItem${lightClass}" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true" renderData="false">
                    <fanta:textShorter title="${invoice.addressName}">
                        <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                            <c:out value="${invoice.addressName}"/>
                        </app:link>
                    </fanta:textShorter>
                </fanta:dataColumn>
                <fanta:dataColumn name="currencyName" styleClass="listItem${lightClass}"
                                  title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="8%"
                                  orderable="true"/>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight${lightClass}"
                                  title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItemRight${lightClass}"
                                  title="Invoice.openAmount"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
                <fanta:dataColumn name="levelName" styleClass="listItem${lightClass}"
                                  title="Invoice.reminderLevel"
                                  headerStyle="listHeader" width="9%" orderable="true"/>
                <fanta:dataColumn name="reminderDate" styleClass="listItem2${lightClass}"
                                  title="Invoice.reminderDate"
                                  headerStyle="listHeader" width="9%" orderable="true" renderData="false">
                    <fmt:formatDate var="reminderDateValue" value="${app2:intToDate(invoice.reminderDate)}"
                                    pattern="${datePattern}"/>
                    ${reminderDateValue}
                </fanta:dataColumn>
            </fanta:table>
        </div>

        <app2:checkAccessRight functionality="INVOICEREMINDER" permission="CREATE">
            <c:if test="${size > 0}">
                <div class="row">
                    <div class="col-xs-12">
                        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" onclick="createToSelected()"
                                     tabindex="5">
                            <fmt:message key="Invoice.toRemind.createForSelected"/>
                        </html:button>
                        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton" onclick="createToAll()"
                                     tabindex="6">
                            <fmt:message key="Invoice.toRemind.createForAll"/>
                        </html:button>
                    </div>
                </div>
            </c:if>
        </app2:checkAccessRight>

    </html:form>

</div>