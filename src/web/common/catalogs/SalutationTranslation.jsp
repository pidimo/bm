<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>


<html:form action="${action}" focus="dto(addressText1)">
    <table id="Salutation.jsp" border="0" cellpadding="0" cellspacing="0" width="55%" align="center"
           class="container">

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

        <TR>
            <TD class="title" colspan="3">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <tr>
            <TD class="label" width="30%" nowrap>
                <fmt:message key="Salutation.addressText"/>
            </TD>
            <TD class="label" width="30%" nowrap>
                <fmt:message key="Salutation.letterText"/>
            </TD>
            <TD class="label" width="30%" nowrap>
                <fmt:message key="Language"/>
            </TD>
        </tr>
        <c:forEach var="item" items="${dto.translationStructure}" varStatus="index">
            <tr>
                <td class="contain">
                    <html:text property="dto(addressText${index.count})" styleClass="text"
                               maxlength="250" value="${item.addressText}"/>
                </td>
                <td class="contain">
                    <html:text property="dto(letterText${index.count})" styleClass="text"
                               maxlength="250" value="${item.letterText}"/>
                </td>
                <td class="contain">
                    <html:text property="dto(languageName${index.count})" readonly="true"
                               styleClass="text" maxlength="250" value="${item.languageName}"/>
                    <html:hidden property="dto(languageId${index.count})"
                                 value="${item.languageId}"/>
                </td>
            </tr>
        </c:forEach>
        <TR>
            <TD class="button" colspan="3">
                <html:submit styleClass="button"><c:out value="${button}"/></html:submit>
                <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
    </table>
</html:form>
