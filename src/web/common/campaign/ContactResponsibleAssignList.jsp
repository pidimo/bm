<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(ofCustomer)" value="value"/>
    <app2:jScriptUrlParam param="parameter(allContacts)" value="isAllContacts"/>
</app2:jScriptUrl>
<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<script language="JavaScript" type="text/javascript">
    <!--
    function check() {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = true;
                    }
                } else {
                    if (!field.disabled) field.checked = true;
                }
            }
        } else {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = false;
                    }
                } else {
                    if (!field.disabled) field.checked = false;
                }
            }
        }
    }

    function enableCheckbox() {
        field = document.getElementById('listc').selected;
        var i;
        if (field != undefined) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++) {
                    if (field[i].disabled) field[i].disabled = false;
                }
            } else {
                if (field.disabled) field.disabled = false;
            }
        }
    }

    function jump(obj) {
        var value = "";
        if (document.getElementById('ofCustomer').checked) {
            value = "on";
        }

        var isAllContacts = "";
        if (document.getElementById('allContacts').checked) {
            isAllContacts = "on";
        }
        window.location =${jsAlphabetUrl};
    }

    var isListSubmit = "false";
    function goSubmit() {
        if (isListSubmit == "false") {
            setSubmit(true);
            enableCheckbox();
            document.forms[1].submit();
        }
        isListSubmit = "false";
    }

    function goClose() {
        window.close();
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = ('true' == ss);
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        isListSubmit = "true";
    }

    //-->
</script>

