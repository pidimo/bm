<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<%
    pageContext.setAttribute("publishedList", JSPHelper.getPublishedQuestionList(request));
%>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Support.Report.QuestionList"/></td>
</tr>

<html:form action="/Report/QuestionList/Execute.do" focus="parameter(createUserName)" styleId="supportStyle">
<TR>
    <TD width="15%" class="label"><fmt:message key="Question.askedBy"/></TD>
    <TD class="contain" width="32%">
        <html:hidden property="parameter(createUserId)" styleId="fieldViewUserId_id"/>
        <app:text property="parameter(createUserName)" styleClass="mediumText" maxlength="40" tabindex="1"
                  styleId="fieldViewUserName_id" readonly="true"/>
        <tags:selectPopup url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                          name="searchUser"
                          titleKey="Scheduler.grantAccess.searchUser"
                          width="630"
                          heigth="480"
                          imgWidth="17"
                          imgHeight="19"
                />

        <tags:clearSelectPopup keyFieldId="fieldViewUserId_id"
                               nameFieldId="fieldViewUserName_id"
                               titleKey="Common.clear"/>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="Question.AskedOn"/>
    </td>
    <TD class="contain" width="38%">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startRange)" maxlength="10" tabindex="4" styleId="startRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endRange)" maxlength="10" tabindex="5" styleId="endRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Article.productName"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(productId)" styleId="field_key"/>
        <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
        <html:hidden property="parameter(2)" styleId="field_unitId"/>
        <html:hidden property="parameter(3)" styleId="field_price"/>

        <app:text property="parameter(productName)" styleId="field_name" styleClass="mediumText" maxlength="40"
                  readonly="true" tabindex="2"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>
    </TD>
    <TD class="label">
        <fmt:message key="Article.published"/>
    </td>
    <TD class="contain">
        <html:select property="parameter(published)" styleClass="select" style="width:100" tabindex="6">
            <html:option value=""/>
            <html:options collection="publishedList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Article.category"/></TD>
    <TD class="contain" colspan="3">
        <fanta:select property="parameter(categoryId)" listName="articleCategoryList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="mediumSelect"
                      tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:tree columnId="id" columnParentId="parentCategoryId" separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
        </fanta:select>
    </TD>
</TR>

<c:set var="reportFormats" value="${questionReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${questionReportListForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="summary" labelKey="Question.summary"/>
            <titus:reportGroupSortColumnTag name="categoryName" labelKey="Article.categoryName"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName"/>
            <titus:reportGroupSortColumnTag name="ownerName" labelKey="Question.askedBy"/>
            <titus:reportGroupSortColumnTag name="creationDate" labelKey="Question.AskedOn" isDate="true"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>


<tr>
    <td class="button" colspan="4">
        <html:submit styleClass="button" tabindex="57"><fmt:message key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('supportStyle')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>
</tr>

<c:set var="progress"><fmt:message key="Task.InProgress"/></c:set>
<c:set var="noInit"><fmt:message key="Task.notInit"/></c:set>
<c:set var="concluded"><fmt:message key="Scheduler.Task.Concluded"/></c:set>
<c:set var="deferred"><fmt:message key="Task.Deferred"/></c:set>
<c:set var="toCheck"><fmt:message key="Task.ToCheck"/></c:set>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>

<titus:reportInitializeConstantsTag/>
<titus:reportTag id="questionReportList" title="Support.Report.QuestionList"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="summary" resourceKey="Question.summary" type="${FIELD_TYPE_STRING}" width="25"
                      fieldPosition="1"/>
<titus:reportFieldTag name="categoryName" resourceKey="Article.categoryName" type="${FIELD_TYPE_STRING}" width="15"
                      fieldPosition="3"/>
<titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}" width="15"
                      fieldPosition="4"/>
<titus:reportFieldTag name="ownerName" resourceKey="Question.askedBy" type="${FIELD_TYPE_STRING}" width="25"
                      fieldPosition="5"/>
<titus:reportFieldTag name="creationDate" resourceKey="Question.AskedOn" type="${FIELD_TYPE_DATELONG}" width="20"
                      patternKey="dateTimePattern" fieldPosition="6"/>

</html:form>
</table>
