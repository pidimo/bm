<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<script type="text/javascript" language="JavaScript">

    function checkOrUncheckAll(checkbox) {
        var childCheckBoxes = $("input[id='childCheckBoxId']");

        var isChecked = false;
        if ($(checkbox).attr("checked")) {
            isChecked = true;
        }

        for (var i = 0; i < childCheckBoxes.length; i++) {
            $(childCheckBoxes[i]).attr("checked", isChecked);
        }
    }
</script>
<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>
<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param.invoiceId, pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="${action}">
    <html:hidden property="dto(invoiceId)" value="${param.invoiceId}" styleId="invoiceId"/>
    <html:hidden property="dto(invoiceNetGross)" value="${invoiceNetGross}" styleId="invoiceNetGrossId"/>

    <html:hidden property="dto(availableInvoicePositions)"/>

    <html:hidden property="dto(op)" value="${op}"/>

    <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container">
        <tr>
            <TD class="button">
                <app2:securitySubmit operation="${op}" functionality="INVOICEPOSITION" styleClass="button">
                    <fmt:message key="Common.copy"/>
                </app2:securitySubmit>
                <html:cancel styleClass="button">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </tr>
        <tr>
            <td class="title">
                    ${title}
            </td>
        </tr>
        <tr>
            <td>
                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                    <th class="listHeader" width="5%">
                        <html:checkbox property="dto(parentCheckBox)"
                                       styleClass="radio"
                                       styleId="parentCheckBoxId"
                                       onclick="javascript:checkOrUncheckAll(this);"/>
                    </th>
                    <th class="listHeader" width="10%" align="center">
                        <fmt:message key="InvoicePosition.number"/>
                    </th>
                    <th class="listHeader" width="30%" align="center">
                        <fmt:message key="InvoicePosition.product"/>
                    </th>
                    <th class="listHeader" width="15%" align="center">
                        <fmt:message key="InvoicePosition.quantity"/>
                    </th>
                    <c:if test="${useNetPrice}">
                        <th class="listHeader" width="20%" align="center">
                            <fmt:message key="InvoicePosition.unitPrice"/>
                        </th>
                        <th class="listHeader" width="20%" align="center">
                            <fmt:message key="InvoicePosition.totalPrice"/>
                        </th>
                    </c:if>
                    <c:if test="${useGrossPrice}">
                        <th class="listHeader" width="20%" align="center">
                            <fmt:message key="InvoicePosition.unitPriceGross"/>
                        </th>
                        <th class="listHeader" width="20%" align="center">
                            <fmt:message key="InvoicePosition.totalPriceGross"/>
                        </th>
                    </c:if>
                    <c:choose>
                        <c:when test="${not empty creditNoteInvoicePositionForm.dtoMap['sourceInvoicePositions']}">
                            <c:forEach var="sourceInvoicePosition"
                                       items="${creditNoteInvoicePositionForm.dtoMap['sourceInvoicePositions']}">
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_positionId)"
                                             value="${sourceInvoicePosition.positionId}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_productName)"
                                             value="${sourceInvoicePosition.productName}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_number)"
                                             value="${sourceInvoicePosition.number}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_quantity)"
                                             value="${sourceInvoicePosition.quantity}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_unitPrice)"
                                             value="${sourceInvoicePosition.unitPrice}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_totalPrice)"
                                             value="${sourceInvoicePosition.totalPrice}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_unitPriceGross)"
                                             value="${sourceInvoicePosition.unitPriceGross}"/>
                                <html:hidden property="dto(${sourceInvoicePosition.positionId}_totalPriceGross)"
                                             value="${sourceInvoicePosition.totalPriceGross}"/>
                                <tr class="listRow">
                                    <td class='listItemCenter'>
                                        <html:checkbox property="dto(${sourceInvoicePosition.positionId})"
                                                       value="${sourceInvoicePosition.positionId}"
                                                       styleClass="radio"
                                                       styleId="childCheckBoxId"/>
                                    </td>
                                    <td class="listItemRight">
                                            ${sourceInvoicePosition.number}
                                    </td>
                                    <td class="listItem">
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
                                    &nbsp;<fmt:message key="Common.list.empty"/>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </table>
            </td>
        </tr>
        <tr>
            <TD class="button">
                <app2:securitySubmit operation="${op}" functionality="INVOICEPOSITION" styleClass="button">
                    <fmt:message key="Common.copy"/>
                </app2:securitySubmit>
                <html:cancel styleClass="button">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </tr>
    </table>
</html:form>