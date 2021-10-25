<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(signatureName)" styleClass="form-horizontal">
    <div class="col-xs-12 col-sm-10 col-sm-offset-1">

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(save)"
                                     styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId" tabindex="9">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="10">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(delete)"
                                     styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId" tabindex="11">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormSmallLabelClasses()}" for="signatureName_id">
                        <fmt:message key="Webmail.signature.name"/>
                    </label>

                    <div class="${app2:getFormBigContainClasses('delete' == op)}">
                        <app:text property="dto(signatureName)"
                                  styleClass="${app2:getFormInputClasses()} middleText"
                                  maxlength="30"
                                  styleId="signatureName_id"
                                  tabindex="1"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormSmallLabelClasses()}" for="mailAccountId_id">
                        <fmt:message key="Webmail.signature.mailAccount"/>
                    </label>

                    <div class="${app2:getFormBigContainClasses('delete' == op)}">
                        <fanta:select property="dto(mailAccountId)"
                                      listName="mailAccountList"
                                      labelProperty="email"
                                      styleId="mailAccountId_id"
                                      valueProperty="mailAccountId"
                                      firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} select"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
                            <c:if test="${'delete'== op || 'update' == op}">
                                <fanta:parameter field="mailAccountId"
                                                 value="${signatureForm.dtoMap['mailAccountId']}"/>
                            </c:if>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormSmallLabelClasses()}" for="isDefault_id">
                        <fmt:message key="Webmail.signature.isDefault"/>
                    </label>

                    <div class="${app2:getFormBigContainClasses('delete' == op)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(isDefault)"
                                               disabled="${op == 'delete'}"
                                               styleClass="radio"
                                               styleId="isDefault_id"
                                               tabindex="2"/>
                                <label for="isDefault_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormSmallLabelClasses()}" for="signatureMessage_id">
                        <fmt:message key="Webmail.signature.messageLabel"/>
                    </label>

                    <div class="${app2:getFormBigContainClasses('delete' == op)}">
                        <html:textarea property="dto(signatureMessage)"
                                       styleClass="${app2:getFormInputClasses()} mediumDetail"
                                       readonly="${op == 'delete'}"
                                       tabindex="3"
                                       styleId="signatureMessage_id"
                                       style="height:120px;width:100%;"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="topLabel ${app2:getFormGroupClasses()}" id="tr_html" style="${displayHtml}">
                    <label class="${app2:getFormSmallLabelClasses()}" for="body_field">
                        <fmt:message key="Webmail.htmlSignature.messageLabel"/>
                    </label>

                    <div class="${app2:getFormBigContainViewClasses()}">
                        <c:choose>
                            <c:when test="${'delete' != op}">
                                <tags:initTinyMCE4 textAreaId="body_field"
                                                       addBrowseImagePlugin="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                                <html:textarea property="dto(signatureHtmlMessage)"
                                               tabindex="4"
                                               styleClass="webmailBody ${app2:getFormInputClasses()}"
                                               styleId="body_field"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <c:set var="ad" value="${signatureForm.dtoMap.signatureHtmlMessage}"
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

                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(signatureId)"/>
                    <html:hidden property="dto(textSignatureId)"/>
                    <html:hidden property="dto(htmlSignatureId)"/>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(save)"
                                     styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId" tabindex="5">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="6">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(delete)"
                                     styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId" tabindex="7">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="8">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="signatureForm"/>