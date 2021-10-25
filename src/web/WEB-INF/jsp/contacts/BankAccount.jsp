<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>

<script>
    function setId(i) {
        document.getElementById('bankId').value = i;
        document.forms[0].submit();
    }
</script>

<html:form action="${action}" focus="dto(accountNumber)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>
        <html:hidden property="dto(value)" styleId="bankId"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(bankAccountId)"/>
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
                    <label class="${app2:getFormLabelClasses()}" for="accountNumber_id">
                        <fmt:message key="Contact.BankAccount.number"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(accountNumber)" styleId="accountNumber_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="12" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="bankId">
                        <fmt:message key="Contact.BankAccount.bank"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">   <!-- Collection banks-->
                        <div class="col-xs-11 row">
                            <fanta:select property="dto(bankId)" styleId="bankId" listName="bankBaseList"
                                          firstEmpty="true"
                                          labelProperty="nameCode" valueProperty="id" module="/catalogs"
                                          styleClass="${app2:getFormSelectClasses()} largeSelect"
                                          readOnly="${op == 'delete'}" tabIndex="2">
                                <fanta:parameter field="companyId"
                                                 value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                        <span class="pull-right">
                          <app2:checkAccessRight functionality="BANK" permission="CREATE">
                              <tags:bootstrapSelectPopup styleId="Create_id"
                                                         url="/contacts/BankAccount/Bank/Create.do?contactId=${param.contactId}"
                                                         name="Create" titleKey="Common.create"
                                                         hide="${op == 'delete'}"
                                                         submitOnSelect="true"
                                                         tabindex="3"
                                                         glyphiconClass="glyphicon glyphicon-plus"
                                      />
                          </app2:checkAccessRight>
                        </span>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="accountOwner_id">
                        <fmt:message key="Contact.BankAccount.owner"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(accountOwner)" styleId="accountOwner_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="40" tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="defaultBankAccount_id">
                        <fmt:message key="Contact.BankAccount.default"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">   <!-- Bank account default -->
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(defaultBankAccount)" styleId="defaultBankAccount_id"
                                               disabled="${op == 'delete'}"
                                               tabindex="5"/>
                                <label for="defaultBankAccount_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="iban_id">
                        <fmt:message key="Contact.BankAccount.iban"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(iban)" styleId="iban_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText" maxlength="34"
                                  tabindex="6"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>

                    </div>
                </div>

                <c:if test="${sessionScope.user.valueMap['companyId'] eq param.contactId}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="accountId_id">
                            <fmt:message key="Contact.BankAccount.account"/>
                        </label>

                        <div class="${app2:getFormContainClasses(null)}">
                            <fanta:select property="dto(accountId)"
                                          styleId="accountId_id"
                                          listName="accountList"
                                          labelProperty="name"
                                          valueProperty="accountId"
                                          styleClass="${app2:getFormSelectClasses()} largeSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          readOnly="${'delete' == op}"
                                          tabIndex="7">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="description_id">
                        <fmt:message key="Contact.BankAccount.description"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(description)" styleId="description_id" view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} largeText"
                                  maxlength="250"
                                  tabindex="8"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="BANKACCOUNT"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="10">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="BANKACCOUNT"
                                     styleClass="${app2:getFormButtonClasses()}" property="SaveAndNew" tabindex="11">
                    <fmt:message key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="bankAccountForm"/>