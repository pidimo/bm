<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(active)" value="document.getElementById('activeId').value"/>
</app2:jScriptUrl>
<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>
<script language="JavaScript">
    function jumpAlphabet(obj) {
        window.location = ${jsAlphabetUrl};
    }
</script>
<div class="${app2:getListWrapperClasses()}">
    <html:form action="/ContactPerson/List.do" focus="parameter(contactSearchName)"
               styleClass="form-horizontal">
        <fieldset>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="lastName@_firstName@_searchName_id">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:text property="parameter(contactSearchName)"
                               styleId="lastName@_firstName@_searchName_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"
                               maxlength="40" tabindex="1"/>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="">

                </label>

                <div class="${app2:getFormContainClasses(false)}">
                    <html:select property="parameter(active)" styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                 tabindex="2"
                                 styleId="activeId">
                        <html:options collection="activeList" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchButton()}">
                <div class="col-sm-12">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="3"><fmt:message
                            key="Common.go"/></html:submit>
                </div>
            </div>
        </fieldset>
    </html:form>
    <fanta:alphabet action="ContactPerson/List.do"
                    parameterName="lastNameAlpha"
                    mode="bootstrap"
                    onClick="jumpAlphabet(this);return false;"/>
    <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
        <html:form action="/ContactPerson/SearchContact.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="ContactPerson.add"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="contactPersonList" width="97%" id="contactPerson" action="ContactPerson/List.do"
                     imgPath="${baselayout}" align="center" styleClass="${app2:getFantabulousTableClases()}">
            <c:set var="editLink"
                   value="ContactPerson/Forward/Update.do?dto(addressId)=${contactPerson.addressId}&dto(contactPersonId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CONTACTPERSON" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete"
                                        action="ContactPerson/Forward/Delete.do?dto(withReferences)=true&dto(addressId)=${contactPerson.addressId}&dto(contactPersonId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="contactPersonName" action="${editLink}" styleClass="listItem"
                              title="ContactPerson.name" headerStyle="listHeader" width="30%" orderable="true"
                              maxLength="40">
            </fanta:dataColumn>
            <fanta:dataColumn name="department" styleClass="listItem" title="ContactPerson.department"
                              headerStyle="listHeader" width="20%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="function" styleClass="listItem" title="ContactPerson.function"
                              headerStyle="listHeader" width="20%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="telecoms" styleClass="listItem" title="ContactPerson.telecomNumbers"
                              headerStyle="listHeader" width="20%" renderData="false">
                <app:telecomSelect numberColumn="telecomnumber" addressId="${contactPerson.addressId}"
                                   contactPersonId="${contactPerson.contactPersonId}"
                                   styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                   maxLength="30" optionStyleClass="list" showPrivate="true" showDescription="true"
                                   groupedByTelecomType="true"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItem2Center" title="Common.active" headerStyle="listHeader"
                              width="5%" renderData="false">
                <c:if test="${contactPerson.active == 1}">
                    <%--<img align="middle" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>--%>
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
        <html:form action="/ContactPerson/SearchContact.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="ContactPerson.add"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
</div>

<style type="text/css">
    .alpha {
        border: transparent;
    }
</style>