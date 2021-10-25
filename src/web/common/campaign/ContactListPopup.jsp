<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<script language="JavaScript">
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
    if (request.getAttribute("searchAllAddressList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("searchAllAddressList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td height="20" class="title">
            <fmt:message key="Contact.search.ContactsOrContactPerson"/>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                <html:form action="/Campaign/Contact/List/Popup.do?campaignId=${param.campaignId}"
                           focus="parameter(contactSearchName)">
                    <tr>
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
                    </tr>
                </html:form>

                <tr>
                    <td colspan="4" align="center" class="alpha">
                        <fanta:alphabet action="Campaign/Contact/List/Popup.do?campaignId=${param.campaignId}"
                                        parameterName="name1A1"/>
                    </td>
                </tr>
            </table>
            <br/>
        </td>
    </tr>

    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="1" cellspacing="0">
                <html:form action="/Campaign/Activity/CampContact/CreateFromContact.do" styleId="listc"
                           onsubmit="return false;">
                    <tr>
                        <td class="button"><!--Button create up -->
                            <c:if test="${size >0}">
                                <html:button property="" styleClass="button" onclick="goSubmit()">
                                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                                </html:button>
                            </c:if>
                            <html:button property="" styleClass="button" onclick="goClose()">
                                <fmt:message key="Common.cancel"/>
                            </html:button>
                            <html:hidden property="dto(op)" value="createContact"/>
                            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                            <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                            <html:hidden property="dto(activityId)" value="${param.activityId}"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fanta:table list="searchAllAddressList" width="100%" id="contact"
                                         action="Campaign/Contact/List/Popup.do?campaignId=${param.campaignId}"
                                         imgPath="${baselayout}" withCheckBox="true">
                                <c:if test="${size >0}">
                                    <!--check box value is sent as "addressId,contactPersonId"-->
                                    <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
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

                                <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader"
                                                    width="4%">

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
                                            alt="" border="0"/>
                                </fanta:actionColumn>

                                <c:set var="viewLink"
                                       value="javascript:viewContactDetailInfo(${contact.addressType},${contact.addressId});"/>
                                <fanta:dataColumn name="addressName1" action="${viewLink}" styleClass="listItem"
                                                  title="Contact.name" headerStyle="listHeader" width="46%"
                                                  orderable="true" useJScript="true">
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
                    <tr>
                        <td class="button"><!--Button create up -->
                            <c:if test="${size >0}">
                                <html:button property="" styleClass="button" onclick="goSubmit()">
                                    <fmt:message key="Contact.Organization.Employee.addEmployee"/>
                                </html:button>
                            </c:if>
                            <html:button property="" styleClass="button" onclick="goClose()">
                                <fmt:message key="Common.cancel"/>
                            </html:button>
                        </td>
                    </tr>
                </html:form>
            </table>
        </td>
    </tr>
</table>
