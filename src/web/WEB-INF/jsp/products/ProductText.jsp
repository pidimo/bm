<%@ include file="/Includes.jsp" %>

<html:form action="${action}" focusIndex="1">

    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="20">
                <fmt:message key="Common.save"/>
            </html:submit>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <fmt:message key="ProductText.title.translate"/>
            </legend>
           <div>
               <div class="table-responsive">
                   <table class="${app2:getTableClasesIntoForm()}" width="100%" align="center">
                       <tr>
                           <th width="10%"><fmt:message key="ProductText.default"/></th>
                           <th width="30%"><fmt:message key="ProductText.language"/></th>
                           <th width="60%"><fmt:message key="ProductText.text"/></th>
                       </tr>

                       <html:hidden property="dto(productId)" styleId="s"/>
                       <html:hidden property="dto(op)" value="${op}"/>
                       <html:hidden property="dto(version)"/>
                       <c:set var="tabIdx" value="${0}"/>
                       <c:forEach var="language"
                                  items="${app2:getCompanyLanguages(pageContext.request)}" varStatus="i">
                           <tr>
                               <td width="20%">
                                   <c:set var="tabIdx" value="${tabIdx + 1}"/>

                                   <div class="radio radio-default radio-inline">
                                       <html:radio property="dto(isDefault)"
                                                   value="${language.languageId}"
                                                   styleClass="radio" tabindex="${tabIdx}"/>
                                       <label></label>
                                   </div>
                               </td>
                               <td width="20%">
                                   <html:hidden property="dto(uiLanguages)"
                                                value="${language.languageId}"
                                                styleId="dto(uiLanguages)"/>
                                   <html:hidden property="dto(language_${language.languageId})"
                                                value="${language.languageName}"/>
                                   <c:out value="${language.languageName}"/>
                               </td>
                               <td width="60%">
                                   <c:set var="tabIdx" value="${tabIdx + 1}"/>
                                   <html:textarea property="dto(text_${language.languageId})"
                                                  styleClass="minimumDetail ${app2:getFormInputClasses()}"
                                                  tabindex="${tabIdx}"/>
                               </td>
                           </tr>
                       </c:forEach>
                   </table>
               </div>
           </div>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="${tabIdx+3}">
                <fmt:message key="Common.save"/>
            </html:submit>
        </div>
    </div>
</html:form>

