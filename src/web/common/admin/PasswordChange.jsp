<%@include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("hourList", JSPHelper.getHours());
    pageContext.setAttribute("minutesList", JSPHelper.getMinutes());
%>

<tags:jscript language="JavaScript" src="/js/admin/admin.jsp"/>
<calendar:initialize/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="dto(description)">

    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${'create' == op}">
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
    </c:if>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(passwordChangeId)"/>
        <html:hidden property="dto(userId)"/>
        <html:hidden property="dto(changeTime)"/>
        <html:hidden property="dto(updateDateTime)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <c:set var="isOldChangeTime" value="${app2:isOldRelatedToCurrentTime(passwordChangeForm.dtoMap['changeTime'], pageContext.request)}" scope="request"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <c:set var="readOnly" value="${op == 'delete' or isOldChangeTime}" scope="request"/>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button">
                <c:if test="${!isOldChangeTime}">
                    <app2:securitySubmit operation="${op}" functionality="PASSWORDCHANGE"
                                         onclick="send(defineRoles, undefineRoles)"
                                         tabindex="10" styleClass="button">
                        ${button}
                    </app2:securitySubmit>
                </c:if>

                <html:cancel styleClass="button" tabindex="11">
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
            <td class="label" width="25%">
                <fmt:message key="PasswordChange.description"/>
            </td>
            <td class="contain" width="75%">
                <app:text property="dto(description)" styleClass="mediumText" maxlength="255" tabindex="1"
                          view="${readOnly}"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="PasswordChange.changeTime"/>
            </td>
            <td class="contain">
                <app:dateText property="dto(changeDate)"
                              styleId="changeDateId"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text"
                              maxlength="10"
                              currentDate="true"
                              view="${readOnly}"
                              tabindex="2"/>

                &nbsp;
                <html:select property="dto(hourChange)" tabindex="3"
                             styleClass="select" readonly="${readOnly}" style="width:43">
                    <html:options collection="hourList" property="value" labelProperty="label"/>
                </html:select>
                :
                <html:select property="dto(minuteChange)" styleClass="select" tabindex="4"
                             readonly="${readOnly}" style="width:43">
                    <html:options collection="minutesList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <table width="100%" border="0" cellpadding="4" cellspacing="0">
                    <tr>
                        <td class="label"><fmt:message key="PasswordChange.role.available"/></td>
                        <td class="label"></td>
                        <td class="label"><fmt:message key="PasswordChange.role.assigned"/></td>
                    </tr>
                    <tr>
                        <td class="contain">
                            <c:if test="${availableRolesList == null}">
                                <c:set var="passChangeRoles"
                                       value="${app2:getPasswordChangeRoles(passwordChangeForm.dtoMap['passwordChangeId'],pageContext.request)}"
                                       scope="request"/>
                                <c:set var="availableRolesList" value="${passChangeRoles.availableRolePassChange}"
                                       scope="request"/>
                                <c:set var="assignedRolesList" value="${passChangeRoles.assignedRolePassChange}"
                                       scope="request"/>
                            </c:if>

                            <html:select property="undefineRoles" styleClass="multipleSelect" tabindex="6" size="20"
                                         multiple="true"
                                         ondblclick="move(this.form.undefineRoles,this.form.defineRoles)"
                                         disabled="${readOnly}">
                                <c:forEach var="item" items="${availableRolesList}">
                                    <html:option value="${item.roleId}">${item.roleName}</html:option>
                                </c:forEach>
                            </html:select>
                        </td>

                        <td class="label">
                            <html:button onclick="move(this.form.undefineRoles, this.form.defineRoles)"
                                         styleClass="button"
                                         property="B1" style="height:20px;width:40px;">
                                >
                            </html:button>
                            <br>
                            <html:button onclick="alls(this.form.undefineRoles, this.form.defineRoles)"
                                         styleClass="button"
                                         property="B11" style="height:20px;width:40px;">
                                >>
                            </html:button>
                            <br>
                            <html:button onclick="move(this.form.defineRoles, this.form.undefineRoles)"
                                         styleClass="button"
                                         property="B2" style="height:20px;width:40px;">
                                <
                            </html:button>
                            <br>
                            <html:button onclick="alls(this.form.defineRoles, this.form.undefineRoles)"
                                         styleClass="button"
                                         property="B22" style="height:20px;width:40px;">
                                <<
                            </html:button>
                        </td>
                        <td class="contain">
                            <html:select property="defineRoles" size="20" multiple="true" styleClass="multipleSelect"
                                         indexed="10" tabindex="7"
                                         disabled="${readOnly}">
                                <c:forEach var="item" items="${assignedRolesList}">
                                    <html:option value="${item.roleId}">${item.roleName}</html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <c:if test="${op != 'create'}">
            <tr>
                <td class="label">
                    <fmt:message key="PasswordChange.createdBy"/>
                </td>
                <td class="contain">
                    <fanta:label listName="userBaseList" module="/admin" patron="0" columnOrder="name">
                        <fanta:parameter field="userId" value="${passwordChangeForm.dtoMap['userId']}"/>
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:label>
                </td>
            </tr>
            <c:if test="${not empty passwordChangeForm.dtoMap['updateDateTime']}">
                <tr>
                    <td class="label">
                        <fmt:message key="PasswordChange.lastModified"/>
                    </td>
                    <td class="contain">
                        <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
                        <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
                        <c:out value="${app2:getDateWithTimeZone(passwordChangeForm.dtoMap['updateDateTime'], timeZone, dateTimePattern)}"/>
                    </td>
                </tr>
            </c:if>
        </c:if>

        <tr>
            <td colspan="2" class="button">
                <c:if test="${!isOldChangeTime}">
                    <app2:securitySubmit operation="${op}" functionality="PASSWORDCHANGE"
                                         onclick="send(defineRoles, undefineRoles)"
                                         tabindex="8" styleClass="button">
                        ${button}
                    </app2:securitySubmit>
                </c:if>

                <html:cancel styleClass="button" tabindex="9">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>