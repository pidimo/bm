<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<script>
    function check() {
        field = document.getElementById('listc').aditionals;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        } else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }

    function goSubmit() {
        document.getElementById('Import_value').value = true;
        document.forms[1].submit();
    }
</script>

<c:set var="groupName"><%=request.getParameter("dto(groupName)")%>
</c:set>

<div class="${app2:getListWrapperClasses()}">
    <div class="searchContainer">
        <html:form action="${actionReload}" focus="parameter(lastName@_firstName@_login)" styleClass="form-horizontal">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Admin.User.Title.search"/>
                </legend>

                <div class="${app2:getSearchWrapperClasses()}">
                    <label class="${app2:getFormLabelOneSearchInput()} label-left">
                        <fmt:message key="Common.search"/>
                    </label>

                    <div class="${app2:getFormOneSearchInput()} marginButton">
                        <c:if test="${param.module!='scheduler'}">
                        <div class="input-group">
                            </c:if>
                            <html:text property="parameter(lastName@_firstName@_login)"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       maxlength="40"/>

                            <c:if test="${param.module !='scheduler'}">
                                        <span class="input-group-btn">
                                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                                            <fmt:message key="Common.go"/>
                                        </html:submit>
                                            </span>
                            </c:if>
                            <c:if test="${param.module!='scheduler'}">
                        </div>
                        </c:if>
                    </div>

                    <c:if test="${param.module=='scheduler'}">
                        <div class="col-xs-12 col-sm-6 col-md-6">
                            <div class="row">
                                <label class="control-label col-xs-12 col-sm-5">
                                    <fmt:message key="User.externalAlso"/>
                                </label>

                                <div class="col-xs-12 col-sm-3 marginButton">
                                    <div class="radiocheck">
                                        <div class="checkbox checkbox-default">
                                            <html:checkbox property="parameter(userType)" styleId="userType_id"
                                                           tabindex="2"/>
                                            <label for="userType_id"></label>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-4">
                                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                                        <fmt:message key="Common.go"/>
                                    </html:submit>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </div>
            </fieldset>

        </html:form>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="${actionReload}" parameterName="lastName" mode="bootstrap"/>
        </div>
    </div>

    <html:form action="${action}" styleId="listc">
        <div class="${app2:getFormGroupClasses()}">
            <c:url var="url" value="/${param.module}${actionCancel}">
                <c:if test="${param.module=='admin'}">
                    <c:param name="userGroupId" value="${param.userGroupId}"/>
                </c:if>
                <c:if test="${param.module=='scheduler'}">
                    <c:param name="taskId" value="${param.taskId}"/>
                </c:if>
            </c:url>
            <c:set var="path" value="${pageContext.request.contextPath}"/>
            <html:hidden property="Import_value" value="false" styleId="Import_value"/>
            <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>


            <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit()">
                <fmt:message
                        key="Contact.Organization.Employee.addEmployee"/>
            </html:button>
            <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="location.href='${url}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
                <%--<html:cancel styleClass="button" value="Cancel" property="dto(cancel)"><fmt:message key="Common.cancel"/></html:cancel>--%>
        </div>

        <div class="table-responsive">
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}" width="100%"
                         align="left"
                         id="userGroup" action="${actionReload}" imgPath="${baselayout}" withCheckBox="true">
                <fanta:checkBoxColumn name="guia" id="aditionals" onClick="javascript:check();" property="userId"
                                      headerStyle="listHeader" styleClass="listItemCenter" width="5%"/>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name"
                                  headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="login" styleClass="listItem" title="User.userName" headerStyle="listHeader"
                                  width="25%" orderable="true"/>
                <fanta:dataColumn name="type" styleClass="listItem2" title="User.typeUser" renderData="false"
                                  headerStyle="listHeader" width="15%" orderable="true">
                    <c:if test="${userGroup.type == '1'}">
                        <fmt:message key="User.intenalUser"/>
                    </c:if>
                    <c:if test="${userGroup.type == '0'}">
                        <fmt:message key="User.externalUser"/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </div>
    </html:form>
</div>
