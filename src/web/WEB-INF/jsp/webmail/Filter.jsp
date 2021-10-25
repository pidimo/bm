<%@include file="/Includes.jsp" %>

<c:set var="oneFilterConditions" value="${dto.filterConditions}"/>
<c:set var="numCond" value="${dto.numCond}"/>

<html:form action="${action}" focus="dto(filterName)" styleClass="form-horizontal">
    <div class="${app2:getFormClassesLarge()}">
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${'update' == op || op=='delete'}">
            <html:hidden property="dto(filterId)"/>
        </c:if>
        <div class="${app2:getFormPanelClasses()}">
            <legend class="title">
                <c:out value="${title}"/>
            </legend>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="filterName_id">
                    <fmt:message key="Webmail.filter.name"/>
                </label>
                <div class="${app2:getFormContainClasses(op == 'delete')}">
                    <app:text property="dto(filterName)"
                              styleId="filterName_id"
                              styleClass="middleText ${app2:getFormInputClasses()}"
                              maxlength="50"
                              tabindex="1"
                              view="${op == 'delete'}"/>
                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                </div>

            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()} text-success text-left">
                    <fmt:message key="Webmail.filter.message"/>
                </label>

                <div class="${app2:getFormContainClasses(true)}">
                </div>
            </div>

            <c:set var="conditionNameKeys" value="${app2:getConditionNameKeys(pageContext.request)}"/>
            <c:set var="conditionsKeys" value="${app2:getConditionsKeys(pageContext.request)}"/>
            <c:forEach var="listCondNameKeys" items="${conditionNameKeys}" varStatus="status">
                <div class="row">
                    <html:hidden property="identifyCondition" value="${status.index}"
                                 styleId="identifyCondition_Id${status.index}"/>
                    <c:set var="band" value="0"/>
                    <c:forEach begin="0" end="${numCond}" varStatus="condStatus"> <!-- only if is update -->
                        <c:if test="${oneFilterConditions[condStatus.current].conditionNameKey == listCondNameKeys.value}">

                            <label class="${app2:getFormLabelClasses()}" for="conditionkey${status.index}">
                                <c:out value="${listCondNameKeys.label}"/>
                                <html:hidden property="dto(namekey${status.index})" value="${listCondNameKeys.value}"
                                             styleId="namekey${status.index}_Id"/>
                                <html:hidden property="dto(conditionId${status.index})"
                                             value="${oneFilterConditions[condStatus.current].conditionId}"
                                             styleId="conditionId${status.index}_Id"/>
                            </label>

                            <div class="${app2:getFormContainClasses(op=='delete')}">
                                <div class="row">
                                    <div class="col-sm-5 wrapperButton">
                                        <c:choose>
                                            <c:when test="${op=='delete'}">
                                                <c:forEach var="listCondKeys" items="${conditionsKeys}">
                                                    <c:if test="${listCondKeys.value == oneFilterConditions[condStatus.current].conditionKey }">
                                                        <c:out value="${listCondKeys.label}"/>
                                                    </c:if>
                                                </c:forEach>
                                            </c:when>

                                            <c:otherwise>
                                                <html:select property="dto(conditionkey${status.index})"
                                                             styleId="conditionkey${status.index}"
                                                             value="${oneFilterConditions[condStatus.current].conditionKey}"
                                                             styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                                             tabindex="1">
                                                    <html:option value=""/>
                                                    <c:forEach var="listCondKeys" items="${conditionsKeys}">
                                                        <html:option value="${listCondKeys.value}"><c:out
                                                                value="${listCondKeys.label}"/> </html:option>
                                                    </c:forEach>
                                                </html:select>
                                            </c:otherwise>
                                        </c:choose>
                                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                    <div class="col-sm-7">
                                        <app:text property="dto(text${status.index})"
                                                  value="${oneFilterConditions[condStatus.current].conditionText}"
                                                  styleClass="middleText ${app2:getFormInputClasses()}" maxlength="50"
                                                  tabindex="1" view="${op == 'delete'}"/>
                                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </div>
                            <c:set var="band" value="1"/>

                        </c:if>
                    </c:forEach>

                    <c:if test="${band==0}">
                        <label class="${app2:getFormLabelClasses()}" for="onditionkey${status.index}">
                            <c:out value="${listCondNameKeys.label}"/>
                            <html:hidden property="dto(namekey${status.index})" value="${listCondNameKeys.value}"
                                         styleId="namekey${status.index}_Id"/>
                            <html:hidden property="dto(conditionId${status.index})" value="null"
                                         styleId="conditionId${status.index}_Id"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <div class="row">
                                <c:if test="${op != 'delete'}">
                                    <div class="col-sm-5 wrapperButton">
                                        <html:select property="dto(conditionkey${status.index})"
                                                     styleId="onditionkey${status.index}"
                                                     styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                                     tabindex="1">
                                            <html:option value=""/>
                                            <c:forEach var="listCondKeys" items="${conditionsKeys}">
                                                <html:option value="${listCondKeys.value}"><c:out
                                                        value="${listCondKeys.label}"/>
                                                </html:option>
                                            </c:forEach>
                                        </html:select>
                                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </c:if>
                                <div class="col-sm-7">
                                    <app:text property="dto(text${status.index})"
                                              styleClass="middleText ${app2:getFormInputClasses()}" maxlength="50"
                                              tabindex="1" view="${op == 'delete'}"/>
                                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                                </div>
                            </div>
                        </div>
                    </c:if>

                </div>
            </c:forEach>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()} text-left text-success">
                    <fmt:message key="Webmail.filter.then"/>
                </label>

                <div class="contain">

                </div>
            </div>
            <div class="${app2:getFormGroupClasses()}">
                <label class="${app2:getFormLabelClasses()}" for="folderId_id">
                    <fmt:message key="Webmail.filter.movetomessage"/>
                </label>

                <div class="${app2:getFormContainClasses(op=='delete')}">

                    <c:choose>
                        <c:when test="${dto.userFolderList != null}">
                            <c:set var="userFolderList" value="${dto.userFolderList}"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="userFolderList" value="${formUserFolders}"/>
                        </c:otherwise>
                    </c:choose>

                        <%--set folders--%>
                    <c:forEach var="folder" items="${userFolderList}">
                        <html:hidden property="userFolder" value="${folder.userFolderId}<s>${folder.userFolder}"
                                     styleId="userFolder_${folder.userFolderId}_${folder.userFolder}_Id"/>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${op=='delete'}">
                            <c:forEach var="folder" items="${userFolderList}">
                                <c:if test="${dto.folderId == folder.userFolderId }">
                                    <c:choose>
                                        <c:when test="${folder.userFolder == 'Webmail.folder.trash'}">
                                            <fmt:message key="${folder.userFolder}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${folder.userFolder}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <html:select property="dto(folderId)"
                                         styleId="folderId_id"
                                         styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                         tabindex="10">
                                <html:option value=""/>
                                <c:forEach var="folder" items="${userFolderList}">
                                    <c:choose>
                                        <c:when test="${folder.userFolder == 'Webmail.folder.trash'}">
                                            <html:option value="${folder.userFolderId}"> <fmt:message
                                                    key="${folder.userFolder}"/> </html:option>
                                        </c:when>
                                        <c:otherwise>
                                            <html:option value="${folder.userFolderId}"> <c:out
                                                    value="${folder.userFolder}"/> </html:option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </html:select>
                        </c:otherwise>
                    </c:choose>
                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                </div>
            </div>
        </div>
        <div class="row col-xs-12 ">
            <app2:securitySubmit operation="${op}" functionality="WEBMAILFILTER"
                                 styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                 tabindex="11"><c:out value="${button}"/></app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILFILTER"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton"
                                     property="SaveAndNew" tabindex="12"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"
                         tabindex="13"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>
<tags:jQueryValidation formName="filterForm"/>
