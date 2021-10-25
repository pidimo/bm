<%@ include file="/Includes.jsp" %>
<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ContactPerson/Add" focus="parameter(name1)" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fielset>
                <legend class="title">
                    <fmt:message key="ContactPerson.add"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name1">
                            <fmt:message key="Contact.Person.lastname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(name1)"
                                       styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                       tabindex="1"
                                       styleId="name1"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="searchName">
                            <fmt:message key="Contact.Person.searchName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(searchName)"
                                       styleClass="middleText ${app2:getFormInputClasses()}" maxlength="40"
                                       tabindex="2"
                                       styleId="searchName"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name2">
                            <fmt:message key="Contact.Person.firstname"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(name2)" styleClass="middleText ${app2:getFormInputClasses()}"
                                       maxlength="40"
                                       tabindex="3"
                                       styleId="name2"/>
                        </div>
                    </div>
                </div>
            </fielset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit property="parameter(searchButton)" styleClass="button ${app2:getFormButtonClasses()}"
                         tabindex="4">
                <fmt:message key="Common.search"/>
            </html:submit>
            <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
                <html:submit property="parameter(newButton)" styleClass="${app2:getFormButtonClasses()}"
                             tabindex="5">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="6">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="personSearchList_CPerson" width="100%" id="contact" action="ContactPerson/SearchContact.do"
                     imgPath="${baselayout}" align="center" styleClass="${app2:getFantabulousTableClases()}">
            <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="import"
                                        action="ContactPerson/Import.do?dto(importAddress)=true&dto(addressIdToImport)=${contact.addressId}&dto(addressType)=${contact.addressType}&dto(name1)=${app2:encode(contact.name1)}&dto(name2)=${app2:encode(contact.name2)}"
                                        title="Common.import" styleClass="listItemCenter" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphImport()}"/>
                </fanta:columnGroup>
            </app2:checkAccessRight>
            <fanta:dataColumn name="personName" useJScript="true"
                              action="javascript:viewContactDetailInfo(1,${contact.addressId});" styleClass="listItem"
                              title="Contact.name" headerStyle="listHeader" width="50%" orderable="true"
                              maxLength="40"/>
            <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                              headerStyle="listHeader" width="7%" orderable="true"/>
            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                              width="7%" orderable="true"/>
            <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                              width="26%" orderable="true"/>
        </fanta:table>
    </div>
</div>