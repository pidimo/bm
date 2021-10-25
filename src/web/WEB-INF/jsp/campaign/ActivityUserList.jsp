<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup styleId="addContactButton_popup_id"
                          isLargeModal="true" modalTitleKey="Campaign.activity.user.searchUser"/>
<script language="JavaScript">
    function goSubmit() {
        document.forms[0].submit();
    }

    function goClose() {
        parent.close();
    }
    function showPopup(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        launchBootstrapPopup("addContactButton_popup_id", url, searchName, autoSubmit);
    }

    function selectedSubmit() {
        document.getElementById("userListForm").submit();
    }
</script>

<%--
#########A${param.activityId}
#########C${param.campaignId}
--%>

<html:form action="/Activity/User/List.do?activityId=${param.activityId}" styleId="userListForm">
    <!--this to refresh user list-->
</html:form>
<app:url var="urlAddUser" value="/Activity/Add/UserImportList.do?activityId=${param.activityId}"/>
<div class="${app2:getFormButtonWrapperClasses()}">
    <html:button property="addContactButton" styleClass="button ${app2:getFormButtonClasses()}"
                 onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
        <fmt:message key="Campaign.activity.user.addUser"/>
    </html:button>

    <app:url var="urlActivityUpdate"
             value="/CampaignActivity/Forward/Update.do?dto(activityId)=${param.activityId}&dto(campaignId)=${param.campaignId}&dto(op)=read"/>
    <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlActivityUpdate}'">
        <fmt:message key="Common.cancel"/>
    </html:button>
</div>

<legend class="title">
    <c:out value="${title}"/>
</legend>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="activityUserList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 align="left" width="100%" id="actUser"
                 action="Activity/User/List.do?activityId=${param.activityId}" imgPath="${baselayout}">

        <app:url
                value="/Campaign/Activity/User/Forward/Update.do?dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&dto(op)=read"
                var="urlUpdate" enableEncodeURL="false"/>
        <app:url
                value="/Campaign/Activity/User/Forward/Delete.do?dto(withReferences)=true&dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&dto(op)=read"
                var="urlDelete" enableEncodeURL="false"/>
        <app:url
                value="/Activity/User/Forward/Assign.do?dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&employeeId=${actUser.userAddressId}&userId=${actUser.userId}&userName=${app2:encode(actUser.userName)}&dto(op)=read"
                var="urlAssign"/>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="ACTIVITYUSER" permission="DELETE">
                <fanta:actionColumn action="${urlDelete}"
                                    title="Common.delete"
                                    styleClass="listItem" headerStyle="listHeader" width="50%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}" name="del" label="Common.delete">
                </fanta:actionColumn>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="ACTIVITYUSER" permission="VIEW">
                <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%"
                                    render="false">
                    <c:if test="${actUser.activityId != null && actUser.activityId != ''}">
                        <a href="${urlAssign}" title="<fmt:message key="Campaign.activity.user.assign"/>">
                            <span class="${app2:getClassGlyphContactGroupEdit()}"></span>
                        </a>
                    </c:if>
                </fanta:actionColumn>
            </app2:checkAccessRight>
        </fanta:columnGroup>

        <fanta:dataColumn name="userName" styleClass="listItem" title="Campaign.activity.campContact.assign.responsible"
                          headerStyle="listHeader" width="85%"
                          orderable="true"/>
        <fanta:dataColumn name="" styleClass="listItem2Right" title="Campaign.activity.campContact.assign.assignedContacts"
                          headerStyle="listHeader" width="10%"
                          renderData="false" nowrap="nowrap">
            <fanta:label var="countVar" listName="CountActivityUserContactList" module="/campaign" patron="0"
                         label="contactCount" columnOrder="contactCount">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="activityId" value="${param.activityId}"/>
                <fanta:parameter field="userId" value="${actUser.userId}"/>
            </fanta:label>
            <c:if test="${empty countVar}">
                <c:set var="countVar" value="0"/>
            </c:if>
            <c:out value="${countVar}"/>
        </fanta:dataColumn>

    </fanta:table>
</div>

<div class="${app2:getFormButtonWrapperClasses()}">
    <html:button property="addContactButton" styleClass="button ${app2:getFormButtonClasses()}"
                 onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
        <fmt:message key="Campaign.activity.user.addUser"/>
    </html:button>

    <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlActivityUpdate}'">
        <fmt:message key="Common.cancel"/>
    </html:button>
</div>
