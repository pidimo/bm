<%@ include file="/Includes.jsp" %>

 <c:set var="context" value="<%=request.getContextPath()%>"/>


<html:form action="${action}" styleClass="form-horizontal">

 <html:hidden property="dto(op)" value="${op}"/>
 <fmt:message   var="datePattern" key="datePattern"/>
 <fmt:message   var="dateTimePattern" key="dateTimePattern"/>
<c:if test="${'delete' == op}">
<html:hidden property="dto(version)"/>
<html:hidden property="dto(relatedArticleId)" />
</c:if>

     <div class="${app2:getFormButtonWrapperClasses()}">

     <c:if test="${param.operation != 'view'}">
        <app2:securitySubmit operation="${op}" functionality="ARTICLERELATED" styleClass="button ${app2:getFormButtonClasses()}" tabindex="9">
            ${button}
        </app2:securitySubmit>
    </c:if>
             <html:cancel  styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="10" >
                <fmt:message    key="Common.cancel"/>
            </html:cancel>
      </div>

    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message   key="Article.ownerName"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(ownerName)" write="true" />
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message   key="Article.changeName"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(changeName)" write="true" />
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.createDate"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(createDateTime)" />
                            ${app2:getDateWithTimeZone(relationForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.changeDate"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(updateDateTime)" />
                            ${app2:getDateWithTimeZone(relationForm.dtoMap.updateDateTime, timeZone, dateTimePattern)}
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.lastVisit"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(visitDateTime)" />
                            ${app2:getDateWithTimeZone(relationForm.dtoMap.visitDateTime, timeZone, dateTimePattern)}
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.readyBy"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(readyBy)" write="true" />
                        (<fmt:message key="Article.users"/>)
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.number"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <html:hidden property="dto(number)" write="true" />
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Article.averageRate"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
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
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message   key="Article.title"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text property="dto(articleTitle)" styleClass="middleText" maxlength="40" tabindex="1" view="${op == 'delete'}"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message   key="Article.categoryName"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <fanta:select property="dto(categoryId)" listName="articleCategorySimpleList"
                                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                      readOnly="true">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                        </fanta:select>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" >
                        <fmt:message   key="Article.keywords"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <app:text property="dto(keywords)" styleClass="middleText" maxlength="50" tabindex="6" view="${op == 'delete'}"/>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" >
                        <fmt:message key="Article.productName"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <fanta:select property="dto(productId)" listName="productList" labelProperty="name"  tabIndex="5"
                                      valueProperty="id" firstEmpty="true" styleClass="mediumSelect ${app2:getFormSelectClasses()}" readOnly="${'delete' == op}"  module="/products">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="embed-responsive col-xs-12 well" style="height: 240px; !important;">
                        <c:set var="ad" value="${relationForm.dtoMap.content}" scope="session"/>
                        <iframe class="embed-responsive-item" name="frame2" src="<c:url value="/WEB-INF/jsp/support/PreviusDetail.jsp?var=ad" />"
                                style="width : 100%;height: 240px; !important; background-color:#ffffff;" scrolling="yes" frameborder="1">
                        </iframe>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>

     <div class="${app2:getFormButtonWrapperClasses()}">
<c:if test="${param.operation != 'view'}">
<app2:securitySubmit operation="${op}" functionality="ARTICLERELATED" styleClass="button ${app2:getFormButtonClasses()}" tabindex="9">
    ${button}
</app2:securitySubmit>
    </c:if>
         <c:if test="${'update' != op}">
             <html:cancel  styleClass="button ${app2:getFormButtonCancelClasses()}" tabindex="10" >
                <fmt:message    key="Common.cancel"/>
            </html:cancel>
         </c:if>
      </div>
 </html:form>

