<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ImportRecord/Duplicate/List.do"
               focus="parameter(contactSearchName)"
               styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="ImportRecord.duplicate.title.list"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left" for="selectId">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(contactSearchName)"
                                   styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                       <html:submit styleClass="${app2:getFormButtonClasses()}">
                           <fmt:message key="Common.go"/>
                       </html:submit>
                    </span>
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="ImportRecord/Duplicate/List.do" parameterName="name1" mode="bootstrap"/>
    </div>

    <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
        <html:form action="/ImportRecord/Duplicate/Empty.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="ImportRecord.emptyDuplicates"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="importRecordDuplicateList"
                     width="100%"
                     id="importRecord"
                     action="ImportRecord/Duplicate/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="ImportRecord/Forward/Deduplication.do?importRecordId=${importRecord.importRecordId}&dto(importRecordId)=${importRecord.importRecordId}&dto(profileId)=${param.profileId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                    <fanta:actionColumn name="edit" title="Common.merge"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="100%"
                                        glyphiconClass="${app2:getClassGlyphMerge()}"/>
                </app2:checkAccessRight>
                <%--
                                    <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                                        <fanta:actionColumn name="delete" title="Common.delete"
                                                            action="${deleteLink}"
                                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                                            image="${baselayout}/img/delete.gif"/>
                                    </app2:checkAccessRight>
                --%>
            </fanta:columnGroup>
            <fanta:dataColumn name="addressName" action="${editLink}" styleClass="listItem" title="Contact.name"
                              headerStyle="listHeader" orderable="true" maxLength="40" width="35%"/>
            <fanta:dataColumn name="countryName" styleClass="listItem" title="Contact.country"
                              headerStyle="listHeader" orderable="true" width="10%"/>
            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                              headerStyle="listHeader" orderable="true" width="10%"/>
            <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"
                              headerStyle="listHeader" orderable="true" width="15%"/>
            <fanta:dataColumn name="street" styleClass="listItem" title="Contact.street"
                              headerStyle="listHeader" width="15%"/>
            <fanta:dataColumn name="houseNumber" styleClass="listItem2" title="Contact.houseNumber"
                              headerStyle="listHeader" width="10%"/>
        </fanta:table>
    </div>
</div>
