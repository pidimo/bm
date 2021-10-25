<%@ include file="/Includes.jsp" %>

<html:form action="${action}"
           focus="${'create' == op ? 'dto(languageId)' : 'dto(isDefault)' }"
           enctype="multipart/form-data">

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(reminderLevelId)" value="${param.reminderLevelId}"/>

        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(languageId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <html:hidden property="dto(version)"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="40%"><fmt:message key="ReminderText.language"/></TD>
            <TD class="contain" width="60%">
                <c:choose>
                    <c:when test="${'create' == op}">
                        <fanta:select property="dto(languageId)"
                                      listName="unselectedLanguageListInReminderText"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="select"
                                      tabIndex="1">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="reminderLevelId"
                                             value="${param.reminderLevelId}"/>
                        </fanta:select>
                    </c:when>
                    <c:otherwise>
                        <fanta:select property="dto(languageId)"
                                      listName="languageList"
                                      labelProperty="name"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="select"
                                      tabIndex="1"
                                      readOnly="true">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </c:otherwise>
                </c:choose>
            </TD>
        </TR>
        <tr>
            <TD class="label" width="25%" nowrap>
                <fmt:message key="ReminderText.isDefault"/>
            </TD>
            <TD class="contain" width="75%">
                <html:checkbox property="dto(isDefault)"
                               disabled="${'delete' == op}"
                               value="true"
                               styleClass="radio" tabindex="2"/>
            </TD>
        </tr>
        <c:if test="${'delete' != op}">
            <TR>
                <TD class="label" width="25%" nowrap>
                    <fmt:message key="ReminderText.template"/>
                </TD>
                <TD class="contain" width="75%">
                    <html:file property="dto(file)" accept="application/msword" tabindex="3"/>
                </TD>
            </TR>
        </c:if>
    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="UPDATE">
                    <html:submit styleClass="button" tabindex="3">${button}</html:submit>
                </app2:checkAccessRight>
                <html:cancel styleClass="button" tabindex="4"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
</html:form>
