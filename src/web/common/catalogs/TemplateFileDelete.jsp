<%@ include file="/Includes.jsp" %>

<html:form action="/TemplateFile/Delete.do">
    <html:hidden property="dto(op)" value="deleteFile"/>
    <html:hidden property="dto(templateId)" value="${templateForm.dtoMap['templateId']}" />
    <html:hidden property="dto(maxAttachSize)" value="${sessionScope.user.valueMap['maxAttachSize']}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(languageId)" value="${templateForm.dtoMap['languageId']}"/>
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
        <TD colspan="4" class="title">
        <c:out value="${title}"/>
        </TD>
    </TR>
    <tr>
        <TD class="label" width="40%"><fmt:message key="Template.description"/></TD>
        <TD colspan="2" class="contain" width="60%">${dto.templateDescription}</td>
    </tr>
    <tr>
        <TD class="label" width="40%"><fmt:message   key="Template.language"/></TD>
        <TD colspan="2" class="contain" width="60%">${dto.languageName}</td>
    </tr>
    <tr>
        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <html:submit property="dto(save)" styleClass="button" ><c:out value="${button}"/></html:submit>
            <html:submit property="dto(cancel)" styleClass="button" ><fmt:message   key="Common.cancel"/></html:submit>
        </TD>
    </TR>
    </table>
</html:form>
