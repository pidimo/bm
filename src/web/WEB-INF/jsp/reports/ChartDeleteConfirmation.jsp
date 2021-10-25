<%@ include file="/Includes.jsp" %>

<html:form action="/ChartType/Delete.do" focus="deleteButton" styleClass="form-horizontal">
    <c:set var="op" value="delete"/>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(reportId)" value="${param['reportId']}"/>
    <html:hidden property="dto(chartId)"/>
    <html:hidden property="dto(title)"/>
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Report.chart.msg.delete"/>
                </legend>

                <p>
                    <fmt:message key="Report.chart.msg.deleteConfirmation"/>
                </p>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit styleId="deleteButton" operation="UPDATE" functionality="CHART"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="1">
                <fmt:message key="Common.delete"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>

