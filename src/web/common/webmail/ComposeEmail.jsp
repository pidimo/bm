<%@ include file="/Includes.jsp" %>

<c:import url="/common/webmail/ReturnToMailTrayUrlFragment.jsp"/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/webmail/compose.jsp"/>

<script language="JavaScript" type="text/javascript">
    <!--
    function addAddressField(keyId, keyValue, nameId, nameValue) {
        document.getElementById(keyId).value = keyValue;
        document.getElementById(nameId).value = unescape(nameValue);
    }
    //-->
</script>

<html:form action="/Mail/ComposeEmail.do" enctype="multipart/form-data" styleId="composeEmailId">
    <TABLE width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="2" class="button">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:submit property="save" styleClass="button" tabindex="14">
                        <fmt:message key="Common.send"/>
                    </html:submit>
                    <html:submit property="saveAsDraft" styleClass="button"
                                 onclick="javascript:enableDraftEmail();" tabindex="15">
                        <fmt:message key="Webmail.saveAsDraft"/>
                    </html:submit>
                </app2:checkAccessRight>
                <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" tabindex="16">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
        <tr>
            <TD colspan="2" class="title">
                <fmt:message key="Mail.Title.compose"/>
            </TD>
        </tr>
        <c:set var="dynamicHiddens" value="${emailForm.dtoMap['dynamicHiddens']}" scope="request"/>
        <c:set var="mailAccountId" value="${emailForm.dtoMap['mailAccountId']}" scope="request"/>
        <c:set var="attachments" value="${emailForm.dtoMap['attachments']}" scope="request"/>
        <c:set var="userMailId" value="${emailForm.dtoMap['userMailId']}" scope="request"/>

        <c:import url="/common/webmail/ComposeFragment.jsp"/>

        <tr>
            <td colspan="2" class="button">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <html:submit property="save" styleClass="button" tabindex="10">
                        <fmt:message key="Common.send"/>
                    </html:submit>
                    <html:submit property="saveAsDraft" styleClass="button"
                                 onclick="javascript:enableDraftEmail();" tabindex="11">
                        <fmt:message key="Webmail.saveAsDraft"/>
                    </html:submit>
                </app2:checkAccessRight>
                <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" tabindex="12">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
    </TABLE>
</html:form>