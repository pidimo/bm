<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<%--
Page options:
dynamicHiddens      : This variable is used to rewrite dynamic hiddens created when select some
                      contact from search contac pop-up.
useHtmlEditor       : This variable indicate if is enabled htmlEditor.
attachments         : Attachments array list.
mailAccountId       : Mail account identifier selected.
userMailId          : User email Identifier.
enableSaveSentItem  : enable or disable saveSent option, by default is enabled.
enableCreateOutCommunications : enable or disable createOutCommunications option, by default is enabled.
startTabIndex : initial value for tabindex.
composeObjectForm : struts form object for email compose.
--%>
<script language="JavaScript" type="text/javascript">
    <!--
    function onSubmit(obj) {
        document.getElementById("composeEmailId").submit();
    }
    function enableDraftEmail() {
        document.getElementById("isDraftEmailId").value = "true";
    }
    //-->
</script>

<c:if test="${empty composeObjectForm}">
    <c:set var="composeObjectForm" value="${emailForm}" scope="request"/>
</c:if>
<c:if test="${empty enableSaveSentItem}">
    <c:set var="enableSaveSentItem" value="true" scope="request"/>
</c:if>
<c:if test="${empty enableCreateOutCommunications}">
    <c:set var="enableCreateOutCommunications" value="true" scope="request"/>
</c:if>
<c:if test="${empty startTabIndex}">
    <c:set var="startTabIndex" value="1" scope="request"/>
</c:if>

<c:set var="emailPriorities" value="${app2:getEmailPriorities(pageContext.request)}"/>


<%--add this tag to enable auto save email--%>
<tags:autosaveComposeEmail autosaveAction="/webmail/Mail/Ajax/Autosave/ComposeEmail.do" formStyleId="composeEmailId" tempMailIdStyleId="tempMailId_id" isUseHtmlEditor="${useHtmlEditor}"/>


<c:forEach var="hiddenElement" items="${dynamicHiddens}">
    <c:set var="hiddenId" value="${hiddenElement.hiddenId}"/>
    <c:set var="dtoName" value="addressElement_${hiddenElement.dtoName}"/>
    <c:set var="value" value="${hiddenElement.value}"/>
    <html:hidden property="dto(${dtoName})" value="${value}" styleId="${hiddenId}"/>
</c:forEach>

<c:choose>
    <c:when test="${not empty modeLabel}">
        <c:set var="modeLabelCompose" value="${app2:getFormLabelClasses()}" scope="request"/>
        <c:set var="modeContainCompose" value="${app2:getFormContainClasses(null)}" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:set var="modeLabelCompose" value="control-label col-sm-2 label-left" scope="request"/>
        <c:set var="modeContainCompose" value="col-sm-9" scope="request"/>
    </c:otherwise>
</c:choose>

<div id="dynamicHiddensId">
</div>

<html:hidden property="dto(addressList)" styleId="addressListId"/>
<html:hidden property="dto(toHidden)" styleId="toHiddenId"/>

<html:hidden property="dto(userMailId)"/>
<html:hidden property="dto(folderType)"/>
<html:hidden property="dto(bodyType)"/>
<html:hidden property="dto(isDraftEmail)" styleId="isDraftEmailId"/>
<html:hidden property="dto(draftId)" styleId="draftId"/>


<!--is used when some email is reply / replyall, forward or when edit draft email-->
<html:hidden property="dto(mailId)"/>
<html:hidden property="dto(mailState)"/>
<html:hidden property="dto(useHtmlEditor)" value="${useHtmlEditor}"/>
<html:hidden property="dto(replyMode)"/>

<%--temp mail id is used to autosave mail as temporal--%>
<html:hidden property="dto(tempMailId)" styleId="tempMailId_id"/>

<%--old hiddens to remove--%>
<html:hidden property="dto(redirect)" styleId="redirectId"/>
<html:hidden property="dto(redirectMailId)" styleId="redirectMailId"/>
<html:hidden property="dto(editMode)"/>

<%--to validate token fields--%>
<html:hidden property="dto(isTokenFieldRecipients)" value="true"/>

<div class="${app2:getFormGroupClasses()}">
    <label class="${modeLabelCompose}" for="mailAccountId_id">
        <fmt:message key="Mail.from"/>
    </label>

    <div class="${modeContainCompose}">
        <fanta:select property="dto(mailAccountId)"
                      styleId="mailAccountId_id"
                      listName="mailAccountList"
                      labelProperty="email"
                      valueProperty="mailAccountId"
                      firstEmpty="true"
                      onChange="javascript:onSubmit(this);"
                      styleClass="composeSelect ${app2:getFormSelectClasses()}"
                      module="/webmail"
                      tabIndex="${startTabIndex}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
        </fanta:select>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<%--initialize token field plugin--%>
