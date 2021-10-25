<%@ include file="/Includes.jsp" %>

<app:url var="addUser" value="/RoleUser/Forward/UserImportList.do"/> <%--AQUI--%>
<script language="JavaScript">
    <!--
    function jump(obj) {
        window.location = '${addUser}';
    }

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
        document.getElementById('isSubmit').value = true;
        document.getElementById('listc').submit();
    }
    //-->
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/RoleUser/RoleUserList.do" styleId="listc" styleClass="">
        <html:hidden property="isSubmit" value="false" styleId="isSubmit"/>
        <legend class="title">
            <fmt:message key="Admin.User.Title.search"/>
        </legend>
        <div class="form-horizontal">
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.search"/>
                </label>
                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(name1@_name2@_name3)"
                                   styleClass="largeText ${app2:getFormInputClasses()}"
                                   maxlength="40"/>
               <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.go"/></html:submit>
               </span>
                    </div>
                </div>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet
                    mode="bootstrap"
                    action="RoleUser/RoleUserList.do?roleId=${param.roleId}&roleName=${app2:encode(param.roleName)}"
                    parameterName="name1Alpha"/>
        </div>


        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="USER" permission="UPDATE">
                <html:button property="dto(add)" styleClass="button ${app2:getFormButtonClasses()}" onclick="javascript:jump(this);">
                    <fmt:message key="Common.add"/>
                </html:button>
                <html:button property="dto(delete)" styleClass="button ${app2:getFormButtonClasses()}"
                             onclick="javascript:goSubmit();">
                    <fmt:message key="Common.deleteSelected"/>
                </html:button>
            </app2:checkAccessRight>
        </div>
        <div class="table-responsive">
            <fanta:table list="roleUserList" width="100%" id="myUser"
                         styleClass="${app2:getFantabulousTableClases()}"
                         mode="bootstrap"
                         action="RoleUser/RoleUserList.do?roleId=${param.roleId}"
                         imgPath="${baselayout}">

                <app:url var="deleteAction"
                         value="RoleUser/Forward/Delete.do?dto(userId)=${myUser.userId}&dto(roleId)=${myUser.roleId}"
                         enableEncodeURL="false"/>
                <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                    <app2:checkAccessRight functionality="USER" permission="VIEW">
                        <fanta:checkBoxColumn name="guia" id="aditionals" onClick="javascript:check();"
                                              property="userId"
                                              headerStyle="listHeader"
                                              styleClass=" listItemCenter"/>
                        <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                            styleClass="listItem"
                                            headerStyle="listHeader"
                                            glyphiconClass="${app2:getClassGlyphTrash()}"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="userName" styleClass="listItem2" title="Contact.name"
                                  headerStyle="listHeader" width="95%"
                                  orderable="true" maxLength="25"/>

            </fanta:table>
        </div>
    </html:form>
</div>





