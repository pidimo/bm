<%@ include file="/Includes.jsp" %>
<tags:initBootstrapFile/>
<html:form action="${action}" focus="dto(comment)" enctype="multipart/form-data" styleClass="form-horizontal">
<div class="${app2:getFormClasses()}">
    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(attachId)"/>
        <html:hidden property="dto(freeTextId)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(attachId)"/>
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit property="dto(save)"
                             operation="${op}"
                             functionality="CAMPAIGNATTACH"
                             styleClass="button ${app2:getFormButtonClasses()}">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="7">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                    ${title}
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="comment_id">
                    <fmt:message key="Attach.comment"/>
                </label>

                <div class="${app2:getFormContainClasses('delete' == op)}">
                    <app:text property="dto(comment)"
                              styleClass="middleText ${app2:getFormInputClasses()}"
                              styleId="comment_id"
                              tabindex="1"
                              maxlength="100"
                              view="${'delete' == op}"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <c:if test="${'update'== op || 'delete' == op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Attach.fileName"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(filename)"/>
                        <html:hidden property="dto(filenameAux)"
                                     value="${attachForm.dtoMap['filename']}"/>
                            ${attachForm.dtoMap['filename']}
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <html:hidden property="dto(size)"/>
                        <fmt:message key="Attach.fileSize"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <c:set var="e" value="${app2:divide(attachForm.dtoMap['size'],(1024))}"/>
                        <c:choose>
                            <c:when test="${e > 0}">
                                ${e}
                            </c:when>
                            <c:otherwise>
                                1
                            </c:otherwise>
                        </c:choose>
                        <fmt:message key="Webmail.mailTray.Kb"/>

                    </div>
                </div>
            </c:if>
            <c:if test="${'delete' != op}">
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="imageFile_id">
                        <fmt:message key="Common.file"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <tags:bootstrapFile property="file"
                                            glyphiconClass="glyphicon-folder-open"
                                            tabIndex="2"
                                            styleId="imageFile_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </c:if>
        </fieldset>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit property="dto(save)"
                             operation="${op}"
                             functionality="CAMPAIGNATTACH"
                             styleClass="button ${app2:getFormButtonClasses()}" tabindex="4">
            ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="5">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>
</div>
</html:form>

<tags:jQueryValidation formName="attachForm"/>

