<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapFile/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProductId_id, fieldProductName_id, field_versionNumber, field_price, field_unitId"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="singleWithoutContract" value="<%=SalesConstants.PayMethod.SingleWithoutContract.getConstantAsString()%>"/>
<c:set var="canChangePayMethod" value="${salePositionForm.dtoMap['canChangePayMethod']}" scope="request"/>
<c:set var="categoryTabLinkUrl"
       value="/sales/SalesProcess/SalePosition/CategoryTab/Forward/Update.do?index=${param.index}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&salePositionId=${salePositionForm.dtoMap['salePositionId']}"
       scope="request"/>

<c:import url="/WEB-INF/jsp/sales/SalePositionFormFragment.jsp"/>

<c:if test="${singleWithoutContract != salePositionForm.dtoMap['copyPayMethod']}">
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <c:if test="${'update' == op}">
            <div class="embed-responsive embed-responsive-16by9 col-xs-12">
                <iframe name="frame1"
                        src="<app:url value="SalesProcess/ProductContractBySalePosition/List.do?salePositionId=${salePositionForm.dtoMap['salePositionId']}&dto(salePositionId)=${salePositionForm.dtoMap['salePositionId']}&productName=${app2:encode(salePositionForm.dtoMap['productName'])}"/>"
                        class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </div>
        </c:if>
    </app2:checkAccessRight>
</c:if>
