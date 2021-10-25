<%@ include file="/Includes.jsp" %>
<app2:jScriptUrl url="obj" var="jsAlphabetUrl" isHrefObj="true">
    <app2:jScriptUrlParam param="parameter(allowCreation)" value="creation"/>
</app2:jScriptUrl>
<script>
    function select(id, name) {
        opener.selectField('fieldAddressId_id', id, 'fieldAddressName_id', name);
    }

    function openNewContact(type) {
        if (type == '1')
            location = '<c:url value="/contacts/Person/Forward/Create/Popup.do?allowCreation=${param.allowCreation}"/>';
        else if (type == '0')
            location = '<c:url value="/contacts/Organization/Forward/Create/Popup.do?allowCreation=${param.allowCreation}"/>';

        newWidth = 860;
        newHeight = 550;
        window.resizeTo(newWidth, newHeight);
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - newWidth / 2;
        var posy = winy - newHeight / 2;
        window.moveTo(posx, posy);
    }

    function jump(obj) {
        var creation = "-1";
        if (document.getElementById('allowCreationId') != null) {
            creation = document.getElementById('allowCreationId').value;
        }
        window.location =${jsAlphabetUrl};
    }

</script>

<c:set var="url" value="${param.action==null ? 'SearchAddress.do' : param.action}" scope="page"/>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="3">
            <fmt:message key="Contact.Title.search"/>
        </td>
    </tr>


    <TR>
        <td class="label"><fmt:message key="Common.search"/></td>
        <html:form action="${url}?allowCreation=${param.allowCreation}" focus="parameter(contactSearchName)">
            <td class="contain" nowrap="nowrap">
                <html:text property="parameter(contactSearchName)" styleClass="largeText"/>&nbsp;
                <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>&nbsp;


            </td>
            <td class="contain" nowrap="nowrap" style="text-align:right;">

                    <%--Doc:
                    Only Available for Search of contacts which use the SearchAddress.jsp in the Struts configurationd
                    success forward.
                     1.-if a parameter named  "allowCreation" is defined in the url of the select popup
                     a new person or new organization buttons are available in this search pop-up.
                     The values of the parameter are:
                     1 = A new person button appears.
                     0 = A new organization button appears.
                     2= person and organization buttons appears.

                     If parameter is not passed, buttons only does not appears.

                     Example:
                     <tags:selectPopup url="/contacts/OrganizationSearch.do?allowCreation=0"

                     Above means that in the search list a new Organization buttons will appears.
                    --%>

                <c:choose>
                    <c:when test="${not empty param.allowCreation}">
                        <c:set var="allowCreation" value="${param.allowCreation}"/>
                        <html:hidden property="parameter(allowCreation)" value="${allowCreation}"
                                     styleId="allowCreationId"/>
                    </c:when>
                    <c:when test="${not empty param['parameter(allowCreation)']}">
                        <c:set var="allowCreation" value="${param['parameter(allowCreation)']}"/>
                        <html:hidden property="parameter(allowCreation)" styleId="allowCreationId"/>
                    </c:when>
                </c:choose>

                <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                    <c:if test="${allowCreation=='1' || allowCreation=='2'}">
                        <html:button property="" styleClass="button" onclick="openNewContact('1');"><fmt:message
                                key="Contact.Person.new"/></html:button>
                    </c:if>
                    <c:if test="${allowCreation=='0' || allowCreation=='2'}">
                        <html:button property="" styleClass="button" onclick="openNewContact('0');"><fmt:message
                                key="Contact.Organization.new"/></html:button>
                    </c:if>
                </app2:checkAccessRight>
            </td>

        </html:form>
    </TR>
    <tr>
        <td colspan="3" align="center" class="alpha">

            <fanta:alphabet action="${url}?allowCreation=${param.allowCreation}" parameterName="name1"
                            onClick="jump(this);return false;"/>
        </td>
    </tr>
    <tr>
        <td colspan="3" align="center">
            <br>
            <c:choose>
                <c:when test="${param.columnName != null}">
                    <c:set var="columnName" value="${param.columnName}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="columnName" value="addressName"/>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${param.columnId != null}">
                    <c:set var="columnId" value="${param.columnId}"/>
                </c:when>
                <c:otherwise>
                    <c:set var="columnId" value="addressId"/>
                </c:otherwise>
            </c:choose>

            <fanta:table width="100%" id="contact" action="${url}" imgPath="${baselayout}">
                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <fanta:actionColumn name="" title="Common.select"
                                        useJScript="true"
                                        action="javascript:select('${app2:mapElement(contact, columnId)}', '${app2:jscriptEncode(app2:mapElement(contact, columnName))}');"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        image="${baselayout}/img/import.gif"/>
                </fanta:columnGroup>
                <fanta:dataColumn name="${columnName}" styleClass="listItem" title="Contact.name"
                                  headerStyle="listHeader" width="50%" orderable="true" maxLength="40"/>
                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="7%" orderable="true"/>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="7%" orderable="true"/>
                <fanta:dataColumn name="cityName" styleClass="listItem2" title="Contact.city" headerStyle="listHeader"
                                  width="20%" orderable="true"/>
            </fanta:table>
        </td>
    </tr>
</table>

