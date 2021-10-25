<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">
    <c:if test="${!empty configurationMenuMap['1']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['1'])}?module=catalogs&category=1&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.contact"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['2']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['2'])}?category=2&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.customer"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['3']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['3'])}?category=3&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.communication"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['4']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['4'])}?category=4&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.products"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['5']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['5'])}?category=5&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.Sales"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['6']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['6'])}?category=6&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.Scheduler"/>
            </app:link>
        </li>
    </c:if>

    <c:if test="${!empty configurationMenuMap['7']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['7'])}?category=7&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.Support"/>
            </app:link>
        </li>
    </c:if>
    <c:if test="${!empty configurationMenuMap['8']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['8'])}?category=8&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.General"/>
            </app:link>
        </li>
    </c:if>
    <c:if test="${!empty configurationMenuMap['9']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['9'])}?category=9&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.Campaign"/>
            </app:link>
        </li>
    </c:if>
    <c:if test="${!empty configurationMenuMap['10']}">
        <li>
            <app:link
                    page="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['10'])}?category=10&module=catalogs&index=0"
                    contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Catalog.Title.Finance"/>
            </app:link>
        </li>
    </c:if>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapReportsMenu module="catalogs"/>
    </tags:bootstrapMenu>
</ul>
