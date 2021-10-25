<%@ include file="/Includes.jsp" %>


<div class="row">
    <div class="label">
        <tags:colonMessage key="Common.name"/>
    </div>
    <div class="content">
        <c:choose>
            <c:when test="${not empty dto.name2}">
                <c:out value="${dto.name1}, ${dto.name2}"/>
            </c:when>
            <c:otherwise>
                <c:out value="${dto.name1}"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<c:if test="${not empty dto.function}">
    <div class="row">
        <div class="label"><tags:colonMessage key="ContactPerson.function"/></div>
        <div class="content">
            <c:out value="${dto.function}"/>
        </div>
    </div>
</c:if>

<c:if test="${not empty dto.birthday}">
    <div class="row">
        <div class="label"><tags:colonMessage key="Contact.Person.birthday"/></div>
        <div class="content">
            <c:choose>
                <c:when test="${not empty dto.dateWithoutYear}">
                    <fmt:message var="datePattern" key="withoutYearPattern"/>
                    <fmt:formatDate value="${app2:intToDate(dto.birthday)}" pattern="${datePattern}"/>
                </c:when>
                <c:otherwise>
                    <fmt:message var="datePattern" key="datePattern"/>
                    <fmt:formatDate value="${app2:intToDate(dto.birthday)}" pattern="${datePattern}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>

<c:if test="${not empty dto.contactPersonTelecomMap}">
    <div class="row">
        <div class="label"><tags:colonMessage key="Contact.telecoms"/></div>
        <div class="content">
            <c:set var="telecomItems" value="${dto.contactPersonTelecomMap}" scope="request"/>
            <c:import url="/mobile/common/contacts/TelecomTile.jsp"/>
        </div>
    </div>
</c:if>




