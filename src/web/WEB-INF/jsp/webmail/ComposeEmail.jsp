<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/webmail/ReturnToMailTrayUrlFragment.jsp"/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function addAddressField(keyId, keyValue, nameId, nameValue) {
        document.getElementById(keyId).value = keyValue;
        document.getElementById(nameId).value = unescape(nameValue);
    }
    //-->
</script>


<html:form action="/Mail/ComposeEmail.do" enctype="multipart/form-data" styleId="composeEmailId"
           styleClass="form-horizontal">
    <div class="row">
        <div class="col-xs-12">
            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                <html:submit property="save" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             tabindex="14">
                    <fmt:message key="Common.send"/>
                </html:submit>
                <html:submit property="saveAsDraft" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             onclick="javascript:enableDraftEmail();" tabindex="15">
                    <fmt:message key="Webmail.saveAsDraft"/>
                </html:submit>
            </app2:checkAccessRight>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"
                         onclick="location.href='${urlCancel}'" tabindex="16">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="Mail.Title.compose"/>
            </legend>
            <c:set var="dynamicHiddens" value="${emailForm.dtoMap['dynamicHiddens']}" scope="request"/>
            <c:set var="mailAccountId" value="${emailForm.dtoMap['mailAccountId']}" scope="request"/>
            <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
            <c:set var="userMailId" value="${emailForm.dtoMap['userMailId']}" scope="request"/>

            <c:import url="/WEB-INF/jsp/webmail/ComposeFragment.jsp"/>
        </fieldset>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                <html:submit property="save" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             tabindex="10">
                    <fmt:message key="Common.send"/>
                </html:submit>
                <html:submit property="saveAsDraft" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             onclick="javascript:enableDraftEmail();" tabindex="11">
                    <fmt:message key="Webmail.saveAsDraft"/>
                </html:submit>
            </app2:checkAccessRight>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"
                         onclick="location.href='${urlCancel}'" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>