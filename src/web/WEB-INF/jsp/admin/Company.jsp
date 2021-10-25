<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>

<script type="text/javascript">
    function updateCopyConfigurationSelect(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        document.getElementById("languageSelected").value = opt.value;
        document.getElementById("updateLanguage").value = 'true';
        document.getElementById("companyFormId").submit();
    }

    function updateTimeZoneSelect(obj) {
        var idx = obj.selectedIndex;
        var opt = obj.options[idx];
        document.getElementById("templateSelected").value = opt.value;
        document.getElementById("updateLanguage").value = 'true';
        document.getElementById("companyFormId").submit();
    }
</script>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#finishLicense").val($("#startLicense").val());
        $("#finishLicenseMobile").val($("#startLicenseMobile").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="companyTemplateTypes" value="${app2:getCompanyTemplateType(pageContext.request)}"/>
<html:form action="${action}" styleId="companyFormId" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(updateLanguage)" value="" styleId="updateLanguage"/>
        <html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
        <html:hidden property="dto(templateSelected)" styleId="templateSelected"/>
        <c:if test="${('update' == op)}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(companyId)"/>
        </c:if>
        <html:hidden property="dto(creation_)"/>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="COMPANY"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Company.information"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Company.active"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(active)" styleId="active_id" value="true"
                                               styleClass="adminCheckBox"/>
                                <label for="active_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="name1_id">
                        <fmt:message key="Company.name"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op!='create')}">
                        <app:text property="dto(name1)" styleId="name1_id" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                        <%--<td >&nbsp;</td>--%>
                    <div class="${app2:getFormContainRenderCategory(op!='create')} col-sm-offset-4">
                        <app:text property="dto(name2)" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60" tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                        <%--<td class="label">&nbsp;</td>--%>
                    <div class="${app2:getFormContainRenderCategory(op!='create')} col-sm-offset-4">
                        <app:text property="dto(name3)" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60" tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="companyCreateLogin_id">
                        <fmt:message key="Company.login"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op=='update')}">

                        <c:set var="userIsRoot" value="${app2:isRootUser(sessionScope.user.valueMap['userId'])}" scope="request"/>

                        <app:text property="dto(companyCreateLogin)" styleId="companyCreateLogin_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="startLicense">
                        <fmt:message key="Company.startLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op=='delete')}">
                        <div class="input-group date">
                            <fmt:message var="datePattern" key="datePattern"/>
                            <app:dateText property="dto(startLicenseDate)" view="${op=='delete'}"
                                          maxlength="10"
                                          styleId="startLicense"
                                          calendarPicker="true" datePatternKey="${datePattern}"
                                          tabindex="5"
                                          onchange="changeStartDateValue()"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()}"
                                          convert="true"
                                          currentDateTimeZone="${currentDateTimeZone}"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="finishLicense">
                        <fmt:message key="Company.finishLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op=='delete')}">
                        <div class="input-group date">
                            <app:dateText property="dto(finishLicenseDate)" view="${op=='delete'}"
                                          maxlength="10"
                                          styleId="finishLicense"
                                          calendarPicker="true" datePatternKey="${datePattern}"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()}"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          convert="true"
                                          tabindex="6"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="companyType_id">
                        <fmt:message key="Company.companyType"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <c:set var="companyTypes" value="${app2:getCompanyTypes(pageContext.request)}"/>
                        <html:select property="dto(companyType)" styleId="companyType_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     tabindex="7">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="companyTypes" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="usersAllowed_id">
                        <fmt:message key="Company.usersAllowed"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <app:text property="dto(usersAllowed)" styleId="usersAllowed_id"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4"
                                  tabindex="8"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="favoriteLanguage_id">
                        <fmt:message key="Company.defaultUILanguage"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <c:choose>
                            <c:when test="${op == 'create'}">
                                <html:select property="dto(favoriteLanguage)" styleId="favoriteLanguage_id"
                                             styleClass="${app2:getFormSelectClasses()} shortSelect"
                                             tabindex="9"
                                             onchange="javascript:updateCopyConfigurationSelect(this);">
                                    <html:option value="">&nbsp;</html:option>
                                    <html:options collection="systemLanguageList" property="value"
                                                  labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <html:select property="dto(favoriteLanguage)" styleId="favoriteLanguage_id "
                                             styleClass="${app2:getFormSelectClasses()} shortSelect"
                                             tabindex="9">
                                    <html:option value="">&nbsp;</html:option>
                                    <html:options collection="systemLanguageList" property="value"
                                                  labelProperty="label"/>
                                </html:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="maxMaxAttachSize_id">
                        <fmt:message key="Company.maxMaxAttachSize"/>(<fmt:message key="Common.megabytes"/>)
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <app:text property="dto(maxMaxAttachSize)" styleId="maxMaxAttachSize_id"
                                  styleClass="${app2:getFormInputClasses()} numberText" maxlength="2"
                                  tabindex="10"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${op == 'create'}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="templateCampaignId_id">
                            <fmt:message key="Company.copyConfigurationFrom"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <fanta:select property="dto(templateCampaignId)" styleId="templateCampaignId_id"
                                          listName="templateCompanyList"
                                          labelProperty="companyName"
                                          valueProperty="templateCompanyId"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          tabIndex="10"
                                          onChange="javascript:updateTimeZoneSelect(this);">
                                <fanta:parameter field="language"
                                                 value="${companyForm.dtoMap['languageSelected']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="timeZone_id">
                        <fmt:message key="Company.timeZone"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <c:set var="timeZonesConstants"
                               value="${app2:getTimeZones(pageContext.request)}"/>
                        <html:select property="dto(timeZone)" styleId="timeZone_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="11">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="timeZonesConstants" property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="copyTemplate_id">
                        <fmt:message key="Company.setAsTemplate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <html:select property="dto(copyTemplate)" styleId="copyTemplate_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     tabindex="12">
                            <html:option value="">&nbsp;</html:option>
                            <html:options collection="companyTemplateTypes" property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${('create' != op)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="Common.creationDate"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <html:hidden property="dto(creation_Date)" write="true"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </c:if>
                <!--     user - Information      -->
                <!--<td colspan="2" class="title">-->
                <legend class="title">
                    <fmt:message key="Company.userInfo"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="rootName1_id">
                        <fmt:message key="Company.rootName1"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op!='create')}">
                        <app:text property="dto(rootName1)" styleId="rootName1_id" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  tabindex="13"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="rootName2_id">
                        <fmt:message key="Company.rootName2"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op!='create')}">
                        <app:text property="dto(rootName2)" styleId="rootName2_id" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  tabindex="14"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="userName_id">
                        <fmt:message key="Company.userName"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op!='create')}">
                        <app:text property="dto(userName)" styleId="userName_id" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20" tabindex="15"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="userPassword_id">
                        <fmt:message key="Company.userPassword"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <html:password property="dto(userPassword)" styleId="userPassword_id"
                                       styleClass="${app2:getFormInputClasses()} mediumText"
                                       maxlength="20"
                                       tabindex="16"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="rePassword_id">
                        <fmt:message key="Company.reuserPassword"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <html:password property="dto(rePassword)" styleId="rePassword_id"
                                       styleClass="${app2:getFormInputClasses()} mediumText" maxlength="20"
                                       tabindex="17"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="email_id">
                        <fmt:message key="Company.rootMail"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op!='create')}">
                        <app:text property="dto(email)" styleId="email_id" view="${op!='create'}"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60" tabindex="18"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${op == 'create'}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="Company.sentNotification"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(sentNotification)" styleId="sentNotification_id"
                                                   value="true"
                                                   styleClass="adminCheckBox"
                                                   tabindex="19"/>
                                    <label for="sentNotification_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>


                <legend class="title">
                    <fmt:message key="Company.modules"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <div class="${app2:getFormContainRenderCategory(view)} col-sm-offset-4">
                        <label class="control-label">
                            <fmt:message key="Company.entriesLimit"/>
                        </label>
                    </div>
                </div>
                <c:forEach var="module" items="${app2:getSystemModules(pageContext.request)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="radiocheck pull-left col-xs-12 col-sm-1">
                            <div class="checkbox checkbox-default">
                                <html:multibox property="modules" styleId="modules_id" value="${module.moduleId}"
                                               tabindex="20"/>
                                <label for="modules_id"></label>
                            </div>
                        </div>
                        <label class="control-label col-xs-12 col-sm-3"
                               for="${fn:replace(module.nameKey,'.','_')}_${module.moduleId})_id">
                            <fmt:message key="${module.nameKey}"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(view)}">
                            <app:text
                                    property="dto(${fn:replace(module.nameKey,'.','_')}_${module.moduleId})"
                                    styleId="${fn:replace(module.nameKey,'.','_')}_${module.moduleId})_id"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    maxlength="7" tabindex="20"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:forEach>

                <legend class="title">
                    <fmt:message key="Company.mobileAccess"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Company.mobile.active"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(mobileActive)" styleId="mobileActive_id"
                                               disabled="${op == 'delete'}"
                                               tabindex="21"/>
                                <label for="mobileActive_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="mobileUserAllowed_id">
                        <fmt:message key="Company.mobile.usersAllowed"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <app:text property="dto(mobileUserAllowed)" styleId="mobileUserAllowed_id"
                                  view="${op == 'delete'}"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4" tabindex="22"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="startLicenseMobile">
                        <fmt:message key="Company.mobile.startLicense"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <div class="input-group date">
                            <app:dateText property="dto(mobileStartLicense)" view="${op == 'delete'}"
                                          maxlength="10"
                                          styleId="startLicenseMobile"
                                          calendarPicker="true" datePatternKey="${datePattern}"
                                          onchange="changeStartDateValue()"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()}"
                                          convert="true"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          tabindex="23"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="finishLicenseMobile">
                        <fmt:message key="Company.mobile.finishLicense"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(op == 'delete')}">
                        <div class="input-group date">
                            <app:dateText property="dto(mobileEndLicense)" view="${op == 'delete'}"
                                          maxlength="10"
                                          styleId="finishLicenseMobile"
                                          calendarPicker="true" datePatternKey="${datePattern}"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()}"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          tabindex="24"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="COMPANY"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)" tabindex="25"
                                 indexed="31">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="26">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="companyForm"/>


