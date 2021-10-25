<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(groupName)" styleClass="form-horizontal" styleId="ProductGroup.jsp">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(groupId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="groupName_id">
                        <fmt:message key="ProductGroup.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(groupName)" styleId="groupName_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} largeText"
                                  maxlength="80" tabindex=""/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="parentGroupId_id">
                        <fmt:message key="ProductGroup.nameParent"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(parentGroupId)"
                                      listName="productGroupSelectTagList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      styleId="parentGroupId_id"
                                      firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} select"
                                      readOnly="${'delete' == op}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <c:if test="${'update' == op}">
                                <fanta:parameter field="groupId" value="${not empty dto.groupId?dto.groupId:0}"/>
                            </c:if>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PRODUCTGROUP"
                                 styleClass="${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:choose>
                <c:when test="${op == 'create' }">
                    <app2:securitySubmit operation="${op}" functionality="PRODUCTGROUP"
                                         styleClass="${app2:getFormButtonClasses()}"
                                         property="SaveAndNew">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                    <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </c:when>
                <c:otherwise>
                    <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </c:otherwise>
            </c:choose>

        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="productGroupForm"/>

