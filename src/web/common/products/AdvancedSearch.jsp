<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>
<c:set var="eventTypeId" value="${app2:findProductTypeIdOfEventType(pageContext.request)}"/>

<calendar:initialize/>

<script>
    function myReset() {
        var form = document.listForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }

    function checkProductType(box) {
        if('${eventTypeId}' == box.value) {
            document.getElementById('tr_eventDateId').style.display = "";
        } else {
            document.getElementById('tr_eventDateId').style.display = "none";
            document.getElementById('startDate').value = "";
            document.getElementById('endDate').value = "";
        }
    }

</script>

<br>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Product.Title.AdvancedSearch"/></td>
    </tr>
    <html:form action="/AdvancedSearch.do" focus="parameter(productName)">
        <TR>
            <TD width="15%" class="label"><fmt:message key="Product.name"/></TD>
            <TD class="contain" width="35%">
                <html:text property="parameter(productName)" styleClass="largeText" tabindex="1"/>
            </TD>
            <TD width="15%" class="label"><fmt:message key="Product.supplier"/></TD>
            <TD width="35%" class="contain">
                <app:supplierSelect property="parameter(supplierId)" styleClass="largeSelect" tabindex="3"/>
            </TD>
        </TR>
        <TR>
            <TD class="label"><fmt:message key="Product.type"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(typeId)"
                              listName="productTypeList"
                              firstEmpty="true"
                              labelProperty="name"
                              valueProperty="id"
                              module="/catalogs"
                              styleClass="largeSelect"
                              preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                              onChange="checkProductType(this);"
                              tabIndex="2">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
            <TD class="label"><fmt:message key="Product.group"/></TD>
            <TD class="contain">
                <fanta:select property="parameter(groupId)" listName="productGroupList" firstEmpty="true"
                              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="largeSelect"
                              readOnly="${op == 'delete'}" tabIndex="4">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                </fanta:select>
            </TD>
        </TR>

        <c:set var="displayEventFields" value="display: none;"/>
        <c:if test="${eventTypeId eq productAdvancedListForm.params['typeId']}">
            <c:set var="displayEventFields" value=""/>
        </c:if>

        <tr id="tr_eventDateId" style="${displayEventFields}">
            <td class="label"><fmt:message key="Product.eventDate"/></td>
            <td class="contain" colspan="3">
                <fmt:message key="Common.from"/>
                &nbsp;
                <fmt:message key="datePattern" var="datePattern"/>
                <app:dateText property="parameter(eventDateFrom)" maxlength="10" tabindex="2" styleId="startDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                &nbsp;<fmt:message key="Common.to"/>&nbsp;
                <app:dateText property="parameter(eventDateTo)" maxlength="10" tabindex="2" styleId="endDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" align="center" class="alpha">
                <fanta:alphabet action="AdvancedSearch.do" parameterName="productNameSingle"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="11"><fmt:message key="Common.go"/></html:submit>
                <html:button property="reset1" tabindex="12" styleClass="button" onclick="myReset()"><fmt:message
                        key="Common.clear"/></html:button>
            </td>
        </tr>
    </html:form>
    <tr>
        <td colspan="4" align="center">
            <br/>
            <app2:checkAccessRight functionality="PRODUCT" permission="CREATE">
                <c:set var="newButtonsTable" scope="page">
                    <tags:buttonsTable>
                        <app:url value="/Product/Forward/Create.do?advancedListForward=ProductAdvancedSearch"
                                 addModuleParams="false" var="newProductUrl"/>
                        <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newProductUrl}'">
                    </tags:buttonsTable>
                </c:set>
            </app2:checkAccessRight>
            <c:out value="${newButtonsTable}" escapeXml="false"/>
            <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
            <fanta:table width="100%" id="product" action="AdvancedSearch.do" imgPath="${baselayout}">
                <c:set var="editLink"
                       value="Product/Forward/Update.do?productId=${product.id}&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
                <c:set var="deleteLink"
                       value="Product/Forward/Delete.do?productId=${product.id}&dto(withReferences)=true&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                        <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                            headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="name" action="${editLink}" styleClass="listItem" title="Product.name"
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

