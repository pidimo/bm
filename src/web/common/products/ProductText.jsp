<%@ include file="/Includes.jsp" %>
<table cellpadding="0" cellspacing="0" border="0" align="center" width="60%">
    <tr>
        <td>
            <html:form action="${action}" focusIndex="1">
                <table cellpadding="0" cellspacing="0" border="0" class="container" width="100%" align="center">
                    <tr>
                        <td colspan="3" class="button">
                            <html:submit styleClass="button" tabindex="20">
                                <fmt:message key="Common.save"/>
                            </html:submit>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3" class="title"><fmt:message key="ProductText.title.translate"/></td>
                    </tr>
                    <tr>
                        <td class="label" width="10%"><fmt:message key="ProductText.default"/></td>
                        <td class="label" width="30%"><fmt:message key="ProductText.language"/></td>
                        <td class="label" width="60%"><fmt:message key="ProductText.text"/></td>
                    </tr>

                    <html:hidden property="dto(productId)" styleId="s"/>
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(version)"/>
                    <c:set var="tabIdx" value="${0}"/>
                    <c:forEach var="language"
                               items="${app2:getCompanyLanguages(pageContext.request)}" varStatus="i">
                        <tr>
                            <td class="contain" style="vertical-align: top;" width="20%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <html:radio property="dto(isDefault)"
                                            value="${language.languageId}"
                                            styleClass="radio" tabindex="${tabIdx}"/>
                            </td>
                            <td class="contain" style="vertical-align: top;" width="20%">
                                <html:hidden property="dto(uiLanguages)"
                                             value="${language.languageId}"
                                             styleId="dto(uiLanguages)"/>
                                <html:hidden property="dto(language_${language.languageId})"
                                             value="${language.languageName}"/>
                                <c:out value="${language.languageName}"/>
                            </td>
                            <td class="contain" width="60%">
                                <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                <html:textarea property="dto(text_${language.languageId})"
                                               styleClass="minimumDetail" style="width:250px;" tabindex="${tabIdx}"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="3" class="button">
                            <html:submit styleClass="button" tabindex="${tabIdx+3}">
                                <fmt:message key="Common.save"/>
                            </html:submit>
                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>

