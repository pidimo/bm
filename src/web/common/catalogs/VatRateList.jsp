<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<%
    String vatRateVatId = request.getParameter("dto(vatId)");
    String vatLabel = request.getParameter("dto(vatLabel)");
    request.setAttribute("vatId", vatRateVatId);
    request.setAttribute("vatLabel", vatLabel);
%>

<table width="95%" border="0" align="center" cellpadding="2" cellspacing="0" id="tableId">
    <tr>
        <td>

            <table width="100%" cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <app2:checkAccessRight functionality="VATRATE" permission="CREATE">
                            <app:url value="/VatRate/Forward/Create?dto(check)=true" var="url"/>
                            <table cellpadding="0" cellspacing="0" border="0" width="60%" align="center">
                                <TR>
                                    <TD colspan="6" align="left">
                                        <html:submit styleClass="button" property="new"
                                                     onclick="window.parent.location='${url}'">
                                            <fmt:message key="Common.new"/>
                                        </html:submit>
                                    </TD>
                                </TR>
                            </table>
                        </app2:checkAccessRight>
                    </td>
                </tr>

                <tr>
                    <td>
                        <TABLE border="0" cellpadding="0" cellspacing="0" width="60%" align="center">
                            <tr>
                                <td class="title">
                                    <fmt:message key="${windowTitle}"/>
                                </td>
                            </tr>

                            <TR align="center">
                                <td>
                                    <fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
                                    <fmt:message var="datePattern" key="datePattern"/>

                                    <fanta:table list="vatRateList" width="100%" id="vatRate"
                                                 action="VatRate/List.do?dto(vatId)=${vatId}&dto(vatLabel)=${app2:encode(vatLabel)}"
                                                 imgPath="${baselayout}">

                                        <app:url var="editAction"
                                                 value="VatRate/Forward/Update.do?op=update&dto(vatrateId)=${vatRate.id}&dto(validFrom)=${vatRate.from}&dto(vatRate)=${vatRate.rate}&dto(vatId)=${vatRate.vId}&dto(vatLabel)=${app2:encode(vatRate.vat)}&dto(datePattern)=${datePattern}"/>
                                        <app:url var="deleteAction"
                                                 value="VatRate/Forward/Delete.do?op=delete&dto(vatrateId)=${vatRate.id}&dto(withReferences)=true&dto(validFrom)=${vatRate.from}&dto(vatRate)=${vatRate.rate}&dto(vatId)=${vatRate.vId}&dto(vatLabel)=${app2:encode(vatRate.vat)}&dto(datePattern)=${datePattern}"/>

                                        <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                                            <app2:checkAccessRight functionality="VATRATE" permission="VIEW">
                                                <fanta:actionColumn name="up" title="Common.update"
                                                                    action="javascript:goParentURL('${editAction}')"
                                                                    styleClass="listItem" headerStyle="listHeader"
                                                                    image="${baselayout}/img/edit.gif"
                                                                    useJScript="true"/>
                                            </app2:checkAccessRight>

                                            <app2:checkAccessRight functionality="VATRATE" permission="DELETE">
                                                <fanta:actionColumn name="del" title="Common.delete"
                                                                    action="javascript:goParentURL('${deleteAction}')"
                                                                    styleClass="listItem" headerStyle="listHeader"
                                                                    image="${baselayout}/img/delete.gif"
                                                                    useJScript="true"/>
                                            </app2:checkAccessRight>
                                        </fanta:columnGroup>

                                        <fanta:dataColumn name="from"
                                                          action="javascript:goParentURL('${editAction}')"
                                                          styleClass="listItem" title="VatRate.validFrom"
                                                          headerStyle="listHeader" width="60%" orderable="true"
                                                          renderData="false" useJScript="true">
                                            <fmt:formatDate value="${app2:intToDate(vatRate.from)}"
                                                            pattern="${datePattern}"/>
                                            &nbsp;
                                        </fanta:dataColumn>
                                        <fanta:dataColumn name="rate" styleClass="listItem2" title="VatRate.vatRate"
                                                          headerStyle="listHeader" width="35%" orderable="true"
                                                          renderData="false" style="text-align:right">
                                            <fmt:formatNumber value="${vatRate.rate}" type="number"
                                                              pattern="${numberFormat}"/>
                                            &nbsp;
                                        </fanta:dataColumn>
                                    </fanta:table>


                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<script>
    addAppPageEvent(window, 'load', incrementTableInIframe);
</script>