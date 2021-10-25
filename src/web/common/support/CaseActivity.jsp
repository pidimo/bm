<%@ page import="com.piramide.elwis.web.contactmanager.form.CommunicationFieldValidatorUtil" %>
<%@ include file="/Includes.jsp" %>
<c:import url="/common/support/CommunicationHeaderActivity.jsp"/>
<fmt:message var="datePattern" key="datePattern" scope="request"/>

<c:set var="saveButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Save.getKey()%>" scope="page"/>
<c:set var="generateButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Generate.getKey()%>" scope="page"/>
<c:set var="sendButton" value="<%=CommunicationFieldValidatorUtil.FormButtonProperties.Send.getKey()%>" scope="page"/>

<app2:checkAccessRight functionality="${funcionality}" permission="UPDATE" var="deleteCommunicationPermission"/>
<app2:checkAccessRight functionality="${funcionality}" permission="UPDATE" var="updatePermission"/>

<c:set var="executeDocument"
       value="${(caseActivityForm.dtoMap.type == letter || caseActivityForm.dtoMap.type == fax)}" scope="request"/>

<c:set var="isEmail"
       value="${caseActivityForm.dtoMap.type == email}" scope="request"/>

<c:set var="isNewMail" value="${null == caseActivityForm.dtoMap['mailId'] || caseActivityForm.dtoMap['mailId']==''}" scope="request" />

<c:set var="hasCommunication"
       value="${caseActivityForm.dtoMap.commVersion != null && caseActivityForm.dtoMap.commVersion!='' }"/>

<%--isAssignedToThisUser:${isAssignedToThisUser} -isExternal:${isExternal} - externalOnlyView:${externalOnlyView} -
onlyView:${onlyView}<br>
executeDocument:${executeDocument} - hasCommunication:${hasCommunication} - isEmail:${isEmail}
<hr>
${caseActivityForm.dtoMap}
<hr>--%>

<html:form action="/CaseActivity/Update.do"   enctype="multipart/form-data" styleId="composeEmailId">
<html:hidden property="dto(changeCommunicationType)" value="false" styleId="changeCommunicationTypeId"/>
<html:hidden property="dto(op)" value="${op}" styleId="op"/>

<input type="hidden" name="comm" value="${rebuild == 'true' ? "s" : "n"}" id="comm" >
<input type="hidden" name="a_o" value="n" id="a_o" >

<c:if test="${!isAssignedToThisUser && hasCommunication && executeDocument}">
    <input type="hidden" name="dto(nosave)" value="s" id="nosave">
</c:if>
<input type="hidden" name="a_o" value="n" id="a_o">
<c:if test="${op == 'update'}">
    <html:hidden property="dto(userAssigned)" styleId="user"/>
    <html:hidden property="dto(version)"/>
    <html:hidden property="dto(activityId)" styleId="activity"/>
    <c:if test="${hasCommunication}">
        <html:hidden property="dto(contactId)"/>
    </c:if>
</c:if>

<table border="0" cellpadding="0" cellspacing="0" width="97%" align="center" class="container">
<tr>
<td colspan="4" class="button">
<c:if test="${updatePermission}">

    <c:if test="${(isEmail && isNewMail)}">
        <app2:securitySubmit tabindex="1" operation="update" functionality="COMMUNICATION" property="${sendButton}"
                             styleClass="button"><fmt:message key="Common.send"/></app2:securitySubmit>
    </c:if>

    <c:if test="${isAssignedToThisUser || (!onlyView && !isAssignedToThisUser)}">
        <c:if test="${!isEmail || (isEmail && !isNewMail)}">
            <app2:securitySubmit operation="update" functionality="${funcionality}" property="${saveButton}"
                                 styleClass="button" tabindex="1">  <c:out value="${button}"/></app2:securitySubmit>
        </c:if>
        <c:choose>
            <c:when test="${!isEmail || (isEmail && !isNewMail)}">
                <app2:securitySubmit operation="${op}" functionality="${funcionality}" onclick="acop('n_a')"
                                     property="${saveButton}" styleClass="button" tabindex="2">
                    <fmt:message key="${!isExternal?'SupportCaseActivity.nextActivity':'SupportCaseActivity.backToAssigner'}"/>
                </app2:securitySubmit>
            </c:when>
            <c:otherwise>
                <app2:securitySubmit operation="${op}" functionality="${funcionality}" onclick="acop('n_a')"
                                     property="${sendButton}" styleClass="button" tabindex="2">
                    <fmt:message key="${(!isExternal)?'SupportCaseActivity.sendNextActivity':'SupportCaseActivity.backToAssigner'}"/>
                </app2:securitySubmit>
            </c:otherwise>
        </c:choose>
    </c:if>

    <%--<c:if test="${op!='delete' && executeDocument && !isExternal}">--%>
    <c:if test="${op!='delete' && executeDocument && (!isExternal)}">
        <app2:securitySubmit operation="execute"  tabindex="3" functionality="CASEACTIVITY" styleClass="button"
                             property="${generateButton}" onclick="checkComm()">
            <fmt:message  key="Document.generate"/>
        </app2:securitySubmit>

        <app2:checkAccessRight functionality="CASEACTIVITY" permission="UPDATE">
            <c:set var="docInfo" value="${app2:getCommunicationDocumentInfo(caseActivityForm.dtoMap['contactId'])}"/>
            <c:if test="${not empty docInfo.freeTextId}">
                <app:url var="openDocUrl" value="contacts/Download.do?dto(type)=comm&dto(fid)=${docInfo.freeTextId}" contextRelative="true"/>
                <html:button property="" styleClass="button" onclick="location.href='${openDocUrl}'" tabindex="4">
                    <fmt:message key="Document.open"/>
                </html:button>
            </c:if>
        </app2:checkAccessRight>
    </c:if>

    <%--<c:if test="${hasCommunication && !isExternal && isAssignedToThisUser}">--%>
    <c:if test="${hasCommunication && !isExternal && isAssignedToThisUser}">
        <app2:securitySubmit  tabindex="5" operation="delete" functionality="COMMUNICATION" styleClass="button"
                              property="${saveButton}" onclick="checkComm(); acop('d_c')">
            <fmt:message  key="SupportCaseActivity.deleteCommunication"/>
        </app2:securitySubmit>
    </c:if>

