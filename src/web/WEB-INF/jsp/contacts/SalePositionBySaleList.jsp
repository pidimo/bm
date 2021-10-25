<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">
    <div id="tableId">
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <app:url value="SalePositionBySale/Forward/Create.do" var="url"/>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(new)"
                             onclick="window.parent.location='${url}'">
                    <fmt:message key="Common.new"/>
                </html:button>
            </div>
        </app2:checkAccessRight>
        <fieldset>
            <legend class="title">
                <fmt:message key="${windowTitle}"/>
            </legend>
        </fieldset>

        <app:url value="SalePositionBySale/List" var="listUrl" enableEncodeURL="false"
                 context="request"/>
        <c:set var="useJavaScript" value="true" scope="request"/>
        <c:set var="editUrl" value="SalePositionBySale/Forward/Update.do" scope="request"/>
        <c:set var="deleteUrl" value="SalePositionBySale/Forward/Delete.do" scope="request"/>
        <c:import url="/WEB-INF/jsp/contacts/SalePositionBySaleFragmentList.jsp"/>
    </div>
</div>

