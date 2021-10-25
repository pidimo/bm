
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


<html:form action="/Activity/CampContact/User/AutomaticAssign.do" focus="dto(homogeneously)" styleId="userListForm">
    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    <html:hidden property="dto(activityId)" value="${param.activityId}"/>


<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td class="button" colspan="2">
            <app:url var="urlAddUser" value="/Activity/Add/UserImportList.do?activityId=${param.activityId}"/>

            <html:button  property="addContactButton" styleClass="button" onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
                <fmt:message key="Campaign.activity.user.addUser"/>
            </html:button>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <table border="0" align="center" width="100%" cellpadding="2" cellspacing="0">
                <tr>
                    <td>
                        <iframe name="frame1"
                                src="<app:url value="Activity/AutomaticAssign/User/List.do?activityId=${param.activityId}&campaignId=${param.campaignId}"/>"
                                class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                        </iframe>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

    <tr>
        <td class="title" colspan="2">
            <fmt:message key="Activity.user.assign.options"/>
        </td>
    </tr>
    <tr>
        <td class="contain" width="50%">
            <html:checkbox property="dto(homogeneously)" styleId="homogeneously_id" styleClass="radio" tabindex="2">
                <fmt:message key="Activity.user.assign.homogeneouslyDistributed"/>
            </html:checkbox>
        </td>
        <td class="contain">
            <html:checkbox property="dto(customerPriority)" styleId="customerPriority_id" styleClass="radio" tabindex="3">
                <fmt:message key="Activity.user.assign.withPriorityforCustomerResp"/>
            </html:checkbox>
        </td>
    </tr>
</table>

    <table cellSpacing="0" cellPadding="0" width="80%" border="0" align="center">
        <tr>
            <td class="button">

<app2:checkAccessRight functionality="ACTIVITYUSER" permission="UPDATE">
                    <html:submit property="assignButton" styleClass="button" tabindex="4">
                        <fmt:message key="Campaign.activity.user.assign"/>
                    </html:submit>
</app2:checkAccessRight>

                    <html:cancel styleClass="button" tabindex="5">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
            </td>
        </tr>
    </table>

</html:form>

