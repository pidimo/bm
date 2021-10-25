<%@ page import="com.jatun.titus.listgenerator.Titus,
                 com.jatun.titus.listgenerator.structure.Module,
                 com.jatun.titus.listgenerator.structure.StructureManager,
                 com.jatun.titus.listgenerator.structure.Table,
                 com.piramide.elwis.utils.ReportConstants" %>
<%@ page import="com.piramide.elwis.utils.SortUtils" %>
<%@ page import="java.util.*" %>
<%@ include file="/Includes.jsp" %>
<c:set var="reportTypeList" value="<%=JSPHelper.getReportType(request)%>"/>
<c:set var="actualModule" value="${reportJrxmlForm.dtoMap['module']}" scope="request"/>
<c:set var="SOURCETYPE_JRXML" value="<%=ReportConstants.SourceType.JRXML.getConstantAsString()%>"/>

<tags:initBootstrapFile/>

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

    function companySubmitForm() {
        document.getElementById('changeCompany_id').value = 'true';
        document.forms[0].submit();
    }
    //-->
</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" enctype="multipart/form-data" focus="dto(name)" styleClass="form-horizontal">

        <html:hidden property="dto(sourceType)" value="${SOURCETYPE_JRXML}"/>
        <html:hidden property="dto(isChangeCompany)" value="false" styleId="changeCompany_id"/>
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(reportId)"/>
            <html:hidden property="dto(version)"/>
        </c:if>


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
            <c:if test="${not empty reportJrxmlButtonsPath}">
                <c:set var="buttonsTabIndex" value="1" scope="request"/>
                <c:import url="${reportJrxmlButtonsPath}"/>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="3">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}" id="Report.jsp">
            <fieldset>

                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="name_id">
                            <fmt:message key="Reports.report.reportName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op == 'delete')}">
                            <app:text property="dto(name)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}"
                                      maxlength="150"
                                      styleId="name_id"
                                      view="${isReadOnly or op == 'delete'}"
                                      tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="state_id">
                            <fmt:message key="Reports.report.reportStatus"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                            <html:select property="dto(state)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="5"
                                         styleId="state_id"
                                         readonly="${isReadOnly or op=='delete'}">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="reportStatusSelect" property="value" labelProperty="label"/>
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

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                            <html:select property="dto(module)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="6"
                                         styleId="module_id"
                                         readonly="${isReadOnly or op=='delete'}"
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="format">
                            <fmt:message key="ReportGenerator.reportFormat"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                            <c:set var="reportFormats" value="${app2:getReportFormats(pageContext.request)}"
                                   scope="request"/>
                            <html:select property="dto(reportFormat)"
                                         styleId="format"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="7"
                                         readonly="${isReadOnly or op=='delete'}">
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
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="tableId">
                            <fmt:message key="Reports.report.reportFunctionality"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                            <html:select property="dto(initialTableReference)"
                                         styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         tabindex="8"
                                         readonly="${isReadOnly or op=='delete'}"
                                         styleId="tableId">
                                <html:option value=""></html:option>
                                <c:forEach var="item" items="${tables}">
                                    <html:option value="${item.name}">${item.resource}</html:option>
                                </c:forEach>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="jrxmlFile_id">
                            <fmt:message key="Report.jrxml.file"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly || op == 'delete')}">
                            <c:if test="${op!='delete' && !isReadOnly}">
                                <tags:bootstrapFile property="dto(file)"
                                                    tabIndex="9"
                                                    accept=".jrxml"
                                                    styleId="jrxmlFile_id"/>
                            </c:if>

                            <html:hidden property="dto(jrxmlFileName)" write="true"/>

                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="empluyeeId_id">
                            <fmt:message key="Reports.report.employee"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                            <fanta:select property="dto(employeeId)"
                                          listName="employeeBaseList"
                                          labelProperty="employeeName"
                                          styleId="empluyeeId_id"
                                          valueProperty="employeeId"
                                          styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                          readOnly="${isReadOnly or op=='delete'}"
                                          value="${sessionScope.user.valueMap['userAddressId']}" module="/contacts"
                                          tabIndex="10">
                                <fanta:parameter field="companyId"
                                                 value="${not empty reportJrxmlForm.dtoMap['companyId'] ? reportJrxmlForm.dtoMap['companyId'] : -1}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${sessionScope.user.valueMap['isDefaultCompany'] == true}">
                            <div class="${app2:getFormGroupClassesTwoColumns()}">
                                <label class="${app2:getFormLabelClassesTwoColumns()}" for="companyId_id">
                                    <fmt:message key="Report.jrxml.company"/>
                                </label>

                                <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op=='delete')}">
                                    <fanta:select property="dto(companyId)"
                                                  listName="lightCompanyList"
                                                  module="/admin"
                                                  styleId="companyId_id"
                                                  labelProperty="companyName"
                                                  valueProperty="companyId"
                                                  firstEmpty="true"
                                                  styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                                  onChange="companySubmitForm()"
                                                  tabIndex="11"
                                                  readOnly="${isReadOnly or op=='delete'}">
                                        <fanta:parameter field="active" value="1"/>
                                    </fanta:select>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <html:hidden property="dto(companyId)"/>
                        </c:otherwise>
                    </c:choose>
                </div>


                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="descriptionText_id">
                            <fmt:message key="Reports.report.reportDescription"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(isReadOnly or op == 'delete')}">
                            <html:textarea property="dto(descriptionText)"
                                           styleClass="middleDetail ${app2:getFormInputClasses()}"
                                           styleId="descriptionText_id"
                                           readonly="${isReadOnly or op == 'delete'}"
                                           tabindex="12" style="height:120px;width:100%;"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${not empty reportJrxmlButtonsPath}">
                <c:set var="buttonsTabIndex" value="14" scope="request"/>
                <c:import url="${reportJrxmlButtonsPath}"/>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="13">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </html:form>
</div>

<tags:jQueryValidation formName="reportJrxmlForm"/>