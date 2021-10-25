<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="state_inProgres">
    <%=CampaignConstants.ActivityStatus.IN_PROGRESS.getConstant()%>
</c:set>
<c:set var="state_concluded">
    <%=CampaignConstants.ActivityStatus.CONCLUDED.getConstant()%>
</c:set>
<c:set var="state_planned">
    <%=CampaignConstants.ActivityStatus.PLANNED.getConstant()%>
</c:set>
<fmt:message var="datePattern" key="datePattern"/>

<html:form action="/CampaignAvtivity/List.do" focus="parameter(title)" styleClass="form-horizontal">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left" for="title_id">
            <fmt:message key="Common.search"/>
        </label>
        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(title)"
                           styleId="title_id"
                           styleClass="largeText ${app2:getFormInputClasses()}"/>
               <div class="input-group-btn">
                   <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                       <fmt:message key="Common.go"/>
                   </html:submit>
               </div>
            </div>
        </div>
    </div>
</html:form>
<div class="clearfix"></div>
<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet mode="bootstrap" action="CampaignAvtivity/List.do" parameterName="title"/>
</div>

<html:form action="/CampaignActivity/Forward/Create.do">
   <div class="${app2:getFormButtonWrapperClasses()}">
       <app2:securitySubmit operation="create" functionality="CAMPAIGNACTIVITY"
                            styleClass="button ${app2:getFormButtonClasses()}">
           <fmt:message key="Common.new"/>
       </app2:securitySubmit>
   </div>
</html:form>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="campaignActivityList" width="100%" id="activity"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="CampaignAvtivity/List.do"
                 imgPath="${baselayout}">

        <c:set var="editAction"
               value="CampaignActivity/Forward/Update.do?dto(activityId)=${activity.activityId}&dto(title)=${app2:encode(activity.title)}"/>
        <c:set var="deleteAction"
               value="CampaignActivity/Forward/Delete.do?dto(withReferences)=true&dto(activityId)=${activity.activityId}&dto(title)=${app2:encode(activity.title)}"/>

        <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">

            <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="VIEW">
                <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="DELETE">
                <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                    styleClass="listItem" headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>

        <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem"
                          title="CampaignActivity.title" headerStyle="listHeader" width="50%"
                          orderable="true" maxLength="25">
        </fanta:dataColumn>

        <fanta:dataColumn name="startDate" styleClass="listItem"
                          title="CampaignActivity.startDate" headerStyle="listHeader" width="15%"
                          orderable="true" renderData="false" maxLength="25">
            <fmt:formatDate var="dateValue1" value="${app2:intToDate(activity.startDate)}"
                            pattern="${datePattern}"/>
            ${dateValue1}&nbsp;
        </fanta:dataColumn>

        <fanta:dataColumn name="closeDate" styleClass="listItem"
                          title="CampaignActivity.closeDate" headerStyle="listHeader" width="15%"
                          orderable="true" renderData="false" maxLength="25">
            <fmt:formatDate var="dateValue2" value="${app2:intToDate(activity.closeDate)}"
                            pattern="${datePattern}"/>
            ${dateValue2}&nbsp;
        </fanta:dataColumn>

        <fanta:dataColumn name="state" styleClass="listItem2"
                          title="CampaignActivity.state" headerStyle="listHeader" width="20%"
                          renderData="false"
                          maxLength="25">
            <c:choose>
                <c:when test="${activity.state == state_inProgres}">
                    <fmt:message key="Activity.status.InProgress"/>
                </c:when>
                <c:when test="${activity.state == state_concluded}">
                    <fmt:message key="Activity.status.concluded"/>
                </c:when>
                <c:when test="${activity.state == state_planned}">
                    <fmt:message key="Activity.status.planned"/>
                </c:when>
            </c:choose>
        </fanta:dataColumn>
    </fanta:table>
</div>
