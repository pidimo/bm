<%@ page import="org.alfacentauro.fantabulous.controller.ResultList"%>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function check(){
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked){
            if(field.length != undefined){
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            }else
                field.checked = true;
        } else {
            if(field.length != undefined){
                for (i = 0; i < field.length; i++)
                   field[i].checked = false;
            }else
                field.checked = false;
        }
    }

    function goSubmit() {
        document.forms[1].submit();
    }

    //this is called in onLoad body property
    function goToParent(){
        opener.selectedSubmit();
        window.close();
    }

    function goClose() {
        window.close();
    }

    //this is requireq of fanta table paginator
    function setSubmit(issubmit) {
        return issubmit;
    }
    //-->
</script>

<%
    if (request.getAttribute("elwisUserList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("elwisUserList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="2"><fmt:message key="Campaign.activity.user.searchUser"/></td>
    </tr>

    <html:form action="/Activity/Add/UserImportList.do" focus="parameter(lastName@_firstName@_searchName)">
        <html:hidden property="parameter(ownUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        <TR>
            <td class="label" width="15%"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain" width="85%">
                <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText" maxlength="40"/>
                &nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </TR>
    </html:form>

    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="/Activity/Add/UserImportList.do" parameterName="lastNameAlpha"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <br/>
            <html:form action="/Campaign/Activity/User/Add.do?campaignId=${param.campaignId}" styleId="listc" onsubmit="return false;">
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
                <tr>
                    <td class="button"><!--Button create up -->
                        <c:if test="${size >0}">
                            <html:button property="" styleClass="button" onclick="goSubmit()">
                                <fmt:message key="Campaign.activity.searchUser.btn.add"/>
                            </html:button>
                        </c:if>
                        <html:button property="" styleClass="button" onclick="goClose()" >
                            <fmt:message   key="Common.cancel"/>
                        </html:button>
                    </td>
                </tr>

                <tr>
                    <td align="center">
                        <html:hidden property="dto(op)" value="createUser"/>
                        <html:hidden property="dto(activityId)" value="${param.activityId}"/>
                        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>


                        <fanta:table list="elwisUserList" width="100%" id="viewUser" action="Activity/Add/UserImportList.do"
                                     imgPath="${baselayout}" align="center" withCheckBox="true">
                            <c:if test="${size >0}">
                                <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                                      property="userId" headerStyle="listHeader"
                                                      styleClass="radio listItemCenter" width="5%"/>
                            </c:if>

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
                    </td>
                </tr>
                <tr>
                    <td class="button"><!--Button create up -->
                        <c:if test="${size >0}">
                            <html:button property="" styleClass="button" onclick="goSubmit()">
                                <fmt:message key="Campaign.activity.searchUser.btn.add"/>
                            </html:button>
                        </c:if>
                        <html:button property="" styleClass="button" onclick="goClose()" >
                            <fmt:message   key="Common.cancel"/>
                        </html:button>
                    </td>
                </tr>
            </table>
            </html:form>
        </td>
    </tr>
</table>