<%--
#########EE${param.employeeId}
#########UU${param.userId}
--%>
<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td class="title">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td colspan="2">
            <html:form action="/Activity/User/Assign/List.do?campaignId=${param.campaignId}"
                       focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)">
                <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr>
                        <td class="label">
                            <fmt:message key="Campaign.activity.user"/>
                        </td>
                        <td class="contain">
                            <c:out value="${param.userName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label"><fmt:message key="Common.search"/></td>
                        <td class="contain" nowrap>
                            <html:text
                                    property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                                    styleClass="largeText"/>&nbsp;
                            <html:checkbox property="parameter(allContacts)" styleId="allContacts"
                                           styleClass="radio"><fmt:message key="Activity.user.manualAssign.allContact"/></html:checkbox>&nbsp;
                            <html:checkbox property="parameter(ofCustomer)" styleId="ofCustomer"
                                           styleClass="radio"><fmt:message
                                    key="Campaign.activity.user.customerResponsibleOf"/></html:checkbox>&nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                        </td>
                    </tr>
                    <!-- choose alphbet to simple and advanced search  -->
                    <tr>
                        <td align="center" colspan="2" class="alpha">
                            <fanta:alphabet
                                    action="/Activity/User/Assign/List.do?campaignId=${param.campaignId}&employeeId=${param.employeeId}&userId=${param.userId}&userName=${param.userName}"
                                    parameterName="contactName1" onClick="jump(this);return false;"/>
                        </td>
                    </tr>

                    <%
                        if (request.getAttribute("AssignCampaignContactList") != null) {
                            ResultList resultList = (ResultList) request.getAttribute("AssignCampaignContactList");
                            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
                        }
                    %>
                </table>
            </html:form>
        </td>
    </tr>

    <html:form action="/Activity/CampContact/User/Assign.do?campaignId=${param.campaignId}" styleId="listc"
               onsubmit="return testSubmit();">
        <tr>
            <td class="button">
                <html:hidden property="dto(op)" value="update"/>
                <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                <html:hidden property="dto(userId)" value="${param.userId}"/>
                <html:hidden property="dto(activityId)" value="${param.activityId}"/>

                <c:if test="${size >0}">
                    <html:button property="save" styleClass="button" onclick="goSubmit()"><fmt:message
                            key="Campaign.activity.user.assign"/></html:button>
                </c:if>

                <app:url var="urlUserList" value="/Activity/User/List.do?activityId=${param.activityId}"/>
                <html:button property="cancel" styleClass="button" onclick="location.href='${urlUserList}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <fanta:table list="AssignCampaignContactList" align="left" width="100%" id="campaignContact"
                             action="Activity/User/Assign/List.do?campaignId=${param.campaignId}&employeeId=${param.employeeId}&userId=${param.userId}&userName=${param.userName}"
                             imgPath="${baselayout}" withCheckBox="true">

                    <c:if test="${size >0}">

                        <c:if test="${not empty campaignContact.campaignContactId}">
                            <!--this is necessary hidden with styleId to send as array in form-->
                            <html:hidden property="listViewContactIds" styleId="viewId"
                                         value="${campaignContact.campaignContactId}"/>
                        </c:if>

                        <c:choose>
                            <c:when test="${empty campaignContact.userId or (param.userId eq campaignContact.userId)}">
                                <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                                      property="campaignContactId" headerStyle="listHeader"
                                                      styleClass="radio listItemCenter" width="5%"
                                                      checked="${param.userId eq campaignContact.userId}"
                                                      disabled="${app2:activityUserHasTaskCreated(campaignContact.campaignContactId,param.campaignId)}"/>
                            </c:when>
                            <c:otherwise>
                                <fanta:actionColumn name="" label="" styleClass="listItem" headerStyle="listHeader"
                                                    width="5%">
                                    &nbsp;
                                </fanta:actionColumn>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader" width="4%">

                        <c:choose>
                            <c:when test="${not empty campaignContact.contactPersonName}">
                                <c:set var="personPrefixImageName" value="person"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="personPrefixImageName" value="person-private"/>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${personType == campaignContact.addressType}">
                            <html:img src="${baselayout}/img/${personPrefixImageName}.gif" border="0"/>
                        </c:if>

                        <c:if test="${organizationType == campaignContact.addressType}">
                            <c:choose>
                                <c:when test="${not empty campaignContact.contactPersonName}">
                                    <html:img src="${baselayout}/img/person.gif" border="0"/>
                                </c:when>
                                <c:otherwise>
                                    <html:img src="${baselayout}/img/org.gif" border="0"/>
                                </c:otherwise>
                            </c:choose>
                        </c:if>

                    </fanta:actionColumn>
                    <fanta:dataColumn name="contactNames" useJScript="true"
                                      action="javascript:viewContactDetailInfo(${campaignContact.addressType},${campaignContact.addressId});"
                                      styleClass="listItem" title="Campaign.company" headerStyle="listHeader"
                                      width="23%"
                                      orderable="true" maxLength="45"/>
                    <fanta:dataColumn name="contactPersonName" useJScript="true"
                                      action="javascript:viewContactDetailInfo(1,${campaignContact.contactPersonId});"
                                      styleClass="listItem" title="Campaign.contactPerson" headerStyle="listHeader"
                                      width="23%"
                                      orderable="true" maxLength="30">
                    </fanta:dataColumn>
                    <fanta:dataColumn name="customerResponsibleName"
                                      styleClass="listItem" title="Activity.user.manualAssign.customerResponsible"
                                      headerStyle="listHeader" width="22%"
                                      orderable="true">
                    </fanta:dataColumn>
                    <fanta:dataColumn name="userRelatedName"
                                      styleClass="listItem2" title="Activity.user.manualAssign.relatedTo"
                                      headerStyle="listHeader" width="22%"
                                      orderable="true">
                    </fanta:dataColumn>
                </fanta:table>
            </td>
        </tr>
        <tr>
            <td class="button">
                <c:if test="${size >0}">
                    <html:button property="save" styleClass="button" onclick="goSubmit()"><fmt:message
                            key="Campaign.activity.user.assign"/></html:button>
                </c:if>

                <html:button property="cancel" styleClass="button" onclick="location.href='${urlUserList}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
    </html:form>

</table>

