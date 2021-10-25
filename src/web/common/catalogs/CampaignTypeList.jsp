<%@ include file="/Includes.jsp" %>


<table width="97%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="CREATE">
                <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <TR>

                        <html:form action="/Campaign/CampaignType/Forward/Create.do">
                            <TD colspan="6" class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </TD>
                        </html:form>

                    </TR>
                </table>
            </app2:checkAccessRight>
            <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
                <tr>
                    <td>
                        <fanta:table list="campaignTypeList" width="100%" id="campaignType"
                                     action="Campaign/CampaignTypeList.do" imgPath="${baselayout}">
                            <c:set var="editAction"
                                   value="Campaign/CampaignType/Forward/Update.do?dto(campaignTypeId)=${campaignType.id}&dto(title)=${app2:encode(campaignType.title)}"/>
                            <c:set var="deleteAction"
                                   value="Campaign/CampaignType/Forward/Delete.do?dto(withReferences)=true&dto(campaignTypeId)=${campaignType.id}&dto(title)=${app2:encode(campaignType.title)}"/>
                            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CAMPAIGNTYPE" permission="DELETE">
                                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>
                            <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem"
                                              title="CampaignType.title" headerStyle="listHeader" width="95%"
                                              orderable="true" maxLength="25"/>
                        </fanta:table>
                    </td>
                </tr>

            </table>
        </td>
    </tr>
</table>