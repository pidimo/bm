<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd,
                 com.piramide.elwis.utils.CatalogConstants,
                 com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.FinanceConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="voucherType"><%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>
</c:set>
<c:if test="${empty hasCompanyUpdateAccessRight}">
    <c:set var="hasCompanyUpdateAccessRight"
           value="${app2:hasAccessRight(pageContext.request,'COMPANYINFO' ,'UPDATE')}" scope="request"/>
</c:if>
<%
    boolean errorPage = false;
    //company name
    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
    addressCmd.putParam("addressId", request.getParameter("contactId"));
    request.setAttribute("addressId", request.getParameter("contactId"));

    try {
        //company name
        ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
        request.setAttribute("addressType", resultDTO.get("addressType"));
        request.setAttribute("name1", resultDTO.get("name1"));
        request.setAttribute("name2", resultDTO.get("name2"));
        request.setAttribute("name3", resultDTO.get("name3"));

    } catch (Exception e) {
        errorPage = true;
    }
    request.setAttribute("errorPage", new Boolean(errorPage));
%>

<c:choose>
    <c:when test="${param['isLogoCompany'] == 'true'}">
        <c:set var="action" value="/Company/LogoUpdate.do"/>
        <c:set var="focus" value="idImgFile"/>
    </c:when>
    <c:otherwise>
        <c:set var="action" value="/Company/Update.do"/>
        <c:set var="focus" value="dto(maxAttachSize)"/>
    </c:otherwise>
