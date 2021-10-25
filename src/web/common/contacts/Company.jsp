<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd,
                 com.piramide.elwis.utils.CatalogConstants,
                 com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.utils.FinanceConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>

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

<html:form action="${action}" focus="${focus}" enctype="multipart/form-data">

<html:hidden property="dto(op)" value="update"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<html:hidden property="dto(addressId)" value="${param.contactId}"/>
<html:hidden property="dto(version)"/>

<table cellSpacing=0 cellPadding=0 width="40%" border=0 align="center">

<tr>
<td>
<c:choose>
<c:when test="${param['isLogoCompany'] == 'true'}">
    <%--logo section--%>
    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="UIManager.LogoCompany"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="20%" nowrap><fmt:message key="UIManager.Logo"/></td>
            <td class="contain" style="height: 50px">
                <html:hidden property="dto(logoId)"/>
                <c:set var="logoId" value="${companyLogoForm.dtoMap['logoId']}"/>
                <c:if test="${not empty logoId}">
                    <html:img page="/contacts/DownloadAddressImage.do?dto(freeTextId)=${logoId}"
                              border="0"
                              vspace="10"
                              hspace="10"/>
                </c:if>
            </td>
        </tr>
        <TR>
            <TD class="label"><fmt:message key="Common.file"/></TD>
            <TD class="contain">
                <html:file property="imageFile" tabindex="1" styleId="idImgFile"/>
                <br>
                <fmt:message key="Common.fileUpload.info">
                    <fmt:param value="100 KB"/>
                    <fmt:param value="gif, jpeg, png"/>
                </fmt:message>
                <br>
                <fmt:message key="Common.imageHeightWidth.info">
                    <fmt:param value="40"/>
                    <fmt:param value="200"/>
                </fmt:message>
            </TD>
        </TR>
    </table>

