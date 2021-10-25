<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function select(id, name) {
        opener.selectField('fieldViewUserId_id', id, 'fieldViewUserName_id', name);
    }
    //-->
</script>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="ProjectUser.search"/>
        </td>
    </tr>


    <TR>
        <html:form action="/ProjectUser/SearchElwisUser.do" focus="parameter(lastName@_firstName@_searchName)">
            <td class="label" width="15%">
                <fmt:message key="Common.search"/>
            </td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText" maxlength="40"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </td>
        </html:form>
    </TR>


    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="ProjectUser/SearchElwisUser.do" parameterName="lastName"/>
        </td>
    </tr>


    <tr>
        <td colspan="2" align="center">
            <br/>
            <fanta:table width="100%"
                         id="viewUser"
                         action="ProjectUser/SearchElwisUser.do"
                         imgPath="${baselayout}"
                         align="center">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name=""
                                        useJScript="true"
                                        action="javascript:select('${viewUser.userId}','${app2:jscriptEncode(viewUser.userName)}');"
                                        title="Common.select" styleClass="listItem" headerStyle="listHeader"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName"
                                  styleClass="listItem"
                                  title="Employee.name"
                                  headerStyle="listHeader"
                                  width="65%"
                                  orderable="true"/>
                <fanta:dataColumn name="type"
                                  styleClass="listItem2"
                                  title="User.typeUser"
                                  renderData="false"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true">
                    <c:if test="${viewUser.type == '1'}">
                        <fmt:message key="User.intenalUser"/>
                    </c:if>
                    <c:if test="${viewUser.type == '0'}">
                        <fmt:message key="User.externalUser"/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
</table>