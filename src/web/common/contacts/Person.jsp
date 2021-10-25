<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>
<tags:jscript language="JavaScript" src="/js/contacts/userAddressAccessRight.js"/>

<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}" focus="${addressForm.dtoMap['addTelecom'] != null ? 'dto(telecomTypeId)' : 'dto(name1)'}"
           enctype="multipart/form-data">

<html:hidden property="dto(customerConfirmationFlag)" styleId="customerConfirmationFlagId"/>
<html:hidden property="dto(supplierConfirmationFlag)" styleId="supplierConfirmationFlagId"/>
<div id="formPanel">
<table border="0" cellpadding="0" cellspacing="0" width="800" align="center">
    <TR>
        <TD class="button">
            <c:choose>
                <c:when test="${onlyView !=null}">
                    <html:button property="" onclick="window.close();" styleClass="button">
                        <fmt:message key="Common.ok"/>
                    </html:button>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                         styleClass="button" styleId="saveButtonId"
                                         onclick="javascript:fillMultipleSelectValues();">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <c:if test="${op == 'update' && param['dto(onlyViewDetail)'] != 'true'}">
                        <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                            <html:submit property="dto(delete)" styleClass="button"
                                         onclick="javascript:fillMultipleSelectValues();">
                                <fmt:message key="Common.delete"/>
                            </html:submit>
                        </app2:checkAccessRight>
                    </c:if>
                    <c:if test="${op == 'create' || op == 'delete' || param['dto(onlyViewDetail)'] == true}">
                        <c:choose>
                            <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                                <html:button property="" onclick="window.close();" styleClass="button">
                                    <fmt:message key="Common.cancel"/>
                                </html:button>
                            </c:when>
                            <c:otherwise>
                                <html:cancel styleClass="button">
                                    <fmt:message key="Common.cancel"/>
                                </html:cancel>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:otherwise>
            </c:choose>

        </TD>
    </TR>
</table>

<table id="Person.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(addressType)" value="${personType}"/>
<html:hidden property="dto(userTypeValue)" value="0"/>
<c:if test="${op == 'create'}">
    <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
</c:if>
<c:if test="${op == 'update'}">
    <html:hidden property="dto(lastModificationUserId)" value="${sessionScope.user.valueMap['userId']}"/>
    <jsp:useBean id="now" class="java.util.Date"/>
    <html:hidden property="dto(lastUpdateDate)" value="${now}"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(recordUserId)"/>
    <%--if comming from contactperson "edit personal info", save the address to return it as contact person--%>
    <html:hidden property="dto(sourceAddressId)"/>
    <html:hidden property="dto(sourceEmployeeId)"/>
    <html:hidden property="dto(onlyViewDetail)"/>
</c:if>

<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
<c:if test="${'update' == op || op == 'delete'}">
    <html:hidden property="dto(addressId)"/>

</c:if>
<c:if test="${isCreatedByPopup}"> <%--for popup creation--%>
    <html:hidden property="dto(isCreatedByPopup)" value="true"/>
</c:if>
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="Contact.Person.salutation"/>
        /
        <fmt:message key="Contact.Person.title"/>
    </TD>
    <TD class="contain" width="35%">
        <fanta:select property="dto(salutationId)" listName="salutationBaseList" firstEmpty="true" labelProperty="label"
                      valueProperty="id" module="/catalogs" styleClass="halfMiddleTextSelect"
                      readOnly="${op == 'delete'}" tabIndex="1">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
        <c:if test="${op == 'delete'}">
            &nbsp;
        </c:if>
        <fanta:select property="dto(titleId)" listName="titleList" firstEmpty="true" labelProperty="name"
                      valueProperty="id" module="/catalogs" styleClass="halfMiddleTextSelect"
                      readOnly="${op == 'delete'}" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="Contact.Person.searchName"/>
    </TD>
    <TD class="contain" width="35%">
        <app:text property="dto(searchName)" styleClass="mediumText" maxlength="60" tabindex="13"
                  view="${op == 'delete'}"/>
    </TD>
</TR>

