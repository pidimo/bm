<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">

    <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
        <li>
            <app:link page="/sales/SalesProcess/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="SalesProcess.Title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="SALE" permission="VIEW">
        <li>
            <app:link page="/sales/Sale/SingleList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Sale.shortCut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <li>
            <app:link page="/sales/ProductContract/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="ProductContract.shortCut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/sales/Report/SalesProcessList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.SalesProcessList" functionality="SALESPROCESS"
                                permission="VIEW"/>
        <tags:bootstrapMenuItem action="/sales/Report/ProcessActionList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.ProcessActionList"
                                functionality="SALESPROCESSACTION" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/sales/Report/ActionPositionList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.ActionPositionList"
                                functionality="SALESPROCESSPOSITION" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/sales/Report/ContractsOverviewList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.ContractsOverviewList"
                                functionality="PRODUCTCONTRACT" permission="VIEW"/>

        <tags:bootstrapReportsMenu module="sales"/>
    </tags:bootstrapMenu>
</ul>