</c:choose>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="${focus}" enctype="multipart/form-data" styleClass="form-horizontal">
        <div class="col-lg-10 col-lg-offset-1">
            <html:hidden property="dto(op)" value="update"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <html:hidden property="dto(addressId)" value="${param.contactId}"/>
            <html:hidden property="dto(version)"/>

            <c:choose>
                <c:when test="${param['isLogoCompany'] == 'true'}">
                    <%--logo section--%>
                    <div class="${app2:getFormPanelClasses()}">
                        <fieldset>
                            <legend class="title">
                                <fmt:message key="UIManager.LogoCompany"/>
                            </legend>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}">
                                    <fmt:message key="UIManager.Logo"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(true)}">
                                    <html:hidden property="dto(logoId)"/>
                                    <c:set var="logoId" value="${companyLogoForm.dtoMap['logoId']}"/>
                                    <c:if test="${not empty logoId}">
                                        <html:img page="/contacts/DownloadAddressImage.do?dto(freeTextId)=${logoId}"
                                                  border="0"
                                                  styleClass="img-responsive img-rounded"
                                                  vspace="10"
                                                  hspace="10"/>
                                    </c:if>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="idImgFile">
                                    <fmt:message key="Common.file"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(false)}">
                                    <tags:bootstrapFile property="imageFile"
                                                        glyphiconClass="glyphicon-picture"
                                                        tabIndex="1"
                                                        styleId="idImgFile"/>
                                    <fmt:message key="Common.fileUpload.info">
                                        <fmt:param value="100 KB"/>
                                        <fmt:param value="gif, jpeg, png"/>
                                    </fmt:message>
                                    <fmt:message key="Common.imageHeightWidth.info">
                                        <fmt:param value="40"/>
                                        <fmt:param value="200"/>
                                    </fmt:message>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:set var="hasAssignedFinanceModule"
                           value="${app2:hasAssignedFinanceModule(pageContext.request)}"/>
                    <html:hidden property="dto(hasAssignedFinanceModule)" value="${hasAssignedFinanceModule}"/>
                    <div class="${app2:getFormButtonWrapperClasses()}">
                        <c:if test="${hasCompanyUpdateAccessRight}">
                            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="17">
                                <c:out value="${save}"/>
                            </html:submit>
                        </c:if>
                            <%--top links--%>
                        <c:if test="${!errorPage && param.flagCompany != null}">
                            <%-- Edit company info link--%>
                            <c:choose>
                                <c:when test="${addressType == personType}">
                                    <c:set var="editLink"
                                           value="contacts/Person/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="editLink"
                                           value="contacts/Organization/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
                                </c:otherwise>
                            </c:choose>
                            <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                <app:link action="${editLink}" styleClass="folderTabLink btn btn-link">
                                    <span class="glyphicon glyphicon-edit"></span>
                                    <fmt:message key="Company.editCompanyInfo"/>
                                </app:link>
                            </app2:checkAccessRight>
                        </c:if>
                    </div>
                    <div class="${app2:getFormPanelClasses()}">
                        <fieldset id="Company.jsp">
                            <legend class="title">
                                <fmt:message key="Company.preferences.general"/>
                            </legend>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="maxAttachSize_id">
                                    <fmt:message key="Company.attach"/>
                                    (<fmt:message key="Common.megabytes"/>)
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <html:text property="dto(maxAttachSize)"
                                               styleId="maxAttachSize_id"
                                               styleClass="mediumText ${app2:getFormInputClasses()}"
                                               maxlength="2" tabindex="2"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="rowsPerPage_id">
                                    <fmt:message key="Company.row"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <html:text property="dto(rowsPerPage)"
                                               styleId="rowsPerPage_id"
                                               styleClass="mediumText ${app2:getFormInputClasses()}"
                                               maxlength="2" tabindex="3"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="timeout_id">
                                    <fmt:message key="Company.timeOut"/>
                                    (<fmt:message key="Common.minutes"/>)
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <html:text property="dto(timeout)"
                                               styleId="timeout_id"
                                               styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="2"
                                               tabindex="4"/>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="timeZone_id">
                                    <fmt:message key="Company.timeZone"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
                                    <html:select property="dto(timeZone)"
                                                 styleId="timeZone_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="5">
                                        <html:option value="">&nbsp;</html:option>
                                        <html:options collection="timeZonesConstants" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="route_id">
                                    <fmt:message key="Company.routePage"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">

                                    <div class="">
                                        <div class="row col-xs-11">
                                            <html:textarea property="dto(route)"
                                                           styleId="route_id"
                                                           styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                                           tabindex="6" rows="4"/>
                                        </div>
                                        <span class="pull-right">
                                            <tags:bootstrapSelectPopup styleId="RoutePageFieldHelp_id"
                                                                       url="/contacts/RoutePageField.jsp"
                                                                       name="RoutePageFieldHelp"
                                                                       modalTitleKey="Company.routePage.allowedFields"
                                                                       glyphiconClass="glyphicon-question-sign"
                                                                       titleKey="Common.help"/>
                                        </span>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="salutationId_id">
                                    <fmt:message key="Company.defaultSalutation"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <fanta:select property="dto(salutationId)"
                                                  styleId="salutationId_id"
                                                  listName="salutationList"
                                                  labelProperty="label"
                                                  valueProperty="id"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  firstEmpty="true"
                                                  module="/catalogs"
                                                  tabIndex="7">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                            <div class="${app2:getFormGroupClasses()}">
                                <label class="${app2:getFormLabelRenderCategory()}" for="mediaType_id">
                                    <fmt:message key="Company.defaultMediaType"/>
                                </label>

                                <div class="${app2:getFormContainRenderCategory(null)}">
                                    <c:set var="defaultMediaTypes"
                                           value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                                    <html:select property="dto(mediaType)"
                                                 styleId="mediaType_id"
                                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                 tabindex="8">
                                        <html:option value="">&nbsp;</html:option>
                                        <html:options collection="defaultMediaTypes" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </fieldset>
                        <fieldset>
                            <c:if test="${hasAssignedFinanceModule}">
                                <legend class="title">
                                    <fmt:message key="Company.preferences.finance"/>
                                </legend>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="vatId">
                                        <fmt:message key="Company.vatid"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <html:text property="dto(vatId)"
                                                   styleId="vatId"
                                                   styleClass="mediumText ${app2:getFormInputClasses()}"
                                                   maxlength="40"
                                                   tabindex="9"/>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="invoiceDaysSend_id">
                                        <fmt:message key="Company.dayToSendInvoice"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <html:text property="dto(invoiceDaysSend)"
                                                   styleId="invoiceDaysSend_id"
                                                   styleClass="mediumText ${app2:getFormInputClasses()}"
                                                   maxlength="5"
                                                   tabindex="10"/>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="sequenceRuleIdForInvoice_id">
                                        <fmt:message key="Company.sequenceRuleForInvoice"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <fanta:select property="dto(sequenceRuleIdForInvoice)"
                                                      styleId="sequenceRuleIdForInvoice_id"
                                                      listName="sequenceRuleList"
                                                      labelProperty="label"
                                                      valueProperty="numberId"
                                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                      firstEmpty="true"
                                                      module="/catalogs"
                                                      tabIndex="11">
                                            <fanta:parameter field="companyId"
                                                             value="${sessionScope.user.valueMap['companyId']}"/>
                                            <fanta:parameter field="type" value="${voucherType}"/>
                                        </fanta:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="sequenceRuleIdForCreditNote_id">
                                        <fmt:message key="Company.sequenceRuleForCreditNote"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <fanta:select property="dto(sequenceRuleIdForCreditNote)"
                                                      styleId="sequenceRuleIdForCreditNote_id"
                                                      listName="sequenceRuleList"
                                                      labelProperty="label"
                                                      valueProperty="numberId"
                                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                      firstEmpty="true"
                                                      module="/catalogs"
                                                      tabIndex="12">
                                            <fanta:parameter field="companyId"
                                                             value="${sessionScope.user.valueMap['companyId']}"/>
                                            <fanta:parameter field="type" value="${voucherType}"/>
                                        </fanta:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="netGross_id">
                                        <fmt:message key="Company.netGross"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <c:set var="netGrossOptions"
                                               value="${app2:getNetGrossOptions(pageContext.request)}"/>
                                        <html:select property="dto(netGross)"
                                                     styleId="netGross_id"
                                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                     readonly="false"
                                                     tabindex="13">
                                            <html:option value=""/>
                                            <html:options collection="netGrossOptions"
                                                          property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="emailContract_id">
                                        <fmt:message key="Company.contractEndReminderEmail"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <html:text property="dto(emailContract)"
                                                   styleId="emailContract_id"
                                                   styleClass="mediumText ${app2:getFormInputClasses()}"
                                                   maxlength="200"
                                                   tabindex="14"/>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="invoiceMailTemplateId_id">
                                        <fmt:message key="Company.invoiceMailTemplate"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <c:set var="mediatype_HTML"
                                               value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
                                        <fanta:select property="dto(invoiceMailTemplateId)"
                                                      styleId="invoiceMailTemplateId_id"
                                                      listName="templateList"
                                                      module="/catalogs"
                                                      labelProperty="description"
                                                      valueProperty="id"
                                                      firstEmpty="true"
                                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                      tabIndex="14">
                                            <fanta:parameter field="companyId"
                                                             value="${sessionScope.user.valueMap['companyId']}"/>
                                            <fanta:parameter field="mediaType" value="${mediatype_HTML}"/>
                                        </fanta:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                
                                <div class="${app2:getFormGroupClasses()}">
                                    <label class="${app2:getFormLabelRenderCategory()}" for="xinvoiceMailTemplateId_id">
                                        <fmt:message key="Company.xinvoiceMailTemplate"/>
                                    </label>

                                    <div class="${app2:getFormContainRenderCategory(null)}">
                                        <c:set var="mediatype_HTML"
                                               value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
                                        <fanta:select property="dto(xinvoiceMailTemplateId)"
                                                      styleId="xinvoiceMailTemplateId_id"
                                                      listName="templateList"
                                                      module="/catalogs"
                                                      labelProperty="description"
                                                      valueProperty="id"
                                                      firstEmpty="true"
                                                      styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                      tabIndex="14">
                                            <fanta:parameter field="companyId"
                                                             value="${sessionScope.user.valueMap['companyId']}"/>
                                            <fanta:parameter field="mediaType" value="${mediatype_HTML}"/>
                                        </fanta:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                                
                            </c:if>
                        </fieldset>
                    </div>
                </c:otherwise>
            </c:choose>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <c:choose>
                    <c:when test="${param['isLogoCompany'] == 'true'}">
                        <app2:securitySubmit operation="update"
                                             functionality="COMPANYLOGO"
                                             property="dto(save)"
                                             styleClass="button ${app2:getFormButtonClasses()}"
                                             tabindex="15">
                            <c:out value="${save}"/>
                        </app2:securitySubmit>
                        <c:if test="${not empty logoId}">
                            <app2:securitySubmit operation="update"
                                                 functionality="COMPANYLOGO"
                                                 property="dto(logoDelete)"
                                                 styleClass="button ${app2:getFormButtonClasses()}"
                                                 tabindex="16">
                                <fmt:message key="Common.delete"/>
                            </app2:securitySubmit>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${hasCompanyUpdateAccessRight}">
                            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="16">
                                <c:out value="${save}"/>
                            </html:submit>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="companyForm" isValidate="true"/>



