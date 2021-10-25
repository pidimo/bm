<%@ include file="/Includes.jsp" %>


<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="ImportRecord.duplicate.title.list"/>
        </td>
    </tr>
    <tr>
        <html:form action="/ImportRecord/Duplicate/List.do" focus="parameter(contactSearchName)">
            <td class="label">
                <fmt:message key="Common.search"/>
            </td>
            <td class="contain">
                <html:text property="parameter(contactSearchName)" styleClass="largeText"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </td>
        </html:form>
    </tr>
    <tr>
        <td align="center" class="alpha" colspan="2">
            <fanta:alphabet action="ImportRecord/Duplicate/List.do" parameterName="name1"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br>
            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/ImportRecord/Duplicate/Empty.do">
                            <td class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="ImportRecord.emptyDuplicates"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <fanta:table list="importRecordDuplicateList"
                         width="100%"
                         id="importRecord"
                         action="ImportRecord/Duplicate/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink" value="ImportRecord/Forward/Deduplication.do?importRecordId=${importRecord.importRecordId}&dto(importRecordId)=${importRecord.importRecordId}&dto(profileId)=${param.profileId}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                        <fanta:actionColumn name="edit" title="Common.merge"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="100%" image="${baselayout}/img/merge.png"/>
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

        </td>
    </tr>
</table>
