<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="numberFormat4Decimal" key="numberFormat.4DecimalPlaces"/>

<c:set var="netPrice" value="<%=FinanceConstants.NetGrossFLag.NET.getConstantAsString()%>"/>
<c:set var="grossPrice" value="<%=FinanceConstants.NetGrossFLag.GROSS.getConstantAsString()%>"/>
<c:set var="invoiceNetGross" value="${app2:getInvoiceNetGross(param.invoiceId, pageContext.request)}"/>

<c:set var="useNetPrice" value="${netPrice == invoiceNetGross}"/>
<c:set var="useGrossPrice" value="${grossPrice == invoiceNetGross}"/>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="INVOICEPOSITION" permission="CREATE">
        <div class="${app2:getFormGroupClasses()}">
            <c:if test="${app2:creditNoteIsRelatedWithInvoice(param.invoiceId, pageContext.request)}">
                <html:form action="/CreditNoteInvoicePosition/Forward/Create.do">
                    <div class="pull-left marginRight">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.copy"/>
                        </html:submit>
                    </div>
                </html:form>
            </c:if>
            <html:form action="/InvoicePosition/Forward/Create.do">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </html:form>
        </div>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="invoicePositionList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="invoicePosition"
                     action="/InvoicePosition/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="InvoicePosition/Forward/Update.do?positionId=${invoicePosition.positionId}&dto(positionId)=${invoicePosition.positionId}&dto(productName)=${app2:encode(invoicePosition.productName)}"/>
            <c:set var="deleteLink"
                   value="InvoicePosition/Forward/Delete.do?positionId=${invoicePosition.positionId}&dto(positionId)=${invoicePosition.positionId}&dto(productName)=${app2:encode(invoicePosition.productName)}&dto(withReferences)=true"/>

            <%--product links--%>
            <c:set var="productEditLink"
                   value="/products/Product/Forward/Update.do?productId=${invoicePosition.productId}&dto(productId)=${invoicePosition.productId}&dto(productName)=${app2:encode(invoicePosition.productName)}&index=0"/>
            <app2:checkAccessRight functionality="PRODUCT" permission="VIEW" var="hasProductViewPermission"/>


            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="INVOICEPOSITION" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="INVOICEPOSITION" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

            </fanta:columnGroup>
            <fanta:dataColumn name="number"
                              action="${editLink}"
                              styleClass="listItemRight"
                              title="InvoicePosition.number"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"
                              maxLength="40"/>

            <fanta:dataColumn name="productName"
                              styleClass="listItem"
                              title="InvoicePosition.product"
                              headerStyle="listHeader"
                              width="25%"
                              orderable="true"
                              maxLength="40" renderData="false">
                <fanta:textShorter title="${invoicePosition.productName}">
                    <c:choose>
                        <c:when test="${hasProductViewPermission}">
                            <app:link action="${productEditLink}" contextRelative="true">
                                <c:out value="${invoicePosition.productName}"/>
                            </app:link>
                        </c:when>
                        <c:otherwise>
                            <c:out value="${invoicePosition.productName}"/>
                        </c:otherwise>
                    </c:choose>
                </fanta:textShorter>
            </fanta:dataColumn>
            <c:if test="${useNetPrice}">
                <fanta:dataColumn name="unitPrice"
                                  styleClass="listItemRight"
                                  title="InvoicePosition.unitPrice"
                                  headerStyle="listHeader"
                                  width="11%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="unitPriceFormatted" value="${invoicePosition.unitPrice}" type="number"
                                      pattern="${numberFormat4Decimal}"/>
                    ${unitPriceFormatted}
                </fanta:dataColumn>
            </c:if>
            <c:if test="${useGrossPrice}">
                <fanta:dataColumn name="unitPriceGross"
                                  styleClass="listItemRight"
                                  title="InvoicePosition.unitPriceGross"
                                  headerStyle="listHeader"
                                  width="11%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="unitPriceGrossFormatted" value="${invoicePosition.unitPriceGross}"
                                      type="number"
                                      pattern="${numberFormat4Decimal}"/>
                    ${unitPriceGrossFormatted}
                </fanta:dataColumn>
            </c:if>
            <fanta:dataColumn name="quantity"
                              styleClass="listItemRight"
                              title="InvoicePosition.quantity"
                              headerStyle="listHeader"
                              width="11%"
                              orderable="true"
                              renderData="false">
                <fmt:formatNumber var="quantityFormatted" value="${invoicePosition.quantity}" type="number"
                                  pattern="${numberFormat}"/>
                ${quantityFormatted}
            </fanta:dataColumn>
            <c:if test="${useNetPrice}">
                <fanta:dataColumn name="totalPrice"
                                  styleClass="listItemRight"
                                  title="InvoicePosition.totalPrice"
                                  headerStyle="listHeader"
                                  width="11%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="totalPriceFormatted" value="${invoicePosition.totalPrice}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalPriceFormatted}
                </fanta:dataColumn>
            </c:if>
            <c:if test="${useGrossPrice}">
                <fanta:dataColumn name="totalPriceGross"
                                  styleClass="listItemRight"
                                  title="InvoicePosition.totalPriceGross"
                                  headerStyle="listHeader"
                                  width="11%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatNumber var="totalPriceGrossFormatted" value="${invoicePosition.totalPriceGross}"
                                      type="number"
                                      pattern="${numberFormat}"/>
                    ${totalPriceGrossFormatted}
                </fanta:dataColumn>
            </c:if>
            <fanta:dataColumn name="vatLabel"
                              styleClass="listItem"
                              title="InvoicePosition.vat"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"/>

            <fanta:dataColumn name="accountName"
                              styleClass="listItem2"
                              title="InvoicePosition.account"
                              headerStyle="listHeader"
                              width="17%"
                              orderable="true"/>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="INVOICEPOSITION" permission="CREATE">
        <div class="${app2:getFormGroupClasses()}">
            <c:if test="${app2:creditNoteIsRelatedWithInvoice(param.invoiceId, pageContext.request)}">
                <html:form action="/CreditNoteInvoicePosition/Forward/Create.do">
                    <div class="pull-left marginRight">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.copy"/>
                        </html:submit>
                    </div>
                </html:form>
            </c:if>
            <html:form action="/InvoicePosition/Forward/Create.do">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </html:form>
        </div>
    </app2:checkAccessRight>
</div>