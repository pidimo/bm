<%@ page import="com.jatun.titus.listgenerator.Titus,
                 com.jatun.titus.listgenerator.structure.Module,
                 com.jatun.titus.listgenerator.structure.StructureManager,
                 com.jatun.titus.listgenerator.structure.Table,
                 com.piramide.elwis.utils.ReportConstants" %>
<%@ page import="com.piramide.elwis.utils.SortUtils" %>
<%@ page import="java.util.*" %>
<%@ include file="/Includes.jsp" %>
<c:set var="reportTypeList" value="<%=JSPHelper.getReportType(request)%>"/>
<c:set var="actualModule" value="${reportForm.dtoMap['module']}" scope="request"/>

<c:set var="SOURCETYPE_INTERNAL" value="<%=ReportConstants.SourceType.INTERNAL.getConstantAsString()%>"/>

<%
    String actualModule = (String) request.getAttribute("actualModule");
    StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
    Iterator it = structureManager.getModulesIterator();

    List moduleList = new ArrayList();
    List tablesList = new ArrayList();
    while (it.hasNext()) {
        Map moduleMap = new HashMap();

        Module module = (Module) it.next();

        moduleMap.put("name", module.getName());
        moduleMap.put("resource", JSPHelper.getMessage(request, module.getResource()));

        moduleList.add(moduleMap);

        if (module.getName().equals(actualModule)) {
            Iterator tIt = module.getTableNamesIterator();
            while (tIt.hasNext()) {
                Map tableMap = new HashMap();

                String tableName = (String) tIt.next();

                Table table = structureManager.getTable(tableName);
                tableMap.put("name", table.getName());
                tableMap.put("resource", JSPHelper.getMessage(request, table.getResource()));
                tablesList.add(tableMap);
            }
        }
    }


    request.setAttribute("modules", SortUtils.orderByPropertyMap(moduleList, "resource"));
    request.setAttribute("tables", SortUtils.orderByPropertyMap(tablesList, "resource"));

    List reportStatusSelect = JSPHelper.getReportStatus(request);
    request.setAttribute("reportStatusSelect", reportStatusSelect);

%>


<script language="JavaScript">
    <!--
    function submitForm(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.getElementById('tableId').selectedIndex = 0;
            document.forms[0].submit();
        }
    }
    //-->