</c:when>
<c:otherwise>
    <c:set var="hasAssignedFinanceModule" value="${app2:hasAssignedFinanceModule(pageContext.request)}"/>
    <html:hidden property="dto(hasAssignedFinanceModule)" value="${hasAssignedFinanceModule}"/>

    <table width="100%" border="0" cellpadding="4" cellspacing="0">
        <tr>
            <TD class="button">
                <c:if test="${hasCompanyUpdateAccessRight}">
                    <html:submit styleClass="button" tabindex="12">
                        <c:out value="${save}"/>
                    </html:submit>
                </c:if>

                <%--top links--%>
                &nbsp;
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
                            <app:link action="${editLink}" styleClass="folderTabLink">
                                <img src="<c:out value="${sessionScope.baselayout}"/>/img/edit.gif"
                                     title="<fmt:message    key="Company.editCompanyInfo"/>" border="0"
                                     alt="<fmt:message    key="Company.editCompanyInfo"/>"/>
                                <fmt:message key="Company.editCompanyInfo"/>
                            </app:link>
                        </app2:checkAccessRight>
                </c:if>
            </TD>
        </tr>
    </table>
    <table id="Company.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
           class="container">
        <TR>
            <TD colspan="2" class="title">
                <fmt:message key="Company.preferences.general"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="30%"><fmt:message key="Company.attach"/></TD>
            <TD class="contain" width="70%">
                <html:text property="dto(maxAttachSize)" styleClass="mediumText" maxlength="2" tabindex="2"/>
                &nbsp; (<fmt:message key="Common.megabytes"/>)
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="Company.row"/></TD>
            <TD class="contain">
                <html:text property="dto(rowsPerPage)" styleClass="mediumText" maxlength="2" tabindex="3"/>
            </TD>
        </tr>
        <tr>
            <TD class="label"><fmt:message key="Company.timeOut"/></TD>
            <TD class="contain">
                <html:text property="dto(timeout)" styleClass="mediumText" maxlength="2" tabindex="4"/>
                &nbsp; (<fmt:message key="Common.minutes"/>)
            </TD>
        </tr>
        <tr>
            <TD class="label">
                <fmt:message key="Company.timeZone"/>
            </TD>
            <td class="contain">
                <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
                <html:select property="dto(timeZone)" styleClass="mediumSelect" tabindex="5">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="timeZonesConstants" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <TD class="topLabel"><fmt:message key="Company.routePage"/></TD>
            <TD class="contain">
                <html:textarea property="dto(route)" styleClass="mediumDetailHigh" style="height:110px;width:300px;"
                               tabindex="6"/>
                <tags:selectPopup url="/contacts/RoutePageField.jsp" name="RoutePageFieldHelp"
                                  titleKey="Common.help"
                                  width="400" heigth="300" scrollbars="0" imgPath="/img/help.gif" imgWidth="10"
                                  imgHeight="13"/>
            </TD>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Company.defaultSalutation"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(salutationId)"
                              listName="salutationList"
                              labelProperty="label"
                              valueProperty="id"
                              styleClass="mediumSelect"
                              firstEmpty="true"
                              module="/catalogs"
                              tabIndex="7">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Company.defaultMediaType"/>
            </td>
            <td class="contain">
                <c:set var="defaultMediaTypes" value="${app2:defaultCommunicationTypes(pageContext.request)}"/>
                <html:select property="dto(mediaType)"
                             styleClass="mediumSelect"
                             tabindex="8">
                    <html:option value="">&nbsp;</html:option>
                    <html:options collection="defaultMediaTypes" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <c:if test="${hasAssignedFinanceModule}">
            <tr>
                <td colspan="2" class="title">
                    <fmt:message key="Company.preferences.finance"/>
                </td>
            </tr>
            <tr>
                <TD class="label">
                    <fmt:message key="Company.dayToSendInvoice"/>
                </TD>
                <TD class="contain">
                    <html:text property="dto(invoiceDaysSend)"
                               styleClass="mediumText"
                               maxlength="5"
                               tabindex="9"/>
                </TD>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Company.sequenceRuleForInvoice"/>
                </td>
                <td class="contain">
                    <fanta:select property="dto(sequenceRuleIdForInvoice)"
                                  listName="sequenceRuleList"
                                  labelProperty="label"
                                  valueProperty="numberId"
                                  styleClass="mediumSelect"
                                  firstEmpty="true"
                                  module="/catalogs"
                                  tabIndex="10">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="type" value="${voucherType}"/>
                    </fanta:select>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Company.sequenceRuleForCreditNote"/>
                </td>
                <td class="contain">
                    <fanta:select property="dto(sequenceRuleIdForCreditNote)"
                                  listName="sequenceRuleList"
                                  labelProperty="label"
                                  valueProperty="numberId"
                                  styleClass="mediumSelect"
                                  firstEmpty="true"
                                  module="/catalogs"
                                  tabIndex="11">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="type" value="${voucherType}"/>
                    </fanta:select>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Company.netGross"/>
                </td>
                <td class="contain">
                    <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                    <html:select property="dto(netGross)"
                                 styleClass="mediumSelect"
                                 readonly="false"
                                 tabindex="12">
                        <html:option value=""/>
                        <html:options collection="netGrossOptions"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Company.contractEndReminderEmail"/>
                </td>
                <td class="contain">
                    <html:text property="dto(emailContract)" styleClass="mediumText" maxlength="200" tabindex="12"/>
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="Company.invoiceMailTemplate"/>
                </td>
                <td class="contain">
                    <c:set var="mediatype_HTML" value="<%=CatalogConstants.MediaType.HTML.getConstantAsString()%>"/>
                    <fanta:select property="dto(invoiceMailTemplateId)"
                                  listName="templateList"
                                  module="/catalogs"
                                  labelProperty="description"
                                  valueProperty="id"
                                  firstEmpty="true"
                                  styleClass="mediumSelect"
                                  tabIndex="13">
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        <fanta:parameter field="mediaType" value="${mediatype_HTML}"/>
                    </fanta:select>
                </td>
            </tr>
        </c:if>

    </table>

</c:otherwise>
</c:choose>

<table width="100%" border="0" cellpadding="4" cellspacing="0" align="center">
    <tr>
        <TD class="button">
            <c:choose>
                <c:when test="${param['isLogoCompany'] == 'true'}">
                    <app2:securitySubmit operation="update"
                                         functionality="COMPANYLOGO"
                                         property="dto(save)"
                                         styleClass="button"
                                         tabindex="14">
                        <c:out value="${save}"/>
                    </app2:securitySubmit>
                    <c:if test="${not empty logoId}">
                        <app2:securitySubmit operation="update"
                                             functionality="COMPANYLOGO"
                                             property="dto(logoDelete)"
                                             styleClass="button"
                                             tabindex="15">
                            <fmt:message key="Common.delete"/>
                        </app2:securitySubmit>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <c:if test="${hasCompanyUpdateAccessRight}">
                        <html:submit styleClass="button" tabindex="16">
                            <c:out value="${save}"/>
                        </html:submit>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </TD>
    </tr>
</table>
</html:form>
</td>
</tr>
</table>




