<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<legend class="title">
    <fmt:message key="Campaign.activity.contacts"/>
</legend>

<app:url value="/Campaign/Activity/CampContact/List.do?campaignId=${param.campaignId}" var="urlList"
         enableEncodeURL="false"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="ActivityCampaignContactList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 align="left" width="100%" id="campaignContact"
                 action="${urlList}" imgPath="${baselayout}" withContext="false">
        <app:url
                value="/Campaign/Activity/CampContact/Forward/Update.do?dto(activityId)=${campaignContact.activityId}&dto(campaignContactId)=${campaignContact.campaignContactId}&dto(campaignId)=${campaignContact.campaignId}&dto(contactName)=${app2:encode(campaignContact.contactNames)}&dto(op)=read"
                var="urlUpdate"/>
        <app:url
                value="/Campaign/Activity/CampContact/Forward/Delete.do?dto(activityId)=${campaignContact.activityId}&dto(campaignContactId)=${campaignContact.campaignContactId}&dto(campaignId)=${campaignContact.campaignId}&dto(contactName)=${app2:encode(campaignContact.contactNames)}&addressId=${campaignContact.addressId}&dto(op)=read"
                var="urlDelete"/>
        <app:url
                value="/SalesProcess/Forward/Create.do?dto(activityId)=${campaignContact.activityId}&dto(campaignContactId)=${campaignContact.campaignContactId}&dto(campaignId)=${campaignContact.campaignId}&dto(addressId)=${campaignContact.addressId}&dto(addressName)=${app2:encode(campaignContact.contactNames)}&dto(employeeId)=${campaignContact.userAddressId}"
                var="urlSalessProcess"/>

        <app:url
                value="/Communication/Forward/CreateFromActivity.do?dto(activityId)=${campaignContact.activityId}&dto(contactPersonId)=${campaignContact.contactPersonId}&dto(op)=create&dto(contact)=${app2:encode(campaignContact.contactNames)}&dto(campaignContactId)=${campaignContact.campaignContactId}&dto(campaignId)=${campaignContact.campaignId}&dto(addressId)=${campaignContact.addressId}&dto(addressName)=${app2:encode(campaignContact.contactNames)}&dto(employeeId)=${campaignContact.userAddressId}&tabKey=Contacts.Tab.communications"
                var="urlCommunication"/>

        <c:if test="${personType == campaignContact.addressType}">
            <app:url
                    value="contacts/Person/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(addressType)=${campaignContact.addressType}&dto(name1)=${app2:encode(campaignContact.contactNames)}&index=0"
                    var="addressEditUrl" addModuleName="false" contextRelative="true"/>
        </c:if>

        <c:if test="${organizationType == campaignContact.addressType}">
            <app:url
                    value="contacts/Organization/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(addressType)=${campaignContact.addressType}&dto(name1)=${app2:encode(campaignContact.contactNames)}&index=0"
                    var="addressEditUrl" addModuleName="false" contextRelative="true"/>
        </c:if>

        <c:if test="${null != campaignContact.contactPersonId}">
            <app:url
                    value="contacts/ContactPerson/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(contactPersonId)=${campaignContact.contactPersonId}&tabKey=Contacts.Tab.contactPersons"
                    var="contactPersonEditUrl" addModuleName="false" contextRelative="true"/>
        </c:if>

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="VIEW">
                <fanta:actionColumn useJScript="true"
                                    action="javascript:goParentURL('${urlUpdate}')"
                                    title="Common.update"
                                    styleClass="listItem" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphEdit()}" name="del" label="Common.update">
                </fanta:actionColumn>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="DELETE">
                <fanta:actionColumn useJScript="true"
                                    action="javascript:goParentURL('${urlDelete}')"
                                    title="Common.delete"
                                    styleClass="listItem" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphTrash()}" name="del" label="Common.delete">
                </fanta:actionColumn>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                <c:choose>
                    <c:when test="${not empty campaignContact.campaignId && app2:campaignContactHasSalesProcess(campaignContact.addressId,campaignContact.activityId,pageContext.request)}">
                        <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader"
                                            width="25%">
                            <span class="${app2:getClassGlyphSalesProcess()}"></span>
                        </fanta:actionColumn>
                    </c:when>
                    <c:when test="${campaignContact.active == 0}">
                        <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader"
                                            width="25%">
                            &nbsp;
                        </fanta:actionColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn useJScript="true"
                                            action="javascript:goParentURL('${urlSalessProcess}')"
                                            title="Campaign.activity.campContact.createSalesProcess"
                                            styleClass="listItem" headerStyle="listHeader" width="25%"
                                            glyphiconClass="${app2:getClassGlyphSalesProcessTwo()}" name="del"
                                            label="Campaign.activity.campContact.createSalesProcess">
                        </fanta:actionColumn>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="VIEW">
                <fanta:actionColumn useJScript="true"
                                    action="javascript:goParentURL('${urlCommunication}')"
                                    title="Campaign.activity.templateGenerate.createcommunications"
                                    styleClass="listItem" headerStyle="listHeader" width="25%"
                                    glyphiconClass="${app2:getClassGlyphPhoneTwo()}" name="del"
                                    label="Common.communication">
                </fanta:actionColumn>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader" width="4%">

            <c:choose>
                <c:when test="${not empty campaignContact.contactPersonName}">
                    <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                </c:otherwise>
            </c:choose>

            <c:if test="${personType == campaignContact.addressType}">
                <span class="${personPrefixImageName}"></span>
            </c:if>

            <c:if test="${organizationType == campaignContact.addressType}">
                <c:choose>
                    <c:when test="${not empty campaignContact.contactPersonName}">
                        <span class="${app2:getClassGlyphPerson()}"></span>
                    </c:when>
                    <c:otherwise>
                        <span class="${app2:getClassGlyphOrganization()}"></span>
                    </c:otherwise>
                </c:choose>
            </c:if>

        </fanta:actionColumn>
        <fanta:dataColumn name="contactNames"
                          useJScript="true"
                          action="javascript:goParentURL('${addressEditUrl}')"
                          styleClass="listItem"
                          title="Campaign.company"
                          headerStyle="listHeader"
                          width="25%"
                          orderable="true"
                          maxLength="45"/>
        <c:choose>
            <c:when test="${null != campaignContact.contactPersonId}">
                <fanta:dataColumn name="contactPersonName"
                                  useJScript="true"
                                  action="javascript:goParentURL('${contactPersonEditUrl}')"
                                  styleClass="listItem"
                                  title="Campaign.contactPerson"
                                  headerStyle="listHeader"
                                  width="25%"
                                  orderable="true"
                                  maxLength="30"/>
            </c:when>
            <c:otherwise>
                <fanta:dataColumn name="contactPersonName"
                                  styleClass="listItem"
                                  title="Campaign.contactPerson"
                                  headerStyle="listHeader"
                                  width="25%"
                                  orderable="true"
                                  maxLength="30"/>
            </c:otherwise>
        </c:choose>

        <fanta:dataColumn name="userName"
                          styleClass="listItem"
                          title="Campaign.activity.campContact.responsible"
                          headerStyle="listHeader"
                          width="25%"
                          orderable="true" maxLength="30"/>
        <fanta:dataColumn name="active"
                          styleClass="listItem"
                          title="Common.active"
                          headerStyle="listHeader"
                          width="5%"
                          orderable="true"
                          renderData="false"
                          style="text-align:center">
            <c:choose>
                <c:when test="${campaignContact.active == 1}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:when>
                <c:otherwise>
                    &nbsp;
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <c:set var="titleColorMap"
               value="${app2:getResponsibleStatus(campaignContact.userId, campaignContact.taskId, campaignContact.taskStatus, pageContext.request)}"/>
        <fanta:dataColumn name="" styleClass="listItem2" title="Campaign.activity.campContact.responsibleStatus"
                          headerStyle="listHeader" width="10%"
                          renderData="false">
            <span class="label label-default" data-toggle="tooltip"
                  data-placement="auto"
                  title="${titleColorMap.title}"
                  style="background-color: ${titleColorMap.color};">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </span>
        </fanta:dataColumn>
    </fanta:table>
</div>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>
<%--wz_tooltip.js always the end--%>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>