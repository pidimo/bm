<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>
<%
    boolean errorPage = false;
    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
    addressCmd.putParam("addressId", request.getParameter("contactId"));
    try {
        ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
        request.setAttribute("addressType", resultDTO.get("addressType"));
        request.setAttribute("city", resultDTO.get("city"));
        request.setAttribute("countryCode", resultDTO.get("countryCode"));
        request.setAttribute("code", resultDTO.get("code"));
        request.setAttribute("name1", resultDTO.get("name1"));
        request.setAttribute("name2", resultDTO.get("name2"));
        request.setAttribute("name3", resultDTO.get("name3"));
        request.setAttribute("addressName", resultDTO.get("addressName"));
    } catch (Exception e) {
        errorPage = true;
    }
    request.setAttribute("errorPage", new Boolean(errorPage));
%>

<c:set var="tabHeader" scope="request">
    <div class="label tabLabel">
        <c:choose>
            <c:when test="${addressType == personType }">
                <tags:colonMessage key="Contact.person"/><c:out value=" ${name1}"/><c:if test="${name2 != null}">,
                <c:out value="${name2}"/></c:if>
            </c:when>
            <c:otherwise><tags:colonMessage key="Contact.organization"/><c:out value="${name1}"/></c:otherwise>
        </c:choose>
    </div>
</c:set>


<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(addressId)" value="${param.contactId}"/>
<div class="tabMenu">
    <ul>
        <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
            <li>
                <c:choose>
                    <c:when test="${addressType == personType}">
                        <app:link page="/contacts/Person/Forward/View.do" name="tabParams" accesskey="1">
                            [1] <fmt:message key="Contacts.Tab.detail"/>
                        </app:link>
                    </c:when>
                    <c:otherwise>
                        <app:link page="/contacts/Organization/Forward/View.do" name="tabParams">
                            [1] <fmt:message key="Contacts.Tab.detail"/>
                        </app:link>
                    </c:otherwise>
                </c:choose>
            </li>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="CONTACTPERSON" permission="VIEW">
            <li>
                <app:link action="/contacts/ContactPerson/List.do?parameter(active)=1" accesskey="2">
                    [2] <fmt:message key="Contacts.Tab.contactPersons"/>
                </app:link>
            </li>
        </app2:checkAccessRight>
    </ul>
</div>
