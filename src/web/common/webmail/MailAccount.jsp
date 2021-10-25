<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="pop" value="<%=WebMailConstants.MailAccountType.POP.getType()%>"/>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function() {
        var smtpAuthenticationOption = '${mailAccountForm.dtoMap['smtpAuthentication']}';
        if ('true' == smtpAuthenticationOption) {
            $("#aditionalSMTPOptionsId").css("display", "table-row");
        }

        var usePOPConfigurationOption = '${mailAccountForm.dtoMap['usePOPConfiguration']}';
        if ('true' == usePOPConfigurationOption) {
            $("#loginSMTPOptionsId").css("display", "none");
        } else {
            $("#loginSMTPOptionsId").css("display", "table-row");
        }

    <c:if test="${'update' ==  op}">
        var popOptions = $("input[id='popOptionId'], select[id='popOptionId']");
        for (var i = 0; i < popOptions.length; i++) {
            $(popOptions[i]).change(function() {
                $("#popOptionChangedId").attr("value", "true");
            });
        }

        var smtpOptions = $("input[id='smtpOptionId'], select[id='smtpOptionId']");
        for (var j = 0; j < smtpOptions.length; j++) {
            $(smtpOptions[j]).change(function() {
                $("#smtpOptionChangedId").attr("value", "true");
            });
        }
    </c:if>

    });

    function enableAditionalOptionsForSMTP(checkbox) {
        if ($(checkbox).attr("checked")) {
            $("#aditionalSMTPOptionsId").css("display", "table-row");
        } else {
            $("#aditionalSMTPOptionsId").css("display", "none");
        }
    }

    function enableLoginOptionsForSMTP(checkbox) {
        if (!$(checkbox).attr("checked")) {
            $("#loginSMTPOptionsId").css("display", "table-row");
        } else {
            $("#loginSMTPOptionsId").css("display", "none");
        }
    }

    function enableRemoveAfterDays(checkbox) {
        if ($(checkbox).attr("checked")) {
            $("#trRemoveAfter").css("display", "");
        } else {
            $("#trRemoveAfter").css("display", "none");
            $("#removeAfterDaysId").val("");
        }
    }
</script>


<c:set var="secureConnectionTypes" value="${app2:getMailAccountSecureConnectionTypes()}"/>

<html:form action="${action}" focus="dto(prefix)">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(accountType)" value="${pop}"/>
<html:hidden property="dto(userMailId)" value="${sessionScope.user.valueMap['userId']}"/>
<!--
Used when Creates new mail accountt
-->
<html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(mailAccountId)"/>
    <html:hidden property="dto(popOptionChanged)" styleId="popOptionChangedId"/>
    <html:hidden property="dto(smtpOptionChanged)" styleId="smtpOptionChangedId"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>


<table cellpadding="0" cellspacing="0" border="0" align="center" width="60%" class="container">
<tr>
    <td class="button" colspan="2">
        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
            <html:submit styleClass="button">${button}</html:submit>
        </app2:checkAccessRight>
        <c:if test="${null == showCancel}">
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </c:if>
    </td>
</tr>
<tr>
    <td colspan="2" class="title">${title}</td>
</tr>
<tr>
    <td class="label" width="45%">
        <fmt:message key="MailAccount.prefix"/>
    </td>
    <td class="contain" width="55%">
        <app:text property="dto(prefix)"
                  styleClass="mediumText"
                  maxlength="240"
                  view="${'delete' == op}"
                  tabindex="1"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.email"/>
    </td>
    <td class="contain">
        <app:text property="dto(email)"
                  styleClass="mediumText"
                  maxlength="240"
                  view="${'delete' == op}"
                  tabindex="2"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.default"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(defaultAccount)"
                       styleClass="adminCheckBox"
                       value="true"
                       disabled="${'delete' == op}"
                       tabindex="3"/>
    </td>
