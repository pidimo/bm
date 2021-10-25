<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<script type="text/javascript" language="JavaScript">

    function checkOrUncheckAll(checkbox) {
        var childCheckBoxes = $("input[class='childCheckBoxId']");
        var isChecked = false;
        if ($(checkbox).is(":checked")) {
            isChecked = true;
        }
        for (var i = 0; i < childCheckBoxes.length; i++) {
            $(childCheckBoxes[i]).prop("checked", isChecked);
        }
    }
</script>
<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>
<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param.invoiceId, pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${action}">
        <html:hidden property="dto(invoiceId)" value="${param.invoiceId}" styleId="invoiceId"/>
        <html:hidden property="dto(invoiceNetGross)" value="${invoiceNetGross}" styleId="invoiceNetGrossId"/>

        <html:hidden property="dto(availableInvoicePositions)" styleId="invoicePositions_id"/>

        <html:hidden property="dto(op)" value="${op}"/>
        <fieldset>
            <legend class="title">
                    ${title}
            </legend>
        </fieldset>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="INVOICEPOSITION"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.copy"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="table-responsive">
            <table width="100%" class="${app2:getFantabulousTableClases()}">
                <th class="listHeader" width="5%">
                    <div class="checkbox checkbox-default listItemCheckbox">
                        <html:checkbox property="dto(parentCheckBox)"
                                       styleId="parentCheckBoxId"
                                       onclick="javascript:checkOrUncheckAll(this);"/>
                        <label for="parentCheckBoxId"></label>
                    </div>
                </th>
                <th class="listHeader" width="10%">
                    <fmt:message key="InvoicePosition.number"/>
                </th>
                <th class="listHeader" width="30%">
                    <fmt:message key="InvoicePosition.product"/>
                </th>
                <th class="listHeader" width="15%">
                    <fmt:message key="InvoicePosition.quantity"/>
                </th>
                <c:if test="${useNetPrice}">
                    <th  class="listHeader" width="20%">
                        <fmt:message key="InvoicePosition.unitPrice"/>
                    </th>
                    <th  class="listHeader" width="20%">
                        <fmt:message key="InvoicePosition.totalPrice"/>
                    </th>
                </c:if>
                <c:if test="${useGrossPrice}">
                    <th class="listHeader" width="20%">
                        <fmt:message key="InvoicePosition.unitPriceGross"/>
                    </th>
                    <th class="listHeader" width="20%">
                        <fmt:message key="InvoicePosition.totalPriceGross"/>
                    </th>
                </c:if>
                <c:choose>
                    <c:when test="${not empty creditNoteInvoicePositionForm.dtoMap['sourceInvoicePositions']}">
                        <c:forEach var="sourceInvoicePosition"
                                   varStatus="status"
                                   items="${creditNoteInvoicePositionForm.dtoMap['sourceInvoicePositions']}">
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_positionId)"
                                         value="${sourceInvoicePosition.positionId}" styleId="positionId_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_productName)"
                                         value="${sourceInvoicePosition.productName}" styleId="productName_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_number)"
                                         value="${sourceInvoicePosition.number}" styleId="number_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_quantity)"
                                         value="${sourceInvoicePosition.quantity}" styleId="quantity_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_unitPrice)"
                                         value="${sourceInvoicePosition.unitPrice}" styleId="unitPrice_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_totalPrice)"
                                         value="${sourceInvoicePosition.totalPrice}" styleId="totalPrice_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_unitPriceGross)"
                                         value="${sourceInvoicePosition.unitPriceGross}" styleId="unitPriceGross_${status.index}"/>
                            <html:hidden property="dto(${sourceInvoicePosition.positionId}_totalPriceGross)"
                                         value="${sourceInvoicePosition.totalPriceGross}" styleId="totalPriceGross_${status.index}"/>
                            <tr class="listRow">
                                <td class="listItemCenter">
                                    <div class="checkbox checkbox-default listItemCheckbox">
                                        <html:checkbox property="dto(${sourceInvoicePosition.positionId})"
                                                       value="${sourceInvoicePosition.positionId}"
                                                       styleClass="childCheckBoxId"
                                                       styleId="childCheckBoxId_${status.count}"/>
                                        <label for="childCheckBoxId_${status.count}"></label>
                                    </div>
                                </td>
                                <td class="listItemRight">
                                        ${sourceInvoicePosition.number}
                                </td>
                                <td>
                                    <c:out value="${sourceInvoicePosition.productName}"/>
                                </td>
                                <td class="listItemRight">
                                        ${sourceInvoicePosition.quantity}
                                </td>
                                <c:if test="${useNetPrice}">
                                    <td class="listItemRight">
                                        <fmt:formatNumber var="unitPriceFormatted"
                                                          value="${sourceInvoicePosition.unitPrice}"
                                                          type="number"
                                                          pattern="${numberFormat}"/>
                                            ${unitPriceFormatted}
                                    </td>
                                    <td class="listItemRight">
                                        <fmt:formatNumber var="totalPriceFormatted"
                                                          value="${sourceInvoicePosition.totalPrice}"
                                                          type="number"
                                                          pattern="${numberFormat}"/>
                                            ${totalPriceFormatted}
                                    </td>
                                </c:if>
                                <c:if test="${useGrossPrice}">
                                    <td class="listItemRight">
                                        <fmt:formatNumber var="unitPriceGrossFormatted"
                                                          value="${sourceInvoicePosition.unitPriceGross}"
                                                          type="number"
                                                          pattern="${numberFormat}"/>
                                            ${unitPriceGrossFormatted}
                                    </td>
                                    <td class="listItemRight">
                                        <fmt:formatNumber var="totalPriceGrossFormatted"
                                                          value="${sourceInvoicePosition.totalPriceGross}"
                                                          type="number"
                                                          pattern="${numberFormat}"/>
                                            ${totalPriceGrossFormatted}
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr class="listRow">
                            <td colspan="6">
                                <fmt:message key="Common.list.empty"/>
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </table>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="INVOICEPOSITION"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.copy"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>