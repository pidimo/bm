<%@include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>

<html:form action="${action}" focus="dto(relatedAddressName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(relationId)"/>
        </c:if>
        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">

            <app2:securitySubmit operation="${op}"
                                 functionality="ADDITIONALADDRESS"
                                 tabindex="20"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}"
                         tabindex="21">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="fieldAddressName_id">
                        <fmt:message key="AddressRelation.relatedAddress"/>
                    </label>

                    <div class="${app2:getFormContainClasses('create' != op)}">
                        <div class=" input-group">
                            <app:text property="dto(relatedAddressName)"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40"
                                      readonly="true"
                                      tabindex="2"
                                      view="${op =='delete' || op == 'update'}"
                                      styleId="fieldAddressName_id"/>
                            <html:hidden property="dto(relatedAddressId)" styleId="fieldAddressId_id"/>
                            <span class="input-group-btn">
                               <tags:bootstrapSelectPopup styleId="ss"
                                                          url="/contacts/SearchAddress.do?allowCreation=2"
                                                          name="searchAddress"
                                                          titleKey="Common.search"
                                                          modalTitleKey="Contact.Title.search"
                                                          hide="${'create' != op}"
                                                          tabindex="3"
                                                          isLargeModal="true"/>

                               <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                               nameFieldId="fieldAddressName_id"
                                                               tabindex="4"
                                                               titleKey="Common.clear"
                                                               hide="${op != 'create'}"/>

                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>

                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="relationTypeId_id">
                        <fmt:message key="AddressRelation.relationType"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <fanta:select property="dto(relationTypeId)"
                                      listName="addressRelationTypeList"
                                      labelProperty="title"
                                      valueProperty="relationTypeId"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      firstEmpty="true"
                                      module="/catalogs"
                                      tabIndex="5"
                                      readOnly="${'delete' == op}"
                                      styleId="relationTypeId_id">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="comment_id">
                        <fmt:message key="AddressRelation.comment"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <html:textarea property="dto(comment)"
                                       styleClass="${app2:getFormSelectClasses()} mediumDetailHigh"
                                       style="height:120px;width:99%;"
                                       tabindex="6"
                                       readonly="${'delete' == op}"
                                       styleId="comment_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="ADDITIONALADDRESS"
                                 tabindex="9"
                                 styleClass="${app2:getFormButtonClasses()}">
                ${button}
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="10">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="addressRelationForm"/>
