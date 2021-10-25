<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    function check()
    {
        field = document.getElementById('favoriteForm').checkbox;
        guia = document.getElementById('favoriteForm').checkAll;

        var i;

        if (guia.checked)
        {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else
        {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }

</script>


<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %></c:set>

<html:form action="${action}" styleId="favoriteForm">
    <html:hidden property="dto(op)" value="save"/>
    <html:hidden property="dto(sizeOfFavorites)" value="${dto.sizeOfFavorites}"/>
    <html:hidden property="dto(companyId)"/>
    <html:hidden property="dto(userId)"/>
    <html:hidden property="dto(addressType)"/>

    <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    <td>
    <table border="0" cellpadding="3" cellspacing="0" width="40%" align="center" class="container">
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>

        <tr>
            <TD class="label" width="20%" nowrap style="text-align:center;">
                <input type=checkbox name="checkAll" value="1" onclick="javascript:check();" class="radio"
                       title="<fmt:message key="Common.selectAll"/>">
            </TD>
            <TD class="label" width="80%" nowrap><fmt:message key="Favorite.name"/></TD>
        </tr>
        <c:forEach var="favorites" items="${dto.favoriteList}" varStatus="index" begin="0" end="${dto.sizeOfFavorites}">
            <TR height="15">
                <td class="contain" style="text-align:center;padding:0">
                    <input type="checkbox" class="radio" name="dto(item<c:out value="${index.count}"/>)"
                           value="<c:out value="${favorites.addressId}"/>" id="checkbox">
                    <html:hidden property="dto(codes${index.count})" value="${favorites.addressId}"/>
                </td>
                <c:if test="${favorites.addressType == personType}">
                    <c:choose>
                        <c:when test="${favorites.name3 == 'null'}">
                            <TD class="contain" nowrap>
                                <html:link
                                        action="/Person/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                                    <c:out value="${favorites.name1}"/>, <c:out value="${favorites.name2}"/>, <c:out
                                        value="${favorites.name3}"/>
                                </html:link>
                            </TD>
                        </c:when>
                        <c:otherwise>
                            <TD class="contain" nowrap>
                                <html:link
                                        action="/Person/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                                    <c:out value="${favorites.name1}"/><c:if test="${favorites.name2 != ''}"><c:out
                                        value=", ${favorites.name2}"/></c:if>
                                </html:link>
                            </TD>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${favorites.addressType == organizationType}">
                    <TD class="contain">
                        <html:link
                                action="/Organization/Forward/Update.do?contactId=${favorites.addressId}&index=0&amp;dto(addressId)=${favorites.addressId}">
                            <c:out value="${favorites.name1}"/> <c:out value="${favorites.name2}"/> <c:out
                                value="${favorites.name3}"/>
                        </html:link>
                    </TD>
                </c:if>
            </TR>
        </c:forEach>
    </table>
    <table cellSpacing=0 cellPadding=4 width="40%" border=0 align="center">
        <TR>
            <c:choose>
                <c:when test="${dto.sizeOfFavorites != 0}">
                    <TD class="button">
                        <app2:checkAccessRight functionality="FAVORITE" permission="UPDATE">
                            <html:submit styleClass="button"><fmt:message key="Common.delete"/></html:submit>
                        </app2:checkAccessRight>
                        <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                    </TD>
                </c:when>
                <c:otherwise>
                    <TD class="button">
                        <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                    </TD>
                </c:otherwise>
            </c:choose>
        </TR>
    </table>
</html:form>

</td>
</tr>
</table>