<%@ include file="/Includes.jsp" %>

<script>
    function select(id, name) {
        parent.selectField('projectId_id', id, 'projectName_id', name);
    }
</script>

<html:form action="/Project/ProjectListPopUp.do" focus="parameter(projectName)" styleClass="form-horizontal">

    <div class="${app2:getFormGroupClasses()}">
        <fieldset>
            <label class="${app2:getFormLabelOneSearchInput()}">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(projectName)" styleClass="largeText ${app2:getFormInputClasses()}"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
                </div>
            </div>
        </fieldset>
    </div>

</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Project/ProjectListPopUp.do" mode="bootstrap" parameterName="projectNameAlpha"/>
</div>


<div class="table-responsive">
    <fanta:table mode="bootstrap" align="center"
                 styleClass="${app2:getFantabulousTableClases()}"
                 id="project"
                 action="Project/ProjectListPopUp.do"
                 imgPath="${baselayout}">

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <fanta:actionColumn name="" title="Common.select"
                                useJScript="true"
                                action="javascript:select('${app2:mapElement(project, 'projectId')}', '${app2:jscriptEncode(app2:mapElement(project, 'projectName'))}');"
                                styleClass="listItem" headerStyle="listHeader" width="100%"
                                glyphiconClass="${app2:getClassGlyphImport()}"/>
        </fanta:columnGroup>

        <fanta:dataColumn name="projectName" styleClass="listItem"
                          title="Project.name" headerStyle="listHeader" width="20%"
                          orderable="true"/>
        <fanta:dataColumn name="customerName" styleClass="listItem" title="Project.customer"
                          headerStyle="listHeader" width="20%" orderable="true"/>
        <fanta:dataColumn name="userName" styleClass="listItem" title="Project.responsible"
                          headerStyle="listHeader" width="15%" orderable="true"/>
    </fanta:table>
</div>
