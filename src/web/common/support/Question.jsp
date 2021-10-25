<%@ include file="/Includes.jsp" %>
 <c:set var="context" value="<%=request.getContextPath()%>"/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
 <%
 pageContext.setAttribute("publishedList", JSPHelper.getPublishedQuestionList(request));
 %>
 <br>
 <html:form action="${action}" focus="dto(summary)" >
 <table width="60%" border="0" align="center" cellpadding="0" cellspacing="0" >
 <tr>
 <td>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">

 <html:hidden property="dto(op)" value="${op}"/>
 <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
 <fmt:message   var="dateTimePattern" key="dateTimePattern"/>

<c:if test="${'update' == op || 'delete' == op}">
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(questionId)" />
    <html:hidden property="dto(createUserId)" />
    <html:hidden property="dto(createDateTime)" />
</c:if>
<TR>
     <TD class="button" colspan="4" nowrap>
     <c:if test="${'create' != op && questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId']}">
<app2:securitySubmit operation="${op}" functionality="QUESTION" styleClass="button" >
    ${button}
</app2:securitySubmit>
    </c:if>
     <c:if test="${('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']) && questionForm.dtoMap.article != 'false'}">
<app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
    <c:url var="url" value="/support/Question/Article.do?dto(questionId)=${questionForm.dtoMap.questionId}&dto(summary)=${app2:encode(param['dto(summary)'])}&question=true"/>
    <html:button  property="dto(answer)" styleClass="button" onclick="location.href='${url}'" >
             <fmt:message    key="Question.answer"/>
    </html:button>
</app2:checkAccessRight>
     </c:if>
     <c:if test="${'create' == op}">
<app2:securitySubmit operation="${op}" functionality="QUESTION" styleClass="button" >
    ${button}
</app2:securitySubmit>
     </c:if>
<%--
<c:choose>
      <c:when test="${'delete' != op && 'create'!= op}">
 <html:cancel  property="dto(cancel)"  styleClass="button" tabindex="11" >
     <fmt:message    key="Common.cancel"/>
 </html:cancel>
      </c:when>
      <c:otherwise>
--%>
      <html:cancel styleClass="button" tabindex="11" >
            <fmt:message    key="Common.cancel"/>
        </html:cancel>
      <%--</c:otherwise>
</c:choose>--%>
</TD>
 </TR>

 <tr>
     <td colspan="4" class="title">
     <c:out value="${title}"/>
     </td>
 </TR>
 <c:if test="${'create' != op}">
 <TR>
      <TD  width="15%" class="label"><fmt:message   key="Question.askedBy"/></TD>
      <TD  width="35%" class="contain">
      <app:text property="dto(ownerName)" styleClass="mediumText" maxlength="40" tabindex="1" view="${true}"/>
      </TD>
      <TD class="topLabel" width="16%" ><fmt:message   key="Question.AskedOn"/></TD>
      <TD class="containTop" width="34%" >
          ${app2:getDateWithTimeZone(questionForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
     </TD>
  </TR>
</c:if>
 <TR>
     <TD  width="15%" class="label"><fmt:message   key="Question.summary"/></TD>
     <TD  width="35%" class="contain">
     <app:text property="dto(summary)" styleClass="middleText" maxlength="40" tabindex="3" view="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>
     </TD>
<%--        Change for CATEGORY--%>
     <TD class="topLabel" width="16%" ><fmt:message   key="Article.categoryName"/></TD>
     <TD class="containTop" width="34%" >
<c:choose>
  <c:when test="${'delete' != op && (questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId'] || 'create' == op)}">
     <fanta:select property="dto(categoryId)" listName="articleCategoryList" firstEmpty="true"
                  labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                  tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:tree columnId="id" columnParentId="parentCategoryId" separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
     </fanta:select>
</c:when>
<c:otherwise>
     <fanta:select property="dto(categoryId)" listName="articleCategorySimpleList"
              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
              readOnly="true">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
     </fanta:select>
</c:otherwise>
</c:choose>
    </TD>
 </TR>
 <tr>
    <td class="label" ><fmt:message key="Article.productName"/></td>
     <td class="contain">
    <html:hidden property="dto(productId)" styleId="field_key" />
    <html:hidden property="dto(1)" styleId="field_versionNumber"  />
    <html:hidden property="dto(2)" styleId="field_unitId"  />
    <html:hidden property="dto(3)" styleId="field_price"  />

    <app:text property="dto(productName)" styleId="field_name" styleClass="mediumText" maxlength="40" readonly="true" tabindex="" view="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>

        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" hide="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}" />
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" hide="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}"/>

    </TD>
    <TD class="topLabel" width="16%" ><fmt:message   key="Article.published"/></TD>
     <TD class="containTop" width="34%" >

     <html:select property="dto(published)" styleClass="halfMiddleTextSelect" readonly="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}" tabindex="6">
            <html:option value="" />
            <html:options collection="publishedList"  property="value" labelProperty="label" />
     </html:select>

    </TD>
 </tr>
 <TR>
    <TD class="topLabel" colspan="4"><fmt:message   key="Question.detail"/><br>
        <html:textarea property="dto(detail)" styleClass="mediumDetail" readonly="${op == 'delete' || ('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId'])}" tabindex="7" style="height:120px;width:99%;"/><br>&nbsp;
    </TD>
  </TR>
 </table>
</td>
</tr>
 <TR>
      <TD class="button" colspan="4" nowrap>
      <c:if test="${'create' != op && questionForm.dtoMap.createUserId == sessionScope.user.valueMap['userId']}">
 <app2:securitySubmit operation="${op}" functionality="QUESTION" styleClass="button" >
    ${button}
 </app2:securitySubmit>
     </c:if>
      <c:if test="${('create' != op && questionForm.dtoMap.createUserId != sessionScope.user.valueMap['userId']) && questionForm.dtoMap.article != 'false'}">
 <app2:checkAccessRight functionality="ARTICLE" permission="CREATE">
     <c:url var="url" value="/support/Question/Article.do?dto(questionId)=${questionForm.dtoMap.questionId}&dto(summary)=${app2:encode(param['dto(summary)'])}&question=true"/>
     <html:button  property="dto(answer)" styleClass="button" onclick="location.href='${url}'" >
              <fmt:message    key="Question.answer"/>
     </html:button>
 </app2:checkAccessRight>
      </c:if>
      <c:if test="${'create' == op}">
 <app2:securitySubmit operation="${op}" functionality="QUESTION" styleClass="button" >
    ${button}
 </app2:securitySubmit>
      </c:if>
          <html:cancel styleClass="button" tabindex="11" >
            <fmt:message    key="Common.cancel"/>
        </html:cancel>
 </TD>
  </TR>

 </table>
</html:form>