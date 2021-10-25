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
        }
        else {
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

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = 'true' == ss;
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        var ss = document.getElementById('isSubmit').value;
    }

    //-->
</script>

<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2">
            <html:form action="/Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
                       focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)">
                <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
                    <tr>
                        <td class="label"><fmt:message key="Common.search"/></td>
                        <td class="contain" nowrap>
                            <html:text
                                    property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                                    styleClass="largeText"/>&nbsp;
                            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;
                        </td>
                    </tr>
                    <!-- choose alphbet to simple and advanced search  -->
                    <tr>
                        <td align="center" colspan="2" class="alpha">
                            <fanta:alphabet action="/Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
                                            parameterName="contactName1"/>
                        </td>
                    </tr>
                    <br>

                    <c:set var="path" value="${pageContext.request.contextPath}"/>
                    <%
                        if (request.getAttribute("campaignContactPopupList") != null) {
                            ResultList resultList = (ResultList) request.getAttribute("campaignContactPopupList");
                            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
                        }
                    %>
                </table>
            </html:form>
        </td>
    </tr>

    <html:form action="/Campaign/Activity/CampContact/CreateFromRecipients.do?campaignId=${param.campaignId}"
               styleId="listc" onsubmit="return false">
        <tr>
            <td class="button">
                <c:if test="${size >0}">
                    <html:button property="" styleClass="button" onclick="goSubmit()"><fmt:message
                            key="Common.add"/></html:button>
                </c:if>

                <app:url var="urlAddAll"
                         value="/Campaign/Recipients/Forward/CopyAll.do?activityId=${param.activityId}&campaignId=${param.campaignId}"/>
                <html:button property="" styleClass="button" onclick="location.href='${urlAddAll}'">
                    <fmt:message key="Campaign.activity.campaignRecipients.addAll"/>
                </html:button>
                <html:button property="" styleClass="button" onclick="goClose()">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <fanta:table list="campaignContactPopupList" align="left" width="100%" id="campaignContact"
                             action="Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
                             imgPath="${baselayout}" withCheckBox="true">

                    <html:hidden property="dto(recipientsSize)" value="${size}"/>
                    <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                    <html:hidden property="dto(op)" value="createRecipients"/>
                    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                    <html:hidden property="dto(activityId)" value="${param.activityId}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>


                    <c:if test="${size >0}">
                        <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                              property="campaignContactId" headerStyle="listHeader"
                                              styleClass="radio listItemCenter" width="5%"/>
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
                                      width="33%"
                                      orderable="true" maxLength="45"/>
                    <fanta:dataColumn name="contactPersonName" useJScript="true"
                                      action="javascript:viewContactDetailInfo(1,${campaignContact.contactPersonId});"
                                      styleClass="listItem" title="Campaign.contactPerson" headerStyle="listHeader"
                                      width="33%"
                                      orderable="true" maxLength="30">
                        &nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="function" styleClass="listItem2" title="Campaign.function"
                                      headerStyle="listHeader"
                                      width="26%" orderable="true">
                        &nbsp;
                    </fanta:dataColumn>
                </fanta:table>
            </td>
        </tr>
        <tr>
            <td class="button">
                <c:if test="${size >0}">
                    <html:button property="" styleClass="button" onclick="goSubmit()"><fmt:message
                            key="Common.add"/></html:button>
                </c:if>

                <html:button property="" styleClass="button" onclick="location.href='${urlAddAll}'">
                    <fmt:message key="Campaign.activity.campaignRecipients.addAll"/>
                </html:button>
                <html:button property="" styleClass="button" onclick="goClose()">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </td>
        </tr>
    </html:form>

</table>
