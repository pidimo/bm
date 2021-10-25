<%@ include file="/Includes.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Mail/Forward/CreateContactGroup.do">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create" functionality="WEBMAILGROUP"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.new"/></app2:securitySubmit>
        </div>
    </html:form>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="contactGroupList" width="100%"
                     styleClass="${app2:getFantabulousTableClases()}"
                     id="contactGroup" action="Mail/ContactGroupList.do"
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
                                    glyphiconClass="${app2:getClassGlyphContactGroupEdit()}"/>

                <app2:checkAccessRight functionality="WEBMAILGROUP" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="WEBMAILGROUP" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>

            </fanta:columnGroup>
            <fanta:dataColumn name="CONTACTGROUPNAME" styleClass="listItem2" action="${urlUpdate}"
                              title="Webmail.contactGroup.name" headerStyle="listHeader" width="95%"
                              orderable="true" maxLength="50"/>
        </fanta:table>
    </div>
</div>