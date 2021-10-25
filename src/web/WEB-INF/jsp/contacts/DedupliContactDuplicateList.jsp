<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/DedupliContact/Duplicate/List.do" focus="parameter(contactSearchName)"
               styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="DedupliContact.duplicate.title.list"/>
            </legend>
        </fieldset>
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="contactSearchName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(contactSearchName)"
                               styleId="contactSearchName_id"
                               styleClass="${app2:getFormInputClasses()} largeText"/>
                    <div class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </div>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="DedupliContact/Duplicate/List.do" parameterName="name1" mode="bootstrap"/>
    </div>

    <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
        <html:form action="/DedupliContact/Duplicate/Empty.do">
            <div class="${app2:getSearchWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="DedupliContact.emptyDuplicates"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="dedupliContactDuplicateList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="duplicateGroup"
                     action="DedupliContact/Duplicate/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="DedupliContact/Forward/Deduplication.do?duplicateGroupId=${duplicateGroup.duplicateGroupId}&dto(duplicateGroupId)=${duplicateGroup.duplicateGroupId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                    <fanta:actionColumn name="edit" title="Common.merge"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="100%"
                                        glyphiconClass="${app2:getClassGlyphMerge()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="addressName" action="${editLink}" styleClass="listItem" title="Contact.name"
                              headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                              headerStyle="listHeader" width="15%" orderable="true"/>
            <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                              width="10%" orderable="true"/>
            <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                              width="20%" orderable="true"/>
        </fanta:table>
    </div>
</div>