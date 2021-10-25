<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper" %>
<%@include file="/Includes.jsp" %>

<c:set var="importRecordId" value="${importDeduplicationForm.dtoMap['importRecordId']}" scope="request"/>

<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<c:set var="PREFIX_MERGE" value="<%=DeduplicationHelper.ImportMergePrefix.MERGE.getConstant()%>"/>
<c:set var="ROWTYPE_ADDRESS" value="<%=DeduplicationHelper.RowType.ADDRESS.getConstant()%>"/>

<fmt:message var="datePattern" key="datePattern"/>

<script type="text/javascript">

    $(function () {
        //search property onchange
        $(document).on("change", "#mergeSelect", function () {
            var boxValue = $(this).val();

            hideCheckbox();
            if (boxValue != '') {
                if (isMergeSelectedItem(boxValue)) {
                    showMergeCheckbox(boxValue);
                }
            }
        });

        $(document).ready(function () {
            initializeMerge();
        });
    });

    function initializeMerge() {
        var boxValue = $("#mergeSelect").val();
        if (boxValue != '') {
            if (isMergeSelectedItem(boxValue)) {
                showMergeCheckbox(boxValue);
            }
        }
    }

    function isMergeSelectedItem(value) {
        var mergePrefix = '${PREFIX_MERGE}';
        if (value.indexOf(mergePrefix) == 0) {
            return true;
        }
        return false;
    }

    function showMergeCheckbox(boxValue) {
        var mergePrefix = '${PREFIX_MERGE}';
        var startIndex = mergePrefix.length;

        var rowKey = boxValue.substring(startIndex);
        var importRecordRowKey = composeImportRecordTrId();

        showCheckbox(importRecordRowKey);
        showCheckbox(rowKey);

        enableRequiredEmptyCheckbox(importRecordRowKey, rowKey);
        preselectedCheckbox(rowKey);
    }

    function composeImportRecordTrId() {
        return "R" + '${importRecordId}';
    }

    function hideCheckbox() {
        $("#mergeTable input:radio").each(function () {
            $(this).prop("checked", false);
            $(this).css("display", "none");
        });

        $("#mergeTable .radiocheck").each(function () {
            $(this).prop("checked", false);
            $(this).css("display", "none");
        });
    }

    function showCheckbox(trId) {
        $("#" + trId + " .clsRadio").each(function () {
            $(this).css("display", "inline");
        });
    }

    function preselectedCheckbox(trId) {
        $("#" + trId + " input:radio").each(function () {
            if ($(this).css("display") != "none") {
                $(this).prop("checked", true);
            }
        });
    }

    function enableRequiredEmptyCheckbox(trId1, trId2) {
        $("#" + trId1 + " .clsRadio").each(function () {
            var checkBoxName = $(this).attr('name');

            $("#" + trId2 + " .clsEmptyRadio").each(function () {
                if (checkBoxName == $(this).attr('name')) {
                    $(this).css("display", "inline");
                }
            });
        });
    }

