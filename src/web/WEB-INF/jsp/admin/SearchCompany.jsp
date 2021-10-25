<%@ include file="/Includes.jsp" %>
<script>
    function select(id, name) {
        parent.selectField('fieldReportCompany_id', id, 'fieldReportCompanyName_id', name);
    }
</script>
<%--<c:set var="url" value="${param.action==null ? 'Report/SearchCompany.do' : param.action}" scope="page"/>--%>
<html:form action="/Report/SearchCompany.do" focus="parameter(contactSearchName)" styleClass="form-horizontal">
    <div class="${app2:getFormGroupClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(contactSearchName)"
                           styleClass="largeText ${app2:getFormInputClasses()}"/>
                <span class="input-group-btn">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </span>
            </div>
        </div>
    </div>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="Report/SearchCompany.do"
                    mode="bootstrap"
                    parameterName="name1Alpha"/>
</div>
<div class="table-responsive ">
    <fanta:table mode="bootstrap" width="100%" id="company"
                 styleClass="${app2:getFantabulousTableClases()}"
                 action="Report/SearchCompany.do" imgPath="${baselayout}"
                 list="lightCompanyList">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <fanta:actionColumn name="" title="Common.select"
                                useJScript="true"
                                action="javascript:select('${company.companyId}', '${app2:jscriptEncode(company.companyName)}');"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/import.gif"/>
        </fanta:columnGroup>
        <fanta:dataColumn name="companyName" styleClass="listItem" title="Contact.name"
                          headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
    </fanta:table>
</div>