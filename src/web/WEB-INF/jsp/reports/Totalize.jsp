<%@ include file="/Includes.jsp" %>

<html:form action="/Report/Totalize/Create.do" styleClass="form-horizontal">

    <html:hidden property="dto(op)" value="create"/>
    <html:hidden property="dto(reportId)" value="${param.reportId}"/>
    <html:hidden property="dto(sumVersion)" styleId="sumVersionId"/>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="UPDATE"
                             functionality="TOTALIZE"
                             styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.save"/>
        </app2:securitySubmit>
    </div>

    <div class="${app2:getFormPanelClasses()}">

        <legend class="title">
            <fmt:message key="Totalize.title"/>
        </legend>

        <div class="table-responsive">
            <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container table">
                <fmt:message key="Totalize.sum" var="sum"/>
                <fmt:message key="Totalize.average" var="average"/>
                <fmt:message key="Totalize.largestValue" var="largestValue"/>
                <fmt:message key="Totalize.smallestValue" var="smallestValue"/>

                <tr>
                    <th width="50%"><fmt:message key="Totalize.column"/></th>
                    <th width="12%">${sum}</th>
                    <th width="12%">${average}</th>
                    <th width="12%">${largestValue}</th>
                    <th width="14%">${smallestValue}</th>
                </tr>

                <html:hidden property="dto(msgSum)" value="${sum}"/>
                <html:hidden property="dto(msgAverage)" value="${average}"/>
                <html:hidden property="dto(msgLargestValue)" value="${largestValue}"/>
                <html:hidden property="dto(msgSmallestValue)" value="${smallestValue}"/>


                <c:forEach var="column" items="${dto.structure}">
                    <c:set var="columnName"
                           value="${app2:composeColumnLabelByTitusPath(column.path, column.label, pageContext.request)}"/>
                    <html:hidden property="dto(columnName_${column.columnId})" value="${columnName}"
                                 styleId="columnNameId${column.columnId}"/>

                    <tr>
                        <td>${columnName}</td>
                        <c:forEach var="totalize" items="${column.totalizers}">

                            <c:if test="${null != totalize.totalizeId}">
                                <td>
                                    <c:set var="checkIdentifier"
                                           value="${column.columnId}_${totalize.totalizeId}_${totalize.totalizeType}_${totalize.version}"/>
                                    <html:hidden property="dto(checked_${checkIdentifier})" value="true"
                                                 styleId="checkedId${checkIdentifier}"/>
                                    <div class="paddingCheckboxInTable checkbox checkbox-default listItemCheckbox ">
                                        <input type="checkbox"
                                               name="dto(checkBox_${checkIdentifier})"
                                               class="adminCheckBox" checked>
                                        <label for="parentCheckBoxId"></label>
                                    </div>

                                </td>
                            </c:if>

                            <c:if test="${null == totalize.totalizeId}">
                                <td>
                                    <div class="paddingCheckboxInTable checkbox checkbox-default listItemCheckbox">
                                        <input type="checkbox"
                                               name="dto(checkBox_${column.columnId}_NONE_${totalize.totalizeType}_NONE)"
                                               class="adminCheckBox">
                                        <label for="parentCheckBoxId"></label>
                                    </div>

                                </td>
                            </c:if>

                        </c:forEach>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <legend class="title">
            <fmt:message key="Totalize.general.title"/>
        </legend>

        <div class="table-responsive">
            <table border="0" cellpadding="0" cellspacing="0" width="70%" align="center" class="container table">
                <tr>
                    <fmt:message key="Totalize.sumRecords" var="resource"/>
                    <html:hidden property="dto(sumRecordsResource)" value="${resource}"/>
                    <td width="30%">
                            ${resource}
                    </td>
                    <td width="70%">
                        <div class="checkbox checkbox-default listItemCheckbox paddingCheckboxInTable">
                            <html:checkbox property="dto(sumRecords)"
                                           styleClass="adminCheckBox"/>
                            <label for="parentCheckBoxId"></label>
                        </div>
                    </td>

                </tr>
            </table>
        </div>
    </div>

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:securitySubmit operation="UPDATE"
                             functionality="TOTALIZE"
                             styleClass="${app2:getFormButtonClasses()}">
            <fmt:message key="Common.save"/>
        </app2:securitySubmit>
    </div>

</html:form>