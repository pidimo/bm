<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>

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

<c:set var="isReadOnly" value="${'delete' == op || param.cancelledContract}"/>

<c:if test="${empty param.payMethod  || param.payMethod == partialPeriodic || param.payMethod == partialFixed}">
    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="openAmount_id">
                <fmt:message key="Contract.openAmount"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <app:numberText property="dto(openAmount)"
                                styleClass="${app2:getFormInputClasses()} numberText"
                                maxlength="12"
                                styleId="openAmount_id"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                view="true"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>

        <c:if test="${param.cancelledContract}">
            <div class="form-group col-xs-12 paddingRemove">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="cancelledContract_id">
                    <fmt:message key="Contract.cancelled"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="checkbox checkbox-default">
                        <html:checkbox property="dto(cancelledContract)"
                                       styleId="cancelledContract_id"
                                       disabled="${op == 'delete'}"
                                       tabindex="16"/>
                        <label for="cancelledContract_id"></label>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </c:if>
    </div>

    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="groupingId">
                <fmt:message key="ProductContract.grouping"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                <c:choose>
                    <c:when test="${isReadOnly}">
                        <app:text styleClass="${app2:getFormInputClasses()}" property="dto(grouping)" view="true"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(groupingSelect)"
                                      listName="productContractGroupingList"
                                      labelProperty="grouping"
                                      valueProperty="grouping"
                                      styleClass="${app2:getFormSelectClasses()}"
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
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${param.payMethod == single}">
    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="openAmount_id">
                <fmt:message key="Contract.openAmount"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                <app:numberText property="dto(openAmount)"
                                styleClass="${app2:getFormInputClasses()} numberText"
                                maxlength="12"
                                styleId="openAmount_id"
                                numberType="decimal"
                                maxInt="10"
                                maxFloat="2"
                                view="true"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>

        <c:if test="${param.cancelledContract}">
            <div class="form-group col-xs-12 paddingRemove">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="cancelledContract_id">
                    <fmt:message key="Contract.cancelled"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <div class="checkbox checkbox-default">
                        <html:checkbox property="dto(cancelledContract)"
                                       styleId="cancelledContract_id"
                                       disabled="${op == 'delete'}"
                                       tabindex="16"/>
                        <label for="cancelledContract_id"></label>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </c:if>
    </div>

    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="groupingId">
                <fmt:message key="ProductContract.grouping"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                <c:choose>
                    <c:when test="${isReadOnly}">
                        <app:text styleClass="${app2:getFormInputClasses()}" property="dto(grouping)" view="true"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(groupingSelect)"
                                      listName="productContractGroupingList"
                                      labelProperty="grouping"
                                      styleClass="${app2:getFormSelectClasses()}"
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
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
</c:if>

<c:if test="${param.payMethod == periodic}">
    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}">
                <fmt:message key="Contract.openAmount"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(true)}"></div>
        </div>
    </div>

    <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
        <div class="form-group col-xs-12 paddingRemove">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="groupingId">
                <fmt:message key="ProductContract.grouping"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                <c:choose>
                    <c:when test="${isReadOnly}">
                        <app:text styleClass="${app2:getFormInputClasses()}" property="dto(grouping)" view="true"/>
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
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
</c:if>

