<%@ include file="/Includes.jsp" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left" width="90%" class="moduleShortCut" nowrap="nowrap">
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Contract/ToInvoice/List.do" addModuleParams="false">
                    <fmt:message key="Contract.toInvoice.shortcut.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Invoice/SingleList.do" addModuleParams="false">
                    <fmt:message key="Invoice.shortCut.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Invoice/ToRemind/List.do" addModuleParams="false">
                    <fmt:message key="Invoice.toRemind.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INVOICEREMINDER" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Invoice/RemindersList.do" addModuleParams="false">
                    <fmt:message key="Invoice.reminders.shortcut.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/Invoice/RangePrintList.do" addModuleParams="false">
                    <fmt:message key="Invoice.rangePrint.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INVOICEPAYMENT" permission="CREATE">
                &nbsp;|&nbsp;
                <app:link page="/InvoicePayment/Forward/CreateInOneStep.do" addModuleParams="false">
                    <fmt:message key="InvoicePayment.shortCut.title"/>
                </app:link>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/IncomingInvoice/List.do" addModuleParams="false">
                    <fmt:message key="Finance.incomingInvoice"/>
                </app:link>
            </app2:checkAccessRight>
            &nbsp;|
        </td>

        <%--        for reports--%>
        <td align="right" class="moduleShortCut" width="10%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Report/InvoiceList.do"
                                       titleKey="SalesProcess.Report.InvoiceList"
                                       functionality="INVOICE" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/ContractToInvoiceList.do"
                                       titleKey="SalesProcess.Report.ContractToInvoiceList"
                                       functionality="PRODUCTCONTRACT" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/IncomingInvoiceList.do"
                                       titleKey="Finance.incomingInvoiceList"
                                       functionality="INCOMINGINVOICE" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoiceCustomerDataExport.do"
                                       titleKey="Finance.report.invoiceCustomerDataExport"
                                       functionality="INVOICECUSTOMERDATAEXPORT" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoicePartnerDataExport.do"
                                       titleKey="Finance.report.invoicePartnerDataExport"
                                       functionality="INVOICEPARTNERDATAEXPORT" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoiceCustomerExport.do"
                                       titleKey="Finance.report.invoiceCustomerExport"
                                       functionality="INVOICECUSTOMEREXPORT" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoicePartnerExport.do"
                                       titleKey="Finance.report.invoicePartnerExport"
                                       functionality="INVOICEPARTNEREXPORT" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoiceCustomerPaymentExport.do"
                                       titleKey="Finance.report.invoiceCustomerPaymentExport"
                                       functionality="INVOICECUSTOMERPAYMENTEXPORT" permission="VIEW"/>

                <tags:pullDownMenuItem action="/Report/InvoicePartnerPaymentExport.do"
                                       titleKey="Finance.report.invoicePartnerPaymentExport"
                                       functionality="INVOICEPARTNERPAYMENTEXPORT" permission="VIEW"/>

                <tags:reportsMenu module="finances"/>
            </tags:pullDownMenu>
        </td>

    </tr>
</table>