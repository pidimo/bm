<%@ page import="com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd,
                 com.piramide.elwis.utils.ContactConstants,
                 com.piramide.elwis.web.admin.session.User,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate"%>
<%@ include file="/Includes.jsp" %>

<%
pageContext.setAttribute("languageList", JSPHelper.getLanguageList(request));
%>

<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
<%
    boolean errorPage = false;
    //User name
    LightlyAddressCmd addressCmd = new LightlyAddressCmd();
    addressCmd.putParam("addressId", ((User)session.getAttribute("user")).getValue("userAddressId"));
    request.setAttribute("addressId", ((User)session.getAttribute("user")).getValue("userAddressId"));

    try {
        //user name
        ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, request);
        request.setAttribute("addressType", resultDTO.get("addressType"));
        request.setAttribute("name1", resultDTO.get("name1"));
        request.setAttribute("name2", resultDTO.get("name2"));
        request.setAttribute("name3", resultDTO.get("name3"));

    } catch (Exception e) {
        errorPage = true;
    }
    request.setAttribute("errorPage", new Boolean(errorPage));
%>


<c:set var="path" value="${pageContext.request.contextPath}"/>

   <html:form action="/User/Save.do" focus="dto(maxRecentList)" >
   <html:hidden property="dto(op)" value="update"/>
   <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
   <html:hidden property="dto(opPreference)" value="preferences"/>

    <table cellSpacing=0 cellPadding=0 width="500" border=0 align="center" >
    <TR>
        <td>
            <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
                <TR>
                    <td class="button">
                        <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button">${save}</app2:securitySubmit>
                        <%--<html:submit property="dto(save)" styleClass="button" ><c:out value="${save}"/></html:submit>--%>

                        <%--top links--%>
                        &nbsp;
                        <c:if test="${!errorPage}">
                            <%-- Edit personal info link--%>
                                <c:choose>
                                    <c:when test="${addressType == personType}">
                                        <c:set var="editLink" value="contacts/Person/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="editLink" value="contacts/Organization/Forward/Update.do?contactId=${addressId}&dto(addressId)=${addressId}&dto(addressType)=${addressType}&dto(name1)=${app2:encode(name1)}&dto(name2)=${app2:encode(name2)}&dto(name3)=${app2:encode(name3)}&index=0"/>
                                    </c:otherwise>
                                </c:choose>
                                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                                    <app:link action="${editLink}" styleClass="folderTabLink">
                                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/edit.gif" title="<fmt:message    key="ContactPerson.personalInfo"/>" border="0" alt=""/>
                                        <fmt:message   key="ContactPerson.personalInfo"/>
                                    </app:link>
                                </app2:checkAccessRight>
                        </c:if>
                    </td>
                </TR>
            </table>
        </td>
    </TR>
    <TR>
        <TD>
            <table id="User.jsp" border="0" cellpadding="0" cellspacing="0" width="500" align="center" class="container">
                <TR>
                    <TD colspan="2" class="title">
                        <c:out value="${title}"/>
                    </TD>
                </TR>
                <TR>
                    <TD class="label" width="15%"><fmt:message   key="User.language"/></TD>
                    <TD class="contain" width="35%">
                        <html:select property="dto(favoriteLanguage)" styleClass="mediumSelect" tabindex="1"  >
                            <html:options collection="languageList"  property="value" labelProperty="label"/>
                        </html:select>
                    </TD>
                </tr>
                <tr>
                    <TD class="label"><fmt:message   key="User.login"/></TD>
                    <TD class="contain">
                        <html:hidden property="dto(userLogin)" write="true" />
                        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    </TD>
             </tr>
             <tr>
                <TD class="label"><fmt:message   key="User.maxRecentes"/></TD>
                <TD class="contain">
                  <html:text property="dto(maxRecentList)" styleClass="mediumText" maxlength="2" tabindex="2"/>
                </TD>
             </tr>
             <tr>
                <TD class="label"><fmt:message   key="User.rows"/></TD>
                <TD class="contain">
                <html:text property="dto(rowsPerPage)"  styleClass="mediumText" maxlength="2" tabindex="3"/>
                </TD>
             </tr>
             <tr>
                <TD class="label"><fmt:message   key="User.timeout"/></TD>
                <TD class="contain">
                <html:text property="dto(timeout)"  styleClass="mediumText" maxlength="2" tabindex="4"/>
                 &nbsp; (<fmt:message   key="Common.minutes"/>)
                </TD>
             </tr>
            <tr>
                <TD class="label"><fmt:message   key="User.timeZone"/></TD>
                <td class="contain">
                    <c:set var="timeZonesConstants" value="${app2:getTimeZones(pageContext.request)}"/>
                    <html:select property="dto(timeZone)" styleClass="mediumSelect" tabindex="5" >
                        <html:option value="">&nbsp;</html:option> 
                        <html:options collection="timeZonesConstants"  property="value" labelProperty="label"/>
                    </html:select>
                </td>
            </tr>

      </table>
     </TD>
  </tr>
  <TR>
    <TD class="button">
        <app2:securitySubmit operation="UPDATE" functionality="USERSETTINGS" styleClass="button">${save}</app2:securitySubmit>
         <%--<html:submit property="dto(save)" styleClass="button" tabindex="8" ><c:out value="${save}"/></html:submit>--%>
    </td>
   </TR>

    </table>
 </html:form>

 
