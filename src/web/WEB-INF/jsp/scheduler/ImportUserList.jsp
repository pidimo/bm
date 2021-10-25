<%@ include file="/Includes.jsp" %>

<html:form action="/${action}?other=${param.other}" focus="parameter(lastName@_firstName@_searchName)"
           styleClass="form-horizontal">
    <html:hidden property="parameter(ownUserId)" value="${sessionScope.user.valueMap['userId']}"/>
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left" for="fieldSearchNane_id">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(lastName@_firstName@_searchName)"
                           styleClass="${app2:getFormInputClasses()}"
                           styleId="fieldSearchNane_id"
                           maxlength="40"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}"><fmt:message
                                key="Common.go"/></html:submit>
                    </span>
            </div>
        </div>
    </div>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="${action}?other=${param.other}" mode="bootstrap" parameterName="lastNameAlpha"/>
</div>


<div class="table-responsive">
    <fanta:table mode="bootstrap" width="100%" id="viewUser"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="${action}?other=${param.other}"
                 imgPath="${baselayout}" align="center">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <c:choose>
                <c:when test="${param.other =='true'}">

                    <fanta:actionColumn name="" useJScript="true"
                                        action="javascript:parent.selectMultipleEvenField('${viewUser.userId}', '${app2:jscriptEncode(viewUser.userName)}');"
                                        title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphImport()}"/>
                </c:when>
                <c:otherwise>
                    <fanta:actionColumn name="" useJScript="true"
                                        action="javascript:parent.selectField('fieldViewUserId_id', '${viewUser.userId}', 'fieldViewUserName_id', '${app2:jscriptEncode(viewUser.userName)}');"
                                        title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphImport()}"/>
                </c:otherwise>
            </c:choose>
        </fanta:columnGroup>
        <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name" headerStyle="listHeader"
                          width="65%" orderable="true"/>
        <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                          headerStyle="listHeader" width="15%" orderable="true">
            <c:if test="${viewUser.type == '1'}">
                <fmt:message key="User.intenalUser"/>
            </c:if>
            <c:if test="${viewUser.type == '0'}">
                <fmt:message key="User.externalUser"/>
            </c:if>
        </fanta:dataColumn>
    </fanta:table>
</div>