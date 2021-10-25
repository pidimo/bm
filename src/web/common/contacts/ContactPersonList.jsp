<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(active)" value="document.getElementById('activeId').value"/>
</app2:jScriptUrl>

<%
    pageContext.setAttribute("activeList", JSPHelper.getActiveList(request));
%>

<script language="JavaScript">
    <!--
    function jumpAlphabet(obj)
    {
        window.location = ${jsAlphabetUrl};
    }
    //-->
</script>




<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>
        <br>

        <table width="97%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
            <tr>
                <html:form action="/ContactPerson/List.do" focus="parameter(lastName@_firstName@_searchName)">
                    <td class="label"><fmt:message key="Common.search"/></td>
                    <td align="left" class="contain">
                        <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText"
                                   maxlength="40" tabindex="1"/>
                        &nbsp;
                        <html:select property="parameter(active)" styleClass="shortSelect" tabindex="2"
                                     styleId="activeId">
                            <html:options collection="activeList" property="value" labelProperty="label"/>
                        </html:select>
                        &nbsp;
                        <html:submit styleClass="button" tabindex="3"><fmt:message key="Common.go"/></html:submit>
                    </td>
                </html:form>
            </tr>
            <tr>
                <td colspan="2" align="center" class="alpha">
                    <fanta:alphabet action="ContactPerson/List.do" parameterName="lastNameAlpha"
                                    onClick="jumpAlphabet(this);return false;"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <br/>
    </td>
</tr>

<tr>
    <td>

        <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
            <table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <html:form action="/ContactPerson/SearchContact.do">
                        <td class="button">
                            <html:submit styleClass="button"><fmt:message key="ContactPerson.add"/></html:submit>
                        </td>
                    </html:form>
                </tr>
            </table>
        </app2:checkAccessRight>

        <fanta:table list="contactPersonList" width="97%" id="contactPerson" action="ContactPerson/List.do"
                     imgPath="${baselayout}" align="center">
            <c:set var="editLink"
                   value="ContactPerson/Forward/Update.do?dto(addressId)=${contactPerson.addressId}&dto(contactPersonId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"/>
            <fanta:columnGroup headerStyle="listHeader" width="5%" title="Common.action">
                <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editLink}" styleClass="listItem"
                                        headerStyle="listHeader" width="50%" image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CONTACTPERSON" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete"
                                        action="ContactPerson/Forward/Delete.do?dto(withReferences)=true&dto(addressId)=${contactPerson.addressId}&dto(contactPersonId)=${contactPerson.contactPersonId}&dto(name1)=${app2:encode(contactPerson.lastName)}&dto(name2)=${app2:encode(contactPerson.firstName)}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/delete.gif"/>
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
                                   contactPersonId="${contactPerson.contactPersonId}" styleClass="mediumSelect"
                                   maxLength="30" optionStyleClass="list" showPrivate="true" showDescription="true"
                                   groupedByTelecomType="true"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItem2Center" title="Common.active" headerStyle="listHeader"
                              width="5%" renderData="false">
                <c:if test="${contactPerson.active == 1}">
                    <img align="middle" src="<c:out value="${sessionScope.baselayout}"/>/img/check.gif" alt=""/>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>

        <app2:checkAccessRight functionality="CONTACTPERSON" permission="CREATE">
            <table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <html:form action="/ContactPerson/SearchContact.do">
                        <td class="button">
                            <html:submit styleClass="button"><fmt:message key="ContactPerson.add"/></html:submit>
                        </td>
                    </html:form>
                </tr>
            </table>
        </app2:checkAccessRight>
    </td>
</tr>
</table>