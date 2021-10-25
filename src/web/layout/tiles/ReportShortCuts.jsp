<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top" align="left" width="30%" class="moduleShortCut">

            <app2:checkAccessRight functionality="REPORT" permission="VIEW">
                &nbsp;
                |&nbsp;
                <app:link page="/Report/List.do" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Common.search"/>
                </app:link>
            </app2:checkAccessRight>
            &nbsp;|
        </td>
    </tr>
</table>