</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${action}" styleClass="form-horizontal">

        <html:hidden property="dto(importRecordId)"/>
        <html:hidden property="dto(profileId)"/>
        <html:hidden property="dto(parentRecordId)"/>
        <html:hidden property="dto(organizationId)"/>
        <html:hidden property="dto(isContactPerson)" value="true"/>

        <c:set var="headerColumnList"
               value="${app2:buildHeaderProfileContactPersonColumns(param.profileId, pageContext.request)}"
               scope="request"/>
        <c:set var="deduplicationItemList"
               value="${app2:buildImportContactPersonDeduplicationItemValues(importRecordId, param.profileId, importDeduplicationForm.dtoMap['organizationId'] , pageContext.request)}"
               scope="request"/>

        <div class="${app2:getSearchWrapperClasses()}">
            <app2:checkAccessRight functionality="CONTACTIMPORT" permission="EXECUTE">
            <div class="${app2:getFormOneSearchInput()}">
                <div class="form-group col-xs-12">
                    <c:set var="mergeList"
                           value="${app2:getImportMergeValuesList(importRecordId, pageContext.request)}"/>
                    <html:select property="dto(importMerge)"
                                 styleId="mergeSelect"
                                 styleClass="${app2:getFormInputClasses()} middleSelect"
                                 tabindex="22">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="mergeList" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>

            <div class="form-group col-xs-12 col-sm-4">
                <div class="col-xs-12">
                    <html:submit property="mergeButton" styleClass="${app2:getFormButtonClasses()}" tabindex="23">
                        <fmt:message key="ImportRecord.deduplicate.merge"/>
                    </html:submit>
                    </app2:checkAccessRight>
                    <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="24">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </div>
            </div>
        </div>

        <div class="table-responsive">
            <table id="mergeTable"
                   border="0"
                   cellpadding="0"
                   cellspacing="0"
                   width="100%"
                   align="center"
                   class="${app2:getFantabulousTableClases()}">
                    <%--style="table-layout: auto; display: block;"--%>
                <thead>
                <tr>
                    <th width="95px" class="hideBorder">
                    </th>
                    <th width="25px" class="listHeader">
                    </th>

                    <c:forEach var="headerItem" items="${headerColumnList}">
                        <th class="listHeader">
                            <c:out value="${headerItem.value}"/>
                        </th>
                    </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="rowItem" items="${deduplicationItemList}" varStatus="indexVar">

                    <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                        <c:if test="${ROWTYPE_ADDRESS eq rowItem.rowType}">
                            <c:set var="addressMap" value="${app2:getAddressMap(rowItem.rowElementId)}"/>
                            <c:choose>
                                <c:when test="${personType == addressMap['addressType']}">
                                    <c:set var="addressEditLink"
                                           value="/contacts/Person/Forward/Update.do?contactId=${addressMap['addressId']}&dto(addressId)=${addressMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="addressEditLink"
                                           value="/contacts/Organization/Forward/Update.do?contactId=${addressMap['addressId']}&dto(addressId)=${addressMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </app2:checkAccessRight>

                    <tr id="${rowItem.uiKey}" class="listRow">
                        <td style="background-color: #FCF8E8; min-width:95px" align="center">
                            <c:out value="${rowItem.rowTitle}"/>
                        </td>
                        <td class="listItem">
                            <c:if test="${not empty addressEditLink}">
                                <fmt:message key="Common.edit" var="common_edit"/>
                                <app:link action="${addressEditLink}" title="${common_edit}" contextRelative="true">
                                    <span class="${app2:getClassGlyphEdit()}"></span>
                                </app:link>
                            </c:if>
                        </td>

                        <c:forEach var="item" items="${rowItem.itemList}">
                            <td class="listItem">

                                <table border="0" cellpadding="2" cellspacing="0" width="100%" align="center">
                                    <tr>
                                        <td>
                                            <div class="paddingRight">
                                                <c:choose>
                                                    <c:when test="${item.isDateValue}">
                                                        <fmt:formatDate var="dateValue"
                                                                        value="${app2:intToDate(item.value)}"
                                                                        pattern="${datePattern}"/>
                                                        <c:out value="${dateValue}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${item.value}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                        <td align="right">
                                            <c:set var="checkboxDisplay" value="display:none" scope="request"/>
                                            <c:set var="checkboxClass" value="clsEmptyRadio" scope="request"/>

                                            <c:if test="${not empty item.value}">
                                                <c:set var="checkboxClass" value="clsRadio" scope="request"/>
                                            </c:if>

                                            <div class='radio radio-default radio-inline ${checkboxClass}'
                                                 style="${checkboxDisplay}">
                                                <html:radio property="dto(${item.checkboxName})"
                                                            value="${item.checkboxValue}"
                                                            style="${checkboxDisplay}"
                                                            styleId="isDefault_id"
                                                            styleClass="${checkboxClass}"/>
                                                <label for="isDefault_id"></label>
                                            </div>

                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </html:form>
</div>