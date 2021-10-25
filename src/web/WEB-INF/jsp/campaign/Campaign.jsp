<%@ page import="com.piramide.elwis.utils.DateUtils,
                 net.java.dev.strutsejb.web.DefaultForm" %>
<%@ page import="java.util.Date" %>

<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern" scope="request"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:formatNumber pattern="${numberFormat}" value="${0}" var="test" type="number"/>
<c:set var="statusList" value="${app2:getCampaignStatusList(pageContext.request)}"/>

<c:set var="userAddresId" value="${sessionScope.user.valueMap['userAddressId']}" scope="request"/>
<%
    String datePattern = (String) request.getAttribute("datePattern");
    String showDate = DateUtils.parseDate(new Date(), datePattern);

    Integer integetDate = com.piramide.elwis.utils.DateUtils.dateToInteger(new Date());
    request.setAttribute("showDate", showDate);
    request.setAttribute("integerDate", integetDate);


    String op = (String) request.getAttribute("op");
    if ("create".equals(op)) {
        Integer userAddressId = new Integer(request.getAttribute("userAddresId").toString());
        DefaultForm f = (DefaultForm) request.getAttribute("form");
        if (null != f)
            f.setDto("employeeId", userAddressId);
    }
%>

<tags:initBootstrapSelectPopup/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="dto(campaignName)" styleClass="form-horizontal">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGN" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="13">
                ${button}
            </app2:securitySubmit>

            <c:if test="${'update' == op}">
                <app2:checkAccessRight functionality="CAMPAIGNCASCADEDELETE" permission="DELETE">
                    <app:url var="urlCascadeDelete" value="/Campaign/Forward/Cascade/Delete.do?dto(campaignId)=${campaignForm.dtoMap['campaignId']}&dto(campaignName)=${app2:encode(campaignForm.dtoMap['campaignName'])}"/>
                    <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="location.href='${urlCascadeDelete}'" tabindex="13">
                        <fmt:message key="Campaign.button.cascadeDelete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>

            <c:if test="${'update' != op}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <html:hidden property="dto(op)" value="${op}"/>

            <c:if test="${'update' == op || 'delete' == op}">
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(campaignId)"/>
            </c:if>

            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:choose>
                    <c:when test="${'create'== op}">
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="campaignName_id">
                                    <fmt:message key="Campaign.mailing"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:text property="dto(campaignName)"
                                              styleId="campaignName_id"
                                              styleClass="${app2:getFormInputClasses()} middleText"
                                              maxlength="40"
                                              tabindex="1"
                                              view="${op == 'delete'}"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.dateCreation"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        ${showDate}
                                    <html:hidden property="dto(recordDate)" value="${integerDate}"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldCampaignName_id">
                                    <fmt:message key="Campaign.copyOf"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(view)}">
                                    <div class="input-group">
                                        <app:text property="dto(copyCampaignName)"
                                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                                  maxlength="40"
                                                  readonly="true"
                                                  tabindex="2"
                                                  styleId="fieldCampaignName_id"/>
                                        <html:hidden property="dto(copyCampaignId)" styleId="fieldCampaignCopyId_id"/>
                                        <span class="input-group-btn">
                                            <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                                       url="/campaign/Campaign/Forward/Copy.do"
                                                                       name="searchAddress"
                                                                       titleKey="Common.search"
                                                                       modalTitleKey="Campaign.Title.search"
                                                                       hide="${op != 'create'}"
                                                                       isLargeModal="true"
                                                                       tabindex="2"/>
                                            <tags:clearBootstrapSelectPopup keyFieldId="fieldCampaignCopyId_id"
                                                                            nameFieldId="fieldCampaignName_id"
                                                                            titleKey="Common.clear"
                                                                            hide="${op != 'create'}"
                                                                            tabindex="2"/>
                                        </span>
                                    </div>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.updatedDate"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <c:if test="${null != campaignForm.dtoMap['changeDate']}">
                                        <c:set var="updateDate" value="${campaignForm.dtoMap['changeDate']}"
                                               scope="request"/>
                                        <%
                                            Integer i = new Integer(request.getAttribute("updateDate").toString());
                                            String fd = DateUtils.parseDate(DateUtils.integerToDate(i), datePattern);
                                            request.setAttribute("formattedDate", fd);
                                        %>
                                        ${formattedDate}
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                                    <fmt:message key="Campaign.responsibleEmployee"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <fanta:select property="dto(employeeId)" styleId="employeeId_id"
                                                  listName="employeeBaseList"
                                                  tabIndex="3"
                                                  labelProperty="employeeName" valueProperty="employeeId"
                                                  styleClass="${app2:getFormSelectClasses()} middleSelect"
                                                  value="${sessionScope.user.valueMap['userAddressId']}"
                                                  module="/contacts">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateTextId_${i.count}">
                                    <fmt:message key="Campaign.closeDate"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <div class="input-group date">
                                        <app:dateText
                                                property="dto(closeDate)"
                                                mode="bootstrap"
                                                calendarPicker="${op!='delete'}"
                                                datePatternKey="${datePattern}"
                                                view="${true}"
                                                styleId="dateTextId_${i.count}"
                                                currentDate="true"
                                                styleClass="${app2:getFormInputClasses()} dateText" tabindex="4"/>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="typeId_id">
                                    <fmt:message key="Campaign.type"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <fanta:select property="dto(typeId)" styleId="typeId_id" listName="campaignTypeList"
                                                  valueProperty="id" labelProperty="title" firstEmpty="true"
                                                  styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                                  module="/catalogs" tabIndex="5"
                                                  readOnly="${'delete' == op}">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="budgetCost_id">
                                    <fmt:message key="Campaign.budgetedCost"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <app:numberText property="dto(budgetCost)"
                                                    styleId="budgetCost_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="10"
                                                    view="${'delete' == op}" numberType="decimal" maxInt="10"
                                                    maxFloat="2" tabindex="6"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                                    <fmt:message key="Campaign.status"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <html:select property="dto(status)" styleId="status_id"
                                                 styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                                 readonly="${op == 'delete'}" tabindex="7">
                                        <html:options collection="statusList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.profits"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        ${test}
                                    <html:hidden property="dto(awaitedUtility)" value="0"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="numberContacts_id">
                                    <fmt:message key="Campaign.totalContacts"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <app:text
                                            property="dto(numberContacts)"
                                            styleId="numberContacts_id"
                                            styleClass="${app2:getFormInputClasses()} numberText"
                                            tabindex="8"
                                            view="${true}"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.realCost"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                        ${test}
                                </div>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="campaignName_id">
                                    <fmt:message key="Campaign.mailing"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <app:text property="dto(campaignName)" styleId="campaignName_id"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                              tabindex="1"
                                              view="${op == 'delete'}"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.dateCreation"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <c:set var="recordDate" value="${campaignForm.dtoMap['recordDate']}"
                                           scope="request"/>
                                    <%
                                        Integer i = new Integer(request.getAttribute("recordDate").toString());
                                        String fd = DateUtils.parseDate(DateUtils.integerToDate(i), datePattern);
                                        request.setAttribute("formattedRecordDate", fd);
                                    %>
                                    <html:hidden property="dto(recordDate)"/>
                                        ${formattedRecordDate}
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                                    <fmt:message key="Campaign.responsibleEmployee"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <fanta:select property="dto(employeeId)" styleId="employeeId_id"
                                                  listName="employeeBaseList"
                                                  tabIndex="2"
                                                  labelProperty="employeeName" valueProperty="employeeId"
                                                  styleClass="${app2:getFormSelectClasses()} middleSelect"
                                                  readOnly="${op == 'delete'}" module="/contacts">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}">
                                    <fmt:message key="Campaign.updatedDate"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <c:if test="${null != campaignForm.dtoMap['changeDate']}">
                                        <c:set var="updateDate" value="${campaignForm.dtoMap['changeDate']}"
                                               scope="request"/>
                                        <%
                                            Integer ii = new Integer(request.getAttribute("updateDate").toString());
                                            String fdd = DateUtils.parseDate(DateUtils.integerToDate(ii), datePattern);
                                            request.setAttribute("formattedDate", fdd);
                                        %>
                                        ${formattedDate}
                                    </c:if>
                                    <html:hidden property="dto(changeDate)" value="${integerDate}"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="typeId_id">
                                    <fmt:message key="Campaign.type"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                    <fanta:select property="dto(typeId)" styleId="typeId_id" listName="campaignTypeList"
                                                  valueProperty="id" labelProperty="title" firstEmpty="true"
                                                  styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                                  module="/catalogs" tabIndex="3"
                                                  readOnly="${'delete' == op}">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateTextId_${i.count}">
                                    <fmt:message key="Campaign.closeDate"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <div class="input-group date">
                                        <app:dateText
                                                property="dto(closeDate)"
                                                mode="bootstrap"
                                                calendarPicker="${op!='delete'}"
                                                datePatternKey="${datePattern}"
                                                currentDate="${op=='create'}"
                                                view="${true}"
                                                styleId="dateTextId_${i.count}"
                                                styleClass="${app2:getFormInputClasses()} dateText" tabindex="4"/>
                                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                                    <fmt:message key="Campaign.status"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <html:select property="dto(status)" styleId="status_id"
                                                 styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                                 readonly="${op == 'delete'}" tabindex="5">
                                        <html:options collection="statusList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="budgetCost_id">
                                    <fmt:message key="Campaign.budgetedCost"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                    <app:numberText property="dto(budgetCost)" styleId="budgetCost_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    view="${'delete' == op}" numberType="decimal" maxInt="10"
                                                    maxFloat="2" tabindex="6"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="numberContacts_id">
                                    <fmt:message key="Campaign.totalContacts"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <app:text
                                            property="dto(numberContacts)"
                                            styleId="numberContacts_id"
                                            styleClass="numberText"
                                            view="${true}" tabindex="7"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="awaitedUtility_id">
                                    <fmt:message key="Campaign.profits"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <app:numberText property="dto(awaitedUtility)" styleId="awaitedUtility_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText"
                                                    maxlength="12"
                                                    tabindex="8"
                                                    view="true" numberType="decimal" maxInt="10" maxFloat="2"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="realCost_id">
                                    <fmt:message key="Campaign.realCost"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(true)}" colspan="3">
                                    <app:numberText property="dto(realCost)" styleId="realCost_id"
                                                    styleClass="${app2:getFormInputClasses()} numberText" maxlength="12"
                                                    view="true" numberType="decimal" maxInt="10" tabindex="9"
                                                    maxFloat="2"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="col-xs-12 col-sm-12">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 col-sm-12 label-left row" for="remarkValue_id">
                            <fmt:message key="Campaign.remark"/>
                        </label>

                        <div class="col-xs-12 col-sm-12 row">
                            <html:textarea property="dto(remarkValue)" styleId="remarkValue_id"
                                           styleClass="${app2:getFormInputClasses()} tinyDetail"
                                           readonly="${op == 'delete'}"
                                           style="height:100px;" tabindex="10"/>
                            <html:hidden property="dto(remark)"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="CAMPAIGN" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="11">
                ${button}
            </app2:securitySubmit>
            <c:if test="${'update' != op}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
    </html:form>
</div>
<tags:jQueryValidation formName="campaignForm"/>