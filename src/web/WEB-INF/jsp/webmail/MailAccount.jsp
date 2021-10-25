<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="pop" value="<%=WebMailConstants.MailAccountType.POP.getType()%>"/>

<script type="text/javascript" language="JavaScript">
    $(document).ready(function () {
        var smtpAuthenticationOption = '${mailAccountForm.dtoMap['smtpAuthentication']}';
        if ('true' == smtpAuthenticationOption) {
            $("#aditionalSMTPOptionsId").css("display", "block");
        }

        var usePOPConfigurationOption = '${mailAccountForm.dtoMap['usePOPConfiguration']}';
        if ('true' == usePOPConfigurationOption) {
            $("#loginSMTPOptionsId").css("display", "none");
        } else {
            $("#loginSMTPOptionsId").css("display", "block");
        }

        <c:if test="${'update' ==  op}">
        var popOptions = $("input[id='popOptionId'], select[id='popOptionId']");
        for (var i = 0; i < popOptions.length; i++) {
            $(popOptions[i]).change(function () {
                $("#popOptionChangedId").attr("value", "true");
            });
        }

        var smtpOptions = $("input[id='smtpOptionId'], select[id='smtpOptionId']");
        for (var j = 0; j < smtpOptions.length; j++) {
            $(smtpOptions[j]).change(function () {
                $("#smtpOptionChangedId").attr("value", "true");
            });
        }
        </c:if>

    });

    function enableAditionalOptionsForSMTP(checkbox) {
        if ($(checkbox).is(":checked")) {
            $("#aditionalSMTPOptionsId").css("display", "block");
        } else {
            $("#aditionalSMTPOptionsId").css("display", "none");
        }
    }

    function enableLoginOptionsForSMTP(checkbox) {
        if (!$(checkbox).is(":checked")) {
            $("#loginSMTPOptionsId").css("display", "block");
        } else {
            $("#loginSMTPOptionsId").css("display", "none");
        }
    }

    function enableRemoveAfterDays(checkbox) {
        if ($(checkbox).is(":checked")) {
            $("#trRemoveAfter").css("display", "");
        } else {
            $("#trRemoveAfter").css("display", "none");
            $("#removeAfterDaysId").val("");
        }
    }
</script>


<c:set var="secureConnectionTypes" value="${app2:getMailAccountSecureConnectionTypes()}"/>

