<%@include file="/Includes.jsp" %>

  <c:set var="oneFilterConditions" value="${dto.filterConditions}" />
  <c:set var="numCond" value="${dto.numCond}" />

  <html:form action="${action}" focus="dto(filterName)">

        
        <html:hidden property="dto(op)" value="${op}"/>
        <c:if test="${'update' == op || op=='delete'}">
            <html:hidden property="dto(filterId)"/>
        </c:if>

      <table width="60%" height="77" border="0" cellpadding="0" cellspacing="0" align="center" class="container">
        <tr>
          <td colspan="3" class="title">
              <c:out value="${title}"/>
          </td>
        </tr>
        <tr>
          <td width="30%" class="label" ><fmt:message   key="Webmail.filter.name"/></td>
          <td width="30%" class="contain" colspan="2" >
            <app:text property="dto(filterName)" styleClass="middleText" maxlength="50" tabindex="1" view="${op == 'delete'}"/>
          </td>

        </tr>
        <tr>
          <td class="labelGreen" ><fmt:message key="Webmail.filter.message"/></td>
          <td class="contain" colspan="2"></td>
        </tr>

        <c:set var="conditionNameKeys" value="${app2:getConditionNameKeys(pageContext.request)}"/>
        <c:set var="conditionsKeys" value="${app2:getConditionsKeys(pageContext.request)}"/>
        <c:forEach var="listCondNameKeys" items="${conditionNameKeys}" varStatus="status" >
            <tr>
                  <html:hidden property="identifyCondition" value="${status.index}" styleId="identifyCondition_Id${status.index}" />
                  <c:set var="band" value="0"/>
                      <c:forEach begin="0" end="${numCond}" varStatus="condStatus"> <!-- only if is update -->
                        <c:if test="${oneFilterConditions[condStatus.current].conditionNameKey == listCondNameKeys.value}">

                             <td width="30%" class="labelTab">
                                    <c:out value="${listCondNameKeys.label}"/>
                                    <html:hidden property="dto(namekey${status.index})" value="${listCondNameKeys.value}" styleId="namekey${status.index}_Id"/>
                                    <html:hidden property="dto(conditionId${status.index})" value="${oneFilterConditions[condStatus.current].conditionId}" styleId="conditionId${status.index}_Id"/>
                             </td>

                              <td width="20%" class="contain">

                                    <c:choose>
                                        <c:when test="${op=='delete'}">
                                            <c:forEach var="listCondKeys" items="${conditionsKeys}" >
                                                <c:if test="${listCondKeys.value == oneFilterConditions[condStatus.current].conditionKey }">
                                                     <c:out value="${listCondKeys.label}"/>
                                                </c:if>
                                            </c:forEach>
                                        </c:when>

                                        <c:otherwise>
                                            <html:select property="dto(conditionkey${status.index})" value="${oneFilterConditions[condStatus.current].conditionKey}" styleClass="shortSelect" tabindex="1" >
                                               <html:option value="" /> 
                                               <c:forEach var="listCondKeys" items="${conditionsKeys}">
                                                    <html:option value="${listCondKeys.value}" ><c:out value="${listCondKeys.label}"/> </html:option>
                                               </c:forEach>
                                            </html:select>
                                        </c:otherwise>
                                    </c:choose>
                              </td>
                              <td width="30%" class="contain">
                                    <app:text property="dto(text${status.index})" value="${oneFilterConditions[condStatus.current].conditionText}" styleClass="middleText" maxlength="50" tabindex="1" view="${op == 'delete'}"/>
                              </td>

                              <c:set var="band" value="1"/>

                        </c:if>
                      </c:forEach>

                  <c:if test="${band==0}">
                          <td width="30%" class="labelTab" >
                                 <c:out value="${listCondNameKeys.label}"/>
                                 <html:hidden property="dto(namekey${status.index})" value="${listCondNameKeys.value}" styleId="namekey${status.index}_Id"/>
                                 <html:hidden property="dto(conditionId${status.index})" value="null" styleId="conditionId${status.index}_Id"/>
                          </td>

                          <td width="20%" class="contain">

                                <c:if test="${op != 'delete'}">
                                        <html:select property="dto(conditionkey${status.index})" styleClass="shortSelect" tabindex="1" >
                                           <html:option value="" /> 
                                           <c:forEach var="listCondKeys" items="${conditionsKeys}" >
                                                <html:option value="${listCondKeys.value}" ><c:out value="${listCondKeys.label}"/> </html:option>
                                           </c:forEach>
                                        </html:select>
                                </c:if>

                          </td >
                          <td width="30%" class="contain">
                            <app:text property="dto(text${status.index})" styleClass="middleText" maxlength="50" tabindex="1"   view="${op == 'delete'}"/>
                          </td>
                  </c:if>

             </tr>
        </c:forEach>
        <tr>
          <td class="labelGreen" ><fmt:message   key="Webmail.filter.then"/></td>
          <td class="contain" colspan="2"></td>
        </tr>
        <tr>
          <td width="30%" class="label" ><fmt:message   key="Webmail.filter.movetomessage"/></td>
          <td width="30%" class="contain" colspan="2" >

          <c:choose>
            <c:when test="${dto.userFolderList != null}">
                <c:set var="userFolderList" value="${dto.userFolderList}"/>
            </c:when>
            <c:otherwise>
                <c:set var="userFolderList" value="${formUserFolders}"/>
            </c:otherwise>
          </c:choose>

          <%--set folders--%>
          <c:forEach var="folder" items="${userFolderList}" >
            <html:hidden property="userFolder" value="${folder.userFolderId}<s>${folder.userFolder}" styleId="userFolder_${folder.userFolderId}_${folder.userFolder}_Id"/>
          </c:forEach>
          <c:choose>
                <c:when test="${op=='delete'}">
                           <c:forEach var="folder" items="${userFolderList}" >
                              <c:if test="${dto.folderId == folder.userFolderId }">
                                   <c:choose >
                                       <c:when test="${folder.userFolder == 'Webmail.folder.trash'}">
                                            <fmt:message key="${folder.userFolder}"/>
                                       </c:when>
                                       <c:otherwise >
                                            <c:out value="${folder.userFolder}"/>
                                       </c:otherwise>
                                   </c:choose>
                              </c:if>
                           </c:forEach>
                </c:when>

                <c:otherwise>
                       <html:select property="dto(folderId)" styleClass="shortSelect" tabindex="10" >
                           <html:option value="" />
                           <c:forEach var="folder" items="${userFolderList}" >
                               <c:choose >
                                   <c:when test="${folder.userFolder == 'Webmail.folder.trash'}">
                                        <html:option value="${folder.userFolderId}" > <fmt:message key="${folder.userFolder}"/> </html:option>
                                   </c:when>
                                   <c:otherwise >
                                        <html:option value="${folder.userFolderId}" > <c:out value="${folder.userFolder}"/> </html:option>
                                   </c:otherwise>
                               </c:choose>
                           </c:forEach>
                       </html:select>
                </c:otherwise>
          </c:choose>

          </td>
        </tr>

      </table>

      <table cellSpacing=0 cellPadding=3 width="60%" border=0 align="center">
              <tr>
                   <td class="button">
                      <app2:securitySubmit operation="${op}" functionality="WEBMAILFILTER" styleClass="button" tabindex="11" ><c:out value="${button}"/></app2:securitySubmit>
                      <c:if test="${op == 'create'}" >
                          <app2:securitySubmit operation="${op}" functionality="WEBMAILFILTER" styleClass="button" property="SaveAndNew" tabindex="12" ><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
                      </c:if>
                      <html:cancel styleClass="button" tabindex="13" ><fmt:message   key="Common.cancel"/></html:cancel>
                  </td>
              </tr>
      </table>

  </html:form>