</tr>
<tr>
    <td class="title" colspan="2">
        <fmt:message key="MailAccount.popConfiguration"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.serverName"/>
    </td>
    <td class="contain">
        <app:text property="dto(serverName)"
                  styleClass="mediumText"
                  maxlength="50"
                  view="${'delete' == op}"
                  styleId="popOptionId"
                  tabindex="4"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.serverPort"/>
    </td>
    <td class="contain">
        <app:text property="dto(serverPort)"
                  styleClass="numberText"
                  maxlength="50"
                  view="${'delete' == op}"
                  styleId="popOptionId"
                  tabindex="5"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.login"/>
    </td>
    <td class="contain">
        <app:text property="dto(login)"
                  styleClass="mediumText"
                  maxlength="50"
                  view="${'delete' == op}"
                  styleId="popOptionId"
                  tabindex="6"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.password"/>
    </td>
    <td class="contain">
        <html:password property="dto(password)"
                       styleClass="mediumText"
                       maxlength="50"
                       readonly="${'delete' == op}"
                       styleId="popOptionId"
                       tabindex="7"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.useSSLConnection"/>
    </td>
    <td class="contain">
        <html:select property="dto(useSSLConnection)" styleClass="shortSelect"
                     readonly="${op == 'delete'}"
                     tabindex="8" styleId="popOptionId">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="secureConnectionTypes" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="title" colspan="2">
        <fmt:message key="MailAccount.smtpConfiguration"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.smtpServer"/>
    </td>
    <td class="contain">
        <app:text property="dto(smtpServer)"
                  styleClass="mediumText"
                  maxlength="50"
                  view="${'delete' == op}"
                  styleId="smtpOptionId"
                  tabindex="9"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.smtpPort"/>
    </td>
    <td class="contain">
        <app:text property="dto(smtpPort)"
                  styleClass="numberText"
                  maxlength="50"
                  view="${'delete' == op}"
                  styleId="smtpOptionId"
                  tabindex="10"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="MailAccount.smtpAuthentication"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(smtpAuthentication)"
                       styleClass="adminCheckBox"
                       value="true"
                       disabled="${'delete' == op}"
                       tabindex="11"
                       styleId="smtpOptionId"
                       onclick="javascript:enableAditionalOptionsForSMTP(this);"/>
    </td>
</tr>
<tr id="aditionalSMTPOptionsId" style="display:none;">
    <td colspan="2">
        <table border="0" cellpadding="0" cellspacing="0" width="100%">
            <tr>
                <td class="label" width="45%">
                    <fmt:message key="MailAccount.usePOPConfiguration"/>
                </td>
                <td class="contain" width="55%">
                    <html:checkbox property="dto(usePOPConfiguration)"
                                   styleClass="adminCheckBox"
                                   value="true"
                                   disabled="${'delete' == op}"
                                   tabindex="12"
                                   styleId="smtpOptionId"
                                   onclick="javascript:enableLoginOptionsForSMTP(this);"/>
                </td>
            </tr>
            <tr id="loginSMTPOptionsId" style="display:none;">
                <td colspan="2">
                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                        <tr>
                            <td class="label" width="45%">
                                <fmt:message key="MailAccount.smtpLogin"/>
                            </td>
                            <td class="contain" width="55%">
                                <app:text property="dto(smtpLogin)"
                                          styleClass="mediumText"
                                          maxlength="50"
                                          view="${'delete' == op}"
                                          styleId="smtpOptionId"
                                          tabindex="13"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="label" width="45%">
                                <fmt:message key="MailAccount.smtpPassword"/>
                            </td>
                            <td class="contain" width="55%">
                                <html:password property="dto(smtpPassword)"
                                               styleClass="mediumText"
                                               maxlength="50"
                                               readonly="${'delete' == op}"
                                               styleId="smtpOptionId"
                                               tabindex="14"/>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="MailAccount.smtpSSL"/>
                </td>
                <td class="contain">
                    <html:select property="dto(smtpSSL)" styleClass="shortSelect"
                                 readonly="${op == 'delete'}"
                                 tabindex="15" styleId="smtpOptionId">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="secureConnectionTypes" property="value" labelProperty="label"/>
                    </html:select>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td class="title" colspan="2">
        <fmt:message key="MailAccount.preferences"/>
    </td>
