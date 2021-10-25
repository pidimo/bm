<%@ include file="/Includes.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Mail/SignatureList.do" focus="parameter(mailAccountId)" styleClass="form-horizontal">

        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} label-left" for="mailAccountId_id">
                <fmt:message key="Webmail.signature.mailAccount"/>
            </label>

            <div class="${app2:getFormOneSearchInput()} wrapperButton">
                <fanta:select property="parameter(mailAccountId)"
                              listName="mailAccountList"
                              labelProperty="email"
                              valueProperty="mailAccountId"
                              firstEmpty="true"
                              styleId="mailAccountId_id"
                              styleClass="${app2:getFormInputClasses()} select"
                              readOnly="${'delete' == op}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
                </fanta:select>
            </div>

            <div class="col-xs-12 col-sm-4">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </div>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Mail/SignatureList.do" parameterName="signatureName" mode="bootstrap"/>
        </div>
    </html:form>

    <html:form action="/Mail/Forward/CreateSignature.do">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="create"
                                 functionality="WEBMAILSIGNATURE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="1">
                <fmt:message key="Common.new"/>
            </app2:securitySubmit>
        </div>
    </html:form>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="signatureList" width="100%" id="signature" action="Mail/SignatureList.do"
                     imgPath="${baselayout}" align="center" styleClass="${app2:getFantabulousTableClases()}">
            <app:url
                    value="/Mail/Forward/UpdateSignature.do?dto(signatureId)=${signature.SIGNATUREID}&dto(op)=read"
                    var="urlUpdate" enableEncodeURL="false"/>
            <app:url
                    value="/Mail/Forward/DeleteSignature.do?dto(signatureId)=${signature.SIGNATUREID}&dto(op)=read"
                    var="urlDelete" enableEncodeURL="false"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="VIEW">
                    <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="WEBMAILSIGNATURE" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="SIGNATURENAME" styleClass="listItem"
                              action="${urlUpdate}"
                              title="Webmail.signature.name" headerStyle="listHeader" width="35%"
                              orderable="true"
                              maxLength="25"/>
            <fanta:dataColumn name="EMAIL" styleClass="listItem"
                              title="Webmail.signature.mailAccount" headerStyle="listHeader" width="40%"
                              orderable="true" maxLength="25"/>
            <fanta:dataColumn name="ISDEFAULT" styleClass="listItem2Center" title="Webmail.signature.isDefault"
                              headerStyle="listHeader" width="20%" orderable="false" maxLength="10"
                              renderData="false">
                <c:if test="${signature.ISDEFAULT == 1}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>

        </fanta:table>
    </div>
</div>