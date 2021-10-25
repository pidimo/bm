<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/SearchProduct.do" focus="parameter(productName)" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">

            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group col-xs-12">
                    <html:text property="parameter(productName)"
                               styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                       <html:submit styleClass="${app2:getFormButtonClasses()}">
                           <fmt:message key="Common.go"/>
                       </html:submit>
                    </span>
                </div>
            </div>

        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="SearchProduct.do" parameterName="productNameSingle" mode="bootstrap"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" id="product" styleClass="${app2:getFantabulousTableClases()}"
                     action="SearchProduct.do" imgPath="${baselayout}">
            <fmt:formatNumber var="priceFormatted" value="${product.price}" type="number"
                              pattern="${numberFormat}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn useJScript="true"
                                    action="javascript:parent.selectMultipleField('${product.id}', '${app2:jscriptEncode(product.name)}', '${app2:jscriptEncode(product.versionNumber)}', '${priceFormatted}', '${product.unitId}');"
                                    name="" title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                    width="50%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" styleClass="listItem" title="Product.name" headerStyle="listHeader"
                              width="25%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="type" styleClass="listItem" title="Product.type" headerStyle="listHeader"
                              width="15%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${PRODUCTTYPETYPE_EVENT eq product.productTypeType}">
                        <fmt:message key="ProductType.item.event"/>
                    </c:when>
                    <c:otherwise>
                        <c:out value="${product.type}"/>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="group" styleClass="listItem" title="Product.group" headerStyle="listHeader"
                              width="10%" orderable="true"/>
            <fanta:dataColumn name="unit" styleClass="listItem" title="Product.unit" headerStyle="listHeader"
                              width="10%" orderable="true"/>
            <fanta:dataColumn name="price" styleClass="listItem2Right" title="Product.priceNet"
                              headerStyle="listHeader"
                              width="10%" orderable="true" renderData="false">
                ${priceFormatted}
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

