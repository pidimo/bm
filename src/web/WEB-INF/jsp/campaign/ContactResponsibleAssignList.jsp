<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>
<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(ofCustomer)" value="value"/>
    <app2:jScriptUrlParam param="parameter(allContacts)" value="isAllContacts"/>
</app2:jScriptUrl>
<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<script language="JavaScript" type="text/javascript">
    <!--
    function check() {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = true;
                    }
                } else {
                    if (!field.disabled) field.checked = true;
                }
            }
        } else {
            if (field != undefined) {
                if (field.length != undefined) {
                    for (i = 0; i < field.length; i++) {
                        if (!field[i].disabled)
                            field[i].checked = false;
                    }
                } else {
                    if (!field.disabled) field.checked = false;
                }
            }
        }
    }

    function enableCheckbox() {
        field = document.getElementById('listc').selected;
        var i;
        if (field != undefined) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++) {
                    if (field[i].disabled) field[i].disabled = false;
                }
            } else {
                if (field.disabled) field.disabled = false;
            }
        }
    }

    function jump(obj) {
        var value = "";
        if (document.getElementById('ofCustomer').checked) {
            value = "on";
        }

        var isAllContacts = "";
        if (document.getElementById('allContacts').checked) {
            isAllContacts = "on";
        }
        window.location =${jsAlphabetUrl};
    }

    var isListSubmit = "false";
    function goSubmit() {
        if (isListSubmit == "false") {
            setSubmit(true);
            enableCheckbox();
            document.forms[1].submit();
        }
        isListSubmit = "false";
    }

    function goClose() {
        parent.hideBootstrapPopup();
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = ('true' == ss);
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        isListSubmit = "true";
    }

    //-->
</script>

<%--
#########EE${param.employeeId}
#########UU${param.userId}
--%>

<html:form action="/Activity/User/Assign/List.do?campaignId=${param.campaignId}"
           styleClass="form-horizontal"
           focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)">
    <div class="${app2:getFormPanelClasses()}">
        <legend class="title">
            <c:out value="${title}"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Campaign.activity.user"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                    <c:out value="${param.userName}"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClassesTwoColumns()}">
                    <fmt:message key="Common.search"/>
                </label>
                <div class="${app2:getFormContainClassesTwoColumns(null)}">
                    <html:text
                            property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                            styleClass="largeText ${app2:getFormInputClasses()}"/>
                </div>
            </div>
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <div class="col-xs-12 col-sm-offset-4 col-md-offset-0 col-md-4">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="parameter(allContacts)" styleId="allContacts"
                                           styleClass="radio"/>
                            <label for="allContacts">
                                <fmt:message key="Activity.user.manualAssign.allContact"/>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-offset-4 col-md-offset-0 col-md-8">
                    <div class="radiocheck">
                        <div class="checkbox checkbox-default">
                            <html:checkbox property="parameter(ofCustomer)" styleId="ofCustomer"
                                           styleClass=""/>
                            <label for="ofCustomer">
                                <fmt:message key="Campaign.activity.user.customerResponsibleOf"/>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.go"/></html:submit>
            </div>
        </div>
    </div>
    <!-- choose alphbet to simple and advanced search -->
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet
                mode="bootstrap"
                action="/Activity/User/Assign/List.do?campaignId=${param.campaignId}&employeeId=${param.employeeId}&userId=${param.userId}&userName=${param.userName}"
                parameterName="contactName1" onClick="jump(this);return false;"/>
    </div>


    <%
        if (request.getAttribute("AssignCampaignContactList") != null) {
            ResultList resultList = (ResultList) request.getAttribute("AssignCampaignContactList");
            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
        }
    %>
</html:form>

<html:form action="/Activity/CampContact/User/Assign.do?campaignId=${param.campaignId}" styleId="listc"
           onsubmit="return testSubmit();" >
    <div class="row">
        <div class="col-xs-12">
            <html:hidden property="dto(op)" value="update"/>
            <input type="hidden" name="isSubmit" value="false" id="isSubmit">
            <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
            <html:hidden property="dto(userId)" value="${param.userId}"/>
            <html:hidden property="dto(activityId)" value="${param.activityId}"/>

            <c:if test="${size >0}">
                <html:button property="save" styleClass="button ${app2:getFormButtonClasses()} marginButton" onclick="goSubmit()"><fmt:message
                        key="Campaign.activity.user.assign"/></html:button>
            </c:if>

            <app:url var="urlUserList" value="/Activity/User/List.do?activityId=${param.activityId}"/>
            <html:button property="cancel" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" onclick="location.href='${urlUserList}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
    <div class="table-responsive">
        <fanta:table list="AssignCampaignContactList" align="left"
                     mode="bootstrap"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="campaignContact"
                     action="Activity/User/Assign/List.do?campaignId=${param.campaignId}&employeeId=${param.employeeId}&userId=${param.userId}&userName=${param.userName}"
                     imgPath="${baselayout}" withCheckBox="true">

            <c:if test="${size >0}">

                <c:if test="${not empty campaignContact.campaignContactId}">
                    <!--this is necessary hidden with styleId to send as array in form-->
                    <html:hidden property="listViewContactIds" styleId="viewId"
                                 value="${campaignContact.campaignContactId}"/>
                </c:if>

                <c:choose>
                    <c:when test="${empty campaignContact.userId or (param.userId eq campaignContact.userId)}">
                        <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                              property="campaignContactId" headerStyle="listHeader"
                                              styleClass="listItemCenter" width="5%"
                                              checked="${param.userId eq campaignContact.userId}"
                                              disabled="${app2:activityUserHasTaskCreated(campaignContact.campaignContactId,param.campaignId)}"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn name="" label="" styleClass="listItem" headerStyle="listHeader"
                                            width="5%">
                            &nbsp;
                        </fanta:actionColumn>
                    </c:otherwise>
                </c:choose>
            </c:if>
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
            <fanta:dataColumn name="contactNames" useJScript="true"
                              action="javascript:viewContactDetailInfo(${campaignContact.addressType},${campaignContact.addressId});"
                              styleClass="listItem" title="Campaign.company" headerStyle="listHeader"
                              width="23%"
                              orderable="true" maxLength="45"/>
            <fanta:dataColumn name="contactPersonName" useJScript="true"
                              action="javascript:viewContactDetailInfo(1,${campaignContact.contactPersonId});"
                              styleClass="listItem" title="Campaign.contactPerson" headerStyle="listHeader"
                              width="23%"
                              orderable="true" maxLength="30">
            </fanta:dataColumn>
            <fanta:dataColumn name="customerResponsibleName"
                              styleClass="listItem" title="Activity.user.manualAssign.customerResponsible"
                              headerStyle="listHeader" width="22%"
                              orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="userRelatedName"
                              styleClass="listItem2" title="Activity.user.manualAssign.relatedTo"
                              headerStyle="listHeader" width="22%"
                              orderable="true">
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <c:if test="${size >0}">
                <html:button property="save" styleClass="button ${app2:getFormButtonClasses()}" onclick="goSubmit()"><fmt:message
                        key="Campaign.activity.user.assign"/></html:button>
            </c:if>

            <html:button property="cancel" styleClass="button ${app2:getFormButtonCancelClasses()}" onclick="location.href='${urlUserList}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>