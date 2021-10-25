<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<script language="JavaScript">
    <!--
    function check() {
        field = document.getElementById('listc').excludes;
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
        document.getElementById('Import_value').value = true;
        document.forms[1].submit();
    }

    //this is requireq of fanta table paginator
    function setSubmit(issubmit) {
        return issubmit;
    }

    //-->
</script>

<%
    if (request.getAttribute("searchAllAddressList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("searchAllAddressList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<br>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                <tr>
                    <td height="20" class="title" colspan="2">
                        <fmt:message key="Campaign.addCriterias"/>
                    </td>
                </tr>
                <tr>
                    <html:form action="/Recipients/Add.do?campaignId=${param.campaignId}"
                               focus="parameter(contactSearchName)">
                        <td class="label">
                            <fmt:message key="Common.search"/>
                        </td>
                        <td class="contain">
                            <html:text property="parameter(contactSearchName)"
                                       styleClass="largeText"/>
                            &nbsp;
                            <html:submit styleClass="button">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </td>
                    </html:form>
                </tr>
                <tr>
                    <td colspan="2" align="center" class="alpha">
                        <fanta:alphabet action="Recipients/Add.do?campaignId=${param.campaignId}"
                                        parameterName="name1A1"/>
                    </td>
                </tr>
            </table>
            <br/>
        </td>
    </tr>

    <html:form action="/Recipients/Import.do" styleId="listc" onsubmit="return false;">
        <tr>
            <td class="button"><!--Button create up -->
                <html:button property="" styleClass="button" onclick="goSubmit()">
                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                </html:button>
                <c:url var="url" value="/campaign/Recipients/List.do?index=${param.index}">
                    <c:param name="campaignId" value="${param.campaignId}"/>
                </c:url>
                <html:button property="" styleClass="button" onclick="location.href='${url}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>

                <html:hidden property="Import_value" value="false" styleId="Import_value"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
            </td>
        </tr>
        <tr>
            <td>
                <fanta:table list="searchAllAddressList" width="100%" id="contact"
                             action="Recipients/Add.do?campaignId=${param.campaignId}" imgPath="${baselayout}"
                             withCheckBox="true">
                    <c:if test="${size >0}">
                        <!--check box value is sent as "addressId,contactPersonId"-->
                        <fanta:checkBoxColumn name="guia" id="excludes" onClick="javascript:check();"
                                              property="organizationId" headerStyle="listHeader"
                                              styleClass="radio listItemCenter" width="5%">
                            <c:choose>
                                <c:when test="${empty contact.contactPersonAddressId}">
                                    ${contact.addressId},
                                </c:when>
                                <c:otherwise>
                                    ${contact.contactPersonAddressId},${contact.addressId}
                                </c:otherwise>
                            </c:choose>
                        </fanta:checkBoxColumn>
                    </c:if>
                    <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="3%"
                                      renderData="false">

                        <c:choose>
                            <c:when test="${not empty contact.contactPersonAddressId}">
                                <c:set var="personPrefixImageName" value="person"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="personPrefixImageName" value="person-private"/>
                            </c:otherwise>
                        </c:choose>

                        <html:img
                                src="${baselayout}/img/${contact.addressType == personType? personPrefixImageName : 'org'}.gif"
                                border="0"/>

                    </fanta:dataColumn>

                    <c:set var="viewLink"
                           value="javascript:viewContactDetailInfo(${contact.addressType},${contact.addressId});"/>
                    <fanta:dataColumn name="addressName1" action="${viewLink}" styleClass="listItem"
                                      title="Contact.name" headerStyle="listHeader" width="47%" orderable="true"
                                      useJScript="true">
                    </fanta:dataColumn>

                    <c:set var="viewLink2"
                           value="javascript:viewContactDetailInfo(${contact.addressType2},${contact.contactPersonAddressId});"/>
                    <fanta:dataColumn name="addressName2" action="${viewLink2}" styleClass="listItem2"
                                      title="ContactPerson.contactName" headerStyle="listHeader" width="45%"
                                      orderable="true" renderData="false" useJScript="true">
                        <c:if test="${not empty contact.addressName2}">
                            <fanta:textShorter title="${contact.addressName2}">
                                <c:out value="${contact.addressName2}"/>
                            </fanta:textShorter>
                        </c:if>
                    </fanta:dataColumn>
                </fanta:table>
            </td>
        </tr>
    </html:form>
</table>



