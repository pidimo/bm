<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="ADDADDRESSTYPE_MAIN" value="<%= ContactConstants.AdditionalAddressType.MAIN.getConstant()%>"/>

<div class="${app2:getListWrapperClasses()}">
    <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="CREATE">
        <html:form action="/AdditionalAddress/Forward/Create.do">
            <div class="${app2:getFormGroupClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </div>
        </html:form>
    </app2:checkAccessRight>

    <fieldset>
        <legend class="title">
            <fmt:message key="AdditionalAddress.plural"/>
        </legend>
    </fieldset>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="additionalAddressList"
                     width="100%"
                     id="addAddress"
                     styleClass="${app2:getFantabulousTableClases()}"
                     action="AdditionalAddress/List.do"
                     imgPath="${baselayout}"
                     align="center">

            <c:set var="editLink"
                   value="AdditionalAddress/Forward/Update.do?dto(additionalAddressId)=${addAddress.additionalAddressId}&dto(name)=${app2:encode(addAddress.name)}&dto(op)=read"/>
            <c:set var="deleteLink"
                   value="AdditionalAddress/Forward/Delete.do?dto(additionalAddressId)=${addAddress.additionalAddressId}&dto(name)=${app2:encode(addAddress.name)}&dto(op)=read&dto(withReferences)=true"/>

            <c:set var="isMainAddAddress" value="${addAddress.additionalAddressType eq ADDADDRESSTYPE_MAIN}"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="VIEW">

                    <c:choose>
                        <c:when test="${isMainAddAddress}">
                            <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                            </fanta:actionColumn>
                        </c:when>
                        <c:otherwise>
                            <fanta:actionColumn name="edit" title="Common.update"
                                                action="${editLink}"
                                                styleClass="listItem"
                                                headerStyle="listHeader" width="50%"
                                                glyphiconClass="${app2:getClassGlyphEdit()}"/>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="ADDITIONALADDRESS" permission="DELETE">
                    <fanta:actionColumn name="delete" title="Common.delete"
                                        action="${deleteLink}"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>

            <c:choose>
                <c:when test="${isMainAddAddress}">
                    <fanta:dataColumn name="name"
                                      title="AdditionalAddress.name"
                                      styleClass="listItem"
                                      headerStyle="listHeader"
                                      orderable="true"
                                      width="20%" renderData="false">
                        <fmt:message key="AdditionalAddress.item.mainAddress"/>
                    </fanta:dataColumn>
                </c:when>
                <c:otherwise>
                    <fanta:dataColumn name="name"
                                      action="${editLink}"
                                      title="AdditionalAddress.name"
                                      styleClass="listItem"
                                      headerStyle="listHeader"
                                      orderable="true"
                                      width="20%"/>
                </c:otherwise>
            </c:choose>

            <fanta:dataColumn name="street"
                              title="AdditionalAddress.street"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              width="20%"/>
            <fanta:dataColumn name="houseNumber"
                              title="AdditionalAddress.houseNumber"
                              styleClass="listItem"
                              headerStyle="listHeader"
                              orderable="true"
                              width="15%"/>

            <fanta:dataColumn name="countryCode"
                              styleClass="listItem"
                              title="Contact.countryCode"
                              headerStyle="listHeader" width="10%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="zip"
                              styleClass="listItem"
                              title="Contact.zip"
                              headerStyle="listHeader" width="10%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="cityName"
                              styleClass="listItem"
                              title="Contact.city"
                              headerStyle="listHeader"
                              width="15%" orderable="true">
            </fanta:dataColumn>
            <fanta:dataColumn name="isDefault"
                              styleClass="listItem2"
                              title="AdditionalAddress.default"
                              headerStyle="listHeader"
                              width="5%" orderable="true" renderData="false">
                <c:choose>
                    <c:when test="${addAddress.isDefault == 1}">
                        <span class="${app2:getClassGlyphOk()}"></span>
                    </c:when>
                    <c:otherwise>
                        &nbsp;
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>
    </div>

    <div class="embed-responsive embed-responsive-16by9 col-xs-12">
        <iframe name="frame1"
                src="<app:url value="AddressRelation/List.do"/>"
                class="embed-responsive-item Iframe" scrolling="no" frameborder="0" id="iFrameId">
        </iframe>
    </div>
</div>