<tr>
    <TD class="label">
        <fmt:message key="Contact.Person.lastname"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(name1)" styleClass="middleText" maxlength="60" tabindex="3" view="${op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.keywords"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(keywords)" styleClass="mediumText" maxlength="50" tabindex="14"
                  view="${op == 'delete'}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contact.Person.firstname"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(name2)" styleClass="middleText" maxlength="60" tabindex="4" view="${op == 'delete'}"/>
        <html:hidden property="dto(name3)"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.Person.education"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(education)" styleClass="mediumText" maxlength="40" tabindex="15"
                  view="${op == 'delete'}"/>
    </TD>

</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contact.country"/>
    </TD>
    <TD class="contain">
        <fanta:select property="countryId" listName="countryBasicList" firstEmpty="true" labelProperty="name"
                      valueProperty="id" module="/catalogs" styleClass="middleSelect" tabIndex="5"
                      readOnly="${op == 'delete'}" styleId="countryId">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.language"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(languageId)" listName="languageBaseList" firstEmpty="true" labelProperty="name"
                      valueProperty="id" module="/catalogs" styleClass="mediumSelect" readOnly="${op == 'delete'}"
                      tabIndex="16">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contact.cityZip"/>
    </TD>
    <TD class="contain" nowrap>
        <app2:checkAccessRight functionality="CITY" permission="VIEW" var="hasCityAccess"/>
        <html:hidden property="cityId" styleId="cityId"/>
        <html:hidden property="beforeZip" styleId="beforeZipId"/>
        <app:text property="zip" styleClass="zipText" maxlength="10" titleKey="Contact.zip" tabindex="6"
                  view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="zipId"/>
        &nbsp;
        <app:text property="city" styleClass="cityNameText" maxlength="40" titleKey="Contact.city" tabindex="7"
                  view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="cityNameId"/>
        <c:if test="${op != 'delete' && hasCityAccess}">
            <a href="javascript:openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'empty');"
               tabindex="8">
                <img src="<c:out value="${sessionScope.baselayout}"/>/img/search.gif"
                     alt="<fmt:message   key="City.Title.search"/>" border="0" align="middle" width="18" height="19"/>
            </a>
        </c:if>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.Person.birthday"/>
    </TD>
    <TD class="contain">
        <c:choose>
            <c:when test="${not empty addressForm.dtoMap['dateWithoutYear']}">
                <app:dateText property="dto(birthday)" datePatternKey="withoutYearPattern" view="${'delete' == op}"
                              styleId="validFromId" styleClass="mediumText" maxlength="10" withOutYear="${true}"
                              tabindex="17"/>
            </c:when>
            <c:otherwise>
                <app:dateText property="dto(birthday)" datePatternKey="datePattern" view="${'delete' == op}"
                              styleId="validFromId" styleClass="mediumText" maxlength="10" tabindex="17"/>
            </c:otherwise>
        </c:choose>
        <c:if test="${'delete' != op}">
            <a href="javascript:openCalendarPicker(document.getElementById('validFromId'));"
               title="<fmt:message   key="Calendar.open"/>" tabindex="18">
                <img src="<c:out value="${sessionScope.baselayout}"/>/img/calendarPicker.gif"
                     title="<fmt:message   key="Calendar.open"/>" alt="<fmt:message   key="Calendar.open"/>" border="0"
                     align="middle"/>
            </a>
        </c:if>
    </TD>

</tr>
<TR>
    <TD class="label">
        <fmt:message key="Contact.street"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(street)" styleClass="cityNameText" maxlength="40" titleKey="Contact.street" tabindex="9"
                  view="${op == 'delete'}"/>
        &nbsp;
        <app:text property="dto(houseNumber)" styleClass="zipText" maxlength="10" titleKey="Contact.houseNumber"
                  tabindex="10" view="${op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.type"/>
    </TD>
    <TD class="contain">
        <table border="0" cellspacing="0" cellPadding="0" width="100%">
            <td width="50%" class="contain" style="padding:0;height:0">
                <app2:checkAccessRight functionality="CUSTOMER" permission="CREATE"
                                       var="hasCustomerCreate"/>
                <c:choose>
                    <c:when test="${op == 'update' && addressForm.dtoMap['isCustomer']}">
                        <html:checkbox styleClass="radio" property="dto(isCustomer)"
                                       disabled="${op == 'delete' || !hasCustomerCreate}"
                                       tabindex="19"/>
                        &nbsp;
                        <fmt:message key="Contact.customer"/>
                    </c:when>
                    <c:otherwise>
                        <html:checkbox styleClass="radio" property="dto(isCustomer)" tabindex="19"
                                       disabled="${op == 'delete' || !hasCustomerCreate}"/>
                        &nbsp;
                        <fmt:message key="Contact.customer"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${!hasCustomerCreate && addressForm.dtoMap['isCustomer']}">
                    <html:hidden property="dto(isCustomer)"/>
                </c:if>
            </td>
            <td width="50%" class="contain" style="padding:0;height:0">
                <app2:checkAccessRight functionality="SUPPLIER" permission="CREATE"
                                       var="hasSupplierCreate"/>
                <c:choose>
                    <c:when test="${op == 'update' && addressForm.dtoMap['isSupplier']}">
                        <html:checkbox styleClass="radio" property="dto(isSupplier)"
                                       disabled="${op == 'delete' || !hasSupplierCreate}"
                                       tabindex="20"/>
                        &nbsp;
                        <fmt:message key="Contact.supplier"/>
                    </c:when>
                    <c:otherwise>
                        <html:checkbox styleClass="radio" property="dto(isSupplier)" tabindex="20"
                                       disabled="${op == 'delete' || !hasSupplierCreate}"/>
                        &nbsp;
                        <fmt:message key="Contact.supplier"/>
                    </c:otherwise>
                </c:choose>
                <c:if test="${!hasSupplierCreate && addressForm.dtoMap['isSupplier']}">
                    <html:hidden property="dto(isSupplier)"/>
                </c:if>
            </td>
        </table>
    </TD>
</TR>
<TR>
    <TD class="label">
        <fmt:message key="Contact.additionalAddressLine"/>
    </TD>
    <TD class="contain" nowrap>
        <app:text property="dto(additionalAddressLine)" styleClass="middleText" maxlength="100"
                  tabindex="11" view="${op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.status"/>
    </TD>
    <TD class="contain">
        <table border="0" cellspacing="0" cellPadding="0" width="100%">
            <tr>
                <td width="50%" class="contain" style="padding:0;height:0">
                    <c:if test="${op != 'create'}">
                        <html:checkbox styleClass="radio" property="dto(isActive)" disabled="${op == 'delete'}"
                                       tabindex="21"/>
                        &nbsp;
                        <fmt:message key="Contact.active"/>
                        &nbsp;
                    </c:if>
                </td>
<%--
                <td width="50%" class="contain" style="padding:0;height:0">
                    <c:choose>
                        <c:when test="${op == 'create'}">
                            <html:checkbox styleClass="radio" property="dto(isPrivate)" tabindex="22"/>
                            &nbsp;
                            <fmt:message key="Contact.private"/>
                        </c:when>
                        <c:otherwise>
                            <html:checkbox styleClass="radio" property="dto(isPrivate)" tabindex="22"
                                           disabled="${op == 'delete' || (addressForm.dtoMap['recordUserId'] != sessionScope.user.valueMap['userId'])}"/>
                            &nbsp;
                            <fmt:message key="Contact.private"/>
                        </c:otherwise>
                    </c:choose>
                </td>
--%>
            </tr>
        </table>
    </TD>
</TR>

    <%--Telecoms area--%>
<c:set var="lastTabIndex" value="22" scope="request"/>
<c:set var="isType" value="contact" scope="request"/>
<html:hidden property="initTelecoms"/>
<c:set var="telecomItems" value="${addressForm.telecomMap}" scope="request"/>
<c:import url="/common/contacts/TelecomTile.jsp"/>


<TR>
    <TD colspan="4" class="title">
        <fmt:message key="Contact.additionalInfo"/>
    </TD>
</TR>

<tr>
    <TD class="topLabel" colspan="4" style="height: 150px">
        <html:hidden property="dto(imageId)"/>
        <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
        <c:set var="imageId" value="${addressForm.dtoMap['imageId']}"/>
        <table border="0" cellspacing="0" cellPadding="0" width="100%">
            <tr>
                <c:if test="${imageId != null && imageId != '' && addressForm.dtoMap['imageRemovedFlag']!='true'}">
                    <td class="topLabel" style="border: 0px;" width="135" nowrap>
                        <div id="imageDivId">
                            &nbsp;<br>
                            <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${imageId}" border="0"
                                      width="110px" hspace="0" vspace="0" styleId="imageId"/>
                            <c:if test="${op != 'delete'}">
                                &nbsp;<a href="javascript:hideImageDiv();"><img src="${sessionScope.baselayout}/img/tinydelete.gif" alt="<fmt:message
                                    key="Contact.removePhoto"/>" title="<fmt:message
                                    key="Contact.removePhoto"/>" border="0" align="top"/></a>
                            </c:if>
                        </div>
                    </td>
                </c:if>

                <td class="topLabel" style="border: 0px" width="${imageId != null ? '330':'400'}">
                    <fmt:message key="Contact.notes"/>
                    <br>
                    <html:textarea property="dto(note)" styleClass="mediumDetailHigh"
                                   tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                   readonly="${op == 'delete'}"
                                   style="height:110px;width:${(imageId != null && imageId != '') ? '300px' : '400px'}"/>
                </td>
                <td class="topLabel" style="border: 0px" width="${imageId != null ? '330':'400'}">
                    <fmt:message key="Contact.wayDescription"/>
                    <c:if test="${op == 'update'}">
                        <c:import url="/layout/tiles/map24.jsp"/>
                    </c:if>
                    <br>
                    <html:textarea property="dto(wayDescription)" styleClass="mediumDetailHigh"
                                   tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                   readonly="${op == 'delete'}"
                                   style="height:110px;width:${(imageId != null && imageId != '') ? '300px' : '400px'}"/>
                </td>
            </tr>
        </table>
    </TD>
</tr>

<c:if test="${'delete' != op}">
    <TR>
        <TD class="label">
            <fmt:message key="Contact.photo"/>
        </TD>
        <TD class="contain" colspan="3">
            <html:file property="imageFile" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}" size="50"/>
            <fmt:message key="Common.fileUpload.info">
                <fmt:param value="200 KB"/>
                <fmt:param value="gif, jpeg, png"/>
            </fmt:message>
        </TD>
    </TR>