</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="dto(name)" styleClass="form-horizontal">

        <%--default source type--%>
        <html:hidden property="dto(sourceType)" value="${SOURCETYPE_INTERNAL}"/>

        <html:hidden property="dto(formMsgCounter)" value="${msgCounter}"/>

        <c:if test="${haveConfirmate == true}">
            <html:hidden property="dto(haveConfirmate)" value="${haveConfirmate}"/>
            <fmt:message key="Common.saveAnyway" var="button" scope="request"/>
        </c:if>
        <c:if test="${null == haveConfirmate || false == haveConfirmate}">
            <html:hidden property="dto(haveConfirmate)" value="false"/>
        </c:if>

        <c:if test="${haveChangeType == true}">
            <html:hidden property="dto(haveChangeType)" value="true"/>
        </c:if>
        <c:if test="${haveChangeColumn == true}">
            <html:hidden property="dto(haveChangeColumn)" value="true"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="REPORT" property="dto(save)"
                                     styleId="saveButtonId" tabindex="1"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="REPORT" property="dto(delete)"
                                     styleId="saveButtonId" tabindex="2"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="3">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div id="Report.jsp" class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name_id">
                            <fmt:message key="Reports.report.reportName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <app:text property="dto(name)" styleId="name_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText" maxlength="150"
                                      view="${op == 'delete'}"
                                      tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="state_id">
                            <fmt:message key="Reports.report.reportStatus"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <html:select property="dto(state)" styleId="state_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="5"
                                         readonly="${op=='delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="reportStatusSelect" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                                <%--<html:checkbox property="dto(state)" disabled="${op == 'delete'}" styleClass="radio" tabindex="4"/>--%>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="reportType_id">
                            <fmt:message key="Reports.report.reportType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <html:select property="dto(reportType)" styleId="reportType_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="6"
                                         readonly="${op=='delete'}">
                                <html:options collection="reportTypeList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="pageSize">
                            <fmt:message key="ReportGenerator.reportPageSize"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <c:set var="pageSizes" value="${app2:getReportPageSizes(pageContext.request)}"
                                   scope="request"/>
                            <html:select property="dto(pageSize)" styleId="pageSize"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         tabindex="7"
                                         readonly="${op=='delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="pageSizes" property="pageSize" labelProperty="pageSizeName"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="module_id">
                            <fmt:message key="Reports.report.reportModule"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <html:select property="dto(module)" styleId="module_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="8"
                                         readonly="${op=='delete'}"
                                         onchange="submitForm(this)">
                                <html:option value=""></html:option>
                                <c:forEach var="item" items="${modules}">
                                    <html:option value="${item.name}">${item.resource}</html:option>
                                </c:forEach>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="pageOrientation">
                            <fmt:message key="ReportGenerator.reportPageOrientation"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <c:set var="pageOrientations" value="${app2:getReportPageOrientations(pageContext.request)}"
                                   scope="request"/>
                            <html:select property="dto(pageOrientation)" styleId="pageOrientation"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         tabindex="9" readonly="${op=='delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="pageOrientations" property="reportPageOrientation"
                                              labelProperty="reportPageOrientationName"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="tableId">
                            <fmt:message key="Reports.report.reportFunctionality"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <html:select property="dto(initialTableReference)"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="10"
                                         readonly="${op=='delete'}" styleId="tableId">
                                <html:option value=""></html:option>
                                <c:forEach var="item" items="${tables}">
                                    <html:option value="${item.name}">${item.resource}</html:option>
                                </c:forEach>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="format">
                            <fmt:message key="ReportGenerator.reportFormat"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <c:set var="reportFormats" value="${app2:getReportFormats(pageContext.request)}"
                                   scope="request"/>
                            <html:select property="dto(reportFormat)" styleId="format"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         tabindex="11"
                                         readonly="${op=='delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="reportFormats" property="reportFormat"
                                              labelProperty="reportFormatName"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="employeeId_id">
                            <fmt:message key="Reports.report.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <fanta:select property="dto(employeeId)" styleId="employeeId_id" listName="employeeBaseList"
                                          labelProperty="employeeName" valueProperty="employeeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          readOnly="${op=='delete'}"
                                          value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts"
                                          tabIndex="12">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                        <%--<TD class="label">

                        </TD>--%>
                    <label class="control-label col-xs-12 label-left" for="descriptionText_id">
                        <fmt:message key="Reports.report.reportDescription"/>
                    </label>

                    <div class="col-xs-12">
                        <html:textarea property="dto(descriptionText)" styleId="descriptionText_id"
                                       styleClass="${app2:getFormInputClasses()} middleDetail"
                                       readonly="${op == 'delete'}"
                                       tabindex="13" style="height:120px;"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(reportId)"/>
                    <html:hidden property="dto(version)"/>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="REPORT" property="dto(save)"
                                     styleId="saveButtonId" tabindex="14"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="REPORT" property="dto(delete)"
                                     styleId="saveButtonId" tabindex="15"
                                     styleClass="${app2:getFormButtonClasses()}">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="16">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <%--<table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
             <TR>
                  <TD class="button">
                     <c:if test="${op == 'create' || op == 'update'}">
                         <html:submit property="dto(save)" styleClass="button" styleId="saveButtonId" tabindex="8" ><c:out value="${button}"/></html:submit>
                     </c:if>
                     <c:if test="${op == 'delete'}">
                         <html:submit property="dto(delete)" styleClass="button" styleId="saveButtonId" tabindex="9" ><fmt:message   key="Common.delete"/></html:submit>
                     </c:if>
                     <html:cancel styleClass="button" tabindex="10" ><fmt:message   key="Common.cancel"/></html:cancel>
                 </TD>
             </TR>
        </table>
        <br>--%>
    </html:form>
</div>
<tags:jQueryValidation formName="reportForm"/>