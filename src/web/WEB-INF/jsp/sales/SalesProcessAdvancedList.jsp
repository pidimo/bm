<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<tags:initBootstrapDatepicker/>

<script>
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

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/AdvancedSearch.do"
               focus="parameter(processName)"
               styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="SalesProcess.Title.simpleSearch"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="processName_id">
                            <fmt:message key="SalesProcess.name"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(processName)"
                                       styleId="processName_id"
                                       styleClass="${app2:getFormInputClasses()} largeText"
                                       tabindex="1"/>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="processEmployeeId_id">
                            <fmt:message key="SalesProcess.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(processEmployeeId)"
                                          listName="employeeBaseList"
                                          module="/contacts"
                                          styleId="processEmployeeId_id"
                                          labelProperty="employeeName"
                                          valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          firstEmpty="true"
                                          tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="valueFrom_id">
                            <fmt:message key="SalesProcess.value"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <app:numberText property="parameter(valueFrom)"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    styleId="valueFrom_id"
                                                    maxFloat="2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="3"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <app:numberText property="parameter(valueTo)"
                                                    numberType="decimal"
                                                    maxInt="10"
                                                    maxFloat="2"
                                                    styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                    tabindex="4"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactSearchName_id">
                            <fmt:message key="SalesProcess.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(contactSearchName)"
                                       styleClass="${app2:getFormInputClasses()} largeText"
                                       tabindex="5"
                                       styleId="contactSearchName_id"/>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="SalesProcess.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(priorityId)"
                                          listName="sProcessPriorityList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          styleId="priorityId_id"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/sales"
                                          firstEmpty="true"
                                          tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                            <fmt:message key="Product.name"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(productName)"
                                       styleClass="${app2:getFormInputClasses()} largeText"
                                       tabindex="7"
                                       styleId="productName_id"/>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="statusId_id">
                            <fmt:message key="SalesProcess.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(statusId)"
                                          listName="statusList"
                                          labelProperty="statusName"
                                          valueProperty="statusId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/sales"
                                          styleId="statusId_id"
                                          firstEmpty="true"
                                          tabIndex="8">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="SalesProcess.endDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>

                                    <div class="input-group date">
                                        <app:dateText property="parameter(endDateFrom)"
                                                      maxlength="10"
                                                      tabindex="9"
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
                                        <app:dateText property="parameter(endDateTo)"
                                                      maxlength="10"
                                                      tabindex="10"
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

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="number_id">
                            <fmt:message key="SalesProcess.actionNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(number)"
                                       styleClass="${app2:getFormInputClasses()} largeText"
                                       tabindex="11"
                                       styleId="number_id"/>
                        </div>
                    </div>
                </div>
            </fieldset>
            <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="12">
                <fmt:message key="Common.go"/>
            </html:submit>
            <html:button property="reset1" tabindex="12" styleClass="${app2:getFormButtonClasses()}"
                         onclick="myReset()">
                <fmt:message key="Common.clear"/>
            </html:button>

        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="AdvancedSearch.do"
                            parameterName="processNameAlpha"
                            mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <tags:buttonsTable>
                <app:url value="/SalesProcess/Forward/Create.do?advancedListForward=SalesProcessAdvancedSearch"
                         addModuleParams="false" var="newSalesProcessUrl"/>

                <div class="${app2:getFormGroupClasses()}">
                        <%--<div class="${app2:getSearchWrapperClasses()}">--%>
                    <input type="button"
                           class="${app2:getFormButtonClasses()}"
                           onclick='window.location ="${newSalesProcessUrl}"'
                           value='<fmt:message key="Common.new"/>'>
                </div>
            </tags:buttonsTable>
        </c:set>
    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>

    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
    <fmt:message var="datePattern" key="datePattern"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="salesProcessAdvancedList"
                     width="100%" id="process"
                     action="AdvancedSearch.do"
                     styleClass="${app2:getFantabulousTableLargeClases()}"
                     imgPath="${baselayout}"
                     align="center">
            <c:set var="editLink"
                   value="SalesProcess/Forward/Update.do?processId=${process.processId}&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}"/>
            <c:set var="deleteLink"
                   value="SalesProcess/Forward/Delete.do?processId=${process.processId}&dto(withReferences)=true&dto(processId)=${process.processId}&dto(processName)=${app2:encode(process.processName)}&addressId=${process.addressId}&dto(isAction)=0"/>

            <%--address link--%>
            <c:choose>
                <c:when test="${personType == process.addressType}">
                    <c:set var="addressEditLink"
                           value="/contacts/Person/Forward/Update.do?contactId=${process.addressId}&dto(addressId)=${process.addressId}&dto(addressType)=${process.addressType}&dto(name1)=${app2:encode(process.addressName1)}&dto(name2)=${app2:encode(process.addressName2)}&dto(name3)=${app2:encode(process.addressName3)}&index=0"/>
                </c:when>
                <c:otherwise>
                    <c:set var="addressEditLink"
                           value="/contacts/Organization/Forward/Update.do?contactId=${process.addressId}&dto(addressId)=${process.addressId}&dto(addressType)=${process.addressType}&dto(name1)=${app2:encode(process.addressName1)}&dto(name2)=${app2:encode(process.addressName2)}&dto(name3)=${app2:encode(process.addressName3)}&index=0"/>
                </c:otherwise>
            </c:choose>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="SALESPROCESS" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="processName" action="${editLink}" styleClass="listItem"
                              title="SalesProcess.name"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="addressName" styleClass="listItem" title="SalesProcess.contact"
                              headerStyle="listHeader" width="15%" orderable="true" maxLength="40"
                              renderData="false">
                <fanta:textShorter title="${process.addressName}">
                    <app:link action="${addressEditLink}" contextRelative="true" addModuleName="false">
                        <c:out value="${process.addressName}"/>
                    </app:link>
                </fanta:textShorter>
            </fanta:dataColumn>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="SalesProcess.employee"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="priorityName" styleClass="listItem" title="SalesProcess.priority"
                              headerStyle="listHeader" width="8%" orderable="true"/>
            <fanta:dataColumn name="probability" styleClass="listItemRight" title="SalesProcess.probability"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                ${process.probability}&nbsp;<fmt:message key="Common.probabilitySymbol"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="SalesProcess.startDate"
                              headerStyle="listHeader" width="7%" orderable="true" renderData="false">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(process.startDate)}"
                                pattern="${datePattern}"/>${dateValue}
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate" styleClass="listItem" title="SalesProcess.endDate"
                              headerStyle="listHeader"
                              width="7%" orderable="true" renderData="false">
                <fmt:formatDate value="${app2:intToDate(process.endDate)}" pattern="${datePattern}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="value" styleClass="listItemRight" title="SalesProcess.value"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                <fmt:formatNumber value="${process.value}" type="number" pattern="${numberFormat}"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="statusName" styleClass="listItem2" title="SalesProcess.status"
                              headerStyle="listHeader" width="7%" orderable="true"/>
        </fanta:table>
    </div>

    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>