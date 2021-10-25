<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>

    </tr>
    <tr>
        <td>
            <html:form action="${action}" focus="dto(workLevelName)">
                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>

                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(workLevelId)"/>
                    </c:if>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <c:set var="haveFirstTranslation" value="${workLevelForm.dtoMap['haveFirstTranslation']}"/>
                        <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
                        <html:hidden property="dto(langTextId)"/>
                    </c:if>
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>

                    <TR>
                        <TD class="label" width="25%" nowrap><fmt:message key="WorkLevel.name"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(workLevelName)" styleClass="largetext" maxlength="40"
                                      view="${'delete' == op}" tabindex="1"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label"><fmt:message key="WorkLevel.sequence"/></TD>
                        <TD class="contain">
                            <app:text property="dto(sequence)" styleClass="shortText" style="text-align:right"
                                      maxlength="4" view="${'delete' == op}" tabindex="2"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label"><fmt:message key="WorkLevel.language"/></td>
                        <td class="contain">
                            <fanta:select property="dto(languageId)" listName="systemLanguageList" labelProperty="name"
                                          valueProperty="id" tabIndex="3"
                                          firstEmpty="true" styleClass="select"
                                          readOnly="${op == 'delete'|| true == haveFirstTranslation}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </td>
                    </tr>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="WORKLEVEL" styleClass="button"
                                                 tabindex="4">
                                ${button}
                            </app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="WORKLEVEL" tabindex="5"
                                                     styleClass="button" property="SaveAndNew"><fmt:message
                                        key="Common.saveAndNew"/></app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button" tabindex="6"><fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>



