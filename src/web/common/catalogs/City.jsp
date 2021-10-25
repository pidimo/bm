<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(cityName)">
                <table id="City.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(cityId)"/>
                    </c:if>
                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="City.name"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(cityName)" styleClass="largetext" maxlength="40"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%"><fmt:message key="City.zip"/></TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(cityZip)" styleClass="text" maxlength="10"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <TR>
                        <TD class="label" width="25%"><fmt:message key="City.country"/></TD>
                        <TD class="contain" width="75%">
                            <fanta:select property="dto(countryId)" listName="countryBasicList" labelProperty="name"
                                          valueProperty="id" firstEmpty="true" styleClass="select"
                                          readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </TD>
                    </TR>
                    <tr>
                        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false"/>><img
                                src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
                    </tr>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="CITY"
                                                 styleClass="button">${button}</app2:securitySubmit>

                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="CITY" styleClass="button"
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