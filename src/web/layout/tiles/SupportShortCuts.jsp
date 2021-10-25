<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td valign="middle" align="left" class="moduleShortCut" width="85%">

            <app2:checkAccessRight functionality="ARTICLE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/ArticleList.do" addModuleParams="false">
                    <fmt:message key="Support.articleList"/>
                </app:link>
            </app2:checkAccessRight>



            <app2:checkAccessRight functionality="QUESTION" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/QuestionList.do" addModuleParams="false">
                    <fmt:message key="Question.questionList"/>
                </app:link>
            </app2:checkAccessRight>


            <app2:checkAccessRight functionality="CASE" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/CaseList.do" addModuleParams="false">
                    <fmt:message key="SupportCase.title.search"/>
                </app:link>
            </app2:checkAccessRight>

                &nbsp;|


        </td>

        <td valign="middle" align="right" class="moduleShortCut" width="15%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Report/ArticleList.do" titleKey="Support.Report.ArticleList" functionality="ARTICLE" permission="VIEW" />
                <tags:pullDownMenuItem action="/Report/QuestionList.do" titleKey="Support.Report.QuestionList" functionality="QUESTION" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/CaseList.do" titleKey="Support.Report.CaseList" functionality="CASE" permission="VIEW"/>
                <tags:reportsMenu module="support"/>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>