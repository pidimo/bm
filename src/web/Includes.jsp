<%@ page import="com.piramide.elwis.utils.SystemLanguage,
                 com.piramide.elwis.web.common.util.JSPHelper, java.util.Locale"%>

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-wml.tld" prefix="wml" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ taglib uri="/WEB-INF/sslext.tld" prefix="sslext" %>
<%@ taglib uri="/WEB-INF/fantabulous.tld" prefix="fanta" %>
<%@ taglib uri="/WEB-INF/calendar.tld" prefix="calendar" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/x.tld" prefix="x" %>
<%@ taglib uri="/WEB-INF/titus.tld" prefix="titus" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%
    //please do not remove below lines, they are used to skip removing the imports
    //when optimize imports is performed.Optimize imports, remove the non used imports.
    JSPHelper.getMessage(Locale.GERMANY, "emptyMessage");
    SystemLanguage.SYSTEM_CONSTANT_KEY.trim();
%>
