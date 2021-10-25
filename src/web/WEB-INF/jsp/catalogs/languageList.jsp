<%@ include file="/Includes.jsp" %>
<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="LANGUAGE" permission="CREATE">
        <html:form styleId="CREATE_NEW_LANGUAGE" action="/Language/Forward/Create.do">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                        key="Common.new"/></html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="languageList" styleClass="${app2:getFantabulousTableClases()}" id="language"
                     action="Language/List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Language/Forward/Update.do?dto(languageId)=${language.id}&dto(languageName)=${app2:encode(language.name)}"/>
            <c:set var="deleteAction"
                   value="Language/Forward/Delete.do?dto(withReferences)=true&dto(languageId)=${language.id}&dto(languageName)=${app2:encode(language.name)}"/>
            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                <app2:checkAccessRight functionality="LANGUAGE" permission="VIEW">
                    <fanta:actionColumn name="" title="Common.update" action="${editAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="LANGUAGE" permission="DELETE">
                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}" styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="name" action="${editAction}" styleClass="listItem" title="Language.name"
                              headerStyle="listHeader" width="40%" orderable="true" maxLength="25">
            </fanta:dataColumn>
            <fanta:dataColumn name="iso" styleClass="listItem" title="Language.iso" headerStyle="listHeader" width="40%"
                              orderable="false" renderData="false">
                <c:set var="languageConstant" value="${language.iso}" scope="request"/>

                <%
                    String constant = (String) request.getAttribute("languageConstant");
                    String key = JSPHelper.getSystemLanguage(constant, request);
                    request.setAttribute("constantKey", key);
                %>
                ${constantKey}&nbsp;

            </fanta:dataColumn>
            <fanta:dataColumn name="" styleClass="listItem2" title="Common.default" headerStyle="listHeader" width="20%"
                              renderData="false">
                <c:if test="${language.isDefault == '1'}">
                    <img src='<c:url value="${sessionScope.layout}/img/check.gif"/>'/>&nbsp;
                </c:if>
            </fanta:dataColumn>

        </fanta:table>
    </div>
</div>