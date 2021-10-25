<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="organizationType"><%= ContactConstants.ADDRESSTYPE_ORGANIZATION %>
</c:set>

<TABLE cellspacing="0" cellpadding="0" width="50%" border="0" align="center">
    <TR>
        <html:form action="/Supplier/Update.do" focus="dto(customerNumber)">
            <TD>
                <table cellSpacing=0 cellPadding=0 width="450" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="update"
                                                 functionality="SUPPLIER"
                                                 property="dto(save)"
                                                 styleClass="button"
                                                 tabindex="6">
                                <c:out value="${save}"/>
                            </app2:securitySubmit>
                        </TD>
                    </TR>
                </table>
                <table border="0" cellpadding="0" cellspacing="0" width="450" align="center" class="container">
                    <html:hidden property="dto(supplierId)" value="${param.contactId}"/>
                    <html:hidden property="dto(addressId)" value="${param.contactId}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                    <html:hidden property="dto(addressType)" value="${addressType}"/>
                    <html:hidden property="dto(name1)" value="${name1}"/>
                    <html:hidden property="dto(name2)" value="${name2}"/>
                    <html:hidden property="dto(name3)" value="${name3}"/>
                    <html:hidden property="dto(version)"/>
                    <html:hidden property="dto(op)" value="update"/>
                    <tr>
                        <td colspan="2" class="title">
                            <c:out value="${title}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="label" width="35%">
                            <fmt:message key="Supplier.customerNumber"/>
                        </td>
                        <td class="contain" width="65%">
                            <html:text property="dto(customerNumber)"
                                       styleClass="mediumText"
                                       maxlength="20"
                                       tabindex="1"/>
                        </td>
                    </tr>
                    <tr>
                        <TD class="label" width="35%">
                            <fmt:message key="Supplier.branch"/>
                        </TD>
                        <TD class="contain" width="65%">
                            <app:catalogSelect property="dto(branchId)"
                                               catalogTable="branch"
                                               idColumn="branchid"
                                               labelColumn="branchname"
                                               styleClass="mediumSelect"
                                               tabindex="2"/>
                        </TD>
                    </tr>

                    <tr>
                        <TD class="label">
                            <fmt:message key="Supplier.type"/>
                        </TD>
                        <TD class="contain">
                            <app:catalogSelect property="dto(supplierTypeId)"
                                               catalogTable="suppliertype"
                                               idColumn="suppliertypeid"
                                               labelColumn="suppliertypename"
                                               styleClass="mediumSelect"
                                               tabindex="3"/>
                        </TD>
                    </tr>
                    <c:if test="${sessionScope.user.valueMap['addressType'] == organizationType}">
                        <TR>
                            <TD class="label">
                                <fmt:message key="Contact.taxNumber"/>
                            </TD>
                            <TD class="contain">
                                <html:text property="dto(taxNumber)"
                                           styleClass="mediumText"
                                           maxlength="20"
                                           tabindex="4"
                                           readonly="${op == 'delete'}"/>
                            </TD>
                        </TR>
                    </c:if>
                </table>
                <table cellSpacing=0 cellPadding=0 width="450" border=0 align="center">
                    <TR>
                        <TD class="button">
                            <app2:securitySubmit operation="update"
                                                 functionality="SUPPLIER"
                                                 property="dto(save)"
                                                 styleClass="button"
                                                 tabindex="5">
                                <c:out value="${save}"/>
                            </app2:securitySubmit>
                        </TD>
                    </TR>
                </table>
            </TD>
        </html:form>
    </TR>
</TABLE>

</td>
</tr>
</table>