<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Country/List.do" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="countryName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(countryName)" styleId="countryName_id"
                               styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                       <html:submit styleClass="${app2:getFormButtonClasses()}">
                           <fmt:message key="Common.go"/>
                       </html:submit>
                    </span>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Country/List.do" parameterName="countryNameAlpha" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="COUNTRY" permission="CREATE">
        <html:form styleId="CREATE_NEW_COUNTRY" action="/Country/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="countryList" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="country"
                     action="Country/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Country/Forward/Update.do?dto(countryId)=${country.id}&dto(currencyId)=${country.myCurrencyId}&dto(countryName)=${app2:encode(country.name)}"/>
            <c:set var="deleteAction"
                   value="Country/Forward/Delete.do?dto(withReferences)=true&dto(countryId)=${country.id}&dto(countryName)=${app2:encode(country.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="COUNTRY" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="COUNTRY" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Country.name"
                              headerStyle="listHeader" width="40%" orderable="true" maxLength="25">
            </fanta:dataColumn>
            <fanta:dataColumn name="areaCode" styleClass="listItem" title="Country.areaCode"
                              headerStyle="listHeader" width="15%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="prefix" styleClass="listItem" title="Country.prefix" headerStyle="listHeader"
                              width="15%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="currency" styleClass="listItem2" title="Country.currency"
                              headerStyle="listHeader" width="30%" orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>
