<%@ include file="/Includes.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <html:form action="/Mail/Forward/CreateContactGroup.do">
            <td class="button">
                <app2:securitySubmit operation="create" functionality="WEBMAILGROUP" styleClass="button"><fmt:message
                        key="Common.new"/></app2:securitySubmit>
            </td>
        </html:form>
    </tr>
</table>
<TABLE id="ContactGroupList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container"
       align="center">
    <tr>
        <td>
            <fanta:table list="contactGroupList" width="100%" id="contactGroup" action="Mail/ContactGroupList.do"
                         imgPath="${baselayout}" align="center">

                <app:url value="/Mail/Forward/UpdateContactGroup.do?dto(mailGroupAddrId)=${contactGroup.GROUPADDRID}"
                         var="urlUpdate" enableEncodeURL="false"/>
                <app:url value="/Mail/Forward/DeleteContactGroup.do?dto(mailGroupAddrId)=${contactGroup.GROUPADDRID}"
                         var="urlDelete" enableEncodeURL="false"/>
                <app:url value="/Mail/Forward/AddAddressGroup.do" var="urlUpdateContacts" enableEncodeURL="false"/>
                <app:url value="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${contactGroup.GROUPADDRID}"
                         var="urlViewContacts" enableEncodeURL="false"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

                    <fanta:actionColumn name="viewContacts"
                                        title="Webmail.addressGroup.edit"
                                        action="${urlViewContacts}"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        image="${baselayout}/img/webmail/contactgroup_edit.gif"/>

                    <app2:checkAccessRight functionality="WEBMAILGROUP" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>

                    <app2:checkAccessRight functionality="WEBMAILGROUP" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                </fanta:columnGroup>
                <fanta:dataColumn name="CONTACTGROUPNAME" styleClass="listItem2" action="${urlUpdate}"
                                  title="Webmail.contactGroup.name" headerStyle="listHeader" width="95%"
                                  orderable="true" maxLength="50"/>
            </fanta:table>
        </td>
    </tr>
</table>