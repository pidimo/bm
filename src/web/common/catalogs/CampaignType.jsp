<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <html:form action="${action}" focus="dto(title)">
                <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

                    <html:hidden property="dto(op)" value="${op}"/>

                        <%--if update action or delete action--%>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(campaignTypeId)"/>
                    </c:if>

                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>

                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <TR>
                        <TD colspan="2" class="title">
                            <c:out value="${title}"/>
                        </TD>
                    </TR>

                    <TR>
                        <TD class="label" width="25%" nowrap>
                            <fmt:message key="CampaignType.title"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(title)" styleClass="text" maxlength="20" view="${'delete' == op}"/>
                        </TD>
                    </TR>
                </table>


                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="CAMPAIGNTYPE"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="CAMPAIGNTYPE" styleClass="button"
                                                     property="SaveAndNew">
                                    <fmt:message key="Common.saveAndNew"/>
                                </app2:securitySubmit>
                            </c:if>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                        </TD>
                    </TR>
                </table>
            </html:form>

        </td>
    </tr>
</table>