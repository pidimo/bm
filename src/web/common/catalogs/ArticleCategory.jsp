<%@ include file="/Includes.jsp" %>

<table width="60%" border="0" align="center" cellspacing="0" cellpadding="10">
  <tr>
    <td align="left">
    <html:form action="${action}" focus="dto(categoryName)" >

    <table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${('update' == op) || ('delete' == op)}">
            <html:hidden property="dto(categoryId)"/>
        </c:if>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
        </c:if>
        <tr >
          <td colspan="2" class="title"><c:out value="${title}"/> </td>
        </tr>
        <tr>
          <td width="25%" class="label" ><fmt:message  key="ArticleCategory.categoryName"/>
          </td>
          <td width="75%" class="contain">
               <app:text property="dto(categoryName)" view="${op == 'delete'}" styleClass="largeText" maxlength="40" tabindex="1"/>
          </td>
        </tr>
        <tr>
          <td class="label"><fmt:message key="ArticleCategory.parent"/></td>
          <td  class="contain">   <!-- Collection articleCategories-->
          <fanta:select property="dto(parentCategoryId)" listName="articleCategoryList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                      tabIndex="2"  readOnly="${op == 'delete'}" value="${articleCategoryForm.dtoMap.parentCategoryId}">
              <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
              <c:if test="${'create' != op}">
                <fanta:parameter field="categoryIdAux" value="${not empty articleCategoryForm.dtoMap.categoryId?articleCategoryForm.dtoMap.categoryId:0}"/>
              </c:if>
          </fanta:select>


          </td>
        </tr>
      </table>
<table width="100%" border="0" cellpadding="2" cellspacing="0">
<tr >
  <td class="button">
    <app2:securitySubmit operation="${op}" functionality="SUPPORTCATEGORY" styleClass="button" tabindex="3" >
       ${button}
    </app2:securitySubmit>
       <c:if test="${op == 'create'}" >
       <app2:securitySubmit operation="${op}" functionality="SUPPORTCATEGORY" styleClass="button" property="SaveAndNew" tabindex="4" >
       <fmt:message key="Common.saveAndNew"/>
    </app2:securitySubmit>
       </c:if>
            <html:cancel styleClass="button" tabindex="5"><fmt:message   key="Common.cancel"/></html:cancel>
      </td>
 </tr>
</table>
      </html:form>
    </td>
  </tr>
</table>