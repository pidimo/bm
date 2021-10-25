<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>
<c:set var="generalWidth" value="${200}" scope="request"/>
<c:set var="elementCounter" value="${0}" scope="request"/>
<c:set var="categoryTab" value="${app2:getCategoryTab(param['categoryTabId'], pageContext.request)}"/>
<c:set var="formName" value="categoryFieldValueForm" scope="request"/>
<c:set var="labelWidth" value="15" scope="request"/>
<c:set var="containWidth" value="85" scope="request"/>
<c:set var="operation" value="${op}" scope="request"/>

<c:import url="/WEB-INF/jsp/catalogs/CategoryTabUtilHeader.jsp"/>
<html:form action="${action}" enctype="multipart/form-data" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(categoryTabVersion)"/>
    <html:hidden property="dto(checkVersion)" value="true"/>
    <html:hidden property="dto(categoryTabId)" value="${categoryTab.categoryTabId}"/>
   <div class="${app2:getFormClasses()}">
       <div class="${app2:getFormButtonWrapperClasses()}">
           <html:submit styleClass="button ${app2:getFormButtonClasses()}"
                        onclick="javascript:fillMultipleSelectValues();">${button}
           </html:submit>
           <c:if test="${'true' == showCancelButton}">
               <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                   <fmt:message key="Common.cancel"/>
               </html:cancel>
           </c:if>
       </div>
       <div class="${app2:getFormPanelClasses()}">
           <fieldset>
               <c:forEach var="categoryGroup" items="${categoryTab.categoryGroups}">
                   <c:choose>
                       <c:when test="${true == categoryGroup.onlySubCategories}">
                           <div id="groupId_${categoryGroup.categoryGroupId}" style="display:none;">
                               <legend class="title">
                                       ${categoryGroup.label}
                               </legend>
                           </div>
                           <html:hidden property="dto(groupCounter_${categoryGroup.categoryGroupId})"
                                        value=""
                                        styleId="groupCounter_${categoryGroup.categoryGroupId}"/>
                       </c:when>
                       <c:otherwise>
                           <legend class="title">
                                   ${categoryGroup.label}
                           </legend>
                       </c:otherwise>
                   </c:choose>
                   <c:forEach var="categoryId" items="${categoryGroup.categories}">
                       <c:set var="elementCounter" value="${elementCounter+1}" scope="request"/>
                       <c:set var="categoryId" value="${categoryId}" scope="request"/>
                       <c:set var="groupId" value="${categoryGroup.categoryGroupId}" scope="request"/>
                       <c:import url="/WEB-INF/jsp/catalogs/RenderCategory.jsp"/>
                   </c:forEach>

               </c:forEach>
           </fieldset>
       </div>
       <div class="${app2:getFormButtonWrapperClasses()}">
           <html:submit styleClass="button ${app2:getFormButtonClasses()}"
                        onclick="javascript:fillMultipleSelectValues();">${button}</html:submit>
           <c:if test="${'true' == showCancelButton}">
               <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}">
                   <fmt:message key="Common.cancel"/>
               </html:cancel>
           </c:if>
       </div>
   </div>
</html:form>
<tags:jQueryValidation formName="categoryFieldValueForm"/>
