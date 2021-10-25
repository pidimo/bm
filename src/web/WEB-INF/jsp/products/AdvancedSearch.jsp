<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<c:set var="PRODUCTTYPETYPE_EVENT" value="<%=ProductConstants.ProductTypeType.EVENT.getConstant()%>"/>
<c:set var="eventTypeId" value="${app2:findProductTypeIdOfEventType(pageContext.request)}"/>

<tags:initBootstrapDatepicker/>

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

<html:form action="/AdvancedSearch.do" focus="parameter(productName)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Product.Title.AdvancedSearch"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                        <fmt:message key="Product.name"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(productName)"
                                   styleId="productName_id"
                                   styleClass="largeText ${app2:getFormInputClasses()}" tabindex="1"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Product.supplier"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:supplierSelect property="parameter(supplierId)"
                                            styleClass="largeSelect ${app2:getFormSelectClasses()}" tabindex="2"/>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Product.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(typeId)"
                                      listName="productTypeList"
                                      firstEmpty="true"
                                      labelProperty="name"
                                      valueProperty="id"
                                      module="/catalogs"
                                      styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                      preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                                      onChange="checkProductType(this);"
                                      tabIndex="3">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Product.group"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(groupId)" listName="productGroupList" firstEmpty="true"
                                      labelProperty="name" valueProperty="id" module="/catalogs"
                                      styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete'}" tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                        separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                        </fanta:select>
                    </div>
                </div>

            </div>

            <c:set var="displayEventFields" value="display: none;"/>
            <c:if test="${eventTypeId eq productAdvancedListForm.params['typeId']}">
                <c:set var="displayEventFields" value=""/>
            </c:if>

            <div class="row" id="tr_eventDateId" style="${displayEventFields}">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="Product.eventDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(eventDateFrom)"
                                                  maxlength="10"
                                                  tabindex="5"
                                                  styleId="startDate"
                                                  placeHolder="${from}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message var="to" key="Common.to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(eventDateTo)"
                                                  maxlength="10"
                                                  tabindex="6"
                                                  styleId="endDate"
                                                  placeHolder="${to}"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"
                                                  mode="bootstrap"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="11">
                    <fmt:message key="Common.go"/>
                </html:submit>
                <html:button property="reset1" tabindex="12" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="myReset()"><fmt:message
                        key="Common.clear"/></html:button>
            </div>
        </fieldset>
    </div>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="AdvancedSearch.do" mode="bootstrap" parameterName="productNameSingle"/>
    </div>

</html:form>


<app2:checkAccessRight functionality="PRODUCT" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <tags:buttonsTable>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <app:url value="/Product/Forward/Create.do?advancedListForward=ProductAdvancedSearch"
                         addModuleParams="false" var="newProductUrl"/>
                <input type="button" class="button ${app2:getFormButtonClasses()}"
                       value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newProductUrl}'">
            </div>
        </tags:buttonsTable>
    </c:set>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%"
                 styleClass="${app2:getFantabulousTableClases()}"
                 id="product"
                 action="AdvancedSearch.do"
                 imgPath="${baselayout}">
        <c:set var="editLink"
               value="Product/Forward/Update.do?productId=${product.id}&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
        <c:set var="deleteLink"
               value="Product/Forward/Delete.do?productId=${product.id}&dto(withReferences)=true&dto(productId)=${product.id}&dto(productName)=${app2:encode(product.name)}&index=0"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                <fanta:actionColumn name="edit" title="Common.update" action="${editLink}" styleClass="listItem"
                                    headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
                <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
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
</div>
<c:out value="${newButtonsTable}" escapeXml="false"/>

