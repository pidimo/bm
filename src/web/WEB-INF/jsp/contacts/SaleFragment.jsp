<%@ include file="/Includes.jsp" %>

<%--
Documentation:
         showSalesProcessOption        : if true, then show SalesProcessAction select box.
         readOnlySalesProcessOption    : if true, the SalesProcessAction select box showed as read only view.

         Exists function changeAction() this is javaSript function and make submit of de form when the page is imported;
         this function is used in the salesProcessOption in the onchange property.
--%>

<script language="JavaScript" type="text/javascript">
    function changeAction() {
        document.forms[0].submit();
    }
</script>

<tags:initBootstrapDatepicker/>
<tags:initSelectPopup/>

<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="title_id">
        <fmt:message key="Sale.title"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <app:text property="dto(title)"
                  styleId="title_id"
                  styleClass="${app2:getFormInputClasses()} middleText" maxlength="40" tabindex="1"
                  view="${'delete' == op}"/>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<c:if test="${true == showSalesProcessOption}">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="contactId_id">
            <fmt:message key="Sale.salesProcessAction"/>
        </label>

        <div class="${app2:getFormContainClasses(true == readOnlySalesProcessOption)}">
            <fanta:select property="dto(contactId)"
                          styleId="contactId_id"
                          listName="actionSelectList"
                          module="/sales"
                          firstEmpty="true"
                          labelProperty="note"
                          valueProperty="contactId"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          readOnly="${true == readOnlySalesProcessOption}"
                          onChange="javascript:changeAction();"
                          tabIndex="2">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="processId" value="${saleObject.dtoMap['processId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="contactPersonId_id">
        <fmt:message key="Sale.contactPerson"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                      tabIndex="3"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleObject.dtoMap['addressId']?saleObject.dtoMap['addressId']:param.contactId}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="sellerId_id">
        <fmt:message key="Sale.seller"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(sellerId)"
                      styleId="sellerId_id"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                      value="${sessionScope.user.valueMap['userAddressId']}"
                      module="/contacts"
                      tabIndex="4"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="sentAddressId_id">
        <fmt:message key="Sale.sentAddress"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(sentAddressId)" styleId="sentAddressId_id" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                      readOnly="${op == 'delete'}"
                      onChange="javascript:changeAction();"
                      module="/contacts" tabIndex="5" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleObject.dtoMap['addressId'] ? saleObject.dtoMap['addressId'] : param.contactId}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="sentContactPersonId_id">
        <fmt:message key="Sale.sentContactPerson"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(sentContactPersonId)" styleId="sentContactPersonId_id"
                      listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                      tabIndex="6"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleObject.dtoMap['sentAddressId'] ? saleObject.dtoMap['sentAddressId'] : 0}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="additionalAddressId_id">
        <fmt:message key="Sale.additionalAddress"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(additionalAddressId)" styleId="additionalAddressId_id"
                      listName="additionalAddressSelectList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                      firstEmpty="true" styleClass="${app2:getFormSelectClasses()} middleSelect"
                      readOnly="${'delete' == op}" tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty saleObject.dtoMap['addressId'] ? saleObject.dtoMap['addressId'] : param.contactId}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<c:if test="${not empty saleObject.dtoMap['processId']}">
    <html:hidden property="dto(processId)"/>
    <html:hidden property="dto(contactId)"/>
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelClasses()}" for="processName_id">
            <fmt:message key="Sale.salesProcess"/>
        </label>

        <div class="${app2:getFormContainClasses('update' == op && not empty saleObject.dtoMap['processId'])}">
            <app:text property="dto(processName)"
                      styleId="processName_id"
                      styleClass="${app2:getFormInputClasses()} middleText"
                      maxlength="40"
                      tabindex="1"
                      view="true"/>

            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:if test="${'update' == op && not empty saleObject.dtoMap['processId']}">
                    <c:set var="processEditLink"
                           value="/sales/SalesProcess/Forward/Update.do?processId=${saleObject.dtoMap['processId']}&dto(processId)=${saleObject.dtoMap['processId']}&dto(processName)=${app2:encode(saleObject.dtoMap['processName'])}&addressId=${saleObject.dtoMap['addressId']}&index=0"/>
                   <span class="pull-right">
                        <app:link action="${processEditLink}" contextRelative="true" titleKey="Common.edit">
                            <span class="glyphicon glyphicon-edit"></span>
                        </app:link>
                   </span>
                </c:if>
            </app2:checkAccessRight>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</c:if>
<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="startDate">
        <fmt:message key="Sale.saleDate"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <div class="input-group date">
            <app:dateText property="dto(saleDate)"
                          styleId="startDate"
                          mode="bootstrap"
                          calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}"
                          styleClass="${app2:getFormInputClasses()} text"
                          maxlength="10"
                          tabindex="8"
                          currentDate="true" view="${'delete' == op}"/>
        </div>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="currencyId_id">
        <fmt:message key="Sale.currency"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <fanta:select property="dto(currencyId)"
                      styleId="currencyId_id"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                      module="/catalogs"
                      readOnly="${op == 'delete'}"
                      tabIndex="9">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="netGross_id">
        <fmt:message key="Sale.netGross"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleId="netGross_id"
                     styleClass="${app2:getFormSelectClasses()} middleSelect"
                     readonly="${op == 'delete'}"
                     tabindex="10">
            <html:option value=""/>
            <html:options collection="netGrossOptions" property="value" labelProperty="label"/>
        </html:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${app2:getFormLabelClasses()}" for="text_id">
        <fmt:message key="Sale.text"/>
    </label>

    <div class="${app2:getFormContainClasses('delete' == op)}">
        <html:textarea property="dto(text)"
                       styleId="text_id"
                       styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                       style="height:120px;"
                       readonly="${'delete'== op}"
                       tabindex="11"/>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<tags:jQueryValidation formName="saleForm"/>
