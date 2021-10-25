<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<%--url to send invoice via email--%>
<app2:jScriptUrl
        url="${urlSendViaEmail}?invoiceId=${dto.lastInvoiceId}"
        var="jsSendViaEmailUrl">
    <app2:jScriptUrlParam param="telecomId" value="telecomId"/>
</app2:jScriptUrl>


<script language="JavaScript" type="text/javascript">
<!--
function mergeInvoiceDocuments() {
    location.href = '${urlMergeDocument}';
}

function sendViaEmail(emailBoxId){
    var telecomId = document.getElementById(emailBoxId).value;
    location.href = ${jsSendViaEmailUrl};
}

//-->
</script>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
    <tr>
        <td class="button">
            <c:set var="isSendViaEmail"
                   value="${not empty urlSendViaEmail and dto.totalCreated == 1 and not empty dto.lastInvoiceId and app2:hasDefaultMailAccount(pageContext.request)}"/>
            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:button property="" styleClass="button" onclick="sendViaEmail('upToEmail_id')">
                        <fmt:message key="ContractToInvoice.summary.sendViaEmail"/>
                    </html:button>
                    <app:telecomSelect property="toEmail"
                                       styleId="upToEmail_id"
                                       telecomIdColumn="telecomid"
                                       numberColumn="telecomnumber"
                                       telecomType="${TELECOMTYPE_EMAIL}"
                                       addressId="${dto.lastInvoiceAddressId}"
                                       contactPersonId="${dto.lastInvoiceContactPersonId}"
                                       showOwner="true"
                                       styleClass="select"
                                       optionStyleClass="list" showDescription="false"
                                       selectPredetermined="true"/>
                </app2:checkAccessRight>
            </c:if>
            <html:button property="" styleClass="button" onclick="location.href='${urlAccept}'">
                <fmt:message key="ContractToInvoice.summary.accept"/>
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="50%" align="center" class="container">
    <tr>
        <td colspan="2" class="title">
            <fmt:message key="ContractToInvoice.summary.invoiced"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="40%">
            <fmt:message key="ContractToInvoice.summary.totalProcessed"/>
        </td>
        <td class="contain" width="60%">
            <c:out value="${dto.totalProcessed}"/>
        </td>
    </tr>

    <c:if test="${dto.totalValidContract > 0}">
        <tr>
            <td class="label">
                <fmt:message key="ContractToInvoice.summary.totalValid"/>
            </td>
            <td class="contain">
                <c:out value="${dto.totalValidContract}"/>
            </td>
        </tr>
    </c:if>
    <c:if test="${dto.totalValidSalePosition > 0}">
        <tr>
            <td class="label">
                <fmt:message key="ContractToInvoice.summary.salePositionTotalValid"/>
            </td>
            <td class="contain">
                <c:out value="${dto.totalValidSalePosition}"/>
            </td>
        </tr>
    </c:if>
    <c:if test="${dto.totalFailed > 0}">
        <tr>
            <td class="label">
                <fmt:message key="ContractToInvoice.summary.totalFail"/>
            </td>
            <td class="contain">
                <c:out value="${dto.totalFailed}"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="label">
            <fmt:message key="ContractToInvoice.summary.totalInvoiceCreated"/>
        </td>
        <td class="contain">
            <c:out value="${dto.totalCreated}"/>
        </td>
    </tr>

    <c:set var="failMap" value="${dto.invalidToInvoiceMap}"/>

    <c:if test="${not empty failMap.failPayCondition}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="ContractToInvoice.summary.fail.withoutPayCondition"/>
            </td>
        </tr>
        <c:forEach var="infoMap" items="${failMap.failPayCondition}">
            <tr>
                <td class="label" colspan="2">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${not empty failMap.failProductAccount}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="ContractToInvoice.summary.fail.withoutProductAccount"/>
            </td>
        </tr>
        <c:forEach var="infoMap" items="${failMap.failProductAccount}">
            <tr>
                <td class="label" colspan="2">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${not empty failMap.failVatRate}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="ContractToInvoice.summary.fail.vatRateNotValid"/>
            </td>
        </tr>
        <c:forEach var="infoMap" items="${failMap.failVatRate}">
            <tr>
                <td class="label" colspan="2">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${not empty failMap.failCurrency}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="ContractToInvoice.summary.fail.withoutCurrency"/>
            </td>
        </tr>
        <c:forEach var="infoMap" items="${failMap.failCurrency}">
            <tr>
                <td class="label" colspan="2">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>

    <c:if test="${not empty failMap.failNetGross}">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="ContractToInvoice.summary.fail.withoutNetGross"/>
            </td>
        </tr>
        <c:forEach var="infoMap" items="${failMap.failNetGross}">
            <tr>
                <td class="label" colspan="2">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/common/finance/InvoicedFailLabelFragment.jsp"/>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    
</table>
<table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
    <tr>
        <td class="button">
            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:button property="" styleClass="button" onclick="sendViaEmail('belowToEmail_id')">
                        <fmt:message key="ContractToInvoice.summary.sendViaEmail"/>
                    </html:button>
                    <app:telecomSelect property="toEmail"
                                       styleId="belowToEmail_id"
                                       telecomIdColumn="telecomid"
                                       numberColumn="telecomnumber"
                                       telecomType="${TELECOMTYPE_EMAIL}"
                                       addressId="${dto.lastInvoiceAddressId}"
                                       contactPersonId="${dto.lastInvoiceContactPersonId}"
                                       showOwner="true"
                                       styleClass="select"
                                       optionStyleClass="list" showDescription="false"
                                       selectPredetermined="true"/>
                </app2:checkAccessRight>
            </c:if>

            <html:button property="" styleClass="button" onclick="location.href='${urlAccept}'">
                <fmt:message key="ContractToInvoice.summary.accept"/>
            </html:button>
        </td>
    </tr>
</table>
