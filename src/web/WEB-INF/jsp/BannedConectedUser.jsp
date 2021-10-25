<%@include file="/Includes.jsp" %>

<br/>
<br/>

<html:form action="/Banned/PreviousUserConnected.do">

    <%--when process from external logon AJAX--%>
    <div id="isOtherUserConnectedDivId">
        <input type="hidden" id="urlOtherUserConnectedId" value="${param['urlOtherUserConnected']}">
    </div>


    <div class="${app2:getFormClasses()}">

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="User.alreadyOtherUserLogged.title"/>
                </legend>

                <div class="alert alert-danger" role="alert">
                    <fmt:message key="User.alreadyOtherUserLogged"/>
                </div>
            </fieldset>
        </div>


        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit property="dto(save)" styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                <fmt:message key="Common.continue"/>
            </html:submit>

            <app:url var="urlLogoff"
                     value="/Logoff.do?locale=${sessionScope.user.valueMap['locale']}&keepUserSessionAsActive=true"/>
            <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlLogoff}'" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>

