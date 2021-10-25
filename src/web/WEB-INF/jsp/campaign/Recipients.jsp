<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<c:set var="TEMPLATE_HTML" value="<%=CampaignConstants.DocumentType.HTML.getConstantAsString()%>"/>
<c:set var="TEMPLATE_WORD" value="<%=CampaignConstants.DocumentType.WORD.getConstantAsString()%>"/>

<c:set var="personType" value="<%= ContactConstants.ADDRESSTYPE_PERSON %>"/>
<c:set var="organizationType" value="<%= ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>


<app2:jScriptUrl
        url="/campaign/Campaign/Light/Forward/Email/Send.do?campaignId=${param.campaignId}&dto(campaignId)=${param.campaignId}"
        var="jsEmailUrl">
    <app2:jScriptUrlParam param="dto(documentType)" value="docType.value"/>
</app2:jScriptUrl>

<app2:jScriptUrl
        url="/campaign/Campaign/Light/Forward/Generate/Document.do?campaignId=${param.campaignId}&dto(campaignId)=${param.campaignId}"
        var="jsDocumentUrl">
    <app2:jScriptUrlParam param="dto(documentType)" value="docType.value"/>
</app2:jScriptUrl>


<script language="JavaScript">
    <!--
    function check() {
        field = document.getElementById('listc').excludes;
        guia = document.getElementById('listc').guia;
        var i;

        if (guia.checked) {
            for (i = 0; i < field.length; i++)
                field[i].checked = true;
        }
        else {
            for (i = 0; i < field.length; i++)
                field[i].checked = false;
        }
    }

    function goSubmit() {
        document.getElementById('Delete').value = true;
        document.forms[1].submit();
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


    function getGenerateLocation() {
        var docType = document.getElementById("docTypeSelect");

        if (docType.value == "${TEMPLATE_HTML}") {
            location.href = ${jsEmailUrl};
        } else if (docType.value == "${TEMPLATE_WORD}") {
            location.href = ${jsDocumentUrl};
        }
    }

    //-->
</script>


            <html:form action="/Recipients/List.do?campaignId=${param.campaignId}" styleClass="form-horizontal"
                       focus="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)">
                <div class="${app2:getSearchWrapperClasses()}">
                    <fieldset>
                        <label class="${app2:getFormLabelOneSearchInput()} text-left" for="fieldInput_id">
                            <fmt:message key="Common.search"/>
                        </label>

                        <div class="${app2:getFormOneSearchInput()}">
                            <div class="input-group">
                                <html:text
                                        property="parameter(contactName1@_contactName2@_contactName3@_contactPersonName1@_contactPersonName2)"
                                        styleId="fieldInput_id"
                                        styleClass="largeText ${app2:getFormInputClasses()}"/>
                                <span class="input-group-btn">
                                    <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message key="Common.go"/></html:submit>
                                </span>
                            </div>
                        </div>
                    </fieldset>
                </div>
                <!-- choose alphbet to simple and advanced search  -->

                <div class="${app2:getAlphabetWrapperClasses()}">
                    <fanta:alphabet action="Recipients/List.do?campaignId=${param.campaignId}"
                                    mode="bootstrap"
                                    parameterName="contactName1"/>
                </div>

                <c:set var="path" value="${pageContext.request.contextPath}"/>
                <%
                    ResultList resultList = (ResultList) request.getAttribute("campaignContactList");
                    pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
                %>
            </html:form>


    <html:form action="/Campaign/Recipients/Delete.do?campaignId=${param.campaignId}" styleId="listc"
               onsubmit="return testSubmit()">

        <div class="row">
            <div class="col-xs-12">
                <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="DELETE">
                    <c:if test="${size >1}">
                        <html:button property="" styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                                     onclick="goSubmit()"><fmt:message
                                key="Common.delete"/></html:button>
                    </c:if>
                </app2:checkAccessRight>


                <c:if test="${size > 0}">
                    <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="EXECUTE">
                        <c:set var="templateDocumentList"
                               value="${app2:getCampaignTemplateDocumentType(pageContext.request)}"/>
                        <div class="form-group col-xs-12 col-sm-3">
                            <html:select property="dto(documentType)" styleId="docTypeSelect" styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                         value="">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="templateDocumentList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                        <app2:securitySubmit property="execute" onclick="JavaScript:getGenerateLocation();"
                                             operation="execute" functionality="CAMPAIGNACTIVITY"
                                             styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton">
                            <fmt:message key="Document.generate"/>
                        </app2:securitySubmit>
                    </app2:checkAccessRight>
                </c:if>


                <c:url var="url" value="/campaign/Recipients/Add.do?index=${param.index}">
                    <c:param name="campaignId" value="${param.campaignId}"/>
                    <c:param name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                </c:url>
                <html:button property="" styleClass="button ${app2:getFormButtonClasses()} pull-left marginRight marginLeft marginButton"
                             onclick="location.href='${url}'">
                    <fmt:message key="Campaign.add"/>
                </html:button>
            </div>
            </div>



        <div class="table-responsive">
                <fanta:table list="campaignContactList"
                             styleClass="${app2:getFantabulousTableClases()}"
                             mode="bootstrap"
                             align="left"
                             width="100%"
                             id="campaignContact"
                             action="Recipients/List.do?campaignId=${param.campaignId}"
                             imgPath="${baselayout}"
                             withCheckBox="true">

                    <html:hidden property="dto(recipientsSize)" value="${size}"/>
                    <input type="hidden" name="isSubmit" value="false" id="isSubmit">
                    <html:hidden property="dto(op)" value="exclude"/>
                    <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="Delete" value="false" styleId="Delete"/>

                    <c:if test="${size > 1}">
                        <fanta:checkBoxColumn name="guia" id="excludes" onClick="javascript:check();"
                                              property="campaignContactId" headerStyle="listHeader"
                                              styleClass="listItemCenter" width="5%"/>
                    </c:if>

                    <c:if test="${size == 1}">
                        <fanta:actionColumn name="del"
                                            action="Campaign/Recipients/Delete.do?campaignId=${param.campaignId}&dto(campaignContactId)=${campaignContact.campaignContactId}&dto(campaignId)=${campaignContact.campaignId}&dto(op)=exclude"
                                            title="Common.delete" styleClass="listItem" headerStyle="listHeader"
                                            width="4%"
                                            glyphiconClass="${app2:getClassGlyphTrash()}"/>
                    </c:if>

                    <c:if test="${personType == campaignContact.addressType}">
                        <c:set var="editAddressUrl"
                               value="contacts/Person/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(addressType)=${campaignContact.addressType}&dto(name1)=${app2:encode(campaignContact.contactNames)}&index=0"/>
                    </c:if>

                    <c:if test="${organizationType == campaignContact.addressType}">
                        <c:set var="editAddressUrl"
                               value="contacts/Organization/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(addressType)=${campaignContact.addressType}&dto(name1)=${app2:encode(campaignContact.contactNames)}&index=0"/>
                    </c:if>

                    <c:if test="${null != campaignContact.contactPersonId}">
                        <c:set var="editContactPersonUrl"
                               value="contacts/ContactPerson/Forward/Update.do?contactId=${campaignContact.addressId}&dto(addressId)=${campaignContact.addressId}&dto(contactPersonId)=${campaignContact.contactPersonId}&tabKey=Contacts.Tab.contactPersons"/>
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
                    <fanta:dataColumn name="contactNames"
                                      styleClass="listItem"
                                      title="Campaign.company"
                                      headerStyle="listHeader"
                                      width="33%"
                                      orderable="true"
                                      maxLength="45"
                                      renderData="false">
                        <app:link action="${editAddressUrl}" addModuleName="false" contextRelative="true">
                            <c:out value="${campaignContact.contactNames}"/>
                        </app:link>&nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="contactPersonName"
                                      styleClass="listItem"
                                      title="Campaign.contactPerson"
                                      headerStyle="listHeader"
                                      width="33%"
                                      orderable="true"
                                      maxLength="30"
                                      renderData="false">
                        <app:link action="${editContactPersonUrl}" addModuleName="false" contextRelative="true">
                            <c:out value="${campaignContact.contactPersonName}"/>
                        </app:link> &nbsp;
                    </fanta:dataColumn>
                    <fanta:dataColumn name="function" styleClass="listItem2" title="Campaign.function"
                                      headerStyle="listHeader"
                                      width="26%" orderable="true">
                        &nbsp;
                    </fanta:dataColumn>
                </fanta:table>
            </div>

    </html:form>
