<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="actionNetGross"
       value="${app2:getNetGrossFieldFromAction(param['dto(contactId)'],param['dto(processId)'] ,pageContext.request)}"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>

<c:set var="useNetPrice" value="${netPrice == actionNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == actionNetGross}"/>

<c:if test="${isSalesProcess==true}">
    <app:url value="/SalesProcess/ActionPosition/List.do" var="urlList" enableEncodeURL="false"/>
</c:if>
<c:if test="${isSalesProcess==false}">
    <app:url value="/SalesProcess/ActionPosition/List.do?processId=${param['dto(processId)']}" var="urlList"
             enableEncodeURL="false"/>
</c:if>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%" class="container" align="center" id="tableId">
    <tr>
        <td class="title">
            <fmt:message key="SalesProcess.actionPositions"/>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table action="${urlList}"
                         list="actionPositionList"
                         width="100%"
                         id="actionPosition"
                         imgPath="${baselayout}"
                         withContext="false">

                <app:url
                        value="/SalesProcess/ActionPosition/Forward/Update.do?cmd=true&dto(positionId)=${actionPosition.positionId}&dto(productName)=${app2:encode(actionPosition.productName)}"
                        var="urlUpdate"/>

                <app:url
                        value="/SalesProcess/ActionPosition/Forward/Delete.do?cmd=true&dto(positionId)=${actionPosition.positionId}&dto(productName)=${app2:encode(actionPosition.productName)}"
                        var="urlDelete"/>

                <fanta:columnGroup title="Common.action"
                                   headerStyle="listHeader"
                                   width="5%">
                    <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="VIEW">
                        <fanta:actionColumn useJScript="true"
                                            action="javascript:goParentURL('${urlUpdate}')"
                                            title="Common.update"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/edit.gif"
                                            name="del"
                                            label="Common.update">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESSPOSITION" permission="DELETE">
                        <fanta:actionColumn useJScript="true"
                                            action="javascript:goParentURL('${urlDelete}')"
                                            title="Common.delete"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            width="50%"
                                            image="${baselayout}/img/delete.gif"
                                            name="del"
                                            label="Common.delete">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="number"
                                  useJScript="true"
                                  action="javascript:goParentURL('${urlUpdate}')"
                                  styleClass="listItem"
                                  title="ActionPosition.number"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"
                                  maxLength="25"/>
                <fanta:dataColumn name="productName"
                                  useJScript="true"
                                  action="javascript:goParentURL('${urlUpdate}')"
                                  styleClass="listItem"
                                  title="ActionPosition.product"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"
                                  maxLength="25"/>
                <fanta:dataColumn name="unit"
                                  styleClass="listItem"
                                  title="ActionPosition.unit"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"/>
                <fanta:dataColumn name="amount"
                                  styleClass="listItemRight"
                                  title="ActionPosition.quantity"
                                  headerStyle="listHeader"
                                  width="20%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="numberValue" value="${actionPosition.amount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${numberValue}&nbsp;
                </fanta:dataColumn>
                <c:if test="${useNetPrice}">
                    <fanta:dataColumn name="price"
                                      styleClass="listItemRight"
                                      title="ActionPosition.unitPriceNet"
                                      headerStyle="listHeader"
                                      width="15%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="numberValue" value="${actionPosition.price}" type="number"
                                          pattern="${numberFormat4Decimal}"/>
                        ${numberValue}&nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPrice"
                                      styleClass="listItem2Right"
                                      title="ActionPosition.totalPriceNet"
                                      headerStyle="listHeader"
                                      width="15%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="numberValue" value="${actionPosition.totalPrice}"
                                          type="number"
                                          pattern="${numberFormat}"/>
                        ${numberValue}&nbsp;
                    </fanta:dataColumn>
                </c:if>
                <c:if test="${useGrossPrice}">
                    <fanta:dataColumn name="unitPriceGross"
                                      styleClass="listItemRight"
                                      title="ActionPosition.unitPriceGross"
                                      headerStyle="listHeader"
                                      width="15%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="numberValue" value="${actionPosition.unitPriceGross}"
                                          type="number"
                                          pattern="${numberFormat4Decimal}"/>
                        ${numberValue}&nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="totalPriceGross"
                                      styleClass="listItem2Right"
                                      title="ActionPosition.totalPriceGross"
                                      headerStyle="listHeader"
                                      width="15%"
                                      orderable="true"
                                      renderData="false">
                        <fmt:formatNumber var="numberValue" value="${actionPosition.totalPriceGross}"
                                          type="number"
                                          pattern="${numberFormat}"/>
                        ${numberValue}&nbsp;
                    </fanta:dataColumn>
                </c:if>
            </fanta:table>
        </td>
    </tr>
</table>


<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>