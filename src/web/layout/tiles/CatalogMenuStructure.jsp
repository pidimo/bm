<%@ include file="/Includes.jsp" %>

<jsp:useBean id="configurationMenuMap" class="java.util.LinkedHashMap" scope="request"/>

<%--contacts menu category--%>
<jsp:useBean id="contactTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="SALUTATION" permission="VIEW">
    <c:set target="${contactTabItems}" property="Salutation.plural" value="/Salutation/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="TITLE" permission="VIEW">
    <c:set target="${contactTabItems}" property="Title.plural" value="/Title/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="LANGUAGE" permission="VIEW">
    <c:set target="${contactTabItems}" property="Language.plural" value="/Language/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CURRENCY" permission="VIEW">
    <c:set target="${contactTabItems}" property="Currency.plural" value="/Currency/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="COUNTRY" permission="VIEW">
    <c:set target="${contactTabItems}" property="Country.plural" value="/Country/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CITY" permission="VIEW">
    <c:set target="${contactTabItems}" property="City.plural" value="/City/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="BANK" permission="VIEW">
    <c:set target="${contactTabItems}" property="Bank.plural" value="/Bank/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="COSTCENTERS" permission="VIEW">
    <c:set target="${contactTabItems}" property="CostCenter.plural" value="/CostCenter/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PERSONTYPE" permission="VIEW">
    <c:set target="${contactTabItems}" property="PersonType.plural" value="/PersonType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SUPPLIERTYPE" permission="VIEW">
    <c:set target="${contactTabItems}" property="SupplierType.plural" value="/SupplierType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ADDRESSRELATIONTYPE" permission="VIEW">
    <c:set target="${contactTabItems}" property="AddressRelationType.plural" value="/AddressRelationType/List.do"/>
</app2:checkAccessRight>

<c:set target="${configurationMenuMap}" property="1" value="${contactTabItems}"/>


<%--customer menu category--%>
<jsp:useBean id="customerTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="ADDRESSSOURCE" permission="VIEW">
    <c:set target="${customerTabItems}" property="AddressSource.plural" value="/AddressSource/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="BRANCH" permission="VIEW">
    <c:set target="${customerTabItems}" property="Branch.plural" value="/Branch/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CUSTOMERTYPE" permission="VIEW">
    <c:set target="${customerTabItems}" property="CustomerType.plural" value="/CustomerType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PAYMORALITY" permission="VIEW">
    <c:set target="${customerTabItems}" property="PayMorality.plural" value="/PayMorality/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PRIORITY" permission="VIEW">
    <c:set target="${customerTabItems}" property="Priority.plural" value="/Customer/Priority/List.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="2" value="${customerTabItems}"/>


<%--communication menu category--%>
<jsp:useBean id="communicationTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="TELECOMTYPE" permission="VIEW">
    <c:set target="${communicationTabItems}" property="TelecomType.plural" value="/TelecomType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="TEMPLATE" permission="VIEW">
    <c:set target="${communicationTabItems}" property="Template.plural" value="/Template/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="WEBDOCUMENT" permission="VIEW">
    <c:set target="${communicationTabItems}" property="WebDocument.plural" value="/WebDocument/List.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="3" value="${communicationTabItems}"/>


<%--products menu category--%>
<jsp:useBean id="productTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="PRODUCTTYPE" permission="VIEW">
    <c:set target="${productTabItems}" property="ProductType.plural" value="/ProductType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PRODUCTGROUP" permission="VIEW">
    <c:set target="${productTabItems}" property="ProductGroup.plural" value="/ProductGroup/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PRODUCTUNIT" permission="VIEW">
    <c:set target="${productTabItems}" property="ProductUnit.plural" value="/ProductUnit/List.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="4" value="${productTabItems}"/>


<%--sales menu category--%>
<jsp:useBean id="salesTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="STATUS" permission="VIEW">
    <c:set target="${salesTabItems}" property="SalesProcessStatus.plural" value="/Status/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ACTIONTYPE" permission="VIEW">
    <c:set target="${salesTabItems}" property="ActionType.plural" value="/ActionType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SALESPROCESSPRIORITY" permission="VIEW">
    <c:set target="${salesTabItems}" property="Priority.plural" value="/SProcessPriority/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CONTRACTTYPE" permission="VIEW">
    <c:set target="${salesTabItems}" property="ContractType.plural" value="/ContractType/List.do"/>
