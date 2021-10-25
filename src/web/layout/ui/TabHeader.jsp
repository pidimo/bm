<%@ include file="/Includes.jsp" %>
<tr>
    <td>
        <table border="0" align="left" cellspacing="0" cellpadding="0" height="15" width="100%">
            <tr>
                <td align="left" class="pageHeader" nowrap>
                    <fmt:message key="${tabHeaderLabel}"/><c:if test="${tabHeaderValue != null}">:</c:if>
                </td>
                <c:if test="${tabHeaderValue != null}">
                    <td align="left" class="pageHeaderValue" width="100%">
                        &nbsp;<c:out value="${tabHeaderValue}" escapeXml="false"/>
                    </td>
                </c:if>
            </tr>
        </table>
    </td>
</tr>