</c:if>
<tr>
    <td colspan="4">
        <!--added by ivan-->
        <c:set var="elementCounter" value="${app2:incrementByOne(pageContext, 'lastTabIndex')}" scope="request"/>
        <c:set var="downloadAction"
               value="/contacts/MainPage/Person/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}"
               scope="request"/>
        <c:set var="ajaxAction" value="/contacts/MainPageReadSubCategories.do" scope="request"/>
        <c:set var="formName" value="addressForm" scope="request"/>
        <c:set var="table" value="<%=ContactConstants.ADDRESS_CATEGORY%>" scope="request"/>
        <c:set var="secondTable" value="<%=ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY%>" scope="request"/>
        <c:set var="operation" value="${op}" scope="request"/>
        <c:set var="labelWidth" value="15" scope="request"/>
        <c:set var="containWidth" value="85" scope="request"/>
        <c:set var="generalWidth" value="${250}" scope="request"/>
        <c:import url="/common/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </td>
</tr>

<%--data level access rights security--%>
<tr>
    <td colspan="4" class="title">
        <fmt:message key="Contact.accessRight.security"/>
    </td>
</tr>
<tr>
    <td class="label" style="vertical-align:top">
        <fmt:message key="Contact.accessRight.userGroupsWithAccess"/>
    </td>
    <td class="contain" colspan="3">
        <html:hidden property="dto(accessUserGroupIds)" styleId="accessUserGroupIds_Id"/>

        ${app2:buildAddressUserGroupAccessRightValues(addressForm.dtoMap['accessUserGroupIds'], pageContext.request)}
        <c:set var="selectedElms" value="${selectedUserGroupList}"/>
        <c:set var="availableElms" value="${availableUserGroupList}"/>

        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <fmt:message key="common.selected"/>
                    <br>
                    <html:select property="dto(selectedUserGroup)"
                                 styleId="selectedUserGroup_Id"
                                 multiple="true"
                                 styleClass="middleMultipleSelect"
                                 tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                 disabled="${op == 'delete'}">

                        <html:options collection="selectedElms" property="value" labelProperty="label"/>
                    </html:select>
                </td>
                <td>
                    <html:button property="select"
                                 onclick="javascript:moveOptionSelected('availableUserGroup_Id','selectedUserGroup_Id');"
                                 styleClass="adminLeftArrow" titleKey="Common.add" disabled="${op == 'delete'}">&nbsp;
                    </html:button>
                    <br>
                    <br>
                    <html:button property="select"
                                 onclick="javascript:moveOptionSelected('selectedUserGroup_Id','availableUserGroup_Id');"
                                 styleClass="adminRighttArrow" titleKey="Common.delete" disabled="${op == 'delete'}">&nbsp;
                    </html:button>
                </td>
                <td>
                    <fmt:message key="common.available"/>
                    <br>
                    <html:select property="dto(availableUserGroup)"
                                 styleId="availableUserGroup_Id"
                                 multiple="true"
                                 styleClass="middleMultipleSelect"
                                 tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                 disabled="${op == 'delete'}">

                        <html:options collection="availableElms" property="value" labelProperty="label"/>
                    </html:select>
                </td>
            </tr>
        </table>
        <script type="text/javascript">
            fillUserAddressAccessRight();
        </script>
    </td>
