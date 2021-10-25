<%@ include file="/Includes.jsp" %>
<ul class="dropdown-menu">
    <app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
        <li>
            <app:link page="/products/SimpleSearch.do?module=products" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Common.search"/>
            </app:link>
        </li>
        <li>
            <app:link page="/products/AdvancedSearch.do?advancedListForward=ProductAdvancedSearch&module=products" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Common.advancedSearch"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/products/Report/ProductList.do" contextRelative="true" titleKey="Product.Report.ProductList"  functionality="PRODUCT" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/products/Report/SupplierList.do" contextRelative="true" titleKey="Product.Report.SupplierList"  functionality="SUPPLIER" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/products/Report/CompetitorList.do" contextRelative="true" titleKey="Product.Report.CompetitorList"  functionality="COMPETITORPRODUCT" permission="VIEW"/>
        <tags:bootstrapReportsMenu module="products"/>
    </tags:bootstrapMenu>
</ul>
