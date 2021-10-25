<%@ include file="/Includes.jsp" %>
<html:form action="${action}" focusIndex="1">
    <table border="0" cellpadding="0" cellspacing="1" width="60%" align="center" class="container">
        <html:hidden property="dto(op)" value="update"/>
        <html:hidden property="dto(version)" styleId="s"/>
        <html:hidden property="dto(payConditionId)"/>

        <TR>
            <TD colspan="3" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>

        <tr>
            <td class="label"><fmt:message key="PayConditionText.language"/></td>
            <td class="label"><fmt:message key="PayConditionText.text"/></td>
        </tr>

        <c:set var="tabIndex" value="${1}"/>
        <c:forEach var="language" items="${app2:getCompanyLanguages(pageContext.request)}" varStatus="index">
            <c:set var="tabIndex" value="${tabIndex + index.count}"/>
            <tr>
                <td class="contain" style="vertical-align: top;" width="20%">
                    <html:hidden property="dto(uiLanguages)"
                                 value="${language.languageId}"
                                 styleId="dto(uiLanguages)"/>
                    <html:hidden property="dto(language_${language.languageId})"
                                 value="${language.languageName}"/>
                    <c:out value="${language.languageName}"/>
                </td>
                <td class="contain" width="60%">
                    <html:textarea property="dto(text_${language.languageId})"
                                   styleClass="minimumDetail"
                                   style="width:250px;"
                                   tabindex="${tabIndex}"/>
                </td>
            </tr>
        </c:forEach>

    </table>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <html:submit styleClass="button" tabindex="${tabIndex+1}"><c:out value="${button}"/></html:submit>
                <html:cancel styleClass="button" tabindex="${tabIndex+2}"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
</html:form>
