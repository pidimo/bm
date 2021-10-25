<%@ page import="com.piramide.elwis.cmd.campaignmanager.ActivityCampaignReadCmd" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.salesmanager.form.SalesProcessForm" %>
<%@ page import="net.java.dev.strutsejb.AppLevelException" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<c:if test="${empty subCategoriesAjaxAction}">
    <c:set var="subCategoriesAjaxAction" value="/sales/MainPageReadSubCategories.do" scope="request"/>
</c:if>

<c:if test="${empty downloadAttachmentCategoryAction}">
    <c:set var="downloadAttachmentCategoryAction"
           value="/sales/MainPage/DownloadCategoryFieldValue.do?dto(processId)=${param.processId}&processId=${param.processId}"
           scope="request"/>
</c:if>

<c:if test="${empty enableTabLinks}">
    <c:set var="enableTabLinks" value="false" scope="request"/>
</c:if>

<c:if test="${empty tabLinkUrl}">
    <c:set var="tabLinkUrl"
           value="/sales/CategoryTab/Forward/Update.do?processId=${param.processId}&dto(processId)=${param.processId}"
           scope="request"/>
</c:if>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<fmt:message var="datePattern" key="datePattern" scope="page"/>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<%
    if (request.getAttribute("salesProcessForm") != null) {
        SalesProcessForm processForm = (SalesProcessForm) request.getAttribute("salesProcessForm");

        if (processForm.getDto("activityId") != null && !"".equals(processForm.getDto("activityId").toString())) {
            ActivityCampaignReadCmd cmd = new ActivityCampaignReadCmd();
            cmd.putParam("activityId", processForm.getDto("activityId"));
            ResultDTO resultDto = new ResultDTO();
            try {
                resultDto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
            }
            request.setAttribute("campaignName", resultDto.get("campaignName"));
            request.setAttribute("campaignId", resultDto.get("campaignId"));
        }
    }
