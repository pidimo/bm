<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>


<table cellpadding="0" cellspacing="0" border="0" id="tableId" width="100%">
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                <tr>
                    <td class="title">
                        <fmt:message key="Campaign.activity.contacts"/>
                    </td>
                </tr>
            </table>

            <app:url value="/Campaign/Activity/CampContact/List.do?campaignId=${param.campaignId}" var="urlList"
                     enableEncodeURL="false"/>
            <fanta:table list="ActivityCampaignContactList" align="left" width="100%" id="campaignContact"
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
                                            image="${baselayout}/img/edit.gif" name="del" label="Common.update">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="DELETE">
                        <fanta:actionColumn useJScript="true"
                                            action="javascript:goParentURL('${urlDelete}')"
                                            title="Common.delete"
                                            styleClass="listItem" headerStyle="listHeader" width="25%"
                                            image="${baselayout}/img/delete.gif" name="del" label="Common.delete">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="SALESPROCESS" permission="CREATE">
                        <c:choose>
                            <c:when test="${not empty campaignContact.campaignId && app2:campaignContactHasSalesProcess(campaignContact.addressId,campaignContact.activityId,pageContext.request)}">
                                <fanta:actionColumn name="view" label="" styleClass="listItem" headerStyle="listHeader"
                                                    width="25%">
                                    <html:img src="${baselayout}/img/sales_process.gif" border="0"
                                              titleKey="Campaign.activity.campContact.salesProcessCreated"/>
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
                                                    image="${baselayout}/img/sales_process2.gif" name="del"
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
                                            image="${baselayout}/img/phone2.gif" name="del"
                                            label="Common.communication">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
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
                            <img align="middle" src="${sessionScope.baselayout}/img/check.gif" alt=""/>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>
                <fanta:dataColumn name="" styleClass="listItem2" title="Campaign.activity.campContact.responsibleStatus"
                                  headerStyle="listHeader" width="10%"
                                  renderData="false">
                    <c:set var="titleColorMap"
                           value="${app2:getResponsibleStatus(campaignContact.userId, campaignContact.taskId, campaignContact.taskStatus, pageContext.request)}"/>
                    <table border="0" onmouseover="return escape('${titleColorMap.title}')"
                           bgcolor="${titleColorMap.color}"
                           width="60%" align="center">
                        <tr>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>

</table>
<%--wz_tooltip.js always the end--%>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>