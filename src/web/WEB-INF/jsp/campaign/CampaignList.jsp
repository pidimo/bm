<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/List.do" focus="parameter(campaignName)" styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="Campaign.Title.search"/>
            </legend>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left" for="searchInput">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text styleId="searchInput" property="parameter(campaignName)"
                                   styleClass="${app2:getFormInputClasses()}"/>
                        <span class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </span>
                    </div>
                </div>
                <div class="pull-left">
                    <app:link styleClass="btn btn-link"
                              action="/AdvancedSearch.do?advancedListForward=CampaignAdvancedSearch">
                        <fmt:message key="Common.advancedSearch"/>
                    </app:link>
                </div>
            </div>
        </fieldset>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="List.do" parameterName="campaignNameAlpha" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormGroupClasses()}">
                <tags:buttonsTable>
                    <app:url
                            value="/Campaign/Forward/Create.do?dto(operation)=create&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                            addModuleParams="false" var="newCampaignUrl"/>
                    <input type="button" class="${app2:getFormButtonClasses()}" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newCampaignUrl}'">
                </tags:buttonsTable>
            </div>
        </c:set>
    </app2:checkAccessRight>
    <c:out value="${newButtonsTable}" escapeXml="false"/>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="campaignList" width="100%" id="campaign" action="List.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">
            <c:set var="editAction"
                   value="Campaign/Forward/Update.do?campaignId=${campaign.campaignId}&index=0&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(operation)=update"/>
            <c:set var="deleteAction"
                   value="Campaign/Forward/Delete.do?campaignId=${campaign.campaignId}&index=0&dto(withReferences)=true&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
                    <fanta:actionColumn name="update" label="Common.update" action="${editAction}"
                                        title="Common.update" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CAMPAIGN" permission="DELETE">
                    <fanta:actionColumn name="del" label="Common.delete" action="${deleteAction}"
                                        title="Common.delete" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="campaignName" action="${editAction}" styleClass="listItem"
                              title="Campaign.mailing" headerStyle="listHeader" width="25%" orderable="true">
                &nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="Campaign.responsibleEmployee"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="status" styleClass="listItem" title="Campaign.status" headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:if test="${campaign.status == '1'}">
                    <fmt:message key="Campaign.preparation"/>
                </c:if>
                <c:if test="${campaign.status == '2'}">
                    <fmt:message key="Campaign.sent"/>
                </c:if>
                <c:if test="${campaign.status == '3'}">
                    <fmt:message key="Campaign.cancel"/>
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="recordDate" styleClass="listItem" title="Campaign.dateCreation"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.recordDate)}"
                                pattern="${datePattern}"/>
                ${dateValue1}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="closeDate" styleClass="listItem2" title="Campaign.closeDate"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.closeDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}&nbsp;
            </fanta:dataColumn>


        </fanta:table>
    </div>
    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>
