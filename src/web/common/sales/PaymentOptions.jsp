<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>


<calendar:initialize/>


${app2:setValuesFromAjaxRequest(pageContext, pageContext.request)}

<c:set var="single" value="<%=SalesConstants.PayMethod.Single.getConstantAsString()%>"/>
<c:set var="periodic" value="<%=SalesConstants.PayMethod.Periodic.getConstantAsString()%>"/>
<c:set var="partialPeriodic" value="<%=SalesConstants.PayMethod.PartialPeriodic.getConstantAsString()%>"/>
<c:set var="partialFixed" value="<%=SalesConstants.PayMethod.PartialFixed.getConstantAsString()%>"/>

<c:set var="amount" value="<%=SalesConstants.AmounType.AMOUNT.getConstantAsString()%>"/>
<c:set var="percentage" value="<%=SalesConstants.AmounType.PERCENTAGE.getConstantAsString()%>"/>

<c:set var="matchCalendarPeriodList" value="${app2:getMatchCalendarPeriodOptions(pageContext.request)}"/>
<c:set var="amountTypes" value="${app2:getAmounTypes(pageContext.request)}"/>

<c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>

<fmt:message var="datePattern" key="datePattern"/>

<c:if test="${empty labelWidth}">
    <c:set var="labelWidth" value="18%"/>
</c:if>

<c:if test="${empty containWidth}">
    <c:set var="containWidth" value="32%"/>
</c:if>
<c:if test="${empty tabIndex}">
    <c:set var="tabIndex" value="17"/>
</c:if>


<c:if test="${empty param.payMethod  || param.payMethod == partialPeriodic || param.payMethod == partialFixed}">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td class="label" width="${labelWidth}">
                <fmt:message key="Contract.openAmount"/>
            </td>
            <td class="contain" width="${containWidth}">
                <app:numberText property="dto(openAmount)"
                                styleClass="numberText"
                                maxlength="12"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                view="true"/>
            </td>
            <td class="label" width="${labelWidth}">
                <fmt:message key="ProductContract.grouping"/>
            </td>
            <td class="contain" width="${containWidth}">
                <c:choose>
                    <c:when test="${'delete' == op}">
                        <app:text property="dto(grouping)" view="true"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(groupingSelect)"
                                      listName="productContractGroupingList"
                                      labelProperty="grouping"
                                      valueProperty="grouping"
                                      styleId="groupingId"
                                      module="/sales"
                                      tabIndex="16">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <c:choose>
                                <c:when test="${not empty param.addressId}">
                                    <fanta:parameter field="addressId" value="${param.addressId}"/>
                                </c:when>
                                <c:otherwise>
                                    <fanta:parameter field="addressId" value="-100"/>
                                </c:otherwise>
                            </c:choose>
                        </fanta:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</c:if>
<c:if test="${param.payMethod == single}">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td class="label" width="${labelWidth}">
                <fmt:message key="Contract.openAmount"/>
            </td>
            <td class="contain" width="${containWidth}">
                <app:numberText property="dto(openAmount)"
                                styleClass="numberText"
                                maxlength="12"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                view="true"/>
            </td>
            <td class="label" width="${labelWidth}">
                <fmt:message key="ProductContract.grouping"/>
            </td>
            <td class="contain" width="${containWidth}">
                <c:choose>
                    <c:when test="${'delete' == op}">
                        <app:text property="dto(grouping)" view="true"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(groupingSelect)"
                                      listName="productContractGroupingList"
                                      labelProperty="grouping"
                                      valueProperty="grouping"
                                      styleId="groupingId"
                                      module="/sales"
                                      tabIndex="16">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <c:choose>
                                <c:when test="${not empty param.addressId}">
                                    <fanta:parameter field="addressId" value="${param.addressId}"/>
                                </c:when>
                                <c:otherwise>
                                    <fanta:parameter field="addressId" value="-100"/>
                                </c:otherwise>
                            </c:choose>
                        </fanta:select>
                    </c:otherwise>
                </c:choose>
            </td>

        </tr>
    </table>
