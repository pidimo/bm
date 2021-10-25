<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    <!--
    function goSubmit() {
        document.forms[0].submit();
    }

    function goClose() {
        window.close();
    }

    /*function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = 'true' == ss;
        //return issubmit;
        return true;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        var ss = document.getElementById('isSubmit').value;
    }*/

    function showPopup(url, searchName, submitOnSelect, w, h,scroll){
        autoSubmit = submitOnSelect;

        var winx = (screen.width)/2;
        var winy = (screen.height)/2;
        var posx = winx - w/2;
        var posy = winy - h/2;
        searchWindow=window.open(url, searchName, 'resizable=yes,width='+w+',height='+h+',status,left='+posx+',top='+posy+',scrollbars='+scroll);
        searchWindow.focus();
    }

    function selectedSubmit(){
        document.getElementById("userListForm").submit();
    }
    //-->
</script>

<%--
#########A${param.activityId}
#########C${param.campaignId}
--%>

<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2">
            <html:form action="/Activity/User/List.do?activityId=${param.activityId}" styleId="userListForm" >
                <!--this to refresh user list-->
            </html:form>
        </td>
    </tr>
    <tr>
        <td class="button">
            <app:url var="urlAddUser" value="/Activity/Add/UserImportList.do?activityId=${param.activityId}"/>

            <html:button  property="addContactButton" styleClass="button" onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
                <fmt:message key="Campaign.activity.user.addUser"/>
            </html:button>

            <app:url var="urlActivityUpdate" value="/CampaignActivity/Forward/Update.do?dto(activityId)=${param.activityId}&dto(campaignId)=${param.campaignId}&dto(op)=read"/>
            <html:button property="" styleClass="button" onclick="location.href='${urlActivityUpdate}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </td>
    </tr>
    <tr>
        <td class="title">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <fanta:table list="activityUserList" align="left" width="100%" id="actUser"
                         action="Activity/User/List.do?activityId=${param.activityId}" imgPath="${baselayout}">

                <app:url value="/Campaign/Activity/User/Forward/Update.do?dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&dto(op)=read"
                        var="urlUpdate" enableEncodeURL="false"/>
                <app:url value="/Campaign/Activity/User/Forward/Delete.do?dto(withReferences)=true&dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&dto(op)=read"
                        var="urlDelete" enableEncodeURL="false"/>
                <app:url value="/Activity/User/Forward/Assign.do?dto(activityId)=${actUser.activityId}&dto(userId)=${actUser.userId}&dto(userName)=${app2:encode(actUser.userName)}&employeeId=${actUser.userAddressId}&userId=${actUser.userId}&userName=${app2:encode(actUser.userName)}&dto(op)=read"
                        var="urlAssign"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
<app2:checkAccessRight functionality="ACTIVITYUSER" permission="DELETE">
                    <fanta:actionColumn action="${urlDelete}"
                                        title="Common.delete"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/delete.gif" name="del" label="Common.delete">
                    </fanta:actionColumn>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="ACTIVITYUSER" permission="VIEW">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%"
                                        render="false">
                        <c:if test="${actUser.activityId != null && actUser.activityId != ''}">
                            <a href="${urlAssign}" title="<fmt:message key="Campaign.activity.user.assign"/>">
                                <img src="${baselayout}/img/webmail/contactgroup_edit.gif" alt="" border="0"/>
                            </a>
                        </c:if>
                    </fanta:actionColumn>
</app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="userName" styleClass="listItem" title="Campaign.activity.campContact.assign.responsible" headerStyle="listHeader" width="85%"
                                  orderable="true"/>
                <fanta:dataColumn name=""  styleClass="listItem2Right" title="Campaign.activity.campContact.assign.assignedContacts" headerStyle="listHeader" width="10%"
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
        </td>
    </tr>
</table>

<table cellSpacing="0" cellPadding="0" width="80%" border="0" align="center">
    <tr>
        <td class="button">
            <html:button  property="addContactButton" styleClass="button" onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
                <fmt:message key="Campaign.activity.user.addUser"/>
            </html:button>

            <html:button property="" styleClass="button" onclick="location.href='${urlActivityUpdate}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </td>
    </tr>
</table>

