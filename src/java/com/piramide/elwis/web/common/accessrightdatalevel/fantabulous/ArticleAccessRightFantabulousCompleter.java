package com.piramide.elwis.web.common.accessrightdatalevel.fantabulous;

import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.util.FantabulousStructureUtil;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.format.structure.Configuration;
import org.alfacentauro.fantabulous.structure.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class ArticleAccessRightFantabulousCompleter extends AccessRightDataLevelFantabulousCompleter {

    @Override
    protected String getAliasStaticSuffix() {
        return "b";
    }

    @Override
    protected AccessRightDataLevelConstants.DataLevelAccessConfiguration getDataLevelAccessConfiguration() {
        return AccessRightDataLevelConstants.DataLevelAccessConfiguration.ARTICLE_ACCESS;
    }

    @Override
    protected ListStructure addSecurityConditions(Integer userId, ListStructure listStructure, String fkFieldAlias) {
        listStructure.addTable(getArticleTable());
        //relations
        Relation articleRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_ART, FIELD_ART_ARTICLEID), fkFieldAlias, listStructure);
        listStructure.addRelation(articleRelation);

        //conditions
        //is all user condition
        Condition allUserCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ART, FIELD_ART_PUBLISHEDTO)), Constants.OPERATOR_EQUAL, SupportConstants.ArticlePublishedToStatus.ALL_USERS.getConstantAsString());
        //creator user condition
        Condition creatorUserCondition = creatorUserCondition(userId, listStructure);
        //user IN condition
        ListCondition userInListCondition = userInCondition(userId, listStructure);

        //compose conditions
        allUserCondition = FantabulousStructureUtil.addOrCondition(allUserCondition, creatorUserCondition);
        allUserCondition = FantabulousStructureUtil.addOrCondition(allUserCondition, userInListCondition);

        GroupCondition groupCondition = new GroupCondition();
        groupCondition.setGroupCondition(allUserCondition);

        //add conditions to end
        FantabulousStructureUtil.addAndConditionToListStructure(listStructure, groupCondition);

        return listStructure;
    }

    private Condition creatorUserCondition(Integer userId, ListStructure listStructure) {
        Condition creatorUserCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ART, FIELD_ART_PUBLISHEDTO)), Constants.OPERATOR_EQUAL, SupportConstants.ArticlePublishedToStatus.CREATOR_USER.getConstantAsString());
        Condition currentUserCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ART, FIELD_ART_CREATEUSERID)), Constants.OPERATOR_EQUAL, userId.toString());

        creatorUserCondition = FantabulousStructureUtil.addAndCondition(creatorUserCondition, currentUserCondition);

        GroupCondition groupCondition = new GroupCondition();
        groupCondition.setGroupCondition(creatorUserCondition);

        return groupCondition;
    }

    private ListCondition userInCondition(Integer userId, ListStructure listStructure) {
        Field articleIdField = listStructure.getField(composeAlias(PREFIX_ART, FIELD_ART_ARTICLEID));
        ListStructure userINList = composeUserIdSecurityListStructure(articleIdField);

        ListCondition userInListCondition = new ListCondition();
        userInListCondition.setConstantReference(userId.toString());
        userInListCondition.setOperator(Constants.OPERATOR_IN);
        userInListCondition.setList(userINList);

        return userInListCondition;
    }

    private ListStructure composeUserIdSecurityListStructure(Field articleIdField) {
        ListStructure list = new ListStructure();
        list.setListName("articleAccessRightUserIdList");

        list.addTable(getUserArticleAccessTable());
        list.addTable(getUserGroupTable());
        list.addTable(getUserOfGroupTable());

        list.setMainTable(getUserArticleAccessTable());

        //relations
        Relation groupRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_UG, FIELD_UG_USERGROUPID), composeAlias(PREFIX_UARTA, FIELD_UARTA_USERGROUPID), list);
        Relation userOfGroupRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_UOFG, FIELD_UOFG_USERGROUPID), composeAlias(PREFIX_UG, FIELD_UG_USERGROUPID), list);
        list.addRelation(groupRelation);
        list.addRelation(userOfGroupRelation);

        //condition relation to article
        Field field1 = FantabulousStructureUtil.findField(list, composeAlias(PREFIX_UARTA, FIELD_UARTA_ARTICLEID));
        Condition condition = FantabulousStructureUtil.composeSimpleCondition(field1, Constants.OPERATOR_EQUAL, articleIdField);
        list.setCondition(condition);

        //columns
        Configuration conf = new Configuration();
        conf.addColumn(FantabulousStructureUtil.composeColumn(composeAlias(PREFIX_UOFG, FIELD_UOFG_USERID), list));

        list.setConfiguration(conf);

        return list;
    }

}
