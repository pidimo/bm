<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ attribute name="property" required="true" %>
<%@ attribute name="styleId" required="true" %>
<%@ attribute name="tabIndex" %>
<%@ attribute name="view" %>
<%@ attribute name="style" %>
<%@ attribute name="value" %>
<%@ attribute name="onHideJSFunction" %>
<%@ attribute name="isFormatRGBA"%>

<c:choose>
    <c:when test="${not empty value}">
        <app:text property="${property}"
                  value="${value}"
                  view="${view}"
                  tabindex="${tabIndex}"
                  style="${style}"
                  styleId="${styleId}"
                  styleClass="${app2:getFormColorPickerClasses(view)}"
                  maxlength="7"/>
    </c:when>
    <c:otherwise>
        <app:text property="${property}"
                  view="${view}"
                  tabindex="${tabIndex}"
                  style="${style}"
                  styleId="${styleId}"
                  styleClass="${app2:getFormColorPickerClasses(view)}"
                  maxlength="7"/>
    </c:otherwise>
</c:choose>

<c:if test="${not empty onHideJSFunction || 'true' eq isFormatRGBA}">

    <script language="JavaScript">

        $("#"+'${styleId}').minicolors({
            control: 'wheel',
            defaultValue: '',
            letterCase: 'uppercase',
            position: 'top right',

            <c:if test="${'true' eq isFormatRGBA}">
                format: 'rgb',
                opacity: 'true',
            </c:if>
            <c:if test="${not empty onHideJSFunction}">
                hide: function() {
                    eval(${onHideJSFunction});
                },
            </c:if>

            theme: 'bootstrap'
        });

    </script>
</c:if>

