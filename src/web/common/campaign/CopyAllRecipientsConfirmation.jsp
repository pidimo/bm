<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    <!--
    //this is called in onLoad body property
    function goToParent(){
        opener.selectedSubmit();
        window.close();
    }

    function goClose() {
        window.close();
    }
    //-->
</script>

<br/>
<br/>
<table border="0" cellpadding="0" cellspacing="0" width="80%" class="container" align="center">
<html:form action="/Campaign/Activity/CampContact/Create/AllFromRecipients.do" focus="addAll">

<c:set var="op" value="createAllRecipients"/>
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
<html:hidden property="dto(activityId)" value="${param.activityId}"/>

    <tr>
        <td height="20" class="title"><fmt:message key="Campaign.activity.addAllRecipients"/></td>
    </tr>
    <tr>
        <td class="contain"><fmt:message key="Campaign.activity.copyAllContact.confirmation"/></td>
    </tr>
    <tr>
        <td  class="button">
            <html:submit property="addAll" styleId="addAll" styleClass="button" tabindex="1"><fmt:message key="Campaign.activity.campaignRecipients.addAll"/></html:submit>
            <html:cancel styleClass="button" tabindex="2"><fmt:message key="Common.cancel"/></html:cancel>
        </td>
    </tr>
</html:form>
</table>
