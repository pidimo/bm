<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/contacts/searchcity.jsp"/>
<c:set var="NORMAL_TYPE" value="<%=ContactConstants.AdditionalAddressType.NORMAL.getConstant()%>"/>
<c:set var="MAIN_TYPE" value="<%=ContactConstants.AdditionalAddressType.MAIN.getConstant()%>"/>

<html:form action="${action}" focus="dto(name)">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(addressId)" value="${param.contactId}"/>

    <c:if test="${'create' == op}">
        <html:hidden property="dto(additionalAddressType)" value="${NORMAL_TYPE}"/>
    </c:if>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(additionalAddressId)"/>
        <html:hidden property="dto(additionalAddressType)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <c:set var="readOnly" value="${op == 'delete'}" scope="request"/>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                     tabindex="20" styleClass="button">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="21">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="AdditionalAddress.name"/>
            </td>
            <td class="contain">
                <c:choose>
                    <c:when test="${MAIN_TYPE eq additionalAddressForm.dtoMap['additionalAddressType']}">
                        <html:hidden property="dto(name)"/>
                        <fmt:message key="AdditionalAddress.item.mainAddress"/>
                    </c:when>
                    <c:otherwise>
                        <app:text property="dto(name)" styleClass="largeText" maxlength="100"
                                  tabindex="1" view="${op == 'delete'}"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td class="label" width="25%">
                <fmt:message key="AdditionalAddress.street"/>
            </td>
            <td class="contain" width="75%">
                <app:text property="dto(street)" styleClass="largeText" maxlength="50" titleKey="Contact.street" tabindex="1"
                          view="${op == 'delete'}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="AdditionalAddress.houseNumber"/>
            </td>
            <td class="contain">
                <app:text property="dto(houseNumber)" styleClass="zipText" maxlength="10" titleKey="Contact.houseNumber"
                          tabindex="2" view="${op == 'delete'}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="AdditionalAddress.additionalAddressLine"/>
            </td>
            <td class="contain">
                <app:text property="dto(additionalAddressLine)" styleClass="largeText" maxlength="100"
                          tabindex="3" view="${op == 'delete'}"/>
            </td>
        </tr>
        <tr>
            <TD class="label">
                <fmt:message key="Contact.country"/>
            </TD>
            <TD class="contain">
                <fanta:select property="countryId" listName="countryBasicList" firstEmpty="true" labelProperty="name"
                              valueProperty="id" module="/catalogs" styleClass="middleSelect" tabIndex="4"
                              readOnly="${op == 'delete'}" styleId="countryId">
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
                <app:text property="zip" styleClass="zipText" maxlength="10" titleKey="Contact.zip" tabindex="5"
                          view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="zipId"/>
                &nbsp;
                <app:text property="city" styleClass="cityNameText" maxlength="40" titleKey="Contact.city" tabindex="6"
                          view="${op == 'delete'}" readonly="${!hasCityAccess}" styleId="cityNameId"/>
                <c:if test="${op != 'delete' && hasCityAccess}">
                    <a href="javascript:openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'empty');"
                       tabindex="7">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/search.gif"
                             alt="<fmt:message   key="City.Title.search"/>" border="0" align="middle" width="18" height="19"/>
                    </a>
                </c:if>
            </TD>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="AdditionalAddress.default"/>
            </td>
            <td class="contain">
                <html:checkbox styleClass="radio" property="dto(isDefault)" disabled="${op == 'delete'}"
                                       tabindex="8"/>
            </td>
        </tr>
        <tr>
            <td class="topLabel" colspan="2">
                <fmt:message key="AdditionalAddress.comment"/>
                <html:textarea property="dto(comment)"
                               styleClass="mediumDetailHigh"
                               style="height:120px;width:99%;"
                               tabindex="9"
                               readonly="${'delete' == op}"/>
            </td>
        </tr>

        <tr>
            <td colspan="2" class="button">
                <app2:securitySubmit operation="${op}" functionality="ADDITIONALADDRESS"
                                     tabindex="10" styleClass="button">
                    ${button}
                </app2:securitySubmit>

                <html:cancel styleClass="button" tabindex="11">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>