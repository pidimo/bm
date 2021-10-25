<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">
    <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
        <li>
            <app:link page="/finance/Contract/ToInvoice/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Contract.toInvoice.shortcut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
        <li>
            <app:link page="/finance/Invoice/SingleList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Invoice.shortCut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
        <li>
            <app:link page="/finance/Invoice/ToRemind/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Invoice.toRemind.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
        <li>
            <app:link page="/finance/Invoice/RemindersList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Invoice.reminders.shortcut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
        <li>
            <app:link page="/finance/Invoice/RangePrintList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Invoice.rangePrint.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
        <li>
            <app:link page="/finance/Invoice/Send/ViaEmail/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Invoice.sendViaEmail.shorcut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="CREATE">
        <li>
            <app:link page="/finance/InvoicePayment/Forward/CreateInOneStep.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="InvoicePayment.shortCut.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
        <li>
            <app:link page="/finance/IncomingInvoice/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Finance.incomingInvoice"/>
            </app:link>
        </li>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
        <li>
            <app:link page="/finance/IncomingInvoice/RangePrintList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="IncomingInvoice.rangePrint.title"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/finance/Report/InvoiceList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.InvoiceList"
                                functionality="INVOICE" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/finance/Report/ContractToInvoiceList.do"
                                contextRelative="true"
                                titleKey="SalesProcess.Report.ContractToInvoiceList"
                                functionality="PRODUCTCONTRACT" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/finance/Report/IncomingInvoiceList.do"
                                contextRelative="true"
                                titleKey="Finance.incomingInvoiceList"
                                functionality="INCOMINGINVOICE" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoiceCustomerDataExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoiceCustomerDataExport"
                                functionality="INVOICECUSTOMERDATAEXPORT" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoicePartnerDataExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoicePartnerDataExport"
                                functionality="INVOICEPARTNERDATAEXPORT" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoiceCustomerExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoiceCustomerExport"
                                functionality="INVOICECUSTOMEREXPORT" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoicePartnerExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoicePartnerExport"
                                functionality="INVOICEPARTNEREXPORT" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoiceCustomerPaymentExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoiceCustomerPaymentExport"
                                functionality="INVOICECUSTOMERPAYMENTEXPORT" permission="VIEW"/>

        <tags:bootstrapMenuItem action="/finance/Report/InvoicePartnerPaymentExport.do"
                                contextRelative="true"
                                titleKey="Finance.report.invoicePartnerPaymentExport"
                                functionality="INVOICEPARTNERPAYMENTEXPORT" permission="VIEW"/>

        <tags:bootstrapReportsMenu module="finances" moduleContext="/finance"/>
    </tags:bootstrapMenu>
</ul>
