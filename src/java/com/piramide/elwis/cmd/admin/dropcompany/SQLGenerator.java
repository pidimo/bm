package com.piramide.elwis.cmd.admin.dropcompany;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SQLGenerator {
    private Log log = LogFactory.getLog(SQLGenerator.class);

    private final String token = "COMPANY_ID";
    private String sqlCache = "";

    public static SQLGenerator i = new SQLGenerator();

    private List<Table> tables;
    private List<Table> stack;

    private List<String> deletedTables = new ArrayList<String>();

    private static enum SpecialTables {
        Address("address"),
        Company("company");

        private String tableName;

        SpecialTables(String name) {
            this.tableName = name;
        }

        public String getTableName() {
            return tableName;
        }

        public static boolean contain(String name) {
            return Address.getTableName().equals(name) ||
                    Company.getTableName().equals(name);
        }
    }


    private SQLGenerator() {
    }

    public void initialize(ResultSet rsTables, DatabaseMetaData dbmdt) {
        tables = new ArrayList<Table>();
        stack = new ArrayList<Table>();
        log.info("-> Initialize SQL to Delete Companies");
        long startTime = new Date().getTime();
        List<String> elements = getTableNames(rsTables, dbmdt);

        buildStructure(dbmdt, elements, false);
        this.sqlCache = buildSQL(dbmdt);
        long endTime = new Date().getTime();

        log.info("-> Initialize SQL to Delete Companies in " + (endTime - startTime) + " ms OK");
    }

    private List<String> getTableNames(ResultSet rsTables, DatabaseMetaData dbmdt) {
        List<String> tableNames = new ArrayList<String>();
        tableNames.add(0, SpecialTables.Company.getTableName());
        tableNames.add(1, SpecialTables.Address.getTableName());
        try {
            while (rsTables.next()) {
                String tableName = rsTables.getString("TABLE_NAME");
                if (!tableNames.contains(tableName) && hasCompanyId(dbmdt, tableName)) {
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }

    private boolean hasCompanyId(DatabaseMetaData dbmdt, String tableName) {
        try {
            ResultSet columns = dbmdt.getColumns(null, null, tableName, "%companyid%");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                if ("companyid".equals(columnName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<String> getFKColumns(String tableName, DatabaseMetaData dbmdt) {
        List<String> relations = new ArrayList<String>();
        try {
            ResultSet foreignKeys = dbmdt.getExportedKeys(null, null, tableName);
            while (foreignKeys.next()) {
                String FKTableName = foreignKeys.getString("FKCOLUMN_NAME");
                relations.add(FKTableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relations;
    }

    private boolean isRegistredFK(String columnName, String tableName, DatabaseMetaData dbmdt) {
        try {
            ResultSet keys = dbmdt.getImportedKeys(null, null, tableName);
            while (keys.next()) {
                String fkName = keys.getString("FKCOLUMN_NAME");
                if (fkName.equals(columnName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean settingUpAsNull(String columnName, String tableName, DatabaseMetaData dbmdt) {
        try {
            ResultSet keys = dbmdt.getColumns(null, null, tableName, "%" + columnName + "%");

            while (keys.next()) {
                String dbColumnName = keys.getString("COLUMN_NAME");
                if (dbColumnName.equals(columnName)) {
                    String isNull = keys.getString("NULLABLE");
                    if ("1".equals(isNull)) {
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String> getExportedKeys(String tableName, DatabaseMetaData dbmdt) {
        List<String> relations = new ArrayList<String>();
        try {
            ResultSet foreignKeys = dbmdt.getExportedKeys(null, null, tableName);
            while (foreignKeys.next()) {
                String FKTableName = foreignKeys.getString("FKTABLE_NAME");
                relations.add(FKTableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relations;
    }

    private void buildStructure(DatabaseMetaData dbmdt, List<String> tableNames, boolean isFKTables) {
        for (String tableName : tableNames) {
            Table table = searchInStack(tableName);
            if (null != table || (isFKTables && SpecialTables.contain(tableName))) {
                continue;
            } else {
                table = new Table();
                table.setName(tableName);
                stack.add(table);
            }

            if (tables.contains(table)) {
                continue;
            }

            List<String> fkTables = getExportedKeys(tableName, dbmdt);
            if (!fkTables.isEmpty()) {
                table.addReferences(fkTables);
                buildStructure(dbmdt, fkTables, true);
                tables.add(table);
            } else {
                tables.add(table);
            }
        }
    }

    private String getIdentifierQuoteString(DatabaseMetaData dbmdt) {
        String identifierQuoteString = "";
        try {
            identifierQuoteString = dbmdt.getIdentifierQuoteString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return identifierQuoteString;
    }

    public List<Table> getTables() {
        return tables;
    }

    private Table searchInStack(String tableName) {
        for (Table table : stack) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }

        return null;
    }

    public String buildSQL(int companyId, ResultSet rsTables, DatabaseMetaData dbmdt) {
        String newSQL;
        if ("".equals(this.sqlCache)) {
            initialize(rsTables, dbmdt);
        }

        newSQL = this.sqlCache.replaceAll(token, String.valueOf(companyId));
        return newSQL;
    }

    private String buildSQL(DatabaseMetaData dbmdt) {
        final String DELETE = "DELETE FROM ";
        final String WHERE = " WHERE companyid=" + token + ";";

        final String IDENTIFIER_QUOTE_STRING = getIdentifierQuoteString(dbmdt);

        StringBuilder sql = new StringBuilder();
        if (tables.isEmpty()) {
            throw new RuntimeException("-> you must first invoque initialize(ResultSet, DatabaseMetaData) Method ");
        }

        for (Table table : tables) {

            if (SpecialTables.Company.getTableName().equals(table.getName())) {
                continue;
            }

            if (table.getReferences().isEmpty()) {
                sql.append(DELETE).
                        append(IDENTIFIER_QUOTE_STRING).
                        append(table.getName()).
                        append(IDENTIFIER_QUOTE_STRING).
                        append(WHERE);
                deletedTables.add(table.getName());
            } else {
                String updateSQL = buildUpdateSQL(table.getReferences(), table.getName(), dbmdt,
                        IDENTIFIER_QUOTE_STRING);
                if (!"".equals(updateSQL)) {
                    sql.append(updateSQL);
                }
                if (SpecialTables.Address.getTableName().equals(table.getName())) {
                    sql.append(DELETE).append(IDENTIFIER_QUOTE_STRING).
                            append(table.getName()).
                            append(IDENTIFIER_QUOTE_STRING).
                            append(" WHERE companyid=").append(token).
                            append(" AND ").
                            append(" addressid<>").append(token).append(";");
                    sql.append("UPDATE ").
                            append(IDENTIFIER_QUOTE_STRING).
                            append(table.getName()).
                            append(IDENTIFIER_QUOTE_STRING).
                            append(" SET ").
                            append("waydescriptionid=null, ").
                            append("bankaccountid=null, ").
                            append("cityid=null, ").
                            append("companyid=null, ").
                            append("countryid=null, ").
                            append("freetextid=null, ").
                            append("languageid=null, ").
                            append("lastmoduser=null, ").
                            append("recorduser=null, ").
                            append("salutationid=null, ").
                            append("titleid=null, ").
                            append("imageid=null").
                            append(" WHERE addressid=").append(token).append(";");
                } else {
                    sql.append(DELETE).
                            append(IDENTIFIER_QUOTE_STRING).
                            append(table.getName()).
                            append(IDENTIFIER_QUOTE_STRING).
                            append(WHERE);
                }

                deletedTables.add(table.getName());
            }
        }

        sql.append(DELETE).append(SpecialTables.Company.getTableName()).append(WHERE);
        sql.append(DELETE).append(SpecialTables.Address.getTableName()).
                append(" WHERE addressid=").append(token).append(";");
        return sql.toString();
    }

    private String buildUpdateSQL(List<String> references,
                                  String tableName,
                                  DatabaseMetaData dbmdt,
                                  final String IDENTIFIER_QUOTE_STRING) {
        final String UPDATE = "UPDATE ";
        final String SET = " SET ";
        final String WHERE = " WHERE companyid=" + token + ";";
        StringBuilder sql = new StringBuilder();
        List<String> fkColumns = getFKColumns(tableName, dbmdt);

        Set<String> updatedTablesSet = new HashSet<String>();

        for (String referencedTableName : references) {
            if (deletedTables.contains(referencedTableName) || updatedTablesSet.contains(referencedTableName)) {
                continue;
            }

            Set<String> updatedColumnsSet = new HashSet<String>();

            for (String fkColumn : fkColumns) {
                if (!"companyid".equals(fkColumn) && !updatedColumnsSet.contains(fkColumn)) {
                    if (isRegistredFK(fkColumn, referencedTableName, dbmdt) &&
                            settingUpAsNull(fkColumn, referencedTableName, dbmdt)) {
                        sql.append(UPDATE).
                                append(IDENTIFIER_QUOTE_STRING).
                                append(referencedTableName).
                                append(IDENTIFIER_QUOTE_STRING).
                                append(SET).
                                append(fkColumn).
                                append("=null").append(WHERE);

                        updatedColumnsSet.add(fkColumn);
                    }
                }
            }

            updatedTablesSet.add(referencedTableName);
        }
        return sql.toString();
    }

}
