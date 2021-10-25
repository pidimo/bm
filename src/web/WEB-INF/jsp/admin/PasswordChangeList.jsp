<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">

    <fmt:message var="dateTimePattern" key="dateTimePattern"/>
    <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <html:form action="/PasswordChange/List.do" focus="parameter(description)" styleClass="form-horizontal">
        <legend class="title">
            <fmt:message key="PasswordChange.title.list"/>
        </legend>
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(description)" styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="100"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                            key="Common.go"/></html:submit>
                </span>
                </div>
            </div>
        </div>
    </html:form>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="PasswordChange/List.do"
                        mode="bootstrap"
                        parameterName="descriptionAlpha"/>
    </div>

    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="CREATE">
        <html:form action="/PasswordChange/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="passwordChangeList"
                     width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="passwordChange"
                     action="PasswordChange/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="PasswordChange/Forward/Update.do?passwordChangeId=${passwordChange.passwordChangeId}&dto(passwordChangeId)=${passwordChange.passwordChangeId}&dto(op)=read"/>
            <c:set var="deleteLink"
                   value="PasswordChange/Forward/Delete.do?passwordChangeId=${passwordChange.passwordChangeId}&dto(passwordChangeId)=${passwordChange.passwordChangeId}&dto(op)=read&dto(withReferences)=true"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="VIEW">
                    <fanta:actionColumn name="edit"
                                        title="Common.update"
                                        action="${editLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="description" action="${editLink}" styleClass="listItem"
                              title="PasswordChange.description"
                              headerStyle="listHeader" width="50%" maxLength="100" orderable="true"/>

            <fanta:dataColumn name="totalUser" styleClass="listItemRight" title="PasswordChange.totalUser"
                              headerStyle="listHeader" width="10%" orderable="true"/>

            <fanta:dataColumn name="userPassChangeCount" styleClass="listItemRight"
                              title="PasswordChange.totalUserChanged"
                              headerStyle="listHeader" width="10%" orderable="false" renderData="false">
                <c:out value="${passwordChange.totalUser - passwordChange.userPassChangeCount}"/>
            </fanta:dataColumn>

            <fanta:dataColumn name="changeTime" styleClass="listItem2" title="PasswordChange.changeTime"
                              headerStyle="listHeader" width="25%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${not empty passwordChange.changeTime}">
                        ${app2:getDateWithTimeZone(passwordChange.changeTime, timeZone, dateTimePattern)}
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>

    </div>
    <app2:checkAccessRight functionality="PASSWORDCHANGE" permission="CREATE">
        <html:form action="/PasswordChange/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>
