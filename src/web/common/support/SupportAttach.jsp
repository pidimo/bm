<%@ include file="/Includes.jsp" %>


<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>

            <html:form action="${action}" enctype="multipart/form-data" focus="dto(comment)">
                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>


                    <c:if test="${param.articleId!=null}">
                        <html:hidden property="dto(articleId)" value="${param.articleId}"/>
                    </c:if>

                        <%--for ArticleHistory YUMI--%>
                    <html:hidden property="dto(actionValue)" value="${actionValue}"/>

                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(attachId)"/>
                        <c:if test="${null != dto.supportAttachName}">
                            <c:set var="fileName" value="${dto.supportAttachName}"/>
                        </c:if>
                        <html:hidden property="dto(fileName)" value="${fileName}"/>
                    </c:if>

                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <tr>
                        <td colspan="2" class="title"><c:out value="${title}"/></td>
                    </tr>

                    <tr>
                        <td class="label"><fmt:message key="SupportAttach.comment"/></td>
                        <td class="contain">
                            <app:text property="dto(comment)" styleClass="largeText" maxlength="80"
                                      view="${'delete' == op}"/>
                        </td>
                    </tr>

                    <c:choose>
                        <c:when test="${'create' == op}">
                            <tr>
                                <td class="label" width="25%"><fmt:message key="SupportAttach.fileName"/></td>
                                <td class="contain" width="75%">
                                    <html:file property="dto(file)" size="40"/>
                                </td>
                            </tr>
                        </c:when>
                        <c:when test="${'update' == op}">
                            <html:hidden property="dto(supportAttachName)" value="${fileName}"/>
                            <tr>
                                <td class="label" width="25%"><fmt:message key="SupportAttach.fileName"/></td>
                                <td class="contain" width="75%">
                                    <html:file property="dto(file)" size="40"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="label" width="25%"><fmt:message key="SupportAttach.fileUplated"/></td>
                                <td class="contain" width="75%">${fileName}</td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="dto(supportAttachName)" value="${fileName}"/>
                            <tr>
                                <td class="label" width="25%"><fmt:message key="SupportAttach.fileUplated"/></td>
                                <td class="contain" width="75%">${fileName}</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>


                    <c:if test="${'delete' == op || 'update' == op}">
                        <tr>
                            <td class="label"><fmt:message key="SupportAttach.size"/></td>
                            <td class="contain">
                                <html:hidden property="dto(size)"/>
                                <c:choose>
                                    <c:when test="${supportAttachForm.dtoMap['size'] < 1024}">
                                        ${1} <fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:when>
                                    <c:otherwise>
                                        ${fn:substringBefore(supportAttachForm.dtoMap['size']/1024,".")}
                                        <fmt:message key="Webmail.mailTray.Kb"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                        <tr>
                            <td class="label"><fmt:message key="SupportAttach.uploadDate"/></td>
                            <td class="contain">
                                <html:hidden property="dto(uploadDateTime)"/>
                                    ${app2:getDateWithTimeZone(supportAttachForm.dtoMap['uploadDateTime'], timeZone, dateTimePattern)}
                            </td>
                        </tr>
                    </c:if>

                    <c:if test="${'update' == op}">
                        <tr>
                            <td class="label"><fmt:message key="Common.download"/></td>
                            <td class="contain">
                                <html:hidden property="dto(supportAttachName)"/>
                                <c:set var="download"
                                       value="${attachDownload}&dto(attachId)=${supportAttachForm.dtoMap['attachId']}&dto(supportAttachName)=${app2:encode(supportAttachForm.dtoMap['supportAttachName'])}&
dto(articleTitle)=${app2:encode(param['dto(articleTitle)'])}&articleId=${param.articleId}&caseId=${param.caseId}"/>
                                <html:link action="${download}" titleKey="Common.download">
                                    <html:img src="${baselayout}/img/openfile.png" titleKey="Common.download"
                                              border="0"/>
                                </html:link>
                            </td>
                        </tr>
                    </c:if>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <c:if test="${op != 'create' && (supportAttachForm.dtoMap.userId == sessionScope.user.valueMap['userId'] || supportAttachForm.dtoMap.createUserId == sessionScope.user.valueMap['userId'])}">
                                <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH" styleClass="button"
                                                     tabindex="10">
                                    ${button}
                                </app2:securitySubmit>
                            </c:if>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH" styleClass="button"
                                                     tabindex="10">
                                    ${button}
                                </app2:securitySubmit>

                                <app2:securitySubmit operation="${op}" functionality="SUPPORTATTACH" styleClass="button"
                                                     tabindex="11"
                                                     property="SaveAndNew">
                                    <fmt:message key="Common.saveAndNew"/>
                                </app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>
        </td>
    </tr>
</table>