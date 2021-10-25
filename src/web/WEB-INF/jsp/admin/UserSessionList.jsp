<%@ page language="java" session="true" %>
<%@ include file="/Includes.jsp" %>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/User/UserSessionList.do" focus="parameter(lastName@_firstName@_login)"
               styleClass="form-horizontal">
        <legend class="title">
            <fmt:message key="User.userSession"/>
        </legend>
        <div class="row">
            <div class="${app2:getFormGroupClassesTwoColumns()}">
                <label class="${app2:getFormLabelClasses()}">
                    <fmt:message key="User.title"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <div class="row">
                        <div class="col-xs-12 col-sm-10 wrapperButton">
                            <html:text property="parameter(lastName@_firstName@_login)"
                                       styleClass="mediumText ${app2:getFormInputClasses()}" maxlength="40"/>
                        </div>
                        <c:if test="${default=='false'}">
                            <div class="col-xs-12 col-sm-2 pull-left">
                                <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                                        key="Common.go"/></html:submit>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
            <c:if test="${default}">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Common.company"/>
                    </label>

                    <div class="${app2:getFormContainClasses(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-10 wrapperButton">
                                <html:text property="parameter(lastNameCia@_firstNameCia@_loginCia)"
                                           styleClass="mediumText ${app2:getFormInputClasses()}"
                                           maxlength="40"/>
                            </div>
                            <div class="col-xs-12 col-sm-2 pull-left">
                                <html:submit styleClass="button ${app2:getFormButtonClasses()}">
                                    <fmt:message key="Common.go"/>
                                </html:submit>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="User/UserSessionList.do"
                        mode="bootstrap"
                        parameterName="lastNameAlpha"/>
    </div>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="userSessionList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%" id="userValue" action="User/UserSessionList.do" imgPath="${baselayout}"
                     align="center">
            <fmt:message var="dateTimePattern" key="dateTimePattern"/>
            <fanta:dataColumn name="userName" styleClass="listItem" title="User.user"
                              headerStyle="listHeader" width="18%" orderable="true" maxLength="20"/>
            <c:if test="${view != 'true'}">
                <fanta:dataColumn name="companyName" styleClass="listItem" title="Company"
                                  headerStyle="listHeader" width="15%" orderable="true" maxLength="18"/>
            </c:if>
            <fanta:dataColumn name="startConection" styleClass="listItem" title="User.startConection"
                              headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${userValue.startConection != ''}">
                        ${app2:getDateWithTimeZone(userValue.startConection, timeZone, dateTimePattern)}
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="lastConection" styleClass="listItem" title="User.lastConection"
                              headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${userValue.lastConection != ''}">
                        ${app2:getDateWithTimeZone(userValue.lastConection, timeZone, dateTimePattern)}
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="endConection" styleClass="listItem" title="User.endConection"
                              headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${userValue.endConection != ''}">
                        ${app2:getDateWithTimeZone(userValue.endConection, timeZone, dateTimePattern)}
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="ip" styleClass="listItem" title="User.ip" headerStyle="listHeader" width="10%"
                              orderable="true"/>
            <fanta:dataColumn name="lastActionApp" styleClass="listItem" title="User.lastActionApp"
                              headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${userValue.lastActionApp != ''}">
                        ${app2:getDateWithTimeZone(userValue.lastActionApp, timeZone, dateTimePattern)}
                    </c:when>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="statusId" styleClass="listItem2Center" title="User.connected" renderData="false"
                              headerStyle="listHeader" width="5%" orderable="true">
                <c:choose>
                    <c:when test="${userValue.statusId == '1'}">
                        <fmt:message var="UserStatusConnected" key="User.connected"/>
                        <span title="${UserStatusConnected}" class="${app2:getClassGlyphOk()}"></span>
                    </c:when>
                    <c:otherwise>
                        <fmt:message var="UserStatusDisconnected" key="User.disconnected"/>
                        <span title="${UserStatusDisconnected}" class="${app2:getClassGlyphRemoveSign()}"></span>
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>

