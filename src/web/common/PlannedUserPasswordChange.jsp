<%@ page import="com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher" %>
<%@include file="/Includes.jsp" %>

<html:form action="/User/Planned/PasswordUpdate.do" focus="dto(previousPassword)">

    <html:hidden property="dto(op)" value="update"/>

    <c:choose>
        <c:when test="${not empty param['passwordChangeId']}">
            <html:hidden property="dto(passwordChangeId)" value="${param['passwordChangeId']}"/>
            <html:hidden property="dto(userId)" value="${param['userId']}"/>
        </c:when>
        <c:otherwise>
            <html:hidden property="dto(passwordChangeId)"/>
            <html:hidden property="dto(userId)"/>
        </c:otherwise>
    </c:choose>

    <%--when process from external logon AJAX--%>
    <div id="isPlannedPasswordChangeId">
        <input type="hidden" id="urlPasswordChangeId" value="${param['urlPasswordChange']}">
    </div>

    <%--message when process from external logon AJAX--%>
    <c:if test="${not empty param['messageText']}">
        <div align="center">
            <table cellpadding="0" cellspacing="0" align="center" class="expiredMessage">
                <tr>
                    <td>
                        <% try {
                            out.print(UrlEncryptCipher.i.decrypt(request.getParameter("messageText")));
                        } catch (Exception e) {
                            //nothing to print in this case.
                        }
                        %>
                    </td>
                </tr>
            </table>
        </div>
        <br>
    </c:if>

    <table cellSpacing=0 cellPadding=0 width="45%" border=0 align="center">
        <tr>
            <td>
                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                    <tr>
                        <td colspan="2" class="title"><fmt:message key="User.passChange"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%"><fmt:message key="User.pass"/></td>
                        <td class="contain" width="70%">
                            <html:password property="dto(previousPassword)" styleClass="mediumText" maxlength="20"
                                           tabindex="1" value=""/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><fmt:message key="User.passNew"/></td>
                        <td class="contain">
                            <html:password property="dto(password1)" styleClass="mediumText" tabindex="2" value=""
                                           maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><fmt:message key="User.passConfir"/></td>
                        <td class="contain">
                            <html:password property="dto(password2)" styleClass="mediumText" tabindex="3" value=""
                                           maxlength="20"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="button">
                <html:submit property="dto(save)" styleClass="button" tabindex="4">
                    <c:out value="${button}"/>
                </html:submit>
            </td>
        </tr>
    </table>
</html:form>


