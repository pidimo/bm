<%@ include file="/Includes.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html:form action="/Mail/SignatureList.do" focus="parameter(mailAccountId)">
    <table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
        <tr>
            <td class="label" width="40%"><fmt:message key="Webmail.signature.mailAccount"/></td>
            <td align="left" class="contain" width="60%">
                <fanta:select property="parameter(mailAccountId)"
                              listName="mailAccountList"
                              labelProperty="email"
                              valueProperty="mailAccountId"
                              firstEmpty="true"
                              styleClass="select"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
                </fanta:select>
                &nbsp;<html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
            </td>
        </tr>
        <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="Mail/SignatureList.do" parameterName="signatureName"/>
        </td>
    </tr>
    </table>
</html:form>
<table width="60%" border="0" align="center" cellpadding="2" cellspacing="0">
    <tr>
        <html:form action="/Mail/Forward/CreateSignature.do">
            <td class="button">
                <app2:securitySubmit operation="create" functionality="WEBMAILSIGNATURE" styleClass="button"
                                     tabindex="1"><fmt:message key="Common.new"/></app2:securitySubmit>
            </td>
        </html:form>
    </tr>
</table>
<TABLE id="SignatureList.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" class="container" align="center">
    <tr>
        <td>
            <fanta:table list="signatureList" width="100%" id="signature" action="Mail/SignatureList.do"
                         imgPath="${baselayout}" align="center">
                <app:url value="/Mail/Forward/UpdateSignature.do?dto(signatureId)=${signature.SIGNATUREID}&dto(op)=read"
                         var="urlUpdate" enableEncodeURL="false"/>
                <app:url value="/Mail/Forward/DeleteSignature.do?dto(signatureId)=${signature.SIGNATUREID}&dto(op)=read"
                         var="urlDelete" enableEncodeURL="false"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="SIGNATURENAME" styleClass="listItem"
                                  action="${urlUpdate}"
                                  title="Webmail.signature.name" headerStyle="listHeader" width="35%" orderable="true"
                                  maxLength="25"/>
                <fanta:dataColumn name="EMAIL" styleClass="listItem"
                                  title="Webmail.signature.mailAccount" headerStyle="listHeader" width="40%"
                                  orderable="true" maxLength="25"/>
                <fanta:dataColumn name="ISDEFAULT" styleClass="listItem2Center" title="Webmail.signature.isDefault"
                                  headerStyle="listHeader" width="20%" orderable="false" maxLength="10"
                                  renderData="false">
                    <c:if test="${signature.ISDEFAULT == 1}">
                        <html:img src="${baselayout}/img/check.gif" border="0" alt=""/>
                    </c:if>
                </fanta:dataColumn>

            </fanta:table>
        </td>
    </tr>
</table>