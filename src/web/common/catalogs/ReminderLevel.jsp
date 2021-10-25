<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(name)" enctype="multipart/form-data">
<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(reminderLevelId)"/>
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
    <tr>
        <TD class="label" width="25%" nowrap>
            <fmt:message key="ReminderLevel.name"/>
        </TD>
        <TD class="contain" width="75%">
            <app:text property="dto(name)"
                      styleClass="text"
                      maxlength="99"
                      view="${'delete' == op}"
                      tabindex="1"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%" nowrap>
            <fmt:message key="ReminderLevel.level"/>
        </TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(level)"
                            styleClass="numberText"
                            maxlength="5"
                            numberType="integer"
                            view="${'delete' == op}" tabindex="2"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%" nowrap>
            <fmt:message key="ReminderLevel.numberOfDays"/>
        </TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(numberOfDays)"
                            styleClass="numberText"
                            maxlength="5"
                            numberType="integer"
                            view="${'delete' == op}" tabindex="3"/>
        </TD>
    </tr>
    <tr>
        <TD class="label" width="25%" nowrap>
            <fmt:message key="ReminderLevel.fee"/>
        </TD>
        <TD class="contain" width="75%">
            <app:numberText property="dto(fee)"
                            styleClass="numberText"
                            maxlength="5"
                            numberType="decimal" maxInt="2" maxFloat="2"
                            view="${'delete' == op}" tabindex="4"/>
        </TD>
    </tr>
    <c:if test="${'create' == op}">
        <TR>
            <TD class="label" width="25%" nowrap>
                <fmt:message key="ReminderLevel.template"/>
            </TD>
            <TD class="contain" width="75%">
                <html:file property="dto(file)" accept="application/msword" tabindex="5"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="40%"><fmt:message key="ReminderLevel.language"/></TD>
            <TD class="contain" width="60%">
                <fanta:select property="dto(languageId)"
                              listName="languageList"
                              labelProperty="name"
                              valueProperty="id"
                              firstEmpty="true"
                              styleClass="select"
                              tabIndex="6">
                    <fanta:parameter field="companyId"
                                     value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </TD>
        </TR>
    </c:if>
</table>

<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}"
                                 functionality="REMINDERLEVEL"
                                 styleClass="button"
                                 tabindex="7">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="REMINDERLEVEL"
                                     styleClass="button"
                                     property="SaveAndNew"
                                     tabindex="8">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button" tabindex="9">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
</html:form>

<c:if test="${'update' == op}">
    <c:set var="reminderLevelId" value="${reminderLevelForm.dtoMap['reminderLevelId']}"/>
    <c:set var="name" value="${reminderLevelForm.dtoMap['name']}"/>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
        <tr>
            <td>
                <iframe name="frame1"
                        src="<app:url value="/ReminderText/List.do?reminderLevelId=${reminderLevelId}&dto(reminderLevelId)=${reminderLevelId}&dto(name)=${name}"/>"
                        class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </td>
        </tr>
    </table>
</c:if>
