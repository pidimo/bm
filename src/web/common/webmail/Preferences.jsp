<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="HTML_EDITMODE" value="<%=WebMailConstants.HTML_EDITMODE%>"/>
<c:set var="TEXT_EDITMODE" value="<%=WebMailConstants.TEXT_EDITMODE%>"/>

<br/>
<html:form action="/Preferences/Update.do" focus="dto(replyMode)">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

    <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <table cellSpacing=0 cellPadding=4 width="85%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="EXECUTE" functionality="MAIL"
                                                 styleClass="button">
                                <fmt:message key="Common.save"/>
                            </app2:securitySubmit>

                        </TD>
                    </TR>
                </table>
                <table border="0" cellpadding="0" cellspacing="0" width="85%" align="center" class="container">
                    <tr>
                        <td colspan="2" class="title">${title}</td>
                    </tr>

                    <tr>
                        <td class="label" width="60%">
                            <fmt:message key="Webmail.userMail.replyMode"/>
                        </td>
                        <td class="contain" width="40%">
                            <html:checkbox property="dto(replyMode)" value="true" styleClass="radio"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.saveSendItems"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(saveSendItem)" value="true" styleClass="radio"/>
                        </td>
                    </tr>

                    <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                        <tr>
                            <td class="label">
                                <fmt:message key="Webmail.userMail.emptyTrashLogout"/>
                            </td>
                            <td class="contain">
                                <html:checkbox property="dto(emptyTrashLogout)" value="true" styleClass="radio"/>
                            </td>
                        </tr>
                    </app2:checkAccessRight>

                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.editMode"/>
                        </td>
                        <td class="contain">
                            <html:select property="dto(editMode)" styleClass="mediumSelect">
                                <html:option value="" key=""></html:option>
                                <html:option value="${TEXT_EDITMODE}" key="Webmail.userMail.text"/>
                                <html:option value="${HTML_EDITMODE}" key="Webmail.userMail.html"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.editorFont"/>
                        </td>
                        <td class="contain">
                            <c:set var="fontList" value="${app2:getHtmlEditorFonts()}"/>
                            <html:select property="dto(editorFont)" styleClass="mediumSelect">
                                <html:option value=""/>
                                <html:options collection="fontList" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.editorFontSize"/>
                        </td>
                        <td class="contain">
                            <c:set var="fontSizeList" value="${app2:getHtmlEditorFontSizes()}"/>
                            <html:select property="dto(editorFontSize)" styleClass="mediumSelect">
                                <html:option value=""/>
                                <html:options collection="fontSizeList" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.backgroundDownload"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(backgroundDownload)" value="true" styleClass="radio"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Webmail.userMail.shoPopNotifications"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(showPopNotification)" value="true" styleClass="radio"/>
                        </td>
                    </tr>
                </table>

                <table cellSpacing=0 cellPadding=4 width="85%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="EXECUTE" functionality="MAIL"
                                                 styleClass="button">
                                <fmt:message key="Common.save"/>
                            </app2:securitySubmit>

                        </TD>
                    </TR>
                </table>
            </td>
        </tr>
    </table>
</html:form>