<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<div class="table-responsive">
    <table mode="bootstrap" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
           class="${app2:getGeneralFantabulousTableClasses()}">

        <th class='listHeader' width='50%'>
            <fmt:message key="Campaign.company"/>
        </th>
        <th class='listHeader' width='50%'>
            <fmt:message key="Campaign.contactPerson"/>
        </th>

        <c:choose>
            <c:when test="${not empty withoutEmailList}">
                <c:set var="AWITHOUTMAIL" value="<%=CampaignConstants.ADDRESS_WITHOUT_MAIL%>"/>
                <c:set var="CPWITHOUTMAIL" value="<%=CampaignConstants.CONTACTPERSON_WITHOUT_MAIL%>"/>
                <c:forEach var="recipient" items="${withoutEmailList}">
                    <tr class="listRow">
                        <td class="listItem">
                            <c:out value="${recipient.contactName}"/>
                            <c:if test="${recipient.addressWithoutMail eq AWITHOUTMAIL}">
                                <i class="fa fa-times"
                                   title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"
                                   style="cursor: default;"></i>
                            </c:if>
                        </td>
                        <td class="listItem2">
                            <c:out value="${recipient.contactPersonName}"/>
                            <c:if test="${recipient.addressWithoutMail eq CPWITHOUTMAIL}">
                                <i class="fa fa-times" style="cursor: default;"
                                   title="<fmt:message key="Campaign.emailSend.summary.mailNotFound"/>"></i>
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
</div>
