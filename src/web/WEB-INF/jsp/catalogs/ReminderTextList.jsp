<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getListWrapperClasses()}">

    <app2:checkAccessRight functionality="REMINDERLEVEL" permission="UPDATE">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app:url value="/ReminderText/Forward/Create.do" var="url"/>
            <html:button styleClass="${app2:getFormButtonClasses()}" property="dto(new)"
                         onclick="window.parent.location='${url}'">
                <fmt:message key="Common.new"/>
            </html:button>
        </div>
    </app2:checkAccessRight>

    <legend class="title">
        <fmt:message key="${windowTitle}"/>
    </legend>

    <div class="table-responsive" id="tableId">

        <app:url var="listURL" value="/ReminderText/List.do" enableEncodeURL="false"/>

        <fanta:table mode="bootstrap" list="reminderTextList"
                     width="100%"
                     id="reminderText"
                     action="${listURL}"
                     imgPath="${baselayout}" addModuleParams="true"
                     styleClass="${app2:getFantabulousTableClases()}">

            <app:url var="editAction"
                     value="ReminderText/Forward/Update.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}"/>

            <app:url var="deleteAction"
                     value="ReminderText/Forward/Delete.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}&dto(enableLastElementValidation)=true"/>

            <app:url var="downloadAction"
                     value="ReminderText/Forward/Download.do?dto(reminderLevelId)=${reminderText.reminderLevelId}&dto(languageId)=${reminderText.languageId}&dto(freeTextId)=${reminderText.freetextId}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader">
                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
                    <fanta:actionColumn name="up"
                                        title="Common.update"
                                        action="javascript:goParentURL('${editAction}')"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"
                                        useJScript="true"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="DELETE">
                    <c:choose>
                        <c:when test="${1 != reminderText.isDefault}">
                            <fanta:actionColumn name=""
                                                title="Common.delete"
                                                useJScript="true"
                                                action="javascript:goParentURL('${deleteAction}')"
                                                styleClass="listItem"
                                                headerStyle="listHeader"
                                                glyphiconClass="${app2:getClassGlyphTrash()}"/>
                        </c:when>
                        <c:otherwise>
                            <fanta:actionColumn name="" title="" styleClass="listItem"
                                                headerStyle="listHeader">
                                &nbsp;
                            </fanta:actionColumn>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="REMINDERLEVEL" permission="VIEW">
                    <fanta:actionColumn name="download"
                                        title="Common.download"
                                        useJScript="true"
                                        action="javascript:goParentURL('${downloadAction}')"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphDownload()}"/>

                </app2:checkAccessRight>
            </fanta:columnGroup>

            <fanta:dataColumn name="languageName"
                              styleClass="listItem"
                              title="ReminderText.language"
                              action="javascript:goParentURL('${editAction}')"
                              headerStyle="listHeader" width="75%"
                              orderable="true"
                              useJScript="true"/>

            <fanta:dataColumn name="isDefault"
                              styleClass="listItem2"
                              title="ReminderText.isDefault"
                              headerStyle="listHeader"
                              width="20%"
                              renderData="false">

                <c:if test="${reminderText.isDefault == '1'}">
                    <span class="${app2:getClassGlyphOk()}"></span>
                </c:if>
            </fanta:dataColumn>
        </fanta:table>

    </div>
</div>