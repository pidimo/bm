<%@ include file="/Includes.jsp" %>

<c:set var="tabIndexNumber" value="5"/>
<c:set var="currentMillis" value="${app2:getCurrentTimeMillis()}" scope="request"/>

<tags:initBootstrapFile/>

<app2:jScriptUrl url="/contacts/DataImport/AddColumn.do" var="jsAddRowUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="position" value="positionId"/>
    <app2:jScriptUrlParam param="dtoKey" value="dtoKeyId"/>
</app2:jScriptUrl>

<%--fragment to add progress bar--%>
<c:set var="isImportProgressBar" value="true" scope="request"/>
<c:import url="/WEB-INF/jsp/contacts/checkDuplicateProgressBarFragment.jsp"/>

<script type="text/javascript" language="JavaScript">

    function addRow(sourceId) {
        initializeAjaxColumnsDIV();

        var positionCounter = document.getElementById("columnCounterId").value;
        if (positionCounter == undefined) {
            positionCounter = new Number(0);
        }

        var sourceBox = document.getElementById(sourceId);

        for (var i = 0; i < sourceBox.length; i++) {
            var option = sourceBox.options[i];

            if (option.selected) {
                positionCounter = new Number(positionCounter) + 1;
                document.getElementById("columnCounterId").value = positionCounter;

                var positionId = positionCounter;
                var dtoKeyId = option.value + '_' + positionCounter;

                var jsParams = buildJsParameters(option.text, document.getElementById("mainGroupName_" + option.value + "_Id").value);
                makePOSTAjaxRequest(${jsAddRowUrl}, jsParams);
            }
        }
    }

    function recoverRow(dtoKey, position, columnName, groupName) {
        var positionId = position;
        var dtoKeyId = dtoKey;

        var jsParams = buildJsParameters(columnName, groupName);
        makePOSTAjaxRequest(${jsAddRowUrl}, jsParams);
    }

    function buildJsParameters(columnName, groupName) {
        var params = 'columnName=' + encodeURIComponent(columnName) + '&' + 'groupName=' + encodeURIComponent(groupName);
        return params;
    }

    function makePOSTAjaxRequest(urlAddress, parameters) {
        $.ajax({
            async: false,
            type: "POST",
            dataType: "html",
            data: parameters,
            url: urlAddress,
            beforeSend: setLoadMessage,
            complete: hideLoadMessage,
            success: function (data) {
                processData(data);
            },
            error: function (ajaxRequest) {
                ajaxErrorProcess(ajaxRequest.status);
            }
        });
    }

    function hideLoadMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.style.display = 'none';
    }

    function processData(data) {
        $('#ajaxColumnsId').append(data);
    }

    function initializeAjaxColumnsDIV() {
        if (0 == jQuery.trim($('#ajaxColumnsId').html()).length)
            document.getElementById("columnCounterId").value = 0;
    }
    function setLoadMessage() {
        var messagesDiv = document.getElementById('ajaxMessageId');
        messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.message.loading',pageContext.request)}';
        messagesDiv.style.display = 'inline';
    }

    function ajaxErrorProcess(status) {
        var messagesDiv = document.getElementById('ajaxMessageId');
        if (status == 404 || status == 302) {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('Common.sessionExpired',pageContext.request)}';
        } else {
            messagesDiv.innerHTML = '${app2:buildAJAXMessage('error.tooltip.unexpected',pageContext.request)}';
        }
        messagesDiv.style.display = 'inline';
    }


    function removeColumn(dtoKey) {
        var keyParts = dtoKey.split("_");
        var uiPosition = keyParts[2];
        if (uiPosition == document.getElementById("columnCounterId").value) {
            uiPosition = uiPosition - 1;
            document.getElementById("columnCounterId").value = uiPosition;
        }
        $('#column_' + dtoKey).remove();
    }

    function changeConfigurationType() {
        document.getElementById('changeConfigurationTypeId').value = 'true';
        document.getElementById('dataImportFormId').submit();
    }

    function changeProfile(box) {
        var selectedIndex = box.selectedIndex;

        var option = box.options[selectedIndex];

        if ('' != option.value) {
            document.getElementById("selectedProfileLabelId").value = option.text;
            document.getElementById("changeProfileId").value = 'true';
            document.getElementById('dataImportFormId').submit();
        }
    }

    function startCheckProgressBar() {
        if ($("#idCheckDuplicate").prop("checked")) {
            showCheckDuplicateProgressBar();
        }
    }