</app2:checkAccessRight>

<c:set target="${configurationMenuMap}" property="5" value="${salesTabItems}"/>

<%--scheduler menu category--%>
<jsp:useBean id="schedulerTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="SCHEDULERPRIORITY" permission="VIEW">
    <c:set target="${schedulerTabItems}" property="Priority.plural" value="/Scheduler/Priority/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="APPOINTMENTTYPE" permission="VIEW">
    <c:set target="${schedulerTabItems}" property="Task.appointmentType.plural" value="/AppointmentType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="TASKTYPE" permission="VIEW">
    <c:set target="${schedulerTabItems}" property="Task.taskType.plural" value="/TaskType/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="HOLIDAY" permission="VIEW">
    <c:set target="${schedulerTabItems}" property="holiday.plural" value="/Holiday/List.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="6" value="${schedulerTabItems}"/>

<%--support menu category--%>
<jsp:useBean id="supportTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="SUPPORTCATEGORY" permission="VIEW">
    <c:set target="${supportTabItems}" property="BaseCategory.plural" value="/Support/ArticleCategoryList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SUPPORTPRIORITY" permission="VIEW">
    <c:set target="${supportTabItems}" property="Priority.plural" value="/Support/PriorityList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CASETYPE" permission="VIEW">
    <c:set target="${supportTabItems}" property="CaseType.plural" value="/Support/CaseTypeList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CASESEVERITY" permission="VIEW">
    <c:set target="${supportTabItems}" property="CaseSeverity.plural" value="/Support/CaseSeverityList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="WORKLEVEL" permission="VIEW">
    <c:set target="${supportTabItems}" property="WorkingLevel.plural" value="/Support/WorkLevelList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="STATE" permission="VIEW">
    <c:set target="${supportTabItems}" property="State.plural" value="/Support/StateList.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SUPPORTUSER" permission="VIEW">
    <c:set target="${supportTabItems}" property="SupportUser.plural" value="/Support/SupportUserList.do"/>
</app2:checkAccessRight>

<c:set target="${configurationMenuMap}" property="7" value="${supportTabItems}"/>


<jsp:useBean id="generalTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
    <c:set target="${generalTabItems}" property="CategoryTab.plural" value="/CategoryTab/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CATEGORYGROUP" permission="VIEW">
    <c:set target="${generalTabItems}" property="CategoryGroup.plural" value="/CategoryGroup/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CATEGORY" permission="VIEW">
    <c:set target="${generalTabItems}" property="Category.plural" value="/Category/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SEQUENCERULE" permission="VIEW">
    <c:set target="${generalTabItems}" property="SequenceRule.plural" value="/SequenceRule/List.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="8" value="${generalTabItems}"/>


<jsp:useBean id="campaignTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="VIEW">
    <c:set target="${campaignTabItems}" property="CampaignType.plural" value="/Campaign/CampaignTypeList.do"/>
</app2:checkAccessRight>
<c:set target="${configurationMenuMap}" property="9" value="${campaignTabItems}"/>

<jsp:useBean id="financeTabItems" class="java.util.LinkedHashMap"/>
<app2:checkAccessRight functionality="VAT" permission="VIEW">
    <c:set target="${financeTabItems}" property="Vat.plural" value="/Vat/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PAYCONDITION" permission="VIEW">
    <c:set target="${financeTabItems}" property="PayCondition.plural" value="/PayCondition/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="ACCOUNT" permission="VIEW">
    <c:set target="${financeTabItems}" property="Account.plural" value="/Account/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="INVOICETEMPLATE" permission="VIEW">
    <c:set target="${financeTabItems}" property="InvoiceTemplate.plural" value="/InvoiceTemplate/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
    <c:set target="${financeTabItems}" property="ReminderLevel.plural" value="/ReminderLevel/List.do"/>
</app2:checkAccessRight>
<%--<app2:checkAccessRight functionality="SEQUENCERULE" permission="VIEW">
    <c:set target="${financeTabItems}" property="SequenceRule.plural" value="/SequenceRule/List.do"/>
</app2:checkAccessRight>--%>
<c:set target="${configurationMenuMap}" property="10" value="${financeTabItems}"/>