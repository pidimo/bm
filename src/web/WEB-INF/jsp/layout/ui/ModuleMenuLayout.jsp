<%@ include file="/Includes.jsp" %>

<c:if test="${empty applicationScope.menuModuleBootstrap}">
    <x:parse var="menuModuleBootstrap" scope="application">
        <c:import url="/WEB-INF/jsp/layout/xml/ModuleMenu.xml"/>
    </x:parse>
</c:if>
<div id="stickUpNavBar">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->

        </div>
        <div class="navbar-header hidden-sm hidden-md hidden-lg">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#module-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">
                <%--<html:img src="${baselayout}/img/bmIcon.gif" border="0"/>--%>
            </a>
        </div>

        <div class="collapse navbar-collapse manuCustonCollapce" id="module-navbar-collapse-1">
            <ul class="nav navbar-nav" id="myUL1">
                <x:forEach select="$applicationScope:menuModuleBootstrap//module">
                    <c:set var="isDefaultModule"><x:out select="@default"/></c:set>
                    <c:set var="moduleName"><x:out select="@name"/></c:set>
                    <c:set var="moduleTitleKey"><x:out select="@titleKey"/></c:set>
                    <c:set var="currentShortCut"><x:out select="shortCuts"/></c:set>

                    <x:if select="catalogModule">
                        <x:forEach select="catalogModule">
                            <c:set var="structure"><x:out select="@structure"/></c:set>
                            <c:import url="${structure}"/>
                        </x:forEach>
                        <c:if test="${app2:checkConfigurationMenuAccess(configurationMenuMap)}"
                              var="hasCatalogModuleAccess"/>
                    </x:if>

                    <c:set var="iterate" value="true"/>
                    <x:forEach select="forward">
                        <c:if test="${iterate}">
                            <c:set var="action"><x:out select="@action"/></c:set>
                            <c:set var="permission"><x:out select="@permission"/></c:set>
                            <c:set var="functionality"><x:out select="@functionality"/></c:set>
                            <c:choose>
                                <c:when test="${not empty functionality}">
                                    <app2:checkAccessRight functionality="${functionality}"
                                                           permission="${permission}">

                                        <li class="dropdown dropdown-hover ${(param.module == moduleName || (empty param.module && isDefaultModule)) ? 'active' : ''}">

                                            <app:url var="actionUrl" value="${action}?module=${moduleName}" contextRelative="true" addModuleParams="false" addModuleName="false"/>
                                            <a href="${actionUrl}">
                                                <fmt:message key="${moduleTitleKey}"/>
                                                <span class="caret"></span>
                                            </a>
                                            <c:import url="${currentShortCut}"/>
                                        </li>
                                        <c:set var="iterate" value="false"/>
                                    </app2:checkAccessRight>
                                </c:when>
                                <c:when test="${not empty structure && hasCatalogModuleAccess}">
                                    <li class="dropdown dropdown-hover ${(param.module == moduleName || (empty param.module && isDefaultModule)) ? 'active' : ''}">

                                        <c:set var="actionUrl" value="#"/>
                                        <%--check for first contact catalog configuration--%>
                                        <c:if test="${!empty configurationMenuMap['1']}">
                                            <app:url var="actionUrl" value="/catalogs/${app2:getFirstValueOfLinkedMap(configurationMenuMap['1'])}?module=catalogs&category=1&index=0"
                                                     contextRelative="true" addModuleParams="false" addModuleName="false"/>
                                        </c:if>

                                        <a href="${actionUrl}">
                                            <fmt:message key="${moduleTitleKey}"/>
                                            <span class="caret"></span>
                                        </a>
                                        <c:import url="${currentShortCut}"/>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="dropdown dropdown-hover ${(param.module == moduleName || (empty param.module && isDefaultModule)) ? 'active' : ''}">

                                        <app:url var="actionUrl" value="${action}?module=${moduleName}" contextRelative="true" addModuleParams="false" addModuleName="false"/>
                                        <a href="${actionUrl}">
                                            <fmt:message key="${moduleTitleKey}"/>
                                            <span class="caret"></span>
                                        </a>
                                        <c:import url="${currentShortCut}"/>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </x:forEach>
                </x:forEach>
            </ul>
        </div>
    </nav>
</div>







