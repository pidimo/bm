<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">
    <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
        <li>
            <app:link page="/support/ArticleList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Support.articleList"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <app2:checkAccessRight functionality="QUESTION" permission="VIEW">
        <li>
            <app:link page="/support/QuestionList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Question.questionList"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <app2:checkAccessRight functionality="CASE" permission="VIEW">
        <li>
            <app:link page="/support/CaseList.do" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="SupportCase.title.search"/>
            </app:link>
        </li>
    </app2:checkAccessRight>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/support/Report/ArticleList.do"
                                contextRelative="true"
                                titleKey="Support.Report.ArticleList"
                                functionality="ARTICLE" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/support/Report/QuestionList.do"
                                contextRelative="true"
                                titleKey="Support.Report.QuestionList"
                                functionality="QUESTION" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/support/Report/CaseList.do"
                                contextRelative="true"
                                titleKey="Support.Report.CaseList"
                                functionality="CASE" permission="VIEW"/>
        <tags:bootstrapReportsMenu module="support"/>
    </tags:bootstrapMenu>
</ul>
