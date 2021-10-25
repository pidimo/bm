<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="ADDRESSID" value="${dto.addressId}"/>
<c:set var="CONTACTPERSONID" value="${dto.contactPersonId}"/>

<app:url var="urlCancel" value="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${dto.mailGroupAddrId}"/>

<html:form action="${action}?dto(mailGroupAddrId)=${dto.mailGroupAddrId}" styleClass="form-horizontal">
    <html:hidden property="dto(mailGroupAddrId)"/>
    <div class="${app2:getFormClasses()}">
        <div id="AddressGroup.jsp" class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="name_id">
                        <fmt:message key="Webmail.mailGroupAddr.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <app:text property="dto(name)" styleId="name_id"
                                  styleClass="${app2:getFormInputClasses()} middleText" maxlength="80" tabindex="1"
                                  view="${true}"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}" for="addressGroupName_id">
                        <fmt:message key="Webmail.addressGroup.name"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <app:text property="dto(addressGroupName)" styleId="addressGroupName_id"
                                  styleClass="${app2:getFormInputClasses()} middleText" maxlength="80" tabindex="1"
                                  view="${true}"/>
                        <span class=" glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Webmail.addressGroup.email"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op=='delete')}">
                        <c:choose>
                            <c:when test="${CONTACTPERSONID>0}">
                                <app2:relationData table1="telecomtype" table2="telecom" column="telecomnumber"
                                                   column2="telecomid" id="address_mails" table1FkName="telecomtypeid"
                                                   table2PkName="telecomtypeid" table1SearchFieldName="type"
                                                   table1SearchFieldValue="'${TELECOMTYPE_EMAIL}'"
                                                   table2SearchFieldName="contactPersonId"
                                                   table2SearchFieldValue="${CONTACTPERSONID}"/>
                                <html:select property="dto(addressGroupEmailId)"
                                             styleClass="${app2:getFormSelectClasses()}"
                                             readonly="${op=='delete'}">
                                    <html:option value="" styleClass="list"><fmt:message
                                            key="Webmail.addressGroup.allMails"/></html:option>
                                    <c:forEach var="emails" items="${address_mails}">
                                        <html:option value="${emails.telecomid}" styleClass="list"><c:out
                                                value="${emails.telecomnumber}"/></html:option>
                                    </c:forEach>
                                </html:select>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:when test="${ADDRESSID>0}">
                                <app2:relationData table1="telecomtype" table2="telecom" column="telecomnumber"
                                                   column2="telecomid" id="address_mails" table1FkName="telecomtypeid"
                                                   table2PkName="telecomtypeid" table1SearchFieldName="type"
                                                   table1SearchFieldValue="'${TELECOMTYPE_EMAIL}'"
                                                   table2SearchFieldName="addressid"
                                                   table2SearchFieldValue="${ADDRESSID}"
                                                   isNull="contactpersonid"/>
                                <html:select property="dto(addressGroupEmailId)"
                                             styleClass="${app2:getFormSelectClasses()}"
                                             readonly="${op=='delete'}">
                                    <html:option value="" styleClass="list"><fmt:message
                                            key="Webmail.addressGroup.allMails"/></html:option>
                                    <c:forEach var="emails" items="${address_mails}">
                                        <html:option value="${emails.telecomid}" styleClass="list"><c:out
                                                value="${emails.telecomnumber}"/></html:option>
                                    </c:forEach>
                                </html:select>
                                <span class=" glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <html:hidden property="dto(op)" value="${op}"/>
                <html:hidden property="dto(addressId)"/>
                <html:hidden property="dto(contactPersonId)"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(addressGroupId)"/>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(save)"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     styleId="saveButtonId">
                    <c:out value="${button}"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="${op}" functionality="WEBMAILGROUP" property="dto(delete)"
                                     styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>

            <html:button property="" styleClass="${app2:getFormButtonCancelClasses()}"
                         onclick="location.href='${urlCancel}'">
                <fmt:message key="Common.cancel"/>
            </html:button>
        </div>
    </div>
</html:form>

<tags:jQueryValidation formName="addressGroupDefaultForm"/>