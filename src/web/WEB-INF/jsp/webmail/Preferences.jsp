<%@ page import="com.piramide.elwis.utils.WebMailConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="HTML_EDITMODE" value="<%=WebMailConstants.HTML_EDITMODE%>"/>
<c:set var="TEXT_EDITMODE" value="<%=WebMailConstants.TEXT_EDITMODE%>"/>

<html:form action="/Preferences/Update.do" focus="dto(replyMode)" styleClass="form-horizontal">
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="EXECUTE" functionality="MAIL"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>
        </div>
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                        ${title}
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="replyMode_id">
                        <fmt:message key="Webmail.userMail.replyMode"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(replyMode)" styleId="replyMode_id"
                                               value="true" styleClass="radio"/>
                                <label for="replyMode_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="saveSendItem_id">
                        <fmt:message key="Webmail.userMail.saveSendItems"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(saveSendItem)" styleId="saveSendItem_id"
                                               value="true" styleClass="radio"/>
                                <label for="saveSendItem_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <app2:checkAccessRight functionality="MAIL" permission="DELETE">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" for="emptyTrashLogout_id">
                            <fmt:message key="Webmail.userMail.emptyTrashLogout"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(emptyTrashLogout)" styleId="emptyTrashLogout_id"
                                                   value="true" styleClass="radio"/>
                                    <label for="emptyTrashLogout_id"></label>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </app2:checkAccessRight>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="editMode_id">
                        <fmt:message key="Webmail.userMail.editMode"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <html:select property="dto(editMode)" styleId="editMode_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}">
                            <html:option value="" key=""></html:option>
                            <html:option value="${TEXT_EDITMODE}" key="Webmail.userMail.text"/>
                            <html:option value="${HTML_EDITMODE}" key="Webmail.userMail.html"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="editorFont_id">
                        <fmt:message key="Webmail.userMail.editorFont"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <c:set var="fontList" value="${app2:getHtmlEditorFonts()}"/>
                        <html:select property="dto(editorFont)"
                                     styleId="editorFont_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}">
                            <html:option value=""/>
                            <html:options collection="fontList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="editorFontSize_id">
                        <fmt:message key="Webmail.userMail.editorFontSize"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <c:set var="fontSizeList" value="${app2:getHtmlEditorFontSizes()}"/>
                        <html:select property="dto(editorFontSize)"
                                     styleId="editorFontSize_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}">
                            <html:option value=""/>
                            <html:options collection="fontSizeList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="backgroundDownload">
                        <fmt:message key="Webmail.userMail.backgroundDownload"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(backgroundDownload)" styleId="backgroundDownload"
                                               value="true" styleClass="radio"/>
                                <label for="backgroundDownload"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="showPopNotification_id">
                        <fmt:message key="Webmail.userMail.shoPopNotifications"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(null)} col-lg-7">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(showPopNotification)" styleId="showPopNotification_id"
                                               value="true" styleClass="radio"/>
                                <label for="showPopNotification_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="EXECUTE" functionality="MAIL"
                                 styleClass="button ${app2:getFormButtonClasses()}">
                <fmt:message key="Common.save"/>
            </app2:securitySubmit>

        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="preferencesForm"/>