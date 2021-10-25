<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:bootstrapModalPopup styleId="addUser_popup_id" isLargeModal="true"
                          modalTitleKey="Campaign.activity.user.searchUser"/>
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

    function showPopup(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        launchBootstrapPopup("addUser_popup_id", url, searchName, autoSubmit);
    }

    function selectedSubmit() {
        document.getElementById("userListForm").submit();
    }
    //-->
</script>

<%--
#########A${param.activityId}
#########C${param.campaignId}
--%>

<style>
    .embed-responsive-16by9.col-xs-12 {
        padding-bottom: 6.25%;
    }
</style>

<html:form action="/Activity/CampContact/User/AutomaticAssign.do" focus="dto(homogeneously)" styleId="userListForm"
           styleClass="form-horizontal">
    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    <html:hidden property="dto(activityId)" value="${param.activityId}"/>

    <app:url var="urlAddUser" value="/Activity/Add/UserImportList.do?activityId=${param.activityId}"/>

    <div class="row">
        <div class="col-xs-12">
            <html:button property="addContactButton" styleClass="button ${app2:getFormButtonClasses()}"
                         onclick="JavaScript:showPopup('${urlAddUser}','addUser','false',700,500,1)">
                <fmt:message key="Campaign.activity.user.addUser"/>
            </html:button>
        </div>
    </div>
    <br/>

    <div class="${app2:getFormPanelClasses()}">
        <div class="embed-responsive embed-responsive-16by9 col-xs-12">
            <iframe name="frame1"
                    src="<app:url value="Activity/AutomaticAssign/User/List.do?activityId=${param.activityId}&campaignId=${param.campaignId}"/>"
                    class="embed-responsive-item" scrolling="no" frameborder="0" id="iFrameId">
            </iframe>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <fieldset>
                    <legend class="title">
                        <fmt:message key="Activity.user.assign.options"/>
                    </legend>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="radiocheck">
                                    <div class="checkbox checkbox-default">
                                        <html:checkbox property="dto(homogeneously)"
                                                       styleId="homogeneously_id" styleClass="radio" tabindex="2"/>
                                        <label for="homogeneously_id"><fmt:message
                                                key="Activity.user.assign.homogeneouslyDistributed"/></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="radiocheck">
                                    <div class="checkbox checkbox-default">
                                        <html:checkbox property="dto(customerPriority)" styleId="customerPriority_id"
                                                       styleClass="radio"
                                                       tabindex="3"/>
                                        <label for="customerPriority_id"><fmt:message
                                                key="Activity.user.assign.withPriorityforCustomerResp"/></label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>

    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="ACTIVITYUSER" permission="UPDATE">
            <html:submit property="assignButton" styleClass="button ${app2:getFormButtonClasses()}" tabindex="4">
                <fmt:message key="Campaign.activity.user.assign"/>
            </html:submit>
        </app2:checkAccessRight>
        <html:cancel styleClass="button ${app2:getFormButtonClasses()}" tabindex="5">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>

