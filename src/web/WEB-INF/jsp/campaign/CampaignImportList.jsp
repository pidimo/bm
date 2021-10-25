<%@ include file="/Includes.jsp" %>
<c:import url="/WEB-INF/jsp/contacts/ContactModalFragment.jsp"/>

<script>
    function select(id, name) {
        parent.selectField('fieldCampaignCopyId_id', id, 'fieldCampaignName_id', name);
    }
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Campaign/Forward/Copy.do" focus="parameter(campaignName)" styleClass="form-horizontal">
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group col-xs-12">
                    <html:text property="parameter(campaignName)" styleClass="${app2:getFormInputClasses()} largeText"/>
                    <span class="input-group-btn">
                        <html:submit styleClass="${app2:getFormButtonClasses()}">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                    </span>
                </div>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Campaign/Forward/Copy.do" parameterName="campaignNameAlpha" mode="bootstrap"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                     list="campaignImportList"
                     width="100%" id="campaign" action="Campaign/Forward/Copy.do"
                     imgPath="${baselayout}">

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
                    <fanta:actionColumn name="import"
                                        title="Common.import"
                                        useJScript="true"
                                        action="javascript:select('${campaign.campaignId}', '${app2:jscriptEncode(campaign.campaignName)}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphImport()}">
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="campaignName" styleClass="listItem" title="Campaign.mailing"
                              headerStyle="listHeader" width="30%" orderable="true" maxLength="25">
                &nbsp;
            </fanta:dataColumn>
            <c:set var="viewLink" value="javascript:viewContactDetailInfo(1,${campaign.employeeId});"/>
            <fanta:dataColumn name="employeeName" action="${viewLink}" styleClass="listItem"
                              title="Campaign.responsibleEmployee" headerStyle="listHeader" width="30%"
                              maxLength="25" orderable="true" useJScript="true">
                &nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="Campaign.dateCreation"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.startDate)}"
                                pattern="${datePattern}"/>
                ${dateValue1}&nbsp;
            </fanta:dataColumn>

            <fanta:dataColumn name="recordDate" styleClass="listItem" title="Campaign.sendDate"
                              headerStyle="listHeader" width="15%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.recordDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}&nbsp;
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>