<%@ include file="/Includes.jsp" %>


<c:set var="tabIndexNumber" value="5"/>
<c:set var="currentMillis" value="${app2:getCurrentTimeMillis()}" scope="request"/>

<app2:jScriptUrl url="/contacts/DataImport/AddColumn.do" var="jsAddRowUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="position" value="positionId"/>
    <app2:jScriptUrlParam param="dtoKey" value="dtoKeyId"/>
</app2:jScriptUrl>

<%--fragment to add progress bar--%>
<c:set var="isImportProgressBar" value="true" scope="request"/>
<c:import url="/common/contacts/checkDuplicateProgressBarFragment.jsp"/>

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
            async:false,
            type: "POST",
            dataType: "html",
            data:parameters,
            url:urlAddress,
            beforeSend:setLoadMessage,
            complete:hideLoadMessage,
            success: function(data) {
                processData(data);
            },
            error: function(ajaxRequest) {
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
        if($("#idCheckDuplicate").prop("checked")) {
            showCheckDuplicateProgressBar();
        }
    }

</script>

<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td width="100%">
            <html:form action="${action}" enctype="multipart/form-data" styleId="dataImportFormId"
                       focus="dto(label)">

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

                <table border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="container">
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                                <html:submit property="saveButton" styleClass="button">
                                    <c:out value="${button}"/>
                                </html:submit>

                                <c:if test="${'delete' != op}">
                                    <html:submit property="saveAndImportButton" styleClass="button" onclick="startCheckProgressBar();">
                                        <fmt:message key="dataImport.saveAndImport"/>
                                    </html:submit>
                                    <%--<html:submit property="importButton" styleClass="button">
                                        <fmt:message key="Common.import"/>
                                    </html:submit>--%>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" class="title">
                                ${title}
                        </td>
                    </tr>
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
                    <tr>
                        <td class="label">
                            <fmt:message key="dataImport.configuration.name"/>
                        </td>
                        <td class="contain">
                            <app:text property="dto(label)"
                                      styleClass="middleText"
                                      view="${op == 'delete'}"
                                      maxlength="100"
                                      tabindex="2"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="dataImport.configuration.type"/>
                        </td>
                        <td class="contain">
                            <c:set var="importTypes" value="${app2:getImportTypes(pageContext.request)}"/>
                            <html:select property="dto(profileType)"
                                         styleClass="middleSelect"
                                         readonly="${'delete' == op}"
                                         onchange="javascript:changeConfigurationType();"
                                         tabindex="3">
                                <html:option value=""/>
                                <html:options collection="importTypes" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" nowrap>
                            <fmt:message key="dataImport.configuration.skipHeader"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(skipFirstRow)"
                                           styleClass="radio"
                                           disabled="${op == 'delete'}"
                                           value="true"
                                           tabindex="4"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" nowrap>
                            <fmt:message key="dataImport.configuration.checkDuplicate"/>
                        </td>
                        <td class="contain">
                            <html:checkbox property="dto(checkDuplicate)"
                                           styleId="idCheckDuplicate"
                                           styleClass="radio"
                                           disabled="${op == 'delete'}"
                                           value="true"
                                           tabindex="4"/>
                        </td>
                    </tr>

                    <c:if test="${op != 'delete'}">
                        <tr>
                            <td class="label">
                                <fmt:message key="dataImport.File"/>
                            </td>
                            <td class="contain">
                                <html:file property="dto(file)" size="40" tabindex="5"/>
                                <br>
                                <fmt:message key="dataImport.fileMessage"/>
                            </td>
                        </tr>
                    </c:if>

                    <tr>
                        <td colspan="2" align="center" class="label">
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="label" align="center" width="40%">
                                        <fmt:message key="dataImport.columns.available"/>
                                    </td>
                                    <td class="label" align="center" width="42%">
                                        <fmt:message key="dataImpoer.columns.selected"/>
                                    </td>
                                    <td class="label" width="18%" nowrap="true">
                                        <fmt:message key="dataImport.columns.position"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="left" valign="top">
                                        <c:if test="${op != 'delete'}">
                                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                                <tr>
                                                    <td width="100%">
                                                        <c:import
                                                                url="/contacts/DataImport/ReadConfiguration.do?configurationType=${dataImportForm.dtoMap['profileType']}"/>
                                                    </td>
                                                    <td>
                                                        <html:button property="selectOne"
                                                                     onclick="javascript:addRow('availableColumnsId');"
                                                                     styleClass="adminRighttArrow"
                                                                     titleKey="Common.add" tabindex="6">
                                                            &nbsp;
                                                        </html:button>
                                                    </td>
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
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td class="contain" colspan="2">
                            <fmt:message key="dataImport.dateFormat.msg"/>
                            <br>
                            <fmt:message key="dataImpoer.decimalNumberFormat.msg"/>
                            <br>
                            <fmt:message key="dataImport.checkDuplicate.msg"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="button" colspan="2">
                            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
                                <html:submit property="saveButton" styleClass="button" tabindex="9">
                                    <c:out value="${button}"/>
                                </html:submit>

                                <c:if test="${'delete' != op}">
                                    <html:submit property="saveAndImportButton" styleClass="button" tabindex="10" onclick="startCheckProgressBar();">
                                        <fmt:message key="dataImport.saveAndImport"/>
                                    </html:submit>
                                    <%--<html:submit property="importButton" styleClass="button" tabindex="11">
                                        <fmt:message key="Common.import"/>
                                    </html:submit>--%>
                                </c:if>
                            </app2:checkAccessRight>
                            <html:cancel styleClass="button" tabindex="12">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </td>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>
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
