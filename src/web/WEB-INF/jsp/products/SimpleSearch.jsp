<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>
<%
    request.getSession().removeAttribute("values");
%>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>

<legend class="title">
    <fmt:message key="Product.Title.SimpleSearch"/>
</legend>


<html:form action="/SimpleSearch.do" focus="parameter(productName)" styleClass="form-horizontal">
    <div class="${app2:getSearchWrapperClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(productName)" styleClass="largeText ${app2:getFormInputClasses()}"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
            </div>
        </div>
        <div class="pull-left">
            <app:link action="/AdvancedSearch.do?advancedListForward=ProductAdvancedSearch" styleClass="btn btn-link"><fmt:message
                    key="Common.advancedSearch"/></app:link>
        </div>
    </div>
</html:form>
<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="SimpleSearch.do" mode="bootstrap" parameterName="productNameSingle"/>
</div>

<app2:checkAccessRight functionality="PRODUCT" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
       <div class="${app2:getFormButtonWrapperClasses()}">
           <app:url value="/Product/Forward/Create.do"
                    addModuleParams="false" var="newProductUrl"/>
           <input type="button" class="button ${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                  onclick="window.location ='${newProductUrl}'">
       </div>
    </c:set>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="productList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%" id="product" action="SimpleSearch.do" imgPath="${baselayout}">
        <c:set var="editLink"
               value="Product/Forward/Update.do?productId=${product.id}&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
        <c:set var="deleteLink"
               value="Product/Forward/Delete.do?productId=${product.id}&dto(withReferences)=true&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
        <c:set var="translateAction"
               value="Product/Forward/Translate.do?productId=${product.id}&dto(op)=readUserTranslations&dto(productName)=${app2:encode(product.name)}&dto(productId)=${product.id}"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

            <fanta:actionColumn name="trans"
                                title="Common.translate"
                                action="${translateAction}"
                                styleClass="listItem"
                                headerStyle="listHeader"
                                glyphiconClass="${app2:getClassGlyphRefresh()}"/>

            <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                    headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="name" action="${editLink}" styleClass="listItem" title="Contact.name"
                          headerStyle="listHeader" width="25%" orderable="true" maxLength="40"/>
        <fanta:dataColumn name="type" styleClass="listItem" title="Product.type" headerStyle="listHeader"
                          width="20%" orderable="true" renderData="false">
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
                          width="20%" orderable="true"/>
        <fanta:dataColumn name="unit" styleClass="listItem" title="Product.unit" headerStyle="listHeader"
                          width="20%" orderable="true"/>
        <fanta:dataColumn name="price" styleClass="listItem2Right" title="Product.priceNet"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <fmt:formatNumber var="numberValue" value="${product.price}" type="number"
                              pattern="${numberFormat}"/>
            ${numberValue}
        </fanta:dataColumn>

    </fanta:table>
</div>
<c:out value="${newButtonsTable}" escapeXml="false"/>
