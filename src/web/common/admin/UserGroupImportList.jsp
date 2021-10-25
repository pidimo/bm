<%@ include file="/Includes.jsp" %>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
%>

<script>
    function check()
    {
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

<c:set var="groupName"><%=request.getParameter("dto(groupName)")%></c:set>
<table width="80%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="searchContainer">
                <html:form action="${actionReload}" focus="parameter(lastName@_firstName@_login)">
                    <tr>
                        <td height="20" colspan="${(param.module=='scheduler')?'5':'3'}" class="title">
                            <fmt:message key="Admin.User.Title.search"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
                        <td align="left" class="contain" width="30%">
                            <html:text property="parameter(lastName@_firstName@_login)" styleClass="mediumText"
                                       maxlength="40"/>
                            &nbsp;
                        </td>
                        <c:if test="${param.module !='scheduler'}">
                            <td align="left" class="contain" width="50%">
                                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                            </td>
                        </c:if>
                        <c:if test="${param.module=='scheduler'}">
                            <td class="label" width="20%">
                                <fmt:message key="User.externalAlso"/>&nbsp;
                            </td>
                            <td align="left" class="contain" width="10%">
                                <html:checkbox property="parameter(userType)" tabindex="2" styleClass="radio"/>
                            </td>
                            <td align="left" class="contain" width="20%">
                                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                            </td>
                        </c:if>
                    </tr>
                </html:form>

                <tr>
                    <td colspan="${(param.module=='scheduler')?'5':'3'}" align="center" class="alpha">
                        <fanta:alphabet action="${actionReload}" parameterName="lastName"/>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <html:form action="${action}" styleId="listc">
        <tr>
            <td class="button">
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
                <html:button property="" styleClass="button" onclick="goSubmit()"><fmt:message key="Contact.Organization.Employee.addEmployee"/></html:button>
                <html:button property="" styleClass="button" onclick="location.href='${url}'" >
                    <fmt:message   key="Common.cancel"/>
                </html:button>
<%--<html:cancel styleClass="button" value="Cancel" property="dto(cancel)"><fmt:message key="Common.cancel"/></html:cancel>--%>
            </td>
        </tr>
        <tr>
            <td>

                <fanta:table width="100%" align="left" id="userGroup" action="${actionReload}" imgPath="${baselayout}"  withCheckBox="true">
                    <fanta:checkBoxColumn name="guia" id="aditionals" onClick="javascript:check();" property="userId"
                                          headerStyle="listHeader" styleClass="radio listItemCenter" width="5%" />
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
            </td>
        </tr>
    </html:form>
</table>
