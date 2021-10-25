<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<html:form action="${action}" styleId="companyFormId" styleClass="form-horizontal">
    <html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
    <html:hidden property="dto(companyId)"/>
    <html:hidden property="dto(name1)"/>
    <html:hidden property="dto(name2)"/>
    <html:hidden property="dto(name3)"/>

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <p>
                    <fmt:message key="Company.delete.message"/>
                </p>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)"
                                 indexed="31">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
