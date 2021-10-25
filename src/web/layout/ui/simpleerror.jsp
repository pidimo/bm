<%@ include file="/Includes.jsp" %>
<%-- Writing the errors --%>

<c:if test="${skipErrors == null}">
    <logic:messagesPresent message="false">
        <tr>
            <td valign="top">
                <br/>
                <table width="50%" align="center" cellpadding="0" cellspacing="0">
                    <tr>
                        <td class="error">
                            <table width="100%" align="center" cellpadding="0" cellspacing="0">
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
            </td>
        </tr>
    </logic:messagesPresent>
</c:if>
