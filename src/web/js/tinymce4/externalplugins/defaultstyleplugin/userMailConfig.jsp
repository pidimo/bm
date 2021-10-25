//<%@ page language="java" contentType="text/javascript; charset=UTF-8"%>
    //<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
    //<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

    //to get user mail editor configurations
        <c:set var="userMailConf" value="${app2:readUserMailConfiguration(pageContext.request)}" scope="request"/>

        function StyleConfig() {
            this.name = "User mail configuration";
        };

        StyleConfig.getEditorFont = function() {
            var font = null;
        <c:if test="${not empty userMailConf.font}">
            font = '${userMailConf.font}';
        </c:if>
            return font;
        };

        StyleConfig.getEditorFontSize = function() {
            var fontSize = null;
        <c:if test="${not empty userMailConf.fontSize}">
            fontSize = '${userMailConf.fontSize}';
        </c:if>
            return fontSize;
        };