<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript" type="text/javascript">
    <!--
    function check() {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            } else
                field.checked = true;
        } else {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            } else
                field.checked = false;
        }
    }

    function goSubmit() {
        document.forms[1].submit();
    }

    //this is called in onLoad body property
    function goToParent() {
        parent.selectedSubmit();
        parent.hideBootstrapPopup();
    }

    function goClose() {
        parent.hideBootstrapPopup();
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

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Activity/Add/UserImportList.do" focus="parameter(lastName@_firstName@_searchName)"
               styleClass="form-horizontal">
        <html:hidden property="parameter(ownUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group col-xs-12">
                    <html:text property="parameter(lastName@_firstName@_searchName)"
                               styleClass="${app2:getFormInputClasses()} largeText"
                               maxlength="40"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="/Activity/Add/UserImportList.do" parameterName="lastNameAlpha" mode="bootstrap"/>
    </div>

    <html:form action="/Campaign/Activity/User/Add.do?campaignId=${param.campaignId}" styleId="listc"
               onsubmit="return false;">
        <div class="${app2:getFormGroupClasses()}"><!--Button create up -->
            <c:if test="${size >0}">
                <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit()">
                    <fmt:message key="Campaign.activity.searchUser.btn.add"/>
                </html:button>
            </c:if>
            <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="goClose()">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>


        <div class="table-responsive">
            <html:hidden property="dto(op)" value="createUser"/>
            <html:hidden property="dto(activityId)" value="${param.activityId}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                         list="elwisUserList"
                         width="100%" id="viewUser"
                         action="Activity/Add/UserImportList.do"
                         imgPath="${baselayout}" align="center" withCheckBox="true">
                <c:if test="${size >0}">
                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                          property="userId" headerStyle="listHeader"
                                          styleClass="listItemCenter" width="5%"/>
                </c:if>

                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name"
                                  headerStyle="listHeader"
                                  width="65%" orderable="true"/>
                <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser"
                                  renderData="false"
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


        <div class="${app2:getFormGroupClasses()}"><!--Button create up -->
            <c:if test="${size >0}">
                <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit()">
                    <fmt:message key="Campaign.activity.searchUser.btn.add"/>
                </html:button>
            </c:if>
            <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="goClose()">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </html:form>
</div>