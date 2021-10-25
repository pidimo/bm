<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<script type="text/javascript">
    function clear() {

        document.getElementById('fieldViewUserName_id').value = "";
        document.getElementById('fieldViewUserId_id').value = null;
        document.getElementById('reload').value = "true";
        document.getElementById('notification').style.display = "none";
        document.forms[0].submit();
    }

</script>

<html:form action="${action}" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(save_)"/>
        <html:hidden property="dto(lastUser)"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="field_name">
                        <fmt:message key="SupportUser.supportProduct"/>
                    </label>

                    <div class="${app2:getFormContainClasses('create' != op)}">

                        <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                        <html:hidden property="dto(2)" styleId="field_unitId"/>
                        <html:hidden property="dto(3)" styleId="field_price"/>
                        <div class=" input-group">
                            <app:text property="dto(productName)" styleId="field_name"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40" readonly="true" tabindex="1" view="${op != 'create'}"/>
                            <html:hidden property="dto(productId)" styleId="field_key"/>
                            <span class="input-group-btn">
                               <tags:bootstrapSelectPopup styleId="SearchProduct_id"
                                                          styleClass="${app2:getFormButtonClasses()}"
                                                          url="/products/SearchProduct.do"
                                                          name="SearchProduct"
                                                          titleKey="Common.search"
                                                          modalTitleKey="Product.Title.SimpleSearch"
                                                          hide="${op != 'create'}"
                                                          glyphiconClass="glyphicon-search"
                                                          tabindex="2"/>

                               <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                               styleClass="${app2:getFormButtonClasses()}"
                                                               nameFieldId="field_name"
                                                               titleKey="Common.clear"
                                                               hide="${op != 'create'}"
                                                               glyphiconClass="glyphicon-erase"
                                                               tabindex="3"/>

                             </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="fieldViewUserName_id">
                        <fmt:message key="SupportUser.supportUser"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <c:if test="${op == 'update'}">
                            <html:hidden property="dto(userId_selected)" styleId="fieldViewUserId_id"/>
                        </c:if>


                        <div class="input-group">
                            <app:text property="dto(userName)" styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      view="${op == 'delete'}" styleId="fieldViewUserName_id" readonly="true"
                                      tabindex="4"/>
                            <c:if test="${op != 'update'}">
                                <html:hidden property="dto(userId)" styleId="fieldViewUserId_id"/>
                            </c:if>
                            <c:if test="${('update' == op) || ('delete' == op)}">
                                <html:hidden property="dto(userId)" styleId="userId"/>
                                <%--<html:hidden property="dto(productId)"/>--%>
                            </c:if>
                            <span class="input-group-btn">
                               <tags:bootstrapSelectPopup styleId="searchUser_id"
                                                          styleClass="${app2:getFormButtonClasses()}"
                                                          url="/catalogs/Support/SupportUserImportList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                                                          name="searchUser"
                                                          hide="${op == 'delete'}"
                                                          titleKey="Scheduler.grantAccess.searchUser"
                                                          modalTitleKey="Scheduler.grantAccess.searchUser"
                                                          submitOnSelect="true"
                                                          glyphiconClass="glyphicon glyphicon-search"
                                                          tabindex="5"
                                       />
                               <c:if test="${op != 'delete'}">
                                   <html:hidden property="dto(reload)" styleId="reload"/>
                                   <a href="javascript:clear();" tabindex="6" class="${app2:getFormButtonClasses()}">
                                       <span class="glyphicon glyphicon-erase"></span>
                                   </a>
                               </c:if>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%--<c:if test="${op != 'create'}" >--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Admin.CaseEmail"/>
                    </label>

                    <div id="notification" class="${app2:getFormContainClasses(true)}">
                        <html:hidden property="dto(emailNotification)" write="true"/>
                    </div>
                </div>
                    <%--</c:if>--%>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
                <%--<c:if test="${op != 'update'}" >--%>
            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="SUPPORTUSER"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="7">
                ${button}
            </app2:securitySubmit>
                <%--</c:if>--%>
            <c:if test="${op == 'create'}">
                <%--<app2:securitySubmit operation="${op}" functionality="SUPPORTUSER" styleClass="${app2:getFormButtonClasses()}" tabindex="11"  property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>--%>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="8">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="supportUserForm"/>