<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>

<%
    List list = JSPHelper.getLanguageList(request);
    request.setAttribute("systemLanguageList", list);
%>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="whiteSpace" value=" "/>


<html:form action="${action}" styleId="companyFormId" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(languageSelected)" styleId="languageSelected"/>
        <html:hidden property="dto(companyId)"/>

        <html:hidden property="dto(companyName)"
                     value="${companyDeleteForm.dtoMap.name1}&nbsp;${not empty companyDeleteForm.dtoMap.name2 ? companyDeleteForm.dtoMap.name2 : whiteSpace}"/>

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
                                               styleClass="adminCheckBox"
                                               disabled="true"/>
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

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <app:text property="dto(name1)" styleId="name1_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60"
                                  view="true" tabindex="1"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <div class="${app2:getFormContainRenderCategory(true)} col-sm-offset-4">
                        <app:text property="dto(name2)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60"
                                  view="true" tabindex="2"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <div class="${app2:getFormContainRenderCategory(true)} col-sm-offset-4">
                        <app:text property="dto(name3)"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="60"
                                  view="true" tabindex="3"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="login_id">
                        <fmt:message key="Company.login"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <app:text property="dto(login)" styleId="login_id"
                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                  maxlength="20"
                                  view="true" tabindex="4"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Company.startLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <app:dateText property="dto(startLicenseDate)"
                                      maxlength="10"
                                      styleId="startLicense"
                                      calendarPicker="false"
                                      datePatternKey="${datePattern}"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      view="true"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="finishLicense">
                        <fmt:message key="Company.finishLicenseDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <div class="input-group date">
                            <app:dateText property="dto(finishLicenseDate)"
                                          view="true"
                                          maxlength="10"
                                          styleId="finishLicense"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          convert="true"
                                          tabindex="5"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Company.companyType"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(view)}">
                        <c:set var="companyTypes" value="${app2:getCompanyTypes(pageContext.request)}"/>
                        <html:select property="dto(companyType)" styleId="companyType_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                     tabindex="5" readonly="true">
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

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <app:text property="dto(usersAllowed)" styleId="usersAllowed_id"
                                  styleClass="${app2:getFormInputClasses()} numberText"
                                  maxlength="4"
                                  view="true"
                                  tabindex="6"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="finishLicense">
                        <fmt:message key="Common.creationDate"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <div class="input-group date">
                            <app:dateText property="dto(recordDate)"
                                          view="true"
                                          maxlength="10"
                                          styleId="finishLicense"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          mode="bootstrap"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          convert="true"
                                          tabindex="7"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

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
                                  tabindex="8"/>
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
                                  tabindex="9"/>
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
                                  maxlength="20"
                                  tabindex="10"/>
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
                                  maxlength="60"
                                  tabindex="11"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>


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
                <c:forEach var="module" items="${moduleList}">
                    <div class="${app2:getFormGroupClasses()}">
                        <div class="radiocheck pull-left col-xs-12 col-sm-1">
                            <div class="checkbox checkbox-default">
                                <html:multibox property="modules" styleId="modules_id" value="${module.moduleId}"
                                               tabindex="12"
                                               disabled="true"/>
                                <label for="modules_id"></label>
                            </div>
                        </div>
                        <label class="control-label col-xs-12 col-sm-3">
                            <fmt:message key="${module.nameKey}"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <app:text
                                    property="dto(${fn:replace(module.nameKey,'.','_')}_${module.moduleId})"
                                    styleClass="${app2:getFormInputClasses()} mediumText"
                                    maxlength="7" tabindex="13" view="true"/>
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

                    <div class="${app2:getFormContainRenderCategory(null)}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(mobileActive)" styleId="mobileActive_id"
                                               disabled="${op == 'delete'}"
                                               tabindex="14"/>
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
                                  maxlength="4" tabindex="15"/>
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
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          convert="true"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          tabindex="16"/>
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
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          currentDate="true"
                                          currentDateTimeZone="${currentDateTimeZone}"
                                          tabindex="17"/>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="COMPANY" styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)"
                                 indexed="31">
                ${button}
            </app2:securitySubmit>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>
