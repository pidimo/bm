<%@ include file="/Includes.jsp" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="top" align="left"  class="moduleShortCut">
            <c:if test="${!empty configurationMenuMap['1']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['1'])}?module=catalogs&category=1&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.contact"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['2']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['2'])}?category=2&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.customer"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['3']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['3'])}?category=3&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.communication"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['4']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['4'])}?category=4&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.products"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['5']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['5'])}?category=5&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.Sales"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['6']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['6'])}?category=6&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.Scheduler"/>
                </app:link>
            </c:if>

            <c:if test="${!empty configurationMenuMap['7']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['7'])}?category=7&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.Support"/>
                </app:link>
            </c:if>
            <c:if test="${!empty configurationMenuMap['8']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['8'])}?category=8&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.General"/>
                </app:link>
            </c:if>
            <c:if test="${!empty configurationMenuMap['9']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['9'])}?category=9&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.Campaign"/>
                </app:link>
            </c:if>
            <c:if test="${!empty configurationMenuMap['10']}">
                &nbsp;|&nbsp;
                <app:link
                        page="${app2:getFirstValueOfLinkedMap(configurationMenuMap['10'])}?category=10&module=catalogs&index=0"
                        addModuleParams="false">
                    <fmt:message key="Catalog.Title.Finance"/>
                </app:link>
            </c:if>
            &nbsp;|

        </td>
        <td align="right" class="moduleShortCut">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:reportsMenu module="catalogs"/>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>