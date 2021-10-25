<%@ include file="/Includes.jsp" %>

<calendar:initialize/>
<c:set var="generalWidth" value="${200}" scope="request"/>
<c:set var="elementCounter" value="${0}" scope="request"/>
<c:set var="categoryTab" value="${app2:getCategoryTab(param['categoryTabId'], pageContext.request)}"/>
<c:set var="formName" value="categoryFieldValueForm" scope="request"/>
<c:set var="labelWidth" value="15" scope="request"/>
<c:set var="containWidth" value="85" scope="request"/>
<c:set var="operation" value="${op}" scope="request"/>

<c:import url="/common/catalogs/CategoryTabUtilHeader.jsp"/>

<html:form action="${action}" enctype="multipart/form-data">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(categoryTabVersion)"/>
    <html:hidden property="dto(checkVersion)" value="true"/>
    <html:hidden property="dto(categoryTabId)" value="${categoryTab.categoryTabId}"/>
    <table cellpadding="0" cellspacing="0" border="0" width="85%" align="center">
        <tr>
            <td class="button">
                <html:submit styleClass="button"
                             onclick="javascript:fillMultipleSelectValues();">${button}</html:submit>
                <c:if test="${'true' == showCancelButton}">
                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                </c:if>
            </td>
        </tr>
        <c:forEach var="categoryGroup" items="${categoryTab.categoryGroups}">
            <tr>
                <td>
                    <c:choose>
                        <c:when test="${true == categoryGroup.onlySubCategories}">
                            <div id="groupId_${categoryGroup.categoryGroupId}" style="display:none;">
                                <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                    <tr>
                                        <td class="title">${categoryGroup.label}</td>
                                    </tr>
                                </table>
                            </div>
                            <html:hidden property="dto(groupCounter_${categoryGroup.categoryGroupId})"
                                         value=""
                                styleId="groupCounter_${categoryGroup.categoryGroupId}"/>
                        </c:when>
                        <c:otherwise>
                            <table border="0" cellpadding="0" cellspacing="0" width="100%">
                                <tr>
                                    <td class="title">${categoryGroup.label}</td>
                                </tr>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>

                    <c:forEach var="categoryId" items="${categoryGroup.categories}">
                        <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
                        <c:set var="categoryId" value="${categoryId}" scope="request"/>
                        <c:set var="groupId" value="${categoryGroup.categoryGroupId}" scope="request"/>
                        <c:import url="/common/catalogs/RenderCategory.jsp"/>
                    </c:forEach>

        </c:forEach>
        <tr>
            <td class="button">
                <html:submit styleClass="button"
                             onclick="javascript:fillMultipleSelectValues();">${button}</html:submit>
                <c:if test="${'true' == showCancelButton}">
                    <html:cancel styleClass="button"><fmt:message key="Common.cancel"/></html:cancel>
                </c:if>
            </td>
        </tr>
    </table>
</html:form>