%>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="${action}" focus="dto(processName)" enctype="multipart/form-data" styleClass="form-horizontal">
        <c:if test="${'update' == op || 'delete' == op}">
            <html:hidden property="dto(processId)"/>
            <html:hidden property="dto(version)"/>
            <!--It's is used in referential integrity on delete op.-->
            <html:hidden property="dto(isAction)" value="0"/>
        </c:if>

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

        <c:if test="${isCampaign || 'update' == op}">
            <html:hidden property="dto(activityId)"/>
        </c:if>

        <c:if test="${enableTabLinks}">
            <td align="left">
                <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                    <app2:categoryTabLink id="salesProcessCategoryLinkId"
                                          action="${tabLinkUrl}"
                                          categoryConstant="6"
                                          finderName="findValueBySalesProcessId"
                                          styleClass="folderTabLink">
                        <app2:categoryFinderValue forId="salesProcessCategoryLinkId"
                                                  value="${salesProcessForm.dtoMap.processId}"/>
                    </app2:categoryTabLink>

                </app2:checkAccessRight>
            </td>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="SALESPROCESS"
                                 property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 styleId="saveButtonId"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${!isSalesProcess && 'update' == op}">
                <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                    <html:submit property="evaluationButton"
                                 styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="SalesProcess.Title.evaluation"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="processName_id">
                        <fmt:message key="SalesProcess.name"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:text property="dto(processName)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="80"
                                  tabindex="1"
                                  styleId="processName_id"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                        <fmt:message key="SalesProcess.priority"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(priorityId)"
                                      listName="sProcessPriorityList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleId="priorityId_id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}"
                                      module="/sales"
                                      firstEmpty="true"
                                      tabIndex="2">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>


            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="SalesProcess.contact"/>
                    </label>


                    <c:if test="${not empty salesProcessForm.dtoMap['addressId'] and 'update' == op}">
                        <c:set var="addressMap" value="${app2:getAddressMap(salesProcessForm.dtoMap['addressId'])}"/>
                        <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                            <c:choose>
                                <c:when test="${personType == addressMap['addressType']}">
                                    <c:set var="addressEditLink"
                                           value="/contacts/Person/Forward/Update.do?contactId=${salesProcessForm.dtoMap['addressId']}&dto(addressId)=${salesProcessForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="addressEditLink"
                                           value="/contacts/Organization/Forward/Update.do?contactId=${salesProcessForm.dtoMap['addressId']}&dto(addressId)=${salesProcessForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:otherwise>
                            </c:choose>
                        </app2:checkAccessRight>
                    </c:if>

                    <c:choose>
                        <c:when test="${isSalesProcess}">
                            <div class="${app2:getFormContainClassesTwoColumns(op =='delete' || (!isSalesProcess) || op == 'update')}">
                                <c:set var="showAddressEditLink"
                                       value="${not empty addressEditLink and op == 'update'}"/>
                                <div class="${showAddressEditLink ? 'row col-xs-11' : 'input-group'}">
                                    <app:text property="dto(addressName)"
                                              styleClass="${app2:getFormInputClasses()} mediumText"
                                              maxlength="40"
                                              readonly="true"
                                              tabindex="3"
                                              view="${op =='delete' || (!isSalesProcess) || op == 'update'}"
                                              styleId="fieldAddressName_id"/>
                                    <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                                    <c:if test="${showAddressEditLink}">
                                </div>
                                <div>
                                    </c:if>
                        <span class="${showAddressEditLink ? 'pull-right' : 'input-group-btn'}">
                            <c:if test="${not empty addressEditLink and op == 'update'}">
                                <app:link page="${addressEditLink}"
                                          styleClass="${showAddressEditLink ? '':'btn btn-default'}"
                                          contextRelative="true"
                                          addModuleParams="false" titleKey="Common.edit">
                                    <span class="glyphicon glyphicon-edit"></span>
                                </app:link>
                            </c:if>

                            <tags:bootstrapSelectPopup styleId="styleId"
                                                       url="/contacts/SearchAddress.do?allowCreation=2"
                                                       name="searchAddress"
                                                       isLargeModal="true"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Contact.Title.search"
                                                       hide="${op != 'create'}"
                                                       tabindex="4"/>

                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                            nameFieldId="fieldAddressName_id"
                                                            titleKey="Common.clear"
                                                            hide="${op != 'create'}"
                                                            tabindex="4"/>
                        </span>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:when>

                        <c:when test="${isCampaign}">
                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(addressId)"/> <%--module campaign--%>
                                <html:hidden property="dto(campaignContactId)"/>
                                <app:text property="dto(addressName)"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          styleId="fieldAddressName_id"
                                          readonly="true"
                                          tabindex="3"
                                          view="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(addressId)"
                                             value="${param.contactId}"/> <%--module contacts--%>
                                <app:text property="dto(addressName)"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          readonly="true"
                                          styleId="fieldAddressName_id"
                                          tabindex="3"
                                          view="true"
                                          value="${addressName}"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                        <fmt:message key="SalesProcess.employee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(employeeId)"
                                      listName="employeeBaseList"
                                      labelProperty="employeeName"
                                      styleId="employeeId_id"
                                      valueProperty="employeeId"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${op == 'delete'}"
                                      value="${sessionScope.user.valueMap['userAddressId']}"
                                      module="/contacts"
                                      tabIndex="5">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="probability_id">
                        <fmt:message key="SalesProcess.probability"/> (<fmt:message key="Common.probabilitySymbol"/>)
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
                        <html:select property="dto(probability)"
                                     styleClass="${app2:getFormSelectClasses()} select"
                                     readonly="${op == 'delete'}"
                                     styleId="probability_id"
                                     tabindex="6">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="probabilities" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="SalesProcess.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(startDate)"
                                          styleId="startDate"
                                          mode="bootstrap"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          view="${op == 'delete'}"
                                          maxlength="10"
                                          tabindex="7" currentDate="true"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="statusId_id">
                        <fmt:message key="SalesProcess.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <fanta:select property="dto(statusId)"
                                      listName="statusList"
                                      labelProperty="statusName"
                                      valueProperty="statusId"
                                      styleId="statusId_id"
                                      styleClass="${app2:getFormSelectClasses()} select"
                                      readOnly="${op == 'delete'}"
                                      module="/sales"
                                      firstEmpty="true"
                                      tabIndex="8">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="endDate">
                        <fmt:message key="SalesProcess.endDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <div class="input-group date">
                            <app:dateText property="dto(endDate)"
                                          styleId="endDate"
                                          calendarPicker="${op != 'delete'}"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} text"
                                          view="${op == 'delete'}"
                                          mode="bootstrap"
                                          maxlength="10"
                                          tabindex="9"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">

                    <c:set var="showCampaign" value="${false}"/>
                    <c:set var="newColSpan" value="3"/>
                    <c:if test="${not empty salesProcessForm.dtoMap['activityId'] && op == 'update'}">
                        <c:set var="showCampaign" value="${true}"/>
                        <c:set var="newColSpan" value="0"/>
                    </c:if>

                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="value_id">
                        <fmt:message key="SalesProcess.value"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(value)"
                                        styleClass="${app2:getFormInputClasses()} text"
                                        maxlength="12"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        styleId="value_id"
                                        maxInt="10"
                                        maxFloat="2"
                                        tabindex="10"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${showCampaign}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <c:set var="editLink"
                                   value="campaign/Campaign/Forward/Update.do?campaignId=${campaignId}&index=0&dto(campaignId)=${campaignId}&dto(campaignName)=${app2:encode(campaignName)}&dto(operation)=update"/>

                            <div class="row col-xs-11">
                                <c:out value="${campaignName}"/>
                            </div>
                <span class="pull-right">
                    <app:link action="${editLink}" contextRelative="true" tabindex="11" titleKey="Common.edit">
                        <span class="glyphicon glyphicon-edit"></span>
                        <%--<html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>--%>
                    </app:link>
                </span>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </div>

            <div class="col-xs-12 col-sm-12">
                <div class="form-group">
                    <label class="control-label col-xs-12 col-sm-12 label-left row" for="description_id">
                        <fmt:message key="SalesProcess.description"/>
                    </label>

                    <div class="col-xs-12 col-sm-12 row">
                        <html:textarea property="dto(description)"
                                       styleClass="form-control mediumDetailHigh"
                                       style="height:120px;width:99%;"
                                       styleId="description_id"
                                       tabindex="11"
                                       readonly="${op == 'delete'}"/>
                    </div>
                </div>
            </div>

            <c:set var="elementCounter" value="${16}" scope="request"/>
            <c:set var="ajaxAction" value="${subCategoriesAjaxAction}" scope="request"/>
            <c:set var="downloadAction" value="${downloadAttachmentCategoryAction}" scope="request"/>
            <c:set var="formName" value="salesProcessForm" scope="request"/>
            <c:set var="table" value="<%=ContactConstants.SALES_PROCESS_CATEGORY%>" scope="request"/>
            <c:set var="operation" value="${op}" scope="request"/>
            <c:set var="labelWidth" value="15" scope="request"/>
            <c:set var="containWidth" value="85" scope="request"/>
            <c:set var="generalWidth" value="${250}" scope="request"/>
            <c:import url="/WEB-INF/jsp/catalogs/CategoryUtil.jsp"/>
            <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">

            <app2:securitySubmit operation="${op}"
                                 functionality="SALESPROCESS"
                                 property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 styleId="saveButtonId"
                                 onclick="javascript:fillMultipleSelectValues();"
                                 tabindex="12">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${!isSalesProcess && op == 'update'}">
                <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                    <html:submit property="evaluationButton" styleClass="${app2:getFormButtonClasses()}" tabindex="13">
                        <fmt:message key="SalesProcess.Title.evaluation"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

        </div>

    </html:form>
</div>
<tags:jQueryValidation formName="salesProcessForm"/>

<c:if test="${op=='update'}">
    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
        <div class="embed-responsive embed-responsive-16by9 col-xs-12">
            <iframe name="frame1"
                    src="<app:url value="SalesProcess/Action/List.do?parameter(processId)=${dto.processId}&dto(processId)=${dto.processId}&dto(processName)=${app2:encode(dto.processName)}&probability=${dto.probability}&addressId=${salesProcessForm.dtoMap['addressId']}"/>"
                    class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </div>
    </app2:checkAccessRight>
</c:if>