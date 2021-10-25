<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<c:set var="SUCCESS_STATUS" value="<%= CampaignConstants.SentLogContactStatus.SUCCESS.getConstantAsString()%>"/>
<c:set var="FAILED_STATUS" value="<%= CampaignConstants.SentLogContactStatus.FAILED.getConstantAsString()%>"/>
<c:set var="FAILED_WITHOUT_EMAIL_STATUS" value="<%= CampaignConstants.SentLogContactStatus.FAILED_WITHOUT_EMAIL.getConstantAsString()%>"/>
<c:set var="FAILED_UNKNOWN_STATUS" value="<%= CampaignConstants.SentLogContactStatus.FAILED_UNKNOWN.getConstantAsString()%>"/>
<c:set var="FAILED_RESPONSIBLE_STATUS" value="<%= CampaignConstants.SentLogContactStatus.FAILED_RESPONSIBLE.getConstantAsString()%>"/>
<c:set var="WAITING_STATUS" value="<%= CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.getConstantAsString()%>"/>
<c:set var="SENDING_IN_BACKGROUND_STATUS" value="<%= CampaignConstants.SentLogContactStatus.SENDING_IN_BACKGROUND.getConstantAsString()%>"/>

<c:set var="DOCUMENTTYPE_HTML" value="<%= CampaignConstants.DocumentType.HTML.getConstantAsString()%>"/>


<html:form action="/SentLogContact/List.do?campaignId=${param.campaignId}&campaignSentLogId=${param.campaignSentLogId}"
           focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)" styleClass="form-horizontal">

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="filedTextOut_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text
                            property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                            styleId="filedTextOut_id"
                            styleClass="largeText ${app2:getFormInputClasses()}"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="SentLogContact/List.do?campaignId=${param.campaignId}&campaignSentLogId=${param.campaignSentLogId}"
                            mode="bootstrap"
                            parameterName="contactName1"/>
        </div>
