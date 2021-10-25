<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<html:form action="/Supplier/Update.do" focus="dto(customerNumber)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="update"
                                 functionality="SUPPLIER"
                                 property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="6">
                <c:out value="${save}"/>
            </app2:securitySubmit>
        </div>

        <html:hidden property="dto(supplierId)" value="${param.contactId}"/>
        <html:hidden property="dto(addressId)" value="${param.contactId}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(addressType)" value="${addressType}"/>
        <html:hidden property="dto(name1)" value="${name1}"/>
        <html:hidden property="dto(name2)" value="${name2}"/>
        <html:hidden property="dto(name3)" value="${name3}"/>
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(op)" value="update"/>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="customerNumber_id">
                        <fmt:message key="Supplier.customerNumber"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <html:text property="dto(customerNumber)"
                                   styleId="customerNumber_id"
                                   styleClass="${app2:getFormInputClasses()} mediumText"
                                   maxlength="20"
                                   tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="branchId_id">
                        <fmt:message key="Supplier.branch"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <app:catalogSelect property="dto(branchId)"
                                           styleId="branchId_id"
                                           catalogTable="branch"
                                           idColumn="branchid"
                                           labelColumn="branchname"
                                           styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                           tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="supplierTypeId_id">
                        <fmt:message key="Supplier.type"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <app:catalogSelect property="dto(supplierTypeId)"
                                           styleId="supplierTypeId_id"
                                           catalogTable="suppliertype"
                                           idColumn="suppliertypeid"
                                           labelColumn="suppliertypename"
                                           styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                           tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${sessionScope.user.valueMap['addressType'] == organizationType}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="taxNumber_id">
                            <fmt:message key="Contact.taxNumber"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <html:text property="dto(taxNumber)"
                                       styleId="taxNumber_id"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       maxlength="20"
                                       tabindex="4"
                                       readonly="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="update"
                                 functionality="SUPPLIER"
                                 property="dto(save)"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="5">
                <c:out value="${save}"/>
            </app2:securitySubmit>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="supplierForm"/>
