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

    function sendViaEmail(emailBoxId) {
        var telecomId = document.getElementById(emailBoxId).value;
        location.href = ${jsSendViaEmailUrl};
    }

    //-->
</script>

<div class="${app2:getFormClasses()}">

    <c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
    <c:set var="isSendViaEmail"
           value="${not empty urlSendViaEmail and dto.totalCreated == 1 and not empty dto.lastInvoiceId and app2:hasDefaultMailAccount(pageContext.request)}"/>

    <div class="row">
        <div class="col-xs-12">

            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">

                    <html:button property=""
                                 styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 onclick="sendViaEmail('upToEmail_id')">
                        <fmt:message key="ContractToInvoice.summary.sendViaEmail"/>
                    </html:button>

                    <div class="form-group col-xs-12 col-sm-4 row">
                        <app:telecomSelect property="toEmail"
                                           styleId="upToEmail_id"
                                           telecomIdColumn="telecomid"
                                           numberColumn="telecomnumber"
                                           telecomType="${TELECOMTYPE_EMAIL}"
                                           addressId="${dto.lastInvoiceAddressId}"
                                           contactPersonId="${dto.lastInvoiceContactPersonId}"
                                           showOwner="true"
                                           styleClass="${app2:getFormSelectClasses()} select"
                                           optionStyleClass="list" showDescription="false"
                                           selectPredetermined="true"/>
                    </div>
                </app2:checkAccessRight>
            </c:if>

            <html:button property=""
                         styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                         onclick="location.href='${urlAccept}'">
                <fmt:message key="ContractToInvoice.summary.accept"/>
            </html:button>
        </div>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>

            <legend class="title">
                <fmt:message key="ContractToInvoice.summary.invoiced"/>
            </legend>

            <div class="${app2:getFormGroupClasses()} row">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="ContractToInvoice.summary.totalProcessed"/>
                </label>
                <div class="${app2:getFormContainClasses(null)}">
                    <c:out value="${dto.totalProcessed}"/>
                </div>
            </div>

            <c:if test="${dto.totalValidContract > 0}">
                <div class="${app2:getFormGroupClasses()} row">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="ContractToInvoice.summary.totalValid"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <c:out value="${dto.totalValidContract}"/>
                    </div>
                </div>
            </c:if>

            <c:if test="${dto.totalValidSalePosition > 0}">
                <div class="${app2:getFormGroupClasses()} row">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="ContractToInvoice.summary.salePositionTotalValid"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <c:out value="${dto.totalValidSalePosition}"/>
                    </div>
                </div>
            </c:if>

            <c:if test="${dto.totalFailed > 0}">
                <div class="${app2:getFormGroupClasses()} row">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="ContractToInvoice.summary.totalFail"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <c:out value="${dto.totalFailed}"/>
                    </div>
                </div>
            </c:if>

            <div class="${app2:getFormGroupClasses()} row">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="ContractToInvoice.summary.totalInvoiceCreated"/>
                </label>
                <div class="${app2:getFormContainClasses(null)}">
                    <c:out value="${dto.totalCreated}"/>
                </div>
            </div>

            <c:set var="failMap" value="${dto.invalidToInvoiceMap}"/>

            <c:if test="${not empty failMap.failPayCondition}">
                <legend class="title">
                    <fmt:message key="ContractToInvoice.summary.fail.withoutPayCondition"/>
                </legend>
                <c:forEach var="infoMap" items="${failMap.failPayCondition}">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failProductAccount}">
                <legend class="title">
                    <fmt:message key="ContractToInvoice.summary.fail.withoutProductAccount"/>
                </legend>

                <c:forEach var="infoMap" items="${failMap.failProductAccount}">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failVatRate}">
                <legend class="title">
                    <fmt:message key="ContractToInvoice.summary.fail.vatRateNotValid"/>
                </legend>

                <c:forEach var="infoMap" items="${failMap.failVatRate}">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failCurrency}">
                <legend class="title">
                    <fmt:message key="ContractToInvoice.summary.fail.withoutCurrency"/>
                </legend>

                <c:forEach var="infoMap" items="${failMap.failCurrency}">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                </c:forEach>
            </c:if>

            <c:if test="${not empty failMap.failNetGross}">
                <legend class="title">
                    <fmt:message key="ContractToInvoice.summary.fail.withoutNetGross"/>
                </legend>
                <c:forEach var="infoMap" items="${failMap.failNetGross}">
                    <c:set var="item" value="${infoMap}" scope="request"/>
                    <c:import url="/WEB-INF/jsp/finance/InvoicedFailLabelFragment.jsp"/>
                </c:forEach>
            </c:if>

        </fieldset>
    </div>

    <div class="row">
        <div class="col-xs-12">

            <c:if test="${isSendViaEmail}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:button property=""
                                 styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                 onclick="sendViaEmail('belowToEmail_id')">
                        <fmt:message key="ContractToInvoice.summary.sendViaEmail"/>
                    </html:button>

                    <div class="form-group col-xs-12 col-sm-4 row">
                        <app:telecomSelect property="toEmail"
                                           styleId="belowToEmail_id"
                                           telecomIdColumn="telecomid"
                                           numberColumn="telecomnumber"
                                           telecomType="${TELECOMTYPE_EMAIL}"
                                           addressId="${dto.lastInvoiceAddressId}"
                                           contactPersonId="${dto.lastInvoiceContactPersonId}"
                                           showOwner="true"
                                           styleClass="select ${app2:getFormSelectClasses()}"
                                           optionStyleClass="list" showDescription="false"
                                           selectPredetermined="true"/>
                    </div>
                </app2:checkAccessRight>
            </c:if>

            <html:button property=""
                         styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                         onclick="location.href='${urlAccept}'">
                <fmt:message key="ContractToInvoice.summary.accept"/>
            </html:button>
        </div>
    </div>

</div>