<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="payMethodList" value="${app2:getPayMethods(pageContext.request)}"/>

<div class="${app2:getListWrapperClasses()}" id="tableId">

    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
        <app:url
                value="/SalesProcess/ProductContractBySalePosition/Forward/Create.do?salePositionId=${param.salePositionId}&productName=${app2:encode(param.productName)}&customerName=${app2:encode(param.customerName)}"
                var="url"/>

        <div class="${app2:getFormGroupClasses()}">
            <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </div>
    </app2:checkAccessRight>

    <legend class="title">
        <fmt:message key="${windowTitle}"/>
    </legend>

    <app:url value="SalesProcess/ProductContractBySalePosition/List.do" var="listUrl"
             enableEncodeURL="false" context="request"/>
    <c:set var="editUrl" value="SalesProcess/ProductContractBySalePosition/Forward/Update.do"
           scope="request"/>
    <c:set var="deleteUrl" value="SalesProcess/ProductContractBySalePosition/Forward/Delete.do"
           scope="request"/>
    <c:import url="/WEB-INF/jsp/sales/ProductContractFragmentList.jsp"/>

</div>
