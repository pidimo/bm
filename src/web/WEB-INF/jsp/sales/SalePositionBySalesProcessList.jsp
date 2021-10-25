<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="saleNetGross"
       value="${app2:getNetGrossFieldFromSale(param.saleId ,pageContext.request)}"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == saleNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == saleNetGross}"/>


<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">
    <div id="tableId">
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <app:url
                    value="/SalesProcess/SalePosition/Forward/Create.do?dto(saleId)=${param.saleId}"
                    var="url"/>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(new)"
                             onclick="window.parent.location='${url}'">
                    <fmt:message key="Common.new"/>
                </html:button>
            </div>
        </app2:checkAccessRight>

        <fieldset>
            <legend class="title">
                <fmt:message key="SalePosition.Title.plural"/>
            </legend>
        </fieldset>
        <div class="table-responsive">
            <app:url value="SalesProcess/SalePosition/List.do" var="listAction" enableEncodeURL="false"/>
            <fanta:table mode="bootstrap" list="salePositionList"
                         styleClass="${app2:getFantabulousTableClases()}"
                         width="100%"
                         id="salePosition"
                         action="${listAction}"
                         imgPath="${baselayout}"
                         align="center"
                         withContext="false">

                <app:url
                        value="SalesProcess/SalePosition/Forward/Update.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}&dto(processId)=${param.processId}&dto(saleId)=${salePosition.saleId}"
                        var="editLink"/>
                <app:url
                        value="SalesProcess/SalePosition/Forward/Delete.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(productName)=${app2:encode(salePosition.productName)}&dto(processId)=${param.processId}&dto(saleId)=${salePosition.saleId}&dto(withReferences)=true"
                        var="deleteLink"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            useJScript="true"
                                            action="javascript:goParentURL('${editLink}')"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALEPOSITION" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            useJScript="true"
                                            action="javascript:goParentURL('${deleteLink}')"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            glyphiconClass="${app2:getClassGlyphTrash()}"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="productName"
                                  useJScript="true"
                                  action="javascript:goParentURL('${editLink}')"
                                  styleClass="listItem"
                                  title="SalePosition.productName"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="unitName"
                                  styleClass="listItem"
                                  title="SalePosition.unitName"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="quantity"
                                  styleClass="listItemRight"
                                  title="SalePosition.quantity"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>
                <c:if test="${useNetPrice}">
                    <fanta:dataColumn name="unitPrice"
                                      styleClass="listItemRight"
                                      title="SalePosition.unitPriceNet"
                                      headerStyle="listHeader"
                                      width="12%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="unitPriceFormatted" value="${salePosition.unitPrice}" type="number"
                                          pattern="${numberFormat4Decimal}"/>
                        ${unitPriceFormatted}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPrice"
                                      styleClass="listItemRight"
                                      title="SalePosition.totalPriceNet"
                                      headerStyle="listHeader"
                                      width="12%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="totalPriceFormatted" value="${salePosition.totalPrice}" type="number"
                                          pattern="${numberFormat}"/>
                        ${totalPriceFormatted}
                    </fanta:dataColumn>
                </c:if>
                <c:if test="${useGrossPrice}">
                    <fanta:dataColumn name="unitPriceGross"
                                      styleClass="listItemRight"
                                      title="SalePosition.unitPriceGross"
                                      headerStyle="listHeader"
                                      width="12%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="unitPriceFormatted" value="${salePosition.unitPriceGross}"
                                          type="number"
                                          pattern="${numberFormat4Decimal}"/>
                        ${unitPriceFormatted}
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPriceGross"
                                      styleClass="listItemRight"
                                      title="SalePosition.totalPriceGross"
                                      headerStyle="listHeader"
                                      width="12%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="totalPriceFormatted" value="${salePosition.totalPriceGross}"
                                          type="number"
                                          pattern="${numberFormat}"/>
                        ${totalPriceFormatted}
                    </fanta:dataColumn>
                </c:if>
                <fanta:dataColumn name="deliveryDate" styleClass="listItem" title="SalePosition.deliveryDate"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatDate var="deliveryDateValue" value="${app2:intToDate(salePosition.deliveryDate)}"
                                    pattern="${datePattern}"/>
                    ${deliveryDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="active" styleClass="listItem2Center" title="SalePosition.active"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <c:if test="${salePosition.active == 1}">
                        <span class="${app2:getClassGlyphOk()}"></span>
                    </c:if>&nbsp;
                </fanta:dataColumn>
            </fanta:table>

        </div>
    </div>
</div>
