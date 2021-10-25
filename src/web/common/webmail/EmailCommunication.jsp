<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.webmail.form.CommunicationForm" %>
<%@ include file="/Includes.jsp" %>

<c:import url="/common/webmail/ReturnToMailTrayUrlFragment.jsp"/>

<script language="JavaScript">
    <!--
    function selectPopup(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        var winx = (screen.width) / 2;
        var winy = (screen.height) / 2;
        var posx = winx - w / 2;
        var posy = winy - h / 2;
        searchWindow = window.open(url, searchName, 'resizable=yes,width=' + w + ',height=' + h + ',status,left=' + posx + ',top=' + posy + ',scrollbars=' + scroll);
        searchWindow.focus();
    }

    function selectField(addressIdKey, addressNameKey, addressId, addressName, contactPersonIdKey, contactPersonNameKey, contactPersonId, contactPersonName) {
        index = document.getElementById("idx").value;
        firstDeleted = document.getElementById("fIdx").value;

        if (null != firstDeleted && '' != firstDeleted) {
            index = firstDeleted;
            document.getElementById("fIdx").value = '';
        }

        document.getElementById(addressIdKey + '_' + index).value = addressId;
        document.getElementById(addressNameKey + '_' + index).value = unescape(addressName);
        document.getElementById(contactPersonIdKey + '_' + index).value = contactPersonId;
        document.getElementById(contactPersonNameKey + '_' + index).value = unescape(contactPersonName);
        document.getElementById("checkBox_" + index).checked = true;
        document.getElementById(index).style.display = "";

        searchWindow.close();
    }

    function incrementIdx() {
        var v = document.getElementById("idx").value;
        if (null == v)
            v = 0;
        document.getElementById("idx").value = (Math.round(v) + 1);
    }
    function setIndex() {
        var deleted = document.getElementById("dIdx").value;

        if (null == deleted || '' == deleted) {
            incrementIdx();
        } else {
            var flg = 'no';
            for (i = 1; i <= 30; i++) {
                var ev = "[" + i + "],";
                if (deleted.indexOf(ev) > -1) {
                    document.getElementById("fIdx").value = i;
                    deleted = deleted.replace(ev, "");
                    document.getElementById("dIdx").value = deleted;
                    flg = 'no';
                    break;
                } else {
                    flg = 'si';
                }
            }
            if ('si' == flg) {
                incrementIdx();
                document.getElementById("fIdx").value = '';
            }

        }
    }
    function closediv(i) {
        var cad = document.getElementById("dIdx").value;
        document.getElementById("dIdx").value = "[" + i + "]," + cad;
        clearValues(i);
        document.getElementById(i).style.display = "none";
    }
    function enableTelecomTypeSelect(i) {
        document.getElementById("telecomType_" + i).style.display = "";
    }
    function disableTelecomTypeSelect(i) {
        document.getElementById("telecomType_" + i).style.display = "none";
    }
    function clearValues(i) {
        document.getElementById("addressStyleId_" + i).value = '';
        document.getElementById("addressNameStyleId_" + i).value = '';
        document.getElementById("contactPersonStyleId_" + i).value = '';
        document.getElementById("contactPersonNameStyleId_" + i).value = '';
        document.getElementById("checkBox_" + i).checked = false;
        document.getElementById("addEmailyes_" + i).checked = false;
        document.getElementById("addEmailno_" + i).checked = true;
        document.getElementById("telecomType_" + i).style.display = "none";
    }

    function setTelecomTypeName(obj, i) {
        idx = obj.selectedIndex;
        option = obj.options[idx];
        document.getElementById("telecomTypeNameStyleId_" + i).value = option.text;
    }
    //-->
</script>


<c:set var="person" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<c:set var="organization" value="<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<app:url value="/Mail/SearchAddress.do" var="search"/>

