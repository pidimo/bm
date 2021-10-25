<%@ include file="/Includes.jsp" %>
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
        document.getElementById('isSubmit').value = true
        document.getElementById('listc').submit();
    }

</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="${actionReload}" styleId="listc" focus="parameter(lastName@_firstName@_login)">

        <div class="searchContainer">
            <legend class="title">
                <fmt:message key="RoleUser.user.add"/>
            </legend>
            <div class="row">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()} marginButton">
                    <div class="input-group">
                        <html:text property="parameter(lastName@_firstName@_login)"
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

            <div class="${app2:getAlphabetWrapperClasses()}">
                <fanta:alphabet
                        action="RoleUser/Forward/UserImportList.do?roleId=${param.roleId}&roleName=${param.roleName}"
                        parameterName="lastName" mode="bootstrap"/>
            </div>
        </div>


        <html:hidden property="isSubmit" value="false" styleId="isSubmit"/>
        <html:hidden property="dto(roleId)" value="${param.roleId}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

        <div class="wrapperButton">
            <app2:checkAccessRight functionality="USER" permission="UPDATE">
                <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit();">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:button>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="table-responsive">
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}" width="100%"
                         id="userGroup" action="${action}" imgPath="${baselayout}" align="left"
                         list="roleUserSearchList">
                <fanta:checkBoxColumn name="guia" id="aditionals" onClick="javascript:check();"
                                      property="userId"
                                      headerStyle="listHeader" styleClass="listItemCenter" width="5%"/>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name"
                                  headerStyle="listHeader" width="45%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="login" styleClass="listItem" title="User.userName"
                                  headerStyle="listHeader"
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
