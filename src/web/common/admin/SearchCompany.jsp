<%@ include file="/Includes.jsp" %>
<script>
    function select(id, name) {
        opener.selectField('fieldReportCompany_id', id, 'fieldReportCompanyName_id', name);
    }
</script>

<%--<c:set var="url" value="${param.action==null ? 'Report/SearchCompany.do' : param.action}" scope="page"/>--%>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2">
            <fmt:message key="Admin.Company.Title.search"/>
        </td>
    </tr>


    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="/Report/SearchCompany.do" focus="parameter(contactSearchName)">
            <td class="contain" nowrap>
                <html:text property="parameter(contactSearchName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">

            <fanta:alphabet action="Report/SearchCompany.do" parameterName="name1Alpha"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">

            <fanta:table width="100%" id="company" action="Report/SearchCompany.do" imgPath="${baselayout}"
                         list="lightCompanyList">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${company.companyId}', '${app2:jscriptEncode(company.companyName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="companyName" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
            </fanta:table>
        </td>
    </tr>
</table>