<%@ include file="/Includes.jsp" %>


<div class="row">
    <div class="label">
        <tags:colonMessage key="Contact.Organization.name"/>
    </div>
    <div class="content">
        <c:out value="${dto.name1}"/>
    </div>
    <c:if test="${not empty dto.name2}">
        <div class="content">
            <c:out value="${dto.name2}"/>
        </div>
    </c:if>
    <c:if test="${not empty dto.name3}">
        <div class="content">
            <c:out value="${dto.name3}"/>
        </div>
    </c:if>
</div>

<c:if test="${not empty dto.zip || not empty dto.city}">
    <div class="row">
        <div class="label"><tags:colonMessage key="Contact.cityZip"/></div>
        <div class="content">
            <c:choose>
                <c:when test="${not empty dto.zip}">
                    <c:out value="${dto.zip} ${dto.city}"/>
                </c:when>
                <c:otherwise>
                    <c:out value="${dto.city}"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>
<c:if test="${not empty dto.street || not empty dto.houseNumber}">
    <div class="row">
        <div class="label"><tags:colonMessage key="Contact.street"/></div>
        <div class="content">
            <c:out value="${dto.street} ${dto.houseNumber}"/>
        </div>
    </div>
</c:if>
<c:if test="${not empty dto.telecomMap}">
    <div class="row">
        <div class="label"><tags:colonMessage key="Contact.telecoms"/></div>
        <div class="content">
            <c:set var="telecomItems" value="${dto.telecomMap}" scope="request"/>
            <c:import url="/mobile/common/contacts/TelecomTile.jsp"/>
        </div>
    </div>
</c:if>