</html:form>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <c:choose>
                    <c:when test="${not empty param.activityId}">
                        <app:url value="/Campaign/Retry/Forward/Email/Send.do?dto(activityId)=${param.activityId}&dto(campaignId)=${param.campaignId}&campaignSentLogId=${param.campaignSentLogId}&dto(documentType)=${DOCUMENTTYPE_HTML}"
                                 var="retryUrl"/>
                    </c:when>
                    <c:otherwise>
                        <app:url value="/Campaign/Retry/Light/Forward/Email/Send.do?dto(campaignId)=${param.campaignId}&campaignSentLogId=${param.campaignSentLogId}&dto(documentType)=${DOCUMENTTYPE_HTML}"
                                 var="retryUrl"/>
                    </c:otherwise>
                </c:choose>

                <c:if test="${app2:existCampaignSentLogFailedRecipients(param.campaignSentLogId)}">
                    <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="EXECUTE">
                        <html:button property="" styleClass="button ${app2:getFormButtonClasses()}" onclick="location.href='${retryUrl}'">
                            <fmt:message key="SentLogContact.retrySendOfFailed"/>
                        </html:button>
                    </app2:checkAccessRight>
                </c:if>

                <app:url value="/CampaignSentLog/List.do" var="cancelUrl"/>
                <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${cancelUrl}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </div>


            <div class="table-responsive">
                <fanta:table mode="bootstrap" list="sentLogContactList"
                             styleClass="${app2:getFantabulousTableClases()}"
                             align="left"
                             width="100%"
                             id="sentLogContact"
                             action="SentLogContact/List.do?campaignId=${param.campaignId}&campaignSentLogId=${param.campaignSentLogId}"
                             imgPath="${baselayout}">

                    <c:if test="${personType == sentLogContact.addressType}">
                        <c:set var="editAddressUrl"
                               value="contacts/Person/Forward/Update.do?contactId=${sentLogContact.addressId}&dto(addressId)=${sentLogContact.addressId}&dto(addressType)=${sentLogContact.addressType}&dto(name1)=${app2:encode(sentLogContact.contactNames)}&index=0"/>
                    </c:if>

                    <c:if test="${organizationType == sentLogContact.addressType}">
                        <c:set var="editAddressUrl"
                               value="contacts/Organization/Forward/Update.do?contactId=${sentLogContact.addressId}&dto(addressId)=${sentLogContact.addressId}&dto(addressType)=${sentLogContact.addressType}&dto(name1)=${app2:encode(sentLogContact.contactNames)}&index=0"/>
                    </c:if>

                    <c:if test="${null != sentLogContact.contactPersonId}">
                        <c:set var="editContactPersonUrl"
                               value="contacts/ContactPerson/Forward/Update.do?contactId=${sentLogContact.addressId}&dto(addressId)=${sentLogContact.addressId}&dto(contactPersonId)=${sentLogContact.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
                    </c:if>

                    <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader" width="3%">

                        <c:choose>
                            <c:when test="${not empty sentLogContact.contactPersonName}">
                                <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                                <%--<c:set var="personPrefixImageName" value="person"/>--%>
                            </c:when>
                            <c:otherwise>
                                <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                                <%--<c:set var="personPrefixImageName" value="person-private"/>--%>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${personType == sentLogContact.addressType}">
                            <span class="${personPrefixImageName}"></span>
                        </c:if>

                        <c:if test="${organizationType == sentLogContact.addressType}">
                            <c:choose>
                                <c:when test="${not empty sentLogContact.contactPersonName}">
                                    <span class="${app2:getClassGlyphPerson()}"></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="${app2:getClassGlyphPrivatePerson()}"></span>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </fanta:actionColumn>
                    <fanta:dataColumn name="contactNames"
                                      styleClass="listItem"
                                      title="SentLogContact.contact"
                                      headerStyle="listHeader"
                                      width="25%"
                                      orderable="true"
                                      maxLength="45"
                                      renderData="false">
                        <app:link action="${editAddressUrl}" addModuleName="false" contextRelative="true">
                            <c:out value="${sentLogContact.contactNames}"/>
                        </app:link>&nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="contactPersonName"
                                      styleClass="listItem"
                                      title="SentLogContact.contactPerson"
                                      headerStyle="listHeader"
                                      width="25%"
                                      orderable="true"
                                      maxLength="30"
                                      renderData="false">
                        <app:link action="${editContactPersonUrl}" addModuleName="false" contextRelative="true">
                            <c:out value="${sentLogContact.contactPersonName}"/>
                        </app:link> &nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="status" styleClass="listItem" title="SentLogContact.status"
                                      headerStyle="listHeader" style="text-align: center"
                                      width="10%" orderable="true" renderData="false">
                        <c:choose>
                            <c:when test="${sentLogContact.status eq SUCCESS_STATUS}">
                                <span title="<fmt:message key="SentLogContact.success"/>" class="${app2:getClassGlyphOk()}"></span>
                            </c:when>
                            <c:when test="${sentLogContact.status eq WAITING_STATUS}">
                                <fmt:message key="<%= CampaignConstants.SentLogContactStatus.WAITING_TO_BE_SENT_IN_BACKGROUND.getResource()%>"/>
                            </c:when>
                            <c:when test="${sentLogContact.status eq SENDING_IN_BACKGROUND_STATUS}">
                                <fmt:message key="<%= CampaignConstants.SentLogContactStatus.SENDING_IN_BACKGROUND.getResource()%>"/>
                            </c:when>
                            <c:otherwise>
                                <span title="<fmt:message key="SentLogContact.failed"/>" class="${app2:getClassGlyphRemoveSign()}"></span>
                            </c:otherwise>
                        </c:choose>
                    </fanta:dataColumn>
                    <fanta:dataColumn name="errorMessage" styleClass="listItem2" title="SentLogContact.errorMessage"
                                      headerStyle="listHeader"
                                      width="35%" renderData="false">
                        <c:choose>
                            <c:when test="${sentLogContact.status eq FAILED_WITHOUT_EMAIL_STATUS}">
                                <fmt:message key="<%= CampaignConstants.SentLogContactStatus.FAILED_WITHOUT_EMAIL.getResource()%>"/>
                            </c:when>
                            <c:when test="${sentLogContact.status eq FAILED_UNKNOWN_STATUS}">
                                <fmt:message key="<%= CampaignConstants.SentLogContactStatus.FAILED_UNKNOWN.getResource()%>"/>
                            </c:when>
                            <c:when test="${sentLogContact.status eq FAILED_RESPONSIBLE_STATUS}">
                                <fmt:message key="<%= CampaignConstants.SentLogContactStatus.FAILED_RESPONSIBLE.getResource()%>"/>
                            </c:when>
                            <c:otherwise>
                                <c:out value="${sentLogContact.errorMessage}"/>
                            </c:otherwise>
                        </c:choose>
                    </fanta:dataColumn>
                </fanta:table>
            </div>
