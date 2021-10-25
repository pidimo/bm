<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="Organization/Employee/SearchContact" focus="parameter(lastName)" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="lastName_id">
                            <fmt:message key="Contact.Person.lastname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(lastName)"
                                       styleId="lastName_id"
                                       styleClass="${app2:getFormInputClasses()} middleText"
                                       maxlength="40"
                                       tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="searchName_id">
                            <fmt:message key="Contact.Person.searchName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(searchName)"
                                       styleId="searchName_id"
                                       styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                       tabindex="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="firstName_id">
                            <fmt:message key="Contact.Person.firstname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(firstName)"
                                       styleId="firstName_id"
                                       styleClass="${app2:getFormInputClasses()} middleText"
                                       maxlength="40"
                                       tabindex="3"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit property="parameter(searchButton)" styleClass="${app2:getFormButtonClasses()}"
                         tabindex="4">
                <fmt:message key="Common.search"/>
            </html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="5">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>

<div class="col-xs-12">
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="searchNotContactPersonList" styleClass="${app2:getFantabulousTableClases()}" width="100%"
                     id="contactPerson"
                     action="Organization/Employee/SearchContact.do" imgPath="${baselayout}" align="center">
            <app2:checkAccessRight functionality="EMPLOYEE" permission="CREATE">
                <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                    <fanta:actionColumn name=""
                                        action="Employee/Import.do?dto(employeeAddresId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"
                                        title="Common.import" styleClass="listItemCenter" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphImport()}"/>
                </fanta:columnGroup>
            </app2:checkAccessRight>
            <fanta:dataColumn name="contactPersonName" useJScript="true"
                              action="javascript:viewContactDetailInfo(1,${contactPerson.contactPersonId});"
                              styleClass="listItem" title="ContactPerson.name" headerStyle="listHeader" width="45%"
                              orderable="true" maxLength="40"/>
            <fanta:dataColumn name="department" styleClass="listItem" title="ContactPerson.department"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="function" styleClass="listItem2" title="ContactPerson.function"
                              headerStyle="listHeader" width="25%" orderable="true"/>
        </fanta:table>
    </div>
</div>
<tags:jQueryValidation formName="listForm"/>