</tr>

<%--audit info--%>
<c:if test="${op != 'create'}">
    <c:set var="auditAddressId" value="${addressForm.dtoMap['addressId']}" scope="request"/>
    <c:import url="/common/contacts/AuditInformationFragment.jsp"/>
</c:if>

</table>
<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <c:choose>
                <c:when test="${onlyView !=null}">
                    <html:button property="" onclick="window.close();" styleClass="button">
                        <fmt:message key="Common.ok"/>
                    </html:button>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="CONTACT" property="dto(save)"
                                         styleClass="button"
                                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                         onclick="javascript:fillMultipleSelectValues();">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                    <app2:checkAccessRight functionality="CONTACT" permission="DELETE">
                        <c:if test="${op == 'update' && param['dto(onlyViewDetail)'] != 'true'}">
                            <html:submit property="dto(delete)" styleClass="button"
                                         tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                         onclick="javascript:fillMultipleSelectValues();">
                                <fmt:message key="Common.delete"/>
                            </html:submit>
                        </c:if>
                    </app2:checkAccessRight>
                    <c:if test="${op == 'create' || op == 'delete' || param['dto(onlyViewDetail)'] == true}">
                        <c:choose>
                            <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                                <html:button property="" onclick="window.close();"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                             styleClass="button">
                                    <fmt:message key="Common.cancel"/>
                                </html:button>
                            </c:when>
                            <c:otherwise>
                                <html:cancel styleClass="button"
                                             tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                                    <fmt:message key="Common.cancel"/>
                                </html:cancel>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                </c:otherwise>
            </c:choose>
        </TD>
    </TR>
