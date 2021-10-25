<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message   var="dateTimePattern" key="dateTimePattern"/>


    <html:form action="${action}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(generationId)"/>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(campaignSentLogId)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(campaignSentLogId)"/>
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

                <div class="button ${app2:getFormButtonWrapperClasses()}">
                    <app2:securitySubmit property="dto(save)"
                                         operation="${op}"
                                         functionality="CAMPAIGNSENTLOG"
                                         styleClass="button ${app2:getFormButtonClasses()}">
                        ${button}
                    </app2:securitySubmit>
                    <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </div>

            <div class="${app2:getFormPanelClasses()}">
                <fieldset>
                    <legend class="title">
                            ${title}
                    </legend>

                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="CampaignSentLog.generationTime"/>
                        </label>
                        <div class="${app2:getFormContainClasses(true)}">
                            <c:out value="${app2:getDateWithTimeZone(dto.generationTime, timeZone, dateTimePattern)}"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="CampaignSentLog.user"/>
                        </label>
                        <div class="${app2:getFormContainClasses(true)}">
                            <c:out value="${dto.userName}"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="CampaignSentLog.activity"/>
                        </label>
                        <div class="${app2:getFormContainClasses(true)}">
                            <c:out value="${dto.activityName}"/>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="CampaignSentLog.summary"/>
                        </label>
                        <div class="${app2:getFormContainClasses(true)}">
                            <c:out value="${dto.totalSuccess}/${dto.totalSent}"/>
                        </div>
                    </div>
                </fieldset>
            </div>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:securitySubmit property="dto(save)"
                                     operation="${op}"
                                     functionality="CAMPAIGNSENTLOG"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     tabindex="4">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="5">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>

    </html:form>