</script>

<html:form action="${action}"
           enctype="multipart/form-data"
           styleId="dataImportFormId"
           focus="dto(label)"
           styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(importStartTime)" value="${currentMillis}" styleId="importStartTime_key"/>

    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(profileId)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <%-- used to store the selected profile label --%>
    <html:hidden property="dto(selectedProfileLabel)" styleId="selectedProfileLabelId"/>

    <%-- used to detect if the user select another profile --%>
    <html:hidden property="dto(changeProfile)" value="false" styleId="changeProfileId"/>

    <html:hidden property="dto(changeConfigurationType)" value="false" styleId="changeConfigurationTypeId"/>
    <html:hidden property="dto(selectedColumsIdentifiers)" value="" styleId="selectedColumsIdentifiersId"/>

    <html:hidden property="dto(columnCounter)" styleId="columnCounterId"/>

    <div class="${app2:getFormWrapperTwoColumns()}">

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                <html:submit property="saveButton" styleClass="${app2:getFormButtonClasses()}">
                    <c:out value="${button}"/>
                </html:submit>

                <c:if test="${'delete' != op}">
                    <html:submit property="saveAndImportButton" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="startCheckProgressBar();">
                        <fmt:message key="dataImport.saveAndImport"/>
                    </html:submit>
                    <%--<html:submit property="importButton" styleClass="button">
                        <fmt:message key="Common.import"/>
                    </html:submit>--%>
                </c:if>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                    ${title}
            </legend>
                <%--
                                    <tr>
                                        <td class="label">
                                            <fmt:message key="dataImport.configuration"/>
                                        </td>
                                        <td class="contain">
                                            <fanta:select property="dto(profileId)"
                                                          listName="importProfileList"
                                                          labelProperty="label"
                                                          valueProperty="profileId"
                                                          firstEmpty="true"
                                                          styleClass="middleSelect"
                                                          tabIndex="1"
                                                          onChange="javascript:changeProfile(this);">
                                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                                <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                                            </fanta:select>

                                        </td>
                                    </tr>
                --%>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="label_id">
                        <fmt:message key="dataImport.configuration.name"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                        <app:text property="dto(label)"
                                  styleClass="${app2:getFormInputClasses()} middleText"
                                  view="${op == 'delete'}"
                                  maxlength="100"
                                  tabindex="2"
                                  styleId="label_id"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="profileType_id">
                        <fmt:message key="dataImport.configuration.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                        <c:set var="importTypes" value="${app2:getImportTypes(pageContext.request)}"/>
                        <html:select property="dto(profileType)"
                                     styleClass="${app2:getFormSelectClasses()} middleSelect"
                                     readonly="${'delete' == op}"
                                     onchange="javascript:changeConfigurationType();"
                                     tabindex="3"
                                     styleId="profileType_id">
                            <html:option value=""/>
                            <html:options collection="importTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="skipFirstRow_id">
                        <fmt:message key="dataImport.configuration.skipHeader"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(skipFirstRow)"
                                               styleClass="radio"
                                               disabled="${op == 'delete'}"
                                               value="true"
                                               tabindex="4"
                                               styleId="skipFirstRow_id"/>
                                <label for="skipFirstRow_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="idCheckDuplicate">
                        <fmt:message key="dataImport.configuration.checkDuplicate"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(checkDuplicate)"
                                               styleId="idCheckDuplicate"
                                               styleClass="radio"
                                               disabled="${op == 'delete'}"
                                               value="true"
                                               tabindex="4"/>
                                <label for="idCheckDuplicate"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>

            <c:if test="${op != 'delete'}">
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="file_id">
                            <fmt:message key="dataImport.File"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(false)}">
                            <tags:bootstrapFile property="dto(file)"
                                                tabIndex="5"
                                                styleId="file_id"/>
                            <fmt:message key="dataImport.fileMessage"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </c:if>

            <div class="col-xs-12">
                <div class="table-responsive">

                    <table border="0"
                           cellpadding="0"
                           cellspacing="0"
                           width="100%"
                           class="table">
                        <thead>
                        <tr>
                            <th style="min-width: 200px;" class="col-xs-5">
                                <fmt:message key="dataImport.columns.available"/>
                            </th>
                            <th style="min-width: 200px;" class="col-xs-5">
                                <fmt:message key="dataImpoer.columns.selected"/>
                            </th>
                            <th nowrap="true" style="min-width: 200px; text-align: right;" class="col-xs-2">
                                <fmt:message key="dataImport.columns.position"/>
                            </th>
                        </tr>
                        </thead>

                        <tbody>
                        <tr>
                            <td align="left" valign="top" class="col-xs-5">
                                <c:if test="${op != 'delete'}">
                                    <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                        <tr>
                                            <div class="input-group">
                                                <td width="100%">
                                                    <c:import
                                                            url="/contacts/DataImport/ReadConfiguration.do?configurationType=${dataImportForm.dtoMap['profileType']}"/>
                                                </td>
                                                <td>
                                        <span class="input-group-btn">
                                            <label for="selectOne_id" class="btn btn-default">
                                                <span class="glyphicon glyphicon-chevron-right"></span>
                                            </label>
                                            <html:button property="selectOne"
                                                         onclick="javascript:addRow('availableColumnsId');"
                                                         styleClass="hidden adminRighttArrow"
                                                         titleKey="Common.add"
                                                         tabindex="6"
                                                         styleId="selectOne_id">
                                            </html:button>
                                        </span>
                                                </td>
                                            </div>
                                        </tr>
                                    </table>
                                </c:if>
                            </td>
                            <td colspan="2" valign="top">
                                <div id="ajaxMessageId" class="messageToolTip"
                                     style="display:none; bottom:5px; right:5px; position:absolute; z-index: 2200;">
                                    <fmt:message key="Common.message.loading"/>
                                </div>
                                <div id="ajaxColumnsId">
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <fmt:message key="dataImport.dateFormat.msg"/>
            <br/>
            <fmt:message key="dataImpoer.decimalNumberFormat.msg"/>
            <br/>
            <fmt:message key="dataImport.checkDuplicate.msg"/>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                <html:submit property="saveButton" styleClass="${app2:getFormButtonClasses()}" tabindex="9">
                    <c:out value="${button}"/>
                </html:submit>

                <c:if test="${'delete' != op}">
                    <html:submit property="saveAndImportButton" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="10"
                                 onclick="startCheckProgressBar();">
                        <fmt:message key="dataImport.saveAndImport"/>
                    </html:submit>
                    <%--<html:submit property="importButton" styleClass="button" tabindex="11">
                        <fmt:message key="Common.import"/>
                    </html:submit>--%>
                </c:if>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="dataImportForm"/>

<c:if test="${null != selectedColumnsInUI}">
    <script type="text/javascript">
        function firstExecution() {
            <c:forEach var="col" items="${selectedColumnsInUI}">
            <c:set var="dtoKey" value="${col.groupId}_${col.columnId}_${col.uiPosition}"/>
            recoverRow('${dtoKey}', '${col.value}', '${col.columnName}', '${col.groupName}');
            </c:forEach>
        }

        $(document).ready(function () {
            firstExecution();
        });
    </script>
</c:if>
