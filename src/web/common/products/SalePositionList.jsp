<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
                <TR>
                    <td class="label">
                        <fmt:message key="Common.search"/>
                    </td>
                    <html:form action="/SalePosition/List.do"
                               focus="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)">
                        <td class="contain">
                            <html:text
                                    property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                                    styleClass="largeText"/>
                            &nbsp;
                            <html:submit styleClass="button">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                            &nbsp;
                        </td>
                    </html:form>
                </TR>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="SalePosition/List.do"
                                        parameterName="customerAddressName1"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <html:form action="/SalePosition/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>

    <tr>
        <td>
            <fanta:table list="salePositionForProductsList"
                         width="100%"
                         id="salePosition"
                         action="SalePosition/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="SalePosition/Forward/Update.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(customerName)=${app2:encode(salePosition.customerName)}"/>
                <c:set var="deleteLink"
                       value="SalePosition/Forward/Delete.do?salePositionId=${salePosition.salePositionId}&dto(salePositionId)=${salePosition.salePositionId}&dto(customerName)=${app2:encode(salePosition.customerName)}&dto(withReferences)=true"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
                        <fanta:actionColumn name="edit"
                                            title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALEPOSITION" permission="DELETE">
                        <fanta:actionColumn name="delete"
                                            title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="customerName" action="${editLink}" styleClass="listItem"
                                  title="SalePosition.customer"
                                  headerStyle="listHeader" width="17%" orderable="true"/>
                <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="SalePosition.contactPerson"
                                  headerStyle="listHeader" width="17%" orderable="true"/>
                <fanta:dataColumn name="unitName" styleClass="listItem" title="SalePosition.unitName"
                                  headerStyle="listHeader" width="9%" orderable="true"
                                  maxLength="40"/>
                <fanta:dataColumn name="quantity"
                                  styleClass="listItemRight"
                                  title="SalePosition.quantity"
                                  headerStyle="listHeader"
                                  width="9%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="quantityFormatted" value="${salePosition.quantity}" type="number"
                                      pattern="${numberFormat}"/>
                    ${quantityFormatted}
                </fanta:dataColumn>

                <fanta:dataColumn name="unitPrice"
                                  styleClass="listItemRight"
                                  title="SalePosition.unitPrice"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="false"
                                  renderData="false">
                    <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                    <c:choose>
                        <c:when test="${null != salePosition.saleId}">
                            <c:if test="${null == salePosition.netGross}">
                                <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                            </c:if>
                            <c:if test="${null != salePosition.netGross && netPrice == salePosition.netGross}">
                                <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                            </c:if>
                            <c:if test="${null != salePosition.netGross && grossPrice == salePosition.netGross}">
                                <c:set var="unitPriceValue" value="${salePosition.unitPriceGross}"/>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:set var="unitPriceValue" value="${salePosition.unitPrice}"/>
                        </c:otherwise>
                    </c:choose>

                    <fmt:formatNumber var="unitPriceFormatted" value="${unitPriceValue}" type="number"
                                      pattern="${numberFormat4Decimal}"/>
                    ${unitPriceFormatted}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalPrice"
                                  styleClass="listItemRight"
                                  title="SalePosition.totalPrice"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="false"
                                  renderData="false">
                    <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                    <c:choose>
                        <c:when test="${null != salePosition.saleId}">
                            <c:if test="${null == salePosition.netGross}">
                                <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                            </c:if>
                            <c:if test="${null != salePosition.netGross && netPrice == salePosition.netGross}">
                                <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                            </c:if>
                            <c:if test="${null != salePosition.netGross && grossPrice == salePosition.netGross}">
                                <c:set var="totalPriceValue" value="${salePosition.totalPriceGross}"/>
                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <c:set var="totalPriceValue" value="${salePosition.totalPrice}"/>
                        </c:otherwise>
                    </c:choose>
                    <fmt:formatNumber var="totalPriceFormatted" value="${totalPriceValue}"
                                      type="number"
                                      pattern="${numberFormat}"/>
                    ${totalPriceFormatted}
                </fanta:dataColumn>

                <fanta:dataColumn name="deliveryDate" styleClass="listItem" title="SalePosition.deliveryDate"
                                  headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                    <fmt:formatDate var="deliveryDateValue" value="${app2:intToDate(salePosition.deliveryDate)}"
                                    pattern="${datePattern}"/>
                    ${deliveryDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="active" styleClass="listItem2" title="SalePosition.active"
                                  headerStyle="listHeader" width="10%" renderData="false">
                    <c:if test="${salePosition.active == 1}">
                        <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>
                    </c:if>&nbsp;
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>

    <tr>
        <app2:checkAccessRight functionality="SALEPOSITION" permission="CREATE">
            <html:form action="/SalePosition/Forward/Create.do">
                <td class="button">
                    <html:submit styleClass="button">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </td>
            </html:form>
        </app2:checkAccessRight>
    </tr>

</table>