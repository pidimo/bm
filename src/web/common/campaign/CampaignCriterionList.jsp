<%@ page import="com.piramide.elwis.utils.CampaignConstants,
                 org.alfacentauro.fantabulous.controller.ResultList" %>
<%@ include file="/Includes.jsp" %>

<%
    pageContext.setAttribute("categoryTypeList", JSPHelper.getCategoryTypeList(request));
    pageContext.setAttribute("WithContactPerson", JSPHelper.getCampaignWithoutContactPersonList(request));
    pageContext.setAttribute("OnlyCompanies", JSPHelper.getCampaignOnlyCompaniesList(request));
%>
<c:set var="criteriaHasEmail" value="<%=CampaignConstants.HasHasNotEmailCriteria.HASEMAIL.getConstantAsString()%>" scope="request"/>
<c:set var="criteriaHasNotEmail" value="<%=CampaignConstants.HasHasNotEmailCriteria.HASNOTEMAIL.getConstantAsString()%>" scope="request"/>

<tags:jscript language="JavaScript" src="/js/campaign/campaign.jsp"/>

<script type="text/javascript">
    function hasEmailSelected(hasEmailBox){
        if(hasEmailBox.value == '${criteriaHasEmail}' || hasEmailBox.value == '${criteriaHasNotEmail}'){
            document.getElementById('telecomTypeStyle').style.display = '';
        } else {
            document.getElementById('telecomTypeStyle').style.display = 'none';
        }
    }
</script>

