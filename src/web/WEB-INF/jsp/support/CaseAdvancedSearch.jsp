<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<script>
    function myReset() {
        var form = document.caseListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<fmt:message key="datePattern" var="datePattern"/>

<html:form action="/Case/AdvancedSearch.do?t=a" focus="parameter(number)" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Case.Title.advancedSearch"/>
            </legend>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.number"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(number)" styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="1"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Contact.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(contact_name1@_contact_name2@_contact_name3@_contact_searchName)"
                                   styleClass="largeText ${app2:getFormInputClasses()}" tabindex="2"/>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(caseTitle)" styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="3"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.productName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(productName)"
                                   styleClass="largeText ${app2:getFormInputClasses()}" tabindex="4"/>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.keywords"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(keywords)" styleClass="largeText ${app2:getFormInputClasses()}"
                                   tabindex="5"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.totalHours"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:text property="parameter(totalHours)"
                                   styleClass="numberText ${app2:getFormInputClasses()}" tabindex="6"/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CaseType.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(caseTypeId)" listName="caseTypeList" labelProperty="name"
                                      tabIndex="7"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}" readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap.companyId}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.openDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(openDateFrom)"
                                                  maxlength="10"
                                                  tabindex="8"
                                                  styleId="openDate1"
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
                                    <app:dateText property="parameter(openDateTo)"
                                                  maxlength="10"
                                                  tabindex="9"
                                                  styleId="openDate2"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Priority.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(priorityId)" listName="selectPriorityList"
                                      labelProperty="name" tabIndex="10"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}" readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="SUPPORT"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.expireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(expireDateFrom)"
                                                  maxlength="10"
                                                  tabindex="11"
                                                  styleId="expireDate1"
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
                                    <app:dateText property="parameter(expireDateTo)"
                                                  maxlength="10"
                                                  tabindex="12"
                                                  styleId="expireDate2"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="CaseSeverity.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(severityId)" listName="caseSeverityList" labelProperty="name"
                                      tabIndex="13"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}" readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Common.closeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.from" var="from"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(closeDateFrom)"
                                                  maxlength="10"
                                                  tabindex="14"
                                                  styleId="closeDate1"
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
                                    <app:dateText property="parameter(closeDateTo)"
                                                  maxlength="10"
                                                  tabindex="15"
                                                  styleId="closeDate2"
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="State.title"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(stateId)" listName="stateBaseList" labelProperty="name"
                                      tabIndex="16"
                                      valueProperty="id" firstEmpty="true"
                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}" readOnly="${onlyView}"
                                      module="/catalogs">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="1"/>
                        </fanta:select>
                    </div>
                </div>
            </div>
        </fieldset>
        <div class="row">
            <div class="col-xs-12">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="17"><fmt:message key="Common.go"/></html:submit>
                <html:button property="reset" tabindex="18" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="myReset()">
                    <fmt:message key="Common.clear"/></html:button>
            </div>
        </div>
    </div>

    <!-- choose alphbet to simple and advanced search -->
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Case/AdvancedSearch.do?t=a" mode="bootstrap" parameterName="caseTitleAlpha"/>
    </div>

</html:form>


<app2:checkAccessRight functionality="CASE" permission="CREATE">
    <c:set var="newButtonsTable" scope="page">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/Case/Forward/Create.do?advancedListForward=CaseAdvancedSearch"
                     addModuleParams="false" var="newCaseUrl"/>
            <input type="button" class="button ${app2:getFormButtonClasses()}"
                   value="<fmt:message key="Common.new"/>"
                   onclick="window.location ='${newCaseUrl}'"/>
        </div>
    </c:set>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>
<app2:checkAccessRight functionality="CASE" permission="DELETE" var="canDelete"/>
<app2:checkAccessRight functionality="CASE" permission="VIEW" var="canUpdate"/>

<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%" id="case" action="Case/AdvancedSearch.do?t=a"
                 styleClass="${app2:getFantabulousTableLargeClases()}" imgPath="${baselayout}">
        <c:set var="editAction"
               value="${edit}?dto(caseTitle)=${app2:encode(case.caseTitle)}&caseId=${case.id}&index=0&advancedListForward=CaseAdvancedSearch"/>
        <c:set var="deleteAction"
               value="${delete}?caseId=${case.id}&dto(caseTitle)=${app2:encode(case.caseTitle)}&index=0&advancedListForward=CaseAdvancedSearch"/>
        <fanta:columnGroup title="Common.action" headerStyle="listHeader">
            <c:if test="${canUpdate}">
                <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </c:if>
            <c:if test="${canDelete && sessionScope.user.valueMap.userType!=0}">
                <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </c:if>
        </fanta:columnGroup>
        <fanta:dataColumn name="number" action="${editAction}" styleClass="listItem" title="Common.number"
                          headerStyle="listHeader" width="4%" orderable="true"/>

        <fanta:dataColumn name="caseTitle" action="${editAction}" styleClass="listItem" title="Common.title"
                          headerStyle="listHeader" width="16%" orderable="true" maxLength="30"/>

        <fanta:dataColumn name="typeName" styleClass="listItem" title="CaseType.title"
                          headerStyle="listHeader" width="6%" orderable="true"/>

        <fanta:dataColumn name="priorityName" styleClass="listItem" title="Priority.title"
                          headerStyle="listHeader" width="6%" orderable="true"/>

        <fanta:dataColumn name="severityName" styleClass="listItem" title="CaseSeverity.title"
                          headerStyle="listHeader" width="6%" orderable="true"/>

        <fanta:dataColumn name="stateName" styleClass="listItem" title="State.title"
                          headerStyle="listHeader" width="6%" orderable="true"/>

        <fanta:dataColumn name="toUser" styleClass="listItem" title="Common.assignedTo"
                          headerStyle="listHeader" width="10%" orderable="true"/>

        <fanta:dataColumn name="openByUser" styleClass="listItem" title="Common.openBy"
                          headerStyle="listHeader" width="10%" orderable="true"/>

        <fanta:dataColumn name="contact" styleClass="listItem" title="Contact.title"
                          headerStyle="listHeader" width="10%" orderable="true"/>

        <fanta:dataColumn name="productName" styleClass="listItem" title="Product.title"
                          headerStyle="listHeader" width="6%" orderable="true"/>

        <fanta:dataColumn name="openDate" styleClass="listItem" title="Common.openDate"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <fmt:formatDate value="${app2:intToDate(case.openDate)}" pattern="${datePattern}"/>
        </fanta:dataColumn>

        <fanta:dataColumn name="closeDate" styleClass="listItem2" title="Common.closeDate"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            <fmt:formatDate value="${app2:intToDate(case.closeDate)}" pattern="${datePattern}"/>&nbsp;
        </fanta:dataColumn>

    </fanta:table>
</div>
<c:out value="${newButtonsTable}" escapeXml="false"/>
