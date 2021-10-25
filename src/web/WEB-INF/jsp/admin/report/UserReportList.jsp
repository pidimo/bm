<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("languageList", JSPHelper.getLanguageList(request));
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<html:form action="/Report/UserList/Execute.do" focus="parameter(type)" styleId="userReportList"
           styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <fmt:message key="Admin.Report.UserList"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="User.typeUser"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:select property="parameter(type)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 styleId="type"
                                 tabindex="1">
                        <html:option value=""/>
                        <html:options collection="typeUserList"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </div>
            </div>

            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="User.language"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:select property="parameter(favoriteLanguage)"
                                 styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                 tabindex="2">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="languageList"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Common.active"/>
                </label>

                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <c:choose>
                        <c:when test="${userReportListForm.params.active==null}">
                            <html:select property="parameter(active)"
                                         value="1"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="3">
                                <html:options collection="activeList"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </c:when>
                        <c:otherwise>
                            <html:select property="parameter(active)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="4">
                                <html:options collection="activeList"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <c:set var="reportFormats" value="${userReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userReportListForm.pageSizes}" scope="request"/>
        <titus:reportGroupSortTag tableStyleClass="${app2:getTableClasesIntoForm()}" mode="bootstrap" width="100%">
            <titus:reportGroupSortColumnTag name="userName" labelKey="User.userName"/>
            <titus:reportGroupSortColumnTag name="login" labelKey="User.login"/>
        </titus:reportGroupSortTag>
        <tags:bootstrapReportOptionsTag/>


        <c:set var="myActive"><fmt:message key="Common.active"/></c:set>
        <c:set var="unactive"><fmt:message key="Common.inactive"/></c:set>
        <c:set var="internal"><fmt:message key="User.intenalUser"/></c:set>
        <c:set var="external"><fmt:message key="User.externalUser"/></c:set>
        <c:set var="default"><fmt:message key="Common.default"/></c:set>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="userReportList" title="Admin.Report.UserList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="userName" resourceKey="User.userName" type="${FIELD_TYPE_STRING}" width="45"
                              fieldPosition="1"/>
        <titus:reportFieldTag name="login" resourceKey="User.login" type="${FIELD_TYPE_STRING}" width="25"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="active" resourceKey="Common.active" type="${FIELD_TYPE_STRING}" width="10"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource active [${myActive}] [${unactive}] [1] [0]"
                              fieldPosition="3"/>

        <titus:reportFieldTag name="type" resourceKey="User.typeUser" type="${FIELD_TYPE_STRING}" width="20"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource type [${internal}] [${external}] [1] [0]"
                              fieldPosition="4"/>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="button ${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button ${app2:getFormButtonCancelClasses()}"
                     onclick="formReset('userReportList')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>
    <%--<titus:reportFieldTag name="default" resourceKey="Common.default"  type="${FIELD_TYPE_STRING}" width="15"
conditionMethod="com.piramide.elwis.utils.ReportHelper.getDefault default [1] [${default}]" fieldPosition="5" />--%>
</html:form>
