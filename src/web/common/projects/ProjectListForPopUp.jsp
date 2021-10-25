<%@ include file="/Includes.jsp" %>
<br>

<script>
    function select(id, name) {
        opener.selectField('projectId_id', id, 'projectName_id', name);
    }
</script>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">

    <tr>
        <td class="title" height="20" colspan="2">
            <fmt:message key="${pageTitle}"/>
        </td>
    </tr>

<tr>
    <td class="label">
        <fmt:message key="Common.search"/>
    </td>
    <html:form action="/Project/ProjectListPopUp.do" focus="parameter(projectName)">
        <td class="contain" nowrap>
            <html:text property="parameter(projectName)" styleClass="largeText"/>
            &nbsp;
            <html:submit styleClass="button">
                <fmt:message key="Common.go"/>
            </html:submit>
        </td>
    </html:form>
</tr>

<tr>
    <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="Project/ProjectListPopUp.do" parameterName="projectNameAlpha"/>
    </td>
</tr>
</table>
<br/>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
<td align="center" width="100%">

<fanta:table align="center" id="project" action="Project/ProjectListPopUp.do" imgPath="${baselayout}">

<fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
    <fanta:actionColumn name="" title="Common.select"
                        useJScript="true"
                        action="javascript:select('${app2:mapElement(project, 'projectId')}', '${app2:jscriptEncode(app2:mapElement(project, 'projectName'))}');"
                        styleClass="listItem" headerStyle="listHeader" width="100%"
                        image="${baselayout}/img/import.gif"/>
</fanta:columnGroup>

    <fanta:dataColumn name="projectName" styleClass="listItem"
                                      title="Project.name" headerStyle="listHeader" width="20%"
                                      orderable="true"/>
    <fanta:dataColumn name="customerName" styleClass="listItem" title="Project.customer"
                      headerStyle="listHeader" width="20%" orderable="true"/>
    <fanta:dataColumn name="userName" styleClass="listItem" title="Project.responsible"
                      headerStyle="listHeader" width="15%" orderable="true"/>
</fanta:table>
</td>
</tr>
</table>