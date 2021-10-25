<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<app2:jScriptUrl
        url="/campaign/Activity/User/AutomaticAssign/Forward/Update.do?dto(op)=read"
        var="jsUpdateUrl">
    <app2:jScriptUrlParam param="dto(activityId)" value="idActivity"/>
    <app2:jScriptUrlParam param="dto(userId)" value="idUser"/>
    <app2:jScriptUrlParam param="dto(userName)" value="nameUser"/>
    <app2:jScriptUrlParam param="dto(homogeneously)" value="isHomogeneous"/>
    <app2:jScriptUrlParam param="dto(customerPriority)" value="custPriority"/>
</app2:jScriptUrl>


<app2:jScriptUrl
        url="/campaign/Activity/User/AutomaticAssign/Forward/Delete.do?dto(withReferences)=true&dto(op)=read"
        var="jsDeleteUrl">
    <app2:jScriptUrlParam param="dto(activityId)" value="idActivity"/>
    <app2:jScriptUrlParam param="dto(userId)" value="idUser"/>
    <app2:jScriptUrlParam param="dto(userName)" value="nameUser"/>
    <app2:jScriptUrlParam param="dto(homogeneously)" value="isHomogeneous"/>
    <app2:jScriptUrlParam param="dto(customerPriority)" value="custPriority"/>
</app2:jScriptUrl>




<script language="JavaScript" type="text/javascript">
    function getUpdateUrl(idActivity, idUser, nameUser){
        var isHomogeneous = getHomogeneouslyValue();
        var custPriority = getCustPriorityValue();
        goParentURL(${jsUpdateUrl});
    }

    function getDeleteUrl(idActivity, idUser, nameUser){
        var isHomogeneous = getHomogeneouslyValue();
        var custPriority = getCustPriorityValue();
        goParentURL(${jsDeleteUrl});
    }

    function getHomogeneouslyValue(){
        var value = "";
        var checkHomogeneous = window.parent.document.getElementById("homogeneously_id");
        if (checkHomogeneous != undefined && checkHomogeneous.checked) {
            value = "on";
        }
        return value;
    }

    function getCustPriorityValue(){
        var value = "";
        var checkCustPriority = window.parent.document.getElementById("customerPriority_id");
        if (checkCustPriority != undefined && checkCustPriority.checked) {
            value = "on";
        }
        return value;
    }

</script>

<table cellpadding="0" cellspacing="0" border="0" id="tableId" width="100%">
<tr>
<td>
<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td class="title">
            <fmt:message key="Campaign.activity.user.automaticAssign"/>
        </td>
    </tr>
</table>

<fanta:table list="activityUserList" align="left" width="100%" id="actUser"
             action="Activity/AutomaticAssign/User/List.do?activityId=${param.activityId}" imgPath="${baselayout}">


    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
<app2:checkAccessRight functionality="ACTIVITYUSER" permission="VIEW">
        <fanta:actionColumn useJScript="true" action="javascript:getUpdateUrl('${actUser.activityId}','${actUser.userId}','${app2:jscriptEncode(actUser.userName)}')"
                            title="Common.update"
                            styleClass="listItem" headerStyle="listHeader" width="50%"
                            image="${baselayout}/img/edit.gif" name="upd" label="Common.update">
        </fanta:actionColumn>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ACTIVITYUSER" permission="DELETE">
        <fanta:actionColumn useJScript="true" action="javascript:getDeleteUrl('${actUser.activityId}','${actUser.userId}','${app2:jscriptEncode(actUser.userName)}')"
                            title="Common.delete"
                            styleClass="listItem" headerStyle="listHeader" width="50%"
                            image="${baselayout}/img/delete.gif" name="del" label="Common.delete">
        </fanta:actionColumn>
</app2:checkAccessRight>

    </fanta:columnGroup>

    <fanta:dataColumn name="userName" useJScript="true" action="javascript:getUpdateUrl('${actUser.activityId}','${actUser.userId}','${app2:jscriptEncode(actUser.userName)}')"
                      styleClass="listItem" title="Campaign.activity.campContact.assign.responsible"
                      headerStyle="listHeader" width="80%"
                      orderable="true"/>

    <fanta:dataColumn name="percent" styleClass="listItem2" title="Campaign.activity.user.percent" headerStyle="listHeader" width="15%"
                      orderable="true" style="text-align:right" renderData="false" nowrap="nowrap">
        <c:if test="${not empty actUser.percent}">
                        ${actUser.percent}<fmt:message key="Common.probabilitySymbol"/>
        </c:if>
    </fanta:dataColumn>

</fanta:table>

</td>
</tr>
<tr>
    <td align="right">
        <fanta:label var="sumPrct" listName="ActivityUserSumPercentList" module="/campaign" patron="0"
                     label="sumPercent" columnOrder="sumPercent">
            <fanta:parameter field="activityId" value="${param.activityId}"/>
        </fanta:label>
        <c:if test="${empty sumPrct}">
            <c:set var="sumPrct" value="0"/>
        </c:if>
        <fmt:message key="Campaign.activity.user.automaticAssign.totalPercent"/>:&nbsp;${sumPrct}<fmt:message key="Common.probabilitySymbol"/>
    </td>
</tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>