<table width="90%" border="0" align="center" cellpadding="3" cellspacing="1">

    <html:form action="/CampaignCriterion/Forward/Create.do">
        <tr>
            <td class="button">
                <app2:securitySubmit operation="create" functionality="CAMPAIGNCRITERION" styleClass="button"
                                     tabindex="1">
                    <fmt:message key="Common.new"/>
                </app2:securitySubmit>
            </td>
        </tr>
    </html:form>

    <tr>
        <td>

            <fanta:table list="campaignCriterionList" width="100%" id="campaignCriterion"
                         action="SelectionCriteria/List.do" imgPath="${baselayout}">
                <c:set var="editAction"
                       value="Campaign/SelectionCriteria/Forward/Update.do?ope=read&campaignCriterionId=${campaignCriterion.campaignCriterionId}&fieldType=${campaignCriterion.fieldType}&descriptionKey=${campaignCriterion.descriptionKey}"/>
                <c:set var="deleteAction"
                       value="Campaign/SelectionCriteria/Forward/Delete.do?ope=readDelete&withReferences=true&campaignCriterionId=${campaignCriterion.campaignCriterionId}&fieldType=${campaignCriterion.fieldType}&descriptionKey=${campaignCriterion.descriptionKey}"/>
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="CAMPAIGNCRITERION" permission="VIEW">
                        <fanta:actionColumn name="del" action="${editAction}" title="Common.update"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            image="${baselayout}/img/edit.gif">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="CAMPAIGNCRITERION" permission="DELETE">
                        <fanta:actionColumn name="del" action="${deleteAction}" title="Common.delete"
                                            styleClass="listItem" headerStyle="listHeader" width="30%"
                                            image="${baselayout}/img/delete.gif">
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="descriptionKey" renderData="false" action="${editAction}" styleClass="listItem"
                                  title="Campaign.criteria" headerStyle="listHeader" width="20%" orderable="false"
                                  maxLength="25">
                    <c:choose>
                        <c:when test="${campaignCriterion.categoryId !=''}">

                            <app:categorySelect categoryId="${campaignCriterion.categoryId}"/>
                            <c:set var="dto(descriptionKey)" value="${description}"/>
                        </c:when>

                        <c:otherwise>
                            <fmt:message key="${campaignCriterion.descriptionKey}"/>
                        </c:otherwise>
                    </c:choose>
                </fanta:dataColumn>

                <fanta:dataColumn name="tableId" renderData="false" styleClass="listItem"
                                  title="CampaignCriterion.category" headerStyle="listHeader" width="15%"
                                  orderable="false" maxLength="25">
                    <c:choose>
                        <c:when test="${campaignCriterion.tableId != null && '' != campaignCriterion.tableId}">
                            <c:set var="tableValue" value="${campaignCriterion.tableId}"/>
                        </c:when>
                        <c:when test="${campaignCriterion.categoryTableId != null && '' != campaignCriterion.categoryTableId}">
                            <c:set var="tableValue" value="${campaignCriterion.categoryTableId}"/>
                        </c:when>
                    </c:choose>

                    <c:choose>
                        <c:when test="${not empty tableValue }">
                            <c:set var="tableLabel" value="${app2:getCategoryTableType(tableValue, pageContext.request)}"/>
                            <fanta:textShorter title="${tableLabel}">
                                <c:out value="${tableLabel}"/>
                            </fanta:textShorter>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>

                </fanta:dataColumn>
                <fanta:dataColumn name="" renderData="false" styleClass="listItem" title="Campaign.compare"
                                  headerStyle="listHeader" width="18%" orderable="false" maxLength="25">

                    <c:set scope="page" var="ope" value="${campaignCriterion.operator}"/>
                    <%
                        out.println(JSPHelper.getCriteriaOperator((String) pageContext.getAttribute("ope"), request));
                    %>
                </fanta:dataColumn>
                <fanta:dataColumn name="" renderData="false" styleClass="listItem" title="Campaign.value"
                                  headerStyle="listHeader" width="33%" orderable="false">

                    <app:criteriaValueTag criterionId="${campaignCriterion.campaignCriterionId}"
                                          campCriterionValueId="${campaignCriterion.campCriterionValueId}"
                                          categoryId="${campaignCriterion.categoryId}"/>

                </fanta:dataColumn>
                <fanta:dataColumn name="numberHits" styleClass="listItem2Right" title="Campaign.hits"
                                  headerStyle="listHeader" width="17%" orderable="false">
                </fanta:dataColumn>
            </fanta:table>
            <%
                ResultList resultList = (ResultList) request.getAttribute("campaignCriterionList");
                pageContext.setAttribute("size", new Integer(resultList.getResultSize()));
            %>
            <html:form action="/Campaign/AditionalCriteria.do?tabKey=Campaign.Tab.recipients">

                <table width="100%" align="center">
                    <tr>
                        <td class="button">
                            <c:if test="${not empty criterionListForm.totalHits}">
                                <fmt:message key="Campaign.totalHits.message"/>
                                <c:out value="${criterionListForm.totalHits}"/>
                            </c:if>
                            <c:if test="${size > 0}">
                                <html:submit styleClass="button" property="updateHitsButton">
                                    <fmt:message key="Campaign.updateHits"/>
                                </html:submit>
                            </c:if>
                        </td>
                    </tr>
                </table>

            <br>


                <table width="90%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                    <tr>
                        <td class="title" colspan="4">
                            <fmt:message key="Campaign.aditionalCriterias"/>
                        </td>
                    </tr>

                    <tr vAlign=top>
                        <TD class="label"><fmt:message key="Campaign.companies"/></TD>
                        <td class="contain">
                            <html:select property="addressType" styleClass="mediumSelect" tabindex="8">
                                <html:options collection="OnlyCompanies" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                        <TD class="label">
                            <fmt:message key="Campaign.partner"/>
                        </TD>
                        <TD class="contain"><html:checkbox property="includePartner" styleClass="radio"/></TD>
                    </TR>
                    <TR>
                        <TD class="label"><fmt:message key="Campaign.withoutContactPerson"/></TD>
                        <TD class="contain">
                            <html:select property="contactType" styleClass="mediumSelect" tabindex="9">
                                <html:options collection="WithContactPerson" property="value" labelProperty="label"/>
                            </html:select>
                        </TD>
                        <TD class="label"><fmt:message key="Campaign.duplicate"/></TD>
                        <td class="contain"><html:checkbox property="isDouble" styleClass="radio"/></td>
                    </TR>
                    <TR>
                        <TD class="label"><fmt:message key="Campaign.hasHasNotMailAddress"/></TD>
                        <TD class="contain" colspan="3">
                            <c:set var="hasHasNotEmailCriteriaList" value="${app2:getHasHasNotEmailCriteria(pageContext.request)}"/>
                            <html:select property="hasEmail" styleClass="mediumSelect" tabindex="10"
                                         onchange="hasEmailSelected(this)"
                                         onkeyup="hasEmailSelected(this)">
                                <html:options collection="hasHasNotEmailCriteriaList" property="value" labelProperty="label"/>
                            </html:select>

                            <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                            <c:choose>
                                <c:when test="${(criterionListForm.hasEmail eq criteriaHasEmail) or (criterionListForm.hasEmail eq criteriaHasNotEmail)}">
                                    <c:set var="displayTelecomType" value=""/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="displayTelecomType" value="none"/>
                                </c:otherwise>
                            </c:choose>
                            &nbsp;
                            <html:select property="hasEmailTelecomType" styleClass="select" tabindex="11"
                                         styleId="telecomTypeStyle"
                                         style="display:${displayTelecomType}">
                                <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                            </html:select>
                        </TD>
                    </TR>
                </table>


                <app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="CREATE">
                    <table width="90%" border="0" align="center" cellpadding="2" cellspacing="0">
                        <tr>
                            <td class="button">

                                <fmt:message key="Campaign.deleteAll"/> &nbsp;
                                <html:checkbox property="deletePrevius" styleClass="radio"/>

                                <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
                                <html:hidden property="dto(sizeCurrentCampContact)" value="${sizeCurrentCampContact}"/>
                                <html:hidden property="dto(companyId)"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                                <html:hidden property="dto(recordUserId)"
                                             value="${sessionScope.user.valueMap['userId']}"/>
                                <html:hidden property="dto(operation)" value="execute"/>
                                <html:submit styleClass="button"><fmt:message
                                        key="Campaign.executeCriteria"/></html:submit>
                            </td>
                        </tr>
                    </table>
                </app2:checkAccessRight>
            </html:form>
        </td>
    </tr>
</table>



