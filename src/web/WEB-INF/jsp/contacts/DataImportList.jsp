<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="TYPE_ORGANIZATION" value="<%=ContactConstants.ImportProfileType.ORGANIZATION.getConstant().toString()%>"/>
<c:set var="TYPE_PERSON" value="<%=ContactConstants.ImportProfileType.PERSON.getConstant().toString()%>"/>
<c:set var="TYPE_ORG_AND_CONTACTPERSON"
       value="<%=ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.getConstant().toString()%>"/>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
        <html:form action="/DataImport/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="importProfileList"
                     width="100%"
                     id="importProfile"
                     action="DataImport/List.do"
                     imgPath="${baselayout}"
                     align="center"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="editLink"
                   value="DataImport/Forward/Update.do?profileId=${importProfile.profileId}&dto(profileId)=${importProfile.profileId}&dto(label)=${app2:encode(importProfile.label)}&index=0"/>
            <c:set var="deleteLink"
                   value="DataImport/Forward/Delete.do?profileId=${importProfile.profileId}&dto(profileId)=${importProfile.profileId}&dto(label)=${app2:encode(importProfile.label)}&dto(withReferences)=true&index=0"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="label" action="${editLink}" styleClass="listItem"
                              title="dataImport.configuration.name"
                              headerStyle="listHeader"
                              width="75%"
                              orderable="true"
                              maxLength="40"/>
            <fanta:dataColumn name="profileType" styleClass="listItem2" title="dataImport.configuration.type"
                              headerStyle="listHeader" orderable="true" renderData="false" width="20%">

                <c:choose>
                    <c:when test="${importProfile.profileType == TYPE_ORGANIZATION}">
                        <fmt:message key="dataImport.type.Organization"/>
                    </c:when>
                    <c:when test="${importProfile.profileType == TYPE_PERSON}">
                        <fmt:message key="dataImport.type.Person"/>
                    </c:when>
                    <c:when test="${importProfile.profileType == TYPE_ORG_AND_CONTACTPERSON}">
                        <fmt:message key="dataImport.type.OrganizationAndContactPerson"/>
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>

</div>