<tags:initTokenfieldTypeahead/>
<c:set var="TO_TOKENFIELD" value="<%=WebMailConstants.TokenFieldType.TO.getConstant()%>"/>
<c:set var="CC_TOKENFIELD" value="<%=WebMailConstants.TokenFieldType.CC.getConstant()%>"/>
<c:set var="BCC_TOKENFIELD" value="<%=WebMailConstants.TokenFieldType.BCC.getConstant()%>"/>

<div class="${app2:getFormGroupClasses()}">
    <label class="${modeLabelCompose}" for="toRecipientId">
        <fmt:message key="Mail.to"/>
    </label>

    <div class="${modeContainCompose}">
        <div class="input-group toTokenFieldCls">

            <c:set var="toTokenList" value="${app2:getTokenFieldFormRecipients(TO_TOKENFIELD, composeObjectForm, pageContext.request)}"/>
            <tags:tokenfieldTypeahead inputTextId="toRecipientId" identifierPrefix="${TO_TOKENFIELD}" rewriteTokenFieldMapList="${toTokenList}"/>

            <app:text property="dto(to)" styleClass="largeText ${app2:getFormInputClasses()}" styleId="toRecipientId"
                      tabindex="${startTabIndex}" value=""/>
           <span class="input-group-btn">
               <tags:bootstrapSelectPopup
                       url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=toRecipientId"
                       name="mailTO"
                       styleId="mailTO_to_id"
                       isLargeModal="true"
                       glyphiconClass="fa fa-user fa-lg"
                       titleKey="Webmail.Contact.searchContactsOrContactPerson"
                       tabindex="${startTabIndex}"/>
               <tags:clearBootstrapSelectPopup keyFieldId="toHiddenId"
                                               nameFieldId="toRecipientId"
                                               titleKey="Common.clear"
                                               submitOnClear="true"
                                               hide="true"/>
               <tags:bootstrapSelectPopup
                       url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=toRecipientId"
                       name="mailTO"
                       styleId="mailTO_togroup_id"
                       isLargeModal="true"
                       glyphiconClass="fa fa-users fa-lg"
                       titleKey="Webmail.group_Contact.search"
                       tabindex="${startTabIndex}"/>
           </span>
        </div>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${modeLabelCompose}" for="ccRecipientId">
        <fmt:message key="Mail.Cc"/>
    </label>

    <div class="${modeContainCompose}">
        <div class="input-group ccTokenFieldCls">

            <c:set var="ccTokenList" value="${app2:getTokenFieldFormRecipients(CC_TOKENFIELD, composeObjectForm, pageContext.request)}"/>
            <tags:tokenfieldTypeahead inputTextId="ccRecipientId" identifierPrefix="${CC_TOKENFIELD}" rewriteTokenFieldMapList="${ccTokenList}"/>

            <app:text property="dto(cc)"
                      styleClass="largeText ${app2:getFormInputClasses()}"
                      styleId="ccRecipientId"
                      tabindex="${startTabIndex}" value=""/>
            <span class="input-group-btn">
                <tags:bootstrapSelectPopup
                        url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=ccRecipientId"
                        name="mailCC"
                        styleId="mailCC_pe_id"
                        isLargeModal="true"
                        glyphiconClass="fa fa-user fa-lg"
                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                        tabindex="${startTabIndex}"/>
                <tags:clearBootstrapSelectPopup keyFieldId="toHiddenId"
                                                nameFieldId="ccRecipientId"
                                                titleKey="Common.clear"
                                                submitOnClear="true"
                                                hide="true"/>
                <tags:bootstrapSelectPopup
                        url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=ccRecipientId"
                        name="mailCC"
                        styleId="mailCC_group_id"
                        isLargeModal="true"
                        glyphiconClass="fa fa-users fa-lg"
                        titleKey="Webmail.group_Contact.search"
                        tabindex="${startTabIndex}"/>
            </span>
        </div>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${modeLabelCompose}" for="bccRecipientId">
        <fmt:message key="Mail.Bcc"/>
    </label>

    <div class="${modeContainCompose}">
        <div class="input-group bccTokenFieldCls">

            <c:set var="bccTokenList" value="${app2:getTokenFieldFormRecipients(BCC_TOKENFIELD, composeObjectForm, pageContext.request)}"/>
            <tags:tokenfieldTypeahead inputTextId="bccRecipientId" identifierPrefix="${BCC_TOKENFIELD}" rewriteTokenFieldMapList="${bccTokenList}"/>

            <app:text property="dto(bcc)"
                      styleClass="largeText ${app2:getFormInputClasses()}"
                      styleId="bccRecipientId"
                      tabindex="${startTabIndex}" value=""/>
            <span class="input-group-btn">
                <tags:bootstrapSelectPopup
                        url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=bccRecipientId"
                        name="mailBCC"
                        styleId="mailBCC_pe_id"
                        isLargeModal="true"
                        glyphiconClass="fa fa-user fa-lg"
                        titleKey="Webmail.Contact.searchContactsOrContactPerson"
                        tabindex="${startTabIndex}"/>
                <tags:clearBootstrapSelectPopup keyFieldId="toHiddenId"
                                                nameFieldId="bccRecipientId"
                                                titleKey="Common.clear"
                                                submitOnClear="true"
                                                hide="true"/>
                <tags:bootstrapSelectPopup
                        url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=bccRecipientId"
                        name="mailBCC"
                        isLargeModal="true"
                        styleId="mailBCC_group_id"
                        glyphiconClass="fa fa-users fa-lg"
                        titleKey="Webmail.group_Contact.search"
                        tabindex="${startTabIndex}"/>
            </span>
        </div>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>
