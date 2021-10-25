<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="isContactPersonDedupli" value="${param.isContactPersonDedupli}" scope="request"/>

<table id="tableId" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td class="title">
            <fmt:message var="listTitle" key="ImportRecord.dupli.contactPerson.list.title"/>
            <c:if test="${isContactPersonDedupli}">
                <fmt:message var="listTitle" key="ImportRecord.deduplicate.contactPersonDuplicates"/>
            </c:if>

            <c:out value="${listTitle}"/>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="importRecordContactPersonDupliList"
                         width="100%"
                         id="importRecord"
                         action="ImportRecord/Duplicate/ContactPerson/List.do?isContactPersonDedupli=${isContactPersonDedupli}"
                         imgPath="${baselayout}"
                         align="center">

                <c:choose>
                    <c:when test="${isContactPersonDedupli}">

                        <app:url var="editLink" value="ImportRecord/ContactPerson/Forward/Deduplication.do?importRecordId=${importRecord.importRecordId}&dto(importRecordId)=${importRecord.importRecordId}&dto(profileId)=${param.profileId}&dto(organizationId)=${importRecord.organizationId}&dto(parentRecordId)=${importRecord.parentRecordId}"/>

                        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                                <fanta:actionColumn useJScript="true"
                                                    action="javascript:goParentURL('${editLink}')"
                                                    name="edit" title="Common.merge"
                                                    styleClass="listItem"
                                                    headerStyle="listHeader" width="100%" image="${baselayout}/img/merge.png"/>
                            </app2:checkAccessRight>
                        </fanta:columnGroup>

                        <fanta:dataColumn useJScript="true" action="javascript:goParentURL('${editLink}')"
                                          name="addressName" styleClass="listItem" title="ImportRecord.dupli.contactPerson.name"
                                          headerStyle="listHeader" orderable="true" width="65%"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:dataColumn name="addressName" styleClass="listItem" title="ImportRecord.dupli.contactPerson.name"
                                          headerStyle="listHeader" orderable="true" width="70%"/>
                    </c:otherwise>
                </c:choose>

                <fanta:dataColumn name="function" styleClass="listItem2" title="ContactPerson.function"
                                  headerStyle="listHeader" orderable="true" width="30%"/>
            </fanta:table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>
