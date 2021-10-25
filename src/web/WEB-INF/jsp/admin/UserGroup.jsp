<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(groupName)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div id="userGroup.jsp">
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(op)" value="${op}"/>
            <c:if test="${op=='update' || op=='delete'}">
                <html:hidden property="dto(userGroupId)"/>
            </c:if>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
            </c:if>
            <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
            </c:if>
            <div class="${app2:getFormPanelClasses()}">

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="groupName_id">
                        <fmt:message key="UserGroup.name"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <app:text property="dto(groupName)" styleId="groupName_id"
                                  styleClass="${app2:getFormInputClasses()} middleText"
                                  maxlength="60" tabindex="1"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="groupType_id">
                        <fmt:message key="UserGroup.groupType"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op)}">
                        <c:set var="groupTypes" value="${app2:getUserGroupTypes(pageContext.request)}"/>
                        <html:select property="dto(groupType)" styleId="groupType_id"
                                     styleClass="${app2:getFormSelectClasses()} middleSelect"
                                     tabindex="2"
                                     readonly="${'delete' == op}">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="groupTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <c:if test="${op == 'create' || op == 'update'}">
                    <app2:securitySubmit operation="${op}" functionality="USERGROUP" property="dto(save)"
                                         styleClass="${app2:getFormButtonClasses()}" styleId="saveButtonId"
                                         tabindex="5">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'delete'}">
                    <app2:securitySubmit operation="DELETE" functionality="USERGROUP" property="dto(delete)"
                                         styleClass="${app2:getFormButtonClasses()}" tabindex="6">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="7">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="userGroupForm"/>