<div class="${app2:getFormGroupClasses()}">
    <label class="${modeLabelCompose}" for="mailSubject_id">
        <fmt:message key="Mail.subject"/>
    </label>

    <div class="${modeContainCompose}">
        <app:text property="dto(mailSubject)"
                  styleId="mailSubject_id"
                  styleClass="largeText ${app2:getFormInputClasses()}"
                  maxlength="245"
                  tabindex="${startTabIndex}"/>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<%--set fragment to import HTML templates--%>
<c:if test="${useHtmlEditor}">
    <c:import url="/WEB-INF/jsp/webmail/ImportHtmlTemplateFragment.jsp"/>
</c:if>


<%--bootstrap file attachment--%>
<c:set var="enableAddAttachLink" value="true" scope="request"/>
<c:if test="${null != attachments}">
    <c:set var="enableCheckBoxes" value="true" scope="request"/>
    <c:set var="attachments" value="${attachments}" scope="request"/>
</c:if>
<c:import url="/WEB-INF/jsp/webmail/FileInputAttachtFragment.jsp"/>


<div class="${app2:getFormGroupClasses()}">
    <div class="col-xs-12">
        <html:textarea property="dto(body)"
                       styleClass="webmailBody ${app2:getFormInputClasses()}"
                       rows="15"
                       style="height: 350px;"
                       styleId="body_field" tabindex="${startTabIndex+1}"/>
        <span class="glyphicon form-control-feedback iconValidation"></span>
    </div>
</div>

<legend class="title">
    <fmt:message key="Mail.mailOptions"/>
</legend>

<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="mailPriority_id">
            <fmt:message key="Mail.priority"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(null)}">
            <html:select property="dto(mailPriority)"
                         styleId="mailPriority_id"
                         styleClass="shortSelect ${app2:getFormSelectClasses()}"
                         tabindex="${startTabIndex}">
                <html:options collection="emailPriorities"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="VIEW">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="signatureId_id">
                <fmt:message key="Webmail.signature.selectSignature"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                <c:if test="${not empty mailAccountId}">
                    <fanta:select property="dto(signatureId)"
                                  styleId="signatureId_id"
                                  styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                  listName="signatureList"
                                  labelProperty="SIGNATURENAME"
                                  valueProperty="SIGNATUREID"
                                  firstEmpty="true"
                                  module="/webmail"
                                  tabIndex="${startTabIndex}">
                        <fanta:parameter field="userMailId"
                                         value="${userMailId}"/>
                        <fanta:parameter field="companyId"
                                         value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="mailAccountId"
                                         value="${mailAccountId}"/>
                    </fanta:select>
                </c:if>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </app2:checkAccessRight>
</div>
<div class="row">
    <c:if test="${true == enableSaveSentItem}">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="control-label col-xs-12 col-sm-9" for="saveSendItem_id">
                <fmt:message key="Webmail.common.saveInSentItems"/>
            </label>

            <div class="col-xs-11 col-sm-2">
                <div class="radiocheck">
                    <div class="checkbox checkbox-default">
                        <html:checkbox property="dto(saveSendItem)"
                                       styleId="saveSendItem_id"
                                       styleClass="radio"
                                       value="true"
                                       tabindex="${startTabIndex}"/>
                        <label for="saveSendItem_id"></label>
                    </div>
                </div>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </c:if>
    <c:if test="${true == enableCreateOutCommunications}">
        <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="control-label col-xs-12 col-sm-9" for="createOutCommunication_id">
                    <fmt:message key="Webmail.common.createOutCommunication"/>
                </label>

                <div class="col-xs-11 col-sm-2">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="dto(createOutCommunication)"
                                           styleId="createOutCommunication_id"
                                           styleClass="radio"
                                           value="true"
                                           tabindex="${startTabIndex}"/>
                            <label for="createOutCommunication_id"></label>
                        </div>
                    </div>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </app2:checkAccessRight>
    </c:if>
</div>
<%--
<c:set var="enableAddAttachLink" value="true" scope="request"/>
<c:if test="${null != attachments}">
    <c:set var="enableCheckBoxes" value="true" scope="request"/>
    <c:set var="attachments" value="${attachments}" scope="request"/>
</c:if>
<c:import url="/WEB-INF/jsp/webmail/AttachmentFragment.jsp"/>
--%>

<c:if test="${'true' eq emailForm.dtoMap['isDraftTemp']}">
    <fmt:message key="Mail.message.isDraftTemporal"/>
</c:if>