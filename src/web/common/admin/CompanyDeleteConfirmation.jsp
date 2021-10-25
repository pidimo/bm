<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<html:form action="${action}" styleId="companyFormId">
<html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
<html:hidden property="dto(companyId)"/>
<html:hidden property="dto(name1)"/>
<html:hidden property="dto(name2)"/>
<html:hidden property="dto(name3)"/>

<table cellSpacing=0 cellPadding=0 width="500" border=0 align="center">
<tr>
    <td class="button">
        <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="button" property="dto(save)">
            ${button}
        </app2:securitySubmit>
        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
<tr>
    <td colspan="2" class="title">
        ${title}
    </td>
</tr>
<tr>
<td>
    <fmt:message key="Company.delete.message"/>
</td>
</tr>
<tr>
    <td class="button">
        <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="button" property="dto(save)"
                             indexed="31">
            ${button}
        </app2:securitySubmit>

        <html:cancel styleClass="button">
            <fmt:message key="Common.cancel"/>
        </html:cancel>
    </td>
</tr>
</table>
</html:form>
