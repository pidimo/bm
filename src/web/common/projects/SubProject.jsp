<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<html:form action="${action}" focus="dto(name)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(projectId)" value="${param.projectId}"/>

    <c:if test="${'update'== op || 'delete'== op}">
        <html:hidden property="dto(subProjectId)"/>
    </c:if>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTSUBPROJECT" property="save"
                                     styleClass="button" tabindex="5">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY" styleClass="button"
                                         property="SaveAndNew" tabindex="6">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="7">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="title" width="100%">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="30%">
                <fmt:message key="SubProject.name"/>
            </td>
            <td class="contain" width="70%">
                <app:text property="dto(name)"
                          styleClass="middleText"
                          maxlength="40"
                          view="${'delete' == op}"
                          tabindex="1"/>
            </td>
        </tr>

        <c:if test="${op != 'create'}">
            <tr>
                <td class="label">
                    <fmt:message key="SubProject.totalInvoiceTime"/>
                </td>
                <td class="contain">
                    <c:set var="totalTimeMap" value="${app2:calculateProjectTimesBySubProject(subProjectForm.dtoMap['projectId'], subProjectForm.dtoMap['subProjectId'])}"/>

                    <fmt:formatNumber var="invoiceTotalFormated" value="${totalTimeMap.totalInvoiceTime}" type="number" pattern="${numberFormat}"/>
                        ${invoiceTotalFormated}
                </td>
            </tr>
            <tr>
                <td class="label">
                    <fmt:message key="SubProject.totalNoInvoiceTime"/>
                </td>
                <td class="contain">
                    <fmt:formatNumber var="noInvoiceTotalFormated" value="${totalTimeMap.totalNoInvoiceTime}" type="number" pattern="${numberFormat}"/>
                        ${noInvoiceTotalFormated}
                </td>
            </tr>
        </c:if>

        <tr>
            <td colspan="2" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTSUBPROJECT" property="save"
                                     styleClass="button" tabindex="2">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="PROJECTACTIVITY" styleClass="button"
                                         property="SaveAndNew" tabindex="3">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="4">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>

</html:form>

<c:if test="${'update'== op}">
    <table border="0" align="center" width="60%" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <iframe name="frame1"
                        src="<app:url value="SubProject/ProjectTime/List.do?parameter(subProjectId)=${subProjectForm.dtoMap['subProjectId']}"/>"
                        class="Iframe" scrolling="no" frameborder="0" id="iFrameId">
                </iframe>
            </td>
        </tr>
    </table>
</c:if>