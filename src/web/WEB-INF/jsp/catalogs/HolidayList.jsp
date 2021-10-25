<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>
<%
    String[] monthNames = JSPHelper.getOnlyMonthNames(request);
    String[] dayNames = JSPHelper.getOnlyDayNames(request, true);
    String[] numberWeeks = JSPHelper.getOnlyNumberWeekList(request);
    String ruleDateType = JSPHelper.getMessage(request, "holiday.rule.dateType");
    String ruleDayWeekType = JSPHelper.getMessage(request, "holiday.rule.dayWeekType");
%>
<app2:jScriptUrl url="obj" var="jsJumpUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(countryId)" value="field"/>
</app2:jScriptUrl>
<script language="JavaScript">
    <!--
    function jump(obj) {
        field = document.getElementById('searchForm').country.value;
        holidayField = document.getElementById('searchForm').holiday.value;
        window.location =${jsJumpUrl};
    }
    //-->

</script>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Holiday/List.do" styleClass="form-horizontal" styleId="searchForm"
               focus="parameter(countryId)">
        <fieldset>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="">
                    <fmt:message key="Contact.country"/>
                    <fmt:message key="holiday.select.indepent" var="indepent"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <fanta:select property="parameter(countryId)" tabIndex="1" listName="countryList"
                                  labelProperty="name"
                                  valueProperty="id" firstEmpty="true"
                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                  styleId="country">
                        <fanta:addItem key="indepent" value="${indepent}"/>
                        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    </fanta:select>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="">
                    <fmt:message key="Common.title"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:text property="parameter(title)" styleClass="form-control"
                               titleKey="Common.title"
                               tabindex="2"
                               styleId="holiday"/>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchButton()}">
                <div class="col-sm-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </fieldset>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Holiday/List.do" parameterName="titleAlpha"
                            onClick="jump(this);return false;" mode="bootstrap"/>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
                <app2:checkAccessRight functionality="HOLIDAY" permission="CREATE">
                    <html:submit property="parameter(new)" styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </app2:checkAccessRight>
        </div>
    </html:form>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%" styleClass="${app2:getFantabulousTableClases()}" id="Holiday"
                     action="Holiday/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Holiday/Forward/Update.do?dto(holidayId)=${Holiday.id}&dto(title)=${app2:encode(Holiday.title)}"/>
            <c:set var="deleteAction"
                   value="Holiday/Forward/Delete.do?dto(holidayId)=${Holiday.id}&dto(title)=${app2:encode(Holiday.title)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="HOLIDAY" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="HOLIDAY" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="holiday.title"
                              headerStyle="listHeader" width="40%" orderable="true" maxLength="25"/>
            <fanta:dataColumn name="country" styleClass="listItem" title="City.country" headerStyle="listHeader"
                              width="20%" orderable="true"/>
            <fanta:dataColumn name="" styleClass="listItem2" title="holiday.rule" headerStyle="listHeader"
                              width="35%" orderable="false" renderData="false">
                <%
                    try {
                        Map holiday = (Map) pageContext.getAttribute("Holiday");
                        String day = (String) holiday.get("day");
                        String month;
                        String occurrence = (String) holiday.get("occurrence");
                        String type = (String) holiday.get("type");
                        String moveToMonday = (String) holiday.get("moveToMonday");

                        if (occurrence == null) {
                            occurrence = "";
                        }

                        month = monthNames[Integer.parseInt((String) holiday.get("month")) - 1];

                        String ruleKey = ruleDateType;
                        String occurrenceKey = "\\{year\\}";


                        if ("1".equals(type)) {
                            //dayWeekType
                            ruleKey = ruleDayWeekType;
                            occurrenceKey = "\\{week\\}";
                            day = dayNames[Integer.parseInt(day) - 1];

                            if (occurrence.length() > 0) {
                                occurrence = numberWeeks[Integer.parseInt(occurrence) - 1];
                            }
                        }

                        out.print(ruleKey.replaceFirst("\\{day\\}", day).replaceFirst("\\{month\\}", month).replaceFirst(
                                occurrenceKey, occurrence));

                        if ("1".equals(moveToMonday)) {
                            out.print("&nbsp;(");
                            out.print(JSPHelper.getMessage(request, "holiday.celebration"));
                            out.print(")");
                        }
                    } catch (Exception e) {
                    }
                %>
                &nbsp;
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>