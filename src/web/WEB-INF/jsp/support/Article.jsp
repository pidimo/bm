<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>
<%--<tags:initSelectPopup/>--%>
<%--<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>--%>

<c:set var="context" value="<%=request.getContextPath()%>"/>

<script language="JavaScript">

    function isRating() {
        $("#ratingArea").hide();
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

        $("#selectedUserGroup_Id").find("option").each(function () {
            if (userGroupIds.length > 0) {
                userGroupIds = userGroupIds + ",";
            }
            userGroupIds = userGroupIds + $(this).val();
        });

        //set in hidden dto property
        $("#articleUserGroupIds_Id").val(userGroupIds);
    }

</script>

<div class="${app2:getFormWrapperTwoColumns()}">
    <%--<table border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="">--%>
    <html:form action="${action}" styleClass="form-horizontal">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(rating)" value="false"/>
        <html:hidden property="dto(position)"/>
        <html:hidden property="dto(noPosition)"/>
        <html:hidden property="dto(voted)"/>
        <html:hidden property="dto(question)"/>
        <html:hidden property="dto(createDateTimeQuestion)"/>
        <html:hidden property="dto(ownerQuestionName)"/>
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:message var="dateTimePattern" key="dateTimePattern"/>
        <c:set var="isOwner" value="true"/>

        <c:if test="${'update' == op || 'delete' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(articleId)"/>
            <html:hidden property="dto(createUserId)"/>
            <c:set var="isOwner"
                   value="${sessionScope.user.valueMap['userId'] == articleForm.dtoMap.createUserId }"/>
        </c:if>

        <c:if test="${'create' == op}">
            <c:set var="isOwner" value="true"/>
            <html:hidden property="dto(createUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">

                <%--for Question functionality--%>
            <app2:securitySubmit operation="${op}" functionality="ARTICLE"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="1">
                ${button}
            </app2:securitySubmit>

            <c:if test="${'update' == op && isOwner}">

                <app:url var="url"
                         value="/Article/Forward/Delete.do?articleId=${param.articleId}&dto(articleId)=${param.articleId}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>
                <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)"
                                 tabindex="2">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <c:if test="${'update' != op}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="3">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:if test="${op != 'create'}">
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.ownerName"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(ownerName)" write="true"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.changeName"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(changeName)" write="true"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.createDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(createDateTime)"/>
                                    ${app2:getDateWithTimeZone(articleForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.changeDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(updateDateTime)"/>
                                    ${app2:getDateWithTimeZone(articleForm.dtoMap.updateDateTime, timeZone, dateTimePattern)}
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.lastVisit"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(visitDateTime)"/>
                                    ${app2:getDateWithTimeZone(articleForm.dtoMap.visitDateTime, timeZone, dateTimePattern)}
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.readyBy"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(readyBy)" write="true"/>
                                (<fmt:message key="Article.users"/>)
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.number"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(number)" write="true"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.averageRate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <c:if test="${articleForm.dtoMap.position >= 0}">
                                    <c:forEach begin="0" end="${articleForm.dtoMap.position}">
                                        <html:img src="${baselayout}/img/amarillo.gif" border="0"/>
                                    </c:forEach>
                                </c:if>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <c:if test="${articleForm.dtoMap.question == 'true'}">
                    <html:hidden property="dto(rootQuestionId)"/>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Question.summary"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(summary)" write="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Question.detail"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(detail)" write="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.ownerName"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <html:hidden property="dto(ownerQuestionName)" write="true"/>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Article.createDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    ${app2:getDateWithTimeZone(articleForm.dtoMap.createDateTimeQuestion, timeZone, dateTimePattern)}
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="articleTitle_id">
                            <fmt:message key="Article.title"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(articleTitle)"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="45"
                                      styleId="articleTitle_id"
                                      tabindex="4"
                                      view="${op == 'delete'}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="categoryId_id">
                            <fmt:message key="Article.categoryName"/>
                        </label>

                        <c:choose>
                            <c:when test="${'delete' != op}">
                                <div class="${app2:getFormContainClassesTwoColumns(false)}">
                                    <fanta:select property="dto(categoryId)" listName="articleCategoryList"
                                                  firstEmpty="true"
                                                  labelProperty="name"
                                                  valueProperty="id"
                                                  styleId="categoryId_id"
                                                  module="/catalogs"
                                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                                  tabIndex="5">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                        <fanta:tree columnId="id" columnParentId="parentCategoryId"
                                                    separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                    <fanta:select property="dto(categoryId)"
                                                  listName="articleCategorySimpleList"
                                                  labelProperty="name"
                                                  styleId="categoryId_id"
                                                  valueProperty="id"
                                                  module="/catalogs"
                                                  tabIndex="5"
                                                  styleClass="middleSelect ${app2:getFormSelectClasses()}"
                                                  readOnly="true">
                                        <fanta:parameter field="companyId"
                                                         value="${sessionScope.user.valueMap['companyId']}"/>
                                    </fanta:select>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="keywords_id">
                            <fmt:message key="Article.keywords"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(keywords)"
                                      styleClass="middleText ${app2:getFormInputClasses()}"
                                      maxlength="40"
                                      styleId="keywords_id"
                                      tabindex="6"
                                      view="${op == 'delete'}"/>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="field_name">
                            <fmt:message key="Article.productName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="input-group">
                                <app:text property="dto(productName)"
                                          styleId="field_name"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"
                                          maxlength="40"
                                          readonly="true" view="${op == 'delete'}" tabindex="7"/>

                                <html:hidden property="dto(productId)" styleId="field_key"/>
                                <html:hidden property="dto(1)" styleId="field_versionNumber"/>
                                <html:hidden property="dto(2)" styleId="field_unitId"/>
                                <html:hidden property="dto(3)" styleId="field_price"/>

                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/products/SearchProduct.do"
                                                               name="SearchProduct"
                                                               titleKey="Common.search"
                                                               hide="${op == 'delete'}"
                                                               isLargeModal="true"
                                                               tabindex="7"
                                                               modalTitleKey="Product.Title.SimpleSearch"
                                                               styleId="selectPopupProduct_id"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="field_key"
                                                                    nameFieldId="field_name"
                                                                    tabindex="7"
                                                                    titleKey="Common.clear"
                                                                    hide="${op == 'delete'}"/>
                                </span>
                            </div>
                            <span class=" glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>


                <legend class="title">
                    <fmt:message key="Article.accessRight.title"/>
                </legend>

                    <%--access rights security--%>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="${category.categoryId}_id">
                        <fmt:message key="Article.accessRight.userGroupsWithAccess"/>
                    </label>

                    <html:hidden property="dto(articleAccessUserGroupIds)" styleId="articleUserGroupIds_Id"/>

                        ${app2:buildArticleUserGroupAccessRightValues(articleForm.dtoMap['articleAccessUserGroupIds'], pageContext.request)}

                    <c:set var="selectedElms" value="${selectedArticleUserGroupList}"/>
                    <c:set var="availableElms" value="${availableArticleUserGroupList}"/>

                    <div class="${app2:getFormContainRenderCategory(false)}">
                        <table class="col-xs-12">
                            <tr>
                                <td width="47.5%">
                                    <fmt:message key="common.selected"/>
                                </td>
                                <td width="5%"></td>
                                <td width="47.5%">
                                    <fmt:message key="common.available"/>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <html:select property="dto(selectedUserGroup)"
                                                 styleId="selectedUserGroup_Id"
                                                 multiple="true"
                                                 styleClass="middleMultipleSelect ${app2:getFormInputClasses()}"
                                                 tabindex="11"
                                                 disabled="${op == 'delete'}">

                                        <html:options collection="selectedElms" property="value" labelProperty="label"/>
                                    </html:select>
                                </td>

                                <td>
                                    <c:set var="addIcon" value="<"/>
                                    <html:button property="select"
                                                 onclick="javascript:moveOptionSelected('availableUserGroup_Id','selectedUserGroup_Id');"
                                                 styleClass="${app2:getFormButtonClasses()}"
                                                 titleKey="Common.add"
                                                 disabled="${op == 'delete'}">
                                        ${addIcon}
                                    </html:button>
                                    <br>
                                    <br>
                                    <c:set var="deleteIcon" value=">"/>
                                    <html:button property="select"
                                                 onclick="javascript:moveOptionSelected('selectedUserGroup_Id','availableUserGroup_Id');"
                                                 styleClass="${app2:getFormButtonClasses()}"
                                                 titleKey="Common.delete"
                                                 disabled="${op == 'delete'}">
                                        ${deleteIcon}
                                    </html:button>
                                </td>

                                <td>
                                    <html:select property="dto(availableUserGroup)"
                                                 styleId="availableUserGroup_Id"
                                                 multiple="true"
                                                 styleClass="middleMultipleSelect ${app2:getFormInputClasses()}"
                                                 tabindex="12"
                                                 disabled="${op == 'delete'}">

                                        <html:options collection="availableElms" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </td>
                            </tr>
                        </table>
                        <script type="text/javascript">
                            fillUserArticleAccessRight();
                        </script>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${op != 'delete'}">
                        <tags:initTinyMCE4 textAreaId="description_text"/>
                        <html:textarea property="dto(content)"
                                       tabindex="15"
                                       styleClass="mediumDetailHigh ${app2:getFormInputClasses()}"
                                       styleId="description_text"
                                       readonly="${true}" style="height:240px;width:100%"/>
                    </c:when>

                    <c:otherwise>
                        <c:set var="ad" value="${articleForm.dtoMap.content}" scope="session"/>
                        <iframe name="frame2" src="<c:url value="/WEB-INF/jsp/support/PreviusDetail.jsp?var=ad" />"
                                style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes"
                                frameborder="1">
                        </iframe>
                    </c:otherwise>
                </c:choose>

            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
                <%--for Question functionality--%>
            <app2:securitySubmit operation="${op}" functionality="ARTICLE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="16">
                ${button}
            </app2:securitySubmit>

            <c:if test="${'update' == op && isOwner}">
                <app2:checkAccessRight functionality="ARTICLE" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)"
                                 tabindex="17">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>

            </c:if>

            <c:if test="${'update' != op}">
                <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="18">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </c:if>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="ARTICLERATING" permission="UPDATE">
        <c:if test="${!isOwner && 'update' == op}">
            <div id="ratingArea" ${!(articleForm.dtoMap.voted !='true' && op == 'update')? "style=\"display: none;\"":""}>

                <html:form action="/Article/Rating/Update.do">
                    <html:hidden property="dto(rating)" value="true"/>
                    <html:hidden property="dto(articleId)"/>
                    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

                    <%--<TD colspan="4" align="left">--%>
                    <fmt:message key="Article.rating"/>
                    <table>
                        <tr>
                            <td class="col-xs-6">
                                <div class="table-responsive row">
                                    <table class="table">
                                        <tr>
                                            <td>
                                                <strong>
                                                    <fmt:message key="Article.poor"/>
                                                </strong>
                                            </td>
                                            <td>
                                                <div class="radio radio-default radio-inline">
                                                    <html:radio property="dto(rate)" value="1" styleClass="radio"
                                                                disabled="${'delete' == op}"
                                                                tabindex="19"/>
                                                    <label></label>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="radio radio-default radio-inline">
                                                    <html:radio property="dto(rate)" value="2" styleClass="radio"
                                                                disabled="${'delete' == op}"
                                                                tabindex="20"/>
                                                    <label></label>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="radio radio-default radio-inline">
                                                    <html:radio property="dto(rate)" value="3" styleClass="radio"
                                                                disabled="${'delete' == op}"
                                                                tabindex="21"/>
                                                    <label></label>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="radio radio-default radio-inline">
                                                    <html:radio property="dto(rate)" value="4" styleClass="radio"
                                                                disabled="${'delete' == op}"
                                                                tabindex="22"/>
                                                    <label></label>
                                                </div>
                                            </td>
                                            <td>
                                                <div class="radio radio-default radio-inline">
                                                    <html:radio property="dto(rate)" value="5" styleClass="radio"
                                                                disabled="${'delete' == op}"
                                                                tabindex="23"/>
                                                    <label></label>
                                                </div>
                                            </td>
                                            <td>
                                                <strong>
                                                    <fmt:message key="Article.excellent"/>
                                                </strong>
                                            </td>
                                        </tr>

                                    </table>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <app2:securitySubmit operation="update" functionality="ARTICLERATING"
                                                     styleClass="${app2:getFormButtonClasses()}"
                                                     tabindex="24">
                                    <fmt:message key="Article.RateArticle"/>
                                </app2:securitySubmit>
                            </td>
                        </tr>
                    </table>

                </html:form>
                <br/>

            </div>
        </c:if>
    </app2:checkAccessRight>
</div>

<tags:jQueryValidation formName="articleForm"/>