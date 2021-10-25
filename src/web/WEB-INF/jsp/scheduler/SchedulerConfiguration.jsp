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


<html:form action="/Configuration/Update.do" focus="dto(calendarDefaultView)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(operation)"/>
        <html:hidden property="dto(op)" value="${configurationForm.dtoMap['operation']}"/>


        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE"
                                 functionality="USERSETTINGS"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="6">${save}</app2:securitySubmit>
                <%--<html:submit styleClass="button" tabindex="6">
                    <fmt:message key="Common.save"/>
                </html:submit>--%>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <fmt:message key="Scheduler.configuration"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="calendarDefaultView_id">
                        <fmt:message key="Scheduler.calendarDefaultView"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(calendarDefaultView)"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     styleId="calendarDefaultView_id"
                                     tabindex="1">
                            <html:option value="">
                            </html:option>
                            <html:options collection="calendarOptions" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="dayFragmentation_id">
                        <fmt:message key="Scheduler.dayFragmentation"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(dayFragmentation)"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     styleId="dayFragmentation_id"
                                     tabindex="2">
                        <html:option value="">
                        </html:option>
                            <html:options collection="dayFragmentationList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="initialDayOfWork_id">
                        <fmt:message key="Scheduler.initDayOfWork"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(initialDayOfWork)"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     styleId="initialDayOfWork_id"
                                     tabindex="3">
                            <html:option value="">
                            </html:option>
                            <html:options collection="dayOfWork" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="finalDayOfWork_id">
                        <fmt:message key="Scheduler.finalDayOfWork"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <html:select property="dto(finalDayOfWork)"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     styleId="finalDayOfWork_id"
                                     tabindex="4">
                            <html:option value="">
                            </html:option>
                            <html:options collection="endDayOfWork" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="holidayCountryId_id">
                        <fmt:message key="Scheduler.holidayCountry"/>
                    </label>
                    <div class="${app2:getFormContainClasses(null)}">
                        <fanta:select property="dto(holidayCountryId)" listName="countryList" module="/catalogs"
                                      labelProperty="name" valueProperty="id" firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()}" tabIndex="5"
                                      styleId="holidayCountryId_id" readOnly="${isDelete}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="6">${save}</app2:securitySubmit>
                <%--<html:submit styleClass="button" tabindex="6">
                    <fmt:message key="Common.save"/>
                </html:submit>--%>
        </div>

    </div>
</html:form>
<tags:jQueryValidation formName="configurationForm" isValidate="true"/>