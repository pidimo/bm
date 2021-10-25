<%@ include file="/Includes.jsp" %>

 <c:set var="context" value="<%=request.getContextPath()%>"/>

 </br>
 <table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
 <tr>
 <td>
<table border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">
<html:form action="${action}" >

 <html:hidden property="dto(op)" value="${op}"/>
 <fmt:message   var="datePattern" key="datePattern"/>
 <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<c:if test="${'delete' == op}">
<html:hidden property="dto(version)"/>
<html:hidden property="dto(relatedArticleId)" />
</c:if>
<TR>
     <TD class="button" colspan="4" nowrap>

     <c:if test="${param.operation != 'view'}">
<app2:securitySubmit operation="${op}" functionality="ARTICLERELATED" styleClass="button" tabindex="9">
    ${button}
</app2:securitySubmit>
    </c:if>
             <html:cancel  styleClass="button" tabindex="10" >
                <fmt:message    key="Common.cancel"/>
            </html:cancel>
      </TD>
 </TR>
 <TR>
     <TD colspan="4" class="title">
     <c:out value="${title}"/>
     </TD>
 </TR>
     <TR>
         <TD  width="15%" class="label"><fmt:message   key="Article.ownerName"/></TD>
         <TD  width="35%" class="contain">
           <html:hidden property="dto(ownerName)" write="true" />
         </TD>
         <TD class="label" width="16%" ><fmt:message   key="Article.changeName"/></TD>
         <TD class="contain" width="34%" >
              <html:hidden property="dto(changeName)" write="true" />
        </TD>
    </TR>
    <TR>
         <TD  width="15%" class="label"><fmt:message key="Article.createDate"/></TD>
         <TD  width="35%" class="contain">
         <html:hidden property="dto(createDateTime)" />
         ${app2:getDateWithTimeZone(relationForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
         </TD>
         <TD class="label" width="16%" ><fmt:message key="Article.changeDate"/></TD>
         <TD class="contain" width="34%" >
         <html:hidden property="dto(updateDateTime)" />
         ${app2:getDateWithTimeZone(relationForm.dtoMap.updateDateTime, timeZone, dateTimePattern)}
        </TD>
    </TR>
    <TR>
         <TD  width="15%" class="label"><fmt:message key="Article.lastVisit"/></TD>
         <TD  width="35%" class="contain">
         <html:hidden property="dto(visitDateTime)" />
         ${app2:getDateWithTimeZone(relationForm.dtoMap.visitDateTime, timeZone, dateTimePattern)}
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
                <c:if test="${relationForm.dtoMap.position >= 0}">
                    <c:forEach begin="0"  end="${relationForm.dtoMap.position}"  >
                            <html:img src="${baselayout}/img/amarillo.gif" border="0"/>
                    </c:forEach>
                </c:if>
                <c:if test="${relationForm.dtoMap.noPosition >= 0}">
                <c:forEach begin="0"  end="${relationForm.dtoMap.noPosition}"  >
                        <html:img src="${baselayout}/img/negro.gif" border="0"/>
                </c:forEach>
                </c:if>
        </TD>
    </TR>
 <TR>

     <TD  width="15%" class="label"><fmt:message   key="Article.title"/></TD>
     <TD  width="35%" class="contain">
     <app:text property="dto(articleTitle)" styleClass="middleText" maxlength="40" tabindex="1" view="${op == 'delete'}"/>
     </TD>
     <TD class="topLabel" width="16%" ><fmt:message   key="Article.categoryName"/></TD>
     <TD class="containTop" width="34%" >
<fanta:select property="dto(categoryId)" listName="articleCategorySimpleList"
              labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
              readOnly="true">
<fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
</fanta:select>
     </TD>
 </TR>
  <tr>
      <TD class="label" ><fmt:message   key="Article.keywords"/></TD>
      <TD class="contain">
         <app:text property="dto(keywords)" styleClass="middleText" maxlength="50" tabindex="6" view="${op == 'delete'}"/>
       </TD>

      <TD class="label" ><fmt:message key="Article.productName"/></TD>
      <TD class="contain">
          <fanta:select property="dto(productId)" listName="productList" labelProperty="name"  tabIndex="5"
                        valueProperty="id" firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}"  module="/products">
              <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
          </fanta:select>
      </TD>
  </tr>

 <tr>
     <TD class="contain" colspan="4">
     <c:set var="ad" value="${relationForm.dtoMap.content}" scope="session"/>
                <iframe name="frame2" src="<c:url value="/common/support/PreviusDetail.jsp?var=ad" />"
                        style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes" frameborder="1">
                </iframe>
    </TD>
 </tr>
 </table>
</td></tr>
<tr><td>
 <table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
 <TR>
     <TD class="button" nowrap>
<c:if test="${param.operation != 'view'}">
<app2:securitySubmit operation="${op}" functionality="ARTICLERELATED" styleClass="button" tabindex="9">
    ${button}
</app2:securitySubmit>
    </c:if>
         <c:if test="${'update' != op}">
             <html:cancel  styleClass="button" tabindex="10" >
                <fmt:message    key="Common.cancel"/>
            </html:cancel>
         </c:if>
      </TD>
 </TR>
 </html:form>
 </table>
 </td>
 </tr>
 </table>