<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}" styleId="communicationForm">

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(size)" value="${dto.size}"/>
<html:hidden property="dto(mailId)" value="${dto.mailId}"/>
<html:hidden property="dto(email)" value="${dto.email}"/>
<html:hidden property="dto(inOut)" value="${dto.inOut}"/>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <tr>
        <TD colspan="3" class="title"><fmt:message key="Webmail.mailCommunications.plural"/></TD>
    </tr>
    <tr>
        <td class="label" width="10%"><fmt:message key="Common.email"/></TD>
        <td class="contain" width="85%" colspan="2">${dto.email}</td>
    </tr>
    <tr>
        <td class="title" colspan="3"><fmt:message key="Webmail.mailCommunications.create"/></TD>
    </tr>
    <TR>
        <td class="label" width="10%"><fmt:message key="Webmail.mailCommunications.relate"/></TD>
        <td class="label" width="45%"><fmt:message key="Webmail.mailCommunications.name"/></TD>
        <td class="label" width="45%"><fmt:message key="Webmail.mailCommunications.contactPersonOf"/></TD>
    </TR>
    <c:set var="j" value="0"/>
    <c:forEach var="item" items="${dto.webmailCommunications}" varStatus="counter">
        <c:set var="j" value="${counter.count}"/>
        <tr>
            <td class="contain">
                <c:if test="${null != item.contactId}">
                    <input type="checkbox" name="dto(checkBox_${counter.count})" id="box"
                           value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                           disabled="disabled" checked="checked" class="radio">
                    <html:hidden property="dto(checkBox_${counter.count})"
                                 value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}_hidden=true"/>
                </c:if>
                <c:if test="${null == item.contactId && null == dto.read}">
                    <html:checkbox property="dto(checkBox_${counter.count})" styleId="box"
                                   value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                                   styleClass="radio"/>
                </c:if>

                <c:if test="${null == item.contactId && null != dto.read}">
                    <html:checkbox property="dto(checkBox_${counter.count})" styleId="box"
                                   value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                                   styleClass="radio"/>
                </c:if>
            </td>

            <td class="contain">
                <c:set var="name">
                    <c:if test="${item.address.addressType == person}">${item.address.name1}<c:if
                            test="${item.address.name2 != null}">, ${item.address.name2}</c:if></c:if>
                    <c:if test="${item.address.addressType == organization}">${item.address.name1}<c:if
                            test="${item.address.name2 != null}"> ${item.address.name2}<c:if
                            test="${item.address.name3 != null}"> ${item.address.name3}</c:if></c:if>
                    </c:if>
                </c:set>
                    ${name}
                <html:hidden property="dto(name_def_${counter.count})" value="${name}"
                             styleId="name_def_${counter.count}_Id"/>
            </td>
            <td class="contain">
                <c:set var="contactPerson">
                    <c:if test="${item.chief.addressType == person}">${item.chief.name1}<c:if
                            test="${item.chief.name2 != null}">, ${item.chief.name2}</c:if></c:if>
                    <c:if test="${item.chief.addressType == organization}">${item.chief.name1}<c:if
                            test="${item.chief.name2 != null}"> ${item.chief.name2}<c:if
                            test="${item.chief.name3 != null}"> ${item.chief.name3}</c:if></c:if></c:if>
                </c:set>
                    ${contactPerson}
                <html:hidden property="dto(contactPerson_def_${counter.count})" value="${contactPerson}"
                             styleId="contactPerson_def_${counter.count}_Id"/>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${j == 0}">
        <tr>
            <td colspan="3" class="contain"><fmt:message key="Webmail.mailCommunications.notFoundContacts"/></td>
        </tr>
    </c:if>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
    <tr>
        <td class="title" colspan="6"><fmt:message key="Webmail.mailCommunications.create.addContacts"/></TD>
    </tr>
    <tr>
        <td class="label" width="10%"><fmt:message key="Webmail.mailCommunications.relate"/></td>
        <td class="label" width="20%"><fmt:message key="Webmail.mailCommunications.addEmail"/></td>
        <td class="label" width="20%"><fmt:message key="Telecom.telecomType"/></td>
        <td class="label" width="25%"><fmt:message key="Webmail.mailCommunications.name"/></td>
        <td colspan="2" class="label" width="25%"><fmt:message key="Webmail.mailCommunications.contactPersonOf"/></td>

    </tr>

    <c:set var="telecomtypeDisplay" value="display:none"/>
    <c:set var="display" value="display:none"/>

    <html:hidden property="dto(visibleIndex)" styleId="idx"/>
    <html:hidden property="dto(hideIndex)" styleId="dIdx"/>
    <html:hidden property="dto(firstHidden)" styleId="fIdx"/>

    <c:forEach begin="1" end="30" varStatus="i">
        <c:set var="cad" value="[${i}]"/>
        <c:set var="count" value="${i.count}" scope="request"/>

        <%
            Integer count = new Integer(request.getAttribute("count").toString());
            CommunicationForm f = (CommunicationForm) request.getAttribute("communicationForm");

            if (null != f.getDto("addressId_" + count) && !"".equals(f.getDto("addressId_" + count).toString())) {
                request.setAttribute("hasValue", Boolean.valueOf(true));
            } else {
                request.setAttribute("hasValue", Boolean.valueOf(false));
            }

            Boolean addEmailtoContact = Boolean.valueOf(false);
            if (null != f.getDto("addEmail_" + count)) {
                addEmailtoContact = Boolean.valueOf(f.getDto("addEmail_" + count).toString());
            } else {
                f.setDto("addEmail_" + count, Boolean.valueOf(false));
            }

            request.setAttribute("addEmailToContact", addEmailtoContact);
        %>
        <c:choose>
            <c:when test="${fn:contains(dto.hideIndex,cad) == false && hasValue == true}">
                <c:set var="display" value=""/>
            </c:when>
            <c:otherwise>
                <c:set var="display" value="display:none"/>
            </c:otherwise>
        </c:choose>


        <tr id="${i.count}" style="${display}">
            <td class="contain">
                <html:checkbox property="dto(checkBoxContact_${i.count})"
                               styleClass="radio"
                               styleId="checkBox_${i.count}" value="true"/>
            </td>
            <td class="contain">
                <html:radio property="dto(addEmail_${i.count})" value="true" styleClass="radio"
                            onclick="javascript:enableTelecomTypeSelect(${i.count})"
                            styleId="addEmailyes_${i.count}"/>
                <fmt:message key="Common.yes"/>&nbsp;&nbsp;
                <html:radio property="dto(addEmail_${i.count})" value="false" styleClass="radio"
                            onclick="javascript:disableTelecomTypeSelect(${i.count})"
                            styleId="addEmailno_${i.count}"/>
                <fmt:message key="Common.no"/>&nbsp;&nbsp;
            </td>
            <td class="contain">
                <table>
                    <c:choose>
                        <c:when test="${addEmailToContact == true}">
                            <c:set var="telecomtypeDisplay" value=""/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="telecomtypeDisplay" value="display:none"/>
                        </c:otherwise>
                    </c:choose>

                    <tr id="telecomType_${i.count}" style="${telecomtypeDisplay}">
                        <td>
                            <c:set var="telecomTypes" value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                            <html:hidden property="dto(telecomTypeName_${i.count})"
                                         styleId="telecomTypeNameStyleId_${i.count}"/>
                            <html:select property="dto(telecomTypeId_${i.count})" styleClass="shortSelect"
                                         onchange="javascript:setTelecomTypeName(this, ${i.count})">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="telecomTypes" property="value" labelProperty="label"/>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </td>
            <td class="contain">
                <html:hidden property="dto(addressId_${i.count})" styleId="addressStyleId_${i.count}"/>
                <html:text property="dto(contact_${i.count})"
                           styleId="addressNameStyleId_${i.count}"
                           readonly="true"
                           styleClass="text"/>
            </td>
            <td class="contain">
                <html:hidden property="dto(contactPersonId_${i.count})" styleId="contactPersonStyleId_${i.count}"/>
                <html:text property="dto(contactPerson_${i.count})"
                           styleId="contactPersonNameStyleId_${i.count}"
                           readonly="true"
                           styleClass="text"/>
            </td>
            <td class="contain">
                <html:button property="close"
                             styleClass="minusButton" value="&nbsp"
                             onclick="javascript:closediv(${i.count})"
                             titleKey="Common.delete"/>
            </td>
        </tr>
    </c:forEach>
</table>


<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                <html:submit property="dto(save)" styleClass="button"><fmt:message key="Common.save"/></html:submit>
            </app2:checkAccessRight>
            <html:button property="copy"
                         onclick="javascript:selectPopup('${search}', 'searchAddress' , 'false', 755, 480, 1)"
                         styleClass="button">
                <fmt:message key="Webmail.mail.addContact"/>
            </html:button>
            <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </TD>
    </TR>
</table>
</html:form>
</td>
</tr>
</table>
