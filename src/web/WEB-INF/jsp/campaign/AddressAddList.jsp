<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>
<script language="JavaScript">
    <!--
    function check() {
        field = document.getElementById('listc').excludes;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = true;
            } else
                field.checked = true;
        } else {
            if (field.length != undefined) {
                for (i = 0; i < field.length; i++)
                    field[i].checked = false;
            } else
                field.checked = false;
        }
    }

    function goSubmit() {
        document.getElementById('Import_value').value = true;
        document.forms[1].submit();
    }

    //this is requireq of fanta table paginator
    function setSubmit(issubmit) {
        return issubmit;
    }

    //-->
</script>

<%
    if (request.getAttribute("searchAllAddressList") != null) {
        ResultList resultList = (ResultList) request.getAttribute("searchAllAddressList");
        pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
    }
%>

<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>

<legend class="title">
    <fmt:message key="Campaign.addCriterias"/>
</legend>

<div class="${app2:getFormGroupClasses()}">
    <html:form action="/Recipients/Add.do?campaignId=${param.campaignId}"
               styleClass="form-horizontal"
               focus="parameter(contactSearchName)">
        <fieldset>
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="contactSearchName_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(contactSearchName)"
                               styleId="contactSearchName_id"
                               styleClass="${app2:getFormInputClasses()}"/>
                        <span class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </span>
                </div>
            </div>
        </fieldset>
    </html:form>
</div>


<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Recipients/Add.do?campaignId=${param.campaignId}"
                    mode="bootstrap"
                    parameterName="name1A1"/>
</div>


<html:form action="/Recipients/Import.do" styleId="listc" onsubmit="return false;">

    <div class="${app2:getFormButtonWrapperClasses()}"><!--Button create up -->
        <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="goSubmit()">
            <fmt:message key="Contact.Organization.Employee.addEmployee"/>
        </html:button>
        <c:url var="url" value="/campaign/Recipients/List.do?index=${param.index}">
            <c:param name="campaignId" value="${param.campaignId}"/>
        </c:url>
        <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}" onclick="location.href='${url}'">
            <fmt:message key="Common.cancel"/>
        </html:button>

        <html:hidden property="Import_value" value="false" styleId="Import_value"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
    </div>

        <div class="table-responsive">
            <fanta:table list="searchAllAddressList" width="100%" id="contact"
                         styleClass="${app2:getFantabulousTableClases()}"
                         mode="bootstrap"
                         action="Recipients/Add.do?campaignId=${param.campaignId}" imgPath="${baselayout}"
                         withCheckBox="true">
                <c:if test="${size >0}">
                    <!--check box value is sent as "addressId,contactPersonId"-->
                    <fanta:checkBoxColumn name="guia" id="excludes" onClick="javascript:check();"
                                          property="organizationId" headerStyle="listHeader"
                                          styleClass="listItemCenter" width="5%">
                        <c:choose>
                            <c:when test="${empty contact.contactPersonAddressId}">
                                ${contact.addressId},
                            </c:when>
                            <c:otherwise>
                                ${contact.contactPersonAddressId},${contact.addressId}
                            </c:otherwise>
                        </c:choose>
                    </fanta:checkBoxColumn>
                </c:if>
                <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="3%"
                                  renderData="false">

                    <c:choose>
                        <c:when test="${not empty contact.contactPersonAddressId}">
                            <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                        </c:otherwise>
                    </c:choose>


                    <span class="${contact.addressType == personType? personPrefixImageName : app2:getClassGlyphOrganization()}"></span>

                </fanta:dataColumn>

                <c:set var="viewLink"
                       value="javascript:viewContactDetailInfo(${contact.addressType},${contact.addressId});"/>
                <fanta:dataColumn name="addressName1"
                                  action="${viewLink}"
                                  styleClass="listItem"
                                  title="Contact.name"
                                  headerStyle="listHeader"
                                  width="47%"
                                  orderable="true"
                                  useJScript="true">
                </fanta:dataColumn>

                <c:set var="viewLink2"
                       value="javascript:viewContactDetailInfo(${contact.addressType2},${contact.contactPersonAddressId});"/>
                <fanta:dataColumn name="addressName2" action="${viewLink2}" styleClass="listItem2"
                                  title="ContactPerson.contactName" headerStyle="listHeader" width="45%"
                                  orderable="true" renderData="false" useJScript="true">
                    <c:if test="${not empty contact.addressName2}">
                        <fanta:textShorter title="${contact.addressName2}">
                            <c:out value="${contact.addressName2}"/>
                        </fanta:textShorter>
                    </c:if>
                </fanta:dataColumn>
            </fanta:table>
        </div>
</html:form>



