<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<tags:initSelectPopupEven fields="user_key, user_name"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="90%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Support.Report.ArticleList"/></td>
</tr>

<html:form action="/Report/ArticleList/Execute.do" focus="parameter(createUserName)" styleId="supportStyle">
<TR>
    <TD width="15%" class="label"><fmt:message key="Article.ownerName"/></TD>
    <TD class="contain" width="30%">
        <html:hidden property="parameter(createUserId)" styleId="user_key"/>
        <app:text property="parameter(createUserName)" styleClass="mediumText" maxlength="40" tabindex="1"
                  styleId="user_name" readonly="true"/>
        <tags:selectPopup url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                          name="searchUser"
                          titleKey="Scheduler.grantAccess.searchUser"
                          width="630"
                          heigth="480"
                          imgWidth="17"
                          imgHeight="19" />
        <tags:clearSelectPopup keyFieldId="user_key" nameFieldId="user_name" titleKey="Common.clear"/>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="Common.creationDate"/>
    </td>
    <TD class="contain" width="40%">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startRange)" maxlength="10" tabindex="6" styleId="startDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endRange)" maxlength="10" tabindex="7" styleId="endDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Article.changeName"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(updateUserId)" styleId="fieldViewUserId_id"/>
        <app:text property="parameter(updateUserName)" styleClass="mediumText" maxlength="40" tabindex="2"
                  styleId="fieldViewUserName_id" readonly="true"/>
        <tags:selectPopup url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                          name="searchUser"
                          titleKey="Scheduler.grantAccess.searchUser"
                          width="630"
                          heigth="480"
                          imgWidth="17"
                          imgHeight="19" />

        <tags:clearSelectPopup keyFieldId="fieldViewUserId_id"
                               nameFieldId="fieldViewUserName_id"
                               titleKey="Common.clear"/>

    </TD>
    <TD class="label">
        <fmt:message key="Article.changeDate"/>
    </td>
    <TD class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startChangeRange)" maxlength="10" tabindex="8" styleId="startChangeRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
                      &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endChangeRange)" maxlength="10" tabindex="9" styleId="endChangeRange"
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
                  readonly="true" tabindex="3"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear"/>
    </TD>
    <TD class="label">
        <fmt:message key="Article.lastVisit"/>
    </td>
    <TD class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startVisitRange)" maxlength="10" tabindex="10" styleId="startVisitRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endVisitRange)" maxlength="10" tabindex="11" styleId="endVisitRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Article.categoryName"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(categoryId)" listName="articleCategoryList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="mediumSelect"
                      readOnly="${op == 'delete'}" tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:tree columnId="id" columnParentId="parentCategoryId" separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
        </fanta:select>
    </TD>
    <TD class="label"><fmt:message key="Article.readyBy"/></TD>
    <TD class="contain">
            <fmt:message key="Common.from"/>
            &nbsp;
        <html:text property="parameter(view1)" styleClass="numberText" tabindex="13"  />
        &nbsp;
            <fmt:message key="Common.to"/>
        &nbsp;
        <html:text property="parameter(view2)" styleId="view2" styleClass="numberText" tabindex="14"/>
    </TD>
</TR>

<c:set var="reportFormats" value="${articleReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${articleReportListForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="number" labelKey="Article.number"/>
            <titus:reportGroupSortColumnTag name="articleTitle" labelKey="Article.title"/>
            <titus:reportGroupSortColumnTag name="categoryName" labelKey="Article.categoryName"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Article.productName"/>
            <titus:reportGroupSortColumnTag name="ownerName" labelKey="Article.ownerName"/>
            <titus:reportGroupSortColumnTag name="creationDate" labelKey="Article.createDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="changeDate" labelKey="Article.changeDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="changeName" labelKey="Article.changeName"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>

<tr>
    <td colspan="4" class="button">
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
<titus:reportTag id="articleReportList" title="Support.Report.ArticleList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="number" resourceKey="Article.number" type="${FIELD_TYPE_STRING}" width="7"
                      fieldPosition="1"/>
<titus:reportFieldTag name="articleTitle" resourceKey="Article.title" type="${FIELD_TYPE_STRING}" width="12"
                      fieldPosition="2"/>
<titus:reportFieldTag name="categoryName" resourceKey="Article.categoryName" type="${FIELD_TYPE_STRING}" width="9"
                      fieldPosition="3"/>
<titus:reportFieldTag name="productName" resourceKey="Article.productName" type="${FIELD_TYPE_STRING}" width="14"
                      fieldPosition="4"/>
<titus:reportFieldTag name="ownerName" resourceKey="Article.ownerName" type="${FIELD_TYPE_STRING}" width="18"
                      fieldPosition="5"/>
<titus:reportFieldTag name="creationDate" resourceKey="Article.createDate" type="${FIELD_TYPE_DATELONG}" width="10"
                      patternKey="dateTimePattern" fieldPosition="6"/>
<titus:reportFieldTag name="changeDate" resourceKey="Article.changeDate" type="${FIELD_TYPE_DATELONG}" width="12"
                      patternKey="dateTimePattern" fieldPosition="7"/>
<titus:reportFieldTag name="changeName" resourceKey="Article.changeName" type="${FIELD_TYPE_STRING}" width="18"
                      fieldPosition="8"/>
</html:form>
</table>
