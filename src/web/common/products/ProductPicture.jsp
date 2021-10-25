<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(picture)" enctype="multipart/form-data" >
    <table id="language.jsp" border="0" cellpadding="0" cellspacing="0" width="65%" align="center" class="container">

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

    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>
    <%--<c:if test="${'create' == op}">--%>
    <c:if test="${('delete' == op) || ('update' == op)}">
        <TR>
            <TD class="label" width="25%" nowrap><fmt:message    key="ProductPicture.picture"/></TD>
            <TD class="contain" width="75%">
            <html:img page="/ProductPicture/DownloadImage.do?dto(freeTextId)=${freeId}&dto(thumbnail)=true" border="0"  vspace="10" hspace="10"/>
            </TD>
        </TR>
    </c:if>
    <c:if test="${'delete' != op}">
        <TR>
            <TD class="label" width="25%" nowrap><fmt:message  key="ProductPicture.file"/></TD>
            <TD class="contain" width="75%">
            <html:file property="dto(picture)" styleClass="largetext" maxlength="80" />
            <fmt:message    key="ProductPicture.fileMaxLength"/>
            </TD>
        </TR>
    </c:if>
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="ProductPicture.name"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(productPictureName)" styleClass="largetext" maxlength="20" view="${'delete' == op}"/>
        </TD>
    </TR>

    <c:if test="${('delete' == op) || ('update' == op)}">
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="ProductPicture.size"/></TD>
        <TD class="contain" width="75%">
        <app:text property="dto(size)" styleClass="text" maxlength="8" view="true"/>&nbsp<fmt:message    key="msg.Kbytes"/>
        </TD>
    </TR>
    </c:if>
    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="ProductPicture.uploadDate"/></TD>
        <TD class="contain" width="75%">

        <jsp:useBean id="now1" class="java.util.Date"/>
        <c:set var="dateValueParse" value="${now1}"/>
        <fmt:message   var="datePattern" key="datePattern"/>
        <fmt:formatDate var="dateValue" value="${dateValueParse}" pattern="${datePattern}"/>

        <c:out value="${dateValue}"/>
        <html:hidden property="dto(uploadDate)" value="${dateValue}"/>
        </TD>
    </TR>

    <tr>
        <td colspan="2" <c:out value="${sessionScope.listshadow}" escapeXml="false" />><img src='<c:out value="${sessionScope.baselayout}"/>/img/zsp.gif' alt="" height="5"></td>
    </tr>
    </table>
<%--CREATE, CANCEL, SAVE AND NEW buttons--%>
    <table cellSpacing=0 cellPadding=4 width="65%" border=0 align="center">
    <TR>
        <TD class="button">
        <html:submit styleClass="button" ><c:out value="${button}"/></html:submit>
        <%--<c:if test="${op == 'create'}" >
            <html:submit styleClass="button" property="SaveAndNew" ><fmt:message   key="Common.saveAndNew"/></html:submit>
        </c:if>--%>
        <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>
</html:form>

    </td>
</tr>
</table>