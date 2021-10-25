<%@ include file="/Includes.jsp" %>

<app2:jScriptUrl url="/campaign/Campaign/Download/Document.do?campaignId=${param.campaignId}" var="jsDownloadUrl"
                 addModuleParams="false">
    <app2:jScriptUrlParam param="templateId" value="idTemplate"/>
    <app2:jScriptUrlParam param="languageId" value="idLanguage"/>
</app2:jScriptUrl>


<script language="JavaScript">
    function documentDownload(idTemplate, idLanguage) {
        location.href = ${jsDownloadUrl};
    }

</script>

<div class="${app2:getFormClassesLarge()}">
    <form class="form-horizontal">
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                         onclick="location.href='${urlGenerateDocumentReturn}'">
                ${button}
            </html:button>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <fmt:message key="Campaign.document.summary.title"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Campaign.activity.docGenerate.template"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <c:out value="${templateName}"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Campaign.activity.docGenerate.totalContacts"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <c:out value="${totalContact}"/>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelRenderCategory()}">
                        <fmt:message key="Campaign.activity.docGenerate.contactProcessed"/>
                    </label>

                    <div class="${app2:getFormContainRenderCategory(true)}">
                        <c:out value="${totalProcessed}"/>
                    </div>
                </div>
                <c:if test="${totalFail > 0}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <fmt:message key="Campaign.activity.docGenerate.contactNotProcessed"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <c:out value="${totalFail}"/>
                        </div>
                    </div>
                </c:if>


                <legend class="title">
                    <fmt:message key="Campaign.activity.docGenerate.docOutputByLanguage"/>
                </legend>

                <c:forEach var="summary" items="${summaryList}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelRenderCategory()}">
                            <c:out value="${summary.language}"/>
                        </label>

                        <div class="${app2:getFormContainRenderCategory(true)}">
                            <a href="javascript:documentDownload('${templateId}','${summary.languageId}')"
                               style="text-decoration: none;"
                               title="<fmt:message key="Campaign.document.summary.download"/>">
                                <span class="glyphicon glyphicon-folder-open"></span>
                            </a> &nbsp;
                            <fmt:message key="Campaign.activity.docGenerate.contacts">
                                <fmt:param value="${summary.count}"/>
                            </fmt:message>
                        </div>
                    </div>
                </c:forEach>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:button property="" styleClass="${app2:getFormButtonClasses()}"
                         onclick="location.href='${urlGenerateDocumentReturn}'">
                ${button}
            </html:button>
        </div>
    </form>
</div>