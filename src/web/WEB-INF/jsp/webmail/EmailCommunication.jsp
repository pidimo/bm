<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.webmail.form.CommunicationForm" %>
<%@ include file="/Includes.jsp" %>

<c:import url="/WEB-INF/jsp/webmail/ReturnToMailTrayUrlFragment.jsp"/>
<tags:bootstrapModalPopup styleId="addContact_popup_id" isLargeModal="true"
                          modalTitleKey="Contact.search.ContactsOrContactPerson"/>
<tags:initBootstrapSelectPopup/>
<script language="JavaScript">
    <!--
    function selectPopup(url, searchName, submitOnSelect, w, h, scroll) {
        autoSubmit = submitOnSelect;
        ;
        launchBootstrapPopup("addContact_popup_id", url, searchName, autoSubmit);
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
<style type="text/css">
    .radio, .checkbox {
        margin-top: 1px;
        margin-bottom: 1px;
    }
</style>

<c:set var="person" value="<%=ContactConstants.ADDRESSTYPE_PERSON%>"/>
<c:set var="organization" value="<%=ContactConstants.ADDRESSTYPE_ORGANIZATION%>"/>
<app:url value="/Mail/SearchAddress.do" var="search"/>
<html:form action="${action}" styleId="communicationForm" styleClass="">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(size)" value="${dto.size}"/>
    <html:hidden property="dto(mailId)" value="${dto.mailId}"/>
    <html:hidden property="dto(email)" value="${dto.email}"/>
    <html:hidden property="dto(inOut)" value="${dto.inOut}"/>
    <fieldset>
        <legend class="title">
            <fmt:message key="Webmail.mailCommunications.plural"/>
        </legend>
        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Common.email"/>
            </label>

            <div class="${app2:getFormGroupClasses()}">
                <div class="${app2:getFormContainClasses(null)}">
                        ${dto.email}
                </div>
            </div>
        </div>
    </fieldset>
    <legend class="title">
        <fmt:message key="Webmail.mailCommunications.create"/>
    </legend>
    <div class="table-responsive">
        <table class="${app2:getFantabulousTableClases()}">
            <tr>
                <th class="listHeader" width="10%">
                    <fmt:message key="Webmail.mailCommunications.relate"/>
                </th>
                <th class="listHeader" width="45%">
                    <fmt:message key="Webmail.mailCommunications.name"/>
                </th>
                <th class="listHeader" width="45%">
                    <fmt:message key="Webmail.mailCommunications.contactPersonOf"/>
                </th>
            </tr>
            <c:set var="j" value="0"/>
            <c:forEach var="item" items="${dto.webmailCommunications}" varStatus="counter">
                <c:set var="j" value="${counter.count}"/>
                <tr class="listRow">
                    <td class="listItemCenter">
                        <c:if test="${null != item.contactId}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <input type="checkbox" name="dto(checkBox_${counter.count})" id="box_${j}"
                                           value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                                           disabled="disabled" checked="checked" class="">
                                    <label for="box_${j}"></label>
                                </div>
                            </div>
                            <html:hidden property="dto(checkBox_${counter.count})"
                                         value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}_hidden=true"/>
                        </c:if>
                        <c:if test="${null == item.contactId && null == dto.read}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(checkBox_${counter.count})" styleId="box_${j}"
                                                   value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                                                   styleClass=""/>
                                    <label for="box_${j}"></label>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${null == item.contactId && null != dto.read}">
                            <div class="radiocheck">
                                <div class="checkbox checkbox-default">
                                    <html:checkbox property="dto(checkBox_${counter.count})" styleId="box_${j}"
                                                   value="addressId=${item.address.addressId}_contactPersonId=${item.chief.contactPersonId}_contactId=${item.contactId}"
                                                   styleClass=""/>
                                    <label for="box_${j}"></label>
                                </div>
                            </div>
                        </c:if>
                    </td>

                    <td class="listItem">
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
                    <td class="listItem">
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
                <tr class="listRow">
                    <td colspan="3" class="listItem">
                        <fmt:message key="Webmail.mailCommunications.notFoundContacts"/>
                    </td>
                </tr>
            </c:if>
        </table>
    </div>
    <legend class="title">
        <fmt:message key="Webmail.mailCommunications.create.addContacts"/>
    </legend>
    <div class="table-responsive">
        <table class="${app2:getFantabulousTableClases()}">
            <tr>
                <th class="listHeader" width="10%">
                    <fmt:message key="Webmail.mailCommunications.relate"/>
                </th>
                <th class="listHeader" width="20%">
                    <fmt:message key="Webmail.mailCommunications.addEmail"/>
                </th>
                <th class="listHeader" width="20%">
                    <fmt:message key="Telecom.telecomType"/>
                </th>
                <th class="listHeader" width="25%">
                    <fmt:message key="Webmail.mailCommunications.name"/>
                </th>
                <th class="listHeader" width="25%" colspan="2">
                    <fmt:message key="Webmail.mailCommunications.contactPersonOf"/>
                </th>
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


                <tr id="${i.count}" class="listRow" style="${display}">
                    <td class="listItemCenter">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(checkBoxContact_${i.count})"
                                               styleClass=""
                                               styleId="checkBox_${i.count}" value="true"/>
                                <label for="checkBox_${i.count}"></label>
                            </div>
                        </div>
                    </td>
                    <td class="listItemCenter">
                        <div class="radiocheck">
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(addEmail_${i.count})" value="true" styleClass="radio"
                                            onclick="javascript:enableTelecomTypeSelect(${i.count})"
                                            styleId="addEmailyes_${i.count}"/>
                                <label> <fmt:message key="Common.yes"/></label>
                            </div>
                            <div class="radio radio-default radio-inline">
                                <html:radio property="dto(addEmail_${i.count})" value="false" styleClass="radio"
                                            onclick="javascript:disableTelecomTypeSelect(${i.count})"
                                            styleId="addEmailno_${i.count}"/>
                                <label><fmt:message key="Common.no"/></label>
                            </div>
                        </div>
                    </td>
                    <td class="listItem">
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
                                    <c:set var="telecomTypes"
                                           value="${app2:getTelecomTypesOfEmails(pageContext.request)}"/>
                                    <html:hidden property="dto(telecomTypeName_${i.count})"
                                                 styleId="telecomTypeNameStyleId_${i.count}"/>
                                    <html:select property="dto(telecomTypeId_${i.count})"
                                                 styleClass="shortSelect ${app2:getFormSelectClasses()}"
                                                 onchange="javascript:setTelecomTypeName(this,${i.count})">
                                        <html:option value="">&nbsp;</html:option>
                                        <html:options collection="telecomTypes" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td class="listItem">
                        <html:hidden property="dto(addressId_${i.count})" styleId="addressStyleId_${i.count}"/>
                        <html:text property="dto(contact_${i.count})"
                                   styleId="addressNameStyleId_${i.count}"
                                   readonly="true"
                                   styleClass="text ${app2:getFormInputClasses()}"/>
                    </td>
                    <td class="listItem">
                        <html:hidden property="dto(contactPersonId_${i.count})"
                                     styleId="contactPersonStyleId_${i.count}"/>
                        <html:text property="dto(contactPerson_${i.count})"
                                   styleId="contactPersonNameStyleId_${i.count}"
                                   readonly="true"
                                   styleClass="text ${app2:getFormInputClasses()}"/>
                    </td>
                    <td class="listItem">
                        <html:button property="close"
                                     styleClass="minusButton" value="&nbsp"
                                     onclick="javascript:closediv(${i.count})"
                                     titleKey="Common.delete"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <div class="row col-xs-12">
        <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
            <html:submit property="dto(save)" styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                    key="Common.save"/></html:submit>
        </app2:checkAccessRight>
        <html:button property="copy"
                     onclick="javascript:selectPopup('${search}', 'searchAddress' , 'false', 755, 480, 1)"
                     styleClass="button ${app2:getFormButtonClasses()}">
            <fmt:message key="Webmail.mail.addContact"/>
        </html:button>
        <html:button property="" styleClass="button ${app2:getFormButtonCancelClasses()}"
                     onclick="location.href='${urlCancel}'">
            <fmt:message key="Common.cancel"/>
        </html:button>
    </div>
</html:form>

