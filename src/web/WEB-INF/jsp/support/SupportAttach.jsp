<%@ include file="/Includes.jsp" %>

<tags:initBootstrapFile/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<html:form action="${action}" enctype="multipart/form-data" focus="dto(comment)" styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>

    <c:if test="${param.articleId!=null}">
        <html:hidden property="dto(articleId)" value="${param.articleId}"/>
    </c:if>

    <%--for ArticleHistory YUMI--%>
    <html:hidden property="dto(actionValue)" value="${actionValue}"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(attachId)"/>
        <c:if test="${null != dto.supportAttachName}">
            <c:set var="fileName" value="${dto.supportAttachName}"/>
        </c:if>
        <html:hidden property="dto(fileName)" value="${fileName}"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="comment_id">
                    <fmt:message key="SupportAttach.comment"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(comment)"
                              styleId="comment_id"
                              styleClass="largeText ${app2:getFormInputClasses()}" maxlength="80"
                              view="${'delete' == op}"/>
                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>

            <c:choose>
                <c:when test="${'create' == op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="SupportAttach.fileName"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <tags:bootstrapFile property="dto(file)" styleId="file_id"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:when>
                <c:when test="${'update' == op}">
                    <html:hidden property="dto(supportAttachName)" value="${fileName}"/>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="file_id">
                            <fmt:message key="SupportAttach.fileName"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <tags:bootstrapFile property="dto(file)" styleId="file_id"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="SupportAttach.fileUplated"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                                ${fileName}
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(supportAttachName)" value="${fileName}"/>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="SupportAttach.fileUplated"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                                ${fileName}
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>


            <c:if test="${'delete' == op || 'update' == op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="SupportAttach.size"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(size)"/>
                        <c:choose>
                            <c:when test="${supportAttachForm.dtoMap['size'] < 1024}">
                                ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                            </c:when>
                            <c:otherwise>
                                ${fn:substringBefore(supportAttachForm.dtoMap['size']/1024,".")}
                                <fmt:message key="Webmail.mailTray.Kb"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="SupportAttach.uploadDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(uploadDateTime)"/>
                            ${app2:getDateWithTimeZone(supportAttachForm.dtoMap['uploadDateTime'], timeZone, dateTimePattern)}
                    </div>
                </div>
            </c:if>

            <c:if test="${'update' == op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Common.download"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(supportAttachName)"/>
                        <c:set var="download"
                               value="${attachDownload}&dto(attachId)=${supportAttachForm.dtoMap['attachId']}&dto(supportAttachName)=${app2:encode(supportAttachForm.dtoMap['supportAttachName'])}&dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}&articleId=${param.articleId}&caseId=${param.caseId}"/>
                        <html:link action="${download}" titleKey="Common.download">
                            <span class="glyphicon glyphicon-download-alt"></span>
                        </html:link>
                    </div>
                </div>
            </c:if>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="row">
            <div class="col-xs-12 ">
                <c:if test="${op != 'create' && (supportAttachForm.dtoMap.userId == sessionScope.user.valueMap['userId'] || supportAttachForm.dtoMap.createUserId == sessionScope.user.valueMap['userId'])}">
                    <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         tabindex="10">
                        ${button}
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         tabindex="10">
                        ${button}
                    </app2:securitySubmit>

                    <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH"
                                         styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                         tabindex="11"
                                         property="SaveAndNew">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"><fmt:message
                        key="Common.cancel"/></html:cancel>
            </div>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="supportAttachForm"/>
