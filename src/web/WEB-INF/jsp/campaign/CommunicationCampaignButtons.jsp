<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil" %>
<%@ include file="/Includes.jsp" %>

<c:if test="${empty communicationButtonsTabIndex}">
    <c:set var="communicationButtonsTabIndex" value="40" scope="request"/>
</c:if>

<c:set var="saveButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey()%>" scope="page"/>
<c:set var="generateButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey()%>"
       scope="page"/>
<c:set var="sendButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey()%>" scope="page"/>

<c:set var="phone" value="<%= com.piramide.elwis.utils.CommunicationTypes.PHONE %>" scope="page"/>
<c:set var="meeting" value="<%= com.piramide.elwis.utils.CommunicationTypes.MEETING %>" scope="page"/>
<c:set var="fax" value="<%= com.piramide.elwis.utils.CommunicationTypes.FAX %>" scope="page"/>
<c:set var="letter" value="<%= com.piramide.elwis.utils.CommunicationTypes.LETTER %>" scope="page"/>
<c:set var="other" value="<%= com.piramide.elwis.utils.CommunicationTypes.OTHER %>" scope="page"/>
<c:set var="email" value="<%= com.piramide.elwis.utils.CommunicationTypes.EMAIL%>" scope="page"/>
<c:set var="web_document" value="<%= com.piramide.elwis.utils.CommunicationTypes.WEB_DOCUMENT%>" scope="page"/>