</c:if>
<html:cancel styleClass="button" tabindex="7"><fmt:message key="Common.cancel"/></html:cancel>
</td>
</tr>
<tr>
    <td colspan="4" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td width="15%" class="label"><fmt:message key="Common.assignedFrom"/></td>
    <td width="35%" class="contain">
        <html:hidden property="dto(fromUserName)" write="true"/>
    </td>
    <td width="15%" class="label"><fmt:message key="WorkLevel.title"/></td>
    <td width="35%" class="contain">
    <html:hidden property="dto(isExternal)" value="${isExternal}" styleId="isExternal"/>
        <c:set var="supportWorkLevels" value="${app2:getSupportWorkLevel(pageContext.request)}"/>
        <html:select property="dto(workLevelId)" styleClass="middleSelect" readonly="${onlyView || (isExternal && isAssignedToThisUser)}" tabindex="8">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportWorkLevels" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td width="15%" class="label"><fmt:message key="SupportCaseActivity.assignedAt"/></td>
    <td width="35%" class="contain">

        <c:catch var="failDate">
        <fmt:formatDate value="${app2:intToDate(caseActivityForm.dtoMap.od)}" pattern="${datePattern}"/>
        </c:catch>
        <c:if test="${failDate!=null}">
            ${caseActivityForm.dtoMap.od}
        </c:if>
        <html:hidden property="dto(od)"/>
    </td>
    <td width="15%" class="label"><fmt:message key="State.title"/></td>
    <td width="35%" class="contain">
        <c:set var="supportStatus" value="${app2:getSupportStatus(pageContext.request)}"/>
        <html:select property="dto(stateId)" styleClass="middleSelect" readonly="${onlyView || (isExternal && isAssignedToThisUser)}" tabindex="10">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="supportStatus" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td width="15%" class="label"><fmt:message key="Common.assignedTo"/></td>
    <td width="35%" class="contain" colspan="3">
<%--readOnly="${onlyView || !isAssignedToThisUser || isExternal}"--%>
        <fmt:message var="externalUser" key="User.externalUser"/>
        <fmt:message var="internalUser" key="User.intenalUser"/>
        <html:hidden property="dto(closeDate)" />
        <fanta:select property="dto(toUserId)" listName="userBaseList" labelProperty="name"
                      valueProperty="id" styleClass="mediumSelect"
        readOnly="${(isExternal && caseActivityForm.dtoMap.redirectValidation != 'true')
                 || ((caseActivityForm.dtoMap.toUserId != sessionScope.user.valueMap.userId) && caseActivityForm.dtoMap.redirectValidation != 'true')
                 || (caseActivityForm.dtoMap.closeDate != null && caseActivityForm.dtoMap.closeDate != '')}"
                      module="/admin" value="${sessionScope.user.valueMap['userId']}" withGroups="true" tabIndex="11">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:group groupName="${externalUser}" columnDiscriminator="type" valueDiscriminator="0"/>
            <fanta:group groupName="${internalUser}" columnDiscriminator="type" valueDiscriminator="1"/>

        </fanta:select>
    </td>
</tr>
<c:if test="${!isExternal}">
    <tr>
        <td colspan="4" class="title">
            <fmt:message key="Communication.title"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="15%"><fmt:message key="Communication.type"/></td>
        <td class="contain" width="35%" colspan="3">
            <c:set var="types" value="${app2:defaultMediaTypes(pageContext.request)}"/>
            <html:select property="dto(type)" styleClass="mediumSelect" onchange="changeCommunicationType(this)"
                         tabindex="12" readonly="${onlyView || hasCommunication}" styleId="type">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="types" property="value" labelProperty="label" />
            </html:select>
        </td>
    </tr>

    <c:if test="${(caseActivityForm.dtoMap.type != '' && caseActivityForm.dtoMap.type != null)}">
        <c:import url="/common/support/CommunicationActivity.jsp"/>
    </c:if>

