<%@ include file="/Includes.jsp" %>

<%--<tags:bootstrapModalPopup styleId="urlLink_popup_id" isLargeModal="true"/>--%>
<tags:initBootstrapSelectPopup/>

<script language="JavaScript">
    function openURL() {
        var value = document.getElementById('url').value;
        if (value != '') {
            if (value.indexOf(':') == -1) { //has no protocol symbol
                window.open('http://' + value);//adding a default protocol
//                launchBootstrapPopup("urlLink_popup_id", urlPopup, searchName, submitOnSelect);
            } else {
                window.open(value); //open with the protocol defined
            }
        }
    }
</script>

<!--class="containTop"-->
<html:form action="${action}" focus="dto(comment)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormPanelClasses()}">
            <fmt:message var="dateTimePattern" key="dateTimePattern"/>
            <html:hidden property="dto(op)" value="${op}"/>
            <html:hidden property="dto(actionHistory)" value="${actionHistory}"/>
            <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>

            <c:set var="view" value="${false}"/>
            <c:if test="${('update' == op) || ('delete' == op)}">
                <html:hidden property="dto(articleOwnerId)"/>
                <html:hidden property="dto(linkId)"/>
                <html:hidden property="dto(createUserId)"/>
                <c:set var="view"
                       value="${(sessionScope.user.valueMap['userId'] == linkForm.dtoMap.createUserId) || (sessionScope.user.valueMap['userId'] == linkForm.dtoMap.articleOwnerId)}"/>
            </c:if>
            <c:if test="${'update' == op}">
                <html:hidden property="dto(version)"/>
            </c:if>
            <c:if test="${'delete' == op}">
                <html:hidden property="dto(withReferences)" value="true"/>
            </c:if>
            <c:if test="${'create' == op}">
                <html:hidden property="dto(createUserId)" value="${sessionScope.user.valueMap['userId']}"/>
            </c:if>

            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <c:if test="${'create' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" nowrap for="ownerName_id">
                            <fmt:message key="Link.publishBy"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <app:text property="dto(ownerName)" styleClass="${app2:getFormInputClasses()}"
                                      styleId="ownerName_id" maxlength="40" view="true"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}" nowrap>
                            <fmt:message key="Link.publishDate"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                                ${app2:getDateWithTimeZone(linkForm.dtoMap.createDateTime, timeZone, dateTimePattern)}
                        </div>
                    </div>
                </c:if>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" nowrap for="comment_id">
                        <fmt:message key="Link.comment"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op || (!view && op != 'create'))}">
                        <app:text property="dto(comment)" styleId="comment_id"
                                  styleClass="${app2:getFormInputClasses()} largetext" maxlength="160"
                                  view="${'delete' == op || (!view && op != 'create')}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}" for="url">
                        <fmt:message key="Link.url"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory('delete' == op || (!view && op != 'create'))}">
                        <c:set var="openLink"><fmt:message key="Common.openLink"/></c:set>
                        <c:choose>
                            <c:when test="${'delete' == op || (!view && op != 'create')}">
                                <app:text property="dto(url)" styleId="url"
                                          styleClass="${app2:getFormInputClasses()} largeText" maxlength="200"
                                          view="${'delete' == op || (!view && op != 'create')}"/>
                                <a class="btn-link pull-right" href="javascript:openURL();" title="${openLink}"/>
                                    <span class="glyphicon glyphicon-globe"></span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <div class="input-group">
                                    <app:text property="dto(url)" styleId="url"
                                              styleClass="${app2:getFormInputClasses()} largeText" maxlength="200"
                                              view="${'delete' == op || (!view && op != 'create')}"/>
                                        <span class="input-group-btn">
                                            <a class="${app2:getFormButtonClasses()}"
                                               href="javascript:openURL();"
                                               title="${openLink}">
                                                <span class="glyphicon glyphicon-globe"></span>
                                            </a>
                                        </span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${view || op == 'create'}">
                <app2:securitySubmit property="dto(save)" operation="${op}" functionality="ARTICLELINK"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="10">
                    ${button}
                </app2:securitySubmit>
            </c:if>

            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="ARTICLELINK"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="11" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="12">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

