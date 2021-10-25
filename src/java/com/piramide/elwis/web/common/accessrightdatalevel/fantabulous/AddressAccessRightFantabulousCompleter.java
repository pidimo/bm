package com.piramide.elwis.web.common.accessrightdatalevel.fantabulous;

import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.util.FantabulousStructureUtil;
import org.alfacentauro.fantabulous.common.Constants;
import org.alfacentauro.fantabulous.format.structure.Configuration;
import org.alfacentauro.fantabulous.structure.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class AddressAccessRightFantabulousCompleter extends AccessRightDataLevelFantabulousCompleter {

    @Override
    protected String getAliasStaticSuffix() {
        return "a";
    }

    @Override
    protected AccessRightDataLevelConstants.DataLevelAccessConfiguration getDataLevelAccessConfiguration() {
        return AccessRightDataLevelConstants.DataLevelAccessConfiguration.ADDRESS_ACCESS;
    }

    @Override
    protected ListStructure addSecurityConditions(Integer userId, ListStructure listStructure, String fkFieldAlias) {
        listStructure.addTable(getAddressTable());
        //relations
        Relation addressRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_ADD, FIELD_ADD_ADDRESSID), fkFieldAlias, listStructure);
        listStructure.addRelation(addressRelation);

        //conditions
        //is public condition
        Condition isPublicCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ADD, FIELD_ADD_ISPUBLIC)), Constants.OPERATOR_EQUAL, "1");
        //personal record user condition
        Condition personalCondition = personalRecordUserCondition(userId, listStructure);
        //user IN condition
        ListCondition userInListCondition = userInCondition(userId, listStructure);

        //compose conditions
        isPublicCondition = FantabulousStructureUtil.addOrCondition(isPublicCondition, personalCondition);
        isPublicCondition = FantabulousStructureUtil.addOrCondition(isPublicCondition, userInListCondition);

        GroupCondition groupCondition = new GroupCondition();
        groupCondition.setGroupCondition(isPublicCondition);

        //add conditions to end
        FantabulousStructureUtil.addAndConditionToListStructure(listStructure, groupCondition);

        return listStructure;
    }

    private Condition personalRecordUserCondition(Integer userId, ListStructure listStructure) {
        Condition personalCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ADD, FIELD_ADD_PERSONAL)), Constants.OPERATOR_EQUAL, "1");
        Condition recordUserCondition = FantabulousStructureUtil.composeSimpleCondition(listStructure.getField(composeAlias(PREFIX_ADD, FIELD_ADD_RECORDUSER)), Constants.OPERATOR_EQUAL, userId.toString());

        personalCondition = FantabulousStructureUtil.addAndCondition(personalCondition, recordUserCondition);

        GroupCondition groupCondition = new GroupCondition();
        groupCondition.setGroupCondition(personalCondition);

        return groupCondition;
    }

    private ListCondition userInCondition(Integer userId, ListStructure listStructure) {
        Field addressIdField = listStructure.getField(composeAlias(PREFIX_ADD, FIELD_ADD_ADDRESSID));
        ListStructure userINList = composeUserIdSecurityListStructure(addressIdField);

        ListCondition userInListCondition = new ListCondition();
        userInListCondition.setConstantReference(userId.toString());
        userInListCondition.setOperator(Constants.OPERATOR_IN);
        userInListCondition.setList(userINList);

        return userInListCondition;
    }

    private ListStructure composeUserIdSecurityListStructure(Field addressIdField) {
        ListStructure list = new ListStructure();
        list.setListName("accessRightUserIdList");

        list.addTable(getUserAddressAccessTable());
        list.addTable(getUserGroupTable());
        list.addTable(getUserOfGroupTable());

        list.setMainTable(getUserAddressAccessTable());

        //relations
        Relation groupRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_UG, FIELD_UG_USERGROUPID), composeAlias(PREFIX_UAA, FIELD_UAA_USERGROUPID), list);
        Relation userOfGroupRelation = FantabulousStructureUtil.composeLeftJoinRelation(composeAlias(PREFIX_UOFG, FIELD_UOFG_USERGROUPID), composeAlias(PREFIX_UG, FIELD_UG_USERGROUPID), list);
        list.addRelation(groupRelation);
        list.addRelation(userOfGroupRelation);

        //condition relation to address
        Field field1 = FantabulousStructureUtil.findField(list, composeAlias(PREFIX_UAA, FIELD_UAA_ADDRESSID));
        Condition condition = FantabulousStructureUtil.composeSimpleCondition(field1, Constants.OPERATOR_EQUAL, addressIdField);
        list.setCondition(condition);

        //columns
        Configuration conf = new Configuration();
        conf.addColumn(FantabulousStructureUtil.composeColumn(composeAlias(PREFIX_UOFG, FIELD_UOFG_USERID), list));

        list.setConfiguration(conf);

        return list;
    }

}
