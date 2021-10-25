<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>
<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<script language="JavaScript">
    <!--
    function check() {
        field = document.getElementById('listc').selected;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            } else
                field.checked = true;
        }
        else {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            } else
                field.checked = false;
        }
    }

    function goSubmit() {
        document.forms[1].submit();
    }

    //this is called in onLoad body property
    function goToParent() {
        parent.selectedSubmit();
        parent.hideBootstrapPopup();
    }

    function goClose() {
        parent.hideBootstrapPopup();
    }

    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = 'true' == ss;
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        var ss = document.getElementById('isSubmit').value;
    }

    //-->
</script>


<html:form action="/Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
           focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
           styleClass="form-horizontal">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text
                        property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                        styleClass="largeText ${app2:getFormInputClasses()}"/>
                <div class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                            key="Common.go"/></html:submit>
                </div>
            </div>
        </div>
    </div>

    <!-- choose alphbet to simple and advanced search -->
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="/Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
                        mode="bootstrap" parameterName="contactName1"/>
    </div>

    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <%
        if (request.getAttribute("campaignContactPopupList") != null) {
            ResultList resultList = (ResultList) request.getAttribute("campaignContactPopupList");
            pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
        }
    %>
</html:form>


<html:form action="/Campaign/Activity/CampContact/CreateFromRecipients.do?campaignId=${param.campaignId}"
           styleId="listc" onsubmit="return false" styleClass="">

    <div class="row">
        <div class="col-xs-12 ">
            <c:if test="${size >0}">
                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             onclick="goSubmit()"><fmt:message
                        key="Common.add"/></html:button>
            </c:if>

            <app:url var="urlAddAll"
                     value="/Campaign/Recipients/Forward/CopyAll.do?activityId=${param.activityId}&campaignId=${param.campaignId}"/>
            <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                         onclick="location.href='${urlAddAll}'">
                <fmt:message key="Campaign.activity.campaignRecipients.addAll"/>
            </html:button>
            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" onclick="goClose()">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>


    <div class="table-responsive">
        <fanta:table list="campaignContactPopupList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     mode="bootstrap"
                     align="left"
                     width="100%"
                     id="campaignContact"
                     action="Campaign/Recipients/List/Popup.do?campaignId=${param.campaignId}"
                     imgPath="${baselayout}" withCheckBox="true">

            <html:hidden property="dto(recipientsSize)" value="${size}"/>
            <input type="hidden" name="isSubmit" value="false" id="isSubmit">
            <html:hidden property="dto(op)" value="createRecipients"/>
            <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
            <html:hidden property="dto(activityId)" value="${param.activityId}"/>
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>


            <c:if test="${size >0}">
                <fanta:checkBoxColumn name="guia" id="selected" onClick="javascript:check();"
                                      property="campaignContactId" headerStyle="listHeader"
                                      styleClass="listItemCenter" width="5%"/>
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
                              width="33%"
                              orderable="true" maxLength="45"/>
            <fanta:dataColumn name="contactPersonName" useJScript="true"
                              action="javascript:viewContactDetailInfo(1,${campaignContact.contactPersonId});"
                              styleClass="listItem" title="Campaign.contactPerson" headerStyle="listHeader"
                              width="33%"
                              orderable="true" maxLength="30">

            </fanta:dataColumn>
            <fanta:dataColumn name="function" styleClass="listItem2" title="Campaign.function"
                              headerStyle="listHeader"
                              width="26%" orderable="true">

            </fanta:dataColumn>
        </fanta:table>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <c:if test="${size >0}">
                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                             onclick="goSubmit()"><fmt:message
                        key="Common.add"/></html:button>
            </c:if>

            <html:button property="" styleClass="button ${app2:getFormButtonClasses()} marginButton"
                         onclick="location.href='${urlAddAll}'">
                <fmt:message key="Campaign.activity.campaignRecipients.addAll"/>
            </html:button>

            <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton" onclick="goClose()">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>

