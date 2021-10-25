<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

 <c:set var="context" value="<%=request.getContextPath()%>"/>

<script language="JavaScript">
<!--
    function isRating(){
    document.getElementById('ratingArea').style.display = "none";
    }

function moveOptionSelected(sourceId, targetId) {
    var sourceBox = document.getElementById(sourceId);
    var targetBox = document.getElementById(targetId);
    var i = 0;
    while (i < sourceBox.options.length) {
        var opt = sourceBox.options[i];
        if (opt.selected) {
            var nOpt = new Option();
            nOpt.text = opt.text;
            nOpt.value = opt.value;
            targetBox.options[targetBox.options.length] = nOpt;
            sourceBox.remove(i);
        } else {
            i = i + 1;
        }
    }

    fillUserArticleAccessRight();
}

function fillUserArticleAccessRight() {
    var userGroupIds = '';

    $("#selectedUserGroup_Id").find("option").each(function() {
        if(userGroupIds.length > 0) {
            userGroupIds = userGroupIds + ",";
        }
        userGroupIds = userGroupIds + $(this).val();
    });

    //set in hidden dto property
    $("#articleUserGroupIds_Id").val(userGroupIds);
}

//-->
</script>

<table border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<html:form action="${action}" >

 <html:hidden property="dto(op)" value="${op}"/>
 <html:hidden property="dto(rating)" value="false"/>
 <html:hidden property="dto(position)" />
 <html:hidden property="dto(noPosition)" />
 <html:hidden property="dto(voted)" />
 <html:hidden property="dto(question)" />
 <html:hidden property="dto(createDateTimeQuestion)" />
 <html:hidden property="dto(ownerQuestionName)" />
 <fmt:message   var="datePattern" key="datePattern"/>
 <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<c:set var="isOwner" value="true"/>

<c:if test="${'update' == op || 'delete' == op}">
<html:hidden property="dto(version)"/>
<html:hidden property="dto(articleId)" />
<html:hidden property="dto(createUserId)"/>

<c:set var="isOwner" value="${sessionScope.user.valueMap['userId'] == articleForm.dtoMap.createUserId }"/>


</c:if>
<c:if test="${'create' == op}">
    <c:set var="isOwner" value="true"/>
    <html:hidden property="dto(createUserId)" value="${sessionScope.user.valueMap['userId']}"/>
</c:if>
<tr>
     <td class="button" colspan="4" nowrap>
     <%--for Question functionality--%>

    <app2:securitySubmit operation="${op}" functionality="ARTICLE" styleClass="button" tabindex="1">
        ${button}
    </app2:securitySubmit>

<c:if test="${'update' == op && isOwner}">

        <app:url var="url"
                 value="/Article/Forward/Delete.do?articleId=${param.articleId}&dto(articleId)=${param.articleId}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
        <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                <html:button  onclick="location.href='${url}'" styleClass="button" property="dto(save)"  tabindex="2" >
                    <fmt:message  key="Common.delete"/>
                </html:button>
        </app2:checkAccessRight>
