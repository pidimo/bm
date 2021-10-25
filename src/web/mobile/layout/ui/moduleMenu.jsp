<%@ include file="/Includes.jsp" %>

<c:if test="${!(applicationScope.menuModuleDocMobile != null)}">
    <x:parse var="menuModuleDocMobile" scope="application">
        <c:import url="/mobile/layout/xml/ModuleMenuMobile.xml"/>
    </x:parse>
</c:if>


<div class="modules">

    <x:forEach select="$applicationScope:menuModuleDocMobile//module">
        <c:set var="isDefaultModule"><x:out select="@default"/></c:set>
        <c:set var="moduleName"><x:out select="@name"/></c:set>
        <c:set var="moduleTitleKey"><x:out select="@titleKey"/></c:set>
        <c:set var="currentShortCut"><x:out select="shortCuts"/></c:set>
        <c:set var="iterate" value="true"/>
        <x:forEach select="forward">
            <c:if test="${iterate}">
                <c:set var="action"><x:out select="@action"/></c:set>
                <c:set var="permission"><x:out select="@permission"/></c:set>
                <c:set var="functionality"><x:out select="@functionality"/></c:set>
                <c:choose>
                    <c:when test="${functionality != null && functionality != ''}">
                        <app2:checkAccessRight functionality="${functionality}" permission="${permission}">
                            <div>
                                <app:link page="${action}" contextRelative="true" addModuleParams="false"
                                          addModuleName="false"><fmt:message key="${moduleTitleKey}"/></app:link>
                            </div>
                            <c:set var="iterate" value="false"/>
                        </app2:checkAccessRight>
                    </c:when>
                    <c:when test="${structure == null}">
                        <div>
                            <app:link page="${action}" contextRelative="true" addModuleParams="false"
                                      addModuleName="false"><fmt:message key="${moduleTitleKey}"/></app:link>
                        </div>
                    </c:when>
                </c:choose>
            </c:if>
        </x:forEach>
    </x:forEach>

</div>




