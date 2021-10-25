<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(projectId)" value="${param.projectId}"/>

        <c:if test="${'update'== op || 'delete'== op}">
            <html:hidden property="dto(subProjectId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PROJECTSUBPROJECT" property="save"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="5">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="6">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="SubProject.name"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <app:text property="dto(name)"
                                  styleClass="${app2:getFormInputClasses()} middleText"
                                  maxlength="40"
                                  view="${'delete' == op}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${op != 'create'}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="SubProject.totalInvoiceTime"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <c:set var="totalTimeMap"
                                   value="${app2:calculateProjectTimesBySubProject(subProjectForm.dtoMap['projectId'], subProjectForm.dtoMap['subProjectId'])}"/>

                            <fmt:formatNumber var="invoiceTotalFormated" value="${totalTimeMap.totalInvoiceTime}"
                                              type="number" pattern="${numberFormat}"/>
                                ${invoiceTotalFormated}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="SubProject.totalNoInvoiceTime"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <fmt:formatNumber var="noInvoiceTotalFormated" value="${totalTimeMap.totalNoInvoiceTime}"
                                              type="number" pattern="${numberFormat}"/>
                                ${noInvoiceTotalFormated}
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PROJECTSUBPROJECT" property="save"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="2">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew" tabindex="3">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="4">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="subProjectForm"/>

<c:if test="${'update'== op}">
    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe name="frame1"
                src="<app:url value="SubProject/ProjectTime/List.do?parameter(subProjectId)=${subProjectForm.dtoMap['subProjectId']}"/>"
                class="embed-responsive-item" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</c:if>
