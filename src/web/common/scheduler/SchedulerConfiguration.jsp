<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<%
    List dayFragmentationList = JSPHelper.getDayFragmentationOptions(request);

    request.setAttribute("dayFragmentationList", dayFragmentationList);

    List dayOfWork = JSPHelper.getDayOfWorkOptions();

    List endDayOfWork = JSPHelper.getEndDayOfWorkOptions();

    request.setAttribute("dayOfWork", dayOfWork);
    request.setAttribute("endDayOfWork", endDayOfWork);

    List calendarOptions = JSPHelper.getCalendarDefaultViewOptions(request);

    request.setAttribute("calendarOptions", calendarOptions);
%>
<br/>
<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="/Configuration/Update.do" focus="dto(calendarDefaultView)">
                <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(operation)"/>
                <html:hidden property="dto(op)" value="${configurationForm.dtoMap['operation']}"/>
                <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button" tabindex="6">${save}</app2:securitySubmit>
                            <%--<html:submit styleClass="button" tabindex="6">
                                <fmt:message key="Common.save"/>
                            </html:submit>--%>
                        </TD>
                    </TR>
                </table>
                <table cellSpacing=0 cellPadding=0 width="50%" border=0 align="center">
                    <TR>
                        <TD colspan="2" class="title">
                            <fmt:message key="Scheduler.configuration"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label" width="50%">
                            <fmt:message key="Scheduler.calendarDefaultView"/>
                        </td>
                        <td class="contain" width="50%">
                            <html:select property="dto(calendarDefaultView)" styleClass="shortSelect" tabindex="1">
                                <html:option value="">
                                    &nbsp;
                                </html:option>
                                <html:options collection="calendarOptions" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Scheduler.dayFragmentation"/>
                        </td>
                        <td class="contain">
                            <html:select property="dto(dayFragmentation)" styleClass="shortSelect" tabindex="2">
                            <html:option value="">
                            &nbsp;
                            </html:option>
                                <html:options collection="dayFragmentationList" property="value" labelProperty="label"/>
                            </html:select>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Scheduler.initDayOfWork"/>
                        </td>
                        <td class="contain">
                            <html:select property="dto(initialDayOfWork)" styleClass="shortSelect" tabindex="3">
                                <html:option value="">
                                    &nbsp;
                                </html:option>
                                <html:options collection="dayOfWork" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Scheduler.finalDayOfWork"/>
                        </td>
                        <td class="contain">
                            <html:select property="dto(finalDayOfWork)" styleClass="shortSelect" tabindex="4">
                                <html:option value="">
                                    &nbsp;
                                </html:option>
                                <html:options collection="endDayOfWork" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="Scheduler.holidayCountry"/>
                        </td>
                        <td class="contain">
                            <fanta:select property="dto(holidayCountryId)" listName="countryList" module="/catalogs"
                                          labelProperty="name" valueProperty="id" firstEmpty="true"
                                          styleClass="mediumSelect" tabIndex="5" readOnly="${isDelete}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </td>
                    </tr>
                </table>
                <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button" tabindex="6">${save}</app2:securitySubmit>
                            <%--<html:submit styleClass="button" tabindex="6">
                                <fmt:message key="Common.save"/>
                            </html:submit>--%>
                        </TD>
                    </TR>
                </table>
            </html:form>
        </td>
    </tr>
</table>
