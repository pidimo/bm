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

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
<fmt:message var="datePattern" key="datePattern" scope="page"/>

<tags:initSelectPopup/>
<calendar:initialize/>
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

<br/>

<html:form action="${action}" focus="dto(processName)" enctype="multipart/form-data">
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

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<table cellSpacing="0" cellPadding="4" width="800" border="0" align="center">
    <TR>
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
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)"
                                 styleClass="button"
                                 styleId="saveButtonId"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${!isSalesProcess && 'update' == op}">
                <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                    <html:submit property="evaluationButton" styleClass="button">
                        <fmt:message key="SalesProcess.Title.evaluation"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
    <TR>
        <TD colspan="4" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label" width="15%"><fmt:message key="SalesProcess.name"/></TD>
        <TD class="contain" width="35%">
            <app:text property="dto(processName)" styleClass="mediumText" maxlength="80" tabindex="1"
                      view="${op == 'delete'}"/>
        </TD>
        <TD class="label" width="15%"><fmt:message key="SalesProcess.priority"/></TD>
        <TD class="contain" width="35%">
            <fanta:select property="dto(priorityId)" listName="sProcessPriorityList"
                          labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                          readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="6">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="SalesProcess.contact"/></TD>
        <TD class="contain">
            <%--address detail url--%>
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
                    <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                    <app:text property="dto(addressName)" styleClass="mediumText" maxlength="40"
                              readonly="true"
                              tabindex="2" view="${op =='delete' || (!isSalesProcess) || op == 'update'}"
                              styleId="fieldAddressName_id"/>

                    <c:if test="${not empty addressEditLink and op == 'update'}">
                        <app:link page="${addressEditLink}" styleClass="folderTabLink" contextRelative="true" addModuleParams="false">
                            <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                        </app:link>
                    </c:if>

                    <tags:selectPopup url="/contacts/SearchAddress.do?allowCreation=2" name="searchAddress"
                                      titleKey="Common.search"
                                      hide="${op != 'create'}" tabindex="3"/>
                    <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                           titleKey="Common.clear" hide="${op != 'create'}" tabindex="3"/>
                </c:when>
                <c:when test="${isCampaign}">
                    <html:hidden property="dto(addressId)"/> <%--module campaign--%>
                    <html:hidden property="dto(campaignContactId)"/>
                    <app:text property="dto(addressName)" styleClass="mediumText" maxlength="40"
                              readonly="true"
                              tabindex="2" view="true"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(addressId)"
                                 value="${param.contactId}"/> <%--module contacts--%>
                    <app:text property="dto(addressName)" styleClass="mediumText" maxlength="40"
                              readonly="true"
                              tabindex="2" view="true" value="${addressName}"/>
                </c:otherwise>
            </c:choose>
        </TD>
        <TD class="topLabel"><fmt:message key="SalesProcess.employee"/></TD>
        <TD class="containTop">
            <fanta:select property="dto(employeeId)" listName="employeeBaseList"
                          labelProperty="employeeName" valueProperty="employeeId" styleClass="mediumSelect"
                          readOnly="${op == 'delete'}"
                          value="${sessionScope.user.valueMap['userAddressId']}"
                          module="/contacts" tabIndex="8">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="SalesProcess.probability"/></TD>
        <TD class="contain">
            <c:set var="probabilities" value="${app2:defaultProbabilities()}"/>
            <html:select property="dto(probability)" styleClass="select" readonly="${op == 'delete'}"
                         tabindex="3">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="probabilities" property="value" labelProperty="label"/>
            </html:select>
            <fmt:message key="Common.probabilitySymbol"/>
        </TD>

        <TD class="label"><fmt:message key="SalesProcess.startDate"/></TD>
        <TD class="contain">
            <app:dateText property="dto(startDate)" styleId="startDate" calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}" styleClass="text" view="${op == 'delete'}"
                          maxlength="10"
                          tabindex="9" currentDate="true"/>
        </TD>
    </TR>
    <TR>

        <TD class="label"><fmt:message key="SalesProcess.status"/></TD>
        <TD class="contain">
            <fanta:select property="dto(statusId)" listName="statusList"
                          labelProperty="statusName" valueProperty="statusId" styleClass="select"
                          readOnly="${op == 'delete'}" module="/sales" firstEmpty="true" tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </TD>

        <TD class="label"><fmt:message key="SalesProcess.endDate"/></TD>
        <TD class="contain">
            <app:dateText property="dto(endDate)" styleId="endDate" calendarPicker="${op != 'delete'}"
                          datePatternKey="${datePattern}" styleClass="text" view="${op == 'delete'}"
                          maxlength="10"
                          tabindex="10"/>
        </TD>
    </TR>
    <TR>
        <c:set var="showCampaign" value="${false}"/>
        <c:set var="newColSpan" value="3"/>
        <c:if test="${not empty salesProcessForm.dtoMap['activityId'] && op == 'update'}">
            <c:set var="showCampaign" value="${true}"/>
            <c:set var="newColSpan" value="0"/>
        </c:if>

        <TD class="label"><fmt:message key="SalesProcess.value"/></TD>
        <TD class="contain" colspan="${newColSpan}">
            <app:numberText property="dto(value)" styleClass="text" maxlength="12" view="${'delete' == op}"
                            numberType="decimal" maxInt="10" maxFloat="2" tabindex="5"/>
        </TD>
        <c:if test="${showCampaign}">
            <td class="label">
                <fmt:message key="Campaign"/>
            </td>
            <TD class="contain">
                <c:set var="editLink"
                       value="campaign/Campaign/Forward/Update.do?campaignId=${campaignId}&index=0&dto(campaignId)=${campaignId}&dto(campaignName)=${app2:encode(campaignName)}&dto(operation)=update"/>

                <c:out value="${campaignName}"/>
                <app:link action="${editLink}" contextRelative="true" tabindex="11">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>
            </TD>
        </c:if>
    </TR>

    <TR>
        <TD class="topLabel" colspan="4"><fmt:message key="SalesProcess.description"/><br>
            <html:textarea property="dto(description)" styleClass="mediumDetailHigh"
                           style="height:120px;width:99%;"
                           tabindex="11" readonly="${op == 'delete'}"/>
        </TD>

    </TR>

    <tr>
        <td colspan="4">
            <c:set var="elementCounter" value="${16}" scope="request"/>
            <c:set var="ajaxAction" value="${subCategoriesAjaxAction}" scope="request"/>
            <c:set var="downloadAction" value="${downloadAttachmentCategoryAction}" scope="request"/>
            <c:set var="formName" value="salesProcessForm" scope="request"/>
            <c:set var="table" value="<%=ContactConstants.SALES_PROCESS_CATEGORY%>" scope="request"/>
            <c:set var="operation" value="${op}" scope="request"/>
            <c:set var="labelWidth" value="15" scope="request"/>
            <c:set var="containWidth" value="85" scope="request"/>
            <c:set var="generalWidth" value="${250}" scope="request"/>
            <c:import url="/common/catalogs/CategoryUtil.jsp"/>
            <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
        </td>
    </tr>

</table>

<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="SALESPROCESS" property="dto(save)"
                                 styleClass="button"
                                 styleId="saveButtonId"
                                 onclick="javascript:fillMultipleSelectValues();"
                                 tabindex="12">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${!isSalesProcess && op == 'update'}">
                <app2:checkAccessRight functionality="SALESPROCESSEVALUATION" permission="EXECUTE">
                    <html:submit property="evaluationButton" styleClass="button" tabindex="13">
                        <fmt:message key="SalesProcess.Title.evaluation"/>
                    </html:submit>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="button" tabindex="14"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>

</td>
</tr>
</table>
</html:form>
<br>

<c:if test="${op=='update'}">
    <app2:checkAccessRight functionality="SALESPROCESSACTION" permission="VIEW">
        <iframe name="frame1"
                src="<app:url value="SalesProcess/Action/List.do?parameter(processId)=${dto.processId}&dto(processId)=${dto.processId}&dto(processName)=${app2:encode(dto.processName)}&probability=${dto.probability}&addressId=${salesProcessForm.dtoMap['addressId']}"/>"
                class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </app2:checkAccessRight>
</c:if>
