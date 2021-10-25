<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
%>

<html:form action="${action}?index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}" focusIndex="2"
           styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="participantName_id">
                        <fmt:message key="Appointment.userName"/>
                    </label>
                    <div class="${app2:getFormContainClasses(true)}">
                        <app:text property="dto(participantName)" styleId="participantName_id" view="true" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="taskGroupName_id">
                        <fmt:message key="Appointment.groupName"/>
                    </label>
                    <div class="${app2:getFormContainClasses(true)}">
                        <app:text property="dto(taskGroupName)" styleId="taskGroupName_id" view="true" tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="statusId_id">
                        <fmt:message key="Task.status"/>
                    </label>
                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <html:select property="dto(statusId)" styleId="statusId_id" styleClass="${app2:getFormSelectClasses()}" readonly="${op == 'delete'}"
                                     tabindex="3">
                            <html:option value=""/>
                            <html:options collection="statusList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="noteValue_id">
                        <fmt:message key="Task.notes"/>
                        </label>
                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <html:textarea property="dto(noteValue)" styleId="noteValue_id" styleClass="${app2:getFormInputClasses()} mediumDetail"
                                       style="height:100px;"
                                       readonly="${op == 'delete'}" tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(scheduledUserId)"/>
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(group)" value="${param.group}"/>
                </c:if>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="TASKUSER" permission="UPDATE">
                <c:if test="${op == 'create' || op == 'update'}">
                    <html:submit property="dto(submit)" styleClass="${app2:getFormButtonClasses()}" tabindex="5">
                        <fmt:message key="Common.save"/>
                    </html:submit>
                </c:if>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="TASKUSER" permission="DELETE">
                <c:if test="${op == 'delete'}">
                    <html:submit property="dto(delete)" styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                        <fmt:message key="Common.delete"/>
                    </html:submit>
                </c:if>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="participantForm"/>