<html:form action="${action}" focus="dto(prefix)" styleClass="form-horizontal">
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

    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </html:submit>
            </app2:checkAccessRight>
            <c:if test="${null == showCancel}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="prefix_id">
                        <fmt:message key="MailAccount.prefix"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(prefix)"
                                  styleId="prefix_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="240"
                                  view="${'delete' == op}"
                                  tabindex="1"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="email_id">
                        <fmt:message key="MailAccount.email"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(email)"
                                  styleId="email_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="240"
                                  view="${'delete' == op}"
                                  tabindex="2"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="MailAccount.default"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(defaultAccount)"
                                           styleId="defaultAccount_id"
                                           styleClass="adminCheckBox"
                                           value="true"
                                           disabled="${'delete' == op}"
                                           tabindex="3"/>
                            <label for="defaultAccount_id"></label>
                        </div>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <legend class="title">
                    <fmt:message key="MailAccount.popConfiguration"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="popOptionId">
                        <fmt:message key="MailAccount.serverName"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(serverName)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="50"
                                  view="${'delete' == op}"
                                  styleId="popOptionId"
                                  tabindex="4"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="popOptionId">
                        <fmt:message key="MailAccount.serverPort"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(serverPort)"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="50"
                                  view="${'delete' == op}"
                                  styleId="popOptionId"
                                  tabindex="5"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="popOptionId">
                        <fmt:message key="MailAccount.login"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(login)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="50"
                                  view="${'delete' == op}"
                                  styleId="popOptionId"
                                  tabindex="6"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="popOptionId">
                        <fmt:message key="MailAccount.password"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <html:password property="dto(password)"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       maxlength="50"
                                       readonly="${'delete' == op}"
                                       styleId="popOptionId"
                                       tabindex="7"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="popOptionId">
                        <fmt:message key="MailAccount.useSSLConnection"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <html:select property="dto(useSSLConnection)"
                                     styleClass="${app2:getFormSelectClasses()} shortSelect"
                                     readonly="${op == 'delete'}"
                                     tabindex="8" styleId="popOptionId">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="secureConnectionTypes" property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <legend class="title">
                    <fmt:message key="MailAccount.smtpConfiguration"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="smtpOptionId">
                        <fmt:message key="MailAccount.smtpServer"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(smtpServer)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="50"
                                  view="${'delete' == op}"
                                  styleId="smtpOptionId"
                                  tabindex="9"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="smtpOptionId">
                        <fmt:message key="MailAccount.smtpPort"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(smtpPort)"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="50"
                                  view="${'delete' == op}"
                                  styleId="smtpOptionId"
                                  tabindex="10"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="MailAccount.smtpAuthentication"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(smtpAuthentication)"
                                           styleClass="adminCheckBox"
                                           value="true"
                                           disabled="${'delete' == op}"
                                           tabindex="11"
                                           styleId="smtpOptionId"
                                           onclick="javascript:enableAditionalOptionsForSMTP(this);"/>
                            <label for="smtpOptionId"></label>
                        </div>
                    </div>
                </div>
                <div id="aditionalSMTPOptionsId" style="display:none;">
                    <div>

                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}">
                                    <fmt:message key="MailAccount.usePOPConfiguration"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                                    <div class="checkbox checkbox-default">
                                        <html:checkbox property="dto(usePOPConfiguration)"
                                                       styleClass="adminCheckBox"
                                                       value="true"
                                                       disabled="${'delete' == op}"
                                                       tabindex="12"
                                                       styleId="smtpOptionId"
                                                       onclick="javascript:enableLoginOptionsForSMTP(this);"/>
                                        <label for="smtpOptionId"></label>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div id="loginSMTPOptionsId" style="display:none;">
                                        <div class="${app2:getFormGroupClasses()}">
                                            <label class="${app2:getFormLabelRenderCategory()}" for="smtpOptionId">
                                                <fmt:message key="MailAccount.smtpLogin"/>
                                            </label>

                                            <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                                                <app:text property="dto(smtpLogin)"
                                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                                          maxlength="50"
                                                          view="${'delete' == op}"
                                                          styleId="smtpOptionId"
                                                          tabindex="13"/>
                                                <span class="glyphicon form-control-feedback iconValidation"></span>
                                            </div>
                                        </div>
                                        <div class="${app2:getFormGroupClasses()}">
                                            <label class="${app2:getFormLabelRenderCategory()}" for="smtpOptionId">
                                                <fmt:message key="MailAccount.smtpPassword"/>
                                            </label>

                                            <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                                                <html:password property="dto(smtpPassword)"
                                                               styleClass="${app2:getFormInputClasses()} mediumText"
                                                               maxlength="50"
                                                               readonly="${'delete' == op}"
                                                               styleId="smtpOptionId"
                                                               tabindex="14"/>
                                                <span class="glyphicon form-control-feedback iconValidation"></span>
                                            </div>
                                        </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="smtpOptionId">
                                    <fmt:message key="MailAccount.smtpSSL"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                                    <html:select property="dto(smtpSSL)"
                                                 styleClass="${app2:getFormSelectClasses()} shortSelect"
                                                 readonly="${op == 'delete'}"
                                                 tabindex="15" styleId="smtpOptionId">
                                        <html:option value="">&nbsp;</html:option>
                                        <html:options collection="secureConnectionTypes" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                    </div>
                </div>

                <legend class="title">
                    <fmt:message key="MailAccount.preferences"/>
                </legend>

                <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="Webmail.userMail.createInCommunication"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(createInCommunication)"
                                               styleId="createInCommunication_id" value="true"
                                               disabled="${'delete' == op}" tabindex="16"/>
                                <label for="createInCommunication_id"></label>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="Webmail.userMail.createOutCommunication"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(createOutCommunication)"
                                               styleId="createOutCommunication_id" value="true"
                                               styleClass="radio"
                                               disabled="${'delete' == op}" tabindex="17"/>
                                <label for="createOutCommunication_id"></label>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </app2:checkAccessRight>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Webmail.userMail.keepEmailOnServer"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(keepEmailOnServer)" styleId="keepEmailOnServer_id"
                                           value="true"
                                           disabled="${'delete' == op}" tabindex="18"
                                           onclick="javascript:enableRemoveAfterDays(this);"/>
                            <label for="keepEmailOnServer_id"></label>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${!('true' eq mailAccountForm.dtoMap['keepEmailOnServer'])}">
                    <c:set var="trAfterDisplay" value="display:none"/>
                </c:if>

                <div id="trRemoveAfter" style="${trAfterDisplay}" class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="removeAfterDaysId">
                        <fmt:message key="MailAccount.removeMailAfterOf"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:numberText property="dto(removeEmailOnServerAfterOf)" styleId="removeAfterDaysId"
                                        styleClass="${app2:getFormInputClasses()} numberText" maxlength="3"
                                        numberType="integer" view="${'delete' == op}" tabindex="19"/>
                        <fmt:message key="MailAccount.removeMailAfterOf.days"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <legend class="title">
                    <fmt:message key="Webmail.preferences.automaticForwardConfigurationTitle"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Webmail.userMail.automaticForward"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(automaticForward)" styleId="automaticForward_id"
                                           value="true"
                                           disabled="${'delete' == op}" tabindex="21"/>
                            <label for="automaticForward_id"></label>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="forwardEmail_id">
                        <fmt:message key="Webmail.userMail.forwardEmail"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(forwardEmail)" styleId="forwardEmail_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  view="${'delete' == op}" tabindex="22"/>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>

                <legend class="title">
                    <fmt:message key="Webmail.preferences.automaticReplyConfigurationTitle"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Webmail.userMail.automaticReply"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(automaticReply)" styleId="automaticReply_id" value="true"
                                           disabled="${'delete' == op}" tabindex="23"/>
                            <label for="automaticReply_id"></label>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="automaticReplyMessageSubject_id">
                        <fmt:message key="Webmail.userMail.replySubject"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(automaticReplyMessageSubject)"
                                  styleId="automaticReplyMessageSubject_id"
                                  maxlength="249"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  view="${'delete' == op}" tabindex="24"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-12">
                    <div class="form-group">
                        <label class="control-label col-xs-12 col-sm-12 label-left row" for="automaticReplyMessage_id">
                            <fmt:message key="MailAccount.textReplyMessage"/>
                        </label>

                        <div class="col-xs-12 col-sm-12 row">
                            <%--this field can be removed when bm v6.5 is released--%>
                            <html:hidden property="dto(automaticReplyMessage)"/>

                            <html:textarea property="dto(textReplyMessage)"
                                           styleId="automaticReplyMessage_id"
                                           styleClass="${app2:getFormInputClasses()} tinyDetail"
                                           style="height:200px; width:100%;" readonly="${'delete' == op}" tabindex="25"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-12">
                    <div class="form-group">
                        <label class="control-label col-xs-12 col-sm-12 label-left row" for="body_field">
                            <fmt:message key="MailAccount.htmlReplyMessage"/>
                        </label>

                        <div class="col-xs-12 col-sm-12 row">
                            <c:choose>
                                <c:when test="${'delete' != op}">
                                    <tags:initTinyMCE4 textAreaId="body_field"/>

                                    <html:textarea property="dto(htmlReplyMessage)"
                                                   tabindex="26"
                                                   styleClass="webmailBody ${app2:getFormInputClasses()}"
                                                   styleId="body_field"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="ad" value="${mailAccountForm.dtoMap['htmlReplyMessage']}"
                                           scope="session"/>
                                    <iframe name="frame2"
                                            src="<c:url value="/WEB-INF/jsp/support/PreviusDetail.jsp?var=ad" />"
                                            style="width : 100%;height: 240px;background-color:#ffffff"
                                            scrolling="yes"
                                            class="form-control"
                                            frameborder="1">
                                    </iframe>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="27">
                    ${button}
                </html:submit>
            </app2:checkAccessRight>
            <c:if test="${null == showCancel}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="28">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
    </div>


</html:form>
<tags:jQueryValidation formName="mailAccountForm"/>