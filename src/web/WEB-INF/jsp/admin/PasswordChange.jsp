<%@include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("hourList", JSPHelper.getHours());
    pageContext.setAttribute("minutesList", JSPHelper.getMinutes());
%>

<tags:jscript language="JavaScript" src="/js/admin/admin.jsp"/>
<tags:initBootstrapDatepicker/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="dto(description)" styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${'create' == op}">
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
    </c:if>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(passwordChangeId)"/>
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(changeTime)"/>
        <html:hidden property="dto(updateDateTime)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <c:set var="isOldChangeTime"
               value="${app2:isOldRelatedToCurrentTime(passwordChangeForm.dtoMap['changeTime'], pageContext.request)}"
               scope="request"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <c:set var="readOnly" value="${op == 'delete' or isOldChangeTime}" scope="request"/>
   <div class="${app2:getFormClasses()}">
       <div class="${app2:getFormButtonWrapperClasses()}">
           <c:if test="${!isOldChangeTime}">
               <app2:securitySubmit operation="${op}" functionality="PASSWORDCHANGE"
                                    onclick="send(defineRoles, undefineRoles)"
                                    tabindex="10" styleClass="button ${app2:getFormButtonClasses()}">
                   ${button}
               </app2:securitySubmit>
           </c:if>

           <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="11">
               <fmt:message key="Common.cancel"/>
           </html:cancel>
       </div>
       <div class="${app2:getFormPanelClasses()}">
           <legend class="title">
               <c:out value="${title}"/>
           </legend>
           <div class="${app2:getFormGroupClasses()}" }>
               <label class="${app2:getFormLabelClasses()}" for="description_id">
                   <fmt:message key="PasswordChange.description"/>
               </label>

               <div class="${app2:getFormContainClasses(readOnly)}">
                   <app:text property="dto(description)"
                             styleId="description_id"
                             styleClass="mediumText ${app2:getFormInputClasses()}"
                             maxlength="255" tabindex="1"
                             view="${readOnly}"/>
                   <span class="glyphicon form-control-feedback iconValidation"></span>
               </div>
           </div>
           <div class="${app2:getFormGroupClasses()}" }>
               <label class="${app2:getFormLabelClasses()}" for="changeDateId">
                   <fmt:message key="PasswordChange.changeTime"/>
               </label>

               <div class="${app2:getFormContainClasses(readOnly)}">
                   <div class="row">
                       <div class="col-xs-12 col-sm-6 wrapperButton">
                           <div class="input-group date">
                               <app:dateText property="dto(changeDate)"
                                             styleId="changeDateId"
                                             mode="bootstrap"
                                             calendarPicker="${op != 'delete'}"
                                             datePatternKey="${datePattern}"
                                             styleClass="text ${app2:getFormInputClasses()}"
                                             maxlength="10"
                                             currentDate="true"
                                             view="${readOnly}"
                                             tabindex="2"/>
                           </div>
                       </div>
                       <div class="col-xs-12 col-sm-6 wrapperButton">
                           <div class="input-group">
                               <html:select property="dto(hourChange)" tabindex="3"
                                            styleClass="select ${app2:getFormSelectClasses()}" readonly="${readOnly}"
                                            style="border-radius:4px">
                                   <html:options collection="hourList" property="value" labelProperty="label"/>
                               </html:select>
                            <span class="${readOnly ?'':'input-group-addon'}"
                                  style="background-color: transparent;border: transparent">:</span>
                               <html:select property="dto(minuteChange)" styleClass="select ${app2:getFormSelectClasses()}"
                                            tabindex="4"
                                            readonly="${readOnly}" style="border-radius:4px">
                                   <html:options collection="minutesList" property="value" labelProperty="label"/>
                               </html:select>
                           </div>
                       </div>
                   </div>
                   <span class="glyphicon form-control-feedback iconValidation"></span>
               </div>
           </div>
           <table width="100%">
               <tr>
                   <td class="control-label label-left">
                       <fmt:message key="PasswordChange.role.available"/>
                   </td>
                   <td></td>
                   <td class="control-label label-left">
                       <fmt:message key="PasswordChange.role.assigned"/>
                   </td>
               </tr>
               <tr>
                   <td width="47%">
                       <c:if test="${availableRolesList == null}">
                           <c:set var="passChangeRoles"
                                  value="${app2:getPasswordChangeRoles(passwordChangeForm.dtoMap['passwordChangeId'],pageContext.request)}"
                                  scope="request"/>
                           <c:set var="availableRolesList" value="${passChangeRoles.availableRolePassChange}"
                                  scope="request"/>
                           <c:set var="assignedRolesList" value="${passChangeRoles.assignedRolePassChange}"
                                  scope="request"/>
                       </c:if>

                       <html:select property="undefineRoles" styleClass="multipleSelect ${app2:getFormSelectClasses()}"
                                    tabindex="6" size="10"
                                    multiple="true"
                                    ondblclick="move(this.form.undefineRoles,this.form.defineRoles)"
                                    disabled="${readOnly}">
                           <c:forEach var="item" items="${availableRolesList}">
                               <html:option value="${item.roleId}">${item.roleName}</html:option>
                           </c:forEach>
                       </html:select>
                   </td>

                   <td>
                       <div class="btn-group-vertical">
                           <html:button onclick="move(this.form.undefineRoles, this.form.defineRoles)"
                                        value="&#xf054;"
                                        style="border-radius: 4px;"
                                        styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                        property="B1">
                               >
                           </html:button>
                           <html:button onclick="alls(this.form.undefineRoles, this.form.defineRoles)"
                                        value="&#xf054;&#xf054;"
                                        style="border-radius: 4px;"
                                        styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                        property="B11">
                               >>
                           </html:button>
                           <html:button onclick="move(this.form.defineRoles, this.form.undefineRoles)"
                                        value="&#xf053;"
                                        style="border-radius: 4px;"
                                        styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                        property="B2">
                               <
                           </html:button>
                           <html:button onclick="alls(this.form.defineRoles, this.form.undefineRoles)"
                                        value="&#xf053;&#xf053;"
                                        style="border-radius: 4px;"
                                        styleClass="button fa ${app2:getFormButtonClasses()} marginButton"
                                        property="B22">
                               <<
                           </html:button>
                       </div>
                   </td>
                   <td width="47%">
                       <html:select property="defineRoles" size="10" multiple="true"
                                    styleClass="multipleSelect ${app2:getFormSelectClasses()}"
                                    indexed="10" tabindex="7"
                                    disabled="${readOnly}">
                           <c:forEach var="item" items="${assignedRolesList}">
                               <html:option value="${item.roleId}">${item.roleName}</html:option>
                           </c:forEach>
                       </html:select>
                   </td>
               </tr>
           </table>
           <c:if test="${op != 'create'}">
               <div class="${app2:getFormGroupClasses()}">
                   <label class="${app2:getFormLabelClasses()}">
                       <fmt:message key="PasswordChange.createdBy"/>
                   </label>

                   <div class="${app2:getFormContainClasses(true)}">
                       <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                           <fanta:parameter field="userId" value="${passwordChangeForm.dtoMap['userId']}"/>
                           <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                       </fanta:label>
                   </div>
               </div>
               <c:if test="${not empty passwordChangeForm.dtoMap['updateDateTime']}">
                   <div class="${app2:getFormGroupClasses()}">
                       <label class="${app2:getFormLabelClasses()}">
                           <fmt:message key="PasswordChange.lastModified"/>
                       </label>

                       <div class="${app2:getFormContainClasses(true)}">
                           <fmt:message var="dateTimePattern" key="dateTimePattern"/>
                           <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
                           <c:out value="${app2:getDateWithTimeZone(passwordChangeForm.dtoMap['updateDateTime'], timeZone, dateTimePattern)}"/>
                       </div>
                   </div>
               </c:if>
           </c:if>
       </div>
       <div class="${app2:getFormButtonWrapperClasses()}">
           <c:if test="${!isOldChangeTime}">
               <app2:securitySubmit operation="${op}" functionality="PASSWORDCHANGE"
                                    onclick="send(defineRoles, undefineRoles)"
                                    tabindex="8" styleClass="button ${app2:getFormButtonClasses()}">
                   ${button}
               </app2:securitySubmit>
           </c:if>

           <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="9">
               <fmt:message key="Common.cancel"/>
           </html:cancel>
       </div>
   </div>
</html:form>
<tags:jQueryValidation formName="passwordChangeForm"/>