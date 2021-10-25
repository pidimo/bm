<%@ include file="/Includes.jsp" %>

<table width="60%" border="0" align="center" cellspacing="0" cellpadding="10">
    <tr>
        <td align="left">
            <html:form action="${action}" focus="dto(groupName)">

                <table id="ProductGroup.jsp" border="0" cellpadding="0" cellspacing="0" width="100%" align="center"
                       class="container">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(groupId)"/>
                    </c:if>
                    <c:if test="${('update' == op)}">
                        <html:hidden property="dto(version)"/>
                    </c:if>
                    <tr>
                        <td colspan="2" class="title">
                            <c:out value="${title}"/>
                        </td>
                    </tr>
                    <tr>
                        <td width="25%" class="label">
                            <fmt:message key="ProductGroup.name"/>
                        </td>
                        <td width="75%" class="contain">
                            <app:text property="dto(groupName)" view="${op == 'delete'}" styleClass="largeText"
                                      maxlength="80" tabindex=""/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label">
                            <fmt:message key="ProductGroup.nameParent"/>
                        </td>
                        <td class="contain">
                            <fanta:select property="dto(parentGroupId)"
                                          listName="productGroupSelectTagList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="select"
                                          readOnly="${'delete' == op}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <c:if test="${'update' == op}">
                                    <fanta:parameter field="groupId" value="${not empty dto.groupId?dto.groupId:0}"/>
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
                <table width="100%" border="0" cellpadding="2" cellspacing="0">
                    <tr>
                        <td class="button">
                                <app2:securitySubmit operation="${op}" functionality="PRODUCTGROUP"
                                                     styleClass="button">${button}</app2:securitySubmit>
                            <c:choose>
                            <c:when test="${op == 'create' }">
                            <app2:securitySubmit operation="${op}" functionality="PRODUCTGROUP" styleClass="button"
                                                 property="SaveAndNew">
                                <fmt:message key="Common.saveAndNew"/>
                            </app2:securitySubmit>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                            </c:when>
                            <c:otherwise>
                            <html:cancel styleClass="button">
                                <fmt:message key="Common.cancel"/>
                            </html:cancel>
                            </c:otherwise>
                            </c:choose>
                    </tr>
                </table>
            </html:form>
        </td>
    </tr>
</table>