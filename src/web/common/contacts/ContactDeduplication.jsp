<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper" %>
<%@include file="/Includes.jsp" %>

<c:set var="personType" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>

<c:set var="PREFIX_MERGE" value="<%=DeduplicationHelper.ContactMergePrefix.MERGE.getConstant()%>"/>
<c:set var="SEPARATOR" value="<%=DeduplicationHelper.MERGE_SEPARATOR%>"/>

<fmt:message var="datePattern" key="datePattern"/>

<script type="text/javascript">

    $(function(){
        //search property onchange
        $(document).on("change","#mergeSelect",function(){
            var boxValue = $(this).val();

            hideCheckbox();
            if(boxValue != '') {
                if(isMergeSelectedItem(boxValue)) {
                    showMergeCheckbox(boxValue);
                }
            }
        });

        $(document).ready(function() {
            initializeMerge();
        });
    });

    function initializeMerge() {
        var boxValue = $("#mergeSelect").val();
        if(boxValue != '') {
            if(isMergeSelectedItem(boxValue)) {
                showMergeCheckbox(boxValue);
            }
        }
    }

    function isMergeSelectedItem(value) {
        var mergePrefix = '${PREFIX_MERGE}';
        if(value.indexOf(mergePrefix) == 0) {
            return true;
        }
        return false;
    }

    function showMergeCheckbox(boxValue) {
        var mergePrefix = '${PREFIX_MERGE}';
        var separator = '${SEPARATOR}';
        var startIndex = mergePrefix.length;
        var separatorIndex = boxValue.indexOf(separator);

        var rowKey1 = boxValue.substring(startIndex, separatorIndex);
        var rowKey2 = boxValue.substring(separatorIndex + 1);

        showCheckbox(rowKey1);
        showCheckbox(rowKey2);

        enableRequiredEmptyCheckbox(rowKey1, rowKey2);
        enableRequiredEmptyCheckbox(rowKey2, rowKey1);

        preselectedCheckbox(rowKey2)
    }

    function hideCheckbox() {
        $("#mergeTable input:radio").each(function() {
            $(this).prop( "checked", false);
            $(this).css("display", "none");
        });
    }

    function showCheckbox(trId) {
        $("#"+ trId +" .clsRadio").each(function() {
            $(this).css("display", "inline");
        });
    }

    function preselectedCheckbox(trId) {
        $("#"+ trId +" input:radio").each(function() {
            if($(this).css("display") != "none") {
                $(this).prop( "checked", true);
            }
        });
    }

    function enableRequiredEmptyCheckbox(trId1, trId2) {
        $("#"+ trId1 +" .clsRadio").each(function() {
            var checkBoxName = $(this).attr('name');

            $("#"+ trId2 +" .clsEmptyRadio").each(function() {
                if(checkBoxName == $(this).attr('name')) {
                    $(this).css("display", "inline");
                }
            });
        });
    }

</script>



<html:form action="${action}" >

    <html:hidden property="dto(duplicateGroupId)"/>

    <c:set var="headerColumnList" value="${app2:buildHeaderContactDeduplicationColumns(pageContext.request)}" scope="request"/>
    <c:set var="deduplicationItemList" value="${app2:buildContactDeduplicationItemValues(contactDeduplicationForm.dtoMap['duplicateGroupId'], pageContext.request)}" scope="request"/>

    <table border="0" cellpadding="0" cellspacing="0" width="90%" align="center" class="container">
        <tr>
            <td style="padding-left:25px" class="button">

                <app2:checkAccessRight functionality="DEDUPLICATIONCONTACT" permission="EXECUTE">
                    <c:set var="mergeList" value="${app2:getContactMergeSelectValuesList(contactDeduplicationForm.dtoMap['duplicateGroupId'], pageContext.request)}"/>
                    <html:select property="dto(contactMerge)" styleId="mergeSelect" styleClass="middleSelect" tabindex="22">
                        <html:option value="">&nbsp;</html:option>
                        <html:options collection="mergeList" property="value" labelProperty="label"/>
                    </html:select>

                    <html:submit property="mergeButton" styleClass="button" tabindex="23">
                        <fmt:message key="DedupliContact.deduplicate.merge"/>
                    </html:submit>
                </app2:checkAccessRight>

                <html:cancel styleClass="button" tabindex="24">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <tr>
            <td>
                <table id="mergeTable" border="0" cellpadding="0" cellspacing="0" width="100%" align="center" style="table-layout: auto; display: block;">
                    <tr>
                        <td width="25px">
                        </td>
                        <td width="25px" class="listHeader">
                        </td>

                        <c:forEach var="headerItem" items="${headerColumnList}">
                            <td class="listHeader">
                                <c:out value="${headerItem.value}"/>
                            </td>
                        </c:forEach>
                    </tr>

                    <c:forEach var="rowItem" items="${deduplicationItemList}" varStatus="indexVar">

                        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
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
                        </app2:checkAccessRight>

                        <tr id="${rowItem.uiKey}" class="listRow">
                            <td style="background-color: #FCF8E8; min-width:25px" align="center">
                                <c:out value="${rowItem.rowTitle}"/>
                            </td>
                            <td class="listItem">
                                <c:if test="${not empty addressEditLink}">
                                    <app:link action="${addressEditLink}" contextRelative="true">
                                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                                    </app:link>
                                </c:if>
                            </td>

                            <c:forEach var="item" items="${rowItem.itemList}">
                                <td class="listItem">
                                    <table border="0" cellpadding="2" cellspacing="0" width="100%" align="center">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.isDateValue}">
                                                        <fmt:formatDate var="dateValue" value="${app2:intToDate(item.value)}" pattern="${datePattern}"/>
                                                        <c:out value="${dateValue}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:out value="${item.value}"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td align="right">
                                                <c:set var="checkboxDisplay" value="display:none" scope="request"/>
                                                <c:set var="checkboxClass" value="radio clsEmptyRadio" scope="request"/>

                                                <c:if test="${not empty item.value}">
                                                    <c:set var="checkboxClass" value="radio clsRadio" scope="request"/>
                                                </c:if>

                                                <html:radio property="dto(${item.checkboxName})" value="${item.checkboxValue}" style="${checkboxDisplay}" styleClass="${checkboxClass}"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </table>
</html:form>