</c:if>
<tr>
    <td colspan="4" class="title">
        <fmt:message key="SupportCaseActivity.details"/>
    </td>
</tr>
<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="SupportCaseActivity.previousDetail"/><br>
        <html:hidden property="dto(parentDescription)"/>
        <c:set var="pd" value="${caseActivityForm.dtoMap.parentDescription}" scope="session"/>
        <iframe name="frame1" src="<c:url value="/common/support/PreviusDetail.jsp?var=pd" />"
                style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes" frameborder="1">
        </iframe>
            <%--<c:remove var="pd" scope="session"/>--%>
    </td>
</tr>

<tr>
    <td class="topLabel" colspan="4">
        <fmt:message key="SupportCaseActivity.currentDetail"/><br>
        <c:choose>
            <c:when test="${!onlyView}">
                <html:textarea property="dto(activityDescription)" styleId="description_text"
                               styleClass="mediumDetailHigh"
                               readonly="${onlyView}" tabindex="20"  style="height:240px;width:100%"/>
            </c:when>            
            <c:otherwise>

                <c:set var="ad" value="${caseActivityForm.dtoMap.activityDescription}" scope="session"/>
                <iframe name="frame2"  src="<c:url value="/common/support/PreviusDetail.jsp?var=ad" />"
                        style="width : 100%;height: 240px;background-color:#ffffff" scrolling="yes" frameborder="1">
                </iframe>
                <%--<c:remove var="ad" scope="session"/>--%>
            </c:otherwise>
        </c:choose>
    </td>
</tr>

<tr>
    <td colspan="4" class="button">
        <c:if test="${updatePermission}">

            <c:if test="${(isEmail && isNewMail)}">
                <app2:securitySubmit operation="update" functionality="COMMUNICATION" property="${sendButton}"
                                     styleClass="button" tabindex="30"><fmt:message key="Common.send"/></app2:securitySubmit>
            </c:if>

            <c:if test="${isAssignedToThisUser || (!onlyView && !isAssignedToThisUser)}">
                <c:if test="${!isEmail || (isEmail && !isNewMail)}">
                    <app2:securitySubmit operation="update" functionality="${funcionality}" property="${saveButton}"
                                         tabindex="30" styleClass="button"><c:out value="${button}"/></app2:securitySubmit>
                </c:if>
                <c:choose>
                    <c:when test="${!isEmail || (isEmail && !isNewMail)}">
                        <app2:securitySubmit operation="${op}" functionality="${funcionality}" onclick="acop('n_a')"
                                             property="${saveButton}" tabindex="31"
                                             styleClass="button"><fmt:message key="${!isExternal?'SupportCaseActivity.nextActivity':'SupportCaseActivity.backToAssigner'}"/>
                        </app2:securitySubmit>
                    </c:when>
                    <c:otherwise>
                        <app2:securitySubmit operation="${op}" functionality="${funcionality}" onclick="acop('n_a')"
                                             property="${sendButton}" tabindex="31"
                                             styleClass="button"><fmt:message key="${!isExternal?'SupportCaseActivity.sendNextActivity':'SupportCaseActivity.backToAssigner'}"/>
                        </app2:securitySubmit>
                    </c:otherwise>
                </c:choose>
            </c:if>

            <c:if test="${executeDocument && !isExternal}">
                <c:set var="msgStatus" value="Document.${dto.status=='1' ? 'viewDocument' : 'generate'}" scope="page"/>

                <app2:securitySubmit tabindex="32" operation="execute" functionality="CASEACTIVITY" styleClass="button"
                                     property="${generateButton}" onclick="checkComm()">
                        <fmt:message  key="Document.generate"/>
                </app2:securitySubmit>

                <app2:checkAccessRight functionality="CASEACTIVITY" permission="UPDATE">
                    <c:if test="${not empty docInfo.freeTextId}">
                        <html:button property="" styleClass="button" onclick="location.href='${openDocUrl}'" tabindex="33">
                            <fmt:message key="Document.open"/>
                        </html:button>
                    </c:if>
                </app2:checkAccessRight>

            </c:if>

            <c:if test="${hasCommunication && !isExternal && isAssignedToThisUser}">
                <app2:securitySubmit tabindex="34" operation="delete" functionality="COMMUNICATION" styleClass="button"
                                     property="${saveButton}" onclick="checkComm(); acop('d_c')">
                    <fmt:message key="SupportCaseActivity.deleteCommunication"/>
                </app2:securitySubmit>
            </c:if>

        </c:if>
        <html:cancel styleClass="button" tabindex="36"><fmt:message key="Common.cancel"/></html:cancel>
    </td>
</tr>
</table>
<html:hidden property="dto(isSupport)" value="${true}" styleId="iss"/>
<html:hidden property="dto(isDelete)" styleId="isDelete" />
</html:form>