<c:choose>
    <c:when test="${empty param.payMethod  || param.payMethod == single}">

    </c:when>
    <c:otherwise>

        <legend class="title col-xs-12">
            <fmt:message key="ProductContract.title.payMethod"/>
        </legend>

        <c:if test="${param.payMethod == periodic}">
            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payPeriod_id">
                        <fmt:message key="ProductContract.payPeriod"/>
                        <c:out value="("/><fmt:message key="ProductContract.months"/><c:out value=")"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(payPeriod)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="2"
                                        styleId="payPeriod_id"
                                        numberType="integer"
                                        view="${isReadOnly}"
                                        tabindex="${tabIndex+2}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceUntil">
                        <fmt:message key="ProductContract.invoiceUntil"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <c:if test="${!(isReadOnly)}">
                        <div class="input-group date">
                            </c:if>
                            <app:dateText property="dto(invoicedUntil)"
                                          styleId="invoiceUntil"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="${tabIndex+4}"
                                          view="${isReadOnly}"/>

                            <c:if test="${!(isReadOnly)}">
                        </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceDelay_id">
                        <fmt:message key="ProductContract.invoiceDelay"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(invoiceDelay)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        style="width:30"
                                        styleId="invoiceDelay_id"
                                        maxlength="8"
                                        numberType="integer"
                                        view="${isReadOnly}"
                                        tabindex="${tabIndex+6}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:set var="isDisabledMatchCalendar" value="true"/>
                <c:if test="${'true' == param.showMatchCalendarPeriod}">
                    <c:set var="isDisabledMatchCalendar" value="false"/>
                </c:if>

                <html:hidden property="dto(showMatchCalendarPeriod)" styleId="showMatchCalendarPeriodId"/>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="matchCalendarPeriod_id">
                        <fmt:message key="ProductContract.mathCalendarPeriod"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <html:select property="dto(matchCalendarPeriod)"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     readonly="${isReadOnly}"
                                     disabled="${isDisabledMatchCalendar}"
                                     styleId="matchCalendarPeriod_id"
                                     tabindex="${tabIndex+8}">
                            <html:option value=""/>
                            <html:options collection="matchCalendarPeriodList"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </div>

            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payStartDate">
                        <fmt:message key="ProductContract.payStartDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        <c:if test="${!(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        <div class="input-group date">
                            </c:if>
                            <app:dateText property="dto(payStartDate)"
                                          styleId="payStartDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="${tabIndex+3}"
                                          view="${isReadOnly || 'true' == param.hasInvoicePositions}"/>
                            <c:if test="${!(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="contractEndDate">
                        <fmt:message key="ProductContract.contractEndDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <c:if test="${!(isReadOnly)}">
                        <div class="input-group date">
                            </c:if>
                            <app:dateText property="dto(contractEndDate)"
                                          styleId="contractEndDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          mode="bootstrap"
                                          maxlength="10"
                                          tabindex="${tabIndex+5}"
                                          view="${isReadOnly}"/>
                            <c:if test="${!(isReadOnly)}">
                        </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="daysToRemind_id">
                        <fmt:message key="ProductContract.endRemiderDays"/>
                    </label>
                        <%--<td class="contain" ${'true' == param.showMatchCalendarPeriod ? "" : " colspan='3'"}>--%>
                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(daysToRemind)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        style="width:30"
                                        styleId="daysToRemind_id"
                                        maxlength="3"
                                        numberType="integer"
                                        view="${isReadOnly}"
                                        tabindex="${tabIndex+7}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </div>
        </c:if>

        <c:if test="${param.payMethod == partialPeriodic}">
            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <c:set var="leftTabIndex" value="${tabIndex + param.installment + 2}"/>
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payPeriod_id">
                        <fmt:message key="ProductContract.payPeriod"/>
                        <c:out value="("/><fmt:message key="ProductContract.months"/><c:out value=")"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(payPeriod)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="2"
                                        styleId="payPeriod_id"
                                        numberType="integer"
                                        view="${isReadOnly}"
                                        tabindex="${tabIndex+2}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="installment_id">
                        <fmt:message key="ProductContract.installment"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(installment)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        maxlength="3"
                                        styleId="installment_id"
                                        numberType="integer"
                                        view="${isReadOnly}" tabindex="${tabIndex+4}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payStartDate">
                        <fmt:message key="ProductContract.payStartDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        <c:if test="${!(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        <div class="input-group date">
                            </c:if>
                            <app:dateText property="dto(payStartDate)"
                                          styleId="payStartDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="${tabIndex+3}"
                                          view="${isReadOnly || 'true' == param.hasInvoicePositions}"/>
                            <c:if test="${!(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceUntil">
                        <fmt:message key="ProductContract.invoiceUntil"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <c:if test="${!(isReadOnly)}">
                        <div class="input-group date">
                            </c:if>
                            <app:dateText property="dto(invoicedUntil)"
                                          styleId="invoiceUntil"
                                          calendarPicker="${'delete' != op}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          maxlength="10"
                                          mode="bootstrap"
                                          tabindex="${tabIndex+5}"
                                          view="${isReadOnly}"/>

                            <c:if test="${!(isReadOnly)}">
                        </div>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

        </c:if>

        <c:if test="${param.payMethod == partialFixed}">

            <html:hidden property="dto(steptsInvoicedCounter)" styleId="steptsInvoicedCounterId"/>

            <c:set var="leftTabIndex" value="${tabIndex + 2}"/>
            <c:if test="${not empty param.installment && app2:isInteger(param.installment) && param.installment > 0}">
                <c:set var="leftTabIndex" value="${tabIndex + param.installment + 2}"/>
            </c:if>

            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="installmentId">
                        <fmt:message key="ProductContract.installment"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly)}">
                        <app:numberText property="dto(installment)"
                                        styleClass="${app2:getFormInputClasses()} numberText"
                                        styleId="installmentId"
                                        maxlength="3"
                                        numberType="integer"
                                        view="${isReadOnly}"
                                        tabindex="${tabIndex+2}"
                                        onchange="javascript:addRow(this);"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-12 col-md-6 paddingRemove">
                <div class="form-group col-xs-12 paddingRemove">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="amounTypeId">
                        <fmt:message key="ProductContract.amountType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == param.hasInvoicePositions)}">
                        <c:if test="${'true' == param.hasInvoicePositions}">
                            <html:hidden property="dto(amounTypeBK)" value="${param.amounType}"
                                         styleId="amounTypeId"/>
                        </c:if>

                        <html:select property="dto(amounType)"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     styleId="amounTypeId"
                                     readonly="${isReadOnly || 'true' == param.hasInvoicePositions}"
                                     tabindex="${tabIndex+2}"
                                     onchange="javascript:showPercentageSymbol(this);">
                            <html:option value=""/>
                            <html:options collection="amountTypes"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:if test="${not empty param.installment && app2:isInteger(param.installment) && param.installment > 0}">

                <c:forEach begin="1" end="${param.installment}" varStatus="i">

                    <html:hidden property="dto(payStepId_${i.count})" styleId="paymentStep_${i.count}"/>
                    <html:hidden property="dto(hasInvoicePosition_${i.count})" styleId="hasInvoicePosition_${i.count}"/>
                    <html:hidden property="dto(totalPriceFromInvoicePosition_${i.count})"
                                 styleId="totalPriceFromInvoicePosition_${i.count}"/>
                    <c:set var="invoicePosition" value="hasInvoicePosition_${i.count}"/>
                    <c:set var="payAmount" value="payAmount_${i.count}"/>

                    <div class="col-xs-12 paddingRemove">

                        <div class="${app2:getFormGroupClassesTwoColumns()} paddingRemove">
                            <%--<div class="col-xs-12">--%>
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="payAmountId_${i.count}">
                                    <fmt:message key="ProductContract.paymentStep.amount"/>
                                    <c:choose>
                                        <c:when test="${percentage == param.amounType && 'delete' != op}">
                                            <c:out value="%"/>
                                        </c:when>
                                        <c:when test="${percentage == param.amounType && isReadOnly && not empty param[payAmount]}">
                                            <c:out value="%"/>
                                        </c:when>
                                        <c:otherwise>
                                            &nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == param[invoicePosition])}">
                                    <app:numberText property="dto(payAmount_${i.count})"
                                                    styleId="payAmountId_${i.count}"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    tabindex="${tabIndex+2}"
                                                    view="${isReadOnly || 'true' == param[invoicePosition]}"/>
                                    <!--tabIndex+i.count+2-->
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            <%--</div>--%>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()} paddingRemove paddingaa">
                            <%--<div class="col-xs-12">--%>
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="payDateId_${i.count}">
                                    <fmt:message key="ProductContract.paymentStep.payDate"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || 'true' == param[invoicePosition])}">
                                    <c:if test="${!(isReadOnly || 'true' == param[invoicePosition])}">
                                    <div class="input-group date">
                                        </c:if>
                                        <app:dateText property="dto(payDate_${i.count})"
                                                      styleId="payDateId_${i.count}"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      maxlength="10"
                                                      mode="bootstrap"
                                                      tabindex="${tabIndex+2}"
                                                      view="${isReadOnly || 'true' == param[invoicePosition]}"/>
                                        <c:if test="${!(isReadOnly || 'true' == param[invoicePosition])}">
                                    </div>
                                    </c:if>

                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        <%--</div>--%>
                    </div>

                </c:forEach>

            </c:if>
        </c:if>

    </c:otherwise>
</c:choose>
