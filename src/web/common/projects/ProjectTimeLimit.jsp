<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="${action}" focus="dto(assigneeId)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(projectId)" value="${param.projectId}"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(timeLimitId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="75%" align="center" class="container">
        <tr>
            <td colspan="4" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" property="save"
                                     styleClass="button" tabindex="13">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" styleClass="button"
                                         property="SaveAndNew" tabindex="14">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="15">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="title" width="100%">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="16%">
                <fmt:message key="ProjectTimeLimit.assignee"/>
            </td>
            <td class="contain" width="34%">
                <fanta:select property="dto(assigneeId)"
                              listName="projectUserList"
                              labelProperty="userName"
                              valueProperty="addressId"
                              styleClass="middleSelect"
                              module="/projects"
                              firstEmpty="true"
                              readOnly="${op == 'delete'}"
                              tabIndex="1">
                    <fanta:parameter field="companyId"
                                     value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="projectId"
                                     value="${param.projectId}"/>
                </fanta:select>
            </td>
            <td class="label" width="16%">
                <fmt:message key="ProjectTimeLimit.hasTimeLimit"/>
            </td>
            <td class="contain" width="34%">
                <html:checkbox property="dto(hasTimeLimit)" disabled="${op == 'delete'}" tabindex="6"
                               styleClass="radio" value="true"/>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="ProjectTimeLimit.subProject"/>
            </td>
            <td class="contain" colspan="3">
                <fanta:select property="dto(subProjectId)"
                              listName="subProjectList"
                              labelProperty="subProjectName"
                              valueProperty="subProjectId"
                              firstEmpty="true"
                              styleClass="middleSelect"
                              module="/projects"
                              readOnly="${'delete' == op}"
                              tabIndex="2">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="projectId" value="${param.projectId}"/>
                </fanta:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="ProjectTimeLimit.invoiceLimit"/>
            </td>
            <td class="contain">
                <app:numberText property="dto(invoiceLimit)" styleClass="numberText" maxlength="6"
                                numberType="decimal" maxInt="6" maxFloat="1"
                                view="${'delete' == op}"
                                tabindex="3"/>
            </td>
            <td class="label">
                <fmt:message key="ProjectTimeLimit.totalInvoiceLimit"/>
            </td>
            <td class="contain">
                <c:set var="totalTimeMap" value="${app2:calculateProjectTimesByAssigneeSubProject(projectTimeLimitForm.dtoMap['projectId'], projectTimeLimitForm.dtoMap['assigneeId'], projectTimeLimitForm.dtoMap['subProjectId'])}"/>

                <fmt:formatNumber var="invoiceTotalFormated" value="${totalTimeMap.totalInvoiceTime}" type="number" pattern="${numberFormat}"/>
                ${invoiceTotalFormated}
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="ProjectTimeLimit.noInvoiceLimit"/>
            </td>
            <td class="contain">
                <app:numberText property="dto(noInvoiceLimit)" styleClass="numberText"
                                maxlength="6" numberType="decimal" maxInt="6" maxFloat="1"
                                view="${'delete' == op}" tabindex="4"/>
            </td>

            <td class="label">
                <fmt:message key="ProjectTimeLimit.totalNoInvoiceLimit"/>
            </td>
            <td class="contain">
                <fmt:formatNumber var="noInvoiceTotalFormated" value="${totalTimeMap.totalNoInvoiceTime}" type="number" pattern="${numberFormat}"/>
                ${noInvoiceTotalFormated}
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" property="save"
                                     styleClass="button" tabindex="10">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="PROJECTTIMELIMIT" styleClass="button"
                                         property="SaveAndNew" tabindex="11">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="12">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>