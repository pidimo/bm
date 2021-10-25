<%--<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>--%>
<%--<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>--%>
<%--<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>--%>
<%--<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>--%>

<%@ include file="/Includes.jsp" %>

<tags:initBootstrapFile/>

<html:form action="${action}" focus="dto(picture)" enctype="multipart/form-data" styleClass="form-horizontal">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <html:hidden property="dto(productId)" value="${param.productId}"/>

                    <%--if update action or delete action--%>
                <c:if test="${('update' == op) || ('delete' == op)}">
                    <html:hidden property="dto(freeTextId)"/>
                    <c:set var="freeId" value="${productPictureForm.dtoMap['freeTextId']}"/>
                    <c:set var="sizeImage" value="${productPictureForm.dtoMap['size']}"/>
                    <c:set var="nameImage" value="${productPictureForm.dtoMap['productPictureName']}"/>
                </c:if>

                    <%--for the version control if update action--%>
                <c:if test="${'update' == op}">
                    <html:hidden property="dto(version)"/>
                </c:if>

                <c:if test="${('delete' == op) || ('update' == op)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="actionTypeName_id">
                            <fmt:message key="ProductPicture.picture"/>
                        </label>

                        <div class="${app2:getFormContainClasses(false)}">
                            <html:img
                                    page="/ProductPicture/DownloadImage.do?dto(freeTextId)=${freeId}&dto(thumbnail)=true"
                                    border="0" vspace="10" hspace="10"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <c:if test="${'delete' != op}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="picture_id">
                            <fmt:message key="ProductPicture.file"/>
                        </label>

                        <div class="${app2:getFormContainClasses(false)}">
                            <tags:bootstrapFile property="dto(picture)" styleId="picture_id" tabIndex="1"/>

                            <fmt:message key="ProductPicture.fileMaxLength"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="productPictureName_id">
                        <fmt:message key="ProductPicture.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses('delete' == op)}">
                        <app:text property="dto(productPictureName)"
                                  styleClass="largetext ${app2:getFormInputClasses()}"
                                  maxlength="20"
                                  styleId="productPictureName_id"
                                  tabindex="1"
                                  view="${'delete' == op}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

                <c:if test="${('delete' == op) || ('update' == op)}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}" for="size_id">
                            <fmt:message key="ProductPicture.size"/>
                        </label>

                        <div class="${app2:getFormContainClasses(true)}">
                            <app:text property="dto(size)"
                                      styleClass="text ${app2:getFormInputClasses()}"
                                      maxlength="8"
                                      styleId="size_id"
                                      tabindex="1"
                                      view="true"/>&nbsp<fmt:message key="msg.Kbytes"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="ProductPicture.uploadDate"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <jsp:useBean id="now1" class="java.util.Date"/>
                        <c:set var="dateValueParse" value="${now1}"/>
                        <fmt:message var="datePattern" key="datePattern"/>
                        <fmt:formatDate var="dateValue" value="${dateValueParse}" pattern="${datePattern}"/>

                        <c:out value="${dateValue}"/>
                        <html:hidden property="dto(uploadDate)" value="${dateValue}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>

            </fieldset>
        </div>

            <%--CREATE, CANCEL, SAVE AND NEW buttons--%>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}">
                <c:out value="${button}"/>
            </html:submit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

    </div>
</html:form>

<tags:jQueryValidation formName="productPictureForm"/>


















