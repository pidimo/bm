<%@ include file="/Includes.jsp" %>

<script type="text/javascript">
    function submitOnChange(obj) {
        document.getElementById('mainFormId').submit();
    }
</script>


<c:if test="${empty workOnPerson}">
    <c:set var="workOnPerson" value="false"/>
</c:if>

<tags:initSelectPopup/>
<tags:initSelectPopup/>

<c:set var="focusValue" value="dto(new)"/>
<c:if test="${'create' ==  op && workOnPerson}">
    <c:set var="focusValue" value="dto(userName)"/>
</c:if>
<c:if test="${'create' ==  op && !workOnPerson}">
    <c:set var="focusValue" value="dto(addressId)"/>
</c:if>

<html:form action="${action}" focus="${focusValue}" styleId="mainFormId">
    <html:hidden property="dto(op)" value="${op}"/>

    <html:hidden property="dto(projectId)" value="${param.projectId}"/>

    <html:hidden property="dto(responsibleAddressId)"/>

    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>

    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>

    <table border="0" cellpadding="0" cellspacing="0" width="65%" align="center" class="container">
        <tr>
            <td colspan="5" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTUSER" property="saveButton"
                                     styleClass="button" tabindex="11">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'create' ==  op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="PROJECTUSER"
                                         styleClass="button"
                                         property="SaveAndNew"
                                         tabindex="12">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="13">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
        <TR>
            <TD colspan="5" class="title">
                <c:out value="${title}"/>
            </TD>
        </TR>
        <tr>
            <TD class="label" width="25%">
                <fmt:message key="ProjectAssignee.userName"/>
            </TD>
            <TD class="contain" width="75%" colspan="4">
                <c:choose>
                    <c:when test="${'create' == op}">
                        <c:choose>
                            <c:when test="${workOnPerson}">
                                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                                <app:text property="dto(userName)" styleId="fieldAddressName_id"
                                          styleClass="mediumText" maxlength="35"
                                          view="false"
                                          readonly="true"
                                          tabindex="1"/>

                                <tags:selectPopup url="/contacts/PersonSearch.do"
                                                  name="personSearchList"
                                                  titleKey="Common.search"
                                                  hide="false"
                                                  submitOnSelect="false" tabindex="2"/>
                                <tags:clearSelectPopup keyFieldId="fieldAddressId_id"
                                                       nameFieldId="fieldAddressName_id"
                                                       titleKey="Common.clear"
                                                       hide="false"
                                                       submitOnClear="false" tabindex="3"/>
                            </c:when>
                            <c:otherwise>
                                <html:hidden property="dto(userName)" styleId="fieldAddressName_id"/>
                                <fanta:select property="dto(addressId)"
                                              listName="userBaseList"
                                              labelProperty="name"
                                              valueProperty="addressId"
                                              styleClass="middleSelect"
                                              module="/admin"
                                              firstEmpty="true"
                                              readOnly="${op == 'delete'}"
                                              onChange="javascript:submitOnChange(this);"
                                              tabIndex="1">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <html:hidden property="dto(addressId)"/>
                        <html:hidden property="dto(userName)" styleId="fieldAddressName_id"/>
                        <c:out value="${projectUserForm.dtoMap['userName']}"/>
                    </c:otherwise>
                </c:choose>
            </TD>
        </tr>
        <tr>
            <td class="label" width="25%">
                <fmt:message key="ProjectAssignee.permission"/>
            </td>
            <td class="contain" width="17%">
                <html:checkbox property="dto(new)"
                               styleClass="radio"
                               disabled="${op == 'delete'}"
                               tabindex="4">
                    <fmt:message key="ProjectAssignee.permission.new"/>
                </html:checkbox>
            </td>
            <td class="contain" width="17%">
                <html:checkbox property="dto(view)"
                               styleClass="radio"
                               disabled="${op == 'delete'}"
                               tabindex="5">
                    <fmt:message key="ProjectAssignee.permission.view"/>
                </html:checkbox>
            </td>
            <td class="contain" width="18%">
                <html:checkbox property="dto(confirm)"
                               styleClass="radio"
                               disabled="${op == 'delete'}"
                               tabindex="6">
                    <fmt:message key="ProjectAssignee.permission.confirm"/>
                </html:checkbox>
            </td>
            <td class="contain" width="17%">
                <c:set var="disableAdminPermission"
                       value="${'delete' == op || (not empty projectUserForm.dtoMap['admin'] && not empty projectUserForm.dtoMap['responsibleAddressId'] && projectUserForm.dtoMap['responsibleAddressId'] == projectUserForm.dtoMap['addressId'])}"/>
                <c:if test="${disableAdminPermission}">
                    <html:hidden property="dto(admin)"/>
                </c:if>
                <html:checkbox property="dto(admin)"
                               styleClass="radio"
                               disabled="${disableAdminPermission}"
                               tabindex="7">
                    <fmt:message key="ProjectAssignee.permission.admin"/>
                </html:checkbox>
            </td>
        </tr>
        <tr>
            <td colspan="5" class="button" width="100%">
                <app2:securitySubmit operation="${op}" functionality="PROJECTUSER" property="saveButton"
                                     styleClass="button" tabindex="8">
                    ${button}
                </app2:securitySubmit>
                <c:if test="${'create' ==  op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="PROJECTUSER"
                                         styleClass="button"
                                         property="SaveAndNew" tabindex="9">
                        <fmt:message key="Common.saveAndNew"/>
                    </app2:securitySubmit>
                </c:if>
                <html:cancel styleClass="button" tabindex="10">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </td>
        </tr>
    </table>
</html:form>
