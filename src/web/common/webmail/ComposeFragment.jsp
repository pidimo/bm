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
--%>

<style>
    SELECT.composeSelect {
        width: 85%;
    }
</style>

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

<c:forEach var="hiddenElement" items="${dynamicHiddens}">
    <c:set var="hiddenId" value="${hiddenElement.hiddenId}"/>
    <c:set var="dtoName" value="addressElement_${hiddenElement.dtoName}"/>
    <c:set var="value" value="${hiddenElement.value}"/>
    <html:hidden property="dto(${dtoName})" value="${value}" styleId="${hiddenId}"/>
</c:forEach>

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

<%--old hiddens to remove--%>
<html:hidden property="dto(redirect)" styleId="redirectId"/>
<html:hidden property="dto(redirectMailId)" styleId="redirectMailId"/>
<html:hidden property="dto(editMode)"/>

<tr>
    <td class="label" width="15%">
        <fmt:message key="Mail.from"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(mailAccountId)"
                      listName="mailAccountList"
                      labelProperty="email"
                      valueProperty="mailAccountId"
                      firstEmpty="true"
                      onChange="javascript:onSubmit(this);"
                      styleClass="composeSelect"
                      module="/webmail"
                      tabIndex="${startTabIndex}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <TD class="label" width="15%">
        <fmt:message key="Mail.to"/>
    </TD>
    <TD class="contain" width="85%" nowrap>
        <app:text property="dto(to)" styleClass="largeText" styleId="toRecipientId"
                  style="width:85%;" tabindex="${startTabIndex}"/>
        &nbsp;
        <tags:selectPopup url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=toRecipientId"
                          name="mailTO"
                          titleKey="Webmail.Contact.searchContactsOrContactPerson"
                          width="755"
                          heigth="480"
                          imgPath="/img/webmail/contact_edit.gif"
                          imgWidth="17"
                          imgHeight="19"
                          tabindex="${startTabIndex}"/>
        <tags:clearSelectPopup keyFieldId="toHiddenId"
                               nameFieldId="toRecipientId"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="true"/>
        &nbsp;
        <tags:selectPopup
                url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=toRecipientId"
                name="mailTO"
                titleKey="Webmail.group_Contact.search"
                imgPath="/img/webmail/contactgroup_edit.gif"
                imgWidth="25"
                imgHeight="19"
                tabindex="${startTabIndex}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Mail.Cc"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(cc)"
                  styleClass="largeText"
                  styleId="ccRecipientId"
                  style="width:85%;" tabindex="${startTabIndex}"/>
        &nbsp;
        <tags:selectPopup url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=ccRecipientId"
                          name="mailCC"
                          titleKey="Webmail.Contact.searchContactsOrContactPerson"
                          width="755"
                          heigth="480"
                          imgPath="/img/webmail/contact_edit.gif"
                          imgWidth="17"
                          imgHeight="19"
                          tabindex="${startTabIndex}"/>
        <tags:clearSelectPopup keyFieldId="toHiddenId"
                               nameFieldId="ccRecipientId"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="true"/>
        &nbsp;
        <tags:selectPopup
                url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=ccRecipientId"
                name="mailCC"
                titleKey="Webmail.group_Contact.search"
                imgPath="/img/webmail/contactgroup_edit.gif"
                imgWidth="25"
                imgHeight="19"
                tabindex="${startTabIndex}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Mail.Bcc"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(bcc)"
                  styleClass="largeText"
                  styleId="bccRecipientId"
                  style="width:85%;"
                  tabindex="${startTabIndex}"/>
        &nbsp;
        <tags:selectPopup url="/webmail/Mail/ImportAddress.do?idStyle=toHiddenId&nameStyle=bccRecipientId"
                          name="mailBCC"
                          titleKey="Webmail.Contact.searchContactsOrContactPerson"
                          width="755"
                          heigth="480"
                          imgPath="/img/webmail/contact_edit.gif"
                          imgWidth="17"
                          imgHeight="19"
                          tabindex="${startTabIndex}"/>
        <tags:clearSelectPopup keyFieldId="toHiddenId"
                               nameFieldId="bccRecipientId"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="true"/>
        &nbsp;
        <tags:selectPopup
                url="/webmail/Mail/Forward/AddressGroupImportList.do?dto(op)=getListGroups&idStyle=toHiddenId&nameStyle=bccRecipientId"
                name="mailBCC"
                titleKey="Webmail.group_Contact.search"
                imgPath="/img/webmail/contactgroup_edit.gif"
                imgWidth="25"
                imgHeight="19"
                tabindex="${startTabIndex}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Mail.subject"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(mailSubject)"
                  styleClass="largeText"
                  maxlength="245"
                  style="width:85%;"
                  tabindex="${startTabIndex}"/>
    </TD>
</tr>

<%--set fragment to import HTML templates--%>
<c:if test="${useHtmlEditor}">
    <c:import url="/common/webmail/ImportHtmlTemplateFragment.jsp"/>
</c:if>

<tr>
    <TD colspan="2">
        <html:textarea property="dto(body)"
                       styleClass="webmailBody"
                       styleId="body_field" tabindex="${startTabIndex+1}"/>
    </TD>
</tr>

<tr>
    <TD class="title" colspan="2">
        <fmt:message key="Mail.mailOptions"/>
    </TD>
</tr>
<tr>
    <td colspan="2">
        <table cellspacing="0" cellpadding="0" border="0" width="100%">
            <tr>
                <TD class="label">
                    <fmt:message key="Mail.priority"/>
                </TD>
                <TD nowrap class="contain">
                    <html:select property="dto(mailPriority)"
                                 styleClass="shortSelect"
                                 tabindex="${startTabIndex}">
                        <html:options collection="emailPriorities"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </TD>
                <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="VIEW">
                    <TD class="label">
                        <fmt:message key="Webmail.signature.selectSignature"/>
                    </TD>
                    <TD class="contain">
                        <c:if test="${not empty mailAccountId}">
                            <fanta:select property="dto(signatureId)"
                                          styleClass="shortSelect"
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
                    </TD>
                </app2:checkAccessRight>
                <c:if test="${true == enableSaveSentItem}">
                    <TD class="label" nowrap>
                        <fmt:message key="Webmail.common.saveInSentItems"/>
                    </TD>
                    <td class="contain">
                        <html:checkbox property="dto(saveSendItem)"
                                       styleClass="radio"
                                       value="true"
                                       tabindex="${startTabIndex}"/>
                    </td>
                </c:if>
                <c:if test="${true == enableCreateOutCommunications}">
                    <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                        <TD class="label">
                            <fmt:message key="Webmail.common.createOutCommunication"/>
                        </TD>
                        <td class="contain" nowrap>
                            <html:checkbox property="dto(createOutCommunication)"
                                           styleClass="radio"
                                           value="true"
                                           tabindex="${startTabIndex}"/>
                        </td>
                    </app2:checkAccessRight>
                </c:if>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td colspan="2">
        <c:set var="enableAddAttachLink" value="true" scope="request"/>
        <c:if test="${null != attachments}">
            <c:set var="enableCheckBoxes" value="true" scope="request"/>
            <c:set var="attachments" value="${attachments}" scope="request"/>
        </c:if>
        <c:import url="/common/webmail/AttachmentFragment.jsp"/>
    </td>
</tr>
<c:if test="${'true' eq emailForm.dtoMap['isDraftTemp']}">
    <tr>
        <td colspan="2">
            </br>
            <fmt:message key="Mail.message.isDraftTemporal"/>
        </td>
    </tr>
</c:if>
