<%@ include file="/Includes.jsp"%>

<html:form action="${action}" enctype="multipart/form-data">

    <html:hidden property="dto(reportId)" value="${param['reportId']}"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${op=='update' || op=='delete'}">
        <html:hidden property="dto(artifactId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${not empty reportCompanyId}">
        <html:hidden property="dto(companyId)" value="${reportCompanyId}"/>
    </c:if>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">

            <app2:securitySubmit operation="${op}" functionality="JRXMLREPORT"
                                 styleClass="button" tabindex="1">${button}</app2:securitySubmit>

            <html:cancel styleClass="button" tabindex="3"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label" width="20%">
            <fmt:message key="Report.artifact.file"/>
        </TD>
        <TD class="contain">
            <html:hidden property="dto(fileName)" write="${op != 'create'}"/>
            <br/>
            <c:if test="${op != 'delete'}">
                <html:file property="dto(file)" tabindex="5"/>
            </c:if>
        </TD>
    </TR>
</table>

<table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="JRXMLREPORT"
                                 styleClass="button" tabindex="6">${button}</app2:securitySubmit>

            <html:cancel styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
</table>
</html:form>