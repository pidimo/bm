<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>

<html:form action="${action}" enctype="multipart/form-data" styleClass="form-horizontal">

    <html:hidden property="dto(reportId)" value="${param['reportId']}"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${op=='update' || op=='delete'}">
        <html:hidden property="dto(artifactId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${not empty reportCompanyId}">
        <html:hidden property="dto(companyId)" value="${reportCompanyId}"/>
    </c:if>

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">

            <app2:securitySubmit operation="${op}" functionality="JRXMLREPORT"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="1">${button}</app2:securitySubmit>

            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="3"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="file_id">
                    <fmt:message key="Report.artifact.file"/>
                </label>

                <div class="${app2:getFormContainClasses(op != 'create')}">

                    <c:if test="${op != 'delete'}">
                        <tags:bootstrapFile property="dto(file)"
                                            glyphiconClass="glyphicon-folder-open"
                                            tabIndex="5"
                                            styleId="file_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </c:if>
                    <html:hidden property="dto(fileName)" write="${op != 'create'}"/>
                </div>
            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="JRXMLREPORT"
                                 styleClass="button ${app2:getFormButtonClasses()}"
                                 tabindex="6">${button}</app2:securitySubmit>

            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="7"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="reportArtifactForm"/>