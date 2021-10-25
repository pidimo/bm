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
    function selectByDate(){
        var sel = document.getElementById('holidayType');
        sel.selectedIndex=1;
        selectRule(sel);
    }
    function selectRule(selected) {
        var sel = selected.value;
        var myHide=true;
        if (sel == '0') {
            document.getElementById('byDate').style.display = "";
            document.getElementById('byOccurrence').style.display = "none";
            myHide=false;
            enableRule(false);
        }else if(sel=='1'){
            document.getElementById('byOccurrence').style.display = "";
            document.getElementById('byDate').style.display = "none";
            myHide=false;
            enableRule(true);
        }
        if(myHide){
            document.getElementById('byOccurrence').style.display = "none";
            document.getElementById('byDate').style.display = "none";

        }
    }
</script>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}" focus="dto(title)">
<html:hidden property="dto(op)" value="${op}"/>
<c:if test="${('update' == op) || (isDelete)}">
    <html:hidden property="dto(holidayId)"/>
</c:if>
<c:if test="${'update' == op}">
    <html:hidden property="dto(version)"/>
</c:if>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2" class="title" width="100%">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="30%">
            <fmt:message key="holiday.title"/>
        </td>
        <td class="contain" width="70%">
            <app:text property="dto(title)" styleClass="largetext" maxlength="40" view="${isDelete}" tabindex="1"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="30%">
            <fmt:message key="City.country"/>
        </td>
        <td class="contain" width="70%">
            <fanta:select property="dto(countryId)" listName="countryList" tabIndex="2" labelProperty="name"
                          valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${isDelete}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>
    </tr>
    <tr>
        <td class="label" width="30%">
            <fmt:message key="holiday.type"/>
        </td>
        <td class="contain" width="70%">
            <html:select property="dto(holidayType)" styleId="holidayType" styleClass="shortSelect" tabindex="3" readonly="${isDelete}" onchange="selectRule(this)">
                <html:option value=""/>
                <html:options collection="holidayType" property="value" labelProperty="label"/>
            </html:select>
        </td>
    </tr>
    <tr id="byDate" ${HolidayForm.dtoMap.holidayType == '0' ? "":"style=\"display: none;\""}>
        <td class="label" width="30%">&nbsp;</td>
        <td class="contain" width="70%">
            <html:select property="dto(dayA)" styleClass="tinySelect" tabindex="4" readonly="${isDelete}"
                         styleId="ruleTypeA2">
                <html:option value=""/>
                <html:options name="monthDays"/>
            </html:select>
            &nbsp;
            <html:select property="dto(monthA)" styleClass="shortSelect" tabindex="5" readonly="${isDelete}"
                         styleId="ruleTypeA3">
                <html:option value=""/>
                <html:options collection="monthNames" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;
            <app:text property="dto(occurrenceA)" styleClass="numberText" maxlength="4" view="${isDelete}" tabindex="6"
                      styleId="ruleTypeA4"/>
            <c:if test="${!isDelete}">
                <br>
                <i><fmt:message key="holiday.byDate.msg"/></i>
            </c:if>
        </td>
    </tr>
    <tr id="byOccurrence" ${HolidayForm.dtoMap.holidayType == '1' ? "":"style=\"display: none;\""}>
        <td class="label" width="30%">&nbsp;</td>
        <td  class="contain" width="70%">
            <html:select property="dto(occurrenceB)" styleClass="tinySelect" tabindex="4" readonly="${isDelete}"
                         styleId="ruleTypeB4">
                <html:option value=""/>
                <html:options collection="numberWeeks" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;
            <html:select property="dto(dayB)" styleClass="shortSelect" tabindex="5" readonly="${isDelete}"
                         styleId="ruleTypeB2">
                <html:option value=""/>
                <html:options collection="dayNames" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;
            <html:select property="dto(monthB)" styleClass="shortSelect" tabindex="6" readonly="${isDelete}"
                         styleId="ruleTypeB3">
                <html:option value=""/>
                <html:options collection="monthNames" property="value" labelProperty="label"/>
            </html:select>
            &nbsp;
        </td>
    </tr>
    <tr>
        <td class="label" width="30%">
            <fmt:message key="holiday.celebration"/>
        </td>
        <td class="contain" width="70%">
            <html:checkbox property="dto(moveToMonday)" styleClass="radio" disabled="${isDelete}" tabindex="11"
                           value="true"/>
            <fmt:message key="holiday.celebration.msg"/>
        </td>
    </tr>
</table>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <tr>
        <td class="button">
            <c:if test="${op == 'create' || op=='update'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY" styleClass="button" tabindex="12">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY" styleClass="button" tabindex="13">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="HOLIDAY" styleClass="button" tabindex="14"
                                     property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>
            <input type="hidden" name="cancel" value="cancel"/>
            <html:cancel styleClass="button" tabindex="15">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
<html:hidden property="dto(op)" value="${op}"/>
<c:if test="${op=='update' || op=='delete'}">
    <html:hidden property="dto(taskTypeId)"/>
    <html:hidden property="dto(version)"/>
</c:if>
</html:form>
</td>
</tr>
</table>
