<%@ include file="/Includes.jsp" %>


<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0" >
<tr>
    <td>

<app2:checkAccessRight functionality="LANGUAGE" permission="CREATE">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <TR>
    <html:form styleId="CREATE_NEW_LANGUAGE" action="/Language/Forward/Create.do">
        <TD class="button">
            <html:submit styleClass="button"><fmt:message    key="Common.new"/></html:submit>
        </TD>
    </html:form>
    </TR>
    </table>
</app2:checkAccessRight>

    <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td>
            <fanta:table list="languageList" width="100%" id="language" action="Language/List.do" imgPath="${baselayout}"  >
                <c:set var="editAction" value="Language/Forward/Update.do?dto(languageId)=${language.id}&dto(languageName)=${app2:encode(language.name)}"/>
                <c:set var="deleteAction" value="Language/Forward/Delete.do?dto(withReferences)=true&dto(languageId)=${language.id}&dto(languageName)=${app2:encode(language.name)}"/>
                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="LANGUAGE" permission="VIEW">
                        <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/edit.gif" />
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="LANGUAGE" permission="DELETE">
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem" headerStyle="listHeader" image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>

                <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Language.name"  headerStyle="listHeader" width="40%" orderable="true" maxLength="25" >
                </fanta:dataColumn>
                <fanta:dataColumn name="iso" styleClass="listItem" title="Language.iso"  headerStyle="listHeader" width="40%" orderable="false" renderData="false"  >
                <c:set var="languageConstant" value="${language.iso}" scope="request"/>

<%
    String constant = (String) request.getAttribute("languageConstant");
    String key = JSPHelper.getSystemLanguage(constant,request);
    request.setAttribute("constantKey",key);
%>
                ${constantKey}&nbsp;

                </fanta:dataColumn>
                <fanta:dataColumn name="" styleClass="listItem2" title="Common.default" headerStyle="listHeader" width="20%" renderData="false" >
                    <c:if test="${language.isDefault == '1'}">
                    <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>&nbsp;
                    </c:if>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
    <tr>
        <td <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
    </td>
</tr>
</table>

