<%@ include file="/Includes.jsp" %>


<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="DedupliContact.duplicate.title.list"/>
        </td>
    </tr>
    <tr>
        <html:form action="/DedupliContact/Duplicate/List.do" focus="parameter(contactSearchName)">
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
            <fanta:alphabet action="DedupliContact/Duplicate/List.do" parameterName="name1"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br>
            <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/DedupliContact/Duplicate/Empty.do">
                            <td class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="DedupliContact.emptyDuplicates"/>
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
            <fanta:table list="dedupliContactDuplicateList"
                         width="100%"
                         id="duplicateGroup"
                         action="DedupliContact/Duplicate/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink" value="DedupliContact/Forward/Deduplication.do?duplicateGroupId=${duplicateGroup.duplicateGroupId}&dto(duplicateGroupId)=${duplicateGroup.duplicateGroupId}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                        <fanta:actionColumn name="edit" title="Common.merge"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="100%" image="${baselayout}/img/merge.png"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="addressName" action="${editLink}" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="7%" orderable="true"/>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="7%" orderable="true"/>
                <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
</table>
