<%@ include file="/Includes.jsp" %>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/Bank/List.do" styleClass="form-horizontal">
        <div class="row wrapperSearch">
            <html:hidden property="listName" value="bankList"/>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()} label-left" for="bankName_id">
                    <fmt:message key="Bank.name"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="parameter(bankName)" styleId="bankName_id"
                               styleClass="${app2:getFormInputClasses()} largeText"/>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()}" for="bankCode_id">
                    <fmt:message key="Bank.code"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="parameter(bankCode)" styleId="bankCode_id"
                               styleClass="${app2:getFormInputClasses()} mediumText"/>
                </div>
            </div>
            <div class="${app2:getFormGroupTwoSearchButton()}">
                <div class="col-sm-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
                <%--<div class="form-group">--%>
                <%--<html:submit styleClass="${app2:getFormButtonClasses()}">--%>
                <%--<fmt:message key="Common.go"/>--%>
                <%--</html:submit>--%>
                <%--</div>--%>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="Bank/List.do" parameterName="bankNameAlpha" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="BANK" permission="CREATE">
        <html:form styleId="CREATE_NEW_BANK" action="/Bank/Forward/Create.do?op=create">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="bankList" id="bank" width="100%" action="Bank/List.do"
                     styleClass="${app2:getFantabulousTableClases()}"
                     imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Bank/Forward/Update.do?dto(bankId)=${bank.id}&dto(bankName)=${app2:encode(bank.name)}"/>
            <c:set var="deleteAction"
                   value="Bank/Forward/Delete.do?dto(withReferences)=true&dto(bankId)=${bank.id}&dto(bankName)=${app2:encode(bank.name)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="BANK" permission="VIEW">
                    <fanta:actionColumn name="up" title="Common.update" action="${editAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="BANK" permission="DELETE">
                    <fanta:actionColumn name="del" title="Common.delete" action="${deleteAction}"
                                        styleClass="listItem" headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Bank.name"
                              headerStyle="listHeader" width="35%" orderable="true" maxLength="25"/>
            <fanta:dataColumn name="code" styleClass="listItem" title="Bank.code"
                              headerStyle="listHeader" width="10%" orderable="true"/>
            <fanta:dataColumn name="label" styleClass="listItem" title="Bank.label"
                              headerStyle="listHeader" width="20%" orderable="true"/>
            <fanta:dataColumn name="internationalCode" styleClass="listItem2"
                              title="Bank.internationalCode" headerStyle="listHeader" width="30%"
                              orderable="true" maxLength="18"/>
        </fanta:table>
    </div>
</div>