</tr>

<app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
    <tr>
        <td colspan="2">
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td class="label" width="45%">
                        <fmt:message key="Webmail.userMail.createInCommunication"/>
                    </td>
                    <td class="contain" width="55%">
                        <html:checkbox property="dto(createInCommunication)" value="true" styleClass="radio" disabled="${'delete' == op}" tabindex="16"/>
                    </td>
                </tr>


                <tr>
                    <td class="label">
                        <fmt:message key="Webmail.userMail.createOutCommunication"/>
                    </td>
                    <td class="contain">
                        <html:checkbox property="dto(createOutCommunication)" value="true" styleClass="radio" disabled="${'delete' == op}" tabindex="17"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</app2:checkAccessRight>

<tr>
    <td class="label">
        <fmt:message key="Webmail.userMail.keepEmailOnServer"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(keepEmailOnServer)" value="true" styleClass="radio" disabled="${'delete' == op}" tabindex="18"
                       onclick="javascript:enableRemoveAfterDays(this);"/>
    </td>
</tr>

<c:if test="${!('true' eq mailAccountForm.dtoMap['keepEmailOnServer'])}">
    <c:set var="trAfterDisplay" value="display:none"/>
</c:if>

<tr id="trRemoveAfter" style="${trAfterDisplay}">
    <td class="label">
        <fmt:message key="MailAccount.removeMailAfterOf"/>
    </td>
    <td class="contain">
        <app:numberText property="dto(removeEmailOnServerAfterOf)" styleId="removeAfterDaysId" styleClass="numberText" maxlength="3"
                        numberType="integer" view="${'delete' == op}" tabindex="19"/>

        <fmt:message key="MailAccount.removeMailAfterOf.days"/>
    </td>
</tr>

<tr>
    <td colspan="2" class="title">
        <fmt:message key="Webmail.preferences.automaticForwardConfigurationTitle"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Webmail.userMail.automaticForward"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(automaticForward)" value="true" styleClass="radio" disabled="${'delete' == op}" tabindex="21"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Webmail.userMail.forwardEmail"/>
    </td>
    <td class="contain">
        <app:text property="dto(forwardEmail)" styleClass="mediumText" view="${'delete' == op}" tabindex="22"/>
    </td>
</tr>

<tr>
    <td colspan="2" class="title">
        <fmt:message key="Webmail.preferences.automaticReplyConfigurationTitle"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Webmail.userMail.automaticReply"/>
    </td>
    <td class="contain">
        <html:checkbox property="dto(automaticReply)" value="true" styleClass="radio" disabled="${'delete' == op}" tabindex="23"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Webmail.userMail.replySubject"/>
    </td>
    <td class="contain">
        <app:text property="dto(automaticReplyMessageSubject)" maxlength="249" styleClass="mediumText" view="${'delete' == op}" tabindex="24"/>
    </td>
</tr>
<tr>
    <td class="topLabel" colspan="2">
        <fmt:message key="Webmail.userMail.replyMessage"/>
        <html:textarea property="dto(automaticReplyMessage)"
                       styleClass="tinyDetail"
                       style="height:100px;width:99%;" readonly="${'delete' == op}" tabindex="25"/>
    </td>
</tr>

<tr>
    <td class="button" colspan="2">
        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
            <html:submit styleClass="button" tabindex="27">${button}</html:submit>
        </app2:checkAccessRight>
        <c:if test="${null == showCancel}">
            <html:cancel styleClass="button" tabindex="28"><fmt:message key="Common.cancel"/></html:cancel>
        </c:if>
    </td>
</tr>
</table>

</html:form>