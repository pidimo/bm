<%@ include file="/Includes.jsp" %>

<c:if test="${empty applicationScope.menuModuleDoc}">
    <x:parse var="menuModuleDoc" scope="application">
        <c:import url="/layout/xml/ModuleMenu.xml"/>
    </x:parse>
</c:if>

<!-- module tabs -->
<TR>
    <TD valign="top" style="padding: 2 0 0 0;">

        <table width="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td valign="top" nowrap>
                    <table width="100%" align="center" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                            <td class="moduleTab" id="moduleSpace"><img src="<c:url value="/layout/ui/img/spacer.gif"/>"
                                                                        width="2"/></td>
                            <x:forEach select="$applicationScope:menuModuleDoc//module">
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
                                            <c:when test="${functionality != null && functionality != ''}">
                                                <app2:checkAccessRight functionality="${functionality}"
                                                                       permission="${permission}">
                                                    <td class="moduleTab" nowrap <c:if
                                                            test="${param.module==moduleName || ((param.module==null || param.module == '') && isDefaultModule)}"> id="currentModule"
                                                        <c:set var="urlShortCuts" value="${currentShortCut}"/> </c:if>>
                                                        <app:link page="${action}?module=${moduleName}"
                                                                  contextRelative="true" addModuleParams="false"
                                                                  addModuleName="false"><fmt:message
                                                                key="${moduleTitleKey}"/></app:link>
                                                    </td>
                                                    <td class="moduleTab" id="moduleSpace"><img
                                                            src="<c:url value="/layout/ui/img/spacer.gif"/>" alt=""
                                                            width="2"/></td>
                                                    <c:set var="iterate" value="false"/>
                                                </app2:checkAccessRight>
                                            </c:when>
                                            <c:when test="${structure != null && hasCatalogModuleAccess}">
                                                <td class="moduleTab" nowrap <c:if
                                                        test="${param.module==moduleName  || ((param.module==null || param.module == '') && isDefaultModule)}"> id="currentModule"
                                                    <c:set var="urlShortCuts" value="${currentShortCut}"/> </c:if>>
                                                    <app:link page="${action}?module=${moduleName}"
                                                              contextRelative="true" addModuleParams="false"
                                                              addModuleName="false"><fmt:message
                                                            key="${moduleTitleKey}"/></app:link>
                                                </td>
                                                <td class="moduleTab" id="moduleSpace"><img
                                                        src="<c:url value="/layout/ui/img/spacer.gif"/>" alt=""
                                                        width="2"/></td>
                                            </c:when>
                                            <c:when test="${structure == null}">
                                                <td class="moduleTab" nowrap <c:if
                                                        test="${param.module==moduleName  || ((param.module==null || param.module == '') && isDefaultModule)}"> id="currentModule"
                                                    <c:set var="urlShortCuts" value="${currentShortCut}"/> </c:if>>
                                                    <app:link page="${action}?module=${moduleName}"
                                                              contextRelative="true" addModuleParams="false"
                                                              addModuleName="false"><fmt:message
                                                            key="${moduleTitleKey}"/></app:link>
                                                </td>
                                                <td class="moduleTab" id="moduleSpace"><img
                                                        src="<c:url value="/layout/ui/img/spacer.gif"/>" alt=""
                                                        width="2"/></td>
                                            </c:when>
                                        </c:choose>
                                    </c:if>
                                </x:forEach>

                            </x:forEach>
                            <td class="moduleTab" id="moduleSpace" style="width:100%;"><img
                                    src="<c:url value="/layout/ui/img/spacer.gif"/>" alt=""/></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <%--Module shortcuts--%>
            <TR>
                <TD valign="top">
                    <c:import url="${urlShortCuts}"/>
                </TD>
            </TR>
            <tr>
                <td class="shadow" width="100%">&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>





