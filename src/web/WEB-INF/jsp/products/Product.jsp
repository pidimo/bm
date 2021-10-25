<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapFile/>

<c:set var="eventTypeId" value="${app2:findProductTypeIdOfEventType(pageContext.request)}"/>

<%
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
%>

<app2:jScriptUrl url="/calendar.html" var="jsUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="date" value="field.value"/>
    <app2:jScriptUrlParam param="field" value="field.id"/>
    <app2:jScriptUrlParam param="field_" value="field_.id"/>
</app2:jScriptUrl>

<script language="JavaScript">

    function changeInitDateToEndDateValue() {
        $("#endDate").val($("#startDate").val());
    }

    function checkProductType(box) {
        if ('${eventTypeId}' == box.value) {
            document.getElementById('tr_initId').style.display = "";
            document.getElementById('tr_endId').style.display = "";
            document.getElementById('tr_closingId').style.display = "";
            document.getElementById('td_maxParticipantId').style.display = "";
        } else {
            document.getElementById('tr_initId').style.display = "none";
            document.getElementById('tr_endId').style.display = "none";
            document.getElementById('tr_closingId').style.display = "none";
            document.getElementById('td_maxParticipantId').style.display = "none";
        }
    }

    //calendar part
    function openCalendarPickerCustom(field, field_) {
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - 244 / 2;
        var posy = winy - 190 / 2;
        calendarWindow = window.open(${jsUrl}, 'calendarWindowScheduler', 'resizable=no,scrollbars=no,width=244,height=190,left=' + posx + ',top=' + posy);
        calendarWindow.focus();
    }

    function incrHour() {
        var a = document.getElementById('startHour').value;
        var i = parseInt(a) + 1;
        if (a < 23)
            document.getElementById('endHour').value = i;
        else document.getElementById('endHour').value = a;
    }

    function equalMin() {
        document.getElementById('endMin').value = document.getElementById('startMin').value;
    }

    function equalDate() {
        document.getElementById('endDate').value = document.getElementById('startDate').value;
    }

</script>

