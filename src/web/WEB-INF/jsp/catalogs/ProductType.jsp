<%@ page import="com.piramide.elwis.utils.ProductConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="PRODUCTTYPETYPE_NORMAL" value="<%=ProductConstants.ProductTypeType.NORMAL.getConstant()%>"/>


<html:form action="${action}" focus="dto(typeName)" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div id="ProductType.jsp">
                    <html:hidden property="dto(op)" value="${op}"/>
                    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

                    <c:if test="${'create' == op}">
                        <html:hidden property="dto(productTypeType)" value="${PRODUCTTYPETYPE_NORMAL}"/>
                    </c:if>

                    <c:if test="${('update' == op) || ('delete' == op)}">
                        <html:hidden property="dto(typeId)"/>
                        <html:hidden property="dto(productTypeType)"/>
                    </c:if>


                    <c:if test="${'update' == op}">
                        <html:hidden property="dto(version)"/>
                    </c:if>


                    <c:if test="${'delete' == op}">
                        <html:hidden property="dto(withReferences)" value="true"/>
                    </c:if>
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="typeName_id">
                            <fmt:message key="ProductType.typeName"/>
                        </label>

                        <div class="${app2:getFormContainClasses('delete' == op)}">
                            <app:text styleClass="largetext ${app2:getFormInputClasses()}" property="dto(typeName)"
                                      styleId="typeName_id"
                                      maxlength="80"
                                      view="${'delete' == op}"/>
                            <c:out value="${sessionScope.listshadow}" escapeXml="false"/><img
                                src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5">
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" functionality="PRODUCTTYPE"
                                 styleClass="button ${app2:getFormButtonClasses()}">${button}</app2:securitySubmit>
            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}" functionality="PRODUCTTYPE"
                                     styleClass="button ${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"><fmt:message
                        key="Common.saveAndNew"/></app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}"><fmt:message
                    key="Common.cancel"/></html:cancel>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="productTypeForm"/>