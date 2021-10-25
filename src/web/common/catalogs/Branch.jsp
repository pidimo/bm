<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
    </tr>
    <tr>
        <td>
            <html:form action="${action}" focus="dto(branchName)">
                <table id="Branch.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center"
                       class="container">

                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(branchId)"/>
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
                            <fmt:message key="Branch.name"/>
                        </TD>
                        <TD class="contain" width="75%">
                            <app:text property="dto(branchName)" styleClass="largeText" maxlength="100"
                                      view="${'delete' == op}"/>
                        </TD>
                    </TR>
                    <tr>
                        <td class="label">
                            <fmt:message key="Branch.group"/>
                        </td>
                        <td class="contain">
                            <fanta:select property="dto(group)"
                                          listName="BranchSelectTagList" labelProperty="name" valueProperty="id"
                                          firstEmpty="true" styleClass="largeSelect" readOnly="${'delete' == op}"
                                          value="${dto.group}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <c:if test="${'create' != op}">
                                    <fanta:parameter field="branchId" value="${not empty dto.branchId?dto.branchId:0}"/>
                                </c:if>
                            </fanta:select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"
                                <c:out value="${sessionScope.listshadow}" escapeXml="false"/>
                                ><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
                        </td>
                    </tr>
                </table>
                <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
                <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
                    <TR>

                        <TD class="button">
                            <app2:securitySubmit operation="${op}" functionality="BRANCH"
                                                 styleClass="button">${button}</app2:securitySubmit>
                            <c:if test="${op == 'create'}">
                                <app2:securitySubmit operation="${op}" functionality="BRANCH" styleClass="button"
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
