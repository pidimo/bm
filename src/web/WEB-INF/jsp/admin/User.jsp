<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd,
                 com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.web.admin.session.User,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("languageList", JSPHelper.getLanguageList(request));
%>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<%
    boolean errorPage = false;
    //User name
    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
    addressCmd.putParam("addressId", ((User) session.getAttribute("user")).getValue("userAddressId"));
    request.setAttribute("addressId", ((User) session.getAttribute("user")).getValue("userAddressId"));

    try {
        //user name
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


<c:set var="path" value="${pageContext.request.contextPath}"/>
<div class="${app2:getFormClasses()}">
    <html:form action="/User/Save.do" focus="dto(maxRecentList)" styleClass="form-horizontal">
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(opPreference)" value="preferences"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS"
                                 styleClass="button ${app2:getFormButtonClasses()}">${save}</app2:securitySubmit>
                <%--<html:submit property="dto(save)" styleClass="button" ><c:out value="${save}"/></html:submit>--%>

                <%--top links--%>
            <c:if test="${!errorPage}">
                <%-- Edit personal info link--%>
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
                    <app:link action="${editLink}" styleClass="btn btn-link">
                        <span title="<fmt:message    key="ContactPerson.personalInfo"/>"
                              class="glyphicon glyphicon-edit"></span>
                        <fmt:message key="ContactPerson.personalInfo"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>
        </div>

        <div id="User.jsp" class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.language"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:select property="dto(favoriteLanguage)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 tabindex="1">
                        <html:options collection="languageList" property="value" labelProperty="label"/>
                    </html:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>

            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.login"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                    <html:hidden property="dto(userLogin)" write="true"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.maxRecentes"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="dto(maxRecentList)"
                               styleClass="mediumText ${app2:getFormInputClasses()}"
                               maxlength="2" tabindex="2"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.rows"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="dto(rowsPerPage)"
                               styleClass="mediumText ${app2:getFormInputClasses()}"
                               maxlength="2" tabindex="3"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}" v>
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.timeout"/>
                    (<fmt:message key="Common.minutes"/>)
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="dto(timeout)"
                               styleClass="mediumText ${app2:getFormInputClasses()}"
                               maxlength="4"
                               tabindex="4"/>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.timeZone"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
                    <html:select property="dto(timeZone)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 tabindex="5">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="timeZonesConstants" property="value" labelProperty="label"/>
                    </html:select>
                    <span class="glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE"
                                 functionality="USERSETTINGS"
                                 styleClass="button ${app2:getFormButtonClasses()}">${save}</app2:securitySubmit>
                <%--<html:submit property="dto(save)" styleClass="button" tabindex="8" ><c:out value="${save}"/></html:submit>--%>
        </div>
    </html:form>
    <tags:jQueryValidation formName="userForm"/>
</div>
