<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(enabled)">
    <html:hidden property="dto(op)" value="${op}"/>
    <table cellpadding="0" cellspacing="0" border="0" class="container" width="70%" align="center">
        <tr>
            <td class="button">
                <app2:securitySubmit operation="${op}" functionality="APPLICATIONSIGNATURE" styleClass="button">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </td>
        </tr>
        <tr>
            <td>
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tr>
                        <td class="title" colspan="2">
                                ${title}
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="ApplicationSignature.enabled"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(enabled)" value="true" styleClass="radio"/>
                        </td>
                    </tr>
                    <c:forEach var="systemLanguage" items="${applicationSignatureForm.dtoMap['systemLanguages']}">
                        <html:hidden property="dto(languageName_${systemLanguage.key})"
                                     value="${systemLanguage.key}"/>
                        <tr>
                            <td colspan="2">
                                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                                    <tr>
                                        <td class="title">
                                            <fmt:message key="${systemLanguage.value}"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="topLabel">
                                            <fmt:message key="ApplicationSignature.text"/>

                                            <html:textarea property="dto(text_${systemLanguage.key})"
                                                           styleClass="minimumDetail"
                                                           style="width:100%;height: 80px;"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="topLabel">
                                            <fmt:message key="ApplicationSignature.html"/>
                                            <html:textarea property="dto(html_${systemLanguage.key})"
                                                           styleClass="minimumDetail"
                                                           styleId="signature_field_${systemLanguage.key}"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
        <tr>
            <td class="button">
                <app2:securitySubmit operation="${op}" functionality="APPLICATIONSIGNATURE" styleClass="button">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </td>
        </tr>
    </table>
</html:form>
