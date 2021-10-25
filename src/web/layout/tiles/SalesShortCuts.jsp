<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left" width="90%" class="moduleShortCut" nowrap="nowrap">
            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                &nbsp;|&nbsp; <app:link page="/SalesProcess/List.do" addModuleParams="false">
                <fmt:message key="SalesProcess.Title"/></app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Sale/SingleList.do" addModuleParams="false">
                    <fmt:message key="Sale.shortCut.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/ProductContract/List.do" addModuleParams="false">
                    <fmt:message key="ProductContract.shortCut.title"/>
                </app:link>
            </app2:checkAccessRight>
            &nbsp;|
        </td>

        <%--        for reports--%>
        <td align="right" class="moduleShortCut" width="10%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Report/SalesProcessList.do"
                                       titleKey="SalesProcess.Report.SalesProcessList" functionality="SALESPROCESS"
                                       permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/ProcessActionList.do"
                                       titleKey="SalesProcess.Report.ProcessActionList"
                                       functionality="SALESPROCESSACTION" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/ActionPositionList.do"
                                       titleKey="SalesProcess.Report.ActionPositionList"
                                       functionality="SALESPROCESSPOSITION" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/ContractsOverviewList.do"
                                       titleKey="SalesProcess.Report.ContractsOverviewList"
                                       functionality="PRODUCTCONTRACT" permission="VIEW"/>
                <tags:reportsMenu module="sales"/>
            </tags:pullDownMenu>
        </td>

    </tr>
</table>