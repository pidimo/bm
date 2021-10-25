<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focus="dto(signatureName)">

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <c:if test="${op == 'create' || op == 'update'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(save)"
                                         styleClass="button" styleId="saveButtonId" tabindex="9">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" styleClass="button"
                                         property="SaveAndNew" tabindex="10">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'delete'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(delete)"
                                         styleClass="button" styleId="saveButtonId" tabindex="11">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="12">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </TR>
    </table>
    <table id="Signature.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <TR>
            <TD colspan="2" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <TR>
            <TD class="label" width="15%">
                <fmt:message key="Webmail.signature.name"/>
            </TD>
            <TD class="contain" width="35%">
                <app:text property="dto(signatureName)" styleClass="middleText" maxlength="30" tabindex="1"
                          view="${op == 'delete'}"/>
            </TD>
        </TR>
        <tr>
            <td class="label">
                <fmt:message key="Webmail.signature.mailAccount"/>
            </td>
            <td class="contain">
                <fanta:select property="dto(mailAccountId)"
                              listName="mailAccountList"
                              labelProperty="email"
                              valueProperty="mailAccountId"
                              firstEmpty="true"
                              styleClass="select"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
                    <c:if test="${'delete'== op || 'update' == op}">
                        <fanta:parameter field="mailAccountId" value="${signatureForm.dtoMap['mailAccountId']}"/>
                    </c:if>
                </fanta:select>
            </td>
        </tr>
        <TR>
            <TD class="label" width="15%">
                <fmt:message key="Webmail.signature.isDefault"/>
            </TD>
            <TD class="contain" width="35%">
                <html:checkbox property="dto(isDefault)" disabled="${op == 'delete'}" styleClass="radio" tabindex="2"/>
            </TD>
        </TR>
        <tr>
            <TD class="topLabel" colspan="2">
                <fmt:message key="Webmail.signature.messageLabel"/>
                <br>
                <html:textarea property="dto(signatureMessage)" styleClass="mediumDetail" readonly="${op == 'delete'}"
                               tabindex="3"
                               style="height:120px;width:99%;"/>
                <br>&nbsp;
            </TD>
        </tr>
        <tr>
            <td colspan="2" class="topLabel">
                <fmt:message key="Webmail.htmlSignature.messageLabel"/>
                <br>
                <table cellpadding="0" cellspacing="0" border="0" width="100%" class="container">
                    <tr>
                        <td>
                            <c:choose>
                                <c:when test="${'delete' != op}">
                                    <tags:initTinyMCE4 textAreaId="body_field" addBrowseImagePlugin="true"/>
                                    <html:textarea property="dto(signatureHtmlMessage)" tabindex="4"
                                                   styleClass="webmailBody"
                                                   styleId="body_field"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="ad" value="${signatureForm.dtoMap.signatureHtmlMessage}"
                                           scope="session"/>
                                    <iframe name="frame2"
                                            src="<c:url value="/common/support/PreviusDetail.jsp?var=ad" />"
                                            style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes"
                                            frameborder="1">
                                    </iframe>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </table>

            </td>
        </tr>

        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(signatureId)"/>
            <html:hidden property="dto(textSignatureId)"/>
            <html:hidden property="dto(htmlSignatureId)"/>
        </c:if>

    </table>
    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
        <TR>
            <TD class="button">
                <c:if test="${op == 'create' || op == 'update'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(save)"
                                         styleClass="button" styleId="saveButtonId" tabindex="5">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'create'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" styleClass="button"
                                         property="SaveAndNew" tabindex="6">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <c:if test="${op == 'delete'}">
                    <app2:securitySubmit operation="${op}" functionality="WEBMAILSIGNATURE" property="dto(delete)"
                                         styleClass="button" styleId="saveButtonId" tabindex="7">
                        <fmt:message key="Common.delete"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="8">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </TD>
        </TR>
    </table>
    <br>

</html:form>