<html:form action="${action}" focus="dto(productName)" enctype="multipart/form-data" styleClass="form-horizontal">
    <tags:jscript language="JavaScript" src="/js/common.jsp"/>

    <div class="row">
        <div class="col-xs-12 ">
            <app2:securitySubmit operation="${op}" tabindex="1" property="dto(save)" functionality="PRODUCT"
                                 styleId="saveButtonId" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                 onclick="javascript:fillMultipleSelectValues();">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'update'}">

                <app:url var="url"
                         value="/Product/Forward/Delete.do?productId=${param.productId}&dto(productId)=${param.productId}&dto(productName)=${productForm.dtoMap.productName}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
                <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                    <html:button onclick="location.href='${url}'"
                                 styleClass="button ${app2:getFormButtonClasses()} marginButton" property="dto(delete)"
                                 tabindex="2">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op == 'create' || op == 'delete'}">
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" tabindex="3">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
    </div>

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

    <c:if test="${op == 'update'}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <c:if test="${'update' == op || op == 'delete'}">
        <html:hidden property="dto(productId)" value="${productForm.dtoMap['productId']}"/>
    </c:if>
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${title}"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                    <fmt:message key="Product.name"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <app:text property="dto(productName)"
                              styleId="productName_id"
                              styleClass="middleText ${app2:getFormInputClasses()}"
                              maxlength="80"
                              tabindex="4"
                              view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="productGroupId_id">
                    <fmt:message key="Product.group"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <c:choose>
                        <c:when test="${'delete' != op }">
                            <fanta:select property="dto(productGroupId)"
                                          styleId="productGroupId_id"
                                          listName="productGroupList"
                                          firstEmpty="true"
                                          labelProperty="name"
                                          valueProperty="id"
                                          module="/catalogs"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:tree columnId="id" columnParentId="parentProductGroupId"
                                            separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                            </fanta:select>
                        </c:when>
                        <c:otherwise>
                            <fanta:select property="dto(productGroupId)"
                                          styleId="productGroupId_id"
                                          listName="productGroupSimpleList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          module="/catalogs"
                                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                          readOnly="true"
                                          tabIndex="5">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </c:otherwise>
                    </c:choose>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="unitId_id">
                    <fmt:message key="Product.unit"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <fanta:select property="dto(unitId)"
                                  styleId="unitId_id"
                                  listName="productUnitList"
                                  firstEmpty="true"
                                  labelProperty="name"
                                  valueProperty="id"
                                  module="/catalogs"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${op == 'delete'}"
                                  tabIndex="6">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="productTypeId_id">
                    <fmt:message key="Product.type"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <fanta:select property="dto(productTypeId)"
                                  styleId="productTypeId_id"
                                  listName="productTypeList"
                                  firstEmpty="true"
                                  labelProperty="name"
                                  valueProperty="id"
                                  module="/catalogs"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  readOnly="${op == 'delete'}"
                                  preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                                  onChange="checkProductType(this);"
                                  tabIndex="7">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="vatId_id">
                    <fmt:message key="Product.vat"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <fanta:select property="dto(vatId)"
                                  styleId="vatId_id"
                                  listName="vatList"
                                  labelProperty="name"
                                  valueProperty="id"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  module="/catalogs"
                                  firstEmpty="true"
                                  readOnly="${'delete' == op}"
                                  tabIndex="8">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="currentVersion_id">
                    <fmt:message key="Product.version"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                    <app:text property="dto(currentVersion)"
                              styleId="currentVersion_id"
                              styleClass="shortText ${app2:getFormInputClasses()}"
                              maxlength="40"
                              tabindex="9"
                              view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="price_id">
                    <fmt:message key="Product.priceNet"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:numberText property="dto(price)"
                                    styleId="price_id"
                                    styleClass="numberText ${app2:getFormInputClasses()}"
                                    tabindex="10"
                                    maxlength="12"
                                    view="${'delete' == op}"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="accountId_id">
                    <fmt:message key="Product.account"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <fanta:select property="dto(accountId)"
                                  styleId="accountId_id"
                                  listName="accountList"
                                  labelProperty="name"
                                  valueProperty="accountId"
                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                  module="/catalogs"
                                  firstEmpty="true"
                                  readOnly="${'delete' == op}"
                                  tabIndex="11">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="priceGross_id">
                    <fmt:message key="Product.priceGross"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:numberText property="dto(priceGross)"
                                    styleId="priceGross_id"
                                    styleClass="numberText ${app2:getFormInputClasses()}"
                                    tabindex="12"
                                    maxlength="15"
                                    view="${'delete' == op}"
                                    numberType="decimal"
                                    maxInt="10"
                                    maxFloat="2"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="productNumber_id">
                    <fmt:message key="Product.number"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:text property="dto(productNumber)"
                              styleId="productNumber_id"
                              styleClass="shortText ${app2:getFormInputClasses()}"
                              maxlength="40"
                              tabindex="13"
                              view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <c:set var="displayEventFields" value="display: none;"/>
        <c:if test="${eventTypeId eq productForm.dtoMap['productTypeId']}">
            <c:set var="displayEventFields" value=""/>
        </c:if>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.NumberOfCustomers"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        ${app2:countSalePositionByProduct(productForm.dtoMap['productId'])}
                </div>
            </div>
            <div id="td_maxParticipantId" style="${displayEventFields}" class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}" for="eventMaxParticipant_id">
                    <fmt:message key="Product.eventMaxParticipant"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                    <app:text property="dto(eventMaxParticipant)" styleId="eventMaxParticipant_id" styleClass="numberText ${app2:getFormInputClasses()}" maxlength="6" tabindex="13" view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="row" id="tr_initId" style="${displayEventFields}">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.startDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete'==op)}">
                    <c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
                    <fmt:message var="datePattern" key="datePattern"/>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <div class="input-group date">
                                <app:dateText property="dto(initDate)" tabindex="13"
                                              mode="bootstrap"
                                              onchange="changeInitDateToEndDateValue()"
                                              styleId="startDate" onkeyup="equalDate()"
                                              calendarPicker="${op != 'delete'}" datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}" view="${op == 'delete'}" maxlength="10" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <abbr id="startDate_Id">
                                <div class="input-group">
                                    <html:select property="dto(initHour)" onchange="incrHour()" onkeyup="incrHour()" tabindex="13"
                                                 styleClass="select ${app2:getFormSelectClasses()}" readonly="${op == 'delete'}" styleId="startHour"
                                                 style="border-radius:4px">
                                        <html:options collection="hourList" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="${'delete'==op ? '' : 'input-group-addon'}"
                                          style="background-color: transparent;border:0px">:</span>
                                    <html:select property="dto(initMin)" onchange="equalMin()" onkeyup="equalMin()"
                                                 styleClass="select ${app2:getFormSelectClasses()}"
                                                 tabindex="13" readonly="${op == 'delete'}" styleId="startMin" style="border-radius:4px">
                                        <html:options collection="intervalMinList" property="value" labelProperty="label"/>
                                    </html:select>
                                </div>
                            </abbr>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.webSiteLink"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete'==op)}">
                    <app:text property="dto(webSiteLink)" styleClass="middleText ${app2:getFormInputClasses()}"
                              maxlength="255" tabindex="14"
                              view="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="row" id="tr_endId" style="${displayEventFields}">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.endDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete'==op)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <div class="input-group date">
                                <app:dateText property="dto(endDate)" tabindex="15" styleId="endDate"
                                              calendarPicker="${op != 'delete'}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              view="${op == 'delete'}" maxlength="10" currentDate="true"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <abbr id="endDate_Id" >
                                <div class="input-group">
                                    <html:select property="dto(endHour)" tabindex="15" styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete'}"
                                                 style="border-radius:4px"
                                                 styleId="endHour">
                                        <html:options collection="hourList" property="value" labelProperty="label"/>
                                    </html:select>
                                    <span class="${'delete'==op ? '' : 'input-group-addon'}"
                                          style="background-color: transparent;border:0px">:</span>
                                    <html:select property="dto(endMin)" tabindex="15" styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete'}"
                                                 style="border-radius:4px"
                                                 styleId="endMin">
                                        <html:options collection="intervalMinList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </div>
                            </abbr>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row" id="tr_closingId" style="${displayEventFields}">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.closingDate"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete'==op)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6 wrapperButton">
                            <div class="input-group date">
                                <app:dateText property="dto(closeDate)" tabindex="17" styleId="closeDate"
                                              calendarPicker="${op != 'delete'}"
                                              mode="bootstrap"
                                              datePatternKey="${datePattern}"
                                              styleClass="dateText ${app2:getFormInputClasses()}"
                                              view="${op == 'delete'}" maxlength="10"
                                              currentDateTimeZone="${currentDateTimeZone}"/>
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <abbr id="closeDate_Id" >
                                <div class="input-group">
                                    <html:select property="dto(closeHour)" tabindex="17" styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete'}"
                                                 style="border-radius:4px">
                                        <html:options collection="hourList" property="value" labelProperty="label"/>
                                    </html:select>

                                    <c:if test="${not empty productForm.dtoMap['closeDate'] || op != 'delete'}">
                                        <span class="${'delete'==op ? '' : 'input-group-addon'}"
                                              style="background-color: transparent;border:0px">
                                            :
                                        </span>
                                    </c:if>

                                    <html:select property="dto(closeMin)" tabindex="17" styleClass="select ${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete'}"
                                                 style="border-radius:4px">
                                        <html:options collection="intervalMinList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </div>
                            </abbr>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Product.eventAddress"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns('delete'==op)}">
                    <html:textarea property="dto(eventAddress)"
                                   styleId="eventAddress_id"
                                   rows="5"
                                   styleClass="middleDetail ${app2:getFormInputClasses()}"
                                   tabindex="16"
                                   readonly="${op == 'delete'}"/>

                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="col-xs-12 col-sm-12">
            <div class="form-group">
                <label class="control-label label-left col-xs-12 row" for="description_id">
                    <fmt:message key="Competitor.description"/>
                </label>

                <div class="col-xs-12 col-sm-12 row">
                    <html:textarea property="dto(description)"
                                   styleId="description_id"
                                   styleClass="middleDetail ${app2:getFormInputClasses()}"
                                   tabindex="18"
                                   style="height:120px;width:100%;"
                                   readonly="${op == 'delete'}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>


        <!--added by ivan-->
        <c:set var="elementCounter" value="${18}" scope="request"/>
        <c:set var="ajaxAction" value="/products/MainPageReadSubCategories.do" scope="request"/>
        <c:set var="downloadAction"
               value="/products/MainPage/DownloadCategoryFieldValue?dto(productId)=${param.productId}&productId=${param.productId}"
               scope="request"/>
        <c:set var="formName" value="productForm" scope="request"/>
        <c:set var="table" value="<%=ContactConstants.PRODUCT_CATEGORY%>" scope="request"/>
        <c:set var="operation" value="${op}" scope="request"/>
        <c:set var="labelWidth" value="15" scope="request"/>
        <c:set var="containWidth" value="85" scope="request"/>
        <c:set var="generalWidth" value="${250}" scope="request"/>
        <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <app2:securitySubmit operation="${op}" property="dto(save)" functionality="PRODUCT"
                                 styleId="saveButtonId"
                                 styleClass="button ${app2:getFormButtonClasses()}" tabindex="${lastTabIndex+1}"
                                 onclick="javascript:fillMultipleSelectValues();">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'update'}">
                <app2:checkAccessRight functionality="PRODUCT" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="button ${app2:getFormButtonClasses()}"
                                 property="dto(delete)"
                                 tabindex="${lastTabIndex+2}">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${op == 'create' || op == 'delete'}">
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="${lastTabIndex+3}">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="productForm"/>