</c:if>
         <c:if test="${'update' != op}">
             <html:cancel  styleClass="button" tabindex="3" >
                <fmt:message    key="Common.cancel"/>
            </html:cancel>
         </c:if>
      </td>
 </tr>
 <tr>
     <td colspan="4" class="title">
    <c:out value="${title}"/>
     </td>
 </tr>
 <c:if test="${op != 'create'}">
    <tr>
         <td  width="15%" class="label"><fmt:message   key="Article.ownerName"/></TD>
         <td  width="35%" class="contain">
           <html:hidden property="dto(ownerName)" write="true" />
         </td>
         <td class="label" width="16%" ><fmt:message   key="Article.changeName"/></TD>
         <td class="contain" width="34%" >
              <html:hidden property="dto(changeName)" write="true" />
        </td>
    </tr>
    <tr>
         <TD  width="15%" class="label"><fmt:message key="Article.createDate"/></TD>
         <TD  width="35%" class="contain">
         <html:hidden property="dto(createDateTime)" />
         ${app2:getDateWithTimeZone(articleForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
         </TD>
         <TD class="label" width="16%" ><fmt:message key="Article.changeDate"/></TD>
         <TD class="contain" width="34%" >
         <html:hidden property="dto(updateDateTime)" />
         ${app2:getDateWithTimeZone(articleForm.dtoMap.updateDateTime, timeZone, dateTimePattern)}
        </TD>
    </TR>
    <TR>
         <TD  width="15%" class="label"><fmt:message key="Article.lastVisit"/></TD>
         <TD  width="35%" class="contain">
         <html:hidden property="dto(visitDateTime)" />
         ${app2:getDateWithTimeZone(articleForm.dtoMap.visitDateTime, timeZone, dateTimePattern)}
         </TD>
         <TD class="label" width="16%" ><fmt:message key="Article.readyBy"/></TD>
         <TD class="contain" width="34%" >
            <html:hidden property="dto(readyBy)" write="true" />
              (<fmt:message key="Article.users"/>)
        </TD>
    </TR>
    <TR>
         <TD  width="15%" class="label"><fmt:message key="Article.number"/></TD>
         <TD  width="35%" class="contain">
         <html:hidden property="dto(number)" write="true" />
         </TD>
         <TD class="topLabel" width="16%" ><fmt:message key="Article.averageRate"/></TD>
         <TD class="containTop" width="34%" >

<c:if test="${articleForm.dtoMap.position >= 0}">
    <c:forEach begin="0"  end="${articleForm.dtoMap.position}"  >
            <html:img src="${baselayout}/img/amarillo.gif" border="0"/>
    </c:forEach>
</c:if>
<%--
<c:if test="${articleForm.dtoMap.position == -1}">
    0
</c:if>
--%>
        </TD>
    </TR>
 </c:if>
 
 <c:if test="${articleForm.dtoMap.question == 'true'}">
 <html:hidden property="dto(rootQuestionId)"/>

    <tr>
         <td class="label" ><fmt:message key="Question.summary"/></TD>
         <td class="contain" colspan="3">
             <html:hidden property="dto(summary)" write="true" />
        </td>
     </tr>
        <tr>
         <td class="label" ><fmt:message key="Question.detail"/></TD>
         <td class="contain" colspan="3" >
             <html:hidden property="dto(detail)" write="true" />
        </td>
        </tr>

    <TR>
     <TD  width="15%" class="label"><fmt:message   key="Article.ownerName"/></TD>
         <TD  width="35%" class="contain">
           <html:hidden property="dto(ownerQuestionName)" write="true" />
         </TD>
         <TD  width="15%" class="label"><fmt:message key="Article.createDate"/></TD>
         <TD  width="35%" class="contain">
         ${app2:getDateWithTimeZone(articleForm.dtoMap.createDateTimeQuestion, timeZone, dateTimePattern)}
         </TD>
    </TR>
 </c:if>
 <TR>
     <TD  width="15%" class="label"><fmt:message   key="Article.title"/></TD>
     <TD  width="35%" class="contain">
     <app:text property="dto(articleTitle)" styleClass="middleText" maxlength="45" tabindex="4" view="${op == 'delete'}"/>
     </TD>
     <TD class="topLabel" width="16%" ><fmt:message   key="Article.categoryName"/></TD>
     <TD class="containTop" width="34%" >

<c:choose>
  <c:when test="${'delete' != op}">
        <fanta:select property="dto(categoryId)" listName="articleCategoryList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                      tabIndex="5">
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
     <TD class="label" ><fmt:message   key="Article.keywords"/></TD>
     <TD class="contain">
        <app:text property="dto(keywords)" styleClass="middleText" maxlength="40" tabindex="6" view="${op == 'delete'}"/>
      </TD>

     <TD class="label" ><fmt:message key="Article.productName"/></TD>
     <TD class="contain">

         <html:hidden property="dto(productId)" styleId="field_key" />
         <html:hidden property="dto(1)" styleId="field_versionNumber"  />
         <html:hidden property="dto(2)" styleId="field_unitId"  />
         <html:hidden property="dto(3)" styleId="field_price"  />

         <app:text property="dto(productName)" styleId="field_name" styleClass="mediumText" maxlength="40" readonly="true"  view="${op == 'delete'}" tabindex="7"/>
         <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" hide="${op == 'delete'}" />
         <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" hide="${op == 'delete'}"/>

     </TD>
 </tr>

<%--access rights security--%>
 <tr>
     <td colspan="4" class="title">
         <fmt:message key="Article.accessRight.title"/>
     </td>
 </tr>
 <tr>
     <td class="label" style="vertical-align:top">
         <fmt:message key="Article.accessRight.userGroupsWithAccess"/>
     </td>
     <td class="contain" colspan="3">
         <html:hidden property="dto(articleAccessUserGroupIds)" styleId="articleUserGroupIds_Id"/>

         ${app2:buildArticleUserGroupAccessRightValues(articleForm.dtoMap['articleAccessUserGroupIds'], pageContext.request)}
         <c:set var="selectedElms" value="${selectedArticleUserGroupList}"/>
         <c:set var="availableElms" value="${availableArticleUserGroupList}"/>

         <table cellpadding="0" cellspacing="0" border="0">
             <tr>
                 <td>
                     <fmt:message key="common.selected"/>
                     <br>
                     <html:select property="dto(selectedUserGroup)"
                                  styleId="selectedUserGroup_Id"
                                  multiple="true"
                                  styleClass="middleMultipleSelect"
                                  tabindex="11"
                                  disabled="${op == 'delete'}">

                         <html:options collection="selectedElms" property="value" labelProperty="label"/>
                     </html:select>
                 </td>
                 <td>
                     <html:button property="select"
                                  onclick="javascript:moveOptionSelected('availableUserGroup_Id','selectedUserGroup_Id');"
                                  styleClass="adminLeftArrow" titleKey="Common.add" disabled="${op == 'delete'}">&nbsp;
                     </html:button>
                     <br>
                     <br>
                     <html:button property="select"
                                  onclick="javascript:moveOptionSelected('selectedUserGroup_Id','availableUserGroup_Id');"
                                  styleClass="adminRighttArrow" titleKey="Common.delete" disabled="${op == 'delete'}">&nbsp;
                     </html:button>
                 </td>
                 <td>
                     <fmt:message key="common.available"/>
                     <br>
                     <html:select property="dto(availableUserGroup)"
                                  styleId="availableUserGroup_Id"
                                  multiple="true"
                                  styleClass="middleMultipleSelect"
                                  tabindex="12"
                                  disabled="${op == 'delete'}">

                         <html:options collection="availableElms" property="value" labelProperty="label"/>
                     </html:select>
                 </td>
             </tr>
         </table>
         <script type="text/javascript">
             fillUserArticleAccessRight();
         </script>
     </td>
 </tr>

 <tr>
     <TD class="contain" colspan="4">
     <c:choose>
            <c:when test="${op != 'delete'}">
                <tags:initTinyMCE4 textAreaId="description_text"/>
                <html:textarea property="dto(content)" tabindex="15" styleClass="mediumDetailHigh" styleId="description_text"
                 readonly="${true}" style="height:240px;width:100%"/>
            </c:when>
            <c:otherwise>
                <c:set var="ad" value="${articleForm.dtoMap.content}" scope="session"/>
                <iframe name="frame2" src="<c:url value="/common/support/PreviusDetail.jsp?var=ad" />"
                        style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes" frameborder="1">
                </iframe>
            </c:otherwise>
        </c:choose>
    </TD>
 </tr>
<TR>
     <TD class="button" colspan="4" nowrap>
     <%--for Question functionality--%>

    <app2:securitySubmit operation="${op}" functionality="ARTICLE" styleClass="button" tabindex="16">
        ${button}
    </app2:securitySubmit>

<c:if test="${'update' == op && isOwner}">
<app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
    <html:button  onclick="location.href='${url}'" styleClass="button" property="dto(save)" tabindex="17" >
        <fmt:message  key="Common.delete"/>
    </html:button>
</app2:checkAccessRight>

</c:if>
<c:if test="${'update' != op}">
 <html:cancel  styleClass="button" tabindex="18">
    <fmt:message    key="Common.cancel"/>
 </html:cancel>
</c:if>
      </TD>
 </TR>
</html:form>
<app2:checkAccessRight functionality="ARTICLERATING" permission="UPDATE">
      <c:if test="${!isOwner && 'update' == op}">
          <tr align="left" id="ratingArea" ${!(articleForm.dtoMap.voted !='true' && op == 'update')? "style=\"display: none;\"":""}>
          <html:form action="/Article/Rating/Update.do" >
                 <html:hidden property="dto(rating)" value="true"/>
                 <html:hidden property="dto(articleId)" />
                 <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
             <TD colspan="4" align="left"><fmt:message key="Article.rating"/><br>
                <fmt:message key="Article.poor"/>
                    <html:radio property="dto(rate)" value="1" styleClass="radio" disabled="${'delete' == op}" tabindex="19" />
                    <html:radio property="dto(rate)" value="2" styleClass="radio" disabled="${'delete' == op}" tabindex="20"/>
                    <html:radio property="dto(rate)" value="3" styleClass="radio" disabled="${'delete' == op}" tabindex="21"/>
                    <html:radio property="dto(rate)" value="4" styleClass="radio" disabled="${'delete' == op}" tabindex="22"/>
                    <html:radio property="dto(rate)" value="5" styleClass="radio" disabled="${'delete' == op}" tabindex="23"/>
                <fmt:message key="Article.excellent"/><br>
                <app2:securitySubmit operation="update" functionality="ARTICLERATING" styleClass="button" tabindex="24">
                    <fmt:message key="Article.RateArticle"/>    
                </app2:securitySubmit>
             </TD>
             </html:form>
          </tr>
     </c:if>
 </app2:checkAccessRight>
 </table>
