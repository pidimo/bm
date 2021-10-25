<%@ include file="/Includes.jsp" %>


<html:form action="${action}" focus="dto(branchName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div id="Branch.jsp">
                        <html:hidden property="dto(op)" value="${op}"/>
                        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(branchId)"/>
                    </c:if>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="branchName_id">
                            <fmt:message key="Branch.name"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text property="dto(branchName)" styleClass="largeText ${app2:getFormInputClasses()}"
                                      styleId="branchName_id"
                                      maxlength="100"
                                      view="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="group_id">
                            <fmt:message key="Branch.group"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <fanta:select property="dto(group)"
                                          listName="BranchSelectTagList" labelProperty="name" valueProperty="id"
                                          firstEmpty="true" styleClass="largeSelect ${app2:getFormSelectClasses()}"
                                          styleId="group_id"
                                          readOnly="${'delete' == op}"
                                          value="${dto.group}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <c:if test="${'create' != op}">
                                    <fanta:parameter field="branchId" value="${not empty dto.branchId?dto.branchId:0}"/>
                                </c:if>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                        <c:out value="${sessionScope.listshadow}" escapeXml="false"/>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="BRANCH"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="BRANCH"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="branchForm"/>