<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<calendar:initialize/>
<tags:initBootstrapSelectPopup/>


<html:form action="${action}" focus="dto(productName)" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(productId)" value="${param.productId}"/>

    <c:if test="${'update' == op || 'delete' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(competitorProductId)"/>
    </c:if>


    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="${op}"
                             functionality="COMPETITORPRODUCT"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             property="dto(save)"
                             tabindex="1">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"
                     tabindex="2">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>

            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="productName_id">
                        <fmt:message key="Competitor.product"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:text property="dto(productName)"
                                  styleId="productName_id"
                                  styleClass="mediumText ${app2:getFormInputClasses()}"
                                  maxlength="40"
                                  tabindex="3"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Competitor.entryDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <fmt:message var="datePattern" key="datePattern"/>
                        <jsp:useBean id="now" class="java.util.Date"/>
                        <c:set var="dateValueParse" value="${now}"/>

                        <c:if test="${'create' == op}">
                            <fmt:formatDate var="entryDate" value="${dateValueParse}" pattern="${datePattern}"/>
                            <fmt:formatDate var="changeDate" value="${dateValueParse}" pattern="${datePattern}"/>
                        </c:if>

                        <c:if test="${'create' != op}">
                            <c:set var="parseable" value="${dto.entryDate}"/>
                            <fmt:formatDate var="entryDate" value="${app2:intToDate(parseable)}"
                                            pattern="${datePattern}"/>
                            <fmt:formatDate var="changeDate" value="${dateValueParse}" pattern="${datePattern}"/>
                        </c:if>
                        <c:out value="${entryDate}"/>
                        <html:hidden property="dto(entryDate)"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Competitor.competitorName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <div class="input-group">
                            <app:text property="dto(competitorName)"
                                      styleId="fieldAddressName_id"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="40" tabindex="4"
                                      view="${op == 'delete'}"
                                      readonly="true"/>
                            <html:hidden property="dto(competitorId)" styleId="fieldAddressId_id"/>
                                    <span class="input-group-btn">
                                        <tags:bootstrapSelectPopup
                                                styleId="searchAddress_id"
                                                url="/contacts/SearchAddress.do"
                                                name="searchAddress"
                                                titleKey="Common.search"
                                                modalTitleKey="Contact.Title.search"
                                                tabindex="5"
                                                hide="${op == 'delete'}"
                                                isLargeModal="true"/>
                                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                        nameFieldId="fieldAddressName_id"
                                                                        titleKey="Common.clear"
                                                                        hide="${op == 'delete'}"
                                                                        tabindex="6"/>
                                    </span>
                        </div>
                        <c:set var="path" value="${pageContext.request.contextPath}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Competitor.changeDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <c:out value="${changeDate}"/>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="price_id">
                        <fmt:message key="Competitor.price"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <app:numberText property="dto(price)"
                                        styleClass="mediumText ${app2:getFormInputClasses()}"
                                        styleId="price_id"
                                        tabindex="7"
                                        maxlength="12"
                                        view="${'delete' == op}"
                                        numberType="decimal"
                                        maxFloat="2"
                                        maxInt="7"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="col-xs-12 col-sm-12">
                <div class="form-group">
                    <label class="control-label col-xs-12 col-sm-12 label-left row" for="description_id">
                        <fmt:message key="Competitor.description"/>
                    </label>

                    <div class="col-xs-12 col-sm-12 row">
                        <html:textarea property="dto(description)"
                                       styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                       styleId="description_id"
                                       tabindex="8"
                                       style="height:120px;"
                                       readonly="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">

        <!-- Buttons -->
        <app2:securitySubmit operation="${op}"
                             functionality="COMPETITORPRODUCT"
                             styleClass="button ${app2:getFormButtonClasses()}"
                             property="dto(save)" tabindex="9">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="10"><fmt:message
                key="Common.cancel"/></html:cancel>

    </div>

</html:form>
<tags:jQueryValidation formName="invoiceForm"/>