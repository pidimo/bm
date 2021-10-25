<%@ page import="com.piramide.elwis.utils.SupportConstants" %>
<%@ page import="com.piramide.elwis.web.admin.session.User" %>
<%@ page import="com.piramide.elwis.web.common.validator.DataBaseValidator" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List"%>
<%@ include file="/Includes.jsp" %>

<c:if test="${'create' != op}">
    <c:set var="isOpen" value="${stateForm.dtoMap.stageType == 0}"/>
</c:if>


<%
    Map conditions = new HashMap();
    conditions.put("companyid", ((User) request.getSession().getAttribute("user")).getValue("companyId"));
    conditions.put("type", Integer.toString(SupportConstants.SUPPORT_TYPE_STATE));
    boolean hasOpenState = DataBaseValidator.i.isDuplicate(SupportConstants.TABLE_STATE, "stagetype", Integer.toString(SupportConstants.OPEN_STATE),
            null, null, conditions, false);
    Boolean isOpen = (Boolean) pageContext.getAttribute("isOpen");
    if (isOpen != null) {
        hasOpenState = hasOpenState && !isOpen.booleanValue();
    }
    pageContext.setAttribute("stages", JSPHelper.getStageTypesState(request, hasOpenState));
%>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>

</tr>
<tr>
<td>
<html:form action="${action}" focus="dto(stateName)">


<c:if test="${('update' == op) || ('delete' == op)}">
    <c:set var="haveFirstTranslation" value="${stateForm.dtoMap['haveFirstTranslation']}"/>
    <html:hidden property="dto(haveFirstTranslation)" value="${haveFirstTranslation}"/>
    <html:hidden property="dto(langTextId)"/>
</c:if>


<table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <c:if test="${type != null}">
        <html:hidden property="dto(stateType)" value="${type}"/>
    </c:if>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(stateId)"/>
    </c:if>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
        <html:hidden property="dto(oldStageType)"/>
    </c:if>
    <c:if test="${'delete' == op}">
        <html:hidden property="dto(withReferences)" value="true"/>
    </c:if>
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message key="State.name"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(stateName)" styleClass="largetext" maxlength="40" tabindex="1"
                      view="${'delete' == op}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="State.sequence"/></TD>
        <TD class="contain">
            <app:text property="dto(sequence)" styleClass="shortText" style="text-align:right" tabindex="2"
                      maxlength="6" view="${'delete' == op}"/>
        </TD>
    </TR>
    <TR>
        <TD class="label"><fmt:message key="State.stageType"/></TD>
        <TD class="contain">
            <html:select property="dto(stageType)" styleClass="mediumSelect"
                         readonly="${op == 'delete'}" tabindex="3">
                <html:option value=""/>
                <html:options collection="stages" property="value" labelProperty="label"/>
            </html:select>
        </TD>
    </TR>

    <tr>
        <td class="label" ><fmt:message key="State.language"/></td>
        <td class="contain" >
            <fanta:select property="dto(languageId)" listName="systemLanguageList" labelProperty="name"  valueProperty="id"
                          firstEmpty="true" styleClass="select" readOnly="${op == 'delete'|| true == haveFirstTranslation}"
                          tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
            </fanta:select>
        </td>
    </tr>

</table>
<table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="STATE" styleClass="button"
                                 tabindex="5" property="dto(save)">
                ${button}
            </app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="STATE" styleClass="button"
                                     tabindex="6" property="SaveAndNew">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>

            </c:if>
            <html:cancel styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/>
            </html:cancel>
            <input type="hidden" name="cancel" value="cancel"/>
        </TD>
    </TR>
</table>
</html:form>

</td>
</tr>
</table>



