<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<br>

<table cellSpacing=0 cellPadding=4 width="650" border=0 align="center">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(productName)">
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(productId)" value="${param.productId}"/>

                <c:if test="${'update' == op || 'delete' == op}">
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(competitorProductId)"/>
                </c:if>

                <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
                    <TR>
                        <TD class="button" nowrap>
                            <app2:securitySubmit operation="${op}" functionality="COMPETITORPRODUCT" styleClass="button"
                                                 property="dto(save)" tabindex="1">
                                ${button}
                            </app2:securitySubmit>
                            <html:cancel styleClass="button" tabindex="2">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>

                <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
                    <TR>
                        <TD colspan="4" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </tr>
                    <tr>
                        <td width="23%" class="label">
                            <fmt:message key="Competitor.product"/>
                        </TD>
                        <td width="42%" class="contain">
                            <app:text property="dto(productName)" styleClass="mediumText" maxlength="40" tabindex="3"
                                      view="${op == 'delete'}"/>
                        </TD>
                        <TD class="label" width="15%">
                            <fmt:message key="Competitor.entryDate"/>
                        </TD>
                        <TD class="contain" width="25%">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <jsp:useBean id="now" class="java.util.Date"/>
                            <c:set var="dateValueParse" value="${now}"/>

                            <c:if test="${'create' == op}">
                                <fmt:formatDate var="entryDate" value="${dateValueParse}" pattern="${datePattern}"/>
                                <fmt:formatDate var="changeDate" value="${dateValueParse}" pattern="${datePattern}"/>
                            </c:if>

                            <c:if test="${'create' != op}">
                                <c:set var="parseable" value="${dto.entryDate}"/>
                                <fmt:formatDate var="entryDate" value="${app2:intToDate(parseable)}"
                                                pattern="${datePattern}"/>
                                <fmt:formatDate var="changeDate" value="${dateValueParse}" pattern="${datePattern}"/>
                            </c:if>

                            <c:out value="${entryDate}"/>
                            <html:hidden property="dto(entryDate)"/>
                        </TD>
                    </TR>

                    <tr>
                        <TD class="label">
                            <fmt:message key="Competitor.competitorName"/>
                        </TD>
                        <TD class="contain">
                            <html:hidden property="dto(competitorId)" styleId="fieldAddressId_id"/>
                            <app:text property="dto(competitorName)" styleId="fieldAddressName_id"
                                      styleClass="mediumText" maxlength="40" tabindex="4" view="${op == 'delete'}"
                                      readonly="true"/>

                            <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                                              titleKey="Common.search"
                                              hide="${op == 'delete'}" tabindex="5"/>

                            <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                                   titleKey="Common.clear"
                                                   hide="${op == 'delete'}" tabindex="6"/>

                            <c:set var="path" value="${pageContext.request.contextPath}"/>
                        </TD>
                        <TD class="label">
                            <fmt:message key="Competitor.changeDate"/>
                        </TD>
                        <TD class="contain">
                            <c:out value="${changeDate}"/>
                        </TD>
                    </tr>
                    <tr>
                        <TD class="topLabel">
                            <fmt:message key="Competitor.price"/>
                        </TD>
                        <TD class="contain" colspan="3">
                            <app:numberText property="dto(price)"
                                            styleClass="mediumText"
                                            tabindex="7"
                                            maxlength="12"
                                            view="${'delete' == op}"
                                            numberType="decimal"
                                            maxFloat="2"
                                            maxInt="7"/>
                        </td>

                    </tr>

                    <tr>
                        <TD class="label" colspan="4"><fmt:message key="Competitor.description"/><br>
                            <html:textarea property="dto(description)" styleClass="mediumDetail" tabindex="8"
                                           style="height:80px;width:650px;" readonly="${op == 'delete'}"/>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="4" <c:out value="${sessionScope.listshadow}" escapeXml="false"/>><img
                                src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
                    </tr>
                </table>

                <table width="100%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">

                            <!-- Buttons -->
                            <app2:securitySubmit operation="${op}" functionality="COMPETITORPRODUCT" styleClass="button"
                                                 property="dto(save)" tabindex="9">
                                ${button}
                            </app2:securitySubmit>
                            <html:cancel styleClass="button" tabindex="10"><fmt:message
                                    key="Common.cancel"/></html:cancel>

                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>