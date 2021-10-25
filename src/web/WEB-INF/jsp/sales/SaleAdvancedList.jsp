<%@ include file="/Includes.jsp" %>

<script>
    function resetFields() {
        var form = document.saleAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>
<div class="${app2:getListWrapperClasses()}">
    <c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
    <fmt:message key="datePattern" var="datePattern"/>
    <tags:initBootstrapDatepicker/>
    <html:form action="/Sale/AdvancedList.do" focus="parameter(title)" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Sale.advancedSearch"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                            <fmt:message key="Sale.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(title)" styleId="title_id"
                                       styleClass="mediumText ${app2:getFormInputClasses()}" tabindex="1"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sellerId_id">
                            <fmt:message key="Sale.seller"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(sellerId)"
                                          styleId="sellerId_id"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/contacts"
                                          firstEmpty="true"
                                          tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}"
                               for="customerAddressName1@_customerAddressName2@_customerAddressName3_id">
                            <fmt:message key="Sale.customer"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(customerAddressName1@_customerAddressName2@_customerAddressName3)"
                                    styleId="customerAddressName1@_customerAddressName2@_customerAddressName3_id"
                                    styleClass="mediumText ${app2:getFormInputClasses()}"
                                    maxlength="80" tabindex="3"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productGroupId_id">
                            <fmt:message key="Sale.productGroup"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productGroupId)"
                                          styleId="productGroupId_id"
                                          listName="productGroupList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="4">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}"
                               for="contactPersonName1@_contactPersonName2@_contactPersonName3_id">
                            <fmt:message key="Sale.contactPerson"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(contactPersonName1@_contactPersonName2@_contactPersonName3)"
                                    styleId="contactPersonName1@_contactPersonName2@_contactPersonName3_id"
                                    styleClass="mediumText ${app2:getFormInputClasses()}"
                                    maxlength="80" tabindex="5"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productTypeId_id">
                            <fmt:message key="Sale.productType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(productTypeId)"
                                          styleId="productTypeId_id"
                                          listName="productTypeList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                            <fmt:message key="Sale.product"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text
                                    property="parameter(productName)"
                                    styleId="productName_id"
                                    styleClass="mediumText ${app2:getFormInputClasses()}"
                                    maxlength="80" tabindex="7"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="saleDateFrom">
                            <fmt:message key="Sale.saleDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-sm-6 wrapperButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(saleDateFrom)"
                                                      maxlength="10" styleId="saleDateFrom"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      placeHolder="${from}"
                                                      mode="bootstrap"
                                                      convert="true" tabindex="8"/>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(saleDateTo)"
                                                      maxlength="10" styleId="saleDateTo"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="dateText ${app2:getFormInputClasses()}"
                                                      placeHolder="${to}"
                                                      mode="bootstrap"
                                                      convert="true" tabindex="9"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
            <div class="row">
                <div class="col-xs-12">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="10"><fmt:message
                            key="Common.go"/></html:submit>
                    <html:button property="reset1" tabindex="11" styleClass="button ${app2:getFormButtonClasses()}"
                                 onclick="resetFields();">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </div>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="/Sale/AdvancedList.do"
                            mode="bootstrap"
                            parameterName="alphabetTitle"/>

        </div>
    </html:form>
    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/Sale/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="saleAdvancedList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="sale"
                     action="Sale/AdvancedList.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="Sale/Forward/Update.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&index=0"/>
            <c:set var="deleteLink"
                   value="Sale/Forward/Delete.do?saleId=${sale.saleId}&dto(saleId)=${sale.saleId}&dto(title)=${app2:encode(sale.title)}&dto(withReferences)=true&index=0"/>

            <%--customer link--%>
            <tags:addressEditContextRelativeUrl varName="customerEditLink" addressId="${sale.customerAddressId}" addressType="${sale.customerAddressType}" name1="${sale.customerName1}" name2="${sale.customerName2}" name3="${sale.customerName3}"/>

            <%--contact person link--%>
            <c:if test="${not empty sale.contactPersonId}">
                <c:set var="editContactPersonUrl"
                       value="/contacts/ContactPerson/Forward/Update.do?contactId=${sale.customerAddressId}&dto(addressId)=${sale.customerAddressId}&dto(contactPersonId)=${sale.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
            </c:if>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editLink}" styleClass="listItem" title="Sale.title"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="customerName" styleClass="listItem" title="Sale.customer"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <fanta:textShorter title="${sale.customerName}">
                    <app:link action="${customerEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${sale.customerName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Sale.contactPerson"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <fanta:textShorter title="${sale.contactPersonName}">
                    <app:link action="${editContactPersonUrl}" contextRelative="true" addModuleName="false">
                        <c:out value="${sale.contactPersonName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="sellerName" styleClass="listItem" title="Sale.seller"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="saleDate" styleClass="listItem2" title="Sale.saleDate"
                              headerStyle="listHeader" width="20%" orderable="true" renderData="false">
                <fmt:formatDate var="saleDateValue" value="${app2:intToDate(sale.saleDate)}"
                                pattern="${datePattern}"/>
                ${saleDateValue}
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <app2:checkAccessRight functionality="SALE" permission="CREATE">
        <html:form action="/Sale/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>