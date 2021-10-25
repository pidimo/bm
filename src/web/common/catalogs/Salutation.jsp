<%@ include file="/Includes.jsp" %>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(salutationLabel)">
                <table id="Salutation.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                        <%--if update action or delete action--%>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(salutationId)"/>
                        <html:hidden property="dto(letterTextId)"/>
                        <html:hidden property="dto(addressTextId)"/>
                    </c:if>

                        <%--for the version control if update action--%>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>

                        <%--for the control referencial integrity if delete action--%>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>

                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>

                    <TR>
                        <TD class="label" width="25%" nowrap><fmt:message key="Salutation.label"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(salutationLabel)" styleClass="largetext" maxlength="20"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label"><fmt:message key="Salutation.letterText"/></TD>
                        <TD class="contain">
                            <app:text property="dto(letterText)" styleClass="largetext" maxlength="60"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label"><fmt:message key="Salutation.addressText"/></TD>
                        <TD class="contain">
                            <app:text property="dto(addressText)" styleClass="largetext" maxlength="60"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label" nowrap><fmt:message key="Salutation.languageForTexts"/></td>
                        <td class="contain">
                            <fanta:select property="dto(languageId)"
                                          listName="languageBaseList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true" styleClass="select"
                                          readOnly="${'delete' == op || (null != salutationForm.dtoMap['languageId'] && '' != salutationForm.dtoMap['languageId'])}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>

                            <%--<html:hidden property="dto(languageId)"/>--%>
                        </td>
                    </tr>

                </table>

                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="SALUTATION"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="SALUTATION" styleClass="button"
                                                     property="SaveAndNew"><fmt:message
                                        key="Common.saveAndNew"/></app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>