</c:if>
<c:if test="${param.payMethod == periodic}">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td class="label" width="${labelWidth}">
                <fmt:message key="Contract.openAmount"/>
            </td>
            <td class="contain" width="${containWidth}">
                &nbsp;
            </td>
            <td class="label" width="${labelWidth}">
                <fmt:message key="ProductContract.grouping"/>
            </td>
            <td class="contain" nowrap="true">
                <c:choose>
                    <c:when test="${'delete' == op}">
                        <app:text property="dto(grouping)" view="true"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(groupingSelect)"
                                      listName="productContractGroupingList"
                                      labelProperty="grouping"
                                      valueProperty="grouping"
                                      styleId="groupingId"
                                      module="/sales"
                                      tabIndex="16">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <c:choose>
                                <c:when test="${not empty param.addressId}">
                                    <fanta:parameter field="addressId" value="${param.addressId}"/>
                                </c:when>
                                <c:otherwise>
                                    <fanta:parameter field="addressId" value="-100"/>
                                </c:otherwise>
                            </c:choose>
                        </fanta:select>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </table>
</c:if>
<c:choose>
<c:when test="${empty param.payMethod  || param.payMethod == single}">
</c:when>
<c:otherwise>
<table border="0" cellpadding="0" cellspacing="0" class="container" width="100%">
<tr>
    <td class="title" colspan="4">
        <fmt:message key="ProductContract.title.payMethod"/>
    </td>
</tr>

<c:if test="${param.payMethod == periodic}">
    <tr>
        <td class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.payPeriod"/>
        </td>
        <td class="contain" width="${containWidth}">
            <app:numberText property="dto(payPeriod)"
                            styleClass="numberText"
                            maxlength="2"
                            numberType="integer"
                            view="${'delete' == op}"
                            tabindex="${tabIndex+2}"/>&nbsp;<fmt:message key="ProductContract.months"/>
        </td>
        <td class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.payStartDate"/>
        </td>
        <td class="contain" width="${containWidth}">
            <app:dateText property="dto(payStartDate)"
                          styleId="payStartDate"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          tabindex="${tabIndex+5}"
                          view="${'delete' == op || 'true' == param.hasInvoicePositions}"/>
        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProductContract.invoiceUntil"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(invoicedUntil)"
                          styleId="invoiceUntil"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          tabindex="${tabIndex+3}"
                          view="${'delete' == op}"/>
        </td>
        <td class="label">
            <fmt:message key="ProductContract.contractEndDate"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(contractEndDate)"
                          styleId="contractEndDate"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          tabindex="${tabIndex+6}"
                          view="${'delete' == op}"/>

        </td>
    </tr>
    <tr>
        <td class="label">
            <fmt:message key="ProductContract.endRemiderDays"/>
        </td>
        <td class="contain" ${'true' == param.showMatchCalendarPeriod ? "" : " colspan='3'"}>
                <%--remider part--%>
            <app:numberText property="dto(daysToRemind)"
                            styleClass="numberText"
                            style="width:30"
                            maxlength="3"
                            numberType="integer"
                            view="${'delete' == op}"
                            tabindex="${tabIndex+4}"/>
        </td>
        <c:if test="${'true' == param.showMatchCalendarPeriod}">
            <html:hidden property="dto(showMatchCalendarPeriod)" styleId="showMatchCalendarPeriodId"/>
            <td class="label">
                <fmt:message key="ProductContract.mathCalendarPeriod"/>
            </td>
            <td class="contain">
                <html:select property="dto(matchCalendarPeriod)"
                             styleClass="mediumSelect"
                             readonly="${'delete' == op}"
                             tabindex="${tabIndex+7}">
                    <html:option value=""/>
                    <html:options collection="matchCalendarPeriodList"
                                  property="value"
                                  labelProperty="label"/>
                </html:select>
            </td>
        </c:if>
    </tr>

</c:if>

<c:if test="${param.payMethod == partialPeriodic}">
    <tr>
        <td class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.payPeriod"/>
        </td>
        <td class="contain" width="${containWidth}">
            <app:numberText property="dto(payPeriod)"
                            styleClass="numberText"
                            maxlength="2"
                            numberType="integer"
                            view="${'delete' == op}"
                            tabindex="${tabIndex+2}"/>&nbsp;<fmt:message key="ProductContract.months"/>
        </td>
        <td class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.payStartDate"/>
        </td>
        <td class="contain" width="${containWidth}">
            <app:dateText property="dto(payStartDate)"
                          styleId="payStartDate"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          tabindex="${tabIndex+4}"
                          view="${'delete' == op || 'true' == param.hasInvoicePositions}"/>
        </td>
    </tr>
    <tr>
        <TD class="label">
            <fmt:message key="ProductContract.installment"/>
        </TD>
        <TD class="contain">
            <app:numberText property="dto(installment)"
                            styleClass="numberText"
                            maxlength="3"
                            numberType="integer"
                            view="${'delete' == op}" tabindex="${tabIndex+3}"/>
        </TD>
        <td class="label">
            <fmt:message key="ProductContract.invoiceUntil"/>
        </td>
        <td class="contain">
            <app:dateText property="dto(invoicedUntil)"
                          styleId="invoiceUntil"
                          calendarPicker="${'delete' != op}"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="10"
                          tabindex="${tabIndex+5}"
                          view="${'delete' == op}"/>
        </td>
    </tr>
