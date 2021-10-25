<%@ include file="/Includes.jsp" %>
        <div class="table-responsive">
            <fmt:message var="datePattern" key="datePattern"/>

            <fanta:table mode="bootstrap" width="100%"
                         styleClass="${app2:getFantabulousTableClases()}"
                         id="activity"
                         action="${action}"
                         imgPath="${baselayout}">
                <c:set var="editAction" value="${edit}?dto(activityId)=${activity.id}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="CASEACTIVITY" permission="VIEW">

                        <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <c:remove var="expireClass"/>
                <c:if test="${activity.closeDate == null || activity.closeDate == ''}">
                    <c:set var="expireClass" value=" task_expireColor"/>
                </c:if>
                <fanta:dataColumn name="toUser" styleClass="listItem${expireClass}" title="Common.assignedTo"
                                  headerStyle="listHeader" width="34%" orderable="true" action="${editAction}"/>

                <fanta:dataColumn name="stateName" styleClass="listItem" title="State.title"
                                  headerStyle="listHeader" width="12%" orderable="true"/>

                <fanta:dataColumn name="fromUser" styleClass="listItem" title="Common.assignedFrom"
                                  headerStyle="listHeader" width="20%" orderable="true"/>

                <fanta:dataColumn name="workLevelName" styleClass="listItem" title="WorkLevel.title"
                                  headerStyle="listHeader" width="12%" orderable="true"/>

                <fanta:dataColumn name="openDateActivityId" styleClass="listItem" title="Common.openDate"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(activity.openDate)}" pattern="${datePattern}"/>
                </fanta:dataColumn>
                                        <!--closeDate_ActivityId-->
                <fanta:dataColumn name="closeDateActivityId" styleClass="listItem2" title="Common.closeDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate value="${app2:intToDate(activity.closeDate)}" pattern="${datePattern}"/>
                    &nbsp;
                </fanta:dataColumn>
            </fanta:table>
        </div>