</table>
<c:if test="${op == 'update' || onlyView}">
    <br>
    <app:jobInformationTableTag tableStyleClass="container" titleKey="Person.jobInformation.summary"
                                addressId="${addressForm.dtoMap['addressId']}" tableWith="800" tableAlign="center"
                                tdTitleStyleClass="summaryTitle" tdStyleClass="label" tdEndStyleClass="label2"
                                enableLinks="${onlyView != null ? false : true}"/>
</c:if>
</div>
<div id="duplicatedListPanel">
    <c:if test="${duplicatedAddressesList != null}">
        <table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
            <TR>
                <TD class="button">
                    <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                        <html:submit property="dto(save)" styleClass="button">
                            <fmt:message key="Common.saveAnyway"/>
                        </html:submit>
                    </app2:checkAccessRight>
                    <html:button property="return" styleClass="button" onclick="showAddressForm();">
                        <fmt:message key="Common.edit"/>
                    </html:button>
                    <c:choose>
                        <c:when test="${isCreatedByPopup}"> <%--creation from popup window--%>
                            <html:button property="" onclick="window.close();" styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:button>
                        </c:when>
                        <c:otherwise>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </c:otherwise>
                    </c:choose>

                    <html:hidden property="confirmDuplicatedCreation" value="true"
                                 styleId="confirmDuplicatedCreationId"/>
                </TD>
            </TR>
        </table>
        <br>
        <c:import url="/common/contacts/DuplicatedAddressesList.jsp"/>
    </c:if>
</div>
</html:form>
</td>
</tr>

</table>