</c:if>

<c:if test="${param.payMethod == partialFixed}">

    <html:hidden property="dto(steptsInvoicedCounter)" styleId="steptsInvoicedCounterId"/>

    <c:set var="leftTabIndex" value="${tabIndex + 2}"/>
    <c:if test="${not empty param.installment && app2:isInteger(param.installment) && param.installment > 0}">
        <c:set var="leftTabIndex" value="${tabIndex + param.installment + 2}"/>
    </c:if>
    <tr>
        <TD class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.installment"/>
        </TD>
        <TD class="contain" width="${containWidth}">
            <app:numberText property="dto(installment)"
                            styleClass="numberText"
                            styleId="installmentId"
                            maxlength="3"
                            numberType="integer"
                            view="${'delete' == op}"
                            tabindex="${tabIndex+2}"
                            onchange="javascript:addRow(this);"/>
        </TD>
        <td class="label" width="${labelWidth}">
            <fmt:message key="ProductContract.amountType"/>
        </td>
        <td class="contain" width="${containWidth}">
            <c:if test="${'true' == param.hasInvoicePositions}">
                <html:hidden property="dto(amounTypeBK)" value="${param.amounType}"
                             styleId="amounTypeId"/>
            </c:if>

            <html:select property="dto(amounType)"
                         styleClass="mediumSelect"
                         styleId="amounTypeId"
                         readonly="${'delete' == op || 'true' == param.hasInvoicePositions}"
                         tabindex="${leftTabIndex+1}"
                         onchange="javascript:showPercentageSymbol(this);">
                <html:option value=""/>
                <html:options collection="amountTypes"
                              property="value"
                              labelProperty="label"/>
            </html:select>
        </td>
    </tr>

    <c:if test="${not empty param.installment && app2:isInteger(param.installment) && param.installment > 0}">

        <c:forEach begin="1" end="${param.installment}" varStatus="i">

            <html:hidden property="dto(payStepId_${i.count})" styleId="paymentStep_${i.count}"/>
            <html:hidden property="dto(hasInvoicePosition_${i.count})" styleId="hasInvoicePosition_${i.count}"/>
            <html:hidden property="dto(totalPriceFromInvoicePosition_${i.count})"
                         styleId="totalPriceFromInvoicePosition_${i.count}"/>
            <c:set var="invoicePosition" value="hasInvoicePosition_${i.count}"/>
            <c:set var="payAmount" value="payAmount_${i.count}"/>
            <tr>
                <td class="label">
                    <fmt:message key="ProductContract.paymentStep.amount"/>
                </td>
                <td class="contain">
                    <app:numberText property="dto(payAmount_${i.count})"
                                    styleId="payAmountId_${i.count}"
                                    styleClass="numberText"
                                    maxlength="12"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"
                                    tabindex="${tabIndex+i.count+2}"
                                    view="${'delete' == op || 'true' == param[invoicePosition]}"/>

                    <c:choose>
                        <c:when test="${percentage == param.amounType && 'delete' != op}">
                            <c:out value="%"/>
                        </c:when>
                        <c:when test="${percentage == param.amounType && 'delete' == op && not empty param[payAmount]}">
                            <c:out value="%"/>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>

                </td>
                <td class="label">
                    <fmt:message key="ProductContract.paymentStep.payDate"/>
                </td>
                <td class="contain">
                    <app:dateText property="dto(payDate_${i.count})"
                                  styleId="payDateId_${i.count}"
                                  calendarPicker="${op != 'delete'}"
                                  datePatternKey="${datePattern}"
                                  styleClass="text"
                                  maxlength="10"
                                  tabindex="${leftTabIndex+i.count+2}"
                                  view="${'delete' == op || 'true' == param[invoicePosition]}"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</c:if>
</table>
</c:otherwise>
</c:choose>
