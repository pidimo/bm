<%@ page import="com.piramide.elwis.utils.CampaignConstants"%>
<%@ include file="/Includes.jsp" %>

<table cellSpacing=0 cellPadding=4 width="95%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="window.close();">
                <fmt:message key="Common.close"/>
            </html:button>
        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="95%" align="center" class="container">
    <tr>
        <td height="20" colspan="2" class="title">
            <fmt:message key="Campaign.activity.emailSend.popup.recipientWithoutEmail"/>
        </td>
    </tr>

    <TH class='listHeader' width='50%'><fmt:message   key="Campaign.company"/></TH>
    <TH class='listHeader' width='50%'><fmt:message   key="Campaign.contactPerson"/> </TH>

    <c:choose>
         <c:when test="${not empty withoutEmailList}">
             <c:set var="AWITHOUTMAIL" value="<%=CampaignConstants.ADDRESS_WITHOUT_MAIL%>"/>
             <c:set var="CPWITHOUTMAIL" value="<%=CampaignConstants.CONTACTPERSON_WITHOUT_MAIL%>"/>
             <c:forEach var="recipient" items="${withoutEmailList}">
                 <tr class="listRow">
                     <td class="listItem">
                         <c:out value="${recipient.contactName}"/>
                         <c:if test="${recipient.addressWithoutMail eq AWITHOUTMAIL}">
                             <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt="" title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                         </c:if>
                     </td>
                     <td class="listItem2">
                         <c:out value="${recipient.contactPersonName}"/>
                         <c:if test="${recipient.addressWithoutMail eq CPWITHOUTMAIL}">
                             <img src='<c:out value="${sessionScope.baselayout}"/>/img/close.gif' alt="" title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"/>
                         </c:if>
                         &nbsp;
                     </td>
                 </tr>
             </c:forEach>
         </c:when>
         <c:otherwise>
                 <tr class="listRow">
                     <td colspan="2" align="left">&nbsp;<fmt:message key="Common.list.empty"/></td>
                 </tr>
         </c:otherwise>
    </c:choose>
</table>
<table cellSpacing=0 cellPadding=4 width="95%" border=0 align="center">
    <tr>
        <td class="button">
            <html:button property="" styleClass="button" onclick="window.close();">
                <fmt:message key="Common.close"/>
            </html:button>
        </td>
    </tr>
</table>
