<%@ include file="/Includes.jsp" %>

<div class="${app2:getFormClasses()} form-horizontal">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlGenerateEmailReturn}'">
            ${button}
        </html:button>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Campaign.emailSend.inBackground.title"/>
        </legend>

        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <fmt:message key="Campaign.emailSend.inBackground">
                    <fmt:param value="${notificationMail}"/>
                </fmt:message>
            </div>
        </div>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                     onclick="location.href='${urlGenerateEmailReturn}'">
            ${button}
        </html:button>
    </div>
</div>