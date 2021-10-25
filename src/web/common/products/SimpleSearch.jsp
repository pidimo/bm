<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>
<%
    request.getSession().removeAttribute("values");
%>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>

<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Product.Title.SimpleSearch"/>
            <br>
        </td>
    </tr>
    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/SimpleSearch.do" focus="parameter(productName)">
            <td class="contain" nowrap>
                <html:text property="parameter(productName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                <app:link action="/AdvancedSearch.do?advancedListForward=ProductAdvancedSearch">&nbsp;<fmt:message key="Common.advancedSearch"/></app:link>
            </td>
        </html:form>
    </TR>

    <TR>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="SimpleSearch.do" parameterName="productNameSingle"/>
        </td>
    </TR>

    <tr>
        <td colspan="2" align="center">
            <br/>
            <app2:checkAccessRight functionality="PRODUCT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Product/Forward/Create.do"
                                 addModuleParams="false" var="newProductUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newProductUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fanta:table list="productList" width="100%" id="product" action="SimpleSearch.do" imgPath="${baselayout}">
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
                                        image="${baselayout}/img/tournicoti.gif"/>

                    <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
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
            <c:out value="${newButtonsTable}" escapeXml="false"/>
        </td>
    </tr>

</table>

