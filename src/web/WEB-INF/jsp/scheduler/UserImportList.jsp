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
        document.getElementById('Import_value').value = true;
        document.forms[1].submit();
    }

</script>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/${action}" focus="parameter(lastName@_firstName@_login)" styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="Admin.User.Title"/>
            </legend>
            <div class="row">
                <div class="col-xs-12 col-sm-4 marginButton">
                    <label class="${app2:getFormLabelClasses()} paddingLeftRemove">
                        <fmt:message key="Common.search"/>
                    </label>

                    <div class="${app2:getFormContainClasses(view)} paddingLeftRemove">
                        <html:text property="parameter(lastName@_firstName@_login)"
                                   styleClass="${app2:getFormInputClasses()}"
                                   maxlength="40"/>
                    </div>
                </div>
                <c:if test="${param.module !='scheduler'}">
                    <div class="col-xs-12 col-sm-2 marginButton">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </div>
                </c:if>
                <c:if test="${param.module=='scheduler'}">
                    <div class="col-xs-12 col-sm-6">
                        <div class="row">
                            <div class="col-xs-11 col-sm-6 marginButton">
                                <label class="control-label col-xs-12 col-sm-6 paddingLeftRemove">
                                    <fmt:message key="User.externalAlso"/>
                                </label>

                                <div class="parentElementInputSearch col-xs-11 col-sm-6 paddingLeftRemove">
                                    <div class="col-xs-2">
                                        <div class="radiocheck">
                                            <div class="checkbox checkbox-default">
                                                <html:checkbox property="parameter(userType)" styleId="userType_id"/>
                                                <label for="userType_id"></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="col-xs-11 col-sm-6 marginButton">
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
        <fanta:alphabet action="${action}" parameterName="lastNameAlpha" mode="bootstrap"/>
    </div>

    <html:form action="${fantaImportAction}" styleId="listc">

        <html:hidden property="Import_value" value="false" styleId="Import_value"/>
        <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit()">
                <fmt:message
                        key="Contact.Organization.Employee.addEmployee"/>
            </html:button>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" property="dto(cancel)">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="table-responsive">
            <fanta:table width="100%" mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                         id="elwisUser"
                         action="${action}"
                         imgPath="${baselayout}"
                         align="center">
                <c:set var="importAction"
                       value="${fantaImportAction}&dto(userId)=${elwisUser.userId}&dto(title)=${app2:encode(param['dto(title)'])}"/>
                <fanta:checkBoxColumn name="guia" id="aditionals" onClick="javascript:check();" property="userId"
                                      headerStyle="listHeader" styleClass="listItemCenter" width="5%"/>
                <fanta:dataColumn name="userName" styleClass="listItem" title="Employee.name"
                                  headerStyle="listHeader"
                                  width="40%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="login" styleClass="listItem" title="User.userName" headerStyle="listHeader"
                                  width="35%" orderable="true"/>
                <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser" renderData="false"
                                  headerStyle="listHeader" width="15%" orderable="true">
                    <c:if test="${elwisUser.type == '1'}">
                        <fmt:message key="User.intenalUser"/>
                    </c:if>
                    <c:if test="${elwisUser.type == '0'}">
                        <fmt:message key="User.externalUser"/>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </div>
    </html:form>
</div>