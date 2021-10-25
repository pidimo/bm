<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ContactPerson/Search.do" focus="parameter(lastName@_firstName@_searchName)"
               styleClass="form-horizontal">
        <fieldset>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left" for="lastName@_firstName@_searchName_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group col-xs-12">
                        <html:text property="parameter(lastName@_firstName@_searchName)"
                                   styleId="lastName@_firstName@_searchName_id"
                                   styleClass="largeText form-control"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="ContactPerson/Search.do" parameterName="lastName" mode="bootstrap"/>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="searchContactPersonList" width="100%" id="contactPerson" action="ContactPerson/Search.do"
                     imgPath="${baselayout}" align="center"
                     styleClass="${app2:getFantabulousTableClases()}">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <fanta:actionColumn name="" title="Common.select" useJScript="true"
                                    action="javascript:parent.selectField('fieldContactPersonId_id', '${contactPerson.contactPersonId}', 'fieldContactPersonName_id', '${app2:jscriptEncode(contactPerson.contactPersonName)}');"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphImport()}"/>
            </fanta:columnGroup>
            <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="ContactPerson.name"
                              headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="department" styleClass="listItem" title="ContactPerson.department"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="function" styleClass="listItem2" title="ContactPerson.function"
                              headerStyle="listHeader" width="25%" orderable="true"/>
        </fanta:table>
    </div>
</div>