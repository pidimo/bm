<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="STATUS_COMPLETED" value="<%=ContactConstants.DedupliContactStatus.COMPLETED.getConstant().toString()%>"/>
<c:set var="STATUS_PROCESS" value="<%=ContactConstants.DedupliContactStatus.PROCESS.getConstant().toString()%>"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<%--fragment to add progress bar--%>
<c:import url="/WEB-INF/jsp/contacts/checkDuplicateProgressBarFragment.jsp"/>

<script language="JavaScript" type="text/javascript">

    function startCheckDuplicates() {
        showCheckDuplicateProgressBar();
    }

</script>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
        <html:form action="/DedupliContact/Process.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" onclick="startCheckDuplicates();">
                    <fmt:message key="DedupliContact.startCheckDuplicates"/>
                </html:submit>
            </div>
        </html:form>

    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="dedupliContactList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="dedupliContact"
                     action="DedupliContact/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="DedupliContact/Duplicate/List.do?dedupliContactId=${dedupliContact.dedupliContactId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                    <fanta:actionColumn name="edit" title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader" width="100%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="startTime" action="${editLink}" styleClass="listItem"
                              title="DedupliContact.startTime"
                              headerStyle="listHeader" width="30%" orderable="true" renderData="false">
                <c:if test="${not empty dedupliContact.startTime}">
                    ${app2:getDateWithTimeZone(dedupliContact.startTime, timeZone, dateTimePattern)}
                </c:if>
            </fanta:dataColumn>

            <fanta:dataColumn name="userName" styleClass="listItem" title="DedupliContact.userName"
                              headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
            <fanta:dataColumn name="status" styleClass="listItem2" title="DedupliContact.status"
                              headerStyle="listHeader" orderable="true" renderData="false" width="20%">

                <c:choose>
                    <c:when test="${dedupliContact.status == STATUS_COMPLETED}">
                        <fmt:message key="DedupliContact.status.completed"/>
                    </c:when>
                    <c:when test="${dedupliContact.status == STATUS_PROCESS}">
                        <fmt:message key="DedupliContact.status.process"/>
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

