<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>


<div class="${app2:getFormButtonWrapperClasses()}">
        <c:url var="url"
               value="/scheduler/TaskParticipant/Forward/Group/Create.do?taskId=${param.taskId}&index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}">
            <c:param name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        </c:url>
        <app2:checkAccessRight functionality="TASKUSER" permission="CREATE">
            <html:button property=""
                         styleClass="${app2:getFormButtonClasses()}"
                         onclick="location.href='${url}'">
                <fmt:message key="Appoinment.addUserGroup"/>
            </html:button>
        </app2:checkAccessRight>


        <c:url var="url1"
               value="/scheduler/TaskParticipant/Forward/Create.do?taskId=${param.taskId}&index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}">
            <c:param name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        </c:url>
        <app2:checkAccessRight functionality="TASKUSER" permission="CREATE">
            <html:button property=""
                         styleClass="${app2:getFormButtonClasses()}"
                         onclick="location.href='${url1}'">
                <fmt:message key="Appoinment.addUser"/>
            </html:button>
        </app2:checkAccessRight>
</div>


<%
    ResultList resultList = (ResultList) request.getAttribute("participantTaskList");
    pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
%>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="participantTaskList"
                 width="100%" id="participant"
                 action="Task/ParticipantTaskList.do"
                 styleClass="${app2:getFantabulousTableClases()}"
                 imgPath="${baselayout}">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <c:set var="deleteAction"
                   value="TaskParticipant/Forward/Delete.do?userGroupId=${participant.groupId}&size=${size}&taskId=${param.taskId}&dto(withReferences)=true&dto(scheduledUserId)=${participant.scheduledUserId}&dto(participantName)=${app2:encode(participant.userName)}&dto(taskGroupName)=${app2:encode(participant.groupName)}&dto(title)=${app2:encode(param['dto(title)'])}"/>
            <c:set var="updateAction"
                   value="TaskParticipant/Forward/Update.do?taskId=${param.taskId}&dto(scheduledUserId)=${participant.scheduledUserId}&dto(participantName)=${app2:encode(participant.userName)}&dto(taskGroupName)=${app2:encode(participant.groupName)}&dto(title)=${app2:encode(param['dto(title)'])}"/>
            <app2:checkAccessRight functionality="TASKUSER" permission="VIEW">
                <fanta:actionColumn name="update" label="Common.update" action="${updateAction}"
                                    title="Common.update" styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="TASKUSER" permission="DELETE">
                <fanta:actionColumn name="del" label="Common.delete" action="${deleteAction}"
                                    title="Common.delete" styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="userName" action="${updateAction}" styleClass="listItem"
                          title="Appointment.userName" headerStyle="listHeader" width="25%" orderable="true"/>
        <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                          headerStyle="listHeader" width="25%" orderable="true">
            <c:if test="${participant.type == '1'}">
                <fmt:message key="User.intenalUser"/>
            </c:if>
            <c:if test="${participant.type == '0'}">
                <fmt:message key="User.externalUser"/>
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn name="groupName" styleClass="listItem" title="Appointment.groupName"
                          headerStyle="listHeader" width="25%" orderable="true" maxLength="25"/>
        <fanta:dataColumn name="status" styleClass="listItem2" title="Task.status" headerStyle="listHeader"
                          width="20%" orderable="true" renderData="false">
            <c:set var="statusValue" value="${participant.status}"/>
            <c:if test="${statusValue == '1'}">
                <fmt:message key="Task.InProgress"/>
            </c:if>
            <c:if test="${statusValue == '2'}">
                <fmt:message key="Task.notInit"/>
            </c:if>
            <c:if test="${statusValue == '3'}">
                <fmt:message key="Scheduler.Task.Concluded"/>
            </c:if>
            <c:if test="${statusValue == '4'}">
                <fmt:message key="Task.Deferred"/>
            </c:if>
            <c:if test="${statusValue == '5'}">
                <fmt:message key="Task.ToCheck"/>
            </c:if>
        </fanta:dataColumn>
    </fanta:table>
</div>
