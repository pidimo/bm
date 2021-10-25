<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getListWrapperClasses()}" id="tableId">

    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="CREATE">
        <div class="${app2:getFormGroupClasses()}">
            <app:url
                    value="/ProductContractBySale/Forward/Create.do?salePositionId=${param.salePositionId}&productName=${app2:encode(param.productName)}&customerName=${app2:encode(param.customerName)}"
                    var="url"/>
            <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </div>
    </app2:checkAccessRight>

    <legend class="title">
        <fmt:message key="${windowTitle}"/>
    </legend>

    <app:url value="ProductContractBySale/List" var="listUrl" enableEncodeURL="false" context="request"/>
    <c:set var="editUrl" value="ProductContractBySale/Forward/Update.do" scope="request"/>
    <c:set var="deleteUrl" value="ProductContractBySale/Forward/Delete.do" scope="request"/>
    <c:import url="/WEB-INF/jsp/sales/ProductContractFragmentList.jsp"/>

</div>