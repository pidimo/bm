<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("monthNames", JSPHelper.getMonthNames(request));
    pageContext.setAttribute("dayNames", JSPHelper.getDayNames(request, true));
    pageContext.setAttribute("numberWeeks", JSPHelper.getNumberWeekList(request));
    pageContext.setAttribute("monthDays", JSPHelper.getMonthDays(request));
    pageContext.setAttribute("holidayType", JSPHelper.getHolidayTypes(request));
%>
<c:set var="isDelete" value="${'delete' == op}"/>
<script>
    function enableRule(enable) {
        var type = "A";

        if (!enable) {
            type = "B";
        }

        for (i = 2; i < 5; i++) {
            document.getElementById('ruleTypeA' + "" + i).disabled = enable;
            document.getElementById('ruleTypeB' + "" + i).disabled = !enable;
            document.getElementById('ruleType' + type + i).value = "";
        }
    }
    function selectByDate() {
        var sel = document.getElementById('holidayType');
        sel.selectedIndex = 1;
        selectRule(sel);
    }
    function selectRule(selected) {
        var sel = selected.value;
        var myHide = true;
        if (sel == '0') {
            document.getElementById('byDate').style.display = "";
            document.getElementById('byOccurrence').style.display = "none";
            myHide = false;
            enableRule(false);
        } else if (sel == '1') {
            document.getElementById('byOccurrence').style.display = "";
            document.getElementById('byDate').style.display = "none";
            myHide = false;
            enableRule(true);
        }
        if (myHide) {
            document.getElementById('byOccurrence').style.display = "none";
            document.getElementById('byDate').style.display = "none";

        }
    }
</script>


<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${('update' == op) || (isDelete)}">
                    <html:hidden property="dto(holidayId)"/>
                </c:if>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="title_id">
                        <fmt:message key="holiday.title"/>
                    </label>

                    <div class="${app2:getFormContainClasses(isDelete)}">
                        <app:text property="dto(title)" styleId="title_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="40"
                                  view="${isDelete}"
                                  tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="countryId_id">
                        <fmt:message key="City.country"/>
                    </label>

                    <div class="${app2:getFormContainClasses(isDelete)}">
                        <fanta:select property="dto(countryId)" listName="countryList" tabIndex="2" labelProperty="name"
                                      valueProperty="id" firstEmpty="true" styleId="countryId_id"
                                      styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                      readOnly="${isDelete}">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="holidayType">
                        <fmt:message key="holiday.type"/>
                    </label>

                    <div class="${app2:getFormContainClasses(isDelete)}">
                        <html:select property="dto(holidayType)" styleId="holidayType"
                                     styleClass="${app2:getFormSelectClasses()} shortSelect" tabindex="3"
                                     readonly="${isDelete}" onchange="selectRule(this)">
                            <html:option value=""/>
                            <html:options collection="holidayType" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div id="byDate"
                     class="${app2:getFormGroupClasses()}" ${HolidayForm.dtoMap.holidayType == '0' ? "":"style=\"display: none;\""}>
                    <label class="${app2:getFormLabelClasses()}">

                    </label>

                    <div class="col-xs-11 col-sm-3 wrapperButton">
                        <html:select property="dto(dayA)" styleClass="${app2:getFormSelectClasses()}" tabindex="4"
                                     readonly="${isDelete}"
                                     styleId="ruleTypeA2">
                            <html:option value=""/>
                            <html:options name="monthDays"/>
                        </html:select>
                    </div>
                    <div class="col-xs-11 col-sm-3 wrapperButton">
                        <html:select property="dto(monthA)" styleClass="${app2:getFormSelectClasses()}" tabindex="5"
                                     readonly="${isDelete}"
                                     styleId="ruleTypeA3">
                            <html:option value=""/>
                            <html:options collection="monthNames" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                    <div class="col-xs-11 col-sm-2 wrapperButton">
                        <app:text property="dto(occurrenceA)" styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4" view="${isDelete}"
                                  tabindex="6"
                                  styleId="ruleTypeA4"/>
                    </div>
                    <div class="col-sm-offset-3 col-xs-11 col-sm-8">
                        <label class="text-left">
                            <c:if test="${!isDelete}">
                                <fmt:message key="holiday.byDate.msg"/>
                            </c:if>
                        </label>
                    </div>
                </div>
                <div id="byOccurrence"
                     class="${app2:getFormGroupClasses()}" ${HolidayForm.dtoMap.holidayType == '1' ? "":"style=\"display: none;\""}>
                    <label class="${app2:getFormLabelClasses()}">

                    </label>

                    <div class="col-xs-11 col-sm-2 wrapperButton">
                        <html:select property="dto(occurrenceB)" styleClass="${app2:getFormSelectClasses()}"
                                     tabindex="4"
                                     readonly="${isDelete}"
                                     styleId="ruleTypeB4">
                            <html:option value=""/>
                            <html:options collection="numberWeeks" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                    <div class="col-xs-11 col-sm-3 wrapperButton">
                        <html:select property="dto(dayB)" styleClass="${app2:getFormSelectClasses()}" tabindex="5"
                                     readonly="${isDelete}"
                                     styleId="ruleTypeB2">
                            <html:option value=""/>
                            <html:options collection="dayNames" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                    <div class="col-xs-11 col-sm-3 wrapperButton">
                        <html:select property="dto(monthB)" styleClass="${app2:getFormSelectClasses()}" tabindex="6"
                                     readonly="${isDelete}"
                                     styleId="ruleTypeB3">
                            <html:option value=""/>
                            <html:options collection="monthNames" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="moveToMonday_id">
                        <fmt:message key="holiday.celebration"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="col-xs-1">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(moveToMonday)" styleId="moveToMonday_id" styleClass="radio"
                                                   disabled="${isDelete}" tabindex="11"
                                                   value="true"/>
                                    <label for="moveToMonday_id"></label>
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-10">
                            <fmt:message key="holiday.celebration.msg"/>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op=='update'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="12">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="13">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="14"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <input type="hidden" name="cancel" value="cancel"/>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="15">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
            <html:hidden property="dto(op)" value="${op}"/>
            <c:if test="${op=='update' || op=='delete'}">
                <html:hidden property="dto(taskTypeId)"/>
                <html:hidden property="dto(version)"/>
            </c:if>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="HolidayForm"/>