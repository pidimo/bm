<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants"%>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="ADDRESSID" value="${dto.addressId}"/>
<c:set var="CONTACTPERSONID" value="${dto.contactPersonId}"/>

<app:url var="urlCancel" value="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${dto.mailGroupAddrId}" />

<html:form action="${action}?dto(mailGroupAddrId)=${dto.mailGroupAddrId}">
    <html:hidden property="dto(mailGroupAddrId)"/>

   <table id="AddressGroup.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
        <TR>
                <TD colspan="2" class="title">
                    <c:out value="${title}"/>
                </TD>
        </TR>
        <TR>
                <TD class="label" width="15%">
                    <fmt:message key="Webmail.mailGroupAddr.name"/>
                </TD>
                <TD class="contain" width="35%">
                    <app:text property="dto(name)" styleClass="middleText" maxlength="80" tabindex="1" view="${true}"/>
                </TD>
        </TR>
        <TR>
                <TD class="label" width="15%">
                    <fmt:message key="Webmail.addressGroup.name"/>
                </TD>
                <TD class="contain" width="35%">
                    <app:text property="dto(addressGroupName)" styleClass="middleText" maxlength="80" tabindex="1" view="${true}"/>
                </TD>
        </TR>
        <TR>
                <TD class="label" width="15%">
                    <fmt:message   key="Webmail.addressGroup.email"/>
                </TD>
                <TD class="contain" width="35%">
                    <c:choose>
                        <c:when test="${CONTACTPERSONID>0}">
                             <app2:relationData table1="telecomtype" table2="telecom" column="telecomnumber" column2="telecomid" id="address_mails" table1FkName="telecomtypeid" table2PkName="telecomtypeid" table1SearchFieldName="type" table1SearchFieldValue="'${TELECOMTYPE_EMAIL}'" table2SearchFieldName="contactPersonId" table2SearchFieldValue="${CONTACTPERSONID}"/>
                             <html:select property="dto(addressGroupEmailId)" styleClass="select" readonly="${op=='delete'}">
                                  <html:option value="" styleClass="list"><fmt:message key="Webmail.addressGroup.allMails"/></html:option>
                                  <c:forEach var="emails" items="${address_mails}">
                                    <html:option value="${emails.telecomid}" styleClass="list"><c:out value="${emails.telecomnumber}"/></html:option>
                                  </c:forEach>
                            </html:select>
                         </c:when>
                        <c:when test="${ADDRESSID>0}">
                        <app2:relationData table1="telecomtype" table2="telecom" column="telecomnumber" column2="telecomid" id="address_mails" table1FkName="telecomtypeid" table2PkName="telecomtypeid" table1SearchFieldName="type" table1SearchFieldValue="'${TELECOMTYPE_EMAIL}'" table2SearchFieldName="addressid" table2SearchFieldValue="${ADDRESSID}" isNull="contactpersonid"/>
                            <html:select property="dto(addressGroupEmailId)" styleClass="select" readonly="${op=='delete'}" >
                                  <html:option value="" styleClass="list"><fmt:message key="Webmail.addressGroup.allMails"/></html:option>
                                  <c:forEach var="emails" items="${address_mails}">
                                    <html:option value="${emails.telecomid}" styleClass="list"><c:out value="${emails.telecomnumber}"/></html:option>
                                  </c:forEach>
                            </html:select>
                         </c:when>
                    </c:choose>
                </TD>
        </TR>
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(addressId)"/>
        <html:hidden property="dto(contactPersonId)"/>
        <c:if test="${op=='update' || op=='delete'}">
            <html:hidden property="dto(addressGroupId)"/>
        </c:if>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">
         <TR>
             <TD class="button">
                  <table border=0>
                        <TR valign="top">
                             <TD>
                                <c:if test="${op == 'create' || op == 'update'}">
                                    <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(save)" styleClass="button" styleId="saveButtonId"><c:out value="${button}"/></app2:securitySubmit>
                                </c:if>
                                <c:if test="${op == 'delete'}">
                                    <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(delete)" styleClass="button"><fmt:message   key="Common.delete"/></app2:securitySubmit>
                                </c:if>
                             </TD>
                             <TD>
                                <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" >
                                     <fmt:message   key="Common.cancel"/>
                                 </html:button>
                            </TD>
                        </TR>
                   </table>
            </TD>
       </TR>
    </table>
</html:form>