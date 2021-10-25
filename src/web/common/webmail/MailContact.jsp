<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(mailContactName)">

    <table id="MailContact.jsp" border="0" cellpadding="0" cellspacing="0" width="80%" align="center" class="container">
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="15%">
                <fmt:message key="Webmail.mailContact.name"/>
            </TD>
            <TD class="contain" width="35%">
                <app:text property="dto(mailContactName)" styleClass="middleText" maxlength="80" tabindex="1"
                          view="${op == 'delete'}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="15%">
                <fmt:message key="Webmail.mailContact.nick"/>
            </TD>
            <TD class="contain" width="35%">
                <app:text property="dto(mailContactNick)" styleClass="middleText" maxlength="80" tabindex="1"
                          view="${op == 'delete'}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="15%">
                <fmt:message key="Webmail.mailContact.email"/>
            </TD>
            <TD class="contain" width="35%">
                <app:text property="dto(mailContactEmail)" styleClass="middleText" maxlength="80" tabindex="1"
                          view="${op == 'delete'}"/>
            </TD>
        </TR>

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(mailAddressId)"/>
            <html:hidden property="dto(mailContactId)"/>
        </c:if>
            <%--</TR>
                   <td colspan="2" class="shadow"><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
            </TR>--%>
    </table>
    <table cellSpacing=0 cellPadding=4 width="80%" border=0 align="center">
        <TR>
            <TD class="button">
                <c:if test="${op == 'create' || op == 'update'}">
                    <html:submit property="dto(save)" styleClass="button" styleId="saveButtonId">
                        <c:out value="${button}"/>
                    </html:submit>
                </c:if>
                <c:if test="${op == 'delete'}">
                    <html:submit property="dto(delete)" styleClass="button">
                        <fmt:message key="Common.delete"/>
                    </html:submit>
                </c:if>
                <html:cancel styleClass="button">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </TR>
    </table>
    <br>

</html:form>