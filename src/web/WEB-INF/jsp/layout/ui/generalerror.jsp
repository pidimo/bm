<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/layout/ui/simpleerror.jsp"/>

<%-- Writing the errors --%>
<%--
<c:if test="${skipErrors == null}">
    <logic:messagesPresent message="false">
        <tr>
            <td valign="top">


                <c:if test="${not empty index}">
                <table width="100%" align="center" cellpadding="0" cellspacing="0" height="100%">
                    <tr><td class="folderTabBorder folderTabContent" valign="top"
                            style="border-top:0px;border-bottom:0px;">
                        </c:if>


                        <br>
                        <table width="50%" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                                <td align="left" class="error">
                                    <table width="95%" align="center" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td colspan="2">
                                                <span class="error">
                                                        <fmt:message key="Common.message"/>
                                                    </span>
                                            </td>
                                        </tr>
                                        <app2:messages id="message" message="false">
                                            <tr>
                                                <td align="left" valign="top" width="10">
                                                    <c:url var="errorIcon" value="/layout/ui/img/arrowerror.gif"/>
                                                    <img src="${errorIcon}"
                                                         align="top" alt=""/>
                                                </td>
                                                <td align="rigth">
                                                        ${message}
                                                </td>
                                            </tr>
                                        </app2:messages>
                                    </table>
                                </td>
                            </tr>
                        </table>


                        <c:if test="${not empty index}">
                    </td></tr>
                </table>
                </c:if>

            </td>
        </tr>
    </logic:messagesPresent>
</c:if>
--%>
