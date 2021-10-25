<%@ include file="/Includes.jsp" %>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
    <TR>
        <TD>
            <html:form action="${action}" focus="dto(comment)" enctype="multipart/form-data">
                <html:hidden property="dto(op)" value="${op}"/>

                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(attachId)"/>
                    <html:hidden property="dto(freeTextId)"/>
                </c:if>

                <c:if test="${'delete' == op}">
                    <html:hidden property="dto(attachId)"/>
                    <html:hidden property="dto(withReferences)" value="true"/>
                </c:if>

                <table class="container" align="center" width="55%" cellpadding="0" cellspacing="0" border="0">
                    <tr>
                        <td class="button" colspan="2">
                            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CAMPAIGNATTACH"
                                                 styleClass="button">
                                ${button}
                            </app2:securitySubmit>
                            <html:cancel styleClass="button" tabindex="7">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                    <tr>
                        <td class="title" colspan="2">
                                ${title}
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="30%">
                            <fmt:message key="Attach.comment"/>
                        </td>
                        <td class="contain" width="70%">
                            <app:text property="dto(comment)" styleClass="middleText" tabindex="1" maxlength="100"
                                      view="${'delete' == op}"/>
                        </td>
                    </tr>
                    <c:if test="${'update'== op || 'delete' == op}">
                        <tr>
                            <td class="label">
                                <fmt:message key="Attach.fileName"/>
                            </td>
                            <td class="contain">
                                <html:hidden property="dto(filename)"/>
                                <html:hidden property="dto(filenameAux)" value="${attachForm.dtoMap['filename']}"/>
                                    ${attachForm.dtoMap['filename']}
                            </td>
                        </tr>
                        <tr>
                            <td class="label">
                                <html:hidden property="dto(size)"/>
                                <fmt:message key="Attach.fileSize"/>
                            </td>
                            <td class="contain">
                                <c:set var="e" value="${app2:divide(attachForm.dtoMap['size'],(1024))}"/>
                                <c:choose>
                                    <c:when test="${e > 0}">
                                        ${e}
                                    </c:when>
                                    <c:otherwise>
                                        1
                                    </c:otherwise>
                                </c:choose>
                                <fmt:message key="Webmail.mailTray.Kb"/>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${'delete' != op}">
                        <tr>
                            <td class="label" width="30%">
                                <fmt:message key="Common.file"/>
                            </td>
                            <td class="contain" width="70%">
                                <html:file property="file" styleClass="largetext"/>
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:securitySubmit property="dto(save)" operation="${op}" functionality="CAMPAIGNATTACH"
                                                 styleClass="button" tabindex="4">
                                ${button}
                            </app2:securitySubmit>

                            <html:cancel styleClass="button" tabindex="5">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>
        </TD>
    </TR>
</table>