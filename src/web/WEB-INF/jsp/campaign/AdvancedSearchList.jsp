<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<tags:initBootstrapDatepicker/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getStatusList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script language="JavaScript">
    function myReset() {
        var form = document.advancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<html:form action="/AdvancedSearch.do" focus="parameter(campaignName)" styleClass="form-horizontal">
    <fmt:message key="datePattern" var="datePattern"/>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Campaign.Title.advancedSearch"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="campaignName_id">
                        <fmt:message key="Campaign.mailing"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <app:text property="parameter(campaignName)"
                                  styleId="campaignName_id"
                                  styleClass="${app2:getFormInputClasses()}"
                                  maxlength="40" tabindex="1"/>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="endDate1">
                        <fmt:message key="Campaign.closeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endDate1)"
                                                  maxlength="10"
                                                  tabindex="2"
                                                  styleId="endDate1"
                                                  calendarPicker="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endDate2)"
                                                  maxlength="10"
                                                  tabindex="3"
                                                  styleId="endDate2"
                                                  calendarPicker="true"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                        <fmt:message key="Campaign.responsibleEmployee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(employeeId)" listName="employeeBaseList" tabIndex="4"
                                      styleId="employeeId_id"
                                      labelProperty="employeeName" valueProperty="employeeId"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                      module="/contacts" firstEmpty="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="budgetCost1_id">
                        <fmt:message key="Campaign.budgetedCost"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="budgetCost1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(budgetCost1)"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="5" maxlength="12"
                                                numberType="decimal" styleId="budgetCost1_id" maxInt="10" maxFloat="2"/>
                            </div>

                            <div class=" col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="price2">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(budgetCost2)" styleId="price2"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="6"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                        <fmt:message key="Campaign.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(status)"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     styleId="status_id"
                                     tabindex="7">
                            <html:option value=""/>
                            <html:options collection="statusList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="awaitedUtility1_id">
                        <fmt:message key="Campaign.profits"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="awaitedUtility1_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(awaitedUtility1)"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="8" maxlength="12"
                                                numberType="decimal" styleId="awaitedUtility1_id" maxInt="10"
                                                maxFloat="2"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(awaitedUtility2)" styleId="price2"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="9"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(typeId)" listName="campaignTypeList"
                                      valueProperty="id" labelProperty="title" firstEmpty="true"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}" module="/catalogs"
                                      tabIndex="10" readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.realCost"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(realCost1)"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="11" maxlength="12"
                                                numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>

                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(realCost2)" styleId="price2"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="12"
                                                maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.dateCreation"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startDate1)"
                                                  maxlength="10"
                                                  tabindex="13"
                                                  styleId="startDate1"
                                                  calendarPicker="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(startDate2)"
                                                  maxlength="10"
                                                  tabindex="14"
                                                  styleId="startDate2"
                                                  calendarPicker="true"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.totalContacts"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:text property="parameter(numberContacts1)"
                                          styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                          tabindex="15"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:text property="parameter(numberContacts2)"
                                          styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                          tabindex="16"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Campaign.updatedDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(changeDate1)"
                                                  maxlength="10"
                                                  tabindex="17"
                                                  styleId="changeDate1"
                                                  calendarPicker="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>

                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(changeDate2)"
                                                  maxlength="10"
                                                  tabindex="18"
                                                  styleId="changeDate2"
                                                  calendarPicker="true"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)"
                             tabindex="19">
                    <fmt:message key="Common.go"/>
                </html:submit>
                <html:button property="reset1" tabindex="20" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="myReset()">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </div>

        </fieldset>
    </div>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="AdvancedSearch.do"
                        addModuleName="campaign"
                        mode="bootstrap"
                        parameterName="campaignNameAlpha"/>
    </div>
</html:form>

<app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <app:url
                value="/Campaign/Forward/Create.do?dto(operation)=create&dto(companyId)=${sessionScope.user.valueMap['companyId']}&advancedListForward=CampaignAdvancedSearch"
                addModuleParams="false" var="newCampaignUrl"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <input type="button" class="button ${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                   onclick="window.location ='${newCampaignUrl}'">
        </div>
    </c:set>
</app2:checkAccessRight>

<c:out value="${newButtonsTable}" escapeXml="false"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="campaignReportList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%" id="campaign"
                 action="AdvancedSearch.do"
                 imgPath="${baselayout}">
        <c:set var="editAction"
               value="Campaign/Forward/Update.do?campaignId=${campaign.campaignId}&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(operation)=update&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
        <c:set var="deleteAction"
               value="Campaign/Forward/Delete.do?campaignId=${campaign.campaignId}&dto(withReferences)=true&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(companyId)=${sessionScope.user.valueMap['companyId']}&index=0"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
                <fanta:actionColumn name="update" label="Common.update" action="${editAction}" title="Common.update"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGN" permission="DELETE">
                <fanta:actionColumn name="del" label="Common.delete" action="${deleteAction}" title="Common.delete"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="campaignName" action="${editAction}" styleClass="listItem" title="Campaign.mailing"
                          headerStyle="listHeader" width="25%" orderable="true">
            &nbsp;
        </fanta:dataColumn>
        <fanta:dataColumn name="employeeName" styleClass="listItem" title="Campaign.responsibleEmployee"
                          headerStyle="listHeader" width="25%" orderable="true"/>
        <fanta:dataColumn name="status" styleClass="listItem" title="Campaign.status" headerStyle="listHeader"
                          width="13%" orderable="true" renderData="false">
            <c:if test="${campaign.status == '1'}">
                <fmt:message key="Campaign.preparation"/>
            </c:if>
            <c:if test="${campaign.status == '2'}">
                <fmt:message key="Campaign.sent"/>
            </c:if>
            <c:if test="${campaign.status == '3'}">
                <fmt:message key="Campaign.cancel"/>
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn name="startDate" styleClass="listItem" title="Campaign.dateCreation" headerStyle="listHeader"
                          width="13%" orderable="true" renderData="false">
            <fmt:message var="datePattern" key="datePattern"/>
            <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.startDate)}" pattern="${datePattern}"/>
            ${dateValue1}&nbsp;
        </fanta:dataColumn>
        <fanta:dataColumn name="endDate" styleClass="listItem2" title="Campaign.closeDate" headerStyle="listHeader"
                          width="13%" orderable="true" renderData="false">
            <fmt:message var="datePattern" key="datePattern"/>
            <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.endDate)}" pattern="${datePattern}"/>
            ${dateValue}&nbsp;
        </fanta:dataColumn>


    </fanta:table>
</div>
<c:out value="${newButtonsTable}" escapeXml="false"/>

