<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd,
                 com.piramide.elwis.utils.CodeUtil,
                 com.piramide.elwis.utils.Constants,
                 com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.web.admin.session.User,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<%
    boolean errorPage = false;
    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
    addressCmd.putParam("addressId", request.getParameter("contactId"));
    try {
        ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
        request.setAttribute("addressType", resultDTO.get("addressType"));
        request.setAttribute("city", resultDTO.get("city"));
        request.setAttribute("countryCode", resultDTO.get("countryCode"));
        request.setAttribute("code", resultDTO.get("code"));
        request.setAttribute("name1", resultDTO.get("name1"));
        request.setAttribute("name2", resultDTO.get("name2"));
        request.setAttribute("name3", resultDTO.get("name3"));
        request.setAttribute("addressName", resultDTO.get("addressName"));
    } catch (Exception e) {
        errorPage = true;
    }
    request.setAttribute("errorPage", new Boolean(errorPage));
%>

<c:if test="${!errorPage}">

<c:choose>
    <c:when test="${addressType == personType}">
        <c:set var="tabHeaderLabel" value="Contact.person" scope="request"/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${param.contactId == sessionScope.user.valueMap['companyId']}">
                <c:set var="tabHeaderLabel" value="Common.company" scope="request"/>
            </c:when>
            <c:otherwise>
                <c:set var="tabHeaderLabel" value="Contact.organization" scope="request"/>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<c:set var="tabHeaderValue" scope="request">
    <c:choose>
        <c:when test="${addressType == personType }">${app2:filterForHtml(name1)}
            <c:if test="${name2 != null}">, ${app2:filterForHtml(name2)}</c:if>
        </c:when>
        <c:otherwise>${app2:filterForHtml(name1)}&nbsp;${app2:filterForHtml(name2)}</c:otherwise>
    </c:choose>
    <c:if test="${countryCode!= null}">/&nbsp;${countryCode}</c:if>
    <c:if test="${city != null && countryCode != null}"> - ${app2:filterForHtml(city)}</c:if>
    <c:if test="${city != null && countryCode == null}"> / ${app2:filterForHtml(city)}</c:if>
</c:set>
<c:import url="${layout}/TabHeader.jsp"/>

<%
    String code = request.getAttribute("code").toString();
    String companyId = ((User) request.getSession().getAttribute(Constants.USER_KEY)).getValue("companyId").toString();
    String addressId = request.getParameter("contactId");
%>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

