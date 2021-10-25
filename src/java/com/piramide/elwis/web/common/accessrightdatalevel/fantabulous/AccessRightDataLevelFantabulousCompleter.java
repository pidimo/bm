package com.piramide.elwis.web.common.accessrightdatalevel.fantabulous;

import com.piramide.elwis.web.common.accessrightdatalevel.AccessRightDataLevelConstants;
import com.piramide.elwis.web.common.accessrightdatalevel.structure.AccessRightList;
import com.piramide.elwis.web.common.accessrightdatalevel.util.AccessRightDataLevelUtil;
import com.piramide.elwis.web.common.util.FantabulousStructureUtil;
import org.alfacentauro.fantabulous.structure.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Complete fantabulous structure to manage access right data level
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public abstract class AccessRightDataLevelFantabulousCompleter implements AccessDataLevelFantabulousCompleter {
    protected Log log = LogFactory.getLog(this.getClass());

    public static final String TABLE_ADDRESS = "address";
    public static final String FIELD_ADD_ADDRESSID = "addressid";
    public static final String FIELD_ADD_PERSONAL = "personal";
    public static final String FIELD_ADD_RECORDUSER = "recorduser";
    public static final String FIELD_ADD_ISPUBLIC = "ispublic";

    public static final String TABLE_USERADDRESSACCESS = "useraddressaccess";
    public static final String FIELD_UAA_ADDRESSID = "addressid";
    public static final String FIELD_UAA_USERGROUPID = "usergroupid";

    public static final String TABLE_USERGROUP = "usergroup";
    public static final String FIELD_UG_USERGROUPID = "usergroupid";

    public static final String TABLE_USEROFGROUP = "userofgroup";
    public static final String FIELD_UOFG_USERGROUPID = "usergroupid";
    public static final String FIELD_UOFG_USERID = "userid";

    public static final String TABLE_ARTICLE = "article";
    public static final String FIELD_ART_ARTICLEID = "articleid";
    public static final String FIELD_ART_PUBLISHEDTO = "publishedto";
    public static final String FIELD_ART_CREATEUSERID = "createuserid";

    public static final String TABLE_USERARTICLEACCESS = "userarticleaccess";
    public static final String FIELD_UARTA_ARTICLEID = "articleid";
    public static final String FIELD_UARTA_USERGROUPID = "usergroupid";

    public static final String PREFIX_ADD = "01";
    public static final String PREFIX_UAA = "02";
    public static final String PREFIX_UG = "03";
    public static final String PREFIX_UOFG = "04";
    public static final String PREFIX_RELATION_FIELD = "05";
    public static final String PREFIX_ART = "06";
    public static final String PREFIX_UARTA = "07";

    private int processIndex;

    public AccessRightDataLevelFantabulousCompleter() {
        processIndex = 0;
    }

    protected abstract AccessRightDataLevelConstants.DataLevelAccessConfiguration getDataLevelAccessConfiguration();

    protected abstract ListStructure addSecurityConditions(Integer userId, ListStructure listStructure, String fkFieldAlias);

    public ListStructure completeByList(Integer userId, ListStructure listStructure) {

        if (listStructure.getType() == UnionListStructure.UNIONLIST_TYPE) {
            UnionListStructure unionListStructure = (UnionListStructure) listStructure;

            Iterator unionLists = unionListStructure.getList().iterator();
            while (unionLists.hasNext()) {
                ListStructure unionList = (ListStructure) unionLists.next();
                completeSingleList(userId, unionList);
            }
            listStructure = unionListStructure;
        } else {
            listStructure = completeSingleList(userId, listStructure);
        }
        return listStructure;
    }

    private ListStructure completeSingleList(Integer userId, ListStructure listStructure) {
        log.debug("Complete list access right data level... " + listStructure.getListName());

        AccessRightList accessRightList = AccessRightDataLevelUtil.findList(listStructure.getListName(), getDataLevelAccessConfiguration().getConstant());
        if (accessRightList != null) {

            List<String> fkFieldAliasList = AccessRightDataLevelUtil.splitValueAsList(accessRightList.getAliasField());
            for (String fkFieldAlias : fkFieldAliasList) {
                listStructure = addSecurityConditions(userId, listStructure, fkFieldAlias);
                processIndex++;
            }
            log.debug("Complete success...");
        }

        return listStructure;
    }

    public ListStructure completeByTable(Integer userId, ListStructure listStructure) {

        if (listStructure.getType() == UnionListStructure.UNIONLIST_TYPE) {
            UnionListStructure unionListStructure = (UnionListStructure) listStructure;

            Iterator unionLists = unionListStructure.getList().iterator();
            while (unionLists.hasNext()) {
                ListStructure unionList = (ListStructure) unionLists.next();
                completeByTableSingle(userId, unionList);
            }
            listStructure = unionListStructure;
        } else {
            listStructure = completeByTableSingle(userId, listStructure);
        }
        return listStructure;
    }

    private ListStructure completeByTableSingle(Integer userId, ListStructure listStructure) {
        log.debug("Complete by table access right data level... " + listStructure.getListName());

        com.piramide.elwis.web.common.accessrightdatalevel.structure.Table configTable;
        Table fantabulousTable = listStructure.getMainTable();

        configTable = AccessRightDataLevelUtil.findTable(fantabulousTable.getTableName(), getDataLevelAccessConfiguration().getConstant());
        if (configTable == null) {
            for (Iterator relations = listStructure.getRelations().iterator(); relations.hasNext();) {
                Relation relation = (Relation) relations.next();
                fantabulousTable = relation.getPk().getTable();
                configTable = AccessRightDataLevelUtil.findTable(fantabulousTable.getTableName(), getDataLevelAccessConfiguration().getConstant());
                if (configTable != null) {
                    break;
                }
            }
        }

        if (configTable != null) {
            String fieldName = configTable.getRelationField();
            String fkFieldAlias = composeAlias(PREFIX_RELATION_FIELD, fieldName);
            fantabulousTable.addField(composeDBField(fieldName, fkFieldAlias, DBTypes.INTEGER, false));

            listStructure = addSecurityConditions(userId, listStructure, fkFieldAlias);
            log.debug("Complete success...");
        }
        return listStructure;
    }

    protected Table getAddressTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_ADD, TABLE_ADDRESS));
        table.setTableName(TABLE_ADDRESS);

        table.addField(composeDBField(FIELD_ADD_ADDRESSID, composeAlias(PREFIX_ADD, FIELD_ADD_ADDRESSID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_ADD_PERSONAL, composeAlias(PREFIX_ADD, FIELD_ADD_PERSONAL), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_ADD_RECORDUSER, composeAlias(PREFIX_ADD, FIELD_ADD_RECORDUSER), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_ADD_ISPUBLIC, composeAlias(PREFIX_ADD, FIELD_ADD_ISPUBLIC), DBTypes.INTEGER, false));

        return table;
    }

    protected Table getUserAddressAccessTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_UAA, TABLE_USERADDRESSACCESS));
        table.setTableName(TABLE_USERADDRESSACCESS);

        table.addField(composeDBField(FIELD_UAA_ADDRESSID, composeAlias(PREFIX_UAA, FIELD_UAA_ADDRESSID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_UAA_USERGROUPID, composeAlias(PREFIX_UAA, FIELD_UAA_USERGROUPID), DBTypes.INTEGER, true));
        return table;
    }

    protected Table getUserGroupTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_UG, TABLE_USERGROUP));
        table.setTableName(TABLE_USERGROUP);

        table.addField(composeDBField(FIELD_UG_USERGROUPID, composeAlias(PREFIX_UG, FIELD_UG_USERGROUPID), DBTypes.INTEGER, true));
        return table;
    }

    protected Table getUserOfGroupTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_UOFG, TABLE_USEROFGROUP));
        table.setTableName(TABLE_USEROFGROUP);

        table.addField(composeDBField(FIELD_UOFG_USERGROUPID, composeAlias(PREFIX_UOFG, FIELD_UOFG_USERGROUPID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_UOFG_USERID, composeAlias(PREFIX_UOFG, FIELD_UOFG_USERID), DBTypes.INTEGER, true));
        return table;
    }

    protected Table getArticleTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_ART, TABLE_ARTICLE));
        table.setTableName(TABLE_ARTICLE);

        table.addField(composeDBField(FIELD_ART_ARTICLEID, composeAlias(PREFIX_ART, FIELD_ART_ARTICLEID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_ART_CREATEUSERID, composeAlias(PREFIX_ART, FIELD_ART_CREATEUSERID), DBTypes.INTEGER, false));
        table.addField(composeDBField(FIELD_ART_PUBLISHEDTO, composeAlias(PREFIX_ART, FIELD_ART_PUBLISHEDTO), DBTypes.INTEGER, false));

        return table;
    }

    protected Table getUserArticleAccessTable() {
        Table table = new Table();
        table.setTable(composeAlias(PREFIX_UARTA, TABLE_USERARTICLEACCESS));
        table.setTableName(TABLE_USERARTICLEACCESS);

        table.addField(composeDBField(FIELD_UARTA_ARTICLEID, composeAlias(PREFIX_UARTA, FIELD_UARTA_ARTICLEID), DBTypes.INTEGER, true));
        table.addField(composeDBField(FIELD_UARTA_USERGROUPID, composeAlias(PREFIX_UARTA, FIELD_UARTA_USERGROUPID), DBTypes.INTEGER, true));
        return table;
    }

    protected DBField composeDBField(String name, String alias, DBTypes dbTypes, boolean isPrimaryKey) {
        return FantabulousStructureUtil.composeDBField(name, alias, dbTypes, isPrimaryKey);
    }

    protected String composeAlias(String prefix, String name) {
        return name + String.valueOf(processIndex) + prefix + getAliasStaticSuffix();
    }

    protected abstract String getAliasStaticSuffix();
}