<c:choose>
    <c:when test="${letter == mainCommunicationForm.dtoMap['type'] || fax == mainCommunicationForm.dtoMap['type']}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${!isCampGeneration || op == 'delete'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="COMMUNICATION"
                                     property="${saveButton}"
                                     styleId="specialSubmit"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     tabindex="${communicationButtonsTabIndex}">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:if>

            <c:if test="${op != 'delete'}">
                <c:if test="${!isCampGeneration}">
                    <app2:checkAccessRight functionality="COMMUNICATION" permission="EXECUTE">
                        <html:submit styleClass="${app2:getFormButtonClasses()} marginButton"
                                     property="${generateButton}"
                                     tabindex="${communicationButtonsTabIndex}">
                            <fmt:message key="Document.generate"/>
                        </html:submit>
                    </app2:checkAccessRight>
                </c:if>

                <app2:checkAccessRight functionality="COMMUNICATION" permission="UPDATE">
                    <c:set var="docInfo"
                           value="${app2:getCommunicationDocumentInfo(mainCommunicationForm.dtoMap['contactId'])}"/>
                    <c:if test="${not empty docInfo.freeTextId}">
                        <app:url var="openDocUrl"
                                 value="contacts/Download.do?dto(type)=comm&dto(fid)=${docInfo.freeTextId}"
                                 contextRelative="true"/>
                        <c:if test="${docInfo.isfromCampaigGeneration}">
                            <app:url var="openDocUrl"
                                     value="campaign/Download/GenerationCommunication/Document.do?dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}"
                                     contextRelative="true"/>
                        </c:if>
                        <html:button property=""
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="location.href='${openDocUrl}'"
                                     tabindex="${communicationButtonsTabIndex}">
                            <fmt:message key="Document.open"/>
                        </html:button>
                    </c:if>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel
                    styleClass="${app2:getFormButtonCancelClasses()} marginButton"
                    tabindex="${communicationButtonsTabIndex}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </c:when>

    <c:when test="${web_document == mainCommunicationForm.dtoMap['type']}">
        <div class="row alignButtons">
            <div class="col-sx-12">

                <app2:securitySubmit operation="${op}"
                                     functionality="COMMUNICATION"
                                     property="${saveButton}"
                                     styleId="specialSubmit"
                                     styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                     tabindex="${communicationButtonsTabIndex}">
                    <c:out value="${button}"/>
                </app2:securitySubmit>

                <c:if test="${op != 'delete'}">
                    <app2:checkAccessRight functionality="COMMUNICATION" permission="EXECUTE">
                        <html:submit
                                styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                property="${generateButton}"
                                tabindex="${communicationButtonsTabIndex}">
                            <fmt:message key="Document.generate"/>
                        </html:submit>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="COMMUNICATION" permission="UPDATE">
                        <c:set var="docInfo"
                               value="${app2:getCommunicationDocumentInfo(mainCommunicationForm.dtoMap['contactId'])}"/>
                        <c:if test="${not empty docInfo.freeTextId}">
                            <app:url var="openDocUrl"
                                     value="contacts/Download/WebDocument.do?dto(freeTextId)=${docInfo.freeTextId}&dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}"
                                     contextRelative="true"/>

                            <html:button property=""
                                         styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                         onclick="location.href='${openDocUrl}'"
                                         tabindex="${communicationButtonsTabIndex}">
                                <fmt:message key="Document.open"/>
                            </html:button>

                            <%--send via email--%>
                            <c:if test="${app2:hasDefaultMailAccount(pageContext.request)}">

                                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">

                                    <c:set var="telecomEmailBoxId"
                                           value="toEmail_id${communicationButtonsTabIndex}"/>
                                    <html:button property=""
                                                 styleClass="${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                                 onclick="sendWebDocumentViaEmail('${telecomEmailBoxId}')"
                                                 tabindex="${communicationButtonsTabIndex}">
                                        <fmt:message key="Communication.webDocument.sendViaEmail"/>
                                    </html:button>

                                    <%--aaa--%>

                                    <c:set var="TELECOMTYPE_EMAIL"
                                           value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
                                    <div class="form-group selectInlineButtons col-xs-12 col-sm-3">
                                        <app:telecomSelect property="toEmail"
                                                           styleId="${telecomEmailBoxId}"
                                                           tabindex="${communicationButtonsTabIndex}"
                                                           telecomIdColumn="telecomid"
                                                           numberColumn="telecomnumber"
                                                           telecomType="${TELECOMTYPE_EMAIL}"
                                                           addressId="${mainCommunicationForm.dtoMap['addressId']}"
                                                           contactPersonId="${mainCommunicationForm.dtoMap['contactPersonId']}"
                                                           showOwner="true"
                                                           styleClass="select ${app2:getFormSelectClasses()} marginLeft marginRight"
                                                           optionStyleClass="list" showDescription="false"
                                                           selectPredetermined="true"/>
                                    </div>
                                </app2:checkAccessRight>
                            </c:if>
                        </c:if>
                    </app2:checkAccessRight>
                </c:if>

                <html:cancel
                        styleClass="${app2:getFormButtonCancelClasses()} pull-left marginRight marginLeft marginButton"
                        tabindex="${communicationButtonsTabIndex}">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>

            </div>
        </div>
    </c:when>

    <c:when test="${email == mainCommunicationForm.dtoMap['type']}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create'}">
                <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                    <app2:securitySubmit operation="${op}" functionality="COMMUNICATION"
                                         property="${sendButton}"
                                         styleClass="${app2:getFormButtonClasses()} marginButton"
                                         tabindex="${communicationButtonsTabIndex}">
                        <fmt:message key="Common.send"/>
                    </app2:securitySubmit>
                </app2:checkAccessRight>
            </c:if>
            <c:if test="${op == 'update' && !isCampGeneration}">
                <c:if test="${app2:hasEmailAccount(sessionScope.user.valueMap['userId'], pageContext.request)}">
                    <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                        <c:if test="${'1' == mainCommunicationForm.dtoMap['inOut']}">
                            <html:button property="dto(reply)"
                                         styleClass="${app2:getFormButtonClasses()} marginButton"
                                         onclick="javascript:reply();"
                                         tabindex="${communicationButtonsTabIndex}">
                                <fmt:message key="Webmail.reply"/>
                            </html:button>
                            <html:button property="dto(replyAll)"
                                         styleClass="${app2:getFormButtonClasses()} marginButton"
                                         onclick="javascript:replyAll();"
                                         tabindex="${communicationButtonsTabIndex}">
                                <fmt:message key="Webmail.replyAll"/>
                            </html:button>
                        </c:if>
                        <html:button property="dto(forward)"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     onclick="javascript:forwardEmail();"
                                     tabindex="${communicationButtonsTabIndex}">
                            <fmt:message key="Webmail.forward"/>
                        </html:button>
                    </app2:checkAccessRight>
                </c:if>
                <c:if test="${not empty mainCommunicationForm.dtoMap['mailId']}">
                    <input type="button"
                           class="${app2:getFormButtonClasses()} marginButton"
                           value="<fmt:message
                                       key="Common.print"/>"
                           onclick="printMail();"
                           tabindex="${communicationButtonsTabIndex}">
                </c:if>
            </c:if>
            <c:if test="${'delete' == op}">
                <app2:securitySubmit operation="${op}"
                                     functionality="COMMUNICATION"
                                     property="${saveButton}"
                                     styleClass="${app2:getFormButtonClasses()} marginButton"
                                     tabindex="${communicationButtonsTabIndex}">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel
                    styleClass="${app2:getFormButtonCancelClasses()} marginButton"
                    tabindex="${communicationButtonsTabIndex}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </c:when>
    <c:otherwise>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="COMMUNICATION"
                                 property="${saveButton}"
                                 styleClass="${app2:getFormButtonClasses()} marginButton"
                                 tabindex="${communicationButtonsTabIndex}">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'update'}">
                <input type="button"
                       class="${app2:getFormButtonClasses()} marginButton"
                       value="<fmt:message
                                   key="Common.print"/>"
                       onclick="printCommunication();"
                       tabindex="${communicationButtonsTabIndex}">
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()} marginButton"
                         tabindex="${communicationButtonsTabIndex}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </c:otherwise>
</c:choose>

<style type="text/css">
    .selectInlineButtons{
        margin-right: -10px !important;
    }
</style>
