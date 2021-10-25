<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(templateId)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
        <html:hidden property="dto(activityId)"/>

        <html:hidden property="dto(documentType)"/>

        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(userAddressId)" styleId="idUserAddress"
                     value="${sessionScope.user.valueMap['userAddressId']}"/>

        <html:hidden property="dto(isCampaignLight)" value="${isCampaignLight}"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(generate)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                <fmt:message key="Document.generate"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="templateId_id">
                        <fmt:message key="Campaign.activity.docGenerate.template"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(not empty documentSendForm.dtoMap['createAgain'])}">
                        <fanta:select property="dto(templateId)" styleId="templateId_id" listName="templateList"
                                      module="/campaign"
                                      labelProperty="description" valueProperty="templateId"
                                      firstEmpty="${app2:campaignHasOnlyOneTemplate(param.campaignId,documentSendForm.dtoMap['documentType'])?'false':'true'}"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="5"
                                      readOnly="${not empty documentSendForm.dtoMap['createAgain']}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="campaignId"
                                             value="${not empty param.campaignId ? param.campaignId : 0}"/>
                            <fanta:parameter field="documentType"
                                             value="${not empty documentSendForm.dtoMap['documentType'] ? documentSendForm.dtoMap['documentType'] : 0}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="idSenderEmployee">
                        <fmt:message key="Campaign.activity.emailGenerate.senderEmployee"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <c:choose>
                            <c:when test="${isCampaignLight}">
                                <html:hidden property="dto(senderEmployeeId)" styleId="idSenderEmployee"/>

                                <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                                    <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:label>
                            </c:when>
                            <c:otherwise>
                                <c:set var="senderEmployees"
                                       value="${app2:getSenderEmployeeList(documentSendForm.dtoMap['activityId'], pageContext.request)}"/>
                                <html:select property="dto(senderEmployeeId)" styleId="idSenderEmployee"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="6">
                                    <html:options collection="senderEmployees" property="value" labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="firstOrder_id">
                        <fmt:message key="Campaign.activity.docGenerate.OrderedPrimarilyBy"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <c:set var="orderColumns" value="${app2:getDocumentGenerationOrderBy(pageContext.request)}"/>
                        <c:set var="orderTypes" value="${app2:getDocumentColumnOrderType(pageContext.request)}"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <html:select property="dto(firstOrder)"
                                             styleId="firstOrder_id"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                             tabindex="7">
                                    <html:option value="">&nbsp;</html:option>
                                    <html:options collection="orderColumns" property="value" labelProperty="label"/>
                                </html:select>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <html:select property="dto(firstOrderType)"
                                             styleClass="${app2:getFormSelectClasses()} shortSelect" tabindex="8">
                                    <html:options collection="orderTypes" property="value" labelProperty="label"/>
                                </html:select>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="secondOrder_id">
                        <fmt:message key="Campaign.activity.docGenerate.OrderedSecondarilyBy"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <html:select property="dto(secondOrder)" styleId="secondOrder_id"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="9">
                                    <html:option value="">&nbsp;</html:option>
                                    <html:options collection="orderColumns" property="value" labelProperty="label"/>
                                </html:select>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <html:select property="dto(secondOrderType)"
                                             styleClass="${app2:getFormSelectClasses()} shortSelect" tabindex="10">
                                    <html:options collection="orderTypes" property="value" labelProperty="label"/>
                                </html:select>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="docCommTitle_id">
                        <fmt:message key="Campaign.activity.docGenerate.communicationTitle"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <app:text property="dto(docCommTitle)" styleId="docCommTitle_id"
                                  styleClass="${app2:getFormSelectClasses()} middleText"
                                  maxlength="100" tabindex="11"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Campaign.activity.docGenerate.createCommunication"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:hidden property="dto(createAgain)"/>
                                <html:checkbox property="dto(createComm)" styleId="createComm_id" tabindex="12"/>
                                <label for="createComm_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit property="dto(generate)" operation="execute" functionality="CAMPAIGNACTIVITY"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="13">
                <fmt:message key="Document.generate"/>
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="14">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="documentSendForm"/>