<c:set var="isSupplierAndCustomer" value="${app2:isCustomer(code) && app2:isSupplier(code)}"/>
<c:choose>
    <c:when test="${addressType == personType}">


        <%--setting the default person tabs--%>
        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.detail" value="/Person/Forward/Update.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
            <%
                List personParamsList = new ArrayList();
                personParamsList.add(Integer.valueOf(addressId));
                request.setAttribute("personParamsList", personParamsList);
            %>
            <c:set var="categoryTabs"
                   value="${app2:getCategoryTabs('4', 'findValueByAddressId', personParamsList, pageContext.request)}"/>
            <c:forEach var="tab" items="${categoryTabs}">
                <c:set target="${tabItems}" property="001@100-${tab.label}"
                       value="/CategoryTab/Forward/Update.do?categoryTabId=${tab.categoryTabId}&dto(categoryTabId)=${tab.categoryTabId}"/>
            </c:forEach>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.contactPersons"
                   value="/ContactPerson/Forward/List.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.communications"
                   value="/Communication/List.do?forceSimple=true"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.additionalAddress"
                   value="/AdditionalAddress/List.do"/>
        </app2:checkAccessRight>
        <c:if test="${app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="CUSTOMER" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.customerInfo"
                       value="/Customer/Forward/Update.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.salesProcess" value="/SalesProcess/List.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALE" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.sale" value="/Sale/SingleList.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
                <c:set target="${tabItems}" property="SalePosition.tab.title" value="/SalePosition/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <c:if test="${isSupplierAndCustomer || app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.productContract"
                       value="/ProductContract/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <c:if test="${app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                <c:set target="${tabItems}" property="Invoice.tab.title" value="/Invoice/SingleList.do"/>
            </app2:checkAccessRight>
        </c:if>
        <app2:checkAccessRight functionality="TASK" permission="VIEW">
            <c:set target="${tabItems}" property="Scheduler.Tasks" value="/TaskList.do?simple=true"/>
        </app2:checkAccessRight>

        <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
            <c:set target="${tabItems}" property="Scheduler.Appointments" value="/AppointmentList.do"/>
        </app2:checkAccessRight>

        <c:if test="${app2:isSupplier(code)}">
            <app2:checkAccessRight functionality="SUPPLIER" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.supplierInfo"
                       value="/Supplier/Forward/Update.do"/>
            </app2:checkAccessRight>
            <c:if test="${!isSupplierAndCustomer}">
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                    <c:set target="${tabItems}" property="Contact.Tab.productContract"
                           value="/ProductContract/List.do"/>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.supplierProduct"
                       value="/SupplierProduct/List.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.incomingInvoiceList"
                       value="/IncomingInvoice/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <app2:checkAccessRight functionality="BANKACCOUNT" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.bankAccounts" value="/BankAccount/List.do"/>
        </app2:checkAccessRight>
        <%
            if (CodeUtil.isEmployee(code)) { // if employee
        %>
        <app2:checkAccessRight functionality="EMPLOYEE" permission="VIEW">
            <c:set target="${tabItems}" property="Contact.Tab.employeeInfo"
                   value="/Employee/Forward/UpdateInfo.do"/>
        </app2:checkAccessRight>
        <%
            }
        %>
    </c:when>
    <c:otherwise> <%--if organization--%>
        <%--setting the default organization tabs--%>
        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.detail" value="/Organization/Forward/Update.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
            <%
                List organizationParams = new ArrayList();
                organizationParams.add(Integer.valueOf(addressId));
                request.setAttribute("organizationParams", organizationParams);
            %>
            <c:set var="categoryTabs"
                   value="${app2:getCategoryTabs('4','findValueByAddressId',organizationParams,pageContext.request)}"/>
            <c:forEach var="tab" items="${categoryTabs}">
                <c:set target="${tabItems}" property="001@100-${tab.label}"
                       value="/CategoryTab/Forward/Update.do?categoryTabId=${tab.categoryTabId}&dto(categoryTabId)=${tab.categoryTabId}"/>
            </c:forEach>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.contactPersons" value="/ContactPerson/Forward/List.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.communications"
                   value="/Communication/List.do?forceSimple=true"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.additionalAddress"
                   value="/AdditionalAddress/List.do"/>
        </app2:checkAccessRight>
        <c:if test="${app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="CUSTOMER" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.customerInfo" value="/Customer/Forward/Update.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.salesProcess" value="/SalesProcess/List.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALE" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.sale" value="/Sale/SingleList.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
                <c:set target="${tabItems}" property="SalePosition.tab.title" value="/SalePosition/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <c:if test="${isSupplierAndCustomer || app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.productContract" value="/ProductContract/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <c:if test="${app2:isCustomer(code)}">
            <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                <c:set target="${tabItems}" property="Invoice.tab.title" value="/Invoice/SingleList.do"/>
            </app2:checkAccessRight>
        </c:if>

        <app2:checkAccessRight functionality="TASK" permission="VIEW">
            <c:set target="${tabItems}" property="Scheduler.Tasks" value="/TaskList.do"/>
        </app2:checkAccessRight>

        <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
            <c:set target="${tabItems}" property="Scheduler.Appointments" value="/AppointmentList.do"/>
        </app2:checkAccessRight>

        <c:if test="${app2:isSupplier(code)}">
            <app2:checkAccessRight functionality="SUPPLIER" permission="VIEW">
                <c:set target="${tabItems}" property="Contacts.Tab.supplierInfo" value="/Supplier/Forward/Update.do"/>
            </app2:checkAccessRight>
            <c:if test="${!isSupplierAndCustomer}">
                <app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="VIEW">
                    <c:set target="${tabItems}" property="Contact.Tab.productContract"
                           value="/ProductContract/List.do"/>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="PRODUCTSUPPLIER" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.supplierProduct" value="/SupplierProduct/List.do"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                <c:set target="${tabItems}" property="Contact.Tab.incomingInvoiceList"
                       value="/IncomingInvoice/List.do"/>
            </app2:checkAccessRight>
        </c:if>
        <app2:checkAccessRight functionality="DEPARTMENT" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.departments" value="/Organization/Department/List.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="BANKACCOUNT" permission="VIEW">
            <c:set target="${tabItems}" property="Contacts.Tab.bankAccounts" value="/BankAccount/List.do"/>
        </app2:checkAccessRight>
        <%
            if (CodeUtil.isCompany(code) && (companyId.equals(addressId))) { // if company logged
        %>
        <app2:checkAccessRight functionality="COMPANYINFO" permission="VIEW">
            <c:set target="${tabItems}" property="Contact.Tab.companyInfo" value="/Company/Forward/Update.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="OFFICE" permission="VIEW">
            <c:set target="${tabItems}" property="Contact.Tab.offices" value="/Organization/Office/List.do"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="EMPLOYEE" permission="VIEW">
            <c:set target="${tabItems}" property="Contact.Tab.employees" value="/Organization/Employee/List.do"/>
        </app2:checkAccessRight>
        <%
            } //else it is default organization
        %>
    </c:otherwise>
</c:choose>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(addressId)" value="${param.contactId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>

</c:if>