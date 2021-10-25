<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(addressText1)">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <table id="Salutation.jsp" class="${app2:getTableClasesIntoForm()}">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="dto(salutationId)"/>
                    <html:hidden property="dto(salutationLabel)"/>
                    <html:hidden property="dto(letterTextId)"/>
                    <html:hidden property="dto(addressTextId)"/>
                    <html:hidden property="dto(translationStructureSize)"/>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <thead>
                    <tr>
                        <th>
                            <fmt:message key="Salutation.addressText"/>
                        </th>
                        <th>
                            <fmt:message key="Salutation.letterText"/>
                        </th>
                        <th>
                            <fmt:message key="Language"/>
                        </th>
                    </tr>
                    </thead>
                    <c:forEach var="item" items="${dto.translationStructure}" varStatus="index">
                        <tr>
                            <td class="contain">
                                <html:text property="dto(addressText${index.count})"
                                           styleClass="text ${app2:getFormInputClasses()}"
                                           maxlength="250" value="${item.addressText}"/>
                            </td>
                            <td class="contain">
                                <html:text property="dto(letterText${index.count})"
                                           styleClass="text ${app2:getFormInputClasses()}"
                                           maxlength="250" value="${item.letterText}"/>
                            </td>
                            <td class="contain">
                                <html:text property="dto(languageName${index.count})" readonly="true"
                                           styleClass="text ${app2:getFormInputClasses()}" maxlength="250"
                                           value="${item.languageName}"/>
                                <html:hidden property="dto(languageId${index.count})"
                                             value="${item.languageId}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}"><c:out
                    value="${button}"/